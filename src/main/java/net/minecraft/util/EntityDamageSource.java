package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityDamageSource extends DamageSource {
	protected Entity damageSourceEntity;
	private static final String __OBFID = "CL_00001522";

	public EntityDamageSource(String p_i1567_1_, Entity p_i1567_2_) {
		super(p_i1567_1_);
		damageSourceEntity = p_i1567_2_;
	}

	@Override
	public Entity getEntity() {
		return damageSourceEntity;
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		ItemStack itemstack = damageSourceEntity instanceof EntityLivingBase
				? ((EntityLivingBase) damageSourceEntity).getHeldItem()
				: null;
		String s = "death.attack." + damageType;
		String s1 = s + ".item";
		return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
                p_151519_1_.func_145748_c_(), damageSourceEntity.func_145748_c_(),
                itemstack.func_151000_E())
				: new ChatComponentTranslation(s,
                p_151519_1_.func_145748_c_(), damageSourceEntity.func_145748_c_());
	}

	@Override
	public boolean isDifficultyScaled() {
		return damageSourceEntity != null && damageSourceEntity instanceof EntityLivingBase
				&& !(damageSourceEntity instanceof EntityPlayer);
	}
}