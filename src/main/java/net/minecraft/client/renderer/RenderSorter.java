package net.minecraft.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;

import java.util.Comparator;

@SideOnly(Side.CLIENT)
public class RenderSorter implements Comparator {
	private final EntityLivingBase baseEntity;
	private static final String __OBFID = "CL_00000943";

	public RenderSorter(EntityLivingBase p_i1241_1_) {
		baseEntity = p_i1241_1_;
	}

	public int compare(WorldRenderer p_compare_1_, WorldRenderer p_compare_2_) {
		if (p_compare_1_.isInFrustum && !p_compare_2_.isInFrustum)
			return 1;
		else if (p_compare_2_.isInFrustum && !p_compare_1_.isInFrustum)
			return -1;
		else {
			double d0 = p_compare_1_.distanceToEntitySquared(baseEntity);
			double d1 = p_compare_2_.distanceToEntitySquared(baseEntity);
			return d0 < d1 ? 1 : d0 > d1 ? -1 : p_compare_1_.chunkIndex < p_compare_2_.chunkIndex ? 1 : -1;
		}
	}

	@Override
	public int compare(Object p_compare_1_, Object p_compare_2_) {
		return this.compare((WorldRenderer) p_compare_1_, (WorldRenderer) p_compare_2_);
	}
}