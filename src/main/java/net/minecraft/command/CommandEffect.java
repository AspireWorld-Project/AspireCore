package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import java.util.List;

public class CommandEffect extends CommandBase {
	private static final String __OBFID = "CL_00000323";

	@Override
	public String getCommandName() {
		return "effect";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.effect.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length < 2)
			throw new WrongUsageException("commands.effect.usage");
		else {
			EntityPlayerMP entityplayermp = getPlayer(p_71515_1_, p_71515_2_[0]);

			if (p_71515_2_[1].equals("clear")) {
				if (entityplayermp.getActivePotionEffects().isEmpty())
					throw new CommandException("commands.effect.failure.notActive.all",
							entityplayermp.getCommandSenderName());

				entityplayermp.clearActivePotions();
				func_152373_a(p_71515_1_, this, "commands.effect.success.removed.all",
						entityplayermp.getCommandSenderName());
			} else {
				int i = parseIntWithMin(p_71515_1_, p_71515_2_[1], 1);
				int j = 600;
				int k = 30;
				int l = 0;

				if (i < 0 || i >= Potion.potionTypes.length || Potion.potionTypes[i] == null)
					throw new NumberInvalidException("commands.effect.notFound", Integer.valueOf(i));

				if (p_71515_2_.length >= 3) {
					k = parseIntBounded(p_71515_1_, p_71515_2_[2], 0, 1000000);

					if (Potion.potionTypes[i].isInstant()) {
						j = k;
					} else {
						j = k * 20;
					}
				} else if (Potion.potionTypes[i].isInstant()) {
					j = 1;
				}

				if (p_71515_2_.length >= 4) {
					l = parseIntBounded(p_71515_1_, p_71515_2_[3], 0, 255);
				}

				if (k == 0) {
					if (!entityplayermp.isPotionActive(i))
						throw new CommandException("commands.effect.failure.notActive",
								new ChatComponentTranslation(Potion.potionTypes[i].getName()),
								entityplayermp.getCommandSenderName());

					entityplayermp.removePotionEffect(i);
					func_152373_a(p_71515_1_, this, "commands.effect.success.removed",
							new ChatComponentTranslation(Potion.potionTypes[i].getName()),
							entityplayermp.getCommandSenderName());
				} else {
					PotionEffect potioneffect = new PotionEffect(i, j, l);
					entityplayermp.addPotionEffect(potioneffect);
					func_152373_a(p_71515_1_, this, "commands.effect.success",
							new ChatComponentTranslation(potioneffect.getEffectName()),
							Integer.valueOf(i), Integer.valueOf(l), entityplayermp.getCommandSenderName(),
							Integer.valueOf(k));
				}
			}
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, getAllUsernames()) : null;
	}

	protected String[] getAllUsernames() {
		return MinecraftServer.getServer().getAllUsernames();
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return p_82358_2_ == 0;
	}
}