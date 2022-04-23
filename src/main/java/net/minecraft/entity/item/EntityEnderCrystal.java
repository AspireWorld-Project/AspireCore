package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityEnderCrystal extends Entity {
	public int innerRotation;
	public int health;
	private static final String __OBFID = "CL_00001658";

	public EntityEnderCrystal(World p_i1698_1_) {
		super(p_i1698_1_);
		preventEntitySpawning = true;
		setSize(2.0F, 2.0F);
		yOffset = height / 2.0F;
		health = 5;
		innerRotation = rand.nextInt(100000);
	}

	@SideOnly(Side.CLIENT)
	public EntityEnderCrystal(World p_i1699_1_, double p_i1699_2_, double p_i1699_4_, double p_i1699_6_) {
		this(p_i1699_1_);
		setPosition(p_i1699_2_, p_i1699_4_, p_i1699_6_);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(8, Integer.valueOf(health));
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		++innerRotation;
		dataWatcher.updateObject(8, Integer.valueOf(health));
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if (worldObj.provider instanceof WorldProviderEnd && worldObj.getBlock(i, j, k) != Blocks.fire) {
			if (!CraftEventFactory.callBlockIgniteEvent(worldObj, i, j, k, this).isCancelled()) {
				worldObj.setBlock(i, j, k, Blocks.fire);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			if (!isDead && !worldObj.isRemote) {
				if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, p_70097_1_, p_70097_2_))
					return false;
				health = 0;

				if (health <= 0) {
					setDead();

					if (!worldObj.isRemote) {
						worldObj.createExplosion((Entity) null, posX, posY, posZ, 6.0F, true);
					}
				}
			}

			return true;
		}
	}
}