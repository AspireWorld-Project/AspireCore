package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class Session {
	private final String username;
	private final String playerID;
	private final String token;
	private final Session.Type field_152429_d;
	public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_, String p_i1098_4_) {
		if (p_i1098_1_ == null || p_i1098_1_.isEmpty()) {
			p_i1098_1_ = "MissingName";
			p_i1098_2_ = p_i1098_3_ = "NotValid";
			System.out.println("=========================================================");
			System.out.println("Warning the username was not set for this session, typically");
			System.out.println("this means you installed Forge incorrectly. We have set your");
			System.out.println("name to \"MissingName\" and your session to nothing. Please");
			System.out.println("check your instllation and post a console log from the launcher");
			System.out.println("when asking for help!");
			System.out.println("=========================================================");

		}
		username = p_i1098_1_;
		playerID = p_i1098_2_;
		token = p_i1098_3_;
		field_152429_d = Session.Type.func_152421_a(p_i1098_4_);
	}

	public String getSessionID() {
		return "token:" + token + ":" + playerID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public GameProfile func_148256_e() {
		try {
			UUID uuid = UUIDTypeAdapter.fromString(getPlayerID());
			return new GameProfile(uuid, getUsername());
		} catch (IllegalArgumentException illegalargumentexception) {
			return new GameProfile(
					net.minecraft.entity.player.EntityPlayer.func_146094_a(new GameProfile(null, getUsername())),
					getUsername());
		}
	}

	public Session.Type func_152428_f() {
		return field_152429_d;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public enum Type {
		LEGACY("legacy"), MOJANG("mojang");
		@SuppressWarnings("rawtypes")
		private static final Map field_152425_c = Maps.newHashMap();
		private final String field_152426_d;

		Type(String p_i1096_3_) {
			field_152426_d = p_i1096_3_;
		}

		public static Session.Type func_152421_a(String p_152421_0_) {
			return (Session.Type) field_152425_c.get(p_152421_0_.toLowerCase());
		}

		static {
			Session.Type[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				Session.Type var3 = var0[var2];
				field_152425_c.put(var3.field_152426_d, var3);
			}
		}
	}
}