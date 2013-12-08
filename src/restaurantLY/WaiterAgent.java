package restaurantLY;

import agent.Agent;
import restaurantLY.CustomerAgent.AgentEvent;
import restaurantLY.gui.*;
import restaurantLY.interfaces.*;
import restaurantLY.test.mock.EventLog;
import restaurantLY.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.*;
import java.awt.Color;

/**
 * Restaurant Waiter Agent
 */

public class WaiterAgent extends Agent implements Waiter {
	private String name;
	private List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCook = new Semaphore(0, true);
	private Semaphore atDoor = new Semaphore(0, true);
	private Semaphore atCust = new Semaphore(0, true);
	private Semaphore meetCustomer = new Semaphore(0, true);
	
	private Host host;
	private Cook cook;
	private Cashier cashier;
	
	Timer timer = new Timer();
	
	public WaiterGui waiterGui = new WaiterGui(this);
	
	public enum AgentState {waitingForBreak, waitingForWork, onBreak, working};
	private AgentState state;
	
	private boolean onBreak = false;
	
	RestaurantGui gui;
	
	public EventLog log = new EventLog();
	
	public WaiterAgent(String name) {
		super();

		this.name = name;
	}
	
	public String getMaitreDName() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return "Waiter " + getName();
	}
	
	public List getCustomers() {
		return customers;
	}
	
	// Messages
	
	public void msgOnBreak() {
		onBreak = true;
		stateChanged();
	}
	
	public void msgSitAtTable(Customer customer, int tableNumber, int custNumber) {
		myCustomer mc = new myCustomer(customer, tableNumber, custNumber);
		mc.state = customerState.WaitingInRestaurant;
		customers.add(mc);
		stateChanged();
	}
	
	public void msgImReadyToOrder(Customer customer) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.state = customerState.ReadyToOrder;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgHereIsMyChoice(Customer customer, String choice) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.choice = choice;
					mc.state = customerState.Asked;
					meetCustomer.release();
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgOrderIsReady(int tableNumber) {
		synchronized (customers) {
			print("Order is ready");
			for (myCustomer mc : customers) {
				if (mc.tableNumber == tableNumber) {
					mc.state = customerState.GettingOrder;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgDoneEatingAndLeaving(Customer customer) {
		synchronized (customers) {
			for (myCustomer mc : customers){
				if (mc.customer.equals(customer)){
					mc.state = customerState.Done;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgLeaving(Customer customer) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.state = customerState.Leaving;
					stateChanged();
					return;
				}
			}
		}
		stateChanged();
	}
	
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgAtCook() {//from animation
		//print("msgAtCook() called");
		atCook.release();// = true;
		stateChanged();
	}
	
	public void msgAtDoor() {//from animation
		//print("msgAtDoor() called");
		atDoor.release();// = true;
		stateChanged();
	}
	
	public void msgAtCust() {//from animation
		//print("msgAtCust() called");
		atCust.release();// = true;
		stateChanged();
	}
	
	public void msgDecisionOnBreak(boolean isOnBreak) {
		onBreak = isOnBreak;
		gui.setstateCB(onBreak);
		if (isOnBreak) {
			print("Host allows me on break");
			state = AgentState.onBreak;
		}
		else {
			print("Host doesn't allow me on break");
			state = AgentState.working;
		}
		stateChanged();
	}
	
	public void msgCookRunOutOfFood(int tableNumber) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.tableNumber == tableNumber) {
					print("Running out of " + mc.customer.getName() + "'s order of " + mc.choice);
					mc.state = customerState.CookPendingReorder;
					cook.getGui().removeFood();
				}
			}
		}
		stateChanged();
	}
	
	public void msgAskForCheck(Customer customer, double check) {
		log.add(new LoggedEvent("Asking cashier for " + customer.getName() + "'s check of $" + check));
		print("Asking cashier for check");
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.check = check;
					stateChanged();
					return;
				}
			}
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if (state == AgentState.waitingForBreak) {
			print("Asking the host for break");
			host.msgAskForBreak(this);
		}
		if (state == AgentState.waitingForWork) {
			host.msgBackToWork(this);
		}
		
		if(!customers.isEmpty()){
			myCustomer temp = null;
			try {
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.WaitingInRestaurant) {
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					seatCustomer(temp);
					return true;
				}
			
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.ReadyToOrder) {
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					takeOrderFromCustomer(temp);
					while (temp.state != customerState.Asked)
						while(!meetCustomer.tryAcquire());
					giveOrderToCook(temp);
					return true;
				}
				
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.CookPendingReorder){
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					takeReorderFromCustomer(temp);
					return true;
				}
			
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.GettingOrder) {
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					askForCheck(temp);
					serveFoodToCustomer(temp);
					return true;
				}
			
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.Done && mc.check != 0) {
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					clearTable(temp);
					giveCheckToCustomer(temp);
					return true;
				}
			
				temp = null;
				for (myCustomer mc : customers) {
					if(mc.state == customerState.Leaving) {
						temp = mc;
						break;
					}
				}
				if (temp != null) {
					clearTable(temp);
					return true;
			}
		} catch (ConcurrentModificationException e) {return true;}
		}
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	// Actions
	
	private void seatCustomer(myCustomer customer) {
		DoSeatCustomer(customer);
		customer.customer.msgFollowMeToTable(this, customer.menu, customer.tableNumber);
		customer.state = customerState.DoingNothing;
		stateChanged();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToOrigin();
	}
	
	private void takeOrderFromCustomer(myCustomer customer) {
		DoTakeOrderFromCustomer(customer);
		customer.customer.msgWhatWouldYouLike();
		customer.state = customerState.DoingNothing;
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print(customer.customer.getName() + " ordering the " + customer.choice);
		customer.customer.getGui().placeFood(customer.choice + "?");
		stateChanged();
	}
	
	private void giveOrderToCook(myCustomer customer) {
		DoGiveOrderToCook(customer);
		cook.msgHereIsAnOrder(this, customer.tableNumber, customer.choice);
		customer.state = customerState.DoingNothing;
		stateChanged();
	}
	
	private void takeReorderFromCustomer(myCustomer customer) {
		print(customer.customer.getName() + " reordering");
		customer.menu.choices.remove(customer.choice);
		customer.customer.msgReorder(customer.menu);
		customer.state = customerState.CookGettingReorder;
		stateChanged();
	}
	
	private void askForCheck(myCustomer c) {
		print("Calling cashier to make check");
		cashier.msgCreateCheck(this, c.customer, c.choice);
	}
	
	private void serveFoodToCustomer(myCustomer customer) {
		DoServeFoodToCustomer(customer);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.removeFood();
		waiterGui.DoGoToOrigin();
		customer.customer.msgHereIsYourFood(customer.choice);
		customer.state = customerState.DoingNothing;
		stateChanged();
	}
	
	private void giveCheckToCustomer(myCustomer customer) {
		customer.customer.msgHereIsCheck(customer.check);
	}
	
	private void clearTable(myCustomer customer) {
		host.msgTableIsFree(customer.tableNumber);
		customers.remove(customer);
		stateChanged();
	}
	
	// The animation DoXYZ() routines
	private void DoSeatCustomer(myCustomer customer) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer.customer + " at table " + (customer.tableNumber+1));
		//waiterGui.DoGoToOrigin();
		//atDoor.drainPermits();
		waiterGui.DoGoToCust(customer.custNumber+1);
		atCust.drainPermits();
		try {
			//atDoor.acquire();
			atCust.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgAtCustomer();
		waiterGui.DoGoToTable(customer.tableNumber);
	}
	
	private void DoTakeOrderFromCustomer(myCustomer customer) {
		print("Taking order from " + customer.customer);
		waiterGui.DoGoToTable(customer.tableNumber);
	}
	
	private void DoGiveOrderToCook(myCustomer customer) {
		print("Giving " + customer.customer + "'s order of " + customer.choice + " to cook");
		//cook.getGui().placeFood(customer.choice, true);
		waiterGui.DoGoToOrigin();
	}
	
	private void DoServeFoodToCustomer(myCustomer customer) {
		print("Serving " + customer.choice + " to " + customer.customer);
		cook.getGui().placeFood(customer.choice, false);
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.getGui().removeFood();
		waiterGui.placeFood(customer.choice);
		waiterGui.DoGoToTable(customer.tableNumber);
	}
	
	//utilities
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}
	
	public WaiterGui getGui() {
		return waiterGui;
	}
	
	private class myCustomer {
		Customer customer;
		String choice;
		int tableNumber;
		customerState state;
		Menu menu;
		double check;
		int custNumber;
		
		myCustomer(Customer customer, int tableNumber, int custNumber) {
			this.customer = customer;
			this.choice = "";
			this.tableNumber = tableNumber;
			state = customerState.DoingNothing;
			menu = new Menu();
			this.check = 0.0;
			this.custNumber = custNumber;
		}
	}
	public enum customerState {DoingNothing ,WaitingInRestaurant, Seated, ReadyToOrder, Asked, GettingOrder, Done, CookPendingReorder, CookGettingReorder, Leaving};
	
	public void setCook(Cook cook) {
		this.cook = cook;
	}

	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
	
	public boolean isOnBreak() {
		return onBreak;
	}
	
	public void setOnBreak(boolean isOnBreak) {
		if (isOnBreak)
			this.state = AgentState.waitingForBreak;
		else {
			this.state = AgentState.waitingForWork;
			this.onBreak = false;
		}
		stateChanged();
	}

	public void setGuiPanel(RestaurantGui gui) {
		this.gui = gui;
	}
}
