package net.minecraft.event;

import com.google.common.collect.Maps;
import net.minecraft.util.IChatComponent;

import java.util.Map;

public class HoverEvent {
	private final HoverEvent.Action action;
	private final IChatComponent value;
	private static final String __OBFID = "CL_00001264";

	public HoverEvent(HoverEvent.Action p_i45158_1_, IChatComponent p_i45158_2_) {
		action = p_i45158_1_;
		value = p_i45158_2_;
	}

	public HoverEvent.Action getAction() {
		return action;
	}

	public IChatComponent getValue() {
		return value;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
			HoverEvent hoverevent = (HoverEvent) p_equals_1_;

			if (action != hoverevent.action)
				return false;
			else {
				if (value != null) {
                    return value.equals(hoverevent.value);
				} else return hoverevent.value == null;
            }
		} else
			return false;
	}

	@Override
	public String toString() {
		return "HoverEvent{action=" + action + ", value='" + value + '\'' + '}';
	}

	@Override
	public int hashCode() {
		int i = action.hashCode();
		i = 31 * i + (value != null ? value.hashCode() : 0);
		return i;
	}

	public enum Action {
		SHOW_TEXT("show_text", true), SHOW_ACHIEVEMENT("show_achievement", true), SHOW_ITEM("show_item", true);
		private static final Map nameMapping = Maps.newHashMap();
		private final boolean allowedInChat;
		private final String canonicalName;

		private static final String __OBFID = "CL_00001265";

		Action(String p_i45157_3_, boolean p_i45157_4_) {
			canonicalName = p_i45157_3_;
			allowedInChat = p_i45157_4_;
		}

		public boolean shouldAllowInChat() {
			return allowedInChat;
		}

		public String getCanonicalName() {
			return canonicalName;
		}

		public static HoverEvent.Action getValueByCanonicalName(String p_150684_0_) {
			return (HoverEvent.Action) nameMapping.get(p_150684_0_);
		}

		static {
			HoverEvent.Action[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				HoverEvent.Action var3 = var0[var2];
				nameMapping.put(var3.getCanonicalName(), var3);
			}
		}
	}
}