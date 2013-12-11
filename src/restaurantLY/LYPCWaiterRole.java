package restaurantLY;

import agent.Agent;
import restaurantKC.pcCookOrder;
import restaurantLY.gui.*;
import restaurantLY.interfaces.*;
import restaurantLY.test.mock.EventLog;
import restaurantLY.test.mock.LoggedEvent;
import restaurantLY.interfaces.Host;
import trace.AlertLog;
import trace.AlertTag;

import java.util.*;
import java.util.concurrent.*;
import java.awt.Color;

import javax.swing.JCheckBox;

import city.PersonAgent;
import city.Role;

/**
 * Restaurant Waiter Agent
 */

public class LYPCWaiterRole extends Role implements Waiter {
	private String name;
	private List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCook = new Semaphore(0, true);
	private Semaphore atDoor = new Semaphore(0, true);
	private Semaphore atCust = new Semaphore(0, true);
	private Semaphore meetCustomer = new Semaphore(0, true);
    private Semaphore left = new Semaphore(0, true);
	
	private Host host;
	private List<Cook> cooks;
	private Cook cook;
	private Cashier cashier;
	
	Timer timer = new Timer();
	
	public WaiterGui waiterGui = null;//new WaiterGui(this);
	
	public enum AgentState {waitingForBreak, waitingForWork, onBreak, working};
	private AgentState state;
	
	private boolean onBreak = false;
	
	RestaurantGui gui;
    RestaurantPanel restPanel;
    JCheckBox breakBox;
	
	public EventLog log = new EventLog();
	
	public List<PCOrder> cookOrders;
	
	public LYPCWaiterRole(PersonAgent person) {
		super(person);
	}
	
	public LYPCWaiterRole(PersonAgent person, RestaurantPanel rp, List<PCOrder> cookOrders) {
		super(person);
		this.name = person.getName();
		restPanel = rp;
		this.cookOrders = cookOrders;
	}
    
    public LYPCWaiterRole(PersonAgent person, RestaurantPanel rp, Host host, List<Cook> cooks, Cashier cashier, JCheckBox breakBox, List<PCOrder> cookOrders) {
        super(person);
        restPanel = rp;
        this.name = person.getName();
        this.host = host;
        this.breakBox = breakBox;
        this.cooks = cooks;
        this.cashier = cashier;
        this.cookOrders = cookOrders;
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
    
    public void msgLeft() {//from animation
        left.release();
        stateChanged();
    }
    
    public void msgWantBreak()
	{
		breakBox.setEnabled(false);
		state = AgentState.waitingForBreak;
		stateChanged();
	}
	
	public void msgDecisionOnBreak(boolean isOnBreak) {
		onBreak = isOnBreak;
		//gui.setstateCB(onBreak);
		if (isOnBreak) {
			print("Host allows my break");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Host allows my break");
			state = AgentState.onBreak;
		}
		else {
			print("Host denies my break");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Host denies my break");
			state = AgentState.working;
		}
		stateChanged();
	}
	
	public void msgCookRunOutOfFood(int tableNumber) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.tableNumber == tableNumber) {
					print("Running out of " + mc.customer.getName() + "'s order of " + mc.choice);
					AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Running out of " + mc.customer.getName() + "'s order of " + mc.choice);
					mc.state = customerState.CookPendingReorder;
					for (Cook cook: cooks) {
						cook.getGui().removeFood();
					}
				}
			}
		}
		stateChanged();
	}
	
	public void msgAskForCheck(Customer customer, double check) {
		log.add(new LoggedEvent("Asking cashier for " + customer.getName() + "'s check of $" + check));
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Asking cashier for " + customer.getName() + "'s check of $" + check);
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
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.waitingForBreak) {
			print("Asking the host for break");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Asking the host for break");
			host.msgAskForBreak(this);
		}
		if (state == AgentState.waitingForWork) {
			host.msgBackToWork(this);
		}
		
		if(!customers.isEmpty()){
			myCustomer temp = null;
			synchronized(customers) {
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
			}
			
			synchronized(customers) {
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
			}
				
			synchronized(customers) {
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
			}
			
			synchronized(customers) {
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
			}
			
			synchronized(customers) {
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
			}
			
			synchronized(customers) {
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
		}
		}
		
		if(person != null && restPanel != null && customers != null) {
			if(person.cityData.hour >= restPanel.CLOSINGTIME && customers.isEmpty()) {
				LeaveRestaurant();
				return true;
			}
		}
		//if(isActive())
			//gui.DefaultAction();
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
    
    private void LeaveRestaurant() {
        host.msgLeavingNow(this);
        waiterGui.DoLeaveRestaurant();
        left.drainPermits();
        try {
            left.acquire();
        } catch(Exception e){}
        person.exitBuilding();
        person.msgFull();
        person.msgDoneWithJob();
        doneWithRole();
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), customer.customer.getName() + " ordering the " + customer.choice);
		customer.customer.getGui().placeFood(customer.choice + "?");
		stateChanged();
	}
	
	public void giveOrderToCook(myCustomer customer) {
		DoGiveOrderToCook(customer);
		cookOrders.add(new PCOrder(this, customer.tableNumber ,customer.choice));
		log.add(new LoggedEvent("Giving order of " + customer.choice + " to cook"));
		for (Cook cook: cooks) {
			cook.msgHereIsAnOrder(this, customer.tableNumber, customer.choice);
		}
		customer.state = customerState.DoingNothing;
		stateChanged();
	}
	
	private void takeReorderFromCustomer(myCustomer customer) {
		print(customer.customer.getName() + " reordering");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), customer.customer.getName() + " reordering");
		customer.menu.choices.remove(customer.choice);
		customer.customer.msgReorder(customer.menu);
		customer.state = customerState.CookGettingReorder;
		stateChanged();
	}
	
	private void askForCheck(myCustomer c) {
		print("Calling cashier to make check");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Calling cashier to make check");
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Seating " + customer.customer + " at table " + (customer.tableNumber+1));
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Taking order from " + customer.customer);
		waiterGui.DoGoToTable(customer.tableNumber);
	}
	
	private void DoGiveOrderToCook(myCustomer customer) {
		print("Giving " + customer.customer + "'s order of " + customer.choice + " to cook");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Giving " + customer.customer + "'s order of " + customer.choice + " to cook");
		//cook.getGui().placeFood(customer.choice, true);
		waiterGui.DoGoToOrigin();
	}
	
	private void DoServeFoodToCustomer(myCustomer customer) {
		print("Serving " + customer.choice + " to " + customer.customer);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_WAITER, this.getName(), "Serving " + customer.choice + " to " + customer.customer);
		for (Cook cook: cooks) {
			cook.getGui().placeFood(customer.choice, false);
		}
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Cook cook: cooks) {
			cook.getGui().removeFood();
		}
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
	
	public class myCustomer {
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
		
		public myCustomer(Customer customer, int tableNumber, int custNumber, String choice) {
			this.customer = customer;
			this.choice = choice;
			this.tableNumber = tableNumber;
			state = customerState.DoingNothing;
			menu = new Menu();
			this.check = 0.0;
			this.custNumber = custNumber;
		}
	}
	public enum customerState {DoingNothing ,WaitingInRestaurant, Seated, ReadyToOrder, Asked, GettingOrder, Done, CookPendingReorder, CookGettingReorder, Leaving};

	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
	
	public void setCook(Cook cook) {
		this.cook = cook;
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
