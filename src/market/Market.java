package market;

import mainGUI.MainGui;
import city.Building;

public class Market extends Building {
	
	public Inventory inventory = null;
	MarketManagerRole manager = null;

	public Market(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height, mainGui);
		inventory = new Inventory();
	}
}
