package restaurantKC;

import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Waiter;


public class Check {
	public Customer c;
	public Waiter w;
	public int tableNum;
	public String choice;
	public Double price;
	public enum CheckState {uncomputed, unpaid, delivered, paid, incomplete, done};
	public CheckState state;
	
	

	public Check(String choice, int tableNum, Customer c, Waiter w)
	{	
		this.w = w;
		this.c = c;
		this.tableNum = tableNum;
		this.choice = choice;
		state = CheckState.uncomputed;
	}
	
	
}