/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */

package net.minecraftforge.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.*;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.BooleanEntry;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiConfigEntries.SelectValueEntry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.*;

/**
 * This is the base GuiConfig screen class that all the other Forge-specific
 * config screens will be called from. Since Forge has multiple config files I
 * thought I would use that opportunity to show some of the ways that the config
 * GUI system can be extended to create custom config GUIs that have additional
 * features over the base functionality of just displaying Properties and
 * ConfigCategories.
 *
 * The concepts implemented here are: - using custom IConfigEntry objects to
 * define child-screens that have specific Properties listed - using custom
 * IConfigEntry objects to define a dummy property that can be used to generate
 * new ConfigCategory objects - defining the configID string for a GuiConfig
 * object so that the config changed events will be posted when that GuiConfig
 * screen is closed (the configID string is optional; if it is not defined the
 * config changed events will be posted when the top-most GuiConfig screen is
 * closed, eg when the parent is null or is not an instance of GuiConfig) -
 * overriding the IConfigEntry.enabled() method to control the enabled state of
 * one list entry based on the value of another entry - overriding the
 * IConfigEntry.onGuiClosed() method to perform custom actions when the screen
 * that owns the entry is closed (in this case a new ConfigCategory is added to
 * the Configuration object)
 *
 * The config file structure looks like this: forge.cfg (general settings all in
 * one category) forgeChunkLoading.cfg - Forge (category) - defaults (category)
 * - [optional mod override categories]...
 *
 * The GUI structure is this: Base Screen - General Settings (from forge.cfg) -
 * Chunk Loader Settings (from forgeChunkLoading.cfg) - Defaults (these elements
 * are listed directly on this screen) - Mod Overrides - Add New Mod Override -
 * Mod1 - Mod2 - etc.
 *
 * Other things to check out: ForgeModContainer.syncConfig()
 * ForgeModContainer.onConfigChanged() ForgeChunkManager.syncConfigDefaults()
 * ForgeChunkManager.loadConfiguration()
 */
public class ForgeGuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ForgeConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class ForgeConfigGui extends GuiConfig {
		public ForgeConfigGui(GuiScreen parentScreen) {
			super(parentScreen, getConfigElements(), "Forge", false, false,
					I18n.format("forge.configgui.forgeConfigTitle"));
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<>();
			list.add(new DummyCategoryElement("forgeCfg", "forge.configgui.ctgy.forgeGeneralConfig",
					GeneralEntry.class));
			list.add(new DummyCategoryElement("forgeChunkLoadingCfg", "forge.configgui.ctgy.forgeChunkLoadingConfig",
					ChunkLoaderEntry.class));
			return list;
		}

		/**
		 * This custom list entry provides the General Settings entry on the Minecraft
		 * Forge Configuration screen. It extends the base Category entry class and
		 * defines the IConfigElement objects that will be used to build the child
		 * screen.
		 */
		public static class GeneralEntry extends CategoryEntry {
			@SuppressWarnings("rawtypes")
			public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
				super(owningScreen, owningEntryList, prop);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected GuiScreen buildChildScreen() {
				// This GuiConfig object specifies the configID of the object and as such will
				// force-save when it is closed. The parent
				// GuiConfig object's entryList will also be refreshed to reflect the changes.
				return new GuiConfig(owningScreen,
						new ConfigElement(ForgeModContainer.getConfig().getCategory(Configuration.CATEGORY_GENERAL))
								.getChildElements(),
						owningScreen.modID, Configuration.CATEGORY_GENERAL,
						configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
						configElement.requiresMcRestart() || owningScreen.allRequireMcRestart,
						GuiConfig.getAbridgedConfigPath(ForgeModContainer.getConfig().toString()));
			}
		}

