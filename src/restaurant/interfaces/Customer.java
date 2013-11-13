package restaurant.interfaces;

import restaurant.Menu;

public interface Customer {

	public abstract void msgTablesFull();

	public abstract void msgHereIsCheck(double balance);

	public abstract void msgFollowMe(Waiter waiter);

	public abstract void msgFollowMe(Waiter waiter, int table);

	public abstract void msgFollowMe(Waiter waiter, int table, Menu menu);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgReadyToOrder();

	public abstract void msgWhatDoYouWant();

	public abstract void msgHereIsFood();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgAnimationDone();

	public abstract void msgPleaseChooseSomethingElse(Menu menu);

	public abstract void msgGoodToGo();

	public abstract void msgNotEnough();

	public abstract String getName();

	public abstract void msgGoToSpot(int number);

}