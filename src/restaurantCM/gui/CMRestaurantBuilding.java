package restaurantCM.gui;

import java.awt.Container;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import bank.utilities.CustInfo;
import mainGUI.MainGui;
import restaurantCM.*;
import restaurantCM.gui.CMRestaurantGui;
import restaurantCM.gui.CMRestaurantPanel;
import trace.AlertLog;
import trace.AlertTag;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class CMRestaurantBuilding extends Building {
	public static final int CLOSINGTIME = 19;
	public static final int MAXCUSTOMERS = 8;
	private Semaphore enter = new Semaphore(1, true);
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
		try {
			enter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
enter.release();
	}

	public boolean isOpen()
	{	
		return isOpen  && this.fullyStaffed();
	}

	public boolean openToEmployee()
	{
		return isOpen && (this.host != null);
	}

	public CMRestaurantGui getRestaurantGui()
	{
		return restGui;
	}

	public void setOpen(Boolean b) {
		isOpen = b;

	}
	public boolean fullyStaffed() {
		if(this.cook != null && this.cashier != null && host.Waiters.size()>0)
			return true;
		return false;
	}
	public JFrame getBuildingGui() {
		return restGui;
	}
	public void addPerson(String type, PersonAgent person) {
		person.print("attempting to enter CMRest as "+type);
				if(type.equals("Host")){
					CMHostRole host = new CMHostRole(person, this);
					CMHostGui g = new CMHostGui(host);
					restGui.animationPanel.addGui(g);
					this.host = host;
					this.manager = person;
					host.setGui(g);
					person.msgAssignRole(host);
					isOpen = true;
					AlertLog.getInstance().logInfo(AlertTag.RESTAURANTCM, this.name, "RestaurantCM is open for employees only");
				}
				if (type.equals("Customer")) {
					CMCustomerRole c = new CMCustomerRole(person);	
					CMCustomerGui g = new CMCustomerGui(c, restGui);
					restGui.animationPanel.addGui(g);// dw
					c.setHost(host);
					c.setGui(g);
					person.msgAssignRole(c);
					c.getGui().setPresent(true);
					c.gotHungry();
					AlertLog.getInstance().logInfo(AlertTag.RESTAURANTCM, this.name, "RestaurantCM has a hungry cust");
					
					//c.startThread();
				//	gui.myAgents.add(c);
				}
				if(type.equals("Waiter")){
					addWaiter(person);
					AlertLog.getInstance().logInfo(AlertTag.RESTAURANTCM, this.name, "RestaurantCM has a waiter");
				}
				if(type.equals("Cook")){
					addCook(person);
					AlertLog.getInstance().logInfo(AlertTag.RESTAURANTCM, this.name, "RestaurantCM has a cook");
				}
				if(type.equals("Cashier")){
					addCashier(person);
					AlertLog.getInstance().logInfo(AlertTag.RESTAURANTCM, this.name, "RestaurantCM has a cashier");
				}
			}
	public boolean hasHost(){
		if(host!=null)
			return true;
		return false;
	}
	public boolean hasCashier(){
		if(cashier!=null)
			return true;
		return false;
	}
	public boolean hasCook(){
		if(cook!=null)
			return true;
		return false;
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
//	public void test(){
//		PersonAgent p1 = new PersonAgent("CMhost");
//		
//		p1.setHunger(0);
//		//p1.startThread();
//		
//		manager = p1;
//		EnterBuilding(p1, "Host");
//
//		PersonAgent p2 = new PersonAgent("CMCook");
//		//p2.startThread();
//
//		EnterBuilding(p2, "Cook");
//
//
//		PersonAgent p3 = new PersonAgent("CMCashier");
//		//p3.startThread();
//		p3.goToWork = true;
//		EnterBuilding(p3, "Cashier");
//		
//		PersonAgent p4 = new PersonAgent("CMCustomer");
//		//p4.startThread();
//		p4.goToWork = true;
//		EnterBuilding(p3, "Customer");
//	}
}

