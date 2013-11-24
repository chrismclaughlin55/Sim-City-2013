package market.test.mock;

import restaurantMQ.test.mock.EventLog;
import market.MarketCustomerRole;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;

public class MockMarketEmployee extends Mock implements MarketEmployee {

	public EventLog log;
	
	public MockMarketEmployee(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgHereIsAnOrder(String type, int quantity, MarketCustomer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double payment) {
		// TODO Auto-generated method stub
		
	}

}