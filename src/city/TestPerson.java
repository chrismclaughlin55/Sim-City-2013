package city;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import market.MyOrder;
import agent.Agent;

public class TestPerson extends Agent
{
        /*DATA MEMBERS*/
        String name;
        public double money;
        boolean ranonce = false;
        
        private List<Role> roles = new ArrayList<Role>(); //hold all possible roles (even inactive roles)
        public List<MyOrder> thingsToOrder = Collections.synchronizedList(new ArrayList<MyOrder>());;
        
        /*CONSTRUCTORS*/
        public TestPerson(String name) {
                this.name = name;
                
                MyOrder o1 = new MyOrder("Steak", 1);
        		MyOrder o2 = new MyOrder("Salad", 1);
        		MyOrder o3 = new MyOrder("Pizza", 1);
        		MyOrder o4 = new MyOrder("Chicken", 1);
        		thingsToOrder.add(o1);
        		thingsToOrder.add(o2);
        		thingsToOrder.add(o3);
        		thingsToOrder.add(o4);
        }
        
        
        /*MESSAGES*/
        public void msgAssignRole(Role role) {
                for (Role r : roles) {
                        if (r == role) {
                                r.setActive();
                                super.stateChanged();
                                return;
                        }
                }
                
                //If this part is reached, then 'role' is not in the list of roles
                roles.add(role);
                role.setActive();
                super.stateChanged();
        }
        
        /*SCHEDULER*/
        protected boolean pickAndExecuteAnAction() {
                //Iterate through the list of roles
                for (Role role : roles) {
                        //If a role is active, attempt to run its scheduler
                        if (role.isActive()) {
                                if (role.pickAndExecuteAnAction()) {
                                        return true;
                                }
                        }
                }
                
                return false;
        }
        
        /*METHODS TO BE USED FOR PERSON-ROLE INTERACTIONS*/
        protected void stateChanged() {
                super.stateChanged();
        }
        
        /*GETTERS*/
        public String getName() {
                return name;
        }
        
        public void setOrders() {
        	thingsToOrder.clear();
        	MyOrder o1 = new MyOrder("Steak", 1);
    		MyOrder o2 = new MyOrder("Salad", 1);
    		
    		thingsToOrder.add(o1);
    		thingsToOrder.add(o2);
    		
        	
        }
}
