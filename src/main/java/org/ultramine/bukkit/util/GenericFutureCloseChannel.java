package org.ultramine.bukkit.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;

// This class exists, because anonymous classes are not allowed in a @Mixin classes.
@SuppressWarnings("rawtypes")
public class GenericFutureCloseChannel implements GenericFutureListener {
	private final NetworkManager networkmanager;
	private final ChatComponentText chatComponentText;

	public GenericFutureCloseChannel(NetworkManager networkManager, ChatComponentText chatComponentText) {
		networkmanager = networkManager;
		this.chatComponentText = chatComponentText;
	}

	@Override
	public void operationComplete(Future future) throws Exception {
		networkmanager.closeChannel(chatComponentText);
	}
}
