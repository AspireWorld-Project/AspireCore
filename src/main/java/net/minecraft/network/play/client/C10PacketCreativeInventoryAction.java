package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C10PacketCreativeInventoryAction extends Packet {
	private int field_149629_a;
	private ItemStack field_149628_b;
	private static final String __OBFID = "CL_00001369";

	public C10PacketCreativeInventoryAction() {
	}

	@SideOnly(Side.CLIENT)
	public C10PacketCreativeInventoryAction(int p_i45263_1_, ItemStack p_i45263_2_) {
		field_149629_a = p_i45263_1_;
		field_149628_b = p_i45263_2_ != null ? p_i45263_2_.copy() : null;
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processCreativeInventoryAction(this);
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149629_a = p_148837_1_.readShort();
		field_149628_b = p_148837_1_.readItemStackFromBuffer();
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeShort(field_149629_a);
		p_148840_1_.writeItemStackToBuffer(field_149628_b);
	}

	public int func_149627_c() {
		return field_149629_a;
	}

	public ItemStack func_149625_d() {
		return field_149628_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}
}