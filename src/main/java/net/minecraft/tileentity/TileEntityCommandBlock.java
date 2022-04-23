package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {
	private final CommandBlockLogic field_145994_a = new CommandBlockLogic() {
		@Override
		public ChunkCoordinates getPlayerCoordinates() {
			return new ChunkCoordinates(TileEntityCommandBlock.this.xCoord, TileEntityCommandBlock.this.yCoord,
					TileEntityCommandBlock.this.zCoord);
		}

		@Override
		public World getEntityWorld() {
			return TileEntityCommandBlock.this.getWorldObj();
		}

		@Override
		public void func_145752_a(String p_145752_1_) {
			super.func_145752_a(p_145752_1_);
			TileEntityCommandBlock.this.markDirty();
		}

		@Override
		public void func_145756_e() {
			TileEntityCommandBlock.this.getWorldObj().markBlockForUpdate(TileEntityCommandBlock.this.xCoord,
					TileEntityCommandBlock.this.yCoord, TileEntityCommandBlock.this.zCoord);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int func_145751_f() {
			return 0;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void func_145757_a(ByteBuf p_145757_1_) {
			p_145757_1_.writeInt(TileEntityCommandBlock.this.xCoord);
			p_145757_1_.writeInt(TileEntityCommandBlock.this.yCoord);
			p_145757_1_.writeInt(TileEntityCommandBlock.this.zCoord);
		}
	};
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		field_145994_a.func_145758_a(p_145841_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145994_a.func_145759_b(p_145839_1_);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 2, nbttagcompound);
	}

	public CommandBlockLogic func_145993_a() {
		return field_145994_a;
	}
}