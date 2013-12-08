package city;

import java.util.concurrent.Semaphore;

public class RGrid extends Grid {

	Semaphore occupied = new Semaphore(1, true);
	public enum Direction{north, south, east, west, none}
	Direction direction;
	
	public RGrid() {
		
	}
	
	public RGrid(int x, int y, Direction d) {
		super(x, y);
		direction = d;
	}

}
