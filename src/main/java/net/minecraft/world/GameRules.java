package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class GameRules {
	private final TreeMap theGameRules = new TreeMap();
	private static final String __OBFID = "CL_00000136";

	public GameRules() {
		addGameRule("doFireTick", "true");
		addGameRule("mobGriefing", "true");
		addGameRule("keepInventory", "false");
		addGameRule("doMobSpawning", "true");
		addGameRule("doMobLoot", "true");
		addGameRule("doTileDrops", "true");
		addGameRule("commandBlockOutput", "true");
		addGameRule("naturalRegeneration", "true");
		addGameRule("doDaylightCycle", "true");
	}

	public void addGameRule(String p_82769_1_, String p_82769_2_) {
		theGameRules.put(p_82769_1_, new GameRules.Value(p_82769_2_));
	}

	public void setOrCreateGameRule(String p_82764_1_, String p_82764_2_) {
		GameRules.Value value = (GameRules.Value) theGameRules.get(p_82764_1_);

		if (value != null) {
			value.setValue(p_82764_2_);
		} else {
			addGameRule(p_82764_1_, p_82764_2_);
		}
	}

	public String getGameRuleStringValue(String p_82767_1_) {
		GameRules.Value value = (GameRules.Value) theGameRules.get(p_82767_1_);
		return value != null ? value.getGameRuleStringValue() : "";
	}

	public boolean getGameRuleBooleanValue(String p_82766_1_) {
		GameRules.Value value = (GameRules.Value) theGameRules.get(p_82766_1_);
		return value != null && value.getGameRuleBooleanValue();
	}

	public NBTTagCompound writeGameRulesToNBT() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		Iterator iterator = theGameRules.keySet().iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			GameRules.Value value = (GameRules.Value) theGameRules.get(s);
			nbttagcompound.setString(s, value.getGameRuleStringValue());
		}

		return nbttagcompound;
	}

	public void readGameRulesFromNBT(NBTTagCompound p_82768_1_) {
		Set set = p_82768_1_.func_150296_c();
		Iterator iterator = set.iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();
			String s1 = p_82768_1_.getString(s);
			setOrCreateGameRule(s, s1);
		}
	}

	public String[] getRules() {
		return (String[]) theGameRules.keySet().toArray(new String[0]);
	}

	public boolean hasRule(String p_82765_1_) {
		return theGameRules.containsKey(p_82765_1_);
	}

	static class Value {
		private String valueString;
		private boolean valueBoolean;
		private int valueInteger;
		private double valueDouble;
		private static final String __OBFID = "CL_00000137";

		public Value(String p_i1949_1_) {
			setValue(p_i1949_1_);
		}

		public void setValue(String p_82757_1_) {
			valueString = p_82757_1_;
			valueBoolean = Boolean.parseBoolean(p_82757_1_);

			try {
				valueInteger = Integer.parseInt(p_82757_1_);
			} catch (NumberFormatException numberformatexception1) {
			}

			try {
				valueDouble = Double.parseDouble(p_82757_1_);
			} catch (NumberFormatException numberformatexception) {
			}
		}

		public String getGameRuleStringValue() {
			return valueString;
		}

		public boolean getGameRuleBooleanValue() {
			return valueBoolean;
		}
	}
}