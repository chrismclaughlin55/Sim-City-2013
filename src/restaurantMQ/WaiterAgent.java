package restaurantMQ;

import restaurantMQ.gui.CustomerGui;
import restaurantMQ.gui.RestaurantGui;
import restaurantMQ.gui.WaiterGui;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;
import agent.Agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.JCheckBox;

public class WaiterAgent extends Agent implements Waiter
{
	private Semaphore actionDone = new Semaphore(0, true);
	
	//Data members
	private Host host; //assigned in constructor
	private WaiterGui gui = null;
	private Cashier cashier;
	private List<CookAgent> cooks = new ArrayList<CookAgent>();
	public String name;
	int waitingSpot;
	enum BreakStatus {None, WantBreak, AskedForBreak, BreakOK, OnBreak, Back};
	BreakStatus breakStatus = BreakStatus.None;
	JCheckBox breakBox;
	private int waiterNumber;
	
	Menu menu = null;
	
	//Constructs for waiter's information about customers
	enum CustomerState {WaitingForSeat, Seated, ReadyToOrder, Ordering, WaitingForFood, Eating, Done, Gone};
	
	private class MyCustomer
	{	
		
		Customer customer;
		int table;
		int waitingSpot;
		CustomerState customerState = CustomerState.WaitingForSeat;
		
		MyCustomer(Customer customer, int table)
		{
			this.customer = customer;
			this.table = table;
		}
		
		MyCustomer(Customer customer, int waitingSpot, int table)
		{
			this.customer = customer;
			this.waitingSpot = waitingSpot;
			this.table = table;
		}
	}
	
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	//Construct for waiter's information about orders
	enum OrderState {OrderTaken, Cooking, Reject, Done};
	
	private class Order
	{
		String choice;
		int table;
		OrderState state = OrderState.OrderTaken;
		Customer customer;
		
		Order(String choice, int table, Customer customer)
		{
			this.choice = choice;
			this.table = table;
			this.customer = customer;
		}
	}
	
	private List<Order> orders = new ArrayList<Order>();
	
	private class Check
	{
		Customer customer;
		double balance;
		
		Check(Customer customer, double balance)
		{
			this.customer = customer;
			this.balance = balance;
		}
	}
	
	private List<Check> checks = new ArrayList<Check>();
	
	//End of data members
	
	//CONSTRUCTORS
	public WaiterAgent(Host host)
	{
		this.host = host;
	}
	
	public WaiterAgent(Host host, List<CookAgent> cooks)
	{
		this.host = host;
		this.cooks = cooks;
	}
	
	public WaiterAgent(String name, Host host, List<CookAgent> cooks)
	{
		this.name = name;
		this.host = host;
		this.cooks = cooks;
	}
	
	public WaiterAgent(String name, Host host, List<CookAgent> cooks, Menu menu)
	{
		this.name = name;
		this.host = host;
		this.cooks = cooks;
		this.menu = menu;
	}
	
	public WaiterAgent(String name, int waiterNumber, Host host, List<CookAgent> cooks, Cashier cashier, Menu menu, JCheckBox breakBox)
	{
		this.name = name;
		this.waiterNumber = waiterNumber;
		this.breakBox = breakBox;
		this.host = host;
		this.cooks = cooks;
		this.cashier = cashier;
		this.menu = menu;
	}
	
