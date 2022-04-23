package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.client.util.QuadComparator;
import org.lwjgl.opengl.GL11;

import java.nio.*;
import java.util.Arrays;
import java.util.PriorityQueue;

@SideOnly(Side.CLIENT)
public class Tessellator {
	private static int nativeBufferSize = 0x200000;
	private static int trivertsInBuffer = nativeBufferSize / 48 * 6;
	public static boolean renderingWorldRenderer = false;
	public boolean defaultTexture = false;
	private int rawBufferSize = 0;
	public int textureID = 0;

	private static ByteBuffer byteBuffer = GLAllocation.createDirectByteBuffer(nativeBufferSize * 4);
	private static IntBuffer intBuffer = byteBuffer.asIntBuffer();
	private static FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
	private static ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
	private int[] rawBuffer;
	private int vertexCount;
	private double textureU;
	private double textureV;
	private int brightness;
	private int color;
	private boolean hasColor;
	private boolean hasTexture;
	private boolean hasBrightness;
	private boolean hasNormals;
	private int rawBufferIndex;
	private int addedVertices;
	private boolean isColorDisabled;
	private int drawMode;
	private double xOffset;
	private double yOffset;
	private double zOffset;
	private int normal;
	public static final Tessellator instance = new Tessellator(2097152);
	private boolean isDrawing;
	private int bufferSize;
	private static final String __OBFID = "CL_00000960";

	private Tessellator(int p_i1250_1_) {
	}

	public Tessellator() {
	}

	static {
		instance.defaultTexture = true;
	}

	public int draw() {
		if (!isDrawing)
			throw new IllegalStateException("Not tesselating!");
		else {
			isDrawing = false;

			int offs = 0;
			while (offs < vertexCount) {
				int vtc = Math.min(vertexCount - offs, nativeBufferSize >> 5);
				intBuffer.clear();
				intBuffer.put(rawBuffer, offs * 8, vtc * 8);
				byteBuffer.position(0);
				byteBuffer.limit(vtc * 32);
				offs += vtc;

				if (hasTexture) {
					floatBuffer.position(3);
					GL11.glTexCoordPointer(2, 32, floatBuffer);
					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if (hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
					shortBuffer.position(14);
					GL11.glTexCoordPointer(2, 32, shortBuffer);
					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
				}

				if (hasColor) {
					byteBuffer.position(20);
					GL11.glColorPointer(4, true, 32, byteBuffer);
					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
				}

				if (hasNormals) {
					byteBuffer.position(24);
					GL11.glNormalPointer(32, byteBuffer);
					GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
				}

				floatBuffer.position(0);
				GL11.glVertexPointer(3, 32, floatBuffer);
				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glDrawArrays(drawMode, 0, vtc);
				GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

				if (hasTexture) {
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				}

				if (hasBrightness) {
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
				}

				if (hasColor) {
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
				}

				if (hasNormals) {
					GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
				}
			}

			if (rawBufferSize > 0x20000 && rawBufferIndex < rawBufferSize << 3) {
				rawBufferSize = 0x10000;
				rawBuffer = new int[rawBufferSize];
			}

			int i = rawBufferIndex * 4;
			reset();
			return i;
		}
	}

	public TesselatorVertexState getVertexState(float p_147564_1_, float p_147564_2_, float p_147564_3_) {
		int[] aint = new int[rawBufferIndex];
		PriorityQueue priorityqueue = new PriorityQueue(rawBufferIndex, new QuadComparator(rawBuffer,
				p_147564_1_ + (float) xOffset, p_147564_2_ + (float) yOffset, p_147564_3_ + (float) zOffset));
		byte b0 = 32;
		int i;

		for (i = 0; i < rawBufferIndex; i += b0) {
			priorityqueue.add(Integer.valueOf(i));
		}

		for (i = 0; !priorityqueue.isEmpty(); i += b0) {
			int j = ((Integer) priorityqueue.remove()).intValue();

			for (int k = 0; k < b0; ++k) {
				aint[i + k] = rawBuffer[j + k];
			}
		}

		System.arraycopy(aint, 0, rawBuffer, 0, aint.length);
		return new TesselatorVertexState(aint, rawBufferIndex, vertexCount, hasTexture, hasBrightness, hasNormals,
				hasColor);
	}

	public void setVertexState(TesselatorVertexState p_147565_1_) {
		while (p_147565_1_.getRawBuffer().length > rawBufferSize && rawBufferSize > 0) {
			rawBufferSize <<= 1;
		}
		if (rawBufferSize > rawBuffer.length) {
			rawBuffer = new int[rawBufferSize];
		}
		System.arraycopy(p_147565_1_.getRawBuffer(), 0, rawBuffer, 0, p_147565_1_.getRawBuffer().length);
		rawBufferIndex = p_147565_1_.getRawBufferIndex();
		vertexCount = p_147565_1_.getVertexCount();
		hasTexture = p_147565_1_.getHasTexture();
		hasBrightness = p_147565_1_.getHasBrightness();
		hasColor = p_147565_1_.getHasColor();
		hasNormals = p_147565_1_.getHasNormals();
	}

	private void reset() {
		vertexCount = 0;
		byteBuffer.clear();
		rawBufferIndex = 0;
		addedVertices = 0;
	}

	public void startDrawingQuads() {
		startDrawing(7);
	}

	public void startDrawing(int p_78371_1_) {
		if (isDrawing)
			throw new IllegalStateException("Already tesselating!");
		else {
			isDrawing = true;
			reset();
			drawMode = p_78371_1_;
			hasNormals = false;
			hasColor = false;
			hasTexture = false;
			hasBrightness = false;
			isColorDisabled = false;
		}
	}

	public void setTextureUV(double p_78385_1_, double p_78385_3_) {
		hasTexture = true;
		textureU = p_78385_1_;
		textureV = p_78385_3_;
	}

	public void setBrightness(int p_78380_1_) {
		hasBrightness = true;
		brightness = p_78380_1_;
	}

	public void setColorOpaque_F(float p_78386_1_, float p_78386_2_, float p_78386_3_) {
		setColorOpaque((int) (p_78386_1_ * 255.0F), (int) (p_78386_2_ * 255.0F), (int) (p_78386_3_ * 255.0F));
	}

	public void setColorRGBA_F(float p_78369_1_, float p_78369_2_, float p_78369_3_, float p_78369_4_) {
		setColorRGBA((int) (p_78369_1_ * 255.0F), (int) (p_78369_2_ * 255.0F), (int) (p_78369_3_ * 255.0F),
				(int) (p_78369_4_ * 255.0F));
	}

	public void setColorOpaque(int p_78376_1_, int p_78376_2_, int p_78376_3_) {
		setColorRGBA(p_78376_1_, p_78376_2_, p_78376_3_, 255);
	}

	public void setColorRGBA(int p_78370_1_, int p_78370_2_, int p_78370_3_, int p_78370_4_) {
		if (!isColorDisabled) {
			if (p_78370_1_ > 255) {
				p_78370_1_ = 255;
			}

			if (p_78370_2_ > 255) {
				p_78370_2_ = 255;
			}

			if (p_78370_3_ > 255) {
				p_78370_3_ = 255;
			}

			if (p_78370_4_ > 255) {
				p_78370_4_ = 255;
			}

			if (p_78370_1_ < 0) {
				p_78370_1_ = 0;
			}

			if (p_78370_2_ < 0) {
				p_78370_2_ = 0;
			}

			if (p_78370_3_ < 0) {
				p_78370_3_ = 0;
			}

			if (p_78370_4_ < 0) {
				p_78370_4_ = 0;
			}

			hasColor = true;

			if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
				color = p_78370_4_ << 24 | p_78370_3_ << 16 | p_78370_2_ << 8 | p_78370_1_;
			} else {
				color = p_78370_1_ << 24 | p_78370_2_ << 16 | p_78370_3_ << 8 | p_78370_4_;
			}
		}
	}

