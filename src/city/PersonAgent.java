package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import mainGUI.MainGui;
import market.MyOrder;
import agent.Agent;
import city.gui.PersonGui;

public class PersonAgent extends Agent
{
	/*CONSTANTS*/
	public static final int HUNGRY = 7;
	public static final int STARVING = 14;
	public static final int LOWMONEY = 20;
	/*END OF CONSTANTS*/
	
	/*DATA MEMBERS*/
	String name;
	public double cash = 100;
	public double money = 200;
	public int criminalImpulse = 0;
	public int hungerLevel = 0;
	boolean ranonce = false;
	PersonGui personGui;
	MainGui gui;
	CityData cityData;
	Building currentBuilding;
	Building destinationBuilding;
	Building jobBuilding;
	Building home;
	BusStopAgent currentBusStop;
	BusStopAgent destinationBusStop;
	String desiredRole;
	
	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
	
	public enum BigState {doingNothing, goToRestaurant, goToBank, goToMarket, goHome, atHome, leaveHome};
	public enum HomeState {sleeping, onCouch, hungry, none};
	public BigState bigState = BigState.doingNothing;
	public HomeState homeState;
	
	private Semaphore atBuilding = new Semaphore(0, true);
	private Semaphore isMoving = new Semaphore(0, true);
	public List<MyOrder> thingsToOrder = Collections.synchronizedList(new ArrayList<MyOrder>());;
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atEntrance = new Semaphore(0, true);
	
	/*CONSTRUCTORS*/
	public PersonAgent(String name) {
		this.name = name;
		MyOrder o1 = new MyOrder("steak", 2, 1);
		MyOrder o2 = new MyOrder("salad", 2, 1);
		MyOrder o3 = new MyOrder("pizza", 2, 1);
		MyOrder o4 = new MyOrder("chicken", 2, 1);
		thingsToOrder.add(o1);
		thingsToOrder.add(o2);
		thingsToOrder.add(o3);
		thingsToOrder.add(o4);
		personGui = new PersonGui(this, gui);
	}
	
	public PersonAgent(String name, MainGui gui, CityData cd) {
		this.name = name;
		this.gui = gui;
		this.cityData = cd;
		personGui = new PersonGui(this, gui);
	}
	
	/*SETTERS*/
	public void assignHome(Building home)
	{
		this.home = home;
	}
	
	/*MESSAGES*/
	public void msgDoneMoving() {
		isMoving.release();
	}
	
	public void msgAssignRole(Role role) {
		for (Role r : roles) {
			if (r == role) {
				r.setActive();
				super.stateChanged();
				return;
			}
		}
		
		//If this part is reached, then 'role' is not in the list of roles
		roles.add(role);
		role.setActive();
		super.stateChanged();
	}
	
	public void msgDoneWithRole() {
		//implement later
	}
	
	public void msgAtBuilding() {//from animation
		//print("msgAtBuilding() called");
		atBuilding.release();// = true;
		stateChanged();
	}
	
	public void msgAtBed() {//from animation
		//print("msgAtBed() called");
		atBed.release();// = true;
		stateChanged();
	}
	
	public void msgAtEntrance() {//from animation
		//print("msgAtEntrance() called");
		atEntrance.release();// = true;
		stateChanged();
	}
	
	/*SCHEDULER*/
	protected boolean pickAndExecuteAnAction() {
		/*Emergency scheduler rules go here (v2)*/
		
		//This should be the only part of the scheduler which runs if the person has an active role
		boolean anyActive = false;
		for (Role role : roles) {
			if (role.isActive()) {
				anyActive = true;
				if(role.pickAndExecuteAnAction())
					return true;
			}
		}
		//Reaching here means there is an active role, but it is "waiting" for a state to be updated
		//Thus, the PersonAgent's scheduler should return FALSE
		if(anyActive)
		{
			return false;
		}
		
		switch(bigState)
		{
			case atHome: {
				if (homeState == HomeState.sleeping) {
					
				}
				if (homeState == HomeState.hungry) {
					
				}
				if (homeState == HomeState.onCouch) {
					
				}
				if (homeState == HomeState.none) {
					
				}
				//personGui.DoGoToBed();
				return true;
			}
			case leaveHome: {
				//personGui.DoGoToEntrance();
				leaveHome();
				return true;
			}
			case goToRestaurant: {
				goToRestaurant();
				return true;
			}
			case goHome: {
				goHome();
				return true;
			}
			case goToBank: {
				goToBank();
				return true;
			}
			case goToMarket: {
				goToMarket();
				return true;
			}
			case doingNothing: {
				//Decide what the next BigState will be based on current parameters
				if(hungerLevel >= STARVING)
				{
					bigState = BigState.goToRestaurant;
					desiredRole = "Customer";
					return true;
				}
				if(cash <= LOWMONEY)
				{
					bigState = BigState.goToBank;
					desiredRole = "Customer";
					return true;
				}
				if(cash >= HUNGRY)
				{
					bigState = BigState.goToRestaurant;
					desiredRole = "Customer";
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected void goToRandomPlace() {
		//personGui.DoGoToRandomPlace();
	}
	
	protected void goToRestaurant() {
		int restNumber = (int)(12+(int)(Math.random()*17));
		personGui.DoGoToBuilding(restNumber);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	protected void goHome() {
		//int homeNumber = (int)((int)(Math.random()*11));
		personGui.DoGoToBuilding(11); // 11 need to be replaced by the person's data of home number
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		currentBuilding = cityData.buildings.get(11);
		currentBuilding.EnterBuilding(this, "");
		bigState = BigState.atHome;
		
		homeState = HomeState.sleeping;
		if (homeState == HomeState.sleeping) {
			personGui.DoGoToBed();
			atBed.drainPermits();
			try {
				atBed.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		bigState = BigState.leaveHome;
	}
	
	protected void leaveHome() {
		personGui.DoGoToEntrance();
		atEntrance.drainPermits();
		try {
			atEntrance.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoLeaveBuilding();
		currentBuilding = cityData.buildings.get(11);// 11 need to be replaced by the person's data of home number
		currentBuilding.LeaveBuilding(this);
		bigState = BigState.doingNothing;
	}
	
	protected void goToBank() {
		personGui.DoGoToBuilding(18);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	protected void goToMarket() {
		personGui.DoGoToBuilding(19);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
	}
	
	/*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
	protected void stateChanged() {
		super.stateChanged();
	}
	
	/*GETTERS*/
	public String getName() {
		return name;
	}
	
	public void setGui(PersonGui g) {
		personGui = g;
	}
	
	public PersonGui getGui() {
		return personGui;
	}
}
