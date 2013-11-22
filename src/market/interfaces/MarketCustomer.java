package market.interfaces;

import market.Invoice;

public interface MarketCustomer {
	
	public abstract void msgOrderFulFullied(Invoice invoice);
	public abstract void msgOrderUnfulfilled(String type, int amount);
	
}
