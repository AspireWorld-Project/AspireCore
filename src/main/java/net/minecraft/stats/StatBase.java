package net.minecraft.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBase {
	public final String statId;
	private final IChatComponent statName;
	public boolean isIndependent;
	private final IStatType type;
	private final IScoreObjectiveCriteria field_150957_c;
	@SuppressWarnings("rawtypes")
	private Class field_150956_d;
	private static final NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);
	public static IStatType simpleStatType = new IStatType() {
		@Override
		@SideOnly(Side.CLIENT)
		public String format(int p_75843_1_) {
			return StatBase.numberFormat.format(p_75843_1_);
		}
	};
	private static final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
	public static IStatType timeStatType = new IStatType() {
		@Override
		@SideOnly(Side.CLIENT)
		public String format(int p_75843_1_) {
			double d0 = p_75843_1_ / 20.0D;
			double d1 = d0 / 60.0D;
			double d2 = d1 / 60.0D;
			double d3 = d2 / 24.0D;
			double d4 = d3 / 365.0D;
			return d4 > 0.5D ? StatBase.decimalFormat.format(d4) + " y"
					: d3 > 0.5D ? StatBase.decimalFormat.format(d3) + " d"
							: d2 > 0.5D ? StatBase.decimalFormat.format(d2) + " h"
									: d1 > 0.5D ? StatBase.decimalFormat.format(d1) + " m" : d0 + " s";
		}
	};
	public static IStatType distanceStatType = new IStatType() {
		@Override
		@SideOnly(Side.CLIENT)
		public String format(int p_75843_1_) {
			double d0 = p_75843_1_ / 100.0D;
			double d1 = d0 / 1000.0D;
			return d1 > 0.5D ? StatBase.decimalFormat.format(d1) + " km"
					: d0 > 0.5D ? StatBase.decimalFormat.format(d0) + " m" : p_75843_1_ + " cm";
		}
	};
	public static IStatType field_111202_k = new IStatType() {
		@Override
		@SideOnly(Side.CLIENT)
		public String format(int p_75843_1_) {
			return StatBase.decimalFormat.format(p_75843_1_ * 0.1D);
		}
	};
	@SuppressWarnings("unchecked")
	public StatBase(String p_i45307_1_, IChatComponent p_i45307_2_, IStatType p_i45307_3_) {
		statId = p_i45307_1_;
		statName = p_i45307_2_;
		type = p_i45307_3_;
		field_150957_c = new ObjectiveStat(this);
		IScoreObjectiveCriteria.field_96643_a.put(field_150957_c.func_96636_a(), field_150957_c);
	}

	public StatBase(String p_i45308_1_, IChatComponent p_i45308_2_) {
		this(p_i45308_1_, p_i45308_2_, simpleStatType);
	}

	public StatBase initIndependentStat() {
		isIndependent = true;
		return this;
	}

	@SuppressWarnings("unchecked")
	public StatBase registerStat() {
		if (StatList.oneShotStats.containsKey(statId))
			throw new RuntimeException("Duplicate stat id: \"" + ((StatBase) StatList.oneShotStats.get(statId)).statName
					+ "\" and \"" + statName + "\" at id " + statId);
		else {
			StatList.allStats.add(this);
			StatList.oneShotStats.put(statId, this);
			return this;
		}
	}

	public boolean isAchievement() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public String func_75968_a(int p_75968_1_) {
		return type.format(p_75968_1_);
	}

	public IChatComponent func_150951_e() {
		IChatComponent ichatcomponent = statName.createCopy();
		ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
		ichatcomponent.getChatStyle()
				.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(statId)));
		return ichatcomponent;
	}

	public IChatComponent func_150955_j() {
		IChatComponent ichatcomponent = func_150951_e();
		IChatComponent ichatcomponent1 = new ChatComponentText("[").appendSibling(ichatcomponent).appendText("]");
		ichatcomponent1.setChatStyle(ichatcomponent.getChatStyle());
		return ichatcomponent1;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
			StatBase statbase = (StatBase) p_equals_1_;
			return statId.equals(statbase.statId);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return statId.hashCode();
	}

	@Override
	public String toString() {
		return "Stat{id=" + statId + ", nameId=" + statName + ", awardLocallyOnly=" + isIndependent + ", formatter="
				+ type + ", objectiveCriteria=" + field_150957_c + '}';
	}

	public IScoreObjectiveCriteria func_150952_k() {
		return field_150957_c;
	}

	@SuppressWarnings("rawtypes")
	public Class func_150954_l() {
		return field_150956_d;
	}

	@SuppressWarnings("rawtypes")
	public StatBase func_150953_b(Class p_150953_1_) {
		field_150956_d = p_150953_1_;
		return this;
	}
}