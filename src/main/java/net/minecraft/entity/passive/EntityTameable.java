package net.minecraft.entity.passive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
	protected EntityAISit aiSit = new EntityAISit(this);
	private static final String __OBFID = "CL_00001561";

	public EntityTameable(World p_i1604_1_) {
		super(p_i1604_1_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		dataWatcher.addObject(17, "");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (func_152113_b() == null) {
			p_70014_1_.setString("OwnerUUID", "");
		} else {
			p_70014_1_.setString("OwnerUUID", func_152113_b());
		}

		p_70014_1_.setBoolean("Sitting", isSitting());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		String s = "";

		if (p_70037_1_.hasKey("OwnerUUID", 8)) {
			s = p_70037_1_.getString("OwnerUUID");
		} else {
			String s1 = p_70037_1_.getString("Owner");
			s = PreYggdrasilConverter.func_152719_a(s1);
		}

		if (s.length() > 0) {
			func_152115_b(s);
			setTamed(true);
		}

		aiSit.setSitting(p_70037_1_.getBoolean("Sitting"));
		setSitting(p_70037_1_.getBoolean("Sitting"));
	}

	protected void playTameEffect(boolean p_70908_1_) {
		String s = "heart";

		if (!p_70908_1_) {
			s = "smoke";
		}

		for (int i = 0; i < 7; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(s, posX + rand.nextFloat() * width * 2.0F - width,
					posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
					d2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 7) {
			playTameEffect(true);
		} else if (p_70103_1_ == 6) {
			playTameEffect(false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public boolean isTamed() {
		return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setTamed(boolean p_70903_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70903_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 4)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -5)));
		}
	}

	public boolean isSitting() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSitting(boolean p_70904_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70904_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	@Override
	public String func_152113_b() {
		return dataWatcher.getWatchableObjectString(17);
	}

	public void func_152115_b(String p_152115_1_) {
		dataWatcher.updateObject(17, p_152115_1_);
	}

	@Override
	public EntityLivingBase getOwner() {
		try {
			UUID uuid = UUID.fromString(func_152113_b());
			return uuid == null ? null : worldObj.func_152378_a(uuid);
		} catch (IllegalArgumentException illegalargumentexception) {
			return null;
		}
	}

	public boolean func_152114_e(EntityLivingBase p_152114_1_) {
		return p_152114_1_ == getOwner();
	}

	public EntityAISit func_70907_r() {
		return aiSit;
	}

	public boolean func_142018_a(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
		return true;
	}

	@Override
	public Team getTeam() {
		if (isTamed()) {
			EntityLivingBase entitylivingbase = getOwner();

			if (entitylivingbase != null)
				return entitylivingbase.getTeam();
		}

		return super.getTeam();
	}

	@Override
	public boolean isOnSameTeam(EntityLivingBase p_142014_1_) {
		if (isTamed()) {
			EntityLivingBase entitylivingbase1 = getOwner();

			if (p_142014_1_ == entitylivingbase1)
				return true;

			if (entitylivingbase1 != null)
				return entitylivingbase1.isOnSameTeam(p_142014_1_);
		}

		return super.isOnSameTeam(p_142014_1_);
	}
}