package org.ultramine.core.economy.holdings;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.economy.account.Account;
import org.ultramine.core.economy.exception.AccountTypeNotSupportedException;

public interface HoldingsFactory {
	@Nonnull
	Holdings createHoldings(@Nonnull Account account, @Nonnull Currency currency)
			throws AccountTypeNotSupportedException;
}
