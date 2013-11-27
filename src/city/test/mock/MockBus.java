package city.test.mock;

import java.util.HashMap;

import city.BusStopAgent;
import city.PersonAgent;
import city.interfaces.Bus;

public class MockBus implements Bus {

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		System.out.println("");
	}

	@Override
	public void msgPeopleAtStop(HashMap<PersonAgent, BusStopAgent> peopleAtStop) {
		// TODO Auto-generated method stub
		System.out.println("Here are people to load");
	}

	@Override
	public void msgOnBus() {
		// TODO Auto-generated method stub
		
	}

}
