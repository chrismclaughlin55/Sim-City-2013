package restaurantLY.interfaces;

import java.util.List;

import restaurantLY.gui.RestaurantGui;
import restaurantLY.gui.WaiterGui;

public interface Waiter {
	public abstract void msgOnBreak();
	public abstract void msgSitAtTable(Customer customer, int tableNumber, int custNumber);
	public abstract void msgImReadyToOrder(Customer customer);
	public abstract void msgHereIsMyChoice(Customer customer, String choice);
	public abstract void msgOrderIsReady(int tableNumber);
	public abstract void msgDoneEatingAndLeaving(Customer customer);
	public abstract void msgLeaving(Customer customer);
	public abstract void msgAtTable();
	public abstract void msgAtCook();
	public abstract void msgAtDoor();
	public abstract void msgAtCust();
	public abstract void msgDecisionOnBreak(boolean isOnBreak);
	public abstract void msgCookRunOutOfFood(int tableNumber);
	public abstract void msgAskForCheck(Customer customer, double check);
	public abstract String getMaitreDName();
	public abstract String getName();
	public abstract String toString();
	public abstract List getCustomers();
	public abstract void setGui(WaiterGui gui);
	public abstract WaiterGui getGui();
	public abstract void setCook(Cook cook);
	public abstract void setHost(Host host);
	public abstract void setCashier(Cashier cashier);
	public abstract boolean isOnBreak();
	public abstract void setOnBreak(boolean isOnBreak);
	public abstract void setGuiPanel(RestaurantGui gui);
}