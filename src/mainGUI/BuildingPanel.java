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
		if (building.type == BuildingType.restaurant) {
			if (building.name.equals("restaurant")) {
				RestaurantGui restGui = new RestaurantGui();
				restGui.setTitle("restaurantMQ");
				restGui.setVisible(true);
				restGui.setResizable(false);
				restGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		}
		
		switch (building.type) {	
		
		case bank:
			//add bank panel and gui later
			break;
			
		case home:
		case room:
			//add home panel and gui later
			break;
			
		case market:
			//mainGui.marketGui1.setVisible(true);
			MarketGui marketGui = new MarketGui();
			marketGui.setTitle("market1");
			marketGui.setVisible(true);
			marketGui.setResizable(false);
			marketGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			break;
			
		default: 
			break;
		}
		/*if (building.type == BuildingType.apartment) {
			// add apartment panel and gui later
		}
		if (building.type == BuildingType.bank) {
			// add bank panel and gui later
		}
		if (building.type == BuildingType.home) {
			// add home panel and gui later
		}
		if (building.type == BuildingType.market) {
			//mainGui.marketGui1.setVisible(true);
			MarketGui marketGui = new MarketGui();
			marketGui.setTitle("market1");
			marketGui.setVisible(true);
			marketGui.setResizable(false);
			marketGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}*/
	}
}