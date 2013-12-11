package restaurantMQ.interfaces;

import restaurantMQ.Menu;
import restaurantMQ.gui.WaiterGui;

public interface Waiter {

	//MESSAGES
	public abstract void msgLeaving(Customer customer);

	public abstract void msgSeatCustomer(Customer customer, int table);
	
	public abstract void msgSeatCustomer(Customer customer, int spot, int table);

	public abstract void msgReadyToOrder(Customer customer);

	public abstract void msgHereIsMenu(Menu menu);

	public abstract void msgHereIsChoice(String choice, Customer customer);

	public abstract void msgOrderDone(String choice, int table);

	public abstract void msgDoneEating(Customer customer);

	public abstract void msgAskForSomethingElse(String choice, int table);

	public abstract void msgWantBreak();

	public abstract void msgGoOnBreak();

	public abstract void msgHereIsCheck(Customer customer, double price);

	public abstract void msgBackFromBreak();

	public abstract void setGui(WaiterGui waiterGui);

	public abstract void msgAnimationDone();

	public abstract void msgYoureFired();
}