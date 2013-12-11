package bank.test;

import bank.Bank;
import bank.BankManagerRole;
import bank.BankRobber;
import bank.TellerRole;
import bank.interfaces.BankManager;
import mainGUI.MainGui;
import city.BusAgent;
import city.BusStopAgent;
import city.CityData;
import city.PersonAgent;
import junit.framework.TestCase;

public class RobberTest extends TestCase {

	MainGui main;
	BankRobber r;
	PersonAgent p;
	PersonAgent p2;
	PersonAgent p3;
	Bank b;
	BankManagerRole bm;
	TellerRole t;
	CityData cd;
	
	public void setUp() throws Exception {
		super.setUp();
		main = new MainGui();
		cd = new CityData();
		p = new PersonAgent("Robber", main, cd);
		p.setJob("BankRobber");
		p2 = new PersonAgent("Teller", main ,cd);
		p3 = new PersonAgent("Manager", main, cd);
		bm = new BankManagerRole(p3, b);
		b = new Bank(0, 0, 0, 0, "", null, main, cd);
		b.currentManager = bm;
		r = new BankRobber(p, b);
		t = new TellerRole(p2);
	}
	
	//Tests the non normative bank robbery scenario
	public void testRobBankNoTeller() {
		double originalCash = p.cash;
		assertTrue(p.getJob().equals("BankRobber"));
		assertTrue(r.pickAndExecuteAnAction());
		r.pickAndExecuteAnAction();
		assertEquals(originalCash, p.cash);
	}
	
	public void testRobBankWithTeller() {
		double originalCash = p.cash;
		double robberyAmount = 400;
		assertTrue(p.getJob().equals("BankRobber"));
		bm.msgAddTeller(t);
		assertTrue(r.pickAndExecuteAnAction());
		r.pickAndExecuteAnAction();
		t.pickAndExecuteAnAction();
		assertEquals(originalCash + robberyAmount, p.cash);
	}
	
}
