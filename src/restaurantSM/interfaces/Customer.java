package restaurantSM.interfaces;

import restaurantSM.WaiterAgent;
import restaurantSM.utils.Bill;
import restaurantSM.utils.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public abstract void msgHeresYourChange(Bill b);

	public abstract void msgHeresYourBill(Bill b);
	
	public abstract void msgFollowMe(Waiter w, Menu m, int tableNumber);
	
	public abstract void msgWhatWouldYouLike(Menu m);
	
	public abstract void msgHeresYourOrder();

	public abstract void pause();
	
	
	
}