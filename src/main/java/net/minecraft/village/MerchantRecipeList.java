package net.minecraft.village;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings({ "rawtypes", "serial" })
public class MerchantRecipeList extends ArrayList {
	public MerchantRecipeList() {
	}

	public MerchantRecipeList(NBTTagCompound p_i1944_1_) {
		readRecipiesFromTags(p_i1944_1_);
	}

	public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_) {
		if (p_77203_3_ > 0 && p_77203_3_ < size()) {
			MerchantRecipe merchantrecipe1 = (MerchantRecipe) get(p_77203_3_);
			return p_77203_1_.getItem() == merchantrecipe1.getItemToBuy().getItem()
					&& (p_77203_2_ == null && !merchantrecipe1.hasSecondItemToBuy()
							|| merchantrecipe1.hasSecondItemToBuy() && p_77203_2_ != null
									&& merchantrecipe1.getSecondItemToBuy().getItem() == p_77203_2_.getItem())
					&& p_77203_1_.stackSize >= merchantrecipe1.getItemToBuy().stackSize
					&& (!merchantrecipe1.hasSecondItemToBuy()
							|| p_77203_2_.stackSize >= merchantrecipe1.getSecondItemToBuy().stackSize) ? merchantrecipe1
									: null;
		} else {
			for (int j = 0; j < size(); ++j) {
				MerchantRecipe merchantrecipe = (MerchantRecipe) get(j);

				if (p_77203_1_.getItem() == merchantrecipe.getItemToBuy().getItem()
						&& p_77203_1_.stackSize >= merchantrecipe.getItemToBuy().stackSize
						&& (!merchantrecipe.hasSecondItemToBuy() && p_77203_2_ == null
								|| merchantrecipe.hasSecondItemToBuy() && p_77203_2_ != null
										&& merchantrecipe.getSecondItemToBuy().getItem() == p_77203_2_.getItem()
										&& p_77203_2_.stackSize >= merchantrecipe.getSecondItemToBuy().stackSize))
					return merchantrecipe;
			}

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void addToListWithCheck(MerchantRecipe p_77205_1_) {
		for (int i = 0; i < size(); ++i) {
			MerchantRecipe merchantrecipe1 = (MerchantRecipe) get(i);

			if (p_77205_1_.hasSameIDsAs(merchantrecipe1)) {
				if (p_77205_1_.hasSameItemsAs(merchantrecipe1)) {
					set(i, p_77205_1_);
				}

				return;
			}
		}

		this.add(p_77205_1_);
	}

	public void func_151391_a(PacketBuffer p_151391_1_) throws IOException {
		p_151391_1_.writeByte((byte) (size() & 255));

		for (int i = 0; i < size(); ++i) {
			MerchantRecipe merchantrecipe = (MerchantRecipe) get(i);
			p_151391_1_.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
			p_151391_1_.writeItemStackToBuffer(merchantrecipe.getItemToSell());
			ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
			p_151391_1_.writeBoolean(itemstack != null);

			if (itemstack != null) {
				p_151391_1_.writeItemStackToBuffer(itemstack);
			}

			p_151391_1_.writeBoolean(merchantrecipe.isRecipeDisabled());
		}
	}

	@SuppressWarnings("unchecked")
	public void readRecipiesFromTags(NBTTagCompound p_77201_1_) {
		NBTTagList nbttaglist = p_77201_1_.getTagList("Recipes", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			this.add(new MerchantRecipe(nbttagcompound1));
		}
	}

	public NBTTagCompound getRecipiesAsTags() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < size(); ++i) {
			MerchantRecipe merchantrecipe = (MerchantRecipe) get(i);
			nbttaglist.appendTag(merchantrecipe.writeToTags());
		}

		nbttagcompound.setTag("Recipes", nbttaglist);
		return nbttagcompound;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public static MerchantRecipeList func_151390_b(PacketBuffer p_151390_0_) throws IOException {
		MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
		int i = p_151390_0_.readByte() & 255;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = p_151390_0_.readItemStackFromBuffer();
			ItemStack itemstack1 = p_151390_0_.readItemStackFromBuffer();
			ItemStack itemstack2 = null;

			if (p_151390_0_.readBoolean()) {
				itemstack2 = p_151390_0_.readItemStackFromBuffer();
			}

			boolean flag = p_151390_0_.readBoolean();
			MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1);

			if (flag) {
				merchantrecipe.func_82785_h();
			}

			merchantrecipelist.add(merchantrecipe);
		}

		return merchantrecipelist;
	}
}