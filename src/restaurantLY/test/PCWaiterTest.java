package restaurantLY.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurantLY.LYPCWaiterRole;
import restaurantLY.LYPCWaiterRole.myCustomer;
import restaurantLY.LYWaiterRole;
import restaurantLY.PCOrder;
import restaurantLY.test.mock.MockCook;
import restaurantLY.test.mock.MockCustomer;
import city.PersonAgent;
import junit.framework.*;

public class PCWaiterTest extends TestCase {
		//these are instantiated for each test separately via the setUp() method.
		LYPCWaiterRole pcWaiter;
		LYPCWaiterRole pcWaiter2;
		LYWaiterRole waiter;
		MockCustomer customer1;
		MockCustomer customer2;
		MockCook cook;
		List<PCOrder> orders = Collections.synchronizedList(new ArrayList<PCOrder>());
		
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		public void setUp() throws Exception{
			super.setUp();		
			PersonAgent p = new PersonAgent("waiter");
			pcWaiter = new LYPCWaiterRole(p, null, orders);
			pcWaiter2 = new LYPCWaiterRole(p, null, orders);
			waiter = new LYWaiterRole(p, null);	
			customer1 = new MockCustomer("MockCustomer");
			customer2 = new MockCustomer("MockCustomer");
			cook = new MockCook("MockCook");
			pcWaiter.setCook(cook);
			pcWaiter2.setCook(cook);
			waiter.setCook(cook);
		}	
		
		/**
		 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
		 */
		public void testTwoPCWaitersScenario() { 
			System.out.print("TEST TWO PCWAITERS SCENARIO\n");
			
			//setUp() runs first before this test!
			
			//check preconditions
			assertEquals("PCWaiter1 should have 0 customer's order in it. It doesn't.", pcWaiter.cookOrders.size(), 0);
			assertEquals("PCWaiter1 should have an empty event log before the PCWaiter1's giveOrderToCook is called. Instead, the PCWaiter1 event log reads: "
            		+ pcWaiter.log.toString(), 0, pcWaiter.log.size());
			assertEquals("PCWaiter2 should have 0 customer's order in it. It doesn't.", pcWaiter2.cookOrders.size(), 0);
			assertEquals("PCWaiter2 should have an empty event log before the PCWaiter2's giveOrderToCook is called. Instead, the PCWaiter2 event log reads: "
            		+ pcWaiter2.log.toString(), 0, pcWaiter2.log.size());
			
			PCOrder pcOrder = new PCOrder(pcWaiter, 1, "Steak");
			PCOrder pcOrder2 = new PCOrder(pcWaiter2, 2, "Pizza");
			
			//step 1 of the test
			pcWaiter.msgHereIsMyChoice(customer1, "Steak");
			pcWaiter2.msgHereIsMyChoice(customer2, "Pizza");
			pcWaiter.cookOrders.add(pcOrder);
			pcWaiter2.cookOrders.add(pcOrder2);
			pcWaiter.pickAndExecuteAnAction();
			pcWaiter2.pickAndExecuteAnAction();
			
			//check postconditions for step 1
			//assertEquals("PCWaiter should have 1 customer with 1 order in cookOrders in it. It doesn't.", pcWaiter.cookOrders.size(), 1);
			//assertTrue("PCWaiter should have logged \"Giving order of Steak to cook\" but didn't. His log reads instead: " 
            ///		+ pcWaiter.log.getLastLoggedEvent().toString(), pcWaiter.log.containsString("Giving order of Steak to cook"));
			//assertEquals("PCWaiter2 should have 1 customer with 1 order in cookOrders in it. It doesn't.", pcWaiter2.cookOrders.size(), 1);
			//assertTrue("PCWaiter2 should have logged \"Giving order of Pizza to cook\" but didn't. His log reads instead: " 
            //		+ pcWaiter2.log.getLastLoggedEvent().toString(), pcWaiter2.log.containsString("Giving order of Pizza to cook"));
			assertEquals("Shared orders should have 2 orders in it. It doesn't.", orders.size(), 2);
		}
		
		public void testOneNormalAndOnePCWaiterScenario() {
			System.out.print("TEST ONE NORMAL WAITER AND ONE PCWAITER SCENARIO\n");
			
			//check preconditions
			assertEquals("PCWaiter1 should have 0 customer's order in it. It doesn't.", pcWaiter.cookOrders.size(), 0);
			assertEquals("PCWaiter1 should have an empty event log before the PCWaiter1's giveOrderToCook is called. Instead, the PCWaiter1 event log reads: "
            		+ pcWaiter.log.toString(), 0, pcWaiter.log.size());
			assertEquals("Waiter should have an empty event log before the waiter's giveOrderToCook is called. Instead, the waiter event log reads: "
            		+ waiter.log.toString(), 0, waiter.log.size());
			
			PCOrder pcOrder = new PCOrder(pcWaiter, 1, "Steak");
			
			//step 1 of the test
			pcWaiter.msgHereIsMyChoice(customer1, "Steak");
			pcWaiter.cookOrders.add(pcOrder);
			cook.msgHereIsAnOrder(pcWaiter, 1, "Steak");
			pcWaiter.pickAndExecuteAnAction();
			//waiter.giveOrderToCook(new myCustomer(customer2, 2, 2, "Pizza"));
			waiter.msgHereIsMyChoice(customer2, "Pizza");
			cook.msgHereIsAnOrder(waiter, 2, "Pizza");
			waiter.pickAndExecuteAnAction();
			
			//check postconditions for step 1
			assertEquals("PCWaiter should have 1 customer with 1 order in cookOrders in it. It doesn't.", pcWaiter.cookOrders.size(), 1);
			//assertTrue("MockCook should have 1 order of Steak but didn't. His contains instead: " 
            //		+ cook.orders, cook.orders.contains(pcOrder));
			assertTrue("MockCook should contain order of Steak but didn't.", orders.contains(pcOrder));
			
			//assertTrue("PCWaiter should have logged \"Giving order of Steak to cook\" but didn't. His log reads instead: " 
            //		+ pcWaiter.log.getLastLoggedEvent().toString(), pcWaiter.log.containsString("Giving order of Steak to cook"));
			//assertTrue("Waiter should have logged \"Giving order of Pizza to cook\" but didn't. His log reads instead: " 
            //		+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Giving order of Pizza to cook"));
			assertEquals("Shared orders should have 1 orders in it. It doesn't.", orders.size(), 1);
			//assertEquals("MockCook should have order of Pizza in it but doesn't. His contains instead: "+ orders, orders.contains(pcOrder));
		}
}
