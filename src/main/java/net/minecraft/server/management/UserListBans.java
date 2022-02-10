package net.minecraft.server.management;

import java.io.File;
import java.util.Iterator;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListBans extends UserList {
	private static final String __OBFID = "CL_00001873";

	public UserListBans(File p_i1138_1_) {
		super(p_i1138_1_);
	}

	@Override
	protected UserListEntry func_152682_a(JsonObject p_152682_1_) {
		return new UserListBansEntry(p_152682_1_);
	}

	public boolean func_152702_a(GameProfile p_152702_1_) {
		return func_152692_d(p_152702_1_);
	}

	@Override
	public String[] func_152685_a() {
		String[] astring = new String[func_152688_e().size()];
		int i = 0;
		UserListBansEntry userlistbansentry;

		for (Iterator iterator = func_152688_e().values().iterator(); iterator
				.hasNext(); astring[i++] = ((GameProfile) userlistbansentry.func_152640_f()).getName()) {
			userlistbansentry = (UserListBansEntry) iterator.next();
		}

		return astring;
	}

	protected String func_152701_b(GameProfile p_152701_1_) {
		return p_152701_1_.getId().toString();
	}

	public GameProfile func_152703_a(String p_152703_1_) {
		Iterator iterator = func_152688_e().values().iterator();
		UserListBansEntry userlistbansentry;

		do {
			if (!iterator.hasNext())
				return null;

			userlistbansentry = (UserListBansEntry) iterator.next();
		} while (!p_152703_1_.equalsIgnoreCase(((GameProfile) userlistbansentry.func_152640_f()).getName()));

		return (GameProfile) userlistbansentry.func_152640_f();
	}

	@Override
	protected String func_152681_a(Object p_152681_1_) {
		return func_152701_b((GameProfile) p_152681_1_);
	}
}