		/**
		 * This custom list entry provides the Forge Chunk Manager Config entry on the
		 * Minecraft Forge Configuration screen. It extends the base Category entry
		 * class and defines the IConfigElement objects that will be used to build the
		 * child screen.
		 */
		public static class ChunkLoaderEntry extends CategoryEntry {
			@SuppressWarnings("rawtypes")
			public ChunkLoaderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
				super(owningScreen, owningEntryList, prop);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected GuiScreen buildChildScreen() {
				List<IConfigElement> list = new ArrayList<>();

				list.add(new DummyCategoryElement("forgeChunkLoadingModCfg",
						"forge.configgui.ctgy.forgeChunkLoadingModConfig", ModOverridesEntry.class));
				list.addAll(new ConfigElement(ForgeChunkManager.getDefaultsCategory()).getChildElements());

				// This GuiConfig object specifies the configID of the object and as such will
				// force-save when it is closed. The parent
				// GuiConfig object's propertyList will also be refreshed to reflect the
				// changes.
				return new GuiConfig(owningScreen, list, owningScreen.modID, "chunkLoader",
						configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
						configElement.requiresMcRestart() || owningScreen.allRequireMcRestart,
						GuiConfig.getAbridgedConfigPath(ForgeChunkManager.getConfig().toString()),
						I18n.format("forge.configgui.ctgy.forgeChunkLoadingConfig"));
			}
		}

		/**
		 * This custom list entry provides the Mod Overrides entry on the Forge Chunk
		 * Loading config screen. It extends the base Category entry class and defines
		 * the IConfigElement objects that will be used to build the child screen. In
		 * this case it adds the custom entry for adding a new mod override and lists
		 * the existing mod overrides.
		 */
		public static class ModOverridesEntry extends CategoryEntry {
			@SuppressWarnings("rawtypes")
			public ModOverridesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
				super(owningScreen, owningEntryList, prop);
			}

			/**
			 * This method is called in the constructor and is used to set the childScreen
			 * field.
			 */
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected GuiScreen buildChildScreen() {
				List<IConfigElement> list = new ArrayList<>();

				list.add(new DummyCategoryElement("addForgeChunkLoadingModCfg",
						"forge.configgui.ctgy.forgeChunkLoadingAddModConfig", AddModOverrideEntry.class));
				for (ConfigCategory cc : ForgeChunkManager.getModCategories()) {
					list.add(new ConfigElement(cc));
				}

				return new GuiConfig(owningScreen, list, owningScreen.modID,
						configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
						configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, owningScreen.title,
						I18n.format("forge.configgui.ctgy.forgeChunkLoadingModConfig"));
			}

			/**
			 * By overriding the enabled() method and checking the value of the "enabled"
			 * entry this entry is enabled/disabled based on the value of the other entry.
			 */
			@SuppressWarnings("rawtypes")
			@Override
			public boolean enabled() {
				for (IConfigEntry entry : owningEntryList.listEntries) {
					if (entry.getName().equals("enabled") && entry instanceof BooleanEntry)
						return Boolean.valueOf(entry.getCurrentValue().toString());
				}

				return true;
			}

			/**
			 * Check to see if the child screen's entry list has changed.
			 */
			@Override
			public boolean isChanged() {
				if (childScreen instanceof GuiConfig) {
					GuiConfig child = (GuiConfig) childScreen;
					return child.entryList.listEntries.size() != child.initEntries.size()
							|| child.entryList.hasChangedEntry(true);
				}
				return false;
			}

