package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
	private static final String __OBFID = "CL_00001053";

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(p_71519_1_);
	}

	@Override
	public String getCommandName() {
		return "seed";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.seed.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		Object object = p_71515_1_ instanceof EntityPlayer ? ((EntityPlayer) p_71515_1_).worldObj
				: MinecraftServer.getServer().worldServerForDimension(0);
		p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.seed.success",
				Long.valueOf(((World) object).getSeed())));
	}
}