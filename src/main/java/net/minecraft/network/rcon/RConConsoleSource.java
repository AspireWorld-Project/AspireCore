package net.minecraft.network.rcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class RConConsoleSource implements ICommandSender {
	public static final RConConsoleSource instance = new RConConsoleSource();
	private final StringBuffer buffer = new StringBuffer();
	private static final String __OBFID = "CL_00001800";

	@Override
	public String getCommandSenderName() {
		return "Rcon";
	}

	@Override
	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		buffer.append(p_145747_1_.getUnformattedText());
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return true;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(0, 0, 0);
	}

	@Override
	public World getEntityWorld() {
		return MinecraftServer.getServer().getEntityWorld();
	}

	@SideOnly(Side.SERVER)
	public void resetLog() {
		buffer.setLength(0);
	}

	@SideOnly(Side.SERVER)
	public String getLogContents() {
		return buffer.toString();
	}
}