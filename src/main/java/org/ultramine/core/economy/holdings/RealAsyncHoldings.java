package org.ultramine.core.economy.holdings;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.ultramine.server.util.GlobalExecutors;

public class RealAsyncHoldings extends AbstractAsyncHoldings {
	public RealAsyncHoldings(Holdings holdings) {
		super(holdings);
	}

	@Nonnull
	@Override
	protected <T> CompletableFuture<T> execute(@Nonnull Supplier<T> action) {
		return CompletableFuture.supplyAsync(action, GlobalExecutors.cachedIO());
	}
}
