package bank;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.lang.Double;

import javax.swing.JFrame;

import trace.AlertLog;
import trace.AlertTag;
import bank.utilities.CustInfo;
import bankgui.BankCustomerGui;
import bankgui.BankGui;
import bankgui.TellerGui;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;
import city.interfaces.Bus;

public class Bank extends Building {
	public Semaphore enter = new Semaphore(1, true);
	public final static int CLOSINGTIME = 22;
	public BankGui bankGui;
	public BankManagerRole currentManager = null;
	Map<PersonAgent, CustomerRole> existingCustRoles;
	Map<PersonAgent, BankManagerRole> existingManagerRoles;
	Map<PersonAgent, TellerRole> existingTellerRoles;
	public static Map<PersonAgent, CustInfo> CustAccounts = new HashMap<PersonAgent, CustInfo>();
	public static Map<Building, java.lang.Double> BusinessAccounts = new HashMap<Building, java.lang.Double>();
	
	private boolean printed = false;

	public Bank(int xPos, int yPos, int width, int height, String name, BuildingType bank, MainGui mainGui, CityData cd) {
		super(xPos, yPos, width, height, name, bank, mainGui);
		cityData = cd;
		this.bankGui = new BankGui(this);
		bankGui.setTitle("Bank");
		bankGui.setVisible(false);
		bankGui.setResizable(false);
		bankGui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//mainGui.bankGui = this.bankGui;
		//mainGui.bankGui.bank = this;
		super.type = BuildingType.bank;
		existingCustRoles = new HashMap<PersonAgent, CustomerRole>();
		existingManagerRoles = new HashMap<PersonAgent, BankManagerRole>();
		existingTellerRoles = new HashMap<PersonAgent, TellerRole>();

		//accounts
		
	}
	
