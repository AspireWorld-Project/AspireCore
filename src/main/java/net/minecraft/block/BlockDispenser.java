package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDispenser extends BlockContainer {
	public static final IRegistry dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
	protected Random field_149942_b = new Random();
	@SideOnly(Side.CLIENT)
	protected IIcon field_149944_M;
	@SideOnly(Side.CLIENT)
	protected IIcon field_149945_N;
	@SideOnly(Side.CLIENT)
	protected IIcon field_149946_O;
	protected BlockDispenser() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 4;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
		func_149938_m(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
	}

	private void func_149938_m(World p_149938_1_, int p_149938_2_, int p_149938_3_, int p_149938_4_) {
		if (!p_149938_1_.isRemote) {
			Block block = p_149938_1_.getBlock(p_149938_2_, p_149938_3_, p_149938_4_ - 1);
			Block block1 = p_149938_1_.getBlock(p_149938_2_, p_149938_3_, p_149938_4_ + 1);
			Block block2 = p_149938_1_.getBlock(p_149938_2_ - 1, p_149938_3_, p_149938_4_);
			Block block3 = p_149938_1_.getBlock(p_149938_2_ + 1, p_149938_3_, p_149938_4_);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4;
			}

			p_149938_1_.setBlockMetadataWithNotify(p_149938_2_, p_149938_3_, p_149938_4_, b0, 2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		int k = p_149691_2_ & 7;
		return p_149691_1_ == k ? k != 1 && k != 0 ? field_149945_N : field_149946_O
				: k != 1 && k != 0 ? p_149691_1_ != 1 && p_149691_1_ != 0 ? blockIcon : field_149944_M : field_149944_M;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("furnace_side");
		field_149944_M = p_149651_1_.registerIcon("furnace_top");
		field_149945_N = p_149651_1_.registerIcon(getTextureName() + "_front_horizontal");
		field_149946_O = p_149651_1_.registerIcon(getTextureName() + "_front_vertical");
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		else {
			TileEntityDispenser tileentitydispenser = (TileEntityDispenser) p_149727_1_.getTileEntity(p_149727_2_,
					p_149727_3_, p_149727_4_);

			if (tileentitydispenser != null) {
				p_149727_5_.func_146102_a(tileentitydispenser);
			}

			return true;
		}
	}

	protected void func_149941_e(World p_149941_1_, int p_149941_2_, int p_149941_3_, int p_149941_4_) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(p_149941_1_, p_149941_2_, p_149941_3_, p_149941_4_);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();

		if (tileentitydispenser != null) {
			int l = tileentitydispenser.func_146017_i();

			if (l < 0) {
				p_149941_1_.playAuxSFX(1001, p_149941_2_, p_149941_3_, p_149941_4_, 0);
			} else {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
				IBehaviorDispenseItem ibehaviordispenseitem = func_149940_a(itemstack);

				if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider) {
					ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
					tileentitydispenser.setInventorySlotContents(l, itemstack1.stackSize == 0 ? null : itemstack1);
				}
			}
		}
	}

	protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_) {
		return (IBehaviorDispenseItem) dispenseBehaviorRegistry.getObject(p_149940_1_.getItem());
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		boolean flag = p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_)
				|| p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_ + 1, p_149695_4_);
		int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
		boolean flag1 = (l & 8) != 0;

		if (flag && !flag1) {
			p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, tickRate(p_149695_1_));
			p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l | 8, 4);
		} else if (!flag && flag1) {
			p_149695_1_.setBlockMetadataWithNotify(p_149695_2_, p_149695_3_, p_149695_4_, l & -9, 4);
		}
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			func_149941_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDispenser();
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = BlockPistonBase.determineOrientation(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);

		if (p_149689_6_.hasDisplayName()) {
			((TileEntityDispenser) p_149689_1_.getTileEntity(p_149689_2_, p_149689_3_, p_149689_4_))
					.func_146018_a(p_149689_6_.getDisplayName());
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) p_149749_1_.getTileEntity(p_149749_2_,
				p_149749_3_, p_149749_4_);

		if (tileentitydispenser != null) {
			for (int i1 = 0; i1 < tileentitydispenser.getSizeInventory(); ++i1) {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(i1);

				if (itemstack != null) {
					float f = field_149942_b.nextFloat() * 0.8F + 0.1F;
					float f1 = field_149942_b.nextFloat() * 0.8F + 0.1F;
					float f2 = field_149942_b.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = field_149942_b.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(p_149749_1_, p_149749_2_ + f, p_149749_3_ + f1,
								p_149749_4_ + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem()
									.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (float) field_149942_b.nextGaussian() * f3;
						entityitem.motionY = (float) field_149942_b.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) field_149942_b.nextGaussian() * f3;
						p_149749_1_.spawnEntityInWorld(entityitem);
					}
				}
			}

			p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	public static IPosition func_149939_a(IBlockSource p_149939_0_) {
		EnumFacing enumfacing = func_149937_b(p_149939_0_.getBlockMetadata());
		double d0 = p_149939_0_.getX() + 0.7D * enumfacing.getFrontOffsetX();
		double d1 = p_149939_0_.getY() + 0.7D * enumfacing.getFrontOffsetY();
		double d2 = p_149939_0_.getZ() + 0.7D * enumfacing.getFrontOffsetZ();
		return new PositionImpl(d0, d1, d2);
	}

	public static EnumFacing func_149937_b(int p_149937_0_) {
		return EnumFacing.getFront(p_149937_0_ & 7);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		return Container.calcRedstoneFromInventory(
				(IInventory) p_149736_1_.getTileEntity(p_149736_2_, p_149736_3_, p_149736_4_));
	}
}