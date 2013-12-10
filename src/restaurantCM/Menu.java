package restaurantCM;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {
	public HashMap<String, Double> Menu = new HashMap<String, Double>();
	public ArrayList<String> choices = new ArrayList<String>();
	Menu(){
		this.Menu.put("chicken", 10.99);
		this.Menu.put("steak", 20.99);
		this.Menu.put("pasta", 15.99);
		this.Menu.put("pizza", 30.99);
		this.Menu.put("donuts", 4.99);
		this.choices.addAll( this.Menu.keySet());
	}
	
}
