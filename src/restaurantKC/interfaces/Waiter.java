package restaurantKC.interfaces;

import restaurantKC.Check;
import restaurantKC.gui.WaiterGui;

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

	public abstract void msgSitAtTable(Customer customer, int tableNumber);

	public abstract void msgHereIsMyChoice(String choice, Customer cust);

	public abstract void msgImReadyToOrder(Customer cust);

	public abstract void msgDoneEating(Customer cust);

	public abstract void msgLeaving(Customer cust);

	public abstract void msgPause();

	public abstract void setGui(WaiterGui g);

	public abstract void setCook(Cook cook);

	public abstract void setCashier(Cashier cashier);

	public abstract String getName();

	public abstract WaiterGui getGui();

	public abstract void setHost(Host host);

	public abstract void msgBreakApproved();

	public abstract void msgBreakDenied();




}