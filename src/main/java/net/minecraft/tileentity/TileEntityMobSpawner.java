package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity {
	private final MobSpawnerBaseLogic field_145882_a = new MobSpawnerBaseLogic() {
		private static final String __OBFID = "CL_00000361";

		@Override
		public void func_98267_a(int p_98267_1_) {
			TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.xCoord,
					TileEntityMobSpawner.this.yCoord, TileEntityMobSpawner.this.zCoord, Blocks.mob_spawner, p_98267_1_,
					0);
		}

		@Override
		public World getSpawnerWorld() {
			return TileEntityMobSpawner.this.worldObj;
		}

		@Override
		public int getSpawnerX() {
			return TileEntityMobSpawner.this.xCoord;
		}

		@Override
		public int getSpawnerY() {
			return TileEntityMobSpawner.this.yCoord;
		}

		@Override
		public int getSpawnerZ() {
			return TileEntityMobSpawner.this.zCoord;
		}

		@Override
		public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
			super.setRandomEntity(p_98277_1_);

			if (getSpawnerWorld() != null) {
				getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.xCoord, TileEntityMobSpawner.this.yCoord,
						TileEntityMobSpawner.this.zCoord);
			}
		}
	};
	private static final String __OBFID = "CL_00000360";

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145882_a.readFromNBT(p_145839_1_);
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		field_145882_a.writeToNBT(p_145841_1_);
	}

	@Override
	public void updateEntity() {
		field_145882_a.updateSpawner();
		super.updateEntity();
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		nbttagcompound.removeTag("SpawnPotentials");
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbttagcompound);
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		return field_145882_a.setDelayToMin(p_145842_1_) || super.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	public MobSpawnerBaseLogic func_145881_a() {
		return field_145882_a;
	}
}