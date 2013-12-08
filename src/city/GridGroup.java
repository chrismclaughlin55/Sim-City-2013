package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GridGroup 
{
	private List<RGrid> grids;
	private Semaphore sync = new Semaphore(1, true);
	
	public GridGroup()
	{
	}
	
	public boolean intersects(GridGroup g)
	{
		for(RGrid grid1 : grids)
		{
			for(RGrid grid2 : g.grids)
			{
				if(grid1 == grid2)
				{
					return true;
				}
			}
		}
		
		return false;
	}
}
