package city;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import bank.Bank;
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
	public Market market;
	public Bank bank;
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
	public CityData() {
		day = 4;
		//DAY 5 AND DAY 6 ARE WEEKENDS, 0-4 ARE WEEKDAYS
		timeInterval=10;
		hour = 0;
		increment = 0;
		//POPULATE busStops through MainGui as well whlie also assigning them a position
		//also each busStop should be initialized with its next busStopAgent
		//POPULATE 
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
