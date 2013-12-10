package restaurantCM.gui;

import restaurantCM.*;

import java.awt.*;

public class WaiterGui implements Gui{
	//	boolean sentMsg = true;
	private WaiterAgent agent = null;
	public static final int xCook = 200;
	public static final int yCook = 150;
	private int xLobby = 60 , yLobby = 60; //Lobby position
	private int xHome;
	private int yHome;
	public int xPos = xHome;

	public int yPos = yHome;
	public int xDestination = xHome, yDestination = yHome;//default start position
	public int xTable, yTable;
	public boolean carryOrder = false;
	public static final int xTable1 = HostGui.xTable1;
	public static final int yTable1 = HostGui.yTable1;
	public static final int xTable2 = HostGui.xTable2;
	public static final int yTable2 = HostGui.yTable2;
	public static final int xTable3 = HostGui.xTable3;
	public static final int yTable3 = HostGui.yTable3;
	public static final int xTable4 = HostGui.xTable4;
	public static final int yTable4 = HostGui.yTable4;
	public static final int xTable5 = HostGui.xTable5;
	public static final int yTable5 = HostGui.yTable5;
	public static final int xTable6 = HostGui.xTable6;
	public static final int yTable6 = HostGui.yTable6;
	public static final int xTable7 = HostGui.xTable7;
	public static final int yTable7 = HostGui.yTable7;
	public static final int xTable8 = HostGui.xTable8;
	public static final int yTable8 = 160;
	public static final int xTable9 = 160;
	public static final int yTable9 = 220;
	public static final int xTable10 = 160;
	public static final int yTable10 = 280;
	public static final int xTable11 = 160;
	public static final int yTable11 = 340;
	public static final int xTable12 = 160;
	public static final int yTable12 = 400;

	public WaiterGui(WaiterAgent agent, int x, int y) {
		this.agent = agent;
		this.xPos = x;
		this.yPos = y;
		this.xHome = x;
		this.yHome = y;
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
		if(xPos == xTable1+20 && yPos == yTable1-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable2+20 && yPos == yTable2-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable3+20 && yPos == yTable3-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable4+20 && yPos == yTable4-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable5+20 && yPos == yTable5-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable6+20 && yPos == yTable6-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable7+20 && yPos == yTable7-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable8+20 && yPos == yTable8-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable9+20 && yPos == yTable9-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable10+20 && yPos == yTable10-20){
			agent.msgDoneAtTable();
		}
		if(xPos == xTable11+20 && yPos == yTable11-20){
			agent.msgDoneAtTable();
		}	
		if(xPos == xTable12+20 && yPos == yTable12-20){
			agent.msgDoneAtTable();
		}	        
		if(xPos == xLobby && yPos == yLobby){
			agent.msgAtLobby();
		}
		if(xPos == xHome && yPos == yHome){
			agent.msgAtHome();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);

	}

	public boolean isPresent() {
		return true;
	}

	public void DoGoToTable(int place) {

		//sentMsg = false;
		switch(place){
		/*case -1:
	    			xDestination = xLobby;
	    			yDestination = yLobby;
	    			break;
	    		case -2:
	    			xDestination = xCook;
	    			yDestination = yCook;
		 */
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

	public void DoGoToLobby() {
		xDestination = xLobby;
		yDestination = yLobby;
		//  sentMsg = false;
	}
	public void DoGoToHome(){
		xDestination = xHome;
		yDestination = yHome;
	}
	public void DoGoToCook() {
		xDestination = xCook;
		yDestination = yCook;
		agent.msgAtCook();
	}
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}


}


