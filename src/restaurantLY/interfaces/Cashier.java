package restaurantLY.interfaces;

public interface Cashier {
	public abstract void msgCreateCheck(Waiter waiter, Customer customer, String choice);
	public abstract void msgHereIsMoney(Customer customer, double money);
	public abstract String getName();
	public abstract String toString();
	public abstract void msgGetBillFromMarket(Market market, double price);
}