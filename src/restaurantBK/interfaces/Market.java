package restaurantBK.interfaces;

import java.util.HashMap;

public interface Market {

	public abstract String getName();

	public abstract void setCashier(Cashier c);
	
	public abstract void msgFoodRequest(HashMap<String, Integer> order,
			Cook cook);

	public abstract void msgPayingMarketBill(Cashier cashier, double cost);

}