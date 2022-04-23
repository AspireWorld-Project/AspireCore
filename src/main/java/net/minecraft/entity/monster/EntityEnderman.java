package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;

import java.util.IdentityHashMap;
import java.util.UUID;

public class EntityEnderman extends EntityMob {
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(
			attackingSpeedBoostModifierUUID, "Attacking speed boost", 6.199999809265137D, 0).setSaved(false);
	@Deprecated // DO NOT TOUCH THIS EVER
	private static final boolean[] carriableBlocks = new boolean[256];
	private int teleportDelay;
	private int stareTimer;
	private Entity lastEntityToAttack;
	private boolean isAggressive;
	private static final String __OBFID = "CL_00001685";

	public EntityEnderman(World p_i1734_1_) {
		super(p_i1734_1_);
		setSize(0.6F, 2.9F);
		stepHeight = 1.0F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
		dataWatcher.addObject(17, new Byte((byte) 0));
		dataWatcher.addObject(18, new Byte((byte) 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setShort("carried", (short) Block.getIdFromBlock(func_146080_bZ()));
		p_70014_1_.setShort("carriedData", (short) getCarryingData());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		func_146081_a(Block.getBlockById(p_70037_1_.getShort("carried")));
		setCarryingData(p_70037_1_.getShort("carriedData"));
	}

	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);

		if (entityplayer != null) {
			if (shouldAttackPlayer(entityplayer)) {
				isAggressive = true;

				if (stareTimer == 0) {
					worldObj.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ,
							"mob.endermen.stare", 1.0F, 1.0F);
				}

				if (stareTimer++ == 5) {
					stareTimer = 0;
					setScreaming(true);
					return entityplayer;
				}
			} else {
				stareTimer = 0;
			}
		}

