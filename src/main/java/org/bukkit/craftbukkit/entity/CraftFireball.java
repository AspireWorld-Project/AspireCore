package org.bukkit.craftbukkit.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.MathHelper;

public class CraftFireball extends AbstractProjectile implements Fireball {
	public CraftFireball(CraftServer server, EntityFireball entity) {
		super(server, entity);
	}

	@Override
	public float getYield() {
		return getHandle().getBukkitYield();
	}

	@Override
	public boolean isIncendiary() {
		return getHandle().isIncendiary();
	}

	@Override
	public void setIsIncendiary(boolean isIncendiary) {
		getHandle().setIncendiary(isIncendiary);
	}

	@Override
	public void setYield(float yield) {
		getHandle().setBukkitYield(yield);
	}

	@Override
	public ProjectileSource getShooter() {
		return getHandle().getProjectileSource();
	}

	@Override
	public void setShooter(ProjectileSource shooter) {
		if (shooter instanceof CraftLivingEntity) {
			getHandle().shootingEntity = ((CraftLivingEntity) shooter).getHandle();
		} else {
			getHandle().shootingEntity = null;
		}
		getHandle().setProjectileSource(shooter);
	}

	@Override
	public Vector getDirection() {
		return new Vector(getHandle().accelerationX, getHandle().accelerationY, getHandle().accelerationZ);
	}

	@Override
	public void setDirection(Vector direction) {
		Validate.notNull(direction, "Direction can not be null");
		double x = direction.getX();
		double y = direction.getY();
		double z = direction.getZ();
		double magnitude = MathHelper.sqrt_double(x * x + y * y + z * z);
		getHandle().accelerationX = x / magnitude;
		getHandle().accelerationY = y / magnitude;
		getHandle().accelerationZ = z / magnitude;
	}

	@Override
	public EntityFireball getHandle() {
		return (EntityFireball) entity;
	}

	@Override
	public String toString() {
		return "CraftFireball";
	}

	@Override
	public EntityType getType() {
		return EntityType.UNKNOWN;
	}

	@Override
	@Deprecated
	public void _INVALID_setShooter(LivingEntity shooter) {
		setShooter(shooter);
	}

	@Override
	@Deprecated
	public LivingEntity _INVALID_getShooter() {
		if (getHandle().shootingEntity != null)
			return (LivingEntity) getHandle().shootingEntity.getBukkitEntity();
		return null;
	}
}
