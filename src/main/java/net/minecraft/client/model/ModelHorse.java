package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelHorse extends ModelBase {
	private ModelRenderer head;
	private ModelRenderer mouthTop;
	private ModelRenderer mouthBottom;
	private ModelRenderer horseLeftEar;
	private ModelRenderer horseRightEar;
	private ModelRenderer muleLeftEar;
	private ModelRenderer muleRightEar;
	private ModelRenderer neck;
	private ModelRenderer horseFaceRopes;
	private ModelRenderer mane;
	private ModelRenderer body;
	private ModelRenderer tailBase;
	private ModelRenderer tailMiddle;
	private ModelRenderer tailTip;
	private ModelRenderer backLeftLeg;
	private ModelRenderer backLeftShin;
	private ModelRenderer backLeftHoof;
	private ModelRenderer backRightLeg;
	private ModelRenderer backRightShin;
	private ModelRenderer backRightHoof;
	private ModelRenderer frontLeftLeg;
	private ModelRenderer frontLeftShin;
	private ModelRenderer frontLeftHoof;
	private ModelRenderer frontRightLeg;
	private ModelRenderer frontRightShin;
	private ModelRenderer frontRightHoof;
	private ModelRenderer muleLeftChest;
	private ModelRenderer muleRightChest;
	private ModelRenderer horseSaddleBottom;
	private ModelRenderer horseSaddleFront;
	private ModelRenderer horseSaddleBack;
	private ModelRenderer horseLeftSaddleRope;
	private ModelRenderer horseLeftSaddleMetal;
	private ModelRenderer horseRightSaddleRope;
	private ModelRenderer horseRightSaddleMetal;
	private ModelRenderer horseLeftFaceMetal;
	private ModelRenderer horseRightFaceMetal;
	private ModelRenderer horseLeftRein;
	private ModelRenderer horseRightRein;
	private static final String __OBFID = "CL_00000846";

	public ModelHorse() {
		textureWidth = 128;
		textureHeight = 128;
		body = new ModelRenderer(this, 0, 34);
		body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
		body.setRotationPoint(0.0F, 11.0F, 9.0F);
		tailBase = new ModelRenderer(this, 44, 0);
		tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
		tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailBase, -1.134464F, 0.0F, 0.0F);
		tailMiddle = new ModelRenderer(this, 38, 7);
		tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
		tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailMiddle, -1.134464F, 0.0F, 0.0F);
		tailTip = new ModelRenderer(this, 24, 3);
		tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
		tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
		setBoxRotation(tailTip, -1.40215F, 0.0F, 0.0F);
		backLeftLeg = new ModelRenderer(this, 78, 29);
		backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
		backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
		backLeftShin = new ModelRenderer(this, 78, 43);
		backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
		backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
		backLeftHoof = new ModelRenderer(this, 78, 51);
		backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
		backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
		backRightLeg = new ModelRenderer(this, 96, 29);
		backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
		backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
		backRightShin = new ModelRenderer(this, 96, 43);
		backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
		backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
		backRightHoof = new ModelRenderer(this, 96, 51);
		backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
		backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
		frontLeftLeg = new ModelRenderer(this, 44, 29);
		frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
		frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
		frontLeftShin = new ModelRenderer(this, 44, 41);
		frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
		frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
		frontLeftHoof = new ModelRenderer(this, 44, 51);
		frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
		frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
		frontRightLeg = new ModelRenderer(this, 60, 29);
		frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
		frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
		frontRightShin = new ModelRenderer(this, 60, 41);
		frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
		frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
		frontRightHoof = new ModelRenderer(this, 60, 51);
		frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
		frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
		head.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(head, 0.5235988F, 0.0F, 0.0F);
		mouthTop = new ModelRenderer(this, 24, 18);
		mouthTop.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
		mouthTop.setRotationPoint(0.0F, 3.95F, -10.0F);
		setBoxRotation(mouthTop, 0.5235988F, 0.0F, 0.0F);
		mouthBottom = new ModelRenderer(this, 24, 27);
		mouthBottom.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
		mouthBottom.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(mouthBottom, 0.5235988F, 0.0F, 0.0F);
		head.addChild(mouthTop);
		head.addChild(mouthBottom);
		horseLeftEar = new ModelRenderer(this, 0, 0);
		horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
		horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseLeftEar, 0.5235988F, 0.0F, 0.0F);
		horseRightEar = new ModelRenderer(this, 0, 0);
		horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
		horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseRightEar, 0.5235988F, 0.0F, 0.0F);
		muleLeftEar = new ModelRenderer(this, 0, 12);
		muleLeftEar.addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1);
		muleLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(muleLeftEar, 0.5235988F, 0.0F, 0.2617994F);
		muleRightEar = new ModelRenderer(this, 0, 12);
		muleRightEar.addBox(0.0F, -16.0F, 4.0F, 2, 7, 1);
		muleRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(muleRightEar, 0.5235988F, 0.0F, -0.2617994F);
		neck = new ModelRenderer(this, 0, 12);
		neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
		neck.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(neck, 0.5235988F, 0.0F, 0.0F);
		muleLeftChest = new ModelRenderer(this, 0, 34);
		muleLeftChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
		muleLeftChest.setRotationPoint(-7.5F, 3.0F, 10.0F);
		setBoxRotation(muleLeftChest, 0.0F, (float) Math.PI / 2F, 0.0F);
		muleRightChest = new ModelRenderer(this, 0, 47);
		muleRightChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
		muleRightChest.setRotationPoint(4.5F, 3.0F, 10.0F);
		setBoxRotation(muleRightChest, 0.0F, (float) Math.PI / 2F, 0.0F);
		horseSaddleBottom = new ModelRenderer(this, 80, 0);
		horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
		horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseSaddleFront = new ModelRenderer(this, 106, 9);
		horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
		horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseSaddleBack = new ModelRenderer(this, 80, 9);
		horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
		horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
		horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
		horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
		horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
		horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
		horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
		horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
		horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
		horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
		horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
		horseRightSaddleRope = new ModelRenderer(this, 80, 0);
		horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
		horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
		horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
		horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
		horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseLeftFaceMetal, 0.5235988F, 0.0F, 0.0F);
		horseRightFaceMetal = new ModelRenderer(this, 74, 13);
		horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
		horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseRightFaceMetal, 0.5235988F, 0.0F, 0.0F);
		horseLeftRein = new ModelRenderer(this, 44, 10);
		horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
		horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
		horseRightRein = new ModelRenderer(this, 44, 5);
		horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
		horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
		mane = new ModelRenderer(this, 58, 0);
		mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
		mane.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(mane, 0.5235988F, 0.0F, 0.0F);
		horseFaceRopes = new ModelRenderer(this, 80, 12);
		horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
		horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
		setBoxRotation(horseFaceRopes, 0.5235988F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		EntityHorse entityhorse = (EntityHorse) p_78088_1_;
		int i = entityhorse.getHorseType();
		float f6 = entityhorse.getGrassEatingAmount(0.0F);
		boolean flag = entityhorse.isAdultHorse();
		boolean flag1 = flag && entityhorse.isHorseSaddled();
		boolean flag2 = flag && entityhorse.isChested();
		boolean flag3 = i == 1 || i == 2;
		float f7 = entityhorse.getHorseSize();
		boolean flag4 = entityhorse.riddenByEntity != null;

		if (flag1) {
			horseFaceRopes.render(p_78088_7_);
			horseSaddleBottom.render(p_78088_7_);
			horseSaddleFront.render(p_78088_7_);
			horseSaddleBack.render(p_78088_7_);
			horseLeftSaddleRope.render(p_78088_7_);
			horseLeftSaddleMetal.render(p_78088_7_);
			horseRightSaddleRope.render(p_78088_7_);
			horseRightSaddleMetal.render(p_78088_7_);
			horseLeftFaceMetal.render(p_78088_7_);
			horseRightFaceMetal.render(p_78088_7_);

			if (flag4) {
				horseLeftRein.render(p_78088_7_);
				horseRightRein.render(p_78088_7_);
			}
		}

		if (!flag) {
			GL11.glPushMatrix();
			GL11.glScalef(f7, 0.5F + f7 * 0.5F, f7);
			GL11.glTranslatef(0.0F, 0.95F * (1.0F - f7), 0.0F);
		}

		backLeftLeg.render(p_78088_7_);
		backLeftShin.render(p_78088_7_);
		backLeftHoof.render(p_78088_7_);
		backRightLeg.render(p_78088_7_);
		backRightShin.render(p_78088_7_);
		backRightHoof.render(p_78088_7_);
		frontLeftLeg.render(p_78088_7_);
		frontLeftShin.render(p_78088_7_);
		frontLeftHoof.render(p_78088_7_);
		frontRightLeg.render(p_78088_7_);
		frontRightShin.render(p_78088_7_);
		frontRightHoof.render(p_78088_7_);

		if (!flag) {
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(f7, f7, f7);
			GL11.glTranslatef(0.0F, 1.35F * (1.0F - f7), 0.0F);
		}

		body.render(p_78088_7_);
		tailBase.render(p_78088_7_);
		tailMiddle.render(p_78088_7_);
		tailTip.render(p_78088_7_);
		neck.render(p_78088_7_);
		mane.render(p_78088_7_);

		if (!flag) {
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			float f8 = 0.5F + f7 * f7 * 0.5F;
			GL11.glScalef(f8, f8, f8);

			if (f6 <= 0.0F) {
				GL11.glTranslatef(0.0F, 1.35F * (1.0F - f7), 0.0F);
			} else {
				GL11.glTranslatef(0.0F, 0.9F * (1.0F - f7) * f6 + 1.35F * (1.0F - f7) * (1.0F - f6),
						0.15F * (1.0F - f7) * f6);
			}
		}

		if (flag3) {
			muleLeftEar.render(p_78088_7_);
			muleRightEar.render(p_78088_7_);
		} else {
			horseLeftEar.render(p_78088_7_);
			horseRightEar.render(p_78088_7_);
		}

		head.render(p_78088_7_);

		if (!flag) {
			GL11.glPopMatrix();
		}

		if (flag2) {
			muleLeftChest.render(p_78088_7_);
			muleRightChest.render(p_78088_7_);
		}
	}

	private void setBoxRotation(ModelRenderer p_110682_1_, float p_110682_2_, float p_110682_3_, float p_110682_4_) {
		p_110682_1_.rotateAngleX = p_110682_2_;
		p_110682_1_.rotateAngleY = p_110682_3_;
		p_110682_1_.rotateAngleZ = p_110682_4_;
	}

	private float updateHorseRotation(float p_110683_1_, float p_110683_2_, float p_110683_3_) {
		float f3;

		for (f3 = p_110683_2_ - p_110683_1_; f3 < -180.0F; f3 += 360.0F) {
			;
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return p_110683_1_ + p_110683_3_ * f3;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
		float f3 = updateHorseRotation(p_78086_1_.prevRenderYawOffset, p_78086_1_.renderYawOffset, p_78086_4_);
		float f4 = updateHorseRotation(p_78086_1_.prevRotationYawHead, p_78086_1_.rotationYawHead, p_78086_4_);
		float f5 = p_78086_1_.prevRotationPitch
				+ (p_78086_1_.rotationPitch - p_78086_1_.prevRotationPitch) * p_78086_4_;
		float f6 = f4 - f3;
		float f7 = f5 / (180F / (float) Math.PI);

		if (f6 > 20.0F) {
			f6 = 20.0F;
		}

		if (f6 < -20.0F) {
			f6 = -20.0F;
		}

		if (p_78086_3_ > 0.2F) {
			f7 += MathHelper.cos(p_78086_2_ * 0.4F) * 0.15F * p_78086_3_;
		}

		EntityHorse entityhorse = (EntityHorse) p_78086_1_;
		float f8 = entityhorse.getGrassEatingAmount(p_78086_4_);
		float f9 = entityhorse.getRearingAmount(p_78086_4_);
		float f10 = 1.0F - f9;
		float f11 = entityhorse.func_110201_q(p_78086_4_);
		boolean flag = entityhorse.field_110278_bp != 0;
		boolean flag1 = entityhorse.isHorseSaddled();
		boolean flag2 = entityhorse.riddenByEntity != null;
		float f12 = p_78086_1_.ticksExisted + p_78086_4_;
		float f13 = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI);
		float f14 = f13 * 0.8F * p_78086_3_;
		head.rotationPointY = 4.0F;
		head.rotationPointZ = -10.0F;
		tailBase.rotationPointY = 3.0F;
		tailMiddle.rotationPointZ = 14.0F;
		muleRightChest.rotationPointY = 3.0F;
		muleRightChest.rotationPointZ = 10.0F;
		body.rotateAngleX = 0.0F;
		head.rotateAngleX = 0.5235988F + f7;
		head.rotateAngleY = f6 / (180F / (float) Math.PI);
		head.rotateAngleX = f9 * (0.2617994F + f7) + f8 * 2.18166F + (1.0F - Math.max(f9, f8)) * head.rotateAngleX;
		head.rotateAngleY = f9 * (f6 / (180F / (float) Math.PI)) + (1.0F - Math.max(f9, f8)) * head.rotateAngleY;
		head.rotationPointY = f9 * -6.0F + f8 * 11.0F + (1.0F - Math.max(f9, f8)) * head.rotationPointY;
		head.rotationPointZ = f9 * -1.0F + f8 * -10.0F + (1.0F - Math.max(f9, f8)) * head.rotationPointZ;
		tailBase.rotationPointY = f9 * 9.0F + f10 * tailBase.rotationPointY;
		tailMiddle.rotationPointZ = f9 * 18.0F + f10 * tailMiddle.rotationPointZ;
		muleRightChest.rotationPointY = f9 * 5.5F + f10 * muleRightChest.rotationPointY;
		muleRightChest.rotationPointZ = f9 * 15.0F + f10 * muleRightChest.rotationPointZ;
		body.rotateAngleX = f9 * -((float) Math.PI / 4F) + f10 * body.rotateAngleX;
		horseLeftEar.rotationPointY = head.rotationPointY;
		horseRightEar.rotationPointY = head.rotationPointY;
		muleLeftEar.rotationPointY = head.rotationPointY;
		muleRightEar.rotationPointY = head.rotationPointY;
		neck.rotationPointY = head.rotationPointY;
		mouthTop.rotationPointY = 0.02F;
		mouthBottom.rotationPointY = 0.0F;
		mane.rotationPointY = head.rotationPointY;
		horseLeftEar.rotationPointZ = head.rotationPointZ;
		horseRightEar.rotationPointZ = head.rotationPointZ;
		muleLeftEar.rotationPointZ = head.rotationPointZ;
		muleRightEar.rotationPointZ = head.rotationPointZ;
		neck.rotationPointZ = head.rotationPointZ;
		mouthTop.rotationPointZ = 0.02F - f11 * 1.0F;
		mouthBottom.rotationPointZ = 0.0F + f11 * 1.0F;
		mane.rotationPointZ = head.rotationPointZ;
		horseLeftEar.rotateAngleX = head.rotateAngleX;
		horseRightEar.rotateAngleX = head.rotateAngleX;
		muleLeftEar.rotateAngleX = head.rotateAngleX;
		muleRightEar.rotateAngleX = head.rotateAngleX;
		neck.rotateAngleX = head.rotateAngleX;
		mouthTop.rotateAngleX = 0.0F - 0.09424778F * f11;
		mouthBottom.rotateAngleX = 0.0F + 0.15707964F * f11;
		mane.rotateAngleX = head.rotateAngleX;
		horseLeftEar.rotateAngleY = head.rotateAngleY;
		horseRightEar.rotateAngleY = head.rotateAngleY;
		muleLeftEar.rotateAngleY = head.rotateAngleY;
		muleRightEar.rotateAngleY = head.rotateAngleY;
		neck.rotateAngleY = head.rotateAngleY;
		mouthTop.rotateAngleY = 0.0F;
		mouthBottom.rotateAngleY = 0.0F;
		mane.rotateAngleY = head.rotateAngleY;
		muleLeftChest.rotateAngleX = f14 / 5.0F;
		muleRightChest.rotateAngleX = -f14 / 5.0F;
		float f15 = (float) Math.PI / 2F;
		float f18 = 0.2617994F * f9;
		float f19 = MathHelper.cos(f12 * 0.6F + (float) Math.PI);
		frontLeftLeg.rotationPointY = -2.0F * f9 + 9.0F * f10;
		frontLeftLeg.rotationPointZ = -2.0F * f9 + -8.0F * f10;
		frontRightLeg.rotationPointY = frontLeftLeg.rotationPointY;
		frontRightLeg.rotationPointZ = frontLeftLeg.rotationPointZ;
		backLeftShin.rotationPointY = backLeftLeg.rotationPointY
				+ MathHelper.sin((float) Math.PI / 2F + f18 + f10 * -f13 * 0.5F * p_78086_3_) * 7.0F;
		backLeftShin.rotationPointZ = backLeftLeg.rotationPointZ
				+ MathHelper.cos((float) Math.PI * 3F / 2F + f18 + f10 * -f13 * 0.5F * p_78086_3_) * 7.0F;
		backRightShin.rotationPointY = backRightLeg.rotationPointY
				+ MathHelper.sin((float) Math.PI / 2F + f18 + f10 * f13 * 0.5F * p_78086_3_) * 7.0F;
		backRightShin.rotationPointZ = backRightLeg.rotationPointZ
				+ MathHelper.cos((float) Math.PI * 3F / 2F + f18 + f10 * f13 * 0.5F * p_78086_3_) * 7.0F;
		float f20 = (-1.0471976F + f19) * f9 + f14 * f10;
		float f21 = (-1.0471976F + -f19) * f9 + -f14 * f10;
		frontLeftShin.rotationPointY = frontLeftLeg.rotationPointY + MathHelper.sin((float) Math.PI / 2F + f20) * 7.0F;
		frontLeftShin.rotationPointZ = frontLeftLeg.rotationPointZ
				+ MathHelper.cos((float) Math.PI * 3F / 2F + f20) * 7.0F;
		frontRightShin.rotationPointY = frontRightLeg.rotationPointY
				+ MathHelper.sin((float) Math.PI / 2F + f21) * 7.0F;
		frontRightShin.rotationPointZ = frontRightLeg.rotationPointZ
				+ MathHelper.cos((float) Math.PI * 3F / 2F + f21) * 7.0F;
		backLeftLeg.rotateAngleX = f18 + -f13 * 0.5F * p_78086_3_ * f10;
		backLeftShin.rotateAngleX = -0.08726646F * f9
				+ (-f13 * 0.5F * p_78086_3_ - Math.max(0.0F, f13 * 0.5F * p_78086_3_)) * f10;
		backLeftHoof.rotateAngleX = backLeftShin.rotateAngleX;
		backRightLeg.rotateAngleX = f18 + f13 * 0.5F * p_78086_3_ * f10;
		backRightShin.rotateAngleX = -0.08726646F * f9
				+ (f13 * 0.5F * p_78086_3_ - Math.max(0.0F, -f13 * 0.5F * p_78086_3_)) * f10;
		backRightHoof.rotateAngleX = backRightShin.rotateAngleX;
		frontLeftLeg.rotateAngleX = f20;
		frontLeftShin.rotateAngleX = (frontLeftLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F + f19 * 0.2F))
				* f9 + (f14 + Math.max(0.0F, f13 * 0.5F * p_78086_3_)) * f10;
		frontLeftHoof.rotateAngleX = frontLeftShin.rotateAngleX;
		frontRightLeg.rotateAngleX = f21;
		frontRightShin.rotateAngleX = (frontRightLeg.rotateAngleX + (float) Math.PI * Math.max(0.0F, 0.2F - f19 * 0.2F))
				* f9 + (-f14 + Math.max(0.0F, -f13 * 0.5F * p_78086_3_)) * f10;
		frontRightHoof.rotateAngleX = frontRightShin.rotateAngleX;
		backLeftHoof.rotationPointY = backLeftShin.rotationPointY;
		backLeftHoof.rotationPointZ = backLeftShin.rotationPointZ;
		backRightHoof.rotationPointY = backRightShin.rotationPointY;
		backRightHoof.rotationPointZ = backRightShin.rotationPointZ;
		frontLeftHoof.rotationPointY = frontLeftShin.rotationPointY;
		frontLeftHoof.rotationPointZ = frontLeftShin.rotationPointZ;
		frontRightHoof.rotationPointY = frontRightShin.rotationPointY;
		frontRightHoof.rotationPointZ = frontRightShin.rotationPointZ;

		if (flag1) {
			horseSaddleBottom.rotationPointY = f9 * 0.5F + f10 * 2.0F;
			horseSaddleBottom.rotationPointZ = f9 * 11.0F + f10 * 2.0F;
			horseSaddleFront.rotationPointY = horseSaddleBottom.rotationPointY;
			horseSaddleBack.rotationPointY = horseSaddleBottom.rotationPointY;
			horseLeftSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY;
			horseRightSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY;
			horseLeftSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY;
			horseRightSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY;
			muleLeftChest.rotationPointY = muleRightChest.rotationPointY;
			horseSaddleFront.rotationPointZ = horseSaddleBottom.rotationPointZ;
			horseSaddleBack.rotationPointZ = horseSaddleBottom.rotationPointZ;
			horseLeftSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ;
			horseRightSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ;
			horseLeftSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ;
			horseRightSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ;
			muleLeftChest.rotationPointZ = muleRightChest.rotationPointZ;
			horseSaddleBottom.rotateAngleX = body.rotateAngleX;
			horseSaddleFront.rotateAngleX = body.rotateAngleX;
			horseSaddleBack.rotateAngleX = body.rotateAngleX;
			horseLeftRein.rotationPointY = head.rotationPointY;
			horseRightRein.rotationPointY = head.rotationPointY;
			horseFaceRopes.rotationPointY = head.rotationPointY;
			horseLeftFaceMetal.rotationPointY = head.rotationPointY;
			horseRightFaceMetal.rotationPointY = head.rotationPointY;
			horseLeftRein.rotationPointZ = head.rotationPointZ;
			horseRightRein.rotationPointZ = head.rotationPointZ;
			horseFaceRopes.rotationPointZ = head.rotationPointZ;
			horseLeftFaceMetal.rotationPointZ = head.rotationPointZ;
			horseRightFaceMetal.rotationPointZ = head.rotationPointZ;
			horseLeftRein.rotateAngleX = f7;
			horseRightRein.rotateAngleX = f7;
			horseFaceRopes.rotateAngleX = head.rotateAngleX;
			horseLeftFaceMetal.rotateAngleX = head.rotateAngleX;
			horseRightFaceMetal.rotateAngleX = head.rotateAngleX;
			horseFaceRopes.rotateAngleY = head.rotateAngleY;
			horseLeftFaceMetal.rotateAngleY = head.rotateAngleY;
			horseLeftRein.rotateAngleY = head.rotateAngleY;
			horseRightFaceMetal.rotateAngleY = head.rotateAngleY;
			horseRightRein.rotateAngleY = head.rotateAngleY;

			if (flag2) {
				horseLeftSaddleRope.rotateAngleX = -1.0471976F;
				horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
				horseRightSaddleRope.rotateAngleX = -1.0471976F;
				horseRightSaddleMetal.rotateAngleX = -1.0471976F;
				horseLeftSaddleRope.rotateAngleZ = 0.0F;
				horseLeftSaddleMetal.rotateAngleZ = 0.0F;
				horseRightSaddleRope.rotateAngleZ = 0.0F;
				horseRightSaddleMetal.rotateAngleZ = 0.0F;
			} else {
				horseLeftSaddleRope.rotateAngleX = f14 / 3.0F;
				horseLeftSaddleMetal.rotateAngleX = f14 / 3.0F;
				horseRightSaddleRope.rotateAngleX = f14 / 3.0F;
				horseRightSaddleMetal.rotateAngleX = f14 / 3.0F;
				horseLeftSaddleRope.rotateAngleZ = f14 / 5.0F;
				horseLeftSaddleMetal.rotateAngleZ = f14 / 5.0F;
				horseRightSaddleRope.rotateAngleZ = -f14 / 5.0F;
				horseRightSaddleMetal.rotateAngleZ = -f14 / 5.0F;
			}
		}

		f15 = -1.3089F + p_78086_3_ * 1.5F;

		if (f15 > 0.0F) {
			f15 = 0.0F;
		}

		if (flag) {
			tailBase.rotateAngleY = MathHelper.cos(f12 * 0.7F);
			f15 = 0.0F;
		} else {
			tailBase.rotateAngleY = 0.0F;
		}

		tailMiddle.rotateAngleY = tailBase.rotateAngleY;
		tailTip.rotateAngleY = tailBase.rotateAngleY;
		tailMiddle.rotationPointY = tailBase.rotationPointY;
		tailTip.rotationPointY = tailBase.rotationPointY;
		tailMiddle.rotationPointZ = tailBase.rotationPointZ;
		tailTip.rotationPointZ = tailBase.rotationPointZ;
		tailBase.rotateAngleX = f15;
		tailMiddle.rotateAngleX = f15;
		tailTip.rotateAngleX = -0.2618F + f15;
	}
}