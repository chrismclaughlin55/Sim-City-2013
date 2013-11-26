package restaurantMQ.gui;

import mainGUI.MainGui;
import city.Building;
import city.PersonAgent;

public class MQRestaurantBuilding extends Building
{
	RestaurantGui restGui = new RestaurantGui();
	RestaurantPanel restPanel;
	
	public MQRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restPanel = restGui.getRestaurantPanel();
		System.out.println("hello");
	}
	
	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		if(roleRequest.equals("Customer"))
		{
			restPanel.addCustomer(person);
		}
		else if(roleRequest.equals("Waiter"))
		{
			restPanel.addWaiter(person);
		}
		else if(roleRequest.equals("Cook"))
		{
			restPanel.addCook(person);
		}
		else if(roleRequest.equals("Cashier"))
		{
			restPanel.addCashier(person);
		}
		else if(roleRequest.equals("Host"))
		{
			restPanel.addHost(person);
		}
	}
	
	public RestaurantGui getRestaurantGui()
	{
		return restGui;
	}
}
