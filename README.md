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

#### HOME
+ Person: Manager of building that lives in the building.
+ (No landlord for a home, no rent.)
+ Bed / Stove / Refrigerator / Couch: GUI stuff, person can sleep, cook, eat, sit and relax on the couch.

#### BANK
+ BankManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave/assigns them to a teller.
+ BankTeller: Get the information of the customer, make deposit/withdraw for the customer, process the loan request.
+ BankCustomer: Deposit/withdraw cash, apply a loan.

#### APARTMENT
+ Tenant: Have a room to live in, pay rent to landlord.
+ Landlord: Manager of the apartment building and lives inside the apartment he owns.
+ Room: There are 8 rooms for each apartment. Room 1 is reserved for landlord. Room has the same layout and functionality as home; each apartment has eight rooms.

#### Overall information:
+ Global timer
+ Time interval setting: as the input number increases, the animation works slower.



### Testing scenario description:
+ Test bus scenario (BusTest under city.test)
+ 1. One person goes to a bus stop and wait at the stop. Bus stop adds the person to its peopleWating list.
+ 2. One bus comes and tells the bus stop that it is here.
+ 3. Person gets on the bus
+ Test restaurant cashier and market scenario (CashierTest under restaurantMQ.test)
+ 1. One open market does not have any other order to serve.
+ 2. One cashier from one open restaurant does not have any other bill.
+ Test restaurant cashier and waiter scenario (CashierTest under restaurantMQ.test)
+ 1. One normal waiter of one open restaurant sends the cashier the check request for one customer.
+ 2. Cashier generates the check and gives it to the waiter.
+ Test restaurant cashier, waiter and one normal customer scenario (CashierTest under restaurantMQ.test)
+ 1. One waiter from one open restaurant sends the cashier the check request for one customer.
+ 2. Cashier generates the check and gives it to the waiter.
+ 3. Customer pays to the cashier for order of steak.
+ 4. Cashier lets the customer to leave.
+ Test restaurant cashier, waiter and one non-normative customer scenario (CashierTest under restaurantMQ.test)
+ 1. One waiter from one open restaurant sends the cashier the check request for one customer.
+ 2. Cashier generates the check and gives it to the waiter.
+ 3. Customer pays the cashier for order of steak but not fully.
+ 4. Cashier lets the customer to leave.
+ Test restautant cashier and one non-normative market scenario (CashierTest under restaurantMQ.test)
+ 1. One cashier from one open restaurant does not have enough money to fully pay


### Starting state overall:
+ People are created and roles are assigned from the configuration file.
+ More people can be created from person creation panel with a job. When a person created, home/apartment room is assigned.
+ A clock is shown on the top left corner of the animation panel, starting from 0 AM. All people are sleeping at home/apartment. Different jobs have different working time. Bank manager is the earliest.
+ Landlord always stays at his room and does not go out for work. He leaves apartment only when he needs to buy something from market.
+ Fire button click: all persons get fire message and do "Stop, Drop, and Roll" (basically only print message on console for now)

### Full disclosure:
+ Sometimes a person may just miss the bus and he will run to the next stop to get the bus so that he do not need to wait for the next bus to come.
+ There are six individual restaurants in the city such that each restaurant has its own working roles, animation, etc. All implemented by the same restaurant class.
+ In animation panel, persons may stand on top of or walk through each other, or walk on top of buildings/bus.
+ There is only one bus with one route.
+ Control panel with add customer and waiter stuff for the restaurant is not removed. But they are set to disable.

### Work distribution:
+ 1 Spencer Moran: home and apartment - extend base building class, contain agent and gui. Apartment includes multiple rooms whose layout and functionality are same as home. Implement landlord role. Unit testing the roles.
+ 2	Chris McLaughlin: bank - extend base building class, contain agent and gui. Bank has roles of bank manager, bank teller, and bank customer (person goes into the bank and become the bank customer). Unit testing the roles.
+ 3	Kartik Chillakanti: market - extend base building class,  contain agent and gui. Market has roles of market manager, market employee, and market customer (person goes into the market and become the market customer). Also accept order from restaurant cook, make invoice, deliver food to the restaurant. Unit testing the roles.
+ 4	Lezi Yang: base building class and main city gui - all other buildings extend the base building class. Interact with person agent stuff to allow persons enter and leave a building. Construct city layout and animation.
+ 5	Michael Qian: base person agent - all roles extend base person agent. Persons also walk around the city or live in home/apartment. Bus/bus stop agent. Modify old version restaurant - extend the base building class, has roles, interact with market to order food and pay money.
+ 6	Brian Kim : base person agent and bus/bus stop agent - bus interact with bus stop so that it knows which bus stop to go and board/unload the passengers. Bus gui only appears on the road. Construct configuration file to create persons.

### v2 plan
+ Add car to person agent, person needs to make loan to buy a car.
+ Change layout of the city to be grid blocks of certain types to restrict movement of cars, buses, and delivery trucks to roads.
+ In the person creation panel, allow to place a person you want to create, click on the screen to start the person
+ Overall revampled animations for everything.
+ Home maintenance.
+ Bank robbery and identity theft.
+ (A*) Car accident.
+ Emergency state for person agent, such as fire, earthquake, etc. All people will stop their current schedule and react to the emergency.
+ People can die because of car accident, emergency, etc.
+ Add "open sign" for restaurants/market/bank.
+ Add business account for each building - direct deposit for managers to bank; also managers pay employees from building money.
