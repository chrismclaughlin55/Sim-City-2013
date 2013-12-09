package restaurantLY;

import restaurantLY.gui.*;
import agent.Agent;
import restaurantLY.interfaces.*;
import restaurantLY.test.mock.EventLog;
import restaurantLY.test.mock.LoggedEvent;

import java.math.BigDecimal;
import java.util.*;
import java.awt.Color;
import java.util.concurrent.*;

import javax.swing.JCheckBox;

import city.PersonAgent;
import city.Role;

/**
 * Restaurant customer agent.
 */

public class LYCustomerRole extends Role implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	CustomerGui customerGui;
	
	private int seatNumber;
	
	// agent correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;
	private Menu menu;
	
	private boolean isHungry = false; //hack for gui
	private boolean isWaiting = true;
	
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, Seated, ReadyToOrder, Asked, Eating, Paying, Leaving, DoneLeaving, GettingChange};
	private AgentState state = AgentState.DoingNothing;//The start state
	
	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, makeOrder, gotFood, doneEating, gotCheck, gotChange, restaurantIsFull, doneLeaving};
	AgentEvent event = AgentEvent.none;
	
	private RestaurantGui gui;
	private RestaurantPanel restPanel;
	
	private double money;
	private double change;
	private double check;
	
	private boolean NonNormLeave;
	//private boolean changeOrder = false;
	private boolean isOrdered = false;
	private boolean isCheapestOrder = false;
	private boolean noDebt = true;
	
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore meetWaiter = new Semaphore(0, true);
	private Semaphore left = new Semaphore(0, true);
	private Semaphore atWaiting = new Semaphore(0, true);
	
	public EventLog log = new EventLog();
	
	public String custOrder;
    
    private JCheckBox hungerBox;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public LYCustomerRole(PersonAgent person, RestaurantPanel rp, JCheckBox hunger) {//RestaurantGui gui) {
		super(person);
		
		this.name = super.getName();
		restPanel = rp;
		customerGui = new CustomerGui(this, gui);
		if (name.equals("NoMoney") || name.equals("NoMoneyL") || name.equals("NoMoneyO")) {
			this.money = 5.0;
		}
		else if (name.equals("Cheapest")) {
			this.money = 6.0;
		}
		else {
			this.money = ((int)((Math.random() * 100) * 100 + 0.50)) / 100;
		}
		this.change = 0.0;
		this.check = 0.0;
		NonNormLeave = false;
        hungerBox = hunger;
	}
	
	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Host host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
	
	// Messages
    
    public void msgGotHungry() {
        hungerBox.doClick();
    }
	
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		isHungry = true;
		NonNormLeave = false;
		isOrdered = false;
		stateChanged();
	}
	
	public void msgFollowMeToTable(Waiter waiter, Menu menu, int tableNumber) {
		print("Received msgFollowMeToTable from " + waiter.getName());
		this.waiter = waiter;
		this.menu = menu;
		event = AgentEvent.followWaiter;
		stateChanged();
		seatNumber = tableNumber;
	}
	
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgDecidedChoice(){
		event = AgentEvent.seated;
		stateChanged(); 
	}
	
	public void msgWhatWouldYouLike(){
		meetWaiter.release();
		stateChanged(); 
	}
	
	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.gotFood;
		stateChanged();
	}
	
	public void msgDoneEating() {
		event = AgentEvent.doneEating;
		stateChanged(); 
	}
	
	public void msgLeaving() {
		print("Leaving restaurant");
		state = AgentState.DoingNothing;
		isHungry = false;
		stateChanged();
	}
	
	public void msgWaiting() {
		print("Waiting in line");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgReorder(Menu menu) {
		print("Changing choice to reorder");
		state = AgentState.WaitingInRestaurant;
		event = AgentEvent.followWaiter;
		this.menu = menu;
		stateChanged();
	}

	public void msgHereIsCheck(double check) {
		print("Getting check from waiter");
		event = AgentEvent.gotCheck;
		this.check = check;
		stateChanged();
	}
	
	public void msgHereIsChange(double change) {
		print("Getting change from cashier");
		event = AgentEvent.gotChange;
		this.change = change;
		stateChanged();
	}

	public void msgRestaurantIsFull() {
		print("Restaurant is full");
		event = AgentEvent.restaurantIsFull;
		stateChanged();
	}
	
	public void msgOweMoney() {
		event = AgentEvent.gotChange;
		this.noDebt = false;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgLeft() {//from animation
        left.release();
        stateChanged();
    }
	
	public void msgAtWaiting() {//from animation
		//print("msgAtWaiting() called");
		atWaiting.release();// = true;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//  CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant) {
			if (event == AgentEvent.followWaiter) {
				SitDownAndMakeChoice();
				return true;
			}
			else if (event == AgentEvent.restaurantIsFull) {
				waitOrLeave();
				return true;
			}
		}
		if (state == AgentState.Seated && event == AgentEvent.seated) {
			callWaiter();
			while(!meetWaiter.tryAcquire());
			makeOrder();
			state = AgentState.Asked;
			return true;
		}
		if (state == AgentState.Asked && event == AgentEvent.gotFood) {
			EatFood();
			state = AgentState.Eating;
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			leaveTable();
			state = AgentState.Paying;
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.gotCheck) {
			payCheck();
			state = AgentState.GettingChange;
			return true;
		}
		if (state == AgentState.GettingChange && event == AgentEvent.gotChange) {
			gotChange();
			state = AgentState.DoingNothing;
			return true;
		}
		
		return false;
	}

	// Actions
	
	private void goToRestaurant() {
		print("Going to restaurant, having $" + this.money);
		//customerGui.DoGoToWaitingArea();
		
		host.msgIWantToEat(this);//send our instance, so he can respond to us
		stateChanged();
	}
	
	private void SitDownAndMakeChoice(){
		customerGui.DoGoToSeat(seatNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkCheapestOrder();
		
		double orderAnyway = Math.random() + 0.5;
		if (name.equals("NoMoneyL")) {
			orderAnyway = 0;
		}
		else if (name.equals("NoMoneyO")) {
			orderAnyway = 2;
		}
		if (money < 5.99 && orderAnyway < 1) {
			print("No enough money");
			NonNormLeave = true;
			leaveTable();
			return;
		}
		if (isCheapestOrder) {
			if (!menu.choices.containsKey("Salad")) {
				print("Cheapest order running out");
				NonNormLeave = true;
				leaveTable();
				return;
			}
		}
		if (menu.choices.isEmpty()) {
			print("All food running out");
			NonNormLeave = true;
			leaveTable();
			return;
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				msgDecidedChoice();	
			}},
			5000);
		state = AgentState.Seated;
		stateChanged();
	}
	
	private void callWaiter(){
		print("Ready to order");
		waiter.msgImReadyToOrder(this);
		stateChanged();
	}
	
	private void makeOrder(){
		if(menu.choices.isEmpty()) {
			print("All food running out");
			leaveTable();
			NonNormLeave = true;
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			waiter.msgHereIsMyChoice(this, "");
			stateChanged();
			return;
		}
		if (isCheapestOrder) {
			print("Ordering the cheapest order of Salad");
			isOrdered = true;
			menu.choices.remove("Salad");
			waiter.msgHereIsMyChoice(this, "Salad");
			stateChanged();
			return;
		}
		
		Object choices[] = menu.choices.keySet().toArray();
		String choice = (String)choices[(int)(Math.random() * choices.length)];
		waiter.msgHereIsMyChoice(this, choice);
		isOrdered = true;
		menu.choices.remove(choice);
		custOrder = choice;
		stateChanged();
	}
	
	private void EatFood() {
		customerGui.placeFood(custOrder);
		print("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie = " + cookie);
				//isHungry = false;
				msgDoneEating();
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void leaveTable() {
		customerGui.removeFood();
		print("Leaving table");
		if(NonNormLeave) {
			waiter.msgLeaving(this);
			this.msgLeaving();
		}
		else {
			waiter.msgDoneEatingAndLeaving(this);
		}
		customerGui.DoExitRestaurant();
		isHungry = false;
		//gui.setCustomerEnabled(this);
		stateChanged();
        left.drainPermits();
        try {
            left.acquire();
        } catch(Exception e){}
		person.exitBuilding();
		person.msgFull();
		doneWithRole();
	}
	
	private void waitOrLeave() {
		final Customer c = this;
		double decision = Math.random() + 0.5;
		if (this.name.equals("Waiting")) {
			isWaiting = true;
			print("Deciding for waiting");
		}
		else if (this.name.equals("Leaving")) {
			isWaiting = false;
			print("Deciding for leaving");
		}
		else {
			if (decision > 1) {
				isWaiting = true;
				print("Deciding for waiting");
			}
			else {
				isWaiting = false;
				print("Deciding for leaving");
			}
		}
		if (isWaiting) {
			host.msgCustomerWaiting(c);
			c.msgWaiting();
		}
		else {
			host.msgCustomerLeaving(c);
			c.msgLeaving();
			customerGui.DoExitRestaurant();
			person.exitBuilding();
			person.msgFull();
			doneWithRole();
		}
		stateChanged();
	}

	private void payCheck() {
		print("Paying $" + money + " to cashier");
		cashier.msgHereIsMoney(this, money);
		stateChanged();
	}

	private void gotChange() {
		BigDecimal moneyBD = new BigDecimal(this.change);
		moneyBD = moneyBD.setScale(2, BigDecimal.ROUND_HALF_UP);
		log.add(new LoggedEvent("Getting change of $" + moneyBD));
		print("Getting change of $" + moneyBD);
		money = change;
		noDebt = true;
		this.msgLeaving();
		stateChanged();
	}
	
	public void checkCheapestOrder() {
		if (this.money >= 5.99 && this.money < 8.99) {
			print("Having only enough money to order the cheapest order");
			isCheapestOrder = true;
		}
		else {
			isCheapestOrder = false;
		}
		stateChanged();
	}
	
	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}
	
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}
	
	public String toString() {
		return "Customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	public boolean isHungry() {
		return isHungry;
	}
}