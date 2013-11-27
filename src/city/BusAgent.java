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
	List<myPassenger> passengers;
	//LinkedList<BusStopAgent> stops;
	BusStopAgent curr;
	BusStopAgent next;
	private Semaphore atDestination = new Semaphore(0,true);
	
	public BusAgent(CityData cd) {
		this.cd = cd;
		curr = cd.busStops.get(0);
		next = cd.busStops.get(1);
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
	@Override
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
       readyToBoard = true;
        for ( PersonAgent p : peopleAtStop.keySet()) {
        	passengers.add(new myPassenger(p,peopleAtStop.get(p)));
        }
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
			curr.msgArrivedAtStop(this);
			for(myPassenger p : passengers) {
				if(p.dest == curr) {
					UnloadPassenger(p);
					passengers.remove(p);
					return true;
				}
			}
			myState = BusState.unloading;   
			return true;
		}

		if(myState==BusState.unloading) {
			myState=BusState.waitingForResponse;
			return true;
	
		}
		
		if(myState == BusState.waitingForResponse && !readyToBoard)
		{
			super.stateChange.drainPermits();
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
	
	private void LeaveStop() {
		BusStopAgent temp=curr.nextStop;
		curr = next;
		next = temp;
		busgui.DoGoToNextStop(next.getX(),next.getY());
	    myState=BusState.moving;
	    atDestination.drainPermits();
	    try {
	    	atDestination.acquire();
	    }
	    catch(Exception e) {}
	    
	}

	private void BoardPassengers() {
		atDestination.drainPermits();
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
	        }
	    }
	    myState=BusState.leavingStop;
	    readyToBoard = false;
	}

	private void UnloadPassenger(myPassenger p) {
		
		//have a wait time for loading and unloading
		p.p.msgDoneMoving();
		atDestination.drainPermits();
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
