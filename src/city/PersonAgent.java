package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.Alert;
import trace.AlertLog;
import trace.AlertTag;
import trace.TracePanel;
import restaurantCM.gui.CMRestaurantBuilding;
import restaurantBK.gui.BKRestaurantBuilding;
import restaurantKC.gui.KCRestaurantBuilding;
import restaurantMQ.gui.MQRestaurantBuilding;
import restaurantSM.gui.SMRestaurantBuilding;
import bank.Bank;
import bank.utilities.CustInfo;
import mainGUI.MainGui;
import market.Market;
import market.MyOrder;
import market.Inventory;
import agent.Agent;
import city.Building.BuildingType;
import city.RGrid.Direction;
import city.gui.PersonGui;
import city.interfaces.BusStop;


public class PersonAgent extends Agent
{
	/*CONSTANTS*/

	public static final int HUNGRY = 7;
	public static final int STARVING = 14;
	public static final int LOWMONEY = 80;
	public static final int HIGHMONEY = 1200;
	public static final int TIRED = 16;
	public static final double RENT = 20;
	public static final int THRESHOLD = 3;
	/*END OF CONSTANTS*/

	/*DATA MEMBERS*/
	String name;
	public int tiredLevel = 16;
	public double cash = 100;
	public CustInfo bankInfo;
	public boolean driving = false;
	public boolean rentDue = true;
	public int criminalImpulse = 0;
	public int hungerLevel = 0;
	boolean ranonce = false;
	public boolean crossingRoad = false;
	PersonGui personGui;
	MainGui gui;
	public CityData cityData;
	Building currentBuilding;
	Building destinationBuilding;
	Building jobBuilding;
	public Building home;
	int homeNumber;
	int roomNumber = -1;
	int timeUnit = 5;
	public BusStop currentBusStop;
	public BusStopAgent destinationBusStop;
	String desiredRole;
	private String job;
	List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	Timer timer = new Timer();
	List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	public HashMap<String, Integer> inventory = new HashMap<String, Integer>();
	int rent = 50;
	public boolean car;
	public boolean bus;
	public boolean walk;
	public boolean once = true;

	public Grid currGrid;
	public RGrid currRGrid;
	public RGrid nextRGrid;

	public RGrid prevRGrid = new RGrid();
	public RGrid gridToAcquire = null;
	

	public boolean walking = false;
	public boolean atDestination = false;



	boolean goToWork = false;

