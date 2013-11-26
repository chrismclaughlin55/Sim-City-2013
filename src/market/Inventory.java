package market;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import market.gui.MarketGui;

public class Inventory {

	public Map<String,MarketData> inventory = Collections.synchronizedMap(new HashMap<String,MarketData>()); 
	private MarketGui marketGui;
	public Inventory(MarketGui marketGui) {
		MarketData chickenData = new MarketData("chicken", 10, 10);
		MarketData saladData = new MarketData("salad", 10, 10);
		MarketData steakData = new MarketData("steak", 10, 10);
		MarketData pizzaData = new MarketData("pizza", 10, 10);

		inventory.put("chicken", chickenData );
		inventory.put("salad", saladData);
		inventory.put("steak", steakData);
		inventory.put("pizza", pizzaData);
		this.marketGui = marketGui;

	}

	public Inventory(MarketData chickenData, MarketData steakData, MarketData saladData, MarketData pizzaData, MarketGui marketGui) {
		inventory.put("chicken", chickenData);
		inventory.put("salad", saladData);
		inventory.put("steak", steakData);
		inventory.put("pizza", pizzaData);
		this.marketGui = marketGui;
	}
	
	public void update() {
		marketGui.updateMarketPanel();
	}

}
