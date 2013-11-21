package market.gui;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import market.MarketManagerRole;

public class ManagerGui implements Gui{
	
	 private MarketManagerRole role = null;

	private int xPos = 325, yPos = 410;//default waiter position
    

    public ManagerGui() {
       // this.role = role;
    }

    public void draw(Graphics2D g) {
    	ImageIcon icon = new ImageIcon("res/Rami.png");
        g.drawImage(icon.getImage(), xPos, yPos, null);
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
