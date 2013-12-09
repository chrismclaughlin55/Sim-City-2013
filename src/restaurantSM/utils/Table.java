package restaurantSM.utils;

import restaurantSM.SMCustomerRole;

public class Table {
	SMCustomerRole occupiedBy;
	public int tableNumber;

	public Table(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public void setOccupant(SMCustomerRole cust) {
		occupiedBy = cust;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	public SMCustomerRole getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}
}