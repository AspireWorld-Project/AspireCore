package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;

import java.io.IOException;

public class S00PacketServerInfo extends Packet {
	private static final Gson field_149297_a = new GsonBuilder()
			.registerTypeAdapter(ServerStatusResponse.MinecraftProtocolVersionIdentifier.class,
					new ServerStatusResponse.MinecraftProtocolVersionIdentifier.Serializer())
			.registerTypeAdapter(ServerStatusResponse.PlayerCountData.class,
					new ServerStatusResponse.PlayerCountData.Serializer())
			.registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponse.Serializer())
			.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer())
			.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer())
			.registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create();
	private ServerStatusResponse field_149296_b;
	private static final String __OBFID = "CL_00001384";

	public S00PacketServerInfo() {
	}

	public S00PacketServerInfo(ServerStatusResponse p_i45273_1_) {
		field_149296_b = p_i45273_1_;
	}

	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149296_b = field_149297_a.fromJson(p_148837_1_.readStringFromBuffer(32767), ServerStatusResponse.class);
	}

	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(field_149297_a.toJson(field_149296_b));
	}

	public void processPacket(INetHandlerStatusClient p_148833_1_) {
		p_148833_1_.handleServerInfo(this);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerStatusClient) p_148833_1_);
	}

	@SideOnly(Side.CLIENT)
	public ServerStatusResponse func_149294_c() {
		return field_149296_b;
	}
}