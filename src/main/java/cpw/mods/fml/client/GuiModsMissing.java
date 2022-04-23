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
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import net.minecraft.client.gui.GuiErrorScreen;

public class GuiModsMissing extends GuiErrorScreen {

	private final MissingModsException modsMissing;

	public GuiModsMissing(MissingModsException modsMissing) {
		super(null, null);
		this.modsMissing = modsMissing;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int offset = Math.max(85 - modsMissing.missingMods.size() * 10, 10);
		drawCenteredString(fontRendererObj, "Forge Mod Loader has found a problem with your minecraft installation",
				width / 2, offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "The mods and versions listed below could not be found", width / 2, offset,
				0xFFFFFF);
		offset += 5;
		for (ArtifactVersion v : modsMissing.missingMods) {
			offset += 10;
			if (v instanceof DefaultArtifactVersion) {
				DefaultArtifactVersion dav = (DefaultArtifactVersion) v;
				if (dav.getRange() != null && dav.getRange().isUnboundedAbove()) {
					drawCenteredString(fontRendererObj, String.format("%s : minimum version required is %s",
							v.getLabel(), dav.getRange().getLowerBoundString()), width / 2, offset, 0xEEEEEE);
					continue;
				}
			}
			drawCenteredString(fontRendererObj, String.format("%s : %s", v.getLabel(), v.getRangeString()), width / 2,
					offset, 0xEEEEEE);
		}
		offset += 20;
		drawCenteredString(fontRendererObj, "The file 'logs/fml-client-latest.log' contains more information",
				width / 2, offset, 0xFFFFFF);
	}
}