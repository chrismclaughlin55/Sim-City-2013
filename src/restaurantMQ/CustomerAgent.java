package restaurantMQ;

import restaurantMQ.gui.CustomerGui;
import restaurantMQ.gui.RestaurantGui;
import restaurantMQ.gui.RestaurantPanel;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;
import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer 
{
	private Semaphore actionDone = new Semaphore(0, true); //used to pause agent during animation
	
	private String name;
	private int hungerLevel = 1;        // determines length of meal
	Timer timer;
	private CustomerGui customerGui;
	private int table;
	private int waitingNumber = 0;
	private String choice;
	private RestaurantPanel rp;
	
	double money = 100;
	double balance = 0;
	
	Menu menu = null;
	// agent correspondents
	private Host host;
	private Waiter waiter;
	private Cashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Deciding, ReadyToOrder, WaitingForFood, 
		Eating, DoneEating, Leaving, Paying, Paid};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, dontWait, gotHungry, followWaiter, seated, readyToOrder, takingOrder, receivedFood, doneEating, NeedToPay, receivedCheck, leave, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name, Timer timer, RestaurantPanel rp){
		super();
		this.name = name;
		this.timer = timer;
		this.rp = rp; //hack to set up scenarios
		
		if(name.equals("OutOfFood"))
		{
			rp.OutOfFoodHack();
		}
		else if(name.equals("Broke"))
		{
			money = 0;
		}
		else if(name.equals("Poor"))
		{
			money = 6;
		}
		else if(name.equals("Poor2"))
		{
			money = 6;
			rp.OutOfSaladHack();
		}
		else if(name.equals("Flake"))
		{
			money = 0;
		}
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Host host) {
		this.host = host;
	}

	public void setCashier(Cashier cashier)
	{
		this.cashier = cashier;
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print(name + ": I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	/*
	public void msgSitAtTable() {
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		stateChanged();
	}

	public void msgSitAtTable(int tableNum) {
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		table = tableNum;
		stateChanged();
	}*/
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgTablesFull()
	 */
	
	public void msgGoToSpot(int number)
	{
		waitingNumber = number;
		stateChanged();
	}
	
	public void msgTablesFull()
	{
		if(!name.equals("Leaving"))
		{
			return;
		}
		if(name.equals("Leaving"));
		{
			event = AgentEvent.dontWait;
			stateChanged();
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsCheck(double)
	 */
	@Override
	public void msgHereIsCheck(double balance)
	{
		this.balance = balance;
		event = AgentEvent.receivedCheck;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(restaurant.interfaces.Waiter)
	 */
	@Override
	public void msgFollowMe(Waiter waiter)
	{
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(restaurant.interfaces.Waiter, int)
	 */
	@Override
	public void msgFollowMe(Waiter waiter, int table)
	{
		this.table = table;
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(restaurant.interfaces.Waiter, int, restaurant.Menu)
	 */
	@Override
	public void msgFollowMe(Waiter waiter, int table, Menu menu)
	{
		this.table = table;
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		this.menu = menu;
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
	 * @see restaurant.Customer#msgReadyToOrder()
	 */
	@Override
	public void msgReadyToOrder()
	{
		state = AgentState.ReadyToOrder;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgWhatDoYouWant()
	 */
	@Override
	public void msgWhatDoYouWant()
	{
		event = AgentEvent.takingOrder;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsFood()
	 */
	@Override
	public void msgHereIsFood()
	{
		event = AgentEvent.receivedFood;
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
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationDone()
	 */
	@Override
	public void msgAnimationDone()
	{
		actionDone.release();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgPleaseChooseSomethingElse(restaurant.Menu)
	 */
	@Override
	public void msgPleaseChooseSomethingElse(Menu menu)
	{
		this.menu = menu;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgGoodToGo()
	 */
	@Override
	public void msgGoodToGo()
	{
		event = AgentEvent.leave;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgNotEnough()
	 */
	@Override
	public void msgNotEnough()
	{
		event = AgentEvent.leave;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if(waitingNumber != 0)
		{
			GoToSpot(waitingNumber);
			waitingNumber = 0;
			return true;
		}
		
		if(event == AgentEvent.dontWait)
		{
			state = AgentState.DoingNothing;
			event = AgentEvent.doneLeaving;
			LeaveNow();
			return true;
		}
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown(table);
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Deciding;
			ChooseOrder();
			return true;
		}
		
		if(state == AgentState.ReadyToOrder && event == AgentEvent.takingOrder)
		{
			GiveOrder();
			return true;
		}
		if(state == AgentState.WaitingForFood && event == AgentEvent.receivedFood)
		{
			state = AgentState.Eating;
			EatFood();
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		
		if(state == AgentState.Leaving && event == AgentEvent.receivedCheck)
		{
			state = AgentState.Paying;
			PayCashier();
			return true;
		}
		
		if(state == AgentState.Paying && event == AgentEvent.leave)
		{
			state = AgentState.Leaving;
			LeaveRestaurant();
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

	private void LeaveNow()
	{
		System.out.println(name + " Leaving then.");
		host.msgLeaving(this);
		customerGui.DoExitRestaurant();
	}
	
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		//customerGui.DoGoToSeat(1);//hack; only one table
	}
	
	private void SitDown(int tableNum) {
		System.out.println(name + ": Being seated. Going to table " + tableNum);
		
		//animation
		customerGui.DoGoToSeat(tableNum);
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
	}

	private void ChooseOrder()
	{
		System.out.println(name + " Ready to order now.");
		msgReadyToOrder();
		waiter.msgReadyToOrder(this);
	}
	
	private void GiveOrder()
	{
		if(name.equals("OutOfFood") && menu.has("Steak"))
		{
			choice = "Steak";
		}
		else if(name.equals("Broke"))
		{
			System.out.println("I can't afford anything. I'm leaving.");
			waiter.msgLeaving(this);
			state = AgentState.Paying;
			event = AgentEvent.leave;
			return;
		}
		else if(name.equals("Poor"))
		{
			choice = "Salad";
		}
		else if(name.equals("Poor2"))
		{
			choice = "Salad";
			if(!menu.has(choice))
			{
				System.out.println("I can't afford anything. I'm leaving.");
				waiter.msgLeaving(this);
				state = AgentState.Paying;
				event = AgentEvent.leave;
				return;
			}
		}
		else
		{
			choice = menu.chooseRandom();
		}
		
		System.out.println(name + ": I'll have the " + choice);
		waiter.msgHereIsChoice(choice, this);
		customerGui.setWaitingOrder(choice);
		state = AgentState.WaitingForFood;
	}
	
	private void EatFood() {
		customerGui.setEatingOrder(choice);
		Do("Eating Food");
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
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				customerGui.clearOrder();
				//isHungry = false;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		event = AgentEvent.NeedToPay;
		Do("Leaving.");
		waiter.msgDoneEating(this);
	}
	
	private void PayCashier()
	{
		customerGui.GoToCashier();
		try
		{
			actionDone.acquire();
		}
		catch(Exception e){}
		
		double payment;
		if(balance > money)
		{
			payment = money;
		}
		else
		{
			payment = balance;
		}
		money -= payment;
		
		System.out.println("Here is my payment of $" + payment);
		cashier.msgHereIsMoney(this, payment);
	}
	
	private void LeaveRestaurant()
	{
		event = AgentEvent.doneLeaving;
		customerGui.DoExitRestaurant();
	}
	
	private void GoToSpot(int number)
	{
		customerGui.DoGoToSpot(number);
		try
		{
			actionDone.acquire();
		}
		catch(Exception e){}
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

