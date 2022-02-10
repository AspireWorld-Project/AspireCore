package org.ultramine.core.economy.service;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.ultramine.core.economy.account.PlayerAccount;
import org.ultramine.core.service.Service;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;

@Service
@ThreadSafe
public interface Economy {
	@Nonnull
	PlayerAccount getPlayerAccount(@Nonnull GameProfile profile);

	@Nonnull
	default PlayerAccount getPlayerAccount(@Nonnull EntityPlayerMP player) {
		return getPlayerAccount(player.getGameProfile());
	}
}
