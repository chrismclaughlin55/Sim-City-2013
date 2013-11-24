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
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import city.gui.PersonGui;

/**
 * Panel to create PersonAgents
 */
public class PersonCreationPanel extends JPanel implements ActionListener, KeyListener{

	public static final int PANEDIM = 150;

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public JPanel personPane = new JPanel();
	public JPanel jobsPane = new JPanel();

	private JPanel view = new JPanel();
	public JPanel namePane = new JPanel();
	private JButton addPerson = new JButton("Add");
	private JTextField nameField = new JTextField(10);
	private ButtonGroup jobs = new ButtonGroup();

	private List<JButton> list = new ArrayList<JButton>(); 
	private String type = "Person";
	private String name;
	private MainGui mainGui;

	JRadioButton unemployed;
	JRadioButton restMQwaiter;
	JRadioButton restMQhost;
	JRadioButton restMQcook;
	JRadioButton restMQcashier;

	public PersonCreationPanel(MainGui mainGui) {
		this.mainGui = mainGui;
		this.type = "person";
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		nameField.setHorizontalAlignment(JTextField.CENTER);
		namePane.setLayout(new FlowLayout());
		
		namePane.add(nameField);
		namePane.add(addPerson);

		personPane.setLayout(new GridLayout(0,1));
		unemployed = new JRadioButton("Unemployed", true);
		restMQwaiter = new JRadioButton("RestaurantMQ Waiter", false);
		restMQhost = new JRadioButton("RestaurantMQ Host", false);
		restMQcook = new JRadioButton("RestaurantMQ Cook", false);
		restMQcashier = new JRadioButton("RestaurantMQ Cashier", false);
		jobs.add(unemployed);
		jobs.add(restMQwaiter);
		jobs.add(restMQhost);
		jobs.add(restMQcook);
		jobs.add(restMQcashier);
		jobsPane.add(unemployed);
		jobsPane.add(restMQwaiter);
		jobsPane.add(restMQhost);
		jobsPane.add(restMQcook);
		jobsPane.add(restMQcashier);


		unemployed.addActionListener(this);
		addPerson.addActionListener(this);
		nameField.addKeyListener(this);

		view.setLayout(new GridLayout(0,1));
		pane.setViewportView(view);
		pane.setMinimumSize(new Dimension(PANEDIM, PANEDIM));
		pane.setPreferredSize(new Dimension(PANEDIM, PANEDIM));  

		personPane.add(jobsPane);		
		personPane.add(nameField);
		personPane.add(addPerson);

		add(personPane);
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

			mainGui.addPerson(name, getSelectedButtonText(jobs));
			System.out.println(getSelectedButtonText(jobs));
			unemployed.setSelected(true);
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


	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null;
	}
}