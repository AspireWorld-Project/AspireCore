package net.minecraft.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityDragonPart extends Entity {
	public final IEntityMultiPart entityDragonObj;
	public final String field_146032_b;
	private static final String __OBFID = "CL_00001657";

	public EntityDragonPart(IEntityMultiPart p_i1697_1_, String p_i1697_2_, float p_i1697_3_, float p_i1697_4_) {
		super(p_i1697_1_.func_82194_d());
		setSize(p_i1697_3_, p_i1697_4_);
		entityDragonObj = p_i1697_1_;
		field_146032_b = p_i1697_2_;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return isEntityInvulnerable() ? false : entityDragonObj.attackEntityFromPart(this, p_70097_1_, p_70097_2_);
	}

	@Override
	public boolean isEntityEqual(Entity p_70028_1_) {
		return this == p_70028_1_ || entityDragonObj == p_70028_1_;
	}
}