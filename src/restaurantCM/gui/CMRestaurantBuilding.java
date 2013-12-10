package restaurantCM.gui;

import javax.swing.JFrame;

import bank.utilities.CustInfo;
import mainGUI.MainGui;
import restaurantCM.gui.CMRestaurantGui;
import restaurantCM.gui.CMRestaurantPanel;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class CMRestaurantBuilding extends Building {
	public static final int MAXCUSTOMERS = 8;

	CMRestaurantGui restGui;
	CMRestaurantPanel restPanel;

	public CMRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new CMRestaurantGui();
		restPanel = restGui.restPanel;
	}

	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		System.out.println(person.getName() + ": Entering RestaurantCM as " + roleRequest);
		//			if(roleRequest.equals("Customer"))
		//			{
		//				restPanel.addPerson("Customer", person);
		//			}
		if(roleRequest.equals("Waiter")){
			restPanel.addWaiter(person);
		}
		else 
			restPanel.addPerson(roleRequest, person);
		//			}
		//			else if(roleRequest.equals("Cook"))
		//			{
		//				restPanel.addPerson(roleRequest, person);
		//			}
		//			else if(roleRequest.equals("Cashier"))
		//			{
		//				restPanel.addCashier(person);
		//			}
		//			else if(roleRequest.equals("Host"))
		//			{
		//				restPanel.addHost(person);
		//				isOpen = true;
		//			}

	}

	public boolean isOpen()
	{	
		return isOpen && (restPanel.activeCustomers() < MAXCUSTOMERS) && restPanel.fullyStaffed();
	}

	public boolean openToEmployee()
	{
		return isOpen && restPanel.hasHost();
	}

	public boolean hasHost()
	{
		return restPanel.hasHost();
	}

	public boolean hasCashier()
	{
		return restPanel.hasCashier();
	}

	public CMRestaurantGui getRestaurantGui()
	{
		return restGui;
	}

	public void setOpen(Boolean b) {
		isOpen = b;

	}

	public JFrame getBuildingGui() {
		return restGui;
	}
	public void test(){
		PersonAgent p1 = new PersonAgent("CMhost");
		p1.startThread();
		manager = p1;
		EnterBuilding(p1, "Host");

		PersonAgent p2 = new PersonAgent("CMCook");
		p2.startThread();
		EnterBuilding(p2, "Cook");


		PersonAgent p3 = new PersonAgent("CMCashier");
		p3.startThread();
		EnterBuilding(p3, "Cashier");
		
		PersonAgent p4 = new PersonAgent("CMCustomer");
		p3.startThread();
		EnterBuilding(p3, "Customer");
	}
}

