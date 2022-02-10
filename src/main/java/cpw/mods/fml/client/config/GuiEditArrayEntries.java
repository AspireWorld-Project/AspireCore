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

import static cpw.mods.fml.client.config.GuiUtils.INVALID;
import static cpw.mods.fml.client.config.GuiUtils.VALID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiConfigEntries.ArrayEntry;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

/**
 * This class implements the scrolling list functionality of the GuiEditList
 * screen. It also provides all the default controls for editing array-type
 * properties.
 *
 * @author bspkrs
 */
@SuppressWarnings("rawtypes")
public class GuiEditArrayEntries extends GuiListExtended {
	private GuiEditArray owningGui;
	public Minecraft mc;
	public IConfigElement configElement;
	public List<IArrayEntry> listEntries;
	public boolean isDefault;
	public boolean isChanged;
	public boolean canAddMoreEntries;
	public final int controlWidth;
	public final Object[] beforeValues;
	public Object[] currentValues;

	@SuppressWarnings("unchecked")
	public GuiEditArrayEntries(GuiEditArray parent, Minecraft mc, IConfigElement configElement, Object[] beforeValues,
			Object[] currentValues) {
		super(mc, parent.width, parent.height, parent.titleLine2 != null ? parent.titleLine3 != null ? 43 : 33 : 23,
				parent.height - 32, 20);
		owningGui = parent;
		this.mc = mc;
		this.configElement = configElement;
		this.beforeValues = beforeValues;
		this.currentValues = currentValues;
		setShowSelectionBox(false);
		isChanged = !Arrays.deepEquals(beforeValues, currentValues);
		isDefault = Arrays.deepEquals(currentValues, configElement.getDefaults());
		canAddMoreEntries = !configElement.isListLengthFixed()
				&& (configElement.getMaxListLength() == -1 || currentValues.length < configElement.getMaxListLength());

		listEntries = new ArrayList<>();

		controlWidth = parent.width / 2 - (configElement.isListLengthFixed() ? 0 : 48);

		if (configElement.isList() && configElement.getArrayEntryClass() != null) {
			Class<? extends IArrayEntry> clazz = configElement.getArrayEntryClass();
			for (Object value : currentValues) {
				try {
					listEntries
							.add(clazz
									.getConstructor(GuiEditArray.class, GuiEditArrayEntries.class, IConfigElement.class,
											Object.class)
									.newInstance(owningGui, this, configElement, value.toString()));
				} catch (Throwable e) {
					FMLLog.severe(
							"There was a critical error instantiating the custom IGuiEditListEntry for property %s.",
							configElement.getName());
					e.printStackTrace();
				}
			}
		} else if (configElement.isList() && configElement.getType().equals(ConfigGuiType.BOOLEAN)) {
			for (Object value : currentValues) {
				listEntries.add(new BooleanEntry(owningGui, this, configElement, Boolean.valueOf(value.toString())));
			}
		} else if (configElement.isList() && configElement.getType().equals(ConfigGuiType.INTEGER)) {
			for (Object value : currentValues) {
				listEntries.add(new IntegerEntry(owningGui, this, configElement, Integer.parseInt(value.toString())));
			}
		} else if (configElement.isList() && configElement.getType().equals(ConfigGuiType.DOUBLE)) {
			for (Object value : currentValues) {
				listEntries.add(new DoubleEntry(owningGui, this, configElement, Double.parseDouble(value.toString())));
			}
		} else if (configElement.isList()) {
			for (Object value : currentValues) {
				listEntries.add(new StringEntry(owningGui, this, configElement, value.toString()));
			}
		}

		if (!configElement.isListLengthFixed()) {
			listEntries.add(new BaseEntry(owningGui, this, configElement));
		}

	}

	@Override
	protected int getScrollBarX() {
		return width - width / 4;
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return owningGui.width;
	}

	@Override
	public IArrayEntry getListEntry(int index) {
		return listEntries.get(index);
	}

	@Override
	protected int getSize() {
		return listEntries.size();
	}

