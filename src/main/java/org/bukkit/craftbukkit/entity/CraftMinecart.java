package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class CraftMinecart extends CraftVehicle implements Minecart { // Cauldron - concrete for modded minecarts
	public CraftMinecart(CraftServer server, net.minecraft.entity.item.EntityMinecart entity) {
		super(server, entity);
	}

	// Cauldron start
	@Override
	public org.bukkit.entity.EntityType getType() {
		return org.bukkit.entity.EntityType.MINECART;
	}
	// Cauldron end

	@Override
	public void setDamage(double damage) {
		getHandle().setDamage((float) damage);
	}

	@Override
	public double getDamage() {
		return getHandle().getDamage();
	}

	@Override
	public double getMaxSpeed() { // TODO
		// return getHandle().maxSpeed;
		return 0;
	}

	@Override
	public void setMaxSpeed(double speed) {
		// if (speed >= 0D) {
		// getHandle().maxSpeed = speed;
		// }
	}

	@Override
	public boolean isSlowWhenEmpty() {
		// return getHandle().slowWhenEmpty;
		return true;
	}

	@Override
	public void setSlowWhenEmpty(boolean slow) {
		// getHandle().slowWhenEmpty = slow;
	}

	@Override
	public Vector getFlyingVelocityMod() {
		// return getHandle().getFlyingVelocityMod();
		return new Vector();
	}

	@Override
	public void setFlyingVelocityMod(Vector flying) {
		// getHandle().setFlyingVelocityMod(flying);
	}

	@Override
	public Vector getDerailedVelocityMod() {
		// return getHandle().getDerailedVelocityMod();
		return new Vector();
	}

	@Override
	public void setDerailedVelocityMod(Vector derailed) {
		// getHandle().setDerailedVelocityMod(derailed);
	}

	@Override
	public net.minecraft.entity.item.EntityMinecart getHandle() {
		return (net.minecraft.entity.item.EntityMinecart) entity;
	}

	@Override
	@Deprecated
	public void _INVALID_setDamage(int damage) {
		setDamage(damage);
	}

	@Override
	@Deprecated
	public int _INVALID_getDamage() {
		return NumberConversions.ceil(getDamage());
	}
}
