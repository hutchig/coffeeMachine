package com.ibm.coffee;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class RemoteBank implements Subscriber<Vend> {

	private static RemoteBank instance;
	private Subscription subscription;
	private Client client;
	private WebTarget withdraw;
	private WebTarget deposit;
	private WebTarget root;

	private static final String TARGET_ROOT = "http://localhost:9090/Bank/rest/accounts/";

	private RemoteBank() {
		instance = this;

		client = ClientBuilder.newClient();
		root = client.target(TARGET_ROOT);
		withdraw = root.path("withdraw");
		deposit = root.path("deposit");

	}

	@Override
	public void onComplete() {
		client.close();
	}

	@Override
	public void onError(Throwable arg0) {
		client.close();
	}

	@Override
	public void onNext(Vend v) {
		if (v.customer.equals(Customer.getCustomer("Gordon")) && v.drink == Drink.TEA) {
			try {

				String result = withdraw.queryParam("customer", "Gordon").queryParam("amount", "1")
						.request(MediaType.TEXT_PLAIN).get(String.class);

				if (!"REFUSED".equals(result)) {
					CoffeeMachine.screen(v,
							"Enjoy your " + v.drink + " " + v.customer + ", $" + Integer.parseInt(result) + " left.");
				} else {
					WebSocket.pauseVending(v.customer);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
		subscription.request(1);
	}

	@Override
	public void onSubscribe(Subscription sub) {
		if (subscription == null)
			subscription = sub;
		subscription.request(1);
	}

	public static RemoteBank getBank() {
		return instance != null ? instance : new RemoteBank();
	}

}
