package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class BlockRedstoneOre extends Block {
	private boolean field_150187_a;
	private static final String __OBFID = "CL_00000294";

	public BlockRedstoneOre(boolean p_i45420_1_) {
		super(Material.rock);

		if (p_i45420_1_) {
			setTickRandomly(true);
		}

		field_150187_a = p_i45420_1_;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 30;
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
			EntityPlayer p_149699_5_) {
		func_150185_e(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_);
		super.onBlockClicked(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_, p_149699_5_);
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		if (entity instanceof EntityPlayer) {
			PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity,
					Action.PHYSICAL, x, y, z, -1, null);
			if (!event.isCancelled()) {
				func_150185_e(world, x, y, z);
				super.onEntityWalking(world, x, y, z, entity);
			}
		} else {
			EntityInteractEvent event = new EntityInteractEvent(entity.getBukkitEntity(),
					world.getWorld().getBlockAt(x, y, z));
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				func_150185_e(world, x, y, z);
				super.onEntityWalking(world, x, y, z, entity);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		func_150185_e(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
		return super.onBlockActivated(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, p_149727_5_, p_149727_6_,
				p_149727_7_, p_149727_8_, p_149727_9_);
	}

	private void func_150185_e(World p_150185_1_, int p_150185_2_, int p_150185_3_, int p_150185_4_) {
		func_150186_m(p_150185_1_, p_150185_2_, p_150185_3_, p_150185_4_);

		if (this == Blocks.redstone_ore) {
			p_150185_1_.setBlock(p_150185_2_, p_150185_3_, p_150185_4_, Blocks.lit_redstone_ore);
		}
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (this == Blocks.lit_redstone_ore) {
			p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, Blocks.redstone_ore);
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Items.redstone;
	}

	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		return this.quantityDropped(p_149679_2_) + p_149679_2_.nextInt(p_149679_1_ + 1);
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 4 + p_149745_1_.nextInt(2);
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_,
				p_149690_7_);
	}

	private Random rand = new Random();

	@Override // World, meta, fortune
	public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_) {
		if (getItemDropped(p_149690_5_, rand, p_149690_7_) != Item.getItemFromBlock(this))
			return 1 + rand.nextInt(5);
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		if (field_150187_a) {
			func_150186_m(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_);
		}
	}

	private void func_150186_m(World p_150186_1_, int p_150186_2_, int p_150186_3_, int p_150186_4_) {
		Random random = p_150186_1_.rand;
		double d0 = 0.0625D;

		for (int l = 0; l < 6; ++l) {
			double d1 = p_150186_2_ + random.nextFloat();
			double d2 = p_150186_3_ + random.nextFloat();
			double d3 = p_150186_4_ + random.nextFloat();

			if (l == 0 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ + 1, p_150186_4_).isOpaqueCube()) {
				d2 = p_150186_3_ + 1 + d0;
			}

			if (l == 1 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_ - 1, p_150186_4_).isOpaqueCube()) {
				d2 = p_150186_3_ + 0 - d0;
			}

			if (l == 2 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ + 1).isOpaqueCube()) {
				d3 = p_150186_4_ + 1 + d0;
			}

			if (l == 3 && !p_150186_1_.getBlock(p_150186_2_, p_150186_3_, p_150186_4_ - 1).isOpaqueCube()) {
				d3 = p_150186_4_ + 0 - d0;
			}

			if (l == 4 && !p_150186_1_.getBlock(p_150186_2_ + 1, p_150186_3_, p_150186_4_).isOpaqueCube()) {
				d1 = p_150186_2_ + 1 + d0;
			}

			if (l == 5 && !p_150186_1_.getBlock(p_150186_2_ - 1, p_150186_3_, p_150186_4_).isOpaqueCube()) {
				d1 = p_150186_2_ + 0 - d0;
			}

			if (d1 < p_150186_2_ || d1 > p_150186_2_ + 1 || d2 < 0.0D || d2 > p_150186_3_ + 1 || d3 < p_150186_4_
					|| d3 > p_150186_4_ + 1) {
				p_150186_1_.spawnParticle("reddust", d1, d2, d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return new ItemStack(Blocks.redstone_ore);
	}
}