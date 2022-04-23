package cpw.mods.fml.client;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.StartupQuery;
import cpw.mods.fml.common.ZipperUtil;
import net.minecraft.client.gui.*;
import net.minecraft.world.WorldSettings;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;

public class GuiOldSaveLoadConfirm extends GuiYesNo implements GuiYesNoCallback {

	private final String dirName;
	private final String saveName;
	private final File zip;
	private final GuiScreen parent;

	public GuiOldSaveLoadConfirm(String dirName, String saveName, GuiScreen parent) {
		super(null, "", "", 0);
		this.parent = parent;
		this.dirName = dirName;
		this.saveName = saveName;
		zip = new File(FMLClientHandler.instance().getClient().mcDataDir,
				String.format("%s-%2$td%2$tm%2$ty%2$tH%2$tM%2$tS.zip", dirName, System.currentTimeMillis()));
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, String.format("The world %s contains pre-update modding data", saveName),
				width / 2, 50, 16777215);
		drawCenteredString(fontRendererObj, String.format("There may be problems updating it to this version"),
				width / 2, 70, 16777215);
		drawCenteredString(fontRendererObj, String.format("FML will save a zip to %s", zip.getName()), width / 2, 90,
				16777215);
		drawCenteredString(fontRendererObj, String.format("Do you wish to continue loading?"), width / 2, 110,
				16777215);
		int k;

		for (k = 0; k < buttonList.size(); ++k) {
			((GuiButton) buttonList.get(k)).drawButton(mc, p_73863_1_, p_73863_2_);
		}

		for (k = 0; k < labelList.size(); ++k) {
			((GuiLabel) labelList.get(k)).func_146159_a(mc, p_73863_1_, p_73863_2_);
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 1) {
			ObfuscationReflectionHelper.setPrivateValue(GuiSelectWorld.class, (GuiSelectWorld) parentScreen, false,
					"field_" + "146634_i");
			FMLClientHandler.instance().showGuiScreen(parent);
		} else {
			FMLLog.info("Capturing current state of world %s into file %s", saveName, zip.getAbsolutePath());
			try {
				String skip = System.getProperty("fml.doNotBackup");
				if (skip == null || !"true".equals(skip)) {
					ZipperUtil.zip(new File(FMLClientHandler.instance().getSavesDir(), dirName), zip);
				} else {
					for (int x = 0; x < 10; x++) {
						FMLLog.severe("!!!!!!!!!! UPDATING WORLD WITHOUT DOING BACKUP !!!!!!!!!!!!!!!!");
					}
				}
			} catch (IOException e) {
				FMLLog.log(Level.WARN, e, "There was a problem saving the backup %s. Please fix and try again",
						zip.getName());
				FMLClientHandler.instance().showGuiScreen(new GuiBackupFailed(parent, zip));
				return;
			}
			FMLClientHandler.instance().showGuiScreen(null);

			try {
				mc.launchIntegratedServer(dirName, saveName, null);
			} catch (StartupQuery.AbortedException e) {
				// ignore
			}
		}
	}
}