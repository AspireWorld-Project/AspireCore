package net.minecraft.client.gui.inventory;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiDispenser extends GuiContainer {
	private static final ResourceLocation dispenserGuiTextures = new ResourceLocation(
			"textures/gui/container/dispenser.png");
	public TileEntityDispenser tileDispenser;
	private static final String __OBFID = "CL_00000765";

	public GuiDispenser(InventoryPlayer p_i1098_1_, TileEntityDispenser p_i1098_2_) {
		super(new ContainerDispenser(p_i1098_1_, p_i1098_2_));
		tileDispenser = p_i1098_2_;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = tileDispenser.hasCustomInventoryName() ? tileDispenser.getInventoryName()
				: I18n.format(tileDispenser.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(dispenserGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}