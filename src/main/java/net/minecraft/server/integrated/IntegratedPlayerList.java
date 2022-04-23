package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ServerConfigurationManager;

import java.net.SocketAddress;

@SideOnly(Side.CLIENT)
public class IntegratedPlayerList extends ServerConfigurationManager {
	private NBTTagCompound hostPlayerData;
	private static final String __OBFID = "CL_00001128";

	public IntegratedPlayerList(IntegratedServer p_i1314_1_) {
		super(p_i1314_1_);
		func_152611_a(10);
	}

	@Override
	protected void writePlayerData(EntityPlayerMP p_72391_1_) {
		if (p_72391_1_.getCommandSenderName().equals(getServerInstance().getServerOwner())) {
			hostPlayerData = new NBTTagCompound();
			p_72391_1_.writeToNBT(hostPlayerData);
		}

		super.writePlayerData(p_72391_1_);
	}

	@Override
	public String allowUserToConnect(SocketAddress p_148542_1_, GameProfile p_148542_2_) {
		return p_148542_2_.getName().equalsIgnoreCase(getServerInstance().getServerOwner())
				&& func_152612_a(p_148542_2_.getName()) != null ? "That name is already taken."
						: super.allowUserToConnect(p_148542_1_, p_148542_2_);
	}

	@Override
	public IntegratedServer getServerInstance() {
		return (IntegratedServer) super.getServerInstance();
	}

	@Override
	public NBTTagCompound getHostPlayerData() {
		return hostPlayerData;
	}
}