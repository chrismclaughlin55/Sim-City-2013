package restaurantMQ.gui;

import restaurantMQ.CookOrder;
import restaurantMQ.MQCashierRole;
import restaurantMQ.MQCookRole;
import restaurantMQ.MQCustomerRole;
import restaurantMQ.MQHostRole;
import restaurantMQ.MQWaiterRole;
import restaurantMQ.MarketAgent;
import restaurantMQ.Menu;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Cook;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;

import javax.swing.*;

import city.PersonAgent;
import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;

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
	
    //Host, cook, waiters and customers
	private List<Host> hosts = new ArrayList<Host>();
    private Host host;

    private List<Customer> customers = new ArrayList<Customer>();
    private List<Waiter> waiters = new ArrayList<Waiter>();
    private List<Cook> cooks = new ArrayList<Cook>();
    private List<MarketAgent> markets = new ArrayList<MarketAgent>();
    private List<Cashier> cashiers = new ArrayList<Cashier>();
    private Cashier cashier;
    //private Vector<HungerListener> hungerListeners = new Vector<HungerListener>();
    
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

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        //Cashier instantiation (hack)
        PersonAgent p1 = new PersonAgent("Cashier");
        cashier = new MQCashierRole(p1);
        cashiers.add(cashier);
        p1.msgAssignRole((MQCashierRole)cashier);
        p1.startThread();
        
        //Market instantiation (hack)
        for(int i = 0; i < NMARKETS; ++i)
        {
        	markets.add(new MarketAgent(("Market" + (i+1)), timer, i));
        	markets.get(i).startThread();
        }
        
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
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + ((MQHostRole)host).getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
    
    //This is the one which is used!!!!
    public void addPerson(String type, String name, JCheckBox hungry, boolean hunger) {

    	//THIS WILL BE CALLED BY THE PERSON AGENT
    	if (type.equals("Customers"))
    	{
    		PersonAgent p = new PersonAgent(name);
    		MQCustomerRole c = new MQCustomerRole(p, timer, this);	
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
    
    public void addWaiter(String name, final JCheckBox breakBox)
    {
    	PersonAgent p = new PersonAgent(name);
    	MQWaiterRole w = new MQWaiterRole(p, waiters.size(), host, cooks, cookOrders, cashier, new Menu(menu), breakBox);
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
    
    //Hacks to demonstrate program
    public void OutOfFoodHack()
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
    }
}
