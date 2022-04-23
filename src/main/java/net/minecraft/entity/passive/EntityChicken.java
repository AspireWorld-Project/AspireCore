package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityChicken extends EntityAnimal {
	public float field_70886_e;
	public float destPos;
	public float field_70884_g;
	public float field_70888_h;
	public float field_70889_i = 1.0F;
	public int timeUntilNextEgg;
	public boolean field_152118_bv;
	private static final String __OBFID = "CL_00001639";

	public EntityChicken(World p_i1682_1_) {
		super(p_i1682_1_);
		setSize(0.3F, 0.7F);
		timeUntilNextEgg = rand.nextInt(6000) + 6000;
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.4D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3, new EntityAITempt(this, 1.0D, Items.wheat_seeds, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		field_70888_h = field_70886_e;
		field_70884_g = destPos;
		destPos = (float) (destPos + (onGround ? -1 : 4) * 0.3D);

		if (destPos < 0.0F) {
			destPos = 0.0F;
		}

		if (destPos > 1.0F) {
			destPos = 1.0F;
		}

		if (!onGround && field_70889_i < 1.0F) {
			field_70889_i = 1.0F;
		}

		field_70889_i = (float) (field_70889_i * 0.9D);

		if (!onGround && motionY < 0.0D) {
			motionY *= 0.6D;
		}

		field_70886_e += field_70889_i * 2.0F;

		if (!worldObj.isRemote && !isChild() && !func_152116_bZ() && --timeUntilNextEgg <= 0) {
			playSound("mob.chicken.plop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			dropItem(Items.egg, 1);
			timeUntilNextEgg = rand.nextInt(6000) + 6000;
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected String getLivingSound() {
		return "mob.chicken.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.chicken.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.chicken.hurt";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.chicken.step", 0.15F, 1.0F);
	}

	@Override
	protected Item getDropItem() {
		return Items.feather;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3) + rand.nextInt(1 + p_70628_2_);

		for (int k = 0; k < j; ++k) {
			dropItem(Items.feather, 1);
		}

		if (isBurning()) {
			dropItem(Items.cooked_chicken, 1);
		} else {
			dropItem(Items.chicken, 1);
		}
	}

	@Override
	public EntityChicken createChild(EntityAgeable p_90011_1_) {
		return new EntityChicken(worldObj);
	}

	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_ != null && p_70877_1_.getItem() instanceof ItemSeeds;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		field_152118_bv = p_70037_1_.getBoolean("IsChickenJockey");
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return func_152116_bZ() ? 10 : super.getExperiencePoints(p_70693_1_);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("IsChickenJockey", field_152118_bv);
	}

	@Override
	protected boolean canDespawn() {
		return func_152116_bZ() && riddenByEntity == null;
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();
		float f = MathHelper.sin(renderYawOffset * (float) Math.PI / 180.0F);
		float f1 = MathHelper.cos(renderYawOffset * (float) Math.PI / 180.0F);
		float f2 = 0.1F;
		float f3 = 0.0F;
		riddenByEntity.setPosition(posX + f2 * f, posY + height * 0.5F + riddenByEntity.getYOffset() + f3,
				posZ - f2 * f1);

		if (riddenByEntity instanceof EntityLivingBase) {
			((EntityLivingBase) riddenByEntity).renderYawOffset = renderYawOffset;
		}
	}

	public boolean func_152116_bZ() {
		return field_152118_bv;
	}

	public void func_152117_i(boolean p_152117_1_) {
		field_152118_bv = p_152117_1_;
	}
}