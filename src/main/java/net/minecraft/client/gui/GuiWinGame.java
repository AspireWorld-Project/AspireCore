package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiWinGame extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private static final ResourceLocation field_146576_f = new ResourceLocation("textures/gui/title/minecraft.png");
	private static final ResourceLocation field_146577_g = new ResourceLocation("textures/misc/vignette.png");
	private int field_146581_h;
	private List field_146582_i;
	private int field_146579_r;
	private float field_146578_s = 0.5F;
	private static final String __OBFID = "CL_00000719";

	@Override
	public void updateScreen() {
		++field_146581_h;
		float f = (field_146579_r + height + height + 24) / field_146578_s;

		if (field_146581_h > f) {
			func_146574_g();
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			func_146574_g();
		}
	}

	private void func_146574_g() {
		mc.thePlayer.sendQueue
				.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
		mc.displayGuiScreen((GuiScreen) null);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void initGui() {
		if (field_146582_i == null) {
			field_146582_i = new ArrayList();

			try {
				String s = "";
				String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN
						+ EnumChatFormatting.AQUA;
				short short1 = 274;
				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(
						mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(),
						Charsets.UTF_8));
				Random random = new Random(8124371L);
				int i;

				while ((s = bufferedreader.readLine()) != null) {
					String s2;
					String s3;

					for (s = s.replaceAll("PLAYERNAME", mc.getSession().getUsername()); s
							.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED
									+ "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
						i = s.indexOf(s1);
						s2 = s.substring(0, i);
						s3 = s.substring(i + s1.length());
					}

					field_146582_i.addAll(mc.fontRenderer.listFormattedStringToWidth(s, short1));
					field_146582_i.add("");
				}

				for (i = 0; i < 8; ++i) {
					field_146582_i.add("");
				}

				bufferedreader = new BufferedReader(new InputStreamReader(
						mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(),
						Charsets.UTF_8));

				while ((s = bufferedreader.readLine()) != null) {
					s = s.replaceAll("PLAYERNAME", mc.getSession().getUsername());
					s = s.replaceAll("\t", "    ");
					field_146582_i.addAll(mc.fontRenderer.listFormattedStringToWidth(s, short1));
					field_146582_i.add("");
				}

				field_146579_r = field_146582_i.size() * 12;
			} catch (Exception exception) {
				logger.error("Couldn\'t load credits", exception);
			}
		}
	}

	private void func_146575_b(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
		Tessellator tessellator = Tessellator.instance;
		mc.getTextureManager().bindTexture(Gui.optionsBackground);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int k = width;
		float f1 = 0.0F - (field_146581_h + p_146575_3_) * 0.5F * field_146578_s;
		float f2 = height - (field_146581_h + p_146575_3_) * 0.5F * field_146578_s;
		float f3 = 0.015625F;
		float f4 = (field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
		float f5 = (field_146579_r + height + height + 24) / field_146578_s;
		float f6 = (f5 - 20.0F - (field_146581_h + p_146575_3_)) * 0.005F;

		if (f6 < f4) {
			f4 = f6;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		f4 *= f4;
		f4 = f4 * 96.0F / 255.0F;
		tessellator.setColorOpaque_F(f4, f4, f4);
		tessellator.addVertexWithUV(0.0D, height, zLevel, 0.0D, f1 * f3);
		tessellator.addVertexWithUV(k, height, zLevel, k * f3, f1 * f3);
		tessellator.addVertexWithUV(k, 0.0D, zLevel, k * f3, f2 * f3);
		tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.0D, f2 * f3);
		tessellator.draw();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		func_146575_b(p_73863_1_, p_73863_2_, p_73863_3_);
		Tessellator tessellator = Tessellator.instance;
		short short1 = 274;
		int k = width / 2 - short1 / 2;
		int l = height + 50;
		float f1 = -(field_146581_h + p_73863_3_) * field_146578_s;
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, f1, 0.0F);
		mc.getTextureManager().bindTexture(field_146576_f);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(k, l, 0, 0, 155, 44);
		drawTexturedModalRect(k + 155, l, 0, 45, 155, 44);
		tessellator.setColorOpaque_I(16777215);
		int i1 = l + 200;
		int j1;

		for (j1 = 0; j1 < field_146582_i.size(); ++j1) {
			if (j1 == field_146582_i.size() - 1) {
				float f2 = i1 + f1 - (height / 2 - 6);

				if (f2 < 0.0F) {
					GL11.glTranslatef(0.0F, -f2, 0.0F);
				}
			}

			if (i1 + f1 + 12.0F + 8.0F > 0.0F && i1 + f1 < height) {
				String s = (String) field_146582_i.get(j1);

				if (s.startsWith("[C]")) {
					fontRendererObj.drawStringWithShadow(s.substring(3),
							k + (short1 - fontRendererObj.getStringWidth(s.substring(3))) / 2, i1, 16777215);
				} else {
					fontRendererObj.fontRandom.setSeed(j1 * 4238972211L + field_146581_h / 4);
					fontRendererObj.drawStringWithShadow(s, k, i1, 16777215);
				}
			}

			i1 += 12;
		}

		GL11.glPopMatrix();
		mc.getTextureManager().bindTexture(field_146577_g);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		j1 = width;
		int k1 = height;
		tessellator.addVertexWithUV(0.0D, k1, zLevel, 0.0D, 1.0D);
		tessellator.addVertexWithUV(j1, k1, zLevel, 1.0D, 1.0D);
		tessellator.addVertexWithUV(j1, 0.0D, zLevel, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDisable(GL11.GL_BLEND);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}