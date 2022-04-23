package net.minecraft.server.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.swing.*;
import java.util.Vector;

@SideOnly(Side.SERVER)
public class PlayerListComponent extends JList implements IUpdatePlayerListBox {
	private final MinecraftServer field_120015_a;
	private int field_120014_b;
	private static final String __OBFID = "CL_00001795";

	public PlayerListComponent(MinecraftServer p_i2366_1_) {
		field_120015_a = p_i2366_1_;
		p_i2366_1_.func_82010_a(this);
	}

	@Override
	public void update() {
		if (field_120014_b++ % 20 == 0) {
			Vector vector = new Vector();

			for (int i = 0; i < field_120015_a.getConfigurationManager().playerEntityList.size(); ++i) {
				vector.add(((EntityPlayerMP) field_120015_a.getConfigurationManager().playerEntityList.get(i))
						.getCommandSenderName());
			}

			this.setListData(vector);
		}
	}
}