package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.MarketManagerRole.MyCookCustomer;
import market.gui.EmployeeGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import restaurantMQ.MQCashierRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.test.mock.EventLog;
import city.PersonAgent;
import city.Role;


public class MarketEmployeeRole extends Role implements MarketEmployee {

	private enum EmployeeState{nothing, entering, working, processing, doneProcessing, doneProcessingCookOrder, waitingForPayment}
	private EmployeeState state;

	private enum orderState {pending, processing, completed};
	public List<MarketOrder> currentMarketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());
	public List<Double> payments = Collections.synchronizedList(new ArrayList<Double>());
	public List<Double> restPayments = Collections.synchronizedList(new ArrayList<Double>());
	private List<MarketCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomerRole>());
	private List<MyCookCustomer> waitingCookCustomers = Collections.synchronizedList(new ArrayList<MyCookCustomer>());

	public EmployeeGui gui = null;

	//public Map<String,MarketData> inventory = Collections.synchronizedMap(new HashMap<String,MarketData>()); 
	public Inventory inventory = null;
	private Timer timer = new Timer();
	private int marketNum;
	private PersonAgent person;
	private MarketManager manager;
	public EventLog log = new EventLog();
	private boolean workAvailable = true;
	private int deskNum = 0;
	private double amountDue;
	private boolean isProcessed = false;
	private MarketCustomerRole currentCustomer;
	private MyCookCustomer currentCookCustomer;
	private MQCashierRole cashier;


	private Semaphore atDesk = new Semaphore(0, true);




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

	public MarketEmployeeRole(PersonAgent person, MarketManager manager, Inventory inventory) {
		super(person);
		this.person = person;
		this.manager = manager;
		this.inventory = inventory;
		state = EmployeeState.nothing;
	}

	public void msgGoToDesk(int deskNum) {
		print ("Received msgGoToDesk");
		this.deskNum = deskNum;
		state = EmployeeState.entering;
		stateChanged();
	}

	public void msgLeave() {
		workAvailable = false;
		stateChanged();
	}


	public void msgServiceCustomer(MarketCustomerRole customer) {
		print ("Received msgServiceCustomer");
		currentCustomer = customer;
		waitingCustomers.add(customer);
		stateChanged();
	}

	public void msgServiceCookCustomer(MyCookCustomer cook) {
		print ("Received msgServiceCookCustomer");
		currentCookCustomer = cook;
		waitingCookCustomers.add(cook);
		stateChanged();
	}


	public void msgHereAreMyOrders(List<MyOrder> orders, MarketCustomer cust) {
		print ("Received msgHereAreMyOrders " + orders.size());
		for (MyOrder o : orders) {
			print (o.type);
			MarketOrder marketOrder = new MarketOrder(o.type, o.amount, cust, orderState.pending, "person");
			currentMarketOrders.add(marketOrder);
		}
		stateChanged();
	}

	public void msgDoneProcessing() {
		state = EmployeeState.doneProcessing;
		stateChanged();
	}
	
	public void msgDoneProcessingCookOrder() {
		state = EmployeeState.doneProcessingCookOrder;
		stateChanged();
	}

	public void msgHereIsPayment(double payment) {
		print ("Received msgHereIsPayment");
		payments.add(payment);
		stateChanged();
	}
	
	public void msgHereIsRestPayment(double payment) {
		print ("Received msgHereIsPayment from cashier");
		restPayments.add(payment);
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		if (state == EmployeeState.entering) {
			gui.MoveToDesk(deskNum);
			try {
				atDesk.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		
		if (!restPayments.isEmpty()) {
			ProcessRestPayment();
			return true;
		}

		if (!payments.isEmpty()) {
			ProcessPayment();
			return true;
		}
		
		
		
		if (state == EmployeeState.doneProcessingCookOrder) {
			for (final restaurantMQ.MarketOrder o : currentCookCustomer.order) {
				currentCookCustomer.cook.msgFoodDelivered(o.name, o.amount);
				currentCookCustomer.cashier.msgHereIsBill(this, o.amount*inventory.inventory.get(o.name).price);
			}
		}
		
		if (state == EmployeeState.doneProcessing) {
			currentMarketOrders.get(0).cust.msgOrderFulfullied(invoice, amountDue);
			state = EmployeeState.waitingForPayment;
			//currentMarketOrders.clear();
			return true;
		}

		if (!waitingCookCustomers.isEmpty()) {
			FulfillCookOrder(waitingCookCustomers.get(0));
			waitingCustomers.remove(0);
			return true;
		}

		if (!waitingCustomers.isEmpty()) {
			CallCustomer(waitingCustomers.get(0));
			waitingCustomers.remove(0);
			return true;
		}

		

		if ((!currentMarketOrders.isEmpty() && (state == EmployeeState.working))) {
			state = EmployeeState.processing;
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
		customer.msgWhatIsYourOrder(this);
	}



	public void FulfillOrder() {
		amountDue = 0;
		for (final MarketOrder o : currentMarketOrders) {
			//System.out.println (o.quantity + " " + inventory.inventory.get(o.type).amount);
			if(o.quantity <= inventory.inventory.get(o.type).amount) {
				o.state = orderState.completed;
				inventory.inventory.get(o.type).amount -= o.quantity;
				inventory.update();
				final double price = o.quantity * inventory.inventory.get(o.type).price;
				timer.schedule(new TimerTask() {
					public void run() {  
						if (o.custType.equals("person")) {
							Invoice i = new Invoice(o.type, o.quantity, price*o.quantity);
							invoice.add(i);
							amountDue += price;
							if (invoice.size() == currentMarketOrders.size())
								msgDoneProcessing();
						}
					}},
					500);//how long to wait before running task
			}
			else {
				if (o.custType.equals("person")){    
					Invoice i = new Invoice(o.type, 0, 0);
					invoice.add(i);
				}
			}
		}
	}


	public void FulfillCookOrder(MyCookCustomer c) {
		amountDue = 0;
		for (final restaurantMQ.MarketOrder o : c.order) {
			if(o.amount <= inventory.inventory.get(o.name).amount) {
				inventory.inventory.get(o.name).amount -= o.amount;
				inventory.update();
				final double price = o.amount * inventory.inventory.get(o.name).price;
				timer.schedule(new TimerTask() {
					public void run() {  
						Invoice i = new Invoice(o.name, o.amount, price*o.amount);
						invoice.add(i);
						amountDue += price;
						if (invoice.size() == currentMarketOrders.size())
							msgDoneProcessingCookOrder();

					}},
					500);//how long to wait before running task
			}
			else {
				Invoice i = new Invoice(o.name, 0, 0);
				invoice.add(i);
			}
		}
	}



public void ProcessPayment() {

	print ("Received: " + payments.get(0).doubleValue() + " Amount Due: " + amountDue);
	if (payments.get(0).doubleValue() == amountDue) {
		manager.msgHereIsMoney(payments.get(0), this);
		currentCustomer.msgYouCanLeave();
	}
	else {
		print ("You will face the wrath of Rami");
	}
	payments.remove(0);
}

public void ProcessRestPayment() {
	
	print ("Received: " + restPayments.get(0).doubleValue() + " Amount Due: " + amountDue);
	if (restPayments.get(0).doubleValue() == amountDue) {
		manager.msgHereIsMoney(restPayments.get(0), this);
	}
	else {
		print ("Your restaurant will face the wrath of Rami");
	}
	currentMarketOrders.clear();
	restPayments.remove(0);
}


public void setGui (EmployeeGui gui) {
	this.gui = gui;
	state = EmployeeState.entering;
}

public void msgAtDesk() {
	atDesk.release();
	state = EmployeeState.working;
	stateChanged();
}

}

