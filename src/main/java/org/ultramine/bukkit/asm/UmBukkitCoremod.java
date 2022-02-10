package org.ultramine.bukkit.asm;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "org.ultramine.bukkit.asm.", "net.md_5.specialsource.",
		"org.apache.commons.lang.", "org.sqlite.", "com.avaje.ebean.", "guava10." })
public class UmBukkitCoremod implements IFMLLoadingPlugin {
	public UmBukkitCoremod() {
		try {
			String name = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
					.getName();
			CoreModManager.getLoadedCoremods().remove(name);
			CoreModManager.getReparseableCoremods().add(name);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "org.ultramine.bukkit.asm.ASMTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return "org.ultramine.bukkit.UMBukkitImplMod";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
