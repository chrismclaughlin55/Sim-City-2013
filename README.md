team15
======

SimCity201 Project Repository for CS 201 students


Objects (Function and visual representation)
#### CITY
+ PersonAgent (PersonAgentSprite): Exist in the city and home/apartment.
+ BusAgent: There is only one bus with one route; if there is people waiting at the bus stop / reaching his destination, the bus will stop and board / unload the passenger; otherwise the bus will pass the stop.  
+ BusStopAgent: Send / receive messages to / from the bus so that the bus will know where to stop.

#### MARKET
+ MarketManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave / assigns them to an employee.
+ MarketEmployee: Get the customer's order and serve the customer with his order.

#### RESTAURANT
+ Host: Manager of building that decides when to open and close a restaurant and talks to customers as they come in and leave / assigns them to a waiter.
+ Waiter
+ Cook: Make order from market.
+ Cashier: Make payment to market for a cook's order.

#### HOME
+ Person: Manager of building that live in the building.
+ (No landlord for a home, no rent.)
+ Bed / Stove / Refrigerator / Couch: GUI stuff, person can sleep, cook, eat, sit and relax on the couch.

#### BANK
+ BankManager: Manager of building that decides when to open and close a bank and talks to customers as they come in and leave / assigns them to a teller.
+ BankTeller: Get the information of the customer, make deposit / withdraw for the customer, process the loan.

#### APARTMENT
+ Tenrant: Have a room to live in, pay the rent to landlord.
+ Landlord: Manager of the apartment building and lives inside the apartment.
+ Room: Room has the same layout and functionality as home; each apartment has eight rooms.

#### Overall information:
+ Global timer

#### Starting state overall:
+ People are created and roles are assigned from the configuration file.
