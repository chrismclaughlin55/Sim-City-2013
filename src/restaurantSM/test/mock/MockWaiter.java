package restaurantSM.test.mock;


import restaurantSM.interfaces.Cashier;
import restaurantSM.interfaces.Customer;
import restaurantSM.interfaces.Waiter;
import restaurantSM.utils.Bill;
import restaurantSM.utils.Menu;
import restaurantSM.utils.Order;
import restaurantSM.utils.Table;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	@Override
	public void msgOutOfStock(Menu m, Order o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msg"));
	}

	@Override
	public void msgHeresTheBill(Bill b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgHeresTheBill"));
	}

	@Override
	public void msgReadyForCheck(Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgReadyForCheck"));
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgGoOnBreak"));
	}

	@Override
	public void msgSeatAtTable(Customer c, Table t) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgSeatAtTable"));
	}

	@Override
	public void msgReadyToOrder(Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgReadyToOrder"));
	}

	@Override
	public void msgHeresMyChoice(Customer c, String ch) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgHeresMyChoice"));
	}

	@Override
	public void msgOrderIsReady(Order o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgOrderIsReady"));
	}

	@Override
	public void msgDoneEating(Customer c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receieved msgDoneEating"));
	}

	
	
}
