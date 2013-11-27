package market.test.mock;

import java.util.List;

import market.Invoice;
import market.MarketEmployeeRole;
import market.interfaces.MarketCustomer;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	public EventLog log = new EventLog();
	public double payment = 0;
	public MockMarketCustomer(String name) {
		super(name);
	}

	
	@Override
	public void msgWhatIsYourOrder(MarketEmployeeRole marketEmployeeRole) {
		log.add((new LoggedEvent("Called by employee")));
	}

	@Override
	public void msgOrderFulfullied(List<Invoice> invoice, double amountDue) {
		log.add((new LoggedEvent("Order fulfilled")));
		payment = amountDue;
	}


	@Override
	public void msgYouCanLeave() {
		// TODO Auto-generated method stub
		
	}


	
	

	

}
