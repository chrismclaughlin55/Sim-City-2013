package market.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import city.PersonAgent;
import city.Role;
import market.Inventory;
import market.MarketCustomerRole;
import market.MarketData;
import market.MarketEmployeeRole;
import market.MarketManagerRole;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {

	public AnimationPanel animationPanel;
	private MarketPanel marketPanel;

	public MarketGui() {

		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width/2, screenSize.height/2);
		setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
		setLayout(new GridLayout(0,2));

		MarketData chickenData = new MarketData("chicken", 10, 5.99);
		MarketData saladData = new MarketData("salad", 10, 3.99);
		MarketData steakData = new MarketData("steak", 10, 11.99);
		MarketData pizzaData = new MarketData("pizza", 10, 7.99);
		Inventory inventory = new Inventory(chickenData, saladData, steakData, pizzaData);

		animationPanel = new AnimationPanel();
		animationPanel.setVisible(true);
		
		marketPanel = new MarketPanel(inventory);
		marketPanel.setVisible(true);


		add (marketPanel);
		add (animationPanel);
		
		PersonAgent p1 = new PersonAgent("Manager");
		MarketManagerRole manager = new MarketManagerRole(p1, inventory);
		p1.msgAssignRole(manager);
		p1.startThread();
		ManagerGui managerGui = new ManagerGui();
		animationPanel.addGui(managerGui);
		
		
		PersonAgent p2 = new PersonAgent("Employee1");
		MarketEmployeeRole employee = new MarketEmployeeRole(p2, 1, manager, inventory);
		p2.msgAssignRole(employee);
		EmployeeGui employeeGui = new EmployeeGui();
		animationPanel.addGui(employeeGui);
		
		PersonAgent p3 = new PersonAgent("Customer1");
		MarketCustomerRole customer = new MarketCustomerRole(p3, employee, 1);
		p3.msgAssignRole(customer);
		CustomerGui custGui = new CustomerGui();
		animationPanel.addGui(custGui);
		
		

	}
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param person customer (or waiter) object
	 */
	public void updateMarketPanel(Inventory inventory) {

		for (JLabel label : marketPanel.labels) {
			label.setText("Quantity of steak: " + inventory.inventory.get("steak").amount);
			label.setText("Quantity of salad: " + inventory.inventory.get("salad").amount);
			label.setText("Quantity of chicken: " + inventory.inventory.get("chicken").amount);
			label.setText("Quantity of pizza: " + inventory.inventory.get("pizza").amount);
		}

		marketPanel.validate();
	}


	/*public static void main(String[] args) {
		MarketGui gui = new MarketGui();
		gui.setTitle("Market");
		gui.setVisible(true);
		gui.setResizable(true);        

		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}*/


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
