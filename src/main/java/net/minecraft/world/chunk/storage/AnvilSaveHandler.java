package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

import java.io.File;

public class AnvilSaveHandler extends SaveHandler {
	public AnvilSaveHandler(File p_i2142_1_, String p_i2142_2_, boolean p_i2142_3_) {
		super(p_i2142_1_, p_i2142_2_, p_i2142_3_);
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
		File file1 = getWorldDirectory();
		File file2;

		if (!isSingleStorage && p_75763_1_.getSaveFolder() != null) {
			file2 = new File(file1, p_75763_1_.getSaveFolder());
			file2.mkdirs();
			return new AnvilChunkLoader(file2);
		} else
			return new AnvilChunkLoader(file1);
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
		p_75755_1_.setSaveVersion(19133);
		super.saveWorldInfoWithPlayer(p_75755_1_, p_75755_2_);
	}

	@Override
	public void flush() {
		try {
			ThreadedFileIOBase.threadedIOInstance.waitForFinish();
		} catch (InterruptedException interruptedexception) {
			interruptedexception.printStackTrace();
		}

		RegionFileCache.clearRegionFileReferences();
	}

	/*
	 * ======================================== ULTRAMINE START
	 * =====================================
	 */

	private boolean isSingleStorage;

	public void setSingleStorage() {
		isSingleStorage = true;
	}
}