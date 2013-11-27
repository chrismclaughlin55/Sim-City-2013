package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurantMQ.gui.MQRestaurantBuilding;
import bank.Bank;
import bank.utilities.CustInfo;
import mainGUI.MainGui;
import market.Market;
import market.MyOrder;
import market.Inventory;
import agent.Agent;
import city.Building.BuildingType;
import city.gui.PersonGui;
import city.interfaces.BusStop;

public class PersonAgent extends Agent
{
	/*CONSTANTS*/
	public static final int HUNGRY = 7;
	public static final int STARVING = 14;
	public static final int LOWMONEY = 20;
	public static final int TIRED = 16;
	public static final double RENT = 20;
	public static final int THRESHOLD = 3;
	/*END OF CONSTANTS*/

	/*DATA MEMBERS*/
	String name;
	public int tiredLevel = 16;
	public double cash = 100;
	public CustInfo bankInfo = new CustInfo(this.name, this, null);
	public boolean rentDue = true;
	public int criminalImpulse = 0;
	public int hungerLevel = 0;
	boolean ranonce = false;
	PersonGui personGui;
	MainGui gui;
	public CityData cityData;
	Building currentBuilding;
	Building destinationBuilding;
	Building jobBuilding;
	Building home;
	int homeNumber;
	int roomNumber = -1;
	int timeUnit = 5;
	BusStop currentBusStop;
	BusStopAgent destinationBusStop;
	String desiredRole;
	private String job;
	Market market;
	Timer timer = new Timer();
	Bank bank;
	public HashMap<String, Integer> inventory = new HashMap<String, Integer>();
	int rent = 200;


