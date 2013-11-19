package market.interfaces;

import market.MarketCustomerRole;

public interface MarketEmployee {
	
	public abstract void msgHereIsAnOrder(String type, int quantity, MarketCustomer cust); 
	public abstract void msgHereIsPayment(double payment);
		

}
