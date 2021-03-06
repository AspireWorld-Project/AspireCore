package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public enum EnumChatFormatting {
	BLACK('0'), DARK_BLUE('1'), DARK_GREEN('2'), DARK_AQUA('3'), DARK_RED('4'), DARK_PURPLE('5'), GOLD('6'), GRAY(
			'7'), DARK_GRAY('8'), BLUE('9'), GREEN('a'), AQUA('b'), RED('c'), LIGHT_PURPLE('d'), YELLOW('e'), WHITE(
					'f'), OBFUSCATED('k', true), BOLD('l',
							true), STRIKETHROUGH('m', true), UNDERLINE('n', true), ITALIC('o', true), RESET('r');
	@SuppressWarnings("rawtypes")
	private static final Map formattingCodeMapping = new HashMap();
	@SuppressWarnings("rawtypes")
	private static final Map nameMapping = new HashMap();
	private static final Pattern formattingCodePattern = Pattern
			.compile("(?i)" + '\u00a7' + "[0-9A-FK-OR]");
	private final char formattingCode;
	private final boolean fancyStyling;
	private final String controlString;

	EnumChatFormatting(char p_i1336_3_) {
		this(p_i1336_3_, false);
	}

	EnumChatFormatting(char p_i1337_3_, boolean p_i1337_4_) {
		formattingCode = p_i1337_3_;
		fancyStyling = p_i1337_4_;
		controlString = "\u00a7" + p_i1337_3_;
	}

	public char getFormattingCode() {
		return formattingCode;
	}

	public boolean isFancyStyling() {
		return fancyStyling;
	}

	public boolean isColor() {
		return !fancyStyling && this != RESET;
	}

	public String getFriendlyName() {
		return name().toLowerCase();
	}

	@Override
	public String toString() {
		return controlString;
	}

	@SideOnly(Side.CLIENT)
	public static String getTextWithoutFormattingCodes(String p_110646_0_) {
		return p_110646_0_ == null ? null : formattingCodePattern.matcher(p_110646_0_).replaceAll("");
	}

	public static EnumChatFormatting getValueByName(String p_96300_0_) {
		return p_96300_0_ == null ? null : (EnumChatFormatting) nameMapping.get(p_96300_0_.toLowerCase());
	}

	@SuppressWarnings({ "rawtypes" })
	public static Collection getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
		ArrayList arraylist = new ArrayList();
		EnumChatFormatting[] aenumchatformatting = values();
		int i = aenumchatformatting.length;

		for (int j = 0; j < i; ++j) {
			EnumChatFormatting enumchatformatting = aenumchatformatting[j];

			if ((!enumchatformatting.isColor() || p_96296_0_) && (!enumchatformatting.isFancyStyling() || p_96296_1_)) {
				arraylist.add(enumchatformatting.getFriendlyName());
			}
		}

		return arraylist;
	}

	static {
		EnumChatFormatting[] var0 = values();
		int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			EnumChatFormatting var3 = var0[var2];
			formattingCodeMapping.put(Character.valueOf(var3.getFormattingCode()), var3);
			nameMapping.put(var3.getFriendlyName(), var3);
		}
	}

	public static EnumChatFormatting getByColorCode(char code) {
		return (EnumChatFormatting) formattingCodeMapping.get(code);
	}
}
