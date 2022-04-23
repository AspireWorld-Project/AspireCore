package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementInput {
	public float moveStrafe;
	public float moveForward;
	public boolean jump;
	public boolean sneak;
	public void updatePlayerMoveState() {
	}
}