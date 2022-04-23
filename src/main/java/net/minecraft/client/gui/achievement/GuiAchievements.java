package net.minecraft.client.gui.achievement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.AchievementPage;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiAchievements extends GuiScreen implements IProgressMeter {
	private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
	private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
	private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
	private static final ResourceLocation field_146561_C = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");
	protected GuiScreen field_146562_a;
	protected int field_146555_f = 256;
	protected int field_146557_g = 202;
	protected int field_146563_h;
	protected int field_146564_i;
	protected float field_146570_r = 1.0F;
	protected double field_146569_s;
	protected double field_146568_t;
	protected double field_146567_u;
	protected double field_146566_v;
	protected double field_146565_w;
	protected double field_146573_x;
	private int field_146554_D;
	private final StatFileWriter field_146556_E;
	private boolean field_146558_F = true;
	private int currentPage = -1;
	private GuiButton button;
	private final LinkedList<Achievement> minecraftAchievements = new LinkedList<>();

	public GuiAchievements(GuiScreen p_i45026_1_, StatFileWriter p_i45026_2_) {
		field_146562_a = p_i45026_1_;
		field_146556_E = p_i45026_2_;
		short short1 = 141;
		short short2 = 141;
		field_146569_s = field_146567_u = field_146565_w = AchievementList.openInventory.displayColumn * 24 - short1 / 2
				- 12;
		field_146568_t = field_146566_v = field_146573_x = AchievementList.openInventory.displayRow * 24 - short2 / 2;
		minecraftAchievements.clear();
		for (Object achievement : AchievementList.achievementList) {
			if (!AchievementPage.isAchievementInPages((Achievement) achievement)) {
				minecraftAchievements.add((Achievement) achievement);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
		buttonList.clear();
		buttonList.add(new GuiOptionButton(1, width / 2 + 24, height / 2 + 74, 80, 20,
				I18n.format("gui.done")));
		buttonList.add(button = new GuiButton(2, (width - field_146555_f) / 2 + 24, height / 2 + 74, 125, 20,
				AchievementPage.getTitle(currentPage)));
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (!field_146558_F) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_146562_a);
			}

			if (p_146284_1_.id == 2) {
				currentPage++;
				if (currentPage >= AchievementPage.getAchievementPages().size()) {
					currentPage = -1;
				}
				button.displayString = AchievementPage.getTitle(currentPage);
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		} else {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (field_146558_F) {
			drawDefaultBackground();
			drawCenteredString(fontRendererObj, I18n.format("multiplayer.downloadingStats"), width / 2,
					height / 2, 16777215);
			drawCenteredString(fontRendererObj,
					field_146510_b_[(int) (Minecraft.getSystemTime() / 150L % field_146510_b_.length)], width / 2,
					height / 2 + fontRendererObj.FONT_HEIGHT * 2, 16777215);
		} else {
			int k;

			if (Mouse.isButtonDown(0)) {
				k = (width - field_146555_f) / 2;
				int l = (height - field_146557_g) / 2;
				int i1 = k + 8;
				int j1 = l + 17;

				if ((field_146554_D == 0 || field_146554_D == 1) && p_73863_1_ >= i1 && p_73863_1_ < i1 + 224
						&& p_73863_2_ >= j1 && p_73863_2_ < j1 + 155) {
					if (field_146554_D == 0) {
						field_146554_D = 1;
					} else {
						field_146567_u -= (p_73863_1_ - field_146563_h) * field_146570_r;
						field_146566_v -= (p_73863_2_ - field_146564_i) * field_146570_r;
						field_146565_w = field_146569_s = field_146567_u;
						field_146573_x = field_146568_t = field_146566_v;
					}

					field_146563_h = p_73863_1_;
					field_146564_i = p_73863_2_;
				}
			} else {
				field_146554_D = 0;
			}

			k = Mouse.getDWheel();
			float f4 = field_146570_r;

			if (k < 0) {
				field_146570_r += 0.25F;
			} else if (k > 0) {
				field_146570_r -= 0.25F;
			}

			field_146570_r = MathHelper.clamp_float(field_146570_r, 1.0F, 2.0F);

			if (field_146570_r != f4) {
				float f5 = f4 * field_146555_f;
				float f1 = f4 * field_146557_g;
				float f2 = field_146570_r * field_146555_f;
				float f3 = field_146570_r * field_146557_g;
				field_146567_u -= (f2 - f5) * 0.5F;
				field_146566_v -= (f3 - f1) * 0.5F;
				field_146565_w = field_146569_s = field_146567_u;
				field_146573_x = field_146568_t = field_146566_v;
			}

			if (field_146565_w < field_146572_y) {
				field_146565_w = field_146572_y;
			}

			if (field_146573_x < field_146571_z) {
				field_146573_x = field_146571_z;
			}

			if (field_146565_w >= field_146559_A) {
				field_146565_w = field_146559_A - 1;
			}

			if (field_146573_x >= field_146560_B) {
				field_146573_x = field_146560_B - 1;
			}

			drawDefaultBackground();
			func_146552_b(p_73863_1_, p_73863_2_, p_73863_3_);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			func_146553_h();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	@Override
	public void func_146509_g() {
		if (field_146558_F) {
			field_146558_F = false;
		}
	}

	@Override
	public void updateScreen() {
		if (!field_146558_F) {
			field_146569_s = field_146567_u;
			field_146568_t = field_146566_v;
			double d0 = field_146565_w - field_146567_u;
			double d1 = field_146573_x - field_146566_v;

			if (d0 * d0 + d1 * d1 < 4.0D) {
				field_146567_u += d0;
				field_146566_v += d1;
			} else {
				field_146567_u += d0 * 0.85D;
				field_146566_v += d1 * 0.85D;
			}
		}
	}

	protected void func_146553_h() {
		int i = (width - field_146555_f) / 2;
		int j = (height - field_146557_g) / 2;
		fontRendererObj.drawString(I18n.format("gui.achievements"), i + 15, j + 5, 4210752);
	}

	protected void func_146552_b(int p_146552_1_, int p_146552_2_, float p_146552_3_) {
		int k = MathHelper.floor_double(field_146569_s + (field_146567_u - field_146569_s) * p_146552_3_);
		int l = MathHelper.floor_double(field_146568_t + (field_146566_v - field_146568_t) * p_146552_3_);

		if (k < field_146572_y) {
			k = field_146572_y;
		}

		if (l < field_146571_z) {
			l = field_146571_z;
		}

		if (k >= field_146559_A) {
			k = field_146559_A - 1;
		}

		if (l >= field_146560_B) {
			l = field_146560_B - 1;
		}

		int i1 = (width - field_146555_f) / 2;
		int j1 = (height - field_146557_g) / 2;
		int k1 = i1 + 16;
		int l1 = j1 + 17;
		zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(k1, l1, -200.0F);
		// FIXES models rendering weirdly in the acheivements pane
		// see
		// https://github.com/MinecraftForge/MinecraftForge/commit/1b7ce7592caafb760ec93066184182ae0711e793#commitcomment-10512284
		GL11.glScalef(1.0F / field_146570_r, 1.0F / field_146570_r, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int i2 = k + 288 >> 4;
		int j2 = l + 288 >> 4;
		int k2 = (k + 288) % 16;
		int l2 = (l + 288) % 16;
		Random random = new Random();
		float f1 = 16.0F / field_146570_r;
		float f2 = 16.0F / field_146570_r;
		int i3;
		int j3;
		int k3;

		for (i3 = 0; i3 * f1 - l2 < 155.0F; ++i3) {
			float f3 = 0.6F - (j2 + i3) / 25.0F * 0.3F;
			GL11.glColor4f(f3, f3, f3, 1.0F);

			for (j3 = 0; j3 * f2 - k2 < 224.0F; ++j3) {
				random.setSeed(mc.getSession().getPlayerID().hashCode() + i2 + j3 + (j2 + i3) * 16);
				k3 = random.nextInt(1 + j2 + i3) + (j2 + i3) / 2;
				IIcon iicon = Blocks.sand.getIcon(0, 0);

				if (k3 <= 37 && j2 + i3 != 35) {
					if (k3 == 22) {
						if (random.nextInt(2) == 0) {
							iicon = Blocks.diamond_ore.getIcon(0, 0);
						} else {
							iicon = Blocks.redstone_ore.getIcon(0, 0);
						}
					} else if (k3 == 10) {
						iicon = Blocks.iron_ore.getIcon(0, 0);
					} else if (k3 == 8) {
						iicon = Blocks.coal_ore.getIcon(0, 0);
					} else if (k3 > 4) {
						iicon = Blocks.stone.getIcon(0, 0);
					} else if (k3 > 0) {
						iicon = Blocks.dirt.getIcon(0, 0);
					}
				} else {
					iicon = Blocks.bedrock.getIcon(0, 0);
				}

				mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
				drawTexturedModelRectFromIcon(j3 * 16 - k2, i3 * 16 - l2, iicon, 16, 16);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		mc.getTextureManager().bindTexture(field_146561_C);
		int i4;
		int j4;
		int l4;

		List<Achievement> achievementList = currentPage == -1 ? minecraftAchievements
				: AchievementPage.getAchievementPage(currentPage).getAchievements();
		for (i3 = 0; i3 < achievementList.size(); ++i3) {
			Achievement achievement1 = achievementList.get(i3);

			if (achievement1.parentAchievement != null && achievementList.contains(achievement1.parentAchievement)) {
				j3 = achievement1.displayColumn * 24 - k + 11;
				k3 = achievement1.displayRow * 24 - l + 11;
				l4 = achievement1.parentAchievement.displayColumn * 24 - k + 11;
				int l3 = achievement1.parentAchievement.displayRow * 24 - l + 11;
				boolean flag5 = field_146556_E.hasAchievementUnlocked(achievement1);
				boolean flag6 = field_146556_E.canUnlockAchievement(achievement1);
				i4 = field_146556_E.func_150874_c(achievement1);

				if (i4 <= 4) {
					j4 = -16777216;

					if (flag5) {
						j4 = -6250336;
					} else if (flag6) {
						j4 = -16711936;
					}

					drawHorizontalLine(j3, l4, k3, j4);
					drawVerticalLine(l4, k3, l3, j4);

					if (j3 > l4) {
						drawTexturedModalRect(j3 - 11 - 7, k3 - 5, 114, 234, 7, 11);
					} else if (j3 < l4) {
						drawTexturedModalRect(j3 + 11, k3 - 5, 107, 234, 7, 11);
					} else if (k3 > l3) {
						drawTexturedModalRect(j3 - 5, k3 - 11 - 7, 96, 234, 11, 7);
					} else if (k3 < l3) {
						drawTexturedModalRect(j3 - 5, k3 + 11, 96, 241, 11, 7);
					}
				}
			}
		}

		Achievement achievement = null;
		RenderItem renderitem = new RenderItem();
		float f4 = (p_146552_1_ - k1) * field_146570_r;
		float f5 = (p_146552_2_ - l1) * field_146570_r;
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int i5;
		int j5;

		for (l4 = 0; l4 < achievementList.size(); ++l4) {
			Achievement achievement2 = achievementList.get(l4);
			i5 = achievement2.displayColumn * 24 - k;
			j5 = achievement2.displayRow * 24 - l;

			if (i5 >= -24 && j5 >= -24 && i5 <= 224.0F * field_146570_r && j5 <= 155.0F * field_146570_r) {
				i4 = field_146556_E.func_150874_c(achievement2);
				float f6;

				if (field_146556_E.hasAchievementUnlocked(achievement2)) {
					f6 = 0.75F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
				} else if (field_146556_E.canUnlockAchievement(achievement2)) {
					f6 = 1.0F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
				} else if (i4 < 3) {
					f6 = 0.3F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
				} else if (i4 == 3) {
					f6 = 0.2F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
				} else {
					if (i4 != 4) {
						continue;
					}

					f6 = 0.1F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
				}

				mc.getTextureManager().bindTexture(field_146561_C);

				GL11.glEnable(GL11.GL_BLEND);// Forge: Specifically enable blend because it is needed here. And we fix
												// Generic RenderItem's leakage of it.
				if (achievement2.getSpecial()) {
					drawTexturedModalRect(i5 - 2, j5 - 2, 26, 202, 26, 26);
				} else {
					drawTexturedModalRect(i5 - 2, j5 - 2, 0, 202, 26, 26);
				}
				GL11.glDisable(GL11.GL_BLEND); // Forge: Cleanup states we set.

				if (!field_146556_E.canUnlockAchievement(achievement2)) {
					f6 = 0.1F;
					GL11.glColor4f(f6, f6, f6, 1.0F);
					renderitem.renderWithColor = false;
				}

				GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure Lighting is disabled. Fixes MC-33065
				GL11.glEnable(GL11.GL_CULL_FACE);
				renderitem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(),
						achievement2.theItemStack, i5 + 3, j5 + 3);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDisable(GL11.GL_LIGHTING);

				if (!field_146556_E.canUnlockAchievement(achievement2)) {
					renderitem.renderWithColor = true;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				if (f4 >= i5 && f4 <= i5 + 22 && f5 >= j5 && f5 <= j5 + 22) {
					achievement = achievement2;
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_146561_C);
		drawTexturedModalRect(i1, j1, 0, 0, field_146555_f, field_146557_g);
		zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);

		if (achievement != null) {
			String s1 = achievement.func_150951_e().getUnformattedText();
			String s2 = achievement.getDescription();
			i5 = p_146552_1_ + 12;
			j5 = p_146552_2_ - 4;
			i4 = field_146556_E.func_150874_c(achievement);

			if (!field_146556_E.canUnlockAchievement(achievement)) {
				String s;
				int k4;

				if (i4 == 3) {
					s1 = I18n.format("achievement.unknown");
					j4 = Math.max(fontRendererObj.getStringWidth(s1), 120);
					s = new ChatComponentTranslation("achievement.requires",
							achievement.parentAchievement.func_150951_e()).getUnformattedText();
					k4 = fontRendererObj.splitStringWidth(s, j4);
					drawGradientRect(i5 - 3, j5 - 3, i5 + j4 + 3, j5 + k4 + 12 + 3, -1073741824, -1073741824);
					fontRendererObj.drawSplitString(s, i5, j5 + 12, j4, -9416624);
				} else if (i4 < 3) {
					j4 = Math.max(fontRendererObj.getStringWidth(s1), 120);
					s = new ChatComponentTranslation("achievement.requires",
							achievement.parentAchievement.func_150951_e()).getUnformattedText();
					k4 = fontRendererObj.splitStringWidth(s, j4);
					drawGradientRect(i5 - 3, j5 - 3, i5 + j4 + 3, j5 + k4 + 12 + 3, -1073741824, -1073741824);
					fontRendererObj.drawSplitString(s, i5, j5 + 12, j4, -9416624);
				} else {
					s1 = null;
				}
			} else {
				j4 = Math.max(fontRendererObj.getStringWidth(s1), 120);
				int k5 = fontRendererObj.splitStringWidth(s2, j4);

				if (field_146556_E.hasAchievementUnlocked(achievement)) {
					k5 += 12;
				}

				drawGradientRect(i5 - 3, j5 - 3, i5 + j4 + 3, j5 + k5 + 3 + 12, -1073741824, -1073741824);
				fontRendererObj.drawSplitString(s2, i5, j5 + 12, j4, -6250336);

				if (field_146556_E.hasAchievementUnlocked(achievement)) {
					fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken"), i5,
							j5 + k5 + 4, -7302913);
				}
			}

			if (s1 != null) {
				fontRendererObj.drawStringWithShadow(s1, i5, j5,
						field_146556_E.canUnlockAchievement(achievement) ? achievement.getSpecial() ? -128 : -1
								: achievement.getSpecial() ? -8355776 : -8355712);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return !field_146558_F;
	}
}