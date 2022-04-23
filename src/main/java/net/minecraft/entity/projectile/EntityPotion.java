package net.minecraft.entity.projectile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EntityPotion extends EntityThrowable {
	private ItemStack potionDamage;
	private static final String __OBFID = "CL_00001727";

	public ItemStack getPotionDamageItemStack() {
		return potionDamage;
	}

	public void setPotionDamageItemStack(ItemStack potionDamage) {
		this.potionDamage = potionDamage;
	}

	public EntityPotion(World p_i1788_1_) {
		super(p_i1788_1_);
	}

	public EntityPotion(World p_i1789_1_, EntityLivingBase p_i1789_2_, int p_i1789_3_) {
		this(p_i1789_1_, p_i1789_2_, new ItemStack(Items.potionitem, 1, p_i1789_3_));
	}

	public EntityPotion(World p_i1790_1_, EntityLivingBase p_i1790_2_, ItemStack p_i1790_3_) {
		super(p_i1790_1_, p_i1790_2_);
		potionDamage = p_i1790_3_;
	}

	@SideOnly(Side.CLIENT)
	public EntityPotion(World p_i1791_1_, double p_i1791_2_, double p_i1791_4_, double p_i1791_6_, int p_i1791_8_) {
		this(p_i1791_1_, p_i1791_2_, p_i1791_4_, p_i1791_6_, new ItemStack(Items.potionitem, 1, p_i1791_8_));
	}

	public EntityPotion(World p_i1792_1_, double p_i1792_2_, double p_i1792_4_, double p_i1792_6_,
			ItemStack p_i1792_8_) {
		super(p_i1792_1_, p_i1792_2_, p_i1792_4_, p_i1792_6_);
		potionDamage = p_i1792_8_;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected float func_70182_d() {
		return 0.5F;
	}

	@Override
	protected float func_70183_g() {
		return -20.0F;
	}

	public void setPotionDamage(int p_82340_1_) {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		potionDamage.setItemDamage(p_82340_1_);
	}

	public int getPotionDamage() {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		return potionDamage.getItemDamage();
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (!worldObj.isRemote) {
			List list = Items.potionitem.getEffects(potionDamage);
			if (list != null) {
				AxisAlignedBB axisalignedbb = boundingBox.expand(4.0D, 2.0D, 4.0D);
				List list1 = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
				if (list1 != null) {
					Iterator iterator = list1.iterator();
					HashMap<LivingEntity, Double> affected = new HashMap<>();
					while (iterator.hasNext()) {
						EntityLivingBase entitylivingbase = (EntityLivingBase) iterator.next();
						double d0 = getDistanceSqToEntity(entitylivingbase);
						if (d0 < 16.0D) {
							double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
							if (entitylivingbase == pos.entityHit) {
								d1 = 1.0D;
							}
							affected.put((LivingEntity) entitylivingbase.getBukkitEntity(), d1);
						}
					}
					PotionSplashEvent event = CraftEventFactory.callPotionSplashEvent(this, affected);
					if (!event.isCancelled() && !list.isEmpty()) // do not process effects if there are no effects to
																	// process
					{
						for (LivingEntity victim : event.getAffectedEntities()) {
							if (!(victim instanceof CraftLivingEntity)) {
								continue;
							}
							EntityLivingBase entitylivingbase = ((CraftLivingEntity) victim).getHandle();
							double d1 = event.getIntensity(victim);
							for (Object aList : list) {
								PotionEffect potioneffect = (PotionEffect) aList;
								int i = potioneffect.getPotionID();
								if (Potion.potionTypes[i].isInstant()) {
									Potion.potionTypes[i].affectEntity(getMixinThrower(), entitylivingbase,
											potioneffect.getAmplifier(), d1);
								} else {
									int j = (int) (d1 * potioneffect.getDuration() + 0.5D);
									if (j > 20) {
										entitylivingbase
												.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
									}
								}
							}
						}
					}
				}
			}
			worldObj.playAuxSFX(2002, (int) Math.round(posX), (int) Math.round(posY), (int) Math.round(posZ),
					getPotionDamageItemStack().getItemDamage());
			setDead();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("Potion", 10)) {
			potionDamage = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("Potion"));
		} else {
			setPotionDamage(p_70037_1_.getInteger("potionValue"));
		}

		if (potionDamage == null) {
			setDead();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (potionDamage != null) {
			p_70014_1_.setTag("Potion", potionDamage.writeToNBT(new NBTTagCompound()));
		}
	}
}