package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Intersection 
{
	private RGrid[] grids;
	private Semaphore sync = new Semaphore(1, true);
	
	public Intersection(RGrid[] grids)
	{
		this.grids = grids;
	}
	
	public void acquireGroup()
	{
		try {
		sync.acquire();
		}
		catch(Exception e) {};
	}
	
	public void releaseGroup()
	{
		sync.release();
	}
}
