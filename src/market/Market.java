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
	MainGui mainGui;
	private int numEmployees = 0;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		this.mainGui = mainGui;
	}

	public Market(int i, int j, int width, int height, String string, BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, width, height, string, apartment, mainGui);
		//public Market(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui, CityData cd) {
		cityData = cd;
		this.mainGui = mainGui;
		inventory = new Inventory();
	}

	public void EnterBuilding(PersonAgent person, String roleRequest) {
		if (isOpen()) {
			if (existingRoles.get(person) != null) {
				person.msgAssignRole(existingRoles.get(person));
				if (roleRequest.equals("MarketCustomer")) {
					CustomerGui customerGui = new CustomerGui();
					mainGui.marketGui.animationPanel.addGui(customerGui);
				}
				if (roleRequest.equals("MarketEmployee")) {
					currentManager.msgReportingForWork((MarketEmployeeRole) existingRoles.get(person));
					EmployeeGui employeeGui = new EmployeeGui((MarketEmployeeRole) existingRoles.get(person), 70+(35*numEmployees), 205);
					mainGui.marketGui.animationPanel.addGui(employeeGui);
					numEmployees++;
					EmployeeGui employeeGui2 = new EmployeeGui((MarketEmployeeRole) existingRoles.get(person), 70+(35*numEmployees), 205);
					mainGui.marketGui.animationPanel.addGui(employeeGui2);
				}
				if (roleRequest.equals("MarketManager")) {
					ManagerGui managerGui = new ManagerGui((MarketManagerRole) existingRoles.get(person));
					mainGui.marketGui.animationPanel.addGui(managerGui);
				}

			}
			else if (roleRequest.equals("MarketCustomer")) {
				MarketCustomerRole custRole = new MarketCustomerRole(person, currentManager, person.thingsToOrder);
				person.msgAssignRole(custRole);
				existingRoles.put(person, custRole);
				CustomerGui customerGui = new CustomerGui();
				mainGui.marketGui.animationPanel.addGui(customerGui);
			}
		}
		else {
			if (roleRequest.equals("MarketEmployee")) {
				MarketEmployeeRole employeeRole = new MarketEmployeeRole(person, currentManager, inventory);
				person.msgAssignRole(employeeRole);
				existingRoles.put(person, employeeRole);
				currentManager.msgReportingForWork(employeeRole);
				EmployeeGui employeeGui = new EmployeeGui(employeeRole, 70+(35*numEmployees), 205);
				numEmployees++;
				mainGui.marketGui.animationPanel.addGui(employeeGui);
			}
			else if (person.equals(manager)) {
				if (existingRoles.get(person) != null) {
					person.msgAssignRole(existingRoles.get(person));
					currentManager = (MarketManagerRole) existingRoles.get(person);
					ManagerGui managerGui = new ManagerGui(currentManager);
					mainGui.marketGui.animationPanel.addGui(managerGui);
				}
				else {
					currentManager = new MarketManagerRole(person, inventory, this);
					person.msgAssignRole(currentManager);
					existingRoles.put(person, currentManager);
					ManagerGui managerGui = new ManagerGui(currentManager);
					mainGui.marketGui.animationPanel.addGui(managerGui);
				}
			}
			//else p.msgAccessDenied()
		}

	}

	public void test(){
		System.out.println ("testing market");
		PersonAgent p1 = new PersonAgent("Manager");
		p1.startThread();
		manager = p1;
		EnterBuilding(p1, "MarketManager");
		
		PersonAgent p2 = new PersonAgent("Employee");
		p2.startThread();
		EnterBuilding(p2, "MarketEmployee");
		
		PersonAgent p3 = new PersonAgent("Customer");
		p2.startThread();
		EnterBuilding(p3, "MarketCustomer");
		
		//ManagerGui managerGui = new ManagerGui();
		//animationPanel.addGui(managerGui);
	}
}
