package restaurantLY.test;

import restaurantLY.interfaces.Cashier;
import restaurantLY.*;
import restaurantLY.LYCashierRole.checkState;
import restaurantLY.LYCashierRole.marketCheckState;
import restaurantLY.test.mock.*;
import junit.framework.*;
import market.Market;
import restaurantLY.LYCashierRole;

import java.util.*;

import city.PersonAgent;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase {
	//these are instantiated for each test separately via the setUp() method.
	LYCashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockCustomer customer2;
	MockCustomer customer3;
	MockMarket market;
	MockMarket market2;
	PersonAgent p = new PersonAgent("cashier");
	
	 /**
     * This method is run before each test. You can use it to instantiate the class variables
     * for your agent and mocks, etc.
     */
    public void setUp() throws Exception{
            super.setUp();                
            cashier = new LYCashierRole(p, 100.0, null);                
            customer = new MockCustomer("MockCustomer");
            customer2 = new MockCustomer("MockCustomer");
            customer3 = new MockCustomer("MockCustomer");
            waiter = new MockWaiter("MockWaiter");
            market = new MockMarket("MockMarket");
            market2 = new MockMarket("MockMarket");
    }
    
    /*public void testOneMarketScenario() {
    	System.out.print("TEST PAY ONE MARKET SCENARIO\n");
    	
		//setUp() runs first before this test!
		
		//check preconditions
    	assertEquals("Cashier should have 0 markets in it. It doesn't.", cashier.markets.size(), 0);
    	assertEquals("CashierAgent should have an empty event log before the Cashier's msgGetBillFromMarket is called. Instead, the Cashier's event log reads: "
        				+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		//******cashier.msgGetBillFromMarket(market, 9.9, null);//send the message from a market
		//cashier.pickAndExecuteAnAction();
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 1 market in it. It doesn't.", cashier.markets.size(), 1);
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                		+ market.log.toString(), 0, market.log.size());
		assertTrue("Cashier should contain a market's check with state == ready. It doesn't.",
                		cashier.markets.get(0).state == marketCheckState.ready);
		assertTrue("Cashier should contain a market with check price == 9.9. It doesn't.",
 						cashier.markets.get(0).money == 9.9);
		
		//step 2
		assertTrue("Cashier's scheduler should have returned true (react to action payToMarket from a market), but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 2 / preconditions for step 3
        assertTrue("Cashier should have logged \"Paying $9.90 to MockMarket\" but didn't. His log reads instead: " 
                		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Paying $9.90 to MockMarket"));
        assertTrue("MockMarket should have logged an event for receiving \"Receiving $9.9 from cashier\" with the correct money, but his last event logged reads instead: " 
                        + market.log.getLastLoggedEvent().toString(), market.log.containsString("Receiving $9.9 from cashier"));
        assertTrue("Cashier should contain a market's check with state == done. It doesn't.",
                        cashier.markets.get(0).state == marketCheckState.done);
        
        //step 4
        assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                        cashier.pickAndExecuteAnAction());
	}
    
    public void testTwoMarketsScenario() {
    	System.out.print("TEST PAY TWO MARKETS SCENARIO\n");
    	
		//setUp() runs first before this test!
    	List<MockMarket> markets = new ArrayList<MockMarket>();
    	List<Double> marketChecks = new ArrayList<Double>();
    	int marketNumber = 2;
		
		//check preconditions
    	assertEquals("Cashier should have 0 markets in it. It doesn't.", cashier.markets.size(), 0);
    	assertEquals("CashierAgent should have an empty event log before the Cashier's msgGetBillFromMarket is called. Instead, the Cashier's event log reads: "
        				+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
    	markets.add(market);
		markets.add(market2);
		marketChecks.add(3.96);
		marketChecks.add(5.94);
		for (int i = 0; i < marketNumber; i++) {
			//******cashier.msgGetBillFromMarket(markets.get(i), marketChecks.get(i), null);//send the message from a market
		}
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 2 market in it. It doesn't.", cashier.markets.size(), 2);
		
		assertEquals("The first MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
		                + markets.get(0).log.toString(), 0, markets.get(0).log.size());
		assertTrue("Cashier should contain the first market's check with state == ready. It doesn't.",
		                cashier.markets.get(0).state == marketCheckState.ready);
		assertTrue("Cashier should contain the first market with check price == 3.96. It doesn't.",
		 				cashier.markets.get(0).money == 3.96);
				
		//step 2
		assertTrue("Cashier's scheduler should have returned true (react to action payToMarket from the first market), but didn't.", cashier.pickAndExecuteAnAction());
				
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier should have logged \"Paying $3.96 to MockMarket\" but didn't. His log reads instead: " 
		                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Paying $3.96 to MockMarket"));
		assertTrue("The first MockMarket should have logged an event for receiving \"Receiving $3.96 from cashier\" with the correct money, but his last event logged reads instead: " 
		                + markets.get(0).log.getLastLoggedEvent().toString(), markets.get(0).log.containsString("Receiving $3.96 from cashier"));
		assertTrue("Cashier should contain the first market's check with state == done. It doesn't.",
		                cashier.markets.get(0).state == marketCheckState.done);
		
		assertEquals("The second MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
						+ markets.get(1).log.toString(), 0, markets.get(1).log.size());
		assertTrue("Cashier should contain the second market's check with state == ready. It doesn't.",
                		cashier.markets.get(1).state == marketCheckState.ready);
		assertTrue("Cashier should contain the second market with check price == 5.94. It doesn't.",
 						cashier.markets.get(1).money == 5.94);
		
		//step 3
		assertTrue("Cashier's scheduler should have returned true (react to action payToMarket from the second market), but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier should have logged \"Paying $5.94 to MockMarket\" but didn't. His log reads instead: " 
                		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Paying $5.94 to MockMarket"));
		assertTrue("The second MockMarket should have logged an event for receiving \"Receiving $5.94 from cashier\" with the correct money, but his last event logged reads instead: " 
                		+ markets.get(1).log.getLastLoggedEvent().toString(), markets.get(1).log.containsString("Receiving $5.94 from cashier"));
		assertTrue("Cashier should contain the second market's check with state == done. It doesn't.",
                		cashier.markets.get(1).state == marketCheckState.done);
		
		//step 4
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
		                cashier.pickAndExecuteAnAction());
	}*/
	
	public void testOneNormalCustomerScenario() {
		System.out.print("TEST ONE CUSTOMER NORMAL SCENARIO\n");
		
		//setUp() runs first before this test!
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.customers.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgCreateCheck is called. Instead, the Cashier's event log reads: "
                		+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgCreateCheck(waiter, customer, "Steak");//send the message from a waiter
		//cashier.pickAndExecuteAnAction();
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                		+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 customer in it. It doesn't.", cashier.customers.size(), 1);
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have one event log of asking check after the Cashier's scheduler is called. It doesn't.",
                        waiter.log.size(), 1);
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                        + customer.log.toString(), 0, customer.log.size());
		
		//step 2 of the test
		cashier.msgHereIsMoney(customer, 100.0);
		
		//check postconditions for step 2 / preconditions for step 3
        assertTrue("Cashier should contain a customer's check with state == gotMoney. It doesn't.",
                        cashier.customers.get(0).check.state == checkState.gotMoney);
        assertTrue("Cashier should contain a customer with money == 100.0. It doesn't.",
         				cashier.customers.get(0).money == 100.0);
        
        //step 3
        assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
                		cashier.pickAndExecuteAnAction());
        
        //check postconditions for step 3 / preconditions for step 4
        assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $100.0\" but didn't. His log reads instead: " 
                		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $100.0"));
        assertTrue("Cashier should contain a customer's check of $15.99. It contains something else instead: $" 
        				+ cashier.customers.get(0).check.price, cashier.customers.get(0).check.price == 15.99);
        assertTrue("Cashier should contain a customer's debt == 0.0. It contains something else instead: $" 
                        + cashier.customers.get(0).debt, cashier.customers.get(0).debt == 0);
        assertTrue("MockCustomer should have logged an event for receiving \"Getting change from cashier of $84.01\" with the correct change, but his last event logged reads instead: " 
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Getting change from cashier of $84.01"));
        assertTrue("Cashier should contain a customer's check with state == done. It doesn't.",
                        cashier.customers.get(0).check.state == checkState.done);
        
        //step 4
        assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                        cashier.pickAndExecuteAnAction());
	}
	
	public void testOneNoMoneyCustomerScenario() {
		System.out.print("TEST ONE CUSTOMER WITH NO MONEY NON-NORMAL SCENARIO\n");
		
		//setUp() runs first before this test!
				
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.customers.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgCreateCheck is called. Instead, the Cashier's event log reads: "
                		+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgCreateCheck(waiter, customer, "Steak");//send the message from a waiter
		//cashier.pickAndExecuteAnAction();
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                		+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 customer in it. It doesn't.", cashier.customers.size(), 1);
		assertTrue("Cashier's scheduler should have returned true (action createCheck from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have one event log of asking check after the Cashier's scheduler is called. It doesn't",
                        waiter.log.size(), 1);
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                        + customer.log.toString(), 0, customer.log.size());
		
		//step 2 of the test
		cashier.msgHereIsMoney(customer, 0.0);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier should contain a customer's check with state == gotMoney. It doesn't.",
                 		cashier.customers.get(0).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain a customer with money == 0.0. It doesn't.",
             			cashier.customers.get(0).money == 0.0);
		
		//step 3
	    assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
	                	cashier.pickAndExecuteAnAction());
	    
	    //check postconditions for step 3 / preconditions for step 4
        assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $0.0\" but didn't. His log reads instead: " 
                		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $0.0"));
        assertTrue("Cashier should contain a customer's check of $15.99. It contains something else instead: $" 
        				+ cashier.customers.get(0).check.price, cashier.customers.get(0).check.price == 15.99);
        assertTrue("Cashier should contain a customer's debt == 15.99. It contains something else instead: $" 
                        + cashier.customers.get(0).debt, cashier.customers.get(0).debt == 15.99);
        assertTrue("Cashier should have logged \"MockCustomer doesn't have enough money, owe $15.99, pay next time\" with the correct change, but his last event logged reads instead: " 
                        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("MockCustomer doesn't have enough money, owe $15.99, pay next time"));
        assertTrue("Cashier should contain a customer's check with state == done. It doesn't.",
                        cashier.customers.get(0).check.state == checkState.done);
		
        //step 4
        assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                        cashier.pickAndExecuteAnAction());
	}
	
	public void testMultipleCustomersScenario() {
		System.out.print("TEST MULTIPLE CUSTOMERS SCENARIO\n");
		
		//setUp() runs first before this test!
		
		List<MockCustomer> customers = new ArrayList<MockCustomer>();
		List<String> customerOrders = new ArrayList<String>();
		int customerNumber = 3;
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.customers.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgCreateCheck is called. Instead, the Cashier's event log reads: "
                		+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		customerOrders.add("Steak");
		customerOrders.add("Chicken");
		customerOrders.add("Salad");
		customers.add(customer);
		customers.add(customer2);
		customers.add(customer3);
		for (int i = 0; i < customerNumber; i++) {
			cashier.msgCreateCheck(waiter, customers.get(i), customerOrders.get(i));//send the message from a waiter
		}
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
		                + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 3 customers in it. It doesn't.", cashier.customers.size(), 3);
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter for the first customer), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter for the second customer), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter for the third customer), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have 3 event logs of asking check after the Cashier's scheduler is called. It doesn't",
	                	waiter.log.size(), 3);
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customers.get(0).log.toString(), 0, customers.get(0).log.size());
		
		//step 2 of the test
		cashier.msgHereIsMoney(customers.get(0), 0.0);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier should contain the first customer's check with state == gotMoney. It doesn't.",
						cashier.customers.get(0).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain the first customer with money == 0.0. It doesn't.",
		             	cashier.customers.get(0).money == 0.0);
				
		//step 3
		assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
						cashier.pickAndExecuteAnAction());
			    
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $0.0\" but didn't. His log reads instead: " 
		                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $0.0"));
		assertTrue("Cashier should contain the first customer's check of $15.99. It contains something else instead: $" 
		        		+ cashier.customers.get(0).check.price, cashier.customers.get(0).check.price == 15.99);
		assertTrue("Cashier should contain the first customer's debt == 15.99. It contains something else instead: $" 
		                + cashier.customers.get(0).debt, cashier.customers.get(0).debt == 15.99);
		assertTrue("Cashier should have logged \"MockCustomer doesn't have enough money, owe $15.99, pay next time\" with the correct change, but his last event logged reads instead: " 
		                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("MockCustomer doesn't have enough money, owe $15.99, pay next time"));
		assertTrue("Cashier should contain a customer's check with state == done. It doesn't.",
		                cashier.customers.get(0).check.state == checkState.done);
		
		//step 4 of the test
		cashier.msgHereIsMoney(customers.get(1), 100.0);
				
		//check postconditions for step 4 / preconditions for step 5
		assertTrue("Cashier should contain the second customer's check with state == gotMoney. It doesn't.",
						cashier.customers.get(1).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain the second customer with money == 100.0. It doesn't.",
				        cashier.customers.get(1).money == 100.0);
						
		//step 5
		assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
						cashier.pickAndExecuteAnAction());
					    
		//check postconditions for step 5 / preconditions for step 6
		assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $100.0\" but didn't. His log reads instead: " 
				        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $100.0"));
		assertTrue("Cashier should contain the second customer's check of $10.99. It contains something else instead: $" 
				        + cashier.customers.get(1).check.price, cashier.customers.get(1).check.price == 10.99);
		assertTrue("Cashier should contain the second customer's debt == 0.0. It contains something else instead: $" 
                		+ cashier.customers.get(1).debt, cashier.customers.get(1).debt == 0);
		assertTrue("MockCustomer should have logged an event for receiving \"Getting change from cashier of $89.01\" with the correct change, but his last event logged reads instead: " 
                		+ customers.get(1).log.getLastLoggedEvent().toString(), customers.get(1).log.containsString("Getting change from cashier of $89.01"));
		assertTrue("Cashier should contain the second customer's check with state == done. It doesn't.",
                		cashier.customers.get(1).check.state == checkState.done);
		
		//step 6 of the test
		cashier.msgHereIsMoney(customers.get(2), 5.0);
				
		//check postconditions for step 6 / preconditions for step 7
		assertTrue("Cashier should contain the third customer's check with state == gotMoney. It doesn't.",
						cashier.customers.get(2).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain the third customer with money == 5.0. It doesn't.",
				        cashier.customers.get(2).money == 5.0);
						
		//step 7
		assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
						cashier.pickAndExecuteAnAction());
					    
		//check postconditions for step 7 / preconditions for step 8
		assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $5.0\" but didn't. His log reads instead: " 
				        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $5.0"));
		assertTrue("Cashier should contain the third customer's check of $5.99. It contains something else instead: $" 
				        + cashier.customers.get(2).check.price, cashier.customers.get(2).check.price == 5.99);
		assertTrue("Cashier should contain the third customer's debt == 0.99. It contains something else instead: $" 
				        + cashier.customers.get(2).debt, cashier.customers.get(2).debt == 0.99);
		assertTrue("Cashier should have logged \"MockCustomer doesn't have enough money, owe $0.99, pay next time\" with the correct change, but his last event logged reads instead: " 
				        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("MockCustomer doesn't have enough money, owe $0.99, pay next time"));
		assertTrue("Cashier should contain a customer's check with state == done. It doesn't.",
				        cashier.customers.get(2).check.state == checkState.done);
		
		//step 8
        assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                        cashier.pickAndExecuteAnAction());
	}
	
	/*public void testOneMarketAndMultipleCustomersScenario () {
		System.out.print("TEST BOTH PAY MARKET AND RECEIVE CUSTOMERS'S PAYMENT SCENARIO\n");
		
		//setUp() runs first before this test!
		
		List<MockCustomer> customers = new ArrayList<MockCustomer>();
		List<String> customerOrders = new ArrayList<String>();
		int customerNumber = 2;
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.customers.size(), 0);
		assertEquals("Cashier should have 0 markets in it. It doesn't.", cashier.markets.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's messages are called. Instead, the Cashier's event log reads: "
                		+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		customerOrders.add("Steak");
		customerOrders.add("Chicken");
		customers.add(customer);
		customers.add(customer2);
		for (int i = 0; i < customerNumber; i++) {
			cashier.msgCreateCheck(waiter, customers.get(i), customerOrders.get(i));//send the message from a waiter
		}
		//******cashier.msgGetBillFromMarket(market, 9.9, null);//send the message from a market
		
		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                		+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 2 customers in it. It doesn't.", cashier.customers.size(), 2);
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter for the first customer), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's scheduler should have returned true (react to action createCheck from a waiter for the second customer), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have 2 event logs of asking check after the Cashier's scheduler is called. It doesn't",
            			waiter.log.size(), 2);
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customers.get(0).log.toString(), 0, customers.get(0).log.size());
		
		assertEquals("Cashier should have 1 market in it. It doesn't.", cashier.markets.size(), 1);
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
        				+ market.log.toString(), 0, market.log.size());
		assertTrue("Cashier should contain a market's check with state == ready. It doesn't.",
        				cashier.markets.get(0).state == marketCheckState.ready);
		assertTrue("Cashier should contain a market with check price == 9.9. It doesn't.",
						cashier.markets.get(0).money == 9.9);
		
		//step 2 of the test
		cashier.msgHereIsMoney(customers.get(0), 100.0);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier should contain the first customer's check with state == gotMoney. It doesn't.",
						cashier.customers.get(0).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain the first customer with money == 100.0. It doesn't.",
		             	cashier.customers.get(0).money == 100.0);
				
		//step 3
		assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
						cashier.pickAndExecuteAnAction());
		   
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $100.0\" but didn't. His log reads instead: " 
		                + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $100.0"));
		assertTrue("Cashier should contain the first customer's check of $15.99. It contains something else instead: $" 
		        		+ cashier.customers.get(0).check.price, cashier.customers.get(0).check.price == 15.99);
		assertTrue("Cashier should contain the first customer's debt == 0.0. It contains something else instead: $" 
		                + cashier.customers.get(0).debt, cashier.customers.get(0).debt == 0);
		assertTrue("MockCustomer should have logged an event for receiving \"Getting change from cashier of $84.01\" with the correct change, but his last event logged reads instead: " 
                		+ customers.get(0).log.getLastLoggedEvent().toString(), customers.get(0).log.containsString("Getting change from cashier of $84.01"));
		assertTrue("Cashier should contain the second customer's check with state == done. It doesn't.",
                		cashier.customers.get(0).check.state == checkState.done);
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (react to action payToMarket from a market), but didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4 / preconditions for step 5
        assertTrue("Cashier should have logged \"Paying $9.90 to MockMarket\" but didn't. His log reads instead: " 
                		+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Paying $9.90 to MockMarket"));
        assertTrue("MockMarket should have logged an event for receiving \"Receiving $9.9 from cashier\" with the correct money, but his last event logged reads instead: " 
                        + market.log.getLastLoggedEvent().toString(), market.log.containsString("Receiving $9.9 from cashier"));
        assertTrue("Cashier should contain a market's check with state == done. It doesn't.",
                        cashier.markets.get(0).state == marketCheckState.done);
		
		//step 5 of the test
		cashier.msgHereIsMoney(customers.get(1), 5.0);
				
		//check postconditions for step 5 / preconditions for step 6
		assertTrue("Cashier should contain the second customer's check with state == gotMoney. It doesn't.",
						cashier.customers.get(1).check.state == checkState.gotMoney);
		assertTrue("Cashier should contain the second customer with money == 5.0. It doesn't.",
				        cashier.customers.get(1).money == 5.0);
						
		//step 6
		assertTrue("Cashier's scheduler should have returned true (react to GiveChangeToCustomer), but didn't.", 
						cashier.pickAndExecuteAnAction());
					    
		//check postconditions for step 6 / preconditions for step 7
		assertTrue("Cashier should have logged \"Receiving payment from MockCustomer of $5.0\" but didn't. His log reads instead: " 
				        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Receiving payment from MockCustomer of $5.0"));
		assertTrue("Cashier should contain the second customer's check of $10.99. It contains something else instead: $" 
				        + cashier.customers.get(1).check.price, cashier.customers.get(1).check.price == 10.99);
		assertTrue("Cashier should contain the second customer's debt == 5.99. It contains something else instead: $" 
				        + cashier.customers.get(1).debt, cashier.customers.get(1).debt == 5.99);
		assertTrue("Cashier should have logged \"MockCustomer doesn't have enough money, owe $5.99, pay next time\" with the correct change, but his last event logged reads instead: " 
				        + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("MockCustomer doesn't have enough money, owe $5.99, pay next time"));
		assertTrue("Cashier should contain a customer's check with state == done. It doesn't.",
				        cashier.customers.get(1).check.state == checkState.done);
		
		//step 8
        assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                        cashier.pickAndExecuteAnAction());
	}*/
}