	@Override
	public void EnterBuilding(PersonAgent p, String roleRequest){
		try {
			enter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(roleRequest.equals("BankManager")){
			if(manager == null){
				manager = p;
			}
			if(p.equals(manager)){
				if(existingManagerRoles.get(p) != null){
					setOpen(p);
					p.msgAssignRole(existingManagerRoles.get(p));
					currentManager = existingManagerRoles.get(p);
					AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Bank is open for employees only");
				}
				else {
					
					existingManagerRoles.put(p, new BankManagerRole(p, this));
					setOpen(p);
					p.msgAssignRole(existingManagerRoles.get(p));
					currentManager = existingManagerRoles.get(p);
					BankManagerRole bRole = new BankManagerRole(p, this);
					existingManagerRoles.put(p, bRole);
					setOpen(p);
					p.print("bank is open? "+ isOpen);
					p.msgAssignRole(bRole);
					currentManager = bRole;
					cityData.buildings.get(18).manager = currentManager.getPerson();
					AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Bank is open for employees only");
				}
			}
		}

		if(roleRequest.equals("Customer")){
			if(isOpen() && this.currentManager != null){
				if(currentManager.tellerPresent()){
					if(existingCustRoles.get(p) != null){
						CustomerRole role = existingCustRoles.get(p);
						BankCustomerGui custGui = new BankCustomerGui(role);
						role.msgAddGui(custGui);
						p.msgAssignRole(role);
						p.bankInfo.customer = role;
						bankGui.animationPanel.addGui(custGui);
						currentManager.msgINeedService(role);
					}
					else{
						CustomerRole newRole = new CustomerRole(p);
						existingCustRoles.put(p, newRole);
						BankCustomerGui custGui = new BankCustomerGui(newRole);
						p.msgAssignRole(newRole);
						newRole.msgAddGui(custGui);
						p.bankInfo.customer = newRole;
						bankGui.animationPanel.addGui(custGui);
						currentManager.msgINeedService(newRole);
					}
				} else{
					System.out.println("teller present? " + currentManager.tellerPresent());
					p.exitBuilding();
				}
			}
		}
		if(roleRequest.equals("BankTeller")){
			if(isOpen()){
				if(existingTellerRoles.get(p) != null){
					TellerRole role = existingTellerRoles.get(p);
					TellerGui tellerGui = new TellerGui(existingTellerRoles.get(p), currentManager.tellers.size()+5);
					role.msgAddGui(tellerGui);
					p.msgAssignRole(role);
					role.msgAddGui(tellerGui);
					bankGui.animationPanel.addGui(tellerGui);

					currentManager.msgAddTeller(role);
					role.msgAddManager(currentManager);
				}
				else{
					TellerRole newRole = new TellerRole(p);
					existingTellerRoles.put(p, newRole);
					TellerGui tellerGui = new TellerGui(newRole, currentManager.tellers.size()+5);
					p.msgAssignRole(newRole);
					newRole.msgAddGui(tellerGui);
					bankGui.animationPanel.addGui(tellerGui);
					currentManager.msgAddTeller(newRole);
					newRole.msgAddManager(currentManager);
				}
				if(!printed) {
					AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Bank is fully employed");
					AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Bank is open now");
					printed = true;
				}
			}
			else p.exitBuilding();
		}
		if (roleRequest.equals("BankRobber")) {
			if (isOpen()) {
				BankRobber robber = new BankRobber(p, this);
				p.msgAssignRole(robber);
			}
		}
		enter.release();
	}
	public void directDeposit(PersonAgent sender, PersonAgent reciever, double amount){
		CustInfo send = getAccount(sender);
		CustInfo recieve = getAccount(reciever);
		send.moneyInAccount -= amount;
		recieve.moneyInAccount += amount;
		CustAccounts.put(sender, send);
		CustAccounts.put(reciever, recieve);
	}

	public CustInfo getAccount(PersonAgent person){
		CustInfo personInfo = CustAccounts.get(person);
		if(personInfo == null){
			personInfo = new CustInfo(person.bankInfo);
		}
		return personInfo;
	}

	public void test(){
		PersonAgent p1 = new PersonAgent("Manager");
		p1.startThread();
		manager = p1;
		EnterBuilding(p1, "BankManager");

		PersonAgent p2 = new PersonAgent("Teller");
		p2.startThread();
		EnterBuilding(p2, "BankTeller");


		PersonAgent p3 = new PersonAgent("Customer");
		p3.bankInfo = new CustInfo(p3.getName(), p3, null);
		p3.bankInfo.moneyInAccount = 200;
		p3.bankInfo.depositAmount = 200;
		p3.cash=500;
		p3.startThread();
		EnterBuilding(p3, "Customer");

	}

	public JFrame getBuildingGui() {
		return bankGui;
	}


	//BUSINESS BANK ACCOUNT METHODS
	public void addBusinessAccount(Building b, java.lang.Double startMoney){
		BusinessAccounts.put(b, startMoney);
		System.out.println(b.type + " added account");
	}

	public double payPerson(Building b, PersonAgent p, double amount){
		java.lang.Double businessMoney = BusinessAccounts.get(b);
		CustInfo personAccount = CustAccounts.get(p);
		businessMoney = businessMoney - amount;
		personAccount.moneyInAccount += amount;
		System.out.println(b.type + "is paying " +p.getName()+" $"+amount+" for work");
		CustAccounts.put(p, personAccount);
		BusinessAccounts.put(b, businessMoney);
		return businessMoney;

	}
	public double depositMoney(Building b, double amount){
		java.lang.Double account = BusinessAccounts.get(b);
		System.out.println(b.type + " depositing amount = "+ amount);
		System.out.println(b.type + " old balance: " +account);
		account+= amount;
		System.out.println(b.type + " new balance: "+ account);
		return account;
	}
	public double withdrawMoney(Building b, double amount){
		double businessAccount = BusinessAccounts.get(b);
		if(amount<businessAccount){
			System.out.println(b.type+ " is withdrawing "+ amount + " with " + (businessAccount - amount) + " left");
			BusinessAccounts.put(b, (java.lang.Double)(businessAccount - amount));
		}
		else{
			System.out.println(b.type+ " is withdrawing "+ businessAccount + " with " + 0 + " left");
			BusinessAccounts.put(b, (java.lang.Double)(0.0));
		}
		return BusinessAccounts.get(b);
	}
}
