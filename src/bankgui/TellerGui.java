package bankgui;
import Gui.*;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.TellerRole;

public class TellerGui implements Gui {
	private TellerRole teller;
	private int xPos;
	private int xDestination;
	private int yPos;
	private int yDestination;
	private boolean isPresent;

	@Override
	public void updatePosition() {
;
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		for (int i = 0; i < 20; i++) {

		}
	}
	

	@Override
	public void draw(Graphics2D g) {
			Graphics2D g2 = (Graphics2D)g;
			ImageIcon person = new ImageIcon("res/person.png");
			g2.drawImage(person.getImage(), xPos, yPos, null);
		}


	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return isPresent;
	}

}
