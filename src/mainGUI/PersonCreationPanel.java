package mainGUI;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class PersonCreationPanel extends JPanel implements ActionListener, KeyListener{
	
	public static final int PANEDIM = 150;

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPerson = new JButton("Add");
    private JTextField nameField = new JTextField(10);
    private String type = "Person";
    private String name;
    public JPanel namePane = new JPanel();

   
    

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public PersonCreationPanel(String type) {
        this.type = type;
       
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addPerson.addActionListener(this);

        nameField.setHorizontalAlignment(JTextField.CENTER);
        namePane.setLayout(new FlowLayout());
        
        namePane.add(addPerson);
        namePane.add(nameField);
        
        nameField.addKeyListener(this);

        view.setLayout(new GridLayout(0,1));
        pane.setViewportView(view);
        
        pane.setMinimumSize(new Dimension(PANEDIM, PANEDIM));
        pane.setPreferredSize(new Dimension(PANEDIM, PANEDIM));  
        
        add(namePane);
        add(pane);
        
        addPerson.setEnabled(false);
    }
    
    
    public void keyTyped(KeyEvent arg0) {
    	
    }
    		
    	

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height/7));
       
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);

            button.addActionListener(this);
            list.add(button);
            view.add(button);
            validate();
        }
    }


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		name = nameField.getText();
    	if ((name != null) && !name.isEmpty()){
            addPerson.setEnabled(true);
    	} 
    	else {
    		addPerson.setEnabled(false);
    	}
	}


	 /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPerson) {
        	name = nameField.getText();
        	nameField.setText(null);
        	
        	if (name != null && !name.isEmpty()){
        		addPerson(name);
                addPerson.setEnabled(false);
                addPerson.setEnabled(false);
        	}
        }
    }
}