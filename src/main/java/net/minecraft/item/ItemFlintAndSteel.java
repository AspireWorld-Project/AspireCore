package net.minecraft.item;

import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemFlintAndSteel extends Item {
	private static final String __OBFID = "CL_00000035";

	public ItemFlintAndSteel() {
		maxStackSize = 1;
		setMaxDamage(64);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		int clickedX = p_77648_4_, clickedY = p_77648_5_, clickedZ = p_77648_6_; // CraftBukkit

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
			if (p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_, p_77648_6_)) {
				// CraftBukkit start - Store the clicked block
				if (CraftEventFactory
						.callBlockIgniteEvent(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_,
								org.bukkit.event.block.BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, p_77648_2_)
						.isCancelled()) {
					p_77648_1_.damageItem(1, p_77648_2_);
					return false;
				}

				CraftBlockState blockState = CraftBlockState.getBlockState(p_77648_3_, p_77648_4_, p_77648_5_,
						p_77648_6_);
				// CraftBukkit end
				p_77648_3_.playSoundEffect(p_77648_4_ + 0.5D, p_77648_5_ + 0.5D, p_77648_6_ + 0.5D, "fire.ignite", 1.0F,
						itemRand.nextFloat() * 0.4F + 0.8F);
				p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.fire);
				// CraftBukkit start
				org.bukkit.event.block.BlockPlaceEvent placeEvent = CraftEventFactory.callBlockPlaceEvent(p_77648_3_,
						p_77648_2_, blockState, clickedX, clickedY, clickedZ);

				if (placeEvent.isCancelled() || !placeEvent.canBuild()) {
					p_77648_3_.setBlockSilently(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.air, 0, 2);
					return false;
				}

				// CraftBukkit end
			}

			p_77648_1_.damageItem(1, p_77648_2_);
			return true;
		}
	}
}