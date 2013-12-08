package restaurantKC.test.mock;


import restaurantKC.CashierAgent;
import restaurantKC.Check;
import restaurantKC.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public CashierAgent cashier;

	public MockWaiter(String name) {
		super(name);

	}
	
	public void msgHereIsComputedCheck(Check c) {
		log.add(new LoggedEvent("Received msgHereIsComputedcheck from cashier"));
		
	}

	@Override
	public void msgImOutOfFood(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table, int plate) {
		// TODO Auto-generated method stub
		
	}

	
	

}
