package restaurantBK;

import java.util.HashMap;
import java.util.Map;

public final class ItalianMenu extends HashMap<String,Double>{
	
	public ItalianMenu() {
		//prices = new HashMap<String,Double>();
		this.put("Steak",15.99);
		this.put("Chicken", 10.99);
		this.put("Salad", 5.99);
		this.put("Pizza", 8.99);
	}
	
	
}