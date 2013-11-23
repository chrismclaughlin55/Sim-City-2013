package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.interfaces.MarketCustomer;
import city.PersonAgent;
import city.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {

	private enum customerState {waiting, readyToOrder, readyToPay};
	private enum OrderState {pending, fulfilled, unfulfilled};
	private customerState state;
	private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<Invoice>());
	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	private PersonAgent person = null;
	private MarketManagerRole manager = null;
	private MarketEmployeeRole employee = null;
	private int marketNum;
	private double amountDue = 0;

	public class MyOrder {
		String type; 
		int amount;
		int marketNum;
		OrderState orderState;

		public MyOrder(String type, int amount, int marketNum) {
			this.type = type;
			this.amount = amount;
			this.marketNum = marketNum;
			orderState = OrderState.pending;
		}
	}


	public MarketCustomerRole(PersonAgent person, MarketManagerRole manager, int marketNum, List<MyOrder> orders) {
		super(person);
		this.person = person;
		this.manager = manager;
		this.marketNum = marketNum;
		state = customerState.waiting;
		for (MyOrder o : orders) {
			if (o.marketNum == marketNum) {
				this.orders.add(o);
			}
		}
	}

	public void msgWhatIsYourOrder(MarketEmployeeRole employee){
		this.employee = employee;
		state = customerState.readyToOrder;
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

	public void msgOrderFulfullied(List<Invoice> invoice) {
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).type.equals(invoice.get(i).type)) {
				if (orders.get(i).amount == invoice.get(i).amount) {
					orders.get(i).orderState = OrderState.fulfilled;
					amountDue += invoice.get(i).price;
					person.thingsToOrder.remove(orders.get(i));
					// add the stuff to person's inventory
				}
				else if (invoice.get(i).amount == 0) {
					orders.get(i).orderState = OrderState.unfulfilled;
					for (MyOrder o : person.thingsToOrder) {
						if ((orders.get(i).type.equals(o.type)) && (orders.get(i).marketNum == marketNum)) {
							o.marketNum += 1;
						}
					}
				}
			}
		}
		state = customerState.readyToPay;
		stateChanged();
	}

	public void msgOrderUnfulfilled(String type, int amount) {
		for (MyOrder o : orders) {
			if ((o.type.equals(type)) && (o.amount == amount)) {
				o.orderState = OrderState.unfulfilled;
			}
		}
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
		if (state == customerState.waiting) {
			manager.msgNeedToOrder(this);
			return true;
		}

		return false;
	}

	private void Order(){
		//DoGoToEmployee Gui stuff
		employee.msgHereAreMyOrders(orders, this);
	}


	public void PayBill() {
		person.money -= amountDue;
		employee.msgHereIsPayment(amountDue);
		orders.clear();
		invoices.clear();
	}

}
