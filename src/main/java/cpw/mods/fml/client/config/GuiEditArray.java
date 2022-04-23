/*
 * Forge Mod Loader
 * Copyright (c) 2012-2014 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors (this class):
 *     bspkrs - implementation
 */

package cpw.mods.fml.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * This class is the base screen used for editing an array-type property. It
 * provides a list of array entries for the user to edit. This screen is invoked
 * from a GuiConfig screen by controls that use the EditListPropEntry
 * IGuiConfigListEntry object.
 *
 * @author bspkrs
 */
public class GuiEditArray extends GuiScreen {
	protected GuiScreen parentScreen;
	@SuppressWarnings("rawtypes")
	protected IConfigElement configElement;
	private GuiEditArrayEntries entryList;
	private GuiButtonExt btnUndoChanges, btnDefault, btnDone;
	private final String title;
	protected String titleLine2;
	protected String titleLine3;
	protected int slotIndex;
	private final Object[] beforeValues;
	private Object[] currentValues;
	private final HoverChecker tooltipHoverChecker;
	@SuppressWarnings("rawtypes")
	private List toolTip;
	protected boolean enabled;

	@SuppressWarnings("rawtypes")
	public GuiEditArray(GuiScreen parentScreen, IConfigElement configElement, int slotIndex, Object[] currentValues,
			boolean enabled) {
		mc = Minecraft.getMinecraft();
		this.parentScreen = parentScreen;
		this.configElement = configElement;
		this.slotIndex = slotIndex;
		beforeValues = currentValues;
		this.currentValues = currentValues;
		toolTip = new ArrayList();
		this.enabled = enabled;
		String propName = I18n.format(configElement.getLanguageKey());
		String comment;

		comment = I18n.format(configElement.getLanguageKey() + ".tooltip", "\n" + EnumChatFormatting.AQUA,
				configElement.getDefault(), configElement.getMinValue(), configElement.getMaxValue());

		if (!comment.equals(configElement.getLanguageKey() + ".tooltip")) {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(
					EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
		} else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty()) {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(
					EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + configElement.getComment(),
					300);
		} else {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(
					EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
		}

		if (parentScreen instanceof GuiConfig) {
			title = ((GuiConfig) parentScreen).title;
			if (((GuiConfig) parentScreen).titleLine2 != null) {
				titleLine2 = ((GuiConfig) parentScreen).titleLine2;
				titleLine3 = I18n.format(configElement.getLanguageKey());
			} else {
				titleLine2 = I18n.format(configElement.getLanguageKey());
			}
			tooltipHoverChecker = new HoverChecker(28, 37, 0, parentScreen.width, 800);
		} else {
			title = I18n.format(configElement.getLanguageKey());
			tooltipHoverChecker = new HoverChecker(8, 17, 0, parentScreen.width, 800);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		entryList = new GuiEditArrayEntries(this, mc, configElement, beforeValues, currentValues);

		int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges"))
				+ undoGlyphWidth + 20;
		int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault"))
				+ resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
		buttonList.add(btnDone = new GuiButtonExt(2000, width / 2 - buttonWidthHalf, height - 29, doneWidth, 20,
				I18n.format("gui.done")));
		buttonList.add(btnDefault = new GuiUnicodeGlyphButton(2001,
				width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, height - 29, resetWidth, 20,
				" " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
		buttonList.add(btnUndoChanges = new GuiUnicodeGlyphButton(2002, width / 2 - buttonWidthHalf + doneWidth + 5,
				height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			try {
				entryList.saveListChanges();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			mc.displayGuiScreen(parentScreen);
		} else if (button.id == 2001) {
			currentValues = configElement.getDefaults();
			entryList = new GuiEditArrayEntries(this, mc, configElement, beforeValues, currentValues);
		} else if (button.id == 2002) {
			currentValues = Arrays.copyOf(beforeValues, beforeValues.length);
			entryList = new GuiEditArrayEntries(this, mc, configElement, beforeValues, currentValues);
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) {
		if (mouseEvent != 0 || !entryList.func_148179_a(x, y, mouseEvent)) {
			entryList.mouseClicked(x, y, mouseEvent);
			super.mouseClicked(x, y, mouseEvent);
		}
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int mouseEvent) {
		if (mouseEvent != 0 || !entryList.func_148181_b(x, y, mouseEvent)) {
			super.mouseMovedOrUp(x, y, mouseEvent);
		}
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(parentScreen);
		} else {
			entryList.keyTyped(eventChar, eventKey);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		entryList.updateScreen();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		entryList.drawScreen(par1, par2, par3);
		drawCenteredString(fontRendererObj, title, width / 2, 8, 16777215);

		if (titleLine2 != null) {
			drawCenteredString(fontRendererObj, titleLine2, width / 2, 18, 16777215);
		}

		if (titleLine3 != null) {
			drawCenteredString(fontRendererObj, titleLine3, width / 2, 28, 16777215);
		}

		btnDone.enabled = entryList.isListSavable();
		btnDefault.enabled = enabled && !entryList.isDefault();
		btnUndoChanges.enabled = enabled && entryList.isChanged();
		super.drawScreen(par1, par2, par3);
		entryList.drawScreenPost(par1, par2, par3);

		if (tooltipHoverChecker != null && tooltipHoverChecker.checkHover(par1, par2)) {
			drawToolTip(toolTip, par1, par2);
		}
	}

	@SuppressWarnings("rawtypes")
	public void drawToolTip(List stringList, int x, int y) {
		func_146283_a(stringList, x, y);
	}
}