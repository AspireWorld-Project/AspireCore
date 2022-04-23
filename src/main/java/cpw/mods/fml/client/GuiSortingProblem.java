package cpw.mods.fml.client;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.toposort.ModSortingException;
import cpw.mods.fml.common.toposort.ModSortingException.SortingExceptionData;
import net.minecraft.client.gui.GuiScreen;

public class GuiSortingProblem extends GuiScreen {
	private final SortingExceptionData<ModContainer> failedList;

	public GuiSortingProblem(ModSortingException modSorting) {
		failedList = modSorting.getExceptionData();
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int offset = Math.max(85 - (failedList.getVisitedNodes().size() + 3) * 10, 10);
		drawCenteredString(fontRendererObj, "Forge Mod Loader has found a problem with your minecraft installation",
				width / 2, offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "A mod sorting cycle was detected and loading cannot continue", width / 2,
				offset, 0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj,
				String.format("The first mod in the cycle is %s", failedList.getFirstBadNode()), width / 2, offset,
				0xFFFFFF);
		offset += 10;
		drawCenteredString(fontRendererObj, "The remainder of the cycle involves these mods", width / 2, offset,
				0xFFFFFF);
		offset += 5;
		for (ModContainer mc : failedList.getVisitedNodes()) {
			offset += 10;
			drawCenteredString(fontRendererObj, String.format("%s : before: %s, after: %s", mc.toString(),
					mc.getDependants(), mc.getDependencies()), width / 2, offset, 0xEEEEEE);
		}
		offset += 20;
		drawCenteredString(fontRendererObj, "The file 'ForgeModLoader-client-0.log' contains more information",
				width / 2, offset, 0xFFFFFF);
	}

}