package restaurantSM.utils;

import restaurantSM.SMCustomerRole;
import restaurantSM.SMWaiterRole;
import restaurantSM.interfaces.Customer;

public class Order {
	public Order(String ch){
		choice = ch;
	}
	Customer c;
	String choice;
	SMWaiterRole w;
	public enum OrderStatus {Received, OutOfStock, Cooking, Cooked};
	public OrderStatus orderStatus;
	
	public Customer getCustomer() {
		return c;
	}
	
	public void setCustomer(Customer customer) {
		c = customer;
	}
	
	public String getChoice(){
		return choice;
	}
	
	public void setWaiter(SMWaiterRole waiter) {
		w = waiter;
	}
	
	public SMWaiterRole getWaiter() {
		return w;
	}
	
}
