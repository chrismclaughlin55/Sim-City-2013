package restaurantKC.test.mock;


import restaurantKC.Check;
import restaurantKC.Menu;
import restaurantKC.gui.CustomerGui;
import restaurantKC.interfaces.Cashier;
import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Host;
import restaurantKC.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public Check check1 = null;
	public double money;

	public MockCustomer(String name) {
		super(name);

	}

	public void msgHereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}


	public void msgPunish() {
		log.add(new LoggedEvent("Received YouOweUs from cashier."));
	}

	
	public void setCashier(Cashier c) {
		cashier = c;
	}

	@Override
	public void msgHereIsCheck(Check c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodUnavailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startThread() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPause() {
		// TODO Auto-generated method stub
		
	}

	

}
