package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityPiston extends TileEntity {
	private Block storedBlock;
	private int storedMetadata;
	private int storedOrientation;
	private boolean extending;
	private boolean shouldHeadBeRendered;
	private float progress;
	private float lastProgress;
	private List pushedObjects = new ArrayList();
	private static final String __OBFID = "CL_00000369";

	public TileEntityPiston() {
	}

	public TileEntityPiston(Block p_i45444_1_, int p_i45444_2_, int p_i45444_3_, boolean p_i45444_4_,
			boolean p_i45444_5_) {
		storedBlock = p_i45444_1_;
		storedMetadata = p_i45444_2_;
		storedOrientation = p_i45444_3_;
		extending = p_i45444_4_;
		shouldHeadBeRendered = p_i45444_5_;
	}

	public Block getStoredBlockID() {
		return storedBlock;
	}

	@Override
	public int getBlockMetadata() {
		return storedMetadata;
	}

	public boolean isExtending() {
		return extending;
	}

	public int getPistonOrientation() {
		return storedOrientation;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_145867_d() {
		return shouldHeadBeRendered;
	}

	public float func_145860_a(float p_145860_1_) {
		if (p_145860_1_ > 1.0F) {
			p_145860_1_ = 1.0F;
		}

		return lastProgress + (progress - lastProgress) * p_145860_1_;
	}

	private void func_145863_a(float p_145863_1_, float p_145863_2_) {
		if (extending) {
			p_145863_1_ = 1.0F - p_145863_1_;
		} else {
			--p_145863_1_;
		}

		AxisAlignedBB axisalignedbb = Blocks.piston_extension.func_149964_a(worldObj, xCoord, yCoord, zCoord,
				storedBlock, p_145863_1_, storedOrientation);

		if (axisalignedbb != null) {
			List list = worldObj.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);

			if (!list.isEmpty()) {
				pushedObjects.addAll(list);
				Iterator iterator = pushedObjects.iterator();

				while (iterator.hasNext()) {
					Entity entity = (Entity) iterator.next();
					entity.moveEntity(p_145863_2_ * Facing.offsetsXForSide[storedOrientation],
							p_145863_2_ * Facing.offsetsYForSide[storedOrientation],
							p_145863_2_ * Facing.offsetsZForSide[storedOrientation]);
				}

				pushedObjects.clear();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float func_145865_b(float p_145865_1_) {
		return extending ? (func_145860_a(p_145865_1_) - 1.0F) * Facing.offsetsXForSide[storedOrientation]
				: (1.0F - func_145860_a(p_145865_1_)) * Facing.offsetsXForSide[storedOrientation];
	}

	@SideOnly(Side.CLIENT)
	public float func_145862_c(float p_145862_1_) {
		return extending ? (func_145860_a(p_145862_1_) - 1.0F) * Facing.offsetsYForSide[storedOrientation]
				: (1.0F - func_145860_a(p_145862_1_)) * Facing.offsetsYForSide[storedOrientation];
	}

	@SideOnly(Side.CLIENT)
	public float func_145859_d(float p_145859_1_) {
		return extending ? (func_145860_a(p_145859_1_) - 1.0F) * Facing.offsetsZForSide[storedOrientation]
				: (1.0F - func_145860_a(p_145859_1_)) * Facing.offsetsZForSide[storedOrientation];
	}

	public void clearPistonTileEntity() {
		if (lastProgress < 1.0F && worldObj != null) {
			lastProgress = progress = 1.0F;
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			invalidate();

			if (worldObj.getBlock(xCoord, yCoord, zCoord) == Blocks.piston_extension) {
				worldObj.setBlock(xCoord, yCoord, zCoord, storedBlock, storedMetadata, 3);
				worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, storedBlock);
			}
		}
	}

	@Override
	public void updateEntity() {
		lastProgress = progress;

		if (lastProgress >= 1.0F) {
			func_145863_a(1.0F, 0.25F);
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			invalidate();

			if (worldObj.getBlock(xCoord, yCoord, zCoord) == Blocks.piston_extension) {
				worldObj.setBlock(xCoord, yCoord, zCoord, storedBlock, storedMetadata, 3);
				worldObj.notifyBlockOfNeighborChange(xCoord, yCoord, zCoord, storedBlock);
			}
		} else {
			progress += 0.5F;

			if (progress >= 1.0F) {
				progress = 1.0F;
			}

			if (extending) {
				func_145863_a(progress, progress - lastProgress + 0.0625F);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		storedBlock = Block.getBlockById(p_145839_1_.getInteger("blockId"));
		storedMetadata = p_145839_1_.getInteger("blockData");
		storedOrientation = p_145839_1_.getInteger("facing");
		lastProgress = progress = p_145839_1_.getFloat("progress");
		extending = p_145839_1_.getBoolean("extending");
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("blockId", Block.getIdFromBlock(storedBlock));
		p_145841_1_.setInteger("blockData", storedMetadata);
		p_145841_1_.setInteger("facing", storedOrientation);
		p_145841_1_.setFloat("progress", lastProgress);
		p_145841_1_.setBoolean("extending", extending);
	}
}