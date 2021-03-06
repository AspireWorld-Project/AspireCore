package net.minecraft.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerCommand {
	public final String command;
	public final ICommandSender sender;
	private static final String __OBFID = "CL_00001779";

	public ServerCommand(String p_i1491_1_, ICommandSender p_i1491_2_) {
		command = p_i1491_1_;
		sender = p_i1491_2_;
	}
}