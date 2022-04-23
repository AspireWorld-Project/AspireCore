package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenHorseInventory extends GuiContainer {
	private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
	private final IInventory field_147030_v;
	private final IInventory field_147029_w;
	private final EntityHorse field_147034_x;
	private float field_147033_y;
	private float field_147032_z;
	public GuiScreenHorseInventory(IInventory p_i1093_1_, IInventory p_i1093_2_, EntityHorse p_i1093_3_) {
		super(new ContainerHorseInventory(p_i1093_1_, p_i1093_2_, p_i1093_3_));
		field_147030_v = p_i1093_1_;
		field_147029_w = p_i1093_2_;
		field_147034_x = p_i1093_3_;
		allowUserInput = false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(field_147029_w.hasCustomInventoryName() ? field_147029_w.getInventoryName()
				: I18n.format(field_147029_w.getInventoryName()), 8, 6, 4210752);
		fontRendererObj.drawString(field_147030_v.hasCustomInventoryName() ? field_147030_v.getInventoryName()
				: I18n.format(field_147030_v.getInventoryName()), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(horseGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (field_147034_x.isChested()) {
			drawTexturedModalRect(k + 79, l + 17, 0, ySize, 90, 54);
		}

		if (field_147034_x.func_110259_cr()) {
			drawTexturedModalRect(k + 7, l + 35, 0, ySize + 54, 18, 18);
		}

		GuiInventory.func_147046_a(k + 51, l + 60, 17, k + 51 - field_147033_y, l + 75 - 50 - field_147032_z,
				field_147034_x);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_147033_y = p_73863_1_;
		field_147032_z = p_73863_2_;
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}