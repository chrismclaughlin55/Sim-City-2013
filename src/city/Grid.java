package city;

import java.util.concurrent.Semaphore;

public class Grid {
	
	public static final int GRIDWIDTH = 20;
	
	double posX;
	double posY;
	
	public Grid() {
		
	}
	public Grid(int x, int y) {
		posX = x;
		posY = y;
	}
	
	public void setSemaphore(Semaphore s) {} //to be overridden in RGrid
}
