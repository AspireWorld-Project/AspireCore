package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IResourceManagerReloadListener {
	void onResourceManagerReload(IResourceManager p_110549_1_);
}