	boolean goToWork = false;

	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)

	public enum BigState {doingNothing, goToRestaurant, goToBank, goToMarket, goHome, atHome, leaveHome};
	public enum HomeState {sleeping, onCouch, hungry, none, idle};
	public enum EmergencyState {fire, earthquake, none};
	public BigState bigState = BigState.doingNothing;
	public HomeState homeState;
	public EmergencyState emergencyState = EmergencyState.none;

	public BusAgent currentBus;

	private Semaphore atBuilding = new Semaphore(0, true);
	private Semaphore isMoving = new Semaphore(0, true);
	public List<MyOrder> thingsToOrder = Collections.synchronizedList(new ArrayList<MyOrder>());
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atEntrance = new Semaphore(0, true);

	/*CONSTRUCTORS*/
	public PersonAgent(String name) {
		this.name = name;
		/*MyOrder o1 = new MyOrder("Steak", 1);
		MyOrder o2 = new MyOrder("Salad", 1);
		MyOrder o3 = new MyOrder("Pizza", 1);
		MyOrder o4 = new MyOrder("Chicken", 1);
		thingsToOrder.add(o1);
		thingsToOrder.add(o2);
		thingsToOrder.add(o3);
		thingsToOrder.add(o4);*/
		inventory.put("Steak", 3);
		inventory.put("Salad", 3);
		inventory.put("Pizza", 3);
		inventory.put("Chicken", 3);
		personGui = new PersonGui(this, gui);
	}

	public PersonAgent(String name, MainGui gui, CityData cd) {
		this.name = name;
		this.gui = gui;
		this.cityData = cd;
		/*MyOrder o1 = new MyOrder("Steak", 1);
		MyOrder o2 = new MyOrder("Salad", 1);
		MyOrder o3 = new MyOrder("Pizza", 1);
		MyOrder o4 = new MyOrder("Chicken", 1);
		thingsToOrder.add(o1);
		thingsToOrder.add(o2);
		thingsToOrder.add(o3);
		thingsToOrder.add(o4);*/
		inventory.put("Steak", 3);
		inventory.put("Salad", 3);
		inventory.put("Pizza", 3);
		inventory.put("Chicken", 3);
		personGui = new PersonGui(this, gui);
		bank = (Bank) cd.buildings.get(18);
		market = (Market) cd.buildings.get(19);
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
		this.bankInfo.moneyInAccount = moneyInDaBank;
	}

	public void setHunger(int hangry) {
		this.hungerLevel = hangry;
	}

	public void setJobBuilding(Building jobBuilding) {
		this.jobBuilding = jobBuilding;
	}

	public void setDesiredRole(String role) {
		if(this.name == "myName6")
			print("! "+role);
		desiredRole = role;
	}

	/*SETTERS*/

	public void assignHome(Building home)
	{
		this.home = home;
		homeNumber = home.buildingNumber;
	}
	
	public void setInventory(int num) {
		for (String key : inventory.keySet()) {
			inventory.put(key, num);
		}
	}

	public void assignJobBuilding(Building jobBuilding) {
		this.jobBuilding = jobBuilding;
	}

	/*MESSAGES*/
	public void msgDoneWithJob()
	{
		goToWork = false;
	}

	public void refresh() {
		super.refresh();
		if(cityData.hour == 5)
			goToWork = true;
		if(cityData.hour % 4 == 0 && cityData.hour > 8) {
			tiredLevel+=2;
			hungerLevel+=2;
		}
		if(cityData.hour == 0 && this.home instanceof Apartment) {
			rent+=20;
		}
		
	}

	public void msgFull() {
		hungerLevel = 0;
	}

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

	public void msgBusIsHere(BusAgent bus) {
		currentBus = bus;
		isMoving.release();
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
		if(anyActive) {
			return false;
		}

		switch(bigState)
		{
		case atHome: {
			if (homeState == HomeState.sleeping) {
				if(cityData.hour >= 5 && (job.equals("Host") || job.equals("MarketManager")) ||job.equals("BankManager") ){
					//delete the && false when the actual rule is implemented
					WakeUp();
					return true;
				}
				else if (cityData.hour>=6 && job.equals("MarketEmployee")) {
					//print(getJob());
					WakeUp();
					return true;
				}
				else if (cityData.hour>=9) {
					WakeUp();
					return true;
				}
				return false; //put the agent thread back to sleep
			}

			if (tiredLevel >= TIRED) {
				goToSleep();
				return false; //intentional because the thread is being out to sleep
			}
			
			if (hungerLevel >= HUNGRY) { //inventory also has to be sufficient
				makeFood();
				return true;
			}

			if (goToWork && !home.manager.equals(this)) {
				leaveHome(); //leave the house and set bigState to doingNothing
				return true;
			}

			if (home instanceof Apartment && rentDue && !home.manager.equals(this) && bank.isOpen) {
				payRent();
				return true;
			}

			if (homeState == HomeState.onCouch) {
				goToCouch();
				return true;
			}
			if (homeState == HomeState.none) {
				if (home.manager.equals(this) && lowInventory()) {
					leaveHome();
					return true;
				}
				else if (home.manager.equals(this)) {
					goToCouch();
					return true;
				}
				else {
					leaveHome();
					return true;
				}
			}
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
			if(goToWork && jobBuilding != null)
			{
				destinationBuilding = jobBuilding;
				desiredRole = job;

				if(destinationBuilding.type == BuildingType.market) {
					bigState = BigState.goToMarket;
					return true;
				}
				else if(destinationBuilding.type == BuildingType.bank) {
					bigState = BigState.goToBank;
					return true;
				}
				else if(destinationBuilding.type == BuildingType.restaurant) {
					bigState = BigState.goToRestaurant;
					return true;
				}
			}


			if(hungerLevel >= STARVING) {
				bigState = BigState.goToRestaurant;
				desiredRole = "Customer";
				return true;
			}
			if(cash <= LOWMONEY) {
				bigState = BigState.goToBank;
				desiredRole = "Customer";
				return true;
			}
			// Inventory of food stuff
			if(lowInventory()) {
				bigState = BigState.goToMarket;
				desiredRole = "MarketCustomer";
				return true;
			}

			if(hungerLevel >= HUNGRY) {
				bigState = BigState.goToRestaurant;
				desiredRole = "Customer";
				return true;
			}
		}


		}

		return false;
	}

	public boolean lowInventory() {
		for(String food : inventory.keySet()) {
			if(inventory.get(food) < THRESHOLD) {
				thingsToOrder.add(new MyOrder(food, 10));
			}
		}
		if(!thingsToOrder.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	private void payRent() {
		Apartment a = (Apartment) home;
		System.err.println("Paying rent");
		System.err.println(a.manager.bankInfo.moneyInAccount);
		System.err.println(this.bankInfo.moneyInAccount);
		bank.getManager().msgDirectDeposit(this, a.manager, rent);
		rentDue = false;
	}

	private void WakeUp() {
		goToWork = true;
		tiredLevel = 0;
		homeState = homeState.idle;
		hungerLevel = 1000;
	}

	private void makeFood() {
		hungerLevel = 0;
		for (String key : inventory.keySet()) {
			if (inventory.get(key) > 0) {
				inventory.put(key, inventory.get(key) - 1);
				break;
			}
		}
		personGui.DoGoToRefridgerator();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoToStove();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		timer.schedule(new TimerTask() {
			public void run() {
				homeState = HomeState.onCouch;
				isMoving.release();
			}
		}, 5000);
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void goToSleep() {
		personGui.DoGoToBed();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		homeState = HomeState.sleeping;
	}

	private void goToCouch() {
		personGui.DoGoToCouch();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				homeState = HomeState.none;
			}
		}, 5000);
	}

	protected void goToRandomPlace() {
		//personGui.DoGoToRandomPlace();
	}

	protected void goToRestaurant() {
		int restNumber = 12;
		//int restNumber = (int)(12+(int)(Math.random()*5));
		destinationBuilding = cityData.buildings.get(restNumber);
		if(destinationBuilding != currentBuilding)
		{
			takeBusToDestination();
	
			personGui.DoGoToBuilding(restNumber);
			atBuilding.drainPermits();
			try {
				atBuilding.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentBuilding = cityData.buildings.get(restNumber);
		}
		MQRestaurantBuilding restaurant = (MQRestaurantBuilding)currentBuilding;

		if(goToWork)
		{
			if(desiredRole.equals("Host") && !restaurant.hasHost()) {
				personGui.DoGoIntoBuilding();
				currentBuilding.EnterBuilding(this, desiredRole);
				return;
			}
			else if(restaurant.openToEmployee())
			{
				if(desiredRole.equals("Waiter") || desiredRole.equals("Cook")) {
					personGui.DoGoIntoBuilding();
					currentBuilding.EnterBuilding(this, desiredRole);
					return;
				}
				else if(desiredRole.equals("Cashier") && !restaurant.hasCashier()) {
					personGui.DoGoIntoBuilding();
					currentBuilding.EnterBuilding(this, desiredRole);
					return;
				}
			}
		}
		else if(desiredRole.equals("Customer") && restaurant.isOpen()) {
			personGui.DoGoIntoBuilding();
			currentBuilding.EnterBuilding(this, desiredRole);
			return;
		}
	}

	protected void goHome() {
		//int homeNumber = (int)((int)(Math.random()*11));
		currentBuilding = cityData.buildings.get(this.home.buildingNumber);
		personGui.DoGoToBuilding(this.home.buildingNumber); // 11 need to be replaced by the person's data of home number
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		if (home instanceof Home) {
			currentBuilding.EnterBuilding(this, "");
		}
		if (home instanceof Apartment) {
			Apartment a = (Apartment) home;
			a.EnterBuilding(this, "");
			personGui.DoGoToRoom(roomNumber);
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			a.rooms.get(roomNumber).EnterBuilding(this, "");
		}
		bigState = BigState.atHome;
		//hungerLevel = 10000000;
	}

	protected void leaveHome() {
		currentBuilding = cityData.buildings.get(home.buildingNumber);
		if (home instanceof Home) {
			personGui.DoGoToEntrance();
			atEntrance.drainPermits();
			try {
				atEntrance.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			personGui.DoLeaveBuilding();
			currentBuilding.LeaveBuilding(this);
		}
		if (home instanceof Apartment) {
			Apartment a = (Apartment) home;
			personGui.DoGoToEntrance();
			try {
				isMoving.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			a.rooms.get(roomNumber).LeaveBuilding(this);
			personGui.DoGoToHallway();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			personGui.DoLeaveBuilding();
			a.LeaveBuilding(this);
		}
		bigState = BigState.doingNothing;
	}

	protected void goToBank() {

		destinationBuilding = cityData.bank;
		takeBusToDestination();

		personGui.DoGoToBuilding(18);
		currentBuilding = cityData.buildings.get(18);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		print("entering the building and desired role is "+desiredRole);
		currentBuilding.EnterBuilding(this,desiredRole );

	}

	protected void goToMarket() {
		destinationBuilding = cityData.market;
		
		takeBusToDestination();

		personGui.DoGoToBuilding(19);
		currentBuilding = cityData.buildings.get(19);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		currentBuilding.EnterBuilding(this,desiredRole );

	}

	public void takeBusToDestination()
	{
		destinationBusStop = currentBuilding.busStop;
		personGui.DoGoToBusStop(destinationBusStop);
		isMoving.drainPermits();
		try
		{
			isMoving.acquire();
		}
		catch(Exception e){}
		currentBusStop = destinationBusStop;
		destinationBusStop = destinationBuilding.busStop;

		currentBusStop.msgWaitingAtStop(this, destinationBusStop);
		try
		{
			isMoving.acquire();
		}
		catch(Exception e) {}

		personGui.DoGoToBus(currentBus);
		try
		{
			isMoving.acquire();
		}
		catch(Exception e) {}

		cityData.guis.remove(personGui);
		currentBus.msgOnBus();
		try
		{
			isMoving.acquire();
		}
		catch(Exception e) {}

		cityData.guis.add(personGui);
		personGui.setXPos(currentBus.getX());
		personGui.setYPos(currentBus.getY());
		currentBus.msgOnBus();
		personGui.DoGoToBusStop(destinationBusStop);
		try
		{
			isMoving.acquire();
		}
		catch(Exception e) {}
	}

	public void setRoomNumber(int number) {
		roomNumber = number;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	protected void ReactToFire() {
		System.out.println(name +": Stop, Drop, and Roll ");
		emergencyState = EmergencyState.none;
	}

	public void exitBuilding()
	{
		print("Exiting the building");
		bigState = BigState.doingNothing;
		cityData.addGui(personGui);
	}
	/*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
	protected void stateChanged() {
		super.stateChanged();
	}

	/*GETTERS AND SETTERS*/
	public String getName() {
		return name;
	}

	public void setGui(PersonGui g) {
		personGui = g;
	}

	public PersonGui getGui() {
		return personGui;
	}

	public String getJob() {
		return job;
	}

	public int getHomeNumber() {
		return homeNumber;
	}
}

