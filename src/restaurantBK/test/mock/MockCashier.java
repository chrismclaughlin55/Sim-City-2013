package restaurantBK.test.mock;

import restaurantBK.BKCustomerRole;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Market;
import restaurantBK.interfaces.Waiter;

public class MockCashier extends Mock implements Cashier {

	public MockCashier(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgMakeCheck(Customer c, Waiter w, double price,
			int tablenumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTakeMyMoney(double money, Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMarketBill(Market market, double d) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgThanksForPayingBill(Market market, double d) {
		// TODO Auto-generated method stub
		
	}

}
