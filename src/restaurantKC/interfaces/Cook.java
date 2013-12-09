package restaurantKC.interfaces;

import java.util.List;

import restaurantKC.MarketAgent;
import restaurantKC.gui.CookGui;

public interface Cook {

	void msgOrderUnfulfilled(String type);

	void msgOrderFulfilled(String type, int amount);

	void msgOrderPartiallyFulfilled(String type, int i, int amountunfulfilled);

	void addMarkets(List<MarketAgent> markets);

	void setGui(CookGui cookGui);

	void drainInventory();
	
	
	
	
}