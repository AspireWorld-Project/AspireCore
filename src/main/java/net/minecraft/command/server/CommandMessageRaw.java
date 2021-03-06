package net.minecraft.command.server;

import com.google.gson.JsonParseException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

public class CommandMessageRaw extends CommandBase {
	private static final String __OBFID = "CL_00000667";

	@Override
	public String getCommandName() {
		return "tellraw";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.tellraw.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length < 2)
			throw new WrongUsageException("commands.tellraw.usage");
		else {
			EntityPlayerMP entityplayermp = getPlayer(p_71515_1_, p_71515_2_[0]);
			String s = func_82360_a(p_71515_1_, p_71515_2_, 1);

			try {
				IChatComponent ichatcomponent = IChatComponent.Serializer.func_150699_a(s);
				entityplayermp.addChatMessage(ichatcomponent);
			} catch (JsonParseException jsonparseexception) {
				Throwable throwable = ExceptionUtils.getRootCause(jsonparseexception);
				throw new SyntaxErrorException("commands.tellraw.jsonException",
						throwable == null ? "" : throwable.getMessage());
			}
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1
				? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames())
				: null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 0;
	}
}