package restaurantKC.interfaces;

import restaurantKC.Check;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Waiter {


	public abstract void msgHereIsComputedCheck(Check c);

	public abstract void msgImOutOfFood(int table);

	public abstract void msgOrderIsReady(String choice, int table, int plate);


}