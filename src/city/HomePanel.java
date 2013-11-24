package city;

import javax.swing.*;

import city.gui.PersonGui;


import Gui.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;


public class HomePanel extends JPanel implements ActionListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 2;
	private int width;
	private int height;
	

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public HomePanel() {
		setBackground(Color.WHITE);

		setVisible(true);
		

		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this);
		timer.start();

	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		height = this.getHeight();
		width = this.getWidth();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.BLACK);
		g2.drawLine(width/2, 0, width/2, height - 50);
		g2.fillRect((width/4 - 10), 0, 50, 20);
		g2.setColor(Color.GREEN);
		g2.fillRect(0, height - 50, 30, 40);
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 20, 30, 40);
		g2.setColor(Color.PINK);
		g2.fillRect(width - 60, 0, 60, 100);
		g2.setColor(Color.GRAY);
		g2.fillRect(width/2 - 40, height/2 - 30, 40, 60);
		

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
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
	}


}


