package restaurantCM.gui;

import javax.swing.JFrame;

import mainGUI.MainGui;
import restaurantCM.gui.RestaurantGui;
import restaurantCM.gui.RestaurantPanel;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class CMRestaurantBuilding extends Building {
	public static final int MAXCUSTOMERS = 8;

	RestaurantGui restGui;
	RestaurantPanel restPanel;

	public CMRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new RestaurantGui();
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

	public RestaurantGui getRestaurantGui()
	{
		return restGui;
	}

	public void setOpen(Boolean b) {
		isOpen = b;

	}

	public JFrame getBuildingGui() {
		return restGui;
	}
}

