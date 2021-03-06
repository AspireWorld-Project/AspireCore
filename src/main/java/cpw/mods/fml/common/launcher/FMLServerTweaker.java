package cpw.mods.fml.common.launcher;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class FMLServerTweaker extends FMLTweaker {
	@Override
	public String getLaunchTarget() {
		return "net.minecraft.server.MinecraftServer";
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		// The mojang packages are excluded so the log4j2 queue is correctly visible
		// from
		// the obfuscated and deobfuscated parts of the code. Without, the UI won't show
		// anything
		classLoader.addClassLoaderExclusion("com.mojang.");
		classLoader.addTransformerExclusion("cpw.mods.fml.repackage.");
		classLoader.addTransformerExclusion("cpw.mods.fml.relauncher.");
		classLoader.addTransformerExclusion("cpw.mods.fml.common.asm.transformers.");
		classLoader.addClassLoaderExclusion("LZMA.");
		FMLLaunchHandler.configureForServerLaunch(classLoader, this);
		FMLLaunchHandler.appendCoreMods();
	}
}