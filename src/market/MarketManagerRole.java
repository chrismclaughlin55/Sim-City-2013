package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.ManagerGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import restaurantMQ.MQCookRole;
import restaurantMQ.MarketOrder;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;
import bank.BankManagerRole;
import city.PersonAgent;
import city.Role;
import city.TestPerson;

public class MarketManagerRole extends Role implements MarketManager{
	public enum ManagerState {nothing, entering, setting, managing, working, leaving};
	public ManagerState state;
	double undepositedMoney;
	boolean endOfDay = false;
	int bankAccountNum;
	BankManagerRole bankManager;
	Inventory inventory;
	Market market = null;
	private ManagerGui gui = null;

	public EventLog log = new EventLog();

	public List<MarketCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomer>());
	public List<MyEmployee> waitingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	public List<MyEmployee> workingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	private List<MyCookCustomer> waitingCookCustomers = Collections.synchronizedList(new ArrayList<MyCookCustomer>());

	private Semaphore atHome = new Semaphore(0, true);
	private Semaphore leaving = new Semaphore(0, true);


	private class MyEmployee {
		public MarketEmployee employee;
		public boolean isBusy;

		public MyEmployee(MarketEmployee employee) {
			this.employee = employee;
			isBusy = false;
		}
	}

	public class MyCookCustomer {
		public MQCookRole cook;
		public Cashier cashier;
		public List<MarketOrder> order;

		public MyCookCustomer(MQCookRole cook, List<MarketOrder> order, Cashier cashier) {
			this.cook = cook;
			this.order = order;
			this.cashier = cashier;
		}
	}


	public MarketManagerRole(PersonAgent person, Inventory inventory, Market market) {
		super(person);
		this.inventory = inventory;
		this.market = market;
		state = ManagerState.nothing;
	}

	/*public MarketManagerRole(TestPerson person, Inventory inventory, Market market) {
		super(person);
		this.inventory = inventory;
		this.market = market;
		state = ManagerState.nothing;
	}*/

	public void msgReportingForWork(MarketEmployee employee) {
		print("Received msgReportingForWork"); 
		waitingEmployees.add(new MyEmployee(employee));
		stateChanged();
	}

	public void msgNeedToOrder(MarketCustomer cust) {
		print("Received msgNeedToOrder from customer"); 
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgNeedToOrder(MQCookRole cook, List<MarketOrder> marketOrders, Cashier cashier) {
		print("Received msgNeedToOrder from cook");
		waitingCookCustomers.add(new MyCookCustomer(cook, marketOrders, cashier));
		stateChanged();
	}

	public void msgLeavingWork(MarketEmployee employee) {	

		for(Iterator<MyEmployee> iter = workingEmployees.iterator(); iter.hasNext(); ) {
			MyEmployee m = iter.next();
			if(employee.equals(employee)) {
				iter.remove();
			}
		}
		workingEmployees.remove(employee);
		print ("NUMBER OF WORKING EMPLOYEES" + workingEmployees.size());
		stateChanged();
	}

	public void msgHereIsMoney(double money, MarketEmployee employee) {
		print ("Received msgHereIsMoney");
		undepositedMoney += money;
		for (MyEmployee e : workingEmployees){
			if (e.employee.equals(employee)) {
				e.isBusy = false;
			}
		}
		stateChanged();
	}



	@Override
	public boolean pickAndExecuteAnAction() {

		if(person.cityData.hour >= market.CLOSINGTIME && market.isOpen())
		{
			print ("CLOSING THE MARKET");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, this.getName(), "Market is closed");
			AlertLog.getInstance().logMessage(AlertTag.MARKET_MANAGER, this.getName(), "CLOSING THE MARKET");
			market.setClosed(person);
			return true;
		}

		/*if(person.cityData.hour >= market.CLOSINGTIME && !market.isOpen()) {
			print ("NUMBER OF EMPLOYEES " + workingEmployees.size());
			return true;
		}*/

		if(person.cityData.hour >= market.CLOSINGTIME && !market.isOpen() && workingEmployees.isEmpty())
		{
			LeaveRestaurant();
			return true;
		}

		if ((!market.isOpenForEmployees) && (state == ManagerState.setting))  {
			market.isOpenForEmployees = true;
			state = ManagerState.managing;
			log.add (new LoggedEvent ("Market open for employees"));
			print ("The market is now open for employees only");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is open for employees only");
			return true;
		}

		if ((!workingEmployees.isEmpty()) && (!market.isOpen()) && (state == ManagerState.managing)) {
			state = ManagerState.working;
			market.setOpen(person);
			log.add (new LoggedEvent ("Market open"));
			print ("The market is now open");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is fully employed");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is open now");
			return true;
		}

		if (workingEmployees.isEmpty() && market.isOpen()) {
			market.setClosed(person);
			return true;
		}

		if (state == ManagerState.entering) {
			gui.GoToRoom();
			state = ManagerState.managing;
			try {
				atHome.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}

		if (!waitingEmployees.isEmpty()) {
			if (workingEmployees.size() < 10) {
				workingEmployees.add(waitingEmployees.get(0));
				waitingEmployees.get(0).employee.msgGoToDesk(workingEmployees.size()-1);
				waitingEmployees.remove(0);
			}
			else {
				waitingEmployees.get(0).employee.msgLeave();
			}
			return true;
		}

		if (!waitingCustomers.isEmpty()) {
			for (MyEmployee e : workingEmployees) {
				if (!e.isBusy) {
					e.isBusy = true;
					e.employee.msgServiceCustomer(waitingCustomers.get(0));
					waitingCustomers.remove(0);
					return true;
				}
			}
		}
		if (!waitingCookCustomers.isEmpty()) {
			for (MyEmployee e : workingEmployees) {
				if (!e.isBusy) {
					e.isBusy = true;
					e.employee.msgServiceCookCustomer(waitingCookCustomers.get(0));
					waitingCustomers.remove(0);
					return true;
				}
			}
		}

		if ((undepositedMoney > 0) && (endOfDay)) {
			DepositMoney();
			return true;
		}
		return false;
	}

	private void LeaveRestaurant() {
		System.out.println("manager leaving");
		AlertLog.getInstance().logMessage(AlertTag.MARKET_MANAGER, this.getName(), "Leaving the market");
		gui.DoLeaveMarket();
		try{
			leaving.acquire();
		}
		catch(Exception e){}
		person.msgDoneWithJob();
		person.exitBuilding();
		doneWithRole();

	}

	private void DepositMoney() {
		//bankManager.msgDepositMoney(bankAccountNum, undepositedMoney);
		endOfDay = false;
	}

	public void setGui (ManagerGui gui) {
		this.gui = gui;
		state = ManagerState.entering;
	}

	public void msgEntered() {
		atHome.release();
		state = ManagerState.setting;
		stateChanged();
	}

	public void msgLeft() {
		leaving.release();
		stateChanged();
	}


}
