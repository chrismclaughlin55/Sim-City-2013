package city;
import java.util.*;
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
	
	public static Map<BusStopAgent,Coordinate> busStopPositions; //given a busstopagent, will return pixel position in city.
	public static ArrayList<BusStopAgent> busStops; //has busstops in order of the route the bus will take.
	public static ArrayList<BusAgent> buses;
	public static ArrayList<Building> buildings;
	//ALSO needs a 2-d array of the entire place
	
	public CityData() {
		
	}
	/*
	CityData will hold all information regarding bus routes,
	restaurants available and their type, and whatever else
	makes it easier or should be global data known, such as time.
	*/
}
