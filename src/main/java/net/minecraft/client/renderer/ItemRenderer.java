package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.*;

@SideOnly(Side.CLIENT)
public class ItemRenderer {
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
	private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
	private Minecraft mc;
	private ItemStack itemToRender;
	private float equippedProgress;
	private float prevEquippedProgress;
	private RenderBlocks renderBlocksIr = new RenderBlocks();
	private int equippedItemSlot = -1;
	private static final String __OBFID = "CL_00000953";

	public ItemRenderer(Minecraft p_i1247_1_) {
		mc = p_i1247_1_;
	}

	public void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_, int p_78443_3_) {
		this.renderItem(p_78443_1_, p_78443_2_, p_78443_3_, EQUIPPED);
	}

	public void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_, int p_78443_3_, ItemRenderType type) {
		GL11.glPushMatrix();
		TextureManager texturemanager = mc.getTextureManager();
		Item item = p_78443_2_.getItem();
		Block block = Block.getBlockFromItem(item);

		if (p_78443_2_ != null && block != null && block.getRenderBlockPass() != 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}
		IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(p_78443_2_, type);
		if (customRenderer != null) {
			texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
			ForgeHooksClient.renderEquippedItem(type, customRenderer, renderBlocksIr, p_78443_1_, p_78443_2_);
		} else if (p_78443_2_.getItemSpriteNumber() == 0 && item instanceof ItemBlock
				&& RenderBlocks.renderItemIn3d(block.getRenderType())) {
			texturemanager.bindTexture(texturemanager.getResourceLocation(0));

			if (p_78443_2_ != null && block != null && block.getRenderBlockPass() != 0) {
				GL11.glDepthMask(false);
				renderBlocksIr.renderBlockAsItem(block, p_78443_2_.getItemDamage(), 1.0F);
				GL11.glDepthMask(true);
			} else {
				renderBlocksIr.renderBlockAsItem(block, p_78443_2_.getItemDamage(), 1.0F);
			}
		} else {
			IIcon iicon = p_78443_1_.getItemIcon(p_78443_2_, p_78443_3_);

			if (iicon == null) {
				GL11.glPopMatrix();
				return;
			}

			texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
			TextureUtil.func_152777_a(false, false, 1.0F);
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();
			float f4 = 0.0F;
			float f5 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-f4, -f5, 0.0F);
			float f6 = 1.5F;
			GL11.glScalef(f6, f6, f6);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
			renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);

			if (p_78443_2_.hasEffect(p_78443_3_)) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				texturemanager.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				float f7 = 0.76F;
				GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float f8 = 0.125F;
				GL11.glScalef(f8, f8, f8);
				float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
				GL11.glTranslatef(f9, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(f8, f8, f8);
				f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
				GL11.glTranslatef(-f9, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
			TextureUtil.func_147945_b();
		}

		if (p_78443_2_ != null && block != null && block.getRenderBlockPass() != 0) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glPopMatrix();
	}

	public static void renderItemIn2D(Tessellator p_78439_0_, float p_78439_1_, float p_78439_2_, float p_78439_3_,
			float p_78439_4_, int p_78439_5_, int p_78439_6_, float p_78439_7_) {
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 0.0F, 1.0F);
		p_78439_0_.addVertexWithUV(0.0D, 0.0D, 0.0D, p_78439_1_, p_78439_4_);
		p_78439_0_.addVertexWithUV(1.0D, 0.0D, 0.0D, p_78439_3_, p_78439_4_);
		p_78439_0_.addVertexWithUV(1.0D, 1.0D, 0.0D, p_78439_3_, p_78439_2_);
		p_78439_0_.addVertexWithUV(0.0D, 1.0D, 0.0D, p_78439_1_, p_78439_2_);
		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 0.0F, -1.0F);
		p_78439_0_.addVertexWithUV(0.0D, 1.0D, 0.0F - p_78439_7_, p_78439_1_, p_78439_2_);
		p_78439_0_.addVertexWithUV(1.0D, 1.0D, 0.0F - p_78439_7_, p_78439_3_, p_78439_2_);
		p_78439_0_.addVertexWithUV(1.0D, 0.0D, 0.0F - p_78439_7_, p_78439_3_, p_78439_4_);
		p_78439_0_.addVertexWithUV(0.0D, 0.0D, 0.0F - p_78439_7_, p_78439_1_, p_78439_4_);
		p_78439_0_.draw();
		float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / p_78439_5_;
		float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / p_78439_6_;
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(-1.0F, 0.0F, 0.0F);
		int k;
		float f7;
		float f8;

		for (k = 0; k < p_78439_5_; ++k) {
			f7 = (float) k / (float) p_78439_5_;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			p_78439_0_.addVertexWithUV(f7, 0.0D, 0.0F - p_78439_7_, f8, p_78439_4_);
			p_78439_0_.addVertexWithUV(f7, 0.0D, 0.0D, f8, p_78439_4_);
			p_78439_0_.addVertexWithUV(f7, 1.0D, 0.0D, f8, p_78439_2_);
			p_78439_0_.addVertexWithUV(f7, 1.0D, 0.0F - p_78439_7_, f8, p_78439_2_);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(1.0F, 0.0F, 0.0F);
		float f9;

		for (k = 0; k < p_78439_5_; ++k) {
			f7 = (float) k / (float) p_78439_5_;
			f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
			f9 = f7 + 1.0F / p_78439_5_;
			p_78439_0_.addVertexWithUV(f9, 1.0D, 0.0F - p_78439_7_, f8, p_78439_2_);
			p_78439_0_.addVertexWithUV(f9, 1.0D, 0.0D, f8, p_78439_2_);
			p_78439_0_.addVertexWithUV(f9, 0.0D, 0.0D, f8, p_78439_4_);
			p_78439_0_.addVertexWithUV(f9, 0.0D, 0.0F - p_78439_7_, f8, p_78439_4_);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, 1.0F, 0.0F);

		for (k = 0; k < p_78439_6_; ++k) {
			f7 = (float) k / (float) p_78439_6_;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			f9 = f7 + 1.0F / p_78439_6_;
			p_78439_0_.addVertexWithUV(0.0D, f9, 0.0D, p_78439_1_, f8);
			p_78439_0_.addVertexWithUV(1.0D, f9, 0.0D, p_78439_3_, f8);
			p_78439_0_.addVertexWithUV(1.0D, f9, 0.0F - p_78439_7_, p_78439_3_, f8);
			p_78439_0_.addVertexWithUV(0.0D, f9, 0.0F - p_78439_7_, p_78439_1_, f8);
		}

		p_78439_0_.draw();
		p_78439_0_.startDrawingQuads();
		p_78439_0_.setNormal(0.0F, -1.0F, 0.0F);

		for (k = 0; k < p_78439_6_; ++k) {
			f7 = (float) k / (float) p_78439_6_;
			f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
			p_78439_0_.addVertexWithUV(1.0D, f7, 0.0D, p_78439_3_, f8);
			p_78439_0_.addVertexWithUV(0.0D, f7, 0.0D, p_78439_1_, f8);
			p_78439_0_.addVertexWithUV(0.0D, f7, 0.0F - p_78439_7_, p_78439_1_, f8);
			p_78439_0_.addVertexWithUV(1.0D, f7, 0.0F - p_78439_7_, p_78439_3_, f8);
		}

		p_78439_0_.draw();
	}

	public void renderItemInFirstPerson(float p_78440_1_) {
		float f1 = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * p_78440_1_;
		EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
		float f2 = entityclientplayermp.prevRotationPitch
				+ (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * p_78440_1_;
		GL11.glPushMatrix();
		GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(
				entityclientplayermp.prevRotationYaw
						+ (entityclientplayermp.rotationYaw - entityclientplayermp.prevRotationYaw) * p_78440_1_,
				0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		EntityPlayerSP entityplayersp = entityclientplayermp;
		float f3 = entityplayersp.prevRenderArmPitch
				+ (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_78440_1_;
		float f4 = entityplayersp.prevRenderArmYaw
				+ (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_78440_1_;
		GL11.glRotatef((entityclientplayermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((entityclientplayermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
		ItemStack itemstack = itemToRender;

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityclientplayermp.posX),
				MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ),
				0);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f5;
		float f6;
		float f7;

		if (itemstack != null) {
			int l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
			f5 = (l >> 16 & 255) / 255.0F;
			f6 = (l >> 8 & 255) / 255.0F;
			f7 = (l & 255) / 255.0F;
			GL11.glColor4f(f5, f6, f7, 1.0F);
		} else {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}

		float f8;
		float f9;
		float f10;
		float f13;
		Render render;
		RenderPlayer renderplayer;

		if (itemstack != null && itemstack.getItem() instanceof ItemMap) {
			GL11.glPushMatrix();
			f13 = 0.8F;
			f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
			f6 = MathHelper.sin(f5 * (float) Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
			GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.2F,
					-f6 * 0.2F);
			f5 = 1.0F - f2 / 45.0F + 0.1F;

			if (f5 < 0.0F) {
				f5 = 0.0F;
			}

			if (f5 > 1.0F) {
				f5 = 1.0F;
			}

			f5 = -MathHelper.cos(f5 * (float) Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * f13 - (1.0F - f1) * 1.2F - f5 * 0.5F + 0.04F, -0.9F * f13);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(f5 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());

			for (int i1 = 0; i1 < 2; ++i1) {
				int j1 = i1 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * j1);
				GL11.glRotatef(-45 * j1, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-65 * j1, 0.0F, 1.0F, 0.0F);
				render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
				renderplayer = (RenderPlayer) render;
				f10 = 1.0F;
				GL11.glScalef(f10, f10, f10);
				renderplayer.renderFirstPersonArm(mc.thePlayer);
				GL11.glPopMatrix();
			}

			f6 = entityclientplayermp.getSwingProgress(p_78440_1_);
			f7 = MathHelper.sin(f6 * f6 * (float) Math.PI);
			f8 = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f8 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f8 * 80.0F, 1.0F, 0.0F, 0.0F);
			f9 = 0.38F;
			GL11.glScalef(f9, f9, f9);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			f10 = 0.015625F;
			GL11.glScalef(f10, f10, f10);
			mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
			Tessellator tessellator = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			tessellator.startDrawingQuads();
			byte b0 = 7;
			tessellator.addVertexWithUV(0 - b0, 128 + b0, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(128 + b0, 128 + b0, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(128 + b0, 0 - b0, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(0 - b0, 0 - b0, 0.0D, 0.0D, 0.0D);
			tessellator.draw();

			IItemRenderer custom = MinecraftForgeClient.getItemRenderer(itemstack, FIRST_PERSON_MAP);
			MapData mapdata = ((ItemMap) itemstack.getItem()).getMapData(itemstack, mc.theWorld);

			if (custom == null) {
				if (mapdata != null) {
					mc.entityRenderer.getMapItemRenderer().func_148250_a(mapdata, false);
				}
			} else {
				custom.renderItem(FIRST_PERSON_MAP, itemstack, mc.thePlayer, mc.getTextureManager(), mapdata);
			}

			GL11.glPopMatrix();
		} else if (itemstack != null) {
			GL11.glPushMatrix();
			f13 = 0.8F;

			if (entityclientplayermp.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.eat || enumaction == EnumAction.drink) {
					f6 = entityclientplayermp.getItemInUseCount() - p_78440_1_ + 1.0F;
					f7 = 1.0F - f6 / itemstack.getMaxItemUseDuration();
					f8 = 1.0F - f7;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f8 = f8 * f8 * f8;
					f9 = 1.0F - f8;
					GL11.glTranslatef(0.0F,
							MathHelper.abs(MathHelper.cos(f6 / 4.0F * (float) Math.PI) * 0.1F) * (f7 > 0.2D ? 1 : 0),
							0.0F);
					GL11.glTranslatef(f9 * 0.6F, -f9 * 0.5F, 0.0F);
					GL11.glRotatef(f9 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(f9 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(f9 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
				f6 = MathHelper.sin(f5 * (float) Math.PI);
				f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
				GL11.glTranslatef(-f7 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.2F,
						-f6 * 0.2F);
			}

			GL11.glTranslatef(0.7F * f13, -0.65F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
			f6 = MathHelper.sin(f5 * f5 * (float) Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
			GL11.glRotatef(-f6 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f7 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f7 * 80.0F, 1.0F, 0.0F, 0.0F);
			f8 = 0.4F;
			GL11.glScalef(f8, f8, f8);
			float f11;
			float f12;

			if (entityclientplayermp.getItemInUseCount() > 0) {
				EnumAction enumaction1 = itemstack.getItemUseAction();

				if (enumaction1 == EnumAction.block) {
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if (enumaction1 == EnumAction.bow) {
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					f10 = itemstack.getMaxItemUseDuration()
							- (entityclientplayermp.getItemInUseCount() - p_78440_1_ + 1.0F);
					f11 = f10 / 20.0F;
					f11 = (f11 * f11 + f11 * 2.0F) / 3.0F;

					if (f11 > 1.0F) {
						f11 = 1.0F;
					}

					if (f11 > 0.1F) {
						GL11.glTranslatef(0.0F, MathHelper.sin((f10 - 0.1F) * 1.3F) * 0.01F * (f11 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, f11 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					f12 = 1.0F + f11 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, f12);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				this.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
				for (int x = 1; x < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); x++) {
					int k1 = itemstack.getItem().getColorFromItemStack(itemstack, x);
					f10 = (k1 >> 16 & 255) / 255.0F;
					f11 = (k1 >> 8 & 255) / 255.0F;
					f12 = (k1 & 255) / 255.0F;
					GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);
					this.renderItem(entityclientplayermp, itemstack, x, EQUIPPED_FIRST_PERSON);
				}
			} else {
				this.renderItem(entityclientplayermp, itemstack, 0, EQUIPPED_FIRST_PERSON);
			}

			GL11.glPopMatrix();
		} else if (!entityclientplayermp.isInvisible()) {
			GL11.glPushMatrix();
			f13 = 0.8F;
			f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
			f6 = MathHelper.sin(f5 * (float) Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
			GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI * 2.0F) * 0.4F,
					-f6 * 0.4F);
			GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
			f6 = MathHelper.sin(f5 * f5 * (float) Math.PI);
			f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * (float) Math.PI);
			GL11.glRotatef(f7 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
			mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
			renderplayer = (RenderPlayer) render;
			f10 = 1.0F;
			GL11.glScalef(f10, f10, f10);
			renderplayer.renderFirstPersonArm(mc.thePlayer);
			GL11.glPopMatrix();
		}

		if (itemstack != null && itemstack.getItem() instanceof ItemCloth) {
			GL11.glDisable(GL11.GL_BLEND);
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderOverlays(float p_78447_1_) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		if (mc.thePlayer.isBurning()) {
			if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
					.post(new net.minecraftforge.client.event.RenderBlockOverlayEvent(mc.thePlayer, p_78447_1_,
							net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType.FIRE, Blocks.fire,
							MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY),
							MathHelper.floor_double(mc.thePlayer.posZ)))) {
				renderFireInFirstPerson(p_78447_1_);
			}
		}

		if (mc.thePlayer.isEntityInsideOpaqueBlock()) {
			int i = MathHelper.floor_double(mc.thePlayer.posX);
			int j = MathHelper.floor_double(mc.thePlayer.posY);
			int k = MathHelper.floor_double(mc.thePlayer.posZ);
			Block block = mc.theWorld.getBlock(i, j, k);
			int block2_X = i, block2_Y = j, block2_Z = k;

			if (mc.theWorld.getBlock(i, j, k).isNormalCube()) {
				if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
						.post(new net.minecraftforge.client.event.RenderBlockOverlayEvent(mc.thePlayer, p_78447_1_,
								net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType.BLOCK, block, i, j,
								k))) {
					renderInsideOfBlock(p_78447_1_, block.getBlockTextureFromSide(2));
				}
			} else {
				for (int l = 0; l < 8; ++l) {
					float f1 = ((l >> 0) % 2 - 0.5F) * mc.thePlayer.width * 0.9F;
					float f2 = ((l >> 1) % 2 - 0.5F) * mc.thePlayer.height * 0.2F;
					float f3 = ((l >> 2) % 2 - 0.5F) * mc.thePlayer.width * 0.9F;
					int i1 = MathHelper.floor_float(i + f1);
					int j1 = MathHelper.floor_float(j + f2);
					int k1 = MathHelper.floor_float(k + f3);

					if (mc.theWorld.getBlock(i1, j1, k1).isNormalCube()) {
						block = mc.theWorld.getBlock(i1, j1, k1);
						block2_X = i;
						block2_Y = j;
						block2_Z = k;
					}
				}
			}

			if (block.getMaterial() != Material.air) {
				if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
						.post(new net.minecraftforge.client.event.RenderBlockOverlayEvent(mc.thePlayer, p_78447_1_,
								net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType.BLOCK, block,
								block2_X, block2_Y, block2_Z))) {
					renderInsideOfBlock(p_78447_1_, block.getBlockTextureFromSide(2));
				}
			}
		}

		if (mc.thePlayer.isInsideOfMaterial(Material.water)) {
			if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
					.post(new net.minecraftforge.client.event.RenderBlockOverlayEvent(mc.thePlayer, p_78447_1_,
							net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType.WATER, Blocks.water,
							MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY),
							MathHelper.floor_double(mc.thePlayer.posZ)))) {
				renderWarpedTextureOverlay(p_78447_1_);
			}
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderInsideOfBlock(float p_78446_1_, IIcon p_78446_2_) {
		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tessellator = Tessellator.instance;
		float f1 = 0.1F;
		GL11.glColor4f(f1, f1, f1, 0.5F);
		GL11.glPushMatrix();
		float f2 = -1.0F;
		float f3 = 1.0F;
		float f4 = -1.0F;
		float f5 = 1.0F;
		float f6 = -0.5F;
		float f7 = p_78446_2_.getMinU();
		float f8 = p_78446_2_.getMaxU();
		float f9 = p_78446_2_.getMinV();
		float f10 = p_78446_2_.getMaxV();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(f2, f4, f6, f8, f10);
		tessellator.addVertexWithUV(f3, f4, f6, f7, f10);
		tessellator.addVertexWithUV(f3, f5, f6, f7, f9);
		tessellator.addVertexWithUV(f2, f5, f6, f8, f9);
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float p_78448_1_) {
		mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
		Tessellator tessellator = Tessellator.instance;
		float f1 = mc.thePlayer.getBrightness(p_78448_1_);
		GL11.glColor4f(f1, f1, f1, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glPushMatrix();
		float f2 = 4.0F;
		float f3 = -1.0F;
		float f4 = 1.0F;
		float f5 = -1.0F;
		float f6 = 1.0F;
		float f7 = -0.5F;
		float f8 = -mc.thePlayer.rotationYaw / 64.0F;
		float f9 = mc.thePlayer.rotationPitch / 64.0F;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(f3, f5, f7, f2 + f8, f2 + f9);
		tessellator.addVertexWithUV(f4, f5, f7, 0.0F + f8, f2 + f9);
		tessellator.addVertexWithUV(f4, f6, f7, 0.0F + f8, 0.0F + f9);
		tessellator.addVertexWithUV(f3, f6, f7, f2 + f8, 0.0F + f9);
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFireInFirstPerson(float p_78442_1_) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		float f1 = 1.0F;

		for (int i = 0; i < 2; ++i) {
			GL11.glPushMatrix();
			IIcon iicon = Blocks.fire.getFireIcon(1);
			mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			float f2 = iicon.getMinU();
			float f3 = iicon.getMaxU();
			float f4 = iicon.getMinV();
			float f5 = iicon.getMaxV();
			float f6 = (0.0F - f1) / 2.0F;
			float f7 = f6 + f1;
			float f8 = 0.0F - f1 / 2.0F;
			float f9 = f8 + f1;
			float f10 = -0.5F;
			GL11.glTranslatef(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(f6, f8, f10, f3, f5);
			tessellator.addVertexWithUV(f7, f8, f10, f2, f5);
			tessellator.addVertexWithUV(f7, f9, f10, f2, f4);
			tessellator.addVertexWithUV(f6, f9, f10, f3, f4);
			tessellator.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void updateEquippedItem() {
		prevEquippedProgress = equippedProgress;
		EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
		ItemStack itemstack = entityclientplayermp.inventory.getCurrentItem();
		boolean flag = equippedItemSlot == entityclientplayermp.inventory.currentItem && itemstack == itemToRender;

		if (itemToRender == null && itemstack == null) {
			flag = true;
		}

		if (itemstack != null && itemToRender != null && itemstack != itemToRender
				&& itemstack.getItem() == itemToRender.getItem()
				&& itemstack.getItemDamage() == itemToRender.getItemDamage()) {
			itemToRender = itemstack;
			flag = true;
		}

		float f = 0.4F;
		float f1 = flag ? 1.0F : 0.0F;
		float f2 = f1 - equippedProgress;

		if (f2 < -f) {
			f2 = -f;
		}

		if (f2 > f) {
			f2 = f;
		}

		equippedProgress += f2;

		if (equippedProgress < 0.1F) {
			itemToRender = itemstack;
			equippedItemSlot = entityclientplayermp.inventory.currentItem;
		}
	}

	public void resetEquippedProgress() {
		equippedProgress = 0.0F;
	}

	public void resetEquippedProgress2() {
		equippedProgress = 0.0F;
	}
}