package restaurantBK.interfaces;

import java.util.HashMap;

import restaurantBK.gui.CookGui;

public interface Cook {

	public abstract String getName();

	public abstract void addMarket(Market m);

	// Messages

	public abstract void msgHereIsAnOrder(Waiter w, String choice, int tn);

	public abstract void msgMarketOrderResponse(Market m,
			HashMap<String, Integer> shipment);

	public void msgAtDestination();

	public void msgCantMakeMarketOrder(Market m, HashMap<String, Integer> ship);

	public CookGui getGui();

	public void setGui(CookGui gui);

	public abstract void msgPickedUpOrder(int tableNumber);

}