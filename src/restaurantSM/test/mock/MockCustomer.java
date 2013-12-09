package restaurantSM.test.mock;


import restaurantSM.interfaces.Cashier;
import restaurantSM.interfaces.Customer;
import restaurantSM.interfaces.Waiter;
import restaurantSM.utils.Bill;
import restaurantSM.utils.Menu;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	public double bankRoll = 25.00;
	
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

	public void payForFood(Bill b) {
		bankRoll -= b.total;
	}
	
	@Override
	public void msgHeresYourChange(Bill b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgHeresYourChange"));
	}

	@Override
	public void msgHeresYourBill(Bill b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgHeresYourBill"));
	}

	@Override
	public void msgFollowMe(Waiter w, Menu m, int tableNumber) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgFollowMe"));
	}

	@Override
	public void msgWhatWouldYouLike(Menu m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgWhatWouldYouLike"));
	}

	@Override
	public void msgHeresYourOrder() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgHeresYourOrder"));
	}

//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//		log.add(new LoggedEvent("receieved msg"));
//	}

	
}
