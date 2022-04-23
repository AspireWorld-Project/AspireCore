package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class TileEntityEnchantmentTable extends TileEntity {
	public int field_145926_a;
	public float field_145933_i;
	public float field_145931_j;
	public float field_145932_k;
	public float field_145929_l;
	public float field_145930_m;
	public float field_145927_n;
	public float field_145928_o;
	public float field_145925_p;
	public float field_145924_q;
	private static Random field_145923_r = new Random();
	private String field_145922_s;
	private static final String __OBFID = "CL_00000354";

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);

		if (func_145921_b()) {
			p_145841_1_.setString("CustomName", field_145922_s);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);

		if (p_145839_1_.hasKey("CustomName", 8)) {
			field_145922_s = p_145839_1_.getString("CustomName");
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		field_145927_n = field_145930_m;
		field_145925_p = field_145928_o;
		EntityPlayer entityplayer = worldObj.getClosestPlayer(xCoord + 0.5F, yCoord + 0.5F, zCoord + 0.5F, 3.0D);

		if (entityplayer != null) {
			double d0 = entityplayer.posX - (xCoord + 0.5F);
			double d1 = entityplayer.posZ - (zCoord + 0.5F);
			field_145924_q = (float) Math.atan2(d1, d0);
			field_145930_m += 0.1F;

			if (field_145930_m < 0.5F || field_145923_r.nextInt(40) == 0) {
				float f1 = field_145932_k;

				do {
					field_145932_k += field_145923_r.nextInt(4) - field_145923_r.nextInt(4);
				} while (f1 == field_145932_k);
			}
		} else {
			field_145924_q += 0.02F;
			field_145930_m -= 0.1F;
		}

		while (field_145928_o >= (float) Math.PI) {
			field_145928_o -= (float) Math.PI * 2F;
		}

		while (field_145928_o < -(float) Math.PI) {
			field_145928_o += (float) Math.PI * 2F;
		}

		while (field_145924_q >= (float) Math.PI) {
			field_145924_q -= (float) Math.PI * 2F;
		}

		while (field_145924_q < -(float) Math.PI) {
			field_145924_q += (float) Math.PI * 2F;
		}

		float f2;

		for (f2 = field_145924_q - field_145928_o; f2 >= (float) Math.PI; f2 -= (float) Math.PI * 2F) {
			;
		}

		while (f2 < -(float) Math.PI) {
			f2 += (float) Math.PI * 2F;
		}

		field_145928_o += f2 * 0.4F;

		if (field_145930_m < 0.0F) {
			field_145930_m = 0.0F;
		}

		if (field_145930_m > 1.0F) {
			field_145930_m = 1.0F;
		}

		++field_145926_a;
		field_145931_j = field_145933_i;
		float f = (field_145932_k - field_145933_i) * 0.4F;
		float f3 = 0.2F;

		if (f < -f3) {
			f = -f3;
		}

		if (f > f3) {
			f = f3;
		}

		field_145929_l += (f - field_145929_l) * 0.9F;
		field_145933_i += field_145929_l;
	}

	public String func_145919_a() {
		return func_145921_b() ? field_145922_s : "container.enchant";
	}

	public boolean func_145921_b() {
		return field_145922_s != null && field_145922_s.length() > 0;
	}

	public void func_145920_a(String p_145920_1_) {
		field_145922_s = p_145920_1_;
	}
}