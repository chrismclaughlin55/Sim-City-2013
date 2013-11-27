package restaurantMQ.interfaces;

import market.MarketEmployeeRole;

public interface Cashier {

	//MESSAGES
	public abstract void msgProduceCheck(Waiter waiter,
			Customer customer, String choice);

	public abstract void msgHereIsMoney(Customer customer, double payment);

	public abstract void msgHereIsBill(MarketEmployeeRole marketEmployee, double bill);

	public abstract void startThread();
}