	public void setGui(WaiterGui gui)
	{
		this.gui = gui;
		gui.setNumber(waiterNumber);
	}
	
	
	//MESSAGES
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeaving(restaurant.CustomerAgent)
	 */
	@Override
	public void msgLeaving(Customer customer)
	{
		for(MyCustomer c : customers)
		{
			if(c.customer.equals(customer))
			{
				c.customerState = CustomerState.Gone;
				stateChanged();
				return;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSeatCustomer(restaurant.CustomerAgent, int)
	 */
	@Override
	public void msgSeatCustomer(Customer customer, int table)
	{
		customers.add(new MyCustomer(customer, table));
		stateChanged();
	}
	
	public void msgSeatCustomer(Customer customer, int spot, int table)
	{
		customers.add(new MyCustomer(customer, spot, table));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.CustomerAgent)
	 */
	@Override
	public void msgReadyToOrder(Customer customer)
	{
		for(MyCustomer c : customers)
		{
			if(c.customer.equals(customer))
			{
				c.customerState = CustomerState.ReadyToOrder;
			}
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMenu(restaurant.Menu)
	 */
	@Override
	public void msgHereIsMenu(Menu menu)
	{
		this.menu = menu;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsChoice(java.lang.String, restaurant.CustomerAgent)
	 */
	@Override
	public void msgHereIsChoice(String choice, Customer customer)
	{
		for(MyCustomer c : customers)
		{
			if(c.customer.equals(customer))
			{
				orders.add(new Order(choice, c.table, c.customer));
				c.customerState = CustomerState.WaitingForFood;
				break;
			}
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderDone(java.lang.String, int)
	 */
	@Override
	public void msgOrderDone(String choice, int table)
	{
		for(Order o : orders)
		{
			if(o.choice == choice && o.table == table)
			{
				o.state = OrderState.Done;
				gui.setCookingDone(choice, o.customer);
				stateChanged();
				return;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDoneEating(restaurant.CustomerAgent)
	 */
	@Override
	public void msgDoneEating(Customer customer)
	{
		for(MyCustomer c : customers)
		{
			if(c.customer.equals(customer))
			{
				c.customerState = CustomerState.Done;
				break;
			}
		}
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAskForSomethingElse(java.lang.String, int)
	 */
	@Override
	public void msgAskForSomethingElse(String choice, int table)
	{
		menu.remove(choice);
		for(Order o : orders)
		{
			if(o.choice == choice && o.table == table)
			{
				o.state = OrderState.Reject;
				break;
			}
		}
		stateChanged();
	}
	
	public void msgAnimationDone()
	{
		actionDone.release();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgWantBreak()
	 */
	@Override
	public void msgWantBreak()
	{
		breakBox.setEnabled(false);
		breakStatus = BreakStatus.WantBreak;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgGoOnBreak()
	 */
	@Override
	public void msgGoOnBreak()
	{
		breakStatus = BreakStatus.BreakOK;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsCheck(restaurant.CustomerAgent, double)
	 */
	@Override
	public void msgHereIsCheck(Customer customer, double price)
	{
		checks.add(new Check(customer, price));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBackFromBreak()
	 */
	@Override
	public void msgBackFromBreak()
	{
		breakStatus = breakStatus.Back;
		stateChanged();
	}
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction()
	{
		//Come back from break
		if(breakStatus == breakStatus.Back)
		{
			breakStatus = breakStatus.None;
			GoBackToWork();
			return true;
		}
		
		//Go on break if allowed
		try
		{
			if(customers.isEmpty() && breakStatus == BreakStatus.BreakOK)
			{
				breakStatus = BreakStatus.OnBreak;
				GoOnBreak();
				return true;
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//See if I can go on break (if I want to)
		if(breakStatus == BreakStatus.WantBreak)
		{
			breakStatus = BreakStatus.AskedForBreak;
			AskForBreak();
			return true;
		}
		
		//Get rid of customers who are done eating
		try
		{
			for(MyCustomer c : customers)
			{
				if(c.customerState == CustomerState.Gone)
				{
					ClearTable(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		try
		{
			for(MyCustomer c : customers)
			{
				if(c.customerState == CustomerState.Done)
				{
					c.customerState = CustomerState.Done;
					DeliverCheck(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//Deliver finished orders to customers
		try
		{
			for(Order o : orders)
			{
				if(o.state == OrderState.Done)
				{
					DeliverOrder(o);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//Retake rejected orders
		try
		{
			for(Order o : orders)
			{
				if(o.state == OrderState.Reject)
				{
					RetakeOrder(o);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//Tend to customers who want to order
		try
		{
			for(MyCustomer c : customers)
			{
				if(c.customerState == CustomerState.ReadyToOrder)
				{
					c.customerState = CustomerState.Ordering;
					GoToCustomer(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//Seat waiting customers
		try
		{
			for(MyCustomer c : customers)
			{
				if(c.customerState == CustomerState.WaitingForSeat)
				{
					c.customerState = CustomerState.Seated;
					SeatCustomer(c);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		//Give orders to chef
		try
		{
			for(Order o : orders)
			{
				if(o.state == OrderState.OrderTaken)
				{
					o.state = OrderState.Cooking;
					GiveOrderToCook(o);
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e)
		{
			return true;
		}
		
		gui.DefaultAction();
		
		return false;
	}
	
	//ACTIONS
	private void SeatCustomer(MyCustomer customer)
	{
		gui.DoGoToSpot(customer.waitingSpot); //return to starting position to seat customer
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		System.out.println(name + ": Follow me to table " + customer.table);
		customer.customer.msgFollowMe(this, customer.table, new Menu(menu));
		host.msgFreeSpot(customer.waitingSpot);
		
		gui.GoToTable(customer.table);
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
	}
	
	private void GoToCustomer(MyCustomer customer)
	{
		gui.GoToTable(customer.table);
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		System.out.println(name + ": What would you like?");
		customer.customer.msgWhatDoYouWant();
	}
	
	private void GiveOrderToCook(Order order)
	{
		System.out.println(name + ": Giving order to chef");
		
		gui.DoGoToCook();
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		gui.setCooking(order.choice, order.customer);
		
		cooks.get(0).msgHereIsOrder(order.choice, order.table, this);
	}
	
	private void RetakeOrder(Order order)
	{
		gui.GoToTable(order.table);
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		System.out.println(name + ": Please choose something else");
		orders.remove(order);
		order.customer.msgPleaseChooseSomethingElse(new Menu(menu));
	}
	
	private void DeliverOrder(Order order)
	{
		//animation here
		
		gui.DoGoToCook();
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		gui.setOrder(order.choice, order.customer);
		
		gui.GoToTable(order.table);
		try
		{
		actionDone.acquire();
		}
		catch(Exception e){}
		
		System.out.println(name + ": Here is your food");
		order.customer.msgHereIsFood();
		orders.remove(order);
		gui.clearOrder(order.choice, order.customer);
		
		System.out.println(name + ": Getting check from cashier");
		cashier.msgProduceCheck(this, order.customer, order.choice);
	}
	
	private void AskForBreak()
	{
		host.msgIWantBreak(this);
	}
	
	private void DeliverCheck(MyCustomer c)
	{
		for(Check check : checks)
		{
			if(check.customer == c.customer)
			{
				checks.remove(check);
				c.customer.msgHereIsCheck(check.balance);
				System.out.println(name + ": Here is your check");
				ClearTable(c);
				return;
			}
		}
	}
	
	private void ClearTable(MyCustomer c)
	{
		customers.remove(c);
		host.msgTableEmpty(c.table);
		System.out.println(name + ": Table number " + c.table + " is empty.");
	}
	
	private void GoOnBreak()
	{
		breakBox.setEnabled(true);
		breakBox.setSelected(false);
		breakBox.setText("Back to Work");
		System.out.println(name + ": Going on break.");
	}
	
	private void GoBackToWork()
	{
		breakBox.setText("Want Break");
		breakBox.setSelected(false);
		host.msgBackFromBreak(this);
	}
}