package restaurant.interfaces;

import java.util.List;

import restaurant.CookAgent;
import restaurant.MarketOrder;

public interface Market 
{
	public abstract void msgNewOrders(CookAgent cook, Cashier cashier, List<MarketOrder> marketOrders);
	
	public abstract void msgNewOrder(CookAgent cook, Cashier cashier, String name, int quantity);
	
	public abstract void msgHereIsPayment(Cashier cashier, double payment);
	
	public abstract String getName();
}
