package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEnderChest extends BlockContainer {
	protected BlockEnderChest() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
	public int getRenderType() {
		return 22;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.obsidian);
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 8;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		byte b0 = 0;
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		if (l == 0) {
			b0 = 2;
		}

		if (l == 1) {
			b0 = 5;
		}

		if (l == 2) {
			b0 = 3;
		}

		if (l == 3) {
			b0 = 4;
		}

		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, b0, 2);
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		InventoryEnderChest inventoryenderchest = p_149727_5_.getInventoryEnderChest();
		TileEntityEnderChest tileentityenderchest = (TileEntityEnderChest) p_149727_1_.getTileEntity(p_149727_2_,
				p_149727_3_, p_149727_4_);

		if (inventoryenderchest != null && tileentityenderchest != null) {
			if (p_149727_1_.getBlock(p_149727_2_, p_149727_3_ + 1, p_149727_4_).isNormalCube())
				return true;
			else if (p_149727_1_.isRemote)
				return true;
			else {
				inventoryenderchest.func_146031_a(tileentityenderchest);
				p_149727_5_.displayGUIChest(inventoryenderchest);
				return true;
			}
		} else
			return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnderChest();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		for (int l = 0; l < 3; ++l) {
			p_149734_5_.nextFloat();
			double d1 = p_149734_3_ + p_149734_5_.nextFloat();
			// p_149734_4_ + p_149734_5_.nextFloat();
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;
			int i1 = p_149734_5_.nextInt(2) * 2 - 1;
			int j1 = p_149734_5_.nextInt(2) * 2 - 1;
			d3 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			d4 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			d5 = (p_149734_5_.nextFloat() - 0.5D) * 0.125D;
			double d2 = p_149734_4_ + 0.5D + 0.25D * j1;
			d5 = p_149734_5_.nextFloat() * 1.0F * j1;
			double d0 = p_149734_2_ + 0.5D + 0.25D * i1;
			d3 = p_149734_5_.nextFloat() * 1.0F * i1;
			p_149734_1_.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("obsidian");
	}
}