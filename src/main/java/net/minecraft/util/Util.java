package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Util {
	public static Util.EnumOS getOSType() {
		String s = System.getProperty("os.name").toLowerCase();
		return s.contains("win") ? Util.EnumOS.WINDOWS
				: s.contains("mac") ? Util.EnumOS.OSX
						: s.contains("solaris") ? Util.EnumOS.SOLARIS
								: s.contains("sunos") ? Util.EnumOS.SOLARIS
										: s.contains("linux") ? Util.EnumOS.LINUX
												: s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN;
	}

	@SideOnly(Side.CLIENT)
	public enum EnumOS {
		LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN
	}
}