package org.ultramine.bukkit;

public class BukkitConfig {
	public Settings settings = new Settings();
	public Preferences preferences = new Preferences();

	public static class Settings {
		public String pluginsFolder = "plugins";
		public String updatesFolder = "update";
	}

	public static class Preferences {

	}
}
