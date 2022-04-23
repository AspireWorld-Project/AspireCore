package net.minecraft.tileentity;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ultramine.server.internal.UMHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class TileEntity {
	private static final Logger logger = LogManager.getLogger();
	private static final Map nameToClassMap = new HashMap();
	private static final Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	protected boolean tileEntityInvalid;
	public int blockMetadata = -1;
	public Block blockType;
	private static final String __OBFID = "CL_00000340";

	public static void addMapping(Class p_145826_0_, String p_145826_1_) {
		if (nameToClassMap.containsKey(p_145826_1_))
			throw new IllegalArgumentException("Duplicate id: " + p_145826_1_);
		else {
			nameToClassMap.put(p_145826_1_, p_145826_0_);
			classToNameMap.put(p_145826_0_, p_145826_1_);
		}
	}

	public World getWorldObj() {
		return worldObj;
	}

	public void setWorldObj(World p_145834_1_) {
		worldObj = p_145834_1_;
	}

	public boolean hasWorldObj() {
		return worldObj != null;
	}

	public void readFromNBT(NBTTagCompound p_145839_1_) {
		if (owner == null) {
			owner = UMHooks.readObjectOwner(p_145839_1_);
		}
		xCoord = p_145839_1_.getInteger("x");
		yCoord = p_145839_1_.getInteger("y");
		zCoord = p_145839_1_.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound p_145841_1_) {
		String s = (String) classToNameMap.get(this.getClass());

		if (s == null)
			throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
		else {
			if (owner != null) {
				UMHooks.writeObjectOwner(p_145841_1_, owner);
			}
			p_145841_1_.setString("id", s);
			p_145841_1_.setInteger("x", xCoord);
			p_145841_1_.setInteger("y", yCoord);
			p_145841_1_.setInteger("z", zCoord);
		}
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound p_145827_0_) {
		TileEntity tileentity = null;

		Class oclass = null;
		try {
			oclass = (Class) nameToClassMap.get(p_145827_0_.getString("id"));

			if (oclass != null) {
				tileentity = (TileEntity) oclass.newInstance();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (tileentity != null) {
			try {
				tileentity.readFromNBT(p_145827_0_);
			} catch (Exception ex) {
				FMLLog.log(Level.ERROR, ex,
						"A TileEntity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
						p_145827_0_.getString("id"), oclass.getName());
				tileentity = null;
			}
		} else {
			logger.debug("Skipping BlockEntity with id " + p_145827_0_.getString("id"));
		}

		return tileentity;
	}

	public int getBlockMetadata() {
		if (blockMetadata == -1) {
			blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}

		return blockMetadata;
	}

	public void markDirty() {
		if (worldObj != null) {
			blockMetadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);

			if (getBlockType() != Blocks.air) {
				worldObj.func_147453_f(xCoord, yCoord, zCoord, getBlockType());
			}
		}
	}

	public double getDistanceFrom(double p_145835_1_, double p_145835_3_, double p_145835_5_) {
		double d3 = xCoord + 0.5D - p_145835_1_;
		double d4 = yCoord + 0.5D - p_145835_3_;
		double d5 = zCoord + 0.5D - p_145835_5_;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 4096.0D;
	}

	public Block getBlockType() {
		if (blockType == null) {
			blockType = worldObj.getBlock(xCoord, yCoord, zCoord);
		}

		return blockType;
	}

	public Packet getDescriptionPacket() {
		return null;
	}

	public boolean isInvalid() {
		return tileEntityInvalid;
	}

	public void invalidate() {
		tileEntityInvalid = true;
	}

	public void validate() {
		tileEntityInvalid = false;
	}

	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		return false;
	}

	public void updateContainingBlockInfo() {
		blockType = null;
		blockMetadata = -1;
	}

	public void func_145828_a(CrashReportCategory p_145828_1_) {
		p_145828_1_.addCrashSectionCallable("Name", new Callable() {
			private static final String __OBFID = "CL_00000341";

			@Override
			public String call() {
				return TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // "
						+ TileEntity.this.getClass().getCanonicalName();
			}
		});
		CrashReportCategory.func_147153_a(p_145828_1_, xCoord, yCoord, zCoord, getBlockType(), getBlockMetadata());
		p_145828_1_.addCrashSectionCallable("Actual block type", new Callable() {
			private static final String __OBFID = "CL_00000343";

			@Override
			public String call() {
				int i = Block.getIdFromBlock(worldObj.getBlock(xCoord, yCoord, zCoord));

				try {
					return String.format("ID #%d (%s // %s)",
							Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(),
							Block.getBlockById(i).getClass().getCanonicalName());
				} catch (Throwable throwable) {
					return "ID #" + i;
				}
			}
		});
		p_145828_1_.addCrashSectionCallable("Actual block data value", new Callable() {
			private static final String __OBFID = "CL_00000344";

			@Override
			public String call() {
				int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

				if (i < 0)
					return "Unknown? (Got " + i + ")";
				else {
					String s = String.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
					return String.format("%1$d / 0x%1$X / 0b%2$s", Integer.valueOf(i), s);
				}
			}
		});
	}

	static {
		addMapping(TileEntityFurnace.class, "Furnace");
		addMapping(TileEntityChest.class, "Chest");
		addMapping(TileEntityEnderChest.class, "EnderChest");
		addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
		addMapping(TileEntityDispenser.class, "Trap");
		addMapping(TileEntityDropper.class, "Dropper");
		addMapping(TileEntitySign.class, "Sign");
		addMapping(TileEntityMobSpawner.class, "MobSpawner");
		addMapping(TileEntityNote.class, "Music");
		addMapping(TileEntityPiston.class, "Piston");
		addMapping(TileEntityBrewingStand.class, "Cauldron");
		addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
		addMapping(TileEntityEndPortal.class, "Airportal");
		addMapping(TileEntityCommandBlock.class, "Control");
		addMapping(TileEntityBeacon.class, "Beacon");
		addMapping(TileEntitySkull.class, "Skull");
		addMapping(TileEntityDaylightDetector.class, "DLDetector");
		addMapping(TileEntityHopper.class, "Hopper");
		addMapping(TileEntityComparator.class, "Comparator");
		addMapping(TileEntityFlowerPot.class, "FlowerPot");
	}

	// -- BEGIN FORGE PATCHES --
	/**
	 * Determines if this TileEntity requires update calls.
	 *
	 * @return True if you want updateEntity() to be called, false if not
	 */
	public boolean canUpdate() {
		return true;
	}

	/**
	 * Called when you receive a TileEntityData packet for the location this
	 * TileEntity is currently in. On the client, the NetworkManager will always be
	 * the remote server. On the server, it will be whomever is responsible for
	 * sending the packet.
	 *
	 * @param net
	 *            The NetworkManager the packet originated from
	 * @param pkt
	 *            The data packet
	 */
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
	}

	/**
	 * Called when the chunk this TileEntity is on is Unloaded.
	 */
	public void onChunkUnload() {
	}

	private final boolean isVanilla = getClass().getName().startsWith("net.minecraft.tileentity");

	/**
	 * Called from Chunk.setBlockIDWithMetadata, determines if this tile entity
	 * should be re-created when the ID, or Metadata changes. Use with caution as
	 * this will leave straggler TileEntities, or create conflicts with other
	 * TileEntities if not used properly.
	 *
	 * @param oldID
	 *            The old ID of the block
	 * @param newID
	 *            The new ID of the block (May be the same)
	 * @param oldMeta
	 *            The old metadata of the block
	 * @param newMeta
	 *            The new metadata of the block (May be the same)
	 * @param world
	 *            Current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y Position
	 * @param z
	 *            Z Position
	 * @return True to remove the old tile entity, false to keep it in tact {and
	 *         create a new one if the new values specify to}
	 */
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y,
			int z) {
		return !isVanilla || oldBlock != newBlock;
	}

	public boolean shouldRenderInPass(int pass) {
		return pass == 0;
	}

	/**
	 * Sometimes default render bounding box: infinite in scope. Used to control
	 * rendering on {@link TileEntitySpecialRenderer}.
	 */
	public static final AxisAlignedBB INFINITE_EXTENT_AABB = AxisAlignedBB.getBoundingBox(Double.NEGATIVE_INFINITY,
			Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
			Double.POSITIVE_INFINITY);

	/**
	 * Return an {@link AxisAlignedBB} that controls the visible scope of a
	 * {@link TileEntitySpecialRenderer} associated with this {@link TileEntity}
	 * Defaults to the collision bounding box
	 * {@link Block#getCollisionBoundingBoxFromPool(World, int, int, int)}
	 * associated with the block at this location.
	 *
	 * @return an appropriately size {@link AxisAlignedBB} for the
	 *         {@link TileEntity}
	 */
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		Block type = getBlockType();
		if (type == Blocks.enchanting_table) {
			bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
		} else if (type == Blocks.chest || type == Blocks.trapped_chest) {
			bb = AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
		} else if (type != null && type != Blocks.beacon) {
			AxisAlignedBB cbb = type.getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);
			if (cbb != null) {
				bb = cbb;
			}
		}
		return bb;
	}

	/*
	 * ======================================== ULTRAMINE START
	 * =====================================
	 */

	private GameProfile owner;
	public boolean removeThisTick;

	public final void setObjectOwner(GameProfile owner) {
		if (this.owner == null) {
			this.owner = owner;
		}
	}

	public final GameProfile getObjectOwner() {
		return owner;
	}
}