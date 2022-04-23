package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

import java.util.List;

@SideOnly(Side.CLIENT)
public class CreativeCrafting implements ICrafting {
	private final Minecraft field_146109_a;
	private static final String __OBFID = "CL_00000751";

	public CreativeCrafting(Minecraft p_i1085_1_) {
		field_146109_a = p_i1085_1_;
	}

	@Override
	public void sendContainerAndContentsToPlayer(Container p_71110_1_, List p_71110_2_) {
	}

	@Override
	public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
		field_146109_a.playerController.sendSlotPacket(p_71111_3_, p_71111_2_);
	}

	@Override
	public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
	}
}