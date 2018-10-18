package com.ibm.coffee;

import java.util.HashMap;

public class Customer {
	public String name;

	private static HashMap<String, Customer> people = new HashMap<String, Customer>();

	public Customer(String name) {
		this.name = name;
	}

	public static Customer getCustomer(String name) {

		Customer result = people.get(name);
		
		if (result == null) {
			result = new Customer(name);
			people.put(name, result);
		}
		return result;

	}

	public String toString() {
		return name;
	}
}
