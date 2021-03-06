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
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.renderer.Tessellator;

import java.util.ArrayList;

/**
 * @author cpw
 *
 */
public class GuiSlotModList extends GuiScrollingList {
	private final GuiModList parent;
	private final ArrayList<ModContainer> mods;

	public GuiSlotModList(GuiModList parent, ArrayList<ModContainer> mods, int listWidth) {
		super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 66 + 4, 10, 35);
		this.parent = parent;
		this.mods = mods;
	}

	@Override
	protected int getSize() {
		return mods.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		parent.selectModIndex(var1);
	}

	@Override
	protected boolean isSelected(int var1) {
		return parent.modIndexSelected(var1);
	}

	@Override
	protected void drawBackground() {
		parent.drawDefaultBackground();
	}

	@Override
	protected int getContentHeight() {
		return getSize() * 35 + 1;
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
		ModContainer mc = mods.get(listIndex);
		if (Loader.instance().getModState(mc) == ModState.DISABLED) {
			parent.getFontRenderer().drawString(
					parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3, var3 + 2,
					0xFF2222);
			parent.getFontRenderer().drawString(
					parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3,
					var3 + 12, 0xFF2222);
			parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth("DISABLED", listWidth - 10),
					left + 3, var3 + 22, 0xFF2222);
		} else {
			parent.getFontRenderer().drawString(
					parent.getFontRenderer().trimStringToWidth(mc.getName(), listWidth - 10), left + 3, var3 + 2,
					0xFFFFFF);
			parent.getFontRenderer().drawString(
					parent.getFontRenderer().trimStringToWidth(mc.getDisplayVersion(), listWidth - 10), left + 3,
					var3 + 12, 0xCCCCCC);
			parent.getFontRenderer()
					.drawString(parent.getFontRenderer().trimStringToWidth(
							mc.getMetadata() != null ? mc.getMetadata().getChildModCountString() : "Metadata not found",
							listWidth - 10), left + 3, var3 + 22, 0xCCCCCC);
		}
	}

}