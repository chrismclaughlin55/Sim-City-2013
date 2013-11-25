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
	}
	
	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		
	}
}
