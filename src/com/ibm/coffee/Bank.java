package com.ibm.coffee;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Path("accounts")
@ApplicationScoped
public class Bank implements Accounts, Subscriber<Vend> {

	private static Map<Customer, Integer> vault = getVault();
	private static Bank instance;
	private Subscription subscription;

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
		return result + "\n";

	}

	@GET
	@Path("/deposit")
	@Produces(MediaType.TEXT_HTML)
	public String deposit(@QueryParam("customer") String name, @QueryParam("amount") String amount)
			throws NumberFormatException, IOException {
		Customer customer = Customer.getCustomer(name);
		increment(customer, Integer.parseInt(amount));
		return "New balance for " + customer + " is $" + getBalance(customer) + "\n";
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
	public Integer increment(Customer who, int credit) throws IOException {
		Integer balance = vault.get(who);
		vault.put(who, balance + credit);

		WebSocket.resumeVending(who);

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

	@Override
	public void onComplete() {
	}

	@Override
	public void onError(Throwable t) {
	}

	@Override
	public void onSubscribe(Subscription sub) {
		setSubscription(sub);
		subscription.request(100);
	}
	
	private void setSubscription(Subscription sub) {
		if (subscription == null)subscription = sub;
	}

	@Override
	public void onNext(Vend v) {
		try {

			if (!v.customer.equals(Customer.getCustomer("IGNORE"))) {			
				Integer cost = Menu.getDrinkCost(v.drink);
				Integer balance = decrement(v.customer, cost);
				CoffeeMachine.screen(v, "Enjoy your " + v.drink + " " + v.customer + ", $" + balance + " left.");
			}
			
			subscription.request(1);

		} catch (NotOnTheMenuException e) {
			CoffeeMachine.screen(v, "We are fresh out of " + v.drink + ".");
		} catch (NotEnoughFundsException e) {
			CoffeeMachine.screen(v, "Charge account " + v.customer + ", not enough funds for a " + v.drink + ".");
			try {
				WebSocket.pauseVending(v.customer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
