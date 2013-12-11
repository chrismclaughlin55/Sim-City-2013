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
import city.TestPerson;

public class Market extends Building {
	TestPerson manager;
	public Inventory inventory = null;
	public MarketManagerRole currentManager = null;
	MainGui mainGui;
	public MarketGui marketGui;
	private int marketNum;
	public final int CLOSINGTIME = 22;


	public boolean isOpenForEmployees = false;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui, int num) {
		super(xPos, yPos, width, height, mainGui);
		this.mainGui = mainGui;
		marketNum = num;
		super.type = BuildingType.market;

		MarketData chickenData = new MarketData("Chicken", 1000, 5.99);
		MarketData saladData = new MarketData("Salad", 1000, 3.99);
		MarketData steakData = new MarketData("Steak", 1000, 11.99);
		MarketData pizzaData = new MarketData("Pizza", 1000, 7.99);
		inventory = new Inventory(chickenData, saladData, steakData, pizzaData, marketGui);

		marketGui = new MarketGui(this);
		marketGui.setTitle("Market");
		marketGui.setVisible(false);
		marketGui.setResizable(false);
		marketGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		inventory.setGui(marketGui);

		marketGui.updateMarketPanel();

		//test();
	}

	public Market(int i, int j, int width, int height, String string, BuildingType apartment, MainGui mainGui, CityData cd, int num) {
		super(i, j, width, height, string, apartment, mainGui);
		//public Market(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui, CityData cd) {
		cityData = cd;
		marketNum = num;
		this.mainGui = mainGui;
		MarketData chickenData = new MarketData("Chicken", 1000, 5.99);
		MarketData saladData = new MarketData("Salad", 1000, 3.99);
		MarketData steakData = new MarketData("Steak", 1000, 11.99);
		MarketData pizzaData = new MarketData("Pizza", 1000, 7.99);

		inventory = new Inventory(chickenData, saladData, steakData, pizzaData, marketGui);

		marketGui = new MarketGui(this);
		marketGui.setTitle("Market");
		marketGui.setVisible(false);
		marketGui.setResizable(false);
		marketGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		inventory.setGui(marketGui);

		marketGui.updateMarketPanel();

		//test();

	}


	protected Map<PersonAgent, MarketManagerRole> existingManagerRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketManagerRole>());
	protected Map<PersonAgent, MarketCustomerRole> existingCustomerRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketCustomerRole>());
	protected Map<PersonAgent, MarketEmployeeRole> existingEmployeeRoles = Collections.synchronizedMap(new HashMap<PersonAgent, MarketEmployeeRole>());




	public void EnterBuilding(PersonAgent person, String roleRequest) {

		System.out.println("Attempting to enter Market as a " + roleRequest);

		if (person.equals(super.manager)) {

			if (existingManagerRoles.get(person) != null) {
				currentManager = existingManagerRoles.get(person);
				ManagerGui managerGui = new ManagerGui(existingManagerRoles.get(person));
				currentManager.setGui(managerGui);
				marketGui.animationPanel.addGui(managerGui);
				person.msgAssignRole(currentManager);
			}
			else {
				currentManager = new MarketManagerRole(person, inventory, this, marketNum);
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
					MarketEmployeeRole employeeRole = new MarketEmployeeRole(person, currentManager, inventory, this);
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

	@Override
	public JFrame getBuildingGui() {
		// TODO Auto-generated method stub
		return marketGui;
	}

	/*public void EnterBuilding(TestPerson person, String roleRequest) {

		System.out.println("Attempting to enter Market as a " + roleRequest);

		if (person.equals(this.manager)) {

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
				System.out.println (person.getName() + " is exiting the building");
				//person.exitBuilding();
			}

		}
	}*/


	/*public void test(){

		System.out.println ("testing market");


		TestPerson p1 = new TestPerson("Manager");
		p1.startThread();
		this.manager = p1;
		EnterBuilding(p1, "MarketManager");
		
		while(!isOpenForEmployees){
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}

		TestPerson p2 = new TestPerson("Employee1");
		p2.startThread();
		EnterBuilding(p2, "MarketEmployee");
		
		TestPerson p3 = new TestPerson("Employee2");
		p3.startThread();
		EnterBuilding(p3, "MarketEmployee");


		while(!isOpen()){
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}


		TestPerson p4 = new TestPerson("Customer1");
		p4.startThread();
		EnterBuilding(p4, "MarketCustomer");
		
		TestPerson p5 = new TestPerson("Customer2");
		p5.setOrders();
		p5.startThread();
		EnterBuilding(p5, "MarketCustomer");
	}*/


	/*public void setOpen(TestPerson p) {
		if (p.equals(manager)) {
			isOpen = true;
		}
	}


	public void setClosed(TestPerson p) {
		if (p.equals(manager)) {
			isOpen = false;
		}
	}*/


}
