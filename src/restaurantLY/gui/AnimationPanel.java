package restaurantLY.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
    private Image bufferImage;
    private Dimension bufferSize;
    
    static final int rectX = 150;
    static final int rectY = 230;
    static final int rectW = 50;
    static final int rectH = 50;
    
    int WINDOWX = 430;
    int WINDOWY = 512;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this);
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, 430, 512);
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        for(int i = 0; i < 4; i++) {
        	g2.fillRect(55+i*90, rectY, rectW, rectH);
        }
        
        
        //Here is waiting area
        g2.setColor(Color.GRAY);
        for(int i = 0; i < 16; i++) {
        	g2.fillRect(30+i*25, 5, 20, 20);
        }
        
        //Here is cooking area
        g2.setColor(Color.GRAY);
        g2.fillRect(145, 450, 50, 50);
        
        //Here is placing area
        g2.setColor(Color.GRAY);
        g2.fillRect(55, 450, 50, 50);
        
        //Here is refrigerator
        g2.setColor(Color.GRAY);
        g2.fillRect(235, 450, 50, 50);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    
    /*public void setPause() {
    	for (Gui gui : guis) {
    		gui.setPause();
    	}
    }
    
    public void setRestart() {
    	for (Gui gui : guis) {
    		gui.setRestart();
    	}
    }*/
}
