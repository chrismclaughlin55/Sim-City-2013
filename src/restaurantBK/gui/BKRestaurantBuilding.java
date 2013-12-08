package restaurantBK.gui;

import java.io.IOException;

import javax.swing.JFrame;

import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.Building.BuildingType;

public class BKRestaurantBuilding extends Building {

	RestaurantGui restGui;
	RestaurantPanel restPanel;
	public BKRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) throws IOException 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new RestaurantGui();
		this.restPanel = restGui.restPanel;
		// TODO Auto-generated constructor stub
	}

	@Override
	public JFrame getBuildingGui() {
		// TODO Auto-generated method stub
		return restGui;
	}

}
