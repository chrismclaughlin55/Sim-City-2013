package restaurantMQ.gui;

import restaurantMQ.interfaces.Customer;

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
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	JPanel mainPanel = new JPanel();
	AnimationPanel animationPanel = new AnimationPanel();
	MQRestaurantBuilding building;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    /*private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */
    
    public static final int WINDOWX = 1000;
    public static final int WINDOWY = 550;
    private static final int MARGIN = 100;
    private static final int WIDTH = 50;
    private static final double RESIZE1 = 0.6;
    private static final double RESIZE2 = 0.25;
    private static final int GRIDROWS = 1;
    private static final int GRIDCOLS = 2;
    private static final int GRIDHGAP = 30;
    private static final int GRIDVGAP = 0;
    
    private Vector<RestaurantCustomer> customers = new Vector<RestaurantCustomer>();

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui(MQRestaurantBuilding b) {

    	building = b;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(WIDTH, WIDTH , WINDOWX, WINDOWY);
        setVisible(false);
    	mainPanel.setLayout(new GridLayout(1, 2));
    	
    	//setBounds(WIDTH, WIDTH, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * RESIZE1));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        mainPanel.add(restPanel);
        
        mainPanel.add(animationPanel); 
        
        add(mainPanel);
        
        /*// Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * RESIZE2));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(GRIDROWS, GRIDCOLS, GRIDHGAP, GRIDVGAP));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        //add(infoPanel);*/
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
    	/*
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
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
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
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
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(Customer c) {
        /*if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
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
