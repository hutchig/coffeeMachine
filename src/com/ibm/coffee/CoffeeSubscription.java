package com.ibm.coffee;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

class CoffeeSubscription<T> implements Subscription {
	private final Subscriber<? super T> subscriber;
	private final AtomicBoolean cancelled = new AtomicBoolean(false);
	private final AtomicLong requests = new AtomicLong();
	
	CoffeeSubscription(Subscriber<? super T> subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void request(long n) {
		requests.addAndGet(n);
		attemptVend();
	}

	@Override
	public void cancel() {
		cancelled.getAndSet(true);
	}

	public void attemptVend() {
		while (requests.get() > 0 && CoffeeMachine.hasNextVend() && !cancelled.get()) {
			try {
				requests.decrementAndGet();
				subscriber.onNext((T) CoffeeMachine.getNextVend());
			} catch (Throwable e) {
				subscriber.onError(e);
			}
		}
	}

}