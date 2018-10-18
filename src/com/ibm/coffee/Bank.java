package com.ibm.coffee;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("accounts")
@ApplicationScoped
public class Bank implements Accounts {

	private static Map<Customer, Integer> vault = getVault();
	private static Bank instance;

	public Bank() {
		instance = this;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String accounts() {
		String result = "";

		for (Customer person : vault.keySet()) {
			result += person + " $" + vault.get(person) + "<br>";
		}
		return result;

	}

	@GET
	@Path("/deposit") @Produces(MediaType.TEXT_HTML)
	public String deposit(@QueryParam("customer") String name, @QueryParam("amount") String amount) {
		Customer customer = Customer.getCustomer(name);
		increment(customer, Integer.parseInt(amount));
		return "New balance for " + customer + " is $" + getBalance(customer);
	}

	
	
	@Override
	public Integer decrement(Customer who, int debit) throws NotEnoughFundsException {

		int balance = getBalance(who);
		if (balance < debit) {
			throw new NotEnoughFundsException();
		}
		vault.put(who, balance - debit);
		return getBalance(who);
	}

	@Override
	public Integer increment(Customer who, int credit) {
		Integer balance = vault.get(who);
		vault.put(who, balance + credit);
		return getBalance(who);
	}

	@Override
	public Integer getBalance(Customer who) {
		vault.putIfAbsent(who, 0);
		return vault.get(who);
	}

	private static Map<Customer, Integer> getVault() {
		Map<Customer, Integer> m = new HashMap<>();

		m.put(Customer.getCustomer("Bob"), 10);
		m.put(Customer.getCustomer("Sheila"), 10);
		m.put(Customer.getCustomer("Fred"), 10);
		m.put(Customer.getCustomer("Gordon"), 10);

		return m;

	}

	static Bank getBank() {
		return instance != null ? instance : new Bank();
	}

	public String toString() {
		return accounts();
	}

}
