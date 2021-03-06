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

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.*;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * This class implements the scrolling list functionality of the config GUI
 * screens. It also provides all the default control handlers for the various
 * property types.
 *
 * @author bspkrs
 */
public class GuiConfigEntries extends GuiListExtended {
	public final GuiConfig owningScreen;
	public final Minecraft mc;
	@SuppressWarnings("rawtypes")
	public List<IConfigEntry> listEntries;
	/**
	 * The max width of the label of all IConfigEntry objects.
	 */
	public int maxLabelTextWidth = 0;
	/**
	 * The max x boundary of all IConfigEntry objects.
	 */
	public int maxEntryRightBound = 0;
	/**
	 * The x position where the label should be drawn.
	 */
	public int labelX;
	/**
	 * The x position where the control should be drawn.
	 */
	public int controlX;
	/**
	 * The width of the control.
	 */
	public int controlWidth;
	/**
	 * The minimum x position where the Undo/Default buttons will start
	 */
	public int resetX;
	/**
	 * The x position of the scroll bar.
	 */
	public int scrollBarX;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GuiConfigEntries(GuiConfig parent, Minecraft mc) {
		super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);
		owningScreen = parent;
		setShowSelectionBox(false);
		this.mc = mc;
		listEntries = new ArrayList<>();
		// int i = 0;
		// String s = null;

		for (IConfigElement configElement : parent.configElements) {
			if (configElement != null) {
				if (configElement.isProperty() && configElement.showInGui()) // as opposed to being a child category
																				// entry
				{
					int length;

					// protects against language keys that are not defined in the .lang file
					if (!I18n.format(configElement.getLanguageKey()).equals(configElement.getLanguageKey())) {
						length = mc.fontRenderer.getStringWidth(I18n.format(configElement.getLanguageKey()));
					} else {
						length = mc.fontRenderer.getStringWidth(configElement.getName());
					}

					if (length > maxLabelTextWidth) {
						maxLabelTextWidth = length;
					}
				}
			}
		}

		int viewWidth = maxLabelTextWidth + 8 + width / 2;
		labelX = width / 2 - viewWidth / 2;
		controlX = labelX + maxLabelTextWidth + 8;
		resetX = width / 2 + viewWidth / 2 - 45;
		controlWidth = resetX - controlX - 5;
		scrollBarX = width;

