package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.EmployeeGui;
import market.gui.ManagerGui;
import market.gui.MarketGui;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import bank.BankManagerRole;
import city.PersonAgent;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager{
	private enum ManagerState {nothing, entering, managing, leaving};
	private ManagerState state;
	double undepositedMoney;
	boolean endOfDay = false;
	int bankAccountNum;
	BankManagerRole bankManager;
	Inventory inventory;
	Market market = null;
	private ManagerGui gui = null;


	private List<MarketCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomerRole>());
	private List<MyEmployee> waitingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	private List<MyEmployee> workingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());

	private Semaphore atHome = new Semaphore(0, true);


	private class MyEmployee {
		public MarketEmployeeRole employee;
		public boolean isBusy;

		public MyEmployee(MarketEmployeeRole employee) {
			this.employee = employee;
			isBusy = false;
		}
	}


	public MarketManagerRole(PersonAgent person, Inventory inventory, Market market) {
		super(person);
		this.inventory = inventory;
		this.market = market;
		state = ManagerState.nothing;
	}

	public void msgReportingForWork(MarketEmployeeRole employee) {
		print("Received msgReportingForWork"); 
		waitingEmployees.add(new MyEmployee(employee));
		stateChanged();
	}

	public void msgNeedToOrder(MarketCustomerRole cust) {
		print("Received msgNeedToOrder"); 
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingWork(MarketEmployee employee) {
		workingEmployees.remove(employee);
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

		if ((!workingEmployees.isEmpty()) && (!market.isOpen())) {
			market.setOpen(person);
			print ("The market is now open");
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
				waitingEmployees.get(0).employee.msgGoToDesk(workingEmployees.indexOf(waitingEmployees.get(0)));
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

		if ((undepositedMoney > 0) && (endOfDay)) {
			DepositMoney();
			return true;
		}
		return false;
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
		state = ManagerState.managing;
		stateChanged();
	}



}
