package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EntityDamageSourceIndirect extends EntityDamageSource {
	private Entity indirectEntity;
	private static final String __OBFID = "CL_00001523";

	public EntityDamageSourceIndirect(String p_i1568_1_, Entity p_i1568_2_, Entity p_i1568_3_) {
		super(p_i1568_1_, p_i1568_2_);
		indirectEntity = p_i1568_3_;
	}

	@Override
	public Entity getSourceOfDamage() {
		return damageSourceEntity;
	}

	@Override
	public Entity getEntity() {
		return indirectEntity;
	}

	public Entity getProximateDamageSource() {
		return super.getEntity();
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		IChatComponent ichatcomponent = indirectEntity == null ? damageSourceEntity.func_145748_c_()
				: indirectEntity.func_145748_c_();
		ItemStack itemstack = indirectEntity instanceof EntityLivingBase
				? ((EntityLivingBase) indirectEntity).getHeldItem()
				: null;
		String s = "death.attack." + damageType;
		String s1 = s + ".item";
		return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
						new Object[] { p_151519_1_.func_145748_c_(), ichatcomponent, itemstack.func_151000_E() })
				: new ChatComponentTranslation(s, new Object[] { p_151519_1_.func_145748_c_(), ichatcomponent });
	}
}