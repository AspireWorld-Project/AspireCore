package org.bukkit.craftbukkit;

import java.io.IOException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListBansEntry;

public final class CraftProfileBanEntry implements org.bukkit.BanEntry {
	private static final Logger log = LogManager.getLogger();
	private final UserListBans list;
	private final GameProfile profile;
	private Date created;
	private String source;
	private Date expiration;
	private String reason;

	public CraftProfileBanEntry(GameProfile profile, UserListBansEntry entry, UserListBans list) {
		this.list = list;
		this.profile = profile;
		created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
		source = entry.getSource();
		expiration = entry.getBanEndDate() != null ? new Date(entry.getBanEndDate().getTime()) : null;
		reason = entry.getBanReason();
	}

	@Override
	public String getTarget() {
		return profile.getName();
	}

	@Override
	public Date getCreated() {
		return created == null ? null : (Date) created.clone();
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public Date getExpiration() {
		return expiration == null ? null : (Date) expiration.clone();
	}

	@Override
	public void setExpiration(Date expiration) {
		if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
			expiration = null; // Forces "forever"
		}

		this.expiration = expiration;
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public void save() {
		UserListBansEntry entry = new UserListBansEntry(profile, created, source, expiration, reason);
		list.func_152687_a(entry);
		try {
			list.func_152678_f();
		} catch (IOException ex) {
			log.error("Failed to save banned-players.json, " + ex.getMessage());
		}
	}
}
