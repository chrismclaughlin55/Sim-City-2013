package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Agent;
import city.gui.BusStopGui;
import city.interfaces.Bus;
import city.interfaces.BusStop;
public class BusStopAgent extends Agent implements BusStop{

	/*
	 * 
	 * Need to change a lot in here... check back from busAgent
	 */
	List<PersonAgent> waitingPeople;
	class myPerson {
        PersonAgent p;
        BusStop next;
	};
	public enum BusStopState {waitingForBus, busHere, busLeaving };
	BusStopState stopState;
	CityData cd;
    HashMap<PersonAgent, BusStopAgent> peopleWaiting;
    BusStopAgent nextStop;
    Bus currentBus;
    BusStopGui busStopGui;
    int xPosition; //will be where bus should be to be next to this stop
    int yPosition; // will be where bus needs to be
    //actual painting coordinates will be handled by gui
    //CityData places a square at coordinates of this particular BusStop
    int stopNumber;

	public BusStopAgent(CityData cd) {
		waitingPeople = new ArrayList<PersonAgent>();
		peopleWaiting = new HashMap<PersonAgent, BusStopAgent>();
		
	}
	
	public BusStopAgent(int xPos, int yPos, CityData cd) {
		this.cd = cd;
		stopState = BusStopState.waitingForBus;
		xPosition = xPos;
		yPosition = yPos;
		waitingPeople = new ArrayList<PersonAgent>();
		peopleWaiting = new HashMap<PersonAgent, BusStopAgent>();
	}
	
	public void setNextStop(BusStopAgent nextStop) {
		this.nextStop = nextStop;
	}
	
	public BusStop getNextStop() {
		return nextStop;
	}
	
	public void setGui(BusStopGui gui,int x, int y) {
		busStopGui = gui;
		busStopGui.setX(x);
		busStopGui.setY(y);
		xPosition = x;
		yPosition = y;
	}
	
	public int getX() {
		return xPosition;
	}
	
	public int getY() {
		return yPosition;
	}
	
	
	/* (non-Javadoc)
	 * @see city.BusStop#msgWaitingAtStop(city.PersonAgent, city.BusStopAgent)
	 */
	@Override
	public void msgWaitingAtStop(PersonAgent p, BusStopAgent destination) {
        peopleWaiting.put(p, destination);
    }
	
    /* (non-Javadoc)
	 * @see city.BusStop#msgArrivedAtStop(city.interfaces.Bus)
	 */
    @Override
	public void msgArrivedAtStop(Bus bus) {
        currentBus = bus;
        stopState = BusStopState.busHere;
        stateChanged();
    }
    
    
    protected boolean pickAndExecuteAnAction() {
    	if(stopState == BusStopState.busHere)
        {
            BoardPassengers();
            return true;
        }
    	if(stopState == BusStopState.busLeaving) {
    		ClearPassengers();
    		return true;
    	}
    	return false;
    }
	
    
    private void BoardPassengers()
    {
        currentBus.msgPeopleAtStop(peopleWaiting);
        stopState = BusStopState.busLeaving;
          
    }
    
    private void ClearPassengers() {
    	peopleWaiting.clear();
    	stopState = BusStopState.waitingForBus;
    }
    
    protected void stateChanged() {
    	super.stateChanged();
    }
    
    public int getStopNumber() {
    	return stopNumber;
    }
    
    public void setStopNumber(int num) {
    	stopNumber = num;
    }
}