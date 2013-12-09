package restaurantKC.test.mock;


import restaurantKC.interfaces.Cashier;
import restaurantKC.interfaces.Cook;
import restaurantKC.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockMarket(String name, int i, int j, int k, int l, Cook cook, Cashier cashier2) {
		super(name);

	}

	@Override
	public void msgHereIsBill(double amountPaid) {
		log.add(new LoggedEvent("Received msgHereIsBill from cashier"));
		
	}
	
	

}