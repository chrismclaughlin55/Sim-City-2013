package restaurantMQ;

import restaurantMQ.interfaces.Waiter;

public class CookOrder 
{
	String choice;
	int table;
	public enum OrderState {Received, Cooking, Reject, Done};
	OrderState orderState = OrderState.Received;
	Waiter waiter;
	
	CookOrder(String choice, int table, Waiter waiter)
	{
		this.choice = choice;
		this.table = table;
		this.waiter = waiter;
	}
}
