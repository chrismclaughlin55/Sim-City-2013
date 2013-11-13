package restaurantMQ.gui;

import javax.swing.JCheckBox;

import restaurantMQ.CustomerAgent;

public class RestaurantCustomer 
{
	CustomerAgent customer;
	JCheckBox hungerCB;
	
	public RestaurantCustomer(CustomerAgent customer, JCheckBox hungerCB)
	{
		this.customer = customer;
		this.hungerCB = hungerCB;
	}
}
