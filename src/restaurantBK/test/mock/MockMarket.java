package restaurantBK.test.mock;

import java.util.HashMap;

import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public Cashier cashier;
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setCashier(Cashier c) {
		cashier=c;
	}

	@Override
	public void msgFoodRequest(HashMap<String, Integer> order, Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingMarketBill(Cashier cashier, double cost) {
		//System.out.println("Thanks for paying");
		// TODO Auto-generated method stub
		
	}

}
