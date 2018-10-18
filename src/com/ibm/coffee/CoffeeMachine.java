package com.ibm.coffee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("buy")
public class CoffeeMachine implements Vend {

	@Override
	@GET
	@Path("/parms")
	@Produces(MediaType.TEXT_HTML)
	public String buy(@QueryParam("name") String name, @QueryParam("drink") String drink) {

		Customer c = Customer.getCustomer(name);

		try {
			Integer cost = getDrinkCost(drink);
		} catch (InvalidDrinkException e) {
			return "Invalid drink: " + drink + " requested";
		} catch ( NotOnTheMenuException e) {
			return "We are fresh out of " + drink + ".";
				
		}
		
		
		return "Enjoy your " + drink + " " + c;
		
	}

	public Integer getDrinkCost(String drinkName) throws InvalidDrinkException, NotOnTheMenuException {

		Drink d = null;
		if (drinkName != null) {
			try {
				d = Drink.valueOf(drinkName.toUpperCase());
			} catch (IllegalArgumentException x) {
				throw new InvalidDrinkException();
			}
		}

		if( Menu.hasDrink(d) ) {
			return Menu.getCost(d);
		}else {
			throw new InvalidDrinkException();
		}
	}

}
