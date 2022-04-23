package net.minecraft.world.gen.structure;

import net.minecraft.world.World;

import java.util.Random;

public class StructureMineshaftStart extends StructureStart {
	private static final String __OBFID = "CL_00000450";

	public StructureMineshaftStart() {
	}

	public StructureMineshaftStart(World p_i2039_1_, Random p_i2039_2_, int p_i2039_3_, int p_i2039_4_) {
		super(p_i2039_3_, p_i2039_4_);
		StructureMineshaftPieces.Room room = new StructureMineshaftPieces.Room(0, p_i2039_2_, (p_i2039_3_ << 4) + 2,
				(p_i2039_4_ << 4) + 2);
		components.add(room);
		room.buildComponent(room, components, p_i2039_2_);
		updateBoundingBox();
		markAvailableHeight(p_i2039_1_, p_i2039_2_, 10);
	}
}