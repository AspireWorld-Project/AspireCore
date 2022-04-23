package net.minecraft.profiler;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.HttpUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class PlayerUsageSnooper {
	private final Map field_152773_a = Maps.newHashMap();
	private final Map field_152774_b = Maps.newHashMap();
	private final String uniqueID = UUID.randomUUID().toString();
	private final URL serverUrl;
	private final IPlayerUsage playerStatsCollector;
	private final Timer threadTrigger = new Timer("Snooper Timer", true);
	private final Object syncLock = new Object();
	private final long minecraftStartTimeMilis;
	private boolean isRunning;
	private int selfCounter;
	private static final String __OBFID = "CL_00001515";

	public PlayerUsageSnooper(String p_i1563_1_, IPlayerUsage p_i1563_2_, long p_i1563_3_) {
		try {
			serverUrl = new URL("http://snoop.minecraft.net/" + p_i1563_1_ + "?version=" + 2);
		} catch (MalformedURLException malformedurlexception) {
			throw new IllegalArgumentException();
		}

		playerStatsCollector = p_i1563_2_;
		minecraftStartTimeMilis = p_i1563_3_;
	}

	public void startSnooper() {
		if (!isRunning) {
			isRunning = true;
			func_152766_h();
			threadTrigger.schedule(new TimerTask() {
				private static final String __OBFID = "CL_00001516";

				@Override
				public void run() {
					if (playerStatsCollector.isSnooperEnabled()) {
						HashMap hashmap;

						synchronized (syncLock) {
							hashmap = new HashMap(field_152774_b);

							if (selfCounter == 0) {
								hashmap.putAll(field_152773_a);
							}

							hashmap.put("snooper_count",
									Integer.valueOf(PlayerUsageSnooper.access$308(PlayerUsageSnooper.this)));
							hashmap.put("snooper_token", uniqueID);
						}

						HttpUtil.func_151226_a(serverUrl, hashmap, true);
					}
				}
			}, 0L, 900000L);
		}
	}

	private void func_152766_h() {
		addJvmArgsToSnooper();
		func_152768_a("snooper_token", uniqueID);
		func_152767_b("snooper_token", uniqueID);
		func_152767_b("os_name", System.getProperty("os.name"));
		func_152767_b("os_version", System.getProperty("os.version"));
		func_152767_b("os_architecture", System.getProperty("os.arch"));
		func_152767_b("java_version", System.getProperty("java.version"));
		func_152767_b("version", "1.7.10");
		playerStatsCollector.addServerTypeToSnooper(this);
	}

	private void addJvmArgsToSnooper() {
		RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
		List list = runtimemxbean.getInputArguments();
		int i = 0;
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			if (s.startsWith("-X")) {
				func_152768_a("jvm_arg[" + i++ + "]", s);
			}
		}

		func_152768_a("jvm_args", Integer.valueOf(i));
	}

	public void addMemoryStatsToSnooper() {
		func_152767_b("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
		func_152767_b("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
		func_152767_b("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
		func_152767_b("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
		playerStatsCollector.addServerStatsToSnooper(this);
	}

	public void func_152768_a(String p_152768_1_, Object p_152768_2_) {
		synchronized (syncLock) {
			field_152774_b.put(p_152768_1_, p_152768_2_);
		}
	}

	public void func_152767_b(String p_152767_1_, Object p_152767_2_) {
		synchronized (syncLock) {
			field_152773_a.put(p_152767_1_, p_152767_2_);
		}
	}

	@SideOnly(Side.CLIENT)
	public Map getCurrentStats() {
		LinkedHashMap linkedhashmap = new LinkedHashMap();
		synchronized (syncLock) {
			addMemoryStatsToSnooper();
			Iterator iterator = field_152773_a.entrySet().iterator();
			Entry entry;

			while (iterator.hasNext()) {
				entry = (Entry) iterator.next();
				linkedhashmap.put(entry.getKey(), entry.getValue().toString());
			}

			iterator = field_152774_b.entrySet().iterator();

			while (iterator.hasNext()) {
				entry = (Entry) iterator.next();
				linkedhashmap.put(entry.getKey(), entry.getValue().toString());
			}

			return linkedhashmap;
		}
	}

	public boolean isSnooperRunning() {
		return isRunning;
	}

	public void stopSnooper() {
		threadTrigger.cancel();
	}

	@SideOnly(Side.CLIENT)
	public String getUniqueID() {
		return uniqueID;
	}

	public long getMinecraftStartTimeMillis() {
		return minecraftStartTimeMilis;
	}

	static int access$308(PlayerUsageSnooper p_access$308_0_) {
		return p_access$308_0_.selfCounter++;
	}
}