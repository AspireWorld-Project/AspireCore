package net.minecraft.client.shader;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

import javax.vecmath.Matrix4f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@SideOnly(Side.CLIENT)
public class ShaderUniform {
	private static final Logger logger = LogManager.getLogger();
	private int field_148102_b;
	private final int field_148103_c;
	private final int field_148100_d;
	private final IntBuffer field_148101_e;
	private final FloatBuffer field_148098_f;
	private final String field_148099_g;
	private boolean field_148105_h;
	private final ShaderManager field_148106_i;
	private static final String __OBFID = "CL_00001046";

	public ShaderUniform(String p_i45092_1_, int p_i45092_2_, int p_i45092_3_, ShaderManager p_i45092_4_) {
		field_148099_g = p_i45092_1_;
		field_148103_c = p_i45092_3_;
		field_148100_d = p_i45092_2_;
		field_148106_i = p_i45092_4_;

		if (p_i45092_2_ <= 3) {
			field_148101_e = BufferUtils.createIntBuffer(p_i45092_3_);
			field_148098_f = null;
		} else {
			field_148101_e = null;
			field_148098_f = BufferUtils.createFloatBuffer(p_i45092_3_);
		}

		field_148102_b = -1;
		func_148096_h();
	}

	private void func_148096_h() {
		field_148105_h = true;

		if (field_148106_i != null) {
			field_148106_i.func_147985_d();
		}
	}

	public static int func_148085_a(String p_148085_0_) {
		byte b0 = -1;

		if (p_148085_0_.equals("int")) {
			b0 = 0;
		} else if (p_148085_0_.equals("float")) {
			b0 = 4;
		} else if (p_148085_0_.startsWith("matrix")) {
			if (p_148085_0_.endsWith("2x2")) {
				b0 = 8;
			} else if (p_148085_0_.endsWith("3x3")) {
				b0 = 9;
			} else if (p_148085_0_.endsWith("4x4")) {
				b0 = 10;
			}
		}

		return b0;
	}

	public void func_148084_b(int p_148084_1_) {
		field_148102_b = p_148084_1_;
	}

	public String func_148086_a() {
		return field_148099_g;
	}

	public void func_148090_a(float p_148090_1_) {
		field_148098_f.position(0);
		field_148098_f.put(0, p_148090_1_);
		func_148096_h();
	}

	public void func_148087_a(float p_148087_1_, float p_148087_2_) {
		field_148098_f.position(0);
		field_148098_f.put(0, p_148087_1_);
		field_148098_f.put(1, p_148087_2_);
		func_148096_h();
	}

	public void func_148095_a(float p_148095_1_, float p_148095_2_, float p_148095_3_) {
		field_148098_f.position(0);
		field_148098_f.put(0, p_148095_1_);
		field_148098_f.put(1, p_148095_2_);
		field_148098_f.put(2, p_148095_3_);
		func_148096_h();
	}

	public void func_148081_a(float p_148081_1_, float p_148081_2_, float p_148081_3_, float p_148081_4_) {
		field_148098_f.position(0);
		field_148098_f.put(p_148081_1_);
		field_148098_f.put(p_148081_2_);
		field_148098_f.put(p_148081_3_);
		field_148098_f.put(p_148081_4_);
		field_148098_f.flip();
		func_148096_h();
	}

	public void func_148092_b(float p_148092_1_, float p_148092_2_, float p_148092_3_, float p_148092_4_) {
		field_148098_f.position(0);

		if (field_148100_d >= 4) {
			field_148098_f.put(0, p_148092_1_);
		}

		if (field_148100_d >= 5) {
			field_148098_f.put(1, p_148092_2_);
		}

		if (field_148100_d >= 6) {
			field_148098_f.put(2, p_148092_3_);
		}

		if (field_148100_d >= 7) {
			field_148098_f.put(3, p_148092_4_);
		}

		func_148096_h();
	}

	public void func_148083_a(int p_148083_1_, int p_148083_2_, int p_148083_3_, int p_148083_4_) {
		field_148101_e.position(0);

		if (field_148100_d >= 0) {
			field_148101_e.put(0, p_148083_1_);
		}

		if (field_148100_d >= 1) {
			field_148101_e.put(1, p_148083_2_);
		}

		if (field_148100_d >= 2) {
			field_148101_e.put(2, p_148083_3_);
		}

		if (field_148100_d >= 3) {
			field_148101_e.put(3, p_148083_4_);
		}

		func_148096_h();
	}

	public void func_148097_a(float[] p_148097_1_) {
		if (p_148097_1_.length < field_148103_c) {
			logger.warn("Uniform.set called with a too-small value array (expected " + field_148103_c + ", got "
					+ p_148097_1_.length + "). Ignoring.");
		} else {
			field_148098_f.position(0);
			field_148098_f.put(p_148097_1_);
			field_148098_f.position(0);
			func_148096_h();
		}
	}

	public void func_148094_a(float p_148094_1_, float p_148094_2_, float p_148094_3_, float p_148094_4_,
			float p_148094_5_, float p_148094_6_, float p_148094_7_, float p_148094_8_, float p_148094_9_,
			float p_148094_10_, float p_148094_11_, float p_148094_12_, float p_148094_13_, float p_148094_14_,
			float p_148094_15_, float p_148094_16_) {
		field_148098_f.position(0);
		field_148098_f.put(0, p_148094_1_);
		field_148098_f.put(1, p_148094_2_);
		field_148098_f.put(2, p_148094_3_);
		field_148098_f.put(3, p_148094_4_);
		field_148098_f.put(4, p_148094_5_);
		field_148098_f.put(5, p_148094_6_);
		field_148098_f.put(6, p_148094_7_);
		field_148098_f.put(7, p_148094_8_);
		field_148098_f.put(8, p_148094_9_);
		field_148098_f.put(9, p_148094_10_);
		field_148098_f.put(10, p_148094_11_);
		field_148098_f.put(11, p_148094_12_);
		field_148098_f.put(12, p_148094_13_);
		field_148098_f.put(13, p_148094_14_);
		field_148098_f.put(14, p_148094_15_);
		field_148098_f.put(15, p_148094_16_);
		func_148096_h();
	}

	public void func_148088_a(Matrix4f p_148088_1_) {
		func_148094_a(p_148088_1_.m00, p_148088_1_.m01, p_148088_1_.m02, p_148088_1_.m03, p_148088_1_.m10,
				p_148088_1_.m11, p_148088_1_.m12, p_148088_1_.m13, p_148088_1_.m20, p_148088_1_.m21, p_148088_1_.m22,
				p_148088_1_.m23, p_148088_1_.m30, p_148088_1_.m31, p_148088_1_.m32, p_148088_1_.m33);
	}

	public void func_148093_b() {
		if (!field_148105_h) {
        }

		field_148105_h = false;

		if (field_148100_d <= 3) {
			func_148091_i();
		} else if (field_148100_d <= 7) {
			func_148089_j();
		} else {
			if (field_148100_d > 10) {
				logger.warn("Uniform.upload called, but type value (" + field_148100_d + ") is not "
						+ "a valid type. Ignoring.");
				return;
			}

			func_148082_k();
		}
	}

	private void func_148091_i() {
		switch (field_148100_d) {
		case 0:
			OpenGlHelper.func_153181_a(field_148102_b, field_148101_e);
			break;
		case 1:
			OpenGlHelper.func_153182_b(field_148102_b, field_148101_e);
			break;
		case 2:
			OpenGlHelper.func_153192_c(field_148102_b, field_148101_e);
			break;
		case 3:
			OpenGlHelper.func_153162_d(field_148102_b, field_148101_e);
			break;
		default:
			logger.warn("Uniform.upload called, but count value (" + field_148103_c + ") is "
					+ " not in the range of 1 to 4. Ignoring.");
		}
	}

	private void func_148089_j() {
		switch (field_148100_d) {
		case 4:
			OpenGlHelper.func_153168_a(field_148102_b, field_148098_f);
			break;
		case 5:
			OpenGlHelper.func_153177_b(field_148102_b, field_148098_f);
			break;
		case 6:
			OpenGlHelper.func_153191_c(field_148102_b, field_148098_f);
			break;
		case 7:
			OpenGlHelper.func_153159_d(field_148102_b, field_148098_f);
			break;
		default:
			logger.warn("Uniform.upload called, but count value (" + field_148103_c + ") is "
					+ "not in the range of 1 to 4. Ignoring.");
		}
	}

	private void func_148082_k() {
		switch (field_148100_d) {
		case 8:
			OpenGlHelper.func_153173_a(field_148102_b, true, field_148098_f);
			break;
		case 9:
			OpenGlHelper.func_153189_b(field_148102_b, true, field_148098_f);
			break;
		case 10:
			OpenGlHelper.func_153160_c(field_148102_b, true, field_148098_f);
		}
	}
}