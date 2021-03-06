package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemReed extends Item {
	private final Block field_150935_a;
	private static final String __OBFID = "CL_00001773";

	public ItemReed(Block p_i45329_1_) {
		field_150935_a = p_i45329_1_;
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

		if (block == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 7) < 1) {
			p_77648_7_ = 1;
		} else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush) {
			if (p_77648_7_ == 0) {
				--p_77648_5_;
			}

			if (p_77648_7_ == 1) {
				++p_77648_5_;
			}

			if (p_77648_7_ == 2) {
				--p_77648_6_;
			}

			if (p_77648_7_ == 3) {
				++p_77648_6_;
			}

			if (p_77648_7_ == 4) {
				--p_77648_4_;
			}

			if (p_77648_7_ == 5) {
				++p_77648_4_;
			}
		}

		if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
			return false;
		else if (p_77648_1_.stackSize == 0)
			return false;
		else {
			if (p_77648_3_.canPlaceEntityOnSide(field_150935_a, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_,
                    null, p_77648_1_)) {
				int i1 = field_150935_a.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_,
						p_77648_8_, p_77648_9_, p_77648_10_, 0);

				if (p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, field_150935_a, i1, 3)) {
					if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == field_150935_a) {
						field_150935_a.onBlockPlacedBy(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_2_,
								p_77648_1_);
						field_150935_a.onPostBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, i1);
					}

					p_77648_3_.playSoundEffect(p_77648_4_ + 0.5F, p_77648_5_ + 0.5F, p_77648_6_ + 0.5F,
							field_150935_a.stepSound.func_150496_b(),
							(field_150935_a.stepSound.getVolume() + 1.0F) / 2.0F,
							field_150935_a.stepSound.getPitch() * 0.8F);
					--p_77648_1_.stackSize;
				}
			}

			return true;
		}
	}
}