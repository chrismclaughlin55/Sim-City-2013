package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import market.test.mock.MockMarketManager;
import city.PersonAgent;
import city.Role;


public class MarketEmployeeRole extends Role implements MarketEmployee {

	private enum orderState {pending, processing, completed};
	public List<MarketOrder> currentMarketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());
	public List<Double> payments = Collections.synchronizedList(new ArrayList<Double>());
	private List<MarketCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomerRole>());

	//public Map<String,MarketData> inventory = Collections.synchronizedMap(new HashMap<String,MarketData>()); 
	public Inventory inventory = null;
	private Timer timer = new Timer();
	private int marketNum;
	private PersonAgent person;
	private MarketManager manager;
	public EventLog log = new EventLog();

	public class MarketOrder {
		String type;
		int quantity;
		MarketCustomer cust;
		//CookRole cookCust
		orderState state;
		String custType;

		public MarketOrder(String type, int quantity, MarketCustomer cust, orderState state, String custType) {
			this.type = type;
			this.quantity = quantity;
			this.cust = cust;
			this.state = state;
			this.custType = custType; 
		}

		/*public MarketOrder(String type, int quantity, CookRole cookCust, orderState state, String custType) {
	        this.type = type;
	        this.quantity = quantity;
	        this.cookCust = cookCust;
	        this.state = state;
	        this.custType = custType;
	    }*/
	}

	public MarketEmployeeRole(PersonAgent person, int marketNum, MarketManager manager, Inventory inventory) {
		super(person);
		this.marketNum = marketNum;
		this.person = person;
		this.manager = manager;
		this.inventory = inventory;
	}


	public void msgServiceCustomer(MarketCustomerRole customer) {
		waitingCustomers.add(customer);
		stateChanged();
	}


	/*public void msgHereIsAnOrder(String type, int quantity, MarketCustomer cust) {
		log.add(new LoggedEvent("Received order from customer"));
		orders.add(new MarketOrder(type, quantity, cust, orderState.pending, "person"));
		stateChanged();
	}*/


	public void msgHereAreMyOrders(List<MyOrder> orders, MarketCustomer cust) {
		for (MyOrder o : orders) {
			MarketOrder marketOrder = new MarketOrder(o.type, o.amount, cust, orderState.pending, "person");
			currentMarketOrders.add(marketOrder);
		}
		stateChanged();
	}

	public void msgHereIsPayment(double payment) {
		payments.add(payment);
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		if (!payments.isEmpty()) {
			ProcessPayment();
			return true;
		}

		if (!waitingCustomers.isEmpty()) {
			CallCustomer(waitingCustomers.get(0));
			waitingCustomers.remove(0);
			return true;
		}

		if (!currentMarketOrders.isEmpty()) {
			FulfillOrder();
			return true;
		}
		/*for (MarketOrder o : marketOrders) {
			if (o.state == orderState.pending) {
				o.state = orderState.processing;
				FulFillOrder(o);
				return true;
			}
		}*/

		return false;
	}

	private void CallCustomer(MarketCustomerRole customer) {
		//employeeGui.callCustomer(c);
		customer.msgWhatIsYourOrder(this);
	}


	public void ProcessPayment() {
		manager.msgHereIsMoney(payments.get(0), this);
		payments.remove(0);
	}


	/*public void FulFillOrder(final MarketOrder order) {
		if(order.quantity <= inventory.inventory.get(order.type).amount) {
			order.state = orderState.completed;
			inventory.inventory.get(order.type).amount -= order.quantity;
			final double price = order.quantity * inventory.inventory.get(order.type).price;
			timer.schedule(new TimerTask() {
				public void run() {  
					if (order.custType.equals("person")) {
						order.cust.msgOrderFulFullied(new Invoice(order.type, order.quantity, price*order.quantity, marketNum));
					}
				}},
				10000);//how long to wait before running task
		}
		else {
			if (order.custType.equals("person")){    
				order.cust.msgOrderUnfulfilled(order.type, order.quantity);
				marketOrders.remove(order);
			}
			if order.custType.equals("restaurant") {    
	            order.cookCust.msgOrderUnfulfilled(order.type, order.amount);
	            orders.remove(orders);
	        }
		}

	}*/

	public void FulfillOrder() {
		for (final MarketOrder o : currentMarketOrders) {
			if(o.quantity <= inventory.inventory.get(o.type).amount) {
				o.state = orderState.completed;
				inventory.inventory.get(o.type).amount -= o.quantity;
				final double price = o.quantity * inventory.inventory.get(o.type).price;
				timer.schedule(new TimerTask() {
					public void run() {  
						if (o.custType.equals("person")) {
							Invoice i = new Invoice(o.type, o.quantity, price*o.quantity, marketNum);
							invoice.add(i);
						}
					}},
					500*currentMarketOrders.size());//how long to wait before running task
			}
			else {
				if (o.custType.equals("person")){    
					Invoice i = new Invoice(o.type, 0, 0, marketNum);
					invoice.add(i);
				}
			}
		}
		
		currentMarketOrders.get(0).cust.msgOrderFulfullied(invoice);
		currentMarketOrders.clear();

	}
	
}

