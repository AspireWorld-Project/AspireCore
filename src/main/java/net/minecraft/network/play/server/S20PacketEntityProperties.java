package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;
import java.util.*;

public class S20PacketEntityProperties extends Packet {
	private int field_149445_a;
	private final List field_149444_b = new ArrayList();
	private static final String __OBFID = "CL_00001341";

	public S20PacketEntityProperties() {
	}

	public S20PacketEntityProperties(int p_i45236_1_, Collection p_i45236_2_) {
		field_149445_a = p_i45236_1_;
		Iterator iterator = p_i45236_2_.iterator();

		while (iterator.hasNext()) {
			IAttributeInstance iattributeinstance = (IAttributeInstance) iterator.next();
			field_149444_b.add(new S20PacketEntityProperties.Snapshot(
					iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(),
					iattributeinstance.func_111122_c()));
		}
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149445_a = p_148837_1_.readInt();
		int i = p_148837_1_.readInt();

		for (int j = 0; j < i; ++j) {
			String s = p_148837_1_.readStringFromBuffer(64);
			double d0 = p_148837_1_.readDouble();
			ArrayList arraylist = new ArrayList();
			short short1 = p_148837_1_.readShort();

			for (int k = 0; k < short1; ++k) {
				UUID uuid = new UUID(p_148837_1_.readLong(), p_148837_1_.readLong());
				arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", p_148837_1_.readDouble(),
						p_148837_1_.readByte()));
			}

			field_149444_b.add(new S20PacketEntityProperties.Snapshot(s, d0, arraylist));
		}
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149445_a);
		p_148840_1_.writeInt(field_149444_b.size());
		Iterator iterator = field_149444_b.iterator();

		while (iterator.hasNext()) {
			S20PacketEntityProperties.Snapshot snapshot = (S20PacketEntityProperties.Snapshot) iterator.next();
			p_148840_1_.writeStringToBuffer(snapshot.func_151409_a());
			p_148840_1_.writeDouble(snapshot.func_151410_b());
			p_148840_1_.writeShort(snapshot.func_151408_c().size());
			Iterator iterator1 = snapshot.func_151408_c().iterator();

			while (iterator1.hasNext()) {
				AttributeModifier attributemodifier = (AttributeModifier) iterator1.next();
				p_148840_1_.writeLong(attributemodifier.getID().getMostSignificantBits());
				p_148840_1_.writeLong(attributemodifier.getID().getLeastSignificantBits());
				p_148840_1_.writeDouble(attributemodifier.getAmount());
				p_148840_1_.writeByte(attributemodifier.getOperation());
			}
		}
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityProperties(this);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public int func_149442_c() {
		return field_149445_a;
	}

	@SideOnly(Side.CLIENT)
	public List func_149441_d() {
		return field_149444_b;
	}

	public class Snapshot {
		private final String field_151412_b;
		private final double field_151413_c;
		private final Collection field_151411_d;
		private static final String __OBFID = "CL_00001342";

		public Snapshot(String p_i45235_2_, double p_i45235_3_, Collection p_i45235_5_) {
			field_151412_b = p_i45235_2_;
			field_151413_c = p_i45235_3_;
			field_151411_d = p_i45235_5_;
		}

		public String func_151409_a() {
			return field_151412_b;
		}

		public double func_151410_b() {
			return field_151413_c;
		}

		public Collection func_151408_c() {
			return field_151411_d;
		}
	}
}