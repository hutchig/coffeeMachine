package com.ibm.coffee;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.reactive.streams.CompletionRunner;
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
			
			ReactiveStreams.fromPublisher(coffeeMachine).to(bank).run();
			
			//2 ReactiveStreams.fromPublisher(coffeeMachine).peek(System.out::println).to(bank).run();
		
			// ReactiveStreams.fromPublisher(coffeeMachine).peek(System.out::println).to(bank).run();
			//3 ReactiveStreams.fromPublisher(coffeeMachine).filter(d->d.drink!=Drink.TEA).to(bank).run();

			//4 ReactiveStreams.fromPublisher(coffeeMachine).to(remoteBank).run();
		    // ReactiveStreams.fromPublisher(coffeeMachine).peek(v->System.out.println(v.customer + " is in the kitchen.")).to(remoteBank).run();
			// ReactiveStreams.generate(CoffeeMachine::getNextVend).to(bank).run();
            
            // CompletionStage<Void> microserviceA = ReactiveStreams.fromPublisher(coffeeMachine).peek(System.out::println).to(bank).run();
            // CompletionStage<Void> microserviceB = ReactiveStreams.fromPublisher(coffeeMachine).peek(System.out::println).to(bank).run();
            // Runnable then;
			// CompletionStage<Void> result = microserviceA.runAfterBoth(microserviceB, then);
			
					
			running = true;
		}

		
	}
}
