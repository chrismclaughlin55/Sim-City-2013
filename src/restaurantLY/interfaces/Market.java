package restaurantLY.interfaces;

public interface Market {
	public abstract void msgGetOrderFromCook(Cook cook, String choice, int amount);
	public abstract int checkInventory(String choice);
	public abstract String getName();
	public abstract String toString();
	public abstract void msgHereIsMoney(double price);
	public abstract void msgNoMoney();
}