package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderBiped extends RenderLiving {
	public ModelBiped modelBipedMain;
	protected float field_77070_b;
	protected ModelBiped field_82423_g;
	protected ModelBiped field_82425_h;
	private static final Map field_110859_k = Maps.newHashMap();
	public static String[] bipedArmorFilenamePrefix = new String[] { "leather", "chainmail", "iron", "diamond",
			"gold" };
	private static final String __OBFID = "CL_00001001";

	public RenderBiped(ModelBiped p_i1257_1_, float p_i1257_2_) {
		this(p_i1257_1_, p_i1257_2_, 1.0F);
	}

	public RenderBiped(ModelBiped p_i1258_1_, float p_i1258_2_, float p_i1258_3_) {
		super(p_i1258_1_, p_i1258_2_);
		modelBipedMain = p_i1258_1_;
		field_77070_b = p_i1258_3_;
		func_82421_b();
	}

	protected void func_82421_b() {
		field_82423_g = new ModelBiped(1.0F);
		field_82425_h = new ModelBiped(0.5F);
	}

	@Deprecated // Use the more sensitive version getArmorResource below
	public static ResourceLocation func_110857_a(ItemArmor p_110857_0_, int p_110857_1_) {
		return func_110858_a(p_110857_0_, p_110857_1_, null);
	}

	@Deprecated // Use the more sensitive version getArmorResource below
	public static ResourceLocation func_110858_a(ItemArmor p_110858_0_, int p_110858_1_, String p_110858_2_) {
		String s1 = String.format("textures/models/armor/%s_layer_%d%s.png",
				bipedArmorFilenamePrefix[p_110858_0_.renderIndex],
				Integer.valueOf(p_110858_1_ == 2 ? 2 : 1),
				p_110858_2_ == null ? "" : String.format("_%s", p_110858_2_));
		ResourceLocation resourcelocation = (ResourceLocation) field_110859_k.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			field_110859_k.put(s1, resourcelocation);
		}

		return resourcelocation;
	}

	protected int shouldRenderPass(EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_) {
		ItemStack itemstack = p_77032_1_.func_130225_q(3 - p_77032_2_);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor) item;
				bindTexture(getArmorResource(p_77032_1_, itemstack, p_77032_2_, null));
				ModelBiped modelbiped = p_77032_2_ == 2 ? field_82425_h : field_82423_g;
				modelbiped.bipedHead.showModel = p_77032_2_ == 0;
				modelbiped.bipedHeadwear.showModel = p_77032_2_ == 0;
				modelbiped.bipedBody.showModel = p_77032_2_ == 1 || p_77032_2_ == 2;
				modelbiped.bipedRightArm.showModel = p_77032_2_ == 1;
				modelbiped.bipedLeftArm.showModel = p_77032_2_ == 1;
				modelbiped.bipedRightLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
				modelbiped.bipedLeftLeg.showModel = p_77032_2_ == 2 || p_77032_2_ == 3;
				modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_77032_1_, itemstack, p_77032_2_,
						modelbiped);
				setRenderPassModel(modelbiped);
				modelbiped.onGround = mainModel.onGround;
				modelbiped.isRiding = mainModel.isRiding;
				modelbiped.isChild = mainModel.isChild;

				// Move out of if to allow for more then just CLOTH to have color
				int j = itemarmor.getColor(itemstack);
				if (j != -1) {
					float f1 = (j >> 16 & 255) / 255.0F;
					float f2 = (j >> 8 & 255) / 255.0F;
					float f3 = (j & 255) / 255.0F;
					GL11.glColor3f(f1, f2, f3);

					if (itemstack.isItemEnchanted())
						return 31;

					return 16;
				}

				GL11.glColor3f(1.0F, 1.0F, 1.0F);

				if (itemstack.isItemEnchanted())
					return 15;

				return 1;
			}
		}

		return -1;
	}

	protected void func_82408_c(EntityLiving p_82408_1_, int p_82408_2_, float p_82408_3_) {
		ItemStack itemstack = p_82408_1_.func_130225_q(3 - p_82408_2_);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ItemArmor) {
				bindTexture(getArmorResource(p_82408_1_, itemstack, p_82408_2_, "overlay"));
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		ItemStack itemstack = p_76986_1_.getHeldItem();
		func_82420_a(p_76986_1_, itemstack);
		double d3 = p_76986_4_ - p_76986_1_.yOffset;

		if (p_76986_1_.isSneaking()) {
			d3 -= 0.125D;
		}

		super.doRender(p_76986_1_, p_76986_2_, d3, p_76986_6_, p_76986_8_, p_76986_9_);
		field_82423_g.aimedBow = field_82425_h.aimedBow = modelBipedMain.aimedBow = false;
		field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = false;
		field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = 0;
	}

	protected ResourceLocation getEntityTexture(EntityLiving p_110775_1_) {
		return null;
	}

	protected void func_82420_a(EntityLiving p_82420_1_, ItemStack p_82420_2_) {
		field_82423_g.heldItemRight = field_82425_h.heldItemRight = modelBipedMain.heldItemRight = p_82420_2_ != null
				? 1
				: 0;
		field_82423_g.isSneak = field_82425_h.isSneak = modelBipedMain.isSneak = p_82420_1_.isSneaking();
	}

	protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		ItemStack itemstack = p_77029_1_.getHeldItem();
		ItemStack itemstack1 = p_77029_1_.func_130225_q(3);
		Item item;
		float f1;

		if (itemstack1 != null) {
			GL11.glPushMatrix();
			modelBipedMain.bipedHead.postRender(0.0625F);
			item = itemstack1.getItem();

			net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient
					.getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
			boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
					net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack1,
					net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D);

			if (item instanceof ItemBlock) {
				if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())) {
					f1 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(f1, -f1, -f1);
				}

				renderManager.itemRenderer.renderItem(p_77029_1_, itemstack1, 0);
			} else if (item == Items.skull) {
				f1 = 1.0625F;
				GL11.glScalef(f1, -f1, -f1);
				GameProfile gameprofile = null;

				if (itemstack1.hasTagCompound()) {
					NBTTagCompound nbttagcompound = itemstack1.getTagCompound();

					if (nbttagcompound.hasKey("SkullOwner", 10)) {
						gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
					} else if (nbttagcompound.hasKey("SkullOwner", 8)
							&& !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
						gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
					}
				}

				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F,
						itemstack1.getItemDamage(), gameprofile);
			}

			GL11.glPopMatrix();
		}

		if (itemstack != null && itemstack.getItem() != null) {
			item = itemstack.getItem();
			GL11.glPushMatrix();

			if (mainModel.isChild) {
				f1 = 0.5F;
				GL11.glTranslatef(0.0F, 0.625F, 0.0F);
				GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
				GL11.glScalef(f1, f1, f1);
			}

			modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient
					.getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
			boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(
					net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, itemstack,
					net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D);

			if (item instanceof ItemBlock
					&& (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))) {
				f1 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f1 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-f1, -f1, f1);
			} else if (item == Items.bow) {
				f1 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (item.isFull3D()) {
				f1 = 0.625F;

				if (item.shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				func_82422_c();
				GL11.glScalef(f1, -f1, f1);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f1 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f1, f1, f1);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float f2;
			int i;
			float f5;

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				for (i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i) {
					int j = itemstack.getItem().getColorFromItemStack(itemstack, i);
					f5 = (j >> 16 & 255) / 255.0F;
					f2 = (j >> 8 & 255) / 255.0F;
					float f3 = (j & 255) / 255.0F;
					GL11.glColor4f(f5, f2, f3, 1.0F);
					renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, i);
				}
			} else {
				i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
				float f4 = (i >> 16 & 255) / 255.0F;
				f5 = (i >> 8 & 255) / 255.0F;
				f2 = (i & 255) / 255.0F;
				GL11.glColor4f(f4, f5, f2, 1.0F);
				renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
			}

			GL11.glPopMatrix();
		}
	}

	protected void func_82422_c() {
		GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
	}

	@Override
	protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_, float p_82408_3_) {
		this.func_82408_c((EntityLiving) p_82408_1_, p_82408_2_, p_82408_3_);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_) {
		return this.shouldRenderPass((EntityLiving) p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
		this.renderEquippedItems((EntityLiving) p_77029_1_, p_77029_2_);
	}

	@Override
	public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_,
			float p_76986_8_, float p_76986_9_) {
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityLiving) p_110775_1_);
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		this.doRender((EntityLiving) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/*
	 * =================================== FORGE START
	 * =========================================
	 */
	/**
	 * More generic ForgeHook version of the above function, it allows for Items to
	 * have more control over what texture they provide.
	 *
	 * @param entity
	 *            Entity wearing the armor
	 * @param stack
	 *            ItemStack for the armor
	 * @param slot
	 *            Slot ID that the item is in
	 * @param type
	 *            Subtype, can be null or "overlay"
	 * @return ResourceLocation pointing at the armor's texture
	 */
	public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, int slot, String type) {
		ItemArmor item = (ItemArmor) stack.getItem();
		String s1 = String.format("textures/models/armor/%s_layer_%d%s.png", bipedArmorFilenamePrefix[item.renderIndex],
				slot == 2 ? 2 : 1, type == null ? "" : String.format("_%s", type));

		s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = (ResourceLocation) field_110859_k.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			field_110859_k.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
	/*
	 * =================================== FORGE END
	 * ===========================================
	 */
}