package net.minecraft.entity.projectile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEgg extends EntityThrowable {
	private static final String __OBFID = "CL_00001724";

	public EntityEgg(World p_i1779_1_) {
		super(p_i1779_1_);
	}

	public EntityEgg(World p_i1780_1_, EntityLivingBase p_i1780_2_) {
		super(p_i1780_1_, p_i1780_2_);
	}

	public EntityEgg(World p_i1781_1_, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) {
		super(p_i1781_1_, p_i1781_2_, p_i1781_4_, p_i1781_6_);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (pos.entityHit != null) {
			pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getMixinThrower()), 0.0F);
		}

		boolean hatching = !worldObj.isRemote && rand.nextInt(8) == 0;
		int numHatching = rand.nextInt(32) == 0 ? 4 : 1;
		if (!hatching) {
			numHatching = 0;
		}
		EntityType hatchingType = EntityType.CHICKEN;
		Entity shooter = getMixinThrower();
		if (shooter instanceof EntityPlayerMP) {
			Player player = (Player) shooter.getBukkitEntity();
			PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, (org.bukkit.entity.Egg) this.getBukkitEntity(),
					hatching, (byte) numHatching, hatchingType);
			Bukkit.getPluginManager().callEvent(event);
			hatching = event.isHatching();
			numHatching = event.getNumHatches();
			hatchingType = event.getHatchingType();
		}
		if (hatching) {
			for (int k = 0; k < numHatching; k++) {
				org.bukkit.entity.Entity entity = worldObj.getWorld().spawn(
						new org.bukkit.Location(worldObj.getWorld(), posX, posY, posZ, rotationYaw, 0.0F),
						hatchingType.getEntityClass(), CreatureSpawnEvent.SpawnReason.EGG);
				if (entity instanceof Ageable) {
					((Ageable) entity).setBaby();
				}
			}
		}
		for (int j = 0; j < 8; ++j) {
			worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}

		if (!worldObj.isRemote) {
			setDead();
		}
	}
}