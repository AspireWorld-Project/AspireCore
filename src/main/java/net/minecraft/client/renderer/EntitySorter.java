package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

import java.util.Comparator;

@SideOnly(Side.CLIENT)
public class EntitySorter implements Comparator {
	private double entityPosX;
	private double entityPosY;
	private double entityPosZ;
	private static final String __OBFID = "CL_00000944";

	public EntitySorter(Entity p_i1242_1_) {
		entityPosX = -p_i1242_1_.posX;
		entityPosY = -p_i1242_1_.posY;
		entityPosZ = -p_i1242_1_.posZ;
	}

	public int compare(WorldRenderer p_compare_1_, WorldRenderer p_compare_2_) {
		double d0 = p_compare_1_.posXPlus + entityPosX;
		double d1 = p_compare_1_.posYPlus + entityPosY;
		double d2 = p_compare_1_.posZPlus + entityPosZ;
		double d3 = p_compare_2_.posXPlus + entityPosX;
		double d4 = p_compare_2_.posYPlus + entityPosY;
		double d5 = p_compare_2_.posZPlus + entityPosZ;
		return (int) ((d0 * d0 + d1 * d1 + d2 * d2 - (d3 * d3 + d4 * d4 + d5 * d5)) * 1024.0D);
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((WorldRenderer) p_compare_1_, (WorldRenderer) p_compare_2_);
	}
}