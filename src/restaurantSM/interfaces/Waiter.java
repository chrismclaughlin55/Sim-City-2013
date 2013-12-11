package restaurantSM.interfaces;

import restaurantSM.SMCustomerRole;
import restaurantSM.SMWaiterRole;
import restaurantSM.utils.*;

public interface Waiter {

	public abstract void msgOutOfStock(Menu m, Order o);
	
	public abstract void msgHeresTheBill(Bill b);
	
	public abstract void msgReadyForCheck(Customer c);
	
	public abstract void msgGoOnBreak();
	
	public abstract void msgSeatAtTable(Customer c, Table t);
	
	public abstract void msgReadyToOrder(Customer c);
	
	public abstract void msgHeresMyChoice(Customer c, String ch);
	
	public abstract void msgOrderIsReady(Order o);
	
	public abstract void msgDoneEating(Customer c);
	
	
	
	
	
}
