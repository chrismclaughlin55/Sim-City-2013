package restaurantSM.utils;
import restaurantSM.*;
import restaurantSM.interfaces.Market;

public class StockBill {
	public Market market;
	public double total;
	
	public StockBill(Market m, double t){
		market = m;
		total = t;
	}
	
	
}
