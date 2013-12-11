package restaurantSM;

import restaurantSM.gui.CustomerGui;
import restaurantSM.gui.RestaurantGui;
import restaurantSM.interfaces.Customer;
import restaurantSM.interfaces.Waiter;
import agent.Agent;
import restaurantSM.utils.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantSM.*;
import trace.AlertLog;
import trace.AlertTag;
/**
 * Restaurant customer agent.
 */
public class SMCustomerRole extends Role implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private SMCashierRole cashier;
	private int sitAtTableNumber = -1;
	private Waiter waiter;
	private Menu menu = new Menu();
	// agent correspondents
	private SMHostRole host;
	private String choice;
	public double bankRoll = 25.00;
	private Bill bill;
	List<String> possibleChoices = new ArrayList<String>();
	DecimalFormat df = new DecimalFormat("#.00");
	private Semaphore isMoving = new Semaphore(0,true);

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, AtRestaurant, WaitingInRestaurant, BeingSeated, Seated, ReadyToOrder, Ordered, Eating, DoneEating, Leaving, PayingBill, BillPaid};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, makeChoice, seated, payBill, eatFood, doneEating, leave, doneLeaving, willWait};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public SMCustomerRole(PersonAgent p){
		super(p);
		name = p.getName();
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(SMHostRole host) {
		this.host = host;
	}
	
	public void setCashier(SMCashierRole cash) {
		cashier = cash;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgWaitPlease(){
		//Random generator = new Random();
		//int waiting = generator.nextInt(2);
		//if (waiting == 1) {
			event = AgentEvent.willWait;
		//}
		//else {
			//event = AgentEvent.leave;
		//}
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgHeresYourChange(Bill b){
		event = AgentEvent.leave;
		bill = b;
		bankRoll += b.change;
		stateChanged();
	}
	
	public void msgHeresYourBill(Bill b){
		event = AgentEvent.payBill;
		bill = b;
		stateChanged();
	}
	
	public void msgAnimationDone(){
		isMoving.release();
		stateChanged();
	}
	
	public void msgFollowMe(Waiter w, Menu m, int tableNumber){
		event = AgentEvent.followWaiter;
		waiter = w;
		menu = m;
		sitAtTableNumber = tableNumber;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(Menu m){
		menu = m;
		event = AgentEvent.makeChoice;
		stateChanged();
	}
	
	public void msgHeresYourOrder(){
		event = AgentEvent.eatFood;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.AtRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.AtRestaurant && event == AgentEvent.willWait) {
			state = AgentState.WaitingInRestaurant;
			WillingToWait();
			return true;
		}
		if (state == AgentState.AtRestaurant && event == AgentEvent.leave) {
			state = AgentState.DoingNothing;
			UnwillingToWait();
			return true;
		}
		if ((state == AgentState.WaitingInRestaurant || state == AgentState.AtRestaurant) && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown(sitAtTableNumber);
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.ReadyToOrder;
			ReadyToOrder();
			return true;
		}
		if (state == AgentState.ReadyToOrder & event == AgentEvent.makeChoice){
			state = AgentState.Ordered;
			event = AgentEvent.none;
			HeresMyChoice();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.makeChoice){
			state = AgentState.Ordered;
			event = AgentEvent.none;
			HeresMyChoice();
			return true;
		}

		if (state == AgentState.Ordered && event == AgentEvent.eatFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.PayingBill;
			readyForCheck();
			return true;
		}
		if (state == AgentState.PayingBill && event == AgentEvent.payBill){
			state = AgentState.BillPaid;
			GiveBillToCashier();
			return true;
		}
		if (state == AgentState.BillPaid && event == AgentEvent.leave){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Going to restaurant");
		customerGui.DoGoToLine();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		host.msgIWantFood(this);
	}
	
	private void WillingToWait() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Willing to wait");
		host.msgIWillWait(this);
	}
	
	private void UnwillingToWait() {
		customerGui.DoExitRestaurant();
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Unwilling to wait");
	}
	
	private void ReadyToOrder() {
		waiter.msgReadyToOrder(this);
	}
	
	private void HeresMyChoice() {
		for (String item : menu.getItems()) {
			if (menu.prices.get(item) <= bankRoll) {
				possibleChoices.add(item);
			}
		}
		if (possibleChoices.isEmpty()) {
			Random generator = new Random();
			int randomIndex = generator.nextInt(3);
			if (randomIndex == 1) {
				choice = menu.chooseItem();
				customerGui.setStatusText(choice + "?");
				waiter.msgHeresMyChoice(this, choice);
				AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Ordering "+choice);
			}
			else {
			leaveTable();
				this.state = AgentState.DoingNothing;
				customerGui.setFull();
			}
		}
		else {
			Random generator = new Random();
			int randomIndex = generator.nextInt(possibleChoices.size());
			choice = possibleChoices.get(randomIndex);
			customerGui.setStatusText(choice + "?");
			waiter.msgHeresMyChoice(this, choice);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Ordering "+choice);
		}
		possibleChoices.clear();
	}
	
	private void readyForCheck() {
		waiter.msgReadyForCheck(this);
	}

	private void GiveBillToCashier(){
		cashier.msgHeresMyBill(bankRoll, bill);
		bankRoll -= bankRoll;
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Paying the bill of $"+bill.total);
	}
	
	private void SitDown(int tableNumber) {
		customerGui.DoGoToSeat(tableNumber);//hack; only one table
	}

	private void EatFood() {
		customerGui.setStatusText(choice);
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
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		customerGui.setStatusText("");
		waiter.msgDoneEating(this);
		customerGui.DoExitRestaurant();
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTSM_CUSTOMER, this.getName(), "Leaving restaurant");
		person.exitBuilding();
		person.msgFull();
		doneWithRole();
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
		//need to eat until hunger level is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

