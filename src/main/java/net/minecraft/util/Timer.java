package net.minecraft.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class Timer {
	float ticksPerSecond;
	private double lastHRTime;
	public int elapsedTicks;
	public float renderPartialTicks;
	public float timerSpeed = 1.0F;
	public float elapsedPartialTicks;
	private long lastSyncSysClock;
	private long lastSyncHRClock;
	private long field_74285_i;
	private double timeSyncAdjustment = 1.0D;
	public Timer(float p_i1018_1_) {
		ticksPerSecond = p_i1018_1_;
		lastSyncSysClock = Minecraft.getSystemTime();
		lastSyncHRClock = System.nanoTime() / 1000000L;
	}

	public void updateTimer() {
		long i = Minecraft.getSystemTime();
		long j = i - lastSyncSysClock;
		long k = System.nanoTime() / 1000000L;
		double d0 = k / 1000.0D;

		if (j <= 1000L && j >= 0L) {
			field_74285_i += j;

			if (field_74285_i > 1000L) {
				long l = k - lastSyncHRClock;
				double d1 = (double) field_74285_i / (double) l;
				timeSyncAdjustment += (d1 - timeSyncAdjustment) * 0.20000000298023224D;
				lastSyncHRClock = k;
				field_74285_i = 0L;
			}

			if (field_74285_i < 0L) {
				lastSyncHRClock = k;
			}
		} else {
			lastHRTime = d0;
		}

		lastSyncSysClock = i;
		double d2 = (d0 - lastHRTime) * timeSyncAdjustment;
		lastHRTime = d0;

		if (d2 < 0.0D) {
			d2 = 0.0D;
		}

		if (d2 > 1.0D) {
			d2 = 1.0D;
		}

		elapsedPartialTicks = (float) (elapsedPartialTicks + d2 * timerSpeed * ticksPerSecond);
		elapsedTicks = (int) elapsedPartialTicks;
		elapsedPartialTicks -= elapsedTicks;

		if (elapsedTicks > 10) {
			elapsedTicks = 10;
		}

		renderPartialTicks = elapsedPartialTicks;
	}
}