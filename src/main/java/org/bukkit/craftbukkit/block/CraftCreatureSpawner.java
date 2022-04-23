package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityMobSpawner;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
	private final TileEntityMobSpawner spawner;

	public CraftCreatureSpawner(final Block block) {
		super(block);

		spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	@Deprecated
	public CreatureType getCreatureType() {
		return CreatureType.fromName(spawner.func_145881_a().getEntityNameToSpawn());
	}

	@Override
	public EntityType getSpawnedType() {
		return EntityType.fromName(spawner.func_145881_a().getEntityNameToSpawn());
	}

	@Override
	@Deprecated
	public void setCreatureType(CreatureType creatureType) {
		spawner.func_145881_a().setEntityName(creatureType.getName());
	}

	@Override
	public void setSpawnedType(EntityType entityType) {
		if (entityType == null || entityType.getName() == null)
			throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");

		spawner.func_145881_a().setEntityName(entityType.getName());
	}

	@Override
	@Deprecated
	public String getCreatureTypeId() {
		return spawner.func_145881_a().getEntityNameToSpawn();
	}

	@Override
	@Deprecated
	public void setCreatureTypeId(String creatureName) {
		setCreatureTypeByName(creatureName);
	}

	@Override
	public String getCreatureTypeName() {
		return spawner.func_145881_a().getEntityNameToSpawn();
	}

	@Override
	public void setCreatureTypeByName(String creatureType) {
		// Verify input
		EntityType type = EntityType.fromName(creatureType);
		if (type == null)
			return;
		setSpawnedType(type);
	}

	@Override
	public int getDelay() {
		return spawner.func_145881_a().spawnDelay;
	}

	@Override
	public void setDelay(int delay) {
		spawner.func_145881_a().spawnDelay = delay;
	}

}
