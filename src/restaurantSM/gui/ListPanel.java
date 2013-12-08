package restaurantSM.gui;

import restaurantSM.CustomerAgent;
import restaurantSM.HostAgent;

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
    private JTextField nameField = new JTextField(7);
    private JButton addPersonB = new JButton("Add");
    private JCheckBox hungryCB = new JCheckBox();
    private JButton pauseButton = new JButton("Pause");

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
        JPanel northPanel = new JPanel (new GridLayout(2,1));
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel nameEnterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 5));
        

        setLayout(new BorderLayout());
        welcomePanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));	

        addPersonB.addActionListener(this);
        northPanel.add(welcomePanel);
        nameEnterPanel.add(nameField);
        hungryCB.setEnabled(false);
        pauseButton.addActionListener(this);
        nameField.addKeyListener(new KeyListener(){
        	public void keyTyped(KeyEvent e){
        		if (nameField.getText().length() > 0){
        			hungryCB.setEnabled(true);
        		}
        		else {
        			hungryCB.setEnabled(false);
        		}
        	}
        	public void keyPressed(KeyEvent e){
        		
        	}
        	public void keyReleased(KeyEvent e){
        		if (nameField.getText().length() > 0){
        			hungryCB.setEnabled(true);
        		}
        		else {
        			hungryCB.setEnabled(false);
        		}
        	}
        });
        nameEnterPanel.add(hungryCB);
        nameEnterPanel.add(addPersonB);
        northPanel.add(nameEnterPanel);
        add(northPanel, BorderLayout.NORTH);
        add(pauseButton, BorderLayout.SOUTH);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == pauseButton) {
        	restPanel.pauseAllAgents();
        }
    	else if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	addPerson(nameField.getText(), hungryCB.isSelected());
        	nameField.setText("");
        	hungryCB.setEnabled(false);
        	hungryCB.setSelected(false);
        }
        else if (e.getSource() == nameField) {
        	if (nameField.getText().length() > 0) {
        		hungryCB.setEnabled(true);
        	}
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
            restPanel.addPerson(type, name, hungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
