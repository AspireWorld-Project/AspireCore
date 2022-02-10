package org.ultramine.server.economy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.ultramine.core.economy.account.PlayerAccount;
import org.ultramine.core.economy.service.Economy;

import com.mojang.authlib.GameProfile;

@ThreadSafe
public class UMEconomy implements Economy {
	private final Map<UUID, PlayerAccountImpl> accounts = new ConcurrentHashMap<>();

	@Nonnull
	@Override
	public PlayerAccount getPlayerAccount(@Nonnull GameProfile profile) {
		profile.getClass(); // NPE
		return accounts.computeIfAbsent(profile.getId(), k -> new PlayerAccountImpl(profile));
	}
}
