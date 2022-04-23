package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ServerList {
	private static final Logger logger = LogManager.getLogger();
	private final Minecraft mc;
	private final List servers = new ArrayList();
	private static final String __OBFID = "CL_00000891";

	public ServerList(Minecraft p_i1194_1_) {
		mc = p_i1194_1_;
		loadServerList();
	}

	public void loadServerList() {
		try {
			servers.clear();
			NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(mc.mcDataDir, "servers.dat"));

			if (nbttagcompound == null)
				return;

			NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
			}
		} catch (Exception exception) {
			logger.error("Couldn't load server list", exception);
		}
	}

	public void saveServerList() {
		try {
			NBTTagList nbttaglist = new NBTTagList();
			Iterator iterator = servers.iterator();

			while (iterator.hasNext()) {
				ServerData serverdata = (ServerData) iterator.next();
				nbttaglist.appendTag(serverdata.getNBTCompound());
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("servers", nbttaglist);
			CompressedStreamTools.safeWrite(nbttagcompound, new File(mc.mcDataDir, "servers.dat"));
		} catch (Exception exception) {
			logger.error("Couldn't save server list", exception);
		}
	}

	public ServerData getServerData(int p_78850_1_) {
		return (ServerData) servers.get(p_78850_1_);
	}

	public void removeServerData(int p_78851_1_) {
		servers.remove(p_78851_1_);
	}

	public void addServerData(ServerData p_78849_1_) {
		servers.add(p_78849_1_);
	}

	public int countServers() {
		return servers.size();
	}

	public void swapServers(int p_78857_1_, int p_78857_2_) {
		ServerData serverdata = getServerData(p_78857_1_);
		servers.set(p_78857_1_, getServerData(p_78857_2_));
		servers.set(p_78857_2_, serverdata);
		saveServerList();
	}

	public void func_147413_a(int p_147413_1_, ServerData p_147413_2_) {
		servers.set(p_147413_1_, p_147413_2_);
	}

	public static void func_147414_b(ServerData p_147414_0_) {
		ServerList serverlist = new ServerList(Minecraft.getMinecraft());
		serverlist.loadServerList();

		for (int i = 0; i < serverlist.countServers(); ++i) {
			ServerData serverdata1 = serverlist.getServerData(i);

			if (serverdata1.serverName.equals(p_147414_0_.serverName)
					&& serverdata1.serverIP.equals(p_147414_0_.serverIP)) {
				serverlist.func_147413_a(i, p_147414_0_);
				break;
			}
		}

		serverlist.saveServerList();
	}
}