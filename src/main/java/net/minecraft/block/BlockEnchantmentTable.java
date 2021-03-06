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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockEnchantmentTable extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon field_149950_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_149949_b;
	protected BlockEnchantmentTable() {
		super(Material.rock);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
		setLightOpacity(0);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		super.randomDisplayTick(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_, p_149734_5_);

		for (int l = p_149734_2_ - 2; l <= p_149734_2_ + 2; ++l) {
			for (int i1 = p_149734_4_ - 2; i1 <= p_149734_4_ + 2; ++i1) {
				if (l > p_149734_2_ - 2 && l < p_149734_2_ + 2 && i1 == p_149734_4_ - 1) {
					i1 = p_149734_4_ + 2;
				}

				if (p_149734_5_.nextInt(16) == 0) {
					for (int j1 = p_149734_3_; j1 <= p_149734_3_ + 1; ++j1) {
						if (p_149734_1_.getBlock(l, j1, i1) == Blocks.bookshelf) {
							if (!p_149734_1_.isAirBlock((l - p_149734_2_) / 2 + p_149734_2_, j1,
									(i1 - p_149734_4_) / 2 + p_149734_4_)) {
								break;
							}

							p_149734_1_.spawnParticle("enchantmenttable", p_149734_2_ + 0.5D, p_149734_3_ + 2.0D,
									p_149734_4_ + 0.5D, l - p_149734_2_ + p_149734_5_.nextFloat() - 0.5D,
									j1 - p_149734_3_ - p_149734_5_.nextFloat() - 1.0F,
									i1 - p_149734_4_ + p_149734_5_.nextFloat() - 0.5D);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 0 ? field_149949_b : p_149691_1_ == 1 ? field_149950_a : blockIcon;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityEnchantmentTable();
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		else {
			TileEntityEnchantmentTable tileentityenchantmenttable = (TileEntityEnchantmentTable) p_149727_1_
					.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);
			p_149727_5_.displayGUIEnchantment(p_149727_2_, p_149727_3_, p_149727_4_,
					tileentityenchantmenttable.func_145921_b() ? tileentityenchantmenttable.func_145919_a() : null);
			return true;
		}
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);

		if (p_149689_6_.hasDisplayName()) {
			((TileEntityEnchantmentTable) p_149689_1_.getTileEntity(p_149689_2_, p_149689_3_, p_149689_4_))
					.func_145920_a(p_149689_6_.getDisplayName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_" + "side");
		field_149950_a = p_149651_1_.registerIcon(getTextureName() + "_" + "top");
		field_149949_b = p_149651_1_.registerIcon(getTextureName() + "_" + "bottom");
	}
}