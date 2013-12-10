package restaurantLY.gui;

import restaurantLY.LYCustomerRole;
import restaurantLY.LYWaiterRole;
import restaurantLY.interfaces.*;
import restaurantLY.LYHostRole;
import restaurantLY.LYCashierRole;
import restaurantLY.LYCookRole;

import javax.swing.*;

import city.PersonAgent;
import market.Market;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.*; 

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	AnimationPanel animationPanel =  new AnimationPanel();
	
	//Host, cook, waiters and customers
	private List<Host> hosts = new ArrayList<Host>();
	private Host host; //this is the active host
	private List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
	private List<Cook> cooks = Collections.synchronizedList(new ArrayList<Cook>());
	//private Cook cook;
	private List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
	private List<Cashier> cashiers = Collections.synchronizedList(new ArrayList<Cashier>());
	private Cashier cashier; //this is the active cashier
	
	private Market market;
	
	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private ListPanel waiterPanel = new ListPanel(this, "Waiters");
	private JPanel group = new JPanel();
    
    private List<JCheckBox> breakBoxes = new ArrayList<JCheckBox>();
	
	public RestaurantGui gui;
	
	public static final int CLOSINGTIME = 20;
	
    class HungerListener implements ActionListener
    {
    	Customer customer;
    	JCheckBox hungerCheckBox;
    	
    	public HungerListener(Customer customer, JCheckBox hungerCheckBox)
    	{
    		this.customer = customer;
    		this.hungerCheckBox = hungerCheckBox;
    	}
    	
    	public void actionPerformed(ActionEvent e)
    	{
    		if(hungerCheckBox.isSelected())
    		{
    			customer.getGui().setHungry();
    		}
    	}
    }
    
	public RestaurantPanel(RestaurantGui gui, Market market) {
		this.gui = gui;
		this.market = market;
		
		//cook.addMarket(market);
		//market.setCashier(cashier);
		//market.startThread();

		//host.startThread();
		//CookGui cookGui = new CookGui(cook);
		//gui.animationPanel.addGui(cookGui);
		//cook.setGui(cookGui);
		//cook.startThread();
		//cashier.startThread();
		
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
		//label.setText(
        //        "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
				Customer temp = customers.get(i);
				if(temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
		else if (type.equals("Waiters")) {
			for (int i = 0; i < waiters.size(); i++) {
				Waiter temp = waiters.get(i);
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
	//Adding from restaurant
	public void addPerson(String type, String name, JCheckBox hungry, boolean hunger) {
		if (type.equals("Customers")) {
			PersonAgent p = new PersonAgent(name);
			LYCustomerRole c = new LYCustomerRole(p, this, hungry);
			customers.add(c);
			CustomerGui g = new CustomerGui(c, gui, 30+host.getWaitingCustomers().size()*25, 5);
			
            hungry.addActionListener(gui);
			gui.addRestaurantCustomer(c, hungry);
            
			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setCashier(cashier);
			c.setGui(g);
            if(hunger) {
                hungry.doClick();
            }
			//customers.add(c);
			p.msgAssignRole(c);
			p.startThread();
			//c.gotHungry();
			/*if (customerPanel.customerBool == true) {
    			//c.gotHungry();
    			c.getGui().setHungry();
    		}*/
		}
		/*else if (type.equals("Waiters")) {
			PersonAgent p = new PersonAgent(name);
			LYWaiterRole w = new LYWaiterRole(p, this, host);
			waiters.add(w);
			((LYHostRole)host).addWaiter(w);
			WaiterGui g = new WaiterGui(w, 5, 30+(waiters.size()-1)*25);
			
			gui.animationPanel.addGui(g);// dw
			w.setGuiPanel(gui);
			w.setHost(host);
			//w.setCook(cook);
			w.setCashier(cashier);
			host.setWaiter(w);
			//waiters.add(w);
			w.setGui(g);
			p.msgAssignRole(w);
			p.startThread();
		}*/
	}
    
    //Adding from restaurant
    public void addWaiter(String name, final JCheckBox breakBox) {
        PersonAgent p = new PersonAgent(name);
        LYWaiterRole w = new LYWaiterRole(p, this, host, cooks, cashier, breakBox);
        waiters.add(w);
        ((LYHostRole)host).addWaiter(w);
        for(Cook c: cooks) {
            c.addWaiter(w);
        }
        /*final Waiter waiter = w;
    	breakBoxes.add(breakBox);
    	breakBox.addActionListener(new ActionListener()
                                   {
            public void actionPerformed(ActionEvent e)
            {
                if(breakBox.getText().equals("Want Break"))
                {
                    breakBox.setEnabled(true);
                    waiter.msgWantBreak();
                }
                else if(breakBox.getText().equals("Back to Work"))
                {
                    breakBox.setEnabled(true);
                    waiter.msgBackFromBreak();
                }
            }
        });*/
    	
		WaiterGui waiterGui = new WaiterGui(w, 5, 30+(waiters.size()-1)*25);
		w.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		
		//Start the thread
		p.msgAssignRole(w);
		p.startThread(); //hack. PersonAgent's thread should already be running
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
	
	//Adding from outside restaurant
    public void addCustomer(PersonAgent person) {

    	//THIS WILL BE CALLED BY THE PERSON AGENT
    	LYCustomerRole cust;
    	for(Customer c : customers)
    	{
    		cust = (LYCustomerRole)c;
    		if(cust.getPerson() == person)
    		{
    			person.msgAssignRole(cust);
    			cust.gotHungry();
    			return;
    		}
    	}
    	
    	JCheckBox hungry = new JCheckBox("Hungry?");
    	LYCustomerRole c = new LYCustomerRole(person, this, hungry);
    	
    	customers.add(c);
		CustomerGui g = new CustomerGui(c, gui, 30+host.getWaitingCustomers().size()*25, 5);
    	
    	hungry.addActionListener(gui);
    	gui.addRestaurantCustomer(c, hungry);
    	
    	gui.animationPanel.addGui(g);// dw
    	c.setHost(host);
    	c.setCashier(cashier);
    	c.setGui(g);
    	
    	customers.add(c);
    	hungry.doClick(); //set the customer to hungry
    	person.msgAssignRole(c);
    }
    
    public void addWaiter(PersonAgent person)
    {
    	LYWaiterRole waiter;
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		waiter = (LYWaiterRole)w;
	    		if(waiter.getPerson() == person)
	    		{
	    			person.msgAssignRole(waiter);
	    			return;
	    		}
	    	}
    	
    	
    	final JCheckBox breakBox = new JCheckBox("");
    	LYWaiterRole w = new LYWaiterRole(person, this, host, cooks, cashier, breakBox);
    	waiters.add(w);
    	if(host != null)
    		((LYHostRole)host).addWaiter(w);
    	for(Cook c : cooks)
    	{
    		c.addWaiter(w);
    	}
            
        /*final Waiter w2 = w;
        breakBoxes.add(breakBox);
        breakBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(breakBox.getText().equals("Want Break")) {
                    breakBox.setEnabled(true);
                    w2.msgWantBreak();
                }
                else if(breakBox.getText().equals("Back to Work")) {
                    breakBox.setEnabled(true);
                    w2.msgBackFromBreak();
                }
            }
        });*/
    	
		WaiterGui waiterGui = new WaiterGui(w, 5, 30+(waiters.size()-1)*25);
		w.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		
		//Start the thread
		person.msgAssignRole(w);
    	}
    }
    
    public void addHost(PersonAgent person)
    {
        host = new LYHostRole(person, this);
        hosts.add(host);
        ((LYHostRole)host).setCooks(cooks);
        ((LYHostRole)host).setWaiters(waiters);
        
        //update other agents
        synchronized(waiters)
        {
	        for(Waiter w : waiters)
	        {
	        	((LYHostRole)host).addWaiter(w);
	        	((LYWaiterRole)w).setHost(host);
	        }
        }
        for(Cook c : cooks)
        {
        	((LYCookRole)c).setHost(host);
        }
        
        person.msgAssignRole((LYHostRole)host);
    }
    
    public void addCook(PersonAgent person)
    {
    	LYCookRole c = new LYCookRole(person, this, market);
		cooks.add(c);
		person.msgAssignRole(c);
		
		CookGui cookGui = new CookGui(c);
		c.setGui(cookGui);
		gui.animationPanel.addGui(cookGui);
    }
    
    public void addCashier(PersonAgent person)
    {
    	cashier = new LYCashierRole(person, 100, this);
    	person.msgAssignRole((LYCashierRole)cashier);
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		((LYWaiterRole)w).setCashier(cashier);
	    	}
    	}
    }
    
    //Information for Restaurant building
    public int activeCustomers()
    {
    	int count = 0;
    	synchronized(customers) {
	    	for(Customer c : customers)
	    	{
	    		if(c instanceof LYCustomerRole && ((LYCustomerRole)c).isActive())
	    		{
	    			++count;
	    		}
	    	}
    	}
    	return count;
    }
    
    public int activeWaiters()
    {
    	int count = 0;
    	synchronized(waiters) {
	    	for(Waiter w : waiters)
	    	{
	    		if(((LYWaiterRole)w).isActive())
	    		{
	    			++count;
	    		}
	    	}
    	}
    	return count;
    }
    
    public int activeCooks()
    {
    	int count = 0;
    	synchronized(cooks)
    	{
	    	for(Cook c : cooks)
	    	{
	    		if(((LYCookRole)c).isActive())
	    		{
	    			++count;
	    		}
	    	}
    	}
    	return count;
    }
    
    public void setOpen(Boolean b)
    {
    	gui.setOpen(b);
    }
    
    public boolean fullyStaffed()
    {
    	return (activeCooks() > 0) && (activeWaiters() > 0) && (host != null) && (cashier != null);
    }
    
    public boolean justHost()
    {
    	return (activeCooks() == 0) && (activeWaiters() == 0) && 
    			(activeCustomers() == 0) && (cashier == null);
    }
    
    public boolean justCashier()
    {
    	return (activeCooks() == 0) && (activeWaiters() == 0) && 
    			(activeCustomers() == 0) && (cashier != null);
    }
    
    public void hostLeaving()
    {
    	host = null;
    	gui.setOpen(false);
    }
    
    public void cashierLeaving()
    {
    	cashier = null;
    }
    
    public boolean hasHost()
    {
    	return host != null;
    }
    
    public boolean hasCashier()
    {
    	return cashier != null;
    }

	public boolean isOpen() {
		return gui.isOpen();
	}
}
