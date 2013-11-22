package market.test.mock;

import market.Invoice;
import market.interfaces.MarketCustomer;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	public EventLog log = new EventLog();
	
	public MockMarketCustomer(String name) {
		super(name);
	}

	@Override
	public void msgOrderFulFullied(Invoice invoice) {

	}

	@Override
	public void msgOrderUnfulfilled(String type, int amount) {
		// TODO Auto-generated method stub
		
	}

	

}
