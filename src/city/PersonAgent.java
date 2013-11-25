package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import mainGUI.MainGui;
import market.MyOrder;
import market.Inventory;
import agent.Agent;
import city.Building.BuildingType;
import city.gui.PersonGui;

public class PersonAgent extends Agent
{
	/*CONSTANTS*/
	public static final int HUNGRY = 7;
	public static final int STARVING = 14;
	public static final int LOWMONEY = 20;
	public static final int TIRED = 16;
	public static final double RENT = 20;
	/*END OF CONSTANTS*/
	
	/*DATA MEMBERS*/
	String name;
	public int tiredLevel = 0;
	public double cash = 100;
	public double bankMoney = 200;
	public double rentDue = 0;
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
	String job;

	
	boolean goToWork = true;
	
	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
	
	public enum BigState {doingNothing, goToRestaurant, goToBank, goToMarket, goHome, atHome, leaveHome};
	public enum HomeState {sleeping, onCouch, hungry, none};
	public enum EmergencyState {fire, earthquake, none};
	public BigState bigState = BigState.doingNothing;
	public HomeState homeState;
	public EmergencyState emergencyState = EmergencyState.none;
	
	Inventory inventory = new Inventory();
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setJob(String job) {
		this.job = job;
	}
	
	public void setCash(double cash) {
		this.cash = cash;
	}
	
	public void setBankMoney(double moneyInDaBank) {
		this.bankMoney = moneyInDaBank;
	}
	
	public void setHunger(int hangry) {
		this.hungerLevel = hangry;
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
	
	public void msgFire() {
		emergencyState = EmergencyState.fire;
		stateChanged();
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
		if(emergencyState == EmergencyState.fire) {
			ReactToFire();
			return true;
		}
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
					if(false){//if sleeping and it is time to wake up
					//delete the && false when the actual rule is implemented
						WakeUp();
						return true;
					}
					else
						return false; //put the agent thread back to sleep
				}
				
				if(hungerLevel >= HUNGRY) //inventory also has to be sufficient
				{
					makeFood(); //just choose a random item from the inventory
					return true;
				}
				
				if(goToWork)
				{
					leaveHome(); //leave the house and set bigState to doingNothing
				}
				
				if(home.type == BuildingType.apartment && rentDue > 0)
				{
					payRent();
					return true;
				}
				
				if(tiredLevel >= TIRED)
				{
					goToSleep();
					return false; //intentional because the thread is being out to sleep
				}
				
				if (homeState == HomeState.onCouch) {
					
				}
				if (homeState == HomeState.none) {
					leaveHome();
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
				if(hungerLevel >= HUNGRY)
				{
					bigState = BigState.goToRestaurant;
					desiredRole = "Customer";
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void payRent() {
		// TODO Auto-generated method stub
		
	}

	private void WakeUp() {
		// TODO Auto-generated method stub
		
	}

	private void makeFood() {
		// TODO Auto-generated method stub
		
	}

	private void goToSleep() {
		// TODO Auto-generated method stub
		
	}

	protected void goToRandomPlace() {
		//personGui.DoGoToRandomPlace();
	}
	
	protected void goToRestaurant() {
		int restNumber = (int)(12+(int)(Math.random()*5));
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
	
	protected void ReactToFire() {
		System.out.println(name +": Stop, Drop, and Roll ");
		emergencyState = EmergencyState.none;
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

