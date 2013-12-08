package restaurantLY.gui;

import javax.swing.*;

import restaurantLY.CustomerAgent;
import restaurantLY.WaiterAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

	public JScrollPane pane = 
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private Vector<JButton> list = new Vector<JButton>();
	private JButton addPersonB = new JButton("Add");
	
	private RestaurantPanel restPanel;
	private String type;
	
	private JLabel typeLabel = new JLabel();
	private JLabel myLabel = new JLabel();
	private JCheckBox myStateCB = new JCheckBox();
	private JTextField myTF = new JTextField();
	public boolean customerBool = false;
	private Object currentPerson;
    private RestaurantGui gui;
	
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
	public ListPanel(RestaurantPanel rp, String type) {
		restPanel = rp;
		this.type = type;

		setLayout(null);
		
		typeLabel = new JLabel("<html><pre> <u>"+type+ "</u><br></pre></html>");
		typeLabel.setBounds(10, 1, 100, 30);
		add(typeLabel);

		myLabel = new JLabel();
        myLabel.setText("Name:");
		myLabel.setBounds(10, 35, 50, 30);
        add(myLabel);
		myTF = new JTextField(5);
		myTF.setBounds(55, 35, 80, 30);
        add(myTF);
        
        myStateCB = new JCheckBox();
        myStateCB.setVisible(false);
        myStateCB.addActionListener(this);
        myStateCB.setVisible(true);
        myStateCB.setText("Hungry?");
        myStateCB.setSelected(false);
        myStateCB.setEnabled(true);
        myStateCB.setBounds(155, 3, 100, 35);
        add(myStateCB);
		
		addPersonB.addActionListener(this);
		addPersonB.setBounds(160, 35, 80, 30);
		add(addPersonB);
		
		if (type.equals("Waiters")) {
			myStateCB.setVisible(false);
		}
		
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		pane.setBounds(1, 75, 255, 217);
		add(pane);
	}
	
	/**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == myStateCB) {
    		customerBool = myStateCB.isSelected();
    	}
		if(e.getSource() == addPersonB) {
			String input = myTF.getText();
			addPerson(input, type);
		}
		else {
			// Isn't the second for loop more beautiful?
			/*for (int i = 0; i < list.size(); i++) {
            JButton temp = list.get(i);*/
			for (JButton temp:list){
				if (e.getSource() == temp)
					restPanel.showInfo(type, temp.getText());
				}
    		}
	}

	/**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
	public void addPerson(String name, String type) {
		
		if(name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int)(paneSize.height/7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			restPanel.addPerson(type, name);//puts customer on list
			restPanel.showInfo(type, name);//puts hungry button on panel
			validate();
		}
	}
	
	public void updateInfoPanel(Object person) {
    	myStateCB.setVisible(true);
    	currentPerson = person;
    	
    	if (person instanceof CustomerAgent) {
    		CustomerAgent customer = (CustomerAgent) person;
    		myStateCB.setText("Hungry?");
    		//Should checkmark be there? 
    		myStateCB.setSelected(customer.getGui().isHungry());
    		//Is customer hungry? Hack. Should ask customerGui
    		myStateCB.setEnabled(!customer.getGui().isHungry());
    		// Hack. Should ask customerGui
    		//myLabel.setText(
    		//		"<html><pre>     Name: " + customer.getName() + " </pre></html>");
    	}
    	else if (person instanceof WaiterAgent) {
    		WaiterAgent waiter = (WaiterAgent) person;
    		myStateCB.setText("On Break");
    		//Should checkmark be there? 
    		myStateCB.setSelected(waiter.getGui().isOnBreak());
    		//Is customer hungry? Hack. Should ask customerGui
    		myStateCB.setEnabled(!waiter.getGui().isOnBreak());
    		// Hack. Should ask customerGui
    		//myLabel.setText(
    		//		"<html><pre>     Name: " + customer.getName() + " </pre></html>");
    		}
    	validate();
    }
	
	public void setCustomerEnabled(CustomerAgent c) {
    	if (currentPerson instanceof CustomerAgent) {
    		CustomerAgent cust = (CustomerAgent) currentPerson;
    		if (c.equals(cust)) {
    			myStateCB.setEnabled(true);
    			myStateCB.setSelected(false); 
    		}
    	}
    }
}
