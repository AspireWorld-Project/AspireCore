package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

/**
 * Called when a block causes an entity to combust.
 */
public class EntityCombustByBlockEvent extends EntityCombustEvent {
	private final Block combuster;

	public EntityCombustByBlockEvent(final Block combuster, final Entity combustee, final int duration) {
		super(combustee, duration);
		this.combuster = combuster;
	}

	/**
	 * The combuster can be lava or a block that is on fire.
	 * <p>
	 * WARNING: block may be null.
	 *
	 * @return the Block that set the combustee alight.
	 */
	@Nullable
	public Block getCombuster() {
		return combuster;
	}
}