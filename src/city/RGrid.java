package city;

import java.util.concurrent.Semaphore;


public class RGrid extends Grid {

	Semaphore occupied = new Semaphore(1, true);
	public enum Direction{north, south, east, west, none}
	public Direction direction;
	
	public RGrid() {
		
	}
	
	public RGrid(Direction d) {
		super();
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
	
	public void setSempahore(Semaphore s)
	{
		occupied = s;
	}
}
