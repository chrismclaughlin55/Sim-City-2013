package city;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import city.gui.PersonGui;
import Gui.Gui;
import market.Market;

import javax.swing.Timer;
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
	public static ArrayList<BusAgent> buses;
	public static List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public static List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	public Market market;
	Timer globalTimer;
	//ALSO needs a 2-d array of the entire place
	int timeInterval;
	public CityData() {
		timeInterval=10;
		//POPULATE busStops through MainGui as well whlie also assigning them a position
		//also each busStop should be initialized with its next busStopAgent
		//POPULATE 
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
		g.setXDes(g.xBuilding[currentIn]);
		g.setYDes(g.yBuilding[currentIn]);
		g.setPresent(true);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	CityData will hold all information regarding bus routes,
	restaurants available and their type, and whatever else
	makes it easier or should be global data known, such as time.
	*/
}
