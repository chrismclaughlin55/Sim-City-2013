package restaurantSM.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Stock {
	Menu menu = new Menu();

	public HashMap<String, Integer> stock = new HashMap<String, Integer>();
	ArrayList<String> keys = menu.getItems();
	public double total = 0.0;

	public Stock(int a){
		this.createHash(a);
	}
	
	public void setMenu(Menu m){
		menu = m;
	}

	public void createHash(int a){
		for (String key : keys){
			stock.put(key,  a);
		}
	}
	
	public void printStock(){
		for (String key : keys) {
			System.err.println(key + " " + stock.get(key));
		}
	}

	public HashMap<String, Integer> getStock(){
		return stock;
	}
}
