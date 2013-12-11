team15
======

SimCity201 Project Repository for CS 201 students


Objects (Function and visual representation)

#### CITY
+ PersonAgent (PersonAgentSprite): Exist in the city and home/apartment.
+ BuildingMediator: Base building class that every building needs to extend, have manager to set the building open/close and interact with the coming customers.
+ BusAgent: There is only one bus with one route; if there is people waiting at the bus stop/reaching his destination, the bus will stop and board/unload the passenger; otherwise the bus will pass the stop. Keep running all the time even if there is no passenger.  
+ BusStopAgent: Person walks to the nearest bus stop to take the bus. Bus stop sends/receives messages to/from the bus so that the bus will know where to stop.

#### MARKET
+ MarketManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave/assigns them to an employee. Access the market bank account.
+ MarketEmployee: Get the customer's order and serve the customer with his order.
+ MarketCustomer: Order food and buy a car in the market

#### RESTAURANT
+ Host: Manager of building that decides when to open and close a restaurant and talks to customers as they come in and leave/assigns them to a waiter.
+ Waiter: same as restaurant
+ Cook: Make order from market.
+ Cashier: Make payment to market for cook's order. Access the restaurant bank account.
+ Customer: same as restaurant
+ Customer and waiter addition panels can still be used. Interactions will be incomplete if Host, Cashier, or Cooks are missing. If the necessary employees are present, Customers and Waiters can be added and will run normally. These agents will not function properly once they leave the restaurant (this is really just a hack to allow testing of the restaurant by itself).

#### HOME
+ Person: Manager of building that lives in the building.
+ (No landlord for a home, no rent.)
+ Bed / Stove / Refrigerator / Couch: GUI stuff, person can sleep, cook, eat, sit and relax on the couch.

#### BANK
+ BankManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave/assigns them to a teller.
+ BankTeller: Get the information of the customer, make deposit/withdraw for the customer, process the loan request.
+ BankCustomer: Deposit/withdraw cash, apply a loan.
+ BankRobber: NON-NORM SCENARIO, rob the bank and leave to be a normal person

#### APARTMENT
+ Tenant: Have a room to live in, pay rent to landlord.
+ Landlord: Manager of the apartment building and lives inside the apartment he owns.
+ Room: There are 8 rooms for each apartment. Room 1 is reserved for landlord. Room has the same layout and functionality as home; each apartment has eight rooms.

#### Overall information:
+ Global timer
+ Time interval setting: as the input number increases, the animation works slower.



### Testing scenario description:
+ A.
+ 1. All workplaces are fully employed and will be open when all employees arrive.
+ 2. City at 0AM Friday. Persons with jobs will go to work at differnt time. Bank manager is the earliers to go to work.  All person agents have gui animation to leave home, take bus/walk/take car to work. 
+ 3. One not-working person living in one of the houses eats at home and then leave home.
+ 4. Person takes bus/cars to destination building, vehicles stop for pedestrians.
+ B.
+ 3. People may go to work by walking/taking a bus/driving a car.
+ C.
+ 1. Restauant order food from market.
+ 2. Market has "helicopter" for delivery.
+ 3. Restaurant call cashier to pay the invoice.
+ F.
+ 1. Each workplace has a closing time. Employees leave after closing time. Manager leaves at last and close the building.
+ 2. Restaurant host may want to go to market after market is closing. Host can not get in and leave.
+ G. 
+ 1. Market cannot delivery food to closed restaurant. Redeliver later and normally get payment from cashier.
+ J.
+ 1. All workplaces are fully employed. (Info from config file)
+ 2. Apartments and houses are avaliable for up to 54 people.
+ 3. All six restaurants from group members are integrated and works normally.
+ 4. There are 2 markets and 2 banks, fully employed.
+ 5&6. People can eat at home or make random decision for choosing one restaurant to eat.
+ 7. Two buses and multiple cars in the city.
+ 8. Vehicles stop for pedestrians when they are try to go across the road at any position.
+ K.
+ 1. Appropriate images for animation.
+ L.
+ 1. Buttons to test non-norm scenarios (e.g. fire, bank robber, etc). Click on building to zoom into the building.
+ 2&3. Able to add person from control panel with a job, home/apartment room and other infomation are automatically assigned.
+ 4. Vehicles only moves on the road. People go into a building from the door. People can take bus/car.
+ 5. Cars can make a U turn at intersections.
+ O.
+ 1. Create the bank robber from control panel.
+ 2. Bank robber robs the bank if the bank is open. Then the robber leaves with the money and hide and act like a normal person.
+ R.
+ 1. Some builidngs close at weekends. People get up 1 hour later that weekdays.
+ S.
+ 1. Restaurant and bank can fire employees and get changed.


### Starting state overall:
+ People are created and roles are assigned from the configuration file.
+ More people can be created from person creation panel with a job. When a person created, home/apartment room is assigned.
+ A clock is shown on the top left corner of the animation panel, starting from 0 AM Friday. All people are sleeping at home/apartment. Different jobs have different working time. Bank manager is the earliest.
+ Landlord always stays at his room and does not go out for work. He leaves apartment only when he needs to buy something from market.
+ Fire button click: all persons get fire message and do "Stop, Drop, and Roll" (basically only print message on console for now)

### Full disclosure:
+ No A* implementation. People may stand on top of each other or walk on top of buildings.
+ No car collision or car hitting pedestrians scenarios. (But do have design doc.)
+ People only take one of the bus to their destination buildings.
+ People may keep going to a closed building.

### Work distribution:
+ 1 Spencer Moran: home and apartment - extend base building class, contain agent and gui. Apartment includes multiple rooms whose layout and functionality are same as home. Implement landlord role. Unit testing the roles. Person agent interaction. Bank robber and other non-norm scenarios. Integrate restaurantSM.
+ 2	Chris McLaughlin: bank - extend base building class, contain agent and gui. Bank has roles of bank manager, bank teller, and bank customer (person goes into the bank and become the bank customer). Unit testing the roles. Integrate restaurntCM.
+ 3	Kartik Chillakanti: market - extend base building class,  contain agent and gui. Market has roles of market manager, market employee, and market customer (person goes into the market and become the market customer). Also accept order from restaurant cook, make invoice, deliver food to the restaurant. Unit testing the roles. Interaction between market and restaurants. Integrate restaurantKC.
+ 4	Lezi Yang: base building class and main city gui - all other buildings extend the base building class. Interact with person agent stuff to allow persons enter and leave a building. Construct city layout and animation. City grid construction for vehicles. Integrate restaurantLY.
+ 5	Michael Qian: base person agent - all roles extend base person agent. Persons also walk around the city or live in home/apartment. Bus/bus stop agent. Modify old version restaurant - extend the base building class, has roles, interact with market to order food and pay money. Add car with appropriate movement. Modify bus gui. Construct city grid for vehicle gui animations. Integrate restaurantMQ.
+ 6	Brian Kim : base person agent and bus/bus stop agent - bus interact with bus stop so that it knows which bus stop to go and board/unload the passengers. Bus gui only appears on the road. Construct configuration file to create persons. Modify bus and construct car. Non-norm scenarios. Integrate restaurantBK.
+ 
