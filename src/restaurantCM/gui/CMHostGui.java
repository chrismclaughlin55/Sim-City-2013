package restaurantCM.gui;


import restaurantCM.CMCustomerRole;
import restaurantCM.CMHostRole;

import java.awt.*;

public class CMHostGui implements Gui {
	public boolean customerline1 = false;
	public boolean customerline2 = false;
	public boolean customerline3 = false;
	private CMHostRole agent = null;
	private int xLobby = 80, yLobby = 40; //Lobby position
	private int xPos = xLobby, yPos = yLobby;//default waiter position
	public int xDestination = xLobby, yDestination = yLobby;//default start position
	public int xTable, yTable;	//CHANGED Hard coded tables --- TODO dynamic table positioning
	public static final int xTable1 = 400;
	public static final int yTable1 = 100;
	public static final int xTable2 = 400;
	public static final int yTable2 = 160;
	public static final int xTable3 = 400;
	public static final int yTable3 = 220;
	public static final int xTable4 = 400;
	public static final int yTable4 = 280;
	public static final int xTable5 = 400;
	public static final int yTable5 = 340;
	public static final int xTable6 = 400;
	public static final int yTable6 = 400;
	public static final int xTable7 = 160;
	public static final int yTable7 = 100;
	public static final int xTable8 = 160;
	public static final int yTable8 = 160;
	public static final int xTable9 = 160;
	public static final int yTable9 = 220;
	public static final int xTable10 = 160;
	public static final int yTable10 = 280;
	public static final int xTable11 = 160;
	public static final int yTable11 = 340;
	public static final int xTable12 = 160;
	public static final int yTable12 = 400;
	public CMHostGui(CMHostRole agent) {
		this.agent = agent;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
	}



	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, 20, 20);
		if(agent.waitingCustomers.size() >= 1){
			g.setColor(Color.GREEN);
			g.fillRect(60, 40, 20, 20);
		}
		if(agent.waitingCustomers.size()>=2){
			g.setColor(Color.GREEN);
			g.fillRect(30, 40, 20, 20);
		}
		if(agent.waitingCustomers.size()>=3){
			g.setColor(Color.GREEN);
			g.fillRect(0, 40, 20, 20);
		}

	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(CMCustomerRole customer, int table) {
		switch(table){
		case 1:
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
			yTable = yTable1;
			xTable = xTable1;
			break;
		case 2:
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 20;
			yTable = yTable2;
			xTable = xTable2;
			break;
		case 3:
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
			yTable = yTable3;
			xTable = xTable3;
			break;
		case 4:
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
			yTable = yTable4;
			xTable = xTable4;
			break;
		case 5:
			xDestination = xTable5 + 20;
			yDestination = yTable5 - 20;
			yTable = yTable5;
			xTable = xTable5;
			break;
		case 6:
			xDestination = xTable6 + 20;
			yDestination = yTable6 - 20;
			yTable = yTable6;
			xTable = xTable6;
			break;
		case 7:
			xDestination = xTable7 + 20;
			yDestination = yTable7 - 20;
			yTable = yTable7;
			xTable = xTable7;
			break;
		case 8:
			xDestination = xTable8 + 20;
			yDestination = yTable8 - 20;
			yTable = yTable8;
			xTable = xTable8;
			break;
		case 9:
			xDestination = xTable9 + 20;
			yDestination = yTable9 - 20;
			yTable = yTable9;
			xTable = xTable9;
			break;
		case 10:
			xDestination = xTable10 + 20;
			yDestination = yTable10 - 20;
			yTable = yTable10;
			xTable = xTable10;
			break;
		case 11:
			xDestination = xTable11 + 20;
			yDestination = yTable11 - 20;
			yTable = yTable11;
			xTable = xTable11;
			break;
		case 12:
			xDestination = xTable12 + 20;
			yDestination = yTable12 - 20;
			yTable = yTable12;
			xTable = xTable12;
			break;
		}

	}


	public void DoLeaveCustomer() {
		xDestination = -20;
		yDestination = -20;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
