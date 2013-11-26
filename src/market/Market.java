package market;

import javax.swing.JFrame;

import mainGUI.MainGui;
import market.gui.CustomerGui;
import market.gui.EmployeeGui;
import market.gui.ManagerGui;
import market.gui.MarketGui;
import city.Building;
import city.CityData;
import city.PersonAgent;

public class Market extends Building {

	public Inventory inventory = null;
	MarketManagerRole currentManager = null;
	MainGui mainGui;
	public MarketGui marketGui;

	private int testIteration = 0;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		this.mainGui = mainGui;
		
	}

	public Market(int i, int j, int width, int height, String string, BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, width, height, string, apartment, mainGui);
		//public Market(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui, CityData cd) {
		cityData = cd;
		this.mainGui = mainGui;
		
		MarketData chickenData = new MarketData("chicken", 10, 5.99);
		MarketData saladData = new MarketData("salad", 10, 3.99);
		MarketData steakData = new MarketData("steak", 10, 11.99);
		MarketData pizzaData = new MarketData("pizza", 10, 7.99);
		
		
		marketGui = new MarketGui(this);
        marketGui.setTitle("Market");
        marketGui.setVisible(false);
        marketGui.setResizable(false);
        marketGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
		inventory = new Inventory(chickenData, saladData, steakData, pizzaData, marketGui);
		marketGui.updateMarketPanel();
	}

	public void EnterBuilding(PersonAgent person, String roleRequest) {
		if (isOpen()) {
			if (existingRoles.get(person) != null) {
				person.msgAssignRole(existingRoles.get(person));
				if (roleRequest.equals("MarketCustomer")) {
					CustomerGui customerGui = new CustomerGui((MarketCustomerRole)existingRoles.get(person));
					((MarketCustomerRole) existingRoles.get(person)).setGui(customerGui);
					marketGui.animationPanel.addGui(customerGui);
					//currentManager.msgNeedToOrder((MarketCustomerRole) existingRoles.get(person));
				}
				if (roleRequest.equals("MarketEmployee")) {
					currentManager.msgReportingForWork((MarketEmployeeRole) existingRoles.get(person));
					EmployeeGui employeeGui = new EmployeeGui((MarketEmployeeRole) existingRoles.get(person));
					((MarketEmployeeRole) existingRoles.get(person)).setGui(employeeGui);
					marketGui.animationPanel.addGui(employeeGui);
				}
				if (roleRequest.equals("MarketManager")) {
					ManagerGui managerGui = new ManagerGui((MarketManagerRole) existingRoles.get(person));
					((MarketManagerRole) existingRoles.get(person)).setGui(managerGui);
					marketGui.animationPanel.addGui(managerGui);
				}

			}
			else if (roleRequest.equals("MarketCustomer")) {
				System.out.println("creating a market customer");
				MarketCustomerRole custRole = new MarketCustomerRole(person, currentManager, person.thingsToOrder);
				CustomerGui customerGui = new CustomerGui(custRole);
				custRole.setGui(customerGui);
				existingRoles.put(person, custRole);
				marketGui.animationPanel.addGui(customerGui);
				person.msgAssignRole(custRole);
			}
		}
		else {
			if (roleRequest.equals("MarketEmployee")) {
				MarketEmployeeRole employeeRole = new MarketEmployeeRole(person, currentManager, inventory);
				person.msgAssignRole(employeeRole);
				existingRoles.put(person, employeeRole);
				currentManager.msgReportingForWork(employeeRole);
				EmployeeGui employeeGui = new EmployeeGui(employeeRole);
				employeeRole.setGui(employeeGui);
				marketGui.animationPanel.addGui(employeeGui);
			}
			else if (person.equals(manager)) {
				if (existingRoles.get(person) != null) {
					currentManager = (MarketManagerRole) existingRoles.get(person);
					ManagerGui managerGui = new ManagerGui(currentManager);
					((MarketManagerRole) existingRoles.get(person)).setGui(managerGui);
					mainGui.marketGui.animationPanel.addGui(managerGui);
					person.msgAssignRole(existingRoles.get(person));

				}
				else {
					currentManager = new MarketManagerRole(person, inventory, this);
					person.msgAssignRole(currentManager);
					existingRoles.put(person, currentManager);
					ManagerGui managerGui = new ManagerGui(currentManager);
					currentManager.setGui(managerGui);
					marketGui.animationPanel.addGui(managerGui);
				}
			}
			//else p.msgAccessDenied()
		}

	}

	public void test(){

		if (testIteration < 2) {

			System.out.println ("testing market");
			PersonAgent p1 = new PersonAgent("Manager");
			p1.startThread();
			manager = p1;
			EnterBuilding(p1, "MarketManager");

			PersonAgent p2 = new PersonAgent("Employee");
			p2.startThread();
			EnterBuilding(p2, "MarketEmployee");

			PersonAgent p3 = new PersonAgent("Customer");
			p3.startThread();
			EnterBuilding(p3, "MarketCustomer");

			testIteration++;
		}
	}


}
