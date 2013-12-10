package restaurantMQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import market.Market;
import restaurantMQ.CookOrder.OrderState;
import restaurantMQ.gui.RestaurantPanel;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Cook;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;
import city.PersonAgent;
import city.Role;

public class MQCookRole extends Role implements Cook
{
	//DATA MEMBERS
	private Map<String, Food> foodMap = Collections.synchronizedMap(new HashMap<String, Food>());
	private List<MarketOrder> marketOrders = new ArrayList<MarketOrder>();
	
	//Only the cook's own thread ever tampers with marketOrders, so this does not need to be synchronized
	private List<Waiter> waiters = new ArrayList<Waiter>();
	private Market market;
	private List<CookOrder> cookOrders;
	Cashier cashier;
	
	public RestaurantPanel restPanel;

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
		
	private List<CookOrder> orders = Collections.synchronizedList(new ArrayList<CookOrder>());
	
	Timer timer;
	Menu menu = new Menu();
	
	public MQCookRole(PersonAgent person) 
	{
		super(person);
	}
	
	public MQCookRole(PersonAgent person, RestaurantPanel rp, List<CookOrder> cookOrders, Market market, Cashier c, Timer timer)
	{
		super(person);
		restPanel = rp;
		this.market = market;
		cashier = c;
		this.timer = timer;
		this.cookOrders = cookOrders;
		foodMap.put("Steak", new Food("Steak", 4, 100, 300, 1));
		foodMap.put("Chicken", new Food("Chicken", 2, 100, 300, 1));
		foodMap.put("Pizza", new Food("Pizza", 3, 100, 300, 1));
		foodMap.put("Salad", new Food("Salad", 1, 100, 300, 1));
	}

	public void addWaiter(Waiter waiter) 
	{
		waiters.add(waiter);
	}

	public void msgOrdersUpdated()
	{
		stateChanged();
	}
	
	public void msgHereIsOrder(String choice, int table, Waiter waiter) 
	{
		orders.add(new CookOrder(choice, table, waiter));
		stateChanged();
	}

	public void msgFoodDone(CookOrder order) {
		order.orderState = CookOrder.OrderState.Done;
		stateChanged();
	}

	public void msgFoodDelivered(String name, int quantity) {
		synchronized(foodMap)
		{
			Food food = foodMap.get(name);
			food.amount += quantity;
			food.ordering = false;
		}
		orderReceived = true;
		stateChanged();
	}

	public boolean pickAndExecuteAnAction() {
		//Check inventory, see if anything needs to be ordered and order it
		synchronized(foodMap)
		{
			if(orderReceived)
				UpdateWaiterMenus();
		}
		
		//Send out finished orders
		synchronized(orders)
		{
			for(CookOrder o : orders)
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
			for(CookOrder o : orders)
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
			for(CookOrder o : orders)
			{
				if(o.orderState == OrderState.Received)
				{
					CookIt(o);
					return true;
				}
			}
		}
		
		//Check for new orders
		synchronized(cookOrders)
		{
			for(CookOrder o : cookOrders)
			{
				orders.add(o);
				cookOrders.remove(o);
				return true;
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
				OrderFoodFromMarket(market);
			}
			
			return true;
		}
				
		//Let waiters know that food has arrived
		if(orderReceived)
		{
			orderReceived = false;
			backupUsed = false;
		}
				
		if(person.cityData.hour >= restPanel.CLOSINGTIME && orders.isEmpty() && cookOrders.isEmpty() && restPanel.activeWaiters() == 0)
		{
			LeaveRestaurant();
			return true;
		}
		
		return false;
	}
	
	private void LeaveRestaurant() {
		person.exitBuilding();
		person.msgFull();
		person.msgDoneWithJob();
		doneWithRole();
		
	}

	//ACTIONS
	private void CookIt(final CookOrder order)
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
		
	private void PlateIt(CookOrder order)
	{
		System.out.println("Cook: Done cooking");
		order.waiter.msgOrderDone(order.choice, order.table);
	}
		
	private void OrderFoodFromMarket(Market market)
	{	
		System.out.print("Cook: Ordering more: ");
		for(MarketOrder o : marketOrders)
		{
			System.out.print(o.getName() + ", ");
		}
		System.out.println();
		if (market.isOpen()) {
			//market.currentManager.msgNeedToOrder(this, marketOrders, cashier);
		}
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

	public void OutOfFoodHack() {
		
	}

	public void OutOfSaladHack() {
		
	}

	public void setHost(Host host) {
		// TODO Auto-generated method stub
		//this.host = host;
	}
}
