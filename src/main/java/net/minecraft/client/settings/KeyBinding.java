package net.minecraft.client.settings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

import java.util.*;

@SideOnly(Side.CLIENT)
public class KeyBinding implements Comparable {
	private static final List keybindArray = new ArrayList();
	private static final IntHashMap hash = new IntHashMap();
	private static final Set keybindSet = new HashSet();
	private final String keyDescription;
	private final int keyCodeDefault;
	private final String keyCategory;
	private int keyCode;
	private boolean pressed;
	private int pressTime;
	private static final String __OBFID = "CL_00000628";

	public static void onTick(int p_74507_0_) {
		if (p_74507_0_ != 0) {
			KeyBinding keybinding = (KeyBinding) hash.lookup(p_74507_0_);

			if (keybinding != null) {
				++keybinding.pressTime;
			}
		}
	}

	public static void setKeyBindState(int p_74510_0_, boolean p_74510_1_) {
		if (p_74510_0_ != 0) {
			KeyBinding keybinding = (KeyBinding) hash.lookup(p_74510_0_);

			if (keybinding != null) {
				keybinding.pressed = p_74510_1_;
			}
		}
	}

	public static void unPressAllKeys() {
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext()) {
			KeyBinding keybinding = (KeyBinding) iterator.next();
			keybinding.unpressKey();
		}
	}

	public static void resetKeyBindingArrayAndHash() {
		hash.clearMap();
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext()) {
			KeyBinding keybinding = (KeyBinding) iterator.next();
			hash.addKey(keybinding.keyCode, keybinding);
		}
	}

	public static Set getKeybinds() {
		return keybindSet;
	}

	public KeyBinding(String p_i45001_1_, int p_i45001_2_, String p_i45001_3_) {
		keyDescription = p_i45001_1_;
		keyCode = p_i45001_2_;
		keyCodeDefault = p_i45001_2_;
		keyCategory = p_i45001_3_;
		keybindArray.add(this);
		hash.addKey(p_i45001_2_, this);
		keybindSet.add(p_i45001_3_);
	}

	public boolean getIsKeyPressed() {
		return pressed;
	}

	public String getKeyCategory() {
		return keyCategory;
	}

	public boolean isPressed() {
		if (pressTime == 0)
			return false;
		else {
			--pressTime;
			return true;
		}
	}

	private void unpressKey() {
		pressTime = 0;
		pressed = false;
	}

	public String getKeyDescription() {
		return keyDescription;
	}

	public int getKeyCodeDefault() {
		return keyCodeDefault;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int p_151462_1_) {
		keyCode = p_151462_1_;
	}

	public int compareTo(KeyBinding p_compareTo_1_) {
		int i = I18n.format(keyCategory)
				.compareTo(I18n.format(p_compareTo_1_.keyCategory));

		if (i == 0) {
			i = I18n.format(keyDescription)
					.compareTo(I18n.format(p_compareTo_1_.keyDescription));
		}

		return i;
	}

	@Override
	public int compareTo(Object p_compareTo_1_) {
		return this.compareTo((KeyBinding) p_compareTo_1_);
	}
}