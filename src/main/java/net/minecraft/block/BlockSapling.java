package net.minecraft.block;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.ultramine.bukkit.util.UMTreeTypeToBukkit;
import org.ultramine.core.util.UMTreeType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable {
	public static final String[] field_149882_a = new String[] { "oak", "spruce", "birch", "jungle", "acacia",
			"roofed_oak" };
	private static final IIcon[] field_149881_b = new IIcon[field_149882_a.length];
	private static final String __OBFID = "CL_00000305";
	public static UMTreeType treeType;

	protected BlockSapling() {
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, random);
			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
				world.setCaptureTreeGeneration(true);
				func_149879_c(world, x, y, z, random);
				world.setCaptureTreeGeneration(false);
				if (world.capturedBlockSnapshots.size() > 0) {
					TreeType treeType = UMTreeTypeToBukkit.getBukkitTreeType(BlockSapling.treeType);
					BlockSapling.treeType = null;
					Location location = new Location(world.getWorld(), x, y, z);
					List<net.minecraftforge.common.util.BlockSnapshot> blocks = (List<net.minecraftforge.common.util.BlockSnapshot>) world.capturedBlockSnapshots
							.clone();
					List<BlockState> blockstates = new java.util.ArrayList<>();
					for (net.minecraftforge.common.util.BlockSnapshot snapshot : blocks) {
						blockstates.add(new org.bukkit.craftbukkit.block.CraftBlockState(snapshot));
					}
					world.capturedBlockSnapshots.clear();
					StructureGrowEvent event = null;
					if (treeType != null) {
						event = new StructureGrowEvent(location, treeType, false, null, blockstates);
						org.bukkit.Bukkit.getPluginManager().callEvent(event);
					}
					if (event == null || !event.isCancelled()) {
						for (BlockState blockstate : blockstates) {
							blockstate.update(true);
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		p_149691_2_ &= 7;
		return field_149881_b[MathHelper.clamp_int(p_149691_2_, 0, 5)];
	}

	public void func_149879_c(World p_149879_1_, int p_149879_2_, int p_149879_3_, int p_149879_4_,
			Random p_149879_5_) {
		int l = p_149879_1_.getBlockMetadata(p_149879_2_, p_149879_3_, p_149879_4_);

		if ((l & 8) == 0) {
			p_149879_1_.setBlockMetadataWithNotify(p_149879_2_, p_149879_3_, p_149879_4_, l | 8, 4);
		} else {
			func_149878_d(p_149879_1_, p_149879_2_, p_149879_3_, p_149879_4_, p_149879_5_);
		}
	}

	public void func_149878_d(World p_149878_1_, int p_149878_2_, int p_149878_3_, int p_149878_4_,
			Random p_149878_5_) {
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(p_149878_1_, p_149878_5_, p_149878_2_,
				p_149878_3_, p_149878_4_))
			return;
		int l = p_149878_1_.getBlockMetadata(p_149878_2_, p_149878_3_, p_149878_4_) & 7;
		Object object;
		if (p_149878_5_.nextInt(10) == 0) {
			treeType = UMTreeType.BIG_TREE;
			object = new WorldGenBigTree(true);
		} else {
			treeType = UMTreeType.TREE;
			object = new WorldGenTrees(true);
		}
		int i1 = 0;
		int j1 = 0;
		boolean flag = false;

		switch (l) {
		case 0:
		default:
			break;
		case 1:
			treeType = UMTreeType.REDWOOD;
			label78:

			for (i1 = 0; i1 >= -1; --i1) {
				for (j1 = 0; j1 >= -1; --j1) {
					if (func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1, 1)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1, 1)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1 + 1, 1)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1 + 1, 1)) {
						object = new WorldGenMegaPineTree(false, p_149878_5_.nextBoolean());
						flag = true;
						break label78;
					}
				}
			}

			if (!flag) {
				j1 = 0;
				i1 = 0;
				object = new WorldGenTaiga2(true);
			}

			break;
		case 2:
			treeType = UMTreeType.BIRCH;
			object = new WorldGenForest(true, false);
			break;
		case 3:
			label93:

			for (i1 = 0; i1 >= -1; --i1) {
				for (j1 = 0; j1 >= -1; --j1) {
					if (func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1, 3)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1, 3)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1 + 1, 3)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1 + 1, 3)) {
						treeType = UMTreeType.JUNGLE;
						object = new WorldGenMegaJungle(true, 10, 20, 3, 3);
						flag = true;
						break label93;
					}
				}
			}

			if (!flag) {
				j1 = 0;
				i1 = 0;
				treeType = UMTreeType.SMALL_JUNGLE;
				object = new WorldGenTrees(true, 4 + p_149878_5_.nextInt(7), 3, 3, false);
			}

			break;
		case 4:
			treeType = UMTreeType.ACACIA;
			object = new WorldGenSavannaTree(true);
			break;
		case 5:
			label108:

			for (i1 = 0; i1 >= -1; --i1) {
				for (j1 = 0; j1 >= -1; --j1) {
					if (func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1, 5)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1, 5)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1 + 1, 5)
							&& func_149880_a(p_149878_1_, p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1 + 1, 5)) {
						object = new WorldGenCanopyTree(true);
						treeType = UMTreeType.DARK_OAK;
						flag = true;
						break label108;
					}
				}
			}

			if (!flag)
				return;
		}

		Block block = Blocks.air;

		if (flag) {
			p_149878_1_.setBlock(p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1, block, 0, 4);
			p_149878_1_.setBlock(p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1, block, 0, 4);
			p_149878_1_.setBlock(p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1 + 1, block, 0, 4);
			p_149878_1_.setBlock(p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1 + 1, block, 0, 4);
		} else {
			p_149878_1_.setBlock(p_149878_2_, p_149878_3_, p_149878_4_, block, 0, 4);
		}

		if (!((WorldGenerator) object).generate(p_149878_1_, p_149878_5_, p_149878_2_ + i1, p_149878_3_,
				p_149878_4_ + j1)) {
			if (flag) {
				p_149878_1_.setBlock(p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1, this, l, 4);
				p_149878_1_.setBlock(p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1, this, l, 4);
				p_149878_1_.setBlock(p_149878_2_ + i1, p_149878_3_, p_149878_4_ + j1 + 1, this, l, 4);
				p_149878_1_.setBlock(p_149878_2_ + i1 + 1, p_149878_3_, p_149878_4_ + j1 + 1, this, l, 4);
			} else {
				p_149878_1_.setBlock(p_149878_2_, p_149878_3_, p_149878_4_, this, l, 4);
			}
		}
	}

	public boolean func_149880_a(World p_149880_1_, int p_149880_2_, int p_149880_3_, int p_149880_4_,
			int p_149880_5_) {
		return p_149880_1_.getBlock(p_149880_2_, p_149880_3_, p_149880_4_) == this
				&& (p_149880_1_.getBlockMetadata(p_149880_2_, p_149880_3_, p_149880_4_) & 7) == p_149880_5_;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return MathHelper.clamp_int(p_149692_1_ & 7, 0, 5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 3));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 4));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 5));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		for (int i = 0; i < field_149881_b.length; ++i) {
			field_149881_b[i] = p_149651_1_.registerIcon(getTextureName() + "_" + field_149882_a[i]);
		}
	}

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_,
			boolean p_149851_5_) {
		return true;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_,
			int p_149852_5_) {
		return p_149852_1_.rand.nextFloat() < 0.45D;
	}

	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_,
			int p_149853_5_) {
		func_149879_c(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
	}
}