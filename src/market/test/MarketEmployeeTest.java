package market.test;

import junit.framework.TestCase;
import market.Inventory;
import market.MarketData;
import market.MarketEmployeeRole;
import market.test.mock.MockMarketCustomer;
import market.test.mock.MockMarketManager;
import restaurantMQ.test.mock.MockMarket;
import city.PersonAgent;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class MarketEmployeeTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	MarketEmployeeRole employee;
	MockMarketManager manager;
	MockMarketCustomer customer;
	MockMarket market1;
	MockMarket market2;
	PersonAgent person = new PersonAgent("person");
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		
		MarketData chickenData = new MarketData("chicken", 10, 5.99);
		MarketData saladData = new MarketData("salad", 10, 3.99);
		MarketData steakData = new MarketData("steak", 10, 11.99);
		MarketData pizzaData = new MarketData("pizza", 10, 7.99);
		
		Inventory inventory = new Inventory(chickenData, saladData, steakData, pizzaData);
		customer = new MockMarketCustomer("mockcustomer");
		manager = new MockMarketManager("mockmanager");
		employee = new MarketEmployeeRole(person, manager, inventory);
		
		
		
	}	
	
	//Make sure the cashier properly pays a market when the order is delivered
	/*public void testOneOrderOneMarket()
	{	
		//Check preconditions
		assertEquals("Employee should have 0 orders. It doesn't", employee.orders.size(), 0);
		assertEquals("Employee should have 0 payments. It doesn't", employee.payments.size(), 0);
		assertEquals("Employee should have an empty event log. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 0, employee.log.size());
		
		/// Step 1, an order comes in from a customer
		employee.msgHereIsAnOrder("pizza", 5, customer);
		assertEquals("Employee should have 1 orders. It doesn't", employee.orders.size(), 1);
		assertTrue("Employee should have logged that it received the order. Instead, the log reads " +
				employee.log.getLastLoggedEvent().toString(), employee.log.containsString("Received order from customer"));
		
		//Step 2, allow the employee's scheduler to respond to the input
		assertTrue("Employee's scheduler should return true, but didn't.", employee.pickAndExecuteAnAction());
		
		//Step 3, test to see if the employee fulfills the order
		assertEquals("Quantity of pizza should equal 5. It doesn't", employee.inventory.inventory.get("pizza").amount, 5);
		

		//Step 4, test to see if the employee receives the payment
		employee.msgHereIsPayment(7.99);
		assertEquals("Employee should have 1 payment. It doesn't", employee.payments.size(), 1);
		
		//Step 5, allow the employee's scheduler to respond to the input
		assertTrue("Employee's scheduler should return true, but didn't.", employee.pickAndExecuteAnAction());
		assertEquals("Payments should have 0 payments. It doesn't", employee.payments.size(), 0);
		assertTrue("Manager should have logged that money was received. Instead, the log reads: " +
				manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Money received"));
	}*/
}
