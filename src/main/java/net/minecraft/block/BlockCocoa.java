package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.ArrayList;
import java.util.Random;

public class BlockCocoa extends BlockDirectional implements IGrowable {
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149989_a;
	public BlockCocoa() {
		super(Material.plants);
		setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return field_149989_a[2];
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!canBlockStay(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_)) {
			this.dropBlockAsItem(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
					p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_), 0);
			p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, getBlockById(0), 0, 2);
		} else if (p_149674_1_.rand.nextInt(5) == 0) {
			int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);
			int i1 = func_149987_c(l);

			if (i1 < 2) {
				++i1;

				CraftEventFactory.handleBlockGrowEvent(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, this,
						i1 << 2 | getDirection(l));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getCocoaIcon(int p_149988_1_) {
		if (p_149988_1_ < 0 || p_149988_1_ >= field_149989_a.length) {
			p_149988_1_ = field_149989_a.length - 1;
		}

		return field_149989_a[p_149988_1_];
	}

	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
		int l = getDirection(p_149718_1_.getBlockMetadata(p_149718_2_, p_149718_3_, p_149718_4_));
		p_149718_2_ += Direction.offsetX[l];
		p_149718_4_ += Direction.offsetZ[l];
		Block block = p_149718_1_.getBlock(p_149718_2_, p_149718_3_, p_149718_4_);
		return block == Blocks.log
				&& BlockLog.func_150165_c(p_149718_1_.getBlockMetadata(p_149718_2_, p_149718_3_, p_149718_4_)) == 3;
	}

	@Override
	public int getRenderType() {
		return 28;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
		return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
		int i1 = getDirection(l);
		int j1 = func_149987_c(l);
		int k1 = 4 + j1 * 2;
		int l1 = 5 + j1 * 2;
		float f = k1 / 2.0F;

		switch (i1) {
		case 0:
			setBlockBounds((8.0F - f) / 16.0F, (12.0F - l1) / 16.0F, (15.0F - k1) / 16.0F, (8.0F + f) / 16.0F, 0.75F,
					0.9375F);
			break;
		case 1:
			setBlockBounds(0.0625F, (12.0F - l1) / 16.0F, (8.0F - f) / 16.0F, (1.0F + k1) / 16.0F, 0.75F,
					(8.0F + f) / 16.0F);
			break;
		case 2:
			setBlockBounds((8.0F - f) / 16.0F, (12.0F - l1) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F,
					(1.0F + k1) / 16.0F);
			break;
		case 3:
			setBlockBounds((15.0F - k1) / 16.0F, (12.0F - l1) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F,
					(8.0F + f) / 16.0F);
		}
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = ((MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) + 0) % 4;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		if (p_149660_5_ == 1 || p_149660_5_ == 0) {
			p_149660_5_ = 2;
		}

		return Direction.rotateOpposite[Direction.facingToDirection[p_149660_5_]];
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!canBlockStay(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
					p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, getBlockById(0), 0, 2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_,
			int p_149633_4_) {
		setBlockBoundsBasedOnState(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
		return super.getSelectedBoundingBoxFromPool(p_149633_1_, p_149633_2_, p_149633_3_, p_149633_4_);
	}

	public static int func_149987_c(int p_149987_0_) {
		return (p_149987_0_ & 12) >> 2;
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_,
				p_149690_7_);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int p_149690_5_, int fortune) {
		ArrayList<ItemStack> dropped = super.getDrops(world, x, y, z, p_149690_5_, fortune);
		int j1 = func_149987_c(p_149690_5_);
		byte b0 = 1;

		if (j1 >= 2) {
			b0 = 3;
		}

		for (int k1 = 0; k1 < b0; ++k1) {
			dropped.add(new ItemStack(Items.dye, 1, 3));
		}
		return dropped;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.dye;
	}

	@Override
	public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_) {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149989_a = new IIcon[3];

		for (int i = 0; i < field_149989_a.length; ++i) {
			field_149989_a[i] = p_149651_1_.registerIcon(getTextureName() + "_stage_" + i);
		}
	}

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_,
			boolean p_149851_5_) {
		int l = p_149851_1_.getBlockMetadata(p_149851_2_, p_149851_3_, p_149851_4_);
		int i1 = func_149987_c(l);
		return i1 < 2;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_,
			int p_149852_5_) {
		return true;
	}

	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_,
			int p_149853_5_) {
		int l = p_149853_1_.getBlockMetadata(p_149853_3_, p_149853_4_, p_149853_5_);
		int i1 = BlockDirectional.getDirection(l);
		int j1 = func_149987_c(l);
		++j1;
		p_149853_1_.setBlockMetadataWithNotify(p_149853_3_, p_149853_4_, p_149853_5_, j1 << 2 | i1, 2);
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		return null;
	}
}