package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDragon extends ModelBase {
	private final ModelRenderer head;
	private final ModelRenderer spine;
	private final ModelRenderer jaw;
	private final ModelRenderer body;
	private final ModelRenderer rearLeg;
	private final ModelRenderer frontLeg;
	private final ModelRenderer rearLegTip;
	private final ModelRenderer frontLegTip;
	private final ModelRenderer rearFoot;
	private final ModelRenderer frontFoot;
	private final ModelRenderer wing;
	private final ModelRenderer wingTip;
	private float partialTicks;
	public ModelDragon(float p_i1169_1_) {
		textureWidth = 256;
		textureHeight = 256;
		setTextureOffset("body.body", 0, 0);
		setTextureOffset("wing.skin", -56, 88);
		setTextureOffset("wingtip.skin", -56, 144);
		setTextureOffset("rearleg.main", 0, 0);
		setTextureOffset("rearfoot.main", 112, 0);
		setTextureOffset("rearlegtip.main", 196, 0);
		setTextureOffset("head.upperhead", 112, 30);
		setTextureOffset("wing.bone", 112, 88);
		setTextureOffset("head.upperlip", 176, 44);
		setTextureOffset("jaw.jaw", 176, 65);
		setTextureOffset("frontleg.main", 112, 104);
		setTextureOffset("wingtip.bone", 112, 136);
		setTextureOffset("frontfoot.main", 144, 104);
		setTextureOffset("neck.box", 192, 104);
		setTextureOffset("frontlegtip.main", 226, 138);
		setTextureOffset("body.scale", 220, 53);
		setTextureOffset("head.scale", 0, 0);
		setTextureOffset("neck.scale", 48, 0);
		setTextureOffset("head.nostril", 112, 0);
		float f1 = -16.0F;
		head = new ModelRenderer(this, "head");
		head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f1, 12, 5, 16);
		head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f1, 16, 16, 16);
		head.mirror = true;
		head.addBox("scale", -5.0F, -12.0F, 12.0F + f1, 2, 4, 6);
		head.addBox("nostril", -5.0F, -3.0F, -6.0F + f1, 2, 2, 4);
		head.mirror = false;
		head.addBox("scale", 3.0F, -12.0F, 12.0F + f1, 2, 4, 6);
		head.addBox("nostril", 3.0F, -3.0F, -6.0F + f1, 2, 2, 4);
		jaw = new ModelRenderer(this, "jaw");
		jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f1);
		jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
		head.addChild(jaw);
		spine = new ModelRenderer(this, "neck");
		spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
		spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
		body = new ModelRenderer(this, "body");
		body.setRotationPoint(0.0F, 4.0F, 8.0F);
		body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
		body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
		body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
		body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
		wing = new ModelRenderer(this, "wing");
		wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
		wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		wingTip = new ModelRenderer(this, "wingtip");
		wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
		wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
		wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		wing.addChild(wingTip);
		frontLeg = new ModelRenderer(this, "frontleg");
		frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
		frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
		frontLegTip = new ModelRenderer(this, "frontlegtip");
		frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
		frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
		frontLeg.addChild(frontLegTip);
		frontFoot = new ModelRenderer(this, "frontfoot");
		frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
		frontLegTip.addChild(frontFoot);
		rearLeg = new ModelRenderer(this, "rearleg");
		rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
		rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
		rearLegTip = new ModelRenderer(this, "rearlegtip");
		rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
		rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
		rearLeg.addChild(rearLegTip);
		rearFoot = new ModelRenderer(this, "rearfoot");
		rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
		rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
		rearLegTip.addChild(rearFoot);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		partialTicks = p_78086_4_;
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		GL11.glPushMatrix();
		EntityDragon entitydragon = (EntityDragon) p_78088_1_;
		float f6 = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * partialTicks;
		jaw.rotateAngleX = (float) (Math.sin(f6 * (float) Math.PI * 2.0F) + 1.0D) * 0.2F;
		float f7 = (float) (Math.sin(f6 * (float) Math.PI * 2.0F - 1.0F) + 1.0D);
		f7 = (f7 * f7 * 1.0F + f7 * 2.0F) * 0.05F;
		GL11.glTranslatef(0.0F, f7 - 2.0F, -3.0F);
		GL11.glRotatef(f7 * 2.0F, 1.0F, 0.0F, 0.0F);
		float f8 = -30.0F;
		float f10 = 0.0F;
		float f11 = 1.5F;
		double[] adouble = entitydragon.getMovementOffsets(6, partialTicks);
		float f12 = updateRotations(entitydragon.getMovementOffsets(5, partialTicks)[0]
				- entitydragon.getMovementOffsets(10, partialTicks)[0]);
		float f13 = updateRotations(entitydragon.getMovementOffsets(5, partialTicks)[0] + f12 / 2.0F);
		f8 += 2.0F;
		float f14 = f6 * (float) Math.PI * 2.0F;
		f8 = 20.0F;
		float f9 = -12.0F;
		float f15;

		for (int i = 0; i < 5; ++i) {
			double[] adouble1 = entitydragon.getMovementOffsets(5 - i, partialTicks);
			f15 = (float) Math.cos(i * 0.45F + f14) * 0.15F;
			spine.rotateAngleY = updateRotations(adouble1[0] - adouble[0]) * (float) Math.PI / 180.0F * f11;
			spine.rotateAngleX = f15 + (float) (adouble1[1] - adouble[1]) * (float) Math.PI / 180.0F * f11 * 5.0F;
			spine.rotateAngleZ = -updateRotations(adouble1[0] - f13) * (float) Math.PI / 180.0F * f11;
			spine.rotationPointY = f8;
			spine.rotationPointZ = f9;
			spine.rotationPointX = f10;
			f8 = (float) (f8 + Math.sin(spine.rotateAngleX) * 10.0D);
			f9 = (float) (f9 - Math.cos(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
			f10 = (float) (f10 - Math.sin(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
			spine.render(p_78088_7_);
		}

		head.rotationPointY = f8;
		head.rotationPointZ = f9;
		head.rotationPointX = f10;
		double[] adouble2 = entitydragon.getMovementOffsets(0, partialTicks);
		head.rotateAngleY = updateRotations(adouble2[0] - adouble[0]) * (float) Math.PI / 180.0F * 1.0F;
		head.rotateAngleZ = -updateRotations(adouble2[0] - f13) * (float) Math.PI / 180.0F * 1.0F;
		head.render(p_78088_7_);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f12 * f11 * 1.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(0.0F, -1.0F, 0.0F);
		body.rotateAngleZ = 0.0F;
		body.render(p_78088_7_);

		for (int j = 0; j < 2; ++j) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			f15 = f6 * (float) Math.PI * 2.0F;
			wing.rotateAngleX = 0.125F - (float) Math.cos(f15) * 0.2F;
			wing.rotateAngleY = 0.25F;
			wing.rotateAngleZ = (float) (Math.sin(f15) + 0.125D) * 0.8F;
			wingTip.rotateAngleZ = -((float) (Math.sin(f15 + 2.0F) + 0.5D)) * 0.75F;
			rearLeg.rotateAngleX = 1.0F + f7 * 0.1F;
			rearLegTip.rotateAngleX = 0.5F + f7 * 0.1F;
			rearFoot.rotateAngleX = 0.75F + f7 * 0.1F;
			frontLeg.rotateAngleX = 1.3F + f7 * 0.1F;
			frontLegTip.rotateAngleX = -0.5F - f7 * 0.1F;
			frontFoot.rotateAngleX = 0.75F + f7 * 0.1F;
			wing.render(p_78088_7_);
			frontLeg.render(p_78088_7_);
			rearLeg.render(p_78088_7_);
			GL11.glScalef(-1.0F, 1.0F, 1.0F);

			if (j == 0) {
				GL11.glCullFace(GL11.GL_FRONT);
			}
		}

		GL11.glPopMatrix();
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f16 = -((float) Math.sin(f6 * (float) Math.PI * 2.0F)) * 0.0F;
		f14 = f6 * (float) Math.PI * 2.0F;
		f8 = 10.0F;
		f9 = 60.0F;
		f10 = 0.0F;
		adouble = entitydragon.getMovementOffsets(11, partialTicks);

		for (int k = 0; k < 12; ++k) {
			adouble2 = entitydragon.getMovementOffsets(12 + k, partialTicks);
			f16 = (float) (f16 + Math.sin(k * 0.45F + f14) * 0.05000000074505806D);
			spine.rotateAngleY = (updateRotations(adouble2[0] - adouble[0]) * f11 + 180.0F) * (float) Math.PI / 180.0F;
			spine.rotateAngleX = f16 + (float) (adouble2[1] - adouble[1]) * (float) Math.PI / 180.0F * f11 * 5.0F;
			spine.rotateAngleZ = updateRotations(adouble2[0] - f13) * (float) Math.PI / 180.0F * f11;
			spine.rotationPointY = f8;
			spine.rotationPointZ = f9;
			spine.rotationPointX = f10;
			f8 = (float) (f8 + Math.sin(spine.rotateAngleX) * 10.0D);
			f9 = (float) (f9 - Math.cos(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
			f10 = (float) (f10 - Math.sin(spine.rotateAngleY) * Math.cos(spine.rotateAngleX) * 10.0D);
			spine.render(p_78088_7_);
		}

		GL11.glPopMatrix();
	}

	private float updateRotations(double p_78214_1_) {
		while (p_78214_1_ >= 180.0D) {
			p_78214_1_ -= 360.0D;
		}

		while (p_78214_1_ < -180.0D) {
			p_78214_1_ += 360.0D;
		}

		return (float) p_78214_1_;
	}
}