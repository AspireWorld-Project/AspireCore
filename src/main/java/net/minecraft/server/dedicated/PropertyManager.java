package net.minecraft.server.dedicated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class PropertyManager {
	private static final Logger field_164440_a = LogManager.getLogger();
	private final Properties serverProperties = new Properties();
	private final File serverPropertiesFile;
	private static final String __OBFID = "CL_00001782";

	public PropertyManager(File p_i45278_1_) {
		serverPropertiesFile = p_i45278_1_;

		if (p_i45278_1_.exists()) {
			FileInputStream fileinputstream = null;

			try {
				fileinputstream = new FileInputStream(p_i45278_1_);
				serverProperties.load(fileinputstream);
			} catch (Exception exception) {
				field_164440_a.warn("Failed to load " + p_i45278_1_, exception);
				generateNewProperties();
			} finally {
				if (fileinputstream != null) {
					try {
						fileinputstream.close();
					} catch (IOException ioexception) {
						;
					}
				}
			}
		} else {
			field_164440_a.warn(p_i45278_1_ + " does not exist");
			generateNewProperties();
		}
	}

	public void generateNewProperties() {
		field_164440_a.info("Generating new properties file");
		saveProperties();
	}

	public void saveProperties() {
		FileOutputStream fileoutputstream = null;

		try {
			fileoutputstream = new FileOutputStream(serverPropertiesFile);
			serverProperties.store(fileoutputstream, "Minecraft server properties");
		} catch (Exception exception) {
			field_164440_a.warn("Failed to save " + serverPropertiesFile, exception);
			generateNewProperties();
		} finally {
			if (fileoutputstream != null) {
				try {
					fileoutputstream.close();
				} catch (IOException ioexception) {
					;
				}
			}
		}
	}

	public File getPropertiesFile() {
		return serverPropertiesFile;
	}

	public String getStringProperty(String p_73671_1_, String p_73671_2_) {
		if (!serverProperties.containsKey(p_73671_1_)) {
			serverProperties.setProperty(p_73671_1_, p_73671_2_);
			saveProperties();
			saveProperties();
		}

		return serverProperties.getProperty(p_73671_1_, p_73671_2_);
	}

	public int getIntProperty(String p_73669_1_, int p_73669_2_) {
		try {
			return Integer.parseInt(getStringProperty(p_73669_1_, "" + p_73669_2_));
		} catch (Exception exception) {
			serverProperties.setProperty(p_73669_1_, "" + p_73669_2_);
			saveProperties();
			return p_73669_2_;
		}
	}

	public boolean getBooleanProperty(String p_73670_1_, boolean p_73670_2_) {
		try {
			return Boolean.parseBoolean(getStringProperty(p_73670_1_, "" + p_73670_2_));
		} catch (Exception exception) {
			serverProperties.setProperty(p_73670_1_, "" + p_73670_2_);
			saveProperties();
			return p_73670_2_;
		}
	}

	public void setProperty(String p_73667_1_, Object p_73667_2_) {
		serverProperties.setProperty(p_73667_1_, "" + p_73667_2_);
	}
}