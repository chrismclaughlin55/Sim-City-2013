package restaurantSM.utils;

import restaurantSM.CustomerAgent;
import restaurantSM.WaiterAgent;
import restaurantSM.interfaces.Customer;

public class Order {
	public Order(String ch){
		choice = ch;
	}
	Customer c;
	String choice;
	WaiterAgent w;
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
	
	public void setWaiter(WaiterAgent waiter) {
		w = waiter;
	}
	
	public WaiterAgent getWaiter() {
		return w;
	}
	
}
