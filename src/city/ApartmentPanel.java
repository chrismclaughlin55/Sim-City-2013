package city;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Gui.Gui;
import city.gui.PersonGui;

public class ApartmentPanel extends JPanel implements ActionListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;
	private int width = 400;
	private int height = 400;
	

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public ApartmentPanel() {
		setBackground(Color.WHITE);
		setVisible(true);
		//bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this);
		timer.start();
		
	}

	public void actionPerformed(ActionEvent e) {
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}
	
	public List<Gui> getGuis() {
		return guis;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.BLACK);
		g2.drawLine(0, 140, 400, 140);
		g2.drawLine(0, 238, 400, 238);
		g2.drawLine(100, 0, 100, 140);
		g2.drawLine(200, 0, 200, 140);
		g2.drawLine(300, 0, 300, 140);
		g2.drawLine(100, 238, 100, 400);
		g2.drawLine(200, 238, 200, 400);
		g2.drawLine(300, 238, 300, 400);
		g2.drawString("1", 48, 67);
		g2.drawString("2", 148, 67);
		g2.drawString("3", 248, 67);
		g2.drawString("4", 348, 67);
		g2.drawString("5", 48, 305);
		g2.drawString("6", 148, 305);
		g2.drawString("7", 248, 305);
		g2.drawString("8", 348, 305);
		

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(PersonGui gui) {
		guis.add(gui);
		gui.setXPos(0);
		gui.setYPos(340);
		gui.setPresent(true);
	}


}