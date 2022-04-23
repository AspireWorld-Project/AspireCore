package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderManager {
	public Map entityRenderMap = new HashMap();
	public static RenderManager instance = new RenderManager();
	private FontRenderer fontRenderer;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public TextureManager renderEngine;
	public ItemRenderer itemRenderer;
	public World worldObj;
	public EntityLivingBase livingPlayer;
	public Entity field_147941_i;
	public float playerViewY;
	public float playerViewX;
	public GameSettings options;
	public double viewerPosX;
	public double viewerPosY;
	public double viewerPosZ;
	public static boolean debugBoundingBox;
	private static final String __OBFID = "CL_00000991";

	private RenderManager() {
		entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider());
		entityRenderMap.put(EntitySpider.class, new RenderSpider());
		entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
		entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
		entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
		entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		entityRenderMap.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
		entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
		entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
		entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
		entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton());
		entityRenderMap.put(EntityWitch.class, new RenderWitch());
		entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
		entityRenderMap.put(EntityZombie.class, new RenderZombie());
		entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
		entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		entityRenderMap.put(EntityGhast.class, new RenderGhast());
		entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
		entityRenderMap.put(EntityVillager.class, new RenderVillager());
		entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
		entityRenderMap.put(EntityBat.class, new RenderBat());
		entityRenderMap.put(EntityDragon.class, new RenderDragon());
		entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
		entityRenderMap.put(EntityWither.class, new RenderWither());
		entityRenderMap.put(Entity.class, new RenderEntity());
		entityRenderMap.put(EntityPainting.class, new RenderPainting());
		entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame());
		entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot());
		entityRenderMap.put(EntityArrow.class, new RenderArrow());
		entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Items.snowball));
		entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(Items.ender_pearl));
		entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(Items.ender_eye));
		entityRenderMap.put(EntityEgg.class, new RenderSnowball(Items.egg));
		entityRenderMap.put(EntityPotion.class, new RenderSnowball(Items.potionitem, 16384));
		entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(Items.experience_bottle));
		entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(Items.fireworks));
		entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(2.0F));
		entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(0.5F));
		entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull());
		entityRenderMap.put(EntityItem.class, new RenderItem());
		entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
		entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock());
		entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart());
		entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner());
		entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		entityRenderMap.put(EntityBoat.class, new RenderBoat());
		entityRenderMap.put(EntityFishHook.class, new RenderFish());
		entityRenderMap.put(EntityHorse.class, new RenderHorse(new ModelHorse(), 0.75F));
		entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());
		Iterator iterator = entityRenderMap.values().iterator();

		while (iterator.hasNext()) {
			Render render = (Render) iterator.next();
			render.setRenderManager(this);
		}
	}

	public Render getEntityClassRenderObject(Class p_78715_1_) {
		Render render = (Render) entityRenderMap.get(p_78715_1_);

		if (render == null && p_78715_1_ != Entity.class) {
			render = getEntityClassRenderObject(p_78715_1_.getSuperclass());
			entityRenderMap.put(p_78715_1_, render);
		}

		return render;
	}

	public Render getEntityRenderObject(Entity p_78713_1_) {
		return getEntityClassRenderObject(p_78713_1_.getClass());
	}

	public void cacheActiveRenderInfo(World p_147938_1_, TextureManager p_147938_2_, FontRenderer p_147938_3_,
			EntityLivingBase p_147938_4_, Entity p_147938_5_, GameSettings p_147938_6_, float p_147938_7_) {
		worldObj = p_147938_1_;
		renderEngine = p_147938_2_;
		options = p_147938_6_;
		livingPlayer = p_147938_4_;
		field_147941_i = p_147938_5_;
		fontRenderer = p_147938_3_;

		if (p_147938_4_.isPlayerSleeping()) {
			Block block = p_147938_1_.getBlock(MathHelper.floor_double(p_147938_4_.posX),
					MathHelper.floor_double(p_147938_4_.posY), MathHelper.floor_double(p_147938_4_.posZ));
			int x = MathHelper.floor_double(p_147938_4_.posX);
			int y = MathHelper.floor_double(p_147938_4_.posY);
			int z = MathHelper.floor_double(p_147938_4_.posZ);

			if (block.isBed(p_147938_1_, x, y, z, p_147938_4_)) {
				int j = block.getBedDirection(p_147938_1_, x, y, z);
				playerViewY = j * 90 + 180;
				playerViewX = 0.0F;
			}
		} else {
			playerViewY = p_147938_4_.prevRotationYaw
					+ (p_147938_4_.rotationYaw - p_147938_4_.prevRotationYaw) * p_147938_7_;
			playerViewX = p_147938_4_.prevRotationPitch
					+ (p_147938_4_.rotationPitch - p_147938_4_.prevRotationPitch) * p_147938_7_;
		}

		if (p_147938_6_.thirdPersonView == 2) {
			playerViewY += 180.0F;
		}

		viewerPosX = p_147938_4_.lastTickPosX + (p_147938_4_.posX - p_147938_4_.lastTickPosX) * p_147938_7_;
		viewerPosY = p_147938_4_.lastTickPosY + (p_147938_4_.posY - p_147938_4_.lastTickPosY) * p_147938_7_;
		viewerPosZ = p_147938_4_.lastTickPosZ + (p_147938_4_.posZ - p_147938_4_.lastTickPosZ) * p_147938_7_;
	}

	public boolean renderEntitySimple(Entity p_147937_1_, float p_147937_2_) {
		return renderEntityStatic(p_147937_1_, p_147937_2_, false);
	}

	public boolean renderEntityStatic(Entity p_147936_1_, float p_147936_2_, boolean p_147936_3_) {
		if (p_147936_1_.ticksExisted == 0) {
			p_147936_1_.lastTickPosX = p_147936_1_.posX;
			p_147936_1_.lastTickPosY = p_147936_1_.posY;
			p_147936_1_.lastTickPosZ = p_147936_1_.posZ;
		}

		double d0 = p_147936_1_.lastTickPosX + (p_147936_1_.posX - p_147936_1_.lastTickPosX) * p_147936_2_;
		double d1 = p_147936_1_.lastTickPosY + (p_147936_1_.posY - p_147936_1_.lastTickPosY) * p_147936_2_;
		double d2 = p_147936_1_.lastTickPosZ + (p_147936_1_.posZ - p_147936_1_.lastTickPosZ) * p_147936_2_;
		float f1 = p_147936_1_.prevRotationYaw + (p_147936_1_.rotationYaw - p_147936_1_.prevRotationYaw) * p_147936_2_;
		int i = p_147936_1_.getBrightnessForRender(p_147936_2_);

		if (p_147936_1_.isBurning()) {
			i = 15728880;
		}

		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		return func_147939_a(p_147936_1_, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f1, p_147936_2_,
				p_147936_3_);
	}

	public boolean renderEntityWithPosYaw(Entity p_147940_1_, double p_147940_2_, double p_147940_4_,
			double p_147940_6_, float p_147940_8_, float p_147940_9_) {
		return func_147939_a(p_147940_1_, p_147940_2_, p_147940_4_, p_147940_6_, p_147940_8_, p_147940_9_, false);
	}

	public boolean func_147939_a(Entity p_147939_1_, double p_147939_2_, double p_147939_4_, double p_147939_6_,
			float p_147939_8_, float p_147939_9_, boolean p_147939_10_) {
		Render render = null;

		try {
			render = getEntityRenderObject(p_147939_1_);

			if (render != null && renderEngine != null) {
				if (!render.isStaticEntity() || p_147939_10_) {
					try {
						render.doRender(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_, p_147939_9_);
					} catch (Throwable throwable2) {
						throw new ReportedException(
								CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
					}

					try {
						render.doRenderShadowAndFire(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_,
								p_147939_9_);
					} catch (Throwable throwable1) {
						throw new ReportedException(
								CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
					}

					if (debugBoundingBox && !p_147939_1_.isInvisible() && !p_147939_10_) {
						try {
							renderDebugBoundingBox(p_147939_1_, p_147939_2_, p_147939_4_, p_147939_6_, p_147939_8_,
									p_147939_9_);
						} catch (Throwable throwable) {
							throw new ReportedException(
									CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
						}
					}
				}
			} else if (renderEngine != null)
				return false;

			return true;
		} catch (Throwable throwable3) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
			p_147939_1_.addEntityCrashInfo(crashreportcategory);
			CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
			crashreportcategory1.addCrashSection("Assigned renderer", render);
			crashreportcategory1.addCrashSection("Location",
					CrashReportCategory.func_85074_a(p_147939_2_, p_147939_4_, p_147939_6_));
			crashreportcategory1.addCrashSection("Rotation", Float.valueOf(p_147939_8_));
			crashreportcategory1.addCrashSection("Delta", Float.valueOf(p_147939_9_));
			throw new ReportedException(crashreport);
		}
	}

	private void renderDebugBoundingBox(Entity p_85094_1_, double p_85094_2_, double p_85094_4_, double p_85094_6_,
			float p_85094_8_, float p_85094_9_) {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		float f2 = p_85094_1_.width / 2.0F;
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(p_85094_2_ - f2, p_85094_4_, p_85094_6_ - f2,
				p_85094_2_ + f2, p_85094_4_ + p_85094_1_.height, p_85094_6_ + f2);
		RenderGlobal.drawOutlinedBoundingBox(axisalignedbb, 16777215);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	public void set(World p_78717_1_) {
		worldObj = p_78717_1_;
	}

	public double getDistanceToCamera(double p_78714_1_, double p_78714_3_, double p_78714_5_) {
		double d3 = p_78714_1_ - viewerPosX;
		double d4 = p_78714_3_ - viewerPosY;
		double d5 = p_78714_5_ - viewerPosZ;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	public void updateIcons(IIconRegister p_94178_1_) {
		Iterator iterator = entityRenderMap.values().iterator();

		while (iterator.hasNext()) {
			Render render = (Render) iterator.next();
			render.updateIcons(p_94178_1_);
		}
	}
}