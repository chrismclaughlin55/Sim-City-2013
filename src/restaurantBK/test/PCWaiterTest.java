package restaurantBK.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.PersonAgent;
import restaurantBK.BKCashierRole;
import restaurantBK.BKCashierRole.CheckState;
import restaurantBK.BKCashierRole.Check;
import restaurantBK.BKCashierRole.ShipmentState;
import restaurantBK.BKCookRole;
import restaurantBK.BKPCWaiterRole;
import restaurantBK.BKWaiterRole;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Waiter;
import restaurantBK.test.mock.MockCustomer;
import restaurantBK.test.mock.MockMarket;
import restaurantBK.test.mock.MockWaiter;
import restaurantBK.Order;
import junit.framework.*;

public class PCWaiterTest extends TestCase {
		//these are instantiated for each test separately via the setUp() method.
		BKPCWaiterRole waiter0;
		BKPCWaiterRole waiter1;
		BKWaiterRole waiter2;
		BKCookRole cook;
		MockCustomer customer;
		MockMarket market1;
		MockMarket market2;
		List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
		
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		public void setUp() throws Exception{
			super.setUp();		
			PersonAgent p = new PersonAgent("cashier");
			waiter0 = new BKPCWaiterRole(p,orders,"pcwaiter0",null);
			waiter1 = new BKPCWaiterRole(p,orders,"pcwaiter",null);	
			waiter2 = new BKWaiterRole(p,"waiter",null);	
			cook = new BKCookRole(p,orders,"cook",null);		
			waiter2.setCook(cook);
			waiter1.setCook(cook);
			waiter0.setCook(cook);
			
		}	
		/**
		 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
		 */
		public void testPlacingOrderByBothRegularAndProducerConsumerWaiter() { 
			
			//ADD Order to cook's list via method used in producer consumer list.
			Order pc = new Order("Steak",waiter1,1);
			waiter1.orders.add(pc);
			//CLEARLY ADDED ORDER TO WAITER'S LIST OF ORDERS
			cook.msgOrderIsUpdated();
			//FOUND TRUE THAT ORDER WAS ADDED TO COOK'S LIST OF ORDERS
			assertTrue(cook.orders.contains(pc));
			
			
			//Customer c;
			waiter2.cook.msgHereIsAnOrder(waiter2, "Chicken",1);
			cook.stateChanged();
			Order o = new Order("Chicken",waiter2,1);
			orders.add(o);
			cook.msgOrderIsUpdated();
			assertTrue(cook.orders.contains(o));
			//3 ORDERS WERE ADDED, 1 FROM METHOD USED WITHIN PRODUCER CONSUMER METHOD, 2 BY WAITER2 VIA MESSAGES
			assertEquals(cook.orders.size(),3);
		}
		
		public void testPlacing2OrdersFrom2PCWaiters() {
			//TWO PRODUCER CONSUMER WAITERS JUST ADD TO THEIR OWN LISTS, AND THEY ARE FOUND IN COOK ORDERS
			Order pc = new Order("blah",waiter0,1);
			Order pc1 = new Order("meh",waiter1,2);
			waiter1.orders.add(pc);
			waiter0.orders.add(pc1);
			assertEquals(cook.orders.size(),2);
		}
		
		
}
