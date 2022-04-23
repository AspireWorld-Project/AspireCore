package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BlockSkull extends BlockContainer {
	private static final String __OBFID = "CL_00000307";

	protected BlockSkull() {
		super(Material.circuits);
		setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
	}

	@Override
	public int getRenderType() {
		return -1;
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
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 7;

		switch (l) {
		case 1:
		default:
			setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
			break;
		case 2:
			setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
			break;
		case 3:
			setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
			break;
		case 4:
			setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
			break;
		case 5:
			setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
		return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_,
			EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySkull();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.skull;
	}

	@Override
	public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_) {
		TileEntity tileentity = p_149643_1_.getTileEntity(p_149643_2_, p_149643_3_, p_149643_4_);
		return tileentity != null && tileentity instanceof TileEntitySkull
				? ((TileEntitySkull) tileentity).func_145904_a()
				: super.getDamageValue(p_149643_1_, p_149643_2_, p_149643_3_, p_149643_4_);
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_,
			EntityPlayer p_149681_6_) {
		if (p_149681_6_.capabilities.isCreativeMode) {
			p_149681_5_ |= 8;
			p_149681_1_.setBlockMetadataWithNotify(p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, 4);
		}
		this.dropBlockAsItem(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, 0);

		super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_,
			int p_149749_6_, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<>();
		{
			if ((p_149749_6_ & 8) == 0) {
				ItemStack itemstack = new ItemStack(Items.skull, 1,
						getDamageValue(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_));
				TileEntitySkull tileentityskull = (TileEntitySkull) p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_,
						p_149749_4_);

				if (tileentityskull == null)
					return ret;

				if (tileentityskull.func_145904_a() == 3 && tileentityskull.func_152108_a() != null) {
					itemstack.setTagCompound(new NBTTagCompound());
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					NBTUtil.func_152460_a(nbttagcompound, tileentityskull.func_152108_a());
					itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
				}

				ret.add(itemstack);
			}
		}
		return ret;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Items.skull;
	}

	public void func_149965_a(World p_149965_1_, int p_149965_2_, int p_149965_3_, int p_149965_4_,
			TileEntitySkull p_149965_5_) {
		if (p_149965_5_.func_145904_a() == 1 && p_149965_3_ >= 2
				&& p_149965_1_.difficultySetting != EnumDifficulty.PEACEFUL && !p_149965_1_.isRemote) {
			int l;
			EntityWither entitywither;
			Iterator iterator;
			EntityPlayer entityplayer;
			int i1;

			for (l = -2; l <= 0; ++l) {
				if (p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 1) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + l + 1) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 2) == Blocks.soul_sand
						&& func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + l, 1)
						&& func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + l + 1, 1)
						&& func_149966_a(p_149965_1_, p_149965_2_, p_149965_3_, p_149965_4_ + l + 2, 1)) {
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + l, 8, 2);
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + l + 1, 8, 2);
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_, p_149965_3_, p_149965_4_ + l + 2, 8, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + l, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + l + 1, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_, p_149965_4_ + l + 2, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 1, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 2, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + l + 1, getBlockById(0), 0, 2);

					if (!p_149965_1_.isRemote) {
						entitywither = new EntityWither(p_149965_1_);
						entitywither.setLocationAndAngles(p_149965_2_ + 0.5D, p_149965_3_ - 1.45D,
								p_149965_4_ + l + 1.5D, 90.0F, 0.0F);
						entitywither.renderYawOffset = 90.0F;
						entitywither.func_82206_m();

						if (!p_149965_1_.isRemote) {
							iterator = p_149965_1_.getEntitiesWithinAABB(EntityPlayer.class,
									entitywither.boundingBox.expand(50.0D, 50.0D, 50.0D)).iterator();

							while (iterator.hasNext()) {
								entityplayer = (EntityPlayer) iterator.next();
								entityplayer.triggerAchievement(AchievementList.field_150963_I);
							}
						}

						p_149965_1_.spawnEntityInWorld(entitywither);
					}

					for (i1 = 0; i1 < 120; ++i1) {
						p_149965_1_.spawnParticle("snowballpoof", p_149965_2_ + p_149965_1_.rand.nextDouble(),
								p_149965_3_ - 2 + p_149965_1_.rand.nextDouble() * 3.9D,
								p_149965_4_ + l + 1 + p_149965_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + l, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + l + 1, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_, p_149965_4_ + l + 2, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 1, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 1, p_149965_4_ + l + 2, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_, p_149965_3_ - 2, p_149965_4_ + l + 1, getBlockById(0));
					return;
				}
			}

			for (l = -2; l <= 0; ++l) {
				if (p_149965_1_.getBlock(p_149965_2_ + l, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_ + l + 1, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_ + l + 1, p_149965_3_ - 2, p_149965_4_) == Blocks.soul_sand
						&& p_149965_1_.getBlock(p_149965_2_ + l + 2, p_149965_3_ - 1, p_149965_4_) == Blocks.soul_sand
						&& func_149966_a(p_149965_1_, p_149965_2_ + l, p_149965_3_, p_149965_4_, 1)
						&& func_149966_a(p_149965_1_, p_149965_2_ + l + 1, p_149965_3_, p_149965_4_, 1)
						&& func_149966_a(p_149965_1_, p_149965_2_ + l + 2, p_149965_3_, p_149965_4_, 1)) {
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + l, p_149965_3_, p_149965_4_, 8, 2);
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + l + 1, p_149965_3_, p_149965_4_, 8, 2);
					p_149965_1_.setBlockMetadataWithNotify(p_149965_2_ + l + 2, p_149965_3_, p_149965_4_, 8, 2);
					p_149965_1_.setBlock(p_149965_2_ + l, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l + 1, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l + 2, p_149965_3_, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l + 1, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l + 2, p_149965_3_ - 1, p_149965_4_, getBlockById(0), 0, 2);
					p_149965_1_.setBlock(p_149965_2_ + l + 1, p_149965_3_ - 2, p_149965_4_, getBlockById(0), 0, 2);

					if (!p_149965_1_.isRemote) {
						entitywither = new EntityWither(p_149965_1_);
						entitywither.setLocationAndAngles(p_149965_2_ + l + 1.5D, p_149965_3_ - 1.45D,
								p_149965_4_ + 0.5D, 0.0F, 0.0F);
						entitywither.func_82206_m();

						if (!p_149965_1_.isRemote) {
							iterator = p_149965_1_.getEntitiesWithinAABB(EntityPlayer.class,
									entitywither.boundingBox.expand(50.0D, 50.0D, 50.0D)).iterator();

							while (iterator.hasNext()) {
								entityplayer = (EntityPlayer) iterator.next();
								entityplayer.triggerAchievement(AchievementList.field_150963_I);
							}
						}

						p_149965_1_.spawnEntityInWorld(entitywither);
					}

					for (i1 = 0; i1 < 120; ++i1) {
						p_149965_1_.spawnParticle("snowballpoof", p_149965_2_ + l + 1 + p_149965_1_.rand.nextDouble(),
								p_149965_3_ - 2 + p_149965_1_.rand.nextDouble() * 3.9D,
								p_149965_4_ + p_149965_1_.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					p_149965_1_.notifyBlockChange(p_149965_2_ + l, p_149965_3_, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l + 1, p_149965_3_, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l + 2, p_149965_3_, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l + 1, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l + 2, p_149965_3_ - 1, p_149965_4_, getBlockById(0));
					p_149965_1_.notifyBlockChange(p_149965_2_ + l + 1, p_149965_3_ - 2, p_149965_4_, getBlockById(0));
					return;
				}
			}
		}
	}

	private boolean func_149966_a(World p_149966_1_, int p_149966_2_, int p_149966_3_, int p_149966_4_,
			int p_149966_5_) {
		if (p_149966_1_.getBlock(p_149966_2_, p_149966_3_, p_149966_4_) != this)
			return false;
		else {
			TileEntity tileentity = p_149966_1_.getTileEntity(p_149966_2_, p_149966_3_, p_149966_4_);
			return tileentity != null && tileentity instanceof TileEntitySkull && ((TileEntitySkull) tileentity).func_145904_a() == p_149966_5_;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.soul_sand.getBlockTextureFromSide(p_149691_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemIconName() {
		return getTextureName() + "_" + ItemSkull.field_94587_a[0];
	}
}