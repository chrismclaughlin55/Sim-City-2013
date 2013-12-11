package city;

import java.util.concurrent.Semaphore;

public class Grid {
	
	public static final int GRIDWIDTH = 20;
	
	int index1;
	int index2;
	
	public Grid() {
		
	}
	public Grid(int index1, int index2) {
		this.index1 = index1;
		this.index2 = index2;
	}
	
	public int index1()
	{
		return index1;
	}
	
	public int index2()
	{
		return index2;
	}
	
	public void setSemaphore(Semaphore s) {} //to be overridden in RGrid
}
