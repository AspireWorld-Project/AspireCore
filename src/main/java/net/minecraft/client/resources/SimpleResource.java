package net.minecraft.client.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SimpleResource implements IResource {
	private final Map mapMetadataSections = Maps.newHashMap();
	private final ResourceLocation srResourceLocation;
	private final InputStream resourceInputStream;
	private final InputStream mcmetaInputStream;
	private final IMetadataSerializer srMetadataSerializer;
	private boolean mcmetaJsonChecked;
	private JsonObject mcmetaJson;
	private static final String __OBFID = "CL_00001093";

	public SimpleResource(ResourceLocation p_i1300_1_, InputStream p_i1300_2_, InputStream p_i1300_3_,
			IMetadataSerializer p_i1300_4_) {
		srResourceLocation = p_i1300_1_;
		resourceInputStream = p_i1300_2_;
		mcmetaInputStream = p_i1300_3_;
		srMetadataSerializer = p_i1300_4_;
	}

	@Override
	public InputStream getInputStream() {
		return resourceInputStream;
	}

	@Override
	public boolean hasMetadata() {
		return mcmetaInputStream != null;
	}

	@Override
	public IMetadataSection getMetadata(String p_110526_1_) {
		if (!hasMetadata())
			return null;
		else {
			if (mcmetaJson == null && !mcmetaJsonChecked) {
				mcmetaJsonChecked = true;
				BufferedReader bufferedreader = null;

				try {
					bufferedreader = new BufferedReader(new InputStreamReader(mcmetaInputStream));
					mcmetaJson = new JsonParser().parse(bufferedreader).getAsJsonObject();
				} finally {
					IOUtils.closeQuietly(bufferedreader);
				}
			}

			IMetadataSection imetadatasection = (IMetadataSection) mapMetadataSections.get(p_110526_1_);

			if (imetadatasection == null) {
				imetadatasection = srMetadataSerializer.parseMetadataSection(p_110526_1_, mcmetaJson);
			}

			return imetadatasection;
		}
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ instanceof SimpleResource) {
			SimpleResource simpleresource = (SimpleResource) p_equals_1_;
			return srResourceLocation != null ? srResourceLocation.equals(simpleresource.srResourceLocation)
					: simpleresource.srResourceLocation == null;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return srResourceLocation == null ? 0 : srResourceLocation.hashCode();
	}
}