package restaurantSM.utils;

import java.util.*;

public class Menu {

	public ArrayList<String> items = new ArrayList<String>();
	public HashMap<String, Double> prices = new HashMap<String, Double>();
	
	public Menu() {
		items.add("Steak");
		items.add("Chicken");
		items.add("Salad");
		items.add("Pizza");
		prices.put("Steak", 15.99);
		prices.put("Chicken", 10.99);
		prices.put("Salad", 5.99);
		prices.put("Pizza", 8.99);
	}
	
	public String chooseItem() {
		Random generator = new Random();
		return items.get(generator.nextInt(items.size()));
	}
	
	public void removeItem(String s) {
		items.remove(s);
	}
	
	public ArrayList<String> getItems() {
		return items;
	}
	
}
