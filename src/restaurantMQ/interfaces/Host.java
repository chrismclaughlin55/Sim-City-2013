package restaurantMQ.interfaces;

public interface Host {

	public abstract void msgLeaving(Customer customer);

	public abstract void msgBackFromBreak(Waiter waiter);

	public abstract void msgIWantFood(Customer cust);

	public abstract void msgLeavingTable(Customer cust);

	public abstract void msgAtTable();

	public abstract void msgReadyForCust();

	public abstract void msgTableEmpty(int table);

	public abstract void msgTableEmpty(int table, Waiter waiter);

	public abstract void msgIWantBreak(Waiter waiter);

	public abstract void msgFreeSpot(int waitingSpot);

	public abstract void msgLeavingNow(Waiter waiter);

}