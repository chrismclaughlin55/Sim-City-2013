package restaurantLY.test.mock;

import restaurantLY.interfaces.*;

public class MockMarket extends Mock implements Market {
	public EventLog log = new EventLog();
	
	public MockMarket(String name) {
		super(name);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MockMarket";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MockMarket";
	}
	
	@Override
	public void msgGetOrderFromCook(Cook cook, String choice, int amount) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgHereIsMoney(double price) {
		log.add(new LoggedEvent("Receiving $" + price + " from cashier"));
		
	}

	@Override
	public void msgNoMoney() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int checkInventory(String choice) {
		// TODO Auto-generated method stub
		return 0;
	}
}