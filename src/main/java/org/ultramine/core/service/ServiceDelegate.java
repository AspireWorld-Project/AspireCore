package org.ultramine.core.service;

import javax.annotation.Nonnull;

import org.ultramine.server.service.NotResolvedServiceProvider;

public interface ServiceDelegate<T> {
	void setProvider(@Nonnull T obj);

	@Nonnull
	T getProvider();

	@Nonnull
	@SuppressWarnings("unchecked")
	default T asService() {
		return (T) this;
	}

	default boolean isResolved() {
		return !(getProvider() instanceof NotResolvedServiceProvider);
	}
}
