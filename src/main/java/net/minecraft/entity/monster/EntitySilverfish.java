package net.minecraft.entity.monster;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob {
	private int allySummonCooldown;
	private static final String __OBFID = "CL_00001696";

	public EntitySilverfish(World p_i1740_1_) {
		super(p_i1740_1_);
		setSize(0.3F, 0.7F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected Entity findPlayerToAttack() {
		double d0 = 8.0D;
		return worldObj.getClosestVulnerablePlayerToEntity(this, d0);
	}

	@Override
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			if (allySummonCooldown <= 0
					&& (p_70097_1_ instanceof EntityDamageSource || p_70097_1_ == DamageSource.magic)) {
				allySummonCooldown = 20;
			}

			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (attackTime <= 0 && p_70785_2_ < 1.2F && p_70785_1_.boundingBox.maxY > boundingBox.minY
				&& p_70785_1_.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(p_70785_1_);
		}
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	@Override
	protected Item getDropItem() {
		return Item.getItemById(0);
	}

	@Override
	public void onUpdate() {
		renderYawOffset = rotationYaw;
		super.onUpdate();
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if (!worldObj.isRemote) {
			int i;
			int j;
			int k;
			int i1;
			if (allySummonCooldown > 0) {
				--allySummonCooldown;
				if (allySummonCooldown == 0) {
					i = MathHelper.floor_double(posX);
					j = MathHelper.floor_double(posY);
					k = MathHelper.floor_double(posZ);
					boolean flag = false;
					for (int l = 0; !flag && l <= 5 && l >= -5; l = l <= 0 ? 1 - l : 0 - l) {
						for (i1 = 0; !flag && i1 <= 10 && i1 >= -10; i1 = i1 <= 0 ? 1 - i1 : 0 - i1) {
							for (int j1 = 0; !flag && j1 <= 10 && j1 >= -10; j1 = j1 <= 0 ? 1 - j1 : 0 - j1)
								if (worldObj.getBlock(i + i1, j + l, k + j1) == Blocks.monster_egg) {
									if (CraftEventFactory
											.callEntityChangeBlockEvent(this, i + i1, j + l, k + j1, Blocks.air, 0)
											.isCancelled()) {
										continue;
									}
									if (!worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
										int k1 = worldObj.getBlockMetadata(i + i1, j + l, k + j1);
										ImmutablePair immutablepair = BlockSilverfish.func_150197_b(k1);
										worldObj.setBlock(i + i1, j + l, k + j1, (Block) immutablepair.getLeft(),
												(Integer) immutablepair.getRight(), 3);
									} else {
										worldObj.func_147480_a(i + i1, j + l, k + j1, false);
									}
									Blocks.monster_egg.onBlockDestroyedByPlayer(worldObj, i + i1, j + l, k + j1, 0);
									if (rand.nextBoolean()) {
										flag = true;
										break;
									}
								}
						}
					}
				}
			}
			if (entityToAttack == null && !hasPath()) {
				i = MathHelper.floor_double(posX);
				j = MathHelper.floor_double(posY + 0.5D);
				k = MathHelper.floor_double(posZ);
				int l1 = rand.nextInt(6);
				Block block = worldObj.getBlock(i + Facing.offsetsXForSide[l1], j + Facing.offsetsYForSide[l1],
						k + Facing.offsetsZForSide[l1]);
				i1 = worldObj.getBlockMetadata(i + Facing.offsetsXForSide[l1], j + Facing.offsetsYForSide[l1],
						k + Facing.offsetsZForSide[l1]);
				if (BlockSilverfish.func_150196_a(block)) {
					if (CraftEventFactory.callEntityChangeBlockEvent(this, i + Facing.offsetsXForSide[l1],
							j + Facing.offsetsYForSide[l1], k + Facing.offsetsZForSide[l1], Blocks.monster_egg,
							Block.getIdFromBlock(Block.getBlockById(i1))).isCancelled())
						return;
					worldObj.setBlock(i + Facing.offsetsXForSide[l1], j + Facing.offsetsYForSide[l1],
							k + Facing.offsetsZForSide[l1], Blocks.monster_egg,
							BlockSilverfish.func_150195_a(block, i1), 3);
					spawnExplosionParticle();
					setDead();
				} else {
					updateWanderPath();
				}
			} else if (entityToAttack != null && !hasPath()) {
				entityToAttack = null;
			}
		}
	}

	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
		return worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.stone ? 10.0F
				: super.getBlockPathWeight(p_70783_1_, p_70783_2_, p_70783_3_);
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		} else
			return false;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}