package market;


public class MyOrder {
	String type; 
	int amount;
	int marketNum;
	public enum OrderState {pending, fulfilled, unfulfilled};
	OrderState orderState;

	public MyOrder(String type, int amount, int marketNum) {
		this.type = type;
		this.amount = amount;
		this.marketNum = marketNum;
		orderState = OrderState.pending;
	}
}
