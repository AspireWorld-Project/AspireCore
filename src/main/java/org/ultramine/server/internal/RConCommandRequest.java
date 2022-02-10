package org.ultramine.server.internal;

import java.util.function.Supplier;

import net.minecraft.server.MinecraftServer;

public class RConCommandRequest implements Supplier<String> {
	private final String command;

	public RConCommandRequest(String command) {
		this.command = command;
	}

	@Override
	public String get() {
		return MinecraftServer.getServer().handleRConCommand(command);
	}
}
