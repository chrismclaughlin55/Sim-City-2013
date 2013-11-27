package market.interfaces;

import java.util.List;

import market.MarketCustomerRole;
import market.MarketEmployeeRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.MarketOrder;
import restaurantMQ.interfaces.Cashier;

public interface MarketManager {
	
	public abstract void msgReportingForWork(MarketEmployeeRole employee);	
	public abstract void msgNeedToOrder(MarketCustomerRole cust);	
	public abstract void msgNeedToOrder(MQCookRole cook, List<MarketOrder> marketOrders, Cashier cashier); 	
	public abstract void msgLeavingWork(MarketEmployee employee);
	public abstract void msgHereIsMoney(double money, MarketEmployee employee);
		
	
}
