package restaurantBK;

import restaurantBK.interfaces.Waiter;

public class Order{
	String name;
	public enum OrderState {pending,cooking,plated,done};
	OrderState os;
	Waiter w;
	int tablenumber;
	public Order(String choice, Waiter w, int tn) {
		this.name=choice;
		this.w=w;
		this.tablenumber=tn;
		this.os=OrderState.pending;
	}
}