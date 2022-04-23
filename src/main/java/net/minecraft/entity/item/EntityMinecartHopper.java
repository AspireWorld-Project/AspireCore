package net.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {
	private boolean isBlocked = true;
	private int transferTicker = -1;
	private static final String __OBFID = "CL_00001676";

	public EntityMinecartHopper(World p_i1720_1_) {
		super(p_i1720_1_);
	}

	public EntityMinecartHopper(World p_i1721_1_, double p_i1721_2_, double p_i1721_4_, double p_i1721_6_) {
		super(p_i1721_1_, p_i1721_2_, p_i1721_4_, p_i1721_6_);
	}

	@Override
	public int getMinecartType() {
		return 5;
	}

	@Override
	public Block func_145817_o() {
		return Blocks.hopper;
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return 5;
	}

	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, p_130002_1_)))
			return true;
		if (!worldObj.isRemote) {
			p_130002_1_.displayGUIHopperMinecart(this);
		}

		return true;
	}

	@Override
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
		boolean flag1 = !p_96095_4_;

		if (flag1 != getBlocked()) {
			setBlocked(flag1);
		}
	}

	public boolean getBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean p_96110_1_) {
		isBlocked = p_96110_1_;
	}

	@Override
	public World getWorldObj() {
		return worldObj;
	}

	@Override
	public double getXPos() {
		return posX;
	}

	@Override
	public double getYPos() {
		return posY;
	}

	@Override
	public double getZPos() {
		return posZ;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isRemote && isEntityAlive() && getBlocked()) {
			--transferTicker;

			if (!canTransfer()) {
				setTransferTicker(0);

				if (func_96112_aD()) {
					setTransferTicker(4);
					markDirty();
				}
			}
		}
	}

	public boolean func_96112_aD() {
		if (TileEntityHopper.func_145891_a(this))
			return true;
		else {
			List list = worldObj.selectEntitiesWithinAABB(EntityItem.class, boundingBox.expand(0.25D, 0.0D, 0.25D),
					IEntitySelector.selectAnything);

			if (list.size() > 0) {
				TileEntityHopper.func_145898_a(this, (EntityItem) list.get(0));
			}

			return false;
		}
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);
		func_145778_a(Item.getItemFromBlock(Blocks.hopper), 1, 0.0F);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("TransferCooldown", transferTicker);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		transferTicker = p_70037_1_.getInteger("TransferCooldown");
	}

	public void setTransferTicker(int p_98042_1_) {
		transferTicker = p_98042_1_;
	}

	public boolean canTransfer() {
		return transferTicker > 0;
	}
}