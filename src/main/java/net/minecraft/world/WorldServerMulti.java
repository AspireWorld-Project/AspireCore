package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer {
	private static final String __OBFID = "CL_00001430";
	public boolean isSplitted;

	public WorldServerMulti(MinecraftServer p_i45283_1_, ISaveHandler p_i45283_2_, String p_i45283_3_, int p_i45283_4_,
			WorldSettings p_i45283_5_, WorldServer p_i45283_6_, Profiler p_i45283_7_) {
		super(p_i45283_1_, p_i45283_2_, p_i45283_3_, p_i45283_4_, p_i45283_5_, p_i45283_7_);
		mapStorage = p_i45283_6_.mapStorage;
		worldScoreboard = p_i45283_6_.getScoreboard();
		// this.worldInfo = new DerivedWorldInfo(p_i45283_6_.getWorldInfo());
	}

	// CraftBukkit start - Add Environment and ChunkGenerator arguments
	public WorldServerMulti(MinecraftServer p_i45283_1_, ISaveHandler p_i45283_2_, String p_i45283_3_, int p_i45283_4_, WorldSettings p_i45283_5_, WorldServer p_i45283_6_, Profiler p_i45283_7_, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen)
	{
		super(p_i45283_1_, p_i45283_2_, p_i45283_3_, p_i45283_4_, p_i45283_5_, p_i45283_7_, env, gen);
		// CraftBukkit end
		this.mapStorage = p_i45283_6_.mapStorage;
		this.worldScoreboard = p_i45283_6_.getScoreboard();
		//this.worldInfo = new DerivedWorldInfo(p_i45283_6_.getWorldInfo());
		//this.worldInfo.setWorldName(p_i45283_3_);
	}


	@Override
	protected void saveLevel() throws MinecraftException {
		saveHandler.saveWorldInfoWithPlayer(worldInfo, null);
		perWorldStorage.saveAllData();
	}
}