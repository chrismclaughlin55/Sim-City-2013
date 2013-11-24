package mainGUI;

import javax.swing.JFrame;

import market.gui.MarketGui;
import restaurantMQ.gui.RestaurantGui;
import city.Building;
import city.Building.BuildingType;

public class BuildingPanel {
	
	MainGui mainGui;

	public BuildingPanel(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	public void displayBuildingPanel(Building building) {
		switch (building.type) {
		case restaurant:
			if (building.name.equals("restaurant")) {
				mainGui.restaurantGui1.setVisible(true);
			}
			break;
		
		case bank:
			//add bank panel and gui later
			break;
			
		case home:
		case room:
			//add home panel and gui later
			break;
			
		case market:
			mainGui.marketGui1.setVisible(true);
			break;
			
		default: 
			break;
		}

	}
}