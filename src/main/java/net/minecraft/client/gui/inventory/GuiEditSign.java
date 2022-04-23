package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEditSign extends GuiScreen {
	private final TileEntitySign tileSign;
	private int updateCounter;
	private int editLine;
	private GuiButton doneBtn;
	public GuiEditSign(TileEntitySign p_i1097_1_) {
		tileSign = p_i1097_1_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(
				doneBtn = new GuiButton(0, width / 2 - 100, height / 4 + 120, I18n.format("gui.done")));
		tileSign.setEditable(false);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		NetHandlerPlayClient nethandlerplayclient = mc.getNetHandler();

		if (nethandlerplayclient != null) {
			nethandlerplayclient.addToSendQueue(
					new C12PacketUpdateSign(tileSign.xCoord, tileSign.yCoord, tileSign.zCoord, tileSign.signText));
		}

		tileSign.setEditable(true);
	}

	@Override
	public void updateScreen() {
		++updateCounter;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				tileSign.markDirty();
				mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 200) {
			editLine = editLine - 1 & 3;
		}

		if (p_73869_2_ == 208 || p_73869_2_ == 28 || p_73869_2_ == 156) {
			editLine = editLine + 1 & 3;
		}

		if (p_73869_2_ == 14 && tileSign.signText[editLine].length() > 0) {
			tileSign.signText[editLine] = tileSign.signText[editLine].substring(0,
					tileSign.signText[editLine].length() - 1);
		}

		if (ChatAllowedCharacters.isAllowedCharacter(p_73869_1_) && tileSign.signText[editLine].length() < 15) {
			tileSign.signText[editLine] = tileSign.signText[editLine] + p_73869_1_;
		}

		if (p_73869_2_ == 1) {
			actionPerformed(doneBtn);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("sign.edit"), width / 2, 40, 16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2, 0.0F, 50.0F);
		float f1 = 93.75F;
		GL11.glScalef(-f1, -f1, -f1);
		GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
		Block block = tileSign.getBlockType();

		if (block == Blocks.standing_sign) {
			float f2 = tileSign.getBlockMetadata() * 360 / 16.0F;
			GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		} else {
			int k = tileSign.getBlockMetadata();
			float f3 = 0.0F;

			if (k == 2) {
				f3 = 180.0F;
			}

			if (k == 4) {
				f3 = 90.0F;
			}

			if (k == 5) {
				f3 = -90.0F;
			}

			GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
		}

		if (updateCounter / 6 % 2 == 0) {
			tileSign.lineBeingEdited = editLine;
		}

		TileEntityRendererDispatcher.instance.renderTileEntityAt(tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
		tileSign.lineBeingEdited = -1;
		GL11.glPopMatrix();
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}