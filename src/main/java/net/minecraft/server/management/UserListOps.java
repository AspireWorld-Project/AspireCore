package net.minecraft.server.management;

import java.io.File;
import java.util.Iterator;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListOps extends UserList {
	private static final String __OBFID = "CL_00001879";

	public UserListOps(File p_i1152_1_) {
		super(p_i1152_1_);
	}

	@Override
	protected UserListEntry func_152682_a(JsonObject p_152682_1_) {
		return new UserListOpsEntry(p_152682_1_);
	}

	@Override
	public String[] func_152685_a() {
		String[] astring = new String[func_152688_e().size()];
		int i = 0;
		UserListOpsEntry userlistopsentry;

		for (Iterator iterator = func_152688_e().values().iterator(); iterator
				.hasNext(); astring[i++] = ((GameProfile) userlistopsentry.func_152640_f()).getName()) {
			userlistopsentry = (UserListOpsEntry) iterator.next();
		}

		return astring;
	}

	protected String func_152699_b(GameProfile p_152699_1_) {
		return p_152699_1_.getId().toString();
	}

	public GameProfile func_152700_a(String p_152700_1_) {
		Iterator iterator = func_152688_e().values().iterator();
		UserListOpsEntry userlistopsentry;

		do {
			if (!iterator.hasNext())
				return null;

			userlistopsentry = (UserListOpsEntry) iterator.next();
		} while (!p_152700_1_.equalsIgnoreCase(((GameProfile) userlistopsentry.func_152640_f()).getName()));

		return (GameProfile) userlistopsentry.func_152640_f();
	}

	@Override
	protected String func_152681_a(Object p_152681_1_) {
		return func_152699_b((GameProfile) p_152681_1_);
	}
}