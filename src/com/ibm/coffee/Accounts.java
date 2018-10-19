package com.ibm.coffee;

import java.io.IOException;

public interface Accounts {
	public Integer decrement( Customer who, int amount) throws NotEnoughFundsException, IOException;
	public Integer increment(Customer who, int amount) throws IOException;
	public Integer getBalance( Customer who );
}
