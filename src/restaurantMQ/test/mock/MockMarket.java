package restaurantMQ.test.mock;

import java.util.List;

import restaurantMQ.CookAgent;
import restaurantMQ.MarketOrder;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Market;

public class MockMarket extends Mock implements Market
{
	public EventLog log = new EventLog();
	
	public MockMarket(String name)
	{
		super(name);
	}

	public void msgNewOrders(CookAgent cook, Cashier cashier,
			List<MarketOrder> marketOrders) {
		
		
	}

	@Override
	public void msgNewOrder(CookAgent cook, Cashier cashier, String name,
			int quantity) {
		
		
	}

	@Override
	public void msgHereIsPayment(Cashier cashier, double payment) 
	{
		log.add(new LoggedEvent("Received payment from cashier"));
	}
}
