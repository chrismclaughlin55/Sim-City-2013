package restaurantBK.interfaces;

import java.util.Collection;
import java.util.List;

import restaurantBK.BKCustomerRole;

public interface Host {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getWaitingCustomers();

	public abstract List getWaiters();

	public abstract Collection getTables();

	public abstract void gotCustomer();
	
	public abstract void msgIWantFood(Customer cust);

	public abstract void msgBreakPlease(Waiter w);

	public abstract void msgComingBackFromBreak(Waiter w);

	public abstract void msgTableIsFree(int table);

	public abstract void msgNoTablesImLeaving(Customer c);

	public abstract void addWaiter(Waiter waiter);
	/*private void seatCustomer(CustomerAgent customer, Table table) {
		//FIND A WAITER WHO IS AVAILABLE AND GET HIM TO SEAT THIS CUSTOMER
		//waiter.msgSitAtTable(customer, table.tableNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//w.msgSitAtTable(customer,table.tablenumber);
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		//hostGui.DoLeaveCustomer();
	}
	 */
	// The animation DoXYZ() routines


}