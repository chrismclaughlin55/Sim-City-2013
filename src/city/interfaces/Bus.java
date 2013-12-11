package city.interfaces;

import java.util.HashMap;
import java.util.Map;

import city.BusStopAgent;
import city.PersonAgent;
import city.RGrid;

public interface Bus {

	//CALLED BY BUSGUI
	public abstract void msgAtDestination();

	public abstract void msgOnBus();

    public abstract int getRouteNumber();

	public abstract void msgAcquireGrid(RGrid nextRGrid);

	public abstract void setCurrentGrid(RGrid currGrid);

	public abstract void msgAcquireGrid();

	void msgPeopleAtStop(Map<PersonAgent, BusStopAgent> peopleAtStop);
}