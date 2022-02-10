package net.minecraft.client.gui.inventory;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiChest extends GuiContainer {
	private static final ResourceLocation field_147017_u = new ResourceLocation(
			"textures/gui/container/generic_54.png");
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private int inventoryRows;
	private static final String __OBFID = "CL_00000749";

	public GuiChest(IInventory p_i1083_1_, IInventory p_i1083_2_) {
		super(new ContainerChest(p_i1083_1_, p_i1083_2_));
		upperChestInventory = p_i1083_1_;
		lowerChestInventory = p_i1083_2_;
		allowUserInput = false;
		short short1 = 222;
		int i = short1 - 108;
		inventoryRows = p_i1083_2_.getSizeInventory() / 9;
		ySize = i + inventoryRows * 18;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(lowerChestInventory.hasCustomInventoryName() ? lowerChestInventory.getInventoryName()
				: I18n.format(lowerChestInventory.getInventoryName(), new Object[0]), 8, 6, 4210752);
		fontRendererObj.drawString(
				upperChestInventory.hasCustomInventoryName() ? upperChestInventory.getInventoryName()
						: I18n.format(upperChestInventory.getInventoryName(), new Object[0]),
				8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147017_u);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, inventoryRows * 18 + 17);
		drawTexturedModalRect(k, l + inventoryRows * 18 + 17, 0, 126, xSize, 96);
	}
}