package trace;


/**
 * These enums represent tags that group alerts together.  <br><br>
 * 
 * This is a separate idea from the {@link AlertLevel}.
 * A tag would group all messages from a similar source.  Examples could be: BANK_TELLER, RESTAURANT_ONE_WAITER,
 * or PERSON.  This way, the trace panel can sort through and identify all of the alerts generated in a specific group.
 * The trace panel then uses this information to decide what to display, which can be toggled.  You could have all of
 * the bank tellers be tagged as a "BANK_TELLER" group so you could turn messages from tellers on and off.
 * 
 * @author Keith DeRuiter
 *
 */
public enum AlertTag {
	PERSON,
	BUILDING,
	BANK,			//For the demo code where you make a new bank
	BANK_MANAGER,
	BANK_TELLER,
	BANK_CUSTOMER,
	APARTMENT,		//For the demo code where you make a new apartment
	BUS,
	BUS_STOP,
	HOME,			//For the demo code where you make a new home
	MARKET,			//For the demo code where you make a new market
	MARKET_CUSTOMER,
	MARKET_EMPLOYEE,
	MARKET_MANAGER,
	RESTAURANTBK,		//For the demo code where you make a new restaurantBK
	RESTAURANTBK_CASHIER,
	RESTAURANTBK_COOK,
	RESTAURANTBK_CUSTOMER,
	RESTAURANTBK_HOST,
	RESTAURANTBK_WAITER,
	RESTAURANTKC,		//For the demo code where you make a new restaurantKC
	RESTAURANTKC_CASHIER,
	RESTAURANTKC_COOK,
	RESTAURANTKC_CUSTOMER,
	RESTAURANTKC_HOST,
	RESTAURANTKC_WAITER,
	RESTAURANTLY,		//For the demo code where you make a new restaurantLY
	RESTAURANTLY_CASHIER,
	RESTAURANTLY_COOK,
	RESTAURANTLY_CUSTOMER,
	RESTAURANTLY_HOST,
	RESTAURANTLY_WAITER,
	RESTAURANTMQ,		//For the demo code where you make a new restaurantMQ
	RESTAURANTMQ_CASHIER,
	RESTAURANTMQ_COOK,
	RESTAURANTMQ_CUSTOMER,
	RESTAURANTMQ_HOST,
	RESTAURANTMQ_WAITER,
	RESTAURANTSM,		//For the demo code where you make a new restaurantSM
	RESTAURANTSM_CASHIER,
	RESTAURANTSM_COOK,
	RESTAURANTSM_CUSTOMER,
	RESTAURANTSM_HOST,
	RESTAURANTSM_WAITER,
	RESTAURANTCM,		//For the demo code where you make a new restaurantCM
	RESTAURANTCM_CASHIER,
	RESTAURANTCM_COOK,
	RESTAURANTCM_CUSTOMER,
	RESTAURANTCM_HOST,
	RESTAURANTCM_WAITER,
	GENERAL_CITY
}
