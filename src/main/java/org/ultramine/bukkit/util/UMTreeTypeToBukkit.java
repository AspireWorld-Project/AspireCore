package org.ultramine.bukkit.util;

import org.bukkit.TreeType;
import org.ultramine.core.util.UMTreeType;

public class UMTreeTypeToBukkit {
	public static TreeType getBukkitTreeType(UMTreeType umTreeType) {
		if (umTreeType == null)
			return null;
		switch (umTreeType) {
		case TREE:
			return TreeType.TREE;
		case BIG_TREE:
			return TreeType.BIG_TREE;
		case BIRCH:
			return TreeType.BIRCH;
		case SWAMP:
			return TreeType.SWAMP;
		case ACACIA:
			return TreeType.ACACIA;
		case JUNGLE:
			return TreeType.JUNGLE;
		case REDWOOD:
			return TreeType.REDWOOD;
		case DARK_OAK:
			return TreeType.DARK_OAK;
		case COCOA_TREE:
			return TreeType.COCOA_TREE;
		case TALL_BIRCH:
			return TreeType.TALL_BIRCH;
		case JUNGLE_BUSH:
			return TreeType.JUNGLE_BUSH;
		case MEGA_REDWOOD:
			return TreeType.MEGA_REDWOOD;
		case RED_MUSHROOM:
			return TreeType.RED_MUSHROOM;
		case SMALL_JUNGLE:
			return TreeType.SMALL_JUNGLE;
		case TALL_REDWOOD:
			return TreeType.TALL_REDWOOD;
		case BROWN_MUSHROOM:
			return TreeType.BROWN_MUSHROOM;
		default:
			return null;
		}
	}
}
