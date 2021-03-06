package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

public class DamageSource {
	public static DamageSource inFire = new DamageSource("inFire").setFireDamage();
	public static DamageSource onFire = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();
	public static DamageSource lava = new DamageSource("lava").setFireDamage();
	public static DamageSource inWall = new DamageSource("inWall").setDamageBypassesArmor();
	public static DamageSource drown = new DamageSource("drown").setDamageBypassesArmor();
	public static DamageSource starve = new DamageSource("starve").setDamageBypassesArmor().setDamageIsAbsolute();
	public static DamageSource cactus = new DamageSource("cactus");
	public static DamageSource fall = new DamageSource("fall").setDamageBypassesArmor();
	public static DamageSource outOfWorld = new DamageSource("outOfWorld").setDamageBypassesArmor()
			.setDamageAllowedInCreativeMode();
	public static DamageSource generic = new DamageSource("generic").setDamageBypassesArmor();
	public static DamageSource magic = new DamageSource("magic").setDamageBypassesArmor().setMagicDamage();
	public static DamageSource wither = new DamageSource("wither").setDamageBypassesArmor();
	public static DamageSource anvil = new DamageSource("anvil");
	public static DamageSource fallingBlock = new DamageSource("fallingBlock");
	public static DamageSource command = new DamageSource("command").setDamageBypassesArmor()
			.setDamageAllowedInCreativeMode();
	private boolean isUnblockable;
	private boolean isDamageAllowedInCreativeMode;
	private boolean damageIsAbsolute;
	private float hungerDamage = 0.3F;
	private boolean fireDamage;
	private boolean projectile;
	private boolean difficultyScaled;
	private boolean magicDamage;
	private boolean explosion;
	public String damageType;
	public static DamageSource causeMobDamage(EntityLivingBase p_76358_0_) {
		return new EntityDamageSource("mob", p_76358_0_);
	}

	public static DamageSource causePlayerDamage(EntityPlayer p_76365_0_) {
		return new EntityDamageSource("player", p_76365_0_);
	}

	public static DamageSource causeArrowDamage(EntityArrow p_76353_0_, Entity p_76353_1_) {
		return new EntityDamageSourceIndirect("arrow", p_76353_0_, p_76353_1_).setProjectile();
	}

	public static DamageSource causeFireballDamage(EntityFireball p_76362_0_, Entity p_76362_1_) {
		return p_76362_1_ == null
				? new EntityDamageSourceIndirect("onFire", p_76362_0_, p_76362_0_).setFireDamage().setProjectile()
				: new EntityDamageSourceIndirect("fireball", p_76362_0_, p_76362_1_).setFireDamage().setProjectile();
	}

	public static DamageSource causeThrownDamage(Entity p_76356_0_, Entity p_76356_1_) {
		return new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_).setProjectile();
	}

	public static DamageSource causeIndirectMagicDamage(Entity p_76354_0_, Entity p_76354_1_) {
		return new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_).setDamageBypassesArmor()
				.setMagicDamage();
	}

	public static DamageSource causeThornsDamage(Entity p_92087_0_) {
		return new EntityDamageSource("thorns", p_92087_0_).setMagicDamage();
	}

	public static DamageSource setExplosionSource(Explosion p_94539_0_) {
		return p_94539_0_ != null && p_94539_0_.getExplosivePlacedBy() != null
				? new EntityDamageSource("explosion.player", p_94539_0_.getExplosivePlacedBy()).setDifficultyScaled()
						.setExplosion()
				: new DamageSource("explosion").setDifficultyScaled().setExplosion();
	}

	public boolean isProjectile() {
		return projectile;
	}

	public DamageSource setProjectile() {
		projectile = true;
		return this;
	}

	public boolean isExplosion() {
		return explosion;
	}

	public DamageSource setExplosion() {
		explosion = true;
		return this;
	}

	public boolean isUnblockable() {
		return isUnblockable;
	}

	public float getHungerDamage() {
		return hungerDamage;
	}

	public boolean canHarmInCreative() {
		return isDamageAllowedInCreativeMode;
	}

	public boolean isDamageAbsolute() {
		return damageIsAbsolute;
	}

	public DamageSource(String p_i1566_1_) {
		damageType = p_i1566_1_;
	}

	public Entity getSourceOfDamage() {
		return getEntity();
	}

	public Entity getEntity() {
		return null;
	}

	public DamageSource setDamageBypassesArmor() {
		isUnblockable = true;
		hungerDamage = 0.0F;
		return this;
	}

	public DamageSource setDamageAllowedInCreativeMode() {
		isDamageAllowedInCreativeMode = true;
		return this;
	}

	public DamageSource setDamageIsAbsolute() {
		damageIsAbsolute = true;
		hungerDamage = 0.0F;
		return this;
	}

	public DamageSource setFireDamage() {
		fireDamage = true;
		return this;
	}

	public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
		EntityLivingBase entitylivingbase1 = p_151519_1_.func_94060_bK();
		String s = "death.attack." + damageType;
		String s1 = s + ".player";
		return entitylivingbase1 != null && StatCollector.canTranslate(s1)
				? new ChatComponentTranslation(s1,
                p_151519_1_.func_145748_c_(), entitylivingbase1.func_145748_c_())
				: new ChatComponentTranslation(s, p_151519_1_.func_145748_c_());
	}

	public boolean isFireDamage() {
		return fireDamage;
	}

	public String getDamageType() {
		return damageType;
	}

	public DamageSource setDifficultyScaled() {
		difficultyScaled = true;
		return this;
	}

	public boolean isDifficultyScaled() {
		return difficultyScaled;
	}

	public boolean isMagicDamage() {
		return magicDamage;
	}

	public DamageSource setMagicDamage() {
		magicDamage = true;
		return this;
	}
}