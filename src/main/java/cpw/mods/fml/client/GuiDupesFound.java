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

import cpw.mods.fml.common.DuplicateModsFoundException;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.gui.GuiErrorScreen;

import java.io.File;
import java.util.Map.Entry;

public class GuiDupesFound extends GuiErrorScreen {

	private final DuplicateModsFoundException dupes;

	public GuiDupesFound(DuplicateModsFoundException dupes) {
		super(null, null);
		this.dupes = dupes;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int offset = Math.max(85 - dupes.dupes.size() * 10, 10);
		drawCenteredString(fontRendererObj, "Forge Mod Loader has found a problem with your minecraft installation",
				width / 2, offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "You have mod sources that are duplicate within your system", width / 2,
				offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "Mod Id : File name", width / 2, offset, 0xFFFFFF);
		offset += 5;
		for (Entry<ModContainer, File> mc : dupes.dupes.entries()) {
			offset += 10;
			drawCenteredString(fontRendererObj,
					String.format("%s : %s", mc.getKey().getModId(), mc.getValue().getName()), width / 2, offset,
					0xEEEEEE);
		}
	}
}