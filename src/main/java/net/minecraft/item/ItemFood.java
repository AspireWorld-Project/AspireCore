package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFood extends Item {
	public final int itemUseDuration;
	private final int healAmount;
	private final float saturationModifier;
	private final boolean isWolfsFavoriteMeat;
	private boolean alwaysEdible;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;
	private static final String __OBFID = "CL_00000036";

	public ItemFood(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_) {
		itemUseDuration = 32;
		healAmount = p_i45339_1_;
		isWolfsFavoriteMeat = p_i45339_3_;
		saturationModifier = p_i45339_2_;
		setCreativeTab(CreativeTabs.tabFood);
	}

	public ItemFood(int p_i45340_1_, boolean p_i45340_2_) {
		this(p_i45340_1_, 0.6F, p_i45340_2_);
	}

	@Override
	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
		--p_77654_1_.stackSize;
		p_77654_3_.getFoodStats().func_151686_a(this, p_77654_1_);
		p_77654_2_.playSoundAtEntity(p_77654_3_, "random.burp", 0.5F, p_77654_2_.rand.nextFloat() * 0.1F + 0.9F);
		onFoodEaten(p_77654_1_, p_77654_2_, p_77654_3_);
		return p_77654_1_;
	}

	protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_) {
		if (!p_77849_2_.isRemote && potionId > 0 && p_77849_2_.rand.nextFloat() < potionEffectProbability) {
			p_77849_3_.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.eat;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		if (p_77659_3_.canEat(alwaysEdible)) {
			p_77659_3_.setItemInUse(p_77659_1_, getMaxItemUseDuration(p_77659_1_));
		}

		return p_77659_1_;
	}

	public int func_150905_g(ItemStack p_150905_1_) {
		return healAmount;
	}

	public float func_150906_h(ItemStack p_150906_1_) {
		return saturationModifier;
	}

	public boolean isWolfsFavoriteMeat() {
		return isWolfsFavoriteMeat;
	}

	public ItemFood setPotionEffect(int p_77844_1_, int p_77844_2_, int p_77844_3_, float p_77844_4_) {
		potionId = p_77844_1_;
		potionDuration = p_77844_2_;
		potionAmplifier = p_77844_3_;
		potionEffectProbability = p_77844_4_;
		return this;
	}

	public ItemFood setAlwaysEdible() {
		alwaysEdible = true;
		return this;
	}
}