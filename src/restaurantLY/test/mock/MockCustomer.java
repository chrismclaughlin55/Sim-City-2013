package restaurantLY.test.mock;

import restaurantLY.interfaces.Cashier;
import restaurantLY.interfaces.Customer;
import restaurantLY.interfaces.Host;
import restaurantLY.interfaces.Waiter;
import restaurantLY.Menu;
import restaurantLY.gui.CustomerGui;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {
	public Cashier cashier;
	
	public EventLog log = new EventLog();
	
	public MockCustomer(String name) {
		super(name);
	}
	
	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgFollowMeToTable(Waiter waiter, Menu menu, int tableNumber) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgDecidedChoice() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgLeaving() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgWaiting() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgReorder(Menu menu) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgHereIsCheck(double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Getting change from cashier of $" + change));
		
	}
	
	@Override
	public void msgRestaurantIsFull() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgOweMoney() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MockCustomer";
	}
	
	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return "MockCustomer";
	}
	
	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}
}
