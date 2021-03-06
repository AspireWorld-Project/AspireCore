package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon field_149927_a;
	protected BlockJukebox() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_149927_a : blockIcon;
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_) == 0)
			return false;
		else {
			func_149925_e(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
			return true;
		}
	}

	public void func_149926_b(World p_149926_1_, int p_149926_2_, int p_149926_3_, int p_149926_4_,
			ItemStack p_149926_5_) {
		if (!p_149926_1_.isRemote) {
			BlockJukebox.TileEntityJukebox tileentityjukebox = (BlockJukebox.TileEntityJukebox) p_149926_1_
					.getTileEntity(p_149926_2_, p_149926_3_, p_149926_4_);

			if (tileentityjukebox != null) {
				tileentityjukebox.func_145857_a(p_149926_5_.copy());
				p_149926_1_.setBlockMetadataWithNotify(p_149926_2_, p_149926_3_, p_149926_4_, 1, 2);
			}
		}
	}

	public void func_149925_e(World p_149925_1_, int p_149925_2_, int p_149925_3_, int p_149925_4_) {
		if (!p_149925_1_.isRemote) {
			BlockJukebox.TileEntityJukebox tileentityjukebox = (BlockJukebox.TileEntityJukebox) p_149925_1_
					.getTileEntity(p_149925_2_, p_149925_3_, p_149925_4_);

			if (tileentityjukebox != null) {
				ItemStack itemstack = tileentityjukebox.func_145856_a();

				if (itemstack != null) {
					p_149925_1_.playAuxSFX(1005, p_149925_2_, p_149925_3_, p_149925_4_, 0);
					p_149925_1_.playRecord(null, p_149925_2_, p_149925_3_, p_149925_4_);
					tileentityjukebox.func_145857_a(null);
					p_149925_1_.setBlockMetadataWithNotify(p_149925_2_, p_149925_3_, p_149925_4_, 0, 2);
					float f = 0.7F;
					double d0 = p_149925_1_.rand.nextFloat() * f + (1.0F - f) * 0.5D;
					double d1 = p_149925_1_.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
					double d2 = p_149925_1_.rand.nextFloat() * f + (1.0F - f) * 0.5D;
					ItemStack itemstack1 = itemstack.copy();
					EntityItem entityitem = new EntityItem(p_149925_1_, p_149925_2_ + d0, p_149925_3_ + d1,
							p_149925_4_ + d2, itemstack1);
					entityitem.delayBeforeCanPickup = 10;
					p_149925_1_.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		func_149925_e(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		if (!p_149690_1_.isRemote) {
			super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_,
					p_149690_6_, 0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new BlockJukebox.TileEntityJukebox();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_149927_a = p_149651_1_.registerIcon(getTextureName() + "_top");
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		ItemStack itemstack = ((BlockJukebox.TileEntityJukebox) p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_,
				p_149736_4_)).func_145856_a();
		return itemstack == null ? 0
				: Item.getIdFromItem(itemstack.getItem()) + 1 - Item.getIdFromItem(Items.record_13);
	}

	public static class TileEntityJukebox extends TileEntity {
		private ItemStack field_145858_a;
		@Override
		public void readFromNBT(NBTTagCompound p_145839_1_) {
			super.readFromNBT(p_145839_1_);

			if (p_145839_1_.hasKey("RecordItem", 10)) {
				func_145857_a(ItemStack.loadItemStackFromNBT(p_145839_1_.getCompoundTag("RecordItem")));
			} else if (p_145839_1_.getInteger("Record") > 0) {
				func_145857_a(new ItemStack(Item.getItemById(p_145839_1_.getInteger("Record")), 1, 0));
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound p_145841_1_) {
			super.writeToNBT(p_145841_1_);

			if (func_145856_a() != null) {
				p_145841_1_.setTag("RecordItem", func_145856_a().writeToNBT(new NBTTagCompound()));
				p_145841_1_.setInteger("Record", Item.getIdFromItem(func_145856_a().getItem()));
			}
		}

		public ItemStack func_145856_a() {
			return field_145858_a;
		}

		public void func_145857_a(ItemStack p_145857_1_) {
			field_145858_a = p_145857_1_;
			markDirty();
		}
	}
}