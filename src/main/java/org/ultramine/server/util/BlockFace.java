package org.ultramine.server.util;

import java.util.EnumMap;

public enum BlockFace {
	NORTH(0, 0, -1), EAST(1, 0, 0), SOUTH(0, 0, 1), WEST(-1, 0, 0), UP(0, 1, 0), DOWN(0, -1, 0), NORTH_EAST(NORTH,
			EAST), NORTH_WEST(NORTH, WEST), SOUTH_EAST(SOUTH, EAST), SOUTH_WEST(SOUTH, WEST), WEST_NORTH_WEST(WEST,
					NORTH_WEST), NORTH_NORTH_WEST(NORTH, NORTH_WEST), NORTH_NORTH_EAST(NORTH,
							NORTH_EAST), EAST_NORTH_EAST(EAST, NORTH_EAST), EAST_SOUTH_EAST(EAST,
									SOUTH_EAST), SOUTH_SOUTH_EAST(SOUTH, SOUTH_EAST), SOUTH_SOUTH_WEST(SOUTH,
											SOUTH_WEST), WEST_SOUTH_WEST(WEST, SOUTH_WEST), SELF(0, 0, 0);

	private final int modX;
	private final int modY;
	private final int modZ;

	BlockFace(final int modX, final int modY, final int modZ) {
		this.modX = modX;
		this.modY = modY;
		this.modZ = modZ;
	}

	BlockFace(final BlockFace face1, final BlockFace face2) {
		modX = face1.getModX() + face2.getModX();
		modY = face1.getModY() + face2.getModY();
		modZ = face1.getModZ() + face2.getModZ();
	}

	public int getModX() {
		return modX;
	}

	public int getModY() {
		return modY;
	}

	public int getModZ() {
		return modZ;
	}

	public BlockFace getOppositeFace() {
		switch (this) {
		case NORTH:
			return BlockFace.SOUTH;

		case SOUTH:
			return BlockFace.NORTH;

		case EAST:
			return BlockFace.WEST;

		case WEST:
			return BlockFace.EAST;

		case UP:
			return BlockFace.DOWN;

		case DOWN:
			return BlockFace.UP;

		case NORTH_EAST:
			return BlockFace.SOUTH_WEST;

		case NORTH_WEST:
			return BlockFace.SOUTH_EAST;

		case SOUTH_EAST:
			return BlockFace.NORTH_WEST;

		case SOUTH_WEST:
			return BlockFace.NORTH_EAST;

		case WEST_NORTH_WEST:
			return BlockFace.EAST_SOUTH_EAST;

		case NORTH_NORTH_WEST:
			return BlockFace.SOUTH_SOUTH_EAST;

		case NORTH_NORTH_EAST:
			return BlockFace.SOUTH_SOUTH_WEST;

		case EAST_NORTH_EAST:
			return BlockFace.WEST_SOUTH_WEST;

		case EAST_SOUTH_EAST:
			return BlockFace.WEST_NORTH_WEST;

		case SOUTH_SOUTH_EAST:
			return BlockFace.NORTH_NORTH_WEST;

		case SOUTH_SOUTH_WEST:
			return BlockFace.NORTH_NORTH_EAST;

		case WEST_SOUTH_WEST:
			return BlockFace.EAST_NORTH_EAST;

		case SELF:
			return BlockFace.SELF;
		}

		return BlockFace.SELF;
	}

	public BlockFace rotate(int notchCount) {
		return notchToFace(faceToNotch(this) + notchCount);
	}

	private static final BlockFace[] AXIS = new BlockFace[4];
	private static final BlockFace[] RADIAL = { SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST,
			SOUTH_EAST };
	private static final EnumMap<BlockFace, Integer> NOTCHES = new EnumMap<>(BlockFace.class);

	static {
		for (int i = 0; i < RADIAL.length; i++) {
			NOTCHES.put(RADIAL[i], i);
		}
		for (int i = 0; i < AXIS.length; i++) {
			AXIS[i] = RADIAL[i << 1];
		}
	}

	public static BlockFace notchToFace(int notch) {
		return RADIAL[notch & 0x7];
	}

	public static int faceToNotch(BlockFace face) {
		return NOTCHES.get(face);
	}

	public static BlockFace yawToFace(float yaw) {
		return yawToFace(yaw, true);
	}

	public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
		if (useSubCardinalDirections)
			return RADIAL[Math.round(yaw / 45f) & 0x7];
		else
			return AXIS[Math.round(yaw / 90f) & 0x3];
	}
}
