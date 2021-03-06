package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Iterator;
import java.util.Random;

public class BlockBed extends BlockDirectional {
	public static final int[][] field_149981_a = new int[][] { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149980_b;
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149982_M;
	@SideOnly(Side.CLIENT)
	private IIcon[] field_149983_N;
	public BlockBed() {
		super(Material.cloth);
		func_149978_e();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		else {
			int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);

			if (!isBlockHeadOfBed(i1)) {
				int j1 = getDirection(i1);
				p_149727_2_ += field_149981_a[j1][0];
				p_149727_4_ += field_149981_a[j1][1];

				if (p_149727_1_.getBlock(p_149727_2_, p_149727_3_, p_149727_4_) != this)
					return true;

				i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);
			}

			if (p_149727_1_.provider.canRespawnHere()
					&& p_149727_1_.getBiomeGenForCoords(p_149727_2_, p_149727_4_) != BiomeGenBase.hell) {
				if (func_149976_c(i1)) {
					EntityPlayer entityplayer1 = null;
					Iterator iterator = p_149727_1_.playerEntities.iterator();

					while (iterator.hasNext()) {
						EntityPlayer entityplayer2 = (EntityPlayer) iterator.next();

						if (entityplayer2.isPlayerSleeping()) {
							ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;

							if (chunkcoordinates.posX == p_149727_2_ && chunkcoordinates.posY == p_149727_3_
									&& chunkcoordinates.posZ == p_149727_4_) {
								entityplayer1 = entityplayer2;
							}
						}
					}

					if (entityplayer1 != null) {
						p_149727_5_.addChatComponentMessage(
								new ChatComponentTranslation("tile.bed.occupied"));
						return true;
					}

					func_149979_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, false);
				}

				EntityPlayer.EnumStatus enumstatus = p_149727_5_.sleepInBedAt(p_149727_2_, p_149727_3_, p_149727_4_);

				if (enumstatus == EntityPlayer.EnumStatus.OK) {
					func_149979_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, true);
					return true;
				} else {
					if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
						p_149727_5_.addChatComponentMessage(
								new ChatComponentTranslation("tile.bed.noSleep"));
					} else if (enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
						p_149727_5_.addChatComponentMessage(
								new ChatComponentTranslation("tile.bed.notSafe"));
					}

