package market.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import market.Inventory;
import market.Market;
import market.MarketData;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {

	public AnimationPanel animationPanel;
	private MarketPanel marketPanel;
	private Market market;
	
	
	public MarketGui(Market market) {

		this.market = market;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width/2, screenSize.height/2);
		setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
		setLayout(new GridLayout(0,2));

		animationPanel = new AnimationPanel();
		animationPanel.setVisible(true);
		
		marketPanel = new MarketPanel(market);
		marketPanel.setVisible(true);


		add (marketPanel);
		add (animationPanel);
		
		

	}
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param person customer (or waiter) object
	 */
	public void updateMarketPanel() {

		for (JLabel label : marketPanel.labels) {
			label.setText("Quantity of steak: " + market.inventory.inventory.get("Steak").amount);
			label.setText("Quantity of salad: " + market.inventory.inventory.get("Salad").amount);
			label.setText("Quantity of chicken: " + market.inventory.inventory.get("Chicken").amount);
			label.setText("Quantity of pizza: " + market.inventory.inventory.get("Pizza").amount);
		}

		marketPanel.validate();
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
