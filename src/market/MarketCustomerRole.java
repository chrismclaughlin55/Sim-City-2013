package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.Market;
import market.MyOrder.OrderState;
import market.gui.CustomerGui;
import market.gui.EmployeeGui;
import market.interfaces.MarketCustomer;
import city.PersonAgent;
import city.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {

	private enum customerState {waiting, waitingForManager, readyToOrder, readyToPay};
	private customerState state;
	private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<Invoice>());
	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	private PersonAgent person = null;
	private MarketManagerRole manager = null;
	private MarketEmployeeRole employee = null;
	private double amountDue = 0;
	private CustomerGui gui = null;
	private boolean guiset = false;


	public MarketCustomerRole(PersonAgent person, MarketManagerRole manager, List<MyOrder> orders) {
		super(person);
		this.person = person;
		this.manager = manager;
		state = customerState.waiting;
		this.orders = orders;
	}

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
			state = customerState.waitingForManager;
			return true;
		}

		return false;
	}

	private void Order(){
		if (guiset){
			print ("the gui has been set");
		}
		else 
			print ("the guy has NOT been set");
		this.gui.DoGoToEmployee(200, 200);
		//employee.msgHereAreMyOrders(orders, this);
	}


	public void PayBill() {
		person.cash -= amountDue;
		employee.msgHereIsPayment(amountDue);
		orders.clear();
		invoices.clear();
	}
	
	public void setGui (CustomerGui gui) {
		print ("CUST GUI SET");
		this.gui = gui;
		guiset = true;
	}
	
	
	public CustomerGui getGui() {
		return gui;
		
	}
	

}
