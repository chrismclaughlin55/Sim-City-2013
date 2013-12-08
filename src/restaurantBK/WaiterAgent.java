package restaurantBK;

import agent.Agent;
import restaurantBK.CustomerAgent.AgentEvent;
import restaurantBK.gui.WaiterGui;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter {
	//static final int NTABLES = 3;//a global for the number of tables.
	static final int WIDTH = 50;
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private class myCustomer{
		Customer c;
		CustomerState cs;
		String selection;
		int tableNumber;
		double check;
		int waitPos;
	}
	private int restY = 40;
	private int restX = 30;;
	private Cashier cashier;
	private Host host;
	private Cook cook;
	Timer timer = new Timer();
	public enum CustomerState {waiting,seated,readyToOrder,waitingToOrder,reorder,ordered,waitingForFood,orderIsReady,foodOnItsWay,eating,waitingForCheck,checkOrdered,checkOnItsWay,checkGiven,leaving,gone};
	public List<myCustomer> customers	= Collections.synchronizedList(new ArrayList<myCustomer>());
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public enum WorkingState {wantsBreak,waitingForResponse,allowedBreak,breaking,goingBackToWork,working }
	WorkingState ws;
	private Menu m;
	private String name;
	private Semaphore atDestination = new Semaphore(0,true);
	//private Semaphore goingToTakeOrder = new Semaphore(0,true);
	//private Semaphore tellCook = new Semaphore(0,true);
	//private Semaphore getOrder = new Semaphore(0,true);
	//private Semaphore 
	public WaiterGui waiterGui = null;
	public WaiterAgent(String name) {
		super();
		ws = WorkingState.working;
		this.name = name;
		m = new Menu();
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#wantsBreak()
	 */
	@Override
	public boolean wantsBreak() {
		if(ws==WorkingState.working) {
			return false;
		}
		return true;
	}
	public int getRestX() {
		return restX;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#waitingForResponse()
	 */
	@Override
	public boolean waitingForResponse() {
		if(ws==WorkingState.wantsBreak||ws==WorkingState.waitingForResponse||ws==WorkingState.allowedBreak) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getCustomers()
	 */
	@Override
	public List getCustomers() {
		return customers;
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCook(restaurant.CookAgent)
	 */
	@Override
	public void setCook(Cook cook) {// RUN THIS IN THE GUI WHEN YOU CREATE A WAITER AND COOK AGENT
		this.cook=cook;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setHost(restaurant.HostAgent)
	 */
	@Override
	public void setHost(Host host) {
		this.host = host;
	}
	public void setRestX(int x) {
		restX=x*restX;
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier cash) {
		this.cashier = cash;
	}
	// Messages
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgGoBackToWork()
	 */
	@Override
	public void msgGoBackToWork() {
		this.ws=WorkingState.goingBackToWork;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgChangeWantsBreak()
	 */
	@Override
	public void msgChangeWantsBreak() {
		this.ws=WorkingState.wantsBreak;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgGoOnBreak()
	 */
	@Override
	public void msgGoOnBreak() {
		this.ws=WorkingState.allowedBreak;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgCantBreak()
	 */
	@Override
	public void msgCantBreak() {
		this.ws = WorkingState.working; //setting to working, sets checkbox to enabled
		print("No break? I'll ask again later");
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgReadyToOrder(Customer cust) {
		for(myCustomer c : customers) {
			if(c.c==cust) {
				//print("found you");
				c.cs = CustomerState.readyToOrder;
				stateChanged();
			}
		}
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSitAtTable(restaurant.CustomerAgent, int)
	 */
	@Override
	public void msgSitAtTable(Customer cust, int tn, int pos){//, int x, int y) {
		myCustomer c = new myCustomer();
		c.c= cust;
		c.waitPos=pos;
		c.tableNumber=tn;
		c.cs=CustomerState.waiting;
		customers.add(c);
		stateChanged();
		
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyChoice(restaurant.interfaces.Customer, java.lang.String)
	 */
	@Override
	public void msgHereIsMyChoice(Customer c, String choice){
		//TIME TO MESSAGE THE COOK BY CREATING AN ORDER!
		print("Lovely selection.");
		//CHANGE TO CUSTOMER STATE ORDERED
		for(myCustomer cust : customers) {
			if(cust.c==c) {
				cust.selection=choice;
				cust.cs=CustomerState.ordered;
				stateChanged();
				//BAD, NO ACTION IN MSG placeOrder(cust.tableNumber,choice);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderIsReady(int, java.lang.String)
	 */
	@Override
	public void msgOrderIsReady(int tn, String name){
		print("Going to get order for table number " + tn);
		synchronized(customers) {
		for(myCustomer cust : customers) {
			if(cust.selection==name) {
				cust.cs=CustomerState.orderIsReady;
				stateChanged();
			}
		}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtDestination()
	 */
	@Override
	public void msgAtDestination() {//from animation
		atDestination.release();// = true;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOutOfOrder(java.lang.String, int)
	 */
	@Override
	public void msgOutOfOrder(String order, int tn) {
		for(myCustomer cust : customers)
		{
			if(cust.tableNumber==tn) {
				cust.cs=CustomerState.reorder;
				stateChanged();
			}
			
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDoneEatingAndWantCheck(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgDoneEatingAndWantCheck(Customer c)
	{
		print("checking for check");
		for(myCustomer cust : customers)
		{
			if(cust.c==c)
			{
				//print("Thanks for eating with us.");
				cust.cs=CustomerState.waitingForCheck;
				print("Getting your check customer at table "+cust.tableNumber);
				stateChanged();
			}
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgCheckMade(double, int)
	 */
	@Override
	public void msgCheckMade(double cost, int table) {
		for(myCustomer c: customers) {
			if(c.tableNumber==table) {
				c.check=cost;
				c.cs=CustomerState.checkOnItsWay;
				stateChanged();
			}
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeavingEarly(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgLeavingEarly(Customer c) {
		for(myCustomer cust: customers) {
			if(cust.c==c) {
				print("Leaving early? Alright then...");
				cust.cs=CustomerState.leaving;
				stateChanged();
			}
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeavingTableToPay(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgLeavingTableToPay(Customer c) {
		synchronized(customers) {
		for(myCustomer cust : customers)
		{
			if(cust.c==c)
			{
				//print("Thanks for eating with us. Go to the cashier.");
				cust.cs=CustomerState.leaving;
				print("Hey host, table " +cust.tableNumber+" is now clear.");
				stateChanged();
			}
			break;
		}
		}
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(ws==WorkingState.working) {
			waiterGui.setBreakEnabledUnselect();
		}
		if(ws==WorkingState.wantsBreak) {
			print("Can I take a break?");
			TellHost();
		}
		if(ws==WorkingState.goingBackToWork) {
			GoBackToWork();
		}
		if(customers.size()==0 &&ws==WorkingState.allowedBreak) {
			GetOutOfHere();
		}
		if(ws==WorkingState.breaking) {
			setBreakEnabledSelected();
		}
		synchronized(customers) {
			for(myCustomer c : customers) {
				if(c.cs==CustomerState.waiting) {
					seatCustomer(c,c.tableNumber);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		/*catch(ConcurrentModificationException e) {
			return false;
		}*/
		synchronized(customers) {
			for(myCustomer c: customers){
				if(c.cs==CustomerState.readyToOrder) {
					goToCustomerAndTakeOrder(c);
					c.cs=CustomerState.waitingToOrder;
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs == CustomerState.reorder) {
					goToCustomerAndRetakeOrder(c);
					c.cs=CustomerState.waitingToOrder;
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs==CustomerState.ordered) {
					//TELL THE COOK WHAT THE UCSTOMER WNATS
					placeOrder(c,c.tableNumber,c.selection);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs==CustomerState.orderIsReady) {
					PickUpOrder(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs==CustomerState.waitingForCheck) {
					GetCheck(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs==CustomerState.checkOnItsWay) {
					DeliverCheck(c);
					return true;
				}
			}
		}
		synchronized(customers) {
			for(myCustomer c: customers) {
				if(c.cs == CustomerState.leaving)
				{
					FreeTable(c,c.tableNumber);
					return true;
					//FREE UP THE TABLE
				} 
			}
		}
		if(customers.isEmpty()) {
			GoRest();
		}
		//IF ORDER IS READY
		//GO TO COOK AND GIVE THE CUSTOMER THEIR ORDER
		
		//
		//for()
		return false;
		//we have tried all our rules and found
		//nothing to  do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void GoRest() {
		waiterGui.DoGoRest(restX, restY);
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void TellHost() {
		ws=WorkingState.waitingForResponse;
		host.msgBreakPlease(this);
	}
	private void setBreakEnabledSelected() {
		waiterGui.setBreakEnabledSelect();
	}
	private void setBreakEnabledUnselected() {
		waiterGui.setBreakEnabledUnselect();
	}
	
	private void GetOutOfHere() {
		ws=WorkingState.breaking;
		print("Going on break");
		waiterGui.DoGoRest(restX,restY);
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*timer.schedule(new TimerTask() {
			public void run() {
				
				GoBackToWork();
			}
		},
		10000);*/
	}
	private void GoBackToWork() {
		ws=WorkingState.working;
		print("Going back to work");
		host.msgComingBackFromBreak(this);
	}
	private void GetCheck(myCustomer c) {
		//GO TO CASHIER ANIMATION
		waiterGui.DoGoToCashier();
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Hey cashier, my customer from table " + c.tableNumber+ " wants his check");
		cashier.msgMakeCheck(c.c,this,m.get(c.selection),c.tableNumber);
		c.cs=CustomerState.checkOrdered;
	}
	private void DeliverCheck(myCustomer c) {
		//GO TO CUSTOMER
		waiterGui.DoBringToTable(c.c, c.tableNumber);
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.c.msgHereIsTheCheck(c.check);
		c.cs=CustomerState.checkGiven;
		print("Here's you check, thanks for eating with us");
		//c.c.msgHereIsTheCheck();
	}
	private void goToCustomerAndTakeOrder(myCustomer c) {
		
		//MAKE ANOTHER GUI METHOD TO GO BACK TO CUSTOMER'S COORDINATES
		//PLUS A SEMAPHORE, THEN MESSAGE IT
		DoGetOrder(c,c.tableNumber);
		//once he gets there
	}
	private void DoGetOrder(myCustomer c, int tn) {
		
		
		//MAKE A STUPID TEXT BOX WITH c.selection on it
		waiterGui.DoBringToTable(c.c,tn); //this method to have him go
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TAKE A SEMAPHORE HERE SO THAT WAITER AGENT SLEEPS UNTIL HE GETS TO THE TABLE
		//then when finished, print out
		print("yo, what would you like?");
		c.c.msgWhatWouldYouLike();
		
		//ACTUALLY MAKE A TEXT BOX FOR THIS IN THE GUI
		//DoPrintWhatYouWant
	}
	private void goToCustomerAndRetakeOrder(myCustomer c) {
		waiterGui.DoBringToTable(c.c, c.tableNumber);
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Sorry, we're out of the " +c.selection);
		c.c.msgOrderSomethingElse();
	}
	private void placeOrder(myCustomer c, int tn, String choice) {
		//DO ANIMATION, GO TO COOK'S COORDINATES
		//SEMAPHORE TO MAKE WAITER GO TO SLEEP UNTIL IT GETS TO COOK
		print("HEY COOK, THIS GUY AT TABLE "+tn +" WANTS A "+ choice);
		c.cs=CustomerState.waitingForFood;
		waiterGui.DoGoToCook();
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgHereIsAnOrder(this, choice,tn);
		
	}
	private void PickUpOrder(myCustomer c) {
		
		waiterGui.DoGoToCook();
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgPickedUpOrder(c.tableNumber);
		waiterGui.flipOrder(c.selection);
		//CREATE STUPID TEXTBOX WITH C.SELECTION ON IT
		waiterGui.DoBringToTable(c.c,c.tableNumber);//CHANGE THIS METHOD TO C.C AND C.TABLENUBMER ONLY
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.cs=CustomerState.foodOnItsWay;
		waiterGui.flipOrder(c.selection);
		print("Here's your food");
		c.c.msgHereIsYourFood();
	}
	private void GoGetCustomer(int pos) {
		waiterGui.DoLeaveCustomer(pos);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		host.gotCustomer();
	}
	private void seatCustomer(myCustomer customer, int tn) {
		GoGetCustomer(customer.waitPos);
		print("Follow me to your table");
		customer.c.msgFollowMeToTable(this, tn, new Menu());
		DoSeatCustomer(customer.c,tn); //ACQUIRE SEMAPHORE, UNTIL AT DEST
		customer.cs=CustomerState.seated;
		//waiterGui.DoLeaveCustomer();
	}

	/*private void goOffscreen() {
		waiterGui.DoLeaveCustomer();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	private void FreeTable(myCustomer c, int tn) {
		//print("what");
					
		c.cs=CustomerState.gone;
		host.msgTableIsFree(tn);
		customers.remove(c);
		//if(name=="onbreak"&&customers.size()==0) {
		//tryonbreak=true;
		//host.msgBreakPlease(this);

	}
	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, int tn) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		//atDestination.release();
		//print("Semaphore released");
		print("Seating " + customer + " at " + tn);
		//ACQUIRE THE SEMAPHORE HERE..., release when 
		waiterGui.DoBringToTable(customer,tn); //change to c.c and tablenumber
		try {
			//System.out.println("Semaphore acquired");
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//utilities

	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}

	
}

