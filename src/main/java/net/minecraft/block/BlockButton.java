package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public abstract class BlockButton extends Block {
	private final boolean field_150047_a;
	protected BlockButton(boolean p_i45396_1_) {
		super(Material.circuits);
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabRedstone);
		field_150047_a = p_i45396_1_;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return null;
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return field_150047_a ? 30 : 20;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_,
			int p_149707_5_) {
		ForgeDirection dir = ForgeDirection.getOrientation(p_149707_5_);
		return dir == NORTH && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_, p_149707_4_ + 1, NORTH)
				|| dir == SOUTH && p_149707_1_.isSideSolid(p_149707_2_, p_149707_3_, p_149707_4_ - 1, SOUTH)
				|| dir == WEST && p_149707_1_.isSideSolid(p_149707_2_ + 1, p_149707_3_, p_149707_4_, WEST)
				|| dir == EAST && p_149707_1_.isSideSolid(p_149707_2_ - 1, p_149707_3_, p_149707_4_, EAST);
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
		return p_149742_1_.isSideSolid(p_149742_2_ - 1, p_149742_3_, p_149742_4_, EAST)
				|| p_149742_1_.isSideSolid(p_149742_2_ + 1, p_149742_3_, p_149742_4_, WEST)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_, p_149742_4_ - 1, SOUTH)
				|| p_149742_1_.isSideSolid(p_149742_2_, p_149742_3_, p_149742_4_ + 1, NORTH);
	}

	@Override
	public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_,
			float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		int j1 = p_149660_1_.getBlockMetadata(p_149660_2_, p_149660_3_, p_149660_4_);
		int k1 = j1 & 8;
		j1 &= 7;

		ForgeDirection dir = ForgeDirection.getOrientation(p_149660_5_);

		if (dir == NORTH && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ + 1, NORTH)) {
			j1 = 4;
		} else if (dir == SOUTH && p_149660_1_.isSideSolid(p_149660_2_, p_149660_3_, p_149660_4_ - 1, SOUTH)) {
			j1 = 3;
		} else if (dir == WEST && p_149660_1_.isSideSolid(p_149660_2_ + 1, p_149660_3_, p_149660_4_, WEST)) {
			j1 = 2;
		} else if (dir == EAST && p_149660_1_.isSideSolid(p_149660_2_ - 1, p_149660_3_, p_149660_4_, EAST)) {
			j1 = 1;
		} else {
			j1 = func_150045_e(p_149660_1_, p_149660_2_, p_149660_3_, p_149660_4_);
		}

		return j1 + k1;
	}

	private int func_150045_e(World p_150045_1_, int p_150045_2_, int p_150045_3_, int p_150045_4_) {
		if (p_150045_1_.isSideSolid(p_150045_2_ - 1, p_150045_3_, p_150045_4_, EAST))
			return 1;
		if (p_150045_1_.isSideSolid(p_150045_2_ + 1, p_150045_3_, p_150045_4_, WEST))
			return 2;
		if (p_150045_1_.isSideSolid(p_150045_2_, p_150045_3_, p_150045_4_ - 1, SOUTH))
			return 3;
		if (p_150045_1_.isSideSolid(p_150045_2_, p_150045_3_, p_150045_4_ + 1, NORTH))
			return 4;
		return 1;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		if (func_150044_m(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_)) {
			int l = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_) & 7;
			boolean flag = !p_149695_1_.isSideSolid(p_149695_2_ - 1, p_149695_3_, p_149695_4_, EAST) && l == 1;

            if (!p_149695_1_.isSideSolid(p_149695_2_ + 1, p_149695_3_, p_149695_4_, WEST) && l == 2) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_, p_149695_4_ - 1, SOUTH) && l == 3) {
				flag = true;
			}

			if (!p_149695_1_.isSideSolid(p_149695_2_, p_149695_3_, p_149695_4_ + 1, NORTH) && l == 4) {
				flag = true;
			}

			if (flag) {
				this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_,
						p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
				p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
			}
		}
	}

	private boolean func_150044_m(World p_150044_1_, int p_150044_2_, int p_150044_3_, int p_150044_4_) {
		if (!canPlaceBlockAt(p_150044_1_, p_150044_2_, p_150044_3_, p_150044_4_)) {
			this.dropBlockAsItem(p_150044_1_, p_150044_2_, p_150044_3_, p_150044_4_,
					p_150044_1_.getBlockMetadata(p_150044_2_, p_150044_3_, p_150044_4_), 0);
			p_150044_1_.setBlockToAir(p_150044_2_, p_150044_3_, p_150044_4_);
			return false;
		} else
			return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
		func_150043_b(l);
	}

	private void func_150043_b(int p_150043_1_) {
		int j = p_150043_1_ & 7;
		boolean flag = (p_150043_1_ & 8) > 0;
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.1875F;
		float f3 = 0.125F;

		if (flag) {
			f3 = 0.0625F;
		}

		if (j == 1) {
			setBlockBounds(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
		} else if (j == 2) {
			setBlockBounds(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
		} else if (j == 3) {
			setBlockBounds(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
		} else if (j == 4) {
			setBlockBounds(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
		}
	}

	@Override
	public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_,
			EntityPlayer p_149699_5_) {
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_,
			EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		int i1 = p_149727_1_.getBlockMetadata(p_149727_2_, p_149727_3_, p_149727_4_);
		int j1 = i1 & 7;
		int k1 = 8 - (i1 & 8);

		if (k1 == 0)
			return true;
		else {
			org.bukkit.block.Block block = p_149727_1_.getWorld().getBlockAt(p_149727_2_, p_149727_3_, p_149727_4_);
			int old = k1 != 8 ? 15 : 0;
			int current = k1 == 8 ? 15 : 0;
			BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
			Bukkit.getPluginManager().callEvent(eventRedstone);
			if (eventRedstone.getNewCurrent() > 0 != (k1 == 8))
				return true;

			p_149727_1_.setBlockMetadataWithNotify(p_149727_2_, p_149727_3_, p_149727_4_, j1 + k1, 3);
			p_149727_1_.markBlockRangeForRenderUpdate(p_149727_2_, p_149727_3_, p_149727_4_, p_149727_2_, p_149727_3_,
					p_149727_4_);
			p_149727_1_.playSoundEffect(p_149727_2_ + 0.5D, p_149727_3_ + 0.5D, p_149727_4_ + 0.5D, "random.click",
					0.3F, 0.6F);
			func_150042_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, j1);
			p_149727_1_.scheduleBlockUpdate(p_149727_2_, p_149727_3_, p_149727_4_, this, tickRate(p_149727_1_));
			return true;
		}
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_,
			int p_149749_6_) {
		if ((p_149749_6_ & 8) > 0) {
			int i1 = p_149749_6_ & 7;
			func_150042_a(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, i1);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_,
			int p_149709_5_) {
		return (p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_) & 8) > 0 ? 15 : 0;
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_,
			int p_149748_5_) {
		int i1 = p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_);

		if ((i1 & 8) == 0)
			return 0;
		else {
			int j1 = i1 & 7;
			return j1 == 5 && p_149748_5_ == 1 ? 15
					: j1 == 4 && p_149748_5_ == 2 ? 15
							: j1 == 3 && p_149748_5_ == 3 ? 15
									: j1 == 2 && p_149748_5_ == 4 ? 15 : j1 == 1 && p_149748_5_ == 5 ? 15 : 0;
		}
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			int l = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

			if ((l & 8) != 0) {
				if (field_150047_a) {
					func_150046_n(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
				} else {
					p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, l & 7, 3);
					int i1 = l & 7;
					func_150042_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, i1);
					p_149674_1_.playSoundEffect(p_149674_2_ + 0.5D, p_149674_3_ + 0.5D, p_149674_4_ + 0.5D,
							"random.click", 0.3F, 0.5F);
					p_149674_1_.markBlockRangeForRenderUpdate(p_149674_2_, p_149674_3_, p_149674_4_, p_149674_2_,
							p_149674_3_, p_149674_4_);
				}
			}
		}
	}

	@Override
	public void setBlockBoundsForItemRender() {
		float f = 0.1875F;
		float f1 = 0.125F;
		float f2 = 0.125F;
		setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity p_149670_5_) {
		if (!p_149670_1_.isRemote) {
			if (field_150047_a) {
				if ((p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_) & 8) == 0) {
					func_150046_n(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void func_150046_n(World p_150046_1_, int p_150046_2_, int p_150046_3_, int p_150046_4_) {
		int l = p_150046_1_.getBlockMetadata(p_150046_2_, p_150046_3_, p_150046_4_);
		int i1 = l & 7;
		boolean flag = (l & 8) != 0;
		func_150043_b(l);

		List list = p_150046_1_.getEntitiesWithinAABB(EntityArrow.class,
				AxisAlignedBB.getBoundingBox(p_150046_2_ + minX, p_150046_3_ + minY, p_150046_4_ + minZ,
						p_150046_2_ + maxX, p_150046_3_ + maxY, p_150046_4_ + maxZ));
		boolean flag1 = !list.isEmpty();

		if (flag != flag1 && flag1) {
			org.bukkit.block.Block block = p_150046_1_.getWorld().getBlockAt(p_150046_2_, p_150046_3_, p_150046_4_);
			boolean allowed = false;
			for (Object entityObject : list)
				if (entityObject != null) {
					EntityInteractEvent event = new EntityInteractEvent(((Entity) entityObject).getBukkitEntity(),
							block);
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						allowed = true;
						break;
					}
				}
			if (!allowed)
				return;
		}

		if (flag1 && !flag) {
			p_150046_1_.setBlockMetadataWithNotify(p_150046_2_, p_150046_3_, p_150046_4_, i1 | 8, 3);
			func_150042_a(p_150046_1_, p_150046_2_, p_150046_3_, p_150046_4_, i1);
			p_150046_1_.markBlockRangeForRenderUpdate(p_150046_2_, p_150046_3_, p_150046_4_, p_150046_2_, p_150046_3_,
					p_150046_4_);
			p_150046_1_.playSoundEffect(p_150046_2_ + 0.5D, p_150046_3_ + 0.5D, p_150046_4_ + 0.5D, "random.click",
					0.3F, 0.6F);
		}

		if (!flag1 && flag) {
			p_150046_1_.setBlockMetadataWithNotify(p_150046_2_, p_150046_3_, p_150046_4_, i1, 3);
			func_150042_a(p_150046_1_, p_150046_2_, p_150046_3_, p_150046_4_, i1);
			p_150046_1_.markBlockRangeForRenderUpdate(p_150046_2_, p_150046_3_, p_150046_4_, p_150046_2_, p_150046_3_,
					p_150046_4_);
			p_150046_1_.playSoundEffect(p_150046_2_ + 0.5D, p_150046_3_ + 0.5D, p_150046_4_ + 0.5D, "random.click",
					0.3F, 0.5F);
		}

		if (flag1) {
			p_150046_1_.scheduleBlockUpdate(p_150046_2_, p_150046_3_, p_150046_4_, this, tickRate(p_150046_1_));
		}
	}

	private void func_150042_a(World p_150042_1_, int p_150042_2_, int p_150042_3_, int p_150042_4_, int p_150042_5_) {
		p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_, p_150042_3_, p_150042_4_, this);

		if (p_150042_5_ == 1) {
			p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_ - 1, p_150042_3_, p_150042_4_, this);
		} else if (p_150042_5_ == 2) {
			p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_ + 1, p_150042_3_, p_150042_4_, this);
		} else if (p_150042_5_ == 3) {
			p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_, p_150042_3_, p_150042_4_ - 1, this);
		} else if (p_150042_5_ == 4) {
			p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_, p_150042_3_, p_150042_4_ + 1, this);
		} else {
			p_150042_1_.notifyBlocksOfNeighborChange(p_150042_2_, p_150042_3_ - 1, p_150042_4_, this);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
	}
}