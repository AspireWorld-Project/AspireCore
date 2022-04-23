package net.minecraft.client.model;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class TexturedQuad {
	public PositionTextureVertex[] vertexPositions;
	public int nVertices;
	private boolean invertNormal;
	public TexturedQuad(PositionTextureVertex[] p_i1152_1_) {
		vertexPositions = p_i1152_1_;
		nVertices = p_i1152_1_.length;
	}

	public TexturedQuad(PositionTextureVertex[] p_i1153_1_, int p_i1153_2_, int p_i1153_3_, int p_i1153_4_,
			int p_i1153_5_, float p_i1153_6_, float p_i1153_7_) {
		this(p_i1153_1_);
		float f2 = 0.0F / p_i1153_6_;
		float f3 = 0.0F / p_i1153_7_;
		p_i1153_1_[0] = p_i1153_1_[0].setTexturePosition(p_i1153_4_ / p_i1153_6_ - f2, p_i1153_3_ / p_i1153_7_ + f3);
		p_i1153_1_[1] = p_i1153_1_[1].setTexturePosition(p_i1153_2_ / p_i1153_6_ + f2, p_i1153_3_ / p_i1153_7_ + f3);
		p_i1153_1_[2] = p_i1153_1_[2].setTexturePosition(p_i1153_2_ / p_i1153_6_ + f2, p_i1153_5_ / p_i1153_7_ - f3);
		p_i1153_1_[3] = p_i1153_1_[3].setTexturePosition(p_i1153_4_ / p_i1153_6_ - f2, p_i1153_5_ / p_i1153_7_ - f3);
	}

	public void flipFace() {
		PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[vertexPositions.length];

		for (int i = 0; i < vertexPositions.length; ++i) {
			apositiontexturevertex[i] = vertexPositions[vertexPositions.length - i - 1];
		}

		vertexPositions = apositiontexturevertex;
	}

	public void draw(Tessellator p_78236_1_, float p_78236_2_) {
		Vec3 vec3 = vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
		Vec3 vec31 = vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
		Vec3 vec32 = vec31.crossProduct(vec3).normalize();
		p_78236_1_.startDrawingQuads();

		if (invertNormal) {
			p_78236_1_.setNormal(-((float) vec32.xCoord), -((float) vec32.yCoord), -((float) vec32.zCoord));
		} else {
			p_78236_1_.setNormal((float) vec32.xCoord, (float) vec32.yCoord, (float) vec32.zCoord);
		}

		for (int i = 0; i < 4; ++i) {
			PositionTextureVertex positiontexturevertex = vertexPositions[i];
			p_78236_1_.addVertexWithUV((float) positiontexturevertex.vector3D.xCoord * p_78236_2_,
					(float) positiontexturevertex.vector3D.yCoord * p_78236_2_,
					(float) positiontexturevertex.vector3D.zCoord * p_78236_2_, positiontexturevertex.texturePositionX,
					positiontexturevertex.texturePositionY);
		}

		p_78236_1_.draw();
	}
}