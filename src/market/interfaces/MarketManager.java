package market.interfaces;

import java.util.List;

import market.CookMarketOrder;
import market.MarketCustomerRole;
import market.MarketEmployeeRole;
import restaurantKC.KCCashierRole;
import restaurantKC.KCCookRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.MarketOrder;
import restaurantMQ.interfaces.Cashier;

public interface MarketManager {
	
	public abstract void msgReportingForWork(MarketEmployee employee);	
	public abstract void msgNeedToOrder(MarketCustomer cust);	
	public void msgHereIsMarketOrder(KCCookRole cook, KCCashierRole cashier, List<CookMarketOrder>ordersToSend); 
	public abstract void msgLeavingWork(MarketEmployee employee);
	public abstract void msgHereIsMoney(double money, MarketEmployee employee);
		
	
}
