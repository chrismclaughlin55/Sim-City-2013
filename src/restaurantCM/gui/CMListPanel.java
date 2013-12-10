package restaurantCM.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class CMListPanel extends JPanel implements ActionListener {

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();
	private JButton addPersonB = new JButton("Add");
	private JCheckBox Hungry = new JCheckBox("Hungry?");
	private JTextField custName = new JTextField();
	private CMRestaurantPanel restPanel;
	private String type;
	private boolean addWaiter = false;
	public boolean addHungry = false;

	/**
	 * Constructor for ListPanel.  Sets up all the gui
	 *
	 * @param rp   reference to the restaurant panel
	 * @param type indicates if this is for customers or waiters
	 */
	public CMListPanel(CMRestaurantPanel rp, String type) {
		restPanel = rp;
		this.type = type;

		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
		add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
		addPersonB.addActionListener(this);
		Hungry.addActionListener(this);
		add(addPersonB);
		if(type.equals("Customers"))
			add(Hungry);
		int custNameTextFieldHeight = 20, custNameTextFieldWidth = 400;
		Dimension textInputPreferredSize = new Dimension(custNameTextFieldWidth, custNameTextFieldHeight);
		custName.setPreferredSize(textInputPreferredSize);
		custName.setMaximumSize(textInputPreferredSize);
		custName.setMinimumSize(textInputPreferredSize);
		add(custName);

		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane.setViewportView(view);
		add(pane);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	//	public void actionPerformed(ActionEvent e) {
	//		if (e.getSource() == addPersonB) {
	//			// Chapter 2.19 describes showInputDialog()
	//			addPerson(custName.getText());
	//
	//		}
	//		else {
	//			// Isn't the second for loop more beautiful?
	//			/*for (int i = 0; i < list.size(); i++) {
	//                JButton temp = list.get(i);*/
	//			for (JButton temp:list){
	//				if (e.getSource() == temp)
	//					restPanel.showInfo(type, temp.getText());
	//			}
	//
	//			if(e.getSource() == Hungry){
	//				addHungry = !addHungry;
	//			}
	//		}


/**
 * If the add button is pressed, this function creates
 * a spot for it in the scroll pane, and tells the restaurant panel
 * to add a new person.
 *
 * @param name name of new person
 */

}

