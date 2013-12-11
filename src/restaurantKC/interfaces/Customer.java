package restaurantKC.interfaces;

import restaurantKC.Check;
import restaurantKC.Menu;
import restaurantKC.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {

	

	boolean oweMoney = false;
	Check check = null;


	public abstract void msgHereIsYourChange(double total);

	public abstract void msgPunish();

	public abstract void msgHereIsCheck(Check c);

	public abstract void msgFoodUnavailable();

	public abstract void msgFollowMe(Menu menu);

	public abstract void msgWhatWouldYouLike();

	public abstract void msgHereIsYourFood();

	public abstract CustomerGui getGui();

	public abstract String getName();

	public abstract void setWaiter(Waiter waiter);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgGotHungry();

	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cashier);

	public abstract void setGui(CustomerGui g);

	public abstract void startThread();

	public abstract void msgPause();



}