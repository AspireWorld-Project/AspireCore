package org.ultramine.bukkit;

import org.ultramine.bukkit.api.BukkitRegistry;
import org.ultramine.core.service.ServiceDelegate;
import org.ultramine.core.service.ServiceProviderLoader;

import net.minecraftforge.common.MinecraftForge;

public class BukkitRegistryLoader implements ServiceProviderLoader<BukkitRegistry> {
	private BukkitRegistry instance;

	@Override
	public void load(ServiceDelegate<BukkitRegistry> service) {
		BukkitRegistry impl = new BukkitRegistryImpl();
		instance = impl;
		MinecraftForge.EVENT_BUS.register(impl);
		service.setProvider(impl);
	}

	@Override
	public void unload() {
		MinecraftForge.EVENT_BUS.unregister(instance);
	}
}