	private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)

	public enum BigState {doingNothing, goToRestaurant, goToBank, goToMarket, goHome, atHome, leaveHome, waiting};
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
	private Semaphore gridding = new Semaphore(0, true);
	private Semaphore carring = new Semaphore(0, true);

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
		bankInfo = new CustInfo(this.name, this, null);
	}

	public PersonAgent(String name, MainGui gui, CityData cd) {
		this.name = name;
		this.gui = gui;
		this.cityData = cd;

		bankInfo = new CustInfo(this.name, this, null);
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
		banks = cd.banks;
		markets = cd.markets;
	}

	public void setName(String name) {
		this.bankInfo.custName = name;
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

	public void msgDoneGridding() {
		this.gridding.release();
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

    
	public void msgAcquireGrid(RGrid grid)
	{
		gridToAcquire = grid;
		isMoving.release();
	}
	
	public void msgAtDestination()
	{
		driving = false;
		isMoving.release();
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
		bigState = BigState.doingNothing;
		super.stateChanged();
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
		if(getName().equals("bankManager"))
			print("bigState:" +bigState);
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
		switch(bigState) {


		case atHome: {
			if (homeState == HomeState.sleeping) {
				if (!isWeekend()) {
					if(cityData.hour >= 0 && (job.equals("Host") || job.equals("MarketManager") || job.equals("BankManager"))){
						//delete the && false when the actual rule is implemented
						WakeUp();
						return true;
					}

					else if (cityData.hour>=3 && isEmployee()) {

						//print(getJob());
						WakeUp();
						return true;
					}
					else if (cityData.hour>=6) {
						WakeUp();
						return true;
					}
					return false; //put the agent thread back to sleep
				}
				else {
					if(cityData.hour >= 2 && (job.equals("Host") || job.equals("MarketManager") || job.equals("BankManager"))){
						//delete the && false when the actual rule is implemented
						WakeUp();
						return true;
					}

					else if (cityData.hour>=5 && isEmployee()) {

						//print(getJob());
						WakeUp();
						return true;
					}
					else if (cityData.hour>=8) {
						WakeUp();
						return true;
					}
					return false; //put the agent thread back to sleep
				}
			}

			if (tiredLevel >= TIRED) {
				goToSleep();
				return false; //intentional because the thread is being out to sleep
			}

			if (home instanceof Apartment && rentDue && !home.manager.equals(this)) {
				payRent(0);
				return true;
			}

			if (hungerLevel >= HUNGRY) {
				int num = (int) (Math.random() * 2);
				if (num == 0) {
					makeFood();
					return true;
				}
				if (num == 1) {
					leaveHome();
					return true;
				}
			}

			if (goToWork && jobBuilding != null && (!home.manager.equals(this) && home instanceof Apartment)) {
				leaveHome();
				return true;
			}

			if (home instanceof Apartment && rentDue && !home.manager.equals(this)) {
				// TODO
				if (banks.get(0).isOpen) {
					payRent(0);
				}
				else if (banks.get(1).isOpen) {
					payRent(1);
				}
				return true;
			}

			if (homeState == HomeState.onCouch) {
				goToCouch();
				return true;
			}
			if (homeState == HomeState.none) {
				if (home instanceof Apartment && home.manager.equals(this) && lowInventory()) {
					leaveHome();
					return true;
				}
				else if (home instanceof Apartment && home.manager.equals(this)) {
					goToCouch();
					return true;
				}
				else if (cash <= LOWMONEY || cash >= HIGHMONEY) {
					System.err.println("money problems");
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

			if(goToWork && jobBuilding != null) {
				destinationBuilding = jobBuilding;
				desiredRole = job;
				print("destinationBuilding: "+ destinationBuilding.type);
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

			if (hungerLevel >= STARVING) {
				bigState = BigState.goToRestaurant;
				desiredRole = "Customer";
				if(!goToWork)
					 print(name + " " + job + " " + desiredRole);
				return true;
			}

			if (cash <= LOWMONEY) {
				bigState = BigState.goToBank;
				desiredRole = "Customer";
				double withdrawAmount = (bankInfo.moneyInAccount<100)?bankInfo.moneyInAccount : 100;
				bankInfo.depositAmount = - withdrawAmount;
				if(job.equals("bankManager"))
				print(name+ " "+destinationBuilding.type + " " + job + " " + desiredRole);
				return true;
			}

			if (cash >= HIGHMONEY){
				if(job.equals("bankManager"))
				bigState = BigState.goToBank;
				desiredRole = "Customer";
				bankInfo.depositAmount = cash - HIGHMONEY;
				if(job.equals("bankManager"))
				print(name+ " "+destinationBuilding.type + " " + job + " " + desiredRole);
				return true;
			}
			// Inventory of food stuff
			if (lowInventory()) {
				bigState = BigState.goToMarket;
				desiredRole = "MarketCustomer";
				if(!goToWork)
					print(name + " " + job + " " + desiredRole);
				return true;
			}

			if (hungerLevel >= HUNGRY) {
				bigState = BigState.goToRestaurant;
				desiredRole = "Customer";
				if(!goToWork)
					print(name + " " + job + " " + desiredRole);
				return true;
			}

			bigState = BigState.goHome;
			homeState = HomeState.onCouch;
			if(!goToWork)
				print(name + " " + job + " " + desiredRole);
			return true;
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
	//TODO
	private void payRent(int bankNumber) {
		Apartment a = (Apartment) home;
		banks.get(bankNumber).directDeposit(this, a.manager, rent);
		rentDue = false;
	}

	private void WakeUp() {
		print(" is going to work");
		AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name, "Going to work");
		goToWork = true;
		tiredLevel = 0;
		homeState = HomeState.idle;
		hungerLevel += 5;
	}

	private void makeFood() {
		hungerLevel = 0;
		for (String key : inventory.keySet()) {
			if (inventory.get(key) > 0) {
				inventory.put(key, inventory.get(key) - 1);
				break;
			}
		}
		if (personGui.isInBedroom()) {
			personGui.DoAlmostWall();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			personGui.DoGoToWall();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				//
				e.printStackTrace();
			}
		}
		personGui.DoGoToRefridgerator();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoToStove();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
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
			//   Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void goToSleep() {
		personGui.DoReverseWall();
		try {
			isMoving.acquire();
		} catch (InterruptedException e2) {
			//   Auto-generated catch block
			e2.printStackTrace();
		}
		personGui.DoGoToWall();
		try {
			isMoving.acquire();
		} catch (InterruptedException e1) {
			//   Auto-generated catch block
			e1.printStackTrace();
		}
		personGui.DoGoToBed();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		homeState = HomeState.sleeping;
	}

	private void goToCouch() {
		if (personGui.isInBedroom()) {
			personGui.DoGoToWall();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
				e.printStackTrace();
			}
		}
		personGui.DoGoToCouch();
		try {
			isMoving.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				homeState = HomeState.none;
			}
		}, 3000);
	}

	protected void goToRandomPlace() {
		//personGui.DoGoToRandomPlace();
	}

	protected void goToRestaurant() {
		int restNumber;
		if(!goToWork || jobBuilding == null)
		{

			while (true)
			{
				restNumber = 2;
				//restNumber = (int)(12+(int)(Math.random()*6));
				if(restNumber >= 17)
				{
					bigState = BigState.goHome;
					return;
				}


				else if(((KCRestaurantBuilding)cityData.restaurants.get(restNumber)).isOpen())

					break;
			}
			destinationBuilding = cityData.restaurants.get(restNumber);
		}
		else
		{
			//destinationBuilding = jobBuilding;
			restNumber = 0;
			destinationBuilding = cityData.restaurants.get(restNumber);
		}

		if(destinationBuilding != currentBuilding)
		{
			System.out.println("Going to restaurant as " + desiredRole);
			GoToDestination();

			personGui.DoGoToBuilding(destinationBuilding.buildingNumber);
			try {
				atBuilding.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
				e.printStackTrace();
			}
			currentBuilding = cityData.restaurants.get(restNumber);
		}

		SMRestaurantBuilding restaurant = (SMRestaurantBuilding)destinationBuilding;


		if(goToWork && !desiredRole.equals("Customer"))
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


		//This is only reached if the person is unemployed
		if(desiredRole.equals("Customer") && restaurant.isOpen()) {
			personGui.DoGoIntoBuilding();
			currentBuilding.EnterBuilding(this, desiredRole);
			bigState = BigState.waiting;
			return;
		}
		bigState = BigState.goHome;
		homeState = HomeState.onCouch;
	}

	protected void goHome() {
		//int homeNumber = (int)((int)(Math.random()*11));
		destinationBuilding = cityData.buildings.get(this.home.buildingNumber);
		boolean busser = false;
		boolean driver = false;

		if(once) {
			if(bus==true) {
				busser = true;
			}
			if(car==true) {
				driver = true;
			}
			if(bus||car) {
				walk = true;
				bus = false;
				car = false;
			}
		}

		if(once) {
			if(busser) {
				bus = true;
				walk=false;
			}
			if(driver) {
				car = true;
				walk = false;
			}
		}
		once = false;
		personGui.DoGoToBuilding(this.home.buildingNumber); // 11 need to be replaced by the person's data of home number
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		currentBuilding = destinationBuilding;

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
				//   Auto-generated catch block
				e.printStackTrace();
			}
			a.rooms.get(roomNumber).EnterBuilding(this, "");
		}
		bigState = BigState.atHome;
		homeState = HomeState.onCouch;
		//hungerLevel = 10000000;
	}

	protected void leaveHome() {
		currentBuilding = cityData.buildings.get(home.buildingNumber);
		if (personGui.isInBedroom()) {
			personGui.DoAlmostWall();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
				e.printStackTrace();
			}
			personGui.DoGoToWall();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (home instanceof Home) {
			personGui.DoGoToEntrance();
			atEntrance.drainPermits();
			try {
				atEntrance.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
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
				//   Auto-generated catch block
				e1.printStackTrace();
			}
			a.rooms.get(roomNumber).LeaveBuilding(this);
			personGui.DoGoToHallway();
			try {
				isMoving.acquire();
			} catch (InterruptedException e) {
				//   Auto-generated catch block
				e.printStackTrace();
			}
			personGui.DoLeaveBuilding();
			a.LeaveBuilding(this);
		}
		bigState = BigState.doingNothing;
	}

	protected void goToBank() {
		int bankNumber = 0;
		destinationBuilding = cityData.banks.get(bankNumber);
		GoToDestination();

		personGui.DoGoToBuilding(18);
		currentBuilding = cityData.buildings.get(18);
		atBuilding.drainPermits();

		try {
			atBuilding.acquire();
			if(currentBuilding.type == BuildingType.bank)
				print("made it past at Building acquire");
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		print("entering the building and desired role is "+desiredRole);
		currentBuilding.EnterBuilding(this,desiredRole );

	}

	protected void goToMarket() {
		int marketNumber = 0;
		destinationBuilding = cityData.markets.get(0);

		GoToDestination();

		personGui.DoGoToBuilding(19);
		currentBuilding = cityData.buildings.get(19);
		atBuilding.drainPermits();
		try {
			atBuilding.acquire();
		} catch (InterruptedException e) {
			//   Auto-generated catch block
			e.printStackTrace();
		}
		personGui.DoGoIntoBuilding();
		currentBuilding.EnterBuilding(this,desiredRole );

	}

	public void GoToDestination()
	{
		currGrid = cityData.cityGrid[personGui.getX()/20][personGui.getY()/20];
		if(walk==true) {
			personGui.DoGoToBuilding(destinationBuilding.buildingNumber);

			//PersonGui.DoWalk(if you ever hit an RGrid, acquire it first, and then walk through, and don't walk through bgrids)
			walking = true;
			while(walking) {

				if(crossingRoad) {
					try {
						currRGrid.occupied.acquire();
						//System.out.println("1");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						nextRGrid.occupied.acquire();
						//System.out.println("2");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					personGui.crossRoad();
					try {
						gridding.acquire();
					}
					catch(Exception e) {}
					currRGrid.occupied.release();
					//System.out.println("1");	
					nextRGrid.occupied.release();
					//System.out.println("0");
					//ACQUIRE CURRENT AND NEXT
				}
				else {
					//				// go from grid to grid
					//				currGrid = nextGrid
					////				if undo
					////				if(curr instanceof RGrid) {
					////					person.acquire those next two semaphores in that direction
					////					personGui.crossRoad(direction);
					////					move to next
					////					release roads
					////				}
					//				else {
					//				//personGui.MoveToNextGrid;
					//				}
					//print("hello");

					personGui.MoveToNextGrid(currGrid);
					try
					{
						gridding.acquire();
					}
					catch(Exception e){}
				}
			}

		}
		if(car==true) {
			currentBuilding.closest.acquireGrid();
			currRGrid = currentBuilding.closest;
			personGui.DoWalkToRGrid(currentBuilding.buildingNumber);
			//personGui.//acquire the semaphore of the road(currentBuilding.closest.index1(),currentBuilding.closest.index2())
				//then he moves like a bus until he gets to his destinatoin's rgrid, gets off road
				//then he releases last road semaphore
			isMoving.drainPermits();
			try
			{
				isMoving.acquire();
			}
			catch(Exception e){}
			
			System.err.println("hello");
			
			personGui.getInOrOutCar();
			personGui.DriveToDestination(destinationBuilding);
			//personGui.DriveToClosestRGrid(destinationBuilding); **** have similar mechanisms to busgridbehavior
			//if person's rgrid is destination.closestRgrid, then, walk to building
			
			driving = true;
			while(driving) {
				
				if(gridToAcquire != null) {
					MoveToGrid();
					continue;
				}
				
				//puts this "mini-scheduler" to sleep
				try
				{
					isMoving.acquire();
				}
				catch(Exception e){}
			}
			//remember to release current and previous grids
			
			
			//WALKT TO THE BUILDING NOW
			

			currRGrid = currentBuilding.closest;
			//personGui.;
			driving = true;
			while(driving) {
				try
				{
					//.acquire();
				}
				catch(Exception e){}

				personGui.getInOrOutCar();
				//personGui.DriveToClosestRGrid(destinationBuilding); **** have similar mechanisms to busgridbehavior
				//if person's rgrid is destination.closestRgrid, then, walk to building

				try
				{
					isMoving.acquire();
				}
				catch(Exception e){}
				driving = false;
			}	
			//personGui
			//have similar mechanisms to busgridbehavior
			//if person's rgrid is destination.closestRgrid, then, walk to building
			//YOU WILL TURN INTO A ROBOT
		}
		if(bus==true) {
			destinationBusStop = currentBuilding.busStop;
			personGui.DoGoToBusStop(destinationBusStop);
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

			
			if(currentBus == null) {
				try
				{
					isMoving.acquire();
				}
				catch(Exception e) {}
			}
           

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
			personGui.DoGoToBusStop(destinationBusStop); // THIS SHOULD ACQUIRE ROAD SEMAPHORE
			//if it needs to
			try
			{
				isMoving.acquire();
			}
			catch(Exception e) {}
		}

		
		currentBuilding = destinationBuilding;

	}

	public void setRoomNumber(int number) {
		roomNumber = number;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	protected void ReactToFire() {
		System.out.println(name +": Stop, Drop, and Roll ");
		AlertLog.getInstance().logMessage(AlertTag.PERSON, this.name, "Stop, Drop, and Roll");
		emergencyState = EmergencyState.none;
	}

	public void exitBuilding() {
		cityData.addGui(personGui);
		print("Exiting the building");
		bigState = BigState.doingNothing;
		super.stateChanged();
	}

	
	private void MoveToGrid()
	{
		prevRGrid.releaseGrid();
		gridToAcquire.acquireGrid();
		if(gridToAcquire.direction == Direction.none)
		{
			timer.schedule(new TimerTask() {
				public void run()
				{
					isMoving.release();
				}
			}, 300);
			try {
				isMoving.acquire();
			}
			catch(Exception e) {}
		}
		prevRGrid = currRGrid;
		currRGrid = gridToAcquire;
		personGui.moveOn();
		try {
	    	isMoving.acquire();
	    }
	    catch(Exception e) {}
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

	public void acquireGridSemaphore(RGrid r) {
		try {
			r.occupied.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void releaseGridSemaphore(RGrid r) {
		r.occupied.release();
	}

	public void setGoToWork(boolean b) {
		goToWork = b;
	}

	public String getJob() {
		return job;
	}

	public boolean isWeekend() {
		return cityData.day > 4;
	}

	public boolean isEmployee() {
		return job.equals("MarketEmployee") || job.equals("BankTeller") || job.equals("Cashier") || job.equals("Waiter") || job.equals("Cook");
	}

	public int getHomeNumber() {
		return homeNumber;
	}
}
