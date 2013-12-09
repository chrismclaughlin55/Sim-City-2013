package restaurantKC.interfaces;

import restaurantKC.Check;
import restaurantKC.KCWaiterRole;
import restaurantKC.MarketAgent;

public interface Cashier {

	void msgPayingCheck(Check check, Double d);

	void msgGiveOrderToCashier(String choice, int t, Customer c,
			Waiter waiter);

	void msgHereIsMarketBill(Double price, Market m);
	
}
