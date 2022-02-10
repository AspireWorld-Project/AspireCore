package org.ultramine.bukkit.api;

import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

public interface IInventoryTransactionProvider {
	void onOpen(CraftHumanEntity who);

	void onClose(CraftHumanEntity who);

	List<HumanEntity> getViewers();
}