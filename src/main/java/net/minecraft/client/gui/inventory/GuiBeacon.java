package net.minecraft.client.gui.inventory;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiBeacon extends GuiContainer {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
	private TileEntityBeacon tileBeacon;
	private GuiBeacon.ConfirmButton beaconConfirmButton;
	private boolean buttonsNotDrawn;
	private static final String __OBFID = "CL_00000739";

	public GuiBeacon(InventoryPlayer p_i1078_1_, TileEntityBeacon p_i1078_2_) {
		super(new ContainerBeacon(p_i1078_1_, p_i1078_2_));
		tileBeacon = p_i1078_2_;
		xSize = 230;
		ySize = 219;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(beaconConfirmButton = new GuiBeacon.ConfirmButton(-1, guiLeft + 164, guiTop + 107));
		buttonList.add(new GuiBeacon.CancelButton(-2, guiLeft + 190, guiTop + 107));
		buttonsNotDrawn = true;
		beaconConfirmButton.enabled = false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (buttonsNotDrawn && tileBeacon.getLevels() >= 0) {
			buttonsNotDrawn = false;
			int j;
			int k;
			int l;
			int i1;
			GuiBeacon.PowerButton powerbutton;

			for (int i = 0; i <= 2; ++i) {
				j = TileEntityBeacon.effectsList[i].length;
				k = j * 22 + (j - 1) * 2;

				for (l = 0; l < j; ++l) {
					i1 = TileEntityBeacon.effectsList[i][l].id;
					powerbutton = new GuiBeacon.PowerButton(i << 8 | i1, guiLeft + 76 + l * 24 - k / 2,
							guiTop + 22 + i * 25, i1, i);
					buttonList.add(powerbutton);

					if (i >= tileBeacon.getLevels()) {
						powerbutton.enabled = false;
					} else if (i1 == tileBeacon.getPrimaryEffect()) {
						powerbutton.func_146140_b(true);
					}
				}
			}

			byte b0 = 3;
			j = TileEntityBeacon.effectsList[b0].length + 1;
			k = j * 22 + (j - 1) * 2;

			for (l = 0; l < j - 1; ++l) {
				i1 = TileEntityBeacon.effectsList[b0][l].id;
				powerbutton = new GuiBeacon.PowerButton(b0 << 8 | i1, guiLeft + 167 + l * 24 - k / 2, guiTop + 47, i1,
						b0);
				buttonList.add(powerbutton);

				if (b0 >= tileBeacon.getLevels()) {
					powerbutton.enabled = false;
				} else if (i1 == tileBeacon.getSecondaryEffect()) {
					powerbutton.func_146140_b(true);
				}
			}

			if (tileBeacon.getPrimaryEffect() > 0) {
				GuiBeacon.PowerButton powerbutton1 = new GuiBeacon.PowerButton(b0 << 8 | tileBeacon.getPrimaryEffect(),
						guiLeft + 167 + (j - 1) * 24 - k / 2, guiTop + 47, tileBeacon.getPrimaryEffect(), b0);
				buttonList.add(powerbutton1);

				if (b0 >= tileBeacon.getLevels()) {
					powerbutton1.enabled = false;
				} else if (tileBeacon.getPrimaryEffect() == tileBeacon.getSecondaryEffect()) {
					powerbutton1.func_146140_b(true);
				}
			}
		}

		beaconConfirmButton.enabled = tileBeacon.getStackInSlot(0) != null && tileBeacon.getPrimaryEffect() > 0;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == -2) {
			mc.displayGuiScreen((GuiScreen) null);
		} else if (p_146284_1_.id == -1) {
			String s = "MC|Beacon";
			ByteBuf bytebuf = Unpooled.buffer();

			try {
				bytebuf.writeInt(tileBeacon.getPrimaryEffect());
				bytebuf.writeInt(tileBeacon.getSecondaryEffect());
				mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s, bytebuf));
			} catch (Exception exception) {
				logger.error("Couldn\'t send beacon info", exception);
			} finally {
				bytebuf.release();
			}

			mc.displayGuiScreen((GuiScreen) null);
		} else if (p_146284_1_ instanceof GuiBeacon.PowerButton) {
			if (((GuiBeacon.PowerButton) p_146284_1_).func_146141_c())
				return;

			int j = p_146284_1_.id;
			int k = j & 255;
			int i = j >> 8;

			if (i < 3) {
				tileBeacon.setPrimaryEffect(k);
			} else {
				tileBeacon.setSecondaryEffect(k);
			}

			buttonList.clear();
			initGui();
			updateScreen();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		RenderHelper.disableStandardItemLighting();
		drawCenteredString(fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
		drawCenteredString(fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
		Iterator iterator = buttonList.iterator();

		while (iterator.hasNext()) {
			GuiButton guibutton = (GuiButton) iterator.next();

			if (guibutton.func_146115_a()) {
				guibutton.func_146111_b(p_146979_1_ - guiLeft, p_146979_2_ - guiTop);
				break;
			}
		}

		RenderHelper.enableGUIStandardItemLighting();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(beaconGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		itemRender.zLevel = 100.0F;
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(Items.emerald),
				k + 42, l + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(Items.diamond),
				k + 42 + 22, l + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(Items.gold_ingot),
				k + 42 + 44, l + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), new ItemStack(Items.iron_ingot),
				k + 42 + 66, l + 109);
		itemRender.zLevel = 0.0F;
	}

	@SideOnly(Side.CLIENT)
	static class Button extends GuiButton {
		private final ResourceLocation field_146145_o;
		private final int field_146144_p;
		private final int field_146143_q;
		private boolean field_146142_r;
		private static final String __OBFID = "CL_00000743";

		protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_,
				int p_i1077_6_) {
			super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
			field_146145_o = p_i1077_4_;
			field_146144_p = p_i1077_5_;
			field_146143_q = p_i1077_6_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (visible) {
				p_146112_1_.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				field_146123_n = p_146112_2_ >= xPosition && p_146112_3_ >= yPosition && p_146112_2_ < xPosition + width
						&& p_146112_3_ < yPosition + height;
				short short1 = 219;
				int k = 0;

				if (!enabled) {
					k += width * 2;
				} else if (field_146142_r) {
					k += width * 1;
				} else if (field_146123_n) {
					k += width * 3;
				}

				drawTexturedModalRect(xPosition, yPosition, k, short1, width, height);

				if (!GuiBeacon.beaconGuiTextures.equals(field_146145_o)) {
					p_146112_1_.getTextureManager().bindTexture(field_146145_o);
				}

				drawTexturedModalRect(xPosition + 2, yPosition + 2, field_146144_p, field_146143_q, 18, 18);
			}
		}

		public boolean func_146141_c() {
			return field_146142_r;
		}

		public void func_146140_b(boolean p_146140_1_) {
			field_146142_r = p_146140_1_;
		}
	}

	@SideOnly(Side.CLIENT)
	class CancelButton extends GuiBeacon.Button {
		private static final String __OBFID = "CL_00000740";

		public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_) {
			super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), p_146111_1_, p_146111_2_);
		}
	}

	@SideOnly(Side.CLIENT)
	class ConfirmButton extends GuiBeacon.Button {
		private static final String __OBFID = "CL_00000741";

		public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_) {
			super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), p_146111_1_, p_146111_2_);
		}
	}

	@SideOnly(Side.CLIENT)
	class PowerButton extends GuiBeacon.Button {
		private final int field_146149_p;
		private final int field_146148_q;
		private static final String __OBFID = "CL_00000742";

		public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_) {
			super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.field_147001_a,
					0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18,
					198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
			field_146149_p = p_i1076_5_;
			field_146148_q = p_i1076_6_;
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			String s = I18n.format(Potion.potionTypes[field_146149_p].getName(), new Object[0]);

			if (field_146148_q >= 3 && field_146149_p != Potion.regeneration.id) {
				s = s + " II";
			}

			drawCreativeTabHoveringText(s, p_146111_1_, p_146111_2_);
		}
	}
}