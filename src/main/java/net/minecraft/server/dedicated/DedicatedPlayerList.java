package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.management.ServerConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.server.ConfigurationHandler;

import java.io.IOException;

@SideOnly(Side.SERVER)
public class DedicatedPlayerList extends ServerConfigurationManager {
	private static final Logger field_164439_d = LogManager.getLogger();
	private static final String __OBFID = "CL_00001783";

	public DedicatedPlayerList(DedicatedServer p_i1503_1_) {
		super(p_i1503_1_);
		func_152611_a(ConfigurationHandler.getWorldsConfig().global.chunkLoading.viewDistance);
		maxPlayers = ConfigurationHandler.getServerConfig().settings.player.maxPlayers;
		setWhiteListEnabled(ConfigurationHandler.getServerConfig().settings.player.whiteList);

		if (!p_i1503_1_.isSinglePlayer()) {
			func_152608_h().func_152686_a(true);
			getBannedIPs().func_152686_a(true);
		}

		func_152620_y();
		func_152617_w();
		func_152619_x();
		func_152618_v();
		readWhiteList();
		loadOpsList();

		if (!func_152599_k().func_152691_c().exists()) {
			saveWhiteList();
		}
	}

	@Override
	public void setWhiteListEnabled(boolean p_72371_1_) {
		super.setWhiteListEnabled(p_72371_1_);
		ConfigurationHandler.getServerConfig().settings.player.whiteList = p_72371_1_;
		getServerInstance().saveProperties();
	}

	@Override
	public void func_152605_a(GameProfile p_152605_1_) {
		super.func_152605_a(p_152605_1_);
	}

	@Override
	public void func_152610_b(GameProfile p_152610_1_) {
		super.func_152610_b(p_152610_1_);
	}

	@Override
	public void func_152597_c(GameProfile p_152597_1_) {
		super.func_152597_c(p_152597_1_);
		saveWhiteList();
	}

	@Override
	public void func_152601_d(GameProfile p_152601_1_) {
		super.func_152601_d(p_152601_1_);
		saveWhiteList();
	}

	@Override
	public void loadWhiteList() {
		readWhiteList();
	}

	private void func_152618_v() {
		try {
			getBannedIPs().func_152678_f();
		} catch (IOException ioexception) {
			field_164439_d.warn("Failed to save ip banlist: ", ioexception);
		}
	}

	private void func_152617_w() {
		try {
			func_152608_h().func_152678_f();
		} catch (IOException ioexception) {
			field_164439_d.warn("Failed to save user banlist: ", ioexception);
		}
	}

	private void func_152619_x() {
		try {
			getBannedIPs().func_152679_g();
		} catch (IOException ioexception) {
			field_164439_d.warn("Failed to load ip banlist: ", ioexception);
		}
	}

	private void func_152620_y() {
		try {
			func_152608_h().func_152679_g();
		} catch (IOException ioexception) {
			field_164439_d.warn("Failed to load user banlist: ", ioexception);
		}
	}

	private void loadOpsList() {
		try {
			func_152603_m().func_152679_g();
		} catch (Exception exception) {
			field_164439_d.warn("Failed to load operators list: ", exception);
		}
	}

	private void saveOpsList() {
		try {
			func_152603_m().func_152678_f();
		} catch (Exception exception) {
			field_164439_d.warn("Failed to save operators list: ", exception);
		}
	}

	private void readWhiteList() {
		try {
			func_152599_k().func_152679_g();
		} catch (Exception exception) {
			field_164439_d.warn("Failed to load white-list: ", exception);
		}
	}

	private void saveWhiteList() {
		try {
			func_152599_k().func_152678_f();
		} catch (Exception exception) {
			field_164439_d.warn("Failed to save white-list: ", exception);
		}
	}

	@Override
	public boolean func_152607_e(GameProfile p_152607_1_) {
		/*
		 * par1Str = par1Str.trim().toLowerCase(); return !this.isWhiteListEnabled() ||
		 * this.getWhiteListedPlayers().contains(par1Str) ||
		 * PermissionHandler.getInstance().hasGlobally(par1Str,
		 * MinecraftPermissions.IGNORE_WHITE_LIST);
		 */
		return !isWhiteListEnabled() || func_152596_g(p_152607_1_) || func_152599_k().func_152705_a(p_152607_1_);
	}

	@Override
	public DedicatedServer getServerInstance() {
		return (DedicatedServer) super.getServerInstance();
	}
}