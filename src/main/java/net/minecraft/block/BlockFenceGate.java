package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockDirectional {
	private static final String __OBFID = "CL_00000243";

	public BlockFenceGate() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.planks.getBlockTextureFromSide(p_149691_1_);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return !p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_).getMaterial().isSolid() ? false
				: super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		int l = p_149668_1_.getBlockMetadata(p_149668_2_, p_149668_3_, p_149668_4_);
		return isFenceGateOpen(l) ? null
				: l != 2 && l != 0
						? AxisAlignedBB.getBoundingBox(p_149668_2_ + 0.375F, p_149668_3_, p_149668_4_,
								p_149668_2_ + 0.625F, p_149668_3_ + 1.5F, p_149668_4_ + 1)
						: AxisAlignedBB.getBoundingBox(p_149668_2_, p_149668_3_, p_149668_4_ + 0.375F, p_149668_2_ + 1,
								p_149668_3_ + 1.5F, p_149668_4_ + 0.625F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = getDirection(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));

		if (l != 2 && l != 0) {
			setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		} else {
			setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}
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
		return isFenceGateOpen(p_149655_1_.getBlockMetadata(p_149655_2_, p_149655_3_, p_149655_4_));
	}

	@Override
	public int getRenderType() {
		return 21;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = (MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);

		if (isFenceGateOpen(i1)) {
			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, i1 & -5, 2);
		} else {
			int j1 = (MathHelper.floor_double(p_149727_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) % 4;
			int k1 = getDirection(i1);

			if (k1 == (j1 + 2) % 4) {
				i1 = j1;
			}

			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, i1 | 4, 2);
		}

		p_149727_1_.playAuxSFXAtEntity(p_149727_5_, 1003, p_149727_2_, p_149727_3_, p_149727_4_, 0);
		return true;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!p_149695_1_.isRemote) {
			int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
			boolean flag = p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_);

			if (flag || p_149695_5_.canProvidePower()) {
				if (flag && !isFenceGateOpen(l)) {
					p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l | 4, 2);
					p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003, p_149695_2_, p_149695_3_, p_149695_4_, 0);
				} else if (!flag && isFenceGateOpen(l)) {
					p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l & -5, 2);
					p_149695_1_.playAuxSFXAtEntity((EntityPlayer) null, 1003, p_149695_2_, p_149695_3_, p_149695_4_, 0);
				}
			}
		}
	}

	public static boolean isFenceGateOpen(int p_149896_0_) {
		return (p_149896_0_ & 4) != 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
			int p_149646_5_) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}
}