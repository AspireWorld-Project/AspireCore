package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBrewingStand extends GuiContainer {
	private static final ResourceLocation brewingStandGuiTextures = new ResourceLocation(
			"textures/gui/container/brewing_stand.png");
	private final TileEntityBrewingStand tileBrewingStand;
	public GuiBrewingStand(InventoryPlayer p_i1081_1_, TileEntityBrewingStand p_i1081_2_) {
		super(new ContainerBrewingStand(p_i1081_1_, p_i1081_2_));
		tileBrewingStand = p_i1081_2_;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		String s = tileBrewingStand.hasCustomInventoryName() ? tileBrewingStand.getInventoryName()
				: I18n.format(tileBrewingStand.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(brewingStandGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		int i1 = tileBrewingStand.getBrewTime();

		if (i1 > 0) {
			int j1 = (int) (28.0F * (1.0F - i1 / 400.0F));

			if (j1 > 0) {
				drawTexturedModalRect(k + 97, l + 16, 176, 0, 9, j1);
			}

			int k1 = i1 / 2 % 7;

			switch (k1) {
			case 0:
				j1 = 29;
				break;
			case 1:
				j1 = 24;
				break;
			case 2:
				j1 = 20;
				break;
			case 3:
				j1 = 16;
				break;
			case 4:
				j1 = 11;
				break;
			case 5:
				j1 = 6;
				break;
			case 6:
				j1 = 0;
			}

			if (j1 > 0) {
				drawTexturedModalRect(k + 65, l + 14 + 29 - j1, 185, 29 - j1, 12, j1);
			}
		}
	}
}