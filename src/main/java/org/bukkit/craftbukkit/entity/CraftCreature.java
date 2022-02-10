package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;

public class CraftCreature extends CraftLivingEntity implements Creature {
	public CraftCreature(CraftServer server, EntityCreature entity) {
		super(server, entity);
	}

	@Override
	public void setTarget(LivingEntity target) {
		EntityCreature entity = getHandle();
		if (target == null) {
			entity.setEntityToAttack(null);
		} else if (target instanceof CraftLivingEntity) {
			entity.setEntityToAttack(((CraftLivingEntity) target).getHandle());
			entity.setPathToEntity(entity.worldObj.getPathEntityToEntity(entity, entity.getEntityToAttack(), 16.0F,
					true, false, false, true));
		}
	}

	@Override
	public CraftLivingEntity getTarget() {
		EntityCreature entity = getHandle();
		if (entity.getEntityToAttack() == null)
			return null;
		if (!(entity.getEntityToAttack() instanceof EntityLivingBase))
			return null;

		return (CraftLivingEntity) entity.getEntityToAttack().getBukkitEntity();
	}

	@Override
	public EntityCreature getHandle() {
		return (EntityCreature) entity;
	}

	@Override
	public String toString() {
		return entityName; // Cauldron
	}
}
