package mainGUI;

import javax.swing.JFrame;

import restaurantMQ.gui.RestaurantGui;
import city.Building;
import city.Building.BuildingType;

public class BuildingPanel {
	public BuildingPanel() {
		
	}
	
	public void displayBuildingPanel(Building building) {
		if (building.type == BuildingType.restaurant) {
			if (building.name.equals("restaurant")) {
				RestaurantGui restGui = new RestaurantGui();
				restGui.setTitle("restaurantMQ");
				restGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		}
		if (building.type == BuildingType.apartment) {
			// add apartment panel and gui later
		}
		if (building.type == BuildingType.bank) {
			// add bank panel and gui later
		}
		if (building.type == BuildingType.home) {
			// add home panel and gui later
		}
		if (building.type == BuildingType.market) {
			// add market panel and gui later
		}
	}
}