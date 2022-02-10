package net.minecraft.server.network;

import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
	private final MinecraftServer field_147387_a;
	private final NetworkManager field_147386_b;
	private static final String __OBFID = "CL_00001456";

	public NetHandlerHandshakeTCP(MinecraftServer p_i45295_1_, NetworkManager p_i45295_2_) {
		field_147387_a = p_i45295_1_;
		field_147386_b = p_i45295_2_;
	}

	@Override
	public void processHandshake(C00Handshake p_147383_1_) {
		if (!FMLCommonHandler.instance().shouldAllowPlayerLogins()) {
			ChatComponentText chatcomponenttext = new ChatComponentText(
					"Server is still starting! Please wait before reconnecting.");
			field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext),
					new GenericFutureListener[0]);
			field_147386_b.closeChannel(chatcomponenttext);
			return;
		}

		switch (NetHandlerHandshakeTCP.SwitchEnumConnectionState.field_151291_a[p_147383_1_.func_149594_c()
				.ordinal()]) {
		case 1:
			field_147386_b.setConnectionState(EnumConnectionState.LOGIN);
			ChatComponentText chatcomponenttext;

			if (p_147383_1_.func_149595_d() > 5) {
				chatcomponenttext = new ChatComponentText("Outdated server! I\'m still on 1.7.10");
				field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext),
						new GenericFutureListener[0]);
				field_147386_b.closeChannel(chatcomponenttext);
			} else if (p_147383_1_.func_149595_d() < 5) {
				chatcomponenttext = new ChatComponentText("Outdated client! Please use 1.7.10");
				field_147386_b.scheduleOutboundPacket(new S00PacketDisconnect(chatcomponenttext),
						new GenericFutureListener[0]);
				field_147386_b.closeChannel(chatcomponenttext);
			} else {
				field_147386_b.setNetHandler(new NetHandlerLoginServer(field_147387_a, field_147386_b));
			}

			break;
		case 2:
			field_147386_b.setConnectionState(EnumConnectionState.STATUS);
			field_147386_b.setNetHandler(new NetHandlerStatusServer(field_147387_a, field_147386_b));
			break;
		default:
			throw new UnsupportedOperationException("Invalid intention " + p_147383_1_.func_149594_c());
		}
	}

	@Override
	public void onDisconnect(IChatComponent p_147231_1_) {
	}

	@Override
	public void onConnectionStateTransition(EnumConnectionState p_147232_1_, EnumConnectionState p_147232_2_) {
		if (p_147232_2_ != EnumConnectionState.LOGIN && p_147232_2_ != EnumConnectionState.STATUS)
			throw new UnsupportedOperationException("Invalid state " + p_147232_2_);
	}

	@Override
	public void onNetworkTick() {
	}

	static final class SwitchEnumConnectionState {
		static final int[] field_151291_a = new int[EnumConnectionState.values().length];
		private static final String __OBFID = "CL_00001457";

		static {
			try {
				field_151291_a[EnumConnectionState.LOGIN.ordinal()] = 1;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				field_151291_a[EnumConnectionState.STATUS.ordinal()] = 2;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}
}