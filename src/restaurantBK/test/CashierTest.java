package restaurantBK.test;

import city.PersonAgent;
import restaurantBK.BKCashierRole;
import restaurantBK.BKCashierRole.CheckState;
import restaurantBK.BKCashierRole.Check;
import restaurantBK.BKCashierRole.ShipmentState;
import restaurantBK.test.mock.MockCustomer;
import restaurantBK.test.mock.MockMarket;
import restaurantBK.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	BKCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market1;
	MockMarket market2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		PersonAgent p = new PersonAgent("cashier");
		cashier = new BKCashierRole(p,"cashier",null);		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testMarketTestOne() {
		//Cashier starts with 5000.00 in capital;
		market1.setCashier(cashier);
		
		assertEquals("Cashier should have no shipment bills yet.",0,cashier.ships.size());
		
		cashier.msgHereIsMarketBill(market1, 50.00);
		
		assertEquals("Cashier should have received the shipment bill",1,cashier.ships.size());
		
		cashier.ships.get(0).m.msgPayingMarketBill(cashier, cashier.ships.get(0).bill);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier capital should have decreased by bill amount",cashier.capital,450.0);
		
		cashier.msgThanksForPayingBill(market1, 50.00);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier should have no shipment bills again.",0,cashier.ships.size());
		//MAKE SURE THE MESSAGE HAS BEEN CALLED
		//MAKE SURE THE CAPITAL IS DECREASED BY 50
		System.out.println("END OF TEST CASE MARKET 1");
	}
	
	public void testMarketTestTwo() {
		market1.setCashier(cashier);
		
		assertEquals("Cashier should have no shipment bills yet.",0,cashier.ships.size());
		
		cashier.msgHereIsMarketBill(market1, 50.00);
		
		assertEquals("Cashier should have received the 1st shipment bill",1,cashier.ships.size());
		
		market2.setCashier(cashier);
		cashier.msgHereIsMarketBill(market2, 50.00);
		
		assertEquals("Cashier should have received the 2nd shipment bill",2,cashier.ships.size());
		
		cashier.ships.get(0).m.msgPayingMarketBill(cashier, cashier.ships.get(0).bill);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier capital should have decreased by bill amount",cashier.capital,450.0);
		
		cashier.msgThanksForPayingBill(market1, 50.00);
		cashier.pickAndExecuteAnAction();		
		cashier.ships.get(0).m.msgPayingMarketBill(cashier, cashier.ships.get(0).bill);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier capital should have decreased by bill amount",cashier.capital,400.0);
		
		cashier.msgThanksForPayingBill(market2, 50.00);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier should have no shipment bills again.",0,cashier.ships.size());
	
		System.out.println("END OF TEST CASE MARKET TWO");
	}
	
	public void testMarketExtraCredit() {
		market1.setCashier(cashier);
		
		assertEquals("Cashier should have no shipment bills yet.",0,cashier.ships.size());
		cashier.msgHereIsMarketBill(market1, 550.00);
		
		assertEquals("Cashier should have received the shipment bill",1,cashier.ships.size());
		
		cashier.ships.get(0).m.msgPayingMarketBill(cashier, cashier.ships.get(0).bill);
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier makes bill into cantpay state",ShipmentState.cantpay,cashier.ships.get(0).ss);
		cashier.capital+=51.00;
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier capital should have decreased by bill amount",cashier.capital,1.0);
		cashier.msgThanksForPayingBill(market1, 550.00);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Cashier should have no shipment bills again.",0,cashier.ships.size());

		System.out.println("END OF TEST CASE MARKETEXTRACREDIT");
	}
	public void testOneWaiterTellsCashierToMakeCheck()
	{
		//setUp() runs first before this test!
		
		customer.setCashier(cashier);//You can do almost anything in a unit test.			
		waiter.setCashier(cashier);
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		//Check check = cashier.new Check(customer,waiter, 7.98, 2);
		waiter.cashier.msgMakeCheck(customer,waiter,7.98,2);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 check. It doesn't.", cashier.checks.size(), 1);
		
		cashier.checks.get(0).w.msgCheckMade(7.98,2);
		assertEquals("Check should be pending.",CheckState.pending,cashier.checks.get(0).chs);
		//assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		System.out.println("END OF TEST CASE ONE WAITER TELLS CASHIER TO MAKE CHECK");
		//SCENARIO 2 - Customer pays for check
		
		//assertEquals("MockCustomer should have less money because he paid",0,)
		/*
		//step 2 of the test
		cashier.ReadyToPay(customer, bill);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.customerApproached);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).bill.customer == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
		
		
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.bills.get(0).changeDue, cashier.bills.get(0).changeDue == 0);
		
		
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.done);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//step 5
		 CASHIER AND MARKET INTERACTION
		 
		*/
	}//end one normal customer scenario
	
	public void testTwoCustomerPaysCashier() {
		customer.setCashier(cashier);
		waiter.setCashier(cashier);
		waiter.cashier.msgMakeCheck(customer,waiter,7.98,2);
		
		assertEquals("Check should be pending",CheckState.pending,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		customer.cashier.msgTakeMyMoney(7.98, customer);
		
		assertEquals("Check should be pending",CheckState.beingPaid,cashier.checks.get(0).chs);
		
		cashier.checks.get(0).c.msgAllPaidPlusChange(0);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Check should be paid for",CheckState.paid,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Check should be removed",cashier.checks.size(),0);
	
		System.out.println("END OF TEST CASE TWO CUSTOMER CASHIER");
	}
	
	public void testThreeCustomerFailsToPayCashierAndCashierRemembersCustomer() {
		//cashier.oweme.get
		customer.setCashier(cashier);
		waiter.setCashier(cashier);
		waiter.cashier.msgMakeCheck(customer,waiter,7.98,2);
		
		assertEquals("Check should be pending",CheckState.pending,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		customer.cashier.msgTakeMyMoney(0.00, customer);
		
		assertEquals("Check should be pending",CheckState.beingPaid,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		
		assertTrue("Cashier remembers customer",cashier.oweme.containsKey(customer));
		assertTrue("Cashier remembers how much customer owes",cashier.oweme.get(customer)==7.98);
	
		System.out.println("END OF TEST CASE THREE CUSTOMER FAILS TO PAY CASHIER");
	}
	
	public void testFourACustomerWhoOwesMoneyComesBackAndCashierAsksForMoneyOwedPlusOriginalPrice() {
		customer.setCashier(cashier);
		waiter.setCashier(cashier);
		cashier.oweme.put(customer, 10.00);
		
		assertTrue("Cashier remembers customer",cashier.oweme.containsKey(customer));
		
		waiter.cashier.msgMakeCheck(customer,waiter,7.98,2);
		
		assertEquals("Check should be pending",CheckState.pending,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		customer.cashier.msgTakeMyMoney(17.98, customer);
		cashier.pickAndExecuteAnAction();
		
		assertEquals("Owed price tacked onto check price",cashier.checks.get(0).price,10+7.98);
		
		cashier.checks.get(0).c.msgAllPaidPlusChange(0);
		
		assertEquals("Check should be paid",CheckState.paid,cashier.checks.get(0).chs);
		
		cashier.pickAndExecuteAnAction();
		
		assertFalse("Customer gone from oweme",cashier.oweme.containsKey(customer));
		assertEquals("Check should be removed",cashier.checks.size(),0);	
		
		System.out.println("END OF TEST CASE FOUR FLAKY CUSTOMER RETURNS");
	}
	
}
