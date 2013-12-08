package market.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import Gui.Gui;


public class AnimationPanel extends JPanel implements ActionListener {

	private Image bufferImage;
	private Dimension bufferSize;
	private int frameDisplay = 3;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

	public AnimationPanel() {
		setBackground(Color.WHITE);
		setVisible(true);

		bufferSize = this.getSize();

		Timer timer = new Timer(frameDisplay, this );
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



	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color roomColor = new Color(236, 240, 241);
		Color shelfColor = new Color(230, 126, 34);
		Color deskColor = new Color(52, 73, 94);

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, 600, 400);

		g2.setColor(roomColor);
		g2.fillRect(270, 355, 150, 150);

		g2.setColor(shelfColor);
		g2.fillRect(0, 355, 270, 150);

		g2.setColor(deskColor);
		g2.fillRect(50, 150, 600, 50);

		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}


	}

	public void addGui(CustomerGui gui) {
		synchronized(guis) {
			guis.add(gui);
		}
	}

	public void addGui(EmployeeGui gui) {
		synchronized(guis) {
			guis.add(gui);
		}
	}

	public void addGui(ManagerGui gui) {
		synchronized(guis) {
			guis.add(gui);
		}
	}


}


