package restaurantKC;

import restaurantKC.interfaces.Waiter;


public class pcCookOrder 
{
	String choice;
	int table;
	public enum OrderState {Received, Cooking, Reject, Done};
	OrderState orderState = OrderState.Received;
	Waiter waiter;
	
	public pcCookOrder(Waiter waiter, String choice, int t)
	{
		this.choice = choice;
		this.table = t;
		this.waiter = waiter;
	}
}