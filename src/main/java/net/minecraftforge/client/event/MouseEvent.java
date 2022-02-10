package net.minecraftforge.client.event;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * Author: MachineMuse (Claire Semple) Created: 2:46 PM, 9/4/13
 */
@Cancelable
public class MouseEvent extends Event {
	public final int x;
	public final int y;
	public final int dx;
	public final int dy;
	public final int dwheel;
	public final int button;
	public final boolean buttonstate;
	public final long nanoseconds;

	public MouseEvent() {
		x = Mouse.getEventX();
		y = Mouse.getEventY();
		dx = Mouse.getEventDX();
		dy = Mouse.getEventDY();
		dwheel = Mouse.getEventDWheel();
		button = Mouse.getEventButton();
		buttonstate = Mouse.getEventButtonState();
		nanoseconds = Mouse.getEventNanoseconds();
	}
}