		for (IConfigElement configElement : parent.configElements) {
			if (configElement != null && configElement.showInGui()) {
				if (configElement.getConfigEntryClass() != null) {
					try {
						listEntries.add((IConfigEntry) configElement.getConfigEntryClass()
								.getConstructor(GuiConfig.class, GuiConfigEntries.class, IConfigElement.class)
								.newInstance(owningScreen, this, configElement));
					} catch (Throwable e) {
						FMLLog.severe(
								"There was a critical error instantiating the custom IConfigEntry for config element %s.",
								configElement.getName());
						e.printStackTrace();
					}
				} else if (configElement.isProperty()) {
					if (configElement.isList()) {
						listEntries.add(new GuiConfigEntries.ArrayEntry(owningScreen, this, configElement));
					} else if (configElement.getType() == ConfigGuiType.BOOLEAN) {
						listEntries.add(new GuiConfigEntries.BooleanEntry(owningScreen, this, configElement));
					} else if (configElement.getType() == ConfigGuiType.INTEGER) {
						listEntries.add(new GuiConfigEntries.IntegerEntry(owningScreen, this, configElement));
					} else if (configElement.getType() == ConfigGuiType.DOUBLE) {
						listEntries.add(new GuiConfigEntries.DoubleEntry(owningScreen, this, configElement));
					} else if (configElement.getType() == ConfigGuiType.COLOR) {
						if (configElement.getValidValues() != null && configElement.getValidValues().length > 0) {
							listEntries.add(new GuiConfigEntries.ChatColorEntry(owningScreen, this, configElement));
						} else {
							listEntries.add(new GuiConfigEntries.StringEntry(owningScreen, this, configElement));
						}
					} else if (configElement.getType() == ConfigGuiType.MOD_ID) {
						Map<Object, String> values = new TreeMap<>();
						for (ModContainer mod : Loader.instance().getActiveModList()) {
							values.put(mod.getModId(), mod.getName());
						}
						values.put("minecraft", "Minecraft");
						listEntries.add(new SelectValueEntry(owningScreen, this, configElement, values));
					} else if (configElement.getType() == ConfigGuiType.STRING) {
						if (configElement.getValidValues() != null && configElement.getValidValues().length > 0) {
							listEntries.add(new GuiConfigEntries.CycleValueEntry(owningScreen, this, configElement));
						} else {
							listEntries.add(new GuiConfigEntries.StringEntry(owningScreen, this, configElement));
						}
					}
				} else if (configElement.getType() == ConfigGuiType.CONFIG_CATEGORY) {
					listEntries.add(new CategoryEntry(owningScreen, this, configElement));
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected void initGui() {
		width = owningScreen.width;
		height = owningScreen.height;

		maxLabelTextWidth = 0;
		for (IConfigEntry entry : listEntries)
			if (entry.getLabelWidth() > maxLabelTextWidth) {
				maxLabelTextWidth = entry.getLabelWidth();
			}

		top = owningScreen.titleLine2 != null ? 33 : 23;
		bottom = owningScreen.height - 32;
		left = 0;
		right = width;
		int viewWidth = maxLabelTextWidth + 8 + width / 2;
		labelX = width / 2 - viewWidth / 2;
		controlX = labelX + maxLabelTextWidth + 8;
		resetX = width / 2 + viewWidth / 2 - 45;

		maxEntryRightBound = 0;
		for (IConfigEntry entry : listEntries)
			if (entry.getEntryRightBound() > maxEntryRightBound) {
				maxEntryRightBound = entry.getEntryRightBound();
			}

		scrollBarX = maxEntryRightBound + 5;
		controlWidth = maxEntryRightBound - controlX - 45;
	}

	@Override
	public int getSize() {
		return listEntries.size();
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public IConfigEntry getListEntry(int index) {
		return listEntries.get(index);
	}

	@Override
	public int getScrollBarX() {
		return scrollBarX;
	}

	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return owningScreen.width;
	}

	/**
	 * This method is a pass-through for IConfigEntry objects that require
	 * keystrokes. Called from the parent GuiConfig screen.
	 */
	@SuppressWarnings("rawtypes")
	public void keyTyped(char eventChar, int eventKey) {
		for (IConfigEntry entry : listEntries) {
			entry.keyTyped(eventChar, eventKey);
		}
	}

	/**
	 * This method is a pass-through for IConfigEntry objects that contain
	 * GuiTextField elements. Called from the parent GuiConfig screen.
	 */
	@SuppressWarnings("rawtypes")
	public void updateScreen() {
		for (IConfigEntry entry : listEntries) {
			entry.updateCursorCounter();
		}
	}

	/**
	 * This method is a pass-through for IConfigEntry objects that contain
	 * GuiTextField elements. Called from the parent GuiConfig screen.
	 */
	@SuppressWarnings("rawtypes")
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		for (IConfigEntry entry : listEntries) {
			entry.mouseClicked(mouseX, mouseY, mouseEvent);
		}
	}

	/**
	 * This method is a pass-through for IConfigListEntry objects that need to
	 * perform actions when the containing GUI is closed.
	 */
	@SuppressWarnings("rawtypes")
	public void onGuiClosed() {
		for (IConfigEntry entry : listEntries) {
			entry.onGuiClosed();
		}
	}

	/**
	 * Saves all properties on this screen / child screens. This method returns true
	 * if any elements were changed that require a restart for proper handling.
	 */
	@SuppressWarnings("rawtypes")
	public boolean saveConfigElements() {
		boolean requiresRestart = false;
		for (IConfigEntry entry : listEntries)
			if (entry.saveConfigElement()) {
				requiresRestart = true;
			}

		return requiresRestart;
	}

	/**
	 * Returns true if all IConfigEntry objects on this screen are set to default.
	 * If includeChildren is true sub-category objects are checked as well.
	 */
	@SuppressWarnings("rawtypes")
	public boolean areAllEntriesDefault(boolean includeChildren) {
		for (IConfigEntry entry : listEntries)
			if ((includeChildren || !(entry instanceof CategoryEntry)) && !entry.isDefault())
				return false;

		return true;
	}

	/**
	 * Sets all IConfigEntry objects on this screen to default. If includeChildren
	 * is true sub-category objects are set as well.
	 */
	@SuppressWarnings("rawtypes")
	public void setAllToDefault(boolean includeChildren) {
		for (IConfigEntry entry : listEntries)
			if (includeChildren || !(entry instanceof CategoryEntry)) {
				entry.setToDefault();
			}
	}

	/**
	 * Returns true if any IConfigEntry objects on this screen are changed. If
	 * includeChildren is true sub-category objects are checked as well.
	 */
	@SuppressWarnings("rawtypes")
	public boolean hasChangedEntry(boolean includeChildren) {
		for (IConfigEntry entry : listEntries)
			if ((includeChildren || !(entry instanceof CategoryEntry)) && entry.isChanged())
				return true;

		return false;
	}

	/**
	 * Returns true if any IConfigEntry objects on this screen are enabled. If
	 * includeChildren is true sub-category objects are checked as well.
	 */
	@SuppressWarnings("rawtypes")
	public boolean areAnyEntriesEnabled(boolean includeChildren) {
		for (IConfigEntry entry : listEntries)
			if ((includeChildren || !(entry instanceof CategoryEntry)) && entry.enabled())
				return true;

		return false;
	}

	/**
	 * Reverts changes to all IConfigEntry objects on this screen. If
	 * includeChildren is true sub-category objects are reverted as well.
	 */
	@SuppressWarnings("rawtypes")
	public void undoAllChanges(boolean includeChildren) {
		for (IConfigEntry entry : listEntries)
			if (includeChildren || !(entry instanceof CategoryEntry)) {
				entry.undoChanges();
			}
	}

	/**
	 * Calls the drawToolTip() method for all IConfigEntry objects on this screen.
	 * This is called from the parent GuiConfig screen after drawing all other
	 * elements.
	 */
	@SuppressWarnings("rawtypes")
	public void drawScreenPost(int mouseX, int mouseY, float partialTicks) {
		for (IConfigEntry entry : listEntries) {
			entry.drawToolTip(mouseX, mouseY);
		}
	}

	/**
	 * BooleanPropEntry
	 *
	 * Provides a GuiButton that toggles between true and false.
	 */
	public static class BooleanEntry extends ButtonEntry {
		protected final boolean beforeValue;
		protected boolean currentValue;

		private BooleanEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
				IConfigElement<Boolean> configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeValue = Boolean.valueOf(configElement.get().toString());
			currentValue = beforeValue;
			btnValue.enabled = enabled();
			updateValueButtonText();
		}

		@Override
		public void updateValueButtonText() {
			btnValue.displayString = I18n.format(String.valueOf(currentValue));
			btnValue.packedFGColour = currentValue ? GuiUtils.getColorCode('2', true)
					: GuiUtils.getColorCode('4', true);
		}

		@Override
		public void valueButtonPressed(int slotIndex) {
			if (enabled()) {
				currentValue = !currentValue;
			}
		}

		@Override
		public boolean isDefault() {
			return currentValue == Boolean.valueOf(configElement.getDefault().toString());
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				currentValue = Boolean.valueOf(configElement.getDefault().toString());
				updateValueButtonText();
			}
		}

		@Override
		public boolean isChanged() {
			return currentValue != beforeValue;
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				currentValue = beforeValue;
				updateValueButtonText();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled() && isChanged()) {
				configElement.set(currentValue);
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public Boolean getCurrentValue() {
			return currentValue;
		}

		@Override
		public Boolean[] getCurrentValues() {
			return new Boolean[] { getCurrentValue() };
		}
	}

	/**
	 * CycleValueEntry
	 *
	 * Provides a GuiButton that cycles through the prop's validValues array. If the
	 * current prop value is not a valid value, the first entry replaces the current
	 * value.
	 */
	public static class CycleValueEntry extends ButtonEntry {
		protected final int beforeIndex;
		protected final int defaultIndex;
		protected int currentIndex;

		private CycleValueEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
				IConfigElement<String> configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeIndex = getIndex(configElement.get().toString());
			defaultIndex = getIndex(configElement.getDefault().toString());
			currentIndex = beforeIndex;
			btnValue.enabled = enabled();
			updateValueButtonText();
		}

		private int getIndex(String s) {
			for (int i = 0; i < configElement.getValidValues().length; i++)
				if (configElement.getValidValues()[i].equalsIgnoreCase(s))
					return i;

			return 0;
		}

		@Override
		public void updateValueButtonText() {
			btnValue.displayString = I18n.format(configElement.getValidValues()[currentIndex]);
		}

		@Override
		public void valueButtonPressed(int slotIndex) {
			if (enabled()) {
				if (++currentIndex >= configElement.getValidValues().length) {
					currentIndex = 0;
				}

				updateValueButtonText();
			}
		}

		@Override
		public boolean isDefault() {
			return currentIndex == defaultIndex;
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				currentIndex = defaultIndex;
				updateValueButtonText();
			}
		}

