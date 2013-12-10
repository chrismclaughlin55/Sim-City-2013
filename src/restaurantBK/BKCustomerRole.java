package restaurantBK;


import restaurantBK.gui.CustomerGui;
import restaurantBK.gui.RestaurantGui;
import restaurantBK.gui.RestaurantPanel;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;
import trace.AlertLog;
import trace.AlertTag;
import agent.Agent;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import city.PersonAgent;
import city.Role;

/**
 * Restaurant customer agent.
 */
public class BKCustomerRole extends Role implements Customer {
	RestaurantPanel rest;
	private Customer temp = this;
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private String choice;
	private int waitingY = 4;
	private int waitingX = 30;
	private int startX=30;
	int seatNumber;
	double money;
	double bill;
	ItalianMenu menu;
	// agent correspondents
	private Cashier cashier;
	private Host host;
	private Waiter waiter;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, leavingEarly, BeingSeated, Seated, waitingToOrder, Ordered, Eating, DoneEating, waitingForCheck, goingToPay, paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, Ordering, reordering, reordered, gotFood, doneEating, gotCheck, atCashier, donePaying, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BKCustomerRole(PersonAgent person, String name, RestaurantPanel rest){
		super(person);
		this.rest = rest;
		if(name.equals("broke")||name.equals("punk")) {
			this.money=0.00;
		}
		else if(name.equals("cheap")) {
			this.money=8.00;
		}
		else {
			this.money=40.00;
		}
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	@Override
	public void setHost(Host host) {
		this.host = host;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.interfaces.Cashier)
	 */
	@Override
	public void setCashier(Cashier cash) {
		this.cashier = cash;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setWaiter(restaurant.WaiterAgent)
	 */
	@Override
	public void setWaiter(Waiter waiter)//IN RESTAURANT PANEL
	{
		this.waiter = waiter;
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	@Override
	public String getCustomerName() {
		return name;
	}
	// Messages

	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWouldYouLikeToWait(int)
	 */
	@Override
	public void msgWouldYouLikeToWait(int numberahead) {
		//makeADecisiontoLeaveornot
		/*if(wantsToWait) {
			//donothing
		}
		else {*/
		if(numberahead>2) {
			print("I have to wait? pshh I'm outta here");
			state=AgentState.leavingEarly;
			//event=AgentEvent.donePaying;
			stateChanged();
		}
		else {
			waitingX = numberahead*startX;
			//donothingandwait
		}
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMeToTable(restaurant.WaiterAgent, int, restaurant.Menu)
	 */
	
	@Override
	public void msgWaitHerePlease(int x) {
		waitingX=startX+startX*x;
	}
	public void msgFollowMeToTable(Waiter w, int tableNumber, ItalianMenu m) {
		setWaiter(w);
		menu = m;
		print("Received msgFollowMeToTable");
		this.seatNumber=tableNumber;
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatWouldYouLike()
	 */
	@Override
	public void msgWhatWouldYouLike() {
		event=AgentEvent.Ordering;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgOrderSomethingElse()
	 */
	@Override
	public void msgOrderSomethingElse() {
		event = AgentEvent.reordering;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourFood()
	 */
	@Override
	public void msgHereIsYourFood() {
		event = AgentEvent.gotFood;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToCashier()
	 */
	@Override
	public void msgAnimationFinishedGoToCashier() {
		event = AgentEvent.atCashier;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsTheCheck(double)
	 */
	@Override
	public void msgHereIsTheCheck(double price) {
		bill=price;
		event = AgentEvent.gotCheck;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAllPaidPlusChange(double)
	 */
	@Override
	public void msgAllPaidPlusChange(double change) {
		money=change;
		event = AgentEvent.donePaying;
		stateChanged();
	}
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.waitingToOrder;
			pickOrder();
			return true;
		}
		if (state  == AgentState.waitingToOrder && event == AgentEvent.Ordering) {
			state=AgentState.Ordered;
			giveOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.reordering) {
			state =AgentState.Ordered;
			giveNewOrder();
			return true;
		}
		if (state ==AgentState.Ordered && event == AgentEvent.gotFood) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			//state=AgentState.Leaving;
			//leaveTable();
			//return true;
			state = AgentState.waitingForCheck;
			askForCheck();
			return true;
		}
		if(state ==AgentState.waitingForCheck && event == AgentEvent.gotCheck) {
			state=AgentState.goingToPay;
			leaveTableToPay();
			return true;
			//goPay();
		}
		if(state==AgentState.goingToPay && event == AgentEvent.atCashier) {
			state = AgentState.paying;
			payCashier();
			return true;
		}
		if(state==AgentState.leavingEarly) {
			state=AgentState.paying;
			event=AgentEvent.donePaying;
			leaveEarly();
			return true;
		}
		if(state==AgentState.paying && event ==AgentEvent.donePaying) {
			state=AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	/*private void AskToOrder() {
		Do("Ready to order");
		
	}*/
	private void giveOrder() {
		//WAIT UNTIL WAITER COMES AND THEN GIVE HIM THE CHOICE
		
		print("I'll have the " +choice);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "I'll have the "+choice);
		
		//CREATE A  TEXT BOX FOR CHOICE
		customerGui.flipChoice(choice);
		timer.schedule(	new TimerTask() {
					
					public void run() {
						//print("Decided what I want");
						customerGui.flipChoice(choice);
					}
				},
				1000);
		
		waiter.msgHereIsMyChoice(this,choice);
	}
	private void giveNewOrder() {
		print("Dang it.");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "Dang it");
		if(name.equals("cheap")) {
			print("I can't afford anything else here... I'm out");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "I can't afford anything else here... I'm out");
			state=AgentState.leavingEarly;
			stateChanged();
		}
		else {
			ArrayList<String> choices = new ArrayList<String>(menu.keySet());
			Random r = new Random();
			String check=choice;
			while(check==choice) {
				check = choices.get(Math.abs((r.nextInt())%(choices.size()))); //random from Menu;
			}
			choice = check;
			print("I'll have the " +choice);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "I'll have the "+choice);
		
			//CREATE A  TEXT BOX FOR CHOICE
			customerGui.flipChoice(choice);
			timer.schedule(	new TimerTask() {
					
					public void run() {
						//print("Decided what I want");
						customerGui.flipChoice(choice);
					}
				},
				1000);
		
			waiter.msgHereIsMyChoice(this,choice);
			event = AgentEvent.reordered;
		}
	}
			
	private void goToRestaurant() {
		//Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		customerGui.DoGoToWait(waitingX,waitingY);
	}

	private void leaveEarly() {
		//print("No tables, gonna leave");
		if(this.waiter!=null) {
			waiter.msgLeavingEarly(this);
		}
		host.msgNoTablesImLeaving(this);
	}
	private void SitDown() {
		//Do("Being seated. Going to table");
		if(seatNumber==1)
		{
			customerGui.DoGoToSeat(this.seatNumber,200,250);	
		}
		if(seatNumber==2)
		{
			customerGui.DoGoToSeat(this.seatNumber,326,250);	
		}
		if(seatNumber==3)
		{
			customerGui.DoGoToSeat(this.seatNumber,450,250);	
		}
	}

	private void pickOrder() {
		if(name.equals("broke")) {
			print("This place is too pricy for me, I'm leaving");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "This place is too pricy for me, I'm leaving");
			state=AgentState.leavingEarly;
			stateChanged();
		}
		else {
			if(name.equals("cheap")) {
			choice = "Salad";
			}
			else {
				ArrayList<String> choices = new ArrayList<String>(menu.keySet());	
				Random r = new Random();	
				try {
					choice = choices.get((r.nextInt())%(choices.size())); //random from Menu;
				}	
				catch (IndexOutOfBoundsException e) {
					choice = choices.get(2);
					//IF YOU DON'T WANT A REAL CHOICE, PUNK GET A SALAD
				}
			}
			timer.schedule(	new TimerTask() {
				public void run() {
					//print("Decided what I want");
					print("I think I want the " + choice);
					waiter.msgReadyToOrder(temp);
				}
			},
			3500);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "I think I want the " + choice);
		}
	}
	private void EatFood() {
		//Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		customerGui.flipEat();
		
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating " +choice);
				event = AgentEvent.doneEating;
				customerGui.flipEat();
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
		//GUI.TAKEAWAYTEXTBOXATTHISTABLE
		//THEN MAKE THE TEXTBOX ICON DISAPPEAR
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "Done eating " + choice);
	}
	
	private void askForCheck() {
		print("Waiter, check please");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "Waiter, check please");
		waiter.msgDoneEatingAndWantCheck(this);
	}

	private void leaveTableToPay() {
		//Do("Leaving to pay cashier.");
	//	waiter.msgLeavingTableToPay(this);
		customerGui.DoGoToCashier();
		//NEED A SEMAPHORE HERE
		//IF HE ISN'T GONNA FLAKE
		
		//customerGui.DoExitRestaurant();
	}
	private void payCashier() {
		print("Cashier, take my money");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "Cashier, take my money");
		cashier.msgTakeMyMoney(money,this);
		money=0;
	}
	private void leaveRestaurant() {
		print("Leaving restaurant");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTBK_CUSTOMER, this.getName(), "Leaving restaurant");
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	@Override
	public int getHungerLevel() {
		return hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	@Override
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	@Override
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	@Override
	public CustomerGui getGui() {
		return customerGui;
	}
}

