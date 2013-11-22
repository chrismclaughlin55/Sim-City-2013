package restaurantMQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Cook;
import restaurantMQ.interfaces.Market;
import agent.Agent;

public class MarketAgent extends Agent implements Market
{
	private String name;
	private Map<String, Food> foodMap = new HashMap<String, Food>();
	
	private class Food
	{
		String name;
		int amount;
		int processingTime;
		int capacity;
		double price;
		
		Food(String name, int processingTime, int amount, int capacity, double price)
		{
			this.name = name;
			this.amount = amount;
			this.processingTime = processingTime;
			this.capacity = capacity;
			this.price = price;
		}
	}
	
	private class Order
	{
		String choice;
		Cook cook;
		Cashier cashier;
		int amount;
		
		Order(Cook cook, Cashier cashier, String choice, int amount)
		{
			this.choice = choice;
			this.cook = cook;
			this.cashier = cashier;
			this.amount = amount;
		}
	}
	
	private class Payment
	{
		Cashier cashier;
		double payment;
		
		Payment(Cashier cashier, double payment)
		{
			this.cashier = cashier;
			this.payment = payment;
		}
	}
	
	private List<Payment> payments = Collections.synchronizedList(new ArrayList<Payment>());
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	
	private Map<Cashier, Double> bills = Collections.synchronizedMap(new HashMap<Cashier, Double>());
	
	Timer timer;
	
	//CONSTRUCTORS
	public MarketAgent()
	{
		foodMap.put("Steak", new Food("Steak", 10, 100, 300, 15.99));
		foodMap.put("Chicken", new Food("Chicken", 10, 100, 300, 10.99));
		foodMap.put("Pizza", new Food("Pizza", 10, 100, 300, 8.99));
		foodMap.put("Salad", new Food("Salad", 10, 100, 300, 5.99));
	}

	public MarketAgent(String name, Timer timer, int c)
	{
		this.name = name;
		this.timer = timer;
		
		switch(c)
		{
		case 0:
			foodMap.put("Steak", new Food("Steak", 1, 1, 300, 15.99));
			foodMap.put("Chicken", new Food("Chicken", 1, 1, 300, 10.99));
			foodMap.put("Pizza", new Food("Pizza", 1, 1, 300, 8.99));
			foodMap.put("Salad", new Food("Salad", 1, 1, 300, 5.99));
			break;
		default:
			foodMap.put("Steak", new Food("Steak", 10, 100, 300, 15.99));
			foodMap.put("Chicken", new Food("Chicken", 10, 100, 300, 10.99));
			foodMap.put("Pizza", new Food("Pizza", 10, 100, 300, 8.99));
			foodMap.put("Salad", new Food("Salad", 10, 100, 300, 5.99));
		}
	}
	
	//MESSAGES
	public void msgNewOrders(Cook cook, Cashier cashier, List<MarketOrder> marketOrders)
	{
		synchronized(orders)
		{
			for(MarketOrder o : marketOrders)
			{
				orders.add(new Order(cook, cashier, o.getName(), o.getAmount()));
			}
		}
		stateChanged();
	}
	
	public void msgNewOrder(Cook cook, Cashier cashier, String name, int quantity)
	{
		orders.add(new Order(cook, cashier, name, quantity));
		stateChanged();
	}
	
	public void msgHereIsPayment(Cashier cashier, double payment)
	{
		payments.add(new Payment(cashier, payment));
		stateChanged();
	}
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction()
	{
		//Process orders if there are any
		synchronized(orders)
		{
			for(Order o : orders)
			{
				orders.remove(o);
				processOrder(o);
				return true;
			}
		}
		
		synchronized(payments)
		{
			for(Payment p : payments)
			{
				payments.remove(p);
				processPayment(p);
				return true;
			}
		}
		
		return false;
	}
	
	//ACTIONS
	private void processOrder(final Order o)
	{
		System.out.println(name + ": Processing order for: " + o.choice);
		Food food = foodMap.get(o.choice);
		final double price = food.price;
		final int amount;
		final Market m = this;
		
		if(food.amount < o.amount)
		{
			amount = food.amount;
		}
		else if(o.amount <= 0)
		{
			amount = 0;
		}
		else
		{
			amount = o.amount;
		}
		
		food.amount -= amount;
		
		
		//MAKE THIS THREAD-SAFE
		
		timer.schedule(new TimerTask() {
				public void run()
				{
					synchronized(bills)
					{
						if(bills.containsKey(o.cashier))
						{
							bills.put(o.cashier, bills.get(o.cashier) + price*amount);
						}
						else
						{
							bills.put(o.cashier, price*amount);
						}
					}
					System.out.println(name + ": " + amount + " " + o.choice + " has arrived.");
					o.cook.msgFoodDelivered(o.choice, amount);
					o.cashier.msgHereIsBill(m, price*amount);
				}
			}, foodMap.get(o.choice).processingTime*1000);
	}
	
	private void processPayment(Payment p)
	{
		synchronized(bills)
		{
			double balance = bills.get(p.cashier);
			bills.put(p.cashier, round(balance - p.payment));
			System.out.println(name + ": Payment recieved. Remaining balance is $" + bills.get(p.cashier));
		}
	}
	
	//hacks
	private double round(double d)
	{
		d *= 1000;
		int n = (int)d;
		if(n % 10 < 5)
		{
			n /= 10;
		}
		else
		{
			n = (n/10) + 1;
		}
		return ((double)n)/100;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void OutOfFoodHack()
	{
		foodMap.put("Steak", new Food("Steak", 10, 100, 300, 15.99));
		foodMap.put("Chicken", new Food("Chicken", 10, 100, 300, 10.99));
		foodMap.put("Pizza", new Food("Pizza", 10, 100, 300, 8.99));
		foodMap.put("Salad", new Food("Salad", 10, 100, 300, 5.99));
	}
}