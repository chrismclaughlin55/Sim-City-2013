package market;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import market.gui.MarketGui;

public class Inventory {

	public Map<String,MarketData> inventory = Collections.synchronizedMap(new HashMap<String,MarketData>()); 
	private MarketGui marketGui;
	
	public Inventory(MarketGui marketGui) {
		this.marketGui = marketGui;

		MarketData chickenData = new MarketData("Chicken", 10, 10);
		MarketData saladData = new MarketData("Salad", 10, 10);
		MarketData steakData = new MarketData("Steak", 10, 10);
		MarketData pizzaData = new MarketData("Pizza", 10, 10);

		inventory.put("Chicken", chickenData );
		inventory.put("Salad", saladData);
		inventory.put("Steak", steakData);
		inventory.put("Pizza", pizzaData);

	}

	public Inventory(MarketData chickenData, MarketData steakData, MarketData saladData, MarketData pizzaData, MarketGui marketGui) {
		this.marketGui = marketGui;

		inventory.put("Chicken", chickenData);
		inventory.put("Salad", saladData);
		inventory.put("Steak", steakData);
		inventory.put("Pizza", pizzaData);
	}
	
	public void update() {
		marketGui.updateMarketPanel();
	}
	
	public void setGui(MarketGui marketGui){
		this.marketGui = marketGui;
	}

}
