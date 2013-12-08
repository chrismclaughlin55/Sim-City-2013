package restaurantSM.interfaces;

import restaurantSM.MarketAgent;
import restaurantSM.WaiterAgent;
import restaurantSM.test.mock.EventLog;
import restaurantSM.utils.Bill;
import restaurantSM.utils.MyCustomer;

public interface Cashier {
	
	public abstract void msgComputeBill(Waiter waiter, MyCustomer myCust);
	
	public abstract void msgHeresMyBill(Double money, Bill b);
	
	public abstract void msgPayForStock(Market m, double stockPrice);
	
}
