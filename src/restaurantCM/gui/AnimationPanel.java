package restaurantCM.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {
    private final int WINDOWX = 450;
    private final int WINDOWY = 450;
    private Image bufferImage;
    private Dimension bufferSize;
    
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
 
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
    
        //Here is the table//200 and 250 need to be table params
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(CMHostGui.xTable1, CMHostGui.yTable1, 50, 50);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(CMHostGui.xTable2, CMHostGui.yTable2, 50, 50);
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(CMHostGui.xTable3, CMHostGui.yTable3, 50, 50);
        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable4, HostGui.yTable4, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable5, HostGui.yTable5, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable6, HostGui.yTable6, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable7, HostGui.yTable7, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable8, HostGui.yTable8, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable9, HostGui.yTable9, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
//        g2.fillRect(HostGui.xTable10, HostGui.yTable10, 50, 50);
//        g2.setColor(Color.DARK_GRAY);
////        g2.fillRect(HostGui.xTable11, HostGui.yTable11, 50, 50);
////        g2.setColor(Color.DARK_GRAY);
////        g2.fillRect(HostGui.xTable12, HostGui.yTable12, 50, 50);
////        g2.setColor(Color.DARK_GRAY);
//        
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

    public void addGui(CMWaiterGui waiterGui1) {
        guis.add(waiterGui1);
    }

    public void addGui(CMCustomerGui gui) {
        guis.add(gui);
    }
    public void addGui(CMHostGui gui){
    	guis.add(gui);
    }
}
