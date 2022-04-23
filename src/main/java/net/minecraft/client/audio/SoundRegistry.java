package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.RegistrySimple;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class SoundRegistry extends RegistrySimple {
	@SuppressWarnings("rawtypes")
	private Map field_148764_a;
	@SuppressWarnings("rawtypes")
	@Override
	protected Map createUnderlyingMap() {
		field_148764_a = Maps.newHashMap();
		return field_148764_a;
	}

	public void registerSound(SoundEventAccessorComposite p_148762_1_) {
		putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
	}

	public void func_148763_c() {
		field_148764_a.clear();
	}
}