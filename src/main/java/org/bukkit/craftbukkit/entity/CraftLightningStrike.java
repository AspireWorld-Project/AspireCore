package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
	public CraftLightningStrike(final CraftServer server,
			final net.minecraft.entity.effect.EntityLightningBolt entity) {
		super(server, entity);
	}

	@Override
	public boolean isEffect() { // TODO
		// return ((net.minecraft.entity.effect.EntityLightningBolt)
		// getHandle()).isEffect;
		return false;
	}

	@Override
	public net.minecraft.entity.effect.EntityLightningBolt getHandle() {
		return (net.minecraft.entity.effect.EntityLightningBolt) entity;
	}

	@Override
	public String toString() {
		return "CraftLightningStrike";
	}

	@Override
	public EntityType getType() {
		return EntityType.LIGHTNING;
	}
}
