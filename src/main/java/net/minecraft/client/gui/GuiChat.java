package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiChat extends GuiScreen implements GuiYesNoCallback {
	@SuppressWarnings("rawtypes")
	private static final Set field_152175_f = Sets.newHashSet("http", "https");
	private static final Logger logger = LogManager.getLogger();
	private String field_146410_g = "";
	private int sentHistoryCursor = -1;
	private boolean field_146417_i;
	private boolean field_146414_r;
	private int field_146413_s;
	@SuppressWarnings("rawtypes")
	private final List field_146412_t = new ArrayList();
	private URI clickedURI;
	protected GuiTextField inputField;
	private String defaultInputFieldText = "";
	public GuiChat() {
	}

	public GuiChat(String p_i1024_1_) {
		defaultInputFieldText = p_i1024_1_;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		sentHistoryCursor = mc.ingameGUI.getChatGUI().getSentMessages().size();
		inputField = new GuiTextField(fontRendererObj, 4, height - 12, width - 4, 12);
		inputField.setMaxStringLength(100);
		inputField.setEnableBackgroundDrawing(false);
		inputField.setFocused(true);
		inputField.setText(defaultInputFieldText);
		inputField.setCanLoseFocus(false);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		mc.ingameGUI.getChatGUI().resetScroll();
	}

	@Override
	public void updateScreen() {
		inputField.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		field_146414_r = false;

		if (p_73869_2_ == 15) {
			func_146404_p_();
		} else {
			field_146417_i = false;
		}

		if (p_73869_2_ == 1) {
			mc.displayGuiScreen(null);
		} else if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			if (p_73869_2_ == 200) {
				getSentHistory(-1);
			} else if (p_73869_2_ == 208) {
				getSentHistory(1);
			} else if (p_73869_2_ == 201) {
				mc.ingameGUI.getChatGUI().scroll(mc.ingameGUI.getChatGUI().func_146232_i() - 1);
			} else if (p_73869_2_ == 209) {
				mc.ingameGUI.getChatGUI().scroll(-mc.ingameGUI.getChatGUI().func_146232_i() + 1);
			} else {
				inputField.textboxKeyTyped(p_73869_1_, p_73869_2_);
			}
		} else {
			String s = inputField.getText().trim();

			if (s.length() > 0) {
				func_146403_a(s);
			}

			mc.displayGuiScreen(null);
		}
	}

	public void func_146403_a(String p_146403_1_) {
		mc.ingameGUI.getChatGUI().addToSentMessages(p_146403_1_);
		if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, p_146403_1_) != 0)
			return;
		mc.thePlayer.sendChatMessage(p_146403_1_);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}

			if (!isShiftKeyDown()) {
				i *= 7;
			}

			mc.ingameGUI.getChatGUI().scroll(i);
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0 && mc.gameSettings.chatLinks) {
			IChatComponent ichatcomponent = mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());

			if (ichatcomponent != null) {
				ClickEvent clickevent = ichatcomponent.getChatStyle().getChatClickEvent();

				if (clickevent != null) {
					if (isShiftKeyDown()) {
						inputField.writeText(ichatcomponent.getUnformattedTextForChat());
					} else {
						URI uri;

						if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
							try {
								uri = new URI(clickevent.getValue());

								if (!field_152175_f.contains(uri.getScheme().toLowerCase()))
									throw new URISyntaxException(clickevent.getValue(),
											"Unsupported protocol: " + uri.getScheme().toLowerCase());

								if (mc.gameSettings.chatLinksPrompt) {
									clickedURI = uri;
									mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 0, false));
								} else {
									func_146407_a(uri);
								}
							} catch (URISyntaxException urisyntaxexception) {
								logger.error("Can't open url for " + clickevent, urisyntaxexception);
							}
						} else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
							uri = new File(clickevent.getValue()).toURI();
							func_146407_a(uri);
						} else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
							inputField.setText(clickevent.getValue());
						} else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
							func_146403_a(clickevent.getValue());
						} else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
							ChatUserInfo chatuserinfo = mc.func_152346_Z().func_152926_a(clickevent.getValue());

							if (chatuserinfo != null) {
								mc.displayGuiScreen(new GuiTwitchUserMode(mc.func_152346_Z(), chatuserinfo));
							} else {
								logger.error("Tried to handle twitch user but couldn't find them!");
							}
						} else {
							logger.error("Don't know how to handle " + clickevent);
						}
					}

					return;
				}
			}
		}

		inputField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_2_ == 0) {
			if (p_73878_1_) {
				func_146407_a(clickedURI);
			}

			clickedURI = null;
			mc.displayGuiScreen(this);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void func_146407_a(URI p_146407_1_) {
		try {
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
			oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, p_146407_1_);
		} catch (Throwable throwable) {
			logger.error("Couldn't open link", throwable);
		}
	}

	@SuppressWarnings("rawtypes")
	public void func_146404_p_() {
		String s1;

		if (field_146417_i) {
			inputField.deleteFromCursor(inputField.func_146197_a(-1, inputField.getCursorPosition(), false)
					- inputField.getCursorPosition());

			if (field_146413_s >= field_146412_t.size()) {
				field_146413_s = 0;
			}
		} else {
			int i = inputField.func_146197_a(-1, inputField.getCursorPosition(), false);
			field_146412_t.clear();
			field_146413_s = 0;
			String s = inputField.getText().substring(i).toLowerCase();
			s1 = inputField.getText().substring(0, inputField.getCursorPosition());
			func_146405_a(s1, s);

			if (field_146412_t.isEmpty())
				return;

			field_146417_i = true;
			inputField.deleteFromCursor(i - inputField.getCursorPosition());
		}

		if (field_146412_t.size() > 1) {
			StringBuilder stringbuilder = new StringBuilder();

			for (Iterator iterator = field_146412_t.iterator(); iterator.hasNext(); stringbuilder.append(s1)) {
				s1 = (String) iterator.next();

				if (stringbuilder.length() > 0) {
					stringbuilder.append(", ");
				}
			}

			mc.ingameGUI.getChatGUI()
					.printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
		}

		inputField.writeText(
				EnumChatFormatting.getTextWithoutFormattingCodes((String) field_146412_t.get(field_146413_s++)));
	}

	private void func_146405_a(String p_146405_1_, String p_146405_2_) {
		if (p_146405_1_.length() >= 1) {
			net.minecraftforge.client.ClientCommandHandler.instance.autoComplete(p_146405_1_, p_146405_2_);
			mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_));
			field_146414_r = true;
		}
	}

	public void getSentHistory(int p_146402_1_) {
		int j = sentHistoryCursor + p_146402_1_;
		int k = mc.ingameGUI.getChatGUI().getSentMessages().size();

		if (j < 0) {
			j = 0;
		}

		if (j > k) {
			j = k;
		}

		if (j != sentHistoryCursor) {
			if (j == k) {
				sentHistoryCursor = k;
				inputField.setText(field_146410_g);
			} else {
				if (sentHistoryCursor == k) {
					field_146410_g = inputField.getText();
				}

				inputField.setText((String) mc.ingameGUI.getChatGUI().getSentMessages().get(j));
				sentHistoryCursor = j;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
		inputField.drawTextBox();
		IChatComponent ichatcomponent = mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());

		if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
			HoverEvent hoverevent = ichatcomponent.getChatStyle().getChatHoverEvent();

			if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
				ItemStack itemstack = null;

				try {
					NBTBase nbtbase = JsonToNBT.func_150315_a(hoverevent.getValue().getUnformattedText());

					if (nbtbase != null && nbtbase instanceof NBTTagCompound) {
						itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbtbase);
					}
				} catch (NBTException nbtexception) {
				}

				if (itemstack != null) {
					renderToolTip(itemstack, p_73863_1_, p_73863_2_);
				} else {
					drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", p_73863_1_, p_73863_2_);
				}
			} else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
				func_146283_a(Splitter.on("\n").splitToList(hoverevent.getValue().getFormattedText()), p_73863_1_,
						p_73863_2_);
			} else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
				StatBase statbase = StatList.func_151177_a(hoverevent.getValue().getUnformattedText());

				if (statbase != null) {
					IChatComponent ichatcomponent1 = statbase.func_150951_e();
					ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
							"stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic")
					);
					chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));
					String s = statbase instanceof Achievement ? ((Achievement) statbase).getDescription() : null;
					ArrayList arraylist = Lists.newArrayList(ichatcomponent1.getFormattedText(),
							chatcomponenttranslation.getFormattedText());

					if (s != null) {
						arraylist.addAll(fontRendererObj.listFormattedStringToWidth(s, 150));
					}

					func_146283_a(arraylist, p_73863_1_, p_73863_2_);
				} else {
					drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", p_73863_1_,
							p_73863_2_);
				}
			}

			GL11.glDisable(GL11.GL_LIGHTING);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@SuppressWarnings("unchecked")
	public void func_146406_a(String[] p_146406_1_) {
		if (field_146414_r) {
			field_146417_i = false;
			field_146412_t.clear();
			String[] astring1 = p_146406_1_;
			int i = p_146406_1_.length;

			String[] complete = net.minecraftforge.client.ClientCommandHandler.instance.latestAutoComplete;
			if (complete != null) {
				astring1 = com.google.common.collect.ObjectArrays.concat(complete, astring1, String.class);
				i = astring1.length;
			}

			for (int j = 0; j < i; ++j) {
				String s = astring1[j];

				if (s.length() > 0) {
					field_146412_t.add(s);
				}
			}

			String s1 = inputField.getText()
					.substring(inputField.func_146197_a(-1, inputField.getCursorPosition(), false));
			String s2 = StringUtils.getCommonPrefix(p_146406_1_);

			if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
				inputField.deleteFromCursor(inputField.func_146197_a(-1, inputField.getCursorPosition(), false)
						- inputField.getCursorPosition());
				inputField.writeText(s2);
			} else if (field_146412_t.size() > 0) {
				field_146417_i = true;
				func_146404_p_();
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}