package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class MovingSoundMinecartRiding extends MovingSound {
	private final EntityPlayer field_147672_k;
	private final EntityMinecart field_147671_l;
	private static final String __OBFID = "CL_00001119";

	public MovingSoundMinecartRiding(EntityPlayer p_i45106_1_, EntityMinecart p_i45106_2_) {
		super(new ResourceLocation("minecraft:minecart.inside"));
		field_147672_k = p_i45106_1_;
		field_147671_l = p_i45106_2_;
		field_147666_i = ISound.AttenuationType.NONE;
		repeat = true;
		field_147665_h = 0;
	}

	@Override
	public void update() {
		if (!field_147671_l.isDead && field_147672_k.isRiding() && field_147672_k.ridingEntity == field_147671_l) {
			float f = MathHelper.sqrt_double(
					field_147671_l.motionX * field_147671_l.motionX + field_147671_l.motionZ * field_147671_l.motionZ);

			if (f >= 0.01D) {
				volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.0F) * 0.75F;
			} else {
				volume = 0.0F;
			}
		} else {
			donePlaying = true;
		}
	}
}