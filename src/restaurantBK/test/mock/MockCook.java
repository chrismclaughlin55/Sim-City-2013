package restaurantBK.test.mock;

import java.util.HashMap;

import restaurantBK.gui.CookGui;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Market;
import restaurantBK.interfaces.Waiter;

public class MockCook extends Mock implements Cook{

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addMarket(Market m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAnOrder(Waiter w, String choice, int tn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMarketOrderResponse(Market m,
			HashMap<String, Integer> shipment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCantMakeMarketOrder(Market m, HashMap<String, Integer> ship) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CookGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGui(CookGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPickedUpOrder(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsUpdated() {
		// TODO Auto-generated method stub
		
	}

}
