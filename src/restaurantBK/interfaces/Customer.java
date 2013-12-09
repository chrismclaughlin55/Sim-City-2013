package restaurantBK.interfaces;

import restaurantBK.ItalianMenu;
import restaurantBK.gui.CustomerGui;

public interface Customer {

	/**
	 * hack to establish connection to Host agent.
	 */
	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cash);

	public abstract void setWaiter(Waiter waiter);

	public abstract String getCustomerName();

	// Messages

	public abstract void gotHungry();

	public abstract void msgWouldYouLikeToWait(int numberahead);

	public abstract void msgFollowMeToTable(Waiter w, int tableNumber,
			ItalianMenu m);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgOrderSomethingElse();

	public abstract void msgHereIsYourFood();

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedGoToCashier();

	public abstract void msgHereIsTheCheck(double price);

	public abstract void msgAllPaidPlusChange(double change);

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void msgWaitHerePlease(int x);

}