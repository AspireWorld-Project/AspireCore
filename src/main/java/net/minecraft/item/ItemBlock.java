package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlock extends Item {
	public final Block field_150939_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_150938_b;
	private static final String __OBFID = "CL_00001772";

	public ItemBlock(Block p_i45328_1_) {
		field_150939_a = p_i45328_1_;
	}

	@Override
	public ItemBlock setUnlocalizedName(String p_77655_1_) {
		super.setUnlocalizedName(p_77655_1_);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber() {
		return field_150939_a.getItemIconName() != null ? 1 : 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150938_b != null ? field_150938_b : field_150939_a.getBlockTextureFromSide(1);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

		if (block == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 7) < 1) {
			p_77648_7_ = 1;
		} else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush
				&& !block.isReplaceable(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
			if (p_77648_7_ == 0) {
				--p_77648_5_;
			}

			if (p_77648_7_ == 1) {
				++p_77648_5_;
			}

			if (p_77648_7_ == 2) {
				--p_77648_6_;
			}

			if (p_77648_7_ == 3) {
				++p_77648_6_;
			}

			if (p_77648_7_ == 4) {
				--p_77648_4_;
			}

			if (p_77648_7_ == 5) {
				++p_77648_4_;
			}
		}

		if (p_77648_1_.stackSize == 0)
			return false;
		else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
			return false;
		else if (p_77648_5_ == 255 && field_150939_a.getMaterial().isSolid())
			return false;
		else if (p_77648_3_.canPlaceEntityOnSide(field_150939_a, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_,
				p_77648_2_, p_77648_1_)) {
			int i1 = getMetadata(p_77648_1_.getItemDamage());
			int j1 = field_150939_a.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_,
					p_77648_8_, p_77648_9_, p_77648_10_, i1);

			if (placeBlockAt(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_,
					p_77648_8_, p_77648_9_, p_77648_10_, j1)) {
				p_77648_3_.playSoundEffect(p_77648_4_ + 0.5F, p_77648_5_ + 0.5F, p_77648_6_ + 0.5F,
						field_150939_a.stepSound.func_150496_b(), (field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F,
						field_150939_a.stepSound.getPitch() * 0.8F);
				--p_77648_1_.stackSize;
			}

			return true;
		} else
			return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_150936_a(World p_150936_1_, int p_150936_2_, int p_150936_3_, int p_150936_4_, int p_150936_5_,
			EntityPlayer p_150936_6_, ItemStack p_150936_7_) {
		Block block = p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_);

		if (block == Blocks.snow_layer) {
			p_150936_5_ = 1;
		} else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush
				&& !block.isReplaceable(p_150936_1_, p_150936_2_, p_150936_3_, p_150936_4_)) {
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
		}

		return p_150936_1_.canPlaceEntityOnSide(field_150939_a, p_150936_2_, p_150936_3_, p_150936_4_, false,
				p_150936_5_, null, p_150936_7_);
	}

	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return field_150939_a.getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedName() {
		return field_150939_a.getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public CreativeTabs getCreativeTab() {
		return field_150939_a.getCreativeTabToDisplayOn();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
		field_150939_a.getSubBlocks(p_150895_1_, p_150895_2_, p_150895_3_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_) {
		String s = field_150939_a.getItemIconName();

		if (s != null) {
			field_150938_b = p_94581_1_.registerIcon(s);
		}
	}

	/**
	 * Called to actually place the block, after the location is determined and all
	 * permission checks have been made.
	 *
	 * @param stack
	 *            The item stack that was used to place the block. This can be
	 *            changed inside the method.
	 * @param player
	 *            The player who is placing the block. Can be null if the block is
	 *            not being placed by a player.
	 * @param side
	 *            The side the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata) {

		if (!world.setBlock(x, y, z, field_150939_a, metadata, 3))
			return false;

		if (world.getBlock(x, y, z) == field_150939_a) {
			field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
			field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true;
	}
}