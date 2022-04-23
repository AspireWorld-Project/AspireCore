/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.client;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.WrongMinecraftVersionException;
import net.minecraft.client.gui.GuiErrorScreen;

public class GuiWrongMinecraft extends GuiErrorScreen {
	private final WrongMinecraftVersionException wrongMC;

	public GuiWrongMinecraft(WrongMinecraftVersionException wrongMC) {
		super(null, null);
		this.wrongMC = wrongMC;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int offset = 75;
		drawCenteredString(fontRendererObj, "Forge Mod Loader has found a problem with your minecraft installation",
				width / 2, offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj,
				String.format("The mod listed below does not want to run in Minecraft version %s",
						Loader.instance().getMinecraftModContainer().getVersion()),
				width / 2, offset, 0xFFFFFF);
		offset += 5;
		offset += 10;
		drawCenteredString(
				fontRendererObj, String.format("%s (%s) wants Minecraft %s", wrongMC.mod.getName(),
						wrongMC.mod.getModId(), wrongMC.mod.acceptableMinecraftVersionRange()),
				width / 2, offset, 0xEEEEEE);
		offset += 20;
		drawCenteredString(fontRendererObj, "The file 'ForgeModLoader-client-0.log' contains more information",
				width / 2, offset, 0xFFFFFF);
	}
}