package net.minecraft.client.gui;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
public class GuiKeyBindingList extends GuiListExtended {
	private final GuiControls field_148191_k;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] field_148190_m;
	private int field_148188_n = 0;
	private static final String __OBFID = "CL_00000732";

	public GuiKeyBindingList(GuiControls p_i45031_1_, Minecraft p_i45031_2_) {
		super(p_i45031_2_, p_i45031_1_.width, p_i45031_1_.height, 63, p_i45031_1_.height - 32, 20);
		field_148191_k = p_i45031_1_;
		mc = p_i45031_2_;
		KeyBinding[] akeybinding = ArrayUtils.clone(p_i45031_2_.gameSettings.keyBindings);
		field_148190_m = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
		Arrays.sort(akeybinding);
		int i = 0;
		String s = null;
		KeyBinding[] akeybinding1 = akeybinding;
		int j = akeybinding.length;

		for (int k = 0; k < j; ++k) {
			KeyBinding keybinding = akeybinding1[k];
			String s1 = keybinding.getKeyCategory();

			if (!s1.equals(s)) {
				s = s1;
				field_148190_m[i++] = new GuiKeyBindingList.CategoryEntry(s1);
			}

			int l = p_i45031_2_.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));

			if (l > field_148188_n) {
				field_148188_n = l;
			}

			field_148190_m[i++] = new GuiKeyBindingList.KeyEntry(keybinding, null);
		}
	}

	@Override
	protected int getSize() {
		return field_148190_m.length;
	}

	@Override
	public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
		return field_148190_m[p_148180_1_];
	}

	@Override
	protected int getScrollBarX() {
		return super.getScrollBarX() + 15;
	}

	@Override
	public int getListWidth() {
		return super.getListWidth() + 32;
	}

	@SideOnly(Side.CLIENT)
	public class CategoryEntry implements GuiListExtended.IGuiListEntry {
		private final String field_148285_b;
		private final int field_148286_c;
		private static final String __OBFID = "CL_00000734";

		public CategoryEntry(String p_i45028_2_) {
			field_148285_b = I18n.format(p_i45028_2_, new Object[0]);
			field_148286_c = mc.fontRenderer.getStringWidth(field_148285_b);
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
			mc.fontRenderer.drawString(field_148285_b, mc.currentScreen.width / 2 - field_148286_c / 2,
					p_148279_3_ + p_148279_5_ - mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
		}
	}

	@SideOnly(Side.CLIENT)
	public class KeyEntry implements GuiListExtended.IGuiListEntry {
		private final KeyBinding field_148282_b;
		private final String field_148283_c;
		private final GuiButton btnChangeKeyBinding;
		private final GuiButton btnReset;
		private static final String __OBFID = "CL_00000735";

		private KeyEntry(KeyBinding p_i45029_2_) {
			field_148282_b = p_i45029_2_;
			field_148283_c = I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]);
			btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18,
					I18n.format(p_i45029_2_.getKeyDescription(), new Object[0]));
			btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
			boolean flag1 = field_148191_k.buttonId == field_148282_b;
			mc.fontRenderer.drawString(field_148283_c, p_148279_2_ + 90 - field_148188_n,
					p_148279_3_ + p_148279_5_ / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
			btnReset.xPosition = p_148279_2_ + 190;
			btnReset.yPosition = p_148279_3_;
			btnReset.enabled = field_148282_b.getKeyCode() != field_148282_b.getKeyCodeDefault();
			btnReset.drawButton(mc, p_148279_7_, p_148279_8_);
			btnChangeKeyBinding.xPosition = p_148279_2_ + 105;
			btnChangeKeyBinding.yPosition = p_148279_3_;
			btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(field_148282_b.getKeyCode());
			boolean flag2 = false;

			if (field_148282_b.getKeyCode() != 0) {
				KeyBinding[] akeybinding = mc.gameSettings.keyBindings;
				int l1 = akeybinding.length;

				for (int i2 = 0; i2 < l1; ++i2) {
					KeyBinding keybinding = akeybinding[i2];

					if (keybinding != field_148282_b && keybinding.getKeyCode() == field_148282_b.getKeyCode()) {
						flag2 = true;
						break;
					}
				}
			}

			if (flag1) {
				btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW
						+ btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
			} else if (flag2) {
				btnChangeKeyBinding.displayString = EnumChatFormatting.RED + btnChangeKeyBinding.displayString;
			}

			btnChangeKeyBinding.drawButton(mc, p_148279_7_, p_148279_8_);
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			if (btnChangeKeyBinding.mousePressed(mc, p_148278_2_, p_148278_3_)) {
				field_148191_k.buttonId = field_148282_b;
				return true;
			} else if (btnReset.mousePressed(mc, p_148278_2_, p_148278_3_)) {
				mc.gameSettings.setOptionKeyBinding(field_148282_b, field_148282_b.getKeyCodeDefault());
				KeyBinding.resetKeyBindingArrayAndHash();
				return true;
			} else
				return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			btnChangeKeyBinding.mouseReleased(p_148277_2_, p_148277_3_);
			btnReset.mouseReleased(p_148277_2_, p_148277_3_);
		}

		KeyEntry(KeyBinding p_i45030_2_, Object p_i45030_3_) {
			this(p_i45030_2_);
		}
	}
}