	public void func_154352_a(byte p_154352_1_, byte p_154352_2_, byte p_154352_3_) {
		setColorOpaque(p_154352_1_ & 255, p_154352_2_ & 255, p_154352_3_ & 255);
	}

	public void addVertexWithUV(double p_78374_1_, double p_78374_3_, double p_78374_5_, double p_78374_7_,
			double p_78374_9_) {
		setTextureUV(p_78374_7_, p_78374_9_);
		addVertex(p_78374_1_, p_78374_3_, p_78374_5_);
	}

	public void addVertex(double p_78377_1_, double p_78377_3_, double p_78377_5_) {
		if (rawBufferIndex >= rawBufferSize - 32) {
			if (rawBufferSize == 0) {
				rawBufferSize = 0x10000;
				rawBuffer = new int[rawBufferSize];
			} else {
				rawBufferSize *= 2;
				rawBuffer = Arrays.copyOf(rawBuffer, rawBufferSize);
			}
		}
		++addedVertices;

		if (hasTexture) {
			rawBuffer[rawBufferIndex + 3] = Float.floatToRawIntBits((float) textureU);
			rawBuffer[rawBufferIndex + 4] = Float.floatToRawIntBits((float) textureV);
		}

		if (hasBrightness) {
			rawBuffer[rawBufferIndex + 7] = brightness;
		}

		if (hasColor) {
			rawBuffer[rawBufferIndex + 5] = color;
		}

		if (hasNormals) {
			rawBuffer[rawBufferIndex + 6] = normal;
		}

		rawBuffer[rawBufferIndex + 0] = Float.floatToRawIntBits((float) (p_78377_1_ + xOffset));
		rawBuffer[rawBufferIndex + 1] = Float.floatToRawIntBits((float) (p_78377_3_ + yOffset));
		rawBuffer[rawBufferIndex + 2] = Float.floatToRawIntBits((float) (p_78377_5_ + zOffset));
		rawBufferIndex += 8;
		++vertexCount;
	}

	public void setColorOpaque_I(int p_78378_1_) {
		int j = p_78378_1_ >> 16 & 255;
		int k = p_78378_1_ >> 8 & 255;
		int l = p_78378_1_ & 255;
		setColorOpaque(j, k, l);
	}

	public void setColorRGBA_I(int p_78384_1_, int p_78384_2_) {
		int k = p_78384_1_ >> 16 & 255;
		int l = p_78384_1_ >> 8 & 255;
		int i1 = p_78384_1_ & 255;
		setColorRGBA(k, l, i1, p_78384_2_);
	}

	public void disableColor() {
		isColorDisabled = true;
	}

	public void setNormal(float p_78375_1_, float p_78375_2_, float p_78375_3_) {
		hasNormals = true;
		byte b0 = (byte) (int) (p_78375_1_ * 127.0F);
		byte b1 = (byte) (int) (p_78375_2_ * 127.0F);
		byte b2 = (byte) (int) (p_78375_3_ * 127.0F);
		normal = b0 & 255 | (b1 & 255) << 8 | (b2 & 255) << 16;
	}

	public void setTranslation(double p_78373_1_, double p_78373_3_, double p_78373_5_) {
		xOffset = p_78373_1_;
		yOffset = p_78373_3_;
		zOffset = p_78373_5_;
	}

	public void addTranslation(float p_78372_1_, float p_78372_2_, float p_78372_3_) {
		xOffset += p_78372_1_;
		yOffset += p_78372_2_;
		zOffset += p_78372_3_;
	}
}