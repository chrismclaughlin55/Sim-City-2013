package restaurantSM.interfaces;

import restaurantSM.utils.Request;

public interface Market {

	public abstract String getName();

	public abstract void msgRequestStock(Request r);
	
	public abstract void msgReceivePayment(double payment, double tabAmt);
	
	public abstract void msgPayDownTab(double payment);
}
