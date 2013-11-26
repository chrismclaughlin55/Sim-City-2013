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

import city.CityData;
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
	private String job;
	private MainGui mainGui;

	JRadioButton unemployed;
	JRadioButton landlord;
	//restaurant jobs
	JRadioButton restMQwaiter;
	JRadioButton restMQhost;
	JRadioButton restMQcook;
	JRadioButton restMQcashier;
	JRadioButton restMQcustomer;
	//market jobs
	JRadioButton marketManager;
	JRadioButton marketEmployee;
	//bank jobs
	JRadioButton bankManager;
	JRadioButton bankTeller;
	JRadioButton bankCustomer;

	public PersonCreationPanel(MainGui mainGui) {
		this.mainGui = mainGui;
		this.type = "person";
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		nameField.setHorizontalAlignment(JTextField.CENTER);
		namePane.setLayout(new FlowLayout());
		
		namePane.add(nameField);
		namePane.add(addPerson);

		personPane.setLayout(null);
		
		unemployed = new JRadioButton("Unemployed", true);
		landlord = new JRadioButton("Landlord", false);
		restMQwaiter = new JRadioButton("Waiter", false);
		restMQhost = new JRadioButton("Host", false);
		restMQcook = new JRadioButton("Cook", false);
		restMQcashier = new JRadioButton("Cashier", false);
		marketManager = new JRadioButton("MarketManager", false);
		marketEmployee = new JRadioButton("MarketEmployee", false);
		bankManager = new JRadioButton("BankManager", false);
		bankTeller = new JRadioButton("BankTeller", false);
		
		jobs.add(unemployed);
		jobs.add(landlord);
		jobs.add(restMQwaiter);
		jobs.add(restMQhost);
		jobs.add(restMQcook);
		jobs.add(restMQcashier);
		jobs.add(marketManager);
		jobs.add(marketEmployee);
		jobs.add(bankManager);
		jobs.add(bankTeller);
		
		jobsPane.setLayout(null);
		unemployed.setBounds(0, 0, 150, 30);
		landlord.setBounds(145, 0, 140, 30);
		restMQhost.setBounds(0, 30, 140, 30);
		restMQwaiter.setBounds(145, 30, 145, 30);
		restMQcook.setBounds(300, 30, 140, 30);
		restMQcashier.setBounds(450, 30, 170, 30);
		marketManager.setBounds(0, 60, 140, 30);
		marketEmployee.setBounds(145, 60, 140, 30);
		bankManager.setBounds(0, 90, 140, 30);
		bankTeller.setBounds(145, 90, 140, 30);
		jobsPane.add(unemployed);
		jobsPane.add(landlord);
		jobsPane.add(restMQwaiter);
		jobsPane.add(restMQhost);
		jobsPane.add(restMQcook);
		jobsPane.add(restMQcashier);
		jobsPane.add(marketManager);
		jobsPane.add(marketEmployee);
		jobsPane.add(bankManager);
		jobsPane.add(bankTeller);

		unemployed.addActionListener(this);
		addPerson.addActionListener(this);
		nameField.addKeyListener(this);

		view.setLayout(new GridLayout(0,1));
		pane.setViewportView(view);
		pane.setMinimumSize(new Dimension(PANEDIM, PANEDIM));
		pane.setPreferredSize(new Dimension(PANEDIM, PANEDIM));  

		jobsPane.setBounds(0, 0, 615, 125);
		nameField.setBounds(0, 125, 615, 95);
		addPerson.setBounds(0, 220, 615, 90);
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
			
			String role = "";
			String destination;
			
			if(getSelectedButtonText(jobs).contains("Restaurant"))
			{
				destination = "Restaurant";
				if(getSelectedButtonText(jobs).contains("Customer"))
				{
					role = "Customer";
				}
				else if(getSelectedButtonText(jobs).contains("Waiter"))
				{
					role = "Waiter";
				}
				else if(getSelectedButtonText(jobs).contains("Cook"))
				{
					role = "Cook";
				}
				else if(getSelectedButtonText(jobs).contains("Host"))
				{
					role = "Host";
				}
				else if(getSelectedButtonText(jobs).contains("Cashier"))
				{
					role = "Cashier";
				}
			} else if(getSelectedButtonText(jobs).contains("Bank")){
				destination = "Bank";
				if(getSelectedButtonText(jobs).contains("BankManager")){
					role = "BankManager";
				//	mainGui.mainAnimationPanel.cd.buildings.get(18).manager
				}
				else if(getSelectedButtonText(jobs).contains("BankTeller")){
					role = "BankTeller";
				}
				else if(getSelectedButtonText(jobs).contains("Customer")){
					role = "Customer";
				}
			}
			else
			{
				destination = "Home";
			}
			
			mainGui.addPerson(name, role, destination);
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