package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

@SideOnly(Side.CLIENT)
public class MouseHelper {
	public int deltaX;
	public int deltaY;
	public void grabMouseCursor() {
		if (Boolean.parseBoolean(System.getProperty("fml.noGrab", "false")))
			return;
		Mouse.setGrabbed(true);
		deltaX = 0;
		deltaY = 0;
	}

	public void ungrabMouseCursor() {
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		Mouse.setGrabbed(false);
	}

	public void mouseXYChange() {
		deltaX = Mouse.getDX();
		deltaY = Mouse.getDY();
	}
}