package restaurantKC.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurantKC.CustomerAgent;
import restaurantKC.WaiterAgent;
import restaurantKC.interfaces.Customer;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	/* The GUI has two frames, the control frame (in variable gui) 
	 * and the animation frame, (in variable animationFrame within gui)
	 */

	public AnimationPanel animationPanel;
	//JPanel InfoLayout;

	public static final int WINDOWX = 1000;
	public static final int WINDOWY = 550;
	private static final int MARGIN = 100;
	private static final int WIDTH = 50;
	private static final double RESIZE1 = 0.6;
	private static final double RESIZE2 = 0.25;

	/* restPanel holds 2 panels
	 * 1) the staff listing, menu, and lists of current customers all constructed
	 *    in RestaurantPanel()
	 * 2) the infoPanel about the clicked Customer (created just below)
	 */    
	public RestaurantPanel restPanel;

	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel infoPanel;
	private JPanel myPanel;
	private JLabel infoLabel; //part of infoPanel
	private JLabel myLabel;
	private JCheckBox stateCB;//part of infoLabel
	private JPanel InfoLayout; 

	private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public RestaurantGui() {

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		animationPanel = new AnimationPanel();
		animationPanel.setVisible(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(WIDTH, WIDTH , WINDOWX, WINDOWY);
		setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);

    	restPanel = new RestaurantPanel(this);


		Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * RESIZE1));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);

		
		add(restPanel);

		setLayout(new GridLayout(0,2));


		//Dimension dim = new Dimension (screenSize.width/2, screenSize.height);
		//InfoLayout.setPreferredSize(dim);
		//animationPanel.setPreferredSize(dim);
		add (animationPanel);
		// System.out.println (animationPanel.getPreferredSize());
	}
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param person customer (or waiter) object
	 */
	
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
	}
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
	/*public static void main(String[] args) {
		RestaurantGui gui = new RestaurantGui();
		gui.setTitle("Rami's Restaurant - Kartik");
		gui.setVisible(true);
		gui.setResizable(true);        

		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/
}
