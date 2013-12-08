package restaurantSM.gui;

import restaurantSM.CustomerAgent;
import restaurantSM.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
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
    private JPanel waiterPanel;
    private JLabel infoLabel; //part of infoPanel
    private JTextField waiterName;
    private JButton waiterButton;
    private JCheckBox stateCB;//part of infoLabel
    private JCheckBox breakCB;
    private List<JButton> list = new ArrayList<JButton>();
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();

    private JPanel listContentHolder = new JPanel();
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1000;
        int WINDOWY = 600;
        int smallBound = 50;
        int largeBound = 100;

        //animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(largeBound+WINDOWX, smallBound , WINDOWX+largeBound, WINDOWY+largeBound);
        //animationFrame.setVisible(true);
    	
    	
    	setBounds(smallBound, smallBound, WINDOWX, WINDOWY);

        //setLayout(new BoxLayout((Container) getContentPane(), 
//        		BoxLayout.Y_AXIS));
    	setLayout(new GridLayout());
    	listContentHolder.setLayout(new BoxLayout(listContentHolder, 
        		BoxLayout.Y_AXIS));
    	
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .5));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        listContentHolder.add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        breakCB = new JCheckBox();
        stateCB.setVisible(false);
        breakCB.setVisible(false);
        stateCB.addActionListener(this);
        breakCB.addActionListener(this);

        infoPanel.setLayout(new FlowLayout());
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(breakCB);
        listContentHolder.add(infoPanel);
        
        Dimension nameDim = new Dimension(WINDOWX, (int) (WINDOWY * .4));
        waiterPanel = new JPanel();
        waiterPanel.setPreferredSize(nameDim);
        waiterPanel.setMinimumSize(nameDim);
        waiterPanel.setMaximumSize(nameDim);
        waiterPanel.setBorder(BorderFactory.createTitledBorder("Add a waiter"));
        
        waiterPanel.setLayout(new BorderLayout());
        JPanel waiterNorthPanel = new JPanel();
        waiterNorthPanel.setLayout(new FlowLayout());
        waiterName = new JTextField(10);
        waiterButton = new JButton("Add");
        waiterButton.addActionListener(this);
        waiterNorthPanel.add(waiterName);
        waiterNorthPanel.add(waiterButton);
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        waiterPanel.add(pane, BorderLayout.CENTER);
        
        waiterPanel.add(waiterNorthPanel, BorderLayout.NORTH);
        listContentHolder.add(waiterPanel);
        add(listContentHolder);
        add(animationPanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(false);
        breakCB.setVisible(false);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setVisible(true);
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person;
        	breakCB.setVisible(true);
        	breakCB.setText("Break?");
        	breakCB.setSelected(waiter.getGui().isOnBreak());
        	breakCB.setEnabled(!waiter.getGui().isOnBreak());
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
                stateCB.setEnabled(false);
            }
        }
        else if (e.getSource() == waiterButton) {
        	addWaiter(waiterName.getText());
        	waiterName.setText("");
        }
        else if (e.getSource() == breakCB) {
        	if (currentPerson instanceof WaiterAgent) {
        		WaiterAgent waiter = (WaiterAgent) currentPerson;
        		waiter.getGui().breakPlease();
        		breakCB.setEnabled(false);
        	}
        }
        else {
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo("Waiters", temp.getText());
            }
        }
    }
    
    public void addWaiter(String name) {
    	if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson("Waiter", name, false);//puts customer on list
            //restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
    	}
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
    /**
     * Main routine to get gui started
     */
   /* public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/
}
