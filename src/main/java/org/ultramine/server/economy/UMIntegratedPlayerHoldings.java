package org.ultramine.server.economy;

import javax.annotation.Nonnull;

import org.ultramine.core.economy.Currency;
import org.ultramine.core.economy.account.Account;
import org.ultramine.core.economy.holdings.MemoryHoldings;
import org.ultramine.server.data.player.PlayerData;
import org.ultramine.server.util.GlobalExecutors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class UMIntegratedPlayerHoldings extends MemoryHoldings {
	// Current implementation is a legacy of old ultramine versions

	private final PlayerData playerData;

	public UMIntegratedPlayerHoldings(@Nonnull Account account, @Nonnull Currency currency,
			@Nonnull PlayerData playerData) {
		super(account, currency);
		this.playerData = playerData;
	}

	public void writeToNBT(@Nonnull NBTTagCompound nbt) {
		nbt.setLong("b", getBalanceInternal());
	}

	public void readFromNBT(@Nonnull NBTTagCompound nbt) {
		setBalanceInternal(nbt.getLong("b"));
	}

	@Override
	protected void onHoldingsBalanceChange() {
		if (Thread.currentThread() == MinecraftServer.getServer().getServerThread()) {
			save();
		} else {
			GlobalExecutors.syncServer().execute(this::save);
		}
	}

	private void save() {
		playerData.core().setHoldingsInternal(this);
		playerData.save();
	}
}
