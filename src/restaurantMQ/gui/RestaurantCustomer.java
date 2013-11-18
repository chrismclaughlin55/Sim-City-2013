package restaurantMQ.gui;

import javax.swing.JCheckBox;

import restaurantMQ.CustomerAgent;
import restaurantMQ.interfaces.Customer;

public class RestaurantCustomer 
{
	Customer customer;
	JCheckBox hungerCB;
	
	public RestaurantCustomer(Customer customer, JCheckBox hungerCB)
	{
		this.customer = customer;
		this.hungerCB = hungerCB;
	}
}
