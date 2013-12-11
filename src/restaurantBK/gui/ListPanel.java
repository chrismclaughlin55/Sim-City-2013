package restaurantBK.gui;

import restaurantBK.BKCustomerRole;
import restaurantBK.BKHostRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        addPersonB.addActionListener(this);
        add(addPersonB);

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
        	
        	//System.out.println("get there");
        	// Chapter 2.19 describes showInputDialog()
        	final JPanel newCust = new JPanel();
        	newCust.setLayout(new FlowLayout());
        	final JTextField nametext = new JTextField(10);
        	newCust.add(new JLabel("Name:"));
        	newCust.add(nametext);
        	final JCheckBox hunger = new JCheckBox();
        	hunger.setEnabled(false);
        	if(this.type=="Customers") {
        		newCust.add(new JLabel(" You hungry?"));
            	newCust.add(hunger);
        	}
        	else if(this.type == "Waiters") {
        		newCust.add(new JLabel("Go on break?"));
        		newCust.add(hunger);
        	}
        	
        	
        	JButton submit = new JButton("Submit");
            newCust.add(submit);
        	newCust.setVisible(true);
        	this.add(newCust);
        	revalidate();
        	nametext.addKeyListener(new KeyListener() {
        		 public void keyTyped(KeyEvent e) {
        		    }


        		    /** Handle the key-released event from the text field. */
        		    public void keyReleased(KeyEvent e) {
        		    }
        		public void keyPressed(KeyEvent e)
        		{
        			if(nametext.getText()!="")
        			{
        				hunger.setEnabled(true);
        			}
        			else
        			{
        				hunger.setEnabled(false);
        			}
        		}
        	});
        	
        	
        	submit.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e)
        		{
        			addPerson(nametext.getText(),hunger.isSelected());
        			//pass in hungry if clicked
        			nametext.setText("");
        			
        			remove(newCust);
        			revalidate();
        		}
        	});
        	
        	
        	//addPerson(JOptionPane.showInputDialog("Please enter a name:"));
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
    public void addPerson(String name, boolean hungry) {
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
            //restPanel.addHungryPerson(type, name,hungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
