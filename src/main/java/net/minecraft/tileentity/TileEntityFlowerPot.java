package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityFlowerPot extends TileEntity {
	private Item flowerPotItem;
	private int flowerPotData;
	private static final String __OBFID = "CL_00000356";

	public TileEntityFlowerPot() {
	}

	public TileEntityFlowerPot(Item p_i45442_1_, int p_i45442_2_) {
		flowerPotItem = p_i45442_1_;
		flowerPotData = p_i45442_2_;
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("Item", Item.getIdFromItem(flowerPotItem));
		p_145841_1_.setInteger("Data", flowerPotData);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		flowerPotItem = Item.getItemById(p_145839_1_.getInteger("Item"));
		flowerPotData = p_145839_1_.getInteger("Data");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, nbttagcompound);
	}

	public void func_145964_a(Item p_145964_1_, int p_145964_2_) {
		flowerPotItem = p_145964_1_;
		flowerPotData = p_145964_2_;
	}

	public Item getFlowerPotItem() {
		return flowerPotItem;
	}

	public int getFlowerPotData() {
		return flowerPotData;
	}
}