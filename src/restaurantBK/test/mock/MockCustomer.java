package restaurantBK.test.mock;


import restaurantBK.Menu;
import restaurantBK.gui.CustomerGui;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Host;
import restaurantBK.interfaces.Waiter;

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

	public MockCustomer(String name) {
		super(name);

	}

	/*
	 * @Override
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
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cash) {
		// TODO Auto-generated method stub
		cashier=cash;
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWouldYouLikeToWait(int numberahead) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(Waiter w, int tableNumber, Menu m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderSomethingElse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheCheck(double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAllPaidPlusChange(double change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgWaitHerePlease(int x) {
		// TODO Auto-generated method stub
		
	}

}
