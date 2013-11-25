package market;

import mainGUI.MainGui;
import market.gui.CustomerGui;
import market.gui.EmployeeGui;
import market.gui.ManagerGui;
import market.gui.MarketGui;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class Market extends Building {

	public Inventory inventory = null;
	MarketManagerRole currentManager = null;
	public MarketGui marketGui;
	
	public Market(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		
	}

	public Market(int i, int j, int width, int height, String string, BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, width, height, string, apartment, mainGui);
	//public Market(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui, CityData cd) {
		cityData = cd;
		inventory = new Inventory();
	}

	public void EnterBuilding(PersonAgent person, String roleRequest) {
		if (isOpen()) {
			if (existingRoles.get(person) != null) {
				person.msgAssignRole(existingRoles.get(person));
				if (roleRequest.equals("MarketCustomer")) {
					CustomerGui customerGui = new CustomerGui();
					marketGui.animationPanel.addGui(customerGui);
					
				}
				if (roleRequest.equals("MarketEmployee")) {
					EmployeeGui employeeGui = new EmployeeGui();
					marketGui.animationPanel.addGui(employeeGui);
				}
				if (roleRequest.equals("MarketManager")) {
					ManagerGui managerGui = new ManagerGui();
					marketGui.animationPanel.addGui(managerGui);
				}
				
			}
			else if (roleRequest.equals("MarketCustomer")) {
				MarketCustomerRole custRole = new MarketCustomerRole(person, currentManager, person.thingsToOrder);
				person.msgAssignRole(custRole);
				existingRoles.put(person, custRole);
				CustomerGui customerGui = new CustomerGui();
				marketGui.animationPanel.addGui(customerGui);
			}
		}
		else {
			if (roleRequest.equals("MarketEmployee")) {
				MarketEmployeeRole employeeRole = new MarketEmployeeRole(person, currentManager, inventory);
				person.msgAssignRole(employeeRole);
				existingRoles.put(person, employeeRole);
			}
			else if (person.equals(manager)) {
				if (existingRoles.get(person) != null) {
					person.msgAssignRole(existingRoles.get(person));
					currentManager = (MarketManagerRole) existingRoles.get(person);
				}
				else {
					currentManager = new MarketManagerRole(person, inventory, this);
					person.msgAssignRole(currentManager);
				}
			}
			//else p.msgAccessDenied()
		}

	}

	public void addMarketGui(MarketGui marketGui) {
		this.marketGui = marketGui;
	}
}
