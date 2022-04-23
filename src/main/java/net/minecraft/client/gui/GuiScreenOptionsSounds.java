package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenOptionsSounds extends GuiScreen {
	private final GuiScreen field_146505_f;
	private final GameSettings field_146506_g;
	protected String field_146507_a = "Options";
	private String field_146508_h;
	public GuiScreenOptionsSounds(GuiScreen p_i45025_1_, GameSettings p_i45025_2_) {
		field_146505_f = p_i45025_1_;
		field_146506_g = p_i45025_2_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		byte b0 = 0;
		field_146507_a = I18n.format("options.sounds.title");
		field_146508_h = I18n.format("options.off");
		buttonList.add(new GuiScreenOptionsSounds.Button(SoundCategory.MASTER.getCategoryId(),
				width / 2 - 155 + b0 % 2 * 160, height / 6 - 12 + 24 * (b0 >> 1), SoundCategory.MASTER, true));
		int k = b0 + 2;
		SoundCategory[] asoundcategory = SoundCategory.values();
		int i = asoundcategory.length;

		for (int j = 0; j < i; ++j) {
			SoundCategory soundcategory = asoundcategory[j];

			if (soundcategory != SoundCategory.MASTER) {
				buttonList.add(new GuiScreenOptionsSounds.Button(soundcategory.getCategoryId(),
						width / 2 - 155 + k % 2 * 160, height / 6 - 12 + 24 * (k >> 1), soundcategory, false));
				++k;
			}
		}

		buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 200) {
				mc.gameSettings.saveOptions();
				mc.displayGuiScreen(field_146505_f);
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, field_146507_a, width / 2, 15, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	protected String func_146504_a(SoundCategory p_146504_1_) {
		float f = field_146506_g.getSoundLevel(p_146504_1_);
		return f == 0.0F ? field_146508_h : (int) (f * 100.0F) + "%";
	}

	@SideOnly(Side.CLIENT)
	class Button extends GuiButton {
		private final SoundCategory field_146153_r;
		private final String field_146152_s;
		public float field_146156_o = 1.0F;
		public boolean field_146155_p;
		public Button(int p_i45024_2_, int p_i45024_3_, int p_i45024_4_, SoundCategory p_i45024_5_,
				boolean p_i45024_6_) {
			super(p_i45024_2_, p_i45024_3_, p_i45024_4_, p_i45024_6_ ? 310 : 150, 20, "");
			field_146153_r = p_i45024_5_;
			field_146152_s = I18n.format("soundCategory." + p_i45024_5_.getCategoryName());
			displayString = field_146152_s + ": " + func_146504_a(p_i45024_5_);
			field_146156_o = field_146506_g.getSoundLevel(p_i45024_5_);
		}

		@Override
		public int getHoverState(boolean p_146114_1_) {
			return 0;
		}

		@Override
		protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_) {
			if (visible) {
				if (field_146155_p) {
					field_146156_o = (float) (p_146119_2_ - (xPosition + 4)) / (float) (width - 8);

					if (field_146156_o < 0.0F) {
						field_146156_o = 0.0F;
					}

					if (field_146156_o > 1.0F) {
						field_146156_o = 1.0F;
					}

					p_146119_1_.gameSettings.setSoundLevel(field_146153_r, field_146156_o);
					p_146119_1_.gameSettings.saveOptions();
					displayString = field_146152_s + ": " + func_146504_a(field_146153_r);
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				drawTexturedModalRect(xPosition + (int) (field_146156_o * (width - 8)), yPosition, 0, 66, 4, 20);
				drawTexturedModalRect(xPosition + (int) (field_146156_o * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
			}
		}

		@Override
		public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
			if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_)) {
				field_146156_o = (float) (p_146116_2_ - (xPosition + 4)) / (float) (width - 8);

				if (field_146156_o < 0.0F) {
					field_146156_o = 0.0F;
				}

				if (field_146156_o > 1.0F) {
					field_146156_o = 1.0F;
				}

				p_146116_1_.gameSettings.setSoundLevel(field_146153_r, field_146156_o);
				p_146116_1_.gameSettings.saveOptions();
				displayString = field_146152_s + ": " + func_146504_a(field_146153_r);
				field_146155_p = true;
				return true;
			} else
				return false;
		}

		@Override
		public void func_146113_a(SoundHandler p_146113_1_) {
		}

		@Override
		public void mouseReleased(int p_146118_1_, int p_146118_2_) {
			if (field_146155_p) {
				if (field_146153_r == SoundCategory.MASTER) {
				} else {
					field_146506_g.getSoundLevel(field_146153_r);
				}

				GuiScreenOptionsSounds.this.mc.getSoundHandler()
						.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			}

			field_146155_p = false;
		}
	}
}