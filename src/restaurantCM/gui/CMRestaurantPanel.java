package restaurantCM.gui;

import restaurantCM.*;

import javax.swing.*;

import city.PersonAgent;
import agent.Agent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class CMRestaurantPanel extends JPanel {

	//Host, cook, waiters and customers
	private CMHostRole host;
	private CMCashierRole cashier;
private CMCookRole cook;
	private ArrayList<CMWaiterGui> WaiterGuis = new ArrayList<CMWaiterGui>();
	private Vector<CMCustomerRole> customers = new Vector<CMCustomerRole>();
	private Vector<CMWaiterRole> waiters = new Vector<CMWaiterRole>();
	private JPanel restLabel = new JPanel();

	private CMListPanel waiterPanel = new CMListPanel(this, "Waiters");
	private JPanel group = new JPanel();
	private CMRestaurantGui gui; //reference to main gui

	public CMRestaurantPanel(CMRestaurantGui gui) {
		this.gui = gui;
	//	cook.startThread();
		//cook.msgAddCashier(cashier);
		//cashier.msgAddCook(cook);
	//	cashier.startThread();
		//System.out.println(cashier.getName());
		// TODO dont hack in the market, make it dynamic
	//TODO	host.setGui(new CMHostGui(host));
	//	host.startThread();
	//	gui.animationPanel.addGui(host.getGui());
//		gui.myAgents.add(host);
//		gui.myAgents.add(cook);
//		gui.myAgents.add(cashier);
		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

	
		group.add(waiterPanel);
		//initRestLabel();
		add(restLabel);
		add(group);
	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
//	private void initRestLabel() {
//		//restLabel.remove(label);
//		JLabel label = new JLabel();
//		restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
//		restLabel.setLayout(new BorderLayout());
//		StringBuilder labeltext = new StringBuilder();
//		labeltext.append("<html><h3><u>Tonight's Menu</u></h3><table>");
//		for(String a : cook.getFoodInventory().keySet()){
//				labeltext.append("<tr><td>" + a + "</td></tr>");
//		}
//		labeltext.append("</table></html>");
//		label.setText(labeltext.toString());
//
//		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//		restLabel.add(label,BorderLayout.NORTH);
//		     //  restLabel.add(new JLabel("               "), BorderLayout.EAST);
//		      // restLabel.add(new JLabel("               "), BorderLayout.WEST);
//		//validate();
//	}
//	public void updateRestLabel(){
//		initRestLabel();
//		validate();
//	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 */
	
	public void showInfo(String type, String name) {
		if (type.equals("Waiters")) {

			for (int i = 0; i < waiters.size(); i++) {
				CMWaiterRole temp = waiters.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
			}
		
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	public void addPerson(String type, PersonAgent person) {
//TODO 
		if (type.equals("Customer")) {
			CMCustomerRole c = new CMCustomerRole(person);	
			CMCustomerGui g = new CMCustomerGui(c, gui);
			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setGui(g);
			customers.add(c);
			//c.startThread();
		//	gui.myAgents.add(c);
		}
		if(type.equals("Waiter")){
			addWaiter(person);
		}

	}
	public void addWaiter(PersonAgent person){
		
		CMWaiterRole W = new CMWaiterRole(person);
		CMWaiterGui g = new CMWaiterGui(W, 200 , 30*waiters.size());
		W.setCook(cook);
		W.setCashier(cashier);
		W.setHost(host);
		host.addWaiter(W);
		W.setMyGui(g);
		waiters.add(W);
		gui.animationPanel.addGui(g);
	//	gui.myAgents.add(W);
	//	W.startThread();
	//	updateRestLabel();

	}

	public boolean hasHost() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasCashier() {
		// TODO Auto-generated method stub
		return false;
	}

	public int activeCustomers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean fullyStaffed() {
		// TODO Auto-generated method stub
		return false;
	}
}
