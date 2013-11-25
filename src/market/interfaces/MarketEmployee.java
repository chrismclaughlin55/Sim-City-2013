package market.interfaces;

import java.util.List;

import market.MyOrder;

public interface MarketEmployee {
	
	public abstract void msgHereAreMyOrders(List<MyOrder> orders, MarketCustomer cust);
	//public abstract void msgHereIsAnOrder(String type, int quantity, MarketCustomer cust); 
	public abstract void msgHereIsPayment(double payment);
		

}
