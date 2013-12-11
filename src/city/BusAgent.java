package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import agent.Agent;
import city.RGrid.Direction;
import city.gui.BusGui;
import city.interfaces.Bus;
import city.interfaces.BusStop;

public class BusAgent extends Agent implements Bus {

	enum BusState { moving, leavingStop, atStop, unloading, waitingForResponse, loading }
	enum PassengerState { gotOn, beenOn};
	boolean readyToBoard = false;
	class myPassenger {
	    PersonAgent p;
	    BusStop dest;
	    PassengerState ps;
	    public myPassenger(PersonAgent per,BusStop dest) {
	        this.p=per;
	        this.dest=dest;
	        ps=PassengerState.gotOn;
	    }
	}
	
	Timer timer = new Timer();
	
	CityData cd;
	BusState myState;
	BusGui busgui;
	public List<myPassenger> passengers;
	//LinkedList<BusStopAgent> stops;
	public BusStopAgent curr;
	public BusStopAgent next;
	private Semaphore atDestination = new Semaphore(0,true);
	private Semaphore boarding = new Semaphore(0, true);
	private Semaphore gridding = new Semaphore(0, true);
    public int routeNumber;
    public RGrid previousGrid = new RGrid();
    public RGrid currGrid = null;
    public RGrid gridToAcquire = null;
	public BusAgent(CityData cd, int routeNumber) {
		this.cd = cd;
        this.routeNumber = routeNumber;
        if (routeNumber == 1) {
            curr = cd.busStops.get(0);
            next = cd.busStops.get(1);
        }
        if (routeNumber == 2) {
            curr = cd.busStops.get(12);
            next = cd.busStops.get(13);
        }
		myState = BusState.leavingStop;
		passengers = Collections.synchronizedList(new ArrayList<myPassenger>());
		//SHOULD ALSO HAVE A DEFAULT STARTING POSITION
	}
	
	public void setGui(BusGui bg) {
		busgui = bg;
	}
	
	public void setCurrentGrid(RGrid currGrid)
	{
		this.currGrid = currGrid;
		currGrid.acquireGrid();
	}
	//MESSAGES
	
	//CALLED BY BUSGUI
	/* (non-Javadoc)
	 * @see city.Bus#msgAtDestination()
	 */
	public void msgAcquireGrid()
	{
		gridding.release();
	}
	
	public void msgAtDestination() {
		
		myState = BusState.atStop;
		atDestination.release();
		// TODO Auto-generated method stub
		
	}
	
	public void msgPeopleAtStop(Map<PersonAgent,BusStopAgent> peopleAtStop) {
		synchronized(peopleAtStop) {
        for ( PersonAgent p : peopleAtStop.keySet()) {
        	passengers.add(new myPassenger(p,peopleAtStop.get(p)));
        }
		}
        readyToBoard = true;
        stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see city.Bus#msgOnBus()
	 */
	@Override
	public void msgOnBus()
	{
		boarding.release();
	}



	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		
		if(myState==BusState.atStop) {
			for(myPassenger p : passengers) {
				if(p.dest == curr) {
					UnloadPassenger(p);
					passengers.remove(p);
					return true;
				}
			}
			curr.msgArrivedAtStop(this);
			myState = BusState.unloading;
			return false;
		}
		
	    if(readyToBoard) {
	        BoardPassengers();
	        return true;
	    }
	    
	    if(myState==BusState.leavingStop) {
	    	
	        LeaveStop();
	        return true;
	    }
	    
	    return false;
	}
	
	private void MoveToGrid()
	{
		previousGrid.releaseGrid();
		gridToAcquire.acquireGrid();
		if(gridToAcquire.direction == Direction.none)
		{
			timer.schedule(new TimerTask() {
				public void run()
				{
					atDestination.release();
				}
			}, 300);
			try {
				atDestination.acquire();
			}
			catch(Exception e) {}
		}
		previousGrid = currGrid;
		currGrid = gridToAcquire;
		gridToAcquire = null;
		busgui.moveOn();
		try {
	    	gridding.acquire();
	    }
	    catch(Exception e) {}
	}
	
	private void LeaveStop() {
		BusStopAgent temp=next.nextStop;
		myState = BusState.moving;
		curr = next;
		next = temp;
		busgui.moveOn();
		busgui.DoGoToNextStop(curr.getX(),curr.getY());
	    try {
	    	gridding.acquire();
	    }
	    catch(Exception e) {}
	    while(gridToAcquire != null) {
	    	MoveToGrid();
	    }
	    try {
	    atDestination.acquire();
	    }
	    catch(Exception e) {}
	}

	private void BoardPassengers() {
	    for(myPassenger p: passengers) {
	        if(p.ps==PassengerState.gotOn) {
	        	//Message the people and have a semaphore acquire 
	        	//and release cycle for every
	        	//person getting off bus
	        	p.ps = PassengerState.beenOn;
	            p.p.msgBusIsHere(this);
	            try {
	            	boarding.acquire();
	            }
	            catch(Exception e){}
	            AlertLog.getInstance().logMessage(AlertTag.BUS, "bus", "Boarding passengers");
	            return;
	        }
	    }
	    myState=BusState.leavingStop;
	    readyToBoard = false;
	}

	private void UnloadPassenger(myPassenger p) {
		
		//have a wait time for loading and unloading
		p.p.msgDoneMoving();
		try {
        	boarding.acquire();
        }
        catch(Exception e){}
		AlertLog.getInstance().logMessage(AlertTag.BUS, "bus", "Unloading passengers");
	}

	protected void stateChanged() {
		super.stateChanged();
	}
	//ACTIONS

	public int getX() {
		return busgui.xPos;
		
	}
	
	public int getY() {
		return busgui.yPos;
		
	}

	public int getRouteNumber() {
		return routeNumber;
	}

	public void msgAcquireGrid(RGrid nextRGrid) {
		gridToAcquire = nextRGrid;
		gridding.release();
	}

}
