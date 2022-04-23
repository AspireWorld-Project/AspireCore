package net.minecraft.entity;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityUnleashEvent;

import java.util.UUID;

public abstract class EntityCreature extends EntityLiving {
	public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
	public static final AttributeModifier field_110181_i = new AttributeModifier(field_110179_h, "Fleeing speed bonus",
			2.0D, 2).setSaved(false);
	private PathEntity pathToEntity;
	protected Entity entityToAttack;
	protected boolean hasAttacked;
	protected int fleeingTick;
	private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
	private float maximumHomeDistance = -1.0F;
	private EntityAIBase field_110178_bs = new EntityAIMoveTowardsRestriction(this, 1.0D);
	private boolean field_110180_bt;
	private static final String __OBFID = "CL_00001558";
	private int lastPathCountedTick;

	public EntityCreature(World p_i1602_1_) {
		super(p_i1602_1_);
	}

	protected boolean isMovementCeased() {
		return false;
	}

	@Override
	protected void updateEntityActionState() {
		worldObj.theProfiler.startSection("ai");

		if (fleeingTick > 0 && --fleeingTick == 0) {
			IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(field_110181_i);
		}

		hasAttacked = isMovementCeased();
		float f4 = 16.0F;

		if (entityToAttack == null) {
			Entity target = findPlayerToAttack();
			if (target != null) {
				EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(),
						EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
				Bukkit.getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					entityToAttack = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
				}
			}

			if (entityToAttack != null) {
				pathToEntity = worldObj.getPathEntityToEntity(this, entityToAttack, f4, true, false, false, true);
				lastPathCountedTick = MinecraftServer.getServer().getTickCounter();
			}
		} else if (entityToAttack.isEntityAlive()) {
			float f = entityToAttack.getDistanceToEntity(this);

			if (canEntityBeSeen(entityToAttack)) {
				attackEntity(entityToAttack, f);
			}
		} else {
			EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null,
					EntityTargetEvent.TargetReason.TARGET_DIED);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				entityToAttack = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
			}
		}

		if (entityToAttack instanceof EntityPlayerMP
				&& ((EntityPlayerMP) entityToAttack).theItemInWorldManager.isCreative()) {
			entityToAttack = null;
		}

		worldObj.theProfiler.endSection();

		if (!hasAttacked && entityToAttack != null && (pathToEntity == null || rand.nextInt(20) == 0)) {
			// ultramine - fixed path recounting each tick if target is not reachable
			if (MinecraftServer.getServer().getTickCounter() - lastPathCountedTick > 10) {
				pathToEntity = worldObj.getPathEntityToEntity(this, entityToAttack, f4, true, false, false, true);
				lastPathCountedTick = MinecraftServer.getServer().getTickCounter();
			}
		} else if (!hasAttacked
				&& (pathToEntity == null && rand.nextInt(180) == 0 || rand.nextInt(120) == 0 || fleeingTick > 0)
				&& entityAge < 100) {
			updateWanderPath();
		}

		int i = MathHelper.floor_double(boundingBox.minY + 0.5D);
		boolean flag = isInWater();
		boolean flag1 = handleLavaMovement();
		rotationPitch = 0.0F;

		if (pathToEntity != null && rand.nextInt(100) != 0) {
			worldObj.theProfiler.startSection("followpath");
			Vec3 vec3 = pathToEntity.getPosition(this);
			double d0 = width * 2.0F;

			while (vec3 != null && vec3.squareDistanceTo(posX, vec3.yCoord, posZ) < d0 * d0) {
				pathToEntity.incrementPathIndex();

				if (pathToEntity.isFinished()) {
					vec3 = null;
					pathToEntity = null;
				} else {
					vec3 = pathToEntity.getPosition(this);
				}
			}

			isJumping = false;

			if (vec3 != null) {
				double d1 = vec3.xCoord - posX;
				double d2 = vec3.zCoord - posZ;
				double d3 = vec3.yCoord - i;
				float f1 = (float) (Math.atan2(d2, d1) * 180.0D / Math.PI) - 90.0F;
				float f2 = MathHelper.wrapAngleTo180_float(f1 - rotationYaw);
				moveForward = (float) getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();

				if (f2 > 30.0F) {
					f2 = 30.0F;
				}

				if (f2 < -30.0F) {
					f2 = -30.0F;
				}

				rotationYaw += f2;

				if (hasAttacked && entityToAttack != null) {
					double d4 = entityToAttack.posX - posX;
					double d5 = entityToAttack.posZ - posZ;
					float f3 = rotationYaw;
					rotationYaw = (float) (Math.atan2(d5, d4) * 180.0D / Math.PI) - 90.0F;
					f2 = (f3 - rotationYaw + 90.0F) * (float) Math.PI / 180.0F;
					moveStrafing = -MathHelper.sin(f2) * moveForward * 1.0F;
					moveForward = MathHelper.cos(f2) * moveForward * 1.0F;
				}

				if (d3 > 0.0D) {
					isJumping = true;
				}
			}

			if (entityToAttack != null) {
				faceEntity(entityToAttack, 30.0F, 30.0F);
			}

			if (isCollidedHorizontally && !hasPath()) {
				isJumping = true;
			}

			if (rand.nextFloat() < 0.8F && (flag || flag1)) {
				isJumping = true;
			}

			worldObj.theProfiler.endSection();
		} else {
			super.updateEntityActionState();
			pathToEntity = null;
		}
	}

	protected void updateWanderPath() {
		worldObj.theProfiler.startSection("stroll");
		boolean flag = false;
		int i = -1;
		int j = -1;
		int k = -1;
		float f = -99999.0F;

		for (int l = 0; l < 10; ++l) {
			int i1 = MathHelper.floor_double(posX + rand.nextInt(13) - 6.0D);
			int j1 = MathHelper.floor_double(posY + rand.nextInt(7) - 3.0D);
			int k1 = MathHelper.floor_double(posZ + rand.nextInt(13) - 6.0D);
			float f1 = getBlockPathWeight(i1, j1, k1);

			if (f1 > f) {
				f = f1;
				i = i1;
				j = j1;
				k = k1;
				flag = true;
			}
		}

		if (flag) {
			pathToEntity = worldObj.getEntityPathToXYZ(this, i, j, k, 10.0F, true, false, false, true);
		}

		worldObj.theProfiler.endSection();
	}

	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
	}

	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
		return 0.0F;
	}

	protected Entity findPlayerToAttack() {
		return null;
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		return super.getCanSpawnHere() && getBlockPathWeight(i, j, k) >= 0.0F;
	}

	public boolean hasPath() {
		return pathToEntity != null;
	}

	public void setPathToEntity(PathEntity p_70778_1_) {
		pathToEntity = p_70778_1_;
	}

	public Entity getEntityToAttack() {
		return entityToAttack;
	}

	public void setTarget(Entity p_70784_1_) {
		entityToAttack = p_70784_1_;
	}

	public boolean isWithinHomeDistanceCurrentPosition() {
		return isWithinHomeDistance(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
				MathHelper.floor_double(posZ));
	}

	public boolean isWithinHomeDistance(int p_110176_1_, int p_110176_2_, int p_110176_3_) {
		return maximumHomeDistance == -1.0F ? true
				: homePosition.getDistanceSquared(p_110176_1_, p_110176_2_, p_110176_3_) < maximumHomeDistance
						* maximumHomeDistance;
	}

	public void setHomeArea(int p_110171_1_, int p_110171_2_, int p_110171_3_, int p_110171_4_) {
		homePosition.set(p_110171_1_, p_110171_2_, p_110171_3_);
		maximumHomeDistance = p_110171_4_;
	}

	public ChunkCoordinates getHomePosition() {
		return homePosition;
	}

	public float func_110174_bM() {
		return maximumHomeDistance;
	}

	public void detachHome() {
		maximumHomeDistance = -1.0F;
	}

	public boolean hasHome() {
		return maximumHomeDistance != -1.0F;
	}

	@Override
	protected void updateLeashedState() {
		super.updateLeashedState();

		if (getLeashed() && getLeashedToEntity() != null && getLeashedToEntity().worldObj == worldObj) {
			Entity entity = getLeashedToEntity();
			setHomeArea((int) entity.posX, (int) entity.posY, (int) entity.posZ, 5);
			float f = getDistanceToEntity(entity);

			if (this instanceof EntityTameable && ((EntityTameable) this).isSitting()) {
				if (f > 10.0F) {
					Bukkit.getPluginManager().callEvent(
							new EntityUnleashEvent(this.getBukkitEntity(), EntityUnleashEvent.UnleashReason.DISTANCE));
					clearLeashed(true, true);
				}

				return;
			}

			if (!field_110180_bt) {
				tasks.addTask(2, field_110178_bs);
				getNavigator().setAvoidsWater(false);
				field_110180_bt = true;
			}

			func_142017_o(f);

			if (f > 4.0F) {
				getNavigator().tryMoveToEntityLiving(entity, 1.0D);
			}

			if (f > 6.0F) {
				double d0 = (entity.posX - posX) / f;
				double d1 = (entity.posY - posY) / f;
				double d2 = (entity.posZ - posZ) / f;
				motionX += d0 * Math.abs(d0) * 0.4D;
				motionY += d1 * Math.abs(d1) * 0.4D;
				motionZ += d2 * Math.abs(d2) * 0.4D;
			}

			if (f > 10.0F) {
				clearLeashed(true, true);
			}
		} else if (!getLeashed() && field_110180_bt) {
			field_110180_bt = false;
			tasks.removeTask(field_110178_bs);
			getNavigator().setAvoidsWater(true);
			detachHome();
		}
	}

	protected void func_142017_o(float p_142017_1_) {
	}

	public PathEntity getPathToEntity() {
		return pathToEntity;
	}

	public void setEntityToAttack(Entity entityToAttack) {
		this.entityToAttack = entityToAttack;
	}
}