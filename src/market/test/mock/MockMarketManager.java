package market.test.mock;

import java.util.List;

import market.CookMarketOrder;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import restaurantKC.KCCashierRole;
import restaurantKC.KCCookRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.MarketOrder;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;

public class MockMarketManager extends Mock implements MarketManager {

	public EventLog log;
	
	public MockMarketManager(String name) {
		super(name);
		log = new EventLog();
	}

	public void msgHereIsMoney(double money, MarketEmployee employee) {
		log.add(new LoggedEvent("Money received"));
	}

	@Override
	public void msgReportingForWork(MarketEmployee employee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedToOrder(MarketCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void msgLeavingWork(MarketEmployee employee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMarketOrder(KCCookRole cook, KCCashierRole cashier,
			List<CookMarketOrder> ordersToSend) {
		// TODO Auto-generated method stub
		
	}

	

}
