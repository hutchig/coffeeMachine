package com.ibm.coffee;

public interface Accounts {
	public Integer decrement( Customer who, int amount) throws NotEnoughFundsException;
	public Integer increment(Customer who, int amount);
	public Integer getBalance( Customer who );
}
