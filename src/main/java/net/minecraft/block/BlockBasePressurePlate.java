package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.Random;

public abstract class BlockBasePressurePlate extends Block {
	private final String field_150067_a;
	protected BlockBasePressurePlate(String p_i45387_1_, Material p_i45387_2_) {
		super(p_i45387_2_);
		field_150067_a = p_i45387_1_;
		setCreativeTab(CreativeTabs.tabRedstone);
		setTickRandomly(true);
		func_150063_b(func_150066_d(15));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		func_150063_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));
	}

	protected void func_150063_b(int p_150063_1_) {
		boolean flag = func_150060_c(p_150063_1_) > 0;
		float f = 0.0625F;

		if (flag) {
			setBlockBounds(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
		} else {
			setBlockBounds(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
		}
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 20;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_)
				|| BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_));
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		boolean flag = !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_)
                && !BlockFence.func_149825_a(p_149695_1_.getBlock(p_149695_2_, p_149695_3_ - 1, p_149695_4_));

        if (flag) {
			this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			int l = func_150060_c(p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_));

			if (l > 0) {
				func_150062_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, l);
			}
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity p_149670_5_) {
		if (!p_149670_1_.isRemote) {
			int l = func_150060_c(p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_));

			if (l == 0) {
				func_150062_a(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, l);
			}
		}
	}

	protected void func_150062_a(World world, int x, int y, int z, int p_150062_5_) {
		int i1 = func_150065_e(world, x, y, z);
		boolean flag = p_150062_5_ > 0;
		boolean flag1 = i1 > 0;
		if (flag != flag1) {
			BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(world.getWorld().getBlockAt(x, y, z), p_150062_5_,
					i1);
			Bukkit.getPluginManager().callEvent(eventRedstone);
			flag1 = eventRedstone.getNewCurrent() > 0;
			i1 = eventRedstone.getNewCurrent();
		}
		if (p_150062_5_ != i1) {
			world.setBlockMetadataWithNotify(x, y, z, func_150066_d(i1), 2);
			func_150064_a_(world, x, y, z);
			world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}

		if (!flag1 && flag) {
			world.playSoundEffect(x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.5F);
		} else if (flag1 && !flag) {
			world.playSoundEffect(x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if (flag1) {
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}

	protected AxisAlignedBB func_150061_a(int p_150061_1_, int p_150061_2_, int p_150061_3_) {
		float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(p_150061_1_ + f, p_150061_2_, p_150061_3_ + f, p_150061_1_ + 1 - f,
				p_150061_2_ + 0.25D, p_150061_3_ + 1 - f);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		if (func_150060_c(p_149749_6_) > 0) {
			func_150064_a_(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	protected void func_150064_a_(World p_150064_1_, int p_150064_2_, int p_150064_3_, int p_150064_4_) {
		p_150064_1_.notifyBlocksOfNeighborChange(p_150064_2_, p_150064_3_, p_150064_4_, this);
		p_150064_1_.notifyBlocksOfNeighborChange(p_150064_2_, p_150064_3_ - 1, p_150064_4_, this);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return func_150060_c(p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_));
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return p_149748_5_ == 1 ? func_150060_c(p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_))
				: 0;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float f = 0.5F;
		float f1 = 0.125F;
		float f2 = 0.5F;
		setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	protected abstract int func_150065_e(World p_150065_1_, int p_150065_2_, int p_150065_3_, int p_150065_4_);

	protected abstract int func_150060_c(int p_150060_1_);

	protected abstract int func_150066_d(int p_150066_1_);

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(field_150067_a);
	}
}