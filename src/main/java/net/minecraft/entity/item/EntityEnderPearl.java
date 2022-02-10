package net.minecraft.entity.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerTeleportEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityEnderPearl extends EntityThrowable {
	private static final String __OBFID = "CL_00001725";

	public EntityEnderPearl(World p_i1782_1_) {
		super(p_i1782_1_);
	}

	public EntityEnderPearl(World p_i1783_1_, EntityLivingBase p_i1783_2_) {
		super(p_i1783_1_, p_i1783_2_);
	}

	@SideOnly(Side.CLIENT)
	public EntityEnderPearl(World p_i1784_1_, double p_i1784_2_, double p_i1784_4_, double p_i1784_6_) {
		super(p_i1784_1_, p_i1784_2_, p_i1784_4_, p_i1784_6_);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (pos.entityHit != null) {
			pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getMixinThrower()), 0.0F);
		}
		for (int i = 0; i < 32; ++i) {
			worldObj.spawnParticle("portal", posX, posY + rand.nextDouble() * 2.0D, posZ, rand.nextGaussian(), 0.0D,
					rand.nextGaussian());
		}
		if (!worldObj.isRemote) {
			if (getMixinThrower() != null && getMixinThrower() instanceof EntityPlayerMP) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) getMixinThrower();
				if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen()
						&& entityplayermp.worldObj == worldObj) {
					EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, posX, posY, posZ, 5);
					if (MinecraftForge.EVENT_BUS.post(event)) {
						setDead();
						return;
					}
					CraftPlayer player = (CraftPlayer) entityplayermp.getBukkitEntity();
					Location location = this.getBukkitEntity().getLocation();
					location.setPitch(player.getLocation().getPitch());
					location.setYaw(player.getLocation().getYaw());
					PlayerTeleportEvent teleportEvent = new PlayerTeleportEvent(player, player.getLocation(), location,
							PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
					Bukkit.getPluginManager().callEvent(teleportEvent);
					if (!teleportEvent.isCancelled() && !entityplayermp.playerNetServerHandler.isDisconnected()) {
						entityplayermp.playerNetServerHandler.teleport(teleportEvent.getTo());
						getMixinThrower().fallDistance = 0.0F;
						CraftEventFactory.entityDamage = this;
						getMixinThrower().attackEntityFrom(DamageSource.fall, 5.0F);
						CraftEventFactory.entityDamage = null;
					}
				}
			}
			setDead();
		}
	}
}