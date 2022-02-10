package org.bukkit.craftbukkit.block;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.NoteBlock;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

import net.minecraft.tileentity.TileEntityNote;

public class CraftNoteBlock extends CraftBlockState implements NoteBlock {
	private final CraftWorld world;
	private final TileEntityNote note;

	public CraftNoteBlock(final Block block) {
		super(block);

		world = (CraftWorld) block.getWorld();
		note = (TileEntityNote) world.getTileEntityAt(getX(), getY(), getZ());
	}

	@Override
	public Note getNote() {
		return new Note(note.note);
	}

	@Override
	public byte getRawNote() {
		return note.note;
	}

	@Override
	public void setNote(Note n) {
		note.note = n.getId();
	}

	@Override
	public void setRawNote(byte n) {
		note.note = n;
	}

	@Override
	public boolean play() {
		Block block = getBlock();

		if (block.getType() == Material.NOTE_BLOCK) {
			note.triggerNote(world.getHandle(), getX(), getY(), getZ());
			return true;
		} else
			return false;
	}

	@Override
	public boolean play(byte instrument, byte note) {
		Block block = getBlock();

		if (block.getType() == Material.NOTE_BLOCK) {
			world.getHandle().addBlockEvent(getX(), getY(), getZ(), CraftMagicNumbers.getBlock(block), instrument,
					note);
			return true;
		} else
			return false;
	}

	@Override
	public boolean play(Instrument instrument, Note note) {
		Block block = getBlock();

		if (block.getType() == Material.NOTE_BLOCK) {
			world.getHandle().addBlockEvent(getX(), getY(), getZ(), CraftMagicNumbers.getBlock(block),
					instrument.getType(), note.getId());
			return true;
		} else
			return false;
	}
}
