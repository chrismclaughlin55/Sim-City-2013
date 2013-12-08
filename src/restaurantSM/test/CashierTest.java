package restaurantSM.test;

import restaurantSM.CashierAgent;
import restaurantSM.interfaces.Market;
import restaurantSM.test.mock.MockCustomer;
import restaurantSM.test.mock.MockMarket;
import restaurantSM.test.mock.MockWaiter;
import restaurantSM.utils.Bill;
import restaurantSM.utils.MyCustomer;
import restaurantSM.utils.Request;
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
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market1;
	MockMarket market2;
	Request request;
	Bill bill1;
	Bill bill2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");
		customer1 = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1", cashier);
		market2 = new MockMarket("mockmarket2", cashier);
		customer2 = new MockCustomer("mockcustomer");
		request = new Request();
		bill1 = new Bill(waiter, new MyCustomer(customer1, null));
		bill1.total = 15.00;
		bill2 = new Bill(waiter, new MyCustomer(customer2, null));
		bill2.total = 10.00;
	}
	
	/**
	 * This tests the cashier under the first scenario: one market, bill paid in full.
	 */
	public void testOneMarket() {
		double oldTotal = cashier.total;
		assertTrue("Cashier stock bill list should be empty", cashier.marketBills.isEmpty());
		assertEquals("Market shouldn't have any income yet", 0.00, market1.total);
		for (String item : market1.s.stock.keySet()) {
			request.order.put(item, 1);
		}
		market1.msgRequestStock(request);
		assertEquals("Cashier stock bill list should have 1 bill", 1, cashier.marketBills.size());
		double billAmt = cashier.marketBills.get(0).total;
		assertEquals("The total of the bill should be correct", billAmt , market1.billPrice);
		market1.msgReceivePayment(billAmt, 0);
		assertEquals("Market income should have gone up by bill amount", billAmt, market1.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier's total should have gone down by bill amount", cashier.total, oldTotal - billAmt);
	}
	
	/**
	 * This tests the cashier under the second scenario: first market cannot fulfill the entire request, both bills paid in full
	 */
	public void testTwoMarkets() {
		double oldTotal = cashier.total;
		assertTrue("Cashier stock bill list should be empty", cashier.marketBills.isEmpty());
		assertEquals("Market1 shouldn't have any income yet", 0.00, market1.total);
		assertEquals("Market1 shouldn't have any income yet", 0.00, market2.total);
		for (String item : market1.s.stock.keySet()) {
			request.order.put(item, 2);
		}
		market1.msgRequestStock(request);
		assertEquals("Cashier stock bill list should have 1 bill", 1, cashier.marketBills.size());
		double billAmt = cashier.marketBills.get(0).total;
		assertEquals("The total of the bill should be correct", billAmt, market1.billPrice);
		market1.msgReceivePayment(billAmt, 0);
		assertEquals("Market1 income should have gone up by bill amount", billAmt, market1.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier's total should have gone down by bill amount", cashier.total, oldTotal - billAmt);
		oldTotal = cashier.total;
		for (String item : market1.s.stock.keySet()){
			int number = request.order.get(item);
			assertEquals("Request should still be requesting items", 1, number);
		}
		market2.msgRequestStock(request);
		assertEquals("Cashier stock bill list should have 1 bill", 1, cashier.marketBills.size());
		billAmt = cashier.marketBills.get(0).total;
		assertEquals("The total of the bill should be correct", billAmt, market2.billPrice);
		market2.msgReceivePayment(billAmt, 0);
		assertEquals("Market1 income should have gone up by bill amount", billAmt, market2.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier's total should have gone down by bill amount", cashier.total, oldTotal - billAmt);
	}
	
	/**
	 * This tests one customer paying for his meal.
	 */
	public void testOneCustomer() {
		double originalTotal = cashier.total;
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
		assertEquals("Cashier should have correct starting money", originalTotal, cashier.total);
		cashier.msgHeresMyBill(customer1.bankRoll, bill1);
		assertEquals("Cashier should have 1 bill", 1, cashier.paidBills.size());
		assertEquals("Customer should have paid correct amount", customer1.bankRoll, cashier.paidBills.get(0).tender);
		assertEquals("The total of the bill should be correct", cashier.paidBills.get(0).total, bill1.total);
		customer1.payForFood(bill1);
		cashier.pickAndExecuteAnAction();
		assertEquals("The customer's money should go up by the correct change", bill1.change, customer1.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill1.total, cashier.total);
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
	}
	
	/**
	 * This tests two customers paying for their meals.
	 */
	public void testTwoCustomers() {
		double originalTotal = cashier.total;
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
		assertEquals("Cashier should have correct starting money", originalTotal, cashier.total);
		cashier.msgHeresMyBill(customer1.bankRoll, bill1);
		assertEquals("Cashier should have 1 bill", 1, cashier.paidBills.size());
		cashier.msgHeresMyBill(customer2.bankRoll, bill2);
		assertEquals("Cashier should have 2 bills", 2, cashier.paidBills.size());
		assertEquals("First customer should have paid correct amount", customer1.bankRoll, cashier.paidBills.get(0).tender);
		assertEquals("Second customer should have paid correct amount", customer2.bankRoll, cashier.paidBills.get(1).tender);
		assertEquals("The total of the first bill should be correct", cashier.paidBills.get(0).total, bill1.total);
		assertEquals("The total of the second bill should be correct", cashier.paidBills.get(1).total, bill2.total);
		customer1.payForFood(bill1);
		cashier.pickAndExecuteAnAction();
		assertEquals("The first customer's money should go up by the correct change", bill1.change, customer1.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill1.total, cashier.total);
		originalTotal = cashier.total;
		assertEquals("There should be one bill remaining on cashier's bill list", 1, cashier.paidBills.size());
		customer2.payForFood(bill2);
		cashier.pickAndExecuteAnAction();
		assertEquals("The second customer's money should go up by the correct change", bill2.change, customer2.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill2.total, cashier.total);
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
	}
	
	/**
	 * This tests a repeat customer.
	 */
	public void repeatCustomer() {
		double originalTotal = cashier.total;
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
		assertEquals("Cashier should have correct starting money", originalTotal, cashier.total);
		cashier.msgHeresMyBill(customer1.bankRoll, bill1);
		assertEquals("Cashier should have 1 bill", 1, cashier.paidBills.size());
		assertEquals("Customer should have paid correct amount", customer1.bankRoll, cashier.paidBills.get(0).tender);
		assertEquals("The total of the bill should be correct", cashier.paidBills.get(0).total, bill1.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("The customer's money should go up by the correct change", bill1.change, customer1.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill1.total, cashier.total);
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
		cashier.msgHeresMyBill(customer1.bankRoll, bill1);
		assertEquals("Cashier should have 1 bill", 1, cashier.paidBills.size());
		assertEquals("Customer should have paid correct amount", customer1.bankRoll, cashier.paidBills.get(0).tender);
		assertEquals("The total of the bill should be correct", cashier.paidBills.get(0).total, bill1.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("The customer's money should go up by the correct change", bill1.change, customer1.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill1.total, cashier.total);
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
	}
	
	/**
	 * This tests what happens when the cashier has a tab at the market and the cashier is paid by a customer.
	 */
	public void notPaidInFull() {
		market1.tab = 335.00;
		double originalTotal = cashier.total;
		
		assertTrue("Cashier stock bill list should be empty", cashier.marketBills.isEmpty());
		assertEquals("Market shouldn't have any income yet", 0.00, market1.total);
		assertEquals("Cashier should have a tab at the market", 35.00, market1.tab);
		
		cashier.msgHeresMyBill(customer1.bankRoll, bill1);
		assertEquals("Cashier should have 1 bill", 1, cashier.paidBills.size());
		assertEquals("Customer should have paid correct amount", customer1.bankRoll, cashier.paidBills.get(0).tender);
		assertEquals("The total of the bill should be correct", cashier.paidBills.get(0).total, bill1.total);
		cashier.pickAndExecuteAnAction();
		assertEquals("The customer's money should go up by the correct change", bill1.change, customer1.bankRoll);
		assertEquals("The cashier's money should go up by the amount of the bill", originalTotal + bill1.total, cashier.total);
		assertTrue("Cashier bill list should be empty", cashier.paidBills.isEmpty());
		assertEquals("Cashier should pay down tab at the market", 335 - cashier.total, market1.tab);
		assertEquals("Cashier should now be broke", 0, cashier.total);
	}
	
}
