package net.minecraftforge.event.terraingen;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.gen.MapGenBase;

public class InitMapGenEvent extends Event {
	/**
	 * Use CUSTOM to filter custom event types
	 */
	public enum EventType {
		CAVE, MINESHAFT, NETHER_BRIDGE, NETHER_CAVE, RAVINE, SCATTERED_FEATURE, STRONGHOLD, VILLAGE, CUSTOM
	}

	public final EventType type;
	public final MapGenBase originalGen;
	public MapGenBase newGen;

	InitMapGenEvent(EventType type, MapGenBase original) {
		this.type = type;
		originalGen = original;
		newGen = original;
	}
}
