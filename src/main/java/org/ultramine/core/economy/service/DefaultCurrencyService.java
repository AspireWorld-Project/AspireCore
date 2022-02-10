package org.ultramine.core.economy.service;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.service.Service;

@Service
public interface DefaultCurrencyService {
	@Nonnull
	Currency getDefaultCurrency();
}
