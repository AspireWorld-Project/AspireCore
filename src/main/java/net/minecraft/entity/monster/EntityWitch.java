package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
	private static final UUID field_110184_bp = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier field_110185_bq = new AttributeModifier(field_110184_bp,
			"Drinking speed penalty", -0.25D, 0).setSaved(false);
	private static final Item[] witchDrops = new Item[] { Items.glowstone_dust, Items.sugar, Items.redstone,
			Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick };
	private int witchAttackTimer;
	private static final String __OBFID = "CL_00001701";

	public EntityWitch(World p_i1744_1_) {
		super(p_i1744_1_);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
		tasks.addTask(2, new EntityAIWander(this, 1.0D));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(21, Byte.valueOf((byte) 0));
	}

	@Override
	protected String getLivingSound() {
		return "mob.witch.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.witch.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.witch.death";
	}

	public void setAggressive(boolean p_82197_1_) {
		getDataWatcher().updateObject(21, Byte.valueOf((byte) (p_82197_1_ ? 1 : 0)));
	}

	public boolean getAggressive() {
		return getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		if (!worldObj.isRemote) {
			if (getAggressive()) {
				if (witchAttackTimer-- <= 0) {
					setAggressive(false);
					ItemStack itemstack = getHeldItem();
					setCurrentItemOrArmor(0, (ItemStack) null);

					if (itemstack != null && itemstack.getItem() == Items.potionitem) {
						List list = Items.potionitem.getEffects(itemstack);

						if (list != null) {
							Iterator iterator = list.iterator();

							while (iterator.hasNext()) {
								PotionEffect potioneffect = (PotionEffect) iterator.next();
								addPotionEffect(new PotionEffect(potioneffect));
							}
						}
					}

					getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
				}
			} else {
				short short1 = -1;

				if (rand.nextFloat() < 0.15F && isInsideOfMaterial(Material.water)
						&& !this.isPotionActive(Potion.waterBreathing)) {
					short1 = 8237;
				} else if (rand.nextFloat() < 0.15F && isBurning() && !this.isPotionActive(Potion.fireResistance)) {
					short1 = 16307;
				} else if (rand.nextFloat() < 0.05F && getHealth() < getMaxHealth()) {
					short1 = 16341;
				} else if (rand.nextFloat() < 0.25F && getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				} else if (rand.nextFloat() < 0.25F && getAttackTarget() != null
						&& !this.isPotionActive(Potion.moveSpeed)
						&& getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				}

				if (short1 > -1) {
					setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, short1));
					witchAttackTimer = getHeldItem().getMaxItemUseDuration();
					setAggressive(true);
					IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					iattributeinstance.removeModifier(field_110185_bq);
					iattributeinstance.applyModifier(field_110185_bq);
				}
			}

			if (rand.nextFloat() < 7.5E-4F) {
				worldObj.setEntityState(this, (byte) 15);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 15) {
			for (int i = 0; i < rand.nextInt(35) + 10; ++i) {
				worldObj.spawnParticle("witchMagic", posX + rand.nextGaussian() * 0.12999999523162842D,
						boundingBox.maxY + 0.5D + rand.nextGaussian() * 0.12999999523162842D,
						posZ + rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_) {
		p_70672_2_ = super.applyPotionDamageCalculations(p_70672_1_, p_70672_2_);

		if (p_70672_1_.getEntity() == this) {
			p_70672_2_ = 0.0F;
		}

		if (p_70672_1_.isMagicDamage()) {
			p_70672_2_ = (float) (p_70672_2_ * 0.15D);
		}

		return p_70672_2_;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3) + 1;

		for (int k = 0; k < j; ++k) {
			int l = rand.nextInt(3);
			Item item = witchDrops[rand.nextInt(witchDrops.length)];

			if (p_70628_2_ > 0) {
				l += rand.nextInt(p_70628_2_ + 1);
			}

			for (int i1 = 0; i1 < l; ++i1) {
				dropItem(item, 1);
			}
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		if (!getAggressive()) {
			EntityPotion entitypotion = new EntityPotion(worldObj, this, 32732);
			entitypotion.rotationPitch -= -20.0F;
			double d0 = p_82196_1_.posX + p_82196_1_.motionX - posX;
			double d1 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D - posY;
			double d2 = p_82196_1_.posZ + p_82196_1_.motionZ - posZ;
			float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);

			if (f1 >= 8.0F && !p_82196_1_.isPotionActive(Potion.moveSlowdown)) {
				entitypotion.setPotionDamage(32698);
			} else if (p_82196_1_.getHealth() >= 8.0F && !p_82196_1_.isPotionActive(Potion.poison)) {
				entitypotion.setPotionDamage(32660);
			} else if (f1 <= 3.0F && !p_82196_1_.isPotionActive(Potion.weakness) && rand.nextFloat() < 0.25F) {
				entitypotion.setPotionDamage(32696);
			}

			entitypotion.setThrowableHeading(d0, d1 + f1 * 0.2F, d2, 0.75F, 8.0F);
			worldObj.spawnEntityInWorld(entitypotion);
		}
	}
}