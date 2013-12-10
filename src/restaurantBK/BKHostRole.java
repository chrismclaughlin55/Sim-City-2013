package restaurantBK;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantBK.gui.RestaurantPanel;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;
import trace.AlertLog;
import trace.AlertTag;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class BKHostRole extends Role implements Host {
	static final int NTABLES = 3;//a global for the number of tables.
	static final int WIDTH = 50;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	RestaurantPanel rest;
	public List<Customer> waitingCustomers	= Collections.synchronizedList(new ArrayList<Customer>());
	//public Collection<Table> tables;
	private int lobbycusts=0;
	public List<Table> tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
	private int count;
	public enum WaiterState {working, cantBreak, goingOnBreak, onBreak};
	private int breakers = 0;
	private opentables ot;
	public enum opentables {open, waitingToOpen, full};
	private class myWaiter {
		WaiterState ws;
		Waiter w;
	}
	
	public List<myWaiter> myWaiters = Collections.synchronizedList(new ArrayList<myWaiter>());
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	//private Semaphore atTable = new Semaphore(0,true);

	//public HostGui hostGui = null;
	public BKHostRole(PersonAgent person, String name, RestaurantPanel rest) {
		super(person);
		this.rest = rest;
		this.ot=opentables.open;
		this.count = 0;
		this.name = name;
		// make some tables
		
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
			tables.get(ix-1).setUnoccupied();
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getMaitreDName()
	 */
	@Override
	public String getMaitreDName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getWaitingCustomers()
	 */
	@Override
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#getWaiters()
	 */
	@Override
	public List getWaiters() {
		return myWaiters;
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#getTables()
	 */
	@Override
	public Collection getTables() {
		return tables;
	}
	
	
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Host#msgIWantFood(restaurant.CustomerAgent)
	 */
	@Override
	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		lobbycusts++;
		//System.out.println(lobbycusts);
		if(ot==opentables.full) {
			ot=opentables.waitingToOpen;
		}
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Host#msgBreakPlease(restaurant.interfaces.Waiter)
	 */
	@Override
	public void msgBreakPlease(Waiter w) {
		if(myWaiters.size()-breakers>1) {
			for(myWaiter wait : myWaiters) {
				if(wait.w ==w) {
					wait.ws=WaiterState.goingOnBreak;
					breakers++;
				}
			}
		}
		else {
			for(myWaiter wait: myWaiters) {
				if(wait.w==w)
				{
					print("Can't let you go on break");
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_HOST, this.getName(), "Can't let you go on break");
					wait.ws=WaiterState.cantBreak;	
				}
			}
		}
		stateChanged();
	}
	
	public void gotCustomer() {
		lobbycusts--;
	}
	/* (non-Javadoc)
	 * @see restaurant.Host#msgComingBackFromBreak(restaurant.interfaces.Waiter)
	 */
	@Override
	public void msgComingBackFromBreak(Waiter w) {
		synchronized(myWaiters) {
			for(myWaiter wait: myWaiters) {
				if(wait.w==w) {
					print("Have a nice break?");
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_HOST, this.getName(), "Have a nice break?");
					wait.ws=WaiterState.working;
					breakers--;
					stateChanged();
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Host#msgTableIsFree(int)
	 */
	@Override
	public void msgTableIsFree(int table)
	{
		synchronized(tables) {
			for(Table t: tables) {
				if(t.tableNumber==table) {
					t.setUnoccupied();
				}
			}
		}
		if(ot==opentables.waitingToOpen) {
			ot=opentables.open;
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Host#msgNoTablesImLeaving(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgNoTablesImLeaving(Customer c) {
		print("Sorry, come back another time!");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_HOST, this.getName(), "Sorry, come back another time!");
		waitingCustomers.remove(c);
	}
/*	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
*/
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(!waitingCustomers.isEmpty()) {
			waitingCustomers.get(waitingCustomers.size()-1).msgWaitHerePlease(lobbycusts);
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if(myWaiters.size()!=0) {
						int wait = count % myWaiters.size();
						int check=wait;
						while(myWaiters.get(wait).ws!=WaiterState.working) { //THINK OF A BETTER WAY OF SPLITTING UP THE WORK
							wait++;
							wait=wait%myWaiters.size();
							if(check==wait)
							{
								//print("WE HAVE NO WORKING WAITERS...");
								break;
							}
						}
						//WE TAKE A WAITER OFF BREAK
						//ADD A CHECK SO YOU DON'T INFINITE LOOP
						table.setOccupant(waitingCustomers.get(0));
						myWaiters.get(wait).w.msgSitAtTable(waitingCustomers.get(0),table.tableNumber,lobbycusts);
						waitingCustomers.remove(0);
						count++;
						return true;
					}
					//ALSO NEED TO FIND AN AVAILABLE WAITER
					//seatCustomer(waitingCustomers.get(0), table);//the action ADD w WAITER parameter
					//return true to the abstract agent to reinvoke the scheduler.
				}
			}
			if(myWaiters.size()!=0&&waitingCustomers.size()!=0&&ot!=opentables.waitingToOpen) {
				ot=opentables.full;
			}
		}
		if(ot==opentables.full) {
			print("Sorry, you're going to have to wait");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_HOST, this.getName(), "Sorry, you're going to have to wait");
			ot=opentables.waitingToOpen;
			waitingCustomers.get(waitingCustomers.size()-1).msgWouldYouLikeToWait(waitingCustomers.size());
			
		}
		synchronized(myWaiters) {
			for(myWaiter w: myWaiters) {
				if(w.ws == WaiterState.goingOnBreak) {
					w.ws=WaiterState.onBreak;
					w.w.msgGoOnBreak();
					return true;
				}
			}
		}
		synchronized(myWaiters) {
			for(myWaiter w: myWaiters) {
				if(w.ws==WaiterState.cantBreak) {
					w.ws=WaiterState.working;
					w.w.msgCantBreak();
				}
			}
		}
		if(!waitingCustomers.isEmpty()) {
			return true;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	/* (non-Javadoc)
	 * @see restaurant.Host#addWaiter(restaurant.interfaces.Waiter)
	 */
	@Override
	public void addWaiter(Waiter waiter)
	{
		myWaiter x = new myWaiter();
		x.ws=WaiterState.working;
		x.w=waiter;
		myWaiters.add(x);
		stateChanged();
	}
	/*private void seatCustomer(CustomerAgent customer, Table table) {
		//FIND A WAITER WHO IS AVAILABLE AND GET HIM TO SEAT THIS CUSTOMER
		//waiter.msgSitAtTable(customer, table.tableNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//w.msgSitAtTable(customer,table.tablenumber);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		//hostGui.DoLeaveCustomer();
	}
*/
	// The animation DoXYZ() routines
	
	
	//utilities


	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			
			this.tableNumber = tableNumber;
		}
		
	/*	int getX()
		{
			if(this.tableNumber==1)
			{
				return 200;
			}
			if(this.tableNumber==2)
			{
				return 325;
			}
			if(this.tableNumber==3)
			{
				return 450;
			}
			return 0;
		}
		int getY()
		{
			
			return 250;
		}
		*/
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


}

