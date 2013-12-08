package restaurantLY.interfaces;

import restaurantLY.Menu;
import restaurantLY.gui.CustomerGui;

public interface Customer {
	public abstract void gotHungry();
	public abstract void msgFollowMeToTable(Waiter waiter, Menu menu, int tableNumber);
	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgDecidedChoice();
	public abstract void msgWhatWouldYouLike();
	public abstract void msgHereIsYourFood(String choice);
	public abstract void msgDoneEating();
	public abstract void msgLeaving();
	public abstract void msgWaiting();
	public abstract void msgReorder(Menu menu);
	public abstract void msgHereIsCheck(double check);
	public abstract void msgHereIsChange(double change);
	public abstract void msgRestaurantIsFull();
	public abstract void msgOweMoney();
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgAtTable();
	public abstract String getName();
	public abstract int getHungerLevel();
	public abstract void setHungerLevel(int hungerLevel);
	public abstract String toString();
	public abstract void setGui(CustomerGui g);
	public abstract CustomerGui getGui();
	public abstract boolean isHungry();
	public abstract void setHost(Host host);
	public abstract void setWaiter(Waiter waiter);
	public abstract void setCashier(Cashier cashier);
}