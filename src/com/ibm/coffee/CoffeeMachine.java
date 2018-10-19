package com.ibm.coffee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("vend")
public class CoffeeMachine implements Vend {

	@Override
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String buy(@QueryParam("customer") String name, @QueryParam("drink") String drink) {

		Customer customer = Customer.getCustomer(name);
		
		try {
			Integer cost = getDrinkCost(drink);
			Bank bank = Bank.getBank();
			Integer balance = bank.decrement(customer, cost);
			return "Enjoy your " + drink + " " + customer + ", $" + balance + " left.";

		} catch (InvalidDrinkException e) {
			return "Invalid drink: " + drink + " requested";
		} catch ( NotOnTheMenuException e) {
			return "We are fresh out of " + drink + ".";
		} catch (NotEnoughFundsException e) {
			return "Charge account " + name + ", not enough funds for a " + drink + ".";
		}
		
	
		
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
