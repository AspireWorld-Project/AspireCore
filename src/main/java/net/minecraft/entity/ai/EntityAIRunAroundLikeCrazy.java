package net.minecraft.entity.ai;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class EntityAIRunAroundLikeCrazy extends EntityAIBase {
	private EntityHorse horseHost;
	private double field_111178_b;
	private double field_111179_c;
	private double field_111176_d;
	private double field_111177_e;
	private static final String __OBFID = "CL_00001612";

	public EntityAIRunAroundLikeCrazy(EntityHorse p_i1653_1_, double p_i1653_2_) {
		horseHost = p_i1653_1_;
		field_111178_b = p_i1653_2_;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!horseHost.isTame() && horseHost.riddenByEntity != null) {
			Vec3 vec3 = RandomPositionGenerator.findRandomTarget(horseHost, 5, 4);

			if (vec3 == null)
				return false;
			else {
				field_111179_c = vec3.xCoord;
				field_111176_d = vec3.yCoord;
				field_111177_e = vec3.zCoord;
				return true;
			}
		} else
			return false;
	}

	@Override
	public void startExecuting() {
		horseHost.getNavigator().tryMoveToXYZ(field_111179_c, field_111176_d, field_111177_e, field_111178_b);
	}

	@Override
	public boolean continueExecuting() {
		return !horseHost.getNavigator().noPath() && horseHost.riddenByEntity != null;
	}

	@Override
	public void updateTask() {
		if (horseHost.getRNG().nextInt(50) == 0) {
			if (horseHost.riddenByEntity instanceof EntityPlayer) {
				int i = horseHost.getTemper();
				int j = horseHost.getMaxTemper();
				if (j > 0
						&& horseHost.getRNG().nextInt(j) < i && !CraftEventFactory
								.callEntityTameEvent(horseHost, (EntityPlayer) horseHost.riddenByEntity).isCancelled()
						&& horseHost.riddenByEntity instanceof EntityPlayer) {
					horseHost.setTamedBy((EntityPlayer) horseHost.riddenByEntity);
					horseHost.worldObj.setEntityState(horseHost, (byte) 7);
					return;
				}
				horseHost.increaseTemper(5);
			}
			horseHost.riddenByEntity.mountEntity(null);
			horseHost.riddenByEntity = null;
			horseHost.makeHorseRearWithSound();
			horseHost.worldObj.setEntityState(horseHost, (byte) 6);
		}
	}
}