	public void addNewEntry(int index) {
		if (configElement.isList() && configElement.getType() == ConfigGuiType.BOOLEAN) {
			listEntries.add(index, new BooleanEntry(owningGui, this, configElement, Boolean.valueOf(true)));
		} else if (configElement.isList() && configElement.getType() == ConfigGuiType.INTEGER) {
			listEntries.add(index, new IntegerEntry(owningGui, this, configElement, 0));
		} else if (configElement.isList() && configElement.getType() == ConfigGuiType.DOUBLE) {
			listEntries.add(index, new DoubleEntry(owningGui, this, configElement, 0.0D));
		} else if (configElement.isList()) {
			listEntries.add(index, new StringEntry(owningGui, this, configElement, ""));
		}
		canAddMoreEntries = !configElement.isListLengthFixed() && (configElement.getMaxListLength() == -1
				|| listEntries.size() - 1 < configElement.getMaxListLength());
		keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
	}

	public void removeEntry(int index) {
		listEntries.remove(index);
		canAddMoreEntries = !configElement.isListLengthFixed() && (configElement.getMaxListLength() == -1
				|| listEntries.size() - 1 < configElement.getMaxListLength());
		keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
	}

	public boolean isChanged() {
		return isChanged;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void recalculateState() {
		isDefault = true;
		isChanged = false;

		int listLength = configElement.isListLengthFixed() ? listEntries.size() : listEntries.size() - 1;

		if (listLength != configElement.getDefaults().length) {
			isDefault = false;
		}

		if (listLength != beforeValues.length) {
			isChanged = true;
		}

		if (isDefault) {
			for (int i = 0; i < listLength; i++)
				if (!configElement.getDefaults()[i].equals(listEntries.get(i).getValue())) {
					isDefault = false;
				}
		}

		if (!isChanged) {
			for (int i = 0; i < listLength; i++)
				if (!beforeValues[i].equals(listEntries.get(i).getValue())) {
					isChanged = true;
				}
		}
	}

	protected void keyTyped(char eventChar, int eventKey) {
		for (IArrayEntry entry : listEntries) {
			entry.keyTyped(eventChar, eventKey);
		}

		recalculateState();
	}

	protected void updateScreen() {
		for (IArrayEntry entry : listEntries) {
			entry.updateCursorCounter();
		}
	}

	protected void mouseClicked(int x, int y, int mouseEvent) {
		for (IArrayEntry entry : listEntries) {
			entry.mouseClicked(x, y, mouseEvent);
		}
	}

	protected boolean isListSavable() {
		for (IArrayEntry entry : listEntries)
			if (!entry.isValueSavable())
				return false;

		return true;
	}

	@SuppressWarnings("unchecked")
	protected void saveListChanges() {
		int listLength = configElement.isListLengthFixed() ? listEntries.size() : listEntries.size() - 1;

		if (owningGui.slotIndex != -1 && owningGui.parentScreen != null && owningGui.parentScreen instanceof GuiConfig
				&& ((GuiConfig) owningGui.parentScreen).entryList
						.getListEntry(owningGui.slotIndex) instanceof ArrayEntry) {
			ArrayEntry entry = (ArrayEntry) ((GuiConfig) owningGui.parentScreen).entryList
					.getListEntry(owningGui.slotIndex);

			Object[] ao = new Object[listLength];
			for (int i = 0; i < listLength; i++) {
				ao[i] = listEntries.get(i).getValue();
			}

			entry.setListFromChildScreen(ao);
		} else {
			if (configElement.isList() && configElement.getType() == ConfigGuiType.BOOLEAN) {
				Boolean[] abol = new Boolean[listLength];
				for (int i = 0; i < listLength; i++) {
					abol[i] = Boolean.valueOf(listEntries.get(i).getValue().toString());
				}

				configElement.set(abol);
			} else if (configElement.isList() && configElement.getType() == ConfigGuiType.INTEGER) {
				Integer[] ai = new Integer[listLength];
				for (int i = 0; i < listLength; i++) {
					ai[i] = Integer.valueOf(listEntries.get(i).getValue().toString());
				}

				configElement.set(ai);
			} else if (configElement.isList() && configElement.getType() == ConfigGuiType.DOUBLE) {
				Double[] ad = new Double[listLength];
				for (int i = 0; i < listLength; i++) {
					ad[i] = Double.valueOf(listEntries.get(i).getValue().toString());
				}

				configElement.set(ad);
			} else if (configElement.isList()) {
				String[] as = new String[listLength];
				for (int i = 0; i < listLength; i++) {
					as[i] = listEntries.get(i).getValue().toString();
				}

				configElement.set(as);
			}
		}
	}

	protected void drawScreenPost(int mouseX, int mouseY, float f) {
		for (IArrayEntry entry : listEntries) {
			entry.drawToolTip(mouseX, mouseY);
		}
	}

	/**
	 * IGuiListEntry Inner Classes
	 */

	public static class DoubleEntry extends StringEntry {
		public DoubleEntry(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList, IConfigElement configElement,
				Double value) {
			super(owningScreen, owningEntryList, configElement, value);
			isValidated = true;
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (owningScreen.enabled || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				String validChars = "0123456789";
				String before = textFieldValue.getText();
				if (validChars.contains(String.valueOf(eventChar))
						|| !before.startsWith("-") && textFieldValue.getCursorPosition() == 0 && eventChar == '-'
						|| !before.contains(".") && eventChar == '.' || eventKey == Keyboard.KEY_BACK
						|| eventKey == Keyboard.KEY_DELETE || eventKey == Keyboard.KEY_LEFT
						|| eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME
						|| eventKey == Keyboard.KEY_END) {
					textFieldValue.textboxKeyTyped(owningScreen.enabled ? eventChar : Keyboard.CHAR_NONE, eventKey);
				}

				if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-")) {
					try {
						double value = Double.parseDouble(textFieldValue.getText().trim());
						if (value < Double.valueOf(configElement.getMinValue().toString())
								|| value > Double.valueOf(configElement.getMaxValue().toString())) {
							isValidValue = false;
						} else {
							isValidValue = true;
						}
					} catch (Throwable e) {
						isValidValue = false;
					}
				} else {
					isValidValue = false;
				}
			}
		}

		@Override
		public Double getValue() {
			try {
				return Double.valueOf(textFieldValue.getText().trim());
			} catch (Throwable e) {
				return Double.MAX_VALUE;
			}
		}
	}

