package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
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
	CityData cd;
	BusState myState;
	BusGui busgui;
	public List<myPassenger> passengers;
	//LinkedList<BusStopAgent> stops;
	public BusStopAgent curr;
	public BusStopAgent next;
	private Semaphore atDestination = new Semaphore(0,true);
    public int routeNumber;
	
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
	//MESSAGES
	
	//CALLED BY BUSGUI
	/* (non-Javadoc)
	 * @see city.Bus#msgAtDestination()
	 */
	public void msgAtDestination() {
		
		myState = BusState.atStop;
		atDestination.release();
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see city.Bus#msgPeopleAtStop(java.util.HashMap)
	 */
	@Override
	public void msgPeopleAtStop(HashMap<PersonAgent,BusStopAgent>peopleAtStop) {
        for ( PersonAgent p : peopleAtStop.keySet()) {
        	passengers.add(new myPassenger(p,peopleAtStop.get(p)));
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
		atDestination.release();
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
			return true;
		}

		if(myState==BusState.unloading) {
			myState=BusState.waitingForResponse;
			return true;
	
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
	
	private void LeaveStop() {
		BusStopAgent temp=next.nextStop;
		curr = next;
		next = temp;
		busgui.DoGoToNextStop(curr.getX(),curr.getY());
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
	            	atDestination.acquire();
	            }
	            catch(Exception e){}
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
        	atDestination.acquire();
        }
        catch(Exception e){}
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

	
}
