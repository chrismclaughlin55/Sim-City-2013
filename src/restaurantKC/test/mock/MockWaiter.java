package restaurantKC.test.mock;


import restaurantKC.Check;
import restaurantKC.gui.WaiterGui;
import restaurantKC.interfaces.Cashier;
import restaurantKC.interfaces.Cook;
import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Host;
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
	public Cashier cashier;

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

	@Override
	public void msgSitAtTable(Customer customer, int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(String choice, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaving(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakApproved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakDenied() {
		// TODO Auto-generated method stub
		
	}

	
	

}
