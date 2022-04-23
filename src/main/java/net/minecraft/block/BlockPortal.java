package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.ultramine.bukkit.util.BlockPortalPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockPortal extends BlockBreakable {
	public static final int[][] field_150001_a = new int[][] { new int[0], { 3, 1 }, { 2, 0 } };
	public BlockPortal() {
		super("portal", Material.portal, false);
		setTickRandomly(true);
	}

	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_5_);

		if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning")
				&& p_149674_5_.nextInt(2000) < p_149674_1_.difficultySetting.getDifficultyId()) {
			int l;

			for (l = p_149674_3_; !World.doesBlockHaveSolidTopSurface(p_149674_1_, p_149674_2_, l, p_149674_4_)
					&& l > 0; --l) {
            }

			if (l > 0 && !p_149674_1_.getBlock(p_149674_2_, l + 1, p_149674_4_).isNormalCube()) {
				Entity entity = ItemMonsterPlacer.spawnCreature(p_149674_1_, 57, p_149674_2_ + 0.5D, l + 1.1D,
						p_149674_4_ + 0.5D);

				if (entity != null) {
					entity.timeUntilPortal = entity.getPortalCooldown();
				}
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_,
			int p_149668_4_) {
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_,
			int p_149719_4_) {
		int l = func_149999_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));

		if (l == 0) {
			if (p_149719_1_.getBlock(p_149719_2_ - 1, p_149719_3_, p_149719_4_) != this
					&& p_149719_1_.getBlock(p_149719_2_ + 1, p_149719_3_, p_149719_4_) != this) {
				l = 2;
			} else {
				l = 1;
			}

			if (p_149719_1_ instanceof World && !((World) p_149719_1_).isRemote) {
				((World) p_149719_1_).setBlockMetadataWithNotify(p_149719_2_, p_149719_3_, p_149719_4_, l, 2);
			}
		}

		float f = 0.125F;
		float f1 = 0.125F;

		if (l == 1) {
			f = 0.5F;
		}

		if (l == 2) {
			f1 = 0.5F;
		}

		setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean func_150000_e(World p_150000_1_, int p_150000_2_, int p_150000_3_, int p_150000_4_) {
		BlockPortal.Size size = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 1);
		BlockPortal.Size size1 = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 2);

		if (size.func_150860_b() && size.field_150864_e == 0) {
			size.func_150859_c();
			return true;
		} else if (size1.func_150860_b() && size1.field_150864_e == 0) {
			size1.func_150859_c();
			return true;
		} else
			return false;
	}

	@Override
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_,
			Block p_149695_5_) {
		int l = func_149999_b(p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_));
		BlockPortal.Size size = new BlockPortal.Size(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 1);
		BlockPortal.Size size1 = new BlockPortal.Size(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 2);

		if (l == 1 && (!size.func_150860_b() || size.field_150864_e < size.field_150868_h * size.field_150862_g)) {
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
		} else if (l == 2
				&& (!size1.func_150860_b() || size1.field_150864_e < size1.field_150868_h * size1.field_150862_g)) {
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
		} else if (l == 0 && !size.func_150860_b() && !size1.func_150860_b()) {
			p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
			int p_149646_5_) {
		int i1 = 0;

		if (p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_) == this) {
			i1 = func_149999_b(p_149646_1_.getBlockMetadata(p_149646_2_, p_149646_3_, p_149646_4_));

			if (i1 == 0)
				return false;

			if (i1 == 2 && p_149646_5_ != 5 && p_149646_5_ != 4)
				return false;

			if (i1 == 1 && p_149646_5_ != 3 && p_149646_5_ != 2)
				return false;
		}

		boolean flag = p_149646_1_.getBlock(p_149646_2_ - 1, p_149646_3_, p_149646_4_) == this
				&& p_149646_1_.getBlock(p_149646_2_ - 2, p_149646_3_, p_149646_4_) != this;
		boolean flag1 = p_149646_1_.getBlock(p_149646_2_ + 1, p_149646_3_, p_149646_4_) == this
				&& p_149646_1_.getBlock(p_149646_2_ + 2, p_149646_3_, p_149646_4_) != this;
		boolean flag2 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ - 1) == this
				&& p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ - 2) != this;
		boolean flag3 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ + 1) == this
				&& p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ + 2) != this;
		boolean flag4 = flag || flag1 || i1 == 1;
		boolean flag5 = flag2 || flag3 || i1 == 2;
		return flag4 && p_149646_5_ == 4 || flag4 && p_149646_5_ == 5 || flag5 && p_149646_5_ == 2 || flag5 && p_149646_5_ == 3;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_,
			Entity p_149670_5_) {
		if (p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null) {
			EntityPortalEnterEvent event = new EntityPortalEnterEvent(p_149670_5_.getBukkitEntity(),
					new org.bukkit.Location(p_149670_1_.getWorld(), p_149670_2_, p_149670_3_, p_149670_4_));
			Bukkit.getPluginManager().callEvent(event); // TODO: Fix event spam.
			p_149670_5_.setInPortal();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_,
			Random p_149734_5_) {
		if (p_149734_5_.nextInt(100) == 0) {
			p_149734_1_.playSound(p_149734_2_ + 0.5D, p_149734_3_ + 0.5D, p_149734_4_ + 0.5D, "portal.portal", 0.5F,
					p_149734_5_.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int l = 0; l < 4; ++l) {
			double d0 = p_149734_2_ + p_149734_5_.nextFloat();
			double d1 = p_149734_3_ + p_149734_5_.nextFloat();
			double d2 = p_149734_4_ + p_149734_5_.nextFloat();
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;
			int i1 = p_149734_5_.nextInt(2) * 2 - 1;
			d3 = (p_149734_5_.nextFloat() - 0.5D) * 0.5D;
			d4 = (p_149734_5_.nextFloat() - 0.5D) * 0.5D;
			d5 = (p_149734_5_.nextFloat() - 0.5D) * 0.5D;

			if (p_149734_1_.getBlock(p_149734_2_ - 1, p_149734_3_, p_149734_4_) != this
					&& p_149734_1_.getBlock(p_149734_2_ + 1, p_149734_3_, p_149734_4_) != this) {
				d0 = p_149734_2_ + 0.5D + 0.25D * i1;
				d3 = p_149734_5_.nextFloat() * 2.0F * i1;
			} else {
				d2 = p_149734_4_ + 0.5D + 0.25D * i1;
				d5 = p_149734_5_.nextFloat() * 2.0F * i1;
			}

			p_149734_1_.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
		}
	}

	public static int func_149999_b(int p_149999_0_) {
		return p_149999_0_ & 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return Item.getItemById(0);
	}

	public static class Size {
		private final World field_150867_a;
		private final int field_150865_b;
		private final int field_150866_c;
		private final int field_150863_d;
		private int field_150864_e = 0;
		private ChunkCoordinates field_150861_f;
		private int field_150862_g;
		private int field_150868_h;
		private final List<org.bukkit.block.Block> frameBlocks = new ArrayList<>();

		public Size(World p_i45415_1_, int p_i45415_2_, int p_i45415_3_, int p_i45415_4_, int p_i45415_5_) {
			field_150867_a = p_i45415_1_;
			field_150865_b = p_i45415_5_;
			field_150863_d = BlockPortal.field_150001_a[p_i45415_5_][0];
			field_150866_c = BlockPortal.field_150001_a[p_i45415_5_][1];

			for (int i1 = p_i45415_3_; p_i45415_3_ > i1 - 21 && p_i45415_3_ > 0
					&& func_150857_a(p_i45415_1_.getBlock(p_i45415_2_, p_i45415_3_ - 1, p_i45415_4_)); --p_i45415_3_) {
            }

			int j1 = func_150853_a(p_i45415_2_, p_i45415_3_, p_i45415_4_, field_150863_d) - 1;

			if (j1 >= 0) {
				field_150861_f = new ChunkCoordinates(p_i45415_2_ + j1 * Direction.offsetX[field_150863_d], p_i45415_3_,
						p_i45415_4_ + j1 * Direction.offsetZ[field_150863_d]);
				field_150868_h = func_150853_a(field_150861_f.posX, field_150861_f.posY, field_150861_f.posZ,
						field_150866_c);

				if (field_150868_h < 2 || field_150868_h > 21) {
					field_150861_f = null;
					field_150868_h = 0;
				}
			}

			if (field_150861_f != null) {
				field_150862_g = func_150858_a();
			}
		}

		protected int func_150853_a(int p_150853_1_, int p_150853_2_, int p_150853_3_, int p_150853_4_) {
			int j1 = Direction.offsetX[p_150853_4_];
			int k1 = Direction.offsetZ[p_150853_4_];
			int i1;
			Block block;

			for (i1 = 0; i1 < 22; ++i1) {
				block = field_150867_a.getBlock(p_150853_1_ + j1 * i1, p_150853_2_, p_150853_3_ + k1 * i1);

				if (!func_150857_a(block)) {
					break;
				}

				Block block1 = field_150867_a.getBlock(p_150853_1_ + j1 * i1, p_150853_2_ - 1, p_150853_3_ + k1 * i1);

				if (block1 != Blocks.obsidian) {
					break;
				}
			}

			block = field_150867_a.getBlock(p_150853_1_ + j1 * i1, p_150853_2_, p_150853_3_ + k1 * i1);
			return block == Blocks.obsidian ? i1 : 0;
		}

		protected int func_150858_a() {
			int i;
			int j;
			int k;
			int l;
			label56:

			for (field_150862_g = 0; field_150862_g < 21; ++field_150862_g) {
				i = field_150861_f.posY + field_150862_g;

				for (j = 0; j < field_150868_h; ++j) {
					k = field_150861_f.posX + j * Direction.offsetX[BlockPortal.field_150001_a[field_150865_b][1]];
					l = field_150861_f.posZ + j * Direction.offsetZ[BlockPortal.field_150001_a[field_150865_b][1]];
					Block block = field_150867_a.getBlock(k, i, l);

					if (!func_150857_a(block)) {
						break label56;
					}

					if (block == Blocks.portal) {
						++field_150864_e;
					}

					if (j == 0) {
						block = field_150867_a.getBlock(
								k + Direction.offsetX[BlockPortal.field_150001_a[field_150865_b][0]], i,
								l + Direction.offsetZ[BlockPortal.field_150001_a[field_150865_b][0]]);

						if (block != Blocks.obsidian) {
							break label56;
						}
					} else if (j == field_150868_h - 1) {
						block = field_150867_a.getBlock(
								k + Direction.offsetX[BlockPortal.field_150001_a[field_150865_b][1]], i,
								l + Direction.offsetZ[BlockPortal.field_150001_a[field_150865_b][1]]);

						if (block != Blocks.obsidian) {
							break label56;
						}
					}
				}
			}

			for (i = 0; i < field_150868_h; ++i) {
				j = field_150861_f.posX + i * Direction.offsetX[BlockPortal.field_150001_a[field_150865_b][1]];
				k = field_150861_f.posY + field_150862_g;
				l = field_150861_f.posZ + i * Direction.offsetZ[BlockPortal.field_150001_a[field_150865_b][1]];

				if (field_150867_a.getBlock(j, k, l) != Blocks.obsidian) {
					field_150862_g = 0;
					break;
				}
			}

			if (field_150862_g <= 21 && field_150862_g >= 3)
				return field_150862_g;
			else {
				field_150861_f = null;
				field_150868_h = 0;
				field_150862_g = 0;
				return 0;
			}
		}

		protected boolean func_150857_a(Block p_150857_1_) {
			return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire
					|| p_150857_1_ == Blocks.portal;
		}

		public boolean func_150860_b() {
			return field_150861_f != null && field_150868_h >= 2 && field_150868_h <= 21 && field_150862_g >= 3
					&& field_150862_g <= 21;
		}

		public void func_150859_c() {
			List<BlockPortalPos> portalBlocksPos = new ArrayList<>();
			for (int i = 0; i < field_150868_h; ++i) {
				int j = field_150861_f.posX + Direction.offsetX[field_150866_c] * i;
				int k = field_150861_f.posZ + Direction.offsetZ[field_150866_c] * i;

				for (int l = 0; l < field_150862_g; ++l) {
					int i1 = field_150861_f.posY + l;
					portalBlocksPos.add(new BlockPortalPos(j, i1, k, field_150865_b, 2));
					// this.field_150867_a.setBlock(j, i1, k, Blocks.portal, this.field_150865_b,
					// 2);
				}
			}
			// for (BlockPortalPos portalBlockPos : portalBlocksPos)
			// {
			// // Convert portal blocks to list
			// }
			PortalCreateEvent portalCreateEvent = new PortalCreateEvent(frameBlocks, field_150867_a.getWorld(),
					PortalCreateEvent.CreateReason.FIRE);
			Bukkit.getPluginManager().callEvent(portalCreateEvent);
			if (!portalCreateEvent.isCancelled()) {
				for (BlockPortalPos portalBlockPos : portalBlocksPos) {
					field_150867_a.setBlock(portalBlockPos.getX(), portalBlockPos.getY(), portalBlockPos.getZ(),
							Blocks.portal, portalBlockPos.getField_150865_b(), portalBlockPos.getFlag());
				}
			}
		}
	}
}