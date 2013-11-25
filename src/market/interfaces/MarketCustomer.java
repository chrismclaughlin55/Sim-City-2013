package market.interfaces;

import java.util.List;

import market.Invoice;

public interface MarketCustomer {
	
	public abstract void msgOrderFulfullied(List<Invoice> invoice);
	public abstract void msgOrderUnfulfilled(String type, int amount);
	
}
