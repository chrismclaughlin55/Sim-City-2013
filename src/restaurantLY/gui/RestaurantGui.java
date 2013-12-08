package restaurantLY.gui;

import restaurantLY.*;

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
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	private RestaurantPanel restPanel = new RestaurantPanel(this);
	
	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel infoPanel;
	private JLabel infoLabel; //part of infoPanel
	private JCheckBox stateCB;//part of infoLabel
	
	private Object currentPerson;/* Holds the agent that the info is about.
									Seems like a hack */
	
	private JButton pause = new JButton("Pause");
	private JButton restart = new JButton("Restart");
	
	/**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
	public RestaurantGui(){
		int WINDOWX = 1050;
		int WINDOWY = 450;
		int xPos = 50;
		int yPos = 10;
		int WIDTH = 973;
		int HEIGHT = 655;
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
		
		setBounds(xPos, yPos, WIDTH, HEIGHT);

		setLayout(null);

		Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		restPanel.setBounds(panelX, panelY, panelW, panelH);
		add(restPanel);
		
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
		add(restart);
		
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
		stateCB.setVisible(true);
		currentPerson = person;
		
		if (person instanceof CustomerAgent) {
			CustomerAgent customer = (CustomerAgent) person;
			stateCB.setText("Hungry?");
		  //Should checkmark be there? 
			stateCB.setSelected(customer.isHungry());
		  //Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.isHungry());
		  // Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
		}
		else if (person instanceof WaiterAgent) {
			WaiterAgent waiter = (WaiterAgent) person;
			stateCB.setText("On Break?");
		  //Should checkmark be there?
			stateCB.setSelected(waiter.isOnBreak());
			stateCB.setEnabled(true);
			infoLabel.setText(
					"<html><pre>     Name: " + waiter.getName() + " </pre></html>");
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
				CustomerAgent c = (CustomerAgent) currentPerson;
				c.getGui().setHungry();
				c.gotHungry();
				stateCB.setEnabled(false);
			}
			else if (currentPerson instanceof WaiterAgent) {
				WaiterAgent w = (WaiterAgent) currentPerson;
				//w.getGui().setOnBreak();
				w.setOnBreak(stateCB.isSelected());
			}
		}
		if (e.getSource() == pause) {
        	pause();
        }
        if (e.getSource() == restart) {
        	restart();
        }
	}
	
	public void pause() {
    	//System.out.print("PAUSE\n");
    	//restPanel.setPause();
    }
	
	public void restart() {
    	//System.out.println("RESTART");
    	//restPanel.setRestart();
    }
	
	/**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
	public void setCustomerEnabled(CustomerAgent c) {
		if (currentPerson instanceof CustomerAgent) {
			CustomerAgent cust = (CustomerAgent) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
		}
	}

	public void setstateCB(boolean state) {
		stateCB.setSelected(state);
	}
	
	/**
     * Main routine to get gui started
     */
	public static void main(String[] args) {
		RestaurantGui gui = new RestaurantGui();
		gui.setTitle("csci201 Restaurant");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
