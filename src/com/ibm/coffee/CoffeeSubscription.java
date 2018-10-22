package com.ibm.coffee;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class CoffeeSubscription<T> implements Subscription {
	private final Subscriber<? super T> subscriber;
	private final AtomicBoolean terminated = new AtomicBoolean(false);
	private final AtomicLong requests = new AtomicLong();
	private final AtomicReference<Throwable> error = new AtomicReference<>();

	CoffeeSubscription(Subscriber<? super T> subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void request(long n) {
		requests.addAndGet(n);
		attemptVend();
	}

	public void attemptVend() {
		while (requests.get() > 0 && CoffeeMachine.hasNextVend() && !terminated.get()) {
			try {
				requests.decrementAndGet();
				subscriber.onNext((T) CoffeeMachine.getNextVend());
			} catch (Throwable e) {
				subscriber.onError(e);
			}
		}
	}

	@Override
	public void cancel() {
		terminated.getAndSet(true);
	}

}