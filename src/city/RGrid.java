package city;

import java.util.concurrent.Semaphore;


public class RGrid extends Grid {

	Semaphore occupied = new Semaphore(1, true);
	public enum Direction{north, south, east, west, none}
	public Direction direction;
	
	public RGrid() {
		
	}
	
	public RGrid(int x, int y, Direction d) {
		super(x, y);
		direction = d;
	}

	public String getDirection() {
		String d ="";
		if(direction==Direction.north) {
			d = "north";
		}
		if(direction==Direction.south) {
			d = "south";
		}
		if(direction==Direction.east) {
			d = "east";
		}
		if(direction==Direction.west) {
			d = "west";
		}
		return d;
	}
}
