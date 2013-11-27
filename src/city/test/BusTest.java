package city.test;

import mainGUI.MainGui;
import restaurantMQ.MQCashierRole;
import restaurantMQ.test.mock.MockCustomer;
import restaurantMQ.test.mock.MockMarket;
import restaurantMQ.test.mock.MockWaiter;
import junit.framework.TestCase;
import city.BusAgent;
import city.BusStopAgent;
import city.CityData;
import city.PersonAgent;
import city.test.mock.*;

public class BusTest extends TestCase {
	MainGui main;
	BusAgent mbus;
	BusStopAgent mbusstop;
	BusStopAgent dest;
	PersonAgent p;
	CityData cd;
	public void setUp() throws Exception{
		main = new MainGui();
		cd = new CityData();
		super.setUp();		
		p = new PersonAgent("Hello",main,cd);
		mbus = new BusAgent(cd);		
		mbusstop = new BusStopAgent(1,1,cd);
		p.currentBusStop = mbusstop;
		mbus.curr = mbusstop;
	}	
	
	//TESTS THAT BUS STOP ACCEPTS PERSON WHEN HE COMES
	public void testOneBusStopAddsPersonToList() {
		p.currentBusStop.msgWaitingAtStop(p, dest);
		
		assertTrue(mbusstop.peopleWaiting.containsKey(p));
	}
	
	//TESTS THAT 
	public void testTwoBusTellsBusStopItIsHere() {
		mbus.curr.msgArrivedAtStop(mbus);
		assertTrue(mbusstop.currentBus == mbus);
	}
	
	public void testThreePersonGetOnBoard() {
		mbus.curr.msgArrivedAtStop(mbus);
		p.currentBusStop.msgWaitingAtStop(p, dest);
		mbusstop.currentBus.msgPeopleAtStop(mbusstop.peopleWaiting);
		assertEquals(mbus.passengers.size(),1);
	}
	

}
