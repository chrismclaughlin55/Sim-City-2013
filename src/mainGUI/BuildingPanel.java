package mainGUI;
import city.*;

import java.util.List;

import javax.swing.JFrame;

import Gui.Gui;
import market.gui.MarketGui;
import restaurantMQ.gui.RestaurantGui;
import city.Building;
import city.Building.BuildingType;
import city.Home;

public class BuildingPanel {
	
	MainGui mainGui;

	public BuildingPanel(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	public void displayBuildingPanel(Building building, int buildingNumber) {
		switch (building.type) {
		case restaurant:
			for (int i = 0; i < 6; i++) {
				if (building.name.equals("restaurant"+(i+1))) {
					mainGui.restaurantGuis[i].setVisible(true);
				}
			}
			break;
		
		case bank:
			mainGui.bankGui.setVisible(true);
			break;
			
		case home:
			Home h = (Home) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			h.homeGui.setVisible(true);
		case apartment:
			Home a = (Home) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			a.homeGui.setVisible(true);
		case room:
			//add home panel and gui later
			break;
			
		case market:
			mainGui.marketGui.setVisible(true);
			break;
			
		default: 
			break;
		}

	}
}