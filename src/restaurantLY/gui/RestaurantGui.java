package restaurantLY.gui;

import restaurantKC.KCWaiterRole;
import restaurantLY.LYWaiterRole;
import restaurantLY.interfaces.*;
import restaurantLY.gui.RestaurantPanel;
import restaurantLY.interfaces.Customer;
import market.Market;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	/* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	public LYRestaurantBuilding building;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	private RestaurantPanel restPanel;
	
	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel infoPanel;
	private JLabel infoLabel; //part of infoPanel
	private JCheckBox stateCB;//part of infoLabel
	
	private Object currentPerson;/* Holds the agent that the info is about.
									Seems like a hack */
	
	//private JButton pause = new JButton("Pause");
	//private JButton restart = new JButton("Restart");
	
	private Vector<RestaurantCustomer> customers = new Vector<RestaurantCustomer>();
	
	/**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
	public RestaurantGui(Market market, LYRestaurantBuilding b){
		int WINDOWX = 1050;
		int WINDOWY = 450;
		int xPos = 50;
		int yPos = 10;
		int WIDTH = 973;
		int HEIGHT = 538;
		int panelX = 1;
		int panelY = 1;
		int panelW = 970;
		int panelH = 513;
		int infoX = 1;
		int infoY = 518;
		int infoW = 535;
		int infoH = 115;
		int buttonX = 650;
		int buttonY = 573;
		int buttonW = 100;
		int buttonH = 30;
		
		restPanel = new RestaurantPanel(this, market);
		building = b;
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(false);
		setBounds(xPos, yPos, WIDTH, HEIGHT);

		setLayout(null);

		Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		restPanel.setBounds(panelX, panelY, panelW, panelH);
		add(restPanel);
		
		// Now, setup the info panel
		/*Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(infoDim);
		infoPanel.setMinimumSize(infoDim);
		infoPanel.setMaximumSize(infoDim);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);
		
		infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
		
		infoLabel = new JLabel();
		infoLabel.setText("<html><pre><i>Click Add for waiters/customers</i></pre></html>");
		infoPanel.add(infoLabel);
		infoPanel.add(stateCB);
		infoPanel.setBounds(infoX, infoY, infoW, infoH);
		add(infoPanel);
		
		pause.addActionListener(this);
        restart.addActionListener(this);
		pause.setBounds(buttonX, buttonY, buttonW, buttonH);
		restart.setBounds(buttonX+115, buttonY, buttonW, buttonH);
		add(pause);
		add(restart);*/
		
		animationPanel.setVisible(true);
		animationPanel.setBounds(541, 1, 430, 512);
		animationPanel.setBorder(BorderFactory.createTitledBorder(""));
		add(animationPanel);
	}
	/**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
	public void updateInfoPanel(Object person) {
		/*stateCB.setVisible(true);
		currentPerson = person;
		
		if (person instanceof Customer) {
			Customer customer = (Customer) person;
			stateCB.setText("Hungry?");
		  //Should checkmark be there? 
			stateCB.setSelected(customer.isHungry());
		  //Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.isHungry());
		  // Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
		}
		else if (person instanceof Waiter) {
			Waiter waiter = (Waiter) person;
			stateCB.setText("On Break?");
		  //Should checkmark be there?
			stateCB.setSelected(waiter.isOnBreak());
			stateCB.setEnabled(true);
			infoLabel.setText(
					"<html><pre>     Name: " + waiter.getName() + " </pre></html>");
		}
		infoPanel.validate();*/
	}
	/**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
	public void actionPerformed(ActionEvent e) {
		/*if (e.getSource() == stateCB) {
			if (currentPerson instanceof Customer) {
				Customer c = (Customer) currentPerson;
				c.getGui().setHungry();
				c.gotHungry();
				stateCB.setEnabled(false);
			}
			else if (currentPerson instanceof LYWaiterRole) {
				LYWaiterRole w = (LYWaiterRole) currentPerson;
				w.getGui().setOnBreak();
				w.setOnBreak(stateCB.isSelected());
			}
		}
		if (e.getSource() == pause) {
        	pause();
        }
        if (e.getSource() == restart) {
        	restart();
        }*/
        for(RestaurantCustomer c : customers)
    	{
    		if(e.getSource() == c.hungerCB)
    		{
    			c.customer.getGui().setHungry();
    			c.hungerCB.setEnabled(false);
    		}
    	}
	}
	
	/*public void pause() {
    	System.out.print("PAUSE\n");
    	restPanel.setPause();
    }
	
	public void restart() {
    	System.out.println("RESTART");
    	restPanel.setRestart();
    }*/
	
	/**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
	public void setCustomerEnabled(Customer c) {
		/*if (currentPerson instanceof Customer) {
			Customer cust = (Customer) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
		}*/
        for(RestaurantCustomer rc : customers)
    	{
    		if(c.equals(rc.customer))
    		{
    			rc.hungerCB.setEnabled(true);
    			rc.hungerCB.setSelected(false);
    		}
    	}
	}

	/*public void setstateCB(boolean state) {
		stateCB.setSelected(state);
	}*/
	
	public RestaurantPanel getRestaurantPanel()
    {
    	return restPanel;
    }
    
    public void addRestaurantCustomer(Customer c, JCheckBox hungerCB)
    {
    	customers.add(new RestaurantCustomer(c, hungerCB));
    }
	
	/**
     * Main routine to get gui started
     */
	/*public static void main(String[] args) {
		RestaurantGui gui = new RestaurantGui();
		gui.setTitle("csci201 Restaurant");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
	
	public void setOpen(Boolean b) {
		building.setOpen(b);
		
	}
	public boolean isOpen() {
		return building.isOpen();
	}
}
