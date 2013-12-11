package restaurantKC.interfaces;

import restaurantKC.gui.CookGui;

public interface Cook {


	void msgOrderFulfilled(String type, int amount);



	void setGui(CookGui cookGui);

	void drainInventory();
	
	
	
	
}