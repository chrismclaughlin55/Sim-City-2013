package city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.gui.BusGui;

public class BusAgent {

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
	BusState myState;
	BusGui busgui;
	List<myPassenger> passengers;
	//LinkedList<BusStopAgent> stops;
	BusStopAgent curr;
	BusStopAgent next;
	private Semaphore atDestination = new Semaphore(0,true);
	
	public BusAgent() {
		busgui = new BusGui(this);
		BusState bs = BusState.moving;
		passengers = new ArrayList<myPassenger>();
		//SHOULD ALSO HAVE A DEFAULT STARTING POSITION
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
	}

	//CALLED BY BUSGUI
	public void msgAtStop() { 
		BusStopAgent temp=curr.nextStop;
		curr = next;
		next = temp;
		myState = BusState.atStop;
	}
	
	//SCHEDULER
	private boolean pickAndExecuteAnAction() {
		if(myState==BusState.atStop) {
			UnloadPassengers();
			return true;
		}

		if(myState==BusState.unloading) {
			curr.msgArrivedAtStop(this);
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
	}

	private void BoardPassengers() {
		myState=BusState.leavingStop;
	    for(myPassenger p: passengers) {
	        if(p.ps==PassengerState.gotOn) {
	            //p.p.BusIsHere(this);
	        }
	    }
	}

	private void UnloadPassengers() {
		myState = BusState.unloading;    
		for(myPassenger p : passengers) {
			if (p.dest.equals(curr)) {
				//p.p.msgArrivedAtBusStop(curr);
				//personGui animation runs in bus’s thread until animation
				//finished
		        passengers.remove(p);        
			}
		}
	}
	//ACTIONS

	
}
