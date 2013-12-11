package restaurantMQ.test.mock;

import restaurantMQ.Menu;
import restaurantMQ.gui.WaiterGui;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter
{
	public EventLog log = new EventLog();
	
	public MockWaiter(String name)
	{
		super(name);
	}

	public void msgLeaving(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSeatCustomer(Customer customer, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChoice(String choice, Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderDone(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAskForSomethingElse(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Customer customer, double price) {
		log.add(new LoggedEvent("Received check from cashier."));
		
	}

	@Override
	public void msgBackFromBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSeatCustomer(Customer customer, int spot, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui waiterGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYoureFired() {
		// TODO Auto-generated method stub
		
	}
}
