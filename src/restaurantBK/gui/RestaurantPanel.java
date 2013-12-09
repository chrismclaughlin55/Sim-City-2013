package restaurantBK.gui;

import agent.Agent;
import restaurantBK.CashierAgent;
import restaurantBK.CustomerAgent;
import restaurantBK.HostAgent;
import restaurantBK.MarketAgent;
import restaurantBK.WaiterAgent;
import restaurantBK.CookAgent;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	 private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
	 private Vector<Agent> agents = new Vector<Agent>();
	 private JPanel restLabel = new JPanel();
	 private ListPanel customerPanel = new ListPanel(this, "Customers");
	 private JPanel group = new JPanel();
	 private HostAgent host = new HostAgent("Brian");
	 private RestaurantGui gui; //reference to main gui
    //Host, cook, waiters and customers
	 private CookAgent cook = new CookAgent("Johnny");
	 private CookGui cg = new CookGui(cook,gui);
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    //private WaiterGui waiter1Gui = new WaiterGui(waiter1, gui);
    private JButton pause = new JButton("Pause");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private CashierAgent cashier = new CashierAgent("Rocky");
    private MarketAgent market1 = new MarketAgent("Sprouts",3,3,3,3);
    private MarketAgent market2 = new MarketAgent("Whole Foods",20,20,20,20);
    private MarketAgent market3 = new MarketAgent("Last Resort",100,100,100,100);

    public RestaurantPanel(RestaurantGui gui) {
    	
        this.gui = gui;
        /*waiter1.setGui(waiter1Gui);
        waiter1.setCook(cook);
        waiter1.setHost(host);
        host.addWaiter(waiter1);
        agents.add(waiter1);
        gui.animationPanel.addGui(waiter1Gui);
        waiter1.startThread(); */
        cashier.startThread();
        host.startThread();
        cook.setGui(cg);
        gui.animationPanel.addGui(cg);
        cook.startThread();
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        market3.setCashier(cashier);
        market1.startThread();
        market2.startThread();
        market3.startThread();
        //market.startThread();
        agents.add(host);
        agents.add(cook);
        agents.add(cashier);
        agents.add(market1);
        agents.add(market2);
        agents.add(market3);
        setLayout(new GridLayout(3, 2, 20, 20));
        //this.add(gui.animationPanel);
        group.setLayout(new GridLayout(1, 2, 10, 10));
        
        //this.add(gui.animationFrame);

        group.add(customerPanel);
        //group.add(gui.animationPanel);
        initRestLabel();
        add(restLabel);
        add(group);
        add(pause);
        add(waiterPanel);
        pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				for(Agent a : agents) {
					a.pause();
				}				
			}
        	
        });
        //gui.animationFrame.add(this);
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
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + "Brian" + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
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
                Customer temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if(type.equals("Waiters")) {
        	for(int i = 0; i<waiters.size(); i++) {
        		Waiter temp = waiters.get(i);
        		if(temp.getName()==name) {
        			gui.updateInfoPanel(temp);
        		}
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
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		agents.add(c);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	if(type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
            w.setCook(cook);
            w.setHost(host);
            w.setCashier(cashier);
            waiters.add(w);
            w.setRestX(waiters.size());
            WaiterGui g = new WaiterGui(w, gui,waiters.size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
            agents.add(w);
            host.addWaiter(w);
            agents.add(w);
    		w.startThread();
    	}
    	/*if (type.equals("Waiters")) {
    		
    	}*/
    }
    //MAKE A WAITER PANEL HERE
    
    public void addWaiter(String type, String name) {
    	
    }
    public void addHungryPerson(String type, String name,boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		agents.add(c);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    		if(hungry)
    		{
    			g.setHungry();
    		}
    	}
    	if(type.equals("Waiters")) {
    		WaiterAgent w = new WaiterAgent(name);
            w.setCook(cook);
            w.setHost(host);
            w.setCashier(cashier);
            waiters.add(w);
            w.setRestX(waiters.size());
            WaiterGui g = new WaiterGui(w, gui,waiters.size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
            agents.add(w);
            host.addWaiter(w);
            agents.add(w);
    		w.startThread();
    		if(hungry) {
    			g.setWantsBreak();
    		}
    		System.out.println("Welcome to work "+name);
    	}
    }

}
