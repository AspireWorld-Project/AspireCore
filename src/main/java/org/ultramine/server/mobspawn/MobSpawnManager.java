package org.ultramine.server.mobspawn;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldServer;
import org.ultramine.server.WorldsConfig.WorldConfig;

@SideOnly(Side.SERVER)
public class MobSpawnManager {
	private final MobSpawner spawnerMonsters;
	private MobSpawner spawnerAnimals;
	private MobSpawner spawnerWater;
	private MobSpawner spawnerAmbient;

	public MobSpawnManager(WorldServer world) {
		spawnerMonsters = new MobSpawnerMonsters(world);
		if (!(world.provider instanceof WorldProviderEnd) && !(world.provider instanceof WorldProviderHell)) {
			spawnerAnimals = new MobSpawnerAnimals(world);
			spawnerWater = new MobSpawnerWater(world);
			spawnerAmbient = new MobSpawnerAmbient(world);
		}
	}

	public void configure(WorldConfig config) {
		spawnerMonsters.configure(config);
		if (spawnerAnimals != null) {
			spawnerAnimals.configure(config);
			spawnerWater.configure(config);
			spawnerAmbient.configure(config);
		}
	}

	public void performSpawn(boolean spawnMonsters, boolean spawnAnimals, long currentTick) {
		spawnerMonsters.performSpawn(currentTick);
		if (spawnerAnimals != null) {
			spawnerAnimals.performSpawn(currentTick);
			spawnerWater.performSpawn(currentTick);
			spawnerAmbient.performSpawn(currentTick);
		}
	}
}
