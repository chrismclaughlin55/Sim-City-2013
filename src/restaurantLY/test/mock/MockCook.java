package restaurantLY.test.mock;

import java.util.List;

import restaurantLY.LYCookRole.Order;
import restaurantLY.LYPCWaiterRole;
import restaurantLY.LYWaiterRole;
import restaurantLY.gui.CookGui;
import restaurantLY.interfaces.Cook;
import restaurantLY.interfaces.Host;
import restaurantLY.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCook extends Mock implements Cook {
	public LYWaiterRole waiter;
	public LYPCWaiterRole pcWaiter;
	
	public EventLog log = new EventLog();
	public List<Order> orders;
	
	public MockCook(String name) {
		super(name);
	}

	@Override
	public void msgHereIsAnOrder(Waiter waiter, int tableNumber, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderFromMarket(String order, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CookGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtCookingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtPlacingArea() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtRefrigerator() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}
}