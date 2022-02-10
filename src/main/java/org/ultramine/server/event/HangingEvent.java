package org.ultramine.server.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.DamageSource;

@Cancelable
public class HangingEvent extends Event {
	public final EntityHanging entity;

	public HangingEvent(EntityHanging entity) {
		this.entity = entity;
	}

	public static class HangingBreakEvent extends HangingEvent {
		public final DamageSource source;

		public HangingBreakEvent(EntityHanging entity, DamageSource source) {
			super(entity);
			this.source = source;
		}
	}
}
