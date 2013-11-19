package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.interfaces.MarketCustomer;
import city.PersonAgent;
import city.Role;

public class MarketCustomerRole extends Role implements MarketCustomer {

	
	private List<Invoice> invoices = Collections.synchronizedList(new ArrayList<Invoice>());
	private List<MyOrder> orders = Collections.synchronizedList(new ArrayList<MyOrder>());
	private PersonAgent person = null;
	private MarketEmployeeRole employee = null;
	private int marketNum;
	
	class MyOrder {
	    String type; 
	    int amount;
	    boolean unfulfilled;
	    int marketNum;

	    public MyOrder(String type, int amount, int marketNum) {
	        this.type = type;
	        this.amount = amount;
	        this.marketNum = marketNum;
	    }
	}
	
	
	public MarketCustomerRole(PersonAgent person, MarketEmployeeRole employee, int marketNum) {
		super(person);
		this.person = person;
		this.employee = employee;
		this.marketNum = marketNum;
	}
	
	public void msgOrderFulFullied(Invoice invoice) {
	    invoices.add(invoice);
	    stateChanged();
	}

	public void msgOrderUnfulfilled(String type, int amount) {
		for (MyOrder o : orders) {
			if ((o.type.equals(type)) && (o.amount == amount)) {
				o.unfulfilled = true;
			}
		}
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		while (!invoices.isEmpty()){
		    PayBill();
		    return true;
		}
		return false;
	}
	
	public void OrderItem(String type, int amount) {
	    employee.msgHereIsAnOrder(type, amount, this);
	    orders.add(new MyOrder(type, amount, marketNum));
	}

	public void PayBill() {
		
		for (MyOrder o : orders) {
			if (o.type.equals(invoices.get(0).type) && (o.amount == invoices.get(0).amount) && (o.marketNum == invoices.get(0).marketNum)) {
				person.money -= invoices.get(0).price;
				employee.msgHereIsPayment(invoices.get(0).price);
				orders.remove(o);
				invoices.remove(0);
			}
		}
	}

}
