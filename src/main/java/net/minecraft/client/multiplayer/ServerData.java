package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

@SideOnly(Side.CLIENT)
public class ServerData {
	public String serverName;
	public String serverIP;
	public String populationInfo;
	public String serverMOTD;
	public long pingToServer;
	public int field_82821_f;
	public String gameVersion;
	public boolean field_78841_f;
	public String field_147412_i;
	private ServerData.ServerResourceMode field_152587_j;
	private String field_147411_m;
	private boolean field_152588_l;
	private static final String __OBFID = "CL_00000890";

	public ServerData(String p_i1193_1_, String p_i1193_2_) {
		field_82821_f = 5;
		gameVersion = "1.7.10";
		field_152587_j = ServerData.ServerResourceMode.PROMPT;
		serverName = p_i1193_1_;
		serverIP = p_i1193_2_;
	}

	public ServerData(String p_i1055_1_, String p_i1055_2_, boolean p_i1055_3_) {
		this(p_i1055_1_, p_i1055_2_);
		field_152588_l = p_i1055_3_;
	}

	public NBTTagCompound getNBTCompound() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("name", serverName);
		nbttagcompound.setString("ip", serverIP);

		if (field_147411_m != null) {
			nbttagcompound.setString("icon", field_147411_m);
		}

		if (field_152587_j == ServerData.ServerResourceMode.ENABLED) {
			nbttagcompound.setBoolean("acceptTextures", true);
		} else if (field_152587_j == ServerData.ServerResourceMode.DISABLED) {
			nbttagcompound.setBoolean("acceptTextures", false);
		}

		return nbttagcompound;
	}

	public ServerData.ServerResourceMode func_152586_b() {
		return field_152587_j;
	}

	public void func_152584_a(ServerData.ServerResourceMode p_152584_1_) {
		field_152587_j = p_152584_1_;
	}

	public static ServerData getServerDataFromNBTCompound(NBTTagCompound p_78837_0_) {
		ServerData serverdata = new ServerData(p_78837_0_.getString("name"), p_78837_0_.getString("ip"));

		if (p_78837_0_.hasKey("icon", 8)) {
			serverdata.func_147407_a(p_78837_0_.getString("icon"));
		}

		if (p_78837_0_.hasKey("acceptTextures", 1)) {
			if (p_78837_0_.getBoolean("acceptTextures")) {
				serverdata.func_152584_a(ServerData.ServerResourceMode.ENABLED);
			} else {
				serverdata.func_152584_a(ServerData.ServerResourceMode.DISABLED);
			}
		} else {
			serverdata.func_152584_a(ServerData.ServerResourceMode.PROMPT);
		}

		return serverdata;
	}

	public String getBase64EncodedIconData() {
		return field_147411_m;
	}

	public void func_147407_a(String p_147407_1_) {
		field_147411_m = p_147407_1_;
	}

	public void func_152583_a(ServerData p_152583_1_) {
		serverIP = p_152583_1_.serverIP;
		serverName = p_152583_1_.serverName;
		func_152584_a(p_152583_1_.func_152586_b());
		field_147411_m = p_152583_1_.field_147411_m;
	}

	public boolean func_152585_d() {
		return field_152588_l;
	}

	@SideOnly(Side.CLIENT)
	public static enum ServerResourceMode {
		ENABLED("enabled"), DISABLED("disabled"), PROMPT("prompt");
		private final IChatComponent field_152594_d;

		private static final String __OBFID = "CL_00001833";

		private ServerResourceMode(String p_i1053_3_) {
			field_152594_d = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_, new Object[0]);
		}

		public IChatComponent func_152589_a() {
			return field_152594_d;
		}
	}
}