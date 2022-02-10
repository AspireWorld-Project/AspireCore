package net.minecraft.entity.monster;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime extends EntityLiving implements IMob {
	public float squishAmount;
	public float squishFactor;
	public float prevSquishFactor;
	private int slimeJumpDelay;
	private static final String __OBFID = "CL_00001698";

	public EntitySlime(World p_i1742_1_) {
		super(p_i1742_1_);
		int i = 1 << rand.nextInt(3);
		yOffset = 0.0F;
		slimeJumpDelay = rand.nextInt(20) + 10;
		setSlimeSize(i);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 1));
	}

	public void setSlimeSize(int p_70799_1_) {
		dataWatcher.updateObject(16, new Byte((byte) p_70799_1_));
		setSize(0.6F * p_70799_1_, 0.6F * p_70799_1_);
		setPosition(posX, posY, posZ);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(p_70799_1_ * p_70799_1_);
		setHealth(getMaxHealth());
		experienceValue = p_70799_1_;
	}

	public int getSlimeSize() {
		return dataWatcher.getWatchableObjectByte(16);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Size", getSlimeSize() - 1);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		int i = p_70037_1_.getInteger("Size");

		if (i < 0) {
			i = 0;
		}

		setSlimeSize(i + 1);
	}

	protected String getSlimeParticle() {
		return "slime";
	}

	protected String getJumpSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	@Override
	public void onUpdate() {
		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL && getSlimeSize() > 0) {
			isDead = true;
		}

		squishFactor += (squishAmount - squishFactor) * 0.5F;
		prevSquishFactor = squishFactor;
		boolean flag = onGround;
		super.onUpdate();
		int i;

		if (onGround && !flag) {
			i = getSlimeSize();

			for (int j = 0; j < i * 8; ++j) {
				float f = rand.nextFloat() * (float) Math.PI * 2.0F;
				float f1 = rand.nextFloat() * 0.5F + 0.5F;
				float f2 = MathHelper.sin(f) * i * 0.5F * f1;
				float f3 = MathHelper.cos(f) * i * 0.5F * f1;
				worldObj.spawnParticle(getSlimeParticle(), posX + f2, boundingBox.minY, posZ + f3, 0.0D, 0.0D, 0.0D);
			}

			if (makesSoundOnLand()) {
				playSound(getJumpSound(), getSoundVolume(),
						((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			squishAmount = -0.5F;
		} else if (!onGround && flag) {
			squishAmount = 1.0F;
		}

		alterSquishAmount();

		if (worldObj.isRemote) {
			i = getSlimeSize();
			setSize(0.6F * i, 0.6F * i);
		}
	}

	protected void alterSquishAmount() {
		squishAmount *= 0.6F;
	}

	protected int getJumpDelay() {
		return rand.nextInt(20) + 10;
	}

	protected EntitySlime createInstance() {
		return new EntitySlime(worldObj);
	}

	@Override
	public void setDead() {
		int i = getSlimeSize();

		if (!worldObj.isRemote && i > 1 && getHealth() <= 0.0F) {
			int j = 2 + rand.nextInt(3);
			// CraftBukkit start
			SlimeSplitEvent event = new SlimeSplitEvent((org.bukkit.entity.Slime) this.getBukkitEntity(), j);
			this.worldObj.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled() && event.getCount() > 0) {
				j = event.getCount();
			} else {
				super.setDead();
				return;
			}
			// CraftBukkit end
			for (int k = 0; k < j; ++k) {
				float f = (k % 2 - 0.5F) * i / 4.0F;
				float f1 = (k / 2 - 0.5F) * i / 4.0F;
				EntitySlime entityslime = createInstance();
				entityslime.setSlimeSize(i / 2);
				entityslime.setLocationAndAngles(posX + f, posY + 0.5D, posZ + f1, rand.nextFloat() * 360.0F, 0.0F);
				this.worldObj.addEntity(entityslime,
						org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SLIME_SPLIT);// worldObj.spawnEntityInWorld(entityslime);
			}
		}

		super.setDead();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (canDamagePlayer()) {
			int i = getSlimeSize();

			if (canEntityBeSeen(p_70100_1_) && getDistanceSqToEntity(p_70100_1_) < 0.6D * i * 0.6D * i
					&& p_70100_1_.attackEntityFrom(DamageSource.causeMobDamage(this), getAttackStrength())) {
				playSound("mob.attack", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	protected boolean canDamagePlayer() {
		return getSlimeSize() > 1;
	}

	protected int getAttackStrength() {
		return getSlimeSize();
	}

	@Override
	protected String getHurtSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	@Override
	protected String getDeathSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	@Override
	protected Item getDropItem() {
		return getSlimeSize() == 1 ? Items.slime_ball : Item.getItemById(0);
	}

	@Override
	public boolean getCanSpawnHere() {
		Chunk chunk = worldObj.getChunkFromBlockCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));

		if (worldObj.getWorldInfo().getTerrainType().handleSlimeSpawnReduction(rand, worldObj))
			return false;
		else {
			if (getSlimeSize() == 1 || worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
				BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX),
						MathHelper.floor_double(posZ));

				if (biomegenbase == BiomeGenBase.swampland && posY > 50.0D && posY < 70.0D && rand.nextFloat() < 0.5F
						&& rand.nextFloat() < worldObj.getCurrentMoonPhaseFactor()
						&& worldObj.getBlockLightValue(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
								MathHelper.floor_double(posZ)) <= rand.nextInt(8))
					return super.getCanSpawnHere();

				if (rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && posY < 40.0D)
					return super.getCanSpawnHere();
			}

			return false;
		}
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F * getSlimeSize();
	}

	@Override
	public int getVerticalFaceSpeed() {
		return 0;
	}

	protected boolean makesSoundOnJump() {
		return getSlimeSize() > 0;
	}

	protected boolean makesSoundOnLand() {
		return getSlimeSize() > 2;
	}

	private Entity lastTarget = null;

	public void setSlimeSizePub(int size) {
		setSlimeSize(size);
	}

	@Override
	protected void updateEntityActionState() {
		despawnEntity();
		Entity entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
		EntityTargetEvent event = null;
		if (entityplayer != null && !entityplayer.equals(lastTarget)) {
			event = CraftEventFactory.callEntityTargetEvent(this, entityplayer,
					EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
		} else if (lastTarget != null && entityplayer == null) {
			event = CraftEventFactory.callEntityTargetEvent(this, null, EntityTargetEvent.TargetReason.FORGOT_TARGET);
		}
		if (event != null && !event.isCancelled()) {
			entityplayer = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
		}
		lastTarget = entityplayer;
		if (entityplayer != null) {
			faceEntity(entityplayer, 10.0F, 20.0F);
		}
		if (onGround && slimeJumpDelay-- <= 0) {
			slimeJumpDelay = getJumpDelay();
			if (entityplayer != null) {
				slimeJumpDelay /= 3;
			}
			isJumping = true;
			if (makesSoundOnJump()) {
				playSound(getJumpSound(), getSoundVolume(),
						((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}
			moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
			moveForward = getSlimeSize();
		} else {
			isJumping = false;
			if (onGround) {
				moveStrafing = moveForward = 0.0F;
			}
		}
	}
}