package restaurantLY;

import agent.Agent;
import restaurantLY.gui.*;
import restaurantLY.interfaces.*;
import trace.AlertLog;
import trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.awt.Color;

import city.PersonAgent;
import city.Role;
import market.Market;

/**
 * Restaurant Cook Agent
 */

public class LYCookRole extends Role implements Cook {
	private String name;
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	//private List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
    private Market market;
	private Map<String, Amount> inventory = Collections.synchronizedMap(new TreeMap<String, Amount>());
	private List<MarketOrder> marketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	public CookGui cookGui = new CookGui(this);
	
	Timer timer = new Timer();
	
	private Semaphore atCookingArea = new Semaphore(0, true);
	private Semaphore atPlacingArea = new Semaphore(0, true);
	private Semaphore atRefrigerator = new Semaphore(0, true);
	private Semaphore left = new Semaphore(0, true);
    
    private List<Waiter> waiters = new ArrayList<Waiter>();
    public RestaurantPanel restPanel;

    public LYCookRole(PersonAgent person) {
        super(person);
    }
    
	public LYCookRole(PersonAgent person, RestaurantPanel rp, Market market) {
		super(person);
		restPanel = rp;
		this.name = super.getName();
        this.market = market;
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
    
    public void addWaiter(Waiter waiter) {
		waiters.add(waiter);
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
	
	public void msgLeft() {//from animation
        left.release();
        stateChanged();
    }

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
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
		
		if(person.cityData.hour >= restPanel.CLOSINGTIME && orders.isEmpty() && orders.isEmpty() && restPanel.activeWaiters() == 0)
		{
			LeaveRestaurant();
			return true;
		}
		
		return false;
		//we have tried all our rules (in this case only one) and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
    
    private void LeaveRestaurant() {
    	cookGui.DoLeaveRestaurant();
        try {
            left.acquire();
        } catch(Exception e){}
        person.msgFull();
        person.exitBuilding();
        person.msgFull();
        person.msgDoneWithJob();
        doneWithRole();
    }

	// Actions

	private void cookOrder(Order order) {
		DoCookOrder(order);
		inventory.get(order.choice).amount--;
		print("Having " + inventory.get(order.choice).amount + " " + order.choice + " now");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Having " + inventory.get(order.choice).amount + " " + order.choice + " now");
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
		int orderAmount = 10;
		print("Asking " + market.name);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Asking " + market.name);
		if (market.isOpen()) {
			final MarketOrder mo = new MarketOrder(market, order.choice, orderAmount, marketOrderState.waiting);
			print("Ordering " + mo.choice + " from " + mo.market.name);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Ordering " + mo.choice + " from " + mo.market.name);
			marketOrders.add(mo);
			//*****implement later
			//mo.market.currentManager.msgNeedToOrder(cook, marketOrders, cashier);//msgGetOrderFromCook(this, mo.choice, mo.amount);
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
			//markets.remove(mo.market);
			mo.state = marketOrderState.done;
		}
		else {
			inventory.get(mo.choice).amount = inventory.get(mo.choice).amount + mo.amount;
			print("Adding " + mo.choice + " to inventory");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Adding " + mo.choice + " to inventory");
			mo.state = marketOrderState.done;
		}
		stateChanged();
	}

	private void runOutOfFood(Order o) {
		print("Running out of " + o.choice);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Running out of " + o.choice);
		o.waiter.msgCookRunOutOfFood(o.tableNumber);
		o.state = orderState.waiting;
		stateChanged();
	}
	
	private void DoCookOrder(final Order order){
		print("Cooking " + order.choice + " for table " + (order.tableNumber+1));
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Cooking " + order.choice + " for table " + (order.tableNumber+1));
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
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_COOK, this.getName(), "Placing " + order.choice + " for table " + (order.tableNumber+1));
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
	
	/*public void addMarket(Market market) {
		markets.add(market);
	}*/
	
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
	
	public class Order {
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
	
	public void setHost(Host host) {
		//this.host = host;
	}
}
