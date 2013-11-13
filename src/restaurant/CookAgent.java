package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Waiter;

import agent.Agent;

public class CookAgent extends Agent
{
	//DATA MEMBERS
	private Map<String, Food> foodMap = Collections.synchronizedMap(new HashMap<String, Food>());
	private List<MarketOrder> marketOrders = new ArrayList<MarketOrder>();
	//Only the cook's own thread ever tampers with marketOrders, so this does not need to be synchronized
	private List<WaiterAgent> waiters = new ArrayList<WaiterAgent>();
	private List<MarketAgent> markets = new ArrayList<MarketAgent>();
	Cashier cashier;
	
	private boolean orderReceived = false;
	private boolean backupUsed = false;
	
	private class Food
	{
		String name;
		int cookingTime;
		int amount;
		int threshold;
		int capacity;
		boolean ordering = false;
		
		Food(String name, int cookingTime, int amount, int capacity, int threshold)
		{
			this.name = name;
			this.cookingTime = cookingTime;
			this.amount = amount;
			this.capacity = capacity;
			this.threshold = threshold;
		}
	}
	
	private enum OrderState {Received, Cooking, Reject, Done};
	
	private class Order
	{
		String choice;
		int table;
		OrderState orderState = OrderState.Received;
		Waiter waiter;
		
		Order(String choice, int table, Waiter waiter)
		{
			this.choice = choice;
			this.table = table;
			this.waiter = waiter;
		}
	}
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	
	Timer timer;
	Menu menu = new Menu();
	
	//CONSTRUCTORS
	public CookAgent()
	{
		foodMap.put("Steak", new Food("Steak", 4, 0, 3, 1));
		foodMap.put("Chicken", new Food("Chicken", 2, 0, 3, 1));
		foodMap.put("Pizza", new Food("Pizza", 3, 0, 3, 1));
		foodMap.put("Salad", new Food("Salad", 1, 0, 3, 1));
	}
	
	public CookAgent(List<MarketAgent> markets, Cashier c, Timer timer)
	{
		for(MarketAgent m : markets)
		{
			this.markets.add(m);
		}
		cashier = c;
		this.timer = timer;
		foodMap.put("Steak", new Food("Steak", 4, 0, 3, 1));
		foodMap.put("Chicken", new Food("Chicken", 2, 0, 3, 1));
		foodMap.put("Pizza", new Food("Pizza", 3, 0, 3, 1));
		foodMap.put("Salad", new Food("Salad", 1, 0, 3, 1));
	}
	
	public void addWaiter(WaiterAgent waiter)
	{
		waiters.add(waiter);
	}
	
	//MESSAGES
	void msgHereIsOrder(String choice, int table, Waiter waiter)
	{
		orders.add(new Order(choice, table, waiter));
		stateChanged();
	}
	
	void msgFoodDone(Order order)
	{
		order.orderState = OrderState.Done;
		stateChanged();
	}
	
	void msgFoodDelivered(String name, int quantity)
	{
		synchronized(foodMap)
		{
			Food food = foodMap.get(name);
			food.amount += quantity;
			food.ordering = false;
		}
		orderReceived = true;
		stateChanged();
	}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction()
	{
		//Check inventory, see if anything needs to be ordered and order it
		synchronized(foodMap)
		{
			if(orderReceived)
				UpdateWaiterMenus();
		}
		
		//Send out finished orders
			synchronized(orders)
			{
				for(Order o : orders)
				{
					if(o.orderState == OrderState.Done)
					{
						orders.remove(o);
						PlateIt(o);
						return true;
					}
				}
			}
				
				//Send back rejected orders
			synchronized(orders)
			{
				for(Order o : orders)
				{
					if(o.orderState == OrderState.Reject)
					{
						orders.remove(o);
						System.out.println("Cook: Sending order back");
						o.waiter.msgAskForSomethingElse(o.choice, o.table);
						return true;
					}
				}
			}
				
				//Cook pending orders
			synchronized(orders)
			{
				for(Order o : orders)
				{
					if(o.orderState == OrderState.Received)
					{
						CookIt(o);
						return true;
					}
				}
			}
		
			
		synchronized(foodMap)
		{
			CheckInventory(); //populate the list of market orders with foods that are below threshold
		}
		if(!marketOrders.isEmpty())
		{
			if(!orderReceived)
			{
				OrderFoodFromMarket(markets.get(0));
			}
			else
			{
				if(!backupUsed)
				{
					OrderFoodFromMarket(markets.get(1));
				}
				else
				{
					OrderFoodFromMarket(markets.get(2));
				}
			}
			return true;
		}
		
		//Let waiters know that food has arrived
		if(orderReceived)
		{
			orderReceived = false;
			backupUsed = false;
		}
		
		return false;
	}
	
