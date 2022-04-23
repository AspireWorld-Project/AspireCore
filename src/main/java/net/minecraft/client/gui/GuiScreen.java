package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiScreen extends Gui {
	protected static RenderItem itemRender = new RenderItem();
	public Minecraft mc;
	public int width;
	public int height;
	protected List buttonList = new ArrayList();
	protected List labelList = new ArrayList();
	public boolean allowUserInput;
	protected FontRenderer fontRendererObj;
	private GuiButton selectedButton;
	private int eventButton;
	private long lastMouseEvent;
	private int field_146298_h;
	private static final String __OBFID = "CL_00000710";

	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		int k;

		for (k = 0; k < buttonList.size(); ++k) {
			((GuiButton) buttonList.get(k)).drawButton(mc, p_73863_1_, p_73863_2_);
		}

		for (k = 0; k < labelList.size(); ++k) {
			((GuiLabel) labelList.get(k)).func_146159_a(mc, p_73863_1_, p_73863_2_);
		}
	}

	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		}
	}

	public static String getClipboardString() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null);

			if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String) transferable.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception exception) {
			;
		}

		return "";
	}

	public static void setClipboardString(String p_146275_0_) {
		try {
			StringSelection stringselection = new StringSelection(p_146275_0_);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, (ClipboardOwner) null);
		} catch (Exception exception) {
			;
		}
	}

	protected void renderToolTip(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_) {
		List list = p_146285_1_.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

		for (int k = 0; k < list.size(); ++k) {
			if (k == 0) {
				list.set(k, p_146285_1_.getRarity().rarityColor + (String) list.get(k));
			} else {
				list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
			}
		}

		FontRenderer font = p_146285_1_.getItem().getFontRenderer(p_146285_1_);
		drawHoveringText(list, p_146285_2_, p_146285_3_, font == null ? fontRendererObj : font);
	}

	protected void drawCreativeTabHoveringText(String p_146279_1_, int p_146279_2_, int p_146279_3_) {
		func_146283_a(Arrays.asList(new String[] { p_146279_1_ }), p_146279_2_, p_146279_3_);
	}

	protected void func_146283_a(List p_146283_1_, int p_146283_2_, int p_146283_3_) {
		drawHoveringText(p_146283_1_, p_146283_2_, p_146283_3_, fontRendererObj);
	}

	protected void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
		if (!p_146283_1_.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = p_146283_1_.iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);

				if (l > k) {
					k = l;
				}
			}

			int j2 = p_146283_2_ + 12;
			int k2 = p_146283_3_ - 12;
			int i1 = 8;

			if (p_146283_1_.size() > 1) {
				i1 += 2 + (p_146283_1_.size() - 1) * 10;
			}

			if (j2 + k > width) {
				j2 -= 28 + k;
			}

			if (k2 + i1 + 6 > height) {
				k2 = height - i1 - 6;
			}

			zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			int j1 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
				String s1 = (String) p_146283_1_.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0) {
			for (int l = 0; l < buttonList.size(); ++l) {
				GuiButton guibutton = (GuiButton) buttonList.get(l);

				if (guibutton.mousePressed(mc, p_73864_1_, p_73864_2_)) {
					ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this, guibutton, buttonList);
					if (MinecraftForge.EVENT_BUS.post(event)) {
						break;
					}
					selectedButton = event.button;
					event.button.func_146113_a(mc.getSoundHandler());
					actionPerformed(event.button);
					if (equals(mc.currentScreen)) {
						MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this, event.button, buttonList));
					}
				}
			}
		}
	}

	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		if (selectedButton != null && p_146286_3_ == 0) {
			selectedButton.mouseReleased(p_146286_1_, p_146286_2_);
			selectedButton = null;
		}
	}

	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
	}

	protected void actionPerformed(GuiButton p_146284_1_) {
	}

	public void setWorldAndResolution(Minecraft p_146280_1_, int p_146280_2_, int p_146280_3_) {
		mc = p_146280_1_;
		fontRendererObj = p_146280_1_.fontRenderer;
		width = p_146280_2_;
		height = p_146280_3_;
		if (!MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Pre(this, buttonList))) {
			buttonList.clear();
			initGui();
		}
		MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Post(this, buttonList));
	}

	public void initGui() {
	}

	public void handleInput() {
		if (Mouse.isCreated()) {
			while (Mouse.next()) {
				handleMouseInput();
			}
		}

		if (Keyboard.isCreated()) {
			while (Keyboard.next()) {
				handleKeyboardInput();
			}
		}
	}

	public void handleMouseInput() {
		int i = Mouse.getEventX() * width / mc.displayWidth;
		int j = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		int k = Mouse.getEventButton();

		if (Mouse.getEventButtonState()) {
			if (mc.gameSettings.touchscreen && field_146298_h++ > 0)
				return;

			eventButton = k;
			lastMouseEvent = Minecraft.getSystemTime();
			mouseClicked(i, j, eventButton);
		} else if (k != -1) {
			if (mc.gameSettings.touchscreen && --field_146298_h > 0)
				return;

			eventButton = -1;
			mouseMovedOrUp(i, j, k);
		} else if (eventButton != -1 && lastMouseEvent > 0L) {
			long l = Minecraft.getSystemTime() - lastMouseEvent;
			mouseClickMove(i, j, eventButton, l);
		}
	}

	public void handleKeyboardInput() {
		if (Keyboard.getEventKeyState()) {
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

		mc.func_152348_aa();
	}

	public void updateScreen() {
	}

	public void onGuiClosed() {
	}

	public void drawDefaultBackground() {
		drawWorldBackground(0);
	}

	public void drawWorldBackground(int p_146270_1_) {
		if (mc.theWorld != null) {
			drawGradientRect(0, 0, width, height, -1072689136, -804253680);
		} else {
			drawBackground(p_146270_1_);
		}
	}

	public void drawBackground(int p_146278_1_) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator = Tessellator.instance;
		mc.getTextureManager().bindTexture(optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(4210752);
		tessellator.addVertexWithUV(0.0D, height, 0.0D, 0.0D, height / f + p_146278_1_);
		tessellator.addVertexWithUV(width, height, 0.0D, width / f, height / f + p_146278_1_);
		tessellator.addVertexWithUV(width, 0.0D, 0.0D, width / f, p_146278_1_);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, p_146278_1_);
		tessellator.draw();
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
	}

	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)
				: Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}
}