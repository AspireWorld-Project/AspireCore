package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderZombie extends RenderBiped {
	private static final ResourceLocation zombiePigmanTextures = new ResourceLocation(
			"textures/entity/zombie_pigman.png");
	private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation zombieVillagerTextures = new ResourceLocation(
			"textures/entity/zombie/zombie_villager.png");
	private final ModelBiped field_82434_o;
	private ModelZombieVillager zombieVillagerModel;
	protected ModelBiped field_82437_k;
	protected ModelBiped field_82435_l;
	protected ModelBiped field_82436_m;
	protected ModelBiped field_82433_n;
	private int field_82431_q = 1;
	private static final String __OBFID = "CL_00001037";

	public RenderZombie() {
		super(new ModelZombie(), 0.5F, 1.0F);
		field_82434_o = modelBipedMain;
		zombieVillagerModel = new ModelZombieVillager();
	}

	@Override
	protected void func_82421_b() {
		field_82423_g = new ModelZombie(1.0F, true);
		field_82425_h = new ModelZombie(0.5F, true);
		field_82437_k = field_82423_g;
		field_82435_l = field_82425_h;
		field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
		field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
	}

	protected int shouldRenderPass(EntityZombie p_77032_1_, int p_77032_2_, float p_77032_3_) {
		func_82427_a(p_77032_1_);
		return super.shouldRenderPass(p_77032_1_, p_77032_2_, p_77032_3_);
	}

	public void doRender(EntityZombie p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		func_82427_a(p_76986_1_);
		super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	protected ResourceLocation getEntityTexture(EntityZombie p_110775_1_) {
		return p_110775_1_ instanceof EntityPigZombie ? zombiePigmanTextures
				: p_110775_1_.isVillager() ? zombieVillagerTextures : zombieTextures;
	}

	protected void renderEquippedItems(EntityZombie p_77029_1_, float p_77029_2_) {
		func_82427_a(p_77029_1_);
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
	}

	private void func_82427_a(EntityZombie p_82427_1_) {
		if (p_82427_1_.isVillager()) {
			if (field_82431_q != zombieVillagerModel.func_82897_a()) {
				zombieVillagerModel = new ModelZombieVillager();
				field_82431_q = zombieVillagerModel.func_82897_a();
				field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
				field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
			}

			mainModel = zombieVillagerModel;
			field_82423_g = field_82436_m;
			field_82425_h = field_82433_n;
		} else {
			mainModel = field_82434_o;
			field_82423_g = field_82437_k;
			field_82425_h = field_82435_l;
		}

		modelBipedMain = (ModelBiped) mainModel;
	}

	protected void rotateCorpse(EntityZombie p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		if (p_77043_1_.isConverting()) {
			p_77043_3_ += (float) (Math.cos(p_77043_1_.ticksExisted * 3.25D) * Math.PI * 0.25D);
		}

		super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
	}

	@Override
	protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_) {
		this.renderEquippedItems((EntityZombie) p_77029_1_, p_77029_2_);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
		return this.getEntityTexture((EntityZombie) p_110775_1_);
	}

	@Override
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	protected int shouldRenderPass(EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityZombie) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityZombie) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
		this.renderEquippedItems((EntityZombie) p_77029_1_, p_77029_2_);
	}

	@Override
	protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		this.rotateCorpse((EntityZombie) p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
	}

	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityZombie) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityZombie) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}