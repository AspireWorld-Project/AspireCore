package org.bukkit.craftbukkit.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
	private CraftEntityEquipment equipment;
	// Cauldron start
	public Class<? extends net.minecraft.entity.EntityLivingBase> entityClass;
	public String entityName;
	// Cauldron end

	public CraftLivingEntity(final CraftServer server, final net.minecraft.entity.EntityLivingBase entity) {
		super(server, entity);
		// Cauldron start
		entityClass = entity.getClass();
		entityName = EntityRegistry.instance().getCustomEntityTypeName(entityClass);
		if (entityName == null) {
			entityName = entity.getCommandSenderName();
			// Cauldron end
		}

		if (entity instanceof net.minecraft.entity.EntityLiving) {
			equipment = new CraftEntityEquipment(this);
		}
	}

	@Override
	public double getHealth() {
		return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
	}

	@Override
	public void setHealth(double health) {
		if (health < 0 || health > getMaxHealth())
			throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());

		// Cauldron start - setHealth must be set before onDeath to respect events that
		// may prevent death.
		getHandle().setHealth((float) health);

		if (entity instanceof net.minecraft.entity.player.EntityPlayerMP && health == 0) {
			((net.minecraft.entity.player.EntityPlayerMP) entity).onDeath(net.minecraft.util.DamageSource.generic);
		}
		// Cauldron end
	}

	@Override
	public double getMaxHealth() {
		return getHandle().getMaxHealth();
	}

	@Override
	public void setMaxHealth(double amount) {
		Validate.isTrue(amount > 0, "Max health must be greater than 0");

		getHandle().getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(amount);

		if (getHealth() > amount) {
			setHealth(amount);
		}
	}

	@Override
	public void resetMaxHealth() {
		setMaxHealth(getHandle().getMaxHealth());
	}

	@Override
	@Deprecated
	public Egg throwEgg() {
		return launchProjectile(Egg.class);
	}

	@Override
	@Deprecated
	public Snowball throwSnowball() {
		return launchProjectile(Snowball.class);
	}

	@Override
	public double getEyeHeight() {
		return getHandle().getEyeHeight();
	}

	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return getEyeHeight();
	}

	private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
		if (maxDistance > 120) {
			maxDistance = 120;
		}
		ArrayList<Block> blocks = new ArrayList<>();
		Iterator<Block> itr = new BlockIterator(this, maxDistance);
		while (itr.hasNext()) {
			Block block = itr.next();
			blocks.add(block);
			if (maxLength != 0 && blocks.size() > maxLength) {
				blocks.remove(0);
			}
			int id = block.getTypeId();
			if (transparent == null) {
				if (id != 0) {
					break;
				}
			} else {
				if (!transparent.contains((byte) id)) {
					break;
				}
			}
		}
		return blocks;
	}

	@Override
	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
		return getLineOfSight(transparent, maxDistance, 0);
	}

	@Override
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
		return blocks.get(0);
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
		return getLineOfSight(transparent, maxDistance, 2);
	}

	@Override
	@Deprecated
	public Arrow shootArrow() {
		return launchProjectile(Arrow.class);
	}

	@Override
	public int getRemainingAir() {
		return getHandle().getAir();
	}

	@Override
	public void setRemainingAir(int ticks) {
		getHandle().setAir(ticks);
	}

	@Override
	public int getMaximumAir() {
		return getHandle().getMaxAirTicks();
	}

	@Override
	public void setMaximumAir(int ticks) {
		getHandle().setMaxAirTicks(ticks);
	}

	@Override
	public void damage(double amount) {
		damage(amount, null);
	}

	@Override
	public void damage(double amount, org.bukkit.entity.Entity source) {
		net.minecraft.util.DamageSource reason = net.minecraft.util.DamageSource.generic;

		if (source instanceof HumanEntity) {
			reason = net.minecraft.util.DamageSource.causePlayerDamage(((CraftHumanEntity) source).getHandle());
		} else if (source instanceof LivingEntity) {
			reason = net.minecraft.util.DamageSource.causeMobDamage(((CraftLivingEntity) source).getHandle());
		}

		if (entity instanceof net.minecraft.entity.boss.EntityDragon) {
			((net.minecraft.entity.boss.EntityDragon) entity).realAttackEntityFrom(reason, (float) amount);
		} else {
			entity.attackEntityFrom(reason, (float) amount);
		}
	}

	@Override
	public Location getEyeLocation() {
		Location loc = getLocation();
		loc.setY(loc.getY() + getEyeHeight());
		return loc;
	}

	@Override
	public int getMaximumNoDamageTicks() {
		return getHandle().maxHurtResistantTime;
	}

	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		getHandle().maxHurtResistantTime = ticks;
	}

	@Override
	public double getLastDamage() {
		return getHandle().getLastDamage();
	}

	@Override
	public void setLastDamage(double damage) {
		getHandle().setLastDamage((float) damage);
	}

	@Override
	public int getNoDamageTicks() {
		return getHandle().hurtResistantTime;
	}

	@Override
	public void setNoDamageTicks(int ticks) {
		getHandle().hurtResistantTime = ticks;
	}

	@Override
	public net.minecraft.entity.EntityLivingBase getHandle() {
		return (net.minecraft.entity.EntityLivingBase) entity;
	}

	public void setHandle(final net.minecraft.entity.EntityLivingBase entity) {
		super.setHandle(entity);
	}

	@Override
	public String toString() {
		return entityName; // Cauldron
	}

	@Override
	public Player getKiller() {
		EntityPlayer attackingPlayer = getHandle().getAttackingPlayer();
		return attackingPlayer == null ? null : (Player) attackingPlayer.getBukkitEntity();
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect) {
		return addPotionEffect(effect, false);
	}

	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		if (hasPotionEffect(effect.getType())) {
			if (!force)
				return false;
			removePotionEffect(effect.getType());
		}
		getHandle().addPotionEffect(new net.minecraft.potion.PotionEffect(effect.getType().getId(),
				effect.getDuration(), effect.getAmplifier(), effect.isAmbient()));
		return true;
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> effects) {
		boolean success = true;
		for (PotionEffect effect : effects) {
			success &= addPotionEffect(effect);
		}
		return success;
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType type) {
		return getHandle().isPotionActive(net.minecraft.potion.Potion.potionTypes[type.getId()]);
	}

	@Override
	public void removePotionEffect(PotionEffectType type) {
		getHandle().removePotionEffect(type.getId()); // Should be removeEffect.
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		List<PotionEffect> effects = new ArrayList<>();
		for (net.minecraft.potion.PotionEffect handle : getHandle().getActivePotionsMap().values()) {
			if (PotionEffectType.getById(handle.getPotionID()) == null) {
				continue; // Cauldron - ignore null types
			}
			effects.add(new PotionEffect(PotionEffectType.getById(handle.getPotionID()), handle.getDuration(),
					handle.getAmplifier(), handle.getIsAmbient()));
		}
		return effects;
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		return launchProjectile(projectile, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		net.minecraft.world.World world = ((CraftWorld) getWorld()).getHandle();
		net.minecraft.entity.Entity launch = null;

		if (Snowball.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.projectile.EntitySnowball(world, getHandle());
		} else if (Egg.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.projectile.EntityEgg(world, getHandle());
		} else if (EnderPearl.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.item.EntityEnderPearl(world, getHandle());
		} else if (Arrow.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.projectile.EntityArrow(world, getHandle(), 1);
		} else if (ThrownPotion.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.projectile.EntityPotion(world, getHandle(),
					CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
		} else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
			launch = new net.minecraft.entity.item.EntityExpBottle(world, getHandle());
		} else if (Fish.class.isAssignableFrom(projectile)
				&& getHandle() instanceof net.minecraft.entity.player.EntityPlayer) {
			launch = new net.minecraft.entity.projectile.EntityFishHook(world,
					(net.minecraft.entity.player.EntityPlayer) getHandle());
		} else if (Fireball.class.isAssignableFrom(projectile)) {
			Location location = getEyeLocation();
			Vector direction = location.getDirection().multiply(10);

			if (SmallFireball.class.isAssignableFrom(projectile)) {
				launch = new net.minecraft.entity.projectile.EntitySmallFireball(world, getHandle(), direction.getX(),
						direction.getY(), direction.getZ());
			} else if (WitherSkull.class.isAssignableFrom(projectile)) {
				launch = new net.minecraft.entity.projectile.EntityWitherSkull(world, getHandle(), direction.getX(),
						direction.getY(), direction.getZ());
			} else {
				launch = new net.minecraft.entity.projectile.EntityLargeFireball(world, getHandle(), direction.getX(),
						direction.getY(), direction.getZ());
			}

			launch.setProjectileSource(this);
			launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(),
					location.getPitch());
		}

		Validate.notNull(launch, "Projectile not supported");

		if (velocity != null) {
			((T) launch.getBukkitEntity()).setVelocity(velocity);
		}

		world.spawnEntityInWorld(launch);
		return (T) launch.getBukkitEntity();
	}

	@Override
	public EntityType getType() {
		// Cauldron start
		EntityType type = EntityType.fromName(entityName);
		if (type != null)
			return type;
		else
			return EntityType.UNKNOWN;
		// Cauldron end
	}

	@Override
	public boolean hasLineOfSight(Entity other) {
		return getHandle().canEntityBeSeen(((CraftEntity) other).getHandle());
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return getHandle() instanceof net.minecraft.entity.EntityLiving
				&& !((net.minecraft.entity.EntityLiving) getHandle()).isPersistenceRequired();
	}

	@Override
	public void setRemoveWhenFarAway(boolean remove) {
		if (getHandle() instanceof net.minecraft.entity.EntityLiving) {
			((net.minecraft.entity.EntityLiving) getHandle()).setPersistenceRequired(!remove);
		}
	}

	@Override
	public EntityEquipment getEquipment() {
		return equipment;
	}

	@Override
	public void setCanPickupItems(boolean pickup) {
		if (getHandle() instanceof net.minecraft.entity.EntityLiving) {
			((net.minecraft.entity.EntityLiving) getHandle()).setCanPickUpLoot(pickup);
		}
	}

	@Override
	public boolean getCanPickupItems() {
		return getHandle() instanceof net.minecraft.entity.EntityLiving
				&& ((net.minecraft.entity.EntityLiving) getHandle()).isCanPickUpLoot();
	}

	@Override
	public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
		if (getHealth() == 0)
			return false;

		return super.teleport(location, cause);
	}

	@Override
	public void setCustomName(String name) {
		if (!(getHandle() instanceof net.minecraft.entity.EntityLiving))
			return;

		if (name == null) {
			name = "";
		}

		// Names cannot be more than 64 characters due to DataWatcher limitations
		if (name.length() > 64) {
			name = name.substring(0, 64);
		}

		((net.minecraft.entity.EntityLiving) getHandle()).setCustomNameTag(name);
	}

	@Override
	public String getCustomName() {
		if (!(getHandle() instanceof net.minecraft.entity.EntityLiving))
			return null;

		String name = ((net.minecraft.entity.EntityLiving) getHandle()).getCustomNameTag();

		if (name == null || name.length() == 0)
			return null;

		return name;
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		if (getHandle() instanceof net.minecraft.entity.EntityLiving) {
			((net.minecraft.entity.EntityLiving) getHandle()).setAlwaysRenderNameTag(flag);
		}
	}

	@Override
	public boolean isCustomNameVisible() {
		return getHandle() instanceof net.minecraft.entity.EntityLiving
				&& ((net.minecraft.entity.EntityLiving) getHandle()).getAlwaysRenderNameTag();
	}

	@Override
	public boolean isLeashed() {
		if (!(getHandle() instanceof net.minecraft.entity.EntityLiving))
			return false;
		return ((net.minecraft.entity.EntityLiving) getHandle()).getLeashedToEntity() != null;
	}

	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		if (!isLeashed())
			throw new IllegalStateException("Entity not leashed");
		return ((net.minecraft.entity.EntityLiving) getHandle()).getLeashedToEntity().getBukkitEntity();
	}

	private boolean unleash() {
		if (!isLeashed())
			return false;
		((net.minecraft.entity.EntityLiving) getHandle()).clearLeashed(true, false);
		return true;
	}

	@Override
	public boolean setLeashHolder(Entity holder) {
		if (getHandle() instanceof net.minecraft.entity.boss.EntityWither
				|| !(getHandle() instanceof net.minecraft.entity.EntityLiving))
			return false;

		if (holder == null)
			return unleash();

		if (holder.isDead())
			return false;

		unleash();
		((net.minecraft.entity.EntityLiving) getHandle()).setLeashedToEntity(((CraftEntity) holder).getHandle(), true);
		return true;
	}

	@Override
	@Deprecated
	public int _INVALID_getLastDamage() {
		return NumberConversions.ceil(getLastDamage());
	}

	@Override
	@Deprecated
	public void _INVALID_setLastDamage(int damage) {
		setLastDamage(damage);
	}

	@Override
	@Deprecated
	public void _INVALID_damage(int amount) {
		damage(amount);
	}

	@Override
	@Deprecated
	public void _INVALID_damage(int amount, Entity source) {
		damage(amount, source);
	}

	@Override
	@Deprecated
	public int _INVALID_getHealth() {
		return NumberConversions.ceil(getHealth());
	}

	@Override
	@Deprecated
	public void _INVALID_setHealth(int health) {
		setHealth(health);
	}

	@Override
	@Deprecated
	public int _INVALID_getMaxHealth() {
		return NumberConversions.ceil(getMaxHealth());
	}

	@Override
	@Deprecated
	public void _INVALID_setMaxHealth(int health) {
		setMaxHealth(health);
	}
}
