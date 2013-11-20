package market.gui;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import market.Inventory;
import market.MarketData;

public class MarketPanel extends JPanel{

	public JLabel steakLabel;
	public JLabel saladLabel;
	public JLabel chickenLabel;
	public JLabel pizzaLabel;
	public List<JLabel> labels = Collections.synchronizedList(new ArrayList<JLabel>());


	public MarketPanel(Inventory inventory) {
		steakLabel = new JLabel("Quantity of steak: " + inventory.inventory.get("steak").amount);
		saladLabel = new JLabel("Quantity of salad: " + inventory.inventory.get("salad").amount);
		chickenLabel = new JLabel("Quantity of chicken: " + inventory.inventory.get("chicken").amount);
		pizzaLabel = new JLabel("Quantity of pizza: " + inventory.inventory.get("pizza").amount);
		labels.add(steakLabel);
		labels.add(saladLabel);
		labels.add(chickenLabel);
		labels.add(pizzaLabel);

		setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));

		add(steakLabel);
		add(saladLabel);
		add(chickenLabel);
		add(pizzaLabel);
	}

}
