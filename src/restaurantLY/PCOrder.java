package restaurantLY;

public class PCOrder {
	public LYPCWaiterRole waiter;
	public int tableNum;
	public String choice;
	
	public PCOrder(LYPCWaiterRole waiter, int tableNum, String choice) {
		this.waiter = waiter;
		this.tableNum = tableNum;
		this.choice = choice;
	}
}
