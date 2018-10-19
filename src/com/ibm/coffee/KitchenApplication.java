package com.ibm.coffee;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.reactive.streams.ReactiveStreams;

@ApplicationPath("rest")
public class KitchenApplication extends Application {

	public static boolean running = false;

	public static void run() {
		if (!running) {
			ReactiveStreams.generate(CoffeeMachine::getNextVend).to(Bank.getBank()).run();
			running = true;
		}
	}
}
