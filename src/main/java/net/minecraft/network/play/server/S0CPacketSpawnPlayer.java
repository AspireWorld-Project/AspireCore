package net.minecraft.network.play.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class S0CPacketSpawnPlayer extends Packet {
	private int field_148957_a;
	private GameProfile field_148955_b;
	private int field_148956_c;
	private int field_148953_d;
	private int field_148954_e;
	private byte field_148951_f;
	private byte field_148952_g;
	private int field_148959_h;
	private DataWatcher field_148960_i;
	private List field_148958_j;
	private static final String __OBFID = "CL_00001281";

	public S0CPacketSpawnPlayer() {
	}

	public S0CPacketSpawnPlayer(EntityPlayer p_i45171_1_) {
		field_148957_a = p_i45171_1_.getEntityId();
		field_148955_b = p_i45171_1_.getGameProfile();
		field_148956_c = MathHelper.floor_double(p_i45171_1_.posX * 32.0D);
		field_148953_d = MathHelper.floor_double(p_i45171_1_.posY * 32.0D);
		field_148954_e = MathHelper.floor_double(p_i45171_1_.posZ * 32.0D);
		field_148951_f = (byte) (int) (p_i45171_1_.rotationYaw * 256.0F / 360.0F);
		field_148952_g = (byte) (int) (p_i45171_1_.rotationPitch * 256.0F / 360.0F);
		ItemStack itemstack = p_i45171_1_.inventory.getCurrentItem();
		field_148959_h = itemstack == null ? 0 : Item.getIdFromItem(itemstack.getItem());
		field_148960_i = p_i45171_1_.getDataWatcher();
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148957_a = p_148837_1_.readVarIntFromBuffer();
		UUID uuid = UUID.fromString(p_148837_1_.readStringFromBuffer(36));
		field_148955_b = new GameProfile(uuid, p_148837_1_.readStringFromBuffer(16));
		int i = p_148837_1_.readVarIntFromBuffer();

		for (int j = 0; j < i; ++j) {
			String s = p_148837_1_.readStringFromBuffer(32767);
			String s1 = p_148837_1_.readStringFromBuffer(32767);
			String s2 = p_148837_1_.readStringFromBuffer(32767);
			field_148955_b.getProperties().put(s, new Property(s, s1, s2));
		}

		field_148956_c = p_148837_1_.readInt();
		field_148953_d = p_148837_1_.readInt();
		field_148954_e = p_148837_1_.readInt();
		field_148951_f = p_148837_1_.readByte();
		field_148952_g = p_148837_1_.readByte();
		field_148959_h = p_148837_1_.readShort();
		field_148958_j = DataWatcher.readWatchedListFromPacketBuffer(p_148837_1_);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148957_a);
		UUID uuid = field_148955_b.getId();
		p_148840_1_.writeStringToBuffer(uuid == null ? "" : uuid.toString());
		p_148840_1_.writeStringToBuffer(field_148955_b.getName());
		p_148840_1_.writeVarIntToBuffer(field_148955_b.getProperties().size());
		Iterator iterator = field_148955_b.getProperties().values().iterator();

		while (iterator.hasNext()) {
			Property property = (Property) iterator.next();
			p_148840_1_.writeStringToBuffer(property.getName());
			p_148840_1_.writeStringToBuffer(property.getValue());
			p_148840_1_.writeStringToBuffer(property.getSignature());
		}

		p_148840_1_.writeInt(field_148956_c);
		p_148840_1_.writeInt(field_148953_d);
		p_148840_1_.writeInt(field_148954_e);
		p_148840_1_.writeByte(field_148951_f);
		p_148840_1_.writeByte(field_148952_g);
		p_148840_1_.writeShort(field_148959_h);
		field_148960_i.func_151509_a(p_148840_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSpawnPlayer(this);
	}

	@SideOnly(Side.CLIENT)
	public List func_148944_c() {
		if (field_148958_j == null) {
			field_148958_j = field_148960_i.getAllWatched();
		}

		return field_148958_j;
	}

	@Override
	public String serialize() {
		return String.format("id=%d, gameProfile='%s', x=%.2f, y=%.2f, z=%.2f, carried=%d",
				Integer.valueOf(field_148957_a), field_148955_b, Float.valueOf(field_148956_c / 32.0F),
				Float.valueOf(field_148953_d / 32.0F), Float.valueOf(field_148954_e / 32.0F),
				Integer.valueOf(field_148959_h));
	}

	@SideOnly(Side.CLIENT)
	public int func_148943_d() {
		return field_148957_a;
	}

	@SideOnly(Side.CLIENT)
	public GameProfile func_148948_e() {
		return field_148955_b;
	}

	@SideOnly(Side.CLIENT)
	public int func_148942_f() {
		return field_148956_c;
	}

	@SideOnly(Side.CLIENT)
	public int func_148949_g() {
		return field_148953_d;
	}

	@SideOnly(Side.CLIENT)
	public int func_148946_h() {
		return field_148954_e;
	}

	@SideOnly(Side.CLIENT)
	public byte func_148941_i() {
		return field_148951_f;
	}

	@SideOnly(Side.CLIENT)
	public byte func_148945_j() {
		return field_148952_g;
	}

	@SideOnly(Side.CLIENT)
	public int func_148947_k() {
		return field_148959_h;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}
}