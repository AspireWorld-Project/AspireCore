package org.bukkit.entity;

import org.bukkit.util.Vector;

/**
 * Represents a minecart entity.
 */
public interface Minecart extends Vehicle {

	/**
	 * This method exists for legacy reasons to provide backwards compatibility. It
	 * will not exist at runtime and should not be used under any circumstances.
	 */
	@Deprecated
    void _INVALID_setDamage(int damage);

	/**
	 * Sets a minecart's damage.
	 *
	 * @param damage
	 *            over 40 to "kill" a minecart
	 */
    void setDamage(double damage);

	/**
	 * This method exists for legacy reasons to provide backwards compatibility. It
	 * will not exist at runtime and should not be used under any circumstances.
	 */
	@Deprecated
    int _INVALID_getDamage();

	/**
	 * Gets a minecart's damage.
	 *
	 * @return The damage
	 */
    double getDamage();

	/**
	 * Gets the maximum speed of a minecart. The speed is unrelated to the velocity.
	 *
	 * @return The max speed
	 */
    double getMaxSpeed();

	/**
	 * Sets the maximum speed of a minecart. Must be nonnegative. Default is 0.4D.
	 *
	 * @param speed
	 *            The max speed
	 */
    void setMaxSpeed(double speed);

	/**
	 * Returns whether this minecart will slow down faster without a passenger
	 * occupying it
	 *
	 * @return Whether it decelerates faster
	 */
    boolean isSlowWhenEmpty();

	/**
	 * Sets whether this minecart will slow down faster without a passenger
	 * occupying it
	 *
	 * @param slow
	 *            Whether it will decelerate faster
	 */
    void setSlowWhenEmpty(boolean slow);

	/**
	 * Gets the flying velocity modifier. Used for minecarts that are in mid-air. A
	 * flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @return The vector factor
	 */
    Vector getFlyingVelocityMod();

	/**
	 * Sets the flying velocity modifier. Used for minecarts that are in mid-air. A
	 * flying minecart's velocity is multiplied by this factor each tick.
	 *
	 * @param flying
	 *            velocity modifier vector
	 */
    void setFlyingVelocityMod(Vector flying);

	/**
	 * Gets the derailed velocity modifier. Used for minecarts that are on the
	 * ground, but not on rails.
	 * <p>
	 * A derailed minecart's velocity is multiplied by this factor each tick.
	 *
	 * @return derailed visible speed
	 */
    Vector getDerailedVelocityMod();

	/**
	 * Sets the derailed velocity modifier. Used for minecarts that are on the
	 * ground, but not on rails. A derailed minecart's velocity is multiplied by
	 * this factor each tick.
	 *
	 * @param derailed
	 *            visible speed
	 */
    void setDerailedVelocityMod(Vector derailed);
}