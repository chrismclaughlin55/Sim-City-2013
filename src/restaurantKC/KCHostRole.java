package restaurantKC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import restaurantKC.gui.HostGui;
import restaurantKC.gui.RestaurantPanel;
import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Host;
import restaurantKC.interfaces.Waiter;
import city.PersonAgent;
import city.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class KCHostRole extends Role implements Host {
	static final int NTABLES = 3;//a global for the number of tables.


	public List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	private List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;

	private RestaurantPanel restPanel;


	private String name;
	private boolean alreadySeated = false;

	public HostGui hostGui = null;

	private Semaphore seatCustomer = new Semaphore(0, true);

	private class MyWaiter {
		public MyWaiter(Waiter w, int nTables) {
			waiter = w;
			numTables = nTables; 
			onBreak = false;
		}
		Waiter waiter;
		int numTables;
		boolean onBreak;
		public boolean breakApproved = false;
		public boolean breakDenied = false;

	}


	public KCHostRole(PersonAgent p, RestaurantPanel restPanel) {
		super(p);
		this.name = p.getName();
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		this.restPanel = restPanel;
	}

	public String getName() {
		return name;
	}

	public List<Customer> getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}

	// Messages

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgWaiterReporting(Waiter w) {
		waiters.add((new MyWaiter(w, 0)));
		stateChanged();
	}
	public void msgTableIsFree(int table) {
		print ("Received msgTableIsFree");
		for (Table tbl : tables) {
			if (tbl.tableNumber == table) {
				tbl.setUnoccupied();
				stateChanged();
			}
		}
	}

	public void msgIWantABreak(Waiter w)
	{
		if (waiters.size() > 1) {
			for (MyWaiter mw : waiters) {
				if (mw.waiter.equals(w)){
					//mw.onBreak = true;
					//mw.waiter.msgBreakApproved();
					mw.breakApproved  = true;
					stateChanged();
					break;
				}
			}
		}
		else {
			for (MyWaiter mw : waiters) {
				if (mw.waiter.equals(w)){
					mw.breakDenied = true;
				}
			}
		}
	}

	public void msgImOffBreak(Waiter w) {
		for (MyWaiter mw : waiters) {
			if (mw.waiter.equals(w)){
				mw.breakApproved = false;
				mw.breakDenied = false;
				mw.onBreak = false;
			}
		}

	}

	public void msgLeavingNow(Waiter waiter) {
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if(w.waiter == waiter) {
					waiters.remove(w);
					return;
				}
			}
		}
	}

	// removes customer when customer chooses to leave early
	public void msgLeaving(Customer c) {
		if (!alreadySeated) {
			waitingCustomers.remove(c);
			stateChanged();
		}
	}

	//message from Gui once customer has been seated
	public void msgCustomerSeated() {
		seatCustomer.release();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		alreadySeated = false;

		if(person.cityData.hour >= restPanel.CLOSINGTIME && restPanel.isOpen()) {
			restPanel.setOpen(false);
			return true;
		}
		if(person.cityData.hour >= restPanel.CLOSINGTIME && !restPanel.isOpen() 
				&& restPanel.justHost()) {
			LeaveRestaurant();
			return true;
		}

		synchronized(waitingCustomers) {
			synchronized(waiters) {
				if (!waiters.isEmpty())
				{
					for (MyWaiter m : waiters) {
						if (!m.onBreak) {
							if (m.breakApproved) {
								m.waiter.msgBreakApproved();
								m.onBreak = true;
							}
							else if (m.breakDenied) {
								m.waiter.msgBreakDenied();
							}
						}
					}
					for (Table table : tables) {
						if (!table.isOccupied()) {
							if (waitingCustomers.size() > 0) {
								synchronized(waitingCustomers){
									int i = 0;
									for (MyWaiter m : waiters) {
										if (m.onBreak)
											i++;
									}
									int minTables = waiters.get(i).numTables;
									int WaiterWithMinTables = i;
									int j = 0;
									synchronized(waiters){
										for (MyWaiter mw : waiters) {
											if (!mw.onBreak)
											{
												if (mw.numTables < minTables) {
													minTables = mw.numTables;
													WaiterWithMinTables = j;
												}
											}
											j++;
										}
										waiters.get(WaiterWithMinTables).numTables++;
									}
									if (waitingCustomers.size() > 0) {
										alreadySeated = true;
										if (waitingCustomers.contains(waitingCustomers.get(0))) {
											tellWaiterToSeatCustomer(waitingCustomers.get(0), table, waiters.get(WaiterWithMinTables).waiter);
										}
										try {
											seatCustomer.acquire();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	// Actions

	private void tellWaiterToSeatCustomer(Customer customer, Table table, Waiter waiter) {
		waiter.msgSitAtTable(customer, table.tableNumber);
		customer.setWaiter(waiter);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}

	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	private void LeaveRestaurant() {
		person.hungerLevel = 0;
		hostGui.DoLeaveRestaurant();
		restPanel.hostLeaving();
		person.exitBuilding();
		person.msgFull();
		person.msgDoneWithJob();
		doneWithRole();
	}





}

