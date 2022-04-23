package net.minecraft.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class MovingSoundMinecart extends MovingSound {
	private final EntityMinecart field_147670_k;
	private float field_147669_l = 0.0F;
	public MovingSoundMinecart(EntityMinecart p_i45105_1_) {
		super(new ResourceLocation("minecraft:minecart.base"));
		field_147670_k = p_i45105_1_;
		repeat = true;
		field_147665_h = 0;
	}

	@Override
	public void update() {
		if (field_147670_k.isDead) {
			donePlaying = true;
		} else {
			xPosF = (float) field_147670_k.posX;
			yPosF = (float) field_147670_k.posY;
			zPosF = (float) field_147670_k.posZ;
			float f = MathHelper.sqrt_double(
					field_147670_k.motionX * field_147670_k.motionX + field_147670_k.motionZ * field_147670_k.motionZ);

			if (f >= 0.01D) {
				field_147669_l = MathHelper.clamp_float(field_147669_l + 0.0025F, 0.0F, 1.0F);
				volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 0.5F) * 0.7F;
			} else {
				field_147669_l = 0.0F;
				volume = 0.0F;
			}
		}
	}
}