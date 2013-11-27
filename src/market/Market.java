package market;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import mainGUI.MainGui;
import market.gui.CustomerGui;
import market.gui.EmployeeGui;
import market.gui.ManagerGui;
import market.gui.MarketGui;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Role;

public class Market extends Building {

	public Inventory inventory = null;
	public MarketManagerRole currentManager = null;
	MainGui mainGui;
	public MarketGui marketGui;

	private int testIteration = 0;
	
	public boolean isOpenForEmployees = false;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		this.mainGui = mainGui;
		super.type = BuildingType.market;

	}

	public Market(int i, int j, int width, int height, String string, BuildingType apartment, MainGui mainGui, CityData cd) {
		super(i, j, width, height, string, apartment, mainGui);
		//public Market(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui, CityData cd) {
		cityData = cd;
		this.mainGui = mainGui;
		MarketData chickenData = new MarketData("Chicken", 10, 5.99);
		MarketData saladData = new MarketData("Salad", 10, 3.99);
		MarketData steakData = new MarketData("Steak", 10, 11.99);
		MarketData pizzaData = new MarketData("Pizza", 10, 7.99);


		marketGui = new MarketGui(this);
		marketGui.setTitle("Market");
		marketGui.setVisible(false);
		marketGui.setResizable(false);
		marketGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		inventory = new Inventory(chickenData, saladData, steakData, pizzaData, marketGui);
		marketGui.updateMarketPanel();
	}


	protected Map<PersonAgent, MarketManagerRole> existingManagerRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketManagerRole>());
	protected Map<PersonAgent, MarketCustomerRole> existingCustomerRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketCustomerRole>());
	protected Map<PersonAgent, MarketEmployeeRole> existingEmployeeRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketEmployeeRole>());




	public void EnterBuilding(PersonAgent person, String roleRequest) {

		if (person.equals(manager)) {

			if (existingManagerRoles.get(person) != null) {
				currentManager = existingManagerRoles.get(person);
				ManagerGui managerGui = new ManagerGui(existingManagerRoles.get(person));
				currentManager.setGui(managerGui);
				marketGui.animationPanel.addGui(managerGui);
				person.msgAssignRole(currentManager);
			}
			else {
				currentManager = new MarketManagerRole(person, inventory, this);
				existingManagerRoles.put(person,currentManager);
				ManagerGui managerGui = new ManagerGui(currentManager);
				currentManager.setGui(managerGui);
				marketGui.animationPanel.addGui(managerGui);
				person.msgAssignRole(currentManager);
			}
		}

		else if (roleRequest.equals("MarketEmployee")) {
			if (isOpenForEmployees) {
				if (existingEmployeeRoles.get(person) != null) {
					EmployeeGui employeeGui = new EmployeeGui(existingEmployeeRoles.get(person));
					existingEmployeeRoles.get(person).setGui(employeeGui);
					marketGui.animationPanel.addGui(employeeGui);
					person.msgAssignRole(existingEmployeeRoles.get(person));
					currentManager.msgReportingForWork(existingEmployeeRoles.get(person));
				}
				else {
					MarketEmployeeRole employeeRole = new MarketEmployeeRole(person, currentManager, inventory);
					existingEmployeeRoles.put(person,employeeRole);
					EmployeeGui employeeGui = new EmployeeGui(employeeRole);
					employeeRole.setGui(employeeGui);
					marketGui.animationPanel.addGui(employeeGui);
					person.msgAssignRole(employeeRole);
					currentManager.msgReportingForWork(employeeRole);
				}
			}
		}

		else if (roleRequest.equals("MarketCustomer")) {

			if (isOpen()) {

				if (existingCustomerRoles.get(person) != null) {
					CustomerGui customerGui = new CustomerGui(existingCustomerRoles.get(person));
					existingCustomerRoles.get(person).setGui(customerGui);
					marketGui.animationPanel.addGui(customerGui);
					person.msgAssignRole(existingCustomerRoles.get(person));
				}
				else {
					MarketCustomerRole customerRole = new MarketCustomerRole(person, currentManager, person.thingsToOrder);
					existingCustomerRoles.put(person,customerRole);
					CustomerGui customerGui = new CustomerGui(customerRole);
					customerRole.setGui(customerGui);
					marketGui.animationPanel.addGui(customerGui);
					person.msgAssignRole(customerRole);
				}
			}
			else {
				person.exitBuilding();
			}
				
		}
	}


	public void test(){

		if (testIteration < 2) {
			System.out.println ("testing market");

			if (testIteration < 1) {
				PersonAgent p1 = new PersonAgent("Manager");
				p1.startThread();
				manager = p1;
				EnterBuilding(p1, "MarketManager");

				PersonAgent p2 = new PersonAgent("Employee");
				p2.startThread();
				EnterBuilding(p2, "MarketEmployee");
			}

			if (isOpen()) {
				PersonAgent p3 = new PersonAgent("Customer");
				p3.startThread();
				EnterBuilding(p3, "MarketCustomer");
			}

			testIteration++;
		}
	}


}
