package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GuiInventory extends InventoryEffectRenderer {
	private float xSizeFloat;
	private float ySizeFloat;
	public GuiInventory(EntityPlayer p_i1094_1_) {
		super(p_i1094_1_.inventoryContainer);
		allowUserInput = true;
	}

	@Override
	public void updateScreen() {
		if (mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
		}
	}

	@Override
	public void initGui() {
		buttonList.clear();

		if (mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
		} else {
			super.initGui();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(I18n.format("container.crafting"), 86, 16, 4210752);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		xSizeFloat = p_73863_1_;
		ySizeFloat = p_73863_2_;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147001_a);
		int k = guiLeft;
		int l = guiTop;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		func_147046_a(k + 51, l + 75, 30, k + 51 - xSizeFloat, l + 75 - 50 - ySizeFloat, mc.thePlayer);
	}

	public static void func_147046_a(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_,
			float p_147046_4_, EntityLivingBase p_147046_5_) {
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(p_147046_0_, p_147046_1_, 50.0F);
		GL11.glScalef(-p_147046_2_, p_147046_2_, p_147046_2_);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = p_147046_5_.renderYawOffset;
		float f3 = p_147046_5_.rotationYaw;
		float f4 = p_147046_5_.rotationPitch;
		float f5 = p_147046_5_.prevRotationYawHead;
		float f6 = p_147046_5_.rotationYawHead;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		p_147046_5_.renderYawOffset = (float) Math.atan(p_147046_3_ / 40.0F) * 20.0F;
		p_147046_5_.rotationYaw = (float) Math.atan(p_147046_3_ / 40.0F) * 40.0F;
		p_147046_5_.rotationPitch = -((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F;
		p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
		p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
		GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		p_147046_5_.renderYawOffset = f2;
		p_147046_5_.rotationYaw = f3;
		p_147046_5_.rotationPitch = f4;
		p_147046_5_.prevRotationYawHead = f5;
		p_147046_5_.rotationYawHead = f6;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
		}
	}
}