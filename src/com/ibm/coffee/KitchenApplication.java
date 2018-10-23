package com.ibm.coffee;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.reactive.streams.ReactiveStreams;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

@ApplicationPath("rest")
public class KitchenApplication extends Application {

	public static boolean running = false;

	public static void run() {
		if (!running) {

			Publisher<Vend> coffeeMachine = new CoffeeMachine();

			Subscriber<Vend> bank = Bank.getBank();
			Subscriber<Vend> remoteBank = RemoteBank.getBank();

			// ReactiveStreams.generate(CoffeeMachine::getNextVend).to(bank).run();

			// ReactiveStreams.fromPublisher(coffeeMachine).to(bank).run();

			// ReactiveStreams.fromPublisher(coffeeMachine).peek(v->System.out.println(v.customer + " is in the kitchen.")).to(remoteBank).run();

            // ReactiveStreams.fromPublisher(coffeeMachine).peek(System.out::println).to(remoteBank).run();

			// ReactiveStreams.fromPublisher(coffeeMachine).filter(d->d.drink!=Drink.TEA).to(remoteBank).run();

			 ReactiveStreams.fromPublisher(coffeeMachine).to(remoteBank).run();

			running = true;
		}
	}
}
