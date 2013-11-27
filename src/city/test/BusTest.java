package city.test;

import restaurantMQ.MQCashierRole;
import restaurantMQ.test.mock.MockCustomer;
import restaurantMQ.test.mock.MockMarket;
import restaurantMQ.test.mock.MockWaiter;
import junit.framework.TestCase;
import city.test.mock.*;

public class BusTest extends TestCase {
	MockBus mbus;
	MockBusStop mbusstop;
	
	public void setUp() throws Exception{
		super.setUp();		
		mbus = new MockBus();		
		mbusstop = new MockBusStop();
		
	}	

}
