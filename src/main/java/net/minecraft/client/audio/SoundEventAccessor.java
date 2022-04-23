package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundEventAccessor implements ISoundEventAccessor {
	private final SoundPoolEntry field_148739_a;
	private final int field_148738_b;
	SoundEventAccessor(SoundPoolEntry p_i45123_1_, int p_i45123_2_) {
		field_148739_a = p_i45123_1_;
		field_148738_b = p_i45123_2_;
	}

	@Override
	public int func_148721_a() {
		return field_148738_b;
	}

	@Override
	public SoundPoolEntry func_148720_g() {
		return new SoundPoolEntry(field_148739_a);
	}
}