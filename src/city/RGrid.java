package city;

import java.util.concurrent.Semaphore;


public class RGrid extends Grid {

	Semaphore occupied = new Semaphore(1, true);
	public enum Direction{north, south, east, west, none}
	public Direction direction;
	
	public RGrid() {
		
	}
	
	public RGrid(Direction d, int i, int j) {
		super(i, j);
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
		if(direction==Direction.none) {
			d = "none";
		}
		return d;
	}
	
	public void setSempahore(Semaphore s)
	{
		occupied = s;
	}
	
	public void acquireGrid()
	{
		try {
		occupied.acquire();
		}
		catch(Exception e) {}
	}
	
	public void releaseGrid()
	{
		occupied.release();
	}
}
