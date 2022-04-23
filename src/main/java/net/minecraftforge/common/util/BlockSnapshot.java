package net.minecraftforge.common.util;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.Serializable;

/**
 * Represents a captured snapshot of a block which will not change
 * automatically.
 * <p>
 * Unlike Block, which only one object can exist per coordinate, BlockSnapshot
 * can exist multiple times for any given Block.
 */
@SuppressWarnings("serial")
public class BlockSnapshot implements Serializable {
	private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("forge.debugBlockSnapshot", "false"));

	public final int x;
	public final int y;
	public final int z;
	public final int dimId;
	public transient Block replacedBlock;
	public final int meta;
	public int flag;
	private final NBTTagCompound nbt;
	public transient World world;
	public final UniqueIdentifier blockIdentifier;

	public BlockSnapshot(World world, int x, int y, int z, Block block, int meta) {
		this.world = world;
		dimId = world.provider.dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
		replacedBlock = block;
		blockIdentifier = GameRegistry.findUniqueIdentifierFor(block);
		this.meta = meta;
		flag = 3;
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null) {
			nbt = new NBTTagCompound();
			te.writeToNBT(nbt);
		} else {
			nbt = null;
		}
		if (DEBUG) {
			System.out.printf("Created BlockSnapshot - [World: %s ][Location: %d,%d,%d ][Block: %s ][Meta: %d ]",
					world.getWorldInfo().getWorldName(), x, y, z, block, meta);
		}
	}

	public BlockSnapshot(World world, int x, int y, int z, Block block, int meta, NBTTagCompound nbt) {
		this.world = world;
		dimId = world.provider.dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
		replacedBlock = block;
		blockIdentifier = GameRegistry.findUniqueIdentifierFor(block);
		this.meta = meta;
		flag = 3;
		this.nbt = nbt;
		if (DEBUG) {
			System.out.printf("Created BlockSnapshot - [World: %s ][Location: %d,%d,%d ][Block: %s ][Meta: %d ]",
					world.getWorldInfo().getWorldName(), x, y, z, block, meta);
		}
	}

	public BlockSnapshot(World world, int x, int y, int z, Block block, int meta, int flag) {
		this(world, x, y, z, block, meta);
		this.flag = flag;
	}

	/**
	 * Raw constructor designed for serialization usages.
	 */
	public BlockSnapshot(int dimension, int x, int y, int z, String modid, String blockName, int meta, int flag,
			NBTTagCompound nbt) {
		dimId = dimension;
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = meta;
		this.flag = flag;
		blockIdentifier = new UniqueIdentifier(modid + ":" + blockName);
		this.nbt = nbt;
	}

	public static BlockSnapshot getBlockSnapshot(World world, int x, int y, int z) {
		return new BlockSnapshot(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
	}

	public static BlockSnapshot getBlockSnapshot(World world, int x, int y, int z, int flag) {
		return new BlockSnapshot(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), flag);
	}

	public static BlockSnapshot readFromNBT(NBTTagCompound tag) {
		NBTTagCompound nbt = tag.getBoolean("hasTE") ? null : tag.getCompoundTag("tileEntity");

		return new BlockSnapshot(tag.getInteger("dimension"), tag.getInteger("posX"), tag.getInteger("posY"),
				tag.getInteger("posZ"), tag.getString("blockMod"), tag.getString("blockName"),
				tag.getInteger("metadata"), tag.getInteger("flag"), nbt);
	}

	public Block getCurrentBlock() {
		return world.getBlock(x, y, z);
	}

	public World getWorld() {
		if (world == null) {
			world = DimensionManager.getWorld(dimId);
		}
		return world;
	}

	public Block getReplacedBlock() {
		if (replacedBlock == null) {
			replacedBlock = GameRegistry.findBlock(blockIdentifier.modId, blockIdentifier.name);
		}
		return replacedBlock;
	}

	public TileEntity getTileEntity() {
		if (nbt != null)
			return TileEntity.createAndLoadEntity(nbt);
		else
			return null;
	}

	public boolean restore() {
		return restore(false);
	}

	public boolean restore(boolean force) {
		return restore(force, true);
	}

	public boolean restore(boolean force, boolean applyPhysics) {
		if (getCurrentBlock() != getReplacedBlock() || world.getBlockMetadata(x & 15, y, z & 15) != meta) {
			if (force) {
				world.setBlockSilently(x, y, z, getReplacedBlock(), meta, applyPhysics ? 3 : 2);
			} else
				return false;
		}

		world.setBlockMetadataWithNotify(x, y, z, meta, applyPhysics ? 3 : 2);
		world.markBlockForUpdate(x, y, z);
		TileEntity te = null;
		if (nbt != null) {
			te = world.getTileEntity(x, y, z);
			if (te != null) {
				te.readFromNBT(nbt);
			}
		}

		if (DEBUG) {
			System.out.printf(
					"Restored BlockSnapshot with data [World: %s ][Location: %d,%d,%d ][Meta: %d ][Block: %s ][TileEntity: %s ][force: %s ][applyPhysics: %s]",
					world.getWorldInfo().getWorldName(), x, y, z, meta, getReplacedBlock(), te, force, applyPhysics);
		}
		return true;
	}

	public boolean restoreToLocation(World world, int x, int y, int z, boolean force, boolean applyPhysics) {
		if (getCurrentBlock() != getReplacedBlock() || world.getBlockMetadata(x & 15, y, z & 15) != meta) {
			if (force) {
				world.setBlockSilently(x, y, z, getReplacedBlock(), meta, applyPhysics ? 3 : 2);
			} else
				return false;
		}

		world.setBlockMetadataWithNotify(x, y, z, meta, applyPhysics ? 3 : 2);
		world.markBlockForUpdate(x, y, z);
		TileEntity te = null;
		if (nbt != null) {
			te = world.getTileEntity(x, y, z);
			if (te != null) {
				te.readFromNBT(nbt);
			}
		}

		if (DEBUG) {
			System.out.printf(
					"Restored BlockSnapshot with data [World: %s ][Location: %d,%d,%d ][Meta: %d ][Block: %s ][TileEntity: %s ][force: %s ][applyPhysics: %s]",
					world.getWorldInfo().getWorldName(), x, y, z, meta, getReplacedBlock(), te, force, applyPhysics);
		}
		return true;
	}

	public void writeToNBT(NBTTagCompound compound) {
		compound.setString("blockMod", blockIdentifier.modId);
		compound.setString("blockName", blockIdentifier.name);
		compound.setInteger("posX", x);
		compound.setInteger("posY", y);
		compound.setInteger("posZ", z);
		compound.setInteger("flag", flag);
		compound.setInteger("dimension", dimId);
		compound.setInteger("metadata", meta);

		compound.setBoolean("hasTE", nbt != null);

		if (nbt != null) {
			compound.setTag("tileEntity", nbt);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BlockSnapshot other = (BlockSnapshot) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		if (meta != other.meta)
			return false;
		if (dimId != other.dimId)
			return false;
		if (nbt != other.nbt && (nbt == null || !nbt.equals(other.nbt)))
			return false;
		if (world != other.world && (world == null || !world.equals(other.world)))
			return false;
		if (blockIdentifier != other.blockIdentifier
				&& (blockIdentifier == null || !blockIdentifier.equals(other.blockIdentifier)))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 73 * hash + x;
		hash = 73 * hash + y;
		hash = 73 * hash + z;
		hash = 73 * hash + meta;
		hash = 73 * hash + dimId;
		hash = 73 * hash + (nbt != null ? nbt.hashCode() : 0);
		hash = 73 * hash + (world != null ? world.hashCode() : 0);
		hash = 73 * hash + (blockIdentifier != null ? blockIdentifier.hashCode() : 0);
		return hash;
	}
}