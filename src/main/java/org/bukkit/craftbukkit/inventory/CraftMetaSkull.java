package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {
	static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
	static final int MAX_OWNER_LENGTH = 16;

	private GameProfile profile;

	CraftMetaSkull(CraftMetaItem meta) {
		super(meta);
		if (!(meta instanceof CraftMetaSkull))
			return;
		CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
		profile = skullMeta.profile;
	}

	CraftMetaSkull(net.minecraft.nbt.NBTTagCompound tag) {
		super(tag);

		if (tag.hasKey(SKULL_OWNER.NBT, 10)) {
			profile = NBTUtil.func_152459_a(tag.getCompoundTag(SKULL_OWNER.NBT));
		} else if (tag.hasKey(SKULL_OWNER.NBT, 8)) {
			profile = new GameProfile(null, tag.getString(SKULL_OWNER.NBT));
		}
	}

	CraftMetaSkull(Map<String, Object> map) {
		super(map);
		setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
	}

	@Override
	void applyToItem(net.minecraft.nbt.NBTTagCompound tag) {
		super.applyToItem(tag);

		if (hasOwner()) {
			NBTTagCompound owner = new NBTTagCompound();
			NBTUtil.func_152460_a(owner, profile);
			tag.setTag(SKULL_OWNER.NBT, owner);
		}
	}

	@Override
	boolean isEmpty() {
		return super.isEmpty() && isSkullEmpty();
	}

	boolean isSkullEmpty() {
		return !hasOwner();
	}

	@Override
	boolean applicableTo(Material type) {
		switch (type) {
		case SKULL_ITEM:
			return true;
		default:
			return false;
		}
	}

	@Override
	public CraftMetaSkull clone() {
		return (CraftMetaSkull) super.clone();
	}

	@Override
	public boolean hasOwner() {
		return profile != null;
	}

	@Override
	public String getOwner() {
		return hasOwner() ? profile.getName() : null;
	}

	@Override
	public boolean setOwner(String name) {
		if (name != null && name.length() > MAX_OWNER_LENGTH)
			return false;

		if (name == null) {
			profile = null;
		} else {
			profile = new GameProfile(null, name);
		}

		return true;
	}

	@Override
	int applyHash() {
		final int original;
		int hash = original = super.applyHash();
		if (hasOwner()) {
			hash = 61 * hash + profile.hashCode();
		}
		return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
	}

	@Override
	boolean equalsCommon(CraftMetaItem meta) {
		if (!super.equalsCommon(meta))
			return false;
		if (meta instanceof CraftMetaSkull) {
			CraftMetaSkull that = (CraftMetaSkull) meta;

			return hasOwner() ? that.hasOwner() && profile.equals(that.profile) : !that.hasOwner();
		}
		return true;
	}

	@Override
	boolean notUncommon(CraftMetaItem meta) {
		return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
	}

	@Override
	Builder<String, Object> serialize(Builder<String, Object> builder) {
		super.serialize(builder);
		if (hasOwner())
			return builder.put(SKULL_OWNER.BUKKIT, profile.getName());
		return builder;
	}
}
