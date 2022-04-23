package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class Locale {
	private static final Splitter splitter = Splitter.on('=').limit(2);
	private static final Pattern field_135031_c = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
	Map field_135032_a = Maps.newHashMap();
	private boolean field_135029_d;
	private static final String __OBFID = "CL_00001097";

	public synchronized void loadLocaleDataFiles(IResourceManager p_135022_1_, List p_135022_2_) {
		field_135032_a.clear();
		Iterator iterator = p_135022_2_.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			String s1 = String.format("lang/%s.lang", new Object[] { s });
			Iterator iterator1 = p_135022_1_.getResourceDomains().iterator();

			while (iterator1.hasNext()) {
				String s2 = (String) iterator1.next();

				try {
					this.loadLocaleData(p_135022_1_.getAllResources(new ResourceLocation(s2, s1)));
				} catch (IOException ioexception) {
					;
				}
			}
		}

		checkUnicode();
	}

	public boolean isUnicode() {
		return field_135029_d;
	}

	private void checkUnicode() {
		field_135029_d = false;
		int i = 0;
		int j = 0;
		Iterator iterator = field_135032_a.values().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			int k = s.length();
			j += k;

			for (int l = 0; l < k; ++l) {
				if (s.charAt(l) >= 256) {
					++i;
				}
			}
		}

		float f = (float) i / (float) j;
		field_135029_d = f > 0.1D;
	}

	private void loadLocaleData(List p_135028_1_) throws IOException {
		Iterator iterator = p_135028_1_.iterator();

		while (iterator.hasNext()) {
			IResource iresource = (IResource) iterator.next();
			this.loadLocaleData(iresource.getInputStream());
		}
	}

	private void loadLocaleData(InputStream p_135021_1_) throws IOException {
		Iterator iterator = IOUtils.readLines(p_135021_1_, Charsets.UTF_8).iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			if (!s.isEmpty() && s.charAt(0) != 35) {
				String[] astring = Iterables.toArray(splitter.split(s), String.class);

				if (astring != null && astring.length == 2) {
					String s1 = astring[0];
					String s2 = field_135031_c.matcher(astring[1]).replaceAll("%$1s");
					field_135032_a.put(s1, s2);
				}
			}
		}
	}

	private String translateKeyPrivate(String p_135026_1_) {
		String s1 = (String) field_135032_a.get(p_135026_1_);
		return s1 == null ? p_135026_1_ : s1;
	}

	public String formatMessage(String p_135023_1_, Object[] p_135023_2_) {
		String s1 = translateKeyPrivate(p_135023_1_);

		try {
			return String.format(s1, p_135023_2_);
		} catch (IllegalFormatException illegalformatexception) {
			return "Format error: " + s1;
		}
	}
}