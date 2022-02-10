package org.ultramine.core.economy.exception;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.economy.account.Account;

@SuppressWarnings("serial")
public class CurrencyNotSupportedException extends EconomyException {
	@Nonnull
	private final Account account;
	@Nonnull
	private final Currency currency;

	public CurrencyNotSupportedException(@Nonnull Account account, @Nonnull Currency currency) {
		super("ultramine.economy.fail.currency_not_supported", account.getName(), currency.getId());
		this.account = account;
		this.currency = currency;
	}

	@Nonnull
	public Account getAccount() {
		return account;
	}

	public Currency getCurrency() {
		return currency;
	}
}
