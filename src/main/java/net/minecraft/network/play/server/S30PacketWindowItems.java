package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;
import java.util.List;

public class S30PacketWindowItems extends Packet {
	private int field_148914_a;
	private ItemStack[] field_148913_b;
	private static final String __OBFID = "CL_00001294";

	public S30PacketWindowItems() {
	}

	public S30PacketWindowItems(int p_i45186_1_, List p_i45186_2_) {
		field_148914_a = p_i45186_1_;
		field_148913_b = new ItemStack[p_i45186_2_.size()];

		for (int j = 0; j < field_148913_b.length; ++j) {
			ItemStack itemstack = (ItemStack) p_i45186_2_.get(j);
			field_148913_b[j] = itemstack == null ? null : itemstack.copy();
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148914_a = p_148837_1_.readUnsignedByte();
		short short1 = p_148837_1_.readShort();
		field_148913_b = new ItemStack[short1];

		for (int i = 0; i < short1; ++i) {
			field_148913_b[i] = p_148837_1_.readItemStackFromBuffer();
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_148914_a);
		p_148840_1_.writeShort(field_148913_b.length);
		ItemStack[] aitemstack = field_148913_b;
		int i = aitemstack.length;

		for (int j = 0; j < i; ++j) {
			ItemStack itemstack = aitemstack[j];
			p_148840_1_.writeItemStackToBuffer(itemstack);
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleWindowItems(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_148911_c() {
		return field_148914_a;
	}

	@SideOnly(Side.CLIENT)
	public ItemStack[] func_148910_d() {
		return field_148913_b;
	}
}