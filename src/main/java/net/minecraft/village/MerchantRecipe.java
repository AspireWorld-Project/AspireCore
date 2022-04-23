package net.minecraft.village;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe {
	private ItemStack itemToBuy;
	private ItemStack secondItemToBuy;
	private ItemStack itemToSell;
	private int toolUses;
	private int maxTradeUses;
	private static final String __OBFID = "CL_00000126";

	public MerchantRecipe(NBTTagCompound p_i1940_1_) {
		readFromTags(p_i1940_1_);
	}

	public MerchantRecipe(ItemStack p_i1941_1_, ItemStack p_i1941_2_, ItemStack p_i1941_3_) {
		itemToBuy = p_i1941_1_;
		secondItemToBuy = p_i1941_2_;
		itemToSell = p_i1941_3_;
		maxTradeUses = 7;
	}

	public MerchantRecipe(ItemStack p_i1942_1_, ItemStack p_i1942_2_) {
		this(p_i1942_1_, null, p_i1942_2_);
	}

	public MerchantRecipe(ItemStack p_i1943_1_, Item p_i1943_2_) {
		this(p_i1943_1_, new ItemStack(p_i1943_2_));
	}

	public ItemStack getItemToBuy() {
		return itemToBuy;
	}

	public ItemStack getSecondItemToBuy() {
		return secondItemToBuy;
	}

	public boolean hasSecondItemToBuy() {
		return secondItemToBuy != null;
	}

	public ItemStack getItemToSell() {
		return itemToSell;
	}

	public boolean hasSameIDsAs(MerchantRecipe p_77393_1_) {
		return itemToBuy.getItem() == p_77393_1_.itemToBuy.getItem()
				&& itemToSell.getItem() == p_77393_1_.itemToSell.getItem() && (secondItemToBuy == null && p_77393_1_.secondItemToBuy == null
				|| secondItemToBuy != null && p_77393_1_.secondItemToBuy != null
				&& secondItemToBuy.getItem() == p_77393_1_.secondItemToBuy.getItem());
	}

	public boolean hasSameItemsAs(MerchantRecipe p_77391_1_) {
		return hasSameIDsAs(p_77391_1_) && (itemToBuy.stackSize < p_77391_1_.itemToBuy.stackSize
				|| secondItemToBuy != null && secondItemToBuy.stackSize < p_77391_1_.secondItemToBuy.stackSize);
	}

	public void incrementToolUses() {
		++toolUses;
	}

	public void func_82783_a(int p_82783_1_) {
		maxTradeUses += p_82783_1_;
	}

	public boolean isRecipeDisabled() {
		return toolUses >= maxTradeUses;
	}

	@SideOnly(Side.CLIENT)
	public void func_82785_h() {
		toolUses = maxTradeUses;
	}

	public void readFromTags(NBTTagCompound p_77390_1_) {
		NBTTagCompound nbttagcompound1 = p_77390_1_.getCompoundTag("buy");
		itemToBuy = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		NBTTagCompound nbttagcompound2 = p_77390_1_.getCompoundTag("sell");
		itemToSell = ItemStack.loadItemStackFromNBT(nbttagcompound2);

		if (p_77390_1_.hasKey("buyB", 10)) {
			secondItemToBuy = ItemStack.loadItemStackFromNBT(p_77390_1_.getCompoundTag("buyB"));
		}

		if (p_77390_1_.hasKey("uses", 99)) {
			toolUses = p_77390_1_.getInteger("uses");
		}

		if (p_77390_1_.hasKey("maxUses", 99)) {
			maxTradeUses = p_77390_1_.getInteger("maxUses");
		} else {
			maxTradeUses = 7;
		}
	}

	public NBTTagCompound writeToTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("buy", itemToBuy.writeToNBT(new NBTTagCompound()));
		nbttagcompound.setTag("sell", itemToSell.writeToNBT(new NBTTagCompound()));

		if (secondItemToBuy != null) {
			nbttagcompound.setTag("buyB", secondItemToBuy.writeToNBT(new NBTTagCompound()));
		}

		nbttagcompound.setInteger("uses", toolUses);
		nbttagcompound.setInteger("maxUses", maxTradeUses);
		return nbttagcompound;
	}
}