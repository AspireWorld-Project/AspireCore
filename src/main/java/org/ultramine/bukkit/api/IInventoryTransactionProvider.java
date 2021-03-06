package org.ultramine.bukkit.api;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

import java.util.List;

public interface IInventoryTransactionProvider {
	void onOpen(CraftHumanEntity who);

	void onClose(CraftHumanEntity who);

	List<HumanEntity> getViewers();
}