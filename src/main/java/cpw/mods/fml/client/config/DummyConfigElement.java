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

import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.IArrayEntry;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class's main purpose is to provide the necessary objects for a sample
 * Config GUI for FML, although there may be practical uses for the objects
 * defined here such as using the DummyCategoryElement object as a wrapper for a
 * custom IGuiConfigListEntry object that opens a special screen.
 *
 * @author bspkrs
 */
public class DummyConfigElement<T> implements IConfigElement<T> {
	protected boolean isProperty = true;
	protected boolean isList = false;
	protected ConfigGuiType type;
	protected String name;
	protected String langKey;
	protected Object value;
	protected Object defaultValue;
	protected T[] values;
	protected T[] defaultValues;
	protected String[] validValues;
	protected Pattern validStringPattern;
	protected T minValue;
	protected T maxValue;
	protected boolean requiresWorldRestart = false;
	protected boolean requiresMcRestart = false;
	protected boolean isListFixedLength = false;
	protected int maxListLength = -1;
	@SuppressWarnings("rawtypes")
	protected List<IConfigElement> childElements;
	@SuppressWarnings("rawtypes")
	protected Class<? extends IConfigEntry> configEntryClass;
	protected Class<? extends IArrayEntry> arrayEntryClass;

	/**
	 * This class provides a Dummy Category IConfigElement. It can be used to define
	 * a custom list of GUI entries that will appear on the child screen or to
	 * specify a custom IGuiConfigListEntryfor a special category.
	 */
	public static class DummyCategoryElement<T> extends DummyConfigElement<T> {
		@SuppressWarnings("rawtypes")
		public DummyCategoryElement(String name, String langKey, List<IConfigElement> childElements) {
			this(name, langKey, childElements, null);
		}

		@SuppressWarnings("rawtypes")
		public DummyCategoryElement(String name, String langKey, Class<? extends IConfigEntry> customListEntryClass) {
			this(name, langKey, new ArrayList<IConfigElement>(), customListEntryClass);
		}

		@SuppressWarnings("rawtypes")
		public DummyCategoryElement(String name, String langKey, List<IConfigElement> childElements,
				Class<? extends IConfigEntry> customListEntryClass) {
			super(name, null, ConfigGuiType.CONFIG_CATEGORY, langKey);
			this.childElements = childElements;
			configEntryClass = customListEntryClass;
			isProperty = false;
		}
	}

	/**
	 * This class provides a dummy array-type IConfigElement.
	 */
	public static class DummyListElement<T> extends DummyConfigElement<T> {
		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				boolean isListFixedLength, int maxListLength, Pattern validStringPattern, T minValue, T maxValue) {
			super(name, null, type, langKey, minValue, maxValue);
			this.defaultValues = defaultValues;
			values = defaultValues;
			this.isListFixedLength = isListFixedLength;
			this.maxListLength = maxListLength;
			this.validStringPattern = validStringPattern;
			isList = true;
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey) {
			this(name, defaultValues, type, langKey, false, -1, null, null, null);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				boolean isListFixedLength) {
			this(name, defaultValues, type, langKey, isListFixedLength, -1, null, null, null);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey, int maxListLength) {
			this(name, defaultValues, type, langKey, false, maxListLength, null, null, null);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey, T minValue,
				T maxValue) {
			this(name, defaultValues, type, langKey, false, -1, null, minValue, maxValue);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				boolean isListFixedLength, T minValue, T maxValue) {
			this(name, defaultValues, type, langKey, isListFixedLength, -1, null, minValue, maxValue);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey, int maxListLength,
				T minValue, T maxValue) {
			this(name, defaultValues, type, langKey, false, maxListLength, null, minValue, maxValue);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				boolean isListFixedLength, int maxListLength, T minValue, T maxValue) {
			this(name, defaultValues, type, langKey, isListFixedLength, maxListLength, null, minValue,
					maxValue);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				Pattern validStringPattern) {
			this(name, defaultValues, type, langKey, false, -1, validStringPattern, null, null);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey,
				boolean isListFixedLength, Pattern validStringPattern) {
			this(name, defaultValues, type, langKey, isListFixedLength, -1, validStringPattern, null, null);
		}

		public DummyListElement(String name, T[] defaultValues, ConfigGuiType type, String langKey, int maxListLength,
				Pattern validStringPattern) {
			this(name, defaultValues, type, langKey, false, maxListLength, validStringPattern, null, null);
		}

		@SuppressWarnings("rawtypes")
		public DummyListElement setCustomEditListEntryClass(Class<? extends IArrayEntry> clazz) {
			arrayEntryClass = clazz;
			return this;
		}

