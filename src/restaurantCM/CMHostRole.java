package restaurantCM;

import agent.Agent;
import restaurantCM.gui.CMHostGui;
import restaurantCM.gui.CMRestaurantBuilding;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.*;
import city.PersonAgent.BigState;
/** 
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CMHostRole extends Role  {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	int waiterPointer = 0;
	public List<CMCustomerRole> waitingCustomers	= new ArrayList<CMCustomerRole>();
	public Collection<Table> tables;
	public List<myWait> Waiters = new ArrayList<myWait>();
	enum waiterState { noCusts,someCusts, tablesFull, onBreak, noBreak, wantsBreak, leave};

	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atLobby = new Semaphore(0,true);
	public CMHostGui hostGui;
	public CMCookRole cook;
	public CMCashierRole cashier;
	public CMRestaurantBuilding building;
	public CMHostRole(PersonAgent person, CMRestaurantBuilding b) {
		super(person);
		print("host created");
		this.building = b;
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	private class myWait{
		Vector <Table> tables= new Vector<Table>();
		waiterState state = waiterState.noCusts;
		CMWaiterRole w;
		myWait(CMWaiterRole w){
			this.w = w;
		}
	}
	public void addWaiter(CMWaiterRole w){
		Waiters.add(new myWait(w));
		System.out.println("added a new waiter named "+ w.getName());
	}
	// Messages

	public void msgIWantFood(CMCustomerRole cust) {
		if(waitingCustomers.size()>2){
		waitingCustomers.add(cust);
		print("waiting customer "+ cust.getCustomerName());
		}
		else
			cust.msgLeave();
		stateChanged();
	}

	public void msgLeavingTable(CMCustomerRole cust) {
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}

	public void msgAtTable() {//from animation
		atTable.release();// = true;
		stateChanged();
	}//CHANGED added a msg called by the GUI which releases atLobby permit once host is able to seat more customers
	public void msgImOnBreak(CMWaiterRole W){
		boolean onlyWaiter = true;
		print(W.getName()+" wants to go on break");
		myWait myWait = findWaiter(W);
		myWait.state = waiterState.wantsBreak;
		for(myWait w : Waiters){
			if(w.state != waiterState.onBreak){
				onlyWaiter = false;
			}
		}
		if(onlyWaiter){
			myWait.state = waiterState.noBreak;
		}
		stateChanged();
	}
	public void msgImOffBreak(CMWaiterRole W){
		myWait myWait = findWaiter(W);
		myWait.state = waiterState.noCusts;
		print(myWait.w.getName()+" is back from break");
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					seatCustomer(waitingCustomers.get(0), table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		for (myWait w : Waiters){
			if(w.state == waiterState.noBreak){
				w.w.msgNoBreak();
				return true;
			}
		}

		for (myWait w : Waiters){
			if(w.state == waiterState.wantsBreak){
				w.w.msgGoOnBreak();
				return true;
			}


		}
		if(person.cityData.hour > CMRestaurantBuilding.CLOSINGTIME){
			for( myWait w: Waiters){
				boolean leave = true;
				for(myCust c: w.w.Customers){
					if(c.state!= myCust.AgentState.left )
						leave = false;
					break;
				}
				if(leave){
					w.w.msgLeave();
					w.state = waiterState.leave;
				}
			}
			boolean leave = true;
			for( myWait w: Waiters){
				if(w.state != waiterState.leave){
					leave = false;
					break;
				}
			}
			
			if(leave){
				if(cook != null){  cook.msgLeave(); cook = null;}
				if(cashier != null){ cashier.msgLeave(); cashier = null;}
			}
			if(cashier == null && cook == null && leave)
				leave();
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void seatCustomer(CMCustomerRole customer, Table table) {
		boolean satCust = false;
		while(!satCust){
			myWait W = Waiters.get(waiterPointer%Waiters.size());
			if(W.state != waiterState.onBreak){
				W.w.msgSitAtTable(customer, table.tableNumber, this);
				satCust = true;
			}
			waiterPointer++;
		}
		//customer.msgSitAtTable(table.tableNumber);
		//DoSeatCustomer(customer, table);
		//	try {//atTable.release();
		//System.out.println("made it here: Before the atTable.acquire");
		//atTable.acquire();
		//System.out.println("made it here: atTable.acquire");
		//	} //catch (InterruptedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		//}
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
	}


	//utilities
	private void leave() {
		person.bigState = BigState.goHome;
		hostGui.setPresent(false);
		print("leave now");
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();	
		person.setHunger(0);
		building.setClosed(person);
		building.restGui.animationPanel.removeGui(hostGui);
	}
	
	private myWait findWaiter(CMWaiterRole W){
		for(myWait w: Waiters){
			if(w.w.equals(W))
				return w;
		}
		return null;
	}
	public void setGui(CMHostGui gui) {
		hostGui = gui;
	}

	public CMHostGui getGui() {
		return hostGui;
	}

	private class Table {
		CMCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CMCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CMCustomerRole getOccupant() {
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

