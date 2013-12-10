package restaurantSM.gui;

import javax.swing.JFrame;

import trace.AlertLog;
import trace.AlertTag;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;

public class SMRestaurantBuilding extends Building
{
	public static final int MAXCUSTOMERS = 8;
	
	RestaurantGui restGui;
	RestaurantPanel restPanel;
	
	private boolean printed = false;
	
	public SMRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);

		restGui = new RestaurantGui(this);

		restPanel = restGui.getRestaurantPanel();
	}
	
	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		System.out.println(person.getName() + ": Entering RestaurantSM as " + roleRequest);
		if(roleRequest.equals("Customer")) {
			restPanel.addCustomer(person);
		}
		else if(roleRequest.equals("Waiter")) {
			restPanel.addWaiter(person);
		}
		else if(roleRequest.equals("Cook")) {
			restPanel.addCook(person);
		}
		else if(roleRequest.equals("Cashier")) {
			restPanel.addCashier(person);
		}
		else if(roleRequest.equals("Host")) {
			restPanel.addHost(person);
			isOpen = true;
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTSM, this.name, "RestaurantSM is open for employees only");
		}
		
	}

	public JFrame getBuildingGui() {
		return restGui;
	}
	
	public boolean isOpen() {
		if(isOpen && (restPanel.activeCustomers() < MAXCUSTOMERS) && restPanel.fullyStaffed() && !printed) {
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTLY, this.name, "RestaurantLY is fully employed");
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTLY, this.name, "RestaurantLY is open now");
			printed = true;
		}
		return isOpen && (restPanel.activeCustomers() < MAXCUSTOMERS) && restPanel.fullyStaffed();
	}
	
	public boolean openToEmployee() {
		return isOpen && restPanel.activeHost();
	}
	
	public boolean hasHost() {
		return restPanel.activeHost();
	}
	
	public boolean hasCashier() {
		return restPanel.activeCashier();
	}
	
	public RestaurantGui getRestaurantGui() {
		return restGui;
	}

	public void setOpen(Boolean b) {
		isOpen = b;
		
	}
}
