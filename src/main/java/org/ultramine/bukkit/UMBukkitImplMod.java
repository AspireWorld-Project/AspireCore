package org.ultramine.bukkit;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.ultramine.bukkit.api.BukkitRegistry;
import org.ultramine.bukkit.handler.ChunkPopulateHandler;
import org.ultramine.bukkit.handler.CoreEventHandler;
import org.ultramine.bukkit.handler.EntityEventHandler;
import org.ultramine.bukkit.handler.InternalEventHandler;
import org.ultramine.bukkit.handler.PlayerEventHandler;
import org.ultramine.bukkit.handler.WorldEventHandler;
import org.ultramine.bukkit.integration.permissions.b2c.SuperPermsReplacer;
import org.ultramine.core.service.InjectService;
import org.ultramine.core.service.ServiceManager;
import org.ultramine.server.UltraminePlugin;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

public class UMBukkitImplMod extends DummyModContainer {
	@InjectService
	private static ServiceManager services;
	@InjectService
	private static BukkitRegistry bukkitRegistry;
	private CraftServer bserver;
	public LoadController controller = null;
	private static UMBukkitImplMod instance = null;

	public UMBukkitImplMod() {
		super(new ModMetadata());
		instance = this;
		ModMetadata meta = getMetadata();
		meta.modId = "UltramineServerBukkit";
		meta.name = "Ultramine Server Bukkit IMPL";
		meta.version = "1.3";
	}

	public static UMBukkitImplMod getInstance() {
		return instance;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		this.controller = controller;
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent e) {
		services.register(BukkitRegistry.class, new BukkitRegistryLoader(), 0);
	}

	@Subscribe
	public void init(FMLInitializationEvent e) {
		bukkitRegistry
				.injectPlugin("org.ultramine.bukkit.injected.internal.",
						new PluginDescriptionFile("ultramine_core_plugin", "1.0.0",
								"org.ultramine.bukkit.injected.internal.InjectedUltramineCorePlugin")
										.setSoftDepend("Vault"));
	}

	@Subscribe
	public void serverAboutToStart(FMLServerAboutToStartEvent e) {
		setupLoggers();
		bserver = new CraftServer(e.getServer(), e.getServer().getConfigurationManager());
		services.register(CraftPlayerCache.class, new CraftPlayerCacheImpl(bserver), 0);
		register(new CoreEventHandler(bserver));
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(bserver));
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler(bserver));
		MinecraftForge.EVENT_BUS.register(new InternalEventHandler());
		register(new PlayerEventHandler(bserver));
		GameRegistry.registerWorldGenerator(new ChunkPopulateHandler(bserver), 0);
		EventImplProgress.isEventImplemented(org.bukkit.event.block.BlockBreakEvent.class);
	}

	@Subscribe
	public void serverStarting(FMLServerStartingEvent e) {
		bserver.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD);
	}

	private static void setupLoggers() {
		java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
		global.setUseParentHandlers(false);

		for (java.util.logging.Handler handler : global.getHandlers()) {
			global.removeHandler(handler);
		}

		global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());
	}

	private static void register(Object handler) {
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	@NetworkCheckHandler
	public boolean networkCheck(Map<String, String> map, Side side) {
		return true;
	}

	@Override
	public File getSource() {
		return UltraminePlugin.location;
	}

	@Override
	public List<String> getOwnedPackages() {
		return ImmutableList.of("org.ultramine.bukkit", "org.bukkit", "org.spigotmc", "net.minecraftforge.cauldron");
	}
}
