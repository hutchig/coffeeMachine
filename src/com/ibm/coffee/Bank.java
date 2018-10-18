package com.ibm.coffee;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("accounts")
public class Bank implements Accounts {

	Map<Customer, Integer> vault = getVault();

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String accounts() {
		String result = "";

		for (Customer person : vault.keySet()) {
			result += person + " $" + vault.get(person) + "<br>";
		}
		return result;

	}

	@Override
	public void decrement(Customer who, Integer debit) throws NotEnoughFundsException {

		Integer balance = vault.get(who);
		if (balance < debit) {
			throw new NotEnoughFundsException();
		}
		vault.put(who, balance - debit);
	}

	@Override
	public void increment(Customer who, Integer credit) {
		Integer balance = vault.get(who);
		vault.put(who, balance + credit);
	}

	@Override
	public Integer getBalance(Customer who) {
		return vault.get(who);
	}

	private static Map<Customer, Integer> getVault() {
		Map<Customer, Integer> m = new HashMap<>();
		m.put(new Customer("Bob"), 0);
		m.put(new Customer("Sheila"), 0);
		m.put(new Customer("Fred"), 0);
		return m;
	}

}
