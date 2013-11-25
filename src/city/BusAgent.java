package city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.gui.BusGui;

public class BusAgent extends Agent {

	enum BusState { moving, leavingStop, atStop, unloading, waitingForResponse, loading }
	enum PassengerState { gotOn, beenOn};
	class myPassenger {
	    PersonAgent p;
	    BusStopAgent dest;
	    PassengerState ps;
	    public myPassenger(PersonAgent per,BusStopAgent dest) {
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
	
	public BusAgent() {
		cd = new CityData();
		curr = cd.busStops.get(0);
		next = cd.busStops.get(1);
		BusState bs = BusState.leavingStop;
		passengers = new ArrayList<myPassenger>();
		//SHOULD ALSO HAVE A DEFAULT STARTING POSITION
	}
	
	public void setGui(BusGui bg) {
		busgui = bg;
	}
	//MESSAGES
	public void msgAtDestination() {
		atDestination.release();
		// TODO Auto-generated method stub
		
	}
	public void msgPeopleAtStop(HashMap<PersonAgent,BusStopAgent>peopleAtStop) {
        myState=BusState.loading;
        for ( PersonAgent p : peopleAtStop.keySet()) {
        	passengers.add(new myPassenger(p,peopleAtStop.get(p)));
        }
        stateChanged();
	}

	//CALLED BY BUSGUI
	public void msgAtStop() { 
		BusStopAgent temp=curr.nextStop;
		curr = next;
		next = temp;
		myState = BusState.atStop;
		stateChanged();
	}
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		if(myState==BusState.atStop) {
			UnloadPassengers();
			return true;
		}

		if(myState==BusState.unloading) {
			myState=BusState.waitingForResponse;
			return true;
	
		}
	    if(myState==BusState.loading) {
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
		busgui.DoGoToNextStop(next.getX(),next.getY());
	    myState=BusState.moving;
	    try {
	    	atDestination.acquire();
	    }
	    catch(Exception e) {}
	    
	}

	private void BoardPassengers() {
		myState=BusState.leavingStop;
	    for(myPassenger p: passengers) {
	        if(p.ps==PassengerState.gotOn) {
	        	//Message the people and have a semaphore acquire 
	        	//and release cycle for every
	        	//person getting off bus
	            //p.p.BusIsHere(this);
	        }
	    }
	}

	private void UnloadPassengers() {
		myState = BusState.unloading;   
		//have a wait time for loading and unloading
		for(myPassenger p : passengers) {
			if (p.dest.equals(curr)) {
				//have a small wait time as that person gets off
				//acquire semaphore per person
				//p.p.msgArrivedAtBusStop(curr);
				//personGui animation runs in bus’s thread until animation
				//finished
		        passengers.remove(p);        
			}
		}
		curr.msgArrivedAtStop(this);
	}
	//ACTIONS

	
}
