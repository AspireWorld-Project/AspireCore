package net.minecraft.entity.passive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
	private int inLove;
	private int breeding;
	private EntityPlayer field_146084_br;
	private static final String __OBFID = "CL_00001638";

	public EntityAnimal(World p_i1681_1_) {
		super(p_i1681_1_);
	}

	@Override
	protected void updateAITick() {
		if (getGrowingAge() != 0) {
			inLove = 0;
		}

		super.updateAITick();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (getGrowingAge() != 0) {
			inLove = 0;
		}

		if (inLove > 0) {
			--inLove;
			String s = "heart";

			if (inLove % 10 == 0) {
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle(s, posX + rand.nextFloat() * width * 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
						d2);
			}
		} else {
			breeding = 0;
		}
	}

	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (p_70785_1_ instanceof EntityPlayer) {
			if (p_70785_2_ < 3.0F) {
				double d0 = p_70785_1_.posX - posX;
				double d1 = p_70785_1_.posZ - posZ;
				rotationYaw = (float) (Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
				hasAttacked = true;
			}

			EntityPlayer entityplayer = (EntityPlayer) p_70785_1_;

			if (entityplayer.getCurrentEquippedItem() == null
					|| !isBreedingItem(entityplayer.getCurrentEquippedItem())) {
				entityToAttack = null;
			}
		} else if (p_70785_1_ instanceof EntityAnimal) {
			EntityAnimal entityanimal = (EntityAnimal) p_70785_1_;

			if (getGrowingAge() > 0 && entityanimal.getGrowingAge() < 0) {
				if (p_70785_2_ < 2.5D) {
					hasAttacked = true;
				}
			} else if (inLove > 0 && entityanimal.inLove > 0) {
				if (entityanimal.entityToAttack == null) {
					entityanimal.entityToAttack = this;
				}

				if (entityanimal.entityToAttack == this && p_70785_2_ < 3.5D) {
					++entityanimal.inLove;
					++inLove;
					++breeding;

					if (breeding % 4 == 0) {
						worldObj.spawnParticle("heart", posX + rand.nextFloat() * width * 2.0F - width,
								posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width,
								0.0D, 0.0D, 0.0D);
					}

					if (breeding == 60) {
						procreate((EntityAnimal) p_70785_1_);
					}
				} else {
					breeding = 0;
				}
			} else {
				breeding = 0;
				entityToAttack = null;
			}
		}
	}

	private void procreate(EntityAnimal p_70876_1_) {
		EntityAgeable entityageable = createChild(p_70876_1_);

		if (entityageable != null) {
			if (field_146084_br == null && p_70876_1_.func_146083_cb() != null) {
				field_146084_br = p_70876_1_.func_146083_cb();
			}

			if (field_146084_br != null) {
				field_146084_br.triggerAchievement(StatList.field_151186_x);

				if (this instanceof EntityCow) {
					field_146084_br.triggerAchievement(AchievementList.field_150962_H);
				}
			}

			setGrowingAge(6000);
			p_70876_1_.setGrowingAge(6000);
			inLove = 0;
			breeding = 0;
			entityToAttack = null;
			p_70876_1_.entityToAttack = null;
			p_70876_1_.breeding = 0;
			p_70876_1_.inLove = 0;
			entityageable.setGrowingAge(-24000);
			entityageable.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);

			for (int i = 0; i < 7; ++i) {
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", posX + rand.nextFloat() * width * 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
						d2);
			}

			worldObj.spawnEntityInWorld(entityageable);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			fleeingTick = 60;

			if (!isAIEnabled()) {
				IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

				if (iattributeinstance.getModifier(field_110179_h) == null) {
					iattributeinstance.applyModifier(field_110181_i);
				}
			}

			entityToAttack = null;
			inLove = 0;
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
		return worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.grass ? 10.0F
				: worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_) - 0.5F;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("InLove", inLove);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		inLove = p_70037_1_.getInteger("InLove");
	}

	@Override
	protected Entity findPlayerToAttack() {
		if (fleeingTick > 0)
			return null;
		else {
			float f = 8.0F;
			List list;
			int i;
			EntityAnimal entityanimal;

			if (inLove > 0) {
				list = worldObj.getEntitiesWithinAABB(this.getClass(), boundingBox.expand(f, f, f));

				for (i = 0; i < list.size(); ++i) {
					entityanimal = (EntityAnimal) list.get(i);

					if (entityanimal != this && entityanimal.inLove > 0)
						return entityanimal;
				}
			} else if (getGrowingAge() == 0) {
				list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(f, f, f));

				for (i = 0; i < list.size(); ++i) {
					EntityPlayer entityplayer = (EntityPlayer) list.get(i);

					if (entityplayer.getCurrentEquippedItem() != null
							&& isBreedingItem(entityplayer.getCurrentEquippedItem()))
						return entityplayer;
				}
			} else if (getGrowingAge() > 0) {
				list = worldObj.getEntitiesWithinAABB(this.getClass(), boundingBox.expand(f, f, f));

				for (i = 0; i < list.size(); ++i) {
					entityanimal = (EntityAnimal) list.get(i);

					if (entityanimal != this && entityanimal.getGrowingAge() < 0)
						return entityanimal;
				}
			}

			return null;
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		return worldObj.getBlock(i, j - 1, k) == Blocks.grass && worldObj.getFullBlockLightValue(i, j, k) > 8
				&& super.getCanSpawnHere();
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 1 + worldObj.rand.nextInt(3);
	}

	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_.getItem() == Items.wheat;
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();

		if (itemstack != null && isBreedingItem(itemstack) && getGrowingAge() == 0 && inLove <= 0) {
			if (!p_70085_1_.capabilities.isCreativeMode) {
				--itemstack.stackSize;

				if (itemstack.stackSize <= 0) {
					p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
				}
			}

			func_146082_f(p_70085_1_);
			return true;
		} else
			return super.interact(p_70085_1_);
	}

	public void func_146082_f(EntityPlayer p_146082_1_) {
		inLove = 600;
		field_146084_br = p_146082_1_;
		entityToAttack = null;
		worldObj.setEntityState(this, (byte) 18);
	}

	public EntityPlayer func_146083_cb() {
		return field_146084_br;
	}

	public boolean isInLove() {
		return inLove > 0;
	}

	public void resetInLove() {
		inLove = 0;
	}

	public boolean canMateWith(EntityAnimal p_70878_1_) {
		return p_70878_1_ == this ? false
				: p_70878_1_.getClass() != this.getClass() ? false : isInLove() && p_70878_1_.isInLove();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 18) {
			for (int i = 0; i < 7; ++i) {
				double d0 = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", posX + rand.nextFloat() * width * 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
						d2);
			}
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}
}