package restaurantSM;

import agent.Agent;
import restaurantSM.gui.WaiterGui;
import restaurantSM.interfaces.Customer;
import restaurantSM.interfaces.Waiter;
import restaurantSM.utils.*;
import restaurantSM.utils.Order.OrderStatus;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the WaiterAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class SMWaiterRole extends Role implements Waiter {
	//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private List<MyCustomer> custList = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	private SMHostRole host;
	private String name;
	public Semaphore isMoving = new Semaphore(0,true);
	WaiterGui waiterGui;
	SMCookRole cook;
	SMCashierRole cashier;
	public boolean onBreak = false;
	Menu menu = new Menu();
	Timer timer = new Timer();
	Bill b;
	private List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	
	public void setCook(SMCookRole c) {
		cook = c;
	}
	
	public int getNumCustomers(){
		return custList.size();
	}

	public SMWaiterRole(PersonAgent p) {
		super(p);

		name = p.getName();
		// make some tables
	
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setHost(SMHostRole host) {
		this.host = host;
	}
	
	public void setCashier(SMCashierRole cash){
		cashier = cash;
	}
	
	public void askForBreak() {
		host.msgAskForBreak(this);
	}

	// Messages
	public void msgDoneMoving() {//from animation
		//print("msgAtTable() called");
		isMoving.release();// = true;
	}
	
	public void msgOutOfStock(Menu m, Order o){
		menu = m;
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (o.getCustomer() == cust.getCustomer()){
					cust.CustomerStatus = MyCustomer.CustStatus.ReadyToOrder;
				}
			}
		}
		stateChanged();
	}
	
	public void msgHeresTheBill(Bill b) {
		bills.add(b);
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (b.myCust == cust) {
					cust.CustomerStatus = MyCustomer.CustStatus.BillPrepared;
				}
			}
		}
		stateChanged();
	}
	
	public void msgReadyForCheck(Customer c) {
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.getCustomer() == c) {
					cust.CustomerStatus = MyCustomer.CustStatus.ReadyToPay;
				}
			}
	}
		stateChanged();
	}
	
	public void msgGoOnBreak(){
		onBreak = true;
		stateChanged();
	}
	
	public void msgSeatAtTable(Customer c, Table t){
		MyCustomer cust = new MyCustomer(c, t);
		cust.CustomerStatus = MyCustomer.CustStatus.SeatMe;
		custList.add(cust);
		stateChanged();
	}
	
	public void msgReadyToOrder(Customer c){
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.getCustomer() == c){
					cust.CustomerStatus = MyCustomer.CustStatus.ReadyToOrder;
				}
			}
		}
		stateChanged();
	}
	
	public void msgHeresMyChoice(Customer c, String ch){
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.getCustomer() == c){
					cust.setOrder(new Order(ch));
					cust.CustomerStatus = MyCustomer.CustStatus.Ordered;
				}
			}
		}
		stateChanged();
	}
	
	public void msgOrderIsReady(Order o){
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.getCustomer() == o.getCustomer()){
					cust.CustomerStatus = MyCustomer.CustStatus.OrderReady;
				}
			}
		}
		stateChanged();
	}
	
	public void pauseAllCustomers(){
		synchronized(custList){
			for (MyCustomer cust : custList){
				//cust.getCustomer().pause();
			}
		}
	}
	
	public void msgDoneEating(Customer c){
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.getCustomer() == c){
					cust.CustomerStatus = MyCustomer.CustStatus.Done;
				}
			}
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(custList){
			for (MyCustomer cust : custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.SeatMe){
					SeatCustomer(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust: custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.ReadyToPay){
					TakeChoiceToCashier(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust : custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.ReadyToOrder){
					TakeOrder(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust : custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.BillPrepared){
					GiveBillToCustomer(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust : custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.Ordered){
					OrderToKitchen(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust : custList) {
				if (cust.CustomerStatus == MyCustomer.CustStatus.OrderReady) {
					BringOrder(cust);
					return true;
				}
			}
		}
		synchronized(custList){
			for (MyCustomer cust : custList){
				if (cust.CustomerStatus == MyCustomer.CustStatus.Done){
					ClearTable(cust);
					return true;
				}
			}
		}
			if (onBreak == true && custList.isEmpty()) {
				onBreak = false;
				TakeBreak();
				return true;
			}

		waiterGui.DoLeaveCustomer();
		return false;
		
	}
	

	// Actions

	private void BringOrder(MyCustomer myCust){
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		waiterGui.DoGoToCook();
		try {
			isMoving.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		waiterGui.setStatusText(myCust.getOrder().getChoice());
		waiterGui.DoBringToTable(myCust.getTable());
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.setStatusText("");
		myCust.getCustomer().msgHeresYourOrder();
	}
	
	private void GiveBillToCustomer(MyCustomer myCust) {
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		synchronized(bills){
			for (Bill bill : bills) {
				if (myCust == bill.myCust){
					b = bill;
				}
			}
		}
		waiterGui.DoBringToTable(myCust.getTable());
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCust.getCustomer().msgHeresYourBill(b);
		bills.remove(b);
	}
	
	private void TakeChoiceToCashier(MyCustomer myCust){
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		cashier.msgComputeBill(this, myCust);
	}
	
	private void TakeOrder(MyCustomer myCust){
		waiterGui.DoBringToTable(myCust.getTable());
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		myCust.getCustomer().msgWhatWouldYouLike(menu);
	}
	
	private void OrderToKitchen(MyCustomer myCust){
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		myCust.getOrder().setWaiter(this);
		myCust.getOrder().setCustomer(myCust.getCustomer());
		waiterGui.DoGoToCook();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgHeresAnOrder(myCust.getOrder());
	}
	
	private void ClearTable(MyCustomer myCust){
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		custList.remove(myCust);
		host.msgTableIsClear(myCust.getTable());
	}
	
	private void SeatCustomer(MyCustomer myCust) {
		myCust.CustomerStatus = MyCustomer.CustStatus.none;
		Customer customer = myCust.getCustomer();
		Table table = myCust.getTable();
		SMCustomerRole cust = (SMCustomerRole) myCust.getCustomer();
		waiterGui.DoGoToLine(cust.getGui().getIndex());
		try {
			isMoving.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		customer.msgFollowMe(this, menu, table.tableNumber);
		waiterGui.DoBringToTable(table);
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}
	
	private void TakeBreak(){
		final SMWaiterRole self = this;
		timer.schedule(new TimerTask() {
			public void run() {
				host.msgDoneWithBreak(self);
			}
		}, 5000);
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	
	
}

