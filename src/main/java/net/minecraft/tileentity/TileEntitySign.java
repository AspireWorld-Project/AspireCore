package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;

public class TileEntitySign extends TileEntity {
	public String[] signText = new String[] { "", "", "", "" };
	public int lineBeingEdited = -1;
	private boolean field_145916_j = true;
	private EntityPlayer field_145917_k;
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setString("Text1", signText[0]);
		p_145841_1_.setString("Text2", signText[1]);
		p_145841_1_.setString("Text3", signText[2]);
		p_145841_1_.setString("Text4", signText[3]);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		field_145916_j = false;
		super.readFromNBT(p_145839_1_);

		for (int i = 0; i < 4; ++i) {
			signText[i] = p_145839_1_.getString("Text" + (i + 1));

			if (signText[i].length() > 15) {
				signText[i] = signText[i].substring(0, 15);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		String[] astring = new String[4];
		System.arraycopy(signText, 0, astring, 0, 4);
		return new S33PacketUpdateSign(xCoord, yCoord, zCoord, astring);
	}

	public boolean func_145914_a() {
		return field_145916_j;
	}

	@SideOnly(Side.CLIENT)
	public void setEditable(boolean p_145913_1_) {
		field_145916_j = p_145913_1_;

		if (!p_145913_1_) {
			field_145917_k = null;
		}
	}

	public void func_145912_a(EntityPlayer p_145912_1_) {
		field_145917_k = p_145912_1_;
	}

	public EntityPlayer func_145911_b() {
		return field_145917_k;
	}
}