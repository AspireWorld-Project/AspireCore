package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.ArrayList;
import java.util.Random;

public class BlockIce extends BlockBreakable {
	public BlockIce() {
		super("ice", Material.ice, false);
		slipperiness = 0.98F;
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
			int p_149646_5_) {
		return super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, 1 - p_149646_5_);
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_,
			int p_149636_5_, int p_149636_6_) {
		p_149636_2_.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
		p_149636_2_.addExhaustion(0.025F);

		if (this.canSilkHarvest(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_)
				&& EnchantmentHelper.getSilkTouchModifier(p_149636_2_)) {
			ArrayList<ItemStack> items = new ArrayList<>();
			ItemStack itemstack = createStackedBlock(p_149636_6_);

			if (itemstack != null) {
				items.add(itemstack);
			}

			ForgeEventFactory.fireBlockHarvesting(items, p_149636_1_, this, p_149636_3_, p_149636_4_, p_149636_5_,
					p_149636_6_, 0, 1.0f, true, p_149636_2_);
			for (ItemStack is : items) {
				this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, is);
			}
		} else {
			if (p_149636_1_.provider.isHellWorld) {
				p_149636_1_.setBlockToAir(p_149636_3_, p_149636_4_, p_149636_5_);
				return;
			}

			int i1 = EnchantmentHelper.getFortuneModifier(p_149636_2_);
			harvesters.set(p_149636_2_);
			this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, i1);
			harvesters.set(null);
			Material material = p_149636_1_.getBlock(p_149636_3_, p_149636_4_ - 1, p_149636_5_).getMaterial();

			if (material.blocksMovement() || material.isLiquid()) {
				p_149636_1_.setBlock(p_149636_3_, p_149636_4_, p_149636_5_, Blocks.flowing_water);
			}
		}
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (CraftEventFactory.callBlockFadeEvent(
				p_149674_1_.getWorld().getBlockAt(p_149674_2_, p_149674_3_, p_149674_4_), Blocks.water).isCancelled())
			return;
		if (p_149674_1_.getSavedLightValue(EnumSkyBlock.Block, p_149674_2_, p_149674_3_, p_149674_4_) > 11
				- this.getLightOpacity()) {
			if (p_149674_1_.provider.isHellWorld) {
				p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
				return;
			}

			this.dropBlockAsItem(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
					p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_), 0);
			p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, Blocks.water);
		}
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}
}