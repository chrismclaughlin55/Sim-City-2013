package restaurantCM.gui;

import java.awt.Container;

import javax.swing.JFrame;

import bank.utilities.CustInfo;
import mainGUI.MainGui;
import restaurantCM.*;
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
	CMHostRole host;
	CMCookRole cook;
	CMCashierRole cashier;
	public CMRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);
		restGui = new CMRestaurantGui(this);
		restPanel = restGui.restPanel;
	}

	public void EnterBuilding(PersonAgent person, String roleRequest){
		System.out.println(person.getName() + ": Entering RestaurantCM as " + roleRequest);
		//			if(roleRequest.equals("Customer"))
		//			{
		//				restPanel.addPerson("Customer", person);
		//			}
			addPerson(roleRequest, person);
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
	public void addPerson(String type, PersonAgent person) {
		person.print("attempting to enter CMRest as "+type);
				if(type.equals("Host")){
					CMHostRole host = new CMHostRole(person);
					CMHostGui g = new CMHostGui(host);
					restGui.animationPanel.addGui(g);
					this.host = host;
					this.manager = person;
					person.msgAssignRole(host);
					isOpen = true;
				}
				if (type.equals("Customer")) {
					CMCustomerRole c = new CMCustomerRole(person);	
					CMCustomerGui g = new CMCustomerGui(c, restGui);
					restGui.animationPanel.addGui(g);// dw
					c.setHost(host);
					c.setGui(g);
					person.msgAssignRole(c);
					
					//c.startThread();
				//	gui.myAgents.add(c);
				}
				if(type.equals("Waiter")){
					addWaiter(person);
				}
				if(type.equals("Cook")){
					addCook(person);
				}
				if(type.equals("Cashier")){
					addCashier(person);
				}
			}
private void addCashier(PersonAgent person) {
		CMCashierRole cashier = new CMCashierRole(person);
		this.cashier = cashier;
		for(CMWaiterRole w : restPanel.waiters){
			w.setCashier(this.cashier);
		}
		person.msgAssignRole(cashier);
	}
private void addCook(PersonAgent person) {
		CMCookRole cook = new CMCookRole(person);
		this.cook = cook;
		for(CMWaiterRole w : restPanel.waiters){
			w.setCook(this.cook);
		}
		person.msgAssignRole(cook);
	}
public void addWaiter(PersonAgent person){
		CMWaiterRole W = new CMWaiterRole(person);
		CMWaiterGui g = new CMWaiterGui(W, 200 , 30*host.Waiters.size());
		W.setHost(host);
		host.addWaiter(W);
		W.setMyGui(g);
		restPanel.waiters.add(W);
		restGui.animationPanel.addGui(g);
	//	gui.myAgents.add(W);
	//	W.startThread();
	//	updateRestLabel();
		if(cashier != null){
			W.setCashier(cashier);
		}
		if(cook != null)
			W.setCook(cook);
		person.msgAssignRole(W);
	}
	public void test(){
		PersonAgent p1 = new PersonAgent("CMhost");
		
		p1.setHunger(0);
		//p1.startThread();
		p1.goToWork = true;
		manager = p1;
		EnterBuilding(p1, "Host");

		PersonAgent p2 = new PersonAgent("CMCook");
		//p2.startThread();
		p2.goToWork = true;
		EnterBuilding(p2, "Cook");


		PersonAgent p3 = new PersonAgent("CMCashier");
		//p3.startThread();
		p3.goToWork = true;
		EnterBuilding(p3, "Cashier");
		
		PersonAgent p4 = new PersonAgent("CMCustomer");
		//p4.startThread();
		p4.goToWork = true;
		EnterBuilding(p3, "Customer");
	}
}

