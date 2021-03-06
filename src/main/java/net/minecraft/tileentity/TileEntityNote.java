package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.NotePlayEvent;

public class TileEntityNote extends TileEntity {
	public byte note;
	public boolean previousRedstoneState;
	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setByte("note", note);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		note = p_145839_1_.getByte("note");

		if (note < 0) {
			note = 0;
		}

		if (note > 24) {
			note = 24;
		}
	}

	public void changePitch() {
		byte old = note;
		note = (byte) ((note + 1) % 25);
		if (!net.minecraftforge.common.ForgeHooks.onNoteChange(this, old))
			return;
		markDirty();
	}

	public void triggerNote(World p_145878_1_, int p_145878_2_, int p_145878_3_, int p_145878_4_) {
		if (p_145878_1_.getBlock(p_145878_2_, p_145878_3_ + 1, p_145878_4_).getMaterial() == Material.air) {
			Material material = p_145878_1_.getBlock(p_145878_2_, p_145878_3_ - 1, p_145878_4_).getMaterial();
			byte b0 = 0;

			if (material == Material.rock) {
				b0 = 1;
			}

			if (material == Material.sand) {
				b0 = 2;
			}

			if (material == Material.glass) {
				b0 = 3;
			}

			if (material == Material.wood) {
				b0 = 4;
			}

			NotePlayEvent event = CraftEventFactory.callNotePlayEvent(p_145878_1_, p_145878_2_, p_145878_3_,
					p_145878_4_, b0, note);
			if (!event.isCancelled()) {
				p_145878_1_.addBlockEvent(p_145878_2_, p_145878_3_, p_145878_4_, Blocks.noteblock,
						event.getInstrument().getType(), event.getNote().getId());
			}
		}
	}
}