package net.minecraft.block;

import java.util.Random;

import org.bukkit.event.block.BlockFromToEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block {
	private static final String __OBFID = "CL_00000232";

	public BlockDragonEgg() {
		super(Material.dragonEgg);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, tickRate(p_149726_1_));
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, tickRate(p_149695_1_));
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		func_150018_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
	}

	private void func_150018_e(World p_150018_1_, int p_150018_2_, int p_150018_3_, int p_150018_4_) {
		if (BlockFalling.func_149831_e(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_) && p_150018_3_ >= 0) {
			byte b0 = 32;

			if (!BlockFalling.fallInstantly && p_150018_1_.checkChunksExist(p_150018_2_ - b0, p_150018_3_ - b0,
					p_150018_4_ - b0, p_150018_2_ + b0, p_150018_3_ + b0, p_150018_4_ + b0)) {
				EntityFallingBlock entityfallingblock = new EntityFallingBlock(p_150018_1_, p_150018_2_ + 0.5F,
						p_150018_3_ + 0.5F, p_150018_4_ + 0.5F, this);
				p_150018_1_.spawnEntityInWorld(entityfallingblock);
			} else {
				p_150018_1_.setBlockToAir(p_150018_2_, p_150018_3_, p_150018_4_);

				while (BlockFalling.func_149831_e(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_)
						&& p_150018_3_ > 0) {
					--p_150018_3_;
				}

				if (p_150018_3_ > 0) {
					p_150018_1_.setBlock(p_150018_2_, p_150018_3_, p_150018_4_, this, 0, 2);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		func_150019_m(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
		return true;
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
			EntityPlayer p_149699_5_) {
		func_150019_m(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_);
	}

	private void func_150019_m(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z) == this) {
			for (int l = 0; l < 1000; ++l) {
				int i1 = x + world.rand.nextInt(16) - world.rand.nextInt(16);
				int j1 = y + world.rand.nextInt(8) - world.rand.nextInt(8);
				int k1 = z + world.rand.nextInt(16) - world.rand.nextInt(16);
				if (world.getBlock(i1, j1, k1).getMaterial() == Material.air) {
					org.bukkit.block.Block from = world.getWorld().getBlockAt(x, y, z);
					org.bukkit.block.Block to = world.getWorld().getBlockAt(i1, j1, k1);
					BlockFromToEvent event = new BlockFromToEvent(from, to);
					org.bukkit.Bukkit.getPluginManager().callEvent(event);
					if (event.isCancelled())
						return;
					i1 = event.getToBlock().getX();
					j1 = event.getToBlock().getY();
					k1 = event.getToBlock().getZ();
					if (!world.isRemote) {
						world.setBlock(i1, j1, k1, this, world.getBlockMetadata(x, y, z), 2);
						world.setBlockToAir(x, y, z);
					} else {
						short short1 = 128;
						for (int l1 = 0; l1 < short1; ++l1) {
							double d0 = world.rand.nextDouble();
							float f = (world.rand.nextFloat() - 0.5F) * 0.2F;
							float f1 = (world.rand.nextFloat() - 0.5F) * 0.2F;
							float f2 = (world.rand.nextFloat() - 0.5F) * 0.2F;
							double d1 = i1 + (x - i1) * d0 + (world.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
							double d2 = j1 + (y - j1) * d0 + world.rand.nextDouble() * 1.0D - 0.5D;
							double d3 = k1 + (z - k1) * d0 + (world.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
							world.spawnParticle("portal", d1, d2, d3, f, f1, f2);
						}
					}
					return;
				}
			}
		}
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 5;
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
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
			int p_149646_5_) {
		return true;
	}

	@Override
	public int getRenderType() {
		return 27;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemById(0);
	}
}