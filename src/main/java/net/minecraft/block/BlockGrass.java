package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import java.util.Random;

public class BlockGrass extends Block implements IGrowable {
	@SideOnly(Side.CLIENT)
	private IIcon field_149991_b;
	@SideOnly(Side.CLIENT)
	private IIcon field_149993_M;
	@SideOnly(Side.CLIENT)
	private IIcon field_149994_N;
	protected BlockGrass() {
		super(Material.grass);
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149991_b
				: p_149691_1_ == 0 ? Blocks.dirt.getBlockTextureFromSide(p_149691_1_) : blockIcon;
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			if (p_149674_1_.getBlockLightValue(p_149674_2_, p_149674_3_ + 1, p_149674_4_) < 4
					&& p_149674_1_.getBlockLightOpacity(p_149674_2_, p_149674_3_ + 1, p_149674_4_) > 2) {
				org.bukkit.World bworld = p_149674_1_.getWorld();
				BlockState blockState = bworld.getBlockAt(p_149674_2_, p_149674_3_, p_149674_4_).getState();
				blockState.setTypeId(Block.getIdFromBlock(Blocks.dirt));
				BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
				p_149674_1_.getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					blockState.update(true);
				}
			} else if (p_149674_1_.getBlockLightValue(p_149674_2_, p_149674_3_ + 1, p_149674_4_) >= 9) {
				for (int l = 0; l < 4; ++l) {
					int i1 = p_149674_2_ + p_149674_5_.nextInt(3) - 1;
					int j1 = p_149674_3_ + p_149674_5_.nextInt(5) - 3;
					int k1 = p_149674_4_ + p_149674_5_.nextInt(3) - 1;
					p_149674_1_.getBlock(i1, j1 + 1, k1);

					if (p_149674_1_.getBlock(i1, j1, k1) == Blocks.dirt && p_149674_1_.getBlockMetadata(i1, j1, k1) == 0
							&& p_149674_1_.getBlockLightValue(i1, j1 + 1, k1) >= 4
							&& p_149674_1_.getBlockLightOpacity(i1, j1 + 1, k1) <= 2) {
						org.bukkit.World bworld = p_149674_1_.getWorld();
						BlockState blockState = bworld.getBlockAt(i1, j1, k1).getState();
						blockState.setTypeId(Block.getIdFromBlock(Blocks.grass));
						BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(),
								bworld.getBlockAt(p_149674_2_, p_149674_3_, p_149674_4_), blockState);
						p_149674_1_.getServer().getPluginManager().callEvent(event);
						if (!event.isCancelled()) {
							blockState.update(true);
						}
					}
				}
			}
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Blocks.dirt.getItemDropped(0, p_149650_2_, p_149650_3_);
	}

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_,
			boolean p_149851_5_) {
		return true;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_,
			int p_149852_5_) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_) {
		if (p_149673_5_ == 1)
			return field_149991_b;
		else if (p_149673_5_ == 0)
			return Blocks.dirt.getBlockTextureFromSide(p_149673_5_);
		else {
			Material material = p_149673_1_.getBlock(p_149673_2_, p_149673_3_ + 1, p_149673_4_).getMaterial();
			return material != Material.snow && material != Material.craftedSnow ? blockIcon : field_149993_M;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_149991_b = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_149993_M = p_149651_1_.registerIcon(getTextureName() + "_side_snowed");
		field_149994_N = p_149651_1_.registerIcon(getTextureName() + "_side_overlay");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		double d0 = 0.5D;
		double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int p_149741_1_) {
		return getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		int l = 0;
		int i1 = 0;
		int j1 = 0;

		for (int k1 = -1; k1 <= 1; ++k1) {
			for (int l1 = -1; l1 <= 1; ++l1) {
				int i2 = p_149720_1_.getBiomeGenForCoords(p_149720_2_ + l1, p_149720_4_ + k1)
						.getBiomeGrassColor(p_149720_2_ + l1, p_149720_3_, p_149720_4_ + k1);
				l += (i2 & 16711680) >> 16;
				i1 += (i2 & 65280) >> 8;
				j1 += i2 & 255;
			}
		}

		return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getIconSideOverlay() {
		return Blocks.grass.field_149994_N;
	}

	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_,
			int p_149853_5_) {
		int l = 0;

		while (l < 128) {
			int i1 = p_149853_3_;
			int j1 = p_149853_4_ + 1;
			int k1 = p_149853_5_;
			int l1 = 0;

			while (true) {
				if (l1 < l / 16) {
					i1 += p_149853_2_.nextInt(3) - 1;
					j1 += (p_149853_2_.nextInt(3) - 1) * p_149853_2_.nextInt(3) / 2;
					k1 += p_149853_2_.nextInt(3) - 1;

					if (p_149853_1_.getBlock(i1, j1 - 1, k1) == Blocks.grass
							&& !p_149853_1_.getBlock(i1, j1, k1).isNormalCube()) {
						++l1;
						continue;
					}
				} else if (p_149853_1_.getBlock(i1, j1, k1).blockMaterial == Material.air) {
					if (p_149853_2_.nextInt(8) != 0) {
						if (Blocks.tallgrass.canBlockStay(p_149853_1_, i1, j1, k1)) {
							p_149853_1_.setBlock(i1, j1, k1, Blocks.tallgrass, 1, 3);
						}
					} else {
						p_149853_1_.getBiomeGenForCoords(i1, k1).plantFlower(p_149853_1_, p_149853_2_, i1, j1, k1);
					}
				}

				++l;
				break;
			}
		}
	}
}