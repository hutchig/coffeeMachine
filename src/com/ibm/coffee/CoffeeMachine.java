package com.ibm.coffee;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jmx.snmp.ThreadContext;

@Path("vend")
@ApplicationScoped
public class CoffeeMachine  {

	static public final Queue<Vend> vends = new LinkedList<Vend>();

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String buy(@QueryParam("customer") String name, @QueryParam("drink") String drink) {

		try {
			Customer c = Customer.getCustomer(name);
			Drink d = Menu.stringToDrink(drink);
			Vend v = new Vend(c, d);
			vends.add(v);
			return "Purchase queued: " + vends;
		} catch (InvalidDrinkException e) {
			return "Invalid drink: " + drink + " requested";
		}

// The code below was commented out when KitchenApplication composed a reactive stream.
//		try {
//			Integer cost = Menu.getDrinkCost(drink);
//			
//			Bank bank = Bank.getBank();
//			Integer balance = bank.decrement(customer, cost);
//			
//			return "Enjoy your " + drink + " " + customer + ", $" + balance + " left.";
//
//		} catch (InvalidDrinkException e) {
//			return "Invalid drink: " + drink + " requested";
//		} catch ( NotOnTheMenuException e) {
//			return "We are fresh out of " + drink + ".";
//		} catch (NotEnoughFundsException e) {
//			return "Charge account " + name + ", not enough funds for a " + drink + ".";
//		}

	}

	static Vend getNextVend() {

		Vend v = vends.poll();

		while (v == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return new Vend(Customer.getCustomer("IGNORE"), Drink.TEA);
			}
			System.out.println("No pending vends" + vends);
			v = vends.poll();
		}
		return v;

	}

	static void screen(Vend v, String t) {
		try {
			WebSocket.sendText(v.customer, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
