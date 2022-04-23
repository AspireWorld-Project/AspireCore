package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class TileEntityEnderChest extends TileEntity {
	public float field_145972_a;
	public float field_145975_i;
	public int field_145973_j;
	private int field_145974_k;
	@Override
	public void updateEntity() {
		super.updateEntity();

		if (++field_145974_k % 20 * 4 == 0) {
			worldObj.addBlockEvent(xCoord, yCoord, zCoord, Blocks.ender_chest, 1, field_145973_j);
		}

		field_145975_i = field_145972_a;
		float f = 0.1F;
		double d1;

		if (field_145973_j > 0 && field_145972_a == 0.0F) {
			double d0 = xCoord + 0.5D;
			d1 = zCoord + 0.5D;
			worldObj.playSoundEffect(d0, yCoord + 0.5D, d1, "random.chestopen", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (field_145973_j == 0 && field_145972_a > 0.0F || field_145973_j > 0 && field_145972_a < 1.0F) {
			float f2 = field_145972_a;

			if (field_145973_j > 0) {
				field_145972_a += f;
			} else {
				field_145972_a -= f;
			}

			if (field_145972_a > 1.0F) {
				field_145972_a = 1.0F;
			}

			float f1 = 0.5F;

			if (field_145972_a < f1 && f2 >= f1) {
				d1 = xCoord + 0.5D;
				double d2 = zCoord + 0.5D;
				worldObj.playSoundEffect(d1, yCoord + 0.5D, d2, "random.chestclosed", 0.5F,
						worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (field_145972_a < 0.0F) {
				field_145972_a = 0.0F;
			}
		}
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		if (p_145842_1_ == 1) {
			field_145973_j = p_145842_2_;
			return true;
		} else
			return super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public void invalidate() {
		updateContainingBlockInfo();
		super.invalidate();
	}

	public void func_145969_a() {
		++field_145973_j;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Blocks.ender_chest, 1, field_145973_j);
	}

	public void func_145970_b() {
		--field_145973_j;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, Blocks.ender_chest, 1, field_145973_j);
	}

	public boolean func_145971_a(EntityPlayer p_145971_1_) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && p_145971_1_.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}
}