package restaurantMQ.gui;

import restaurantMQ.CashierAgent;
import restaurantMQ.CookAgent;
import restaurantMQ.CustomerAgent;
import restaurantMQ.HostAgent;
import restaurantMQ.MarketAgent;
import restaurantMQ.Menu;
import restaurantMQ.WaiterAgent;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Waiter;

import javax.swing.*;

import agent.Agent;

import java.awt.*;
import java.awt.event.*;
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
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);

    private List<Customer> customers = new ArrayList<Customer>();
    private List<Waiter> waiters = new ArrayList<Waiter>();
    private List<CookAgent> cooks = new ArrayList<CookAgent>();
    private List<MarketAgent> markets = new ArrayList<MarketAgent>();
    private Cashier cashier = new CashierAgent();
    //private Vector<HungerListener> hungerListeners = new Vector<HungerListener>();

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
        host.setGui(hostGui);

        gui.animationPanel.addGui(hostGui);
        
        cashier.startThread();
        
        for(int i = 0; i < NMARKETS; ++i)
        {
        	markets.add(new MarketAgent(("Market" + (i+1)), timer, i));
        	markets.get(i).startThread();
        }
        
        for(int i = 0; i < NCOOKS; ++i)
		{
			cooks.add(new CookAgent(markets, cashier, timer));
			cooks.get(i).startThread();
		}
        
        host.setCooks(cooks);
        
        host.setWaiters(waiters);
        host.startThread();

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
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
    		for(CookAgent c : cooks)
    		{
    			c.msgPause();
    		}
    		host.msgPause();
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
    		for(CookAgent c : cooks)
    		{
    			c.msgPause();
    		}
    		host.msgPause();
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
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name, timer, this);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    }
    
    public void addPerson(String type, String name, boolean hungry) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name, timer, this);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		if(hungry)
    			c.getGui().setHungry();
    		customers.add(c);
    		c.startThread();
    	}
    }
    
    //This is the one which is used!!!!
    public void addPerson(String type, String name, JCheckBox hungry, boolean hunger) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name, timer, this);	
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		hungry.addActionListener(gui);
    		gui.addRestaurantCustomer(c, hungry);
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		if(hunger)
    			hungry.doClick();
    		customers.add(c);
    		c.startThread();
    	}
    }
    
    public void addWaiter(String name, final JCheckBox breakBox)
    {
    	WaiterAgent waiter = new WaiterAgent(name, waiters.size(), host, cooks, cashier, new Menu(menu), breakBox);
    	waiters.add(waiter);
    	host.addWaiter(waiter);
    	for(CookAgent c : cooks)
    	{
    		c.addWaiter(waiter);
    	}
    	
    	final Waiter w = waiter;
    	breakBoxes.add(breakBox);
    	breakBox.addActionListener(new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				if(breakBox.getText().equals("Want Break"))
    				{
    					breakBox.setEnabled(true);
    					w.msgWantBreak();
    				}
    				else if(breakBox.getText().equals("Back to Work"))
    				{
    					breakBox.setEnabled(true);
    					w.msgBackFromBreak();
    				}
    			}
    		});
    	
		WaiterGui waiterGui = new WaiterGui(waiter);
		waiter.setGui(waiterGui);
		gui.animationPanel.addGui(waiterGui);
		waiter.startThread();
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
