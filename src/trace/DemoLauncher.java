package trace;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * A quick demo of some squares moving around and logging when they change color zones.  You can also
 * click the mouse in either color to generate a different kind of message. <br><br>
 * 
 * TUTORIAL: You can search through this file for the word "TUTORIAL" to see relevant pieces of 
 * code with instructions on how to use the {@link AlertLog} and {@link TracePanel}.
 * 
 * @author Keith DeRuiter
 *
 */
@SuppressWarnings("serial")
public class DemoLauncher extends JFrame {
	ControlPanel controlPanel;
	
	//============================ TUTORIAL ==========================================
	//You declare a TracePanel just like any other variable, and it extends JPanel, so you use
	//it in the same way.
	TracePanel tracePanel;
	//================================================================================


	public DemoLauncher() {
		this.tracePanel = new TracePanel();
		this.controlPanel = new ControlPanel(tracePanel);
		tracePanel.setPreferredSize(new Dimension(800, 300));
		
		this.setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.CENTER);
		this.add(tracePanel, BorderLayout.SOUTH);
	}

	public void start() {
		//============================ TUTORIAL ==========================================
		//We have to tell the trace panel what kinds of messages to display.  Here we say to display
		//normal MESSAGEs tagged with the PERSON tag (the type I use in this demo).  You can also 
		//hide messages of certain Levels, or messages tagged a certain way (eg. Don't show anything
		//that says it is from a MARKET_EMPLOYEE).  Here we decide to hide debug messages and things
		//tagged as AlertTag.BUS_STOP
		tracePanel.showAlertsWithLevel(AlertLevel.ERROR);		//THESE PRINT RED, WARNINGS PRINT YELLOW on a black background... :/
		tracePanel.showAlertsWithLevel(AlertLevel.INFO);		//THESE PRINT BLUE
		tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);		//THESE SHOULD BE THE MOST COMMON AND PRINT BLACK
		//tracePanel.hideAlertsWithLevel(AlertLevel.DEBUG);
		
		tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
		tracePanel.showAlertsWithTag(AlertTag.PERSON);
		tracePanel.showAlertsWithTag(AlertTag.BUILDING);
		tracePanel.showAlertsWithTag(AlertTag.BANK);
		tracePanel.showAlertsWithTag(AlertTag.MARKET);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM);
		
		//
		//You will have to add your own AlertTag types to the AlertTag enum for your project.
		//There are two helper methods that enable all AlertLevels and all AlertTags that you can use
		//if you don't want to manually enable them all.  IF NOTHING APPEARS, CHECK THAT YOU HAVE 
		//THE RIGHT LEVELS AND TAGS TURNED ON.  That will likely be the most common problem.
		//================================================================================
	
		//============================ TUTORIAL ==========================================
		//We need to let the log know that the trace panel wants to listen for alert messages.
		//This is how the trace panel will be automatically told when anyone logs a message, and 
		//will store or display it as appropriate.  Note how we are accessing the AlertLog: using
		//the fact that it is a globally accessible singleton (there is only ever ONE log, and 
		//everybody accesses the same one).
		AlertLog.getInstance().addAlertListener(tracePanel);
		//================================================================================		
	}

	
	//CONTROL PANEL CLASS
	private class ControlPanel extends JPanel {
		TracePanel tp;	//Hack so I can easily call showAlertsWithLevel for this demo.
		
		JToggleButton messageButton;
		JToggleButton errorButton;	
		JToggleButton infoButton;	
		/*JToggleButton warningButton;			
		JToggleButton debugButton;*/
		
		JToggleButton PersonTagButton;
		JToggleButton BuildingTagButton;
		JToggleButton BankTagButton;
		JToggleButton BankManagerTagButton;
		JToggleButton BankTellerTagButton;
		JToggleButton BankCustTagButton;
		JToggleButton ApartmentTagButton;
		JToggleButton BusTagButton;
		JToggleButton BusStopTagButton;
		JToggleButton HomeTagButton;
		JToggleButton MarketTagButton;
		JToggleButton MarketCustTagButton;
		JToggleButton MarketEmployeeTagButton;
		JToggleButton MarketManagerTagButton;
		JToggleButton RestBKTagButton;
		JToggleButton BKCashierTagButton;
		JToggleButton BKCookTagButton;
		JToggleButton BKCustTagButton;
		JToggleButton BKHostTagButton;
		JToggleButton BKWaiterTagButton;
		JToggleButton RestKCTagButton;
		JToggleButton KCCashierTagButton;
		JToggleButton KCCookTagButton;
		JToggleButton KCCustTagButton;
		JToggleButton KCHostTagButton;
		JToggleButton KCWaiterTagButton;
		JToggleButton RestLYTagButton;
		JToggleButton LYCashierTagButton;
		JToggleButton LYCookTagButton;
		JToggleButton LYCustTagButton;
		JToggleButton LYHostTagButton;
		JToggleButton LYWaiterTagButton;
		JToggleButton RestMQTagButton;
		JToggleButton MQCashierTagButton;
		JToggleButton MQCookTagButton;
		JToggleButton MQCustTagButton;
		JToggleButton MQHostTagButton;
		JToggleButton MQWaiterTagButton;
		JToggleButton RestSMTagButton;
		JToggleButton SMCashierTagButton;
		JToggleButton SMCookTagButton;
		JToggleButton SMCustTagButton;
		JToggleButton SMHostTagButton;
		JToggleButton SMWaiterTagButton;
		JToggleButton RestCMTagButton;
		JToggleButton CMCashierTagButton;
		JToggleButton CMCookTagButton;
		JToggleButton CMCustTagButton;
		JToggleButton CMHostTagButton;
		JToggleButton CMWaiterTagButton;
		JToggleButton CityTagButton;
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			messageButton = new JToggleButton("Hide Level: MESSAGE");
			errorButton = new JToggleButton("Hide Level: NON-NORM");
			infoButton = new JToggleButton("Hide Level: INFO");
			/*warningButton = new JToggleButton("Hide Level: WARNING");
			debugButton = new JButton("Hide Level: DEBUG");*/
			
			PersonTagButton = new JToggleButton("Hide Tag: PERSON");
			BuildingTagButton = new JToggleButton("Hide Tag: BUILDING");
			BankTagButton = new JToggleButton("Hide Tag: BANK");
			BankManagerTagButton = new JToggleButton("Hide Tag: BANK MANAGER");
			BankTellerTagButton = new JToggleButton("Hide Tag: BANK TELLER");
			BankCustTagButton = new JToggleButton("Hide Tag: BANK CUSTOMER");
			ApartmentTagButton = new JToggleButton("Hide Tag: APARTMENT");
			BusTagButton = new JToggleButton("Hide Tag: BUS");
			BusStopTagButton = new JToggleButton("Hide Tag: BUS STOP");
			HomeTagButton = new JToggleButton("Hide Tag: HOME");
			MarketTagButton = new JToggleButton("Hide Tag: MARKET");
			MarketCustTagButton = new JToggleButton("Hide Tag: MARKET CUSTOMER");
			MarketEmployeeTagButton = new JToggleButton("Hide Tag: MARKET EMPLOYEE");
			MarketManagerTagButton = new JToggleButton("Hide Tag: MARKET MANAGER");
			RestBKTagButton = new JToggleButton("Hide Tag: RESTAURANT_BK");
			RestKCTagButton = new JToggleButton("Hide Tag: RESTAURANT_KC");
			RestLYTagButton = new JToggleButton("Hide Tag: RESTAURANT_LY");
			RestMQTagButton = new JToggleButton("Hide Tag: RESTAURANT_MQ");
			RestSMTagButton = new JToggleButton("Hide Tag: RESTAURANT_SM");
			RestCMTagButton = new JToggleButton("Hide Tag: RESTAURANT_CM");
			CityTagButton = new JToggleButton("Hide Tag: GENERAL CITY");
			
			messageButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(messageButton.isSelected()) {
						messageButton.setText("Show Level: MESSAGE");
						//============================ TUTORIAL ==========================================
						//This is how you make messages with a certain Level (normal MESSAGE here) show up in the trace panel.
						tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
						//================================================================================
					}
					else {
						messageButton.setText("Hide Level: MESSAGE");
						tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
					}
				}
			});
			
			infoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(infoButton.isSelected()) {
						infoButton.setText("Show Level: INFO");
						tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
					}
					else {
						infoButton.setText("Hide Level: INFO");
						tracePanel.showAlertsWithLevel(AlertLevel.INFO);
					}
				}
			});
			
			errorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(errorButton.isSelected()) {
						errorButton.setText("Show Level: NON-NORM");
						//============================ TUTORIAL ==========================================
						//This is how you make messages with a level of ERROR show up in the trace panel.
						tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
						//================================================================================
					}
					else {
						errorButton.setText("Hide Level: NON-NORM");
						tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
					}
				}
			});
			
			BankTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BankTagButton.isSelected()) {
						BankTagButton.setText("Show Tag: BANK");
						//============================ TUTORIAL ==========================================
						//This works the same way as AlertLevels, only you're using tags instead.
						//In this demo, I generate message with tag BANK_CUSTOMER when you click in the 
						//AnimationPanel somewhere.
						tracePanel.hideAlertsWithTag(AlertTag.BANK);
						//================================================================================
					}
					else {
						BankTagButton.setText("Hide Level: BANK");
						//============================ TUTORIAL ==========================================
						//This works the same way as AlertLevels, only you're using tags instead.
						tracePanel.showAlertsWithTag(AlertTag.BANK);
						//================================================================================
					}
				}
			});
			
			MarketTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(MarketTagButton.isSelected()) {
						MarketTagButton.setText("Show Tag: MARKET");
						tracePanel.hideAlertsWithTag(AlertTag.MARKET);
					}
					else {
						MarketTagButton.setText("Hide Tag: MARKET");
						tracePanel.showAlertsWithTag(AlertTag.MARKET);
					}
				}
			});
			
			RestBKTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestBKTagButton.isSelected()) {
						RestBKTagButton.setText("Show Tag: RESTAURANT_BK");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK);
					}
					else {
						RestBKTagButton.setText("Hide Tag: RESTAURANT_BK");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK);
					}
				}
			});
			
			RestKCTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestKCTagButton.isSelected()) {
						RestKCTagButton.setText("Show Tag: RESTAURANT_KC");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC);
					}
					else {
						RestKCTagButton.setText("Hide Tag: RESTAURANT_BK");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC);
					}
				}
			});
			
			RestLYTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestLYTagButton.isSelected()) {
						RestLYTagButton.setText("Show Tag: RESTAURANT_LY");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY);
					}
					else {
						RestLYTagButton.setText("Hide Tag: RESTAURANT_LY");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY);
					}
				}
			});
			
			RestMQTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestMQTagButton.isSelected()) {
						RestMQTagButton.setText("Show Tag: RESTAURANT_MQ");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ);
					}
					else{
						RestMQTagButton.setText("Hide Tag: RESTAURANT_MQ");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ);
					}
				}
			});
			
			RestSMTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestSMTagButton.isSelected()) {
						RestSMTagButton.setText("Show Tag: RESTAURANT_SM");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM);
					}
					else {
						RestSMTagButton.setText("Hide Tag: RESTAURANT_SM");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM);
					}
				}
			});
			
			CityTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(CityTagButton.isSelected()) {
						CityTagButton.setText("Show Tag: GENERAL CITY");
						tracePanel.hideAlertsWithTag(AlertTag.GENERAL_CITY);
					}
					else {
						CityTagButton.setText("Hide Tag: GENERAL CITY");
						tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);
					}
				}
			});
			
			PersonTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(PersonTagButton.isSelected()) {
						PersonTagButton.setText("Show Tag: PERSON");
						tracePanel.hideAlertsWithTag(AlertTag.PERSON);
					}
					else {
						PersonTagButton.setText("Hide Tag: PERSON");
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
					}
				}
			});
			
			
			this.setLayout(new FlowLayout());
			this.add(messageButton);
			this.add(infoButton);
			this.add(errorButton);
			this.add(CityTagButton);
			this.add(PersonTagButton);
			this.add(BankTagButton);
			this.add(MarketTagButton);
			this.add(RestBKTagButton);
			this.add(RestKCTagButton);
			this.add(RestLYTagButton);
			this.add(RestMQTagButton);
			this.add(RestSMTagButton);
			this.setMinimumSize(new Dimension(50, 600));
		}
	}
}
