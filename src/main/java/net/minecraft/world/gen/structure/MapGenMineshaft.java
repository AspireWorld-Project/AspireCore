package net.minecraft.world.gen.structure;

import net.minecraft.util.MathHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapGenMineshaft extends MapGenStructure {
	private double field_82673_e = 0.004D;
	public MapGenMineshaft() {
	}

	@Override
	public String func_143025_a() {
		return "Mineshaft";
	}

	@SuppressWarnings("rawtypes")
	public MapGenMineshaft(Map p_i2034_1_) {
		Iterator iterator = p_i2034_1_.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();

			if (entry.getKey().equals("chance")) {
				field_82673_e = MathHelper.parseDoubleWithDefault((String) entry.getValue(), field_82673_e);
			}
		}
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
		return rand.nextDouble() < field_82673_e
				&& rand.nextInt(80) < Math.max(Math.abs(p_75047_1_), Math.abs(p_75047_2_));
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new StructureMineshaftStart(worldObj, rand, p_75049_1_, p_75049_2_);
	}
}