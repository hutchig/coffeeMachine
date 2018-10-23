package com.ibm.coffee;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

@Path("vend")
@ApplicationScoped
public class CoffeeMachine implements Publisher<Vend>{

	static public final Queue<Vend> vends = new LinkedList<Vend>();
	private static CoffeeSubscription<Vend> subscription;

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String buy(@QueryParam("customer") String name, @QueryParam("drink") String drink) {
		try {
			Customer c = Customer.getCustomer(name);
			Drink d = Menu.stringToDrink(drink);
			Vend v = new Vend(c, d);
			vends.add(v);
			subscription.attemptVend();
			return "Purchase attempted:" + v;
		} catch (InvalidDrinkException e) {
			return "Invalid drink: " + drink + " requested";
		}
	}

	@Override
	public void subscribe(Subscriber<? super Vend> subscriber) {
		subscription = new CoffeeSubscription<Vend>(subscriber);
		subscriber.onSubscribe(subscription);		
	}
	
	static void screen(Vend v, String t) {
		try {
			WebSocket.sendText(v.customer, t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Vend getNextVend() {
		return vends.poll();
	}

	static boolean hasNextVend() {
		return !vends.isEmpty();
	}

	public static void requeueVend(Vend v) {
	    vends.add(v);	
	}
}
