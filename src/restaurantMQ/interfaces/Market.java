package restaurantMQ.interfaces;

import java.util.List;

import restaurantMQ.MarketOrder;

public interface Market 
{
	public abstract void msgNewOrders(Cook cook, Cashier cashier, List<MarketOrder> marketOrders);
	
	public abstract void msgNewOrder(Cook cook, Cashier cashier, String name, int quantity);
	
	public abstract void msgHereIsPayment(Cashier cashier, double payment);
	
	public abstract String getName();
}
