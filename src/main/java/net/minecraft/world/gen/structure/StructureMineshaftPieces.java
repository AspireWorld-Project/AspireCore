package net.minecraft.world.gen.structure;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.ChestGenHooks.MINESHAFT_CORRIDOR;

public class StructureMineshaftPieces {
	public static final WeightedRandomChestContent[] mineshaftChestContents = new WeightedRandomChestContent[] {
			new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
			new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5),
			new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5),
			new WeightedRandomChestContent(Items.dye, 4, 4, 9, 5),
			new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3),
			new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10),
			new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
			new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.rail), 0, 4, 8, 1),
			new WeightedRandomChestContent(Items.melon_seeds, 0, 2, 4, 10),
			new WeightedRandomChestContent(Items.pumpkin_seeds, 0, 2, 4, 10),
			new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
			new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1) };
	private static final String __OBFID = "CL_00000444";

	public static void registerStructurePieces() {
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Corridor.class, "MSCorridor");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Cross.class, "MSCrossing");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Room.class, "MSRoom");
		MapGenStructureIO.func_143031_a(StructureMineshaftPieces.Stairs.class, "MSStairs");
	}

	private static StructureComponent getRandomComponent(List p_78815_0_, Random p_78815_1_, int p_78815_2_,
			int p_78815_3_, int p_78815_4_, int p_78815_5_, int p_78815_6_) {
		int j1 = p_78815_1_.nextInt(100);
		StructureBoundingBox structureboundingbox;

		if (j1 >= 80) {
			structureboundingbox = StructureMineshaftPieces.Cross.findValidPlacement(p_78815_0_, p_78815_1_, p_78815_2_,
					p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
				return new StructureMineshaftPieces.Cross(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
		} else if (j1 >= 70) {
			structureboundingbox = StructureMineshaftPieces.Stairs.findValidPlacement(p_78815_0_, p_78815_1_,
					p_78815_2_, p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
				return new StructureMineshaftPieces.Stairs(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
		} else {
			structureboundingbox = StructureMineshaftPieces.Corridor.findValidPlacement(p_78815_0_, p_78815_1_,
					p_78815_2_, p_78815_3_, p_78815_4_, p_78815_5_);

			if (structureboundingbox != null)
				return new StructureMineshaftPieces.Corridor(p_78815_6_, p_78815_1_, structureboundingbox, p_78815_5_);
		}

		return null;
	}

	private static StructureComponent getNextMineShaftComponent(StructureComponent p_78817_0_, List p_78817_1_,
			Random p_78817_2_, int p_78817_3_, int p_78817_4_, int p_78817_5_, int p_78817_6_, int p_78817_7_) {
		if (p_78817_7_ > 8)
			return null;
		else if (Math.abs(p_78817_3_ - p_78817_0_.getBoundingBox().minX) <= 80
				&& Math.abs(p_78817_5_ - p_78817_0_.getBoundingBox().minZ) <= 80) {
			StructureComponent structurecomponent1 = getRandomComponent(p_78817_1_, p_78817_2_, p_78817_3_, p_78817_4_,
					p_78817_5_, p_78817_6_, p_78817_7_ + 1);

			if (structurecomponent1 != null) {
				p_78817_1_.add(structurecomponent1);
				structurecomponent1.buildComponent(p_78817_0_, p_78817_1_, p_78817_2_);
			}

			return structurecomponent1;
		} else
			return null;
	}

	public static class Corridor extends StructureComponent {
		private boolean hasRails;
		private boolean hasSpiders;
		private boolean spawnerPlaced;
		private int sectionCount;
		private static final String __OBFID = "CL_00000445";

		public Corridor() {
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setBoolean("hr", hasRails);
			p_143012_1_.setBoolean("sc", hasSpiders);
			p_143012_1_.setBoolean("hps", spawnerPlaced);
			p_143012_1_.setInteger("Num", sectionCount);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			hasRails = p_143011_1_.getBoolean("hr");
			hasSpiders = p_143011_1_.getBoolean("sc");
			spawnerPlaced = p_143011_1_.getBoolean("hps");
			sectionCount = p_143011_1_.getInteger("Num");
		}

		public Corridor(int p_i2035_1_, Random p_i2035_2_, StructureBoundingBox p_i2035_3_, int p_i2035_4_) {
			super(p_i2035_1_);
			coordBaseMode = p_i2035_4_;
			boundingBox = p_i2035_3_;
			hasRails = p_i2035_2_.nextInt(3) == 0;
			hasSpiders = !hasRails && p_i2035_2_.nextInt(23) == 0;

			if (coordBaseMode != 2 && coordBaseMode != 0) {
				sectionCount = p_i2035_3_.getXSize() / 5;
			} else {
				sectionCount = p_i2035_3_.getZSize() / 5;
			}
		}

		public static StructureBoundingBox findValidPlacement(List p_74954_0_, Random p_74954_1_, int p_74954_2_,
				int p_74954_3_, int p_74954_4_, int p_74954_5_) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74954_2_, p_74954_3_, p_74954_4_,
					p_74954_2_, p_74954_3_ + 2, p_74954_4_);
			int i1;

			for (i1 = p_74954_1_.nextInt(3) + 2; i1 > 0; --i1) {
				int j1 = i1 * 5;

				switch (p_74954_5_) {
				case 0:
					structureboundingbox.maxX = p_74954_2_ + 2;
					structureboundingbox.maxZ = p_74954_4_ + j1 - 1;
					break;
				case 1:
					structureboundingbox.minX = p_74954_2_ - (j1 - 1);
					structureboundingbox.maxZ = p_74954_4_ + 2;
					break;
				case 2:
					structureboundingbox.maxX = p_74954_2_ + 2;
					structureboundingbox.minZ = p_74954_4_ - (j1 - 1);
					break;
				case 3:
					structureboundingbox.maxX = p_74954_2_ + j1 - 1;
					structureboundingbox.maxZ = p_74954_4_ + 2;
				}

				if (StructureComponent.findIntersecting(p_74954_0_, structureboundingbox) == null) {
					break;
				}
			}

			return i1 > 0 ? structureboundingbox : null;
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
			int i = getComponentType();
			int j = p_74861_3_.nextInt(4);

			switch (coordBaseMode) {
			case 0:
				if (j <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.maxZ + 1,
							coordBaseMode, i);
				} else if (j == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.maxZ - 3, 1,
							i);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.maxZ - 3, 3,
							i);
				}

				break;
			case 1:
				if (j <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ,
							coordBaseMode, i);
				} else if (j == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ - 1, 2, i);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.maxZ + 1, 0, i);
				}

				break;
			case 2:
				if (j <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ - 1,
							coordBaseMode, i);
				} else if (j == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ, 1, i);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ, 3, i);
				}

				break;
			case 3:
				if (j <= 1) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ,
							coordBaseMode, i);
				} else if (j == 2) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX - 3, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.minZ - 1, 2,
							i);
				} else {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX - 3, boundingBox.minY - 1 + p_74861_3_.nextInt(3), boundingBox.maxZ + 1, 0,
							i);
				}
			}

			if (i < 8) {
				int k;
				int l;

				if (coordBaseMode != 2 && coordBaseMode != 0) {
					for (k = boundingBox.minX + 3; k + 3 <= boundingBox.maxX; k += 5) {
						l = p_74861_3_.nextInt(5);

						if (l == 0) {
							StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, k,
									boundingBox.minY, boundingBox.minZ - 1, 2, i + 1);
						} else if (l == 1) {
							StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, k,
									boundingBox.minY, boundingBox.maxZ + 1, 0, i + 1);
						}
					}
				} else {
					for (k = boundingBox.minZ + 3; k + 3 <= boundingBox.maxZ; k += 5) {
						l = p_74861_3_.nextInt(5);

						if (l == 0) {
							StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
									boundingBox.minX - 1, boundingBox.minY, k, 1, i + 1);
						} else if (l == 1) {
							StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
									boundingBox.maxX + 1, boundingBox.minY, k, 3, i + 1);
						}
					}
				}
			}
		}

		@Override
		protected boolean generateStructureChestContents(World p_74879_1_, StructureBoundingBox p_74879_2_,
				Random p_74879_3_, int p_74879_4_, int p_74879_5_, int p_74879_6_,
				WeightedRandomChestContent[] p_74879_7_, int p_74879_8_) {
			int i1 = getXWithOffset(p_74879_4_, p_74879_6_);
			int j1 = getYWithOffset(p_74879_5_);
			int k1 = getZWithOffset(p_74879_4_, p_74879_6_);

			if (p_74879_2_.isVecInside(i1, j1, k1) && p_74879_1_.getBlock(i1, j1, k1).getMaterial() == Material.air) {
				int l1 = p_74879_3_.nextBoolean() ? 1 : 0;
				p_74879_1_.setBlock(i1, j1, k1, Blocks.rail, getMetadataWithOffset(Blocks.rail, l1), 2);
				EntityMinecartChest entityminecartchest = new EntityMinecartChest(p_74879_1_, i1 + 0.5F, j1 + 0.5F,
						k1 + 0.5F);
				WeightedRandomChestContent.generateChestContents(p_74879_3_, p_74879_7_, entityminecartchest,
						p_74879_8_);
				p_74879_1_.spawnEntityInWorld(entityminecartchest);
				return true;
			} else
				return false;
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				int i = sectionCount * 5 - 1;
				fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 0, 2, 1, i, Blocks.air, Blocks.air, false);
				randomlyFillWithBlocks(p_74875_1_, p_74875_3_, p_74875_2_, 0.8F, 0, 2, 0, 2, 2, i, Blocks.air,
						Blocks.air, false);

				if (hasSpiders) {
					randomlyFillWithBlocks(p_74875_1_, p_74875_3_, p_74875_2_, 0.6F, 0, 0, 0, 2, 1, i, Blocks.web,
							Blocks.air, false);
				}

				int j;
				int k;

				for (j = 0; j < sectionCount; ++j) {
					k = 2 + j * 5;
					fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, k, 0, 1, k, Blocks.fence, Blocks.air, false);
					fillWithBlocks(p_74875_1_, p_74875_3_, 2, 0, k, 2, 1, k, Blocks.fence, Blocks.air, false);

					if (p_74875_2_.nextInt(4) == 0) {
						fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, k, 0, 2, k, Blocks.planks, Blocks.air, false);
						fillWithBlocks(p_74875_1_, p_74875_3_, 2, 2, k, 2, 2, k, Blocks.planks, Blocks.air, false);
					} else {
						fillWithBlocks(p_74875_1_, p_74875_3_, 0, 2, k, 2, 2, k, Blocks.planks, Blocks.air, false);
					}

					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k - 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k - 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 0, 2, k + 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.1F, 2, 2, k + 1, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k - 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k - 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 0, 2, k + 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 2, 2, k + 2, Blocks.web, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k - 1, Blocks.torch, 0);
					func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.05F, 1, 2, k + 1, Blocks.torch, 0);

					ChestGenHooks info = ChestGenHooks.getInfo(MINESHAFT_CORRIDOR);
					if (p_74875_2_.nextInt(100) == 0) {
						generateStructureChestContents(p_74875_1_, p_74875_3_, p_74875_2_, 2, 0, k - 1,
								info.getItems(p_74875_2_), info.getCount(p_74875_2_));
					}

					if (p_74875_2_.nextInt(100) == 0) {
						generateStructureChestContents(p_74875_1_, p_74875_3_, p_74875_2_, 0, 0, k + 1,
								info.getItems(p_74875_2_), info.getCount(p_74875_2_));
					}

					if (hasSpiders && !spawnerPlaced) {
						int l = getYWithOffset(0);
						int i1 = k - 1 + p_74875_2_.nextInt(3);
						int j1 = getXWithOffset(1, i1);
						i1 = getZWithOffset(1, i1);

						if (p_74875_3_.isVecInside(j1, l, i1)) {
							spawnerPlaced = true;
							p_74875_1_.setBlock(j1, l, i1, Blocks.mob_spawner, 0, 2);
							TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner) p_74875_1_
									.getTileEntity(j1, l, i1);

							if (tileentitymobspawner != null) {
								tileentitymobspawner.func_145881_a().setEntityName("CaveSpider");
							}
						}
					}
				}

				for (j = 0; j <= 2; ++j) {
					for (k = 0; k <= i; ++k) {
						byte b0 = -1;
						Block block1 = getBlockAtCurrentPosition(p_74875_1_, j, b0, k, p_74875_3_);

						if (block1.getMaterial() == Material.air) {
							byte b1 = -1;
							placeBlockAtCurrentPosition(p_74875_1_, Blocks.planks, 0, j, b1, k, p_74875_3_);
						}
					}
				}

				if (hasRails) {
					for (j = 0; j <= i; ++j) {
						Block block = getBlockAtCurrentPosition(p_74875_1_, 1, -1, j, p_74875_3_);

						if (block.getMaterial() != Material.air && block.func_149730_j()) {
							func_151552_a(p_74875_1_, p_74875_3_, p_74875_2_, 0.7F, 1, 0, j, Blocks.rail,
									getMetadataWithOffset(Blocks.rail, 0));
						}
					}
				}

				return true;
			}
		}
	}

	public static class Cross extends StructureComponent {
		private int corridorDirection;
		private boolean isMultipleFloors;
		private static final String __OBFID = "CL_00000446";

		public Cross() {
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			p_143012_1_.setBoolean("tf", isMultipleFloors);
			p_143012_1_.setInteger("D", corridorDirection);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			isMultipleFloors = p_143011_1_.getBoolean("tf");
			corridorDirection = p_143011_1_.getInteger("D");
		}

		public Cross(int p_i2036_1_, Random p_i2036_2_, StructureBoundingBox p_i2036_3_, int p_i2036_4_) {
			super(p_i2036_1_);
			corridorDirection = p_i2036_4_;
			boundingBox = p_i2036_3_;
			isMultipleFloors = p_i2036_3_.getYSize() > 3;
		}

		public static StructureBoundingBox findValidPlacement(List p_74951_0_, Random p_74951_1_, int p_74951_2_,
				int p_74951_3_, int p_74951_4_, int p_74951_5_) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74951_2_, p_74951_3_, p_74951_4_,
					p_74951_2_, p_74951_3_ + 2, p_74951_4_);

			if (p_74951_1_.nextInt(4) == 0) {
				structureboundingbox.maxY += 4;
			}

			switch (p_74951_5_) {
			case 0:
				structureboundingbox.minX = p_74951_2_ - 1;
				structureboundingbox.maxX = p_74951_2_ + 3;
				structureboundingbox.maxZ = p_74951_4_ + 4;
				break;
			case 1:
				structureboundingbox.minX = p_74951_2_ - 4;
				structureboundingbox.minZ = p_74951_4_ - 1;
				structureboundingbox.maxZ = p_74951_4_ + 3;
				break;
			case 2:
				structureboundingbox.minX = p_74951_2_ - 1;
				structureboundingbox.maxX = p_74951_2_ + 3;
				structureboundingbox.minZ = p_74951_4_ - 4;
				break;
			case 3:
				structureboundingbox.maxX = p_74951_2_ + 4;
				structureboundingbox.minZ = p_74951_4_ - 1;
				structureboundingbox.maxZ = p_74951_4_ + 3;
			}

			return StructureComponent.findIntersecting(p_74951_0_, structureboundingbox) != null ? null
					: structureboundingbox;
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
			int i = getComponentType();

			switch (corridorDirection) {
			case 0:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
				break;
			case 1:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				break;
			case 2:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ + 1, 1, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
				break;
			case 3:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ - 1, 2, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ + 1, 3, i);
			}

			if (isMultipleFloors) {
				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ - 1, 2, i);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX - 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 1, i);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.maxX + 1, boundingBox.minY + 3 + 1, boundingBox.minZ + 1, 3, i);
				}

				if (p_74861_3_.nextBoolean()) {
					StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
							boundingBox.minX + 1, boundingBox.minY + 3 + 1, boundingBox.maxZ + 1, 0, i);
				}
			}
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				if (isMultipleFloors) {
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.minY + 3 - 1, boundingBox.maxZ, Blocks.air, Blocks.air,
							false);
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.minY + 3 - 1, boundingBox.maxZ - 1, Blocks.air, Blocks.air,
							false);
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.maxY - 2, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, Blocks.air, Blocks.air, false);
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.maxY - 2, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.minY + 3,
							boundingBox.minZ + 1, boundingBox.maxX - 1, boundingBox.minY + 3, boundingBox.maxZ - 1,
							Blocks.air, Blocks.air, false);
				} else {
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ,
							boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ, Blocks.air, Blocks.air, false);
					fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.minY, boundingBox.minZ + 1,
							boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ - 1, Blocks.air, Blocks.air, false);
				}

				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.minY, boundingBox.minZ + 1,
						boundingBox.minX + 1, boundingBox.maxY, boundingBox.minZ + 1, Blocks.planks, Blocks.air, false);
				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX + 1, boundingBox.minY, boundingBox.maxZ - 1,
						boundingBox.minX + 1, boundingBox.maxY, boundingBox.maxZ - 1, Blocks.planks, Blocks.air, false);
				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.maxX - 1, boundingBox.minY, boundingBox.minZ + 1,
						boundingBox.maxX - 1, boundingBox.maxY, boundingBox.minZ + 1, Blocks.planks, Blocks.air, false);
				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.maxX - 1, boundingBox.minY, boundingBox.maxZ - 1,
						boundingBox.maxX - 1, boundingBox.maxY, boundingBox.maxZ - 1, Blocks.planks, Blocks.air, false);

				for (int i = boundingBox.minX; i <= boundingBox.maxX; ++i) {
					for (int j = boundingBox.minZ; j <= boundingBox.maxZ; ++j) {
						if (getBlockAtCurrentPosition(p_74875_1_, i, boundingBox.minY - 1, j, p_74875_3_)
								.getMaterial() == Material.air) {
							placeBlockAtCurrentPosition(p_74875_1_, Blocks.planks, 0, i, boundingBox.minY - 1, j,
									p_74875_3_);
						}
					}
				}

				return true;
			}
		}
	}

	public static class Room extends StructureComponent {
		private final List roomsLinkedToTheRoom = new LinkedList();
		private static final String __OBFID = "CL_00000447";

		public Room() {
		}

		public Room(int p_i2037_1_, Random p_i2037_2_, int p_i2037_3_, int p_i2037_4_) {
			super(p_i2037_1_);
			boundingBox = new StructureBoundingBox(p_i2037_3_, 50, p_i2037_4_, p_i2037_3_ + 7 + p_i2037_2_.nextInt(6),
					54 + p_i2037_2_.nextInt(6), p_i2037_4_ + 7 + p_i2037_2_.nextInt(6));
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
			int i = getComponentType();
			int k = boundingBox.getYSize() - 3 - 1;

			if (k <= 0) {
				k = 1;
			}

			int j;
			StructureComponent structurecomponent1;
			StructureBoundingBox structureboundingbox;

			for (j = 0; j < boundingBox.getXSize(); j += 4) {
				j += p_74861_3_.nextInt(boundingBox.getXSize());

				if (j + 3 > boundingBox.getXSize()) {
					break;
				}

				structurecomponent1 = StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_,
						p_74861_3_, boundingBox.minX + j, boundingBox.minY + p_74861_3_.nextInt(k) + 1,
						boundingBox.minZ - 1, 2, i);

				if (structurecomponent1 != null) {
					structureboundingbox = structurecomponent1.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX,
							structureboundingbox.minY, boundingBox.minZ, structureboundingbox.maxX,
							structureboundingbox.maxY, boundingBox.minZ + 1));
				}
			}

			for (j = 0; j < boundingBox.getXSize(); j += 4) {
				j += p_74861_3_.nextInt(boundingBox.getXSize());

				if (j + 3 > boundingBox.getXSize()) {
					break;
				}

				structurecomponent1 = StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_,
						p_74861_3_, boundingBox.minX + j, boundingBox.minY + p_74861_3_.nextInt(k) + 1,
						boundingBox.maxZ + 1, 0, i);

				if (structurecomponent1 != null) {
					structureboundingbox = structurecomponent1.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(structureboundingbox.minX,
							structureboundingbox.minY, boundingBox.maxZ - 1, structureboundingbox.maxX,
							structureboundingbox.maxY, boundingBox.maxZ));
				}
			}

			for (j = 0; j < boundingBox.getZSize(); j += 4) {
				j += p_74861_3_.nextInt(boundingBox.getZSize());

				if (j + 3 > boundingBox.getZSize()) {
					break;
				}

				structurecomponent1 = StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_,
						p_74861_3_, boundingBox.minX - 1, boundingBox.minY + p_74861_3_.nextInt(k) + 1,
						boundingBox.minZ + j, 1, i);

				if (structurecomponent1 != null) {
					structureboundingbox = structurecomponent1.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(boundingBox.minX, structureboundingbox.minY,
							structureboundingbox.minZ, boundingBox.minX + 1, structureboundingbox.maxY,
							structureboundingbox.maxZ));
				}
			}

			for (j = 0; j < boundingBox.getZSize(); j += 4) {
				j += p_74861_3_.nextInt(boundingBox.getZSize());

				if (j + 3 > boundingBox.getZSize()) {
					break;
				}

				structurecomponent1 = StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_,
						p_74861_3_, boundingBox.maxX + 1, boundingBox.minY + p_74861_3_.nextInt(k) + 1,
						boundingBox.minZ + j, 3, i);

				if (structurecomponent1 != null) {
					structureboundingbox = structurecomponent1.getBoundingBox();
					roomsLinkedToTheRoom.add(new StructureBoundingBox(boundingBox.maxX - 1, structureboundingbox.minY,
							structureboundingbox.minZ, boundingBox.maxX, structureboundingbox.maxY,
							structureboundingbox.maxZ));
				}
			}
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.minY, boundingBox.minZ,
						boundingBox.maxX, boundingBox.minY, boundingBox.maxZ, Blocks.dirt, Blocks.air, true);
				fillWithBlocks(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.minY + 1, boundingBox.minZ,
						boundingBox.maxX, Math.min(boundingBox.minY + 3, boundingBox.maxY), boundingBox.maxZ,
						Blocks.air, Blocks.air, false);
				Iterator iterator = roomsLinkedToTheRoom.iterator();

				while (iterator.hasNext()) {
					StructureBoundingBox structureboundingbox1 = (StructureBoundingBox) iterator.next();
					fillWithBlocks(p_74875_1_, p_74875_3_, structureboundingbox1.minX, structureboundingbox1.maxY - 2,
							structureboundingbox1.minZ, structureboundingbox1.maxX, structureboundingbox1.maxY,
							structureboundingbox1.maxZ, Blocks.air, Blocks.air, false);
				}

				func_151547_a(p_74875_1_, p_74875_3_, boundingBox.minX, boundingBox.minY + 4, boundingBox.minZ,
						boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, Blocks.air, false);
				return true;
			}
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
			NBTTagList nbttaglist = new NBTTagList();
			Iterator iterator = roomsLinkedToTheRoom.iterator();

			while (iterator.hasNext()) {
				StructureBoundingBox structureboundingbox = (StructureBoundingBox) iterator.next();
				nbttaglist.appendTag(structureboundingbox.func_151535_h());
			}

			p_143012_1_.setTag("Entrances", nbttaglist);
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
			NBTTagList nbttaglist = p_143011_1_.getTagList("Entrances", 11);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				roomsLinkedToTheRoom.add(new StructureBoundingBox(nbttaglist.func_150306_c(i)));
			}
		}
	}

	public static class Stairs extends StructureComponent {
		private static final String __OBFID = "CL_00000449";

		public Stairs() {
		}

		public Stairs(int p_i2038_1_, Random p_i2038_2_, StructureBoundingBox p_i2038_3_, int p_i2038_4_) {
			super(p_i2038_1_);
			coordBaseMode = p_i2038_4_;
			boundingBox = p_i2038_3_;
		}

		@Override
		protected void func_143012_a(NBTTagCompound p_143012_1_) {
		}

		@Override
		protected void func_143011_b(NBTTagCompound p_143011_1_) {
		}

		public static StructureBoundingBox findValidPlacement(List p_74950_0_, Random p_74950_1_, int p_74950_2_,
				int p_74950_3_, int p_74950_4_, int p_74950_5_) {
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(p_74950_2_, p_74950_3_ - 5, p_74950_4_,
					p_74950_2_, p_74950_3_ + 2, p_74950_4_);

			switch (p_74950_5_) {
			case 0:
				structureboundingbox.maxX = p_74950_2_ + 2;
				structureboundingbox.maxZ = p_74950_4_ + 8;
				break;
			case 1:
				structureboundingbox.minX = p_74950_2_ - 8;
				structureboundingbox.maxZ = p_74950_4_ + 2;
				break;
			case 2:
				structureboundingbox.maxX = p_74950_2_ + 2;
				structureboundingbox.minZ = p_74950_4_ - 8;
				break;
			case 3:
				structureboundingbox.maxX = p_74950_2_ + 8;
				structureboundingbox.maxZ = p_74950_4_ + 2;
			}

			return StructureComponent.findIntersecting(p_74950_0_, structureboundingbox) != null ? null
					: structureboundingbox;
		}

		@Override
		public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {
			int i = getComponentType();

			switch (coordBaseMode) {
			case 0:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, boundingBox.minX,
						boundingBox.minY, boundingBox.maxZ + 1, 0, i);
				break;
			case 1:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.minX - 1, boundingBox.minY, boundingBox.minZ, 1, i);
				break;
			case 2:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_, boundingBox.minX,
						boundingBox.minY, boundingBox.minZ - 1, 2, i);
				break;
			case 3:
				StructureMineshaftPieces.getNextMineShaftComponent(p_74861_1_, p_74861_2_, p_74861_3_,
						boundingBox.maxX + 1, boundingBox.minY, boundingBox.minZ, 3, i);
			}
		}

		@Override
		public boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_) {
			if (isLiquidInStructureBoundingBox(p_74875_1_, p_74875_3_))
				return false;
			else {
				fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5, 0, 2, 7, 1, Blocks.air, Blocks.air, false);
				fillWithBlocks(p_74875_1_, p_74875_3_, 0, 0, 7, 2, 2, 8, Blocks.air, Blocks.air, false);

				for (int i = 0; i < 5; ++i) {
					fillWithBlocks(p_74875_1_, p_74875_3_, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i,
							Blocks.air, Blocks.air, false);
				}

				return true;
			}
		}
	}
}