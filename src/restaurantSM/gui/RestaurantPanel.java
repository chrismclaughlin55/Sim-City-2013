package restaurantSM.gui;

import restaurantSM.*;
import restaurantSM.utils.Order;

import javax.swing.*;

import city.PersonAgent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private SMHostRole host;
    private SMCookRole cook;
    private List<SMCustomerRole> customers = Collections.synchronizedList(new ArrayList<SMCustomerRole>());
    private List<SMWaiterRole> waiters = Collections.synchronizedList(new ArrayList<SMWaiterRole>());
    private SMCashierRole cashier;
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();
    private boolean pcWaiter = true;
    private List<Order> pcOrders = Collections.synchronizedList(new ArrayList<Order>());

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
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
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
                SMCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
        	Collection<SMWaiterRole> waiters = host.getWaiters();
        	for (SMWaiterRole w : waiters) {
        		if (w.getName() == name) {
        			gui.updateInfoPanel(w);
        		}
        	}
        }
    }

    public void pauseAllAgents() {
    	
    }
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    /*public void addPerson(String type, String name, boolean hungry) {

    	if (type.equals("Customers")) {
    		SMCustomerRole c = new SMCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui, customers.size() + 1);
    		if (hungry == true){
    			g.setHungry();
    		}
    		

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		//c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	else if (type.equals("Waiter")) {
    		SMWaiterRole w = new SMWaiterRole(name);
    		WaiterGui g = new WaiterGui(w, host.getWaiters().size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		//w.setCashier(cashier);
    		host.addWaiter(w);
    		w.startThread();
    	}
    }*/

    public void addCustomer(PersonAgent p) {
    	synchronized(customers)
    	{
	    	SMCustomerRole c = new SMCustomerRole(p);
	    	CustomerGui cGui = new CustomerGui(c, gui, customers.size());
	    	c.setGui(cGui);
	    	c.setHost(host);
	    	c.setCashier(cashier);
	    	gui.animationPanel.addGui(cGui);
	    	cGui.setHungry();
	    	p.msgAssignRole(c);
    	}
    }
    
    public void addWaiter(PersonAgent p) {
    	if (waiters.size() < 3) {
    		SMWaiterRole w = new SMWaiterRole(p, pcWaiter, pcOrders);
    		pcWaiter = !pcWaiter;
    		WaiterGui wGui = new WaiterGui(w, waiters.size());
    		w.setGui(wGui);
    		w.setHost(host);
    		if (cook != null) {
    			w.setCook(cook);
    		}
    		if (cashier != null) {
    			w.setCashier(cashier);
    		}
    		p.msgAssignRole(w);
    		waiters.add(w);
    		gui.animationPanel.addGui(wGui);
    	}
    }
    
    public void addHost(PersonAgent p) {
    	if (host == null) {
    		host = new SMHostRole(p, customers, waiters);
    		p.msgAssignRole(host);
    	}
    }
    
    public void addCook(PersonAgent p) {
    	if (cook == null) {
    		cook = new SMCookRole(p, pcOrders);
    		CookGui cGui = new CookGui(cook, gui);
    		cook.setGui(cGui);
    		p.msgAssignRole(cook);
    		gui.animationPanel.addGui(cGui);
    	}
    	for (SMWaiterRole w : waiters) {
    		w.setCook(cook);
    	}
    }
    
    public void addCashier(PersonAgent p) {
    	if (cashier == null) {
    		cashier = new SMCashierRole(p);
    		p.msgAssignRole(cashier);
    		for (SMWaiterRole w : waiters) {
    			w.setCashier(cashier);
    		}
    	}
    }
    
    public int activeCustomers() {
    	return customers.size();
    }
    
    public int activeWaiters() {
    	return waiters.size();
    }
    
    public boolean activeHost() {
    	return host != null;
    }
    
    public boolean activeCook() {
    	return cook != null;
    }
    
    public boolean activeCashier() {
    	return cashier != null;
    }
    
    public boolean fullyStaffed() {
    	return activeWaiters() > 0 && activeHost() && activeCook() && activeCashier();
    }
    
    public void hostLeaving() {
    	host = null;
    }
    
    public void cashierLeaving() {
    	cashier = null;
    }
    
    public boolean isOpen() {
    	return gui.isOpen();
    }
    
    public void setOpen(Boolean b) {
    	gui.setOpen(b);
    }
    
}
