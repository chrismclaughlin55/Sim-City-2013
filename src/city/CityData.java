package city;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import bank.Bank;
import city.RGrid.Direction;
import city.gui.PersonGui;
import Gui.Gui;
import market.Market;

import javax.swing.Timer;

import restaurantMQ.gui.MQRestaurantBuilding;
//import bank.Bank;
public class CityData implements ActionListener {
	
	class Coordinate {
		int x;
		int y;
		public Coordinate(int x,int y) {
			this.x=x;
			this.y=y;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
	}
	
	private List<PersonAgent> people = new ArrayList<PersonAgent>();
	//public static Map<BusStopAgent,Coordinate> busStopPositions; //given a busstopagent, will return pixel position in city.
	public static List<BusStopAgent> busStops = Collections.synchronizedList(new ArrayList<BusStopAgent>()); //has busstops in order of the route the bus will take.
	public static ArrayList<BusAgent> buses = new ArrayList<BusAgent>();
	public static List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public static List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Building> restaurants = Collections.synchronizedList(new ArrayList<Building>());
	public Timer globalTimer;
	public static int incrementLimit = 300;

	public List<Home> homes = Collections.synchronizedList(new ArrayList<Home>());
	public List<Apartment> apartments = Collections.synchronizedList(new ArrayList<Apartment>());
	//ALSO needs a 2-d array of the entire place
	int timeInterval;
	public int day;
	public int hour;
	public int increment;
	
	public Grid cityGrid[][] = new Grid[31][40];
	
