package restaurantMQ.test;

import city.PersonAgent;
import restaurantMQ.MQCashierRole;
import restaurantMQ.test.mock.LoggedEvent;
import restaurantMQ.test.mock.MockCustomer;
import restaurantMQ.test.mock.MockMarket;
import restaurantMQ.test.mock.MockWaiter;
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
	MQCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market1;
	MockMarket market2;
	PersonAgent p = new PersonAgent("cashier");
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new MQCashierRole(p);		
		customer = new MockCustomer("mockcustomer");
		customer.cashier = cashier;
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
		
	}	
	
	//Make sure the cashier properly pays a market when the order is delivered
	public void testOneOrderOneMarket()
	{
		//Check preconditions
		assertEquals("Cashier should have 0 bills from the market. It doesnt.", cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		//Step 1, the bill comes in from the market
		cashier.msgHereIsBill(market1, 12.00); //Bill for $12.00 from market1
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier should have logged that it received the bill. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received bill."));
		
		//Step 2, allow the cashier's scheduler to respond to the input
		assertTrue("Cashier's scheduler should have returne true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//Step 3, test to see if the cashier has sent the proper message to the mock waiter
		assertEquals("mockmarket1 should have logged exactly 1 event. It didn't.", 1, market1.log.size());
		assertTrue("mockmarket 1 should have logged the payment made by the cashier of $12. Instead, the log reads: " +
				market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment from cashier"));
		assertEquals("Cashier should have 0 bills. It doesnt.", cashier.bills.size(), 0);
		assertEquals("Cashier's money should have decreased by 12. It didn't.", cashier.money, 9988.0);
	}
	
	//Test to see if the cashier properly pays both markets when an order is divided between them
	public void testOneOrderTwoMarkets()
	{
		//Test preconditions
		assertEquals("Cashier should have 0 bills. It doesnt.", cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		//Step 1, the bill comes in from the first market
		cashier.msgHereIsBill(market1, 12.00);
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier should have logged that it received the bill. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received bill."));
		
		//Step 2, the bill comes in from the second market
		cashier.msgHereIsBill(market2, 15.00);
		assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.bills.size(), 2);
		assertEquals("Cashier should have logged 2 events by now. It hasn't.", cashier.log.size(), 2);
		assertTrue("Cashier should have logged that it received the bill. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received bill."));
		
		//Step 3, the cashier pays the first bill
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("mockmarket1 should have logged exactly 1 event. It didn't.", 1, market1.log.size());
		assertTrue("mockmarket 1 should have logged the payment made by the cashier of $12. Instead, the log reads: " +
				market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment from cashier"));
		assertEquals("Cashier should now only have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertEquals("mockmarket2 should have logged exactly 0 events. It hasn't.", 0, market2.log.size());
		assertEquals("Cashier's money should have decreased by 12. It didn't.", cashier.money, 9988.0);
		
		//Step 4, the cashier pays the second bill
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("mockmarket2 should have logged exactly 1 event. It didn't.", 1, market2.log.size());
		assertTrue("mockmarket 2 should have logged the payment made by the cashier of $15. Instead, the log reads: " +
				market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received payment from cashier"));
		assertEquals("Cashier should now have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertEquals("mockmarket1 should have logged exactly 1 event. It hasn't.", 1, market1.log.size());
		assertEquals("Cashier's money should be 27 less than 10000. It isn't.", cashier.money, 9973.0);
	}
	
	//Check to make sure the cashier properly makes a check upon the waiter's request
	public void testOneNormalWaiterScenario()
	{
		customer.cashier = cashier;
		
		//Check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		//Step 1, the waiter sends the cashier the check request
		cashier.msgProduceCheck(waiter, customer, "Steak");
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 check request. It doesn't.", cashier.checkRequests.size(), 1);
		
		//Step 2, the cashier generates the check and gives it to the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals(
				"MockWaiter should have 1 event logged after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertEquals("Cashier should have 0 check requests. It doesn't.", cashier.checkRequests.size(), 0);
		assertEquals("Cashier should have created 1 check. It hasn't.", cashier.checks.size(), 1);
		assertEquals(
				"MockWaiter should have logged 1 event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertTrue("Mockwaiter should have logged that it received the check from the cashier. Instead, the log reads " +
						waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		
	}
	
	//Check the normative scenario where the customer pays in full
	public void testOneNormalCustomerScenario()
	{
		//Check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
				
		//Step 1, the waiter sends the cashier the check request
		cashier.msgProduceCheck(waiter, customer, "Steak");
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 check request. It doesn't.", cashier.checkRequests.size(), 1);
		assertEquals("Cashier should have logged 1 event. It didn't.", cashier.log.size(), 1);
		assertTrue("Cashier should have logged that it received the request. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received check request."));
			
		//Step 2, the cashier generates the check and gives it to the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals(
				"MockWaiter should have 1 event logged after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertEquals("Cashier should have 0 check requests. It doesn't.", cashier.checkRequests.size(), 0);
		assertEquals("Cashier should have created 1 check. It hasn't.", cashier.checks.size(), 1);
		assertEquals(
				"MockWaiter should have logged 1 event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertTrue("Mockwaiter should have logged that it received the check from the cashier. Instead, the log reads " +
							waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		
		//Step 3, the customer pays the cashier for the steak
		cashier.msgHereIsMoney(customer, 15.99);
		assertEquals("Cashier should have logged 2 events. It didn't.", cashier.log.size(), 2);
		assertTrue("Cashier should have logged that it received the payment. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment."));
		assertEquals("Cashier should have 1 payment. It doesn't.", cashier.payments.size(), 1);
		
		//Step 4, the cashier tells the customer it is okay to leave
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Customer should have logged one event upon receiving the message. It didn't.", customer.log.size(), 1);
		assertTrue("Customer should have recieved the good to go message. Instead, the log reads " +
				customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Good to go."));
		assertEquals("Cashier should now have 0 payments. It doesn't.", cashier.payments.size(), 0);
		assertEquals("Cashier should now have 0 checks. It doesn't", cashier.checks.size(), 0);
		assertEquals("Cashier's money should have increased by 15.99. It didn't.", cashier.money, 10015.99);
	}
	
	//Test non-normative scenario where the customer is unable to pay the full bill
	public void testOneNonNormalCustomerScenario()
	{
		//Check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.checks.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
				
		//Step 1, the waiter sends the cashier the check request
		cashier.msgProduceCheck(waiter, customer, "Steak");
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 check request. It doesn't.", cashier.checkRequests.size(), 1);
		assertEquals("Cashier should have logged 1 event. It didn't.", cashier.log.size(), 1);
		assertTrue("Cashier should have logged that it received the request. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received check request."));
			
		//Step 2, the cashier generates the check and gives it to the waiter
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals(
				"MockWaiter should have 1 event logged after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		assertEquals("Cashier should have 0 check requests. It doesn't.", cashier.checkRequests.size(), 0);
		assertEquals("Cashier should have created 1 check. It hasn't.", cashier.checks.size(), 1);
		assertEquals(
				"MockWaiter should have logged 1 event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 1, waiter.log.size());
		assertTrue("Mockwaiter should have logged that it received the check from the cashier. Instead, the log reads " +
							waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from cashier."));
		
		//Step 3, the customer pays the cashier for the steak (but not fully, he is short 1 cent)
		//The proper response of the cashier is to store this and remember it the next time this customer comes
		cashier.msgHereIsMoney(customer, 15.98);
		assertEquals("Cashier should have logged 2 events. It didn't.", cashier.log.size(), 2);
		assertTrue("Cashier should have logged that it received the payment. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received payment."));
		assertEquals("Cashier should have 1 payment. It doesn't.", cashier.payments.size(), 1);
		
		//Step 4, the cashier tells the customer it is okay to leave
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Customer should have logged one event upon receiving the message. It didn't.", customer.log.size(), 1);
		assertTrue("Customer should have recieved the 'not enough' message. Instead, the log reads " +
				customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Not enough."));
		assertEquals("Cashier should now have 0 payments. It doesn't.", cashier.payments.size(), 0);
		assertEquals("Cashier should have saved the customer's check (so that he has 1 check). He doesn't", cashier.checks.size(), 1);
		assertEquals("Cashier's money should have increased by 15.98. It didn't", cashier.money, 10015.98);
	}
	
	public void testNonNormativeMarketScenario()
	{
		cashier.setMoney(11.0); //cashier will be billed for $12 but he only has $11
		//Check preconditions
		assertEquals("Cashier should have 0 bills from the market. It doesnt.", cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		
		//Step 1, the bill comes in from the market
		cashier.msgHereIsBill(market1, 12.00); //Bill for $12.00 from market1
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier should have logged that it received the bill. Instead, the log reads " +
				cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received bill."));
		
		//Step 2, allow the cashier's scheduler to respond to the input
		assertTrue("Cashier's scheduler should have returne true, but didn't.", cashier.pickAndExecuteAnAction());
		
		//Step 3, test to see if the cashier has sent the proper message to the mock waiter
		assertEquals("mockmarket1 should have logged exactly 1 event. It didn't.", 1, market1.log.size());
		assertTrue("mockmarket 1 should have logged the payment made by the cashier of $11. Instead, the log reads: " +
				market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received payment from cashier"));
		assertEquals("Cashier should have 0 bills. It doesnt.", cashier.bills.size(), 0);
		assertEquals("Cashier should have 0 money because he tried to pay the bill to the best of his ability.",
				cashier.money, 0.0);
	}
	
	/*
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		Bill bill = new Bill(cashier, customer, 2, 7.98);
		cashier.HereIsBill(bill);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
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
		
	
	}//end one normal customer scenario
	
	*/
	
	
}
