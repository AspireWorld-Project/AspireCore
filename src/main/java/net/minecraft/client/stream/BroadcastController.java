package net.minecraft.client.stream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ThreadSafeBoundList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.twitch.*;
import tv.twitch.broadcast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BroadcastController implements IStatCallbacks, IStreamCallbacks {
	private static final Logger field_152861_B = LogManager.getLogger();
	protected final int field_152865_a = 30;
	protected final int field_152866_b = 3;
	private static final ThreadSafeBoundList field_152862_C = new ThreadSafeBoundList(String.class, 50);
	private String field_152863_D = null;
	protected BroadcastController.BroadcastListener field_152867_c = null;
	protected String field_152868_d = "";
	protected String field_152869_e = "";
	protected String field_152870_f = "";
	protected boolean field_152871_g = true;
	protected Core field_152872_h = null;
	protected Stream field_152873_i = null;
	protected List field_152874_j = new ArrayList();
	protected List field_152875_k = new ArrayList();
	protected boolean field_152876_l = false;
	protected boolean field_152877_m = false;
	protected boolean field_152878_n = false;
	protected BroadcastController.BroadcastState field_152879_o;
	protected String field_152880_p;
	protected VideoParams field_152881_q;
	protected AudioParams field_152882_r;
	protected IngestList field_152883_s;
	protected IngestServer field_152884_t;
	protected AuthToken field_152885_u;
	protected ChannelInfo field_152886_v;
	protected UserInfo field_152887_w;
	protected StreamInfo field_152888_x;
	protected ArchivingState field_152889_y;
	protected long field_152890_z;
	protected IngestServerTester field_152860_A;
	private ErrorCode field_152864_E;
	private static final String __OBFID = "CL_00001822";

	@Override
	public void requestAuthTokenCallback(ErrorCode p_requestAuthTokenCallback_1_,
			AuthToken p_requestAuthTokenCallback_2_) {
		if (ErrorCode.succeeded(p_requestAuthTokenCallback_1_)) {
			field_152885_u = p_requestAuthTokenCallback_2_;
			func_152827_a(BroadcastController.BroadcastState.Authenticated);
		} else {
			field_152885_u.data = "";
			func_152827_a(BroadcastController.BroadcastState.Initialized);
			String s = ErrorCode.getString(p_requestAuthTokenCallback_1_);
			func_152820_d(String.format("RequestAuthTokenDoneCallback got failure: %s", s));
		}

		try {
			if (field_152867_c != null) {
				field_152867_c.func_152900_a(p_requestAuthTokenCallback_1_, p_requestAuthTokenCallback_2_);
			}
		} catch (Exception exception) {
			func_152820_d(exception.toString());
		}
	}

	@Override
	public void loginCallback(ErrorCode p_loginCallback_1_, ChannelInfo p_loginCallback_2_) {
		if (ErrorCode.succeeded(p_loginCallback_1_)) {
			field_152886_v = p_loginCallback_2_;
			func_152827_a(BroadcastController.BroadcastState.LoggedIn);
			field_152877_m = true;
		} else {
			func_152827_a(BroadcastController.BroadcastState.Initialized);
			field_152877_m = false;
			String s = ErrorCode.getString(p_loginCallback_1_);
			func_152820_d(String.format("LoginCallback got failure: %s", s));
		}

		try {
			if (field_152867_c != null) {
				field_152867_c.func_152897_a(p_loginCallback_1_);
			}
		} catch (Exception exception) {
			func_152820_d(exception.toString());
		}
	}

	@Override
	public void getIngestServersCallback(ErrorCode p_getIngestServersCallback_1_,
			IngestList p_getIngestServersCallback_2_) {
		if (ErrorCode.succeeded(p_getIngestServersCallback_1_)) {
			field_152883_s = p_getIngestServersCallback_2_;
			field_152884_t = field_152883_s.getDefaultServer();
			func_152827_a(BroadcastController.BroadcastState.ReceivedIngestServers);

			try {
				if (field_152867_c != null) {
					field_152867_c.func_152896_a(p_getIngestServersCallback_2_);
				}
			} catch (Exception exception) {
				func_152820_d(exception.toString());
			}
		} else {
			String s = ErrorCode.getString(p_getIngestServersCallback_1_);
			func_152820_d(String.format("IngestListCallback got failure: %s", s));
			func_152827_a(BroadcastController.BroadcastState.LoggingIn);
		}
	}

	@Override
	public void getUserInfoCallback(ErrorCode p_getUserInfoCallback_1_, UserInfo p_getUserInfoCallback_2_) {
		field_152887_w = p_getUserInfoCallback_2_;

		if (ErrorCode.failed(p_getUserInfoCallback_1_)) {
			String s = ErrorCode.getString(p_getUserInfoCallback_1_);
			func_152820_d(String.format("UserInfoDoneCallback got failure: %s", s));
		}
	}

	@Override
	public void getStreamInfoCallback(ErrorCode p_getStreamInfoCallback_1_, StreamInfo p_getStreamInfoCallback_2_) {
		if (ErrorCode.succeeded(p_getStreamInfoCallback_1_)) {
			field_152888_x = p_getStreamInfoCallback_2_;

			try {
				if (field_152867_c != null) {
					field_152867_c.func_152894_a(p_getStreamInfoCallback_2_);
				}
			} catch (Exception exception) {
				func_152820_d(exception.toString());
			}
		} else {
			String s = ErrorCode.getString(p_getStreamInfoCallback_1_);
			func_152832_e(String.format("StreamInfoDoneCallback got failure: %s", s));
		}
	}

	@Override
	public void getArchivingStateCallback(ErrorCode p_getArchivingStateCallback_1_,
			ArchivingState p_getArchivingStateCallback_2_) {
		field_152889_y = p_getArchivingStateCallback_2_;

		if (ErrorCode.failed(p_getArchivingStateCallback_1_)) {
		}
	}

	@Override
	public void runCommercialCallback(ErrorCode p_runCommercialCallback_1_) {
		if (ErrorCode.failed(p_runCommercialCallback_1_)) {
			String s = ErrorCode.getString(p_runCommercialCallback_1_);
			func_152832_e(String.format("RunCommercialCallback got failure: %s", s));
		}
	}

	@Override
	public void setStreamInfoCallback(ErrorCode p_setStreamInfoCallback_1_) {
		if (ErrorCode.failed(p_setStreamInfoCallback_1_)) {
			String s = ErrorCode.getString(p_setStreamInfoCallback_1_);
			func_152832_e(String.format("SetStreamInfoCallback got failure: %s", s));
		}
	}

	@Override
	public void getGameNameListCallback(ErrorCode p_getGameNameListCallback_1_,
			GameInfoList p_getGameNameListCallback_2_) {
		if (ErrorCode.failed(p_getGameNameListCallback_1_)) {
			String s = ErrorCode.getString(p_getGameNameListCallback_1_);
			func_152820_d(String.format("GameNameListCallback got failure: %s", s));
		}

		try {
			if (field_152867_c != null) {
				field_152867_c.func_152898_a(p_getGameNameListCallback_1_,
						p_getGameNameListCallback_2_ == null ? new GameInfo[0] : p_getGameNameListCallback_2_.list);
			}
		} catch (Exception exception) {
			func_152820_d(exception.toString());
		}
	}

	@Override
	public void bufferUnlockCallback(long p_bufferUnlockCallback_1_) {
		FrameBuffer framebuffer = FrameBuffer.lookupBuffer(p_bufferUnlockCallback_1_);
		field_152875_k.add(framebuffer);
	}

	@Override
	public void startCallback(ErrorCode p_startCallback_1_) {
		if (ErrorCode.succeeded(p_startCallback_1_)) {
			try {
				if (field_152867_c != null) {
					field_152867_c.func_152899_b();
				}
			} catch (Exception exception) {
				func_152820_d(exception.toString());
			}

			func_152827_a(BroadcastController.BroadcastState.Broadcasting);
		} else {
			field_152881_q = null;
			field_152882_r = null;
			func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
			String s = ErrorCode.getString(p_startCallback_1_);
			field_152867_c.func_152892_c(p_startCallback_1_);
			func_152820_d(String.format("startCallback got failure: %s", s));
		}
	}

	@Override
	public void stopCallback(ErrorCode p_stopCallback_1_) {
		if (ErrorCode.succeeded(p_stopCallback_1_)) {
			field_152881_q = null;
			field_152882_r = null;
			func_152831_M();

			try {
				if (field_152867_c != null) {
					field_152867_c.func_152901_c();
				}
			} catch (Exception exception) {
				func_152820_d(exception.toString());
			}

			if (field_152877_m) {
				func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
			} else {
				func_152827_a(BroadcastController.BroadcastState.Initialized);
			}
		} else {
			func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
			String s = ErrorCode.getString(p_stopCallback_1_);
			func_152820_d(String.format("stopCallback got failure: %s", s));
		}
	}

	@Override
	public void sendActionMetaDataCallback(ErrorCode p_sendActionMetaDataCallback_1_) {
		if (ErrorCode.failed(p_sendActionMetaDataCallback_1_)) {
			func_152832_e("Failed sending action metadata: " + ErrorCode.getString(p_sendActionMetaDataCallback_1_));
		}
	}

	@Override
	public void sendStartSpanMetaDataCallback(ErrorCode p_sendStartSpanMetaDataCallback_1_) {
		if (ErrorCode.failed(p_sendStartSpanMetaDataCallback_1_)) {
			func_152832_e(
					"Failed sending span metadata start: " + ErrorCode.getString(p_sendStartSpanMetaDataCallback_1_));
		}
	}

	@Override
	public void sendEndSpanMetaDataCallback(ErrorCode p_sendEndSpanMetaDataCallback_1_) {
		if (ErrorCode.failed(p_sendEndSpanMetaDataCallback_1_)) {
			func_152832_e("Failed sending span metadata end: " + ErrorCode.getString(p_sendEndSpanMetaDataCallback_1_));
		}
	}

	@Override
	public void statCallback(StatType p_statCallback_1_, long p_statCallback_2_) {
	}

	public void func_152841_a(BroadcastController.BroadcastListener p_152841_1_) {
		field_152867_c = p_152841_1_;
	}

	public boolean func_152858_b() {
		return field_152876_l;
	}

	public void func_152842_a(String p_152842_1_) {
		field_152868_d = p_152842_1_;
	}

	public StreamInfo func_152816_j() {
		return field_152888_x;
	}

	public ChannelInfo func_152843_l() {
		return field_152886_v;
	}

	public boolean func_152850_m() {
		return field_152879_o == BroadcastController.BroadcastState.Broadcasting
				|| field_152879_o == BroadcastController.BroadcastState.Paused;
	}

	public boolean func_152857_n() {
		return field_152879_o == BroadcastController.BroadcastState.ReadyToBroadcast;
	}

	public boolean func_152825_o() {
		return field_152879_o == BroadcastController.BroadcastState.IngestTesting;
	}

	public boolean func_152839_p() {
		return field_152879_o == BroadcastController.BroadcastState.Paused;
	}

	public boolean func_152849_q() {
		return field_152877_m;
	}

	public IngestServer func_152833_s() {
		return field_152884_t;
	}

	public void func_152824_a(IngestServer p_152824_1_) {
		field_152884_t = p_152824_1_;
	}

	public IngestList func_152855_t() {
		return field_152883_s;
	}

	public void func_152829_a(float p_152829_1_) {
		field_152873_i.setVolume(AudioDeviceType.TTV_RECORDER_DEVICE, p_152829_1_);
	}

	public void func_152837_b(float p_152837_1_) {
		field_152873_i.setVolume(AudioDeviceType.TTV_PLAYBACK_DEVICE, p_152837_1_);
	}

	public IngestServerTester func_152856_w() {
		return field_152860_A;
	}

	public long func_152844_x() {
		return field_152873_i.getStreamTime();
	}

	protected boolean func_152848_y() {
		return true;
	}

	public BroadcastController() {
		field_152879_o = BroadcastController.BroadcastState.Uninitialized;
		field_152880_p = null;
		field_152881_q = null;
		field_152882_r = null;
		field_152883_s = new IngestList(new IngestServer[0]);
		field_152884_t = null;
		field_152885_u = new AuthToken();
		field_152886_v = new ChannelInfo();
		field_152887_w = new UserInfo();
		field_152888_x = new StreamInfo();
		field_152889_y = new ArchivingState();
		field_152890_z = 0L;
		field_152860_A = null;
		field_152872_h = new Core(new StandardCoreAPI());
		field_152873_i = new Stream(new DesktopStreamAPI());
	}

	protected PixelFormat func_152826_z() {
		return PixelFormat.TTV_PF_RGBA;
	}

	public boolean func_152817_A() {
		if (field_152876_l)
			return false;
		else {
			ErrorCode errorcode = field_152872_h.initialize(field_152868_d, VideoEncoder.TTV_VID_ENC_DEFAULT,
					System.getProperty("java.library.path"));

			if (!func_152853_a(errorcode)) {
				field_152864_E = errorcode;
				return false;
			} else {
				field_152873_i.setStreamCallbacks(this);

				if (!func_152853_a(errorcode)) {
					field_152873_i.setStreamCallbacks(null);
					field_152864_E = errorcode;
					return false;
				} else {
					errorcode = field_152872_h.setTraceLevel(MessageLevel.TTV_ML_ERROR);

					if (!func_152853_a(errorcode)) {
						field_152873_i.setStreamCallbacks(null);
						field_152864_E = errorcode;
						return false;
					} else if (ErrorCode.succeeded(errorcode)) {
						field_152876_l = true;
						func_152827_a(BroadcastController.BroadcastState.Initialized);
						return true;
					} else {
						field_152864_E = errorcode;
						return false;
					}
				}
			}
		}
	}

	public boolean func_152851_B() {
		if (!field_152876_l)
			return true;
		else if (func_152825_o())
			return false;
		else {
			field_152878_n = true;
			func_152845_C();
			field_152873_i.setStreamCallbacks(null);
			field_152873_i.setStatCallbacks(null);
			ErrorCode errorcode = field_152872_h.shutdown();
			func_152853_a(errorcode);
			field_152876_l = false;
			field_152878_n = false;
			func_152827_a(BroadcastController.BroadcastState.Uninitialized);
			return true;
		}
	}

	public boolean func_152818_a(String p_152818_1_, AuthToken p_152818_2_) {
		if (func_152825_o())
			return false;
		else {
			func_152845_C();

			if (p_152818_1_ != null && !p_152818_1_.isEmpty()) {
				if (p_152818_2_ != null && p_152818_2_.data != null && !p_152818_2_.data.isEmpty()) {
					field_152880_p = p_152818_1_;
					field_152885_u = p_152818_2_;

					if (func_152858_b()) {
						func_152827_a(BroadcastController.BroadcastState.Authenticated);
					}

					return true;
				} else {
					func_152820_d("Auth token must be valid");
					return false;
				}
			} else {
				func_152820_d("Username must be valid");
				return false;
			}
		}
	}

	public boolean func_152845_C() {
		if (func_152825_o())
			return false;
		else {
			if (func_152850_m()) {
				field_152873_i.stop(false);
			}

			field_152880_p = "";
			field_152885_u = new AuthToken();

			if (!field_152877_m)
				return false;
			else {
				field_152877_m = false;

				if (!field_152878_n) {
					try {
						if (field_152867_c != null) {
							field_152867_c.func_152895_a();
						}
					} catch (Exception exception) {
						func_152820_d(exception.toString());
					}
				}

				func_152827_a(BroadcastController.BroadcastState.Initialized);
				return true;
			}
		}
	}

	public boolean func_152828_a(String p_152828_1_, String p_152828_2_, String p_152828_3_) {
		if (!field_152877_m)
			return false;
		else {
			if (p_152828_1_ == null || p_152828_1_.equals("")) {
				p_152828_1_ = field_152880_p;
			}

			if (p_152828_2_ == null) {
				p_152828_2_ = "";
			}

			if (p_152828_3_ == null) {
				p_152828_3_ = "";
			}

			StreamInfoForSetting streaminfoforsetting = new StreamInfoForSetting();
			streaminfoforsetting.streamTitle = p_152828_3_;
			streaminfoforsetting.gameName = p_152828_2_;
			ErrorCode errorcode = field_152873_i.setStreamInfo(field_152885_u, p_152828_1_, streaminfoforsetting);
			func_152853_a(errorcode);
			return ErrorCode.succeeded(errorcode);
		}
	}

	public boolean func_152830_D() {
		if (!func_152850_m())
			return false;
		else {
			ErrorCode errorcode = field_152873_i.runCommercial(field_152885_u);
			func_152853_a(errorcode);
			return ErrorCode.succeeded(errorcode);
		}
	}

	public VideoParams func_152834_a(int p_152834_1_, int p_152834_2_, float p_152834_3_, float p_152834_4_) {
		int[] aint = field_152873_i.getMaxResolution(p_152834_1_, p_152834_2_, p_152834_3_, p_152834_4_);
		VideoParams videoparams = new VideoParams();
		videoparams.maxKbps = p_152834_1_;
		videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
		videoparams.pixelFormat = func_152826_z();
		videoparams.targetFps = p_152834_2_;
		videoparams.outputWidth = aint[0];
		videoparams.outputHeight = aint[1];
		videoparams.disableAdaptiveBitrate = false;
		videoparams.verticalFlip = false;
		return videoparams;
	}

	public boolean func_152836_a(VideoParams p_152836_1_) {
		if (p_152836_1_ != null && func_152857_n()) {
			field_152881_q = p_152836_1_.clone();
			field_152882_r = new AudioParams();
			field_152882_r.audioEnabled = field_152871_g && func_152848_y();

			if (!func_152823_L()) {
				field_152881_q = null;
				field_152882_r = null;
				return false;
			} else {
				ErrorCode errorcode = field_152873_i.start(p_152836_1_, field_152882_r, field_152884_t, StartFlags.None,
						true);

				if (ErrorCode.failed(errorcode)) {
					func_152831_M();
					String s = ErrorCode.getString(errorcode);
					func_152820_d(String.format("Error while starting to broadcast: %s", s));
					field_152881_q = null;
					field_152882_r = null;
					return false;
				} else {
					func_152827_a(BroadcastController.BroadcastState.Starting);
					return true;
				}
			}
		} else
			return false;
	}

	public boolean func_152819_E() {
		if (!func_152850_m())
			return false;
		else {
			ErrorCode errorcode = field_152873_i.stop(true);

			if (ErrorCode.failed(errorcode)) {
				String s = ErrorCode.getString(errorcode);
				func_152820_d(String.format("Error while stopping the broadcast: %s", s));
				return false;
			} else {
				func_152827_a(BroadcastController.BroadcastState.Stopping);
				return ErrorCode.succeeded(errorcode);
			}
		}
	}

	public boolean func_152847_F() {
		if (!func_152850_m())
			return false;
		else {
			ErrorCode errorcode = field_152873_i.pauseVideo();

			if (ErrorCode.failed(errorcode)) {
				func_152819_E();
				String s = ErrorCode.getString(errorcode);
				func_152820_d(String.format("Error pausing stream: %s\n", s));
			} else {
				func_152827_a(BroadcastController.BroadcastState.Paused);
			}

			return ErrorCode.succeeded(errorcode);
		}
	}

	public boolean func_152854_G() {
		if (!func_152839_p())
			return false;
		else {
			func_152827_a(BroadcastController.BroadcastState.Broadcasting);
			return true;
		}
	}

	public boolean func_152840_a(String p_152840_1_, long p_152840_2_, String p_152840_4_, String p_152840_5_) {
		ErrorCode errorcode = field_152873_i.sendActionMetaData(field_152885_u, p_152840_1_, p_152840_2_, p_152840_4_,
				p_152840_5_);

		if (ErrorCode.failed(errorcode)) {
			String s3 = ErrorCode.getString(errorcode);
			func_152820_d(String.format("Error while sending meta data: %s\n", s3));
			return false;
		} else
			return true;
	}

	protected void func_152827_a(BroadcastController.BroadcastState p_152827_1_) {
		if (p_152827_1_ != field_152879_o) {
			field_152879_o = p_152827_1_;

			try {
				if (field_152867_c != null) {
					field_152867_c.func_152891_a(p_152827_1_);
				}
			} catch (Exception exception) {
				func_152820_d(exception.toString());
			}
		}
	}

	public void func_152821_H() {
		if (field_152873_i != null && field_152876_l) {
			ErrorCode errorcode = field_152873_i.pollTasks();
			func_152853_a(errorcode);

			if (func_152825_o()) {
				field_152860_A.func_153041_j();

				if (field_152860_A.func_153032_e()) {
					field_152860_A = null;
					func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
				}
			}

			String s;

			switch (BroadcastController.SwitchBroadcastState.field_152815_a[field_152879_o.ordinal()]) {
			case 1:
				func_152827_a(BroadcastController.BroadcastState.LoggingIn);
				errorcode = field_152873_i.login(field_152885_u);

				if (ErrorCode.failed(errorcode)) {
					s = ErrorCode.getString(errorcode);
					func_152820_d(String.format("Error in TTV_Login: %s\n", s));
				}

				break;
			case 2:
				func_152827_a(BroadcastController.BroadcastState.FindingIngestServer);
				errorcode = field_152873_i.getIngestServers(field_152885_u);

				if (ErrorCode.failed(errorcode)) {
					func_152827_a(BroadcastController.BroadcastState.LoggedIn);
					s = ErrorCode.getString(errorcode);
					func_152820_d(String.format("Error in TTV_GetIngestServers: %s\n", s));
				}

				break;
			case 3:
				func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
				errorcode = field_152873_i.getUserInfo(field_152885_u);

				if (ErrorCode.failed(errorcode)) {
					s = ErrorCode.getString(errorcode);
					func_152820_d(String.format("Error in TTV_GetUserInfo: %s\n", s));
				}

				func_152835_I();
				errorcode = field_152873_i.getArchivingState(field_152885_u);

				if (ErrorCode.failed(errorcode)) {
					s = ErrorCode.getString(errorcode);
					func_152820_d(String.format("Error in TTV_GetArchivingState: %s\n", s));
				}
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			default:
				break;
			case 11:
			case 12:
				func_152835_I();
			}
		}
	}

	protected void func_152835_I() {
		long i = System.nanoTime();
		long j = (i - field_152890_z) / 1000000000L;

		if (j >= 30L) {
			field_152890_z = i;
			ErrorCode errorcode = field_152873_i.getStreamInfo(field_152885_u, field_152880_p);

			if (ErrorCode.failed(errorcode)) {
				String s = ErrorCode.getString(errorcode);
				func_152820_d(String.format("Error in TTV_GetStreamInfo: %s", s));
			}
		}
	}

	public IngestServerTester func_152838_J() {
		if (func_152857_n() && field_152883_s != null) {
			if (func_152825_o())
				return null;
			else {
				field_152860_A = new IngestServerTester(field_152873_i, field_152883_s);
				field_152860_A.func_153033_i();
				func_152827_a(BroadcastController.BroadcastState.IngestTesting);
				return field_152860_A;
			}
		} else
			return null;
	}

	protected boolean func_152823_L() {
		for (int i = 0; i < 3; ++i) {
			FrameBuffer framebuffer = field_152873_i
					.allocateFrameBuffer(field_152881_q.outputWidth * field_152881_q.outputHeight * 4);

			if (!framebuffer.getIsValid()) {
				func_152820_d(String.format("Error while allocating frame buffer"));
				return false;
			}

			field_152874_j.add(framebuffer);
			field_152875_k.add(framebuffer);
		}

		return true;
	}

	protected void func_152831_M() {
		for (int i = 0; i < field_152874_j.size(); ++i) {
			FrameBuffer framebuffer = (FrameBuffer) field_152874_j.get(i);
			framebuffer.free();
		}

		field_152875_k.clear();
		field_152874_j.clear();
	}

	public FrameBuffer func_152822_N() {
		if (field_152875_k.size() == 0) {
			func_152820_d(String.format("Out of free buffers, this should never happen"));
			return null;
		} else {
			FrameBuffer framebuffer = (FrameBuffer) field_152875_k.get(field_152875_k.size() - 1);
			field_152875_k.remove(field_152875_k.size() - 1);
			return framebuffer;
		}
	}

	public void func_152846_a(FrameBuffer p_152846_1_) {
		try {
			field_152873_i.captureFrameBuffer_ReadPixels(p_152846_1_);
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Trying to submit a frame to Twitch");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Broadcast State");
			crashreportcategory.addCrashSection("Last reported errors",
					Arrays.toString(field_152862_C.func_152756_c()));
			crashreportcategory.addCrashSection("Buffer", p_152846_1_);
			crashreportcategory.addCrashSection("Free buffer count", Integer.valueOf(field_152875_k.size()));
			crashreportcategory.addCrashSection("Capture buffer count", Integer.valueOf(field_152874_j.size()));
			throw new ReportedException(crashreport);
		}
	}

	public ErrorCode func_152859_b(FrameBuffer p_152859_1_) {
		if (func_152839_p()) {
			func_152854_G();
		} else if (!func_152850_m())
			return ErrorCode.TTV_EC_STREAM_NOT_STARTED;

		ErrorCode errorcode = field_152873_i.submitVideoFrame(p_152859_1_);

		if (errorcode != ErrorCode.TTV_EC_SUCCESS) {
			String s = ErrorCode.getString(errorcode);

			if (ErrorCode.succeeded(errorcode)) {
				func_152832_e(String.format("Warning in SubmitTexturePointer: %s\n", s));
			} else {
				func_152820_d(String.format("Error in SubmitTexturePointer: %s\n", s));
				func_152819_E();
			}

			if (field_152867_c != null) {
				field_152867_c.func_152893_b(errorcode);
			}
		}

		return errorcode;
	}

	protected boolean func_152853_a(ErrorCode p_152853_1_) {
		if (ErrorCode.failed(p_152853_1_)) {
			func_152820_d(ErrorCode.getString(p_152853_1_));
			return false;
		} else
			return true;
	}

	protected void func_152820_d(String p_152820_1_) {
		field_152863_D = p_152820_1_;
		field_152862_C.func_152757_a("<Error> " + p_152820_1_);
		field_152861_B.error(TwitchStream.field_152949_a, "[Broadcast controller] {}", p_152820_1_);
	}

	protected void func_152832_e(String p_152832_1_) {
		field_152862_C.func_152757_a("<Warning> " + p_152832_1_);
		field_152861_B.warn(TwitchStream.field_152949_a, "[Broadcast controller] {}", p_152832_1_);
	}

	public ErrorCode func_152852_P() {
		return field_152864_E;
	}

	@SideOnly(Side.CLIENT)
	public interface BroadcastListener {
		void func_152900_a(ErrorCode p_152900_1_, AuthToken p_152900_2_);

		void func_152897_a(ErrorCode p_152897_1_);

		void func_152898_a(ErrorCode p_152898_1_, GameInfo[] p_152898_2_);

		void func_152891_a(BroadcastController.BroadcastState p_152891_1_);

		void func_152895_a();

		void func_152894_a(StreamInfo p_152894_1_);

		void func_152896_a(IngestList p_152896_1_);

		void func_152893_b(ErrorCode p_152893_1_);

		void func_152899_b();

		void func_152901_c();

		void func_152892_c(ErrorCode p_152892_1_);
	}

	@SideOnly(Side.CLIENT)
	public enum BroadcastState {
		Uninitialized, Initialized, Authenticating, Authenticated, LoggingIn, LoggedIn, FindingIngestServer, ReceivedIngestServers, ReadyToBroadcast, Starting, Broadcasting, Stopping, Paused, IngestTesting;

		private static final String __OBFID = "CL_00001820";
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchBroadcastState {
		static final int[] field_152815_a = new int[BroadcastController.BroadcastState.values().length];
		private static final String __OBFID = "CL_00001821";

		static {
			try {
				field_152815_a[BroadcastController.BroadcastState.Authenticated.ordinal()] = 1;
			} catch (NoSuchFieldError var12) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.LoggedIn.ordinal()] = 2;
			} catch (NoSuchFieldError var11) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.ReceivedIngestServers.ordinal()] = 3;
			} catch (NoSuchFieldError var10) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Starting.ordinal()] = 4;
			} catch (NoSuchFieldError var9) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Stopping.ordinal()] = 5;
			} catch (NoSuchFieldError var8) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.FindingIngestServer.ordinal()] = 6;
			} catch (NoSuchFieldError var7) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Authenticating.ordinal()] = 7;
			} catch (NoSuchFieldError var6) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Initialized.ordinal()] = 8;
			} catch (NoSuchFieldError var5) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Uninitialized.ordinal()] = 9;
			} catch (NoSuchFieldError var4) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.IngestTesting.ordinal()] = 10;
			} catch (NoSuchFieldError var3) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Paused.ordinal()] = 11;
			} catch (NoSuchFieldError var2) {
			}

			try {
				field_152815_a[BroadcastController.BroadcastState.Broadcasting.ordinal()] = 12;
			} catch (NoSuchFieldError var1) {
			}
		}
	}
}