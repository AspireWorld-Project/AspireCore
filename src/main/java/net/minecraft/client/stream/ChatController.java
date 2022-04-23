package net.minecraft.client.stream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.chat.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ChatController implements IChatCallbacks {
	private static final Logger field_153018_p = LogManager.getLogger();
	protected ChatController.ChatListener field_153003_a = null;
	protected String field_153004_b = "";
	protected String field_153005_c = "";
	protected String field_153006_d = "";
	protected String field_153007_e = "";
	protected Chat field_153008_f = null;
	protected boolean field_153009_g = false;
	protected boolean field_153010_h = false;
	protected ChatController.ChatState field_153011_i;
	protected AuthToken field_153012_j;
	protected List field_153013_k;
	protected LinkedList field_153014_l;
	protected int field_153015_m;
	protected boolean field_153016_n;
	protected boolean field_153017_o;
	private static final String __OBFID = "CL_00001819";

	@Override
	public void chatStatusCallback(ErrorCode p_chatStatusCallback_1_) {
		if (!ErrorCode.succeeded(p_chatStatusCallback_1_)) {
			field_153011_i = ChatController.ChatState.Disconnected;
		}
	}

	@Override
	public void chatChannelMembershipCallback(ChatEvent p_chatChannelMembershipCallback_1_,
			ChatChannelInfo p_chatChannelMembershipCallback_2_) {
		switch (ChatController.SwitchChatState.field_152982_a[p_chatChannelMembershipCallback_1_.ordinal()]) {
		case 1:
			field_153011_i = ChatController.ChatState.Connected;
			func_152999_p();
			break;
		case 2:
			field_153011_i = ChatController.ChatState.Disconnected;
		}
	}

	@Override
	public void chatChannelUserChangeCallback(ChatUserList p_chatChannelUserChangeCallback_1_,
			ChatUserList p_chatChannelUserChangeCallback_2_, ChatUserList p_chatChannelUserChangeCallback_3_) {
		int i;
		int j;

		for (i = 0; i < p_chatChannelUserChangeCallback_2_.userList.length; ++i) {
			j = field_153013_k.indexOf(p_chatChannelUserChangeCallback_2_.userList[i]);

			if (j >= 0) {
				field_153013_k.remove(j);
			}
		}

		for (i = 0; i < p_chatChannelUserChangeCallback_3_.userList.length; ++i) {
			j = field_153013_k.indexOf(p_chatChannelUserChangeCallback_3_.userList[i]);

			if (j >= 0) {
				field_153013_k.remove(j);
			}

			field_153013_k.add(p_chatChannelUserChangeCallback_3_.userList[i]);
		}

		for (i = 0; i < p_chatChannelUserChangeCallback_1_.userList.length; ++i) {
			field_153013_k.add(p_chatChannelUserChangeCallback_1_.userList[i]);
		}

		try {
			if (field_153003_a != null) {
				field_153003_a.func_152904_a(p_chatChannelUserChangeCallback_1_.userList,
						p_chatChannelUserChangeCallback_2_.userList, p_chatChannelUserChangeCallback_3_.userList);
			}
		} catch (Exception exception) {
			func_152995_h(exception.toString());
		}
	}

	@Override
	public void chatQueryChannelUsersCallback(ChatUserList p_chatQueryChannelUsersCallback_1_) {
	}

	@Override
	public void chatChannelMessageCallback(ChatMessageList p_chatChannelMessageCallback_1_) {
		for (int i = 0; i < p_chatChannelMessageCallback_1_.messageList.length; ++i) {
			field_153014_l.addLast(p_chatChannelMessageCallback_1_.messageList[i]);
		}

		try {
			if (field_153003_a != null) {
				field_153003_a.func_152903_a(p_chatChannelMessageCallback_1_.messageList);
			}
		} catch (Exception exception) {
			func_152995_h(exception.toString());
		}

		while (field_153014_l.size() > field_153015_m) {
			field_153014_l.removeFirst();
		}
	}

	@Override
	public void chatClearCallback(String p_chatClearCallback_1_) {
		func_152987_o();
	}

	@Override
	public void emoticonDataDownloadCallback(ErrorCode p_emoticonDataDownloadCallback_1_) {
		if (ErrorCode.succeeded(p_emoticonDataDownloadCallback_1_)) {
			func_152988_s();
		}
	}

	@Override
	public void chatChannelTokenizedMessageCallback(ChatTokenizedMessage[] p_chatChannelTokenizedMessageCallback_1_) {
	}

	public void func_152990_a(ChatController.ChatListener p_152990_1_) {
		field_153003_a = p_152990_1_;
	}

	public boolean func_152991_c() {
		return field_153011_i == ChatController.ChatState.Connected;
	}

	public void func_152994_a(AuthToken p_152994_1_) {
		field_153012_j = p_152994_1_;
	}

	public void func_152984_a(String p_152984_1_) {
		field_153006_d = p_152984_1_;
	}

	public void func_152998_c(String p_152998_1_) {
		field_153004_b = p_152998_1_;
	}

	public ChatController.ChatState func_153000_j() {
		return field_153011_i;
	}

	public ChatController() {
		field_153011_i = ChatController.ChatState.Uninitialized;
		field_153012_j = new AuthToken();
		field_153013_k = new ArrayList();
		field_153014_l = new LinkedList();
		field_153015_m = 128;
		field_153016_n = false;
		field_153017_o = false;
		field_153008_f = new Chat(new StandardChatAPI());
	}

	public boolean func_152986_d(String p_152986_1_) {
		func_153002_l();
		field_153010_h = false;
		field_153005_c = p_152986_1_;
		return func_152985_f(p_152986_1_);
	}

	public boolean func_153002_l() {
		if (field_153011_i != ChatController.ChatState.Connected
				&& field_153011_i != ChatController.ChatState.Connecting) {
			if (field_153011_i == ChatController.ChatState.Disconnected) {
				func_152989_q();
			}
		} else {
			ErrorCode errorcode = field_153008_f.disconnect();

			if (ErrorCode.failed(errorcode)) {
				String s = ErrorCode.getString(errorcode);
				func_152995_h(String.format("Error disconnecting: %s", s));
				return false;
			}

			func_152989_q();
		}

		return func_152993_m();
	}

	protected boolean func_152985_f(String p_152985_1_) {
		if (field_153009_g)
			return false;
		else {
			ErrorCode errorcode = field_153008_f.initialize(p_152985_1_, false);

			if (ErrorCode.failed(errorcode)) {
				String s1 = ErrorCode.getString(errorcode);
				func_152995_h(String.format("Error initializing chat: %s", s1));
				func_152989_q();
				return false;
			} else {
				field_153009_g = true;
				field_153008_f.setChatCallbacks(this);
				field_153011_i = ChatController.ChatState.Initialized;
				return true;
			}
		}
	}

	protected boolean func_152993_m() {
		if (field_153009_g) {
			ErrorCode errorcode = field_153008_f.shutdown();

			if (ErrorCode.failed(errorcode)) {
				String s = ErrorCode.getString(errorcode);
				func_152995_h(String.format("Error shutting down chat: %s", s));
				return false;
			}
		}

		field_153011_i = ChatController.ChatState.Uninitialized;
		field_153009_g = false;
		func_152996_t();
		field_153008_f.setChatCallbacks(null);
		return true;
	}

	public void func_152997_n() {
		if (field_153009_g) {
			ErrorCode errorcode = field_153008_f.flushEvents();
			String s;

			if (ErrorCode.failed(errorcode)) {
				s = ErrorCode.getString(errorcode);
				func_152995_h(String.format("Error flushing chat events: %s", s));
			}

			switch (ChatController.SwitchChatState.field_152983_b[field_153011_i.ordinal()]) {
			case 1:
			case 3:
			case 4:
			default:
				break;
			case 2:
				if (field_153010_h) {
					errorcode = field_153008_f.connectAnonymous();
				} else {
					errorcode = field_153008_f.connect(field_153005_c, field_153012_j.data);
				}

				if (ErrorCode.failed(errorcode)) {
					s = ErrorCode.getString(errorcode);
					func_152995_h(String.format("Error connecting: %s", s));
					func_152993_m();
					func_152989_q();
				} else {
					field_153011_i = ChatController.ChatState.Connecting;
					func_153001_r();
				}

				break;
			case 5:
				func_153002_l();
			}
		}
	}

	public boolean func_152992_g(String p_152992_1_) {
		if (field_153011_i != ChatController.ChatState.Connected)
			return false;
		else {
			ErrorCode errorcode = field_153008_f.sendMessage(p_152992_1_);

			if (ErrorCode.failed(errorcode)) {
				String s1 = ErrorCode.getString(errorcode);
				func_152995_h(String.format("Error sending chat message: %s", s1));
				return false;
			} else
				return true;
		}
	}

	public void func_152987_o() {
		field_153014_l.clear();

		try {
			if (field_153003_a != null) {
				field_153003_a.func_152902_f();
			}
		} catch (Exception exception) {
			func_152995_h(exception.toString());
		}
	}

	protected void func_152999_p() {
		try {
			if (field_153003_a != null) {
				field_153003_a.func_152906_d();
			}
		} catch (Exception exception) {
			func_152995_h(exception.toString());
		}
	}

	protected void func_152989_q() {
		try {
			if (field_153003_a != null) {
				field_153003_a.func_152905_e();
			}
		} catch (Exception exception) {
			func_152995_h(exception.toString());
		}
	}

	protected void func_153001_r() {
	}

	protected void func_152988_s() {
	}

	protected void func_152996_t() {
	}

	protected void func_152995_h(String p_152995_1_) {
		field_153018_p.error(TwitchStream.field_152949_a, "[Chat controller] {}", p_152995_1_);
	}

	@SideOnly(Side.CLIENT)
	public interface ChatListener {
		void func_152903_a(ChatMessage[] p_152903_1_);

		void func_152904_a(ChatUserInfo[] p_152904_1_, ChatUserInfo[] p_152904_2_, ChatUserInfo[] p_152904_3_);

		void func_152906_d();

		void func_152905_e();

		void func_152902_f();
	}

	@SideOnly(Side.CLIENT)
	public enum ChatState {
		Uninitialized, Initialized, Connecting, Connected, Disconnected;

		private static final String __OBFID = "CL_00001817";
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchChatState {
		static final int[] field_152982_a;

		static final int[] field_152983_b = new int[ChatController.ChatState.values().length];
		private static final String __OBFID = "CL_00001818";

		static {
			try {
				field_152983_b[ChatController.ChatState.Uninitialized.ordinal()] = 1;
			} catch (NoSuchFieldError var7) {
			}

			try {
				field_152983_b[ChatController.ChatState.Initialized.ordinal()] = 2;
			} catch (NoSuchFieldError var6) {
			}

			try {
				field_152983_b[ChatController.ChatState.Connecting.ordinal()] = 3;
			} catch (NoSuchFieldError var5) {
			}

			try {
				field_152983_b[ChatController.ChatState.Connected.ordinal()] = 4;
			} catch (NoSuchFieldError var4) {
			}

			try {
				field_152983_b[ChatController.ChatState.Disconnected.ordinal()] = 5;
			} catch (NoSuchFieldError var3) {
			}

			field_152982_a = new int[ChatEvent.values().length];

			try {
				field_152982_a[ChatEvent.TTV_CHAT_JOINED_CHANNEL.ordinal()] = 1;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_152982_a[ChatEvent.TTV_CHAT_LEFT_CHANNEL.ordinal()] = 2;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}