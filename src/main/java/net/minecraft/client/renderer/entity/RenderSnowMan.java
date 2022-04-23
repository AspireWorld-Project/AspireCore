package net.minecraft.client.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

@SideOnly(Side.CLIENT)
public class RenderSnowMan extends RenderLiving {
	private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
	private ModelSnowMan snowmanModel;
	private static final String __OBFID = "CL_00001025";

	public RenderSnowMan() {
		super(new ModelSnowMan(), 0.5F);
		snowmanModel = (ModelSnowMan) super.mainModel;
		setRenderPassModel(snowmanModel);
	}

	protected void renderEquippedItems(EntitySnowman p_77029_1_, float p_77029_2_) {
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		ItemStack itemstack = new ItemStack(Blocks.pumpkin, 1);

		if (itemstack.getItem() instanceof ItemBlock) {
			GL11.glPushMatrix();
			snowmanModel.head.postRender(0.0625F);

			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
			boolean is3D = customRenderer != null
					&& customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D);

			if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
				float f1 = 0.625F;
				GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
			}

			renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
			GL11.glPopMatrix();
		}
	}

	protected ResourceLocation getEntityTexture(EntitySnowman p_110775_1_) {
		return snowManTextures;
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
		this.renderEquippedItems((EntitySnowman) p_77029_1_, p_77029_2_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntitySnowman) p_110775_1_);
	}
}