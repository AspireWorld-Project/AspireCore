package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart {
	private static final String __OBFID = "CL_00001677";

	public EntityMinecartEmpty(World p_i1722_1_) {
		super(p_i1722_1_);
	}

	public EntityMinecartEmpty(World p_i1723_1_, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_) {
		super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_)))
			return true;
		if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != p_130002_1_)
			return true;
		else if (riddenByEntity != null && riddenByEntity != p_130002_1_)
			return false;
		else {
			if (!worldObj.isRemote) {
				p_130002_1_.mountEntity(this);
			}

			return true;
		}
	}

	@Override
	public int getMinecartType() {
		return 0;
	}
}