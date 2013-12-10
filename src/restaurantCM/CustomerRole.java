package restaurantCM;

import restaurantCM.gui.CustomerGui;
import restaurantCM.gui.RestaurantGui;
import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import city.PersonAgent;
import city.Role;

/**
 * Restaurant customer agent.
 */
public class CustomerRole extends Role {
	private String name;
	private String choice;
	private int tablenumber;// CHANGED added tablenumber to allow for customer to store and forward tablenum to GUI
	private int hungerLevel = 1000; // determines length of meal
	private double money;
	private double bill;
	Timer timer = new Timer();
	Menu menu;
	private CustomerGui customerGui;

	// agent correspondents
	private HostRole host;
	private WaiterAgent w;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, readyToOrder, 
		orderedNoFood, Eating, DoneEating, Leaving, askForBill, waitingForBill};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, followWaiter, seated, asked, foodDelivered, doneEating, billHere, doneLeaving };
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole(PersonAgent person){
		super(person);
		Random moneyGen = new Random(System.currentTimeMillis());
		this.name = name;
		this.money = .01 * Math.floor(moneyGen.nextDouble() * 40.0 * 100);
		if(name.equals("broke"))
			this.money = 0.0;
		print("money: "+ money );
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostRole host) {
		this.host = host;
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

	public void msgSitAtTable(int tablenumber) {
		event = AgentEvent.followHost;
		this.tablenumber = tablenumber;
		stateChanged();
	}
	public void msgFollowMeToTable(int tablenum, WaiterAgent w){
		print("Following "+w.getName());
		this.tablenumber = tablenum;
		this.w = w;
		event = AgentEvent.followWaiter;
		
		stateChanged();
	}
	public void msgBadOrder(){
		this.state = AgentState.readyToOrder;
		//stateChanged();
	}
	public void msgWhatWouldYouLike(Menu menu){
		this.menu = menu;		
		event = AgentEvent.asked;	
		print("made it to what would you like from "+ w.getName());
		stateChanged();
	}
	public void msgHereIsYourFood(String choice){
		print("My "+choice+" looks so good");		
		event = AgentEvent.foodDelivered;
		stateChanged();
	}
	public void msgHereIsBill(double bill){
		print("recieved bill");
		event = AgentEvent.billHere;
		this.bill = bill;
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
			SitDown(tablenumber);
			return true;
		}
		if(state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			return true;
		}
		
		if( state == AgentState.Seated && event == AgentEvent.seated){
			state = AgentState.readyToOrder;
			readyToOrder();
			return true;
		}
		if (state == AgentState.readyToOrder && event == AgentEvent.asked){
			state = AgentState.orderedNoFood;
			order();
			return true;
		}
		if(state == AgentState.orderedNoFood && event == AgentEvent.foodDelivered){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
//		if(state == AgentState.Eating && event == AgentEvent.doneEating){
//			state = AgentState.Leaving;
//			doneEatingAndLeavingTable();
//			return true;
//		}
//		if(state == AgentState.Leaving && event == AgentEvent.doneLeaving){
//			state = AgentState.DoingNothing;
//			return true;
//		}


		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.askForBill;
			askForBill();
			return true;
		}
		if(state == AgentState.askForBill && event == AgentEvent.billHere){
			state = AgentState.Leaving;
			payBill();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			doneEatingAndLeavingTable();
			return true;
		}
		return false;
	}


	private void payBill() {
	print("paying bill for "+ bill+". I have "+ money+" left");
		w.msgPayBill(this, money - bill);
		this.event = AgentEvent.doneLeaving;
	}

	private void askForBill() {
		print("asking for bill");
		w.msgAskForBill(this);
		
	}

	// Actions

	private void goToRestaurant() {
		print("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown(int tablenumber) {
		print("Being seated. Going to table");
		customerGui.DoGoToSeat(tablenumber);//hack; only one table
	} // changed SitDown to take tablenumber so it can be passed to the GUI
	
	private void readyToOrder() {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w.msgImReadyToOrder(this);
	}
	
	private void order(){
		Random rand = new Random(System.currentTimeMillis());
		int choose = Math.abs(rand.nextInt()%menu.Menu.size());
		int endpoint = choose;
	do{
		this.choice = menu.choices.get(choose%menu.Menu.size());
		choose++;
		if( choose%menu.Menu.size() == endpoint){
			state = AgentState.Leaving;
			event = AgentEvent.doneLeaving;
			print("didn't have enough money for anything, time to leave");
		return;
		}
	}
	while(menu.Menu.get(choice) > this.money);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w.msgHeresMyChoice(choice, this);
	}
	
	
	private void EatFood() {
		print("Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating "+choice);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		15000);// getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		print("Leaving.");
		host.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}
	private void doneEatingAndLeavingTable(){
		print("Leaving.");
		w.msgDoneAndLeaving(this);
		customerGui.DoExitRestaurant();
		
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
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
}

