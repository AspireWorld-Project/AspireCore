package net.minecraft.crash;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class CrashReport {
	private static final Logger logger = LogManager.getLogger();
	private final String description;
	private final Throwable cause;
	private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
	private final List crashReportSections = new ArrayList();
	private File crashReportFile;
	private boolean field_85059_f = true;
	private StackTraceElement[] stacktrace = new StackTraceElement[0];
	private static final String __OBFID = "CL_00000990";

	public CrashReport(String p_i1348_1_, Throwable p_i1348_2_) {
		description = p_i1348_1_;
		cause = p_i1348_2_;
		populateEnvironment();
	}

	private void populateEnvironment() {
		theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable() {
			private static final String __OBFID = "CL_00001197";

			@Override
			public String call() {
				return "1.7.10";
			}
		});
		theReportCategory.addCrashSectionCallable("Operating System", new Callable() {
			private static final String __OBFID = "CL_00001222";

			@Override
			public String call() {
				return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version "
						+ System.getProperty("os.version");
			}
		});
		theReportCategory.addCrashSectionCallable("Java Version", new Callable() {
			private static final String __OBFID = "CL_00001248";

			@Override
			public String call() {
				return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
			}
		});
		theReportCategory.addCrashSectionCallable("Java VM Version", new Callable() {
			private static final String __OBFID = "CL_00001275";

			@Override
			public String call() {
				return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), "
						+ System.getProperty("java.vm.vendor");
			}
		});
		theReportCategory.addCrashSectionCallable("Memory", new Callable() {
			private static final String __OBFID = "CL_00001302";

			@Override
			public String call() {
				Runtime runtime = Runtime.getRuntime();
				long i = runtime.maxMemory();
				long j = runtime.totalMemory();
				long k = runtime.freeMemory();
				long l = i / 1024L / 1024L;
				long i1 = j / 1024L / 1024L;
				long j1 = k / 1024L / 1024L;
				return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l
						+ " MB)";
			}
		});
		theReportCategory.addCrashSectionCallable("JVM Flags", new Callable() {
			private static final String __OBFID = "CL_00001329";

			@Override
			public String call() {
				RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
				List list = runtimemxbean.getInputArguments();
				int i = 0;
				StringBuilder stringbuilder = new StringBuilder();
				Iterator iterator = list.iterator();

				while (iterator.hasNext()) {
					String s = (String) iterator.next();

					if (s.startsWith("-X")) {
						if (i++ > 0) {
							stringbuilder.append(" ");
						}

						stringbuilder.append(s);
					}
				}

				return String.format("%d total; %s", new Object[] { Integer.valueOf(i), stringbuilder.toString() });
			}
		});
		theReportCategory.addCrashSectionCallable("AABB Pool Size", new Callable() {
			private static final String __OBFID = "CL_00001355";

			@Override
			public String call() {
				byte b0 = 0;
				int i = 56 * b0;
				int j = i / 1024 / 1024;
				byte b1 = 0;
				int k = 56 * b1;
				int l = k / 1024 / 1024;
				return b0 + " (" + i + " bytes; " + j + " MB) allocated, " + b1 + " (" + k + " bytes; " + l
						+ " MB) used";
			}
		});
		theReportCategory.addCrashSectionCallable("IntCache", new Callable() {
			private static final String __OBFID = "CL_00001382";

			@Override
			public String call()
					throws SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
				return IntCache.getCacheSizes();
			}
		});
		FMLCommonHandler.instance().enhanceCrashReport(this, theReportCategory);
	}

	public String getDescription() {
		return description;
	}

	public Throwable getCrashCause() {
		return cause;
	}

	public void getSectionsInStringBuilder(StringBuilder p_71506_1_) {
		if ((stacktrace == null || stacktrace.length <= 0) && crashReportSections.size() > 0) {
			stacktrace = ArrayUtils.subarray(((CrashReportCategory) crashReportSections.get(0)).func_147152_a(), 0, 1);
		}

		if (stacktrace != null && stacktrace.length > 0) {
			p_71506_1_.append("-- Head --\n");
			p_71506_1_.append("Stacktrace:\n");
			StackTraceElement[] astacktraceelement = stacktrace;
			int i = astacktraceelement.length;

			for (int j = 0; j < i; ++j) {
				StackTraceElement stacktraceelement = astacktraceelement[j];
				p_71506_1_.append("\t").append("at ").append(stacktraceelement.toString());
				p_71506_1_.append("\n");
			}

			p_71506_1_.append("\n");
		}

		Iterator iterator = crashReportSections.iterator();

		while (iterator.hasNext()) {
			CrashReportCategory crashreportcategory = (CrashReportCategory) iterator.next();
			crashreportcategory.appendToStringBuilder(p_71506_1_);
			p_71506_1_.append("\n\n");
		}

		theReportCategory.appendToStringBuilder(p_71506_1_);
	}

	public String getCauseStackTraceOrString() {
		StringWriter stringwriter = null;
		PrintWriter printwriter = null;
		Object object = cause;

		if (((Throwable) object).getMessage() == null) {
			if (object instanceof NullPointerException) {
				object = new NullPointerException(description);
			} else if (object instanceof StackOverflowError) {
				object = new StackOverflowError(description);
			} else if (object instanceof OutOfMemoryError) {
				object = new OutOfMemoryError(description);
			}

			((Throwable) object).setStackTrace(cause.getStackTrace());
		}

		String s = ((Throwable) object).toString();

		try {
			stringwriter = new StringWriter();
			printwriter = new PrintWriter(stringwriter);
			((Throwable) object).printStackTrace(printwriter);
			s = stringwriter.toString();
		} finally {
			IOUtils.closeQuietly(stringwriter);
			IOUtils.closeQuietly(printwriter);
		}

		return s;
	}

	public String getCompleteReport() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("---- Minecraft Crash Report ----\n");
		stringbuilder.append("// ");
		stringbuilder.append(getWittyComment());
		stringbuilder.append("\n\n");
		stringbuilder.append("Time: ");
		stringbuilder.append(new SimpleDateFormat().format(new Date()));
		stringbuilder.append("\n");
		stringbuilder.append("Description: ");
		stringbuilder.append(description);
		stringbuilder.append("\n\n");
		stringbuilder.append(getCauseStackTraceOrString());
		stringbuilder.append(
				"\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

		for (int i = 0; i < 87; ++i) {
			stringbuilder.append("-");
		}

		stringbuilder.append("\n\n");
		getSectionsInStringBuilder(stringbuilder);
		return stringbuilder.toString();
	}

	@SideOnly(Side.CLIENT)
	public File getFile() {
		return crashReportFile;
	}

	public boolean saveToFile(File p_147149_1_) {
		if (crashReportFile != null)
			return false;
		else {
			if (p_147149_1_.getParentFile() != null) {
				p_147149_1_.getParentFile().mkdirs();
			}

			try {
				FileWriter filewriter = new FileWriter(p_147149_1_);
				filewriter.write(getCompleteReport());
				filewriter.close();
				crashReportFile = p_147149_1_;
				return true;
			} catch (Throwable throwable) {
				logger.error("Could not save crash report to " + p_147149_1_, throwable);
				return false;
			}
		}
	}

	public CrashReportCategory getCategory() {
		return theReportCategory;
	}

	public CrashReportCategory makeCategory(String p_85058_1_) {
		return makeCategoryDepth(p_85058_1_, 1);
	}

	public CrashReportCategory makeCategoryDepth(String p_85057_1_, int p_85057_2_) {
		CrashReportCategory crashreportcategory = new CrashReportCategory(this, p_85057_1_);

		if (field_85059_f) {
			int j = crashreportcategory.getPrunedStackTrace(p_85057_2_);
			StackTraceElement[] astacktraceelement = cause.getStackTrace();
			StackTraceElement stacktraceelement = null;
			StackTraceElement stacktraceelement1 = null;
			int k = astacktraceelement.length - j;

			if (k < 0) {
				System.out.println(
						"Negative index in crash report handler (" + astacktraceelement.length + "/" + j + ")");
			}

			if (astacktraceelement != null && 0 <= k && k < astacktraceelement.length) {
				stacktraceelement = astacktraceelement[k];

				if (astacktraceelement.length + 1 - j < astacktraceelement.length) {
					stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - j];
				}
			}

			field_85059_f = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement,
					stacktraceelement1);

			if (j > 0 && !crashReportSections.isEmpty()) {
				CrashReportCategory crashreportcategory1 = (CrashReportCategory) crashReportSections
						.get(crashReportSections.size() - 1);
				crashreportcategory1.trimStackTraceEntriesFromBottom(j);
			} else if (astacktraceelement != null && astacktraceelement.length >= j && 0 <= k
					&& k < astacktraceelement.length) {
				stacktrace = new StackTraceElement[k];
				System.arraycopy(astacktraceelement, 0, stacktrace, 0, stacktrace.length);
			} else {
				field_85059_f = false;
			}
		}

		crashReportSections.add(crashreportcategory);
		return crashreportcategory;
	}

	private static String getWittyComment() {
		String[] astring = new String[] { "Who set us up the TNT?",
				"Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?",
				"Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.",
				"I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...",
				"Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.",
				"You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!",
				"Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?",
				"Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.",
				"Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?",
				"Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!",
				"Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.",
				"This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!",
				"But it works on my machine." };

		try {
			return astring[(int) (System.nanoTime() % astring.length)];
		} catch (Throwable throwable) {
			return "Witty comment unavailable :(";
		}
	}

	public static CrashReport makeCrashReport(Throwable p_85055_0_, String p_85055_1_) {
		CrashReport crashreport;

		if (p_85055_0_ instanceof ReportedException) {
			crashreport = ((ReportedException) p_85055_0_).getCrashReport();
		} else {
			crashreport = new CrashReport(p_85055_1_, p_85055_0_);
		}

		return crashreport;
	}
}