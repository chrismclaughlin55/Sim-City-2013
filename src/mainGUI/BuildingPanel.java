package mainGUI;
import bank.Bank;
import market.Market;
import city.Apartment;
import city.Building;
import city.Home;

public class BuildingPanel {
	
	MainGui mainGui;

	public BuildingPanel(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	public void displayBuildingPanel(Building building, int buildingNumber) {
		switch (building.type) {
		case restaurant:
			building.getBuildingGui().setVisible(true);
			break;
			
		case bank:

			Bank b = (Bank) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			b.bankGui.setVisible(true);
			//mainGui.bankGui.setVisible(true);
			//mainGui.bankGui.bank.test();
			break;
			
		case home:
			Home h = (Home) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			h.homeGui.setVisible(true);
			break;
			
		case apartment:
			Apartment a = (Apartment) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			a.apartmentGui.setVisible(true);
			break;
			
		case market:
			Market m = (Market) mainGui.mainAnimationPanel.cd.buildings.get(buildingNumber);
			//m.addMarketGui(mainGui.marketGui);
			m.marketGui.setVisible(true);
			//m.test();
			break;
			
		default: 
			break;
		}

	}
}