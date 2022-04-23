package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.commons.io.Charsets;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRepair extends GuiContainer implements ICrafting {
	private static final ResourceLocation field_147093_u = new ResourceLocation("textures/gui/container/anvil.png");
	private final ContainerRepair field_147092_v;
	private GuiTextField field_147091_w;
	private final InventoryPlayer field_147094_x;
	private static final String __OBFID = "CL_00000738";

	public GuiRepair(InventoryPlayer p_i1073_1_, World p_i1073_2_, int p_i1073_3_, int p_i1073_4_, int p_i1073_5_) {
		super(new ContainerRepair(p_i1073_1_, p_i1073_2_, p_i1073_3_, p_i1073_4_, p_i1073_5_,
				Minecraft.getMinecraft().thePlayer));
		field_147094_x = p_i1073_1_;
		field_147092_v = (ContainerRepair) inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		field_147091_w = new GuiTextField(fontRendererObj, i + 62, j + 24, 103, 12);
		field_147091_w.setTextColor(-1);
		field_147091_w.setDisabledTextColour(-1);
		field_147091_w.setEnableBackgroundDrawing(false);
		field_147091_w.setMaxStringLength(40);
		inventorySlots.removeCraftingFromCrafters(this);
		inventorySlots.addCraftingToCrafters(this);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		inventorySlots.removeCraftingFromCrafters(this);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		fontRendererObj.drawString(I18n.format("container.repair"), 60, 6, 4210752);

		if (field_147092_v.maximumCost > 0) {
			int k = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost",
					Integer.valueOf(field_147092_v.maximumCost));

			if (field_147092_v.maximumCost >= 40 && !mc.thePlayer.capabilities.isCreativeMode) {
				s = I18n.format("container.repair.expensive");
				k = 16736352;
			} else if (!field_147092_v.getSlot(2).getHasStack()) {
				flag = false;
			} else if (!field_147092_v.getSlot(2).canTakeStack(field_147094_x.player)) {
				k = 16736352;
			}

			if (flag) {
				int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
				int i1 = xSize - 8 - fontRendererObj.getStringWidth(s);
				byte b0 = 67;

				if (fontRendererObj.getUnicodeFlag()) {
					drawRect(i1 - 3, b0 - 2, xSize - 7, b0 + 10, -16777216);
					drawRect(i1 - 2, b0 - 1, xSize - 8, b0 + 9, -12895429);
				} else {
					fontRendererObj.drawString(s, i1, b0 + 1, l);
					fontRendererObj.drawString(s, i1 + 1, b0, l);
					fontRendererObj.drawString(s, i1 + 1, b0 + 1, l);
				}

				fontRendererObj.drawString(s, i1, b0, k);
			}
		}

		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (field_147091_w.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
			func_147090_g();
		} else {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	private void func_147090_g() {
		String s = field_147091_w.getText();
		Slot slot = field_147092_v.getSlot(0);

		if (slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName()
				&& s.equals(slot.getStack().getDisplayName())) {
			s = "";
		}

		field_147092_v.updateItemName(s);
		mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("MC|ItemName", s.getBytes(Charsets.UTF_8)));
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_147091_w.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		field_147091_w.drawTextBox();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147093_u);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		drawTexturedModalRect(k + 59, l + 20, 0, ySize + (field_147092_v.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

		if ((field_147092_v.getSlot(0).getHasStack() || field_147092_v.getSlot(1).getHasStack())
				&& !field_147092_v.getSlot(2).getHasStack()) {
			drawTexturedModalRect(k + 99, l + 45, xSize, 0, 28, 21);
		}
	}

	@Override
	public void sendContainerAndContentsToPlayer(Container p_71110_1_, List p_71110_2_) {
		sendSlotContents(p_71110_1_, 0, p_71110_1_.getSlot(0).getStack());
	}

	@Override
	public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
		if (p_71111_2_ == 0) {
			field_147091_w.setText(p_71111_3_ == null ? "" : p_71111_3_.getDisplayName());
			field_147091_w.setEnabled(p_71111_3_ != null);

			if (p_71111_3_ != null) {
				func_147090_g();
			}
		}
	}

	@Override
	public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}