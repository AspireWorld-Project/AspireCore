package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityIronGolem extends EntityGolem {
	private int homeCheckTimer;
	Village villageObj;
	private int attackTimer;
	private int holdRoseTick;
	private static final String __OBFID = "CL_00001652";

	public EntityIronGolem(World p_i1694_1_) {
		super(p_i1694_1_);
		setSize(1.4F, 2.9F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(5, new EntityAILookAtVillager(this));
		tasks.addTask(6, new EntityAIWander(this, 0.6D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIDefendVillage(this));
		targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(3,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void updateAITick() {
		if (--homeCheckTimer <= 0) {
			homeCheckTimer = 70 + rand.nextInt(50);
			villageObj = worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(posX),
					MathHelper.floor_double(posY), MathHelper.floor_double(posZ), 32);

			if (villageObj == null) {
				detachHome();
			} else {
				ChunkCoordinates chunkcoordinates = villageObj.getCenter();
				setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ,
						(int) (villageObj.getVillageRadius() * 0.6F));
			}
		}

		super.updateAITick();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	protected int decreaseAirSupply(int p_70682_1_) {
		return p_70682_1_;
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_) {
		if (p_82167_1_ instanceof IMob && getRNG().nextInt(20) == 0) {
			setAttackTarget((EntityLivingBase) p_82167_1_);
		}

		super.collideWithEntity(p_82167_1_);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (attackTimer > 0) {
			--attackTimer;
		}

		if (holdRoseTick > 0) {
			--holdRoseTick;
		}

		if (motionX * motionX + motionZ * motionZ > 2.500000277905201E-7D && rand.nextInt(5) == 0) {
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(posY - 0.20000000298023224D - yOffset);
			int k = MathHelper.floor_double(posZ);
			Block block = worldObj.getBlock(i, j, k);

			if (block.getMaterial() != Material.air) {
				worldObj.spawnParticle(
						"blockcrack_" + Block.getIdFromBlock(block) + "_" + worldObj.getBlockMetadata(i, j, k),
						posX + (rand.nextFloat() - 0.5D) * width, boundingBox.minY + 0.1D,
						posZ + (rand.nextFloat() - 0.5D) * width, 4.0D * (rand.nextFloat() - 0.5D), 0.5D,
						(rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	@Override
	public boolean canAttackClass(Class p_70686_1_) {
		return (!isPlayerCreated() || !EntityPlayer.class.isAssignableFrom(p_70686_1_)) && super.canAttackClass(p_70686_1_);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("PlayerCreated", isPlayerCreated());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setPlayerCreated(p_70037_1_.getBoolean("PlayerCreated"));
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		attackTimer = 10;
		worldObj.setEntityState(this, (byte) 4);
		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + rand.nextInt(15));

		if (flag) {
			p_70652_1_.motionY += 0.4000000059604645D;
		}

		playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 4) {
			attackTimer = 10;
			playSound("mob.irongolem.throw", 1.0F, 1.0F);
		} else if (p_70103_1_ == 11) {
			holdRoseTick = 400;
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public Village getVillage() {
		return villageObj;
	}

	@SideOnly(Side.CLIENT)
	public int getAttackTimer() {
		return attackTimer;
	}

	public void setHoldingRose(boolean p_70851_1_) {
		holdRoseTick = p_70851_1_ ? 400 : 0;
		worldObj.setEntityState(this, (byte) 11);
	}

	@Override
	protected String getHurtSound() {
		return "mob.irongolem.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.irongolem.death";
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3);
		int k;

		for (k = 0; k < j; ++k) {
			func_145778_a(Item.getItemFromBlock(Blocks.red_flower), 1, 0.0F);
		}

		k = 3 + rand.nextInt(3);

		for (int l = 0; l < k; ++l) {
			dropItem(Items.iron_ingot, 1);
		}
	}

	public int getHoldRoseTick() {
		return holdRoseTick;
	}

	public boolean isPlayerCreated() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setPlayerCreated(boolean p_70849_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70849_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (!isPlayerCreated() && attackingPlayer != null && villageObj != null) {
			villageObj.setReputationForPlayer(attackingPlayer.getCommandSenderName(), -5);
		}

		super.onDeath(p_70645_1_);
	}
}