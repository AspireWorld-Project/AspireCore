package org.ultramine.core.economy.exception;

@SuppressWarnings("serial")
public class NegativeAmountException extends EconomyException {
	public NegativeAmountException(double amount) {
		super("ultramine.economy.fail.negative_amount", amount);
	}
}
