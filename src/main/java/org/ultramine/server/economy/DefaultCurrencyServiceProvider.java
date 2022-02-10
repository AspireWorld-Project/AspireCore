package org.ultramine.server.economy;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.economy.service.DefaultCurrencyService;

public class DefaultCurrencyServiceProvider implements DefaultCurrencyService {
	private final Currency currency;

	public DefaultCurrencyServiceProvider(Currency currency) {
		this.currency = currency;
	}

	@Nonnull
	@Override
	public Currency getDefaultCurrency() {
		return currency;
	}
}
