package market.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import mainGUI.MainGui;
import market.Inventory;
import market.Market;
import market.MarketData;
import market.MarketEmployeeRole;
import market.MarketEmployeeRole.EmployeeState;
import market.MyOrder;
import market.gui.MarketGui;
import market.test.mock.MockMarketCustomer;
import market.test.mock.MockMarketManager;
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
	Market market;
	PersonAgent person = new PersonAgent("person");
	List<MyOrder> thingsToOrder1;
	Inventory inventory;
	MarketGui marketGui;
	MainGui mainGui;
	MarketData chickenData, saladData, steakData, pizzaData;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		
		market = new Market(0, 0, 0, 0, mainGui);
		
		chickenData = new MarketData("Chicken", 10, 5.99);
		saladData = new MarketData("Salad", 10, 3.99);
		steakData = new MarketData("Steak", 10, 11.99);
		pizzaData = new MarketData("Pizza", 10, 7.99);
	
		marketGui = new MarketGui(market);
		inventory = new Inventory(chickenData, saladData, steakData, pizzaData, marketGui);
		
		PersonAgent person = new PersonAgent("Employee");
		employee = new MarketEmployeeRole(person, manager, inventory);
		customer = new MockMarketCustomer("mockcustomer");
		manager = new MockMarketManager("mockmanager");
		
		thingsToOrder1 = Collections.synchronizedList(new ArrayList<MyOrder>());
		MyOrder o1 = new MyOrder("Chicken", 2);
		MyOrder o2 = new MyOrder("Steak", 3);
		thingsToOrder1.add(o1);
		thingsToOrder1.add(o2);
		
		employee.state = EmployeeState.working;
		market.inventory = inventory;
		
	}	
	
	public void testOneOrderOneMarket()
	{	
		//Check preconditions
		assertEquals("Employee should have 0 currenet orders. It doesn't", employee.currentMarketOrders.size(), 0);
		assertEquals("Employee should have 0 payments. It doesn't", employee.payments.size(), 0);
		assertEquals("Employee should have an empty event log. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 0, employee.log.size());
		assertEquals("Employee should have 0 payments. It doesn't", employee.inventory, this.inventory);
				
		
		//Step 1, the manager sends a customer to employee
		employee.msgServiceCustomer(customer);
		assertEquals("Current customer should be set to customer. It isn't", customer, customer );
		assertEquals("waitingCustomers should have 1 customer. It doesn't", employee.waitingCustomers.size(), 1);
		
		//Step2, the employee calls the customer
		employee.pickAndExecuteAnAction();
		assertEquals("waitingCustomers should have 0 customers. It doesn't", employee.waitingCustomers.size(), 0);
		assertTrue("Customer should have logged that it got called. Instead, the log reads " +
				customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Called by employee"));
		
		//Step 2, the customer gives his orders to employee
		employee.msgHereAreMyOrders(thingsToOrder1, customer);
		assertEquals("Employee should have 2 current orders. It doesn't", employee.currentMarketOrders.size(), 2);
		
		employee.pickAndExecuteAnAction();
		assertEquals("Employee should have an event on it's log. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 1, employee.log.size());
		
		/*// Step 1, an order comes in from a customer
		employee.msgHereAreMyOrders(thingsToOrder1, customer);
		assertEquals("Employee should have 1 current order. It doesn't", employee.currentMarketOrders.size(), 0);
		assertEquals("Employee should have 0 payments. It doesn't", employee.payments.size(), 0);*/
		
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
