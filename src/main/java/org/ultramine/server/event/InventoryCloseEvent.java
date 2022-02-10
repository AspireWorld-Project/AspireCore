package org.ultramine.server.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayerMP;

public class InventoryCloseEvent extends Event {
	public final EntityPlayerMP player;

	public InventoryCloseEvent(EntityPlayerMP player) {
		this.player = player;
	}
}
