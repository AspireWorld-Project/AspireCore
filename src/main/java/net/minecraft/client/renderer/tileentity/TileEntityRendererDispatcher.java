package net.minecraft.client.renderer.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnchantmentTable;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.*;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TileEntityRendererDispatcher {
	public Map mapSpecialRenderers = new HashMap();
	public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
	private FontRenderer field_147557_n;
	public static double staticPlayerX;
	public static double staticPlayerY;
	public static double staticPlayerZ;
	public TextureManager field_147553_e;
	public World field_147550_f;
	public EntityLivingBase field_147551_g;
	public float field_147562_h;
	public float field_147563_i;
	public double field_147560_j;
	public double field_147561_k;
	public double field_147558_l;
	private static final String __OBFID = "CL_00000963";

	private TileEntityRendererDispatcher() {
		mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
		mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
		mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityRendererPiston());
		mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
		mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
		mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
		mapSpecialRenderers.put(TileEntityEndPortal.class, new RenderEndPortal());
		mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
		mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
		Iterator iterator = mapSpecialRenderers.values().iterator();

		while (iterator.hasNext()) {
			TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer) iterator.next();
			tileentityspecialrenderer.func_147497_a(this);
		}
	}

	public TileEntitySpecialRenderer getSpecialRendererByClass(Class p_147546_1_) {
		TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer) mapSpecialRenderers
				.get(p_147546_1_);

		if (tileentityspecialrenderer == null && p_147546_1_ != TileEntity.class) {
			tileentityspecialrenderer = getSpecialRendererByClass(p_147546_1_.getSuperclass());
			mapSpecialRenderers.put(p_147546_1_, tileentityspecialrenderer);
		}

		return tileentityspecialrenderer;
	}

	public boolean hasSpecialRenderer(TileEntity p_147545_1_) {
		return getSpecialRenderer(p_147545_1_) != null;
	}

	public TileEntitySpecialRenderer getSpecialRenderer(TileEntity p_147547_1_) {
		return p_147547_1_ == null ? null : getSpecialRendererByClass(p_147547_1_.getClass());
	}

	public void cacheActiveRenderInfo(World p_147542_1_, TextureManager p_147542_2_, FontRenderer p_147542_3_,
			EntityLivingBase p_147542_4_, float p_147542_5_) {
		if (field_147550_f != p_147542_1_) {
			func_147543_a(p_147542_1_);
		}

		field_147553_e = p_147542_2_;
		field_147551_g = p_147542_4_;
		field_147557_n = p_147542_3_;
		field_147562_h = p_147542_4_.prevRotationYaw
				+ (p_147542_4_.rotationYaw - p_147542_4_.prevRotationYaw) * p_147542_5_;
		field_147563_i = p_147542_4_.prevRotationPitch
				+ (p_147542_4_.rotationPitch - p_147542_4_.prevRotationPitch) * p_147542_5_;
		field_147560_j = p_147542_4_.lastTickPosX + (p_147542_4_.posX - p_147542_4_.lastTickPosX) * p_147542_5_;
		field_147561_k = p_147542_4_.lastTickPosY + (p_147542_4_.posY - p_147542_4_.lastTickPosY) * p_147542_5_;
		field_147558_l = p_147542_4_.lastTickPosZ + (p_147542_4_.posZ - p_147542_4_.lastTickPosZ) * p_147542_5_;
	}

	public void renderTileEntity(TileEntity p_147544_1_, float p_147544_2_) {
		if (p_147544_1_.getDistanceFrom(field_147560_j, field_147561_k, field_147558_l) < p_147544_1_
				.getMaxRenderDistanceSquared()) {
			int i = field_147550_f.getLightBrightnessForSkyBlocks(p_147544_1_.xCoord, p_147544_1_.yCoord,
					p_147544_1_.zCoord, 0);
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			renderTileEntityAt(p_147544_1_, p_147544_1_.xCoord - staticPlayerX, p_147544_1_.yCoord - staticPlayerY,
					p_147544_1_.zCoord - staticPlayerZ, p_147544_2_);
		}
	}

	public void renderTileEntityAt(TileEntity p_147549_1_, double p_147549_2_, double p_147549_4_, double p_147549_6_,
			float p_147549_8_) {
		TileEntitySpecialRenderer tileentityspecialrenderer = getSpecialRenderer(p_147549_1_);

		if (tileentityspecialrenderer != null) {
			try {
				tileentityspecialrenderer.renderTileEntityAt(p_147549_1_, p_147549_2_, p_147549_4_, p_147549_6_,
						p_147549_8_);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
				p_147549_1_.func_145828_a(crashreportcategory);
				throw new ReportedException(crashreport);
			}
		}
	}

	public void func_147543_a(World p_147543_1_) {
		field_147550_f = p_147543_1_;
		Iterator iterator = mapSpecialRenderers.values().iterator();

		while (iterator.hasNext()) {
			TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer) iterator.next();

			if (tileentityspecialrenderer != null) {
				tileentityspecialrenderer.func_147496_a(p_147543_1_);
			}
		}
	}

	public FontRenderer getFontRenderer() {
		return field_147557_n;
	}
}