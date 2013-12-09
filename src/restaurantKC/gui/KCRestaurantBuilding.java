package restaurantKC.gui;

import javax.swing.JFrame;

import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;

public class KCRestaurantBuilding extends Building
{
	public static final int MAXCUSTOMERS = 8;
	
	RestaurantGui restGui;
	RestaurantPanel restPanel;
	
	public KCRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new RestaurantGui(this);
		restPanel = restGui.restPanel;
	}
	
	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		System.out.println(person.getName() + ": Entering RestaurantKC as " + roleRequest);
		if(roleRequest.equals("Customer"))
		{
			restPanel.addCustomer(person);
		}
		else if(roleRequest.equals("Waiter"))
		{
			restPanel.addWaiter(person);
		}
		else if(roleRequest.equals("Cook"))
		{
			restPanel.addCook(person);
		}
		else if(roleRequest.equals("Cashier"))
		{
			restPanel.addCashier(person);
		}
		else if(roleRequest.equals("Host"))
		{
			restPanel.addHost(person);
			isOpen = true;
		}
		
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
