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
	private int W = 200;
	private int H = 35;
	
	ControlPanel controlPanel;
	
	//============================ TUTORIAL ==========================================
	//You declare a TracePanel just like any other variable, and it extends JPanel, so you use
	//it in the same way.
	TracePanel tracePanel;
	//================================================================================


	public DemoLauncher() {
		this.tracePanel = new TracePanel();
		this.controlPanel = new ControlPanel(tracePanel);
		tracePanel.setPreferredSize(new Dimension(800, 445));
		
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
		
		tracePanel.showAlertsWithTag(AlertTag.GENERAL_CITY);	//only show non-norm (eg. car collsion, robber)
		tracePanel.hideAlertsWithTag(AlertTag.PERSON);
		tracePanel.showAlertsWithTag(AlertTag.BANK);
		tracePanel.showAlertsWithTag(AlertTag.MARKET);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM);
		tracePanel.hideAlertsWithTag(AlertTag.BUS);
		tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
		tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.BANK_MANAGER);
		tracePanel.showAlertsWithTag(AlertTag.BANK_TELLER);
		tracePanel.showAlertsWithTag(AlertTag.MARKET_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.MARKET_EMPLOYEE);
		tracePanel.showAlertsWithTag(AlertTag.MARKET_MANAGER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_WAITER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_WAITER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_WAITER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_WAITER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_WAITER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_CASHIER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_COOK);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_CUSTOMER);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_HOST);
		tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_WAITER);
		
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
		JToggleButton BankTagButton;
		JToggleButton BankManagerTagButton;
		JToggleButton BankTellerTagButton;
		JToggleButton BankCustTagButton;
		JToggleButton BusTagButton;
		JToggleButton BusStopTagButton;
		JToggleButton MarketTagButton;
		JToggleButton MarketCustTagButton;
		JToggleButton MarketEmployeeTagButton;
		JToggleButton MarketManagerTagButton;
		JToggleButton RestBKTagButton;
		/*JToggleButton BKCashierTagButton;
		JToggleButton BKCookTagButton;
		JToggleButton BKCustTagButton;
		JToggleButton BKHostTagButton;
		JToggleButton BKWaiterTagButton;*/
		JToggleButton BKRolesTagsButton;
		JToggleButton RestKCTagButton;
		JToggleButton KCCashierTagButton;
		JToggleButton KCCookTagButton;
		JToggleButton KCCustTagButton;
		JToggleButton KCHostTagButton;
		JToggleButton KCWaiterTagButton;
		JToggleButton RestLYTagButton;
		/*JToggleButton LYCashierTagButton;
		JToggleButton LYCookTagButton;
		JToggleButton LYCustTagButton;
		JToggleButton LYHostTagButton;
		JToggleButton LYWaiterTagButton;*/
		JToggleButton LYRolesTagsButton;
		JToggleButton RestMQTagButton;
		/*JToggleButton MQCashierTagButton;
		JToggleButton MQCookTagButton;
		JToggleButton MQCustTagButton;
		JToggleButton MQHostTagButton;
		JToggleButton MQWaiterTagButton;*/
		JToggleButton MQRolesTagsButton;
		JToggleButton RestSMTagButton;
		/*JToggleButton SMCashierTagButton;
		JToggleButton SMCookTagButton;
		JToggleButton SMCustTagButton;
		JToggleButton SMHostTagButton;
		JToggleButton SMWaiterTagButton;*/
		JToggleButton SMRolesTagsButton;
		JToggleButton RestCMTagButton;
		/*JToggleButton CMCashierTagButton;
		JToggleButton CMCookTagButton;
		JToggleButton CMCustTagButton;
		JToggleButton CMHostTagButton;
		JToggleButton CMWaiterTagButton;*/
		JToggleButton CMRolesTagsButton;
		JToggleButton CityTagButton;
		
		public ControlPanel(final TracePanel tracePanel) {
			this.tp = tracePanel;
			messageButton = new JToggleButton("Hide Level: MESSAGE");
			errorButton = new JToggleButton("Hide Level: NON-NORM");
			infoButton = new JToggleButton("Hide Level: INFO");
			/*warningButton = new JToggleButton("Hide Level: WARNING");
			debugButton = new JButton("Hide Level: DEBUG");*/
			
			PersonTagButton = new JToggleButton("Show Tag: PERSON");
			BankTagButton = new JToggleButton("Hide Tag: BANK");
			BankManagerTagButton = new JToggleButton("Hide Tag: BANK MANAGER");
			BankTellerTagButton = new JToggleButton("Hide Tag: BANK TELLER");
			BankCustTagButton = new JToggleButton("Hide Tag: BANK CUSTOMER");
			BusTagButton = new JToggleButton("Show Tag: BUS");
			BusStopTagButton = new JToggleButton("Show Tag: BUS STOP");
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
			BKRolesTagsButton = new JToggleButton("Hide Tags: REST_BK ROLES");
			KCCashierTagButton = new JToggleButton("Hide Tag: REST_KC CASHIER");
			KCCookTagButton = new JToggleButton("Hide Tag: REST_KC COOK");
			KCCustTagButton = new JToggleButton("Hide Tag: REST_KC CUST");
			KCHostTagButton = new JToggleButton("Hide Tag: REST_KC HOST");
			KCWaiterTagButton = new JToggleButton("Hide Tag: REST_KC WAITER");
			LYRolesTagsButton = new JToggleButton("Hide Tags: REST_LY ROLES");
			MQRolesTagsButton = new JToggleButton("Hide Tags: REST_MQ ROLES");
			SMRolesTagsButton = new JToggleButton("Hide Tags: REST_SM ROLES");
			CMRolesTagsButton = new JToggleButton("Hide Tags: REST_CM ROLES");
			
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
						BankTagButton.setText("Hide Tag: BANK");
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
			
			RestCMTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(RestCMTagButton.isSelected()) {
						RestCMTagButton.setText("Show Tag: RESTAURANT_CM");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM);
					}
					else {
						RestCMTagButton.setText("Hide Tag: RESTAURANT_CM");
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
						PersonTagButton.setText("Hide Tag: PERSON");
						tracePanel.showAlertsWithTag(AlertTag.PERSON);
					}
					else {
						PersonTagButton.setText("Show Tag: PERSON");
						tracePanel.hideAlertsWithTag(AlertTag.PERSON);
					}
				}
			});
			
			BusTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BusTagButton.isSelected()) {
						BusTagButton.setText("Hide Tag: BUS");
						tracePanel.showAlertsWithTag(AlertTag.BUS);
					}
					else {
						BusTagButton.setText("Show Tag: BUS");
						tracePanel.hideAlertsWithTag(AlertTag.BUS);
					}
				}
			});
			
			BusStopTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BusStopTagButton.isSelected()) {
						BusStopTagButton.setText("Hide Tag: BUS STOP");
						tracePanel.showAlertsWithTag(AlertTag.BUS_STOP);
					}
					else {
						BusStopTagButton.setText("Show Tag: BUS STOP");
						tracePanel.hideAlertsWithTag(AlertTag.BUS_STOP);
					}
				}
			});
			
			BankManagerTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BankManagerTagButton.isSelected()) {
						BankManagerTagButton.setText("Show Tag: BANK MANAGER");
						tracePanel.hideAlertsWithTag(AlertTag.BANK_MANAGER);
					}
					else {
						BankManagerTagButton.setText("Hide Tag: BANK MANAGER");
						tracePanel.showAlertsWithTag(AlertTag.BANK_MANAGER);
					}
				}
			});
			
			BankTellerTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BankTellerTagButton.isSelected()) {
						BankTellerTagButton.setText("Show Tag: BANK TELLER");
						tracePanel.hideAlertsWithTag(AlertTag.BANK_TELLER);
					}
					else {
						BankTellerTagButton.setText("Hide Tag: BANK TELLER");
						tracePanel.showAlertsWithTag(AlertTag.BANK_TELLER);
					}
				}
			});
			
			BankCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BankCustTagButton.isSelected()) {
						BankCustTagButton.setText("Show Tag: BANK CUSTOMER");
						tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
					}
					else {
						BankCustTagButton.setText("Hide Tag: BANK CUSTOMER");
						tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
					}
				}
			});
			
			MarketManagerTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(MarketManagerTagButton.isSelected()) {
						MarketManagerTagButton.setText("Show Tag: MARKET MANAGER");
						tracePanel.hideAlertsWithTag(AlertTag.MARKET_MANAGER);
					}
					else {
						MarketManagerTagButton.setText("Hide Tag: MARKET MANAGER");
						tracePanel.showAlertsWithTag(AlertTag.MARKET_MANAGER);
					}
				}
			});
			
			MarketEmployeeTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(MarketEmployeeTagButton.isSelected()) {
						MarketEmployeeTagButton.setText("Show Tag: MARKET EMPLOYEE");
						tracePanel.hideAlertsWithTag(AlertTag.MARKET_EMPLOYEE);
					}
					else {
						MarketEmployeeTagButton.setText("Hide Tag: MARKET EMPLOYEE");
						tracePanel.showAlertsWithTag(AlertTag.MARKET_EMPLOYEE);
					}
				}
			});
			
			MarketCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(MarketCustTagButton.isSelected()) {
						MarketCustTagButton.setText("Show Tag: MARKET CUSTOMER");
						tracePanel.hideAlertsWithTag(AlertTag.MARKET_CUSTOMER);
					}
					else {
						MarketCustTagButton.setText("Hide Tag: MARKET CUSTOMER");
						tracePanel.showAlertsWithTag(AlertTag.MARKET_CUSTOMER);
					}
				}
			});
			
			KCHostTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(KCHostTagButton.isSelected()) {
						KCHostTagButton.setText("Show Tag: REST_KC HOST");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC_HOST);
					}
					else {
						KCHostTagButton.setText("Hide Tag: REST_KC HOST");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_HOST);
					}
				}
			});
			
			KCWaiterTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(KCWaiterTagButton.isSelected()) {
						KCWaiterTagButton.setText("Show Tag: REST_KC WAITER");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC_WAITER);
					}
					else {
						KCWaiterTagButton.setText("Hide Tag: REST_KC WAITER");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_WAITER);
					}
				}
			});
			
			KCCashierTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(KCCashierTagButton.isSelected()) {
						KCCashierTagButton.setText("Show Tag: REST_KC CASHIER");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC_CASHIER);
					}
					else {
						KCCashierTagButton.setText("Hide Tag: REST_KC CASHIER");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_CASHIER);
					}
				}
			});
			
			KCCookTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(KCCookTagButton.isSelected()) {
						KCCookTagButton.setText("Show Tag: REST_KC COOK");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC_COOK);
					}
					else {
						KCCookTagButton.setText("Hide Tag: REST_KC COOK");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_COOK);
					}
				}
			});
			
			KCCustTagButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(KCCustTagButton.isSelected()) {
						KCCustTagButton.setText("Show Tag: REST_KC CUST");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTKC_CUSTOMER);
					}
					else {
						KCCustTagButton.setText("Hide Tag: REST_KC CUST");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTKC_CUSTOMER);
					}
				}
			});
			
			BKRolesTagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(BKRolesTagsButton.isSelected()) {
						BKRolesTagsButton.setText("Show Tags: REST_BK ROLES");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK_CASHIER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK_COOK);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK_CUSTOMER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK_HOST);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTBK_WAITER);
					}
					else {
						BKRolesTagsButton.setText("Hide Tags: REST_BK ROLES");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_CASHIER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_COOK);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_CUSTOMER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_HOST);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTBK_WAITER);
					}
				}
			});
			
			LYRolesTagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(LYRolesTagsButton.isSelected()) {
						LYRolesTagsButton.setText("Show Tags: REST_LY ROLES");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY_CASHIER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY_COOK);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY_CUSTOMER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY_HOST);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTLY_WAITER);
					}
					else {
						LYRolesTagsButton.setText("Hide Tags: REST_LY ROLES");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_CASHIER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_COOK);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_CUSTOMER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_HOST);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTLY_WAITER);
					}
				}
			});
			
			MQRolesTagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(MQRolesTagsButton.isSelected()) {
						MQRolesTagsButton.setText("Show Tags: REST_MQ ROLES");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ_CASHIER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ_COOK);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ_CUSTOMER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ_HOST);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTMQ_WAITER);
					}
					else {
						MQRolesTagsButton.setText("Hide Tags: REST_MQ ROLES");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_CASHIER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_COOK);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_CUSTOMER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_HOST);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTMQ_WAITER);
					}
				}
			});
			
			SMRolesTagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(SMRolesTagsButton.isSelected()) {
						SMRolesTagsButton.setText("Show Tags: REST_SM ROLES");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM_CASHIER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM_COOK);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM_CUSTOMER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM_HOST);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTSM_WAITER);
					}
					else {
						SMRolesTagsButton.setText("Hide Tags: REST_SM ROLES");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_CASHIER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_COOK);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_CUSTOMER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_HOST);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTSM_WAITER);
					}
				}
			});
			
			CMRolesTagsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(CMRolesTagsButton.isSelected()) {
						CMRolesTagsButton.setText("Show Tags: REST_CM ROLES");
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCM_CASHIER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCM_COOK);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCM_CUSTOMER);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCM_HOST);
						tracePanel.hideAlertsWithTag(AlertTag.RESTAURANTCM_WAITER);
					}
					else {
						CMRolesTagsButton.setText("Hide Tags: REST_CM ROLES");
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_CASHIER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_COOK);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_CUSTOMER);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_HOST);
						tracePanel.showAlertsWithTag(AlertTag.RESTAURANTCM_WAITER);
					}
				}
			});
			
			this.setLayout(null);
			messageButton.setBounds(0, 0, W, H);
			this.add(messageButton);
			infoButton.setBounds(200, 0, W, H);
			this.add(infoButton);
			errorButton.setBounds(400, 0, W, H);
			this.add(errorButton);
			BankTagButton.setBounds(0, 30, W, H);
			this.add(BankTagButton);
			BankManagerTagButton.setBounds(200, 30, W, H);
			this.add(BankManagerTagButton);
			BankTellerTagButton.setBounds(400, 30, W, H);
			this.add(BankTellerTagButton);
			BankCustTagButton.setBounds(0, 60, W, H);
			this.add(BankCustTagButton);
			MarketTagButton.setBounds(200, 60, W, H);
			this.add(MarketTagButton);
			MarketManagerTagButton.setBounds(400, 60, W, H);
			this.add(MarketManagerTagButton);
			MarketEmployeeTagButton.setBounds(0, 90, W, H);
			this.add(MarketEmployeeTagButton);
			MarketCustTagButton.setBounds(200, 90, W, H);
			this.add(MarketCustTagButton);
			RestKCTagButton.setBounds(400, 90, W, H);
			this.add(RestKCTagButton);
			KCHostTagButton.setBounds(0, 120, W, H);
			this.add(KCHostTagButton);
			KCWaiterTagButton.setBounds(200, 120, W, H);
			this.add(KCWaiterTagButton);
			KCCashierTagButton.setBounds(400, 120, W, H);
			this.add(KCCashierTagButton);
			KCCookTagButton.setBounds(0, 150, W, H);
			this.add(KCCookTagButton);
			KCCustTagButton.setBounds(200, 150, W, H);
			this.add(KCCustTagButton);
			RestBKTagButton.setBounds(400, 150, W, H);
			this.add(RestBKTagButton);
			BKRolesTagsButton.setBounds(0, 180, W, H);
			this.add(BKRolesTagsButton);
			RestLYTagButton.setBounds(200, 180, W, H);
			this.add(RestLYTagButton);
			LYRolesTagsButton.setBounds(400, 180, W, H);
			this.add(LYRolesTagsButton);
			RestMQTagButton.setBounds(0, 210, W, H);
			this.add(RestMQTagButton);
			MQRolesTagsButton.setBounds(200, 210, W, H);
			this.add(MQRolesTagsButton);
			RestSMTagButton.setBounds(400, 210, W, H);
			this.add(RestSMTagButton);
			SMRolesTagsButton.setBounds(0, 240, W, H);
			this.add(SMRolesTagsButton);
			RestCMTagButton.setBounds(200, 240, W, H);
			this.add(RestCMTagButton);
			CMRolesTagsButton.setBounds(400, 240, W, H);
			this.add(CMRolesTagsButton);
			CityTagButton.setBounds(0, 270, W, H);
			this.add(CityTagButton);
			PersonTagButton.setBounds(200, 270, W, H);
			this.add(PersonTagButton);
			BusTagButton.setBounds(400, 270, W, H);
			this.add(BusTagButton);
			BusStopTagButton.setBounds(0, 300, W, H);
			this.add(BusStopTagButton);
			this.setMinimumSize(new Dimension(50, 600));
		}
	}
}
