package org.ultramine.bukkit;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.ultramine.core.service.Service;

import com.google.common.base.Function;

import net.minecraft.entity.player.EntityPlayerMP;

@Service
public interface CraftPlayerCache {
	CraftPlayer getOrCreate(EntityPlayerMP player);

	void updateReferences(EntityPlayerMP player);

	void forEach(Function<CraftPlayer, Void> consumer);
}
