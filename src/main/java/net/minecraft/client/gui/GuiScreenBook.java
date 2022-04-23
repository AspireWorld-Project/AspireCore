package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenBook extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
	private final EntityPlayer editingPlayer;
	private final ItemStack bookObj;
	private final boolean bookIsUnsigned;
	private boolean field_146481_r;
	private boolean field_146480_s;
	private int updateCount;
	private final int bookImageWidth = 192;
	private final int bookImageHeight = 192;
	private int bookTotalPages = 1;
	private int currPage;
	private NBTTagList bookPages;
	private String bookTitle = "";
	private GuiScreenBook.NextPageButton buttonNextPage;
	private GuiScreenBook.NextPageButton buttonPreviousPage;
	private GuiButton buttonDone;
	private GuiButton buttonSign;
	private GuiButton buttonFinalize;
	private GuiButton buttonCancel;
	public GuiScreenBook(EntityPlayer p_i1080_1_, ItemStack p_i1080_2_, boolean p_i1080_3_) {
		editingPlayer = p_i1080_1_;
		bookObj = p_i1080_2_;
		bookIsUnsigned = p_i1080_3_;

		if (p_i1080_2_.hasTagCompound()) {
			NBTTagCompound nbttagcompound = p_i1080_2_.getTagCompound();
			bookPages = nbttagcompound.getTagList("pages", 8);

			if (bookPages != null) {
				bookPages = (NBTTagList) bookPages.copy();
				bookTotalPages = bookPages.tagCount();

				if (bookTotalPages < 1) {
					bookTotalPages = 1;
				}
			}
		}

		if (bookPages == null && p_i1080_3_) {
			bookPages = new NBTTagList();
			bookPages.appendTag(new NBTTagString(""));
			bookTotalPages = 1;
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		++updateCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);

		if (bookIsUnsigned) {
			buttonList.add(buttonSign = new GuiButton(3, width / 2 - 100, 4 + bookImageHeight, 98, 20,
					I18n.format("book.signButton")));
			buttonList.add(buttonDone = new GuiButton(0, width / 2 + 2, 4 + bookImageHeight, 98, 20,
					I18n.format("gui.done")));
			buttonList.add(buttonFinalize = new GuiButton(5, width / 2 - 100, 4 + bookImageHeight, 98, 20,
					I18n.format("book.finalizeButton")));
			buttonList.add(buttonCancel = new GuiButton(4, width / 2 + 2, 4 + bookImageHeight, 98, 20,
					I18n.format("gui.cancel")));
		} else {
			buttonList.add(buttonDone = new GuiButton(0, width / 2 - 100, 4 + bookImageHeight, 200, 20,
					I18n.format("gui.done")));
		}

		int i = (width - bookImageWidth) / 2;
		byte b0 = 2;
		buttonList.add(buttonNextPage = new GuiScreenBook.NextPageButton(1, i + 120, b0 + 154, true));
		buttonList.add(buttonPreviousPage = new GuiScreenBook.NextPageButton(2, i + 38, b0 + 154, false));
		updateButtons();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	private void updateButtons() {
		buttonNextPage.visible = !field_146480_s && (currPage < bookTotalPages - 1 || bookIsUnsigned);
		buttonPreviousPage.visible = !field_146480_s && currPage > 0;
		buttonDone.visible = !bookIsUnsigned || !field_146480_s;

		if (bookIsUnsigned) {
			buttonSign.visible = !field_146480_s;
			buttonCancel.visible = field_146480_s;
			buttonFinalize.visible = field_146480_s;
			buttonFinalize.enabled = bookTitle.trim().length() > 0;
		}
	}

	private void sendBookToServer(boolean p_146462_1_) {
		if (bookIsUnsigned && field_146481_r) {
			if (bookPages != null) {
				String s;

				while (bookPages.tagCount() > 1) {
					s = bookPages.getStringTagAt(bookPages.tagCount() - 1);

					if (s.length() != 0) {
						break;
					}

					bookPages.removeTag(bookPages.tagCount() - 1);
				}

				if (bookObj.hasTagCompound()) {
					NBTTagCompound nbttagcompound = bookObj.getTagCompound();
					nbttagcompound.setTag("pages", bookPages);
				} else {
					bookObj.setTagInfo("pages", bookPages);
				}

				s = "MC|BEdit";

				if (p_146462_1_) {
					s = "MC|BSign";
					bookObj.setTagInfo("author", new NBTTagString(editingPlayer.getCommandSenderName()));
					bookObj.setTagInfo("title", new NBTTagString(bookTitle.trim()));
					bookObj.func_150996_a(Items.written_book);
				}

				ByteBuf bytebuf = Unpooled.buffer();

				try {
					new PacketBuffer(bytebuf).writeItemStackToBuffer(bookObj);
					mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s, bytebuf));
				} catch (Exception exception) {
					logger.error("Couldn't send book info", exception);
				} finally {
					bytebuf.release();
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				mc.displayGuiScreen(null);
				sendBookToServer(false);
			} else if (p_146284_1_.id == 3 && bookIsUnsigned) {
				field_146480_s = true;
			} else if (p_146284_1_.id == 1) {
				if (currPage < bookTotalPages - 1) {
					++currPage;
				} else if (bookIsUnsigned) {
					addNewPage();

					if (currPage < bookTotalPages - 1) {
						++currPage;
					}
				}
			} else if (p_146284_1_.id == 2) {
				if (currPage > 0) {
					--currPage;
				}
			} else if (p_146284_1_.id == 5 && field_146480_s) {
				sendBookToServer(true);
				mc.displayGuiScreen(null);
			} else if (p_146284_1_.id == 4 && field_146480_s) {
				field_146480_s = false;
			}

			updateButtons();
		}
	}

	private void addNewPage() {
		if (bookPages != null && bookPages.tagCount() < 50) {
			bookPages.appendTag(new NBTTagString(""));
			++bookTotalPages;
			field_146481_r = true;
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		super.keyTyped(p_73869_1_, p_73869_2_);

		if (bookIsUnsigned) {
			if (field_146480_s) {
				func_146460_c(p_73869_1_, p_73869_2_);
			} else {
				keyTypedInBook(p_73869_1_, p_73869_2_);
			}
		}
	}

	private void keyTypedInBook(char p_146463_1_, int p_146463_2_) {
		switch (p_146463_1_) {
		case 22:
			func_146459_b(GuiScreen.getClipboardString());
			return;
		default:
			switch (p_146463_2_) {
			case 14:
				String s = func_146456_p();

				if (s.length() > 0) {
					func_146457_a(s.substring(0, s.length() - 1));
				}

				return;
			case 28:
			case 156:
				func_146459_b("\n");
				return;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(p_146463_1_)) {
					func_146459_b(Character.toString(p_146463_1_));
				}
			}
		}
	}

	private void func_146460_c(char p_146460_1_, int p_146460_2_) {
		switch (p_146460_2_) {
		case 14:
			if (!bookTitle.isEmpty()) {
				bookTitle = bookTitle.substring(0, bookTitle.length() - 1);
				updateButtons();
			}

			return;
		case 28:
		case 156:
			if (!bookTitle.isEmpty()) {
				sendBookToServer(true);
				mc.displayGuiScreen(null);
			}

			return;
		default:
			if (bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
				bookTitle = bookTitle + p_146460_1_;
				updateButtons();
				field_146481_r = true;
			}
		}
	}

	private String func_146456_p() {
		return bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()
				? bookPages.getStringTagAt(currPage)
				: "";
	}

	private void func_146457_a(String p_146457_1_) {
		if (bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
			bookPages.func_150304_a(currPage, new NBTTagString(p_146457_1_));
			field_146481_r = true;
		}
	}

	private void func_146459_b(String p_146459_1_) {
		String s1 = func_146456_p();
		String s2 = s1 + p_146459_1_;
		int i = fontRendererObj.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);

		if (i <= 118 && s2.length() < 256) {
			func_146457_a(s2);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(bookGuiTextures);
		int k = (width - bookImageWidth) / 2;
		byte b0 = 2;
		drawTexturedModalRect(k, b0, 0, 0, bookImageWidth, bookImageHeight);
		String s;
		String s1;
		int l;

		if (field_146480_s) {
			s = bookTitle;

			if (bookIsUnsigned) {
				if (updateCount / 6 % 2 == 0) {
					s = s + "" + EnumChatFormatting.BLACK + "_";
				} else {
					s = s + "" + EnumChatFormatting.GRAY + "_";
				}
			}

			s1 = I18n.format("book.editTitle");
			l = fontRendererObj.getStringWidth(s1);
			fontRendererObj.drawString(s1, k + 36 + (116 - l) / 2, b0 + 16 + 16, 0);
			int i1 = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, k + 36 + (116 - i1) / 2, b0 + 48, 0);
			String s2 = I18n.format("book.byAuthor", editingPlayer.getCommandSenderName());
			int j1 = fontRendererObj.getStringWidth(s2);
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + s2, k + 36 + (116 - j1) / 2, b0 + 48 + 10, 0);
			String s3 = I18n.format("book.finalizeWarning");
			fontRendererObj.drawSplitString(s3, k + 36, b0 + 80, 116, 0);
		} else {
			s = I18n.format("book.pageIndicator",
					Integer.valueOf(currPage + 1), Integer.valueOf(bookTotalPages));
			s1 = "";

			if (bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
				s1 = bookPages.getStringTagAt(currPage);
			}

			if (bookIsUnsigned) {
				if (fontRendererObj.getBidiFlag()) {
					s1 = s1 + "_";
				} else if (updateCount / 6 % 2 == 0) {
					s1 = s1 + "" + EnumChatFormatting.BLACK + "_";
				} else {
					s1 = s1 + "" + EnumChatFormatting.GRAY + "_";
				}
			}

			l = fontRendererObj.getStringWidth(s);
			fontRendererObj.drawString(s, k - l + bookImageWidth - 44, b0 + 16, 0);
			fontRendererObj.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton {
		private final boolean field_146151_o;
		public NextPageButton(int p_i1079_1_, int p_i1079_2_, int p_i1079_3_, boolean p_i1079_4_) {
			super(p_i1079_1_, p_i1079_2_, p_i1079_3_, 23, 13, "");
			field_146151_o = p_i1079_4_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
			if (visible) {
				boolean flag = p_146112_2_ >= xPosition && p_146112_3_ >= yPosition && p_146112_2_ < xPosition + width
						&& p_146112_3_ < yPosition + height;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				p_146112_1_.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
				int k = 0;
				int l = 192;

				if (flag) {
					k += 23;
				}

				if (!field_146151_o) {
					l += 13;
				}

				drawTexturedModalRect(xPosition, yPosition, k, l, 23, 13);
			}
		}
	}
}