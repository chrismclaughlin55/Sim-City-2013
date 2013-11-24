package restaurantMQ.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class WaiterListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    
    public static final int SMALLMARGIN = 20;
    public static final int SCALING1 = 7;
    public static final int SCALING2 = 4;
    
    //Create text field and check box to allow for waiter creation
    JPanel addField = new JPanel();
	JTextField nameField = new JTextField();
	private JButton addPersonB = new JButton("Add Waiter");
	
    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public WaiterListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        
        addField.setLayout(new GridLayout(2, 2));
        Dimension fieldSize = new Dimension((int)RestaurantGui.WINDOWX/2 - SMALLMARGIN, (int)RestaurantGui.WINDOWY/12);
        addField.setPreferredSize(fieldSize);
        addField.setMinimumSize(fieldSize);
        addField.setMaximumSize(fieldSize);
        addField.add(nameField);
        addField.add(addPersonB);
        add(addField);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	
        	//Add the fields to a JOptionPane and display it
        	/*
        	Object[] parameters = {message, nameField, hungry};
            int n = JOptionPane.showConfirmDialog(null, parameters, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
            if(n == 0)
            	addPerson(nameField.getText(), hungry.isSelected());
            nameField.setText(""); */
        	
        	if(nameField.getText().length() != 0)
        	{
        		addWaiter(nameField.getText());
        		nameField.setText("");
        	}
            
        }
        else if(e.getSource() == nameField)
        {
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
    public void addWaiter(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            JCheckBox breakBox = new JCheckBox("Want Break");
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / SCALING2));
            //button.setPreferredSize(buttonSize);
            //button.setMinimumSize(buttonSize);
            //button.setMaximumSize(buttonSize);
            //button.addActionListener(this);
            JPanel custPanel = new JPanel();
            custPanel.setPreferredSize(buttonSize);
            custPanel.setMinimumSize(buttonSize);
            custPanel.setMaximumSize(buttonSize);
            custPanel.setLayout(new GridLayout(1,2));
            custPanel.add(button);
            custPanel.add(breakBox);
            
            list.add(button);
            view.add(custPanel);
            restPanel.addWaiter(name, breakBox);//puts customer on list
            validate();
        }
    }
}
