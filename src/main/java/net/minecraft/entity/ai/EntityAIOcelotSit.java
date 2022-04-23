package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIBase {
	private final EntityOcelot field_151493_a;
	private final double field_151491_b;
	private int field_151492_c;
	private int field_151489_d;
	private int field_151490_e;
	private int field_151487_f;
	private int field_151488_g;
	private int field_151494_h;
	private static final String __OBFID = "CL_00001601";

	public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_) {
		field_151493_a = p_i45315_1_;
		field_151491_b = p_i45315_2_;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		return field_151493_a.isTamed() && !field_151493_a.isSitting()
				&& field_151493_a.getRNG().nextDouble() <= 0.006500000134110451D && func_151485_f();
	}

	@Override
	public boolean continueExecuting() {
		return field_151492_c <= field_151490_e && field_151489_d <= 60
				&& func_151486_a(field_151493_a.worldObj, field_151487_f, field_151488_g, field_151494_h);
	}

	@Override
	public void startExecuting() {
		field_151493_a.getNavigator().tryMoveToXYZ(field_151487_f + 0.5D, field_151488_g + 1, field_151494_h + 0.5D,
				field_151491_b);
		field_151492_c = 0;
		field_151489_d = 0;
		field_151490_e = field_151493_a.getRNG().nextInt(field_151493_a.getRNG().nextInt(1200) + 1200) + 1200;
		field_151493_a.func_70907_r().setSitting(false);
	}

	@Override
	public void resetTask() {
		field_151493_a.setSitting(false);
	}

	@Override
	public void updateTask() {
		++field_151492_c;
		field_151493_a.func_70907_r().setSitting(false);

		if (field_151493_a.getDistanceSq(field_151487_f, field_151488_g + 1, field_151494_h) > 1.0D) {
			field_151493_a.setSitting(false);
			field_151493_a.getNavigator().tryMoveToXYZ(field_151487_f + 0.5D, field_151488_g + 1, field_151494_h + 0.5D,
					field_151491_b);
			++field_151489_d;
		} else if (!field_151493_a.isSitting()) {
			field_151493_a.setSitting(true);
		} else {
			--field_151489_d;
		}
	}

	private boolean func_151485_f() {
		int i = (int) field_151493_a.posY;
		double d0 = 2.147483647E9D;

		for (int j = (int) field_151493_a.posX - 8; j < field_151493_a.posX + 8.0D; ++j) {
			for (int k = (int) field_151493_a.posZ - 8; k < field_151493_a.posZ + 8.0D; ++k) {
				if (func_151486_a(field_151493_a.worldObj, j, i, k)
						&& field_151493_a.worldObj.isAirBlock(j, i + 1, k)) {
					double d1 = field_151493_a.getDistanceSq(j, i, k);

					if (d1 < d0) {
						field_151487_f = j;
						field_151488_g = i;
						field_151494_h = k;
						d0 = d1;
					}
				}
			}
		}

		return d0 < 2.147483647E9D;
	}

	private boolean func_151486_a(World p_151486_1_, int p_151486_2_, int p_151486_3_, int p_151486_4_) {
		Block block = p_151486_1_.getBlock(p_151486_2_, p_151486_3_, p_151486_4_);
		int l = p_151486_1_.getBlockMetadata(p_151486_2_, p_151486_3_, p_151486_4_);

		if (block == Blocks.chest) {
			TileEntityChest tileentitychest = (TileEntityChest) p_151486_1_.getTileEntity(p_151486_2_, p_151486_3_,
					p_151486_4_);

			return tileentitychest.numPlayersUsing < 1;
		} else {
			if (block == Blocks.lit_furnace)
				return true;

			return block == Blocks.bed && !BlockBed.isBlockHeadOfBed(l);
		}
	}
}