package restaurantMQ.interfaces;

import restaurantMQ.CustomerAgent;

public interface Cashier {

	//MESSAGES
	public abstract void msgProduceCheck(Waiter waiter,
			Customer customer, String choice);

	public abstract void msgHereIsMoney(Customer customer, double payment);

	public abstract void msgHereIsBill(Market market, double bill);

	public abstract void startThread();
}