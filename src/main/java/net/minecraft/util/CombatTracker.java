package net.minecraft.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombatTracker {
	@SuppressWarnings("rawtypes")
	private final List combatEntries = new ArrayList();
	private final EntityLivingBase fighter;
	private int field_94555_c;
	private boolean field_94552_d;
	private boolean field_94553_e;
	private String field_94551_f;
	public CombatTracker(EntityLivingBase p_i1565_1_) {
		fighter = p_i1565_1_;
	}

	public void func_94545_a() {
		func_94542_g();

		if (fighter.isOnLadder()) {
			Block block = fighter.worldObj.getBlock(MathHelper.floor_double(fighter.posX),
					MathHelper.floor_double(fighter.boundingBox.minY), MathHelper.floor_double(fighter.posZ));

			if (block == Blocks.ladder) {
				field_94551_f = "ladder";
			} else if (block == Blocks.vine) {
				field_94551_f = "vines";
			}
		} else if (fighter.isInWater()) {
			field_94551_f = "water";
		}
	}

	@SuppressWarnings("unchecked")
	public void func_94547_a(DamageSource p_94547_1_, float p_94547_2_, float p_94547_3_) {
		func_94549_h();
		func_94545_a();
		CombatEntry combatentry = new CombatEntry(p_94547_1_, fighter.ticksExisted, p_94547_2_, p_94547_3_,
				field_94551_f, fighter.fallDistance);
		combatEntries.add(combatentry);
		field_94555_c = fighter.ticksExisted;
		field_94553_e = true;

		if (combatentry.func_94559_f() && !field_94552_d && fighter.isEntityAlive()) {
			field_94552_d = true;
			fighter.func_152111_bt();
		}
	}

	public IChatComponent func_151521_b() {
		if (combatEntries.size() == 0)
			return new ChatComponentTranslation("death.attack.generic", fighter.func_145748_c_());
		else {
			CombatEntry combatentry = func_94544_f();
			CombatEntry combatentry1 = (CombatEntry) combatEntries.get(combatEntries.size() - 1);
			IChatComponent ichatcomponent = combatentry1.func_151522_h();
			Entity entity = combatentry1.getDamageSrc().getEntity();
			Object object;

			if (combatentry != null && combatentry1.getDamageSrc() == DamageSource.fall) {
				IChatComponent ichatcomponent1 = combatentry.func_151522_h();

				if (combatentry.getDamageSrc() != DamageSource.fall
						&& combatentry.getDamageSrc() != DamageSource.outOfWorld) {
					if (ichatcomponent1 != null
							&& (ichatcomponent == null || !ichatcomponent1.equals(ichatcomponent))) {
						Entity entity1 = combatentry.getDamageSrc().getEntity();
						ItemStack itemstack1 = entity1 instanceof EntityLivingBase
								? ((EntityLivingBase) entity1).getHeldItem()
								: null;

						if (itemstack1 != null && itemstack1.hasDisplayName()) {
							object = new ChatComponentTranslation("death.fell.assist.item", fighter.func_145748_c_(), ichatcomponent1, itemstack1.func_151000_E());
						} else {
							object = new ChatComponentTranslation("death.fell.assist",
                                    fighter.func_145748_c_(), ichatcomponent1);
						}
					} else if (ichatcomponent != null) {
						ItemStack itemstack = entity instanceof EntityLivingBase
								? ((EntityLivingBase) entity).getHeldItem()
								: null;

						if (itemstack != null && itemstack.hasDisplayName()) {
							object = new ChatComponentTranslation("death.fell.finish.item", fighter.func_145748_c_(), ichatcomponent, itemstack.func_151000_E());
						} else {
							object = new ChatComponentTranslation("death.fell.finish",
                                    fighter.func_145748_c_(), ichatcomponent);
						}
					} else {
						object = new ChatComponentTranslation("death.fell.killer",
                                fighter.func_145748_c_());
					}
				} else {
					object = new ChatComponentTranslation("death.fell.accident." + func_94548_b(combatentry),
                            fighter.func_145748_c_());
				}
			} else {
				object = combatentry1.getDamageSrc().func_151519_b(fighter);
			}

			return (IChatComponent) object;
		}
	}

	@SuppressWarnings("rawtypes")
	public EntityLivingBase func_94550_c() {
		EntityLivingBase entitylivingbase = null;
		EntityPlayer entityplayer = null;
		float f = 0.0F;
		float f1 = 0.0F;
		Iterator iterator = combatEntries.iterator();

		while (iterator.hasNext()) {
			CombatEntry combatentry = (CombatEntry) iterator.next();

			if (combatentry.getDamageSrc().getEntity() instanceof EntityPlayer
					&& (entityplayer == null || combatentry.func_94563_c() > f1)) {
				f1 = combatentry.func_94563_c();
				entityplayer = (EntityPlayer) combatentry.getDamageSrc().getEntity();
			}

			if (combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase
					&& (entitylivingbase == null || combatentry.func_94563_c() > f)) {
				f = combatentry.func_94563_c();
				entitylivingbase = (EntityLivingBase) combatentry.getDamageSrc().getEntity();
			}
		}

		if (entityplayer != null && f1 >= f / 3.0F)
			return entityplayer;
		else
			return entitylivingbase;
	}

	private CombatEntry func_94544_f() {
		CombatEntry combatentry = null;
		CombatEntry combatentry1 = null;
		byte b0 = 0;
		float f = 0.0F;

		for (int i = 0; i < combatEntries.size(); ++i) {
			CombatEntry combatentry2 = (CombatEntry) combatEntries.get(i);
			CombatEntry combatentry3 = i > 0 ? (CombatEntry) combatEntries.get(i - 1) : null;

			if ((combatentry2.getDamageSrc() == DamageSource.fall
					|| combatentry2.getDamageSrc() == DamageSource.outOfWorld) && combatentry2.func_94561_i() > 0.0F
					&& (combatentry == null || combatentry2.func_94561_i() > f)) {
				if (i > 0) {
					combatentry = combatentry3;
				} else {
					combatentry = combatentry2;
				}

				f = combatentry2.func_94561_i();
			}

			if (combatentry2.func_94562_g() != null && (combatentry1 == null || combatentry2.func_94563_c() > b0)) {
				combatentry1 = combatentry2;
			}
		}

		if (f > 5.0F && combatentry != null)
			return combatentry;
		else if (b0 > 5 && combatentry1 != null)
			return combatentry1;
		else
			return null;
	}

	private String func_94548_b(CombatEntry p_94548_1_) {
		return p_94548_1_.func_94562_g() == null ? "generic" : p_94548_1_.func_94562_g();
	}

	private void func_94542_g() {
		field_94551_f = null;
	}

	public void func_94549_h() {
		int i = field_94552_d ? 300 : 100;

		if (field_94553_e && (!fighter.isEntityAlive() || fighter.ticksExisted - field_94555_c > i)) {
			boolean flag = field_94552_d;
			field_94553_e = false;
			field_94552_d = false;
			if (flag) {
				fighter.func_152112_bu();
			}

			combatEntries.clear();
		}
	}
}