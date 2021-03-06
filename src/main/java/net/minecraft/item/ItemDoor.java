package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemDoor extends Item {
	private final Material doorMaterial;
	private static final String __OBFID = "CL_00000020";

	public ItemDoor(Material p_i45334_1_) {
		doorMaterial = p_i45334_1_;
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (p_77648_7_ != 1)
			return false;
		else {
			++p_77648_5_;
			Block block;

			if (doorMaterial == Material.wood) {
				block = Blocks.wooden_door;
			} else {
				block = Blocks.iron_door;
			}

			if (p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)
					&& p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_ + 1, p_77648_6_, p_77648_7_, p_77648_1_)) {
				if (!block.canPlaceBlockAt(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_))
					return false;
				else {
					int i1 = MathHelper.floor_double((p_77648_2_.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
					placeDoorBlock(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, i1, block);
					--p_77648_1_.stackSize;
					return true;
				}
			} else
				return false;
		}
	}

	public static void placeDoorBlock(World p_150924_0_, int p_150924_1_, int p_150924_2_, int p_150924_3_,
			int p_150924_4_, Block p_150924_5_) {
		byte b0 = 0;
		byte b1 = 0;

		if (p_150924_4_ == 0) {
			b1 = 1;
		}

		if (p_150924_4_ == 1) {
			b0 = -1;
		}

		if (p_150924_4_ == 2) {
			b1 = -1;
		}

		if (p_150924_4_ == 3) {
			b0 = 1;
		}

		int i1 = (p_150924_0_.getBlock(p_150924_1_ - b0, p_150924_2_, p_150924_3_ - b1).isNormalCube() ? 1 : 0)
				+ (p_150924_0_.getBlock(p_150924_1_ - b0, p_150924_2_ + 1, p_150924_3_ - b1).isNormalCube() ? 1 : 0);
		int j1 = (p_150924_0_.getBlock(p_150924_1_ + b0, p_150924_2_, p_150924_3_ + b1).isNormalCube() ? 1 : 0)
				+ (p_150924_0_.getBlock(p_150924_1_ + b0, p_150924_2_ + 1, p_150924_3_ + b1).isNormalCube() ? 1 : 0);
		boolean flag = p_150924_0_.getBlock(p_150924_1_ - b0, p_150924_2_, p_150924_3_ - b1) == p_150924_5_
				|| p_150924_0_.getBlock(p_150924_1_ - b0, p_150924_2_ + 1, p_150924_3_ - b1) == p_150924_5_;
		boolean flag1 = p_150924_0_.getBlock(p_150924_1_ + b0, p_150924_2_, p_150924_3_ + b1) == p_150924_5_
				|| p_150924_0_.getBlock(p_150924_1_ + b0, p_150924_2_ + 1, p_150924_3_ + b1) == p_150924_5_;
		boolean flag2 = false;

		if (flag && !flag1) {
			flag2 = true;
		} else if (j1 > i1) {
			flag2 = true;
		}

		if (p_150924_0_.captureBlockSnapshots) {
			// Fixed notifyBlocksOfNeighborChange calls if block place event was canceled
			// (reed dupe)
			// Because forge calls markAndNotifyBlock after placing all blocks, flag 3 is
			// now appropriate
			p_150924_0_.setBlock(p_150924_1_, p_150924_2_, p_150924_3_, p_150924_5_, p_150924_4_, 3);
			p_150924_0_.setBlock(p_150924_1_, p_150924_2_ + 1, p_150924_3_, p_150924_5_, 8 | (flag2 ? 1 : 0), 3);
		} else {
			p_150924_0_.setBlock(p_150924_1_, p_150924_2_, p_150924_3_, p_150924_5_, p_150924_4_, 2);
			p_150924_0_.setBlock(p_150924_1_, p_150924_2_ + 1, p_150924_3_, p_150924_5_, 8 | (flag2 ? 1 : 0), 2);
			p_150924_0_.notifyBlocksOfNeighborChange(p_150924_1_, p_150924_2_, p_150924_3_, p_150924_5_);
			p_150924_0_.notifyBlocksOfNeighborChange(p_150924_1_, p_150924_2_ + 1, p_150924_3_, p_150924_5_);
		}
	}
}