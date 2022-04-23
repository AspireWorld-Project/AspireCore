package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class MovingSound extends PositionedSound implements ITickableSound {
	protected boolean donePlaying = false;
	protected MovingSound(ResourceLocation p_i45104_1_) {
		super(p_i45104_1_);
	}

	@Override
	public boolean isDonePlaying() {
		return donePlaying;
	}
}