		@Override
		public boolean isChanged() {
			return currentIndex != beforeIndex;
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				currentIndex = beforeIndex;
				updateValueButtonText();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled() && isChanged()) {
				configElement.set(configElement.getValidValues()[currentIndex]);
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public String getCurrentValue() {
			return configElement.getValidValues()[currentIndex];
		}

		@Override
		public String[] getCurrentValues() {
			return new String[] { getCurrentValue() };
		}
	}

	/**
	 * ChatColorEntry
	 *
	 * Provides a GuiButton that cycles through the list of chat color codes.
	 */
	public static class ChatColorEntry extends CycleValueEntry {
		ChatColorEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement) {
			super(owningScreen, owningEntryList, configElement);
			btnValue.enabled = enabled();
			updateValueButtonText();
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			btnValue.packedFGColour = GuiUtils.getColorCode(configElement.getValidValues()[currentIndex].charAt(0),
					true);
			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		}

		@Override
		public void updateValueButtonText() {
			btnValue.displayString = I18n.format(configElement.getValidValues()[currentIndex]) + " - "
					+ I18n.format("fml.configgui.sampletext");
		}
	}

	/**
	 * SelectValueEntry
	 *
	 * Provides a GuiButton with the current value as the displayString. Accepts a
	 * Map of selectable values with the signature <Object, String> where the key is
	 * the Object to be selected and the value is the String that will show on the
	 * selection list. EG: a map of Mod ID values where the key is the Mod ID and
	 * the value is the Mod Name.
	 */
	public static class SelectValueEntry extends ButtonEntry {
		protected final String beforeValue;
		protected Object currentValue;
		protected Map<Object, String> selectableValues;

		public SelectValueEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
				IConfigElement<String> configElement, Map<Object, String> selectableValues) {
			super(owningScreen, owningEntryList, configElement);
			beforeValue = configElement.get().toString();
			currentValue = configElement.get().toString();
			this.selectableValues = selectableValues;
			updateValueButtonText();
		}

		@Override
		public void updateValueButtonText() {
			btnValue.displayString = currentValue.toString();
		}

		@Override
		public void valueButtonPressed(int slotIndex) {
			mc.displayGuiScreen(new GuiSelectString(owningScreen, configElement, slotIndex, selectableValues,
					currentValue, enabled()));
		}

		public void setValueFromChildScreen(Object newValue) {
			if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null) {
				currentValue = newValue;
				updateValueButtonText();
			}
		}

		@Override
		public boolean isDefault() {
			if (configElement.getDefault() != null)
				return configElement.getDefault().equals(currentValue);
			else
				return currentValue == null;
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				currentValue = configElement.getDefault().toString();
				updateValueButtonText();
			}
		}

		@Override
		public boolean isChanged() {
			if (beforeValue != null)
				return !beforeValue.equals(currentValue);
			else
				return currentValue == null;
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				currentValue = beforeValue;
				updateValueButtonText();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled() && isChanged()) {
				configElement.set(currentValue);
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public String getCurrentValue() {
			return currentValue.toString();
		}

		@Override
		public String[] getCurrentValues() {
			return new String[] { getCurrentValue() };
		}
	}

	/**
	 * ArrayEntry
	 *
	 * Provides a GuiButton with the list contents as the displayString. Clicking
	 * the button navigates to a screen where the list can be edited.
	 */
	public static class ArrayEntry extends ButtonEntry {
		protected final Object[] beforeValues;
		protected Object[] currentValues;

		@SuppressWarnings("rawtypes")
		public ArrayEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeValues = configElement.getList();
			currentValues = configElement.getList();
			updateValueButtonText();
		}

		@Override
		public void updateValueButtonText() {
			btnValue.displayString = "";
			for (Object o : currentValues) {
				btnValue.displayString += ", [" + o + "]";
			}

			btnValue.displayString = btnValue.displayString.replaceFirst(", ", "");
		}

		@Override
		public void valueButtonPressed(int slotIndex) {
			mc.displayGuiScreen(new GuiEditArray(owningScreen, configElement, slotIndex, currentValues, enabled()));
		}

		public void setListFromChildScreen(Object[] newList) {
			if (enabled() && !Arrays.deepEquals(currentValues, newList)) {
				currentValues = newList;
				updateValueButtonText();
			}
		}

		@Override
		public boolean isDefault() {
			return Arrays.deepEquals(configElement.getDefaults(), currentValues);
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				currentValues = configElement.getDefaults();
				updateValueButtonText();
			}
		}

		@Override
		public boolean isChanged() {
			return !Arrays.deepEquals(beforeValues, currentValues);
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				currentValues = beforeValues;
				updateValueButtonText();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled() && isChanged()) {
				configElement.set(currentValues);
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public Object getCurrentValue() {
			return btnValue.displayString;
		}

		@Override
		public Object[] getCurrentValues() {
			return currentValues;
		}
	}

	/**
	 * NumberSliderEntry
	 *
	 * Provides a slider for numeric properties.
	 */
	public static class NumberSliderEntry extends ButtonEntry {
		protected final double beforeValue;

		public NumberSliderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
				IConfigElement<?> configElement) {
			super(owningScreen, owningEntryList, configElement,
					new GuiSlider(0, owningEntryList.controlX, 0, owningEntryList.controlWidth, 18, "", "",
							Double.valueOf(configElement.getMinValue().toString()),
							Double.valueOf(configElement.getMaxValue().toString()),
							Double.valueOf(configElement.get().toString()),
							configElement.getType() == ConfigGuiType.DOUBLE, true));

			if (configElement.getType() == ConfigGuiType.INTEGER) {
				beforeValue = Integer.valueOf(configElement.get().toString());
			} else {
				beforeValue = Double.valueOf(configElement.get().toString());
			}
		}

		@Override
		public void updateValueButtonText() {
			((GuiSlider) btnValue).updateSlider();
		}

		@Override
		public void valueButtonPressed(int slotIndex) {
		}

		@Override
		public boolean isDefault() {
			if (configElement.getType() == ConfigGuiType.INTEGER)
				return ((GuiSlider) btnValue).getValueInt() == Integer.valueOf(configElement.getDefault().toString());
			else
				return ((GuiSlider) btnValue).getValue() == Double.valueOf(configElement.getDefault().toString());
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				((GuiSlider) btnValue).setValue(Double.valueOf(configElement.getDefault().toString()));
				((GuiSlider) btnValue).updateSlider();
			}
		}

		@Override
		public boolean isChanged() {
			if (configElement.getType() == ConfigGuiType.INTEGER)
				return ((GuiSlider) btnValue).getValueInt() != (int) Math.round(beforeValue);
			else
				return ((GuiSlider) btnValue).getValue() != beforeValue;
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				((GuiSlider) btnValue).setValue(beforeValue);
				((GuiSlider) btnValue).updateSlider();
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled() && isChanged()) {
				if (configElement.getType() == ConfigGuiType.INTEGER) {
					configElement.set(((GuiSlider) btnValue).getValueInt());
				} else {
					configElement.set(((GuiSlider) btnValue).getValue());
				}
				return configElement.requiresMcRestart();
			}
			return false;
		}

		@Override
		public Object getCurrentValue() {
			if (configElement.getType() == ConfigGuiType.INTEGER)
				return ((GuiSlider) btnValue).getValueInt();
			else
				return ((GuiSlider) btnValue).getValue();
		}

		@Override
		public Object[] getCurrentValues() {
			return new Object[] { getCurrentValue() };
		}
	}

	/**
	 * ButtonEntry
	 *
	 * Provides a basic GuiButton entry to be used as a base for other entries that
	 * require a button for the value.
	 */
	public static abstract class ButtonEntry extends ListEntryBase {
		protected final GuiButtonExt btnValue;

		public ButtonEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement) {
			this(owningScreen, owningEntryList, configElement,
					new GuiButtonExt(0, owningEntryList.controlX, 0, owningEntryList.controlWidth, 18,
							configElement.get() != null ? I18n.format(String.valueOf(configElement.get())) : ""));
		}

		public ButtonEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement,
				GuiButtonExt button) {
			super(owningScreen, owningEntryList, configElement);
			btnValue = button;
		}

		/**
		 * Updates the displayString of the value button.
		 */
		public abstract void updateValueButtonText();

		/**
		 * Called when the value button has been clicked.
		 */
		public abstract void valueButtonPressed(int slotIndex);

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
			btnValue.width = owningEntryList.controlWidth;
			btnValue.xPosition = owningScreen.entryList.controlX;
			btnValue.yPosition = y;
			btnValue.enabled = enabled();
			btnValue.drawButton(mc, mouseX, mouseY);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (btnValue.mousePressed(mc, x, y)) {
				btnValue.func_146113_a(mc.getSoundHandler());
				valueButtonPressed(index);
				updateValueButtonText();
				return true;
			} else
				return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent,
		 * relativeX, relativeY
		 */
		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
			btnValue.mouseReleased(x, y);
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
	}

	/**
	 * IntegerEntry
	 *
	 * Provides a GuiTextField for user input. Input is restricted to ensure the
	 * value can be parsed using Integer.parseInteger().
	 */
	public static class IntegerEntry extends StringEntry {
		protected final int beforeValue;

		@SuppressWarnings("rawtypes")
		public IntegerEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeValue = Integer.valueOf(configElement.get().toString());
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				String validChars = "0123456789";
				String before = textFieldValue.getText();
				if (validChars.contains(String.valueOf(eventChar))
						|| !before.startsWith("-") && textFieldValue.getCursorPosition() == 0 && eventChar == '-'
						|| eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
						|| eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
						|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
					textFieldValue.textboxKeyTyped(enabled() ? eventChar : Keyboard.CHAR_NONE, eventKey);
				}

				if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-")) {
					try {
						long value = Long.parseLong(textFieldValue.getText().trim());
						isValidValue = value >= Integer.valueOf(configElement.getMinValue().toString())
								&& value <= Integer.valueOf(configElement.getMaxValue().toString());
					} catch (Throwable e) {
						isValidValue = false;
					}
				} else {
					isValidValue = false;
				}
			}
		}

		@Override
		public boolean isChanged() {
			try {
				return beforeValue != Integer.parseInt(textFieldValue.getText().trim());
			} catch (Throwable e) {
				return true;
			}
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				textFieldValue.setText(String.valueOf(beforeValue));
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled()) {
				if (isChanged() && isValidValue) {
					try {
						int value = Integer.parseInt(textFieldValue.getText().trim());
						configElement.set(value);
						return configElement.requiresMcRestart();
					} catch (Throwable e) {
						configElement.setToDefault();
					}
				} else if (isChanged() && !isValidValue) {
					try {
						int value = Integer.parseInt(textFieldValue.getText().trim());
						if (value < Integer.valueOf(configElement.getMinValue().toString())) {
							configElement.set(configElement.getMinValue());
						} else {
							configElement.set(configElement.getMaxValue());
						}

					} catch (Throwable e) {
						configElement.setToDefault();
					}
				}

				return configElement.requiresMcRestart()
						&& beforeValue != Integer.parseInt(configElement.get().toString());
			}
			return false;
		}
	}

	/**
	 * DoubleEntry
	 *
	 * Provides a GuiTextField for user input. Input is restricted to ensure the
	 * value can be parsed using Double.parseDouble().
	 */
	public static class DoubleEntry extends StringEntry {
		protected final double beforeValue;

		@SuppressWarnings("rawtypes")
		public DoubleEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeValue = Double.valueOf(configElement.get().toString());
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				String validChars = "0123456789";
				String before = textFieldValue.getText();
				if (validChars.contains(String.valueOf(eventChar))
						|| !before.startsWith("-") && textFieldValue.getCursorPosition() == 0 && eventChar == '-'
						|| !before.contains(".") && eventChar == '.' || eventKey == Keyboard.KEY_BACK
						|| eventKey == Keyboard.KEY_DELETE || eventKey == Keyboard.KEY_LEFT
						|| eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME
						|| eventKey == Keyboard.KEY_END) {
					textFieldValue.textboxKeyTyped(enabled() ? eventChar : Keyboard.CHAR_NONE, eventKey);
				}

				if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-")) {
					try {
						double value = Double.parseDouble(textFieldValue.getText().trim());
						isValidValue = !(value < Double.valueOf(configElement.getMinValue().toString()))
								&& !(value > Double.valueOf(configElement.getMaxValue().toString()));
					} catch (Throwable e) {
						isValidValue = false;
					}
				} else {
					isValidValue = false;
				}
			}
		}

		@Override
		public boolean isChanged() {
			try {
				return beforeValue != Double.parseDouble(textFieldValue.getText().trim());
			} catch (Throwable e) {
				return true;
			}
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				textFieldValue.setText(String.valueOf(beforeValue));
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled()) {
				if (isChanged() && isValidValue) {
					try {
						double value = Double.parseDouble(textFieldValue.getText().trim());
						configElement.set(value);
						return configElement.requiresMcRestart();
					} catch (Throwable e) {
						configElement.setToDefault();
					}
				} else if (isChanged() && !isValidValue) {
					try {
						double value = Double.parseDouble(textFieldValue.getText().trim());
						if (value < Double.valueOf(configElement.getMinValue().toString())) {
							configElement.set(configElement.getMinValue());
						} else {
							configElement.set(configElement.getMaxValue());
						}
					} catch (Throwable e) {
						configElement.setToDefault();
					}
				}

				return configElement.requiresMcRestart()
						&& beforeValue != Double.parseDouble(configElement.get().toString());
			}
			return false;
		}
	}

	/**
	 * StringEntry
	 *
	 * Provides a GuiTextField for user input.
	 */
	public static class StringEntry extends ListEntryBase {
		protected final GuiTextField textFieldValue;
		protected final String beforeValue;

		public StringEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<?> configElement) {
			super(owningScreen, owningEntryList, configElement);
			beforeValue = configElement.get().toString();
			textFieldValue = new GuiTextField(mc.fontRenderer, this.owningEntryList.controlX + 1, 0,
					this.owningEntryList.controlWidth - 3, 16);
			textFieldValue.setMaxStringLength(10000);
			textFieldValue.setText(configElement.get().toString());
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
			textFieldValue.xPosition = owningEntryList.controlX + 2;
			textFieldValue.yPosition = y + 1;
			textFieldValue.width = owningEntryList.controlWidth - 4;
			textFieldValue.setEnabled(enabled());
			textFieldValue.drawTextBox();
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT
					|| eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
				textFieldValue.textboxKeyTyped(enabled() ? eventChar : Keyboard.CHAR_NONE, eventKey);

				if (configElement.getValidationPattern() != null) {
					isValidValue = configElement.getValidationPattern().matcher(textFieldValue.getText().trim()).matches();
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
		public boolean isDefault() {
			return configElement.getDefault() != null
					? configElement.getDefault().toString().equals(textFieldValue.getText())
					: textFieldValue.getText().trim().isEmpty();
		}

		@Override
		public void setToDefault() {
			if (enabled()) {
				textFieldValue.setText(configElement.getDefault().toString());
				keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_HOME);
			}
		}

		@Override
		public boolean isChanged() {
			return beforeValue != null ? !beforeValue.equals(textFieldValue.getText())
					: textFieldValue.getText().trim().isEmpty();
		}

		@Override
		public void undoChanges() {
			if (enabled()) {
				textFieldValue.setText(beforeValue);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean saveConfigElement() {
			if (enabled()) {
				if (isChanged() && isValidValue) {
					configElement.set(textFieldValue.getText());
					return configElement.requiresMcRestart();
				} else if (isChanged() && !isValidValue) {
					configElement.setToDefault();
					return configElement.requiresMcRestart() && beforeValue != null
							? beforeValue.equals(configElement.getDefault())
							: configElement.getDefault() == null;
				}
			}
			return false;
		}

		@Override
		public Object getCurrentValue() {
			return textFieldValue.getText();
		}

		@Override
		public Object[] getCurrentValues() {
			return new Object[] { getCurrentValue() };
		}
	}

	/**
	 * CategoryEntry
	 *
	 * Provides an entry that consists of a GuiButton for navigating to the child
	 * category GuiConfig screen.
	 */
	public static class CategoryEntry extends ListEntryBase {
		protected GuiScreen childScreen;
		protected final GuiButtonExt btnSelectCategory;

		@SuppressWarnings("rawtypes")
		public CategoryEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);

			childScreen = buildChildScreen();

			btnSelectCategory = new GuiButtonExt(0, 0, 0, 300, 18, I18n.format(name));
			tooltipHoverChecker = new HoverChecker(btnSelectCategory, 800);

			drawLabel = false;
		}

		/**
		 * This method is called in the constructor and is used to set the childScreen
		 * field.
		 */
		@SuppressWarnings("unchecked")
		protected GuiScreen buildChildScreen() {
			return new GuiConfig(owningScreen, configElement.getChildElements(), owningScreen.modID,
					owningScreen.allRequireWorldRestart || configElement.requiresWorldRestart(),
					owningScreen.allRequireMcRestart || configElement.requiresMcRestart(), owningScreen.title,
					(owningScreen.titleLine2 == null ? "" : owningScreen.titleLine2) + " > " + name);
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			btnSelectCategory.xPosition = listWidth / 2 - 150;
			btnSelectCategory.yPosition = y;
			btnSelectCategory.enabled = enabled();
			btnSelectCategory.drawButton(mc, mouseX, mouseY);

			super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			boolean canHover = mouseY < owningScreen.entryList.bottom && mouseY > owningScreen.entryList.top;

			if (tooltipHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				owningScreen.drawToolTip(toolTip, mouseX, mouseY);
			}

			super.drawToolTip(mouseX, mouseY);
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (btnSelectCategory.mousePressed(mc, x, y)) {
				btnSelectCategory.func_146113_a(mc.getSoundHandler());
				Minecraft.getMinecraft().displayGuiScreen(childScreen);
				return true;
			} else
				return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			btnSelectCategory.mouseReleased(x, y);
		}

		@Override
		public boolean isDefault() {
			if (childScreen instanceof GuiConfig && ((GuiConfig) childScreen).entryList != null)
				return ((GuiConfig) childScreen).entryList.areAllEntriesDefault(true);

			return true;
		}

		@Override
		public void setToDefault() {
			if (childScreen instanceof GuiConfig && ((GuiConfig) childScreen).entryList != null) {
				((GuiConfig) childScreen).entryList.setAllToDefault(true);
			}
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
		public boolean saveConfigElement() {
			boolean requiresRestart = false;

			if (childScreen instanceof GuiConfig && ((GuiConfig) childScreen).entryList != null) {
				requiresRestart = configElement.requiresMcRestart()
						&& ((GuiConfig) childScreen).entryList.hasChangedEntry(true);

				if (((GuiConfig) childScreen).entryList.saveConfigElements()) {
					requiresRestart = true;
				}
			}

			return requiresRestart;
		}

		@Override
		public boolean isChanged() {
			if (childScreen instanceof GuiConfig && ((GuiConfig) childScreen).entryList != null)
				return ((GuiConfig) childScreen).entryList.hasChangedEntry(true);
			else
				return false;
		}

		@Override
		public void undoChanges() {
			if (childScreen instanceof GuiConfig && ((GuiConfig) childScreen).entryList != null) {
				((GuiConfig) childScreen).entryList.undoAllChanges(true);
			}
		}

		@Override
		public boolean enabled() {
			return true;
		}

		@Override
		public int getLabelWidth() {
			return 0;
		}

		@Override
		public int getEntryRightBound() {
			return owningEntryList.width / 2 + 155 + 22 + 18;
		}

		@Override
		public String getCurrentValue() {
			return "";
		}

		@Override
		public String[] getCurrentValues() {
			return new String[] { getCurrentValue() };
		}
	}

	/**
	 * ListEntryBase
	 *
	 * Provides a base entry for others to extend. Handles drawing the prop label
	 * (if drawLabel == true) and the Undo/Default buttons.
	 */
	@SuppressWarnings("rawtypes")
	public static abstract class ListEntryBase implements IConfigEntry {
		protected final GuiConfig owningScreen;
		protected final GuiConfigEntries owningEntryList;
		protected final IConfigElement configElement;
		protected final Minecraft mc;
		protected final String name;
		protected final GuiButtonExt btnUndoChanges;
		protected final GuiButtonExt btnDefault;
		protected List toolTip;
		protected List undoToolTip;
		protected List defaultToolTip;
		protected boolean isValidValue = true;
		protected HoverChecker tooltipHoverChecker;
		protected HoverChecker undoHoverChecker;
		protected HoverChecker defaultHoverChecker;
		protected boolean drawLabel;

		@SuppressWarnings({ "unchecked" })
		public ListEntryBase(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
			this.owningScreen = owningScreen;
			this.owningEntryList = owningEntryList;
			this.configElement = configElement;
			mc = Minecraft.getMinecraft();
			String trans = I18n.format(configElement.getLanguageKey());
			if (!trans.equals(configElement.getLanguageKey())) {
				name = trans;
			} else {
				name = configElement.getName();
			}
			btnUndoChanges = new GuiButtonExt(0, 0, 0, 18, 18, UNDO_CHAR);
			btnDefault = new GuiButtonExt(0, 0, 0, 18, 18, RESET_CHAR);

			undoHoverChecker = new HoverChecker(btnUndoChanges, 800);
			defaultHoverChecker = new HoverChecker(btnDefault, 800);
			undoToolTip = Arrays.asList(I18n.format("fml.configgui.tooltip.undoChanges"));
			defaultToolTip = Arrays.asList(I18n.format("fml.configgui.tooltip.resetToDefault"));

			drawLabel = true;

			String comment;

			comment = I18n.format(configElement.getLanguageKey() + ".tooltip").replace("\\n", "\n");

			if (!comment.equals(configElement.getLanguageKey() + ".tooltip")) {
				toolTip = new ArrayList<String>(mc.fontRenderer.listFormattedStringToWidth(
						EnumChatFormatting.GREEN + name + "\n" + EnumChatFormatting.YELLOW + comment, 300));
			} else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty()) {
				toolTip = new ArrayList<String>(mc.fontRenderer.listFormattedStringToWidth(
						EnumChatFormatting.GREEN + name + "\n" + EnumChatFormatting.YELLOW + configElement.getComment(),
						300));
			} else {
				toolTip = new ArrayList<String>(mc.fontRenderer.listFormattedStringToWidth(
						EnumChatFormatting.GREEN + name + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300));
			}

			if (configElement.getType() == ConfigGuiType.INTEGER
					&& (Integer.valueOf(configElement.getMinValue().toString()) != Integer.MIN_VALUE
							|| Integer.valueOf(configElement.getMaxValue().toString()) != Integer.MAX_VALUE)
					|| configElement.getType() == ConfigGuiType.DOUBLE
							&& (Double.valueOf(configElement.getMinValue().toString()) != -Double.MAX_VALUE
									|| Double.valueOf(configElement.getMaxValue().toString()) != Double.MAX_VALUE)) {
				toolTip.addAll(mc.fontRenderer.listFormattedStringToWidth(
						EnumChatFormatting.AQUA + I18n.format("fml.configgui.tooltip.defaultNumeric",
								configElement.getMinValue(), configElement.getMaxValue(), configElement.getDefault()),
						300));
			} else if (configElement.getType() != ConfigGuiType.CONFIG_CATEGORY) {
				toolTip.addAll(mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.AQUA
						+ I18n.format("fml.configgui.tooltip.default", configElement.getDefault()), 300));
			}

			if (configElement.requiresMcRestart() || owningScreen.allRequireMcRestart) {
				toolTip.add(EnumChatFormatting.RED + "[" + I18n.format("fml.configgui.gameRestartTitle") + "]");
			}
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator,
				int mouseX, int mouseY, boolean isSelected) {
			boolean isChanged = isChanged();

			if (drawLabel) {
				String label = (!isValidValue ? EnumChatFormatting.RED.toString()
						: isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString())
						+ (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + name;
				mc.fontRenderer.drawString(label, owningScreen.entryList.labelX,
						y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
			}

			btnUndoChanges.xPosition = owningEntryList.scrollBarX - 44;
			btnUndoChanges.yPosition = y;
			btnUndoChanges.enabled = enabled() && isChanged;
			btnUndoChanges.drawButton(mc, mouseX, mouseY);

			btnDefault.xPosition = owningEntryList.scrollBarX - 22;
			btnDefault.yPosition = y;
			btnDefault.enabled = enabled() && !isDefault();
			btnDefault.drawButton(mc, mouseX, mouseY);

			if (tooltipHoverChecker == null) {
				tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, owningScreen.entryList.controlX - 8, 800);
			} else {
				tooltipHoverChecker.updateBounds(y, y + slotHeight, x, owningScreen.entryList.controlX - 8);
			}
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			boolean canHover = mouseY < owningScreen.entryList.bottom && mouseY > owningScreen.entryList.top;
			if (toolTip != null && tooltipHoverChecker != null) {
				if (tooltipHoverChecker.checkHover(mouseX, mouseY, canHover)) {
					owningScreen.drawToolTip(toolTip, mouseX, mouseY);
				}
			}

			if (undoHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				owningScreen.drawToolTip(undoToolTip, mouseX, mouseY);
			}

			if (defaultHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				owningScreen.drawToolTip(defaultToolTip, mouseX, mouseY);
			}
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (btnDefault.mousePressed(mc, x, y)) {
				btnDefault.func_146113_a(mc.getSoundHandler());
				setToDefault();
				return true;
			} else if (btnUndoChanges.mousePressed(mc, x, y)) {
				btnUndoChanges.func_146113_a(mc.getSoundHandler());
				undoChanges();
				return true;
			}
			return false;
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			btnDefault.mouseReleased(x, y);
		}

		@Override
		public abstract boolean isDefault();

		@Override
		public abstract void setToDefault();

		@Override
		public abstract void keyTyped(char eventChar, int eventKey);

		@Override
		public abstract void updateCursorCounter();

		@Override
		public abstract void mouseClicked(int x, int y, int mouseEvent);

		@Override
		public abstract boolean isChanged();

		@Override
		public abstract void undoChanges();

		@Override
		public abstract boolean saveConfigElement();

		@Override
		public boolean enabled() {
			return !owningScreen.isWorldRunning || !owningScreen.allRequireWorldRestart && !configElement.requiresWorldRestart();
		}

		@Override
		public int getLabelWidth() {
			return mc.fontRenderer.getStringWidth(name);
		}

		@Override
		public int getEntryRightBound() {
			return owningEntryList.resetX + 40;
		}

		@Override
		public IConfigElement getConfigElement() {
			return configElement;
		}

		@Override
		public String getName() {
			return configElement.getName();
		}

		@Override
		public abstract Object getCurrentValue();

		@Override
		public abstract Object[] getCurrentValues();

		@Override
		public void onGuiClosed() {
		}
	}

	/**
	 * Provides an interface for defining GuiPropertyList.listEntry objects.
	 */
	public interface IConfigEntry<T> extends GuiListExtended.IGuiListEntry {
		/**
		 * Gets the IConfigElement object owned by this entry.
		 *
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		IConfigElement getConfigElement();

		/**
		 * Gets the name of the ConfigElement owned by this entry.
		 */
		String getName();

		/**
		 * Gets the current value of this entry as a String.
		 */
		T getCurrentValue();

		/**
		 * Gets the current values of this list entry as a String[].
		 */
		T[] getCurrentValues();

		/**
		 * Is this list entry enabled?
		 *
		 * @return true if this entry's controls should be enabled, false otherwise.
		 */
		boolean enabled();

		/**
		 * Handles user keystrokes for any GuiTextField objects in this entry. Call
		 * {@code GuiTextField.keyTyped()} for any GuiTextField objects that should
		 * receive the input provided.
		 */
		void keyTyped(char eventChar, int eventKey);

		/**
		 * Call {@code GuiTextField.updateCursorCounter()} for any GuiTextField objects
		 * in this entry.
		 */
		void updateCursorCounter();

		/**
		 * Call {@code GuiTextField.mouseClicked()} for and GuiTextField objects in this
		 * entry.
		 */
		void mouseClicked(int x, int y, int mouseEvent);

		/**
		 * Is this entry's value equal to the default value? Generally true should be
		 * returned if this entry is not a property or category entry.
		 *
		 * @return true if this entry's value is equal to this entry's default value.
		 */
		boolean isDefault();

		/**
		 * Sets this entry's value to the default value.
		 */
		void setToDefault();

		/**
		 * Handles reverting any changes that have occurred to this entry.
		 */
		void undoChanges();

		/**
		 * Has the value of this entry changed?
		 *
		 * @return true if changes have been made to this entry's value, false
		 *         otherwise.
		 */
		boolean isChanged();

		/**
		 * Handles saving any changes that have been made to this entry back to the
		 * underlying object. It is a good practice to check isChanged() before
		 * performing the save action. This method should return true if the element has
		 * changed AND REQUIRES A RESTART.
		 */
		boolean saveConfigElement();

		/**
		 * Handles drawing any tooltips that apply to this entry. This method is called
		 * after all other GUI elements have been drawn to the screen, so it could also
		 * be used to draw any GUI element that needs to be drawn after all entries have
		 * had drawEntry() called.
		 */
		void drawToolTip(int mouseX, int mouseY);

		/**
		 * Gets this entry's label width.
		 */
		int getLabelWidth();

		/**
		 * Gets this entry's right-hand x boundary. This value is used to control where
		 * the scroll bar is placed.
		 */
		int getEntryRightBound();

		/**
		 * This method is called when the parent GUI is closed. Most handlers won't need
		 * this; it is provided for special cases.
		 */
		void onGuiClosed();
	}
}