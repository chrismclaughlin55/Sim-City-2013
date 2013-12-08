package restaurantLY;

import agent.Agent;
import restaurantLY.interfaces.*;
import restaurantLY.test.mock.EventLog;
import restaurantLY.test.mock.LoggedEvent;

import java.util.*;

/**
 * Restaurant Market Agent
 */

public class MarketAgent extends Agent implements Market {
	String name;
	
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	Map<String, Amount> inventory = Collections.synchronizedMap(new HashMap<String, Amount>());
	
	Cashier cashier;
	
	public EventLog log = new EventLog();
	
	public MarketAgent(String name) {
		super();
		
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "Market " + getName();
	}
	
	// Messages
	
	public void msgGetOrderFromCook(Cook cook, String choice, int amount) {
		print(cook.getName() + " ordering " + choice);
		orders.add(new Order(cook, choice, amount, orderState.got));
		inventory.get(choice).amount = inventory.get(choice).amount - amount;
		stateChanged();
	}
	
	public void msgHereIsMoney(double amount) {
		log.add(new LoggedEvent("Receiving $" + amount + " from cashier"));
		stateChanged();
	}
	
	public void msgNoMoney() {
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
				if (o.state == orderState.got) {
					order = o;
					break;
				}
			}
		}
		if (order != null) {
			deliverOrder(order);			
			return true;
		}
		
		synchronized (orders) {
			order = null;
			for (Order o : orders){
				if (o.state == orderState.sent) {
					order = o;
					break;
				}
			}
		}
		if (order != null) {
			createCheck(order);			
			return true;
		}
		
		return false;
	}
	
	// Actions
	
	private void deliverOrder(Order o) {
		print("Delivering " + o.amount + " " + o.choice);
		//inventory.get(o.choice).amount = inventory.get(o.choice).amount - o.amount;
		o.cook.msgOrderFromMarket(o.choice, o.amount);
		o.state = orderState.sent;
		stateChanged();
	}
	
	private void createCheck(Order o) {
		cashier.msgGetBillFromMarket(this, o.amount * inventory.get(o.choice).price);
		o.state = orderState.paid;
		stateChanged();
	}
	
	public void addInventory(String choice, int amount, double price) {
		inventory.put(choice, new Amount(amount, price));
	}
	
	public int checkInventory(String choice) {
		if (inventory.get(choice).amount <= 0) {
			return 0;
		}
		else if (inventory.get(choice).amount > 10) {
			return 10;
		}
		else {
			return inventory.get(choice).amount;
		}
	}
	
	//utilities
	
	private class Order {
		Cook cook;
		String choice;
		int amount;
		orderState state;
		
		Order(Cook cook, String choice, int amount, orderState state) {
			this.cook = cook;
			this.choice = choice;
			this.amount = amount;
			this.state = state;
		}
	}
	enum orderState {got, sent, paid};
	
	private class Amount {
		int amount;
		double price;
		
		Amount(int amount, double price) {
			this.amount = amount;
			this.price = price;
		}
	}
	
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}
}

