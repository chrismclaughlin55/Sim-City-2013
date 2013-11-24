package restaurantMQ.interfaces;

import restaurantMQ.CookOrder;

public interface Cook {

	public abstract void addWaiter(Waiter waiter);

	//MESSAGES
	public abstract void msgHereIsOrder(String choice, int table, Waiter waiter);

	public abstract void msgFoodDone(CookOrder order);

	public abstract void msgFoodDelivered(String name, int quantity);

	//hacks
	public abstract void OutOfFoodHack();

	public abstract void OutOfSaladHack();

	public abstract void msgPause();

	public abstract void msgOrdersUpdated();

}