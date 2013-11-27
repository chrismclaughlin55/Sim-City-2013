package market.interfaces;

import java.util.List;

import market.Invoice;
import market.MarketEmployeeRole;

public interface MarketCustomer {
	
	public abstract void msgOrderFulfullied(List<Invoice> invoice, double amountDue);
	public abstract void msgYouCanLeave();
	public abstract void msgWhatIsYourOrder(MarketEmployeeRole marketEmployeeRole);
	
}
