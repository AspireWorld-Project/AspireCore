package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.List;
import java.util.Random;

public class BlockRailDetector extends BlockRailBase {
	@SideOnly(Side.CLIENT)
	private IIcon[] field_150055_b;
	public BlockRailDetector() {
		super(true);
		setTickRandomly(true);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 20;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity p_149670_5_) {
		if (!p_149670_1_.isRemote) {
			int l = p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_);

			if ((l & 8) == 0) {
				func_150054_a(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, l);
			}
		}
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

			if ((l & 8) != 0) {
				func_150054_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, l);
			}
		}
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return (p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_) & 8) != 0 ? 15 : 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		return (p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_) & 8) == 0 ? 0
				: p_149748_5_ == 1 ? 15 : 0;
	}

	@SuppressWarnings("rawtypes")
	private void func_150054_a(World world, int x, int y, int z, int p_150054_5_) {
		boolean flag = (p_150054_5_ & 8) != 0;
		boolean flag1 = false;
		float f = 0.125F;
		List list = world.getEntitiesWithinAABB(EntityMinecart.class,
				AxisAlignedBB.getBoundingBox(x + f, y, z + f, x + 1 - f, y + 1 - f, z + 1 - f));
		if (!list.isEmpty()) {
			flag1 = true;
		}

		if (flag != flag1) {
			org.bukkit.block.Block block = world.getWorld().getBlockAt(x, y, z);
			BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
			Bukkit.getPluginManager().callEvent(eventRedstone);
			flag1 = eventRedstone.getNewCurrent() > 0;
		}

		if (flag1 && !flag) {
			world.setBlockMetadataWithNotify(x, y, z, p_150054_5_ | 8, 3);
			world.notifyBlocksOfNeighborChange(x, y, z, this);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
			world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}

		if (!flag1 && flag) {
			world.setBlockMetadataWithNotify(x, y, z, p_150054_5_ & 7, 3);
			world.notifyBlocksOfNeighborChange(x, y, z, this);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
			world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}

		if (flag1) {
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}

		world.func_147453_f(x, y, z, this);
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		func_150054_a(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_,
				p_149726_1_.getBlockMetadata(p_149726_2_, p_149726_3_, p_149726_4_));
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		if ((p_149736_1_.getBlockMetadata(p_149736_2_, p_149736_3_, p_149736_4_) & 8) > 0) {
			float f = 0.125F;
			List list = p_149736_1_.getEntitiesWithinAABB(EntityMinecartCommandBlock.class,
					AxisAlignedBB.getBoundingBox(p_149736_2_ + f, p_149736_3_, p_149736_4_ + f, p_149736_2_ + 1 - f,
							p_149736_3_ + 1 - f, p_149736_4_ + 1 - f));

			if (list.size() > 0)
				return ((EntityMinecartCommandBlock) list.get(0)).func_145822_e().func_145760_g();

			List list1 = p_149736_1_.selectEntitiesWithinAABB(
					EntityMinecart.class, AxisAlignedBB.getBoundingBox(p_149736_2_ + f, p_149736_3_, p_149736_4_ + f,
							p_149736_2_ + 1 - f, p_149736_3_ + 1 - f, p_149736_4_ + 1 - f),
					IEntitySelector.selectInventories);

			if (list1.size() > 0)
				return Container.calcRedstoneFromInventory((IInventory) list1.get(0));
		}

		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150055_b = new IIcon[2];
		field_150055_b[0] = p_149651_1_.registerIcon(getTextureName());
		field_150055_b[1] = p_149651_1_.registerIcon(getTextureName() + "_powered");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return (p_149691_2_ & 8) != 0 ? field_150055_b[1] : field_150055_b[0];
	}
}