package net.minecraft.client.resources;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public interface IResourceManager {
	Set getResourceDomains();

	IResource getResource(ResourceLocation p_110536_1_) throws IOException;

	List getAllResources(ResourceLocation p_135056_1_) throws IOException;
}