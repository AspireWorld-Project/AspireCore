package net.minecraft.potion;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PotionEffect {
	private int potionID;
	private int duration;
	private int amplifier;
	private boolean isSplashPotion;
	private boolean isAmbient;
	@SideOnly(Side.CLIENT)
	private boolean isPotionDurationMax;
	private static final String __OBFID = "CL_00001529";
	/** List of ItemStack that can cure the potion effect **/
	private List<ItemStack> curativeItems;

	public PotionEffect(int p_i1574_1_, int p_i1574_2_) {
		this(p_i1574_1_, p_i1574_2_, 0);
	}

	public PotionEffect(int p_i1575_1_, int p_i1575_2_, int p_i1575_3_) {
		this(p_i1575_1_, p_i1575_2_, p_i1575_3_, false);
	}

	public PotionEffect(int p_i1576_1_, int p_i1576_2_, int p_i1576_3_, boolean p_i1576_4_) {
		potionID = p_i1576_1_;
		duration = p_i1576_2_;
		amplifier = p_i1576_3_;
		isAmbient = p_i1576_4_;
		curativeItems = new ArrayList<>();
		curativeItems.add(new ItemStack(Items.milk_bucket));
	}

	public PotionEffect(PotionEffect p_i1577_1_) {
		potionID = p_i1577_1_.potionID;
		duration = p_i1577_1_.duration;
		amplifier = p_i1577_1_.amplifier;
		curativeItems = p_i1577_1_.curativeItems;
	}

	public void combine(PotionEffect p_76452_1_) {
		if (potionID != p_76452_1_.potionID) {
			System.err.println("This method should only be called for matching effects!");
		}

		if (p_76452_1_.amplifier > amplifier) {
			amplifier = p_76452_1_.amplifier;
			duration = p_76452_1_.duration;
		} else if (p_76452_1_.amplifier == amplifier && duration < p_76452_1_.duration) {
			duration = p_76452_1_.duration;
		} else if (!p_76452_1_.isAmbient && isAmbient) {
			isAmbient = p_76452_1_.isAmbient;
		}
	}

	public int getPotionID() {
		return potionID;
	}

	public int getDuration() {
		return duration;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setSplashPotion(boolean p_82721_1_) {
		isSplashPotion = p_82721_1_;
	}

	public boolean getIsAmbient() {
		return isAmbient;
	}

	public boolean onUpdate(EntityLivingBase p_76455_1_) {
		if (duration > 0) {
			if (Potion.potionTypes[potionID].isReady(duration, amplifier)) {
				performEffect(p_76455_1_);
			}

			deincrementDuration();
		}

		return duration > 0;
	}

	private int deincrementDuration() {
		return --duration;
	}

	public void performEffect(EntityLivingBase p_76457_1_) {
		if (duration > 0) {
			Potion.potionTypes[potionID].performEffect(p_76457_1_, amplifier);
		}
	}

	public String getEffectName() {
		return Potion.potionTypes[potionID].getName();
	}

	@Override
	public int hashCode() {
		return potionID;
	}

	@Override
	public String toString() {
		String s = "";

		if (getAmplifier() > 0) {
			s = getEffectName() + " x " + (getAmplifier() + 1) + ", Duration: " + getDuration();
		} else {
			s = getEffectName() + ", Duration: " + getDuration();
		}

		if (isSplashPotion) {
			s = s + ", Splash: true";
		}

		return Potion.potionTypes[potionID].isUsable() ? "(" + s + ")" : s;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof PotionEffect))
			return false;
		else {
			PotionEffect potioneffect = (PotionEffect) p_equals_1_;
			return potionID == potioneffect.potionID && amplifier == potioneffect.amplifier
					&& duration == potioneffect.duration && isSplashPotion == potioneffect.isSplashPotion
					&& isAmbient == potioneffect.isAmbient;
		}
	}

	public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound p_82719_1_) {
		p_82719_1_.setByte("Id", (byte) getPotionID());
		p_82719_1_.setByte("Amplifier", (byte) getAmplifier());
		p_82719_1_.setInteger("Duration", getDuration());
		p_82719_1_.setBoolean("Ambient", getIsAmbient());
		return p_82719_1_;
	}

	public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound p_82722_0_) {
		byte b0 = p_82722_0_.getByte("Id");

		if (b0 >= 0 && b0 < Potion.potionTypes.length && Potion.potionTypes[b0] != null) {
			byte b1 = p_82722_0_.getByte("Amplifier");
			int i = p_82722_0_.getInteger("Duration");
			boolean flag = p_82722_0_.getBoolean("Ambient");
			return new PotionEffect(b0, i, b1, flag);
		} else
			return null;
	}

	@SideOnly(Side.CLIENT)
	public void setPotionDurationMax(boolean p_100012_1_) {
		isPotionDurationMax = p_100012_1_;
	}

	@SideOnly(Side.CLIENT)
	public boolean getIsPotionDurationMax() {
		return isPotionDurationMax;
	}

	/*
	 * ======================================== FORGE START
	 * =====================================
	 */
	/***
	 * Returns a list of curative items for the potion effect
	 *
	 * @return The list (ItemStack) of curative items for the potion effect
	 */
	public List<ItemStack> getCurativeItems() {
		return curativeItems;
	}

	/***
	 * Checks the given ItemStack to see if it is in the list of curative items for
	 * the potion effect
	 *
	 * @param stack
	 *            The ItemStack being checked against the list of curative items for
	 *            the potion effect
	 * @return true if the given ItemStack is in the list of curative items for the
	 *         potion effect, false otherwise
	 */
	public boolean isCurativeItem(ItemStack stack) {
		boolean found = false;
		for (ItemStack curativeItem : curativeItems) {
			if (curativeItem.isItemEqual(stack)) {
				found = true;
			}
		}

		return found;
	}

	/***
	 * Sets the array of curative items for the potion effect
	 *
	 * @param curativeItems
	 *            The list of ItemStacks being set to the potion effect
	 */
	public void setCurativeItems(List<ItemStack> curativeItems) {
		this.curativeItems = curativeItems;
	}

	/***
	 * Adds the given stack to list of curative items for the potion effect
	 *
	 * @param stack
	 *            The ItemStack being added to the curative item list
	 */
	public void addCurativeItem(ItemStack stack) {
		boolean found = false;
		for (ItemStack curativeItem : curativeItems) {
			if (curativeItem.isItemEqual(stack)) {
				found = true;
			}
		}
		if (!found) {
			curativeItems.add(stack);
		}
	}
}