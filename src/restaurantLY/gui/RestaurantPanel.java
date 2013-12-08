package restaurantLY.gui;

import restaurantLY.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.concurrent.*; 

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	AnimationPanel animationPanel =  new AnimationPanel();
	
	//Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Sarah");
	private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
	private CookAgent cook = new CookAgent("Cook");
	private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
	//************************************************************
	// change the second parameter of CashierAgent to be 0 to test "cashier short of money to pay market" scenario
	private CashierAgent cashier = new CashierAgent("Cashier", 0);
	//************************************************************
	private MarketAgent market1 = new MarketAgent("Market1");
	private MarketAgent market2 = new MarketAgent("Market2");
	private MarketAgent market3 = new MarketAgent("Market3");
	
	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private ListPanel waiterPanel = new ListPanel(this, "Waiters");
	private JPanel group = new JPanel();
	
	private RestaurantGui gui;
	
	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;

		market1.addInventory("Steak", 50, 10.99);
		market1.addInventory("Chicken", 50, 5.99);
		market1.addInventory("Pizza", 50, 3.99);
		market1.addInventory("Salad", 4, 0.99);
		cook.addMarket(market1);
		market1.setCashier(cashier);
		market1.startThread();
		market2.addInventory("Steak", 50, 10.99);
		market2.addInventory("Chicken", 50, 5.99);
		market2.addInventory("Pizza", 50, 3.99);
		market2.addInventory("Salad", 6, 0.99);
		cook.addMarket(market2);
		market2.setCashier(cashier);
		market2.startThread();
		market3.addInventory("Steak", 50, 10.99);
		market3.addInventory("Chicken", 50, 5.99);
		market3.addInventory("Pizza", 50, 3.99);
		market3.addInventory("Salad", 50, 0.99);
		cook.addMarket(market3);
		market3.setCashier(cashier);
		market1.startThread();
		market2.startThread();
		market3.startThread();
		host.startThread();
		CookGui cookGui = new CookGui(cook);
		gui.animationPanel.addGui(cookGui);
		cook.setGui(cookGui);
		cook.startThread();
		cashier.startThread();
		
		setLayout(null);
		group.setLayout(new GridLayout(1, 2, 10, 10));
		group.add(waiterPanel);
		group.add(customerPanel);
		initRestLabel();
		restLabel.setBounds(1, 1, 531, 220);
		add(restLabel);
		group.setBounds(1, 221, 540, 300);
		add(group);
	}

	/**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
	private void initRestLabel(){
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(new JLabel("               "), BorderLayout.EAST );
		restLabel.add(new JLabel("               "), BorderLayout.WEST );
	}

	/**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
	public void showInfo(String type, String name) {

		if(type.equals("Customers")) {

			for(int i = 0; i < customers.size(); i++) {
				CustomerAgent temp = customers.get(i);
				if(temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
		else if (type.equals("Waiters")) {
			for (int i = 0; i < waiters.size(); i++) {
				WaiterAgent temp = waiters.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
	}

	/**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
	public void addPerson(String type, String name) {
		if (type.equals("Customers")) {
			CustomerAgent c = new CustomerAgent(name, gui);
			customers.add(c);
			CustomerGui g = new CustomerGui(c, gui, 30+host.waitingCustomers.size()*25, 5);
			
			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setCashier(cashier);
			c.setGui(g);
			//customers.add(c);
			c.startThread();
			//c.gotHungry();
			if (customerPanel.customerBool == true) {
    			//c.gotHungry();
    			c.getGui().setHungry();
    		}
		}
		else if (type.equals("Waiters")) {
			WaiterAgent w = new WaiterAgent(name);
			waiters.add(w);
			WaiterGui g = new WaiterGui(w, 5, 30+(waiters.size()-1)*25);
			
			gui.animationPanel.addGui(g);// dw
			w.setGuiPanel(gui);
			w.setHost(host);
			w.setCook(cook);
			w.setCashier(cashier);
			host.setWaiter(w);
			//waiters.add(w);
			w.setGui(g);
			w.startThread();
		}
	}
	
	/*public void setPause() {
		host.setPause();
		for (WaiterAgent w : waiters) {
			w.setPause();
		}
    	cook.setPause();
    	for (CustomerAgent c : customers) {
    		c.setPause();
    	}
    	cashier.setPause();
    	market1.setPause();
    	market2.setPause();
    	market3.setPause();
    	animationPanel.setPause();
	}
	
	public void setRestart() {
		host.setRestart();
		for (WaiterAgent w : waiters) {
			w.setRestart();
		}
    	cook.setRestart();
    	for (CustomerAgent c : customers) {
    		c.setRestart();
    	}
    	cashier.setRestart();
    	market1.setRestart();
    	market2.setRestart();
    	market3.setRestart();
    	animationPanel.setRestart();
	}*/
}
