package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock {
	private final boolean field_150948_b;
	private final BlockSlab field_150949_c;
	private final BlockSlab field_150947_d;
	private static final String __OBFID = "CL_00000071";

	public ItemSlab(Block p_i45355_1_, BlockSlab p_i45355_2_, BlockSlab p_i45355_3_, boolean p_i45355_4_) {
		super(p_i45355_1_);
		field_150949_c = p_i45355_2_;
		field_150947_d = p_i45355_3_;
		field_150948_b = p_i45355_4_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return Block.getBlockFromItem(this).getIcon(2, p_77617_1_);
	}

	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return field_150949_c.func_150002_b(p_77667_1_.getItemDamage());
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (field_150948_b)
			return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_,
					p_77648_8_, p_77648_9_, p_77648_10_);
		else if (p_77648_1_.stackSize == 0)
			return false;
		else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
			return false;
		else {
			Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
			int i1 = p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_);
			int j1 = i1 & 7;
			boolean flag = (i1 & 8) != 0;

			if ((p_77648_7_ == 1 && !flag || p_77648_7_ == 0 && flag) && block == field_150949_c
					&& j1 == p_77648_1_.getItemDamage()) {
				if (p_77648_3_.checkNoEntityCollision(
						field_150947_d.getCollisionBoundingBoxFromPool(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_))
						&& p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, field_150947_d, j1, 3)) {
					p_77648_3_.playSoundEffect(p_77648_4_ + 0.5F, p_77648_5_ + 0.5F, p_77648_6_ + 0.5F,
							field_150947_d.stepSound.func_150496_b(),
							(field_150947_d.stepSound.getVolume() + 1.0F) / 2.0F,
							field_150947_d.stepSound.getPitch() * 0.8F);
					--p_77648_1_.stackSize;
				}

				return true;
			} else
				return func_150946_a(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_) || super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_,
                        p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean func_150936_a(World p_150936_1_, int p_150936_2_, int p_150936_3_, int p_150936_4_, int p_150936_5_,
			EntityPlayer p_150936_6_, ItemStack p_150936_7_) {
		int i1 = p_150936_2_;
		int j1 = p_150936_3_;
		int k1 = p_150936_4_;
		Block block = p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_);
		int l1 = p_150936_1_.getBlockMetadata(p_150936_2_, p_150936_3_, p_150936_4_);
		int i2 = l1 & 7;
		boolean flag = (l1 & 8) != 0;

		if ((p_150936_5_ == 1 && !flag || p_150936_5_ == 0 && flag) && block == field_150949_c
				&& i2 == p_150936_7_.getItemDamage())
			return true;
		else {
			if (p_150936_5_ == 0) {
				--p_150936_3_;
			}

			if (p_150936_5_ == 1) {
				++p_150936_3_;
			}

			if (p_150936_5_ == 2) {
				--p_150936_4_;
			}

			if (p_150936_5_ == 3) {
				++p_150936_4_;
			}

			if (p_150936_5_ == 4) {
				--p_150936_2_;
			}

			if (p_150936_5_ == 5) {
				++p_150936_2_;
			}

			Block block1 = p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_);
			int j2 = p_150936_1_.getBlockMetadata(p_150936_2_, p_150936_3_, p_150936_4_);
			i2 = j2 & 7;
			return block1 == field_150949_c && i2 == p_150936_7_.getItemDamage() || super.func_150936_a(p_150936_1_, i1, j1, k1, p_150936_5_, p_150936_6_, p_150936_7_);
		}
	}

	private boolean func_150946_a(ItemStack p_150946_1_, EntityPlayer p_150946_2_, World p_150946_3_, int p_150946_4_,
			int p_150946_5_, int p_150946_6_, int p_150946_7_) {
		if (p_150946_7_ == 0) {
			--p_150946_5_;
		}

		if (p_150946_7_ == 1) {
			++p_150946_5_;
		}

		if (p_150946_7_ == 2) {
			--p_150946_6_;
		}

		if (p_150946_7_ == 3) {
			++p_150946_6_;
		}

		if (p_150946_7_ == 4) {
			--p_150946_4_;
		}

		if (p_150946_7_ == 5) {
			++p_150946_4_;
		}

		Block block = p_150946_3_.getBlock(p_150946_4_, p_150946_5_, p_150946_6_);
		int i1 = p_150946_3_.getBlockMetadata(p_150946_4_, p_150946_5_, p_150946_6_);
		int j1 = i1 & 7;

		if (block == field_150949_c && j1 == p_150946_1_.getItemDamage()) {
			if (p_150946_3_.checkNoEntityCollision(
					field_150947_d.getCollisionBoundingBoxFromPool(p_150946_3_, p_150946_4_, p_150946_5_, p_150946_6_))
					&& p_150946_3_.setBlock(p_150946_4_, p_150946_5_, p_150946_6_, field_150947_d, j1, 3)) {
				p_150946_3_.playSoundEffect(p_150946_4_ + 0.5F, p_150946_5_ + 0.5F, p_150946_6_ + 0.5F,
						field_150947_d.stepSound.func_150496_b(), (field_150947_d.stepSound.getVolume() + 1.0F) / 2.0F,
						field_150947_d.stepSound.getPitch() * 0.8F);
				--p_150946_1_.stackSize;
			}

			return true;
		} else
			return false;
	}
}