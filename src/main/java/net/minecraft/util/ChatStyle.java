package net.minecraft.util;

import com.google.gson.*;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;

import java.lang.reflect.Type;

public class ChatStyle {
	private ChatStyle parentStyle;
	private EnumChatFormatting color;
	private Boolean bold;
	private Boolean italic;
	private Boolean underlined;
	private Boolean strikethrough;
	private Boolean obfuscated;
	private ClickEvent chatClickEvent;
	private HoverEvent chatHoverEvent;
	private static final ChatStyle rootStyle = new ChatStyle() {
		private static final String __OBFID = "CL_00001267";

		@Override
		public EnumChatFormatting getColor() {
			return null;
		}

		@Override
		public boolean getBold() {
			return false;
		}

		@Override
		public boolean getItalic() {
			return false;
		}

		@Override
		public boolean getStrikethrough() {
			return false;
		}

		@Override
		public boolean getUnderlined() {
			return false;
		}

		@Override
		public boolean getObfuscated() {
			return false;
		}

		@Override
		public ClickEvent getChatClickEvent() {
			return null;
		}

		@Override
		public HoverEvent getChatHoverEvent() {
			return null;
		}

		@Override
		public ChatStyle setColor(EnumChatFormatting p_150238_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setBold(Boolean p_150227_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setItalic(Boolean p_150217_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setStrikethrough(Boolean p_150225_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setUnderlined(Boolean p_150228_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setObfuscated(Boolean p_150237_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setChatClickEvent(ClickEvent p_150241_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setChatHoverEvent(HoverEvent p_150209_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ChatStyle setParentStyle(ChatStyle p_150221_1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return "Style.ROOT";
		}

		@Override
		public ChatStyle createShallowCopy() {
			return this;
		}

		@Override
		public ChatStyle createDeepCopy() {
			return this;
		}

		// @SideOnly(Side.CLIENT)
		@Override
		public String getFormattingCode() {
			return "";
		}
	};
	private static final String __OBFID = "CL_00001266";

	public EnumChatFormatting getColor() {
		return color == null ? getParent().getColor() : color;
	}

	public boolean getBold() {
		return bold == null ? getParent().getBold() : bold.booleanValue();
	}

	public boolean getItalic() {
		return italic == null ? getParent().getItalic() : italic.booleanValue();
	}

	public boolean getStrikethrough() {
		return strikethrough == null ? getParent().getStrikethrough() : strikethrough.booleanValue();
	}

	public boolean getUnderlined() {
		return underlined == null ? getParent().getUnderlined() : underlined.booleanValue();
	}

	public boolean getObfuscated() {
		return obfuscated == null ? getParent().getObfuscated() : obfuscated.booleanValue();
	}

	public boolean isEmpty() {
		return bold == null && italic == null && strikethrough == null && underlined == null && obfuscated == null
				&& color == null && chatClickEvent == null && chatHoverEvent == null;
	}

	public ClickEvent getChatClickEvent() {
		return chatClickEvent == null ? getParent().getChatClickEvent() : chatClickEvent;
	}

	public HoverEvent getChatHoverEvent() {
		return chatHoverEvent == null ? getParent().getChatHoverEvent() : chatHoverEvent;
	}

	public ChatStyle setColor(EnumChatFormatting p_150238_1_) {
		color = p_150238_1_;
		return this;
	}

	public ChatStyle setBold(Boolean p_150227_1_) {
		bold = p_150227_1_;
		return this;
	}

	public ChatStyle setItalic(Boolean p_150217_1_) {
		italic = p_150217_1_;
		return this;
	}

	public ChatStyle setStrikethrough(Boolean p_150225_1_) {
		strikethrough = p_150225_1_;
		return this;
	}

	public ChatStyle setUnderlined(Boolean p_150228_1_) {
		underlined = p_150228_1_;
		return this;
	}

	public ChatStyle setObfuscated(Boolean p_150237_1_) {
		obfuscated = p_150237_1_;
		return this;
	}

	public ChatStyle setChatClickEvent(ClickEvent p_150241_1_) {
		chatClickEvent = p_150241_1_;
		return this;
	}

	public ChatStyle setChatHoverEvent(HoverEvent p_150209_1_) {
		chatHoverEvent = p_150209_1_;
		return this;
	}

	public ChatStyle setParentStyle(ChatStyle p_150221_1_) {
		parentStyle = p_150221_1_;
		return this;
	}

	// @SideOnly(Side.CLIENT)
	public String getFormattingCode() {
		if (isEmpty())
			return parentStyle != null ? parentStyle.getFormattingCode() : "";
		else {
			StringBuilder stringbuilder = new StringBuilder();

			if (getColor() != null) {
				stringbuilder.append(getColor());
			}

			if (getBold()) {
				stringbuilder.append(EnumChatFormatting.BOLD);
			}

			if (getItalic()) {
				stringbuilder.append(EnumChatFormatting.ITALIC);
			}

			if (getUnderlined()) {
				stringbuilder.append(EnumChatFormatting.UNDERLINE);
			}

			if (getObfuscated()) {
				stringbuilder.append(EnumChatFormatting.OBFUSCATED);
			}

			if (getStrikethrough()) {
				stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);
			}

			return stringbuilder.toString();
		}
	}

	private ChatStyle getParent() {
		return parentStyle == null ? rootStyle : parentStyle;
	}

	@Override
	public String toString() {
		return "Style{hasParent=" + (parentStyle != null) + ", color=" + color + ", bold=" + bold + ", italic=" + italic
				+ ", underlined=" + underlined + ", obfuscated=" + obfuscated + ", clickEvent=" + getChatClickEvent()
				+ ", hoverEvent=" + getChatHoverEvent() + '}';
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatStyle))
			return false;
		else {
			ChatStyle chatstyle = (ChatStyle) p_equals_1_;
			boolean flag;

			if (getBold() == chatstyle.getBold() && getColor() == chatstyle.getColor()
					&& getItalic() == chatstyle.getItalic() && getObfuscated() == chatstyle.getObfuscated()
					&& getStrikethrough() == chatstyle.getStrikethrough()
					&& getUnderlined() == chatstyle.getUnderlined()) {
				label56: {
					if (getChatClickEvent() != null) {
						if (!getChatClickEvent().equals(chatstyle.getChatClickEvent())) {
							break label56;
						}
					} else if (chatstyle.getChatClickEvent() != null) {
						break label56;
					}

					if (getChatHoverEvent() != null) {
						if (!getChatHoverEvent().equals(chatstyle.getChatHoverEvent())) {
							break label56;
						}
					} else if (chatstyle.getChatHoverEvent() != null) {
						break label56;
					}

					flag = true;
					return flag;
				}
			}

			flag = false;
			return flag;
		}
	}

	@Override
	public int hashCode() {
		int i = color.hashCode();
		i = 31 * i + bold.hashCode();
		i = 31 * i + italic.hashCode();
		i = 31 * i + underlined.hashCode();
		i = 31 * i + strikethrough.hashCode();
		i = 31 * i + obfuscated.hashCode();
		i = 31 * i + chatClickEvent.hashCode();
		i = 31 * i + chatHoverEvent.hashCode();
		return i;
	}

	public ChatStyle createShallowCopy() {
		ChatStyle chatstyle = new ChatStyle();
		chatstyle.bold = bold;
		chatstyle.italic = italic;
		chatstyle.strikethrough = strikethrough;
		chatstyle.underlined = underlined;
		chatstyle.obfuscated = obfuscated;
		chatstyle.color = color;
		chatstyle.chatClickEvent = chatClickEvent;
		chatstyle.chatHoverEvent = chatHoverEvent;
		chatstyle.parentStyle = parentStyle;
		return chatstyle;
	}

	public ChatStyle createDeepCopy() {
		ChatStyle chatstyle = new ChatStyle();
		chatstyle.setBold(Boolean.valueOf(getBold()));
		chatstyle.setItalic(Boolean.valueOf(getItalic()));
		chatstyle.setStrikethrough(Boolean.valueOf(getStrikethrough()));
		chatstyle.setUnderlined(Boolean.valueOf(getUnderlined()));
		chatstyle.setObfuscated(Boolean.valueOf(getObfuscated()));
		chatstyle.setColor(getColor());
		chatstyle.setChatClickEvent(getChatClickEvent());
		chatstyle.setChatHoverEvent(getChatHoverEvent());
		return chatstyle;
	}

	public static class Serializer implements JsonDeserializer, JsonSerializer {
		private static final String __OBFID = "CL_00001268";

		@Override
		public ChatStyle deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_,
				JsonDeserializationContext p_deserialize_3_) {
			if (p_deserialize_1_.isJsonObject()) {
				ChatStyle chatstyle = new ChatStyle();
				JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();

				if (jsonobject == null)
					return null;
				else {
					if (jsonobject.has("bold")) {
						chatstyle.bold = Boolean.valueOf(jsonobject.get("bold").getAsBoolean());
					}

					if (jsonobject.has("italic")) {
						chatstyle.italic = Boolean.valueOf(jsonobject.get("italic").getAsBoolean());
					}

					if (jsonobject.has("underlined")) {
						chatstyle.underlined = Boolean.valueOf(jsonobject.get("underlined").getAsBoolean());
					}

					if (jsonobject.has("strikethrough")) {
						chatstyle.strikethrough = Boolean.valueOf(jsonobject.get("strikethrough").getAsBoolean());
					}

					if (jsonobject.has("obfuscated")) {
						chatstyle.obfuscated = Boolean.valueOf(jsonobject.get("obfuscated").getAsBoolean());
					}

					if (jsonobject.has("color")) {
						chatstyle.color = (EnumChatFormatting) p_deserialize_3_.deserialize(jsonobject.get("color"),
								EnumChatFormatting.class);
					}

					JsonObject jsonobject1;
					JsonPrimitive jsonprimitive;

					if (jsonobject.has("clickEvent")) {
						jsonobject1 = jsonobject.getAsJsonObject("clickEvent");

						if (jsonobject1 != null) {
							jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
							ClickEvent.Action action = jsonprimitive == null ? null
									: ClickEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
							JsonPrimitive jsonprimitive1 = jsonobject1.getAsJsonPrimitive("value");
							String s = jsonprimitive1 == null ? null : jsonprimitive1.getAsString();

							if (action != null && s != null && action.shouldAllowInChat()) {
								chatstyle.chatClickEvent = new ClickEvent(action, s);
							}
						}
					}

					if (jsonobject.has("hoverEvent")) {
						jsonobject1 = jsonobject.getAsJsonObject("hoverEvent");

						if (jsonobject1 != null) {
							jsonprimitive = jsonobject1.getAsJsonPrimitive("action");
							HoverEvent.Action action1 = jsonprimitive == null ? null
									: HoverEvent.Action.getValueByCanonicalName(jsonprimitive.getAsString());
							IChatComponent ichatcomponent = (IChatComponent) p_deserialize_3_
									.deserialize(jsonobject1.get("value"), IChatComponent.class);

							if (action1 != null && ichatcomponent != null && action1.shouldAllowInChat()) {
								chatstyle.chatHoverEvent = new HoverEvent(action1, ichatcomponent);
							}
						}
					}

					return chatstyle;
				}
			} else
				return null;
		}

		public JsonElement serialize(ChatStyle p_serialize_1_, Type p_serialize_2_,
				JsonSerializationContext p_serialize_3_) {
			if (p_serialize_1_.isEmpty())
				return null;
			else {
				JsonObject jsonobject = new JsonObject();

				if (p_serialize_1_.bold != null) {
					jsonobject.addProperty("bold", p_serialize_1_.bold);
				}

				if (p_serialize_1_.italic != null) {
					jsonobject.addProperty("italic", p_serialize_1_.italic);
				}

				if (p_serialize_1_.underlined != null) {
					jsonobject.addProperty("underlined", p_serialize_1_.underlined);
				}

				if (p_serialize_1_.strikethrough != null) {
					jsonobject.addProperty("strikethrough", p_serialize_1_.strikethrough);
				}

				if (p_serialize_1_.obfuscated != null) {
					jsonobject.addProperty("obfuscated", p_serialize_1_.obfuscated);
				}

				if (p_serialize_1_.color != null) {
					jsonobject.add("color", p_serialize_3_.serialize(p_serialize_1_.color));
				}

				JsonObject jsonobject1;

				if (p_serialize_1_.chatClickEvent != null) {
					jsonobject1 = new JsonObject();
					jsonobject1.addProperty("action", p_serialize_1_.chatClickEvent.getAction().getCanonicalName());
					jsonobject1.addProperty("value", p_serialize_1_.chatClickEvent.getValue());
					jsonobject.add("clickEvent", jsonobject1);
				}

				if (p_serialize_1_.chatHoverEvent != null) {
					jsonobject1 = new JsonObject();
					jsonobject1.addProperty("action", p_serialize_1_.chatHoverEvent.getAction().getCanonicalName());
					jsonobject1.add("value", p_serialize_3_.serialize(p_serialize_1_.chatHoverEvent.getValue()));
					jsonobject.add("hoverEvent", jsonobject1);
				}

				return jsonobject;
			}
		}

		@Override
		public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_,
				JsonSerializationContext p_serialize_3_) {
			return this.serialize((ChatStyle) p_serialize_1_, p_serialize_2_, p_serialize_3_);
		}
	}
}
