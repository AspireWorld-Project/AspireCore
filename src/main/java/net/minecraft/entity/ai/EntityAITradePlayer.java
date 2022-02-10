package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayer extends EntityAIBase {
	private EntityVillager villager;
	private static final String __OBFID = "CL_00001617";

	public EntityAITradePlayer(EntityVillager p_i1658_1_) {
		villager = p_i1658_1_;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!villager.isEntityAlive())
			return false;
		else if (villager.isInWater())
			return false;
		else if (!villager.onGround)
			return false;
		else if (villager.velocityChanged)
			return false;
		else {
			EntityPlayer entityplayer = villager.getCustomer();
			return entityplayer == null ? false
					: villager.getDistanceSqToEntity(entityplayer) > 16.0D ? false
							: entityplayer.openContainer instanceof Container;
		}
	}

	@Override
	public void startExecuting() {
		villager.getNavigator().clearPathEntity();
	}

	@Override
	public void resetTask() {
		villager.setCustomer((EntityPlayer) null);
	}
}