		@Override
		public Object getDefault() {
			return Arrays.toString(defaultValues);
		}
	}

	@SuppressWarnings("unchecked")
	public DummyConfigElement(String name, T defaultValue, ConfigGuiType type, String langKey, String[] validValues,
			Pattern validStringPattern, T minValue, T maxValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
		this.type = type;
		this.langKey = langKey;
		this.validValues = validValues;
		this.validStringPattern = validStringPattern;
		if (minValue == null) {
			if (type == ConfigGuiType.INTEGER) {
				this.minValue = (T) (Integer) Integer.MIN_VALUE;
			} else if (type == ConfigGuiType.DOUBLE) {
				this.minValue = (T) (Double)  Double.MIN_VALUE;
			}
		} else {
			this.minValue = minValue;
		}
		if (maxValue == null) {
			if (type == ConfigGuiType.INTEGER) {
				this.maxValue = (T) (Integer) Integer.MAX_VALUE;
			} else if (type == ConfigGuiType.DOUBLE) {
				this.maxValue = (T) (Double) Double.MAX_VALUE;
			}
		} else {
			this.maxValue = maxValue;
		}
	}

	public DummyConfigElement(String name, T defaultValue, ConfigGuiType type, String langKey,
			Pattern validStringPattern) {
		this(name, defaultValue, type, langKey, null, validStringPattern, null, null);
	}

	public DummyConfigElement(String name, T defaultValue, ConfigGuiType type, String langKey, String[] validValues) {
		this(name, defaultValue, type, langKey, validValues, null, null, null);
	}

	public DummyConfigElement(String name, T defaultValue, ConfigGuiType type, String langKey) {
		this(name, defaultValue, type, langKey, null, null, null, null);
	}

	public DummyConfigElement(String name, T defaultValue, ConfigGuiType type, String langKey, T minValue, T maxValue) {
		this(name, defaultValue, type, langKey, null, null, minValue, maxValue);
	}

	@SuppressWarnings("rawtypes")
	public DummyConfigElement setCustomListEntryClass(Class<? extends IConfigEntry> clazz) {
		this.configEntryClass = clazz;
		return this;
	}

	@Override
	public boolean isProperty() {
		return isProperty;
	}

	@SuppressWarnings("rawtypes")
	public IConfigElement setConfigEntryClass(Class<? extends IConfigEntry> clazz) {
		this.configEntryClass = clazz;
		return this;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return configEntryClass;
	}

	@SuppressWarnings("rawtypes")
	public IConfigElement setArrayEntryClass(Class<? extends IArrayEntry> clazz) {
		this.arrayEntryClass = clazz;
		return this;
	}

	@Override
	public Class<? extends IArrayEntry> getArrayEntryClass() {
		return arrayEntryClass;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getQualifiedName() {
		return name;
	}

	@Override
	public String getLanguageKey() {
		return langKey;
	}

	@Override
	public String getComment() {
		return I18n.format(langKey + ".tooltip");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<IConfigElement> getChildElements() {
		return childElements;
	}

	@Override
	public ConfigGuiType getType() {
		return type;
	}

	@Override
	public boolean isList() {
		return isList;
	}

	@Override
	public boolean isListLengthFixed() {
		return this.isListFixedLength;
	}

	@Override
	public int getMaxListLength() {
		return this.maxListLength;
	}

	@Override
	public boolean isDefault() {
		if (isProperty) {
			if (!isList) {
				if (value != null)
					return value.equals(defaultValue);
				else
					return defaultValue == null;
			} else
				return Arrays.deepEquals(values, defaultValues);
		}

		return true;
	}

	@Override
	public Object getDefault() {
		return defaultValue;
	}

	@Override
	public T[] getDefaults() {
		return defaultValues;
	}

	@Override
	public void setToDefault() {
		if (isList) {
			this.values = Arrays.copyOf(this.defaultValues, this.defaultValues.length);
		} else {
			this.value = defaultValue;
		}
	}

	public IConfigElement<T> setRequiresWorldRestart(boolean requiresWorldRestart) {
		this.requiresWorldRestart = requiresWorldRestart;
		return this;
	}

	@Override
	public boolean requiresWorldRestart() {
		return requiresWorldRestart;
	}

	@Override
	public boolean showInGui() {
		return true;
	}

	public IConfigElement<T> setRequiresMcRestart(boolean requiresMcRestart) {
		this.requiresMcRestart = this.requiresWorldRestart = requiresMcRestart;
		return this;
	}

	@Override
	public boolean requiresMcRestart() {
		return requiresMcRestart;
	}

	@Override
	public String[] getValidValues() {
		return validValues;
	}

	@Override
	public Pattern getValidationPattern() {
		return validStringPattern;
	}

	@Override
	public Object get() {
		return value;
	}

	@Override
	public T[] getList() {
		return values;
	}

	@Override
	public void set(T value) {
		defaultValue = value;
	}

	@Override
	public void set(T[] aVal) {
		defaultValues = aVal;
	}

	@Override
	public T getMinValue() {
		return minValue;
	}

	@Override
	public T getMaxValue() {
		return maxValue;
	}
}