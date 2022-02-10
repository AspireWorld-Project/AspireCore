package org.ultramine.core.economy.service;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.holdings.HoldingsFactory;
import org.ultramine.core.service.Service;

@Service
public interface DefaultHoldingsProvider {
	@Nonnull
	HoldingsFactory getDefaultHoldingsFactory();
}
