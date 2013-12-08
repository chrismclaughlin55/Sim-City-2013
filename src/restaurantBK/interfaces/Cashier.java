package restaurantBK.interfaces;

import restaurantBK.CustomerAgent;

public interface Cashier {

	public abstract String getName();

	// Messages
	public abstract void msgMakeCheck(Customer c, Waiter w,
			double price, int tablenumber);

	public abstract void msgTakeMyMoney(double money, Customer c);

	public abstract void msgHereIsMarketBill(Market market, double d);
	
	public abstract void msgThanksForPayingBill(Market market, double d);

}