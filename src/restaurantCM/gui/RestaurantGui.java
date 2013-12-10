package restaurantCM.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurantCM.CustomerRole;
import restaurantCM.WaiterAgent;
import agent.Agent;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
	/* The GUI has two frames, the control frame (in variable gui) 
	 * and the animation frame, (in variable animationFrame within gui)
	 */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	ArrayList<Agent> myAgents = new ArrayList<Agent>();

	/* restPanel holds 2 panels
	 * 1) the staff listing, menu, and lists of current customers all constructed
	 *    in RestaurantPanel()
	 * 2) the infoPanel about the clicked Customer (created just below)
	 */    
	RestaurantPanel restPanel = new RestaurantPanel(this);

	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel infoPanel;
	//  private JPanel chrisPanel;
	private JLabel infoLabel; //part of infoPanel
	private JCheckBox stateCB;//part of infoLabel
	private Object currentPerson;
	private JButton pause;/* Holds the agent that the info is about.
    								Seems like a hack */

	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public RestaurantGui() {
		int WINDOWX = 1000;
		int WINDOWY = 550;
		int notSureWhatThisIsFor = 50;
		Dimension animationDim = new Dimension((int)(WINDOWX*.3), (int) (WINDOWY));
		//        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//        animationFrame.setBounds(100+WINDOWX, notSureWhatThisIsFor , WINDOWX+100, WINDOWY+100);
		//        animationFrame.setVisible(true);
		//animationFrame.add(animationPanel); 
		add(animationPanel, BorderLayout.WEST);
		setBounds(notSureWhatThisIsFor, notSureWhatThisIsFor, WINDOWX, WINDOWY);

		setLayout(new BorderLayout(5,5));

		Dimension restDim = new Dimension((int)(WINDOWX*.5), (int) (WINDOWY * .6));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		add(restPanel ,BorderLayout.EAST);

		// Now, setup the info panel
		Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
		infoPanel = new JPanel();
		// chrisPanel = new JPanel();

		infoPanel.setPreferredSize(infoDim);
		infoPanel.setMinimumSize(infoDim);
		infoPanel.setMaximumSize(infoDim);
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
		pause = new JButton("pause");
		infoPanel.setLayout(new GridLayout());
		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);
		pause.addActionListener(this);
		//  infoPanel.setLayout(new GridLayout(1, 2, 30, 0));

		infoLabel = new JLabel(); 
		infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
		infoPanel.add(infoLabel);
		infoPanel.add(stateCB);
		infoPanel.add(pause);
		add(infoPanel, BorderLayout.SOUTH);
		//   chrisPanel.setLayout(new BoxLayout(chrisPanel, BoxLayout.Y_AXIS));
		// chrisPanel.add(chrisLabel);
		// chrisPanel.add(new JButton("chrisLabel"));
		// add(chrisPanel, BorderLayout.SOUTH);
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

		if (person instanceof CustomerRole) {
			CustomerRole customer = (CustomerRole) person;
			stateCB.setText("Hungry?");
			//Should checkmark be there? 
			stateCB.setSelected(customer.getGui().isHungry());
			//Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.getGui().isHungry());
			// Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + " </pre></html>");
		}
		if( person instanceof WaiterAgent){
			WaiterAgent w = (WaiterAgent) person;
			stateCB.setText("Go On Break?");
			stateCB.setSelected(w.isOnBreak());
			stateCB.setEnabled(!w.isOnBreak());
			infoLabel.setText("Name: "+w.getName()+ "\n");
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
			if (currentPerson instanceof CustomerRole) {
				CustomerRole c = (CustomerRole) currentPerson;
				c.getGui().setHungry();
				stateCB.setEnabled(false);
			}
			if (currentPerson instanceof WaiterAgent) {
				WaiterAgent w = (WaiterAgent) currentPerson;
				w.msgGoOnBreak();
				stateCB.setEnabled(false);
			}
		}
//		if (e.getSource() == pause){
//			Agent.ispaused = !Agent.ispaused;
//		}
	}
/**
 * Message sent from a customer gui to enable that customer's
 * "I'm hungry" checkbox.
 *unity
 * @param c reference to the customer
 */
public void setCustomerEnabled(CustomerRole c) {
	if (currentPerson instanceof CustomerRole) {
		CustomerRole cust = (CustomerRole) currentPerson;
		if (c.equals(cust)) {
			stateCB.setEnabled(true);
			stateCB.setSelected(false);
		}
	}
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
