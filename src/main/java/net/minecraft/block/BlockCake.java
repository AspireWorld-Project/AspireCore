package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCake extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon field_150038_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_150037_b;
	@SideOnly(Side.CLIENT)
	private IIcon field_150039_M;
	private static final String __OBFID = "CL_00000211";

	protected BlockCake() {
		super(Material.cake);
		setTickRandomly(true);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
		float f = 0.0625F;
		float f1 = (1 + l * 2) / 16.0F;
		float f2 = 0.5F;
		setBlockBounds(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float f = 0.0625F;
		float f1 = 0.5F;
		setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		int l = p_149668_1_.getBlockMetadata(p_149668_2_, p_149668_3_, p_149668_4_);
		float f = 0.0625F;
		float f1 = (1 + l * 2) / 16.0F;
		float f2 = 0.5F;
		return AxisAlignedBB.getBoundingBox(p_149668_2_ + f1, p_149668_3_, p_149668_4_ + f, p_149668_2_ + 1 - f,
				p_149668_3_ + f2 - f, p_149668_4_ + 1 - f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_,
			int p_149633_4_) {
		int l = p_149633_1_.getBlockMetadata(p_149633_2_, p_149633_3_, p_149633_4_);
		float f = 0.0625F;
		float f1 = (1 + l * 2) / 16.0F;
		float f2 = 0.5F;
		return AxisAlignedBB.getBoundingBox(p_149633_2_ + f1, p_149633_3_, p_149633_4_ + f, p_149633_2_ + 1 - f,
				p_149633_3_ + f2, p_149633_4_ + 1 - f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_150038_a
				: p_149691_1_ == 0 ? field_150037_b : p_149691_2_ > 0 && p_149691_1_ == 4 ? field_150039_M : blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150039_M = p_149651_1_.registerIcon(getTextureName() + "_inner");
		field_150038_a = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150037_b = p_149651_1_.registerIcon(getTextureName() + "_bottom");
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
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		func_150036_b(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, p_149727_5_);
		return true;
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
			EntityPlayer p_149699_5_) {
		func_150036_b(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_, p_149699_5_);
	}

	private void func_150036_b(World p_150036_1_, int p_150036_2_, int p_150036_3_, int p_150036_4_,
			EntityPlayer p_150036_5_) {
		if (p_150036_5_.canEat(false)) {
			FoodStats mstats = p_150036_5_.getFoodStats();
			int oldFoodLevel = mstats.getFoodLevel();
			org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory
					.callFoodLevelChangeEvent(p_150036_5_, 2 + oldFoodLevel);

			if (!event.isCancelled()) {
				p_150036_5_.getFoodStats().addStats(event.getFoodLevel() - oldFoodLevel, 0.1F);
			}

			mstats.sendUpdatePacket((EntityPlayerMP) p_150036_5_);

			int l = p_150036_1_.getBlockMetadata(p_150036_2_, p_150036_3_, p_150036_4_) + 1;

			if (l >= 6) {
				p_150036_1_.setBlockToAir(p_150036_2_, p_150036_3_, p_150036_4_);
			} else {
				p_150036_1_.setBlockMetadataWithNotify(p_150036_2_, p_150036_3_, p_150036_4_, l, 2);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return !super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_) ? false
				: canBlockStay(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (!canBlockStay(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
		}
	}

	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
		return p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1, p_149718_4_).getMaterial().isSolid();
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.cake;
	}
}