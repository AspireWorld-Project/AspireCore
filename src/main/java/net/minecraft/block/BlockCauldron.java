package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockCauldron extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon field_150029_a;
	@SideOnly(Side.CLIENT)
	private IIcon field_150028_b;
	@SideOnly(Side.CLIENT)
	private IIcon field_150030_M;
	public BlockCauldron() {
		super(Material.iron);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? field_150028_b : p_149691_1_ == 0 ? field_150030_M : blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150029_a = p_149651_1_.registerIcon(getTextureName() + "_" + "inner");
		field_150028_b = p_149651_1_.registerIcon(getTextureName() + "_top");
		field_150030_M = p_149651_1_.registerIcon(getTextureName() + "_" + "bottom");
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_,
			AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_,
				p_149743_7_);
		setBlockBoundsForItemRender();
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getCauldronIcon(String p_150026_0_) {
		return p_150026_0_.equals("inner") ? Blocks.cauldron.field_150029_a
				: p_150026_0_.equals("bottom") ? Blocks.cauldron.field_150030_M : null;
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 24;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity p_149670_5_) {
		int l = func_150027_b(p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_));
		float f = p_149670_3_ + (6.0F + 3 * l) / 16.0F;

		if (!p_149670_1_.isRemote && p_149670_5_.isBurning() && l > 0 && p_149670_5_.boundingBox.minY <= f) {
			p_149670_5_.extinguish();
			func_150024_a(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, l - 1);
		}
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (p_149727_1_.isRemote)
			return true;
		else {
			ItemStack itemstack = p_149727_5_.inventory.getCurrentItem();

			if (itemstack == null)
				return true;
			else {
				int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);
				int j1 = func_150027_b(i1);

				if (itemstack.getItem() == Items.water_bucket) {
					if (j1 < 3) {
						if (!p_149727_5_.capabilities.isCreativeMode) {
							p_149727_5_.inventory.setInventorySlotContents(p_149727_5_.inventory.currentItem,
									new ItemStack(Items.bucket));
						}

						func_150024_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, 3);
					}

					return true;
				} else {
					if (itemstack.getItem() == Items.glass_bottle) {
						if (j1 > 0) {
							if (!p_149727_5_.capabilities.isCreativeMode) {
								ItemStack itemstack1 = new ItemStack(Items.potionitem, 1, 0);

								if (!p_149727_5_.inventory.addItemStackToInventory(itemstack1)) {
									p_149727_1_.spawnEntityInWorld(new EntityItem(p_149727_1_, p_149727_2_ + 0.5D,
											p_149727_3_ + 1.5D, p_149727_4_ + 0.5D, itemstack1));
								} else if (p_149727_5_ instanceof EntityPlayerMP) {
									((EntityPlayerMP) p_149727_5_)
											.sendContainerToPlayer(p_149727_5_.inventoryContainer);
								}

								--itemstack.stackSize;

								if (itemstack.stackSize <= 0) {
									p_149727_5_.inventory.setInventorySlotContents(p_149727_5_.inventory.currentItem,
                                            null);
								}
							}

							func_150024_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, j1 - 1);
						}
					} else if (j1 > 0 && itemstack.getItem() instanceof ItemArmor
							&& ((ItemArmor) itemstack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.CLOTH) {
						ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
						itemarmor.removeColor(itemstack);
						func_150024_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, j1 - 1);
						return true;
					}

					return false;
				}
			}
		}
	}

	public void func_150024_a(World p_150024_1_, int p_150024_2_, int p_150024_3_, int p_150024_4_, int p_150024_5_) {
		p_150024_1_.setBlockMetadataWithNotify(p_150024_2_, p_150024_3_, p_150024_4_,
				MathHelper.clamp_int(p_150024_5_, 0, 3), 2);
		p_150024_1_.func_147453_f(p_150024_2_, p_150024_3_, p_150024_4_, this);
	}

	@Override
	public void fillWithRain(World p_149639_1_, int p_149639_2_, int p_149639_3_, int p_149639_4_) {
		if (p_149639_1_.rand.nextInt(20) == 1) {
			int l = p_149639_1_.getBlockMetadata(p_149639_2_, p_149639_3_, p_149639_4_);

			if (l < 3) {
				p_149639_1_.setBlockMetadataWithNotify(p_149639_2_, p_149639_3_, p_149639_4_, l + 1, 2);
			}
		}
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Items.cauldron;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Items.cauldron;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_,
			int p_149736_5_) {
		int i1 = p_149736_1_.getBlockMetadata(p_149736_2_, p_149736_3_, p_149736_4_);
		return func_150027_b(i1);
	}

	public static int func_150027_b(int p_150027_0_) {
		return p_150027_0_;
	}

	@SideOnly(Side.CLIENT)
	public static float getRenderLiquidLevel(int p_150025_0_) {
		int j = MathHelper.clamp_int(p_150025_0_, 0, 3);
		return (6 + 3 * j) / 16.0F;
	}
}