package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu 
{
	private List<String> menu = Collections.synchronizedList(new ArrayList<String>());
	private Map<String, Double> foodMap = new HashMap<String, Double>();
	
	public Menu()
	{
		menu.add("Steak");
		menu.add("Chicken");
		menu.add("Pizza");
		menu.add("Salad");
		
		foodMap.put("Steak", 15.99);
		foodMap.put("Chicken", 10.99);
		foodMap.put("Salad", 5.99);
		foodMap.put("Pizza", 8.99);
	}
	
	public Menu(Menu m)
	{
		synchronized(m.menu)
		{
			for(int i = 0; i < m.menu.size(); ++i)
			{
				menu.add(m.menu.get(i));
			}
		}
		
		foodMap.put("Steak", 15.99);
		foodMap.put("Chicken", 10.99);
		foodMap.put("Salad", 5.99);
		foodMap.put("Pizza", 8.99);
	}
	
	public Menu(String without)
	{
		menu.add("Steak");
		menu.add("Chicken");
		menu.add("Pizza");
		menu.add("Salad");
		
		menu.remove(without);
	}
	
	public double getPrice(String choice)
	{
		return foodMap.get(choice);
	}
	
	public boolean has(String choice)
	{
		return menu.contains(choice);
	}
	
	public String chooseRandom()
	{
		synchronized(menu)
		{
			int index = ((int)(Math.random()*menu.size())) % menu.size();
			
			return menu.get(index);
		}
	}
	
	public void remove(String choice)
	{
		menu.remove(choice);
	}
	
	public void add(String choice)
	{
		menu.add(choice);
	}
}
