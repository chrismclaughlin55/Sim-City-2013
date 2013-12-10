package restaurantCM;

public class myCust {
//DATA
	private int tablenum;
	private String choice;
	private CustomerRole c;
	private double totalBill;
	
	public enum AgentState {waiting, seated, readyToOrder,waitingForOrder, sendOrderToCook, foodCooking, orderReady, eating,done, left, badOrder,askedForBill,sentBill, haveBill , billPaid, needsToPay,  }
	public AgentState state = null;
	
	
	public myCust(int tablenum, String choice, CustomerRole c) {
		super();
		this.tablenum = tablenum;
		this.choice = choice;
		this.c = c;
		this.state = AgentState.waiting;
	}
	public int getTablenum() {
		return tablenum;
	}
	public void setTablenum(int tablenum) {
		this.tablenum = tablenum;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public CustomerRole getC() {
		return c;
	}
	public void setC(CustomerRole c) {
		this.c = c;
	};
	
	public double getTotalBill() {
		return totalBill;
	}
	public void setTotalBill(double totalBill) {
		this.totalBill = totalBill;
	}
}
