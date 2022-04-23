package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Random;

public class EntitySpider extends EntityMob {
	private static final String __OBFID = "CL_00001699";

	public EntitySpider(World p_i1743_1_) {
		super(p_i1743_1_);
		setSize(1.4F, 0.9F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isRemote) {
			setBesideClimbableBlock(isCollidedHorizontally);
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.800000011920929D);
	}

	@Override
	protected Entity findPlayerToAttack() {
		float f = getBrightness(1.0F);

		if (f < 0.5F) {
			double d0 = 16.0D;
			return worldObj.getClosestVulnerablePlayerToEntity(this, d0);
		} else
			return null;
	}

	@Override
	protected String getLivingSound() {
		return "mob.spider.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.spider.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.spider.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.spider.step", 0.15F, 1.0F);
	}

	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		float f1 = getBrightness(1.0F);

		if (f1 > 0.5F && rand.nextInt(100) == 0) {
			EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null,
					EntityTargetEvent.TargetReason.FORGOT_TARGET);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled())
				if (event.getTarget() == null) {
					entityToAttack = null;
				} else {
					entityToAttack = ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
				}
		} else {
			if (p_70785_2_ > 2.0F && p_70785_2_ < 6.0F && rand.nextInt(10) == 0) {
				if (onGround) {
					double d0 = p_70785_1_.posX - posX;
					double d1 = p_70785_1_.posZ - posZ;
					float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
					motionX = d0 / f2 * 0.5D * 0.800000011920929D + motionX * 0.20000000298023224D;
					motionZ = d1 / f2 * 0.5D * 0.800000011920929D + motionZ * 0.20000000298023224D;
					motionY = 0.4000000059604645D;
				}
			} else {
				super.attackEntity(p_70785_1_, p_70785_2_);
			}
		}
	}

	@Override
	protected Item getDropItem() {
		return Items.string;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		super.dropFewItems(p_70628_1_, p_70628_2_);

		if (p_70628_1_ && (rand.nextInt(3) == 0 || rand.nextInt(1 + p_70628_2_) > 0)) {
			dropItem(Items.spider_eye, 1);
		}
	}

	@Override
	public boolean isOnLadder() {
		return isBesideClimbableBlock();
	}

	@Override
	public void setInWeb() {
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect p_70687_1_) {
		return p_70687_1_.getPotionID() != Potion.poison.id && super.isPotionApplicable(p_70687_1_);
	}

	public boolean isBesideClimbableBlock() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setBesideClimbableBlock(boolean p_70839_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70839_1_) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 &= -2;
		}

		dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.rand.nextInt(100) == 0) {
			EntitySkeleton entityskeleton = new EntitySkeleton(worldObj);
			entityskeleton.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
			entityskeleton.onSpawnWithEgg(null);
			worldObj.spawnEntityInWorld(entityskeleton);
			entityskeleton.mountEntity(this);
		}

		if (p_110161_1_1 == null) {
			p_110161_1_1 = new EntitySpider.GroupData();

			if (worldObj.difficultySetting == EnumDifficulty.HARD
					&& worldObj.rand.nextFloat() < 0.1F * worldObj.func_147462_b(posX, posY, posZ)) {
				((EntitySpider.GroupData) p_110161_1_1).func_111104_a(worldObj.rand);
			}
		}

		if (p_110161_1_1 instanceof EntitySpider.GroupData) {
			int i = ((EntitySpider.GroupData) p_110161_1_1).field_111105_a;

			if (i > 0 && Potion.potionTypes[i] != null) {
				addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
			}
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	public static class GroupData implements IEntityLivingData {
		public int field_111105_a;
		private static final String __OBFID = "CL_00001700";

		public void func_111104_a(Random p_111104_1_) {
			int i = p_111104_1_.nextInt(5);

			if (i <= 1) {
				field_111105_a = Potion.moveSpeed.id;
			} else if (i <= 2) {
				field_111105_a = Potion.damageBoost.id;
			} else if (i <= 3) {
				field_111105_a = Potion.regeneration.id;
			} else if (i <= 4) {
				field_111105_a = Potion.invisibility.id;
			}
		}
	}
}