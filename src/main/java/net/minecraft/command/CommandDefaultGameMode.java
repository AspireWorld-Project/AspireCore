package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

import java.util.Iterator;

public class CommandDefaultGameMode extends CommandGameMode {
	private static final String __OBFID = "CL_00000296";

	@Override
	public String getCommandName() {
		return "defaultgamemode";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.defaultgamemode.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length > 0) {
			WorldSettings.GameType gametype = getGameModeFromCommand(p_71515_1_, p_71515_2_[0]);
			setGameType(gametype);
			func_152373_a(p_71515_1_, this, "commands.defaultgamemode.success",
					new Object[] { new ChatComponentTranslation("gameMode." + gametype.getName(), new Object[0]) });
		} else
			throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
	}

	protected void setGameType(WorldSettings.GameType p_71541_1_) {
		MinecraftServer minecraftserver = MinecraftServer.getServer();
		minecraftserver.setGameType(p_71541_1_);
		EntityPlayerMP entityplayermp;

		if (minecraftserver.getForceGamemode()) {
			for (Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList
					.iterator(); iterator.hasNext(); entityplayermp.fallDistance = 0.0F) {
				entityplayermp = (EntityPlayerMP) iterator.next();
				entityplayermp.setGameType(p_71541_1_);
			}
		}
	}
}