			/**
			 * Since adding a new entry to the child screen is what constitutes a change
			 * here, reset the child screen listEntries to the saved list.
			 */
			@SuppressWarnings("rawtypes")
			@Override
			public void undoChanges() {
				if (childScreen instanceof GuiConfig) {
					GuiConfig child = (GuiConfig) childScreen;
					for (IConfigEntry ice : child.entryList.listEntries)
						if (!child.initEntries.contains(ice)
								&& ForgeChunkManager.getConfig().hasCategory(ice.getName())) {
							ForgeChunkManager.getConfig()
									.removeCategory(ForgeChunkManager.getConfig().getCategory(ice.getName()));
						}

					child.entryList.listEntries = new ArrayList<>(child.initEntries);
				}
			}
		}

		/**
		 * This custom list entry provides a button that will open to a screen that will
		 * allow a user to define a new mod override.
		 */
		public static class AddModOverrideEntry extends CategoryEntry {
			@SuppressWarnings("rawtypes")
			public AddModOverrideEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
				super(owningScreen, owningEntryList, prop);
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			protected GuiScreen buildChildScreen() {
				List<IConfigElement> list = new ArrayList<>();

				list.add(new DummyConfigElement("modID", "", ConfigGuiType.STRING, "forge.configgui.modID")
						.setCustomListEntryClass(ModIDEntry.class));
				list.add(new ConfigElement<Integer>(new Property("maximumTicketCount", "200", Property.Type.INTEGER,
						"forge.configgui.maximumTicketCount")));
				list.add(new ConfigElement<Integer>(new Property("maximumChunksPerTicket", "25", Property.Type.INTEGER,
						"forge.configgui.maximumChunksPerTicket")));

				return new GuiConfig(owningScreen, list, owningScreen.modID,
						configElement.requiresWorldRestart() || owningScreen.allRequireWorldRestart,
						configElement.requiresMcRestart() || owningScreen.allRequireMcRestart, owningScreen.title,
						I18n.format("forge.configgui.ctgy.forgeChunkLoadingAddModConfig"));
			}

			@Override
			public boolean isChanged() {
				return false;
			}
		}

		/**
		 * This custom list entry provides a Mod ID selector. The control is a button
		 * that opens a list of values to select from. This entry also overrides
		 * onGuiClosed() to run code to save the data to a new ConfigCategory when the
		 * user is done.
		 */
		public static class ModIDEntry extends SelectValueEntry {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ModIDEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
				super(owningScreen, owningEntryList, prop, getSelectableValues());
				if (selectableValues.size() == 0) {
					btnValue.enabled = false;
				}
			}

			private static Map<Object, String> getSelectableValues() {
				Map<Object, String> selectableValues = new TreeMap<>();

				for (ModContainer mod : Loader.instance().getActiveModList())
					// only add mods to the list that have a non-immutable ModContainer
					if (!mod.isImmutable() && mod.getMod() != null) {
						selectableValues.put(mod.getModId(), mod.getName());
					}

				return selectableValues;
			}

			/**
			 * By overriding onGuiClosed() for this entry we can perform additional actions
			 * when the user is done such as saving a new ConfigCategory object to the
			 * Configuration object.
			 */
			@SuppressWarnings("rawtypes")
			@Override
			public void onGuiClosed() {
				Object modObject = Loader.instance().getModObjectList()
						.get(Loader.instance().getIndexedModList().get(currentValue));
				int maxTickets = 200;
				int maxChunks = 25;
				if (modObject != null) {
					owningEntryList.saveConfigElements();
					for (IConfigElement ice : owningScreen.configElements)
						if ("maximumTicketCount".equals(ice.getName())) {
							maxTickets = Integer.valueOf(ice.get().toString());
						} else if ("maximumChunksPerTicket".equals(ice.getName())) {
							maxChunks = Integer.valueOf(ice.get().toString());
						}

					ForgeChunkManager.addConfigProperty(modObject, "maximumTicketCount", String.valueOf(maxTickets),
							Property.Type.INTEGER);
					ForgeChunkManager.addConfigProperty(modObject, "maximumChunksPerTicket", String.valueOf(maxChunks),
							Property.Type.INTEGER);

					if (owningScreen.parentScreen instanceof GuiConfig) {
						GuiConfig superParent = (GuiConfig) owningScreen.parentScreen;
						ConfigCategory modCtgy = ForgeChunkManager.getConfigFor(modObject);
						modCtgy.setPropertyOrder(ForgeChunkManager.MOD_PROP_ORDER);
						ConfigElement modConfig = new ConfigElement(modCtgy);

						boolean found = false;
						for (IConfigElement ice : superParent.configElements)
							if (ice.getName().equals(currentValue)) {
								found = true;
							}

						if (!found) {
							superParent.configElements.add(modConfig);
						}

						superParent.needsRefresh = true;
						superParent.initGui();
					}
				}
			}
		}
	}
}
