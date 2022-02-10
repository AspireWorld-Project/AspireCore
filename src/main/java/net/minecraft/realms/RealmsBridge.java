package net.minecraft.realms;

import java.lang.reflect.Constructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class RealmsBridge extends RealmsScreen {
	private static final Logger LOGGER = LogManager.getLogger();
	private GuiScreen previousScreen;
	private static final String __OBFID = "CL_00001869";

	public void switchToRealms(GuiScreen p_switchToRealms_1_) {
		previousScreen = p_switchToRealms_1_;

		try {
			Class oclass = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
			Constructor constructor = oclass.getDeclaredConstructor(new Class[] { RealmsScreen.class });
			constructor.setAccessible(true);
			Object object = constructor.newInstance(new Object[] { this });
			Minecraft.getMinecraft().displayGuiScreen(((RealmsScreen) object).getProxy());
		} catch (Exception exception) {
			LOGGER.error("Realms module missing", exception);
		}
	}

	@Override
	public void init() {
		Minecraft.getMinecraft().displayGuiScreen(previousScreen);
	}
}