		return null;
	}

	private boolean shouldAttackPlayer(EntityPlayer p_70821_1_) {
		ItemStack itemstack = p_70821_1_.inventory.armorInventory[3];

		if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
			return false;
		else {
			Vec3 vec3 = p_70821_1_.getLook(1.0F).normalize();
			Vec3 vec31 = Vec3.createVectorHelper(posX - p_70821_1_.posX,
					boundingBox.minY + height / 2.0F - (p_70821_1_.posY + p_70821_1_.getEyeHeight()),
					posZ - p_70821_1_.posZ);
			double d0 = vec31.lengthVector();
			vec31 = vec31.normalize();
			double d1 = vec3.dotProduct(vec31);
			return d1 > 1.0D - 0.025D / d0 && p_70821_1_.canEntityBeSeen(this);
		}
	}

	private Block block;

	@Override
	public void onLivingUpdate() {
		if (isWet()) {
			attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (lastEntityToAttack != entityToAttack) {
			IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(attackingSpeedBoostModifier);

			if (entityToAttack != null) {
				iattributeinstance.applyModifier(attackingSpeedBoostModifier);
			}
		}

		lastEntityToAttack = entityToAttack;
		int k;

		if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
			int i;
			int j;
			Block block;

			if (func_146080_bZ().getMaterial() == Material.air) {
				if (rand.nextInt(20) == 0) {
					k = MathHelper.floor_double(posX - 2.0D + rand.nextDouble() * 4.0D);
					i = MathHelper.floor_double(posY + rand.nextDouble() * 3.0D);
					j = MathHelper.floor_double(posZ - 2.0D + rand.nextDouble() * 4.0D);
					block = worldObj.getBlock(k, i, j);

					if (EntityEnderman.getCarriable(block)) {
						if (this.block == null) {
							this.block = block;
						}
						func_146081_a(block);

						if (worldObj.getWorld() == null || !CraftEventFactory.callEntityChangeBlockEvent(this,
								worldObj.getWorld().getBlockAt(k, i, j), org.bukkit.Material.AIR).isCancelled()) // Cauldron
						{
							func_146081_a(block);
							setCarryingData(worldObj.getBlockMetadata(k, i, j));
							worldObj.setBlock(k, i, j, Blocks.air);
						}
					}
				}
			} else if (rand.nextInt(2000) == 0) {
				k = MathHelper.floor_double(posX - 1.0D + rand.nextDouble() * 2.0D);
				i = MathHelper.floor_double(posY + rand.nextDouble() * 2.0D);
				j = MathHelper.floor_double(posZ - 1.0D + rand.nextDouble() * 2.0D);
				block = worldObj.getBlock(k, i, j);
				Block block1 = worldObj.getBlock(k, i - 1, j);

				if (block.getMaterial() == Material.air && block1.getMaterial() != Material.air
						&& block1.renderAsNormalBlock()) {
					if (!CraftEventFactory
							.callEntityChangeBlockEvent(this, k, i, j, func_146080_bZ(), getCarryingData())
							.isCancelled()) {
						worldObj.setBlock(k, i, j, func_146080_bZ(), getCarryingData(), 3);
						func_146081_a(Blocks.air);
					}
					func_146081_a(Blocks.air);
				}
			}
		}

		for (k = 0; k < 2; ++k) {
			worldObj.spawnParticle("portal", posX + (rand.nextDouble() - 0.5D) * width,
					posY + rand.nextDouble() * height - 0.25D, posZ + (rand.nextDouble() - 0.5D) * width,
					(rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
		}

		if (worldObj.isDaytime() && !worldObj.isRemote) {
			float f = getBrightness(1.0F);

			if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ)) && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
				entityToAttack = null;
				setScreaming(false);
				isAggressive = false;
				teleportRandomly();
			}
		}

		if (isWet() || isBurning()) {
			entityToAttack = null;
			setScreaming(false);
			isAggressive = false;
			teleportRandomly();
		}

		if (isScreaming() && !isAggressive && rand.nextInt(100) == 0) {
			setScreaming(false);
		}

		isJumping = false;

		if (entityToAttack != null) {
			faceEntity(entityToAttack, 100.0F, 100.0F);
		}

		if (!worldObj.isRemote && isEntityAlive()) {
			if (entityToAttack != null) {
				if (entityToAttack instanceof EntityPlayer && shouldAttackPlayer((EntityPlayer) entityToAttack)) {
					if (entityToAttack.getDistanceSqToEntity(this) < 16.0D) {
						teleportRandomly();
					}

					teleportDelay = 0;
				} else if (entityToAttack.getDistanceSqToEntity(this) > 256.0D && teleportDelay++ >= 30
						&& teleportToEntity(entityToAttack)) {
					teleportDelay = 0;
				}
			} else {
				setScreaming(false);
				teleportDelay = 0;
			}
		}

		super.onLivingUpdate();
	}

	protected boolean teleportRandomly() {
		double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = posY + (rand.nextInt(64) - 32);
		double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	protected boolean teleportToEntity(Entity p_70816_1_) {
		Vec3 vec3 = Vec3.createVectorHelper(posX - p_70816_1_.posX,
				boundingBox.minY + height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(), posZ - p_70816_1_.posZ);
		vec3 = vec3.normalize();
		double d0 = 16.0D;
		double d1 = posX + (rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
		double d2 = posY + (rand.nextInt(16) - 8) - vec3.yCoord * d0;
		double d3 = posZ + (rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
		return this.teleportTo(d1, d2, d3);
	}

	protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
		EnderTeleportEvent event = new EnderTeleportEvent(this, p_70825_1_, p_70825_3_, p_70825_5_, 0);
		if (MinecraftForge.EVENT_BUS.post(event))
			return false;
		double d3 = posX;
		double d4 = posY;
		double d5 = posZ;
		posX = event.targetX;
		posY = event.targetY;
		posZ = event.targetZ;
		boolean flag = false;
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if (worldObj.blockExists(i, j, k)) {
			boolean flag1 = false;

			while (!flag1 && j > 0) {
				Block block = worldObj.getBlock(i, j - 1, k);

				if (block.getMaterial().blocksMovement()) {
					flag1 = true;
				} else {
					--posY;
					--j;
				}
			}

			if (flag1) {
				// CraftBukkit start - Teleport event
				EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(),
						new Location(this.worldObj.getWorld(), d3, d4, d5),
						new Location(this.worldObj.getWorld(), posX, posY, posZ));
				this.worldObj.getServer().getPluginManager().callEvent(teleport);
				if (teleport.isCancelled())
					return false;

				Location to = teleport.getTo();
				this.setPosition(to.getX(), to.getY(), to.getZ());
				// setPosition(posX, posY, posZ);

				// CraftBukkit end

				if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()
						&& !worldObj.isAnyLiquid(boundingBox)) {
					flag = true;
				}
			}
		}

		if (!flag) {
			setPosition(d3, d4, d5);
			return false;
		} else {
			short short1 = 128;

			for (int l = 0; l < short1; ++l) {
				double d6 = l / (short1 - 1.0D);
				float f = (rand.nextFloat() - 0.5F) * 0.2F;
				float f1 = (rand.nextFloat() - 0.5F) * 0.2F;
				float f2 = (rand.nextFloat() - 0.5F) * 0.2F;
				double d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				double d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height;
				double d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D;
				worldObj.spawnParticle("portal", d7, d8, d9, f, f1, f2);
			}

			worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
			playSound("mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}

	@Override
	protected String getLivingSound() {
		return isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.endermen.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.endermen.death";
	}

	@Override
	protected Item getDropItem() {
		return Items.ender_pearl;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		Item item = getDropItem();

		if (item != null) {
			int j = rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k) {
				dropItem(item, 1);
			}
		}
	}

	public void func_146081_a(Block p_146081_1_) {
		dataWatcher.updateObject(16, Byte.valueOf((byte) (Block.getIdFromBlock(p_146081_1_) & 255)));
	}

	public Block func_146080_bZ() {
		return Block.getBlockById(dataWatcher.getWatchableObjectByte(16));
	}

	public void setCarryingData(int p_70817_1_) {
		dataWatcher.updateObject(17, Byte.valueOf((byte) (p_70817_1_ & 255)));
	}

	public int getCarryingData() {
		return dataWatcher.getWatchableObjectByte(17);
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			setScreaming(true);

			if (p_70097_1_ instanceof EntityDamageSource && p_70097_1_.getEntity() instanceof EntityPlayer) {
				isAggressive = true;
			}

			if (p_70097_1_ instanceof EntityDamageSourceIndirect) {
				isAggressive = false;

				for (int i = 0; i < 64; ++i) {
					if (teleportRandomly())
						return true;
				}

				return super.attackEntityFrom(p_70097_1_, p_70097_2_);
			} else
				return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	public boolean isScreaming() {
		return dataWatcher.getWatchableObjectByte(18) > 0;
	}

	public void setScreaming(boolean p_70819_1_) {
		dataWatcher.updateObject(18, Byte.valueOf((byte) (p_70819_1_ ? 1 : 0)));
	}

	static {
		carriableBlocks[Block.getIdFromBlock(Blocks.grass)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.dirt)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.sand)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.gravel)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.yellow_flower)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.red_flower)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.brown_mushroom)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.red_mushroom)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.tnt)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.cactus)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.clay)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.pumpkin)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.melon_block)] = true;
		carriableBlocks[Block.getIdFromBlock(Blocks.mycelium)] = true;
		for (int x = 0; x < carriableBlocks.length; x++) {
			if (carriableBlocks[x]) {
				setCarriable(Block.getBlockById(x), true);
			}
		}
	}

	/*
	 * ===================================== Forge Start
	 * ==============================
	 */
	private static IdentityHashMap<Block, Boolean> carriable;

	public static void setCarriable(Block block, boolean canCarry) {
		if (carriable == null) {
			carriable = new IdentityHashMap(4096);
		}
		carriable.put(block, canCarry);
	}

	public static boolean getCarriable(Block block) {
		Boolean ret = carriable.get(block);
		return ret != null ? ret : false;
	}
	/*
	 * ===================================== Forge End
	 * ==============================
	 */
}