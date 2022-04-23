package net.minecraft.client.renderer;

import com.google.gson.JsonSyntaxException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class EntityRenderer implements IResourceManagerReloadListener {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
	private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
	public static boolean anaglyphEnable;
	public static int anaglyphField;
	private final Minecraft mc;
	private float farPlaneDistance;
	public final ItemRenderer itemRenderer;
	private final MapItemRenderer theMapItemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity;
	private final MouseFilter mouseFilterXAxis = new MouseFilter();
	private final MouseFilter mouseFilterYAxis = new MouseFilter();
	private final MouseFilter mouseFilterDummy1 = new MouseFilter();
	private final MouseFilter mouseFilterDummy2 = new MouseFilter();
	private final MouseFilter mouseFilterDummy3 = new MouseFilter();
	private final MouseFilter mouseFilterDummy4 = new MouseFilter();
	private final float thirdPersonDistance = 4.0F;
	private float thirdPersonDistanceTemp = 4.0F;
	private float debugCamYaw;
	private float prevDebugCamYaw;
	private float debugCamPitch;
	private float prevDebugCamPitch;
	private float smoothCamYaw;
	private float smoothCamPitch;
	private float smoothCamFilterX;
	private float smoothCamFilterY;
	private float smoothCamPartialTicks;
	private float debugCamFOV;
	private float prevDebugCamFOV;
	private float camRoll;
	private float prevCamRoll;
	private final DynamicTexture lightmapTexture;
	private final int[] lightmapColors;
	private final ResourceLocation locationLightMap;
	private float fovModifierHand;
	private float fovModifierHandPrev;
	private float fovMultiplierTemp;
	private float bossColorModifier;
	private float bossColorModifierPrev;
	private boolean cloudFog;
	private final IResourceManager resourceManager;
	public ShaderGroup theShaderGroup;
	private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[] {
			new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"),
			new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"),
			new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"),
			new ResourceLocation("shaders/post/color_convolve.json"),
			new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"),
			new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"),
			new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"),
			new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"),
			new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"),
			new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"),
			new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"),
			new ResourceLocation("shaders/post/antialias.json") };
	public static final int shaderCount = shaderResourceLocations.length;
	private int shaderIndex;
	private final double cameraZoom;
	private double cameraYaw;
	private double cameraPitch;
	private long prevFrameTime;
	private long renderEndNanoTime;
	private boolean lightmapUpdateNeeded;
	float torchFlickerX;
	float torchFlickerDX;
	float torchFlickerY;
	float torchFlickerDY;
	private final Random random;
	private int rainSoundCounter;
	float[] rainXCoords;
	float[] rainYCoords;
	FloatBuffer fogColorBuffer;
	float fogColorRed;
	float fogColorGreen;
	float fogColorBlue;
	private float fogColor2;
	private float fogColor1;
	public int debugViewDirection;
	private static final String __OBFID = "CL_00000947";

	public EntityRenderer(Minecraft p_i45076_1_, IResourceManager p_i45076_2_) {
		shaderIndex = shaderCount;
		cameraZoom = 1.0D;
		prevFrameTime = Minecraft.getSystemTime();
		random = new Random();
		fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
		mc = p_i45076_1_;
		resourceManager = p_i45076_2_;
		theMapItemRenderer = new MapItemRenderer(p_i45076_1_.getTextureManager());
		itemRenderer = new ItemRenderer(p_i45076_1_);
		lightmapTexture = new DynamicTexture(16, 16);
		locationLightMap = p_i45076_1_.getTextureManager().getDynamicTextureLocation("lightMap", lightmapTexture);
		lightmapColors = lightmapTexture.getTextureData();
		theShaderGroup = null;
	}

	public boolean isShaderActive() {
		return OpenGlHelper.shadersSupported && theShaderGroup != null;
	}

	public void deactivateShader() {
		if (theShaderGroup != null) {
			theShaderGroup.deleteShaderGroup();
		}

		theShaderGroup = null;
		shaderIndex = shaderCount;
	}

	public void activateNextShader() {
		if (OpenGlHelper.shadersSupported) {
			if (theShaderGroup != null) {
				theShaderGroup.deleteShaderGroup();
			}

			shaderIndex = (shaderIndex + 1) % (shaderResourceLocations.length + 1);

			if (shaderIndex != shaderCount) {
				try {
					logger.info("Selecting effect " + shaderResourceLocations[shaderIndex]);
					theShaderGroup = new ShaderGroup(mc.getTextureManager(), resourceManager, mc.getFramebuffer(),
							shaderResourceLocations[shaderIndex]);
					theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
				} catch (IOException ioexception) {
					logger.warn("Failed to load shader: " + shaderResourceLocations[shaderIndex], ioexception);
					shaderIndex = shaderCount;
				} catch (JsonSyntaxException jsonsyntaxexception) {
					logger.warn("Failed to load shader: " + shaderResourceLocations[shaderIndex], jsonsyntaxexception);
					shaderIndex = shaderCount;
				}
			} else {
				theShaderGroup = null;
				logger.info("No effect selected");
			}
		}
	}

	@Override
	public void onResourceManagerReload(IResourceManager p_110549_1_) {
		if (theShaderGroup != null) {
			theShaderGroup.deleteShaderGroup();
		}

		if (shaderIndex != shaderCount) {
			try {
				theShaderGroup = new ShaderGroup(mc.getTextureManager(), p_110549_1_, mc.getFramebuffer(),
						shaderResourceLocations[shaderIndex]);
				theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			} catch (IOException ioexception) {
				logger.warn("Failed to load shader: " + shaderResourceLocations[shaderIndex], ioexception);
				shaderIndex = shaderCount;
			}
		}
	}

	public void updateRenderer() {
		if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		}

		updateFovModifierHand();
		updateTorchFlicker();
		fogColor2 = fogColor1;
		thirdPersonDistanceTemp = thirdPersonDistance;
		prevDebugCamYaw = debugCamYaw;
		prevDebugCamPitch = debugCamPitch;
		prevDebugCamFOV = debugCamFOV;
		prevCamRoll = camRoll;
		float f;
		float f1;

		if (mc.gameSettings.smoothCamera) {
			f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			f1 = f * f * f * 8.0F;
			smoothCamFilterX = mouseFilterXAxis.smooth(smoothCamYaw, 0.05F * f1);
			smoothCamFilterY = mouseFilterYAxis.smooth(smoothCamPitch, 0.05F * f1);
			smoothCamPartialTicks = 0.0F;
			smoothCamYaw = 0.0F;
			smoothCamPitch = 0.0F;
		}

		if (mc.renderViewEntity == null) {
			mc.renderViewEntity = mc.thePlayer;
		}

		f = mc.theWorld.getLightBrightness(MathHelper.floor_double(mc.renderViewEntity.posX),
				MathHelper.floor_double(mc.renderViewEntity.posY), MathHelper.floor_double(mc.renderViewEntity.posZ));
		f1 = mc.gameSettings.renderDistanceChunks / 16.0F;
		float f2 = f * (1.0F - f1) + f1;
		fogColor1 += (f2 - fogColor1) * 0.1F;
		++rendererUpdateCount;
		itemRenderer.updateEquippedItem();
		addRainParticles();
		bossColorModifierPrev = bossColorModifier;

		if (BossStatus.hasColorModifier) {
			bossColorModifier += 0.05F;

			if (bossColorModifier > 1.0F) {
				bossColorModifier = 1.0F;
			}

			BossStatus.hasColorModifier = false;
		} else if (bossColorModifier > 0.0F) {
			bossColorModifier -= 0.0125F;
		}
	}

	public ShaderGroup getShaderGroup() {
		return theShaderGroup;
	}

	public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_) {
		if (OpenGlHelper.shadersSupported) {
			if (theShaderGroup != null) {
				theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
			}
		}
	}

	public void getMouseOver(float p_78473_1_) {
		if (mc.renderViewEntity != null) {
			if (mc.theWorld != null) {
				mc.pointedEntity = null;
				double d0 = mc.playerController.getBlockReachDistance();
				mc.objectMouseOver = mc.renderViewEntity.rayTrace(d0, p_78473_1_);
				double d1 = d0;
				Vec3 vec3 = mc.renderViewEntity.getPosition(p_78473_1_);

				if (mc.playerController.extendedReach()) {
					d0 = 6.0D;
					d1 = 6.0D;
				} else {
					if (d0 > 3.0D) {
						d1 = 3.0D;
					}

					d0 = d1;
				}

				if (mc.objectMouseOver != null) {
					d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
				}

				Vec3 vec31 = mc.renderViewEntity.getLook(p_78473_1_);
				Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
				pointedEntity = null;
				Vec3 vec33 = null;
				float f1 = 1.0F;
				List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity,
						mc.renderViewEntity.boundingBox
								.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f1, f1, f1));
				double d2 = d1;

				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);

					if (entity.canBeCollidedWith()) {
						float f2 = entity.getCollisionBorderSize();
						AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
						MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

						if (axisalignedbb.isVecInside(vec3)) {
							if (0.0D < d2 || d2 == 0.0D) {
								pointedEntity = entity;
								vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
								d2 = 0.0D;
							}
						} else if (movingobjectposition != null) {
							double d3 = vec3.distanceTo(movingobjectposition.hitVec);

							if (d3 < d2 || d2 == 0.0D) {
								if (entity == mc.renderViewEntity.ridingEntity && !entity.canRiderInteract()) {
									if (d2 == 0.0D) {
										pointedEntity = entity;
										vec33 = movingobjectposition.hitVec;
									}
								} else {
									pointedEntity = entity;
									vec33 = movingobjectposition.hitVec;
									d2 = d3;
								}
							}
						}
					}
				}

				if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null)) {
					mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

					if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
						mc.pointedEntity = pointedEntity;
					}
				}
			}
		}
	}

	private void updateFovModifierHand() {
		if (mc.renderViewEntity instanceof EntityPlayerSP) {
			EntityPlayerSP entityplayersp = (EntityPlayerSP) mc.renderViewEntity;
			fovMultiplierTemp = entityplayersp.getFOVMultiplier();
		} else {
			fovMultiplierTemp = mc.thePlayer.getFOVMultiplier();
		}
		fovModifierHandPrev = fovModifierHand;
		fovModifierHand += (fovMultiplierTemp - fovModifierHand) * 0.5F;

		if (fovModifierHand > 1.5F) {
			fovModifierHand = 1.5F;
		}

		if (fovModifierHand < 0.1F) {
			fovModifierHand = 0.1F;
		}
	}

	private float getFOVModifier(float p_78481_1_, boolean p_78481_2_) {
		if (debugViewDirection > 0)
			return 90.0F;
		else {
			EntityLivingBase entityplayer = mc.renderViewEntity;
			float f1 = 70.0F;

			if (p_78481_2_) {
				f1 = mc.gameSettings.fovSetting;
				f1 *= fovModifierHandPrev + (fovModifierHand - fovModifierHandPrev) * p_78481_1_;
			}

			if (entityplayer.getHealth() <= 0.0F) {
				float f2 = entityplayer.deathTime + p_78481_1_;
				f1 /= (1.0F - 500.0F / (f2 + 500.0F)) * 2.0F + 1.0F;
			}

			Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entityplayer, p_78481_1_);

			if (block.getMaterial() == Material.water) {
				f1 = f1 * 60.0F / 70.0F;
			}

			return f1 + prevDebugCamFOV + (debugCamFOV - prevDebugCamFOV) * p_78481_1_;
		}
	}

	private void hurtCameraEffect(float p_78482_1_) {
		EntityLivingBase entitylivingbase = mc.renderViewEntity;
		float f1 = entitylivingbase.hurtTime - p_78482_1_;
		float f2;

		if (entitylivingbase.getHealth() <= 0.0F) {
			f2 = entitylivingbase.deathTime + p_78482_1_;
			GL11.glRotatef(40.0F - 8000.0F / (f2 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if (f1 >= 0.0F) {
			f1 /= entitylivingbase.maxHurtTime;
			f1 = MathHelper.sin(f1 * f1 * f1 * f1 * (float) Math.PI);
			f2 = entitylivingbase.attackedAtYaw;
			GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f1 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float p_78475_1_) {
		if (mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) mc.renderViewEntity;
			float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f2 = -(entityplayer.distanceWalkedModified + f1 * p_78475_1_);
			float f3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * p_78475_1_;
			float f4 = entityplayer.prevCameraPitch
					+ (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * p_78475_1_;
			GL11.glTranslatef(MathHelper.sin(f2 * (float) Math.PI) * f3 * 0.5F,
					-Math.abs(MathHelper.cos(f2 * (float) Math.PI) * f3), 0.0F);
			GL11.glRotatef(MathHelper.sin(f2 * (float) Math.PI) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f2 * (float) Math.PI - 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
		}
	}

	private void orientCamera(float p_78467_1_) {
		EntityLivingBase entitylivingbase = mc.renderViewEntity;
		float f1 = entitylivingbase.yOffset - 1.62F;
		double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * p_78467_1_;
		double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * p_78467_1_ - f1;
		double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * p_78467_1_;
		GL11.glRotatef(prevCamRoll + (camRoll - prevCamRoll) * p_78467_1_, 0.0F, 0.0F, 1.0F);

		if (entitylivingbase.isPlayerSleeping()) {
			f1 = (float) (f1 + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);

			if (!mc.gameSettings.debugCamEnable) {
				ForgeHooksClient.orientBedCamera(mc, entitylivingbase);
				GL11.glRotatef(entitylivingbase.prevRotationYaw
						+ (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F,
						-1.0F, 0.0F);
				GL11.glRotatef(
						entitylivingbase.prevRotationPitch
								+ (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_,
						-1.0F, 0.0F, 0.0F);
			}
		} else if (mc.gameSettings.thirdPersonView > 0) {
			double d7 = thirdPersonDistanceTemp + (thirdPersonDistance - thirdPersonDistanceTemp) * p_78467_1_;
			float f2;
			float f6;

			if (mc.gameSettings.debugCamEnable) {
				f6 = prevDebugCamYaw + (debugCamYaw - prevDebugCamYaw) * p_78467_1_;
				f2 = prevDebugCamPitch + (debugCamPitch - prevDebugCamPitch) * p_78467_1_;
				GL11.glTranslatef(0.0F, 0.0F, (float) -d7);
				GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f6, 0.0F, 1.0F, 0.0F);
			} else {
				f6 = entitylivingbase.rotationYaw;
				f2 = entitylivingbase.rotationPitch;

				if (mc.gameSettings.thirdPersonView == 2) {
					f2 += 180.0F;
				}

				double d3 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI)
						* MathHelper.cos(f2 / 180.0F * (float) Math.PI) * d7;
				double d4 = MathHelper.cos(f6 / 180.0F * (float) Math.PI)
						* MathHelper.cos(f2 / 180.0F * (float) Math.PI) * d7;
				double d5 = -MathHelper.sin(f2 / 180.0F * (float) Math.PI) * d7;

				for (int k = 0; k < 8; ++k) {
					float f3 = (k & 1) * 2 - 1;
					float f4 = (k >> 1 & 1) * 2 - 1;
					float f5 = (k >> 2 & 1) * 2 - 1;
					f3 *= 0.1F;
					f4 *= 0.1F;
					f5 *= 0.1F;
					MovingObjectPosition movingobjectposition = mc.theWorld.rayTraceBlocks(
							Vec3.createVectorHelper(d0 + f3, d1 + f4, d2 + f5),
							Vec3.createVectorHelper(d0 - d3 + f3 + f5, d1 - d5 + f4, d2 - d4 + f5));

					if (movingobjectposition != null) {
						double d6 = movingobjectposition.hitVec.distanceTo(Vec3.createVectorHelper(d0, d1, d2));

						if (d6 < d7) {
							d7 = d6;
						}
					}
				}

				if (mc.gameSettings.thirdPersonView == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(entitylivingbase.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entitylivingbase.rotationYaw - f6, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float) -d7);
				GL11.glRotatef(f6 - entitylivingbase.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(f2 - entitylivingbase.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if (!mc.gameSettings.debugCamEnable) {
			GL11.glRotatef(
					entitylivingbase.prevRotationPitch
							+ (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_,
					1.0F, 0.0F, 0.0F);
			GL11.glRotatef(
					entitylivingbase.prevRotationYaw
							+ (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F,
					0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, f1, 0.0F);
		d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * p_78467_1_;
		d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * p_78467_1_ - f1;
		d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * p_78467_1_;
		cloudFog = mc.renderGlobal.hasCloudFog(d0, d1, d2, p_78467_1_);
	}

	private void setupCameraTransform(float p_78479_1_, int p_78479_2_) {
		farPlaneDistance = mc.gameSettings.renderDistanceChunks * 16;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float f1 = 0.07F;

		if (mc.gameSettings.anaglyph) {
			GL11.glTranslatef(-(p_78479_2_ * 2 - 1) * f1, 0.0F, 0.0F);
		}

		if (cameraZoom != 1.0D) {
			GL11.glTranslatef((float) cameraYaw, (float) -cameraPitch, 0.0F);
			GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
		}

		Project.gluPerspective(getFOVModifier(p_78479_1_, true), (float) mc.displayWidth / (float) mc.displayHeight,
				0.05F, farPlaneDistance * 2.0F);
		float f2;

		if (mc.playerController.enableEverythingIsScrewedUpMode()) {
			f2 = 0.6666667F;
			GL11.glScalef(1.0F, f2, 1.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		if (mc.gameSettings.anaglyph) {
			GL11.glTranslatef((p_78479_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		hurtCameraEffect(p_78479_1_);

		if (mc.gameSettings.viewBobbing) {
			setupViewBobbing(p_78479_1_);
		}

		f2 = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * p_78479_1_;

		if (f2 > 0.0F) {
			byte b0 = 20;

			if (mc.thePlayer.isPotionActive(Potion.confusion)) {
				b0 = 7;
			}

			float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
			f3 *= f3;
			GL11.glRotatef((rendererUpdateCount + p_78479_1_) * b0, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
			GL11.glRotatef(-(rendererUpdateCount + p_78479_1_) * b0, 0.0F, 1.0F, 1.0F);
		}

		orientCamera(p_78479_1_);

		if (debugViewDirection > 0) {
			int j = debugViewDirection - 1;

			if (j == 1) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (j == 2) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (j == 3) {
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			if (j == 4) {
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if (j == 5) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}
	}

	private void renderHand(float p_78476_1_, int p_78476_2_) {
		if (debugViewDirection <= 0) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			float f1 = 0.07F;

			if (mc.gameSettings.anaglyph) {
				GL11.glTranslatef(-(p_78476_2_ * 2 - 1) * f1, 0.0F, 0.0F);
			}

			if (cameraZoom != 1.0D) {
				GL11.glTranslatef((float) cameraYaw, (float) -cameraPitch, 0.0F);
				GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
			}

			Project.gluPerspective(getFOVModifier(p_78476_1_, false),
					(float) mc.displayWidth / (float) mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

			if (mc.playerController.enableEverythingIsScrewedUpMode()) {
				float f2 = 0.6666667F;
				GL11.glScalef(1.0F, f2, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			if (mc.gameSettings.anaglyph) {
				GL11.glTranslatef((p_78476_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			hurtCameraEffect(p_78476_1_);

			if (mc.gameSettings.viewBobbing) {
				setupViewBobbing(p_78476_1_);
			}

			if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping()
					&& !mc.gameSettings.hideGUI && !mc.playerController.enableEverythingIsScrewedUpMode()) {
				enableLightmap(p_78476_1_);
				itemRenderer.renderItemInFirstPerson(p_78476_1_);
				disableLightmap(p_78476_1_);
			}

			GL11.glPopMatrix();

			if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping()) {
				itemRenderer.renderOverlays(p_78476_1_);
				hurtCameraEffect(p_78476_1_);
			}

			if (mc.gameSettings.viewBobbing) {
				setupViewBobbing(p_78476_1_);
			}
		}
	}

	public void disableLightmap(double p_78483_1_) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void enableLightmap(double p_78463_1_) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float f = 0.00390625F;
		GL11.glScalef(f, f, f);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		mc.getTextureManager().bindTexture(locationLightMap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private void updateTorchFlicker() {
		torchFlickerDX = (float) (torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
		torchFlickerDY = (float) (torchFlickerDY + (Math.random() - Math.random()) * Math.random() * Math.random());
		torchFlickerDX = (float) (torchFlickerDX * 0.9D);
		torchFlickerDY = (float) (torchFlickerDY * 0.9D);
		torchFlickerX += (torchFlickerDX - torchFlickerX) * 1.0F;
		torchFlickerY += (torchFlickerDY - torchFlickerY) * 1.0F;
		lightmapUpdateNeeded = true;
	}

	private void updateLightmap(float p_78472_1_) {
		WorldClient worldclient = mc.theWorld;

		if (worldclient != null) {
			for (int i = 0; i < 256; ++i) {
				float f1 = worldclient.getSunBrightness(1.0F) * 0.95F + 0.05F;
				float f2 = worldclient.provider.lightBrightnessTable[i / 16] * f1;
				float f3 = worldclient.provider.lightBrightnessTable[i % 16] * (torchFlickerX * 0.1F + 1.5F);

				if (worldclient.lastLightningBolt > 0) {
					f2 = worldclient.provider.lightBrightnessTable[i / 16];
				}

				float f4 = f2 * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
				float f5 = f2 * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
				float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
				float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
				float f8 = f4 + f3;
				float f9 = f5 + f6;
				float f10 = f2 + f7;
				f8 = f8 * 0.96F + 0.03F;
				f9 = f9 * 0.96F + 0.03F;
				f10 = f10 * 0.96F + 0.03F;
				float f11;

				if (bossColorModifier > 0.0F) {
					f11 = bossColorModifierPrev + (bossColorModifier - bossColorModifierPrev) * p_78472_1_;
					f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
					f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
					f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
				}

				if (worldclient.provider.dimensionId == 1) {
					f8 = 0.22F + f3 * 0.75F;
					f9 = 0.28F + f6 * 0.75F;
					f10 = 0.25F + f7 * 0.75F;
				}

				float f12;

				if (mc.thePlayer.isPotionActive(Potion.nightVision)) {
					f11 = getNightVisionBrightness(mc.thePlayer, p_78472_1_);
					f12 = 1.0F / f8;

					if (f12 > 1.0F / f9) {
						f12 = 1.0F / f9;
					}

					if (f12 > 1.0F / f10) {
						f12 = 1.0F / f10;
					}

					f8 = f8 * (1.0F - f11) + f8 * f12 * f11;
					f9 = f9 * (1.0F - f11) + f9 * f12 * f11;
					f10 = f10 * (1.0F - f11) + f10 * f12 * f11;
				}

				if (f8 > 1.0F) {
					f8 = 1.0F;
				}

				if (f9 > 1.0F) {
					f9 = 1.0F;
				}

				if (f10 > 1.0F) {
					f10 = 1.0F;
				}

				f11 = mc.gameSettings.gammaSetting;
				f12 = 1.0F - f8;
				float f13 = 1.0F - f9;
				float f14 = 1.0F - f10;
				f12 = 1.0F - f12 * f12 * f12 * f12;
				f13 = 1.0F - f13 * f13 * f13 * f13;
				f14 = 1.0F - f14 * f14 * f14 * f14;
				f8 = f8 * (1.0F - f11) + f12 * f11;
				f9 = f9 * (1.0F - f11) + f13 * f11;
				f10 = f10 * (1.0F - f11) + f14 * f11;
				f8 = f8 * 0.96F + 0.03F;
				f9 = f9 * 0.96F + 0.03F;
				f10 = f10 * 0.96F + 0.03F;

				if (f8 > 1.0F) {
					f8 = 1.0F;
				}

				if (f9 > 1.0F) {
					f9 = 1.0F;
				}

				if (f10 > 1.0F) {
					f10 = 1.0F;
				}

				if (f8 < 0.0F) {
					f8 = 0.0F;
				}

				if (f9 < 0.0F) {
					f9 = 0.0F;
				}

				if (f10 < 0.0F) {
					f10 = 0.0F;
				}

				short short1 = 255;
				int j = (int) (f8 * 255.0F);
				int k = (int) (f9 * 255.0F);
				int l = (int) (f10 * 255.0F);
				lightmapColors[i] = short1 << 24 | j << 16 | k << 8 | l;
			}

			lightmapTexture.updateDynamicTexture();
			lightmapUpdateNeeded = false;
		}
	}

	private float getNightVisionBrightness(EntityPlayer p_82830_1_, float p_82830_2_) {
		int i = p_82830_1_.getActivePotionEffect(Potion.nightVision).getDuration();
		return i > 200 ? 1.0F : 0.7F + MathHelper.sin((i - p_82830_2_) * (float) Math.PI * 0.2F) * 0.3F;
	}

	public void updateCameraAndRender(float p_78480_1_) {
		mc.mcProfiler.startSection("lightTex");

		if (lightmapUpdateNeeded) {
			updateLightmap(p_78480_1_);
		}

		mc.mcProfiler.endSection();
		boolean flag = Display.isActive();

		if (!flag && mc.gameSettings.pauseOnLostFocus && (!mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
			if (Minecraft.getSystemTime() - prevFrameTime > 500L) {
				mc.displayInGameMenu();
			}
		} else {
			prevFrameTime = Minecraft.getSystemTime();
		}

		mc.mcProfiler.startSection("mouse");

		if (mc.inGameHasFocus && flag) {
			mc.mouseHelper.mouseXYChange();
			float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			float f3 = mc.mouseHelper.deltaX * f2;
			float f4 = mc.mouseHelper.deltaY * f2;
			byte b0 = 1;

			if (mc.gameSettings.invertMouse) {
				b0 = -1;
			}

			if (mc.gameSettings.smoothCamera) {
				smoothCamYaw += f3;
				smoothCamPitch += f4;
				float f5 = p_78480_1_ - smoothCamPartialTicks;
				smoothCamPartialTicks = p_78480_1_;
				f3 = smoothCamFilterX * f5;
				f4 = smoothCamFilterY * f5;
				mc.thePlayer.setAngles(f3, f4 * b0);
			} else {
				mc.thePlayer.setAngles(f3, f4 * b0);
			}
		}

		mc.mcProfiler.endSection();

		if (!mc.skipRenderWorld) {
			anaglyphEnable = mc.gameSettings.anaglyph;
			final ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int i = scaledresolution.getScaledWidth();
			int j = scaledresolution.getScaledHeight();
			final int k = Mouse.getX() * i / mc.displayWidth;
			final int l = j - Mouse.getY() * j / mc.displayHeight - 1;
			int i1 = mc.gameSettings.limitFramerate;

			if (mc.theWorld != null) {
				mc.mcProfiler.startSection("level");

				if (mc.isFramerateLimitBelowMax()) {
					renderWorld(p_78480_1_, renderEndNanoTime + 1000000000 / i1);
				} else {
					renderWorld(p_78480_1_, 0L);
				}

				if (OpenGlHelper.shadersSupported) {
					if (theShaderGroup != null) {
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glPushMatrix();
						GL11.glLoadIdentity();
						theShaderGroup.loadShaderGroup(p_78480_1_);
						GL11.glPopMatrix();
					}

					mc.getFramebuffer().bindFramebuffer(true);
				}

				renderEndNanoTime = System.nanoTime();
				mc.mcProfiler.endStartSection("gui");

				if (!mc.gameSettings.hideGUI || mc.currentScreen != null) {
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					mc.ingameGUI.renderGameOverlay(p_78480_1_, mc.currentScreen != null, k, l);
				}

				mc.mcProfiler.endSection();
			} else {
				GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				setupOverlayRendering();
				renderEndNanoTime = System.nanoTime();
			}

			if (mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

				try {
					if (!MinecraftForge.EVENT_BUS.post(new DrawScreenEvent.Pre(mc.currentScreen, k, l, p_78480_1_))) {
						mc.currentScreen.drawScreen(k, l, p_78480_1_);
					}
					MinecraftForge.EVENT_BUS.post(new DrawScreenEvent.Post(mc.currentScreen, k, l, p_78480_1_));
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
					crashreportcategory.addCrashSectionCallable("Screen name", new Callable() {
						private static final String __OBFID = "CL_00000948";

						@Override
						public String call() {
							return mc.currentScreen.getClass().getCanonicalName();
						}
					});
					crashreportcategory.addCrashSectionCallable("Mouse location", new Callable() {
						private static final String __OBFID = "CL_00000950";

						@Override
						public String call() {
							return String.format("Scaled: (%d, %d). Absolute: (%d, %d)",
									Integer.valueOf(k), Integer.valueOf(l),
									Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY()));
						}
					});
					crashreportcategory.addCrashSectionCallable("Screen size", new Callable() {
						private static final String __OBFID = "CL_00000951";

						@Override
						public String call() {
							return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
									Integer.valueOf(scaledresolution.getScaledWidth()),
									Integer.valueOf(scaledresolution.getScaledHeight()),
									Integer.valueOf(mc.displayWidth), Integer.valueOf(mc.displayHeight),
									Integer.valueOf(scaledresolution.getScaleFactor()));
						}
					});
					throw new ReportedException(crashreport);
				}
			}
		}
	}

	public void func_152430_c(float p_152430_1_) {
		setupOverlayRendering();
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		mc.ingameGUI.func_152126_a(i, j);
	}

	public void renderWorld(float p_78471_1_, long p_78471_2_) {
		mc.mcProfiler.startSection("lightTex");

		if (lightmapUpdateNeeded) {
			updateLightmap(p_78471_1_);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);

		if (mc.renderViewEntity == null) {
			mc.renderViewEntity = mc.thePlayer;
		}

		mc.mcProfiler.endStartSection("pick");
		getMouseOver(p_78471_1_);
		EntityLivingBase entitylivingbase = mc.renderViewEntity;
		RenderGlobal renderglobal = mc.renderGlobal;
		EffectRenderer effectrenderer = mc.effectRenderer;
		double d0 = entitylivingbase.lastTickPosX
				+ (entitylivingbase.posX - entitylivingbase.lastTickPosX) * p_78471_1_;
		double d1 = entitylivingbase.lastTickPosY
				+ (entitylivingbase.posY - entitylivingbase.lastTickPosY) * p_78471_1_;
		double d2 = entitylivingbase.lastTickPosZ
				+ (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * p_78471_1_;
		mc.mcProfiler.endStartSection("center");

		for (int j = 0; j < 2; ++j) {
			if (mc.gameSettings.anaglyph) {
				anaglyphField = j;

				if (anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			mc.mcProfiler.endStartSection("clear");
			GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
			updateFogColor(p_78471_1_);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			mc.mcProfiler.endStartSection("camera");
			setupCameraTransform(p_78471_1_, j);
			ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
			mc.mcProfiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();

			if (mc.gameSettings.renderDistanceChunks >= 4) {
				setupFog(-1, p_78471_1_);
				mc.mcProfiler.endStartSection("sky");
				renderglobal.renderSky(p_78471_1_);
			}

			GL11.glEnable(GL11.GL_FOG);
			setupFog(1, p_78471_1_);

			if (mc.gameSettings.ambientOcclusion != 0) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			mc.mcProfiler.endStartSection("culling");
			Frustrum frustrum = new Frustrum();
			frustrum.setPosition(d0, d1, d2);
			mc.renderGlobal.clipRenderersByFrustum(frustrum, p_78471_1_);

			if (j == 0) {
				mc.mcProfiler.endStartSection("updatechunks");

				while (!mc.renderGlobal.updateRenderers(entitylivingbase, false) && p_78471_2_ != 0L) {
					long k = p_78471_2_ - System.nanoTime();

					if (k < 0L || k > 1000000000L) {
						break;
					}
				}
			}

			if (entitylivingbase.posY < 128.0D) {
				renderCloudsCheck(renderglobal, p_78471_1_);
			}

			mc.mcProfiler.endStartSection("prepareterrain");
			setupFog(0, p_78471_1_);
			GL11.glEnable(GL11.GL_FOG);
			mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			RenderHelper.disableStandardItemLighting();
			mc.mcProfiler.endStartSection("terrain");
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			renderglobal.sortAndRender(entitylivingbase, 0, p_78471_1_);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			EntityPlayer entityplayer;

			if (debugViewDirection == 0) {
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				RenderHelper.enableStandardItemLighting();
				mc.mcProfiler.endStartSection("entities");
				net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
				renderglobal.renderEntities(entitylivingbase, frustrum, p_78471_1_);
				net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
				// ToDo: Try and figure out how to make particles render sorted correctly..
				// {They render behind water}
				RenderHelper.disableStandardItemLighting();
				disableLightmap(p_78471_1_);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();

				if (mc.objectMouseOver != null && entitylivingbase.isInsideOfMaterial(Material.water)
						&& entitylivingbase instanceof EntityPlayer && !mc.gameSettings.hideGUI) {
					entityplayer = (EntityPlayer) entitylivingbase;
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					mc.mcProfiler.endStartSection("outline");
					if (!ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, mc.objectMouseOver, 0,
							entityplayer.inventory.getCurrentItem(), p_78471_1_)) {
						renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, p_78471_1_);
					}
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();

			if (cameraZoom == 1.0D && entitylivingbase instanceof EntityPlayer && !mc.gameSettings.hideGUI
					&& mc.objectMouseOver != null && !entitylivingbase.isInsideOfMaterial(Material.water)) {
				entityplayer = (EntityPlayer) entitylivingbase;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				mc.mcProfiler.endStartSection("outline");
				if (!ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, mc.objectMouseOver, 0,
						entityplayer.inventory.getCurrentItem(), p_78471_1_)) {
					renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, p_78471_1_);
				}
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			mc.mcProfiler.endStartSection("destroyProgress");
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			renderglobal.drawBlockDamageTexture(Tessellator.instance, entitylivingbase, p_78471_1_);
			GL11.glDisable(GL11.GL_BLEND);

			if (debugViewDirection == 0) {
				enableLightmap(p_78471_1_);
				mc.mcProfiler.endStartSection("litParticles");
				effectrenderer.renderLitParticles(entitylivingbase, p_78471_1_);
				RenderHelper.disableStandardItemLighting();
				setupFog(0, p_78471_1_);
				mc.mcProfiler.endStartSection("particles");
				effectrenderer.renderParticles(entitylivingbase, p_78471_1_);
				disableLightmap(p_78471_1_);
			}

			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_CULL_FACE);
			mc.mcProfiler.endStartSection("weather");
			renderRainSnow(p_78471_1_);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			setupFog(0, p_78471_1_);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

			if (mc.gameSettings.fancyGraphics) {
				mc.mcProfiler.endStartSection("water");

				if (mc.gameSettings.ambientOcclusion != 0) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);

				if (mc.gameSettings.anaglyph) {
					if (anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}

					renderglobal.sortAndRender(entitylivingbase, 1, p_78471_1_);
				} else {
					renderglobal.sortAndRender(entitylivingbase, 1, p_78471_1_);
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				mc.mcProfiler.endStartSection("water");
				renderglobal.sortAndRender(entitylivingbase, 1, p_78471_1_);
			}

			if (debugViewDirection == 0) // Only render if render pass 0 happens as well.
			{
				RenderHelper.enableStandardItemLighting();
				mc.mcProfiler.endStartSection("entities");
				ForgeHooksClient.setRenderPass(1);
				renderglobal.renderEntities(entitylivingbase, frustrum, p_78471_1_);
				ForgeHooksClient.setRenderPass(-1);
				RenderHelper.disableStandardItemLighting();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);

			if (entitylivingbase.posY >= 128.0D) {
				mc.mcProfiler.endStartSection("aboveClouds");
				renderCloudsCheck(renderglobal, p_78471_1_);
			}

			mc.mcProfiler.endStartSection("FRenderLast");
			ForgeHooksClient.dispatchRenderLast(renderglobal, p_78471_1_);

			mc.mcProfiler.endStartSection("hand");

			if (!ForgeHooksClient.renderFirstPersonHand(renderglobal, p_78471_1_, j) && cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				renderHand(p_78471_1_, j);
			}

			if (!mc.gameSettings.anaglyph) {
				mc.mcProfiler.endSection();
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
		mc.mcProfiler.endSection();
	}

	private void renderCloudsCheck(RenderGlobal p_82829_1_, float p_82829_2_) {
		if (mc.gameSettings.shouldRenderClouds()) {
			mc.mcProfiler.endStartSection("clouds");
			GL11.glPushMatrix();
			setupFog(0, p_82829_2_);
			GL11.glEnable(GL11.GL_FOG);
			p_82829_1_.renderClouds(p_82829_2_);
			GL11.glDisable(GL11.GL_FOG);
			setupFog(1, p_82829_2_);
			GL11.glPopMatrix();
		}
	}

	private void addRainParticles() {
		float f = mc.theWorld.getRainStrength(1.0F);

		if (!mc.gameSettings.fancyGraphics) {
			f /= 2.0F;
		}

		if (f != 0.0F) {
			random.setSeed(rendererUpdateCount * 312987231L);
			EntityLivingBase entitylivingbase = mc.renderViewEntity;
			WorldClient worldclient = mc.theWorld;
			int i = MathHelper.floor_double(entitylivingbase.posX);
			int j = MathHelper.floor_double(entitylivingbase.posY);
			int k = MathHelper.floor_double(entitylivingbase.posZ);
			byte b0 = 10;
			double d0 = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			int l = 0;
			int i1 = (int) (100.0F * f * f);

			if (mc.gameSettings.particleSetting == 1) {
				i1 >>= 1;
			} else if (mc.gameSettings.particleSetting == 2) {
				i1 = 0;
			}

			for (int j1 = 0; j1 < i1; ++j1) {
				int k1 = i + random.nextInt(b0) - random.nextInt(b0);
				int l1 = k + random.nextInt(b0) - random.nextInt(b0);
				int i2 = worldclient.getPrecipitationHeight(k1, l1);
				Block block = worldclient.getBlock(k1, i2 - 1, l1);
				BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(k1, l1);

				if (i2 <= j + b0 && i2 >= j - b0 && biomegenbase.canSpawnLightningBolt()
						&& biomegenbase.getFloatTemperature(k1, i2, l1) >= 0.15F) {
					float f1 = random.nextFloat();
					float f2 = random.nextFloat();

					if (block.getMaterial() == Material.lava) {
						mc.effectRenderer.addEffect(new EntitySmokeFX(worldclient, k1 + f1,
								i2 + 0.1F - block.getBlockBoundsMinY(), l1 + f2, 0.0D, 0.0D, 0.0D));
					} else if (block.getMaterial() != Material.air) {
						++l;

						if (random.nextInt(l) == 0) {
							d0 = k1 + f1;
							d1 = i2 + 0.1F - block.getBlockBoundsMinY();
							d2 = l1 + f2;
						}

						mc.effectRenderer.addEffect(new EntityRainFX(worldclient, k1 + f1,
								i2 + 0.1F - block.getBlockBoundsMinY(), l1 + f2));
					}
				}
			}

			if (l > 0 && random.nextInt(3) < rainSoundCounter++) {
				rainSoundCounter = 0;

				if (d1 > entitylivingbase.posY + 1.0D
						&& worldclient.getPrecipitationHeight(MathHelper.floor_double(entitylivingbase.posX),
								MathHelper.floor_double(entitylivingbase.posZ)) > MathHelper
										.floor_double(entitylivingbase.posY)) {
					mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
				} else {
					mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
				}
			}
		}
	}

	protected void renderRainSnow(float p_78474_1_) {
		IRenderHandler renderer = null;
		if ((renderer = mc.theWorld.provider.getWeatherRenderer()) != null) {
			renderer.render(p_78474_1_, mc.theWorld, mc);
			return;
		}

		float f1 = mc.theWorld.getRainStrength(p_78474_1_);

		if (f1 > 0.0F) {
			enableLightmap(p_78474_1_);

			if (rainXCoords == null) {
				rainXCoords = new float[1024];
				rainYCoords = new float[1024];

				for (int i = 0; i < 32; ++i) {
					for (int j = 0; j < 32; ++j) {
						float f2 = j - 16;
						float f3 = i - 16;
						float f4 = MathHelper.sqrt_float(f2 * f2 + f3 * f3);
						rainXCoords[i << 5 | j] = -f3 / f4;
						rainYCoords[i << 5 | j] = f2 / f4;
					}
				}
			}

			EntityLivingBase entitylivingbase = mc.renderViewEntity;
			WorldClient worldclient = mc.theWorld;
			int k2 = MathHelper.floor_double(entitylivingbase.posX);
			int l2 = MathHelper.floor_double(entitylivingbase.posY);
			int i3 = MathHelper.floor_double(entitylivingbase.posZ);
			Tessellator tessellator = Tessellator.instance;
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			double d0 = entitylivingbase.lastTickPosX
					+ (entitylivingbase.posX - entitylivingbase.lastTickPosX) * p_78474_1_;
			double d1 = entitylivingbase.lastTickPosY
					+ (entitylivingbase.posY - entitylivingbase.lastTickPosY) * p_78474_1_;
			double d2 = entitylivingbase.lastTickPosZ
					+ (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * p_78474_1_;
			int k = MathHelper.floor_double(d1);
			byte b0 = 5;

			if (mc.gameSettings.fancyGraphics) {
				b0 = 10;
			}

			byte b1 = -1;
			float f5 = rendererUpdateCount + p_78474_1_;

			if (mc.gameSettings.fancyGraphics) {
				b0 = 10;
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			for (int l = i3 - b0; l <= i3 + b0; ++l) {
				for (int i1 = k2 - b0; i1 <= k2 + b0; ++i1) {
					int j1 = (l - i3 + 16) * 32 + i1 - k2 + 16;
					float f6 = rainXCoords[j1] * 0.5F;
					float f7 = rainYCoords[j1] * 0.5F;
					BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(i1, l);

					if (biomegenbase.canSpawnLightningBolt() || biomegenbase.getEnableSnow()) {
						int k1 = worldclient.getPrecipitationHeight(i1, l);
						int l1 = l2 - b0;
						int i2 = l2 + b0;

						if (l1 < k1) {
							l1 = k1;
						}

						if (i2 < k1) {
							i2 = k1;
						}

						float f8 = 1.0F;
						int j2 = k1;

						if (k1 < k) {
							j2 = k;
						}

						if (l1 != i2) {
							random.setSeed(i1 * i1 * 3121 + i1 * 45238971 ^ l * l * 418711 + l * 13761);
							float f9 = biomegenbase.getFloatTemperature(i1, l1, l);
							float f10;
							double d4;

							if (worldclient.getWorldChunkManager().getTemperatureAtHeight(f9, k1) >= 0.15F) {
								if (b1 != 0) {
									if (b1 >= 0) {
										tessellator.draw();
									}

									b1 = 0;
									mc.getTextureManager().bindTexture(locationRainPng);
									tessellator.startDrawingQuads();
								}

								f10 = ((rendererUpdateCount + i1 * i1 * 3121 + i1 * 45238971 + l * l * 418711
										+ l * 13761 & 31) + p_78474_1_) / 32.0F * (3.0F + random.nextFloat());
								double d3 = i1 + 0.5F - entitylivingbase.posX;
								d4 = l + 0.5F - entitylivingbase.posZ;
								float f12 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / b0;
								float f13 = 1.0F;
								tessellator.setBrightness(worldclient.getLightBrightnessForSkyBlocks(i1, j2, l, 0));
								tessellator.setColorRGBA_F(f13, f13, f13, ((1.0F - f12 * f12) * 0.5F + 0.5F) * f1);
								tessellator.setTranslation(-d0 * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
								tessellator.addVertexWithUV(i1 - f6 + 0.5D, l1, l - f7 + 0.5D, 0.0F * f8,
										l1 * f8 / 4.0F + f10 * f8);
								tessellator.addVertexWithUV(i1 + f6 + 0.5D, l1, l + f7 + 0.5D, 1.0F * f8,
										l1 * f8 / 4.0F + f10 * f8);
								tessellator.addVertexWithUV(i1 + f6 + 0.5D, i2, l + f7 + 0.5D, 1.0F * f8,
										i2 * f8 / 4.0F + f10 * f8);
								tessellator.addVertexWithUV(i1 - f6 + 0.5D, i2, l - f7 + 0.5D, 0.0F * f8,
										i2 * f8 / 4.0F + f10 * f8);
								tessellator.setTranslation(0.0D, 0.0D, 0.0D);
							} else {
								if (b1 != 1) {
									if (b1 >= 0) {
										tessellator.draw();
									}

									b1 = 1;
									mc.getTextureManager().bindTexture(locationSnowPng);
									tessellator.startDrawingQuads();
								}

								f10 = ((rendererUpdateCount & 511) + p_78474_1_) / 512.0F;
								float f16 = random.nextFloat() + f5 * 0.01F * (float) random.nextGaussian();
								float f11 = random.nextFloat() + f5 * (float) random.nextGaussian() * 0.001F;
								d4 = i1 + 0.5F - entitylivingbase.posX;
								double d5 = l + 0.5F - entitylivingbase.posZ;
								float f14 = MathHelper.sqrt_double(d4 * d4 + d5 * d5) / b0;
								float f15 = 1.0F;
								tessellator.setBrightness(
										(worldclient.getLightBrightnessForSkyBlocks(i1, j2, l, 0) * 3 + 15728880) / 4);
								tessellator.setColorRGBA_F(f15, f15, f15, ((1.0F - f14 * f14) * 0.3F + 0.5F) * f1);
								tessellator.setTranslation(-d0 * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
								tessellator.addVertexWithUV(i1 - f6 + 0.5D, l1, l - f7 + 0.5D, 0.0F * f8 + f16,
										l1 * f8 / 4.0F + f10 * f8 + f11);
								tessellator.addVertexWithUV(i1 + f6 + 0.5D, l1, l + f7 + 0.5D, 1.0F * f8 + f16,
										l1 * f8 / 4.0F + f10 * f8 + f11);
								tessellator.addVertexWithUV(i1 + f6 + 0.5D, i2, l + f7 + 0.5D, 1.0F * f8 + f16,
										i2 * f8 / 4.0F + f10 * f8 + f11);
								tessellator.addVertexWithUV(i1 - f6 + 0.5D, i2, l - f7 + 0.5D, 0.0F * f8 + f16,
										i2 * f8 / 4.0F + f10 * f8 + f11);
								tessellator.setTranslation(0.0D, 0.0D, 0.0D);
							}
						}
					}
				}
			}

			if (b1 >= 0) {
				tessellator.draw();
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			disableLightmap(p_78474_1_);
		}
	}

	public void setupOverlayRendering() {
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D,
				1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float p_78466_1_) {
		WorldClient worldclient = mc.theWorld;
		EntityLivingBase entitylivingbase = mc.renderViewEntity;
		float f1 = 0.25F + 0.75F * mc.gameSettings.renderDistanceChunks / 16.0F;
		f1 = 1.0F - (float) Math.pow(f1, 0.25D);
		Vec3 vec3 = worldclient.getSkyColor(mc.renderViewEntity, p_78466_1_);
		float f2 = (float) vec3.xCoord;
		float f3 = (float) vec3.yCoord;
		float f4 = (float) vec3.zCoord;
		Vec3 vec31 = worldclient.getFogColor(p_78466_1_);
		fogColorRed = (float) vec31.xCoord;
		fogColorGreen = (float) vec31.yCoord;
		fogColorBlue = (float) vec31.zCoord;
		float f5;

		if (mc.gameSettings.renderDistanceChunks >= 4) {
			Vec3 vec32 = MathHelper.sin(worldclient.getCelestialAngleRadians(p_78466_1_)) > 0.0F
					? Vec3.createVectorHelper(-1.0D, 0.0D, 0.0D)
					: Vec3.createVectorHelper(1.0D, 0.0D, 0.0D);
			f5 = (float) entitylivingbase.getLook(p_78466_1_).dotProduct(vec32);

			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			if (f5 > 0.0F) {
				float[] afloat = worldclient.provider.calcSunriseSunsetColors(worldclient.getCelestialAngle(p_78466_1_),
						p_78466_1_);

				if (afloat != null) {
					f5 *= afloat[3];
					fogColorRed = fogColorRed * (1.0F - f5) + afloat[0] * f5;
					fogColorGreen = fogColorGreen * (1.0F - f5) + afloat[1] * f5;
					fogColorBlue = fogColorBlue * (1.0F - f5) + afloat[2] * f5;
				}
			}
		}

		fogColorRed += (f2 - fogColorRed) * f1;
		fogColorGreen += (f3 - fogColorGreen) * f1;
		fogColorBlue += (f4 - fogColorBlue) * f1;
		float f8 = worldclient.getRainStrength(p_78466_1_);
		float f9;

		if (f8 > 0.0F) {
			f5 = 1.0F - f8 * 0.5F;
			f9 = 1.0F - f8 * 0.4F;
			fogColorRed *= f5;
			fogColorGreen *= f5;
			fogColorBlue *= f9;
		}

		f5 = worldclient.getWeightedThunderStrength(p_78466_1_);

		if (f5 > 0.0F) {
			f9 = 1.0F - f5 * 0.5F;
			fogColorRed *= f9;
			fogColorGreen *= f9;
			fogColorBlue *= f9;
		}

		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entitylivingbase, p_78466_1_);
		float f10;

		if (cloudFog) {
			Vec3 vec33 = worldclient.getCloudColour(p_78466_1_);
			fogColorRed = (float) vec33.xCoord;
			fogColorGreen = (float) vec33.yCoord;
			fogColorBlue = (float) vec33.zCoord;
		} else if (block.getMaterial() == Material.water) {
			f10 = EnchantmentHelper.getRespiration(entitylivingbase) * 0.2F;
			fogColorRed = 0.02F + f10;
			fogColorGreen = 0.02F + f10;
			fogColorBlue = 0.2F + f10;
		} else if (block.getMaterial() == Material.lava) {
			fogColorRed = 0.6F;
			fogColorGreen = 0.1F;
			fogColorBlue = 0.0F;
		}

		f10 = fogColor2 + (fogColor1 - fogColor2) * p_78466_1_;
		fogColorRed *= f10;
		fogColorGreen *= f10;
		fogColorBlue *= f10;
		double d0 = (entitylivingbase.lastTickPosY
				+ (entitylivingbase.posY - entitylivingbase.lastTickPosY) * p_78466_1_)
				* worldclient.provider.getVoidFogYFactor();

		if (entitylivingbase.isPotionActive(Potion.blindness)) {
			int i = entitylivingbase.getActivePotionEffect(Potion.blindness).getDuration();

			if (i < 20) {
				d0 *= 1.0F - i / 20.0F;
			} else {
				d0 = 0.0D;
			}
		}

		if (d0 < 1.0D) {
			if (d0 < 0.0D) {
				d0 = 0.0D;
			}

			d0 *= d0;
			fogColorRed = (float) (fogColorRed * d0);
			fogColorGreen = (float) (fogColorGreen * d0);
			fogColorBlue = (float) (fogColorBlue * d0);
		}

		float f11;

		if (bossColorModifier > 0.0F) {
			f11 = bossColorModifierPrev + (bossColorModifier - bossColorModifierPrev) * p_78466_1_;
			fogColorRed = fogColorRed * (1.0F - f11) + fogColorRed * 0.7F * f11;
			fogColorGreen = fogColorGreen * (1.0F - f11) + fogColorGreen * 0.6F * f11;
			fogColorBlue = fogColorBlue * (1.0F - f11) + fogColorBlue * 0.6F * f11;
		}

		float f6;

		if (entitylivingbase.isPotionActive(Potion.nightVision)) {
			f11 = getNightVisionBrightness(mc.thePlayer, p_78466_1_);
			f6 = 1.0F / fogColorRed;

			if (f6 > 1.0F / fogColorGreen) {
				f6 = 1.0F / fogColorGreen;
			}

			if (f6 > 1.0F / fogColorBlue) {
				f6 = 1.0F / fogColorBlue;
			}

			fogColorRed = fogColorRed * (1.0F - f11) + fogColorRed * f6 * f11;
			fogColorGreen = fogColorGreen * (1.0F - f11) + fogColorGreen * f6 * f11;
			fogColorBlue = fogColorBlue * (1.0F - f11) + fogColorBlue * f6 * f11;
		}

		if (mc.gameSettings.anaglyph) {
			f11 = (fogColorRed * 30.0F + fogColorGreen * 59.0F + fogColorBlue * 11.0F) / 100.0F;
			f6 = (fogColorRed * 30.0F + fogColorGreen * 70.0F) / 100.0F;
			float f7 = (fogColorRed * 30.0F + fogColorBlue * 70.0F) / 100.0F;
			fogColorRed = f11;
			fogColorGreen = f6;
			fogColorBlue = f7;
		}

		net.minecraftforge.client.event.EntityViewRenderEvent.FogColors event = new net.minecraftforge.client.event.EntityViewRenderEvent.FogColors(
				this, entitylivingbase, block, p_78466_1_, fogColorRed, fogColorGreen, fogColorBlue);
		MinecraftForge.EVENT_BUS.post(event);

		fogColorRed = event.red;
		fogColorBlue = event.blue;
		fogColorGreen = event.green;

		GL11.glClearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
	}

	private void setupFog(int p_78468_1_, float p_78468_2_) {
		EntityLivingBase entitylivingbase = mc.renderViewEntity;
		boolean flag = false;

		if (entitylivingbase instanceof EntityPlayer) {
			flag = ((EntityPlayer) entitylivingbase).capabilities.isCreativeMode;
		}

		if (p_78468_1_ == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, 8.0F);

			if (GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}

			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
		} else {
			GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue, 1.0F));
			GL11.glNormal3f(0.0F, -1.0F, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entitylivingbase, p_78468_2_);
			float f1;

			net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity event = new net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity(
					this, entitylivingbase, block, p_78468_2_, 0.1F);

			if (MinecraftForge.EVENT_BUS.post(event)) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, event.density);
			} else if (entitylivingbase.isPotionActive(Potion.blindness)) {
				f1 = 5.0F;
				int j = entitylivingbase.getActivePotionEffect(Potion.blindness).getDuration();

				if (j < 20) {
					f1 = 5.0F + (farPlaneDistance - 5.0F) * (1.0F - j / 20.0F);
				}

				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

				if (p_78468_1_ < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
					GL11.glFogf(GL11.GL_FOG_END, f1 * 0.8F);
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25F);
					GL11.glFogf(GL11.GL_FOG_END, f1);
				}

				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(34138, 34139);
				}
			} else if (cloudFog) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if (block.getMaterial() == Material.water) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

				if (entitylivingbase.isPotionActive(Potion.waterBreathing)) {
					GL11.glFogf(GL11.GL_FOG_DENSITY, 0.05F);
				} else {
					GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F - EnchantmentHelper.getRespiration(entitylivingbase) * 0.03F);
				}
			} else if (block.getMaterial() == Material.lava) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			} else {
				f1 = farPlaneDistance;

				if (mc.theWorld.provider.getWorldHasVoidParticles() && !flag) {
					double d0 = ((entitylivingbase.getBrightnessForRender(p_78468_2_) & 15728640) >> 20) / 16.0D
							+ (entitylivingbase.lastTickPosY
									+ (entitylivingbase.posY - entitylivingbase.lastTickPosY) * p_78468_2_ + 4.0D)
									/ 32.0D;

					if (d0 < 1.0D) {
						if (d0 < 0.0D) {
							d0 = 0.0D;
						}

						d0 *= d0;
						float f2 = 100.0F * (float) d0;

						if (f2 < 5.0F) {
							f2 = 5.0F;
						}

						if (f1 > f2) {
							f1 = f2;
						}
					}
				}

				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

				if (p_78468_1_ < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
					GL11.glFogf(GL11.GL_FOG_END, f1);
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.75F);
					GL11.glFogf(GL11.GL_FOG_END, f1);
				}

				if (GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(34138, 34139);
				}

				if (mc.theWorld.provider.doesXZShowFog((int) entitylivingbase.posX, (int) entitylivingbase.posZ)) {
					GL11.glFogf(GL11.GL_FOG_START, f1 * 0.05F);
					GL11.glFogf(GL11.GL_FOG_END, Math.min(f1, 192.0F) * 0.5F);
				}
				MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent(
						this, entitylivingbase, block, p_78468_2_, p_78468_1_, f1));
			}

			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
		}
	}

	private FloatBuffer setFogColorBuffer(float p_78469_1_, float p_78469_2_, float p_78469_3_, float p_78469_4_) {
		fogColorBuffer.clear();
		fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_);
		fogColorBuffer.flip();
		return fogColorBuffer;
	}

	public MapItemRenderer getMapItemRenderer() {
		return theMapItemRenderer;
	}
}