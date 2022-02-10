package net.minecraft.entity.ai;

import org.bukkit.Material;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
	private EntityLiving field_151500_b;
	private World field_151501_c;
	int field_151502_a;
	private static final String __OBFID = "CL_00001582";

	public EntityAIEatGrass(EntityLiving p_i45314_1_) {
		field_151500_b = p_i45314_1_;
		field_151501_c = p_i45314_1_.worldObj;
		setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
		if (field_151500_b.getRNG().nextInt(field_151500_b.isChild() ? 50 : 1000) != 0)
			return false;
		else {
			int i = MathHelper.floor_double(field_151500_b.posX);
			int j = MathHelper.floor_double(field_151500_b.posY);
			int k = MathHelper.floor_double(field_151500_b.posZ);
			return field_151501_c.getBlock(i, j, k) == Blocks.tallgrass && field_151501_c.getBlockMetadata(i, j, k) == 1
					? true
					: field_151501_c.getBlock(i, j - 1, k) == Blocks.grass;
		}
	}

	@Override
	public void startExecuting() {
		field_151502_a = 40;
		field_151501_c.setEntityState(field_151500_b, (byte) 10);
		field_151500_b.getNavigator().clearPathEntity();
	}

	@Override
	public void resetTask() {
		field_151502_a = 0;
	}

	@Override
	public boolean continueExecuting() {
		return field_151502_a > 0;
	}

	public int func_151499_f() {
		return field_151502_a;
	}

	@Override
	public void updateTask() {
		field_151502_a = Math.max(0, field_151502_a - 1);
		if (field_151502_a == 4) {
			int i = MathHelper.floor_double(field_151500_b.posX);
			int j = MathHelper.floor_double(field_151500_b.posY);
			int k = MathHelper.floor_double(field_151500_b.posZ);
			if (field_151501_c.getBlock(i, j, k) == Blocks.tallgrass) {
				if (!CraftEventFactory.callEntityChangeBlockEvent(field_151500_b,
						field_151500_b.worldObj.getWorld().getBlockAt(i, j, k), Material.AIR,
						!field_151501_c.getGameRules().getGameRuleBooleanValue("mobGriefing")).isCancelled()) {
					field_151501_c.func_147480_a(i, j, k, false);
				}
				field_151500_b.eatGrassBonus();
			} else if (field_151501_c.getBlock(i, j - 1, k) == Blocks.grass) {
				if (!CraftEventFactory.callEntityChangeBlockEvent(field_151500_b,
						field_151500_b.worldObj.getWorld().getBlockAt(i, j - 1, k), Material.DIRT,
						!field_151501_c.getGameRules().getGameRuleBooleanValue("mobGriefing")).isCancelled()) {
					field_151501_c.playAuxSFX(2001, i, j - 1, k, Block.getIdFromBlock(Blocks.grass));
					field_151501_c.setBlock(i, j - 1, k, Blocks.dirt, 0, 2);
				}
				field_151500_b.eatGrassBonus();
			}
		}
	}
}