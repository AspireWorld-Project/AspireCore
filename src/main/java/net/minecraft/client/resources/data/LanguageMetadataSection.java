package net.minecraft.client.resources.data;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Collection;

@SideOnly(Side.CLIENT)
public class LanguageMetadataSection implements IMetadataSection {
	private final Collection languages;
	private static final String __OBFID = "CL_00001110";

	public LanguageMetadataSection(Collection p_i1311_1_) {
		languages = p_i1311_1_;
	}

	public Collection getLanguages() {
		return languages;
	}
}