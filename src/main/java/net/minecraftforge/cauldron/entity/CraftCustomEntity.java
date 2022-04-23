package net.minecraftforge.cauldron.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity extends CraftEntity {

	public Class<? extends Entity> entityClass;
	public String entityName;

	public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
		super(server, entity);
		entityClass = entity.getClass();
		entityName = EntityRegistry.instance().getCustomEntityTypeName(entityClass);
		if (entityName == null) {
			entityName = entity.getCommandSenderName();
		}
	}

	@Override
	public net.minecraft.entity.Entity getHandle() {
		return entity;
	}

	@Override
	public String toString() {
		return entityName;
	}

	@Override
	public EntityType getType() {
		EntityType type = EntityType.fromName(entityName);
		if (type != null)
			return type;
		else
			return EntityType.UNKNOWN;
	}
}