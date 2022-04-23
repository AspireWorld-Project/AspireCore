package net.minecraft.client.util;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class JsonException extends IOException {
	private final List field_151383_a = Lists.newArrayList();
	private final String field_151382_b;
	private static final String __OBFID = "CL_00001414";

	public JsonException(String p_i45279_1_) {
		field_151383_a.add(new JsonException.Entry(null));
		field_151382_b = p_i45279_1_;
	}

	public JsonException(String p_i45280_1_, Throwable p_i45280_2_) {
		super(p_i45280_2_);
		field_151383_a.add(new JsonException.Entry(null));
		field_151382_b = p_i45280_1_;
	}

	public void func_151380_a(String p_151380_1_) {
		((JsonException.Entry) field_151383_a.get(0)).func_151373_a(p_151380_1_);
	}

	public void func_151381_b(String p_151381_1_) {
		((JsonException.Entry) field_151383_a.get(0)).field_151376_a = p_151381_1_;
		field_151383_a.add(0, new JsonException.Entry(null));
	}

	@Override
	public String getMessage() {
		return "Invalid " + field_151383_a.get(field_151383_a.size() - 1).toString() + ": "
				+ field_151382_b;
	}

	public static JsonException func_151379_a(Exception p_151379_0_) {
		if (p_151379_0_ instanceof JsonException)
			return (JsonException) p_151379_0_;
		else {
			String s = p_151379_0_.getMessage();

			if (p_151379_0_ instanceof FileNotFoundException) {
				s = "File not found";
			}

			return new JsonException(s, p_151379_0_);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Entry {
		private String field_151376_a;
		private final List field_151375_b;
		private static final String __OBFID = "CL_00001416";

		private Entry() {
			field_151376_a = null;
			field_151375_b = Lists.newArrayList();
		}

		private void func_151373_a(String p_151373_1_) {
			field_151375_b.add(0, p_151373_1_);
		}

		public String func_151372_b() {
			return StringUtils.join(field_151375_b, "->");
		}

		@Override
		public String toString() {
			return field_151376_a != null
					? !field_151375_b.isEmpty() ? field_151376_a + " " + func_151372_b() : field_151376_a
					: !field_151375_b.isEmpty() ? "(Unknown file) " + func_151372_b() : "(Unknown file)";
		}

		Entry(Object p_i45278_1_) {
			this();
		}
	}
}