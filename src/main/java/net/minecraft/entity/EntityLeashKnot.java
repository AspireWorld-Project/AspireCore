package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging {
	private static final String __OBFID = "CL_00001548";

	public EntityLeashKnot(World p_i1592_1_) {
		super(p_i1592_1_);
	}

	public EntityLeashKnot(World p_i1593_1_, int p_i1593_2_, int p_i1593_3_, int p_i1593_4_) {
		super(p_i1593_1_, p_i1593_2_, p_i1593_3_, p_i1593_4_, 0);
		setPosition(p_i1593_2_ + 0.5D, p_i1593_3_ + 0.5D, p_i1593_4_ + 0.5D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void setDirection(int p_82328_1_) {
	}

	@Override
	public int getWidthPixels() {
		return 9;
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return p_70112_1_ < 1024.0D;
	}

	@Override
	public void onBroken(Entity p_110128_1_) {
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		ItemStack itemstack = p_130002_1_.getHeldItem();
		boolean flag = false;
		double d0;
		List list;
		Iterator iterator;
		EntityLiving entityliving;

		if (itemstack != null && itemstack.getItem() == Items.lead && !worldObj.isRemote) {
			d0 = 7.0D;
			list = worldObj.getEntitiesWithinAABB(EntityLiving.class,
					AxisAlignedBB.getBoundingBox(posX - d0, posY - d0, posZ - d0, posX + d0, posY + d0, posZ + d0));

			if (list != null) {
				iterator = list.iterator();

				while (iterator.hasNext()) {
					entityliving = (EntityLiving) iterator.next();

					if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == p_130002_1_) {
						// CraftBukkit start
						if (CraftEventFactory.callPlayerLeashEntityEvent(entityliving, this, p_130002_1_)
								.isCancelled()) {
							((EntityPlayerMP) p_130002_1_).playerNetServerHandler.sendPacket(
									new S1BPacketEntityAttach(1, entityliving, entityliving.getLeashedToEntity()));
							continue;
						}
						// CraftBukkit end
						entityliving.setLeashedToEntity(this, true);
						flag = true;
					}
				}
			}
		}

		if (!worldObj.isRemote && !flag) {
			// setDead();

			boolean die = true;

			if (/* p_130002_1_.capabilities.isCreativeMode */ true) {
				d0 = 7.0D;
				list = worldObj.getEntitiesWithinAABB(EntityLiving.class,
						AxisAlignedBB.getBoundingBox(posX - d0, posY - d0, posZ - d0, posX + d0, posY + d0, posZ + d0));

				if (list != null) {
					iterator = list.iterator();

					while (iterator.hasNext()) {
						entityliving = (EntityLiving) iterator.next();

						if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == this) {
							if (CraftEventFactory.callPlayerUnleashEntityEvent(entityliving, p_130002_1_)
									.isCancelled()) {
								die = false;
								continue;
							}
							entityliving.clearLeashed(true, !p_130002_1_.capabilities.isCreativeMode);
						}
					}
				}
			}

			if (die) {
				setDead();
			}
		}

		return true;
	}

	@Override
	public boolean onValidSurface() {
		return worldObj.getBlock(field_146063_b, field_146064_c, field_146062_d).getRenderType() == 11;
	}

	public static EntityLeashKnot func_110129_a(World p_110129_0_, int p_110129_1_, int p_110129_2_, int p_110129_3_) {
		EntityLeashKnot entityleashknot = new EntityLeashKnot(p_110129_0_, p_110129_1_, p_110129_2_, p_110129_3_);
		entityleashknot.forceSpawn = true;
		p_110129_0_.spawnEntityInWorld(entityleashknot);
		return entityleashknot;
	}

	public static EntityLeashKnot getKnotForBlock(World p_110130_0_, int p_110130_1_, int p_110130_2_,
			int p_110130_3_) {
		List list = p_110130_0_.getEntitiesWithinAABB(EntityLeashKnot.class,
				AxisAlignedBB.getBoundingBox(p_110130_1_ - 1.0D, p_110130_2_ - 1.0D, p_110130_3_ - 1.0D,
						p_110130_1_ + 1.0D, p_110130_2_ + 1.0D, p_110130_3_ + 1.0D));

		if (list != null) {
			Iterator iterator = list.iterator();

			while (iterator.hasNext()) {
				EntityLeashKnot entityleashknot = (EntityLeashKnot) iterator.next();

				if (entityleashknot.field_146063_b == p_110130_1_ && entityleashknot.field_146064_c == p_110130_2_
						&& entityleashknot.field_146062_d == p_110130_3_)
					return entityleashknot;
			}
		}

		return null;
	}
}