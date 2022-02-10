package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.Unpooled;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@SideOnly(Side.CLIENT)
public class GuiCommandBlock extends GuiScreen {
	private static final Logger field_146488_a = LogManager.getLogger();
	private GuiTextField commandTextField;
	private GuiTextField field_146486_g;
	private final CommandBlockLogic localCommandBlock;
	private GuiButton doneBtn;
	private GuiButton cancelBtn;
	private static final String __OBFID = "CL_00000748";

	public GuiCommandBlock(CommandBlockLogic p_i45032_1_) {
		localCommandBlock = p_i45032_1_;
	}

	@Override
	public void updateScreen() {
		commandTextField.updateCursorCounter();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(doneBtn = new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20,
				I18n.format("gui.done", new Object[0])));
		buttonList.add(cancelBtn = new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20,
				I18n.format("gui.cancel", new Object[0])));
		commandTextField = new GuiTextField(fontRendererObj, width / 2 - 150, 50, 300, 20);
		commandTextField.setMaxStringLength(32767);
		commandTextField.setFocused(true);
		commandTextField.setText(localCommandBlock.func_145753_i());
		field_146486_g = new GuiTextField(fontRendererObj, width / 2 - 150, 135, 300, 20);
		field_146486_g.setMaxStringLength(32767);
		field_146486_g.setEnabled(false);
		field_146486_g.setText(localCommandBlock.func_145753_i());

		if (localCommandBlock.func_145749_h() != null) {
			field_146486_g.setText(localCommandBlock.func_145749_h().getUnformattedText());
		}

		doneBtn.enabled = commandTextField.getText().trim().length() > 0;
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen((GuiScreen) null);
			} else if (p_146284_1_.id == 0) {
				PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());

				try {
					packetbuffer.writeByte(localCommandBlock.func_145751_f());
					localCommandBlock.func_145757_a(packetbuffer);
					packetbuffer.writeStringToBuffer(commandTextField.getText());
					mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
				} catch (Exception exception) {
					field_146488_a.error("Couldn\'t send command block info", exception);
				} finally {
					packetbuffer.release();
				}

				mc.displayGuiScreen((GuiScreen) null);
			}
		}
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		commandTextField.textboxKeyTyped(p_73869_1_, p_73869_2_);
		field_146486_g.textboxKeyTyped(p_73869_1_, p_73869_2_);
		doneBtn.enabled = commandTextField.getText().trim().length() > 0;

		if (p_73869_2_ != 28 && p_73869_2_ != 156) {
			if (p_73869_2_ == 1) {
				actionPerformed(cancelBtn);
			}
		} else {
			actionPerformed(doneBtn);
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		commandTextField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146486_g.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), width / 2, 20, 16777215);
		drawString(fontRendererObj, I18n.format("advMode.command", new Object[0]), width / 2 - 150, 37, 10526880);
		commandTextField.drawTextBox();
		byte b0 = 75;
		byte b1 = 0;
		FontRenderer fontrenderer = fontRendererObj;
		String s = I18n.format("advMode.nearestPlayer", new Object[0]);
		int i1 = width / 2 - 150;
		int l = b1 + 1;
		drawString(fontrenderer, s, i1, b0 + b1 * fontRendererObj.FONT_HEIGHT, 10526880);
		drawString(fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), width / 2 - 150,
				b0 + l++ * fontRendererObj.FONT_HEIGHT, 10526880);
		drawString(fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), width / 2 - 150,
				b0 + l++ * fontRendererObj.FONT_HEIGHT, 10526880);

		if (field_146486_g.getText().length() > 0) {
			int k = b0 + l * fontRendererObj.FONT_HEIGHT + 20;
			drawString(fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), width / 2 - 150, k,
					10526880);
			field_146486_g.drawTextBox();
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}