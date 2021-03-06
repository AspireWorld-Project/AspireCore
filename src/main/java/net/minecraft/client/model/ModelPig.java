package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPig extends ModelQuadruped {
	public ModelPig() {
		this(0.0F);
	}

	public ModelPig(float p_i1151_1_) {
		super(6, p_i1151_1_);
		head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, p_i1151_1_);
		field_78145_g = 4.0F;
	}
}