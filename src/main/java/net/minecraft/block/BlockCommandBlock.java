package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCommandBlock extends BlockContainer {
	public BlockCommandBlock() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCommandBlock();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
			int l = world.getBlockMetadata(x, y, z);
			boolean flag1 = (l & 1) != 0;
			if (flag && !flag1) {
				world.setBlockMetadataWithNotify(x, y, z, l | 1, 4);
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
			} else if (!flag && flag1) {
				world.setBlockMetadataWithNotify(x, y, z, l & -2, 4);
			}
		}

	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		TileEntity tileentity = p_149674_1_.getTileEntity(p_149674_2_, p_149674_3_, p_149674_4_);

		if (tileentity != null && tileentity instanceof TileEntityCommandBlock) {
			CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock) tileentity).func_145993_a();
			commandblocklogic.func_145755_a(p_149674_1_);
			p_149674_1_.func_147453_f(p_149674_2_, p_149674_3_, p_149674_4_, this);
		}
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 1;
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock) p_149727_1_.getTileEntity(p_149727_2_,
				p_149727_3_, p_149727_4_);

		if (tileentitycommandblock != null) {
			p_149727_5_.func_146100_a(tileentitycommandblock);
		}

		return true;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		TileEntity tileentity = p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_);
		return tileentity != null && tileentity instanceof TileEntityCommandBlock
				? ((TileEntityCommandBlock) tileentity).func_145993_a().func_145760_g()
				: 0;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock) p_149689_1_.getTileEntity(p_149689_2_,
				p_149689_3_, p_149689_4_);

		if (p_149689_6_.hasDisplayName()) {
			tileentitycommandblock.func_145993_a().func_145754_b(p_149689_6_.getDisplayName());
		}
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}
}