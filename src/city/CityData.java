package city;
import java.util.*;

import Gui.Gui;
public class CityData {
	
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
	public static Map<BusStopAgent,Coordinate> busStopPositions; //given a busstopagent, will return pixel position in city.
	public static ArrayList<BusStopAgent> busStops; //has busstops in order of the route the bus will take.
	public static ArrayList<BusAgent> buses;
	public static List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public static List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	//ALSO needs a 2-d array of the entire place
	int timeInterval;
	public CityData() {
		timeInterval=10;
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
	
	/*
	CityData will hold all information regarding bus routes,
	restaurants available and their type, and whatever else
	makes it easier or should be global data known, such as time.
	*/
}
