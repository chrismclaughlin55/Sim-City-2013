package restaurantMQ.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import market.Market;
import restaurantMQ.CookOrder;
import restaurantMQ.MQCashierRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.MQCustomerRole;
import restaurantMQ.MQHostRole;
import restaurantMQ.MQWaiterRole;
import restaurantMQ.Menu;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Cook;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;
import agent.Agent;
import city.PersonAgent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	private static final int IMGW = 50;
	private static final int IMGH = 50;
	private static final int MAINGRIDROWS = 2;
	private static final int MAINGRIDCOLS = 2;
	private static final int SECGRIDROWS = 1;
	private static final int SECGRIDCOLS = 2;
	private static final int NWAITERS = 1;
	private static final int NCOOKS = 1;
	private static final int NMARKETS = 3;
	public static final int CLOSINGTIME = 20;
	
    //all roles ever
	private List<Host> hosts = new ArrayList<Host>();
    private Host host; //this is the active host
    private List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
    private List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
    private List<Cook> cooks = Collections.synchronizedList(new ArrayList<Cook>());
    //private List<MarketAgent> markets = Collections.synchronizedList(new ArrayList<MarketAgent>());
    private List<Cashier> cashiers = Collections.synchronizedList(new ArrayList<Cashier>());
    private Cashier cashier; //this is the active cashier
    //private Vector<HungerListener> hungerListeners = new Vector<HungerListener>();
    
    private Market market;
    
    private List<CookOrder> cookOrders = Collections.synchronizedList(new ArrayList<CookOrder>());

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterListPanel waiterPanel = new WaiterListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JPanel self = new JPanel();
    private JButton pauseButton = new JButton("Pause");
    private boolean paused = false;
    
    private List<JCheckBox> breakBoxes = new ArrayList<JCheckBox>();
    
    Timer timer = new Timer();
    
    Menu menu = new Menu();

    private RestaurantGui gui; //reference to main gui
    
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
        
        /*
        //Cashier instantiation (hack)
        PersonAgent p1 = new PersonAgent("Cashier");
        cashier = new MQCashierRole(p1);
        cashiers.add(cashier);
        p1.msgAssignRole((MQCashierRole)cashier);
        p1.startThread();
        */
        
        //Market instantiation (hack)
       
        
        /*
        //Cook instantiation (hack)
        for(int i = 0; i < NCOOKS; ++i)
		{
        	PersonAgent p = new PersonAgent("Mike");
        	MQCookRole c = new MQCookRole(p, cookOrders, markets, cashier, timer);
			cooks.add(c);
			p.msgAssignRole(c);
			p.startThread();
		}
        
        
        //Host instantiation (hack)
        PersonAgent p2 = new PersonAgent("Host");
        host = new MQHostRole(p2);
        hosts.add(host);
        ((MQHostRole)host).setCooks(cooks);
        ((MQHostRole)host).setWaiters(waiters);
        p2.msgAssignRole((MQHostRole)host);
        p2.startThread();
        */

        setLayout(new GridLayout(MAINGRIDROWS, MAINGRIDCOLS));
        group.setLayout(new GridLayout(SECGRIDROWS, SECGRIDCOLS));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        initSelf();
        add(self);
        add(waiterPanel);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText("RestaurantMQ");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
    
    private void initSelf()
    {
    	JLabel name = new JLabel();
    	self.setLayout(new FlowLayout());
    	name.setText("Michael Qian");
    	self.add(name);
    	ImageIcon pic = new ImageIcon(getClass().getResource("IMG_0008.JPG"));
    	pic.setImage(pic.getImage().getScaledInstance(IMGW, IMGH, Image.SCALE_SMOOTH));
    	JLabel imageLabel = new JLabel(pic);
    	self.add(imageLabel);
    	self.add(pauseButton);
    	pauseButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e)
    		{
    			togglePause();			
    		}
    	});
    	
    }
    
    public void togglePause()
    {
    	if(!paused)
		{
    		for(Waiter w : waiters)
    		{
    			((Agent)w).msgPause();
    		}
    		for(Customer c : customers)
    		{
    			c.msgPause();
    		}
    		for(Cook c : cooks)
    		{
    			c.msgPause();
    		}
    		((MQHostRole)host).msgPause();
			pauseButton.setText("Resume");
			paused = true;
		}
		else
		{
			for(Waiter w : waiters)
    		{
    			((Agent) w).msgPause();
    		}
    		for(Customer c : customers)
    		{
    			c.msgPause();
    		}
    		for(Cook c : cooks)
    		{
    			c.msgPause();
    		}
    		((MQHostRole)host).msgPause();
			pauseButton.setText("Pause");
			paused = false;
		}
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
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    //Adding from restaurant
    public void addPerson(String type, String name, JCheckBox hungry, boolean hunger) {

    	//THIS WILL BE CALLED BY THE PERSON AGENT
    	if (type.equals("Customers"))
    	{
    		PersonAgent p = new PersonAgent(name);
    		MQCustomerRole c = new MQCustomerRole(p, timer, hungry, this);	
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		hungry.addActionListener(gui);
    		gui.addRestaurantCustomer(c, hungry);
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		if(hunger) //This allows the user to control it, later we should let the PersonAgent handle this
    			hungry.doClick();
    		customers.add(c);
    		p.msgAssignRole(c);
    		p.startThread(); //Hack. PersonAgent's thread should already be going
    	}
    }
    
    //Adding from restaurant
    public void addWaiter(String name, final JCheckBox breakBox)
    {
    	PersonAgent p = new PersonAgent(name);
    	MQWaiterRole w = new MQWaiterRole(p, this, waiters.size(), host, cooks, cookOrders, cashier, new Menu(menu), breakBox);
    	waiters.add(w);
    	((MQHostRole)host).addWaiter(w);
    	for(Cook c : cooks)
    	{
    		c.addWaiter(w);
    	}
    	
    	final Waiter waiter = w;
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
    		});
    	
		WaiterGui waiterGui = new WaiterGui(w);
		w.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		
		//Start the thread
		p.msgAssignRole(w);
		p.startThread(); //hack. PersonAgent's thread should already be running
    }
    
    //Adding from outside restaurant
    public void addCustomer(PersonAgent person) {

    	//THIS WILL BE CALLED BY THE PERSON AGENT
    	MQCustomerRole cust;
    	for(Customer c : customers)
    	{
    		cust = (MQCustomerRole)c;
    		if(cust.getPerson() == person)
    		{
    			person.msgAssignRole(cust);
    			cust.msgGotHungry();
    			return;
    		}
    	}
    	
    	
    	JCheckBox hungry = new JCheckBox("Hungry?");
    	MQCustomerRole c = new MQCustomerRole(person, timer, hungry, this);	
    	
    	CustomerGui g = new CustomerGui(c, gui);
    	
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
    	MQWaiterRole waiter;
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		waiter = (MQWaiterRole)w;
	    		if(waiter.getPerson() == person)
	    		{
	    			person.msgAssignRole(waiter);
	    			return;
	    		}
	    	}
    	
    	
    	final JCheckBox breakBox = new JCheckBox("");
    	MQWaiterRole w = new MQWaiterRole(person, this, waiters.size(), host, cooks, cookOrders, cashier, new Menu(menu), breakBox);
    	waiters.add(w);
    	if(host != null)
    		((MQHostRole)host).addWaiter(w);
    	for(Cook c : cooks)
    	{
    		c.addWaiter(w);
    	}
    	
    	final Waiter w2 = w;
    	breakBoxes.add(breakBox);
    	breakBox.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				if(breakBox.getText().equals("Want Break"))
    				{
    					breakBox.setEnabled(true);
    					w2.msgWantBreak();
    				}
    				else if(breakBox.getText().equals("Back to Work"))
    				{
    					breakBox.setEnabled(true);
    					w2.msgBackFromBreak();
    				}
    			}
    		});
    	
		WaiterGui waiterGui = new WaiterGui(w);
		w.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		
		//Start the thread
		person.msgAssignRole(w);
    	}
    }
    
    public void addHost(PersonAgent person)
    {
        host = new MQHostRole(person, this);
        hosts.add(host);
        ((MQHostRole)host).setCooks(cooks);
        ((MQHostRole)host).setWaiters(waiters);
        
        //update other agents
        synchronized(waiters)
        {
	        for(Waiter w : waiters)
	        {
	        	((MQHostRole)host).addWaiter(w);
	        	((MQWaiterRole)w).setHost(host);
	        }
        }
        for(Cook c : cooks)
        {
        	((MQCookRole)c).setHost(host);
        }
        
        person.msgAssignRole((MQHostRole)host);
    }
    
    public void addCook(PersonAgent person)
    {
    	MQCookRole c = new MQCookRole(person, this, cookOrders, market, cashier, timer);
		cooks.add(c);
		person.msgAssignRole(c);
    }
    
    public void addCashier(PersonAgent person)
    {
    	cashier = new MQCashierRole(person, this);
    	person.msgAssignRole((MQCashierRole)cashier);
    	synchronized(waiters)
    	{
	    	for(Waiter w : waiters)
	    	{
	    		((MQWaiterRole)w).setCashier(cashier);
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
	    		if(c instanceof MQCustomerRole && ((MQCustomerRole)c).isActive())
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
	    		if(((MQWaiterRole)w).isActive())
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
	    		if(((MQCookRole)c).isActive())
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
    
    //Hacks to demonstrate program
    /*public void OutOfFoodHack()
    {
    	cooks.get(0).OutOfFoodHack();
    	for(MarketAgent m : markets)
    	{
    		m.OutOfFoodHack();
    	}
    }
    
    public void OutOfSaladHack()
    {
    	cooks.get(0).OutOfSaladHack();
    	for(MarketAgent m : markets)
    	{
    		m.OutOfFoodHack();
    	}
<<<<<<< HEAD
    }*/
    

	public boolean isOpen() {
		return gui.isOpen();
	}
    
}
