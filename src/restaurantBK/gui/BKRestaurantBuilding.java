package restaurantBK.gui;

import java.io.IOException;

import javax.swing.JFrame;

import restaurantBK.gui.RestaurantGui;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class BKRestaurantBuilding extends Building {

	RestaurantGui restGui;
	RestaurantPanel restPanel;
	public BKRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) throws IOException 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new RestaurantGui();
		this.restPanel = restGui.restPanel;
		// TODO Auto-generated constructor stub
	}

	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		System.out.println(person.getName() + ": Entering RestaurantBK as " + roleRequest);
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
		if(restPanel.hasHost()&&restPanel.hasCashier()&&restPanel.hasCook()&&restPanel.hasWaiters()) {
			restPanel.fullyStaffed = true;
		}
		else {
			restPanel.fullyStaffed = false;
		}
	}
	
	public boolean isOpen()
	{	
		return isOpen && restPanel.fullyStaffed;
	}
	
	public boolean openToEmployee()
	{
		return restPanel.hasHost();
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

	public void setOpen(boolean b) {
		isOpen = b;
		
	}
	
	@Override
	public JFrame getBuildingGui() {
		// TODO Auto-generated method stub
		return restGui;
	}

}
