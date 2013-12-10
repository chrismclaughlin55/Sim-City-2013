package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import market.MarketManagerRole.MyCookCustomer;
import market.gui.EmployeeGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;
import city.PersonAgent;
import city.Role;


public class MarketEmployeeRole extends Role implements MarketEmployee {

	public enum EmployeeState{nothing, entering, working, processing, doneProcessing, doneProcessingCookOrder, waitingForPayment}
	public EmployeeState state;

	public enum orderState {pending, processing, completed};
	public List<MarketOrder> currentMarketOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());
	public List<Double> payments = Collections.synchronizedList(new ArrayList<Double>());
	public List<Double> restPayments = Collections.synchronizedList(new ArrayList<Double>());
	public List<MarketCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomer>());
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
	private MarketCustomer currentCustomer;
	private MyCookCustomer currentCookCustomer;
	private Market market;

	private Semaphore atDesk = new Semaphore(0, true);
	private Semaphore atStorage = new Semaphore(0, true);
	private Semaphore leaving = new Semaphore(0, true);



	public MarketEmployeeRole(PersonAgent person, MarketManager manager, Inventory inventory, Market market) {
		super(person);
		this.person = person;
		this.manager = manager;
		this.inventory = inventory;
		this.market = market;
		state = EmployeeState.nothing;
	}

	/*public MarketEmployeeRole(TestPerson person, MarketManager manager, Inventory inventory) {
		super(person);
		this.person = person;
		this.manager = manager;
		this.inventory = inventory;
		state = EmployeeState.nothing;
	}*/

	public void msgGoToDesk(int deskNum) {
		print ("Received msgGoToDesk " + deskNum);
		this.deskNum = deskNum;
		state = EmployeeState.entering;
		stateChanged();
	}

	public void msgLeave() {
		workAvailable = false;
		stateChanged();
	}


	public void msgServiceCustomer(MarketCustomer customer) {
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
		for (MyOrder o : orders) {
			MarketOrder marketOrder = new MarketOrder(o.type, o.amount, cust, orderState.pending, "person");
			currentMarketOrders.add(marketOrder);
		}
		print ("Received msgHereAreMyOrders " + currentMarketOrders.size());

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


		if (state == EmployeeState.doneProcessing) {
			currentMarketOrders.get(0).cust.msgOrderFulfullied(invoice, amountDue);
			state = EmployeeState.waitingForPayment;
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
		
		if(person.cityData.hour >= market.CLOSINGTIME && waitingCustomers.isEmpty())
		{
			LeaveRestaurant();
			return true;
		}

		return false;
	}


	private void CallCustomer(MarketCustomer customer) {
		customer.msgWhatIsYourOrder(this);
	}



	public void FulfillOrder() {
		AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Fulfilling customer's order");
		amountDue = 0;
		log.add((new LoggedEvent("Fufilling order")));
		
		gui.AcquireItems(currentMarketOrders);
		try {
			atStorage.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
		print ("HERE");
		
		for (final MarketOrder o : currentMarketOrders) {
			if(o.quantity <= inventory.inventory.get(o.type).amount) {
				o.state = orderState.completed;
				inventory.inventory.get(o.type).amount -= o.quantity;
				inventory.update();
				final double price = o.quantity * inventory.inventory.get(o.type).price;

				if (o.custType.equals("person")) {
					Invoice i = new Invoice(o.type, o.quantity, price*o.quantity);
					invoice.add(i);
					amountDue += price;
					if (invoice.size() == currentMarketOrders.size())
						msgDoneProcessing();
				}
			}
			else {
				if (o.custType.equals("person")){    
					Invoice i = new Invoice(o.type, 0, 0);
					invoice.add(i);
				}
			}
		}
	}


	/*public void FulfillCookOrder(MyCookCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Fulfilling restaurant's order");
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
	}*/



	public void ProcessPayment() {
		gui.GiveItems();
		print ("Received: " + payments.get(0).doubleValue() + " Amount Due: " + amountDue);
		AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Received: " + payments.get(0).doubleValue() + " Amount Due: " + amountDue);
		if (payments.get(0).doubleValue() == amountDue) {
			manager.msgHereIsMoney(payments.get(0), this);
			currentCustomer.msgYouCanLeave();
		}
		else {
			print ("You will face the wrath of Rami");
			AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "You will face the wrath of Rami");
		}
		currentMarketOrders.clear();
		invoice.clear();
		state = EmployeeState.working;
		payments.remove(0);
	}

	public void ProcessRestPayment() {

		print ("Received: " + restPayments.get(0).doubleValue() + " Amount Due: " + amountDue);
		AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Received: " + restPayments.get(0).doubleValue() + " Amount Due: " + amountDue);
		if (restPayments.get(0).doubleValue() == amountDue) {
			manager.msgHereIsMoney(restPayments.get(0), this);
		}
		else {
			print ("Your restaurant will face the wrath of Rami");
			AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Your restaurant will face the wrath of Rami");
		}
		restPayments.remove(0);
	}


	public void setGui (EmployeeGui gui) {
		this.gui = gui;
		state = EmployeeState.nothing;
	}

	public void msgAtDesk() {
		atDesk.release();
		state = EmployeeState.working;
		stateChanged();
	}
	

	public void msgAtStorage() {
		atStorage.release();
		stateChanged();
	}
	
	public void msgLeft() {
		leaving.release();
		stateChanged();
	}

	
	private void LeaveRestaurant() {
		manager.msgLeavingWork(this);
		gui.DoLeaveMarket();
		try{
			leaving.acquire();
		}
		catch(Exception e){}
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();
	}

}

