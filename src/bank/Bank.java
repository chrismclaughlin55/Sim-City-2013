package bank;

import java.util.HashMap;
import java.util.Map;

import bank.utilities.CustInfo;
import bankgui.BankCustomerGui;
import bankgui.BankGui;
import bankgui.TellerGui;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;
import city.Building.BuildingType;

public class Bank extends Building {
	public BankGui bankGui;
	BankManagerRole currentManager = null;
	Map<PersonAgent, CustomerRole> existingCustRoles;
	Map<PersonAgent, BankManagerRole> existingManagerRoles;
	Map<PersonAgent, TellerRole> existingTellerRoles;
	public Bank(int xPos, int yPos, int width, int height, String name, BuildingType bank, MainGui mainGui, CityData cd) {
		super(xPos, yPos, width, height, name, bank, mainGui);
		cityData = cd;
		this.bankGui = new BankGui();
		mainGui.bankGui = this.bankGui;
		mainGui.bankGui.bank = this;
		super.type = BuildingType.bank;
		existingCustRoles = new HashMap<PersonAgent, CustomerRole>();
		existingManagerRoles = new HashMap<PersonAgent, BankManagerRole>();
		existingTellerRoles = new HashMap<PersonAgent, TellerRole>();
	}
	@Override
	public void EnterBuilding(PersonAgent p, String roleRequest){
		if(roleRequest.equals("BankManager")){
			if(manager == null){
				manager = p;
			}
			if(p.equals(manager)){
				if(existingManagerRoles.get(p) != null){
				setOpen(p);
				p.msgAssignRole(existingManagerRoles.get(p));
				currentManager = existingManagerRoles.get(p);

				}
				else {

				existingManagerRoles.put(p, new BankManagerRole(p));
				setOpen(p);
				p.msgAssignRole(existingManagerRoles.get(p));
				currentManager = existingManagerRoles.get(p);
				cityData.buildings.get(18).manager = currentManager.getPerson();
				p.print("current manager is " + currentManager.getName());
				}
			}
		}
		if(roleRequest.equals("Customer")){
			if(isOpen()){
				if(existingCustRoles.get(p) != null){
					CustomerRole role = existingCustRoles.get(p);
					BankCustomerGui custGui = new BankCustomerGui(role);
					role.msgAddGui(custGui);
					p.msgAssignRole(role);
					bankGui.animationPanel.addGui(custGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgINeedService(role);
				}
				else{
					CustomerRole newRole = new CustomerRole(p);
					existingCustRoles.put(p, newRole);
					BankCustomerGui custGui = new BankCustomerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(custGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgINeedService(newRole);
				}
			}
		}
		if(roleRequest.equals("BankTeller")){
			if(isOpen()){
				if(existingTellerRoles.get(p) != null){
					p.print("teller role existed for me");
					TellerRole role = existingTellerRoles.get(p);
					TellerGui tellerGui = new TellerGui(existingTellerRoles.get(p));
					role.msgAddGui(tellerGui);
					p.msgAssignRole(role);
					role.msgAddGui(tellerGui);
					bankGui.animationPanel.addGui(tellerGui);
					currentManager.msgAddTeller(role);
					role.msgAddManager(currentManager);
				}
				else{
					p.print("assigned teller gui");
					TellerRole newRole = new TellerRole(p);
					existingTellerRoles.put(p, newRole);
					TellerGui tellerGui = new TellerGui(newRole);
					p.msgAssignRole(newRole);
					bankGui.animationPanel.addGui(tellerGui);
					p.print("current manager is " + currentManager.getName());
					currentManager.msgAddTeller(newRole);
					newRole.msgAddManager(currentManager);
				}
			}
		}
	}
	public BankManagerRole getManager(){
		return currentManager;
	}
	
	/*public void test(){
		System.out.println("testing bank");
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
		
	}*/
}
