package restaurantKC.gui;


import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import restaurantKC.KCHostRole;
import restaurantKC.interfaces.Host;

public class HostGui implements Gui {

    private KCHostRole agent = null;

    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 450, yDestination = 450;
            

    public HostGui(Host agent) {
        this.agent = (KCHostRole) agent;
    }

    public void draw(Graphics2D g) {
    	Image icon = new ImageIcon("../restaurant_chillaka/res/Rami.png").getImage();
        g.drawImage(icon, xPos, yPos, null);
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
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
	}
	
	public void DoLeaveRestaurant()
    {
    	xDestination = -30;
        yDestination = -30;
    }
}
