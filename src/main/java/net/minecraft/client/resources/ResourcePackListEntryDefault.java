package net.minecraft.client.resources;

import com.google.gson.JsonParseException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class ResourcePackListEntryDefault extends ResourcePackListEntry {
	private static final Logger logger = LogManager.getLogger();
	private final IResourcePack field_148320_d;
	private final ResourceLocation field_148321_e;
	private static final String __OBFID = "CL_00000822";

	public ResourcePackListEntryDefault(GuiScreenResourcePacks p_i45052_1_) {
		super(p_i45052_1_);
		field_148320_d = field_148317_a.getResourcePackRepository().rprDefaultResourcePack;
		DynamicTexture dynamictexture;

		try {
			dynamictexture = new DynamicTexture(field_148320_d.getPackImage());
		} catch (IOException ioexception) {
			dynamictexture = TextureUtil.missingTexture;
		}

		field_148321_e = field_148317_a.getTextureManager().getDynamicTextureLocation("texturepackicon",
				dynamictexture);
	}

	@Override
	protected String func_148311_a() {
		try {
			PackMetadataSection packmetadatasection = (PackMetadataSection) field_148320_d
					.getPackMetadata(field_148317_a.getResourcePackRepository().rprMetadataSerializer, "pack");

			if (packmetadatasection != null)
				return packmetadatasection.func_152805_a().getFormattedText();
		} catch (JsonParseException jsonparseexception) {
			logger.error("Couldn't load metadata info", jsonparseexception);
		} catch (IOException ioexception) {
			logger.error("Couldn't load metadata info", ioexception);
		}

		return EnumChatFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
	}

	@Override
	protected boolean func_148309_e() {
		return false;
	}

	@Override
	protected boolean func_148308_f() {
		return false;
	}

	@Override
	protected boolean func_148314_g() {
		return false;
	}

	@Override
	protected boolean func_148307_h() {
		return false;
	}

	@Override
	protected String func_148312_b() {
		return "Default";
	}

	@Override
	protected void func_148313_c() {
		field_148317_a.getTextureManager().bindTexture(field_148321_e);
	}

	@Override
	protected boolean func_148310_d() {
		return false;
	}
}