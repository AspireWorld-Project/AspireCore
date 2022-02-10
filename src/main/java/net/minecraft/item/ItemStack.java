package net.minecraft.item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public final class ItemStack {
	public static final DecimalFormat field_111284_a = new DecimalFormat("#.###");
	public int stackSize;
	public int animationsToGo;
	private Item field_151002_e;
	public NBTTagCompound stackTagCompound;
	int itemDamage;
	private EntityItemFrame itemFrame;
	private static final String __OBFID = "CL_00000043";

	private cpw.mods.fml.common.registry.RegistryDelegate<Item> delegate;

	public ItemStack(Block p_i1876_1_) {
		this(p_i1876_1_, 1);
	}

	public ItemStack(Block p_i1877_1_, int p_i1877_2_) {
		this(p_i1877_1_, p_i1877_2_, 0);
	}

	public ItemStack(Block p_i1878_1_, int p_i1878_2_, int p_i1878_3_) {
		this(Item.getItemFromBlock(p_i1878_1_), p_i1878_2_, p_i1878_3_);
	}

	public ItemStack(Item p_i1879_1_) {
		this(p_i1879_1_, 1);
	}

	public ItemStack(Item p_i1880_1_, int p_i1880_2_) {
		this(p_i1880_1_, p_i1880_2_, 0);
	}

	public ItemStack(Item p_i1881_1_, int p_i1881_2_, int p_i1881_3_) {
		func_150996_a(p_i1881_1_);
		stackSize = p_i1881_2_;
		itemDamage = p_i1881_3_;

		if (itemDamage < 0) {
			itemDamage = 0;
		}
	}

	public static ItemStack loadItemStackFromNBT(NBTTagCompound p_77949_0_) {
		ItemStack itemstack = new ItemStack();
		itemstack.readFromNBT(p_77949_0_);
		return itemstack.getItem() != null ? itemstack : null;
	}

	private ItemStack() {
	}

	public ItemStack splitStack(int p_77979_1_) {
		ItemStack itemstack = new ItemStack(field_151002_e, p_77979_1_, itemDamage);

		if (stackTagCompound != null) {
			itemstack.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
		}

		stackSize -= p_77979_1_;
		return itemstack;
	}

	public Item getItem() {
		return delegate != null ? delegate.get() : null;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex() {
		return getItem().getIconIndex(this);
	}

	@SideOnly(Side.CLIENT)
	public int getItemSpriteNumber() {
		return getItem().getSpriteNumber();
	}

	public boolean tryPlaceItemIntoWorld(EntityPlayer p_77943_1_, World p_77943_2_, int p_77943_3_, int p_77943_4_,
			int p_77943_5_, int p_77943_6_, float p_77943_7_, float p_77943_8_, float p_77943_9_) {
		if (!p_77943_2_.isRemote)
			return net.minecraftforge.common.ForgeHooks.onPlaceItemIntoWorld(this, p_77943_1_, p_77943_2_, p_77943_3_,
					p_77943_4_, p_77943_5_, p_77943_6_, p_77943_7_, p_77943_8_, p_77943_9_);
		boolean flag = getItem().onItemUse(this, p_77943_1_, p_77943_2_, p_77943_3_, p_77943_4_, p_77943_5_, p_77943_6_,
				p_77943_7_, p_77943_8_, p_77943_9_);

		if (flag) {
			p_77943_1_.addStat(StatList.objectUseStats[Item.getIdFromItem(field_151002_e)], 1);
		}

		return flag;
	}

	public float func_150997_a(Block p_150997_1_) {
		return getItem().func_150893_a(this, p_150997_1_);
	}

	public ItemStack useItemRightClick(World p_77957_1_, EntityPlayer p_77957_2_) {
		return getItem().onItemRightClick(this, p_77957_1_, p_77957_2_);
	}

	public ItemStack onFoodEaten(World p_77950_1_, EntityPlayer p_77950_2_) {
		return getItem().onEaten(this, p_77950_1_, p_77950_2_);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound p_77955_1_) {
		p_77955_1_.setShort("id", (short) Item.getIdFromItem(field_151002_e));
		p_77955_1_.setByte("Count", (byte) stackSize);
		p_77955_1_.setShort("Damage", (short) itemDamage);

		if (stackTagCompound != null) {
			p_77955_1_.setTag("tag", stackTagCompound.copy());
		}

		return p_77955_1_;
	}

	public void readFromNBT(NBTTagCompound p_77963_1_) {
		func_150996_a(Item.getItemById(p_77963_1_.getShort("id")));
		stackSize = p_77963_1_.getByte("Count");
		itemDamage = p_77963_1_.getShort("Damage");

		if (itemDamage < 0) {
			itemDamage = 0;
		}

		if (p_77963_1_.hasKey("tag", 10)) {
			stackTagCompound = p_77963_1_.getCompoundTag("tag");
		}
	}

	public int getMaxStackSize() {
		return getItem().getItemStackLimit(this);
	}

	public boolean isStackable() {
		return getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged());
	}

	public boolean isItemStackDamageable() {
		return field_151002_e.getMaxDamage(this) <= 0 ? false
				: !hasTagCompound() || !getTagCompound().getBoolean("Unbreakable");
	}

	public boolean getHasSubtypes() {
		return field_151002_e.getHasSubtypes();
	}

	public boolean isItemDamaged() {
		return isItemStackDamageable() && getItem().isDamaged(this);
	}

	public int getItemDamageForDisplay() {
		return getItem().getDisplayDamage(this);
	}

	public int getItemDamage() {
		return getItem().getDamage(this);
	}

	public void setItemDamage(int p_77964_1_) {
		getItem().setDamage(this, p_77964_1_);
	}

	public int getMaxDamage() {
		return getItem().getMaxDamage(this);
	}

	public boolean attemptDamageItem(int p_96631_1_, Random p_96631_2_) {
		if (!isItemStackDamageable())
			return false;
		else {
			if (p_96631_1_ > 0) {
				int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
				int k = 0;

				for (int l = 0; j > 0 && l < p_96631_1_; ++l) {
					if (EnchantmentDurability.negateDamage(this, j, p_96631_2_)) {
						++k;
					}
				}

				p_96631_1_ -= k;

				if (p_96631_1_ <= 0)
					return false;
			}

			setItemDamage(getItemDamage() + p_96631_1_); // Redirect through Item's callback if applicable.
			return getItemDamage() > getMaxDamage();
		}
	}

	public void damageItem(int p_77972_1_, EntityLivingBase p_77972_2_) {
		if (!(p_77972_2_ instanceof EntityPlayer) || !((EntityPlayer) p_77972_2_).capabilities.isCreativeMode) {
			if (isItemStackDamageable()) {
				if (attemptDamageItem(p_77972_1_, p_77972_2_.getRNG())) {
					p_77972_2_.renderBrokenItemStack(this);
					--stackSize;

					if (p_77972_2_ instanceof EntityPlayer) {
						EntityPlayer entityplayer = (EntityPlayer) p_77972_2_;
						entityplayer.addStat(StatList.objectBreakStats[Item.getIdFromItem(field_151002_e)], 1);

						if (stackSize == 0 && getItem() instanceof ItemBow) {
							entityplayer.destroyCurrentEquippedItem();
						}
					}

					if (stackSize < 0) {
						stackSize = 0;
					}

					if (this.stackSize == 0 && p_77972_2_ instanceof EntityPlayer) {
						CraftEventFactory.callPlayerItemBreakEvent((EntityPlayer) p_77972_2_, this);
					}

					itemDamage = 0;
				}
			}
		}
	}

	public void hitEntity(EntityLivingBase p_77961_1_, EntityPlayer p_77961_2_) {
		boolean flag = field_151002_e.hitEntity(this, p_77961_1_, p_77961_2_);

		if (flag) {
			p_77961_2_.addStat(StatList.objectUseStats[Item.getIdFromItem(field_151002_e)], 1);
		}
	}

	public void func_150999_a(World p_150999_1_, Block p_150999_2_, int p_150999_3_, int p_150999_4_, int p_150999_5_,
			EntityPlayer p_150999_6_) {
		boolean flag = field_151002_e.onBlockDestroyed(this, p_150999_1_, p_150999_2_, p_150999_3_, p_150999_4_,
				p_150999_5_, p_150999_6_);

		if (flag) {
			p_150999_6_.addStat(StatList.objectUseStats[Item.getIdFromItem(field_151002_e)], 1);
		}
	}

	public boolean func_150998_b(Block p_150998_1_) {
		return getItem().canHarvestBlock(p_150998_1_, this);
	}

	public boolean interactWithEntity(EntityPlayer p_111282_1_, EntityLivingBase p_111282_2_) {
		return field_151002_e.itemInteractionForEntity(this, p_111282_1_, p_111282_2_);
	}

	public ItemStack copy() {
		ItemStack itemstack = new ItemStack(field_151002_e, stackSize, itemDamage);

		if (stackTagCompound != null) {
			itemstack.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
		}

		return itemstack;
	}

	public static boolean areItemStackTagsEqual(ItemStack p_77970_0_, ItemStack p_77970_1_) {
		return p_77970_0_ == null
				&& p_77970_1_ == null
						? true
						: p_77970_0_ != null && p_77970_1_ != null
								? p_77970_0_.stackTagCompound == null && p_77970_1_.stackTagCompound != null ? false
										: p_77970_0_.stackTagCompound == null
												|| p_77970_0_.stackTagCompound.equals(p_77970_1_.stackTagCompound)
								: false;
	}

	public static boolean areItemStacksEqual(ItemStack p_77989_0_, ItemStack p_77989_1_) {
		return p_77989_0_ == null && p_77989_1_ == null ? true
				: p_77989_0_ != null && p_77989_1_ != null ? p_77989_0_.isItemStackEqual(p_77989_1_) : false;
	}

	private boolean isItemStackEqual(ItemStack p_77959_1_) {
		return stackSize != p_77959_1_.stackSize ? false
				: field_151002_e != p_77959_1_.field_151002_e ? false
						: itemDamage != p_77959_1_.itemDamage ? false
								: stackTagCompound == null && p_77959_1_.stackTagCompound != null ? false
										: stackTagCompound == null
												|| stackTagCompound.equals(p_77959_1_.stackTagCompound);
	}

	public boolean isItemEqual(ItemStack p_77969_1_) {
		return field_151002_e == p_77969_1_.field_151002_e && itemDamage == p_77969_1_.itemDamage;
	}

	public String getUnlocalizedName() {
		return field_151002_e.getUnlocalizedName(this);
	}

	public static ItemStack copyItemStack(ItemStack p_77944_0_) {
		return p_77944_0_ == null ? null : p_77944_0_.copy();
	}

	@Override
	public String toString() {
		return stackSize + "x" + field_151002_e.getUnlocalizedName() + "@" + itemDamage;
	}

	public void updateAnimation(World p_77945_1_, Entity p_77945_2_, int p_77945_3_, boolean p_77945_4_) {
		if (animationsToGo > 0) {
			--animationsToGo;
		}

		field_151002_e.onUpdate(this, p_77945_1_, p_77945_2_, p_77945_3_, p_77945_4_);
	}

	public void onCrafting(World p_77980_1_, EntityPlayer p_77980_2_, int p_77980_3_) {
		p_77980_2_.addStat(StatList.objectCraftStats[Item.getIdFromItem(field_151002_e)], p_77980_3_);
		field_151002_e.onCreated(this, p_77980_1_, p_77980_2_);
	}

	public int getMaxItemUseDuration() {
		return getItem().getMaxItemUseDuration(this);
	}

	public EnumAction getItemUseAction() {
		return getItem().getItemUseAction(this);
	}

	public void onPlayerStoppedUsing(World p_77974_1_, EntityPlayer p_77974_2_, int p_77974_3_) {
		getItem().onPlayerStoppedUsing(this, p_77974_1_, p_77974_2_, p_77974_3_);
	}

	public boolean hasTagCompound() {
		return stackTagCompound != null;
	}

	public NBTTagCompound getTagCompound() {
		return stackTagCompound;
	}

	public NBTTagList getEnchantmentTagList() {
		return stackTagCompound == null ? null : stackTagCompound.getTagList("ench", 10);
	}

	public void setTagCompound(NBTTagCompound p_77982_1_) {
		stackTagCompound = p_77982_1_;
	}

	public String getDisplayName() {
		String s = getItem().getItemStackDisplayName(this);

		if (stackTagCompound != null && stackTagCompound.hasKey("display", 10)) {
			NBTTagCompound nbttagcompound = stackTagCompound.getCompoundTag("display");

			if (nbttagcompound.hasKey("Name", 8)) {
				s = nbttagcompound.getString("Name");
			}
		}

		return s;
	}

	public ItemStack setStackDisplayName(String p_151001_1_) {
		if (stackTagCompound == null) {
			stackTagCompound = new NBTTagCompound();
		}

		if (!stackTagCompound.hasKey("display", 10)) {
			stackTagCompound.setTag("display", new NBTTagCompound());
		}

		stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
		return this;
	}

	public void func_135074_t() {
		if (stackTagCompound != null) {
			if (stackTagCompound.hasKey("display", 10)) {
				NBTTagCompound nbttagcompound = stackTagCompound.getCompoundTag("display");
				nbttagcompound.removeTag("Name");

				if (nbttagcompound.hasNoTags()) {
					stackTagCompound.removeTag("display");

					if (stackTagCompound.hasNoTags()) {
						setTagCompound((NBTTagCompound) null);
					}
				}
			}
		}
	}

	public boolean hasDisplayName() {
		return stackTagCompound == null ? false
				: !stackTagCompound.hasKey("display", 10) ? false
						: stackTagCompound.getCompoundTag("display").hasKey("Name", 8);
	}

	@SideOnly(Side.CLIENT)
	public List getTooltip(EntityPlayer p_82840_1_, boolean p_82840_2_) {
		ArrayList arraylist = new ArrayList();
		String s = getDisplayName();

		if (hasDisplayName()) {
			s = EnumChatFormatting.ITALIC + s + EnumChatFormatting.RESET;
		}

		int i;

		if (p_82840_2_) {
			String s1 = "";

			if (s.length() > 0) {
				s = s + " (";
				s1 = ")";
			}

			i = Item.getIdFromItem(field_151002_e);

			if (getHasSubtypes()) {
				s = s + String.format("#%04d/%d%s",
						new Object[] { Integer.valueOf(i), Integer.valueOf(itemDamage), s1 });
			} else {
				s = s + String.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });
			}
		} else if (!hasDisplayName() && field_151002_e == Items.filled_map) {
			s = s + " #" + itemDamage;
		}

		arraylist.add(s);
		field_151002_e.addInformation(this, p_82840_1_, arraylist, p_82840_2_);

		if (hasTagCompound()) {
			NBTTagList nbttaglist = getEnchantmentTagList();

			if (nbttaglist != null) {
				for (i = 0; i < nbttaglist.tagCount(); ++i) {
					short short1 = nbttaglist.getCompoundTagAt(i).getShort("id");
					short short2 = nbttaglist.getCompoundTagAt(i).getShort("lvl");

					if (Enchantment.enchantmentsList[short1] != null) {
						arraylist.add(Enchantment.enchantmentsList[short1].getTranslatedName(short2));
					}
				}
			}

			if (stackTagCompound.hasKey("display", 10)) {
				NBTTagCompound nbttagcompound = stackTagCompound.getCompoundTag("display");

				if (nbttagcompound.hasKey("color", 3)) {
					if (p_82840_2_) {
						arraylist.add(
								"Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());
					} else {
						arraylist.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
					}
				}

				if (nbttagcompound.func_150299_b("Lore") == 9) {
					NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);

					if (nbttaglist1.tagCount() > 0) {
						for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
							arraylist.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC
									+ nbttaglist1.getStringTagAt(j));
						}
					}
				}
			}
		}

		Multimap multimap = getAttributeModifiers();

		if (!multimap.isEmpty()) {
			arraylist.add("");
			Iterator iterator = multimap.entries().iterator();

			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
				double d0 = attributemodifier.getAmount();

				if (attributemodifier.getID() == Item.field_111210_e) {
					d0 += EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
				}

				double d1;

				if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2) {
					d1 = d0;
				} else {
					d1 = d0 * 100.0D;
				}

				if (d0 > 0.0D) {
					arraylist.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted(
							"attribute.modifier.plus." + attributemodifier.getOperation(),
							new Object[] { field_111284_a.format(d1),
									StatCollector.translateToLocal("attribute.name." + (String) entry.getKey()) }));
				} else if (d0 < 0.0D) {
					d1 *= -1.0D;
					arraylist.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(
							"attribute.modifier.take." + attributemodifier.getOperation(),
							new Object[] { field_111284_a.format(d1),
									StatCollector.translateToLocal("attribute.name." + (String) entry.getKey()) }));
				}
			}
		}

		if (hasTagCompound() && getTagCompound().getBoolean("Unbreakable")) {
			arraylist.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
		}

		if (p_82840_2_ && isItemDamaged()) {
			arraylist.add("Durability: " + (getMaxDamage() - getItemDamageForDisplay()) + " / " + getMaxDamage());
		}
		ForgeEventFactory.onItemTooltip(this, p_82840_1_, arraylist, p_82840_2_);

		return arraylist;
	}

	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean hasEffect() {
		return hasEffect(0);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(int pass) {
		return getItem().hasEffect(this, pass);
	}

	public EnumRarity getRarity() {
		return getItem().getRarity(this);
	}

	public boolean isItemEnchantable() {
		return !getItem().isItemTool(this) ? false : !isItemEnchanted();
	}

	public void addEnchantment(Enchantment p_77966_1_, int p_77966_2_) {
		if (stackTagCompound == null) {
			setTagCompound(new NBTTagCompound());
		}

		if (!stackTagCompound.hasKey("ench", 9)) {
			stackTagCompound.setTag("ench", new NBTTagList());
		}

		NBTTagList nbttaglist = stackTagCompound.getTagList("ench", 10);
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setShort("id", (short) p_77966_1_.effectId);
		nbttagcompound.setShort("lvl", (byte) p_77966_2_);
		nbttaglist.appendTag(nbttagcompound);
	}

	public boolean isItemEnchanted() {
		return stackTagCompound != null && stackTagCompound.hasKey("ench", 9);
	}

	public void setTagInfo(String p_77983_1_, NBTBase p_77983_2_) {
		if (stackTagCompound == null) {
			setTagCompound(new NBTTagCompound());
		}

		stackTagCompound.setTag(p_77983_1_, p_77983_2_);
	}

	public boolean canEditBlocks() {
		return getItem().canItemEditBlocks();
	}

	public boolean isOnItemFrame() {
		return itemFrame != null;
	}

	public void setItemFrame(EntityItemFrame p_82842_1_) {
		itemFrame = p_82842_1_;
	}

	public EntityItemFrame getItemFrame() {
		return itemFrame;
	}

	public int getRepairCost() {
		return hasTagCompound() && stackTagCompound.hasKey("RepairCost", 3) ? stackTagCompound.getInteger("RepairCost")
				: 0;
	}

	public void setRepairCost(int p_82841_1_) {
		if (!hasTagCompound()) {
			stackTagCompound = new NBTTagCompound();
		}

		stackTagCompound.setInteger("RepairCost", p_82841_1_);
	}

	public Multimap getAttributeModifiers() {
		Object object;

		if (hasTagCompound() && stackTagCompound.hasKey("AttributeModifiers", 9)) {
			object = HashMultimap.create();
			NBTTagList nbttaglist = stackTagCompound.getTagList("AttributeModifiers", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				AttributeModifier attributemodifier = SharedMonsterAttributes
						.readAttributeModifierFromNBT(nbttagcompound);

				if (attributemodifier.getID().getLeastSignificantBits() != 0L
						&& attributemodifier.getID().getMostSignificantBits() != 0L) {
					((Multimap) object).put(nbttagcompound.getString("AttributeName"), attributemodifier);
				}
			}
		} else {
			object = getItem().getAttributeModifiers(this);
		}

		return (Multimap) object;
	}

	public void func_150996_a(Item p_150996_1_) {
		delegate = p_150996_1_ != null ? p_150996_1_.delegate : null;
		field_151002_e = p_150996_1_;
	}

	public IChatComponent func_151000_E() {
		IChatComponent ichatcomponent = new ChatComponentText("[").appendText(getDisplayName()).appendText("]");

		if (field_151002_e != null) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeToNBT(nbttagcompound);
			ichatcomponent.getChatStyle().setChatHoverEvent(
					new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nbttagcompound.toString())));
			ichatcomponent.getChatStyle().setColor(getRarity().rarityColor);
		}

		return ichatcomponent;
	}
}