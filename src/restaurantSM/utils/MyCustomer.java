package restaurantSM.utils;

import restaurantSM.utils.Table;
import restaurantSM.utils.Order;
import restaurantSM.interfaces.Customer;
import restaurantSM.*;


public class MyCustomer {
	
	public MyCustomer(Customer cust, Table table){
		c = cust;
		t = table;
	}
	Customer c;
	Table t;
	Order o;
	public enum CustStatus {SeatMe, ReadyToOrder, Ordered, OrderReady, ReadyToPay, BillPrepared, Done, none};
	public CustStatus CustomerStatus;
	
	
	public Customer getCustomer(){
		return c;
	}
	
	public Order getOrder(){
		return o;
	}
	
	public Table getTable(){
		return t;
	}
	
	public void setOrder(Order order) {
		o = order;
	}
}
