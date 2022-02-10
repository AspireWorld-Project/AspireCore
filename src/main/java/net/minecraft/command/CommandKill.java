package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;

public class CommandKill extends CommandBase {
	private static final String __OBFID = "CL_00000570";

	@Override
	public String getCommandName() {
		return "kill";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.kill.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(p_71515_1_);
		entityplayermp.attackEntityFrom(DamageSource.command, Float.MAX_VALUE);
		p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.kill.success", new Object[0]));
	}
}