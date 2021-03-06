package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiTwitchUserMode extends GuiScreen {
	private static final EnumChatFormatting field_152331_a = EnumChatFormatting.DARK_GREEN;
	private static final EnumChatFormatting field_152335_f = EnumChatFormatting.RED;
	private static final EnumChatFormatting field_152336_g = EnumChatFormatting.DARK_PURPLE;
	private final ChatUserInfo field_152337_h;
	private final IChatComponent field_152338_i;
	@SuppressWarnings("rawtypes")
	private final List field_152332_r = Lists.newArrayList();
	private final IStream field_152333_s;
	private int field_152334_t;
	@SuppressWarnings("unchecked")
	public GuiTwitchUserMode(IStream p_i1064_1_, ChatUserInfo p_i1064_2_) {
		field_152333_s = p_i1064_1_;
		field_152337_h = p_i1064_2_;
		field_152338_i = new ChatComponentText(p_i1064_2_.displayName);
		field_152332_r.addAll(func_152328_a(p_i1064_2_.modes, p_i1064_2_.subscriptions, p_i1064_1_));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List func_152328_a(Set p_152328_0_, Set p_152328_1_, IStream p_152328_2_) {
		String s = p_152328_2_ == null ? null : p_152328_2_.func_152921_C();
		boolean flag = p_152328_2_ != null && p_152328_2_.func_152927_B();
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = p_152328_0_.iterator();
		IChatComponent ichatcomponent;
		ChatComponentText chatcomponenttext;

		while (iterator.hasNext()) {
			ChatUserMode chatusermode = (ChatUserMode) iterator.next();
			ichatcomponent = func_152329_a(chatusermode, s, flag);

			if (ichatcomponent != null) {
				chatcomponenttext = new ChatComponentText("- ");
				chatcomponenttext.appendSibling(ichatcomponent);
				arraylist.add(chatcomponenttext);
			}
		}

		iterator = p_152328_1_.iterator();

		while (iterator.hasNext()) {
			ChatUserSubscription chatusersubscription = (ChatUserSubscription) iterator.next();
			ichatcomponent = func_152330_a(chatusersubscription, s, flag);

			if (ichatcomponent != null) {
				chatcomponenttext = new ChatComponentText("- ");
				chatcomponenttext.appendSibling(ichatcomponent);
				arraylist.add(chatcomponenttext);
			}
		}

		return arraylist;
	}

	public static IChatComponent func_152330_a(ChatUserSubscription p_152330_0_, String p_152330_1_,
			boolean p_152330_2_) {
		ChatComponentTranslation chatcomponenttranslation = null;

		if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
			if (p_152330_1_ == null) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.subscription.subscriber"
				);
			} else if (p_152330_2_) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.subscription.subscriber.self"
				);
			} else {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.subscription.subscriber.other",
						p_152330_1_);
			}

			chatcomponenttranslation.getChatStyle().setColor(field_152331_a);
		} else if (p_152330_0_ == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
			chatcomponenttranslation = new ChatComponentTranslation("stream.user.subscription.turbo");
			chatcomponenttranslation.getChatStyle().setColor(field_152336_g);
		}

		return chatcomponenttranslation;
	}

	public static IChatComponent func_152329_a(ChatUserMode p_152329_0_, String p_152329_1_, boolean p_152329_2_) {
		ChatComponentTranslation chatcomponenttranslation = null;

		if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
			chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.administrator");
			chatcomponenttranslation.getChatStyle().setColor(field_152336_g);
		} else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
			if (p_152329_1_ == null) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.banned");
			} else if (p_152329_2_) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.banned.self");
			} else {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.banned.other",
						p_152329_1_);
			}

			chatcomponenttranslation.getChatStyle().setColor(field_152335_f);
		} else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
			if (p_152329_1_ == null) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.broadcaster");
			} else if (p_152329_2_) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.broadcaster.self"
				);
			} else {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.broadcaster.other"
				);
			}

			chatcomponenttranslation.getChatStyle().setColor(field_152331_a);
		} else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
			if (p_152329_1_ == null) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.moderator");
			} else if (p_152329_2_) {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.moderator.self"
				);
			} else {
				chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.moderator.other",
						p_152329_1_);
			}

			chatcomponenttranslation.getChatStyle().setColor(field_152331_a);
		} else if (p_152329_0_ == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
			chatcomponenttranslation = new ChatComponentTranslation("stream.user.mode.staff");
			chatcomponenttranslation.getChatStyle().setColor(field_152336_g);
		}

		return chatcomponenttranslation;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initGui() {
		int i = width / 3;
		int j = i - 130;
		buttonList.add(new GuiButton(1, i * 0 + j / 2, height - 70, 130, 20,
				I18n.format("stream.userinfo.timeout")));
		buttonList.add(new GuiButton(0, i * 1 + j / 2, height - 70, 130, 20,
				I18n.format("stream.userinfo.ban")));
		buttonList.add(new GuiButton(2, i * 2 + j / 2, height - 70, 130, 20,
				I18n.format("stream.userinfo.mod")));
		buttonList.add(new GuiButton(5, i * 0 + j / 2, height - 45, 130, 20, I18n.format("gui.cancel")));
		buttonList.add(new GuiButton(3, i * 1 + j / 2, height - 45, 130, 20,
				I18n.format("stream.userinfo.unban")));
		buttonList.add(new GuiButton(4, i * 2 + j / 2, height - 45, 130, 20,
				I18n.format("stream.userinfo.unmod")));
		int k = 0;
		IChatComponent ichatcomponent;

		for (Iterator iterator = field_152332_r.iterator(); iterator
				.hasNext(); k = Math.max(k, fontRendererObj.getStringWidth(ichatcomponent.getFormattedText()))) {
			ichatcomponent = (IChatComponent) iterator.next();
		}

		field_152334_t = width / 2 - k / 2;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				field_152333_s.func_152917_b("/ban " + field_152337_h.displayName);
			} else if (p_146284_1_.id == 3) {
				field_152333_s.func_152917_b("/unban " + field_152337_h.displayName);
			} else if (p_146284_1_.id == 2) {
				field_152333_s.func_152917_b("/mod " + field_152337_h.displayName);
			} else if (p_146284_1_.id == 4) {
				field_152333_s.func_152917_b("/unmod " + field_152337_h.displayName);
			} else if (p_146284_1_.id == 1) {
				field_152333_s.func_152917_b("/timeout " + field_152337_h.displayName);
			}

			mc.displayGuiScreen(null);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_152338_i.getUnformattedText(), width / 2, 70, 16777215);
		int k = 80;

		for (Iterator iterator = field_152332_r.iterator(); iterator.hasNext(); k += fontRendererObj.FONT_HEIGHT) {
			IChatComponent ichatcomponent = (IChatComponent) iterator.next();
			drawString(fontRendererObj, ichatcomponent.getFormattedText(), field_152334_t, k, 16777215);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}