package city.interfaces;

import java.util.HashMap;

import city.BusStopAgent;
import city.PersonAgent;

public interface Bus {

	//CALLED BY BUSGUI
	public abstract void msgAtDestination();

	public abstract void msgPeopleAtStop(
			HashMap<PersonAgent, BusStopAgent> peopleAtStop);

	public abstract void msgOnBus();

}