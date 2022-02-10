package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class Achievement extends StatBase {
	public final int displayColumn;
	public final int displayRow;
	public final Achievement parentAchievement;
	private final String achievementDescription;
	@SideOnly(Side.CLIENT)
	private IStatStringFormat statStringFormatter;
	public final ItemStack theItemStack;
	private boolean isSpecial;
	private static final String __OBFID = "CL_00001466";

	public Achievement(String p_i45300_1_, String p_i45300_2_, int p_i45300_3_, int p_i45300_4_, Item p_i45300_5_,
			Achievement p_i45300_6_) {
		this(p_i45300_1_, p_i45300_2_, p_i45300_3_, p_i45300_4_, new ItemStack(p_i45300_5_), p_i45300_6_);
	}

	public Achievement(String p_i45301_1_, String p_i45301_2_, int p_i45301_3_, int p_i45301_4_, Block p_i45301_5_,
			Achievement p_i45301_6_) {
		this(p_i45301_1_, p_i45301_2_, p_i45301_3_, p_i45301_4_, new ItemStack(p_i45301_5_), p_i45301_6_);
	}

	public Achievement(String p_i45302_1_, String p_i45302_2_, int p_i45302_3_, int p_i45302_4_, ItemStack p_i45302_5_,
			Achievement p_i45302_6_) {
		super(p_i45302_1_, new ChatComponentTranslation("achievement." + p_i45302_2_, new Object[0]));
		theItemStack = p_i45302_5_;
		achievementDescription = "achievement." + p_i45302_2_ + ".desc";
		displayColumn = p_i45302_3_;
		displayRow = p_i45302_4_;

		if (p_i45302_3_ < AchievementList.minDisplayColumn) {
			AchievementList.minDisplayColumn = p_i45302_3_;
		}

		if (p_i45302_4_ < AchievementList.minDisplayRow) {
			AchievementList.minDisplayRow = p_i45302_4_;
		}

		if (p_i45302_3_ > AchievementList.maxDisplayColumn) {
			AchievementList.maxDisplayColumn = p_i45302_3_;
		}

		if (p_i45302_4_ > AchievementList.maxDisplayRow) {
			AchievementList.maxDisplayRow = p_i45302_4_;
		}

		parentAchievement = p_i45302_6_;
	}

	@Override
	public Achievement initIndependentStat() {
		isIndependent = true;
		return this;
	}

	public Achievement setSpecial() {
		isSpecial = true;
		return this;
	}

	@Override
	public Achievement registerStat() {
		super.registerStat();
		AchievementList.achievementList.add(this);
		return this;
	}

	@Override
	public boolean isAchievement() {
		return true;
	}

	@Override
	public IChatComponent func_150951_e() {
		IChatComponent ichatcomponent = super.func_150951_e();
		ichatcomponent.getChatStyle()
				.setColor(getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
		return ichatcomponent;
	}

	@Override
	public Achievement func_150953_b(Class p_150953_1_) {
		return (Achievement) super.func_150953_b(p_150953_1_);
	}

	@SideOnly(Side.CLIENT)
	public String getDescription() {
		return statStringFormatter != null
				? statStringFormatter.formatString(StatCollector.translateToLocal(achievementDescription))
				: StatCollector.translateToLocal(achievementDescription);
	}

	@SideOnly(Side.CLIENT)
	public Achievement setStatStringFormatter(IStatStringFormat p_75988_1_) {
		statStringFormatter = p_75988_1_;
		return this;
	}

	public boolean getSpecial() {
		return isSpecial;
	}
}