package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockIgniteEvent;

public class ItemFireball extends Item {
	private static final String __OBFID = "CL_00000029";

	public ItemFireball() {
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (p_77648_3_.isRemote)
			return true;
		else {
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

			if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
				return false;
			else {
				if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_).getMaterial() == Material.air) {

					if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(p_77648_3_, p_77648_4_,
							p_77648_5_, p_77648_6_, BlockIgniteEvent.IgniteCause.FIREBALL, p_77648_2_).isCancelled()) {
						if (!p_77648_2_.capabilities.isCreativeMode) {
							--p_77648_1_.stackSize;
						}
						return false;
					}
					p_77648_3_.playSoundEffect(p_77648_4_ + 0.5D, p_77648_5_ + 0.5D, p_77648_6_ + 0.5D, "fire.ignite",
							1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
					p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.fire);
				}

				if (!p_77648_2_.capabilities.isCreativeMode) {
					--p_77648_1_.stackSize;
				}

				return true;
			}
		}
	}
}