package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiMerchant extends GuiContainer {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation field_147038_v = new ResourceLocation("textures/gui/container/villager.png");
	private IMerchant field_147037_w;
	private GuiMerchant.MerchantButton field_147043_x;
	private GuiMerchant.MerchantButton field_147042_y;
	private int field_147041_z;
	private String field_147040_A;
	private static final String __OBFID = "CL_00000762";

	public GuiMerchant(InventoryPlayer p_i1096_1_, IMerchant p_i1096_2_, World p_i1096_3_, String p_i1096_4_) {
		super(new ContainerMerchant(p_i1096_1_, p_i1096_2_, p_i1096_3_));
		field_147037_w = p_i1096_2_;
		field_147040_A = p_i1096_4_ != null && p_i1096_4_.length() >= 1 ? p_i1096_4_
				: I18n.format("entity.Villager.name", new Object[0]);
	}

	@Override
	public void initGui() {
		super.initGui();
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		buttonList.add(field_147043_x = new GuiMerchant.MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
		buttonList.add(field_147042_y = new GuiMerchant.MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
		field_147043_x.enabled = false;
		field_147042_y.enabled = false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(field_147040_A, xSize / 2 - fontRendererObj.getStringWidth(field_147040_A) / 2, 6,
				4210752);
		fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		MerchantRecipeList merchantrecipelist = field_147037_w.getRecipes(mc.thePlayer);

		if (merchantrecipelist != null) {
			field_147043_x.enabled = field_147041_z < merchantrecipelist.size() - 1;
			field_147042_y.enabled = field_147041_z > 0;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		boolean flag = false;

		if (p_146284_1_ == field_147043_x) {
			++field_147041_z;
			flag = true;
		} else if (p_146284_1_ == field_147042_y) {
			--field_147041_z;
			flag = true;
		}

		if (flag) {
			((ContainerMerchant) inventorySlots).setCurrentRecipeIndex(field_147041_z);
			ByteBuf bytebuf = Unpooled.buffer();

			try {
				bytebuf.writeInt(field_147041_z);
				mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", bytebuf));
			} catch (Exception exception) {
				logger.error("Couldn\'t send trade info", exception);
			} finally {
				bytebuf.release();
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147038_v);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		MerchantRecipeList merchantrecipelist = field_147037_w.getRecipes(mc.thePlayer);

		if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
			int i1 = field_147041_z;
			MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(i1);

			if (merchantrecipe.isRecipeDisabled()) {
				mc.getTextureManager().bindTexture(field_147038_v);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				drawTexturedModalRect(guiLeft + 83, guiTop + 21, 212, 0, 28, 21);
				drawTexturedModalRect(guiLeft + 83, guiTop + 51, 212, 0, 28, 21);
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		MerchantRecipeList merchantrecipelist = field_147037_w.getRecipes(mc.thePlayer);

		if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
			int k = (width - xSize) / 2;
			int l = (height - ySize) / 2;
			int i1 = field_147041_z;
			MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(i1);
			GL11.glPushMatrix();
			ItemStack itemstack = merchantrecipe.getItemToBuy();
			ItemStack itemstack1 = merchantrecipe.getSecondItemToBuy();
			ItemStack itemstack2 = merchantrecipe.getItemToSell();
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glEnable(GL11.GL_LIGHTING);
			itemRender.zLevel = 100.0F;
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, k + 36, l + 24);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, k + 36, l + 24);

			if (itemstack1 != null) {
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack1, k + 62,
						l + 24);
				itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack1, k + 62,
						l + 24);
			}

			itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack2, k + 120, l + 24);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack2, k + 120, l + 24);
			itemRender.zLevel = 0.0F;
			GL11.glDisable(GL11.GL_LIGHTING);

			if (func_146978_c(36, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				renderToolTip(itemstack, p_73863_1_, p_73863_2_);
			} else if (itemstack1 != null && func_146978_c(62, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				renderToolTip(itemstack1, p_73863_1_, p_73863_2_);
			} else if (func_146978_c(120, 24, 16, 16, p_73863_1_, p_73863_2_)) {
				renderToolTip(itemstack2, p_73863_1_, p_73863_2_);
			}

			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
		}
	}

	public IMerchant func_147035_g() {
		return field_147037_w;
	}

	@SideOnly(Side.CLIENT)
	static class MerchantButton extends GuiButton {
		private final boolean field_146157_o;
		private static final String __OBFID = "CL_00000763";

		public MerchantButton(int p_i1095_1_, int p_i1095_2_, int p_i1095_3_, boolean p_i1095_4_) {
			super(p_i1095_1_, p_i1095_2_, p_i1095_3_, 12, 19, "");
			field_146157_o = p_i1095_4_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (visible) {
				p_146112_1_.getTextureManager().bindTexture(GuiMerchant.field_147038_v);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = p_146112_2_ >= xPosition && p_146112_3_ >= yPosition && p_146112_2_ < xPosition + width
						&& p_146112_3_ < yPosition + height;
				int k = 0;
				int l = 176;

				if (!enabled) {
					l += width * 2;
				} else if (flag) {
					l += width;
				}

				if (!field_146157_o) {
					k += height;
				}

				drawTexturedModalRect(xPosition, yPosition, l, k, width, height);
			}
		}
	}
}