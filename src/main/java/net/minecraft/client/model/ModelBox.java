package net.minecraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

public class ModelBox {
	private final PositionTextureVertex[] vertexPositions;
	private final TexturedQuad[] quadList;
	public final float posX1;
	public final float posY1;
	public final float posZ1;
	public final float posX2;
	public final float posY2;
	public final float posZ2;
	public String field_78247_g;
	public ModelBox(ModelRenderer p_i1171_1_, int p_i1171_2_, int p_i1171_3_, float p_i1171_4_, float p_i1171_5_,
			float p_i1171_6_, int p_i1171_7_, int p_i1171_8_, int p_i1171_9_, float p_i1171_10_) {
		posX1 = p_i1171_4_;
		posY1 = p_i1171_5_;
		posZ1 = p_i1171_6_;
		posX2 = p_i1171_4_ + p_i1171_7_;
		posY2 = p_i1171_5_ + p_i1171_8_;
		posZ2 = p_i1171_6_ + p_i1171_9_;
		vertexPositions = new PositionTextureVertex[8];
		quadList = new TexturedQuad[6];
		float f4 = p_i1171_4_ + p_i1171_7_;
		float f5 = p_i1171_5_ + p_i1171_8_;
		float f6 = p_i1171_6_ + p_i1171_9_;
		p_i1171_4_ -= p_i1171_10_;
		p_i1171_5_ -= p_i1171_10_;
		p_i1171_6_ -= p_i1171_10_;
		f4 += p_i1171_10_;
		f5 += p_i1171_10_;
		f6 += p_i1171_10_;

		if (p_i1171_1_.mirror) {
			float f7 = f4;
			f4 = p_i1171_4_;
			p_i1171_4_ = f7;
		}

		PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i1171_4_, p_i1171_5_, p_i1171_6_,
				0.0F, 0.0F);
		PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f4, p_i1171_5_, p_i1171_6_, 0.0F, 8.0F);
		PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, f5, p_i1171_6_, 8.0F, 8.0F);
		PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i1171_4_, f5, p_i1171_6_, 8.0F,
				0.0F);
		PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i1171_4_, p_i1171_5_, f6, 0.0F,
				0.0F);
		PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f4, p_i1171_5_, f6, 0.0F, 8.0F);
		PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, f5, f6, 8.0F, 8.0F);
		PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i1171_4_, f5, f6, 8.0F, 0.0F);
		vertexPositions[0] = positiontexturevertex7;
		vertexPositions[1] = positiontexturevertex;
		vertexPositions[2] = positiontexturevertex1;
		vertexPositions[3] = positiontexturevertex2;
		vertexPositions[4] = positiontexturevertex3;
		vertexPositions[5] = positiontexturevertex4;
		vertexPositions[6] = positiontexturevertex5;
		vertexPositions[7] = positiontexturevertex6;
		quadList[0] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex, positiontexturevertex1,
						positiontexturevertex5 },
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_,
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[1] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3, positiontexturevertex6,
						positiontexturevertex2 },
				p_i1171_2_, p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[2] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3, positiontexturevertex7,
						positiontexturevertex },
				p_i1171_2_ + p_i1171_9_, p_i1171_3_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[3] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2, positiontexturevertex6,
						positiontexturevertex5 },
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_,
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_7_, p_i1171_3_, p_i1171_1_.textureWidth,
				p_i1171_1_.textureHeight);
		quadList[4] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7, positiontexturevertex2,
						positiontexturevertex1 },
				p_i1171_2_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_, p_i1171_2_ + p_i1171_9_ + p_i1171_7_,
				p_i1171_3_ + p_i1171_9_ + p_i1171_8_, p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);
		quadList[5] = new TexturedQuad(
				new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4, positiontexturevertex5,
						positiontexturevertex6 },
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_, p_i1171_3_ + p_i1171_9_,
				p_i1171_2_ + p_i1171_9_ + p_i1171_7_ + p_i1171_9_ + p_i1171_7_, p_i1171_3_ + p_i1171_9_ + p_i1171_8_,
				p_i1171_1_.textureWidth, p_i1171_1_.textureHeight);

		if (p_i1171_1_.mirror) {
			for (int j1 = 0; j1 < quadList.length; ++j1) {
				quadList[j1].flipFace();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void render(Tessellator p_78245_1_, float p_78245_2_) {
		for (int i = 0; i < quadList.length; ++i) {
			quadList[i].draw(p_78245_1_, p_78245_2_);
		}
	}

	public ModelBox func_78244_a(String p_78244_1_) {
		field_78247_g = p_78244_1_;
		return this;
	}
}