					return true;
				}
			} else {
				double d2 = p_149727_2_ + 0.5D;
				double d0 = p_149727_3_ + 0.5D;
				double d1 = p_149727_4_ + 0.5D;
				p_149727_1_.setBlockToAir(p_149727_2_, p_149727_3_, p_149727_4_);
				int k1 = getDirection(i1);
				p_149727_2_ += field_149981_a[k1][0];
				p_149727_4_ += field_149981_a[k1][1];

				if (p_149727_1_.getBlock(p_149727_2_, p_149727_3_, p_149727_4_) == this) {
					p_149727_1_.setBlockToAir(p_149727_2_, p_149727_3_, p_149727_4_);
					d2 = (d2 + p_149727_2_ + 0.5D) / 2.0D;
					d0 = (d0 + p_149727_3_ + 0.5D) / 2.0D;
					d1 = (d1 + p_149727_4_ + 0.5D) / 2.0D;
				}

				p_149727_1_.newExplosion(null, p_149727_2_ + 0.5F, p_149727_3_ + 0.5F, p_149727_4_ + 0.5F,
						5.0F, true, true);
				return true;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_1_ == 0)
			return Blocks.planks.getBlockTextureFromSide(p_149691_1_);
		else {
			int k = getDirection(p_149691_2_);
			int l = Direction.bedDirection[k][p_149691_1_];
			int i1 = isBlockHeadOfBed(p_149691_2_) ? 1 : 0;
			return (i1 != 1 || l != 2) && (i1 != 0 || l != 3)
					? l != 5 && l != 4 ? field_149983_N[i1] : field_149982_M[i1]
					: field_149980_b[i1];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149983_N = new IIcon[] { p_149651_1_.registerIcon(getTextureName() + "_feet_top"),
				p_149651_1_.registerIcon(getTextureName() + "_head_top") };
		field_149980_b = new IIcon[] { p_149651_1_.registerIcon(getTextureName() + "_feet_end"),
				p_149651_1_.registerIcon(getTextureName() + "_head_end") };
		field_149982_M = new IIcon[] { p_149651_1_.registerIcon(getTextureName() + "_feet_side"),
				p_149651_1_.registerIcon(getTextureName() + "_head_side") };
	}

	@Override
	public int getRenderType() {
		return 14;
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
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		func_149978_e();
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
		int i1 = getDirection(l);

		if (isBlockHeadOfBed(l)) {
			if (p_149695_1_.getBlock(p_149695_2_ - field_149981_a[i1][0], p_149695_3_,
					p_149695_4_ - field_149981_a[i1][1]) != this) {
				p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
			}
		} else if (p_149695_1_.getBlock(p_149695_2_ + field_149981_a[i1][0], p_149695_3_,
				p_149695_4_ + field_149981_a[i1][1]) != this) {
			p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);

			if (!p_149695_1_.isRemote) {
				this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, l, 0);
			}
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return isBlockHeadOfBed(p_149650_1_) ? Item.getItemById(0) : Items.bed;
	}

	private void func_149978_e() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
	}

	public static boolean isBlockHeadOfBed(int p_149975_0_) {
		return (p_149975_0_ & 8) != 0;
	}

	public static boolean func_149976_c(int p_149976_0_) {
		return (p_149976_0_ & 4) != 0;
	}

	public static void func_149979_a(World p_149979_0_, int p_149979_1_, int p_149979_2_, int p_149979_3_,
			boolean p_149979_4_) {
		int l = p_149979_0_.getBlockMetadata(p_149979_1_, p_149979_2_, p_149979_3_);

		if (p_149979_4_) {
			l |= 4;
		} else {
			l &= -5;
		}

		p_149979_0_.setBlockMetadataWithNotify(p_149979_1_, p_149979_2_, p_149979_3_, l, 4);
	}

	public static ChunkCoordinates func_149977_a(World p_149977_0_, int p_149977_1_, int p_149977_2_, int p_149977_3_,
			int p_149977_4_) {
		int i1 = p_149977_0_.getBlockMetadata(p_149977_1_, p_149977_2_, p_149977_3_);
		int j1 = BlockDirectional.getDirection(i1);

		for (int k1 = 0; k1 <= 1; ++k1) {
			int l1 = p_149977_1_ - field_149981_a[j1][0] * k1 - 1;
			int i2 = p_149977_3_ - field_149981_a[j1][1] * k1 - 1;
			int j2 = l1 + 2;
			int k2 = i2 + 2;

			for (int l2 = l1; l2 <= j2; ++l2) {
				for (int i3 = i2; i3 <= k2; ++i3) {
					if (World.doesBlockHaveSolidTopSurface(p_149977_0_, l2, p_149977_2_ - 1, i3)
							&& !p_149977_0_.getBlock(l2, p_149977_2_, i3).getMaterial().isOpaque()
							&& !p_149977_0_.getBlock(l2, p_149977_2_ + 1, i3).getMaterial().isOpaque()) {
						if (p_149977_4_ <= 0)
							return new ChunkCoordinates(l2, p_149977_2_, i3);

						--p_149977_4_;
					}
				}
			}
		}

		return null;
	}

	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_,
			int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		if (!isBlockHeadOfBed(p_149690_5_)) {
			super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_,
					p_149690_6_, 0);
		}
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.bed;
	}

	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_,
			EntityPlayer p_149681_6_) {
		if (p_149681_6_.capabilities.isCreativeMode && isBlockHeadOfBed(p_149681_5_)) {
			int i1 = getDirection(p_149681_5_);
			p_149681_2_ -= field_149981_a[i1][0];
			p_149681_4_ -= field_149981_a[i1][1];

			if (p_149681_1_.getBlock(p_149681_2_, p_149681_3_, p_149681_4_) == this) {
				p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_, p_149681_4_);
			}
		}
	}
}