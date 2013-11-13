package restaurant.test.mock;

import java.util.List;

import restaurant.CookAgent;
import restaurant.MarketOrder;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;

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
