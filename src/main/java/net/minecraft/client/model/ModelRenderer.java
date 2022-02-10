package net.minecraft.client.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;

public class ModelRenderer {
	public float textureWidth;
	public float textureHeight;
	private int textureOffsetX;
	private int textureOffsetY;
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled;
	private int displayList;
	public boolean mirror;
	public boolean showModel;
	public boolean isHidden;
	public List cubeList;
	public List childModels;
	public final String boxName;
	private ModelBase baseModel;
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	private static final String __OBFID = "CL_00000874";

	public ModelRenderer(ModelBase p_i1172_1_, String p_i1172_2_) {
		textureWidth = 64.0F;
		textureHeight = 32.0F;
		showModel = true;
		cubeList = new ArrayList();
		baseModel = p_i1172_1_;
		p_i1172_1_.boxList.add(this);
		boxName = p_i1172_2_;
		setTextureSize(p_i1172_1_.textureWidth, p_i1172_1_.textureHeight);
	}

	public ModelRenderer(ModelBase p_i1173_1_) {
		this(p_i1173_1_, (String) null);
	}

	public ModelRenderer(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_) {
		this(p_i1174_1_);
		setTextureOffset(p_i1174_2_, p_i1174_3_);
	}

	public void addChild(ModelRenderer p_78792_1_) {
		if (childModels == null) {
			childModels = new ArrayList();
		}

		childModels.add(p_78792_1_);
	}

	public ModelRenderer setTextureOffset(int p_78784_1_, int p_78784_2_) {
		textureOffsetX = p_78784_1_;
		textureOffsetY = p_78784_2_;
		return this;
	}

	public ModelRenderer addBox(String p_78786_1_, float p_78786_2_, float p_78786_3_, float p_78786_4_, int p_78786_5_,
			int p_78786_6_, int p_78786_7_) {
		p_78786_1_ = boxName + "." + p_78786_1_;
		TextureOffset textureoffset = baseModel.getTextureOffset(p_78786_1_);
		setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
		cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, p_78786_2_, p_78786_3_, p_78786_4_, p_78786_5_,
				p_78786_6_, p_78786_7_, 0.0F).func_78244_a(p_78786_1_));
		return this;
	}

	public ModelRenderer addBox(float p_78789_1_, float p_78789_2_, float p_78789_3_, int p_78789_4_, int p_78789_5_,
			int p_78789_6_) {
		cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_,
				p_78789_5_, p_78789_6_, 0.0F));
		return this;
	}

	public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_,
			int p_78790_6_, float p_78790_7_) {
		cubeList.add(new ModelBox(this, textureOffsetX, textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_,
				p_78790_5_, p_78790_6_, p_78790_7_));
	}

	public void setRotationPoint(float p_78793_1_, float p_78793_2_, float p_78793_3_) {
		rotationPointX = p_78793_1_;
		rotationPointY = p_78793_2_;
		rotationPointZ = p_78793_3_;
	}

	@SideOnly(Side.CLIENT)
	public void render(float p_78785_1_) {
		if (!isHidden) {
			if (showModel) {
				if (!compiled) {
					compileDisplayList(p_78785_1_);
				}

				GL11.glTranslatef(offsetX, offsetY, offsetZ);
				int i;

				if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F) {
					if (rotationPointX == 0.0F && rotationPointY == 0.0F && rotationPointZ == 0.0F) {
						GL11.glCallList(displayList);

						if (childModels != null) {
							for (i = 0; i < childModels.size(); ++i) {
								((ModelRenderer) childModels.get(i)).render(p_78785_1_);
							}
						}
					} else {
						GL11.glTranslatef(rotationPointX * p_78785_1_, rotationPointY * p_78785_1_,
								rotationPointZ * p_78785_1_);
						GL11.glCallList(displayList);

						if (childModels != null) {
							for (i = 0; i < childModels.size(); ++i) {
								((ModelRenderer) childModels.get(i)).render(p_78785_1_);
							}
						}

						GL11.glTranslatef(-rotationPointX * p_78785_1_, -rotationPointY * p_78785_1_,
								-rotationPointZ * p_78785_1_);
					}
				} else {
					GL11.glPushMatrix();
					GL11.glTranslatef(rotationPointX * p_78785_1_, rotationPointY * p_78785_1_,
							rotationPointZ * p_78785_1_);

					if (rotateAngleZ != 0.0F) {
						GL11.glRotatef(rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
					}

					if (rotateAngleY != 0.0F) {
						GL11.glRotatef(rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
					}

					if (rotateAngleX != 0.0F) {
						GL11.glRotatef(rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
					}

					GL11.glCallList(displayList);

					if (childModels != null) {
						for (i = 0; i < childModels.size(); ++i) {
							((ModelRenderer) childModels.get(i)).render(p_78785_1_);
						}
					}

					GL11.glPopMatrix();
				}

				GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderWithRotation(float p_78791_1_) {
		if (!isHidden) {
			if (showModel) {
				if (!compiled) {
					compileDisplayList(p_78791_1_);
				}

				GL11.glPushMatrix();
				GL11.glTranslatef(rotationPointX * p_78791_1_, rotationPointY * p_78791_1_,
						rotationPointZ * p_78791_1_);

				if (rotateAngleY != 0.0F) {
					GL11.glRotatef(rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
				}

				if (rotateAngleX != 0.0F) {
					GL11.glRotatef(rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
				}

				if (rotateAngleZ != 0.0F) {
					GL11.glRotatef(rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
				}

				GL11.glCallList(displayList);
				GL11.glPopMatrix();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void postRender(float p_78794_1_) {
		if (!isHidden) {
			if (showModel) {
				if (!compiled) {
					compileDisplayList(p_78794_1_);
				}

				if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F) {
					if (rotationPointX != 0.0F || rotationPointY != 0.0F || rotationPointZ != 0.0F) {
						GL11.glTranslatef(rotationPointX * p_78794_1_, rotationPointY * p_78794_1_,
								rotationPointZ * p_78794_1_);
					}
				} else {
					GL11.glTranslatef(rotationPointX * p_78794_1_, rotationPointY * p_78794_1_,
							rotationPointZ * p_78794_1_);

					if (rotateAngleZ != 0.0F) {
						GL11.glRotatef(rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
					}

					if (rotateAngleY != 0.0F) {
						GL11.glRotatef(rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
					}

					if (rotateAngleX != 0.0F) {
						GL11.glRotatef(rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void compileDisplayList(float p_78788_1_) {
		displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);
		Tessellator tessellator = Tessellator.instance;

		for (int i = 0; i < cubeList.size(); ++i) {
			((ModelBox) cubeList.get(i)).render(tessellator, p_78788_1_);
		}

		GL11.glEndList();
		compiled = true;
	}

	public ModelRenderer setTextureSize(int p_78787_1_, int p_78787_2_) {
		textureWidth = p_78787_1_;
		textureHeight = p_78787_2_;
		return this;
	}
}