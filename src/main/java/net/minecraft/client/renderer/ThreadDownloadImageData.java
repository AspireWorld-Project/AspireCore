package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

@SideOnly(Side.CLIENT)
public class ThreadDownloadImageData extends SimpleTexture {
	private static final Logger logger = LogManager.getLogger();
	private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
	private final File field_152434_e;
	private final String imageUrl;
	private final IImageBuffer imageBuffer;
	private BufferedImage bufferedImage;
	private Thread imageThread;
	private boolean textureUploaded;
	private static final String __OBFID = "CL_00001049";

	public ThreadDownloadImageData(File p_i1049_1_, String p_i1049_2_, ResourceLocation p_i1049_3_,
			IImageBuffer p_i1049_4_) {
		super(p_i1049_3_);
		field_152434_e = p_i1049_1_;
		imageUrl = p_i1049_2_;
		imageBuffer = p_i1049_4_;
	}

	private void checkTextureUploaded() {
		if (!textureUploaded) {
			if (bufferedImage != null) {
				if (textureLocation != null) {
					deleteGlTexture();
				}

				TextureUtil.uploadTextureImage(super.getGlTextureId(), bufferedImage);
				textureUploaded = true;
			}
		}
	}

	@Override
	public int getGlTextureId() {
		checkTextureUploaded();
		return super.getGlTextureId();
	}

	public void setBufferedImage(BufferedImage p_147641_1_) {
		bufferedImage = p_147641_1_;

		if (imageBuffer != null) {
			imageBuffer.func_152634_a();
		}
	}

	@Override
	public void loadTexture(IResourceManager p_110551_1_) throws IOException {
		if (bufferedImage == null && textureLocation != null) {
			super.loadTexture(p_110551_1_);
		}

		if (imageThread == null) {
			if (field_152434_e != null && field_152434_e.isFile()) {
				logger.debug("Loading http texture from local cache ({})", new Object[] { field_152434_e });

				try {
					bufferedImage = ImageIO.read(field_152434_e);

					if (imageBuffer != null) {
						setBufferedImage(imageBuffer.parseUserSkin(bufferedImage));
					}
				} catch (IOException ioexception) {
					logger.error("Couldn\'t load skin " + field_152434_e, ioexception);
					func_152433_a();
				}
			} else {
				func_152433_a();
			}
		}
	}

	protected void func_152433_a() {
		imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet()) {
			private static final String __OBFID = "CL_00001050";

			@Override
			public void run() {
				HttpURLConnection httpurlconnection = null;
				ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}",
						new Object[] { imageUrl, field_152434_e });

				try {
					httpurlconnection = (HttpURLConnection) new URL(imageUrl)
							.openConnection(Minecraft.getMinecraft().getProxy());
					httpurlconnection.setDoInput(true);
					httpurlconnection.setDoOutput(false);
					httpurlconnection.connect();

					if (httpurlconnection.getResponseCode() / 100 == 2) {
						BufferedImage bufferedimage;

						if (field_152434_e != null) {
							FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), field_152434_e);
							bufferedimage = ImageIO.read(field_152434_e);
						} else {
							bufferedimage = ImageIO.read(httpurlconnection.getInputStream());
						}

						if (imageBuffer != null) {
							bufferedimage = imageBuffer.parseUserSkin(bufferedimage);
						}

						ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
						return;
					}
				} catch (Exception exception) {
					ThreadDownloadImageData.logger.error("Couldn\'t download http texture", exception);
					return;
				} finally {
					if (httpurlconnection != null) {
						httpurlconnection.disconnect();
					}
				}
			}
		};
		imageThread.setDaemon(true);
		imageThread.start();
	}
}