package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHopper extends GuiContainer {
	private static final ResourceLocation field_147085_u = new ResourceLocation("textures/gui/container/hopper.png");
	private final IInventory field_147084_v;
	private final IInventory field_147083_w;
	public GuiHopper(InventoryPlayer p_i1092_1_, IInventory p_i1092_2_) {
		super(new ContainerHopper(p_i1092_1_, p_i1092_2_));
		field_147084_v = p_i1092_1_;
		field_147083_w = p_i1092_2_;
		allowUserInput = false;
		ySize = 133;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(field_147083_w.hasCustomInventoryName() ? field_147083_w.getInventoryName()
				: I18n.format(field_147083_w.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(field_147084_v.hasCustomInventoryName() ? field_147084_v.getInventoryName()
				: I18n.format(field_147084_v.getInventoryName()), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147085_u);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}