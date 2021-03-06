package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class SimpleTexture extends AbstractTexture {
	private static final Logger logger = LogManager.getLogger();
	protected final ResourceLocation textureLocation;
	private static final String __OBFID = "CL_00001052";

	public SimpleTexture(ResourceLocation p_i1275_1_) {
		textureLocation = p_i1275_1_;
	}

	@Override
	public void loadTexture(IResourceManager p_110551_1_) throws IOException {
		deleteGlTexture();
		InputStream inputstream = null;

		try {
			IResource iresource = p_110551_1_.getResource(textureLocation);
			inputstream = iresource.getInputStream();
			BufferedImage bufferedimage = ImageIO.read(inputstream);
			boolean flag = false;
			boolean flag1 = false;

			if (iresource.hasMetadata()) {
				try {
					TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource
							.getMetadata("texture");

					if (texturemetadatasection != null) {
						flag = texturemetadatasection.getTextureBlur();
						flag1 = texturemetadatasection.getTextureClamp();
					}
				} catch (RuntimeException runtimeexception) {
					logger.warn("Failed reading metadata of: " + textureLocation, runtimeexception);
				}
			}

			TextureUtil.uploadTextureImageAllocate(getGlTextureId(), bufferedimage, flag, flag1);
		} finally {
			if (inputstream != null) {
				inputstream.close();
			}
		}
	}
}