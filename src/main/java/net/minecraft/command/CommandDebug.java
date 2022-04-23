package net.minecraft.command;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommandDebug extends CommandBase {
	private static final Logger logger = LogManager.getLogger();
	private long field_147206_b;
	private int field_147207_c;
	private static final String __OBFID = "CL_00000270";

	@Override
	public String getCommandName() {
		return "debug";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.debug.usage";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
		if (p_71515_2_.length == 1) {
			if (p_71515_2_[0].equals("start")) {
				func_152373_a(p_71515_1_, this, "commands.debug.start", new Object[0]);
				MinecraftServer.getServer().enableProfiling();
				field_147206_b = MinecraftServer.getSystemTimeMillis();
				field_147207_c = MinecraftServer.getServer().getTickCounter();
				return;
			}

			if (p_71515_2_[0].equals("stop")) {
				if (!MinecraftServer.getServer().theProfiler.profilingEnabled)
					throw new CommandException("commands.debug.notStarted", new Object[0]);

				long i = MinecraftServer.getSystemTimeMillis();
				int j = MinecraftServer.getServer().getTickCounter();
				long k = i - field_147206_b;
				int l = j - field_147207_c;
				func_147205_a(k, l);
				MinecraftServer.getServer().theProfiler.profilingEnabled = false;
				func_152373_a(p_71515_1_, this, "commands.debug.stop",
						new Object[] { Float.valueOf(k / 1000.0F), Integer.valueOf(l) });
				return;
			}
		}

		throw new WrongUsageException("commands.debug.usage", new Object[0]);
	}

	private void func_147205_a(long p_147205_1_, int p_147205_3_) {
		File file1 = new File(MinecraftServer.getServer().getFile("debug"),
				"profile-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
		file1.getParentFile().mkdirs();

		try {
			FileWriter filewriter = new FileWriter(file1);
			filewriter.write(func_147204_b(p_147205_1_, p_147205_3_));
			filewriter.close();
		} catch (Throwable throwable) {
			logger.error("Could not save profiler results to " + file1, throwable);
		}
	}

	private String func_147204_b(long p_147204_1_, int p_147204_3_) {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("---- Minecraft Profiler Results ----\n");
		stringbuilder.append("// ");
		stringbuilder.append(func_147203_d());
		stringbuilder.append("\n\n");
		stringbuilder.append("Time span: ").append(p_147204_1_).append(" ms\n");
		stringbuilder.append("Tick span: ").append(p_147204_3_).append(" ticks\n");
		stringbuilder.append("// This is approximately ")
				.append(String.format("%.2f", new Object[] { Float.valueOf(p_147204_3_ / (p_147204_1_ / 1000.0F)) }))
				.append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
		stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
		func_147202_a(0, "root", stringbuilder);
		stringbuilder.append("--- END PROFILE DUMP ---\n\n");
		return stringbuilder.toString();
	}

	private void func_147202_a(int p_147202_1_, String p_147202_2_, StringBuilder p_147202_3_) {
		List list = MinecraftServer.getServer().theProfiler.getProfilingData(p_147202_2_);

		if (list != null && list.size() >= 3) {
			for (int j = 1; j < list.size(); ++j) {
				Profiler.Result result = (Profiler.Result) list.get(j);
				p_147202_3_.append(String.format("[%02d] ", new Object[] { Integer.valueOf(p_147202_1_) }));

				for (int k = 0; k < p_147202_1_; ++k) {
					p_147202_3_.append(" ");
				}

				p_147202_3_.append(result.field_76331_c);
				p_147202_3_.append(" - ");
				p_147202_3_.append(String.format("%.2f", new Object[] { Double.valueOf(result.field_76332_a) }));
				p_147202_3_.append("%/");
				p_147202_3_.append(String.format("%.2f", new Object[] { Double.valueOf(result.field_76330_b) }));
				p_147202_3_.append("%\n");

				if (!result.field_76331_c.equals("unspecified")) {
					try {
						func_147202_a(p_147202_1_ + 1, p_147202_2_ + "." + result.field_76331_c, p_147202_3_);
					} catch (Exception exception) {
						p_147202_3_.append("[[ EXCEPTION " + exception + " ]]");
					}
				}
			}
		}
	}

	private static String func_147203_d() {
		String[] astring = new String[] { "Shiny numbers!", "Am I not running fast enough? :(",
				"I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!",
				"Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers",
				"Now with the same numbers", "You should add flames to things, it makes them go faster!",
				"Do you feel the need for... optimization?", "*cracks redstone whip*",
				"Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server." };

		try {
			return astring[(int) (System.nanoTime() % astring.length)];
		} catch (Throwable throwable) {
			return "Witty comment unavailable :(";
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, new String[] { "start", "stop" })
				: null;
	}
}