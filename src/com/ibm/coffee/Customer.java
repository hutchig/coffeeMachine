package com.ibm.coffee;

import java.util.HashMap;

public class Customer {
	public String name;

	private static HashMap<String, Customer> people = new HashMap<String, Customer>();

	private Customer(String name) {
		this.name = name.intern();
	}

	public static Customer getCustomer(String name) {

		Customer result = people.get(name);
		
		if (result == null) {
			result = new Customer(name);
			people.put(result.name, result);
		}
		return result;

	}

	public String toString() {
		return name;
	}
}
