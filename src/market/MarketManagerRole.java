package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import bank.BankManagerRole;
import city.PersonAgent;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager{

	double undepositedMoney;
	boolean endOfDay = false;
	int bankAccountNum;
	BankManagerRole bankManager;
	Inventory inventory;
	Market market = null;

	private List<MarketCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomerRole>());
	private List<MyEmployee> employees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	
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
		// TODO Auto-generated constructor stub
	}

	public void msgReportingForWork(MarketEmployeeRole employee) {
		employees.add(new MyEmployee(employee));
		stateChanged();
	}

	public void msgNeedToOrder(MarketCustomerRole cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgLeavingWork(MarketEmployee employee) {
		employees.remove(employee);
		stateChanged();
	}

	public void msgHereIsMoney(double money, MarketEmployee employee) {
		undepositedMoney += money;
		for (MyEmployee e : employees){
			if (e.employee.equals(employee)) {
				e.isBusy = false;
			}
		}
		stateChanged();
	}



	@Override
	public boolean pickAndExecuteAnAction() {

		if ((!employees.isEmpty()) && (!market.isOpen())) {
			market.setOpen(person);
			return true;
		}

		if (employees.isEmpty() && market.isOpen()) {
			market.setClosed(person);
			return true;
		}

		if (!waitingCustomers.isEmpty()) {
			for (MyEmployee e : employees) {
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



}
