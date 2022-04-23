package net.minecraft.client.resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.data.IMetadataSection;

import java.io.InputStream;

@SideOnly(Side.CLIENT)
public interface IResource {
	InputStream getInputStream();

	boolean hasMetadata();

	IMetadataSection getMetadata(String p_110526_1_);
}