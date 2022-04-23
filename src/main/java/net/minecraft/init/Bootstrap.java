package net.minecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class Bootstrap {
	private static boolean field_151355_a = false;
	private static final String __OBFID = "CL_00001397";

	static void func_151353_a() {
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
			private static final String __OBFID = "CL_00001398";

			@Override
			protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_) {
				EntityArrow entityarrow = new EntityArrow(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(),
						p_82499_2_.getZ());
				entityarrow.canBePickedUp = 1;
				return entityarrow;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
			private static final String __OBFID = "CL_00001404";

			@Override
			protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_) {
				return new EntityEgg(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
			private static final String __OBFID = "CL_00001405";

			@Override
			protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_) {
				return new EntitySnowball(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
			private static final String __OBFID = "CL_00001406";

			@Override
			protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_) {
				return new EntityExpBottle(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ());
			}

			@Override
			protected float func_82498_a() {
				return super.func_82498_a() * 0.5F;
			}

			@Override
			protected float func_82500_b() {
				return super.func_82500_b() * 1.25F;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
			private static final String __OBFID = "CL_00001407";

			@Override
			public ItemStack dispense(IBlockSource p_82482_1_, final ItemStack p_82482_2_) {
				return ItemPotion.isSplash(p_82482_2_.getItemDamage()) ? new BehaviorProjectileDispense() {
					private static final String __OBFID = "CL_00001408";

					@Override
					protected IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_) {
						return new EntityPotion(p_82499_1_, p_82499_2_.getX(), p_82499_2_.getY(), p_82499_2_.getZ(),
								p_82482_2_.copy());
					}

					@Override
					protected float func_82498_a() {
						return super.func_82498_a() * 0.5F;
					}

					@Override
					protected float func_82500_b() {
						return super.func_82500_b() * 1.25F;
					}
				}.dispense(p_82482_1_, p_82482_2_) : field_150843_b.dispense(p_82482_1_, p_82482_2_);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
			private static final String __OBFID = "CL_00001410";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				double d0 = blockSource.getX() + enumfacing.getFrontOffsetX();
				double d1 = blockSource.getYInt() + 0.2F;
				double d2 = blockSource.getZ() + enumfacing.getFrontOffsetZ();
				World world = blockSource.getWorld();
				ItemStack itemstack1 = itemStack.splitStack(1);
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new org.bukkit.util.Vector(d0, d1, d2));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					itemStack.stackSize++;
					return itemStack;
				}
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR)
						return itemStack;
					itemStack.stackSize++;
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
				Entity entity = ItemMonsterPlacer.spawnCreature(blockSource.getWorld(), itemStack.getItemDamage(),
						event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
				if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
					((EntityLiving) entity).setCustomNameTag(itemStack.getDisplayName());
				}
				return itemStack;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
			private static final String __OBFID = "CL_00001411";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				double d0 = blockSource.getX() + enumfacing.getFrontOffsetX();
				double d1 = blockSource.getYInt() + 0.2F;
				double d2 = blockSource.getZ() + enumfacing.getFrontOffsetZ();
				World world = blockSource.getWorld();
				ItemStack itemstack1 = itemStack.splitStack(1);
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new org.bukkit.util.Vector(d0, d1, d2));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					itemStack.stackSize++;
					return itemStack;
				}
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR)
						return itemStack;
					itemStack.stackSize++;
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
				EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(blockSource.getWorld(),
						event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), itemstack1);
				blockSource.getWorld().spawnEntityInWorld(entityfireworkrocket);
				return itemStack;
			}

			@Override
			protected void playDispenseSound(IBlockSource p_82485_1_) {
				p_82485_1_.getWorld().playAuxSFX(1002, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(),
						0);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
			private static final String __OBFID = "CL_00001412";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				IPosition iposition = BlockDispenser.func_149939_a(blockSource);
				double d0 = iposition.getX() + enumfacing.getFrontOffsetX() * 0.3F;
				double d1 = iposition.getY() + enumfacing.getFrontOffsetX() * 0.3F;
				double d2 = iposition.getZ() + enumfacing.getFrontOffsetZ() * 0.3F;
				World world = blockSource.getWorld();
				Random random = world.rand;
				double d3 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetX();
				double d4 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetY();
				double d5 = random.nextGaussian() * 0.05D + enumfacing.getFrontOffsetZ();
				ItemStack itemstack1 = itemStack.splitStack(1);
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new org.bukkit.util.Vector(d3, d4, d5));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					itemStack.stackSize++;
					return itemStack;
				}
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR)
						return itemStack;
					itemStack.stackSize++;
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				EntitySmallFireball entitysmallfireball = new EntitySmallFireball(world, d0, d1, d2,
						event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
				entitysmallfireball
						.setProjectileSource(new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource(
								(TileEntityDispenser) blockSource.getBlockTileEntity()));
				world.spawnEntityInWorld(entitysmallfireball);
				return itemStack;
			}

			@Override
			protected void playDispenseSound(IBlockSource p_82485_1_) {
				p_82485_1_.getWorld().playAuxSFX(1009, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(),
						0);
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
			private static final String __OBFID = "CL_00001413";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				World world = blockSource.getWorld();
				double d0 = blockSource.getX() + enumfacing.getFrontOffsetX() * 1.125F;
				double d1 = blockSource.getY() + enumfacing.getFrontOffsetY() * 1.125F;
				double d2 = blockSource.getZ() + enumfacing.getFrontOffsetZ() * 1.125F;
				int i = blockSource.getXInt() + enumfacing.getFrontOffsetX();
				int j = blockSource.getYInt() + enumfacing.getFrontOffsetY();
				int k = blockSource.getZInt() + enumfacing.getFrontOffsetZ();
				Material material = world.getBlock(i, j, k).getMaterial();
				double d3;
				if (Material.water.equals(material)) {
					d3 = 1.0D;
				} else {
					if (!Material.air.equals(material)
							|| !Material.water.equals(world.getBlock(i, j - 1, k).getMaterial()))
						return field_150842_b.dispense(blockSource, itemStack);
					d3 = 0.0D;
				}
				ItemStack itemstack1 = itemStack.splitStack(1);
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new Vector(d0, d1 + d3, d2));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					itemStack.stackSize++;
					return itemStack;
				}
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR)
						return itemStack;
					itemStack.stackSize++;
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				EntityBoat entityboat = new EntityBoat(world, event.getVelocity().getX(), event.getVelocity().getY(),
						event.getVelocity().getZ());
				world.spawnEntityInWorld(entityboat);
				return itemStack;
			}

			@Override
			protected void playDispenseSound(IBlockSource p_82485_1_) {
				p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(),
						0);
			}
		});
		BehaviorDefaultDispenseItem behaviordefaultdispenseitem = new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
			private static final String __OBFID = "CL_00001399";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				ItemBucket itembucket = (ItemBucket) itemStack.getItem();
				int i = blockSource.getXInt();
				int j = blockSource.getYInt();
				int k = blockSource.getZInt();
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				World world = blockSource.getWorld();
				int x = i + enumfacing.getFrontOffsetX();
				int y = j + enumfacing.getFrontOffsetY();
				int z = k + enumfacing.getFrontOffsetZ();
				if (world.isAirBlock(x, y, z) || !world.getBlock(x, y, z).getMaterial().isSolid()) {
					org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
					CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemStack);
					BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
							new org.bukkit.util.Vector(x, y, z));
					world.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled())
						return itemStack;
					if (!event.getItem().equals(craftItem)) {
						if (event.getItem().getType() == org.bukkit.Material.AIR)
							return new ItemStack(Items.bucket);
						ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
						IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
								.getObject(eventStack.getItem());
						if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
								&& ibehaviordispenseitem != this) {
							ibehaviordispenseitem.dispense(blockSource, eventStack);
							return itemStack;
						}
					}
					itembucket = (ItemBucket) CraftItemStack.asNMSCopy(event.getItem()).getItem();
				}
				if (itembucket.tryPlaceContainedLiquid(blockSource.getWorld(), i + enumfacing.getFrontOffsetX(),
						j + enumfacing.getFrontOffsetY(), k + enumfacing.getFrontOffsetZ())) {
					Item item = Items.bucket;
					if (--itemStack.stackSize == 0) {
						itemStack.func_150996_a(Items.bucket);
						itemStack.stackSize = 1;
					} else if (((TileEntityDispenser) blockSource.getBlockTileEntity())
							.func_146019_a(new ItemStack(item)) < 0) {
						field_150841_b.dispense(blockSource, new ItemStack(item));
					}
					return itemStack;
				} else
					return field_150841_b.dispense(blockSource, itemStack);
			}
		};
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, behaviordefaultdispenseitem);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, behaviordefaultdispenseitem);
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
			private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
			private static final String __OBFID = "CL_00001400";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				World world = blockSource.getWorld();
				int i = blockSource.getXInt() + enumfacing.getFrontOffsetX();
				int j = blockSource.getYInt() + enumfacing.getFrontOffsetY();
				int k = blockSource.getZInt() + enumfacing.getFrontOffsetZ();
				Material material = world.getBlock(i, j, k).getMaterial();
				int l = world.getBlockMetadata(i, j, k);
				Item item;
				if (Material.water.equals(material) && l == 0) {
					item = Items.water_bucket;
				} else {
					if (!Material.lava.equals(material) || l != 0)
						return super.dispenseStack(blockSource, itemStack);
					item = Items.lava_bucket;
				}
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemStack);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new org.bukkit.util.Vector(i, j, k));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled())
					return itemStack;
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR)
						return itemStack;
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				world.setBlockToAir(i, j, k);
				if (--itemStack.stackSize == 0) {
					itemStack.func_150996_a(item);
					itemStack.stackSize = 1;
				} else if (((TileEntityDispenser) blockSource.getBlockTileEntity())
						.func_146019_a(new ItemStack(item)) < 0) {
					field_150840_b.dispense(blockSource, new ItemStack(item));
				}
				return itemStack;
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
			private boolean field_150839_b = true;
			private static final String __OBFID = "CL_00001401";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
				World world = blockSource.getWorld();
				int i = blockSource.getXInt() + enumfacing.getFrontOffsetX();
				int j = blockSource.getYInt() + enumfacing.getFrontOffsetY();
				int k = blockSource.getZInt() + enumfacing.getFrontOffsetZ();
				org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
						blockSource.getZInt());
				CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemStack);
				BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
						new org.bukkit.util.Vector(0, 0, 0));
				world.getServer().getPluginManager().callEvent(event);
				if (event.isCancelled())
					return itemStack;
				if (!event.getItem().equals(craftItem)) {
					if (event.getItem().getType() == org.bukkit.Material.AIR) {
						if (itemStack.attemptDamageItem(1, world.rand)) {
							itemStack.stackSize = 0;
						}
						return itemStack;
					}
					ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
					IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
							.getObject(eventStack.getItem());
					if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
							&& ibehaviordispenseitem != this) {
						ibehaviordispenseitem.dispense(blockSource, eventStack);
						return itemStack;
					}
				}
				if (world.isAirBlock(i, j, k)) {
					if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, i, j, k,
							blockSource.getXInt(), blockSource.getYInt(), blockSource.getZInt()).isCancelled()) {
						world.setBlock(i, j, k, Blocks.fire);
						if (itemStack.attemptDamageItem(1, world.rand)) {
							itemStack.stackSize = 0;
						}
					}
				} else if (world.getBlock(i, j, k) == Blocks.tnt) {
					Blocks.tnt.onBlockDestroyedByPlayer(world, i, j, k, 1);
					world.setBlockToAir(i, j, k);
				} else {
					field_150839_b = false;
				}
				return itemStack;
			}

			@Override
			protected void playDispenseSound(IBlockSource p_82485_1_) {
				if (field_150839_b) {
					p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(),
							p_82485_1_.getZInt(), 0);
				} else {
					p_82485_1_.getWorld().playAuxSFX(1001, p_82485_1_.getXInt(), p_82485_1_.getYInt(),
							p_82485_1_.getZInt(), 0);
				}
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
			private boolean field_150838_b = true;
			private static final String __OBFID = "CL_00001402";

			@Override
			public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
				if (itemStack.getItemDamage() == 15) {
					EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
					World world = blockSource.getWorld();
					int i = blockSource.getXInt() + enumfacing.getFrontOffsetX();
					int j = blockSource.getYInt() + enumfacing.getFrontOffsetY();
					int k = blockSource.getZInt() + enumfacing.getFrontOffsetZ();
					org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(),
							blockSource.getYInt(), blockSource.getZInt());
					CraftItemStack craftItem = CraftItemStack.asNewCraftStack(itemStack.getItem());
					BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
							new org.bukkit.util.Vector(0, 0, 0));
					world.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled())
						return itemStack;
					if (!event.getItem().equals(craftItem)) {
						if (event.getItem().getType() == org.bukkit.Material.AIR)
							return itemStack;
						ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
						IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
								.getObject(eventStack.getItem());
						if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
								&& ibehaviordispenseitem != this) {
							ibehaviordispenseitem.dispense(blockSource, eventStack);
							return itemStack;
						}
					}
					if (ItemDye.func_150919_a(itemStack, world, i, j, k))
						if (!world.isRemote) {
							world.playAuxSFX(2005, i, j, k, 0);
						} else {
							field_150838_b = false;
						}
					return itemStack;
				} else
					return super.dispenseStack(blockSource, itemStack);
			}

			@Override
			protected void playDispenseSound(IBlockSource p_82485_1_) {
				if (field_150838_b) {
					p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(),
							p_82485_1_.getZInt(), 0);
				} else {
					p_82485_1_.getWorld().playAuxSFX(1001, p_82485_1_.getXInt(), p_82485_1_.getYInt(),
							p_82485_1_.getZInt(), 0);
				}
			}
		});
		BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt),
				new BehaviorDefaultDispenseItem() {
					private static final String __OBFID = "CL_00001403";

					@Override
					public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
						EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
						World world = blockSource.getWorld();
						int i = blockSource.getXInt() + enumfacing.getFrontOffsetX();
						int j = blockSource.getYInt() + enumfacing.getFrontOffsetY();
						int k = blockSource.getZInt() + enumfacing.getFrontOffsetZ();
						ItemStack itemstack1 = itemStack.splitStack(1);
						org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(),
								blockSource.getYInt(), blockSource.getZInt());
						CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
						BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
								new org.bukkit.util.Vector(i + 0.5, j + 0.5, k + 0.5));
						world.getServer().getPluginManager().callEvent(event);
						if (event.isCancelled()) {
							itemStack.stackSize++;
							return itemStack;
						}
						if (!event.getItem().equals(craftItem)) {
							if (event.getItem().getType() == org.bukkit.Material.AIR)
								return itemStack;
							itemStack.stackSize++;
							ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
							IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
									.getObject(eventStack.getItem());
							if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
									&& ibehaviordispenseitem != this) {
								ibehaviordispenseitem.dispense(blockSource, eventStack);
								return itemStack;
							}
						}
						EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, event.getVelocity().getX(),
								event.getVelocity().getY(), event.getVelocity().getZ(), (EntityLivingBase) null);
						world.spawnEntityInWorld(entitytntprimed);
						return itemStack;
					}
				});
	}

	public static void func_151354_b() {
		if (!field_151355_a) {
			field_151355_a = true;
			Block.registerBlocks();
			BlockFire.func_149843_e();
			Item.registerItems();
			StatList.func_151178_a();
			func_151353_a();
		}
	}
}