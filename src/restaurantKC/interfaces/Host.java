package restaurantKC.interfaces;

import restaurantKC.gui.HostGui;


public interface Host {

	void msgIWantFood(Customer cust);

	void msgLeaving(Customer cust);

	void setGui(HostGui hostGui);

	void msgPause();

	void msgWaiterReporting(Waiter w);

}
