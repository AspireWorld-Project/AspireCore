package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.GameSettings;

@SideOnly(Side.CLIENT)
public class MovementInputFromOptions extends MovementInput {
	private final GameSettings gameSettings;
	private static final String __OBFID = "CL_00000937";

	public MovementInputFromOptions(GameSettings p_i1237_1_) {
		gameSettings = p_i1237_1_;
	}

	@Override
	public void updatePlayerMoveState() {
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (gameSettings.keyBindForward.getIsKeyPressed()) {
			++moveForward;
		}

		if (gameSettings.keyBindBack.getIsKeyPressed()) {
			--moveForward;
		}

		if (gameSettings.keyBindLeft.getIsKeyPressed()) {
			++moveStrafe;
		}

		if (gameSettings.keyBindRight.getIsKeyPressed()) {
			--moveStrafe;
		}

		jump = gameSettings.keyBindJump.getIsKeyPressed();
		sneak = gameSettings.keyBindSneak.getIsKeyPressed();

		if (sneak) {
			moveStrafe = (float) (moveStrafe * 0.3D);
			moveForward = (float) (moveForward * 0.3D);
		}
	}
}