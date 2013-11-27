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
import market.MarketManagerRole;
import market.MarketManagerRole.ManagerState;
import market.MyOrder;
import market.gui.MarketGui;
import market.test.mock.MockMarketCustomer;
import market.test.mock.MockMarketEmployee;
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
public class MarketManagerTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	MockMarketEmployee employee;
	MockMarketEmployee employee2;
	MockMarketCustomer customer;
	MockMarketCustomer customer2;
	MarketManagerRole manager;
	
	Market market;
	PersonAgent person = new PersonAgent("person");
	List<MyOrder> thingsToOrder1;
	List<MyOrder> thingsToOrder2;

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
		manager = new MarketManagerRole(person, inventory, market);
		
		customer = new MockMarketCustomer("mockcustomer");
		customer2 = new MockMarketCustomer("mockcustomer2");
		
		employee = new MockMarketEmployee("mockemployee");
		employee2 = new MockMarketEmployee("mockemployee2");

		
		manager.state = ManagerState.managing;
		market.inventory = inventory;
		
	}	
	
	public void testOneCustomerOneEmployee()
	{	
		//Check preconditions
		assertEquals("Manager should have 0 waiting customers. It doesn't", manager.waitingCustomers.size(), 0);
		assertEquals("Manager should have 0 waiting employees. It doesn't", manager.waitingEmployees.size(), 0);
		assertEquals("Manager should have 0 working employees. It doesn't", manager.workingEmployees.size(), 0);
		assertEquals("Manager should have an empty event log. Instead, the Manager's event log reads: "
				+ manager.log.toString(), 0, manager.log.size());
		
		//Step 1 the building is open for employees
		manager.pickAndExecuteAnAction();
		assertEquals("Manager should have 1 event logged. Instead, the Manager's event log reads: "
				+ manager.log.toString(), 1, manager.log.size());

		

		//Step 2, the employee enters the building
		manager.msgReportingForWork(employee);
		assertEquals("waitingEmployees should have 1 employee. It doesn't", manager.waitingEmployees.size(), 1);
		
		//Step 3, the employee is added to working Employees
		manager.pickAndExecuteAnAction();
		assertEquals("waitingEmployees should have 0 employee. It doesn't", manager.waitingEmployees.size(), 0);
		assertEquals("workingEmployees should have 1 employee. It doesn't", manager.workingEmployees.size(), 1);
		
		//Step 4, the building is now open
		manager.pickAndExecuteAnAction();
		assertEquals("Manager should have 2 events logged. Instead, the Manager's event log reads: "
				+ manager.log.toString(), 2, manager.log.size());
		
		//Step 5, a customer enters
		manager.msgNeedToOrder(customer);
		assertEquals("Manager should have 1 waiting customer. It doesn't", manager.waitingCustomers.size(), 1);
		
		//Step 6, the customer is assigned to an employee
		manager.pickAndExecuteAnAction();
		assertEquals("Employee should have 1 events logged. Instead, the Employees event log reads: "
				+ employee.log.toString(), 1, employee.log.size());
		assertEquals("Manager should have 0 waiting customers. It doesn't", manager.waitingCustomers.size(), 0);
		
		
		



		
	}
	
	/*public void testTwoCustomers()
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
		
		//Step 3, the customer gives his orders to employee
		employee.msgHereAreMyOrders(thingsToOrder1, customer);
		assertEquals("Employee should have 2 current orders. It doesn't", employee.currentMarketOrders.size(), 2);
		
		//Step 4, the employee Fulfills the order
		employee.pickAndExecuteAnAction();
		assertEquals("Employee should have an event on it's log. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 1, employee.log.size());
		
		//Step 4, the employee sends invoice to customer 
		employee.msgDoneProcessing();
		employee.pickAndExecuteAnAction();
		assertTrue("Customer should have logged that order was fulfilled. Instead, the log reads " +
				customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Order fulfilled"));
		
		//Step 5, the customer pays the bill
		employee.msgHereIsPayment(customer.payment);
		assertEquals("Employee should have 1 payment. It doesn't", employee.payments.size(), 1);
		
		//Step 5, the employee processes the payment
		employee.pickAndExecuteAnAction();
		assertTrue("Manager should have logged that money was received. Instead, the log reads " +
				manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Money received"));
		assertEquals("Employee should have 0 current orders. It doesn't", employee.currentMarketOrders.size(), 0);
		
		
		//SECOND CUSTOMER
		
		employee.log.clear();
		
		//Check preconditions
				assertEquals("Employee should have 0 currenet orders. It doesn't", employee.currentMarketOrders.size(), 0);
				assertEquals("Employee should have 0 payments. It doesn't", employee.payments.size(), 0);
				assertEquals("Employee should have an empty event log. Instead, the Employee's event log reads: "
						+ employee.log.toString(), 0, employee.log.size());
				assertEquals("Employee should have 0 payments. It doesn't", employee.inventory, this.inventory);
						
				
				//Step 1, the manager sends a customer to employee
				employee.msgServiceCustomer(customer2);
				assertEquals("Current customer should be set to customer2. It isn't", customer2, customer2 );
				assertEquals("waitingCustomers should have 1 customer. It doesn't", employee.waitingCustomers.size(), 1);
				
				//Step2, the employee calls the customer
				employee.pickAndExecuteAnAction();
				assertEquals("waitingCustomers should have 0 customers. It doesn't", employee.waitingCustomers.size(), 0);
				assertTrue("Customer should have logged that it got called. Instead, the log reads " +
						customer.log.getLastLoggedEvent().toString(), customer2.log.containsString("Called by employee"));
				
				//Step 3, the customer gives his orders to employee
				employee.msgHereAreMyOrders(thingsToOrder2, customer2);
				assertEquals("Employee should have 3 current orders. It doesn't", employee.currentMarketOrders.size(), 3);
				
				//Step 4, the employee Fulfills the order
				employee.pickAndExecuteAnAction();
				assertEquals("Employee should have an event on it's log. Instead, the Employee's event log reads: "
						+ employee.log.toString(), 1, employee.log.size());
				
				//Step 4, the employee sends invoice to customer 
				employee.msgDoneProcessing();
				employee.pickAndExecuteAnAction();
				assertTrue("Customer2 should have logged that order was fulfilled. Instead, the log reads " +
						customer2.log.getLastLoggedEvent().toString(), customer2.log.containsString("Order fulfilled"));
				
				//Step 5, the customer pays the bill
				employee.msgHereIsPayment(customer2.payment);
				assertEquals("Employee should have 1 payment. It doesn't", employee.payments.size(), 1);
				
				//Step 5, the employee processes the payment
				employee.pickAndExecuteAnAction();
				assertTrue("Manager should have logged that money was received. Instead, the log reads " +
						manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Money received"));
				assertEquals("Employee should have 0 current orders. It doesn't", employee.currentMarketOrders.size(), 0);

	}*/
	
	
	
}

