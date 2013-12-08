package city;

import java.util.concurrent.Semaphore;

public class Grid {
	
	public enum Type { crosswalk, road, building, none };
	Type type;
	boolean isOccupied;
	double posX;
	double posY;
	public Semaphore occupied = new Semaphore(1,true);
	public Grid(int x, int y, String type) {
		posX = x;
		posY = y;
		if(type.equals("crosswalk")) {
			this.type=Type.crosswalk;
		}
		if(type.equals("road")) {
			this.type = Type.road;
		}
		if(type.equals("building")) {
			this.type = Type.building;
		}
	}
}
