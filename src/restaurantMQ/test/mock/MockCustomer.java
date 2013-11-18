package restaurantMQ.test.mock;


import restaurantMQ.Menu;
import restaurantMQ.gui.CustomerGui;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log;

	public MockCustomer(String name) {
		super(name);
		log = new EventLog();
	}

	/*
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
*/
	@Override
	public void msgTablesFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(Waiter waiter, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(Waiter waiter, int table, Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseChooseSomethingElse(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoodToGo()
	{
		log.add(new LoggedEvent("Good to go."));
	}

	@Override
	public void msgNotEnough() {
		log.add(new LoggedEvent("Not enough."));
		
	}

	@Override
	public void msgGoToSpot(int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

}
