package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity {
	private int field_145908_a;
	private int field_145910_i;
	private GameProfile field_152110_j = null;
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setByte("SkullType", (byte) (field_145908_a & 255));
		p_145841_1_.setByte("Rot", (byte) (field_145910_i & 255));

		if (field_152110_j != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			NBTUtil.func_152460_a(nbttagcompound1, field_152110_j);
			p_145841_1_.setTag("Owner", nbttagcompound1);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145908_a = p_145839_1_.getByte("SkullType");
		field_145910_i = p_145839_1_.getByte("Rot");

		if (field_145908_a == 3) {
			if (p_145839_1_.hasKey("Owner", 10)) {
				field_152110_j = NBTUtil.func_152459_a(p_145839_1_.getCompoundTag("Owner"));
			} else if (p_145839_1_.hasKey("ExtraType", 8)
					&& !StringUtils.isNullOrEmpty(p_145839_1_.getString("ExtraType"))) {
				field_152110_j = new GameProfile(null, p_145839_1_.getString("ExtraType"));
				func_152109_d();
			}
		}
	}

	public GameProfile func_152108_a() {
		return field_152110_j;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 4, nbttagcompound);
	}

	public void func_152107_a(int p_152107_1_) {
		field_145908_a = p_152107_1_;
		field_152110_j = null;
	}

	public void func_152106_a(GameProfile p_152106_1_) {
		field_145908_a = 3;
		field_152110_j = p_152106_1_;
		func_152109_d();
	}

	private void func_152109_d() {
		if (field_152110_j != null && !StringUtils.isNullOrEmpty(field_152110_j.getName())) {
			if (!field_152110_j.isComplete() || !field_152110_j.getProperties().containsKey("textures")) {
				GameProfile gameprofile = MinecraftServer.getServer().func_152358_ax()
						.func_152655_a(field_152110_j.getName());

				if (gameprofile != null) {
					Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"),
							(Object) null);

					if (property == null) {
						gameprofile = MinecraftServer.getServer().func_147130_as().fillProfileProperties(gameprofile,
								true);
					}

					field_152110_j = gameprofile;
					markDirty();
				}
			}
		}
	}

	public int func_145904_a() {
		return field_145908_a;
	}

	public void func_145903_a(int p_145903_1_) {
		field_145910_i = p_145903_1_;
	}

	@SideOnly(Side.CLIENT)
	public int func_145906_b() {
		return field_145910_i;
	}

	public int getRotation() {
		return field_145910_i;
	}
}