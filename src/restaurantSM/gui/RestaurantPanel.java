package restaurantSM.gui;

import restaurantSM.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private CookAgent cook = new CookAgent("Chef");
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private MarketAgent market0 = new MarketAgent("Stater Bros");
    private MarketAgent market1 = new MarketAgent("Trader Joe's");
    private MarketAgent market2 = new MarketAgent("Ralphs");
    private CashierAgent cashier = new CashierAgent("Moneybags");
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;

        host.startThread();
        cashier.startThread();
        cook.addMarket(market0);
        cook.addMarket(market1);
        cook.addMarket(market2);
        cashier.addMarket(market0);
        cashier.addMarket(market1);
        cashier.addMarket(market2);
        CookGui cookGui = new CookGui(cook, gui);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        cook.startThread();
        market0.setCook(cook);
        market0.setCashier(cashier);
        market0.startThread();
        market1.setCashier(cashier);
        market1.setCook(cook);
        market1.startThread();
        market2.setCashier(cashier);
        market2.setCook(cook);
        market2.startThread();
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("            "), BorderLayout.EAST);
        restLabel.add(new JLabel("            "), BorderLayout.WEST);
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

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
        	Collection<WaiterAgent> waiters = host.getWaiters();
        	for (WaiterAgent w : waiters) {
        		if (w.getName() == name) {
        			gui.updateInfoPanel(w);
        		}
        	}
        }
    }

    public void pauseAllAgents() {
    	host.pause();
    	cook.pause();
    	for (WaiterAgent w : host.getWaiters()) {
    		w.pauseAllCustomers();
    		w.pause();
    	}
    	
    }
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui, customers.size() + 1);
    		if (hungry == true){
    			g.setHungry();
    		}
    		

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	else if (type.equals("Waiter")) {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui g = new WaiterGui(w, host.getWaiters().size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		host.addWaiter(w);
    		w.startThread();
    	}
    }

}
