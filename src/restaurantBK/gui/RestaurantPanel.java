package restaurantBK.gui;

import agent.Agent;
import restaurantBK.BKCashierRole;
import restaurantBK.BKCustomerRole;
import restaurantBK.BKHostRole;
import restaurantBK.MarketAgent;
import restaurantBK.BKWaiterRole;
import restaurantBK.BKCookRole;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Waiter;
import restaurantBK.ItalianMenu;
import restaurantBK.gui.CustomerGui;
import restaurantBK.gui.WaiterGui;
import restaurantBK.interfaces.Cook;
import restaurantBK.interfaces.Cashier;
import restaurantBK.interfaces.Host;

import javax.swing.*;

import city.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	 //private Vector<BKCustomerRole> customers = new Vector<BKCustomerRole>();
	 private Vector<Agent> agents = new Vector<Agent>();
	 private JPanel restLabel = new JPanel();
	 private ListPanel customerPanel = new ListPanel(this, "Customers");
	 private JPanel group = new JPanel();
	 private BKHostRole host;
	 private RestaurantGui gui; //reference to main gui
    //Host, cook, waiters and customers
	 private BKCookRole cook;
	 private List<Host> hosts = new ArrayList<Host>();
	 //private Host host; //this is the active host
	 private List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
	 private List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
	 private List<Cook> cooks = Collections.synchronizedList(new ArrayList<Cook>());
	 //private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
	 private List<Cashier> cashiers = Collections.synchronizedList(new ArrayList<Cashier>());

	 
    //private Vector<BKWaiterRole> waiters = new Vector<BKWaiterRole>();
    //private WaiterGui waiter1Gui = new WaiterGui(waiter1, gui);
    private JButton pause = new JButton("Pause");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private BKCashierRole cashier;
    private MarketAgent market1 = new MarketAgent("Sprouts",3,3,3,3);
    private MarketAgent market2 = new MarketAgent("Whole Foods",20,20,20,20);
    private MarketAgent market3 = new MarketAgent("Last Resort",100,100,100,100);
    
    public boolean fullyStaffed = false;
    	
    public RestaurantPanel(RestaurantGui gui) {
    	
        this.gui = gui;
        /*waiter1.setGui(waiter1Gui);
        waiter1.setCook(cook);
        waiter1.setHost(host);
        host.addWaiter(waiter1);
        agents.add(waiter1);
        gui.animationPanel.addGui(waiter1Gui);
        waiter1.startThread(); */
        //cashier.startThread();
        //host.startThread();
        
        //cook.startThread();
//        cook.addMarket(market1);
//        cook.addMarket(market2);
//        cook.addMarket(market3);
//        market1.setCashier(cashier);
//        market2.setCashier(cashier);
//        market3.setCashier(cashier);
//        market1.startThread();
//        market2.startThread();
//        market3.startThread();
//        //market.startThread();
        //agents.add(host);
        //agents.add(cook);
        //agents.add(cashier);
        //agents.add(market1);
        //agents.add(market2);
        //agents.add(market3);
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
    /*public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		BKCustomerRole c = new BKCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		//agents.add(c);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	}
    	if(type.equals("Waiters")) {
    		BKWaiterRole w = new BKWaiterRole(name);
            w.setCook(cook);
            w.setHost(host);
            w.setCashier(cashier);
            waiters.add(w);
            w.setRestX(waiters.size());
            WaiterGui g = new WaiterGui(w, gui,waiters.size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
            //agents.add(w);
            host.addWaiter(w);
            //agents.add(w);
    		//w.startThread();
    	}
    	/*if (type.equals("Waiters")) {
    		
    	}*/
    //}
    //MAKE A WAITER PANEL HERE
    
    public void addWaiter(String type, String name) {
    	
    }
    /*public void addHungryPerson(String type, String name,boolean hungry) {

    	if (type.equals("Customers")) {
    		BKCustomerRole c = new BKCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		//agents.add(c);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    		if(hungry)
    		{
    			g.setHungry();
    		}
    	}
    	if(type.equals("Waiters")) {
    		BKWaiterRole w = new BKWaiterRole(name);
            w.setCook(cook);
            w.setHost(host);
            w.setCashier(cashier);
            waiters.add(w);
            w.setRestX(waiters.size());
            WaiterGui g = new WaiterGui(w, gui,waiters.size());
    		gui.animationPanel.addGui(g);
    		w.setGui(g);
            //agents.add(w);
            host.addWaiter(w);
            //agents.add(w);
    		//w.startThread();
    		if(hungry) {
    			g.setWantsBreak();
    		}
    		System.out.println("Welcome to work "+name);
    	}
    }*/
    
    public void addCustomer(PersonAgent person) {

    	//THIS WILL BE CALLED BY THE PERSON AGENT
    	BKCustomerRole cust;
    	
    	for(Customer c : customers)
    	{
    		cust = (BKCustomerRole)c;
    		if(cust.getPerson() == person)
    		{
    			person.msgAssignRole(cust);
    			cust.gotHungry();
    			return;
    		}
    	}

    	BKCustomerRole c = new BKCustomerRole(person, person.getName(), this);	
    	
    	CustomerGui g = new CustomerGui(c, gui);
    	g.setHungry();
  
    	//gui.addRestaurantCustomer(c, hungry);
    	
    	gui.animationPanel.addGui(g);// dw
    	c.setHost(host);
    	c.setCashier(cashier);
    	c.setGui(g);
    	
    	customers.add(c);
    	//hungry.doClick(); //set the customer to hungry
    	person.msgAssignRole(c);
    }
    
    public void addWaiter(PersonAgent person)
    {
    	BKWaiterRole waiter;
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		waiter = (BKWaiterRole)w;
	    		if(waiter.getPerson() == person)
	    		{
	    			person.msgAssignRole(waiter);
	    			return;
	    		}
	    	}
    	
    	BKWaiterRole w = new BKWaiterRole(person, person.getName(), this);//, waiters.size(), host, cooks, cookOrders, cashier, new Menu(menu));
    	waiters.add(w);
    	if(host != null)
    		((BKHostRole)host).addWaiter(w);
    	
    		//c.addWaiter(w);
    	
    	
		WaiterGui waiterGui = new WaiterGui(w,gui,waiters.size());
		w.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		
		//Start the thread
		person.msgAssignRole(w);
    	}
    }
    
    public void addHost(PersonAgent person)
    {
        host = new BKHostRole(person, person.getName(), this);
       // hosts.add(host);
        //((BKHostRole)host).setCook(cook);
        //((BKHostRole)host).setWaiters(waiters);
        
        //update other agents
        synchronized(waiters)
        {
	        for(Waiter w : waiters)
	        {
	        	((BKHostRole)host).addWaiter(w);
	        	((BKWaiterRole)w).setHost(host);
	        }
        }
      /*  for(Cook c : cooks)
        {
        	((BKCookRole)c).setHost(host);
        }
       */
        person.msgAssignRole((BKHostRole)host);
    }
    
    public void addCook(PersonAgent person)
    {
    	BKCookRole c = new BKCookRole(person, person.getName(), this);//, cookOrders, market, cashier, timer);
		cook = c;
		CookGui cg = new CookGui(cook,gui);
		cook.setGui(cg);
		gui.animationPanel.addGui(cg);
		person.msgAssignRole(c);
    }
    
    public void addCashier(PersonAgent person)
    {
    	cashier = new BKCashierRole(person, person.getName(), this);
    	person.msgAssignRole((BKCashierRole)cashier);
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		((BKWaiterRole)w).setCashier(cashier);
	    	}
    	}
    }
    
    public boolean hasHost() {
    	if(host==null) {
    		return false;
    	}
    	return true;
    }
    public boolean hasCook() {
    	if(cook==null) {
    		return false;
    	}
    	return true;
    }
    public boolean hasWaiters() {
    	if(waiters.size()!=0) {
    		return true;
    	}
    	return false;
    }
    public boolean hasCashier() {
    	if(cashier==null) {
    		return false;
    	}
    	return true;
    }

}
