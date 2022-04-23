package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.Random;

public class BlockDaylightDetector extends BlockContainer {
	private final IIcon[] field_149958_a = new IIcon[2];
	private static final String __OBFID = "CL_00000223";

	public BlockDaylightDetector() {
		super(Material.wood);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
	}

	public void func_149957_e(World world, int x, int y, int z) {
		if (!world.provider.hasNoSky) {
			int l = world.getBlockMetadata(x, y, z);
			int i1 = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
			float f = world.getCelestialAngleRadians(1.0F);
			if (f < 3.1415927F) {
				f += (0.0F - f) * 0.2F;
			} else {
				f += (6.2831855F - f) * 0.2F;
			}

			i1 = Math.round(i1 * MathHelper.cos(f));
			if (i1 < 0) {
				i1 = 0;
			}

			if (i1 > 15) {
				i1 = 15;
			}

			if (l != i1) {
				i1 = CraftEventFactory.callRedstoneChange(world, x, y, z, l, i1).getNewCurrent();
				world.setBlockMetadataWithNotify(x, y, z, i1, 3);
			}
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDaylightDetector();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149958_a[0] : field_149958_a[1];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149958_a[0] = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_149958_a[1] = p_149651_1_.registerIcon(getTextureName() + "_side");
	}
}