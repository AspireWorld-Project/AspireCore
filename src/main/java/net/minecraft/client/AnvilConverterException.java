package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnvilConverterException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2984619676458995585L;

	public AnvilConverterException(String p_i2160_1_) {
		super(p_i2160_1_);
	}
}