package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Represents an event that is called when a player right clicks an entity.
 */
public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected Entity clickedEntity;
	boolean cancelled = false;

	public PlayerInteractEntityEvent(final Player who, final Entity clickedEntity) {
		super(who);
		this.clickedEntity = clickedEntity;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	/**
	 * Gets the entity that was rightclicked by the player.
	 *
	 * @return entity right clicked by player
	 */
	public Entity getRightClicked() {
		return clickedEntity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}