package restaurantBK.interfaces;

import java.util.List;

import restaurantBK.BKCustomerRole;
import restaurantBK.gui.WaiterGui;

public interface Waiter {

	public abstract int getRestX();
	
	public abstract boolean wantsBreak();

	public abstract boolean waitingForResponse();

	public abstract String getName();

	public abstract List getCustomers();

	public abstract void setCook(Cook cook);

	public abstract void setHost(Host host);

	public abstract void setCashier(Cashier cash);

	// Messages

	public abstract void msgGoBackToWork();

	public abstract void msgChangeWantsBreak();

	public abstract void msgGoOnBreak();

	public abstract void msgCantBreak();

	public abstract void msgReadyToOrder(Customer cust);

	public abstract void msgSitAtTable(Customer customer, int tn, int pos);

	public abstract void msgHereIsMyChoice(Customer c, String choice);

	public abstract void msgOrderIsReady(int tn, String name);

	public abstract void msgAtDestination();

	public abstract void msgOutOfOrder(String order, int tn);

	public abstract void msgDoneEatingAndWantCheck(Customer c);

	public abstract void msgCheckMade(double cost, int table);

	public abstract void msgLeavingEarly(Customer c);

	public abstract void msgLeavingTableToPay(Customer c);

	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

}