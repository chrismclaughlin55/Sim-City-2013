package restaurantKC.interfaces;

import java.util.List;

import market.Invoice;
import market.MarketManagerRole;
import restaurantKC.Check;

public interface Cashier {

	void msgPayingCheck(Check check, Double d);

	void msgGiveOrderToCashier(String choice, int t, Customer c,
			Waiter waiter);

	void msgHereIsMarketBill(double price, List<Invoice> invoice, MarketManagerRole m);
	
}
