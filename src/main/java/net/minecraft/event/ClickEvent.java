package net.minecraft.event;

import com.google.common.collect.Maps;

import java.util.Map;

public class ClickEvent {
	private final ClickEvent.Action action;
	private final String value;
	private static final String __OBFID = "CL_00001260";

	public ClickEvent(ClickEvent.Action p_i45156_1_, String p_i45156_2_) {
		action = p_i45156_1_;
		value = p_i45156_2_;
	}

	public ClickEvent.Action getAction() {
		return action;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
			ClickEvent clickevent = (ClickEvent) p_equals_1_;

			if (action != clickevent.action)
				return false;
			else {
				if (value != null) {
					if (!value.equals(clickevent.value))
						return false;
				} else if (clickevent.value != null)
					return false;

				return true;
			}
		} else
			return false;
	}

	@Override
	public String toString() {
		return "ClickEvent{action=" + action + ", value=\'" + value + '\'' + '}';
	}

	@Override
	public int hashCode() {
		int i = action.hashCode();
		i = 31 * i + (value != null ? value.hashCode() : 0);
		return i;
	}

	public static enum Action {
		OPEN_URL("open_url", true), OPEN_FILE("open_file", false), RUN_COMMAND("run_command",
				true), TWITCH_USER_INFO("twitch_user_info", false), SUGGEST_COMMAND("suggest_command", true);
		private static final Map nameMapping = Maps.newHashMap();
		private final boolean allowedInChat;
		private final String canonicalName;

		private static final String __OBFID = "CL_00001261";

		private Action(String p_i45155_3_, boolean p_i45155_4_) {
			canonicalName = p_i45155_3_;
			allowedInChat = p_i45155_4_;
		}

		public boolean shouldAllowInChat() {
			return allowedInChat;
		}

		public String getCanonicalName() {
			return canonicalName;
		}

		public static ClickEvent.Action getValueByCanonicalName(String p_150672_0_) {
			return (ClickEvent.Action) nameMapping.get(p_150672_0_);
		}

		static {
			ClickEvent.Action[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				ClickEvent.Action var3 = var0[var2];
				nameMapping.put(var3.getCanonicalName(), var3);
			}
		}
	}
}