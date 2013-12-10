package restaurantCM;

import agent.Agent;
import restaurantCM.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;

/** 
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostRole extends Role  {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	int waiterPointer = 0;
	public List<CustomerRole> waitingCustomers	= new ArrayList<CustomerRole>();
	public Collection<Table> tables;
	public List<myWait> Waiters = new ArrayList<myWait>();
	enum waiterState { noCusts,someCusts, tablesFull, onBreak, noBreak, wantsBreak};
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atLobby = new Semaphore(0,true);
	public HostGui hostGui = null;

	public HostRole(PersonAgent person) {
		super(person);
		print("host created");
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
		WaiterAgent w;
		myWait(WaiterAgent w){
			this.w = w;
		}
	}
	public void addWaiter(WaiterAgent w){
		Waiters.add(new myWait(w));
		System.out.println("added a new waiter named "+ w.getName());
	}
	// Messages

	public void msgIWantFood(CustomerRole cust) {
		waitingCustomers.add(cust);
		print("waiting customer "+ cust.getCustomerName());
		stateChanged();
	}

	public void msgLeavingTable(CustomerRole cust) {
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
	public void msgImOnBreak(WaiterAgent W){
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
	public void msgImOffBreak(WaiterAgent W){
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
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(CustomerRole customer, Table table) {
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
	private myWait findWaiter(WaiterAgent W){
		for(myWait w: Waiters){
			if(w.w.equals(W))
				return w;
		}
		return null;
	}
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
		CustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerRole getOccupant() {
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

