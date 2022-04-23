package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import java.util.List;

public class ItemEditableBook extends Item {
	private static final String __OBFID = "CL_00000077";

	public ItemEditableBook() {
		setMaxStackSize(1);
	}

	public static boolean validBookTagContents(NBTTagCompound p_77828_0_) {
		if (!ItemWritableBook.func_150930_a(p_77828_0_))
			return false;
		else if (!p_77828_0_.hasKey("title", 8))
			return false;
		else {
			String s = p_77828_0_.getString("title");
			return s != null && s.length() <= 16 && p_77828_0_.hasKey("author", 8);
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		if (p_77653_1_.hasTagCompound()) {
			NBTTagCompound nbttagcompound = p_77653_1_.getTagCompound();
			String s = nbttagcompound.getString("title");

			if (!StringUtils.isNullOrEmpty(s))
				return s;
		}

		return super.getItemStackDisplayName(p_77653_1_);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		if (p_77624_1_.hasTagCompound()) {
			NBTTagCompound nbttagcompound = p_77624_1_.getTagCompound();
			String s = nbttagcompound.getString("author");

			if (!StringUtils.isNullOrEmpty(s)) {
				p_77624_3_.add(EnumChatFormatting.GRAY
						+ StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { s }));
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		p_77659_3_.displayGUIBook(p_77659_1_);
		return p_77659_1_;
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack p_77636_1_) {
		return true;
	}
}