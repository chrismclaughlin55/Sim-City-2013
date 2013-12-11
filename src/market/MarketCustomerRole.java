package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import market.MyOrder.OrderState;
import market.gui.CustomerGui;
import market.interfaces.MarketCustomer;
import city.PersonAgent;
import city.Role;
import city.TestPerson;

public class MarketCustomerRole extends Role implements MarketCustomer {

	private enum customerState {waiting, waitingForManager, readyToOrder, doneOrdering, readyToPay, donePaying, leaving, left};
	private customerState state;
	private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<Invoice>());
	public List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	private PersonAgent person = null;
	private MarketManagerRole manager = null;
	private MarketEmployeeRole employee = null;
	private double amountDue = 0;
	private CustomerGui gui = null;

	private Semaphore atDesk = new Semaphore(0,true);
	private Semaphore left = new Semaphore(0,true);



	public MarketCustomerRole(PersonAgent person, MarketManagerRole manager, List<MyOrder> orders) {
		super(person);
		print ("Customer Role created");
		this.person = person;
		this.manager = manager;
		state = customerState.waiting;
		this.orders = orders;
	}

	/*public MarketCustomerRole(TestPerson person, MarketManagerRole manager, List<MyOrder> orders) {
		super(person);
		print ("Customer Role created");
		this.person = person;
		this.manager = manager;
		state = customerState.waiting;
		this.orders = orders;
	}*/

	public void msgWhatIsYourOrder(MarketEmployeeRole employee){
		print("Received msgWhatIsYourOrder");
		this.employee = employee;
		state = customerState.readyToOrder;
		stateChanged();
	}

	/*public void msgOrderFulFullied(Invoice invoice) {
		invoices.add(invoice);
		for (MyOrder o : orders) {
			if ((o.type.equals(invoice.type)) && (o.amount == invoice.amount)) {
				o.orderState = OrderState.fulfilled;
			}
		}
		stateChanged();
	} */

	public void msgOrderFulfullied(List<Invoice> invoice, double amountDue) {

		this.amountDue = amountDue;
		print ("Invoice size " + invoice.size() + " orders size " + orders.size() );

		synchronized(person.thingsToOrder)
		{
			synchronized(orders){
				for (MyOrder o : orders) {
					for (Invoice i : invoice) {
						if (o.type.equals(i.type)) {
							if (o.amount == i.amount) {
								o.orderState = OrderState.fulfilled;
								person.inventory.put(o.type, person.inventory.get(o.type) + i.amount);
							}
						}
					}
				}
			}
		}

		print ("I need to pay " + amountDue);
		state = customerState.readyToPay;
		stateChanged();
	}

	public void msgYouCanLeave() {
		state = customerState.leaving;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {

		if (state == customerState.readyToPay) {
			PayBill();
			return true;
		}

		if (state == customerState.readyToOrder) {
			Order();
			return true;
		}

		if (state == customerState.leaving) {
			LeaveMarket();
			return true;
		}

		if (state == customerState.waiting) {
			manager.msgNeedToOrder(this);
			state = customerState.waitingForManager;
			return true;
		}

		return false;
	}

	private void Order(){
		AlertLog.getInstance().logMessage(AlertTag.MARKET_CUSTOMER, this.getName(), "Making order of "+orders);
		gui.DoGoToEmployee(employee);
		try {
			atDesk.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		employee.msgHereAreMyOrders(orders, this);
		state = customerState.doneOrdering;
	}


	private void PayBill() {
		AlertLog.getInstance().logMessage(AlertTag.MARKET_CUSTOMER, this.getName(), "Paying the bill of "+amountDue);
		state = customerState.donePaying;
		//person.cash -= amountDue;
		employee.msgHereIsPayment(amountDue);
		gui.TakeItems();
		orders.clear();
		invoices.clear();
	}

	private void LeaveMarket() {
		AlertLog.getInstance().logMessage(AlertTag.MARKET_CUSTOMER, this.getName(), "Leaving the market");
		state = customerState.left;
		gui.DoLeaveMarket();
		try {
			left.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		person.exitBuilding();
		person.msgDoneWithJob();
		doneWithRole();
	}

	public void setGui (CustomerGui gui) {
		this.gui = gui;
	}


	public CustomerGui getGui() {
		return gui;

	}

	public void msgAtDesk() {
		atDesk.release();
		stateChanged();
	}

	public void msgLeft() {
		left.release();
		stateChanged();
	}


}
