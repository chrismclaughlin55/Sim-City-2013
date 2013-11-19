package market.test.mock;

import market.interfaces.MarketManager;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;

public class MockMarketManager extends Mock implements MarketManager {

	public EventLog log;
	
	public MockMarketManager(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgHereIsMoney(double money) {
		log.add(new LoggedEvent("Money received"));
	}

}
