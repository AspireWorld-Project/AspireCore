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

import cpw.mods.fml.common.MissingModsException;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiModsMissingForServer extends GuiScreen {
	private final MissingModsException modsMissing;

	public GuiModsMissingForServer(MissingModsException modsMissing) {
		this.modsMissing = modsMissing;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(1, width / 2 - 75, height - 38, I18n.format("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton p_73875_1_) {
		if (p_73875_1_.enabled && p_73875_1_.id == 1) {
			FMLClientHandler.instance().showGuiScreen(null);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int offset = Math.max(85 - modsMissing.missingMods.size() * 10, 10);
		drawCenteredString(fontRendererObj, "Forge Mod Loader could not connect to this server", width / 2, offset,
				0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "The mods and versions listed below could not be found", width / 2, offset,
				0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "They are required to play on this server", width / 2, offset, 0xFFFFFF);
		offset += 5;
		for (ArtifactVersion v : modsMissing.missingMods) {
			offset += 10;
			drawCenteredString(fontRendererObj, String.format("%s : %s", v.getLabel(), v.getRangeString()), width / 2,
					offset, 0xEEEEEE);
		}
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}