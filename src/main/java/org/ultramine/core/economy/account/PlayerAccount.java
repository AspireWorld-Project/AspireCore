package org.ultramine.core.economy.account;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.mojang.authlib.GameProfile;

@ThreadSafe
public interface PlayerAccount extends Account {
	@Nonnull
	GameProfile getProfile();
}
