package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemArmor extends Item {
	private static final int[] maxDamageArray = new int[] { 11, 16, 15, 13 };
	private static final String[] CLOTH_OVERLAY_NAMES = new String[] { "leather_helmet_overlay",
			"leather_chestplate_overlay", "leather_leggings_overlay", "leather_boots_overlay" };
	public static final String[] EMPTY_SLOT_NAMES = new String[] { "empty_armor_slot_helmet",
			"empty_armor_slot_chestplate", "empty_armor_slot_leggings", "empty_armor_slot_boots" };
	private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem() {
		private static final String __OBFID = "CL_00001767";

		@Override
		protected ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
			EnumFacing enumfacing = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
			int i = p_82487_1_.getXInt() + enumfacing.getFrontOffsetX();
			int j = p_82487_1_.getYInt() + enumfacing.getFrontOffsetY();
			int k = p_82487_1_.getZInt() + enumfacing.getFrontOffsetZ();
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);
			List list = p_82487_1_.getWorld().selectEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb,
					new IEntitySelector.ArmoredMob(p_82487_2_));

			if (list.size() > 0) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) list.get(0);
				int l = 0;// Forge: We fix the indexes. Mojang Stop hard coding this!
				int i1 = EntityLiving.getArmorPosition(p_82487_2_);
				ItemStack itemstack1 = p_82487_2_.copy();
				itemstack1.stackSize = 1;
				entitylivingbase.setCurrentItemOrArmor(i1 - l, itemstack1);

				if (entitylivingbase instanceof EntityLiving) {
					((EntityLiving) entitylivingbase).setEquipmentDropChance(i1, 2.0F);
				}

				--p_82487_2_.stackSize;
				return p_82487_2_;
			} else
				return super.dispenseStack(p_82487_1_, p_82487_2_);
		}
	};
	public final int armorType;
	public final int damageReduceAmount;
	public final int renderIndex;
	private final ItemArmor.ArmorMaterial material;
	@SideOnly(Side.CLIENT)
	private IIcon overlayIcon;
	@SideOnly(Side.CLIENT)
	private IIcon emptySlotIcon;
	private static final String __OBFID = "CL_00001766";

	public ItemArmor(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_) {
		material = p_i45325_1_;
		armorType = p_i45325_3_;
		renderIndex = p_i45325_2_;
		damageReduceAmount = p_i45325_1_.getDamageReductionAmount(p_i45325_3_);
		setMaxDamage(p_i45325_1_.getDurability(p_i45325_3_));
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabCombat);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserBehavior);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		if (p_82790_2_ > 0)
			return 16777215;
		else {
			int j = getColor(p_82790_1_);

			if (j < 0) {
				j = 16777215;
			}

			return j;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return material == ItemArmor.ArmorMaterial.CLOTH;
	}

	@Override
	public int getItemEnchantability() {
		return material.getEnchantability();
	}

	public ItemArmor.ArmorMaterial getArmorMaterial() {
		return material;
	}

	public boolean hasColor(ItemStack p_82816_1_) {
		return material == ArmorMaterial.CLOTH && p_82816_1_.hasTagCompound() && p_82816_1_.getTagCompound().hasKey("display", 10) && p_82816_1_.getTagCompound().getCompoundTag("display").hasKey("color", 3);
	}

	public int getColor(ItemStack p_82814_1_) {
		if (material != ItemArmor.ArmorMaterial.CLOTH)
			return -1;
		else {
			NBTTagCompound nbttagcompound = p_82814_1_.getTagCompound();

			if (nbttagcompound == null)
				return 10511680;
			else {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
				return nbttagcompound1 == null ? 10511680
						: nbttagcompound1.hasKey("color", 3) ? nbttagcompound1.getInteger("color") : 10511680;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
		return p_77618_2_ == 1 ? overlayIcon : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
	}

	public void removeColor(ItemStack p_82815_1_) {
		if (material == ItemArmor.ArmorMaterial.CLOTH) {
			NBTTagCompound nbttagcompound = p_82815_1_.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1.hasKey("color")) {
					nbttagcompound1.removeTag("color");
				}
			}
		}
	}

	public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_) {
		if (material != ItemArmor.ArmorMaterial.CLOTH)
			throw new UnsupportedOperationException("Can't dye non-leather!");
		else {
			NBTTagCompound nbttagcompound = p_82813_1_.getTagCompound();

			if (nbttagcompound == null) {
				nbttagcompound = new NBTTagCompound();
				p_82813_1_.setTagCompound(nbttagcompound);
			}

			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", p_82813_2_);
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return material.func_151685_b() == p_82789_2_.getItem() || super.getIsRepairable(p_82789_1_, p_82789_2_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_) {
		super.registerIcons(p_94581_1_);

		if (material == ItemArmor.ArmorMaterial.CLOTH) {
			overlayIcon = p_94581_1_.registerIcon(CLOTH_OVERLAY_NAMES[armorType]);
		}

		emptySlotIcon = p_94581_1_.registerIcon(EMPTY_SLOT_NAMES[armorType]);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		int i = EntityLiving.getArmorPosition(p_77659_1_) - 1;
		ItemStack itemstack1 = p_77659_3_.getCurrentArmor(i);

		if (itemstack1 == null) {
			p_77659_3_.setCurrentItemOrArmor(i + 1, p_77659_1_.copy()); // Forge: Vanilla bug fix associated with fixed
																		// setCurrentItemOrArmor indexs for players.
			p_77659_1_.stackSize = 0;
		}

		return p_77659_1_;
	}

	@SideOnly(Side.CLIENT)
	public static IIcon func_94602_b(int p_94602_0_) {
		switch (p_94602_0_) {
		case 0:
			return Items.diamond_helmet.emptySlotIcon;
		case 1:
			return Items.diamond_chestplate.emptySlotIcon;
		case 2:
			return Items.diamond_leggings.emptySlotIcon;
		case 3:
			return Items.diamond_boots.emptySlotIcon;
		default:
			return null;
		}
	}

	public enum ArmorMaterial {
		CLOTH(5, new int[] { 1, 3, 2, 1 }, 15), CHAIN(15, new int[] { 2, 5, 4, 1 }, 12), IRON(15,
				new int[] { 2, 6, 5, 2 },
				9), GOLD(7, new int[] { 2, 5, 3, 1 }, 25), DIAMOND(33, new int[] { 3, 8, 6, 3 }, 10);
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;

		private static final String __OBFID = "CL_00001768";

		// Added by forge for custom Armor materials.
		public Item customCraftingMaterial = null;

		ArmorMaterial(int p_i1827_3_, int[] p_i1827_4_, int p_i1827_5_) {
			maxDamageFactor = p_i1827_3_;
			damageReductionAmountArray = p_i1827_4_;
			enchantability = p_i1827_5_;
		}

		public int getDurability(int p_78046_1_) {
			return ItemArmor.maxDamageArray[p_78046_1_] * maxDamageFactor;
		}

		public int getDamageReductionAmount(int p_78044_1_) {
			return damageReductionAmountArray[p_78044_1_];
		}

		public int getEnchantability() {
			return enchantability;
		}

		public Item func_151685_b() {
			return this == CLOTH ? Items.leather
					: this == CHAIN ? Items.iron_ingot
							: this == GOLD ? Items.gold_ingot
									: this == IRON ? Items.iron_ingot
											: this == DIAMOND ? Items.diamond : customCraftingMaterial;
		}
	}
}