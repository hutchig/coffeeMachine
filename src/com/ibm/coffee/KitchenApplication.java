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

			boolean split = false;

			Publisher<Vend> coffeeMachine = new CoffeeMachine();
			
			Subscriber<Vend> bank;
			if (!split) {
				bank = Bank.getBank();
			} else {
				bank = RemoteBank.getBank();
			}
			
			
			//ReactiveStreams.generate(CoffeeMachine::getNextVend).to(bank).run();
			
			ReactiveStreams.fromPublisher(coffeeMachine).to(bank).run();
			
			running = true;
		}
	}
}
