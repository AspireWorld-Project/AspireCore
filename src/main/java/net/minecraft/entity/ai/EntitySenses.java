package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import java.util.ArrayList;
import java.util.List;

public class EntitySenses {
	EntityLiving entityObj;
	List seenEntities = new ArrayList();
	List unseenEntities = new ArrayList();
	private static final String __OBFID = "CL_00001628";

	public EntitySenses(EntityLiving p_i1672_1_) {
		entityObj = p_i1672_1_;
	}

	public void clearSensingCache() {
		seenEntities.clear();
		unseenEntities.clear();
	}

	public boolean canSee(Entity p_75522_1_) {
		if (seenEntities.contains(p_75522_1_))
			return true;
		else if (unseenEntities.contains(p_75522_1_))
			return false;
		else {
			entityObj.worldObj.theProfiler.startSection("canSee");
			boolean flag = entityObj.canEntityBeSeen(p_75522_1_);
			entityObj.worldObj.theProfiler.endSection();

			if (flag) {
				seenEntities.add(p_75522_1_);
			} else {
				unseenEntities.add(p_75522_1_);
			}

			return flag;
		}
	}
}