package restaurantLY.interfaces;

import java.util.List;

import restaurantLY.interfaces.Customer;
import restaurantLY.interfaces.Waiter;

public interface Host {
	public abstract void msgIWantToEat(Customer cust);
	public abstract void msgTableIsFree(int tableNumber);
	public abstract void msgAskForBreak(Waiter waiter);
	public abstract void msgBackToWork(Waiter waiter);
	public abstract void msgCustomerWaiting(Customer customer);
	public abstract void msgCustomerLeaving(Customer customer);
	public abstract void msgAtCustomer();
	public abstract String getMaitreDName();
	public abstract String getName();
	public abstract List getWaitingCustomers();
	public abstract List getWaiters();
	public abstract void setWaiter(Waiter waiter);
	public abstract void msgLeavingNow(Waiter waiter);
}