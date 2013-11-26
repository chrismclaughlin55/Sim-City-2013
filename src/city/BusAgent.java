package city;

import java.util.ArrayList;
import java.util.Collections;
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
	public void msgAtDestination() {
		
		myState = BusState.atStop;
		atDestination.release();
		stateChanged();
		// TODO Auto-generated method stub
		
	}
	public void msgPeopleAtStop(HashMap<PersonAgent,BusStopAgent>peopleAtStop) {
        myState=BusState.loading;
        for ( PersonAgent p : peopleAtStop.keySet()) {
        	passengers.add(new myPassenger(p,peopleAtStop.get(p)));
        }
        stateChanged();
	}
	
	public void msgOnBus()
	{
		atDestination.release();
	}



	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		//System.out.println("what");
		if(myState==BusState.atStop) {
			curr.msgArrivedAtStop(this);
			for(myPassenger p : passengers) {
				if(p.dest == curr) {
					UnloadPassenger(p);
					passengers.remove(p);
					return true;
				}
			}
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
		BusStopAgent temp=curr.nextStop;
		curr = next;
		next = temp;
        stateChanged();
		busgui.DoGoToNextStop(next.getX(),next.getY());
	    myState=BusState.moving;
	    try {
	    	atDestination.acquire();
	    }
	    catch(Exception e) {}
	    
	}

	private void BoardPassengers() {
		myState=BusState.leavingStop;
		synchronized(passengers) {
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
		}
	    
	}

	private void UnloadPassenger(myPassenger p) {
		myState = BusState.unloading;   
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
