package net.minecraft.client.gui.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class GuiContainer extends GuiScreen {
	protected static final ResourceLocation field_147001_a = new ResourceLocation(
			"textures/gui/container/inventory.png");
	protected int xSize = 176;
	protected int ySize = 166;
	public Container inventorySlots;
	protected int guiLeft;
	protected int guiTop;
	private Slot theSlot;
	private Slot clickedSlot;
	private boolean isRightMouseClick;
	private ItemStack draggedStack;
	private int field_147011_y;
	private int field_147010_z;
	private Slot returningStackDestSlot;
	private long returningStackTime;
	private ItemStack returningStack;
	private Slot field_146985_D;
	private long field_146986_E;
	protected final Set field_147008_s = new HashSet();
	protected boolean field_147007_t;
	private int field_146987_F;
	private int field_146988_G;
	private boolean field_146995_H;
	private int field_146996_I;
	private long field_146997_J;
	private Slot field_146998_K;
	private int field_146992_L;
	private boolean field_146993_M;
	private ItemStack field_146994_N;
	private static final String __OBFID = "CL_00000737";

	public GuiContainer(Container p_i1072_1_) {
		inventorySlots = p_i1072_1_;
		field_146995_H = true;
	}

	@Override
	public void initGui() {
		super.initGui();
		mc.thePlayer.openContainer = inventorySlots;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int k = guiLeft;
		int l = guiTop;
		drawGuiContainerBackgroundLayer(p_73863_3_, p_73863_1_, p_73863_2_);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(k, l, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		theSlot = null;
		short short1 = 240;
		short short2 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k1;

		for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = (Slot) inventorySlots.inventorySlots.get(i1);
			func_146977_a(slot);

			if (isMouseOverSlot(slot, p_73863_1_, p_73863_2_) && slot.func_111238_b()) {
				theSlot = slot;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int j1 = slot.xDisplayPosition;
				k1 = slot.yDisplayPosition;
				GL11.glColorMask(true, true, true, false);
				drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GL11.glColorMask(true, true, true, true);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		// Forge: Force lighting to be disabled as there are some issue where lighting
		// would
		// incorrectly be applied based on items that are in the inventory.
		GL11.glDisable(GL11.GL_LIGHTING);
		drawGuiContainerForegroundLayer(p_73863_1_, p_73863_2_);
		GL11.glEnable(GL11.GL_LIGHTING);
		InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
		ItemStack itemstack = draggedStack == null ? inventoryplayer.getItemStack() : draggedStack;

		if (itemstack != null) {
			byte b0 = 8;
			k1 = draggedStack == null ? 8 : 16;
			String s = null;

			if (draggedStack != null && isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.stackSize = MathHelper.ceiling_float_int(itemstack.stackSize / 2.0F);
			} else if (field_147007_t && field_147008_s.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.stackSize = field_146996_I;

				if (itemstack.stackSize == 0) {
					s = "" + EnumChatFormatting.YELLOW + "0";
				}
			}

			drawItemStack(itemstack, p_73863_1_ - k - b0, p_73863_2_ - l - k1, s);
		}

		if (returningStack != null) {
			float f1 = (Minecraft.getSystemTime() - returningStackTime) / 100.0F;

			if (f1 >= 1.0F) {
				f1 = 1.0F;
				returningStack = null;
			}

			k1 = returningStackDestSlot.xDisplayPosition - field_147011_y;
			int j2 = returningStackDestSlot.yDisplayPosition - field_147010_z;
			int l1 = field_147011_y + (int) (k1 * f1);
			int i2 = field_147010_z + (int) (j2 * f1);
			drawItemStack(returningStack, l1, i2, (String) null);
		}

		GL11.glPopMatrix();

		if (inventoryplayer.getItemStack() == null && theSlot != null && theSlot.getHasStack()) {
			ItemStack itemstack1 = theSlot.getStack();
			renderToolTip(itemstack1, p_73863_1_, p_73863_2_);
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	private void drawItemStack(ItemStack p_146982_1_, int p_146982_2_, int p_146982_3_, String p_146982_4_) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		FontRenderer font = null;
		if (p_146982_1_ != null) {
			font = p_146982_1_.getItem().getFontRenderer(p_146982_1_);
		}
		if (font == null) {
			font = fontRendererObj;
		}
		itemRender.renderItemAndEffectIntoGUI(font, mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_);
		itemRender.renderItemOverlayIntoGUI(font, mc.getTextureManager(), p_146982_1_, p_146982_2_,
				p_146982_3_ - (draggedStack == null ? 0 : 8), p_146982_4_);
		zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}

	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
	}

	protected abstract void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_);

	private void func_146977_a(Slot p_146977_1_) {
		int i = p_146977_1_.xDisplayPosition;
		int j = p_146977_1_.yDisplayPosition;
		ItemStack itemstack = p_146977_1_.getStack();
		boolean flag = false;
		boolean flag1 = p_146977_1_ == clickedSlot && draggedStack != null && !isRightMouseClick;
		ItemStack itemstack1 = mc.thePlayer.inventory.getItemStack();
		String s = null;

		if (p_146977_1_ == clickedSlot && draggedStack != null && isRightMouseClick && itemstack != null) {
			itemstack = itemstack.copy();
			itemstack.stackSize /= 2;
		} else if (field_147007_t && field_147008_s.contains(p_146977_1_) && itemstack1 != null) {
			if (field_147008_s.size() == 1)
				return;

			if (Container.func_94527_a(p_146977_1_, itemstack1, true) && inventorySlots.canDragIntoSlot(p_146977_1_)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.func_94525_a(field_147008_s, field_146987_F, itemstack,
						p_146977_1_.getStack() == null ? 0 : p_146977_1_.getStack().stackSize);

				if (itemstack.stackSize > itemstack.getMaxStackSize()) {
					s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
					itemstack.stackSize = itemstack.getMaxStackSize();
				}

				if (itemstack.stackSize > p_146977_1_.getSlotStackLimit()) {
					s = EnumChatFormatting.YELLOW + "" + p_146977_1_.getSlotStackLimit();
					itemstack.stackSize = p_146977_1_.getSlotStackLimit();
				}
			} else {
				field_147008_s.remove(p_146977_1_);
				func_146980_g();
			}
		}

		zLevel = 100.0F;
		itemRender.zLevel = 100.0F;

		if (itemstack == null) {
			IIcon iicon = p_146977_1_.getBackgroundIconIndex();

			if (iicon != null) {
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND); // Forge: Blending needs to be enabled for this.
				mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
				drawTexturedModelRectFromIcon(i, j, iicon, 16, 16);
				GL11.glDisable(GL11.GL_BLEND); // Forge: And clean that up
				GL11.glEnable(GL11.GL_LIGHTING);
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, i, j);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, i, j, s);
		}

		itemRender.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	private void func_146980_g() {
		ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && field_147007_t) {
			field_146996_I = itemstack.stackSize;
			ItemStack itemstack1;
			int i;

			for (Iterator iterator = field_147008_s.iterator(); iterator
					.hasNext(); field_146996_I -= itemstack1.stackSize - i) {
				Slot slot = (Slot) iterator.next();
				itemstack1 = itemstack.copy();
				i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.func_94525_a(field_147008_s, field_146987_F, itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getSlotStackLimit()) {
					itemstack1.stackSize = slot.getSlotStackLimit();
				}
			}
		}
	}

	private Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_) {
		for (int k = 0; k < inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = (Slot) inventorySlots.inventorySlots.get(k);

			if (isMouseOverSlot(slot, p_146975_1_, p_146975_2_))
				return slot;
		}

		return null;
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		boolean flag = p_73864_3_ == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
		Slot slot = getSlotAtPosition(p_73864_1_, p_73864_2_);
		long l = Minecraft.getSystemTime();
		field_146993_M = field_146998_K == slot && l - field_146997_J < 250L && field_146992_L == p_73864_3_;
		field_146995_H = false;

		if (p_73864_3_ == 0 || p_73864_3_ == 1 || flag) {
			int i1 = guiLeft;
			int j1 = guiTop;
			boolean flag1 = p_73864_1_ < i1 || p_73864_2_ < j1 || p_73864_1_ >= i1 + xSize || p_73864_2_ >= j1 + ySize;
			int k1 = -1;

			if (slot != null) {
				k1 = slot.slotNumber;
			}

			if (flag1) {
				k1 = -999;
			}

			if (mc.gameSettings.touchscreen && flag1 && mc.thePlayer.inventory.getItemStack() == null) {
				mc.displayGuiScreen((GuiScreen) null);
				return;
			}

			if (k1 != -1) {
				if (mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						clickedSlot = slot;
						draggedStack = null;
						isRightMouseClick = p_73864_3_ == 1;
					} else {
						clickedSlot = null;
					}
				} else if (!field_147007_t) {
					if (mc.thePlayer.inventory.getItemStack() == null) {
						if (p_73864_3_ == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
							handleMouseClick(slot, k1, p_73864_3_, 3);
						} else {
							boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
							byte b0 = 0;

							if (flag2) {
								field_146994_N = slot != null && slot.getHasStack() ? slot.getStack() : null;
								b0 = 1;
							} else if (k1 == -999) {
								b0 = 4;
							}

							handleMouseClick(slot, k1, p_73864_3_, b0);
						}

						field_146995_H = true;
					} else {
						field_147007_t = true;
						field_146988_G = p_73864_3_;
						field_147008_s.clear();

						if (p_73864_3_ == 0) {
							field_146987_F = 0;
						} else if (p_73864_3_ == 1) {
							field_146987_F = 1;
						}
					}
				}
			}
		}

		field_146998_K = slot;
		field_146997_J = l;
		field_146992_L = p_73864_3_;
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		Slot slot = getSlotAtPosition(p_146273_1_, p_146273_2_);
		ItemStack itemstack = mc.thePlayer.inventory.getItemStack();

		if (clickedSlot != null && mc.gameSettings.touchscreen) {
			if (p_146273_3_ == 0 || p_146273_3_ == 1) {
				if (draggedStack == null) {
					if (slot != clickedSlot) {
						draggedStack = clickedSlot.getStack().copy();
					}
				} else if (draggedStack.stackSize > 1 && slot != null
						&& Container.func_94527_a(slot, draggedStack, false)) {
					long i1 = Minecraft.getSystemTime();

					if (field_146985_D == slot) {
						if (i1 - field_146986_E > 500L) {
							handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
							handleMouseClick(slot, slot.slotNumber, 1, 0);
							handleMouseClick(clickedSlot, clickedSlot.slotNumber, 0, 0);
							field_146986_E = i1 + 750L;
							--draggedStack.stackSize;
						}
					} else {
						field_146985_D = slot;
						field_146986_E = i1;
					}
				}
			}
		} else if (field_147007_t && slot != null && itemstack != null && itemstack.stackSize > field_147008_s.size()
				&& Container.func_94527_a(slot, itemstack, true) && slot.isItemValid(itemstack)
				&& inventorySlots.canDragIntoSlot(slot)) {
			field_147008_s.add(slot);
			func_146980_g();
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_); // Forge, Call parent to release buttons
		Slot slot = getSlotAtPosition(p_146286_1_, p_146286_2_);
		int l = guiLeft;
		int i1 = guiTop;
		boolean flag = p_146286_1_ < l || p_146286_2_ < i1 || p_146286_1_ >= l + xSize || p_146286_2_ >= i1 + ySize;
		int j1 = -1;

		if (slot != null) {
			j1 = slot.slotNumber;
		}

		if (flag) {
			j1 = -999;
		}

		Slot slot1;
		Iterator iterator;

		if (field_146993_M && slot != null && p_146286_3_ == 0 && inventorySlots.func_94530_a((ItemStack) null, slot)) {
			if (isShiftKeyDown()) {
				if (slot != null && slot.inventory != null && field_146994_N != null) {
					iterator = inventorySlots.inventorySlots.iterator();

					while (iterator.hasNext()) {
						slot1 = (Slot) iterator.next();

						if (slot1 != null && slot1.canTakeStack(mc.thePlayer) && slot1.getHasStack()
								&& slot1.inventory == slot.inventory
								&& Container.func_94527_a(slot1, field_146994_N, true)) {
							handleMouseClick(slot1, slot1.slotNumber, p_146286_3_, 1);
						}
					}
				}
			} else {
				handleMouseClick(slot, j1, p_146286_3_, 6);
			}

			field_146993_M = false;
			field_146997_J = 0L;
		} else {
			if (field_147007_t && field_146988_G != p_146286_3_) {
				field_147007_t = false;
				field_147008_s.clear();
				field_146995_H = true;
				return;
			}

			if (field_146995_H) {
				field_146995_H = false;
				return;
			}

			boolean flag1;

			if (clickedSlot != null && mc.gameSettings.touchscreen) {
				if (p_146286_3_ == 0 || p_146286_3_ == 1) {
					if (draggedStack == null && slot != clickedSlot) {
						draggedStack = clickedSlot.getStack();
					}

					flag1 = Container.func_94527_a(slot, draggedStack, false);

					if (j1 != -1 && draggedStack != null && flag1) {
						handleMouseClick(clickedSlot, clickedSlot.slotNumber, p_146286_3_, 0);
						handleMouseClick(slot, j1, 0, 0);

						if (mc.thePlayer.inventory.getItemStack() != null) {
							handleMouseClick(clickedSlot, clickedSlot.slotNumber, p_146286_3_, 0);
							field_147011_y = p_146286_1_ - l;
							field_147010_z = p_146286_2_ - i1;
							returningStackDestSlot = clickedSlot;
							returningStack = draggedStack;
							returningStackTime = Minecraft.getSystemTime();
						} else {
							returningStack = null;
						}
					} else if (draggedStack != null) {
						field_147011_y = p_146286_1_ - l;
						field_147010_z = p_146286_2_ - i1;
						returningStackDestSlot = clickedSlot;
						returningStack = draggedStack;
						returningStackTime = Minecraft.getSystemTime();
					}

					draggedStack = null;
					clickedSlot = null;
				}
			} else if (field_147007_t && !field_147008_s.isEmpty()) {
				handleMouseClick((Slot) null, -999, Container.func_94534_d(0, field_146987_F), 5);
				iterator = field_147008_s.iterator();

				while (iterator.hasNext()) {
					slot1 = (Slot) iterator.next();
					handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, field_146987_F), 5);
				}

				handleMouseClick((Slot) null, -999, Container.func_94534_d(2, field_146987_F), 5);
			} else if (mc.thePlayer.inventory.getItemStack() != null) {
				if (p_146286_3_ == mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
					handleMouseClick(slot, j1, p_146286_3_, 3);
				} else {
					flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

					if (flag1) {
						field_146994_N = slot != null && slot.getHasStack() ? slot.getStack() : null;
					}

					handleMouseClick(slot, j1, p_146286_3_, flag1 ? 1 : 0);
				}
			}
		}

		if (mc.thePlayer.inventory.getItemStack() == null) {
			field_146997_J = 0L;
		}

		field_147007_t = false;
	}

	private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_) {
		return func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_,
				p_146981_3_);
	}

	protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_,
			int p_146978_6_) {
		int k1 = guiLeft;
		int l1 = guiTop;
		p_146978_5_ -= k1;
		p_146978_6_ -= l1;
		return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1
				&& p_146978_6_ >= p_146978_2_ - 1 && p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
	}

	protected void handleMouseClick(Slot p_146984_1_, int p_146984_2_, int p_146984_3_, int p_146984_4_) {
		if (p_146984_1_ != null) {
			p_146984_2_ = p_146984_1_.slotNumber;
		}

		mc.playerController.windowClick(inventorySlots.windowId, p_146984_2_, p_146984_3_, p_146984_4_, mc.thePlayer);
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1 || p_73869_2_ == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.thePlayer.closeScreen();
		}

		checkHotbarKeys(p_73869_2_);

		if (theSlot != null && theSlot.getHasStack()) {
			if (p_73869_2_ == mc.gameSettings.keyBindPickBlock.getKeyCode()) {
				handleMouseClick(theSlot, theSlot.slotNumber, 0, 3);
			} else if (p_73869_2_ == mc.gameSettings.keyBindDrop.getKeyCode()) {
				handleMouseClick(theSlot, theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
			}
		}
	}

	protected boolean checkHotbarKeys(int p_146983_1_) {
		if (mc.thePlayer.inventory.getItemStack() == null && theSlot != null) {
			for (int j = 0; j < 9; ++j) {
				if (p_146983_1_ == mc.gameSettings.keyBindsHotbar[j].getKeyCode()) {
					handleMouseClick(theSlot, theSlot.slotNumber, j, 2);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onGuiClosed() {
		if (mc.thePlayer != null) {
			inventorySlots.onContainerClosed(mc.thePlayer);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) {
			mc.thePlayer.closeScreen();
		}
	}
}