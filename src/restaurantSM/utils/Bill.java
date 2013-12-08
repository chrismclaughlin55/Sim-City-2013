package restaurantSM.utils;
import agent.*;
import restaurantSM.utils.*;
import restaurantSM.*;
import restaurantSM.interfaces.Waiter;

public class Bill {
	public Waiter waiter;
	public MyCustomer myCust;
	public double change = 0;
	public double total;
	public double tender;
	
	public Bill(Waiter waiter2, MyCustomer c){
		waiter = waiter2;
		myCust = c;
	}
	
	public void setTotal(double t){
		total = t;
	}
	
	public void calcChange() {
		change = tender - total;
		if (change < 0){
			change = 0;
		}
	}
	
}
