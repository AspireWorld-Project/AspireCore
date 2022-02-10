package org.ultramine.core.economy.account;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.economy.exception.CurrencyNotFoundException;
import org.ultramine.core.economy.exception.CurrencyNotSupportedException;
import org.ultramine.core.economy.holdings.Holdings;

public interface Account {
	@Nonnull
	String getName();

	@Nonnull
	Collection<? extends Currency> getSupportedCurrencies();

	boolean isCurrencySupported(@Nonnull Currency currency);

	@Nonnull
	Currency getSupportedCurrency(@Nonnull String id) throws CurrencyNotFoundException, CurrencyNotSupportedException;

	@Nonnull
	Holdings getHoldings(@Nonnull Currency currency) throws CurrencyNotSupportedException;

	@Nonnull
	Holdings getDefaultHoldings();
}
