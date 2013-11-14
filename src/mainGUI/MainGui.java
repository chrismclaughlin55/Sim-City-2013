package mainGUI;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurantMQ.CustomerAgent;
import restaurantMQ.WaiterAgent;
import restaurantMQ.gui.AnimationPanel;
import restaurantMQ.gui.RestaurantGui;
import restaurantMQ.gui.RestaurantPanel;
import restaurantMQ.interfaces.Customer;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MainGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	
	//public AnimationPanel animationPanel;
	//JPanel InfoLayout;
	

    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private PersonCreationPanel personPanel;
    private MainAnimationPanel mainAnimationPanel;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JPanel InfoLayout; 
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public MainGui() {
    	    	
    	//1. create person
    	

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainAnimationPanel = new MainAnimationPanel();
        mainAnimationPanel.setVisible(true);
        personPanel = new PersonCreationPanel("person");
        personPanel.setVisible(true);
    	
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setBounds(0,0,screenSize.width-200, screenSize.height-100);
    	setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);

       

        /*Dimension restDim = new Dimension(screenSize.width/2, (int) (screenSize.height * .75));
        restPanel = new RestaurantPanel(this);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);*/
        
        // Now, setup the info panel
        /*Dimension infoDim = new Dimension(screenSize.width/2, (int) (screenSize.height * .22));
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
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);*/
        
        
        //InfoLayout.add(infoPanel);
        
        setLayout(new GridLayout(0,2));
        
        
        //Dimension dim = new Dimension (screenSize.width/2, screenSize.height);
        //InfoLayout.setPreferredSize(dim);
        add (personPanel);
        //animationPanel.setPreferredSize(dim);
        add (mainAnimationPanel);
       // System.out.println (animationPanel.getPreferredSize());
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    /*public void updateInfoPanel(Object person) {
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
            infoPanel.validate();
        }
        
        if (person instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) person;
            stateCB.setText("Break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.getGui().IsOnBreak());
            stateCB.setEnabled(!waiter.getGui().IsOnBreak());
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
            infoPanel.validate();
        }
        infoPanel.validate();
    }*/
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
   /* public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof WaiterAgent) {
                WaiterAgent w = (WaiterAgent) currentPerson;
                w.getGui().setBreak();
                stateCB.setEnabled(false);
            }
        }
    }*/
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param agent reference to the customer
     */
    public void setCustomerEnabled(Customer agent) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (agent.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(WaiterAgent w) {
        if (currentPerson instanceof WaiterAgent) {
            WaiterAgent waiter = (WaiterAgent) currentPerson;
            if (w.equals(waiter)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
    	
    	RestaurantGui restGui = new RestaurantGui();
        restGui.setTitle("csci201 Restaurant");
        restGui.setVisible(true);
        restGui.setResizable(false);
        restGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        restGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        MainGui gui = new MainGui();
        gui.setTitle("Sim City - Team 15");
        gui.setVisible(true);
        gui.setResizable(true);        

        
       
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
