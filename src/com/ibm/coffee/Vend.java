package com.ibm.coffee;

import java.util.concurrent.CompletableFuture;

public class Vend {
	Vend(Customer c, Drink d) {
		customer = c;
		drink = d;
		cf = new CompletableFuture<>();
	}

	Customer customer;
	Drink drink;
	CompletableFuture<String> cf;
	
	public String toString(){
		return "Vend " + customer + ":" + drink;
	}
}
