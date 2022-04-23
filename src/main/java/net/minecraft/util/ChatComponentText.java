package net.minecraft.util;

import java.util.Iterator;

public class ChatComponentText extends ChatComponentStyle {
	private final String text;
	public ChatComponentText(String p_i45159_1_) {
		text = p_i45159_1_;
	}

	public String getChatComponentText_TextValue() {
		return text;
	}

	@Override
	public String getUnformattedTextForChat() {
		return text;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ChatComponentText createCopy() {
		ChatComponentText chatcomponenttext = new ChatComponentText(text);
		chatcomponenttext.setChatStyle(getChatStyle().createShallowCopy());
		Iterator iterator = getSiblings().iterator();

		while (iterator.hasNext()) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			chatcomponenttext.appendSibling(ichatcomponent.createCopy());
		}

		return chatcomponenttext;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatComponentText))
			return false;
		else {
			ChatComponentText chatcomponenttext = (ChatComponentText) p_equals_1_;
			return text.equals(chatcomponenttext.getChatComponentText_TextValue()) && super.equals(p_equals_1_);
		}
	}

	@Override
	public String toString() {
		return "TextComponent{text='" + text + '\'' + ", siblings=" + siblings + ", style=" + getChatStyle() + '}';
	}
}