package net.minecraft.server.management;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BanEntry extends UserListEntry {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	protected final Date banStartDate;
	protected final String bannedBy;
	protected final Date banEndDate;
	protected final String reason;
	private static final String __OBFID = "CL_00001395";

	public BanEntry(Object p_i1173_1_, Date p_i1173_2_, String p_i1173_3_, Date p_i1173_4_, String p_i1173_5_) {
		super(p_i1173_1_);
		banStartDate = p_i1173_2_ == null ? new Date() : p_i1173_2_;
		bannedBy = p_i1173_3_ == null ? "(Unknown)" : p_i1173_3_;
		banEndDate = p_i1173_4_;
		reason = p_i1173_5_ == null ? "Banned by an operator." : p_i1173_5_;
	}

	protected BanEntry(Object p_i1174_1_, JsonObject p_i1174_2_) {
		super(p_i1174_1_, p_i1174_2_);
		Date date;

		try {
			date = p_i1174_2_.has("created") ? dateFormat.parse(p_i1174_2_.get("created").getAsString()) : new Date();
		} catch (ParseException parseexception1) {
			date = new Date();
		}

		banStartDate = date;
		bannedBy = p_i1174_2_.has("source") ? p_i1174_2_.get("source").getAsString() : "(Unknown)";
		Date date1;

		try {
			date1 = p_i1174_2_.has("expires") ? dateFormat.parse(p_i1174_2_.get("expires").getAsString()) : null;
		} catch (ParseException parseexception) {
			date1 = null;
		}

		banEndDate = date1;
		reason = p_i1174_2_.has("reason") ? p_i1174_2_.get("reason").getAsString() : "Banned by an operator.";
	}

	public Date getBanEndDate() {
		return banEndDate;
	}

	public String getBanReason() {
		return reason;
	}

	@Override
	boolean hasBanExpired() {
		return banEndDate != null && banEndDate.before(new Date());
	}

	@Override
	protected void func_152641_a(JsonObject p_152641_1_) {
		p_152641_1_.addProperty("created", dateFormat.format(banStartDate));
		p_152641_1_.addProperty("source", bannedBy);
		p_152641_1_.addProperty("expires", banEndDate == null ? "forever" : dateFormat.format(banEndDate));
		p_152641_1_.addProperty("reason", reason);
	}

	public Date getCreated() {
		return banStartDate;
	}

	public String getSource() {
		return bannedBy;
	}
}