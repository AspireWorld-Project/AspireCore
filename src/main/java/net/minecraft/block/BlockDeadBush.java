package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BlockDeadBush extends BlockBush implements IShearable {
	protected BlockDeadBush() {
		super(Material.vine);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	@Override
	protected boolean canPlaceBlockOn(Block p_149854_1_) {
		return p_149854_1_ == Blocks.sand || p_149854_1_ == Blocks.hardened_clay
				|| p_149854_1_ == Blocks.stained_hardened_clay || p_149854_1_ == Blocks.dirt;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_,
			int p_149636_5_, int p_149636_6_) {
		{
			super.harvestBlock(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_);
		}
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		return new ArrayList<>(Arrays.asList(new ItemStack(Blocks.deadbush, 1, world.getBlockMetadata(x, y, z))));
	}
}