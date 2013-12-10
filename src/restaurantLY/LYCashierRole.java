package restaurantLY;

import agent.Agent;
import city.PersonAgent;
import city.Role;
import restaurantLY.gui.RestaurantPanel;
import restaurantLY.interfaces.*;
import restaurantLY.test.mock.EventLog;
import restaurantLY.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;

import java.util.*;
import java.math.BigDecimal;

import market.Market;
import market.MarketEmployeeRole;
import city.PersonAgent;

// no gui animation for cashier

/**
 * Restaurant Cashier Agent
 */

public class LYCashierRole extends Role implements Cashier {
	public List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	public List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());
	String name;
	Menu menu;
	double money;
	double debt;
	boolean oweMoney;
    public RestaurantPanel restPanel;
	
	Timer timer = new Timer();
	
	public EventLog log = new EventLog();
	
	public LYCashierRole(PersonAgent person, double money, RestaurantPanel rp) {
		super(person);
		restPanel = rp;
		this.name = super.getName();
		this.menu = new Menu();
		this.money = money;
		//this.debt = 0.0;
		this.oweMoney = false;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		return "Cashier " + getName();
	}
	
	// Messages
	
	public void msgCreateCheck(Waiter waiter, Customer customer, String choice) {
		Check check = new Check(menu.choices.get(choice), checkState.ready);
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.check = check;
					stateChanged();
					return;
				}
			}
		}
		customers.add(new myCustomer(customer, waiter, check));
		stateChanged();
	}

	public void msgHereIsMoney(Customer customer, double money) {
		synchronized (customers) {
			for (myCustomer mc : customers) {
				if (mc.customer.equals(customer)) {
					mc.check.state = checkState.gotMoney;
					mc.money = money + mc.debt;
				}
			}
		}
		stateChanged();
	}
	
	public void msgGetBillFromMarket(Market market, double price, MarketEmployeeRole marketEmployee) {
		markets.add(new myMarket(market, marketCheckState.ready, price, marketEmployee));
		//bills.add(new Bill(marketEmployee, price));
		//log.add(new LoggedEvent("Received bill."));
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		myCustomer mc = null;
		synchronized (customers) {
			mc = null;
			for (myCustomer c : customers) {
				if (c.check.state == checkState.ready) {
					mc = c;
					break;
				}
			}
		}
		if (mc != null) {
			createCheck(mc);
			return true;
		}
		
		synchronized (customers) {
			mc = null;
			for (myCustomer c : customers) {
				if (c.check.state == checkState.gotMoney) {
					mc = c;
					break;
				}
			}
		}
		if (mc != null) {
			giveChangeToCustomer(mc);
			return true;
		}
		
		/*Bill temp = null;
		synchronized (bills) {
			temp = null;
			for (Bill b : bills) {
				temp = b;
				break;
			}
		}
		if (temp != null) {
			payToMarket(temp);
			return true;
		}*/
		myMarket temp = null;
		synchronized (markets) {
			temp = null;
			for (myMarket mm : markets) {
				if (mm.state == marketCheckState.ready) {
					temp = mm;
					break;
				}
			}
		}
		if (temp != null) {
			payToMarket(temp);
			return true;
		}
		
		if(person.cityData.hour >= restPanel.CLOSINGTIME && !restPanel.isOpen() 
				&& restPanel.justCashier())
		{
			LeaveRestaurant();
			return true;
		}
		
		return false;
	}
    
    private void LeaveRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Leaving the restaurant");
        restPanel.cashierLeaving();
        person.msgDoneWithJob();
        person.exitBuilding();
        doneWithRole();
    }
	
	// Actions
	
	private void createCheck(myCustomer mc) {
		print("Creating check for " + mc.customer.getName());
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Creating check for " + mc.customer.getName());
		mc.waiter.msgAskForCheck(mc.customer, mc.check.price);
		mc.check.state = checkState.finished;
		stateChanged();
	}

	private void giveChangeToCustomer(myCustomer mc) {
		log.add(new LoggedEvent("Receiving payment from " + mc.customer.getName() + " of $" + mc.money));
		if (mc.debt == 0) {
			print("Customer " + mc.customer.getName() + " paying $" + mc.money);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Receiving payment from " + mc.customer.getName() + " of $" + mc.money);
			mc.oweMoney = false;
		}
		else {
			print("Customer " + mc.customer.getName() + " owed $" + mc.debt + " last time");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Customer " + mc.customer.getName() + " owed $" + mc.debt + " last time");
			mc.oweMoney = true;
		}
		if (mc.money - mc.check.price >= 0 && !mc.oweMoney) {
			mc.customer.msgHereIsChange(mc.money - mc.check.price);
			this.money += mc.check.price;
		}
		else {
			
			if (mc.debt == 0) {
				mc.debt = mc.check.price - mc.money;
				this.money += mc.money;
			}
			else {
				mc.debt = mc.debt + mc.check.price;
			}
			BigDecimal debtBD = new BigDecimal(mc.debt);
			debtBD = debtBD.setScale(2, BigDecimal.ROUND_HALF_UP);
			log.add(new LoggedEvent(mc.customer.getName() + " doesn't have enough money, owe $" + debtBD + ", pay next time"));
			print("Customer " + mc.customer.getName() + " doesn't have enough money, owe $" + debtBD + ", pay next time");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), mc.customer.getName() + " owed $" + mc.debt + " last time");
			mc.debt = debtBD.doubleValue();
			mc.customer.msgOweMoney();
			mc.oweMoney = true;
		}
		mc.check.state = checkState.done;
		stateChanged();
		BigDecimal moneyBD = new BigDecimal(this.money);
		moneyBD = moneyBD.setScale(2, BigDecimal.ROUND_HALF_UP);
		print("Now restaurant having $" + moneyBD);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Now restaurant having $" + moneyBD);
	}
	
	private void payToMarket(myMarket market) {//Bill bill) {
		/*double payment;
		if(money < bill.bill)
		{
			payment = money;
		}
		else
		{
			payment = bill.bill;
		}
		log.add(new LoggedEvent("Paying $" + payment + " to " + bill.marketEmployee.getName()));
		if (this.debt == 0) {
			this.oweMoney = false;
		}
		else {
			this.oweMoney = true;
		}
		print("Paying $" + payment + " to " + bill.marketEmployee.getName());
		this.money = money - payment;
		bill.marketEmployee.msgHereIsPayment(payment);*/
		BigDecimal moneyBD = new BigDecimal(market.money);
		moneyBD = moneyBD.setScale(2, BigDecimal.ROUND_HALF_UP);
		log.add(new LoggedEvent("Paying $" + moneyBD + " to " + market.market.name));
		if (this.debt == 0) {
			this.oweMoney = false;
		}
		else {
			this.oweMoney = true;
		}
		if (this.money - market.money >= 0 && !this.oweMoney) {
			BigDecimal moneyBD2 = new BigDecimal(market.money);
			moneyBD2 = moneyBD2.setScale(2, BigDecimal.ROUND_HALF_UP);
			print("Paying $" + moneyBD2 + " to " + market.market.name);
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Paying $" + moneyBD2 + " to " + market.market.name);
			market.marketEmployee.msgHereIsPayment(market.money);
			this.money -= market.money;
		}
		else {
			if (this.debt == 0) {
				this.debt = market.money - this.money;
			}
			else {
				this.debt += market.money;
			}
			print("No enough for market, paying $" + this.money + ", owe $" + (market.money - this.money) + ", pay next time");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "No enough for market, paying $" + this.money + ", owe $" + (market.money - this.money) + ", pay next time");
			market.marketEmployee.msgHereIsPayment(this.money);
		}
		market.state = marketCheckState.done;
		stateChanged();
		BigDecimal moneyBD3 = new BigDecimal(this.money);
		moneyBD3 = moneyBD3.setScale(2, BigDecimal.ROUND_HALF_UP);
		print("Now restaurant having $" + moneyBD3);
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTLY_CASHIER, this.getName(), "Now restaurant having $" + moneyBD3);
	}
	
	//utilities
	
	public class myCustomer {
		public Customer customer;
		public Waiter waiter;
		public Check check;
		public double money;
		public double debt;
		public boolean oweMoney;
		
		myCustomer(Customer customer, Waiter waiter, Check check) {
			this.customer = customer;
			this.waiter = waiter;
			this.check = check;
			this.money = 0.0;
			this.debt = 0.0;
			this.oweMoney = false;
		}
	}
	
	public class Check {
		public double price;
		public checkState state;
		public Cashier cashier;
		public Customer customer;
		public int tableNumber;
		
		Check(double price, checkState state) {
			this.price = price;
			this.state = state;
		}
		
		public Check(Cashier cashier, Customer customer, int tableNumber, double price) {
			this.cashier = cashier;
			this.customer = customer;
			this.tableNumber = tableNumber;
			this.price = price;
		}
	}
	public enum checkState {ready, finished, gotMoney, done};
	
	public class myMarket {
		public Market market;
		public double money;
		public marketCheckState state;
		MarketEmployeeRole marketEmployee;
		
		myMarket(Market market, marketCheckState state, double money, MarketEmployeeRole marketEmployee) {
			this.market = market;
			this.state = state;
			this.money = money;
			this.marketEmployee = marketEmployee;
		}
	}
	public enum marketCheckState {ready, done}
	
	public void startThread() {
		
	}
}

