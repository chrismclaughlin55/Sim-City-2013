package city.interfaces;

import city.BusStopAgent;
import city.PersonAgent;

public interface BusStop {

	public abstract void msgWaitingAtStop(PersonAgent p,
			BusStopAgent destination);

	public abstract void msgArrivedAtStop(Bus bus);

}