	//ACTIONS
	private void CookIt(final Order order)
	{
		Food food = foodMap.get(order.choice);
		
		//Animation here
		if(food.amount == 0)
		{
			menu.remove(food.name);
			order.orderState = OrderState.Reject;
			System.out.println("Cook: Out of " + food.name);
		}
		else
		{
			order.orderState = OrderState.Cooking;
			System.out.println("Cook: Cooking the " + order.choice);
			timer.schedule(new TimerTask() {
				public void run()
				{
					msgFoodDone(order);
				}
				}, food.cookingTime*1000);
	
		food.amount--;
		}
	}
	
	private void PlateIt(Order order)
	{
		System.out.println("Cook: Done cooking");
		order.waiter.msgOrderDone(order.choice, order.table);
	}
	
	private void OrderFoodFromMarket(MarketAgent market)
	{	
		System.out.print("Cook: Ordering more: ");
		for(MarketOrder o : marketOrders)
		{
			System.out.print(o.getName() + ", ");
		}
		System.out.println();
		market.msgNewOrders(this, cashier, marketOrders);
		marketOrders.clear();
	}
	
	private void CheckInventory()
	{
		Food food = foodMap.get("Steak");
		if(food.amount <= food.threshold && !food.ordering)
		{
			food.ordering = true;
			marketOrders.add(new MarketOrder("Steak", food.capacity - food.amount));
		}
		
		food = foodMap.get("Chicken");
		if(food.amount <= food.threshold && !food.ordering)
		{
			food.ordering = true;
			marketOrders.add(new MarketOrder("Chicken", food.capacity - food.amount));
		}
		
		food = foodMap.get("Pizza");
		if(food.amount <= food.threshold && !food.ordering)
		{
			food.ordering = true;
			marketOrders.add(new MarketOrder("Pizza", food.capacity - food.amount));
		}
		
		food = foodMap.get("Salad");
		if(food.amount <= food.threshold && !food.ordering)
		{
			food.ordering = true;
			marketOrders.add(new MarketOrder("Salad", food.capacity - food.amount));
		}
	}
	
	private void UpdateWaiterMenus()
	{
		Food food = foodMap.get("Steak");
		boolean menuchanged = false;
		if(food.amount > 0 && !menu.has("Steak"))
		{
			menu.add(food.name);
			menuchanged = true;
		}
		
		food = foodMap.get("Chicken");
		if(food.amount > 0 && !menu.has("Chicken"))
		{
			menu.add(food.name);
			menuchanged = true;
		}
		
		food = foodMap.get("Pizza");
		if(food.amount > 0 && !menu.has("Pizza"))
		{
			menu.add(food.name);
			menuchanged = true;
		}
		
		food = foodMap.get("Salad");
		if(food.amount > 0 && !menu.has("Salad"))
		{
			menu.add(food.name);
			menuchanged = true;
		}
		
		if(menuchanged)
		{
			for(Waiter w : waiters)
			{
				w.msgHereIsMenu(new Menu(menu));
			}
		}
	}
	
	//hacks
	public void OutOfFoodHack()
	{
		Food steak = foodMap.get("Steak");
		steak.amount = 0;
	}
	
	public void OutOfSaladHack()
	{
		Food salad = foodMap.get("Salad");
		salad.amount = 0;
	}
}
