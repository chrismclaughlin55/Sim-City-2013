package city;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusStopAgent {

	/*
	 * 
	 * Need to change a lot in here... check back from busAgent
	 */
	List<PersonAgent> waitingPeople;
	class myPerson {
        PersonAgent p;
        BusStopAgent next;
	};
	CityData CityData;
    HashMap<PersonAgent, BusStopAgent> peopleWaiting;
    BusStopAgent nextStop;
    BusAgent currentBus;
    //CityData places a square at coordinates of this particular BusStop

	public BusStopAgent() {
		waitingPeople = new ArrayList<PersonAgent>();
		peopleWaiting = new HashMap<PersonAgent, BusStopAgent>();
	}
	
	
	
	public void msgWaitingAtStop(PersonAgent p, BusStopAgent destination) {
        peopleWaiting.put(p, destination);
    }
	
    public void msgArrivedAtStop(BusAgent bus) {
        currentBus = bus;
    }
    
    
    private boolean pickAndExecuteAnAction() {
    	if(currentBus != null)
        {
            BoardPassengers();
            return true;
        }

    	return false;
    }
	
    
    private void BoardPassengers()
    {
        currentBus.msgPeopleAtStop(peopleWaiting);
    }

}
