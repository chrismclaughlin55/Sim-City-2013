team15
======

SimCity201 Project Repository for CS 201 students


Objects (Function and visual representation)

#### CITY
+ PersonAgent (PersonAgentSprite): Exist in the city and home/apartment.
+ BuildingMediator: Base building class that every building needs to extend, have manager to set the building open / close and interact with the coming customers.
+ BusAgent: There is only one bus with one route; if there is people waiting at the bus stop / reaching his destination, the bus will stop and board / unload the passenger; otherwise the bus will pass the stop. Keep running all the time even if there is no passenger.  
+ BusStopAgent: Person walks to the nearest bus stop to take the bus. Bus stop sends / receives messages to / from the bus so that the bus will know where to stop.

#### MARKET
+ MarketManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave / assigns them to an employee. Access the market bank account.
+ MarketEmployee: Get the customer's order and serve the customer with his order.
+ MarketCustomer: Order food and buy a car in the market

#### RESTAURANT
+ Host: Manager of building that decides when to open and close a restaurant and talks to customers as they come in and leave / assigns them to a waiter.
+ Waiter
+ Cook: Make order from market.
+ Cashier: Make payment to market for cook's order. Access the restaurant bank account.
+ Customer

#### HOME
+ Person: Manager of building that lives in the building.
+ (No landlord for a home, no rent.)
+ Bed / Stove / Refrigerator / Couch: GUI stuff, person can sleep, cook, eat, sit and relax on the couch.

#### BANK
+ BankManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave / assigns them to a teller.
+ BankTeller: Get the information of the customer, make deposit / withdraw for the customer, process the loan request.
+ BankCustomer: Deposit / withdraw cash, apply a loan.

#### APARTMENT
+ Tenant: Have a room to live in, pay rent to landlord.
+ Landlord: Manager of the apartment building and lives inside the apartment he owns.
+ Room: Room has the same layout and functionality as home; each apartment has eight rooms.

#### Overall information:
+ Global timer

#### Starting state overall:
+ People are created and roles are assigned from the configuration file.
+ Also create people from person creation panel with a job.
+ When a person created, home / apartment room is assigned.


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
+ Add "open sign" for restaurants / market / bank.
+ Add business account for each building - direct deposit for managers to bank; also managers pay employees from building money.
