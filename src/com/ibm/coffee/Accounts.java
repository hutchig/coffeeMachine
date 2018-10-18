package com.ibm.coffee;

public interface Accounts {
	public void decrement( Customer who, Integer amount) throws NotEnoughFundsException;
	public void increment(Customer who, Integer amount);
	public Integer getBalance( Customer who );
}
