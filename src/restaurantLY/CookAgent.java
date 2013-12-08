package restaurantLY;

import agent.Agent;
import restaurantLY.gui.*;
import restaurantLY.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.awt.Color;

/**
 * Restaurant Cook Agent
 */

public class CookAgent extends Agent implements Cook {
	private String name;
	
	private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	private List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	private Map<String, Amount> inventory = Collections.synchronizedMap(new TreeMap<String, Amount>());
	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public CookGui cookGui = new CookGui(this);
	
	Timer timer = new Timer();
	
	private Semaphore atCookingArea = new Semaphore(0, true);
	private Semaphore atPlacingArea = new Semaphore(0, true);
	private Semaphore atRefrigerator = new Semaphore(0, true);

	public CookAgent(String name) {
		super();
		
		this.name = name;
		inventory.put("Steak",new Amount(10)); // 10 inventory of steak
		inventory.put("Chicken",new Amount(10)); // 10 inventory of chicken
		inventory.put("Pizza",new Amount(0)); // 0 inventory of pizza
		inventory.put("Salad",new Amount(0)); // 0 inventory of salad
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "Cook " + getName();
	}

	// Messages
	
	public void msgHereIsAnOrder(Waiter waiter, int tableNumber, String choice) {
		synchronized (orders) {
			for (Order o : orders) 
				if (o.tableNumber == tableNumber) {
					o.waiter = waiter;
					o.choice = choice;
					o.state = orderState.pending;
					stateChanged();
					return;
				}
		}
		orders.add(new Order(waiter, tableNumber, choice));
		stateChanged();
	}

	public void msgOrderFromMarket(String order, int amount) {
		synchronized (marketOrders) {
			for (MarketOrder mo : marketOrders) {
				if (mo.choice.equals(order) && mo.state == marketOrderState.ordered) {
					mo.amount = amount;
					mo.state = marketOrderState.got;
				}
			}
		}
		stateChanged();
	}
	
	public void msgAtCookingArea() {//from animation
		//print("msgAtCookingArea() called");
		atCookingArea.release();// = true;
		stateChanged();
	}
	
	public void msgAtPlacingArea() {//from animation
		//print("msgAtPlacingArea() called");
		atPlacingArea.release();// = true;
		stateChanged();
	}
	
	public void msgAtRefrigerator() {//from animation
		//print("msgAtRefrigerator() called");
		atRefrigerator.release();// = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		Order order = null;
		synchronized (orders) {
			order = null;
			for (Order o : orders) {
				if (inventory.get(o.choice).amount <= 0 && o.state == orderState.pending) {
					order = o;
					break;
				}
			}
		}
		if (order != null) {
			runOutOfFood(order);
			orderFromMarket(order);
			return true;
		}
		
		synchronized (orders) {
			order = null;
			for (Order o : orders) {
				if (o.state == orderState.pending) {
					order = o;
					break;
				}
			}
		}
		if (order != null) {
			cookOrder(order);
			return true;
		}
		
		synchronized (orders) {
			order = null;
			for (Order o : orders) {
				if (o.state == orderState.done) {
					order = o;
					break;
				}
			}
		}
		if (order != null) {
			placeOrder(order);
			return true;
		}
		
		return false;
		//we have tried all our rules (in this case only one) and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void cookOrder(Order order) {
		DoCookOrder(order);
		inventory.get(order.choice).amount--;
		print("Having " + inventory.get(order.choice).amount + " " + order.choice + " now");
		order.state = orderState.cooking;
		stateChanged();
	}

	private void placeOrder(Order order) {
		DoPlaceOrder(order);
		order.waiter.msgOrderIsReady(order.tableNumber);
		orders.remove(order);
		stateChanged();
	}
	
	private void orderFromMarket(Order order) {
		Market market = new MarketAgent("");
		int orderAmount = 0;
		int orderLeft = 10;
		while (orderLeft > 0) {
			synchronized (markets) {
				for (Market m : markets) {
					print("Asking " + m.getName());
					orderAmount = m.checkInventory(order.choice);
					orderLeft -= orderAmount;
					if (orderAmount > 0) {
						market = m;
						break;
					}
					else {
						print(m.getName() + " running out of " + order.choice);
					}
				}
			}
			final MarketOrder mo = new MarketOrder(market, order.choice, orderAmount, marketOrderState.waiting);
			print("Ordering " + mo.choice + " from " + mo.market.getName());
			marketOrders.add(mo);
			mo.market.msgGetOrderFromCook(this, mo.choice, mo.amount);
			mo.state = marketOrderState.ordered;
			timer.schedule(new TimerTask() {
				public void run() {
					addInventory(mo);
				}
			}, 5000);
		}
		stateChanged();
	}

	private void addInventory(MarketOrder mo) {
		if (mo.amount == 0) {
			markets.remove(mo.market);
			mo.state = marketOrderState.done;
		}
		else {
			inventory.get(mo.choice).amount = inventory.get(mo.choice).amount + mo.amount;
			print("Adding " + mo.choice + " to inventory");
			mo.state = marketOrderState.done;
		}
		stateChanged();
	}

	private void runOutOfFood(Order o) {
		print("Running out of " + o.choice);
		o.waiter.msgCookRunOutOfFood(o.tableNumber);
		o.state = orderState.waiting;
		stateChanged();
	}
	
	private void DoCookOrder(final Order order){
		print("Cooking " + order.choice + " for table " + (order.tableNumber+1));
		//cookGui.placeFood(order.choice, true);
		cookGui.DoGoToRefrigerator();
		atRefrigerator.drainPermits();
		try {
			atRefrigerator.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.placeFood(order.choice, true);
		cookGui.DoGoToCookingArea();
		atCookingArea.drainPermits();
		try {
			atCookingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask(){
			public void run(){
				order.state = orderState.done;
				stateChanged();
			}
		}, 5000);
	}
	
	public void DoPlaceOrder(Order order){
		print("Placing " + order.choice + " for table " + (order.tableNumber+1));
		//cookGui.placeFood(order.choice, false);
		cookGui.DoGoToPlacingArea();
		atPlacingArea.drainPermits();
		try {
			atPlacingArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cookGui.DoGoToCookingArea();
	}
	
	public void addMarket(Market market) {
		markets.add(market);
	}
	
	public void setGui(CookGui gui) {
		cookGui = gui;
	}
	
	public CookGui getGui() {
		return cookGui;
	}
	
	private class Amount {
		int amount;

		Amount(int amount) {
			this.amount = amount;
		}
	}
	
	private class Order {
		Waiter waiter;
		int tableNumber;
		String choice;
		orderState state;
		
		Order(Waiter waiter, int tableNumber, String choice) {
			this.waiter = waiter;
			this.choice = choice;
			this.tableNumber = tableNumber;
			this.state = orderState.pending;
		}
	}
	public enum orderState {pending, waiting, cooking, done};
	
	private class MarketOrder {
		Market market;
		String choice;
		int amount;
		marketOrderState state;
		
		MarketOrder(Market market, String choice, int amount, marketOrderState state) {
			this.market = market;
			this.choice = choice;
			this.amount = amount;
			this.state = state;
		}
	}
	public enum marketOrderState {waiting, ordered, got, done}
}
