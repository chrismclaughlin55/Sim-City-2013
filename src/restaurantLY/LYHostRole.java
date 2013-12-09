package restaurantLY;

import agent.Agent;
import restaurantLY.gui.RestaurantPanel;
import restaurantLY.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;

//no gui animation for host

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class LYHostRole extends Role implements Host {
	
	// ******** Lab 3 Part 2 ********
	static int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<myCustomer>());
	private List<myWaiter> myWaiters = Collections.synchronizedList(new ArrayList<myWaiter>());
	private int waiterNumber = 0;
	public Table tables[];
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private String name;
	
	private Semaphore atCust = new Semaphore(0, true);
    
    RestaurantPanel restPanel;
    private List<Cook> cooks = new ArrayList<Cook>();
    private List<Waiter> waiters = new ArrayList<Waiter>();
	
	public LYHostRole(PersonAgent person, RestaurantPanel rp) {
		super(person);
		restPanel = rp;
		this.name = super.getName();
		// make some tables
		tables = new Table[NTABLES];
		for(int i = 0; i < NTABLES; i++) {
			tables[i] = new Table(i);
		}
	}
	
	public String getMaitreDName() {
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	
	public List getWaiters() {
		return myWaiters;
	}
	
	public Table[] getTables() {
		return tables;
	}
	
	public void setWaiter(Waiter waiter) {
		this.myWaiters.add(new myWaiter(waiter));
		stateChanged();
	}

	// Messages

	public void msgIWantToEat(Customer cust) {
		waitingCustomers.add(new myCustomer(cust, customerState.gotHungry));
		stateChanged();
	}

	public void msgTableIsFree(int tableNumber) {
		print("Customer leaving table " + (tableNumber+1));
		tables[tableNumber].isOccupied = false;
		stateChanged();
	}

	public void msgAskForBreak(Waiter waiter) {
		print(waiter.getName() + " asking for break");
		synchronized (myWaiters) {
			for (myWaiter mw : myWaiters) {
				if (mw.waiter.equals(waiter)) {
					mw.waiting = true;
				}
			}
		}
		stateChanged();
	}
	
	public void msgBackToWork(Waiter waiter) {
		//print(waiter.getName() + " resuming work");
		synchronized (myWaiters) {
			for (myWaiter mw : myWaiters) {
				if (mw.waiter.equals(waiter)) {
					mw.waiting = false;
				}
				mw.working = true;
			}
		}
		stateChanged();
	}
	
	public void msgCustomerWaiting(Customer customer) {
		print(customer.getName() + " waiting");
		myCustomer temp = null;
		synchronized (waitingCustomers) {
			for (myCustomer mc : waitingCustomers) {
				if (mc.customer.equals(customer)) {
					temp = mc;
				}
			}
		}
		if (temp != null) {
			temp.state = customerState.waiting;
		}
		stateChanged();
	}
	
	public void msgCustomerLeaving(Customer customer) {
		print(customer.getName() + " leaving");
		stateChanged();
	}
	
	public void msgAtCustomer() {
		atCust.release();
		stateChanged();
	}
	
	public void msgLeavingNow(Waiter waiter)
	{
		synchronized(myWaiters)
		{
			for(myWaiter w : myWaiters)
			{
				if(w.waiter == waiter)
				{
					myWaiters.remove(w);
					return;
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {
		if(person.cityData.hour >= restPanel.CLOSINGTIME && restPanel.isOpen())
		{
			restPanel.setOpen(false);
			return true;
		}
		if(person.cityData.hour >= restPanel.CLOSINGTIME && !restPanel.isOpen() 
				&& restPanel.justHost())
		{
			LeaveRestaurant();
			return true;
		}
		/* Think of this next rule as:
        	Does there exist a table and customer,
        	so that table is unoccupied and customer is waiting.
        	If so seat him at the table.
		*/
		myWaiter temp = null;
		synchronized (myWaiters) {
			temp = null;
			for (myWaiter mw : myWaiters) {
				if (mw.waiting) {
					temp = mw;
					break;
				}
			}
		}
		if (temp != null) {
			considerBreak(temp);
			return true;
		}

		boolean full = true;
		boolean onBreak = true;
		synchronized (myWaiters) {
			for (myWaiter mw : myWaiters) {
				if (mw.working)
					onBreak = false;
			}
		}
		for (int i = 0; i < NTABLES; i++) {
			if (!tables[i].isOccupied) {
				full = false;
				break;
			}
		}
		
		myCustomer t = null;
		if (full || onBreak) {	
			synchronized (waitingCustomers) {
				t = null;
				for (myCustomer c : waitingCustomers) {
					if (c.state == customerState.gotHungry) {
						t = c;
						break;
					}
				}
			}
			if (t != null) {
				tellCustomerRestaurantIsFull(t);
				return true;
			}
		}

		synchronized (waitingCustomers) {
			t = null;
			for (myCustomer c : waitingCustomers) {
				if (c.state == customerState.gotHungry && waitingCustomers.indexOf(c) >= NTABLES) {
					t = c;
					break;
				}
			}
		}
		if (t != null) {
			tellCustomerRestaurantIsFull(t);
			return true;
		}
		
		synchronized (myWaiters) {
			if(!waitingCustomers.isEmpty() && !myWaiters.isEmpty() && !onBreak){
				while(!myWaiters.get(waiterNumber).working){
					waiterNumber = (waiterNumber+1)%myWaiters.size();
				}
			
				for(int i = 0; i < NTABLES; i++) {
					t = null;
					if(!tables[i].isOccupied) {
						synchronized (waitingCustomers) {
							for (myCustomer c : waitingCustomers) {
								if (c.state != customerState.deciding) {
									t = c;
									break;
								}
							}
						}
						if (t != null) {
							callWaiterToSeatCustomer(myWaiters.get(waiterNumber), t, i, waitingCustomers.indexOf(t));
							return true;	
						}
					}
				}
			}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
    
    private void LeaveRestaurant() {
        restPanel.hostLeaving();
        person.msgDoneWithJob();
        person.exitBuilding();
        doneWithRole();
    }

	// Actions

	private void callWaiterToSeatCustomer(myWaiter waiter, myCustomer customer, int tableNumber, int custNumber) {
		print("Calling " + waiter.waiter + " to seat " + customer.customer + " at table " + (tableNumber+1));
		waiter.waiter.msgSitAtTable(customer.customer, tableNumber, custNumber);
		tables[tableNumber].isOccupied = true;
		try {
			atCust.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitingCustomers.remove(customer);
		waiterNumber = (waiterNumber+1)%myWaiters.size();
		stateChanged();
	}

	private void considerBreak(myWaiter waiter) {
		waiter.waiting = false;
		int workingWaiter = 0;
		synchronized (myWaiters) {
			for (myWaiter mw : myWaiters) {
				if (mw.working) {
					workingWaiter++;
				}
			}
		}
		if (workingWaiter > 1) {
			print(waiter.waiter.getName() + " on break");
			waiter.waiter.msgDecisionOnBreak(true);
			waiter.working = false;
		}
		else {
			print(waiter.waiter.getName() + " cannot go on break");
			waiter.waiter.msgDecisionOnBreak(false);
			waiter.working = true;
		}
		stateChanged();
	}

	private void tellCustomerRestaurantIsFull(myCustomer cust) {
		cust.customer.msgRestaurantIsFull();
		cust.state = customerState.deciding;
	}
    
    public void setCooks(List<Cook> cooks) {
        this.cooks = cooks;
    }
    
    public void setWaiters(List<Waiter> waiters) {
        this.waiters = waiters;
    }
	
	public void addWaiter(Waiter waiter)
	{
		myWaiters.add(new myWaiter(waiter));
		stateChanged();
	}
	
	public void removeWaiter(Waiter waiter)
	{
		synchronized(myWaiters)
		{
			for(myWaiter w : myWaiters)
			{
				if(w.waiter == waiter)
				{
					myWaiters.remove(w);
					return;
				}
			}
		}
	}
	
	//utilities
	
	private class Table {
		//Customer occupiedBy;
		int tableNumber;
		boolean isOccupied;
		
		Table(int tableNumber){
			//this.tableNumber = tableNumber;
			isOccupied = false;
		}
	}
	
	private class myCustomer {
		Customer customer;
		customerState state;
		
		myCustomer(Customer customer, customerState state) {
			this.customer = customer;
			this.state = state;
		}
	}
	public enum customerState {gotHungry, deciding, waiting};
	
	private class myWaiter {
		Waiter waiter;
		boolean working;
		boolean waiting;
		
		myWaiter(Waiter waiter) {
			this.waiter = waiter;
			working = true;
			waiting = false;
		}
	}
}
