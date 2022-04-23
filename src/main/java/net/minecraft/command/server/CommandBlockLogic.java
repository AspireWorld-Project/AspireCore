package net.minecraft.command.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class CommandBlockLogic implements ICommandSender {
	private static final SimpleDateFormat field_145766_a = new SimpleDateFormat("HH:mm:ss");
	private int field_145764_b;
	private boolean field_145765_c = true;
	private IChatComponent field_145762_d = null;
	private String field_145763_e = "";
	private String field_145761_f = "@";
	private static final String __OBFID = "CL_00000128";

	public int func_145760_g() {
		return field_145764_b;
	}

	public IChatComponent func_145749_h() {
		return field_145762_d;
	}

	public void func_145758_a(NBTTagCompound p_145758_1_) {
		p_145758_1_.setString("Command", field_145763_e);
		p_145758_1_.setInteger("SuccessCount", field_145764_b);
		p_145758_1_.setString("CustomName", field_145761_f);

		if (field_145762_d != null) {
			p_145758_1_.setString("LastOutput", IChatComponent.Serializer.func_150696_a(field_145762_d));
		}

		p_145758_1_.setBoolean("TrackOutput", field_145765_c);
	}

	public void func_145759_b(NBTTagCompound p_145759_1_) {
		field_145763_e = p_145759_1_.getString("Command");
		field_145764_b = p_145759_1_.getInteger("SuccessCount");

		if (p_145759_1_.hasKey("CustomName", 8)) {
			field_145761_f = p_145759_1_.getString("CustomName");
		}

		if (p_145759_1_.hasKey("LastOutput", 8)) {
			field_145762_d = IChatComponent.Serializer.func_150699_a(p_145759_1_.getString("LastOutput"));
		}

		if (p_145759_1_.hasKey("TrackOutput", 1)) {
			field_145765_c = p_145759_1_.getBoolean("TrackOutput");
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return p_70003_1_ <= 2;
	}

	public void func_145752_a(String p_145752_1_) {
		field_145763_e = p_145752_1_;
	}

	public String func_145753_i() {
		return field_145763_e;
	}

	public void func_145755_a(World p_145755_1_) {
		if (p_145755_1_.isRemote) {
			field_145764_b = 0;
		}

		MinecraftServer minecraftserver = MinecraftServer.getServer();

		if (minecraftserver != null && minecraftserver.isCommandBlockEnabled()) {
			ICommandManager icommandmanager = minecraftserver.getCommandManager();
			field_145764_b = icommandmanager.executeCommand(this, field_145763_e);
		} else {
			field_145764_b = 0;
		}
	}

	@Override
	public String getCommandSenderName() {
		return field_145761_f;
	}

	@Override
	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	public void func_145754_b(String p_145754_1_) {
		field_145761_f = p_145754_1_;
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		if (field_145765_c && getEntityWorld() != null && !getEntityWorld().isRemote) {
			field_145762_d = new ChatComponentText("[" + field_145766_a.format(new Date()) + "] ")
					.appendSibling(p_145747_1_);
			func_145756_e();
		}
	}

	public abstract void func_145756_e();

	@SideOnly(Side.CLIENT)
	public abstract int func_145751_f();

	@SideOnly(Side.CLIENT)
	public abstract void func_145757_a(ByteBuf p_145757_1_);

	public void func_145750_b(IChatComponent p_145750_1_) {
		field_145762_d = p_145750_1_;
	}
}