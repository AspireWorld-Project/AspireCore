package net.minecraft.client.stream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.*;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class IngestServerTester implements IStatCallbacks, IStreamCallbacks {
	protected final boolean field_153043_a = true;
	protected IngestServerTester.IngestTestListener field_153044_b = null;
	protected Stream field_153045_c = null;
	protected IngestList field_153046_d = null;
	protected IngestServerTester.IngestTestState field_153047_e;
	protected long field_153048_f;
	protected long field_153049_g;
	protected long field_153050_h;
	protected RTMPState field_153051_i;
	protected VideoParams field_153052_j;
	protected AudioParams field_153053_k;
	protected long field_153054_l;
	protected List field_153055_m;
	protected boolean field_153056_n;
	protected IStreamCallbacks field_153057_o;
	protected IStatCallbacks field_153058_p;
	protected IngestServer field_153059_q;
	protected boolean field_153060_r;
	protected boolean field_153061_s;
	protected int field_153062_t;
	protected int field_153063_u;
	protected long field_153064_v;
	protected float field_153065_w;
	protected float field_153066_x;
	protected boolean field_153067_y;
	private static final String __OBFID = "CL_00001816";

	public void func_153042_a(IngestServerTester.IngestTestListener p_153042_1_) {
		field_153044_b = p_153042_1_;
	}

	public IngestServer func_153040_c() {
		return field_153059_q;
	}

	public boolean func_153032_e() {
		return field_153047_e == IngestServerTester.IngestTestState.Finished
				|| field_153047_e == IngestServerTester.IngestTestState.Cancelled
				|| field_153047_e == IngestServerTester.IngestTestState.Failed;
	}

	public float func_153030_h() {
		return field_153066_x;
	}

	@Override
	public void requestAuthTokenCallback(ErrorCode p_requestAuthTokenCallback_1_,
			AuthToken p_requestAuthTokenCallback_2_) {
	}

	@Override
	public void loginCallback(ErrorCode p_loginCallback_1_, ChannelInfo p_loginCallback_2_) {
	}

	@Override
	public void getIngestServersCallback(ErrorCode p_getIngestServersCallback_1_,
			IngestList p_getIngestServersCallback_2_) {
	}

	@Override
	public void getUserInfoCallback(ErrorCode p_getUserInfoCallback_1_, UserInfo p_getUserInfoCallback_2_) {
	}

	@Override
	public void getStreamInfoCallback(ErrorCode p_getStreamInfoCallback_1_, StreamInfo p_getStreamInfoCallback_2_) {
	}

	@Override
	public void getArchivingStateCallback(ErrorCode p_getArchivingStateCallback_1_,
			ArchivingState p_getArchivingStateCallback_2_) {
	}

	@Override
	public void runCommercialCallback(ErrorCode p_runCommercialCallback_1_) {
	}

	@Override
	public void setStreamInfoCallback(ErrorCode p_setStreamInfoCallback_1_) {
	}

	@Override
	public void getGameNameListCallback(ErrorCode p_getGameNameListCallback_1_,
			GameInfoList p_getGameNameListCallback_2_) {
	}

	@Override
	public void bufferUnlockCallback(long p_bufferUnlockCallback_1_) {
	}

	@Override
	public void startCallback(ErrorCode p_startCallback_1_) {
		field_153067_y = false;

		if (ErrorCode.succeeded(p_startCallback_1_)) {
			func_153034_a(IngestServerTester.IngestTestState.ConnectingToServer);
			field_153054_l = System.currentTimeMillis();
		} else {
			field_153056_n = false;
			func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
		}
	}

	@Override
	public void stopCallback(ErrorCode p_stopCallback_1_) {
		field_153067_y = false;
		func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
	}

	@Override
	public void sendActionMetaDataCallback(ErrorCode p_sendActionMetaDataCallback_1_) {
	}

	@Override
	public void sendStartSpanMetaDataCallback(ErrorCode p_sendStartSpanMetaDataCallback_1_) {
	}

	@Override
	public void sendEndSpanMetaDataCallback(ErrorCode p_sendEndSpanMetaDataCallback_1_) {
	}

	@Override
	public void statCallback(StatType p_statCallback_1_, long p_statCallback_2_) {
		switch (IngestServerTester.SwitchStatType.field_153026_a[p_statCallback_1_.ordinal()]) {
		case 1:
			field_153051_i = RTMPState.lookupValue((int) p_statCallback_2_);
			break;
		case 2:
			field_153050_h = p_statCallback_2_;
		}
	}

	public IngestServerTester(Stream p_i1019_1_, IngestList p_i1019_2_) {
		field_153047_e = IngestServerTester.IngestTestState.Uninitalized;
		field_153048_f = 8000L;
		field_153049_g = 1000L;
		field_153050_h = 0L;
		field_153051_i = RTMPState.Invalid;
		field_153052_j = null;
		field_153053_k = null;
		field_153054_l = 0L;
		field_153055_m = null;
		field_153056_n = false;
		field_153057_o = null;
		field_153058_p = null;
		field_153059_q = null;
		field_153060_r = false;
		field_153061_s = false;
		field_153062_t = -1;
		field_153063_u = 0;
		field_153064_v = 0L;
		field_153065_w = 0.0F;
		field_153066_x = 0.0F;
		field_153067_y = false;
		field_153045_c = p_i1019_1_;
		field_153046_d = p_i1019_2_;
	}

	@Override
	protected void finalize() throws Throwable {
		if (field_153059_q != null) {
			func_153035_b(field_153059_q);
		}

		func_153031_o();
		super.finalize();
	}

	public void func_153033_i() {
		if (field_153047_e == IngestServerTester.IngestTestState.Uninitalized) {
			field_153062_t = 0;
			field_153060_r = false;
			field_153061_s = false;
			field_153058_p = field_153045_c.getStatCallbacks();
			field_153045_c.setStatCallbacks(this);
			field_153057_o = field_153045_c.getStreamCallbacks();
			field_153045_c.setStreamCallbacks(this);
			field_153052_j = new VideoParams();
			field_153052_j.targetFps = 60;
			field_153052_j.maxKbps = 3500;
			field_153052_j.outputWidth = 1280;
			field_153052_j.outputHeight = 720;
			field_153052_j.pixelFormat = PixelFormat.TTV_PF_BGRA;
			field_153052_j.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
			field_153052_j.disableAdaptiveBitrate = true;
			field_153052_j.verticalFlip = false;
			field_153045_c.getDefaultParams(field_153052_j);
			field_153053_k = new AudioParams();
			field_153053_k.audioEnabled = false;
			field_153055_m = new ArrayList();
			byte b0 = 3;

			for (int i = 0; i < b0; ++i) {
				FrameBuffer framebuffer = field_153045_c
						.allocateFrameBuffer(field_153052_j.outputWidth * field_153052_j.outputHeight * 4);

				if (!framebuffer.getIsValid()) {
					func_153031_o();
					func_153034_a(IngestServerTester.IngestTestState.Failed);
					return;
				}

				field_153055_m.add(framebuffer);
				field_153045_c.randomizeFrameBuffer(framebuffer);
			}

			func_153034_a(IngestServerTester.IngestTestState.Starting);
			field_153054_l = System.currentTimeMillis();
		}
	}

	public void func_153041_j() {
		if (!func_153032_e() && field_153047_e != IngestServerTester.IngestTestState.Uninitalized) {
			if (!field_153067_y) {
				if (field_153060_r) {
					func_153034_a(IngestServerTester.IngestTestState.Cancelled);
				}

				switch (IngestServerTester.SwitchStatType.field_153027_b[field_153047_e.ordinal()]) {
				case 1:
				case 2:
					if (field_153059_q != null) {
						if (!field_153061_s && field_153056_n) {
							func_153035_b(field_153059_q);
						} else {
							field_153059_q.bitrateKbps = 0.0F;
							field_153054_l = 0L;
							field_153067_y = false;
						}

						field_153059_q = null;
					} else if (!field_153067_y && func_153037_m() >= field_153049_g) {
						field_153054_l = 0L;
						field_153061_s = false;
						field_153056_n = true;

						if (field_153047_e != IngestServerTester.IngestTestState.Starting) {
							++field_153062_t;
						}

						if (field_153062_t < field_153046_d.getServers().length) {
							field_153059_q = field_153046_d.getServers()[field_153062_t];
							func_153036_a(field_153059_q);
						} else {
							func_153034_a(IngestServerTester.IngestTestState.Finished);
						}
					}

					break;
				case 3:
				case 4:
					func_153029_c(field_153059_q);
				}

				func_153038_n();

				if (field_153047_e == IngestServerTester.IngestTestState.Cancelled
						|| field_153047_e == IngestServerTester.IngestTestState.Finished) {
					if (field_153059_q != null) {
						if (field_153047_e == IngestServerTester.IngestTestState.Cancelled) {
							field_153059_q.bitrateKbps = 0.0F;
						}

						func_153035_b(field_153059_q);
						field_153059_q = null;
					}

					if (field_153055_m != null) {
						func_153031_o();
					}
				}
			}
		}
	}

	public void func_153039_l() {
		if (!func_153032_e()) {
			field_153060_r = true;
		}
	}

	protected boolean func_153036_a(IngestServer p_153036_1_) {
		field_153056_n = true;
		field_153050_h = 0L;
		field_153051_i = RTMPState.Idle;
		field_153059_q = p_153036_1_;
		func_153034_a(IngestServerTester.IngestTestState.ConnectingToServer);
		field_153067_y = true;
		ErrorCode errorcode = field_153045_c.start(field_153052_j, field_153053_k, p_153036_1_,
				StartFlags.TTV_Start_BandwidthTest, true);

		if (ErrorCode.failed(errorcode)) {
			field_153056_n = false;
			func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
			return false;
		} else {
			field_153064_v = field_153050_h;
			p_153036_1_.bitrateKbps = 0.0F;
			field_153063_u = 0;
			return true;
		}
	}

	protected void func_153035_b(IngestServer p_153035_1_) {
		field_153067_y = true;
		field_153045_c.stop(true);
		field_153045_c.pollStats();
	}

	protected long func_153037_m() {
		return System.currentTimeMillis() - field_153054_l;
	}

	protected void func_153038_n() {
		float f = func_153037_m();

		switch (IngestServerTester.SwitchStatType.field_153027_b[field_153047_e.ordinal()]) {
		case 1:
		case 3:
		case 5:
		case 6:
		case 7:
		case 8:
			field_153066_x = 0.0F;
			break;
		case 2:
			field_153066_x = 1.0F;
			break;
		case 4:
		default:
			field_153066_x = f / field_153048_f;
		}

		switch (IngestServerTester.SwitchStatType.field_153027_b[field_153047_e.ordinal()]) {
		case 6:
		case 7:
		case 8:
			field_153065_w = 1.0F;
			break;
		default:
			field_153065_w = (float) field_153062_t / (float) field_153046_d.getServers().length;
			field_153065_w += field_153066_x / field_153046_d.getServers().length;
		}
	}

	protected boolean func_153029_c(IngestServer p_153029_1_) {
		if (!field_153061_s && func_153037_m() < field_153048_f) {
			if (field_153067_y)
				return true;
			else {
				ErrorCode errorcode = field_153045_c.submitVideoFrame((FrameBuffer) field_153055_m.get(field_153063_u));

				if (ErrorCode.failed(errorcode)) {
					field_153056_n = false;
					func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
					return false;
				} else {
					field_153063_u = (field_153063_u + 1) % field_153055_m.size();
					field_153045_c.pollStats();

					if (field_153051_i == RTMPState.SendVideo) {
						func_153034_a(IngestServerTester.IngestTestState.TestingServer);
						long i = func_153037_m();

						if (i > 0L && field_153050_h > field_153064_v) {
							p_153029_1_.bitrateKbps = (float) (field_153050_h * 8L) / (float) func_153037_m();
							field_153064_v = field_153050_h;
						}
					}

					return true;
				}
			}
		} else {
			func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
			return true;
		}
	}

	protected void func_153031_o() {
		field_153059_q = null;

		if (field_153055_m != null) {
			for (int i = 0; i < field_153055_m.size(); ++i) {
				((FrameBuffer) field_153055_m.get(i)).free();
			}

			field_153055_m = null;
		}

		if (field_153045_c.getStatCallbacks() == this) {
			field_153045_c.setStatCallbacks(field_153058_p);
			field_153058_p = null;
		}

		if (field_153045_c.getStreamCallbacks() == this) {
			field_153045_c.setStreamCallbacks(field_153057_o);
			field_153057_o = null;
		}
	}

	protected void func_153034_a(IngestServerTester.IngestTestState p_153034_1_) {
		if (p_153034_1_ != field_153047_e) {
			field_153047_e = p_153034_1_;

			if (field_153044_b != null) {
				field_153044_b.func_152907_a(this, p_153034_1_);
			}
		}
	}

	public int func_153028_p() {
		return field_153062_t;
	}

	@SideOnly(Side.CLIENT)
	public interface IngestTestListener {
		void func_152907_a(IngestServerTester p_152907_1_, IngestServerTester.IngestTestState p_152907_2_);
	}

	@SideOnly(Side.CLIENT)
	public enum IngestTestState {
		Uninitalized, Starting, ConnectingToServer, TestingServer, DoneTestingServer, Finished, Cancelled, Failed;

		private static final String __OBFID = "CL_00001814";
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchStatType {
		static final int[] field_153026_a;

		static final int[] field_153027_b = new int[IngestServerTester.IngestTestState.values().length];
		private static final String __OBFID = "CL_00001815";

		static {
			try {
				field_153027_b[IngestServerTester.IngestTestState.Starting.ordinal()] = 1;
			} catch (NoSuchFieldError var10) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.DoneTestingServer.ordinal()] = 2;
			} catch (NoSuchFieldError var9) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.ConnectingToServer.ordinal()] = 3;
			} catch (NoSuchFieldError var8) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.TestingServer.ordinal()] = 4;
			} catch (NoSuchFieldError var7) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.Uninitalized.ordinal()] = 5;
			} catch (NoSuchFieldError var6) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.Finished.ordinal()] = 6;
			} catch (NoSuchFieldError var5) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.Cancelled.ordinal()] = 7;
			} catch (NoSuchFieldError var4) {
			}

			try {
				field_153027_b[IngestServerTester.IngestTestState.Failed.ordinal()] = 8;
			} catch (NoSuchFieldError var3) {
			}

			field_153026_a = new int[StatType.values().length];

			try {
				field_153026_a[StatType.TTV_ST_RTMPSTATE.ordinal()] = 1;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_153026_a[StatType.TTV_ST_RTMPDATASENT.ordinal()] = 2;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}