package net.minecraft.client.renderer;

import org.lwjgl.opengl.GLContext;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OpenGlCapsChecker {
	private static final String __OBFID = "CL_00000649";

	public static boolean checkARBOcclusion() {
		return GLContext.getCapabilities().GL_ARB_occlusion_query;
	}
}