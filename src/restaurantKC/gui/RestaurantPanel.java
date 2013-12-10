package restaurantKC.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import market.Market;
import restaurantKC.KCCashierRole;
import restaurantKC.KCCookRole;
import restaurantKC.KCCustomerRole;
import restaurantKC.KCHostRole;
import restaurantKC.KCWaiterRole;
import restaurantKC.pcCookOrder;
import restaurantKC.interfaces.Cashier;
import restaurantKC.interfaces.Cook;
import restaurantKC.interfaces.Customer;
import restaurantKC.interfaces.Host;
import restaurantKC.interfaces.Waiter;
import city.PersonAgent;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {


	public static final int CLOSINGTIME = 20;


	boolean isPaused = false;
	public RestaurantGui gui; //reference to main gui


	private List<pcCookOrder> cookOrders = Collections.synchronizedList(new ArrayList<pcCookOrder>());

	public Cook cook = null; 
	public Host host = null; 
	public Cashier cashier = null;

	/*private Cook cook = new KCCookRole("Sarah", cookOrders, null); 
	private CookGui cookGui = new CookGui(cook);
	private CashierAgent cashier = new CashierAgent("Cashier");*/

	/*private MarketAgent market1 = new MarketAgent("Market 1", 2, 10, 10, 10, cook, cashier);
	private MarketAgent market2 = new MarketAgent("Market 2", 0, 0, 0, 0, cook, cashier);
	private MarketAgent market3 = new MarketAgent("Market 3", 7, 15, 21, 11, cook, cashier);*/
	private List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());
	private List<Waiter> waiters = Collections.synchronizedList(new ArrayList<Waiter>());
	private List<Market> markets = new ArrayList<Market>();

	int custnum = -1;
	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private WaiterPanel waiterPanel = new WaiterPanel(this, "Waiters");
	private JPanel group = new JPanel();
	private JButton b1;
	private JButton b2;
	private int MaxRestLabelX = 1000;
	private int MaxRestLabelY = 350;

	private JPanel buttonPanel;





	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;
		/*host.setGui(hostGui);


		gui.animationPanel.addGui(hostGui);
		//host.startThread();

		markets.add(market1);
		markets.add(market2);
		markets.add(market3);
		cook.addMarkets(markets);

		gui.animationPanel.addGui(cookGui);
		cook.setGui(cookGui);
		cookGui.setAnimationPanel(gui.animationPanel);
		//cook.startThread();

		cashier.startThread();*/

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

		group.add(customerPanel);
		TitledBorder title = BorderFactory.createTitledBorder("Customers");
		title.setTitleJustification(TitledBorder.CENTER);
		customerPanel.setBorder(title);
		group.add(Box.createRigidArea(new Dimension(0, 25)));
		group.add(waiterPanel);
		TitledBorder title2 = BorderFactory.createTitledBorder("Waiters");
		title2.setTitleJustification(TitledBorder.CENTER);
		waiterPanel.setBorder(title2);



		initRestLabel();
		//restLabel.setPreferredSize(new Dimension (190, 100));
		restLabel.setMaximumSize(new Dimension(MaxRestLabelX, MaxRestLabelY));
		add(restLabel/*, BorderLayout.NORTH*/);
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(group/*, BorderLayout.CENTER*/);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));

		b1 = new JButton("Pause");
		b2 = new JButton("Drain");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(b1);
		buttonPanel.add(b2);
		restLabel.add(buttonPanel, BorderLayout.CENTER);
		b1.addActionListener(this);
		b2.addActionListener(this);


	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 */
	/**
	 * Adds a customer or waiter to the appropriate list
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	/*public void addPerson(String type, String name) {

		if (type.equals("Customers")) {
			PersonAgent p = new PersonAgent(name);
    		Customer c = new KCCustomerRole(p);	
    		CustomerGui g = new CustomerGui(c, gui, 0);
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);

    		p.msgAssignRole((KCCustomerRole)c);
    		p.startThread(); //Hack. PersonAgent's thread should already be going
		}

		if (type.equals("Waiters")) {
			System.out.println ("adding waiter");
			WaiterAgent w = new WaiterAgent(name, cookOrders);	
			WaiterGui g = new WaiterGui(w, this);
			waiters.add(w);
			w.setGui(g);
			w.setCook(cook);
			w.setHost(host);
			w.setCashier(cashier);
			host.msgWaiterReporting(w);
			g.setAnimationPanel(gui.animationPanel);
			gui.animationPanel.addGui(g);
			w.startThread();
		}
	}*/



	public void markHungry(String name)
	{
		for (int i = 0; i < customers.size(); i++)
		{
			Customer temp = customers.get(i);
			if (temp.getName() == name)
			{
				temp.getGui().setHungry();
			}
		}	
	}

	public void markBreak(String name)
	{
		for (int i = 0; i < waiters.size(); i++)
		{
			Waiter temp = (KCWaiterRole) waiters.get(i);
			if (temp.getName() == name)
			{
				temp.getGui().setBreak();
			}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// pause each customer, waiter, host, and cook
		if (e.getSource() == b2) {
			cook.drainInventory();
		}

	}

	public void addHost(PersonAgent person)
	{
		host = new KCHostRole(person, this);
		HostGui hostGui = new HostGui(host);
		gui.animationPanel.addGui(hostGui);
		host.setGui(hostGui);
		//((KCHostRole)host).setCooks(cooks);

		//update other agents
		synchronized(waiters)
		{
			for(Waiter w : waiters)
			{
				((KCHostRole)host).msgWaiterReporting(w);;
				((KCWaiterRole)w).setHost(host);
			}
		}
		person.msgAssignRole((KCHostRole)host);
	}

	public void addCustomer(PersonAgent person) {
		System.out.println("Adding Customer");
		Customer c = new KCCustomerRole(person);	
		CustomerGui g = new CustomerGui(c, gui);
		c.setGui(g);
		gui.animationPanel.addGui(g);
		customers.add(c);
		c.setGui(g);
		c.setHost(host);
		c.setCashier(cashier);
		markHungry(person.getName());
		person.msgAssignRole((KCCustomerRole)c);
		//c.msgGotHungry();
		//person.startThread(); //Hack. PersonAgent's thread should already be going
	}

	/*c.setHost(host);
	c.setCashier(cashier);*/

	public void addWaiter(PersonAgent person)
	{
		System.out.println ("Adding Waiter");
		Waiter w = new KCWaiterRole(cookOrders, person, this);	
		waiters.add(w);
		int waiterNum = waiters.indexOf(w);
		WaiterGui g = new WaiterGui(w, this, waiterNum);
		w.setGui(g);
		w.setHost(host);
		w.setCook(cook);
		w.setCashier(cashier);
		host.msgWaiterReporting(w);
		g.setAnimationPanel(gui.animationPanel);
		gui.animationPanel.addGui(g);

		//w.startThread();
		//Start the thread

		person.msgAssignRole((KCWaiterRole)w);
	}
	// SET COOK & Cashier

	public void addCashier(PersonAgent person) {
		cashier = new KCCashierRole(person, this);
		person.msgAssignRole((KCCashierRole)cashier);
		synchronized(waiters) {
			for(Waiter w : waiters) {
				((KCWaiterRole)w).setCashier(cashier);
			}
			for(Customer c : customers) {
				((KCCustomerRole)c).setCashier(cashier);
			}
		}
	}
	//public KCCookRole(List<pcCookOrder> cookOrders, PersonAgent p) {

	public void addCook(PersonAgent person) {
		cook = new KCCookRole(cookOrders, person, this);
		CookGui cookGui = new CookGui(cook);
		cook.setGui(cookGui);
		gui.animationPanel.addGui(cookGui);
		cookGui.setAnimationPanel(gui.animationPanel);
		person.msgAssignRole((KCCookRole)cook);

		for(Waiter w : waiters) {
			((KCWaiterRole)w).setCook(cook);
		}
	}



	public int activeCustomers() {
		int count = 0;
		synchronized(customers) {
			for(Customer c : customers) {
				if(c instanceof KCCustomerRole && ((KCCustomerRole)c).isActive()) {
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
			for(Waiter w : waiters) {
				if(((KCWaiterRole)w).isActive()) {
					++count;
				}
			}
		}
		return count;
	}

	public int activeCooks()
	{
		if (cook != null) {
			if(((KCCookRole)cook).isActive())
				return 1;
			else 
				return 0;
		}
		else
			return 0;
	}


	public boolean fullyStaffed() {
		return (activeCooks() > 0) && (activeWaiters() > 0) && (host != null) && (cashier != null);
	}

	public boolean justHost() {
		return (activeCooks() == 0) && (activeWaiters() == 0) && 
				(activeCustomers() == 0) && (cashier == null);
	}

	public boolean justCashier() {
		return (activeCooks() == 0) && (activeWaiters() == 0) && 
				(activeCustomers() == 0) && (cashier != null);
	}

	public void hostLeaving() {
		host = null;
	}

	public void cashierLeaving() {
		cashier = null;
	}

	public boolean hasHost() {
		return host != null;
	}

	public boolean hasCashier() {
		return cashier != null;
	}

	public boolean isOpen() {
		return gui.isOpen();
	}

	public void setOpen(Boolean b) {
		gui.setOpen(b);
	}

}