	public CityData() {
		day = 4;
		//DAY 5 AND DAY 6 ARE WEEKENDS, 0-4 ARE WEEKDAYS
		timeInterval=10;
		hour = 0;
		increment = 0;
		//POPULATE busStops through MainGui as well whlie also assigning them a position
		//also each busStop should be initialized with its next busStopAgent
		//POPULATE 
		
		for (int i = 0; i < 31; i++) {
			for (int j = 0; j < 40; j++) {
				cityGrid[i][j] = new Grid(i, j);
			}
		}
		
		//construct building grid
		for (int i = 10; i < 15; i++) {
			for (int j = 0; j < 5; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 0; j < 5; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 1; i < 6; i++) {
			for (int j = 7; j < 12; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 10; i < 15; i++) {
			for (int j = 7; j < 12; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 7; j < 12; j++) {
				cityGrid[i][j] = new BGrid(i ,j);
			}
		}
		for (int i = 25; i < 30; i++) {
			for (int j = 7; j < 12; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 1; i < 6; i++) {
			for (int j = 13; j < 18; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 10; i < 15; i++) {
			for (int j = 13; j < 18; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 13; j < 18; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 25; i < 30; i++) {
			for (int j = 13; j < 18; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 1; i < 6; i++) {
			for (int j = 21; j < 26; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 10; i < 15; i++) {
			for (int j = 21; j < 26; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 21; j < 26; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 25; i < 30; i++) {
			for (int j = 21; j < 26; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 1; i < 6; i++) {
			for (int j = 27; j < 32; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 10; i < 15; i++) {
			for (int j = 27; j < 32; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 27; j < 32; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 25; i < 30; i++) {
			for (int j = 27; j < 32; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 10; i < 15; i++) {
			for (int j = 34; j < 39; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 17; i < 22; i++) {
			for (int j = 34; j < 39; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 1; i < 6; i++) {
			for (int j = 34; j < 39; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		for (int i = 25; i < 30; i++) {
			for (int j = 34; j < 39; j++) {
				cityGrid[i][j] = new BGrid(i, j);
			}
		}
		
		
		//construct road grid
		/*for (int i = 0; i < 31; i++) {
			for (int j = 18; j < 20; j++)
				cityGrid[i][j] = new RGrid();
		}*/
		for (int i = 0; i < 31; i++) {
			cityGrid[i][18] = new RGrid(Direction.west, i, 18);
		}
		for (int i = 0; i < 31; i++) {
			cityGrid[i][19] = new RGrid(Direction.east, i, 19);
		}
		/*for (int i = 7; i < 24; i++) {
			for (int j = 5; j < 7; j++)
				cityGrid[i][j] = new RGrid();
		}*/
		for (int i = 7; i < 24; i++) {
			cityGrid[i][5] = new RGrid(Direction.west, i, 5);
		}
		for (int i = 7; i < 24; i++) {
			cityGrid[i][6] = new RGrid(Direction.east, i, 6);
		}
		/*for (int i = 7; i < 24; i++) {
			for (int j = 32; j < 34; j++)
				cityGrid[i][j] = new RGrid();
		}*/
		for (int i = 7; i < 24; i++) {
			cityGrid[i][32] = new RGrid(Direction.west, i, 32);
		}
		for (int i = 7; i < 24; i++) {
			cityGrid[i][33] = new RGrid(Direction.east, i, 33);
		}
		/*for (int i = 7; i < 9; i++) {
			for (int j = 5; j < 33; j++)
				cityGrid[i][j] = new RGrid();
		}*/
		for (int i = 5; i < 35; i++) {
			cityGrid[7][i] = new RGrid(Direction.south, 7, i);
		}
		for (int i = 5; i < 35; i++) {
			cityGrid[8][i] = new RGrid(Direction.north, 8, i);
		}
		/*for (int i = 23; i < 25; i++) {
			for (int j = 5; j < 33; j++)
				cityGrid[i][j] = new RGrid();
		}*/
		for (int i = 5; i < 35; i++) {
			cityGrid[23][i] = new RGrid(Direction.south, 23, i);
		}
		for (int i = 5; i < 35; i++) {
			cityGrid[24][i] = new RGrid(Direction.north, 24, i);
		}
		
		//construct intersections
		Semaphore occupied = new Semaphore(1, true);
		for (int i = 7; i < 9; i++) {
			for (int j = 18; j < 20; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
		occupied = new Semaphore(1, true);
		for (int i = 23; i < 25; i++) {
			for (int j = 18; j < 20; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
		occupied = new Semaphore(1, true);
		for (int i = 7; i < 9; i++) {
			for (int j = 5; j < 7; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
		occupied = new Semaphore(1, true);
		for (int i = 23; i < 25; i++) {
			for (int j = 5; j < 7; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
		occupied = new Semaphore(1, true);
		for (int i = 7; i < 9; i++) {
			for (int j = 32; j < 34; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
		occupied = new Semaphore(1, true);
		for (int i = 23; i < 25; i++) {
			for (int j = 32; j < 34; j++) {
				cityGrid[i][j] = new RGrid(Direction.none, i, j);
				cityGrid[i][j].setSemaphore(occupied); //intersection grid squares share a single semaphore
			}
		}
	}
	
	public void setBusStopRoute(BusAgent bus) {
        if (bus.getRouteNumber() == 1) {
			for(int i=0; i<12; i++) {
				busStops.get(i).setNextStop(busStops.get((i+1)%12));
			}
		}
		else if (bus.getRouteNumber() == 2) {
			for(int i = 0; i < 8; i++) {
				busStops.get(i+12).setNextStop(busStops.get((i+1)%8+12));
			}
		}
	}
	
	public RGrid getNextRGrid(RGrid curr) {
		//CHECK ITS TYPE AND TRY TO RETURN GRID OF SAME TYPE
		//otherwise you're at an intersection?
		//jk it's handled by direction equaling none
		RGrid a = null;
		String dir = curr.getDirection();
		if(dir.equals("north")) {
			return (RGrid)cityGrid[curr.index1()][curr.index2()-1];
		}
		if(dir.equals("south")) {
			return (RGrid)cityGrid[curr.index1()][curr.index2()+1];
		}
		if(dir.equals("east")) {
			return (RGrid)cityGrid[curr.index1()+1][curr.index2()];
		}
		if(dir.equals("west")) {
			return (RGrid)cityGrid[curr.index1()-1][curr.index2()];
		}	
		return a;
	}
	public void incrementTime() {
		increment++;
		if(increment==incrementLimit) {
			hour++;
			if(hour==24) {
				day++;
				day = day%7;
			}
			hour = hour % 24;
			//System.out.println(hour);
			updatePeople();
		}
		increment = increment % incrementLimit;
	}
	
	private void updatePeople()
	{
		for(PersonAgent p : people)
		{
			p.refresh();
		}
	}
	public void setTimeInterval(int newTime) {
		timeInterval=newTime;
	}
	
	public void addPerson(PersonAgent p) {
		people.add(p);
	}
	
	public void removeGui(Gui g) {
		guis.remove(g);
	}
	
	public void addGui(Gui g) {
		guis.add(g);
	}
	
	public void addGui(PersonGui g, int currentIn) {
		guis.add(g);
		g.setXPos(g.xBuilding[currentIn]);
		g.setYPos(g.yBuilding[currentIn]);
		g.setXDes(g.xBuilding[currentIn]);
		g.setYDes(g.yBuilding[currentIn]);
		g.setPresent(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public int getPopulation() {
		return people.size();
	}
    
    public List getAllPeople() {
		return people;
	}
	
	public void setTimer(int delay) {
		globalTimer.setDelay(delay);
	}
	
	public int getHour() {
		return hour;
	}
	
	/*
	CityData will hold all information regarding bus routes,
	restaurants available and their type, and whatever else
	makes it easier or should be global data known, such as time.
	*/
}
