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
			if (building.name.equals("restaurant1")) {
				mainGui.restaurantGui1.setVisible(true);
			}
			if (building.name.equals("restaurant2")) {
				mainGui.restaurantGui2.setVisible(true);
			}
			if (building.name.equals("restaurant3")) {
				mainGui.restaurantGui3.setVisible(true);
			}
			if (building.name.equals("restaurant4")) {
				mainGui.restaurantGui4.setVisible(true);
			}
			if (building.name.equals("restaurant5")) {
				mainGui.restaurantGui5.setVisible(true);
			}
			if (building.name.equals("restaurant6")) {
				mainGui.restaurantGui6.setVisible(true);
			}
			break;
		
		case bank:
			mainGui.bankGui.setVisible(true);
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