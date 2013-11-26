package restaurantMQ;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.swing.JCheckBox;

import restaurantMQ.gui.CustomerGui;
import restaurantMQ.gui.RestaurantPanel;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;
import city.PersonAgent;
import city.Role;

public class MQCustomerRole extends Role implements Customer
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
	private JCheckBox hungerBox;
	
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

	/*CONSTRUCTOR*/
	public MQCustomerRole(PersonAgent person, Timer timer, JCheckBox hunger, RestaurantPanel rp) 
	{
		super(person);
		this.name = super.getName();
		this.timer = timer;
		hungerBox = hunger;
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
	
	/*SETTERS*/
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
	
	
	/* Messages*/
	public void msgPause()
	{
		super.msgPause();
	}
	
	public void msgGotHungry()
	{
		hungerBox.doClick();
	}
	
	public void gotHungry() {//from animation
		System.out.println(name + ": I'm hungry");
		event = AgentEvent.gotHungry;
		setActive();
		stateChanged();
	}
	
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
	
	public void msgHereIsCheck(double balance)
	{
		this.balance = balance;
		event = AgentEvent.receivedCheck;
		stateChanged();
	}

	public void msgFollowMe(Waiter waiter)
	{
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		stateChanged();
	}
	
	public void msgFollowMe(Waiter waiter, int table)
	{
		this.table = table;
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		stateChanged();
	}
	
	public void msgFollowMe(Waiter waiter, int table, Menu menu)
	{
		this.table = table;
		event = AgentEvent.followWaiter;
		this.waiter = waiter;
		this.menu = menu;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	public void msgReadyToOrder()
	{
		state = AgentState.ReadyToOrder;
		stateChanged();
	}

	public void msgWhatDoYouWant()
	{
		event = AgentEvent.takingOrder;
		stateChanged();
	}

	public void msgHereIsFood()
	{
		event = AgentEvent.receivedFood;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	public void msgAnimationDone()
	{
		actionDone.release();
	}

	public void msgPleaseChooseSomethingElse(Menu menu)
	{
		this.menu = menu;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		stateChanged();
	}

	public void msgGoodToGo()
	{
		event = AgentEvent.leave;
		stateChanged();
	}
	
	public void msgNotEnough()
	{
		event = AgentEvent.leave;
		stateChanged();
	}

	
	/*SCHEDULER*/
	public boolean pickAndExecuteAnAction() 
	{
//		CustomerAgent is a finite state machine
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

	
	/*ACTIONS*/
	private void LeaveNow()
	{
		System.out.println(name + " Leaving then.");
		host.msgLeaving(this);
		customerGui.DoExitRestaurant();
	}
	
	private void goToRestaurant() {
		System.out.println(name + "Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		System.out.println(name + "Being seated. Going to table");
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
		System.out.println(name + "Eating Food");
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
				System.out.println(name + "Done eating, cookie=" + cookie);
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
		System.out.println(name + "Leaving.");
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
	
	//THIS IS THE LAST ACTION CALLED BY THE ROLE
	private void LeaveRestaurant()
	{
		event = AgentEvent.doneLeaving;
		customerGui.DoExitRestaurant();
		try
		{
			actionDone.acquire();
		}
		catch(Exception e){}
		//NOW DEACTIVATE THE ROLE
		state = AgentState.DoingNothing;
		
		person.exitBuilding();
		person.msgFull();
		doneWithRole();
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
