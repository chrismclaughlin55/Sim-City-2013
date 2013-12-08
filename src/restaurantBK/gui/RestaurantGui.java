package restaurantBK.gui;

import restaurantBK.CustomerAgent;
import restaurantBK.WaiterAgent;
import restaurantBK.interfaces.Customer;
import restaurantBK.interfaces.Waiter;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame();
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JPanel idPanel; //addition for Lab 1 with my information and graphic
    private JLabel idLabel;
    //private JPanel groupit = new JPanel();
    private Object currentPerson;/* Holds the agent that the info is about.
    			Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     * @throws IOException 
     */
    public RestaurantGui() throws IOException {
        int WINDOWX = 550;
        int WINDOWY = 350;        

        ///animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel);
        
    	
    	setBounds(50, 50, 2*WINDOWX+10, 2*WINDOWY);

        //setLayout(new BoxLayout((Container) getContentPane(), 
        //		BoxLayout.Y_AXIS));arg
        setLayout(new BorderLayout());
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        //groupit.setLayout(new GridLayout(1,2,10,10));
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        infoPanel.setLayout(new GridLayout(1, 2, 10, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click cust/wait.</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        restPanel.add(infoPanel);
       
        Dimension idDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        idPanel = new JPanel();
        idPanel.setPreferredSize(idDim);
        idPanel.setMinimumSize(idDim);
        idPanel.setMaximumSize(idDim);
        idPanel.setBorder(BorderFactory.createTitledBorder("Meet the Maker"));
        idPanel.setLayout(new FlowLayout());
        
        //
        idLabel = new JLabel();
        idLabel.setText("Brian Kim, CECS, Class of 2016");
        idPanel.add(idLabel);
        restPanel.add(idPanel);
        //BufferedImage myPicture = ImageIO.read(new File("/home/brianykim/java-eclipse/restaurant_brianyki/79b94730f0f0b2582ddbcb6cf4da6b51.png"));
        //JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        //idPanel.add(picLabel);
        add(restPanel,BorderLayout.WEST);
        add(animationPanel,BorderLayout.CENTER);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            Customer customer = (Customer) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if(person instanceof WaiterAgent) {
        	Waiter waiter = (Waiter) person;
        	stateCB.setText("Break?");
        	stateCB.setSelected(waiter.wantsBreak());
        	stateCB.setEnabled(!waiter.waitingForResponse());
        	infoLabel.setText( "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                Customer c = (Customer) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if(e.getSource()==stateCB) {
        	if(currentPerson instanceof WaiterAgent) {
        		Waiter w = (Waiter) currentPerson;
        		if(!stateCB.isSelected()) {
        			w.msgGoBackToWork();
        		}
        		else {
        			w.getGui().setWantsBreak();
        			stateCB.setEnabled(false);
        		}
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
        if (currentPerson instanceof CustomerAgent) {
            Customer cust = (Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    public void setWaiterEnabled(Waiter w,boolean select) {
    	if(currentPerson instanceof WaiterAgent) {
    		Waiter wait = (Waiter) currentPerson;
    		if(w.equals(wait)) {
    			stateCB.setEnabled(true);
    			stateCB.setSelected(select);
    		}
    	}
    }
    /**
     * Main routine to get gui started
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
