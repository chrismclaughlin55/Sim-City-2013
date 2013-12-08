package restaurantLY.interfaces;

import restaurantLY.gui.CookGui;

public interface Cook {
	public abstract void msgHereIsAnOrder(Waiter waiter, int tableNumber, String choice);
	public abstract void msgOrderFromMarket(String order, int amount);
	public abstract String getName();
	public abstract String toString();
	public abstract void addMarket(Market market);
	public abstract void setGui(CookGui g);
	public abstract CookGui getGui();
}