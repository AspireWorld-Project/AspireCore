package net.minecraft.profiler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.stack.TLongStack;
import gnu.trove.stack.array.TLongArrayStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Profiler {
	private static final Logger logger = LogManager.getLogger();
	private final List sectionList = new ArrayList();
	private final TLongStack timestampList = new TLongArrayStack();
	public boolean profilingEnabled;
	private String profilingSection = "";
	private final TObjectLongMap<String> profilingMap = new TObjectLongHashMap<>();
	private static final String __OBFID = "CL_00001497";

	public void clearProfiling() {
		profilingMap.clear();
		profilingSection = "";
		sectionList.clear();
	}

	public void startSection(String p_76320_1_) {
		if (profilingEnabled) {
			if (profilingSection.length() > 0) {
				profilingSection = profilingSection + ".";
			}

			profilingSection = profilingSection + p_76320_1_;
			sectionList.add(profilingSection);
			timestampList.push(System.nanoTime());
		}
	}

	public void endSection() {
		if (profilingEnabled) {
			long i = System.nanoTime();
			long j = timestampList.pop();
			sectionList.remove(sectionList.size() - 1);
			long k = i - j;

			if (profilingMap.containsKey(profilingSection)) {
				profilingMap.put(profilingSection, profilingMap.get(profilingSection) + k);
			} else {
				profilingMap.put(profilingSection, k);
			}

			if (k > 50000000L) {
				logger.warn("Something's taking too long! '" + profilingSection + "' took aprox " + k / 1000000.0D
						+ " ms");
			}

			profilingSection = !sectionList.isEmpty() ? (String) sectionList.get(sectionList.size() - 1) : "";
		}
	}

	public List getProfilingData(String p_76321_1_) {
		if (!profilingEnabled)
			return null;
		else {
			long i = profilingMap.containsKey("root") ? profilingMap.get("root") : 0L;
			long j = profilingMap.containsKey(p_76321_1_) ? profilingMap.get(p_76321_1_) : -1L;
			ArrayList arraylist = new ArrayList();

			if (p_76321_1_.length() > 0) {
				p_76321_1_ = p_76321_1_ + ".";
			}

			long k = 0L;
			Iterator iterator = profilingMap.keySet().iterator();

			while (iterator.hasNext()) {
				String s1 = (String) iterator.next();

				if (s1.length() > p_76321_1_.length() && s1.startsWith(p_76321_1_)
						&& s1.indexOf(".", p_76321_1_.length() + 1) < 0) {
					k += profilingMap.get(s1);
				}
			}

			float f = k;

			if (k < j) {
				k = j;
			}

			if (i < k) {
				i = k;
			}

			Iterator iterator1 = profilingMap.keySet().iterator();
			String s2;

			while (iterator1.hasNext()) {
				s2 = (String) iterator1.next();

				if (s2.length() > p_76321_1_.length() && s2.startsWith(p_76321_1_)
						&& s2.indexOf(".", p_76321_1_.length() + 1) < 0) {
					long l = profilingMap.get(s2);
					double d0 = l * 100.0D / k;
					double d1 = l * 100.0D / i;
					String s3 = s2.substring(p_76321_1_.length());
					arraylist.add(new Profiler.Result(s3, d0, d1));
				}
			}

			iterator1 = profilingMap.keySet().iterator();

			while (iterator1.hasNext()) {
				s2 = (String) iterator1.next();
				profilingMap.put(s2, profilingMap.get(s2) * 999L / 1000L);
			}

			if (k > f) {
				arraylist.add(new Profiler.Result("unspecified", (k - f) * 100.0D / k, (k - f) * 100.0D / i));
			}

			Collections.sort(arraylist);
			arraylist.add(0, new Profiler.Result(p_76321_1_, 100.0D, k * 100.0D / i));
			return arraylist;
		}
	}

	public void endStartSection(String p_76318_1_) {
		endSection();
		startSection(p_76318_1_);
	}

	public String getNameOfLastSection() {
		return sectionList.size() == 0 ? "[UNKNOWN]" : (String) sectionList.get(sectionList.size() - 1);
	}

	public static final class Result implements Comparable {
		public double field_76332_a;
		public double field_76330_b;
		public String field_76331_c;
		private static final String __OBFID = "CL_00001498";

		public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
			field_76331_c = p_i1554_1_;
			field_76332_a = p_i1554_2_;
			field_76330_b = p_i1554_4_;
		}

		public int compareTo(Profiler.Result p_compareTo_1_) {
			return p_compareTo_1_.field_76332_a < field_76332_a ? -1
					: p_compareTo_1_.field_76332_a > field_76332_a ? 1
							: p_compareTo_1_.field_76331_c.compareTo(field_76331_c);
		}

		@SideOnly(Side.CLIENT)
		public int func_76329_a() {
			return (field_76331_c.hashCode() & 11184810) + 4473924;
		}

		@Override
		public int compareTo(Object p_compareTo_1_) {
			return this.compareTo((Profiler.Result) p_compareTo_1_);
		}
	}
}