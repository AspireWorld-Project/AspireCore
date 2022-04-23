package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public class CraftProjectile extends AbstractProjectile implements Projectile { // Cauldron - concrete
	public CraftProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
		super(server, entity);
	}

	@Override
	public ProjectileSource getShooter() {
		return getHandle().getProjectileSource();
	}

	@Override
	public void setShooter(ProjectileSource shooter) {
		if (shooter instanceof CraftLivingEntity) {
			getHandle().setThrower((net.minecraft.entity.EntityLivingBase) ((CraftLivingEntity) shooter).entity);
			if (shooter instanceof CraftHumanEntity) {
				getHandle().setThrowerName(((CraftHumanEntity) shooter).getName());
			}
		} else {
			getHandle().setThrower(null);
			getHandle().setThrowerName(null);
		}
		getHandle().setProjectileSource(shooter);
	}

	@Override
	public net.minecraft.entity.projectile.EntityThrowable getHandle() {
		return (net.minecraft.entity.projectile.EntityThrowable) entity;
	}

	@Override
	public String toString() {
		return "CraftProjectile";
	}

	// Cauldron start
	@Override
	public EntityType getType() {
		return EntityType.UNKNOWN;
	}
	// Cauldron end

	@Override
	@Deprecated
	public LivingEntity _INVALID_getShooter() {
		EntityLivingBase thrower = getHandle().getMixinThrower();
		if (thrower == null)
			return null;
		return (LivingEntity) thrower.getBukkitEntity();
	}

	@Override
	@Deprecated
	public void _INVALID_setShooter(LivingEntity shooter) {
		if (shooter == null)
			return;
		getHandle().setThrower(((CraftLivingEntity) shooter).getHandle());
		if (shooter instanceof CraftHumanEntity) {
			getHandle().setThrowerName(((CraftHumanEntity) shooter).getName());
		}
	}
}