	public static class IntegerEntry extends StringEntry {
		public IntegerEntry(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList,
				IConfigElement configElement, Integer value) {
			super(owningScreen, owningEntryList, configElement, value);
			isValidated = true;
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (owningScreen.enabled || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				String validChars = "0123456789";
				String before = textFieldValue.getText();
				if (validChars.contains(String.valueOf(eventChar))
						|| !before.startsWith("-") && textFieldValue.getCursorPosition() == 0 && eventChar == '-'
						|| eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
						|| eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
						|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
					textFieldValue.textboxKeyTyped(owningScreen.enabled ? eventChar : Keyboard.CHAR_NONE, eventKey);
				}

				if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-")) {
					try {
						long value = Long.parseLong(textFieldValue.getText().trim());
						if (value < Integer.valueOf(configElement.getMinValue().toString())
								|| value > Integer.valueOf(configElement.getMaxValue().toString())) {
							isValidValue = false;
						} else {
							isValidValue = true;
						}
					} catch (Throwable e) {
						isValidValue = false;
					}
				} else {
					isValidValue = false;
				}
			}
		}

		@Override
		public Integer getValue() {
			try {
				return Integer.valueOf(textFieldValue.getText().trim());
			} catch (Throwable e) {
				return Integer.MAX_VALUE;
			}
		}
	}

	public static class StringEntry extends BaseEntry {
		protected final GuiTextField textFieldValue;

		public StringEntry(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList, IConfigElement configElement,
				Object value) {
			super(owningScreen, owningEntryList, configElement);
			textFieldValue = new GuiTextField(owningEntryList.mc.fontRenderer, owningEntryList.width / 4 + 1, 0,
					owningEntryList.controlWidth - 3, 16);
			textFieldValue.setMaxStringLength(10000);
			textFieldValue.setText(value.toString());
			isValidated = configElement.getValidationPattern() != null;

			if (configElement.getValidationPattern() != null) {
				if (configElement.getValidationPattern().matcher(textFieldValue.getText().trim()).matches()) {
					isValidValue = true;
				} else {
					isValidValue = false;
				}
			}
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
			if (configElement.isListLengthFixed() || slotIndex != owningEntryList.listEntries.size() - 1) {
				textFieldValue.setVisible(true);
				textFieldValue.yPosition = y + 1;
				textFieldValue.drawTextBox();
			} else {
				textFieldValue.setVisible(false);
			}
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (owningScreen.enabled || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				textFieldValue.textboxKeyTyped(owningScreen.enabled ? eventChar : Keyboard.CHAR_NONE, eventKey);

				if (configElement.getValidationPattern() != null) {
					if (configElement.getValidationPattern().matcher(textFieldValue.getText().trim()).matches()) {
						isValidValue = true;
					} else {
						isValidValue = false;
					}
				}
			}
		}

		@Override
		public void updateCursorCounter() {
			textFieldValue.updateCursorCounter();
		}

		@Override
		public void mouseClicked(int x, int y, int mouseEvent) {
			textFieldValue.mouseClicked(x, y, mouseEvent);
		}

		@Override
		public Object getValue() {
			return textFieldValue.getText().trim();
		}

	}

	public static class BooleanEntry extends BaseEntry {
		protected final GuiButtonExt btnValue;
		private boolean value;

		public BooleanEntry(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList,
				IConfigElement configElement, boolean value) {
			super(owningScreen, owningEntryList, configElement);
			this.value = value;
			btnValue = new GuiButtonExt(0, 0, 0, owningEntryList.controlWidth, 18, I18n.format(String.valueOf(value)));
			btnValue.enabled = owningScreen.enabled;
			isValidated = false;
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
			btnValue.xPosition = listWidth / 4;
			btnValue.yPosition = y;

			String trans = I18n.format(String.valueOf(value));
			if (!trans.equals(String.valueOf(value))) {
				btnValue.displayString = trans;
			} else {
				btnValue.displayString = String.valueOf(value);
			}
			btnValue.packedFGColour = value ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);

			btnValue.drawButton(owningEntryList.mc, mouseX, mouseY);
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (btnValue.mousePressed(owningEntryList.mc, x, y)) {
				btnValue.func_146113_a(owningEntryList.mc.getSoundHandler());
				value = !value;
				owningEntryList.recalculateState();
				return true;
			}

			return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			btnValue.mouseReleased(x, y);
			super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		}

		@Override
		public Object getValue() {
			return Boolean.valueOf(value);
		}
	}

	public static class BaseEntry implements IArrayEntry {
		protected final GuiEditArray owningScreen;
		protected final GuiEditArrayEntries owningEntryList;
		protected final IConfigElement configElement;
		protected final GuiButtonExt btnAddNewEntryAbove;
		private final HoverChecker addNewEntryAboveHoverChecker;
		protected final GuiButtonExt btnRemoveEntry;
		private final HoverChecker removeEntryHoverChecker;
		private List addNewToolTip, removeToolTip;
		protected boolean isValidValue = true;
		protected boolean isValidated = false;

		@SuppressWarnings({ "unchecked" })
		public BaseEntry(GuiEditArray owningScreen, GuiEditArrayEntries owningEntryList, IConfigElement configElement) {
			this.owningScreen = owningScreen;
			this.owningEntryList = owningEntryList;
			this.configElement = configElement;
			btnAddNewEntryAbove = new GuiButtonExt(0, 0, 0, 18, 18, "+");
			btnAddNewEntryAbove.packedFGColour = GuiUtils.getColorCode('2', true);
			btnAddNewEntryAbove.enabled = owningScreen.enabled;
			btnRemoveEntry = new GuiButtonExt(0, 0, 0, 18, 18, "x");
			btnRemoveEntry.packedFGColour = GuiUtils.getColorCode('c', true);
			btnRemoveEntry.enabled = owningScreen.enabled;
			addNewEntryAboveHoverChecker = new HoverChecker(btnAddNewEntryAbove, 800);
			removeEntryHoverChecker = new HoverChecker(btnRemoveEntry, 800);
			addNewToolTip = new ArrayList();
			removeToolTip = new ArrayList();
			addNewToolTip.add(I18n.format("fml.configgui.tooltip.addNewEntryAbove"));
			removeToolTip.add(I18n.format("fml.configgui.tooltip.removeEntry"));
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			if (getValue() != null && isValidated) {
				owningEntryList.mc.fontRenderer.drawString(
						isValidValue ? EnumChatFormatting.GREEN + VALID : EnumChatFormatting.RED + INVALID,
						listWidth / 4 - owningEntryList.mc.fontRenderer.getStringWidth(VALID) - 2,
						y + slotHeight / 2 - owningEntryList.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
			}

			int half = listWidth / 2;
			if (owningEntryList.canAddMoreEntries) {
				btnAddNewEntryAbove.visible = true;
				btnAddNewEntryAbove.xPosition = half + half / 2 - 44;
				btnAddNewEntryAbove.yPosition = y;
				btnAddNewEntryAbove.drawButton(owningEntryList.mc, mouseX, mouseY);
			} else {
				btnAddNewEntryAbove.visible = false;
			}

			if (!configElement.isListLengthFixed() && slotIndex != owningEntryList.listEntries.size() - 1) {
				btnRemoveEntry.visible = true;
				btnRemoveEntry.xPosition = half + half / 2 - 22;
				btnRemoveEntry.yPosition = y;
				btnRemoveEntry.drawButton(owningEntryList.mc, mouseX, mouseY);
			} else {
				btnRemoveEntry.visible = false;
			}
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			boolean canHover = mouseY < owningEntryList.bottom && mouseY > owningEntryList.top;
			if (btnAddNewEntryAbove.visible && addNewEntryAboveHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				owningScreen.drawToolTip(addNewToolTip, mouseX, mouseY);
			}
			if (btnRemoveEntry.visible && removeEntryHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				owningScreen.drawToolTip(removeToolTip, mouseX, mouseY);
			}
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (btnAddNewEntryAbove.mousePressed(owningEntryList.mc, x, y)) {
				btnAddNewEntryAbove.func_146113_a(owningEntryList.mc.getSoundHandler());
				owningEntryList.addNewEntry(index);
				owningEntryList.recalculateState();
				return true;
			} else if (btnRemoveEntry.mousePressed(owningEntryList.mc, x, y)) {
				btnRemoveEntry.func_146113_a(owningEntryList.mc.getSoundHandler());
				owningEntryList.removeEntry(index);
				owningEntryList.recalculateState();
				return true;
			}

			return false;
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			btnAddNewEntryAbove.mouseReleased(x, y);
			btnRemoveEntry.mouseReleased(x, y);
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
		}

		@Override
		public void updateCursorCounter() {
		}

		@Override
		public void mouseClicked(int x, int y, int mouseEvent) {
		}

		@Override
		public boolean isValueSavable() {
			return isValidValue;
		}

		@Override
		public Object getValue() {
			return null;
		}
	}

	public static interface IArrayEntry extends GuiListExtended.IGuiListEntry {
		public void keyTyped(char eventChar, int eventKey);

		public void updateCursorCounter();

		public void mouseClicked(int x, int y, int mouseEvent);

		public void drawToolTip(int mouseX, int mouseY);

		public boolean isValueSavable();

		public Object getValue();
	}
}