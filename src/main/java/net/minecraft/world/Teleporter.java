package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Teleporter {
	private final WorldServer worldServerInstance;
	private final Random random;
	public final LongHashMap destinationCoordinateCache = new LongHashMap();
	@SuppressWarnings("rawtypes")
	public final List destinationCoordinateKeys = new ArrayList();
	public Teleporter(WorldServer p_i1963_1_) {
		worldServerInstance = p_i1963_1_;
		random = new Random(p_i1963_1_.getSeed());
	}

	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		if (worldServerInstance.provider.dimensionId != 1) {
			if (!placeInExistingPortal(par1Entity, par2, par4, par6, par8)) {
				makePortal(par1Entity);
				placeInExistingPortal(par1Entity, par2, par4, par6, par8);
			}
		} else {
			// CraftBukkit start - Modularize end portal creation
			ChunkCoordinates created = createEndPortal(par2, par4, par6);
			par1Entity.setLocationAndAngles(created.posX, created.posY, created.posZ, par1Entity.rotationYaw, 0.0F);
			par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
		}
	}

	// Split out from original a(Entity, double, double, double, float) method in
	// order to enable being called from createPortal
	private ChunkCoordinates createEndPortal(double x, double y, double z) {
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y) - 1;
		int k = MathHelper.floor_double(z);
		byte b0 = 1;
		byte b1 = 0;
		for (int l = -2; l <= 2; ++l) {
			for (int i1 = -2; i1 <= 2; ++i1) {
				for (int j1 = -1; j1 < 3; ++j1) {
					int k1 = i + i1 * b0 + l * b1;
					int l1 = j + j1;
					int i2 = k + i1 * b1 - l * b0;
					boolean flag = j1 < 0;
					worldServerInstance.setBlock(k1, l1, i2, flag ? Blocks.obsidian : Blocks.air);
				}
			}
		}
		return new ChunkCoordinates(i, j, k);
	}

	// use logic based on creation to verify end portal
	private ChunkCoordinates findEndPortal(ChunkCoordinates portal) {
		int i = portal.posX;
		int j = portal.posY - 1;
		int k = portal.posZ;
		byte b0 = 1;
		byte b1 = 0;

		for (int l = -2; l <= 2; ++l) {
			for (int i1 = -2; i1 <= 2; ++i1) {
				for (int j1 = -1; j1 < 3; ++j1) {
					int k1 = i + i1 * b0 + l * b1;
					int l1 = j + j1;
					int i2 = k + i1 * b1 - l * b0;
					boolean flag = j1 < 0;
					if (worldServerInstance.getBlock(k1, l1, i2) != (flag ? Blocks.obsidian : Blocks.air))
						return null;
				}
			}
		}

		return new ChunkCoordinates(i, j, k);
	}

	public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		// CraftBukkit start - Modularize portal search process and entity teleportation
		ChunkCoordinates found = findPortal(par1Entity.posX, par1Entity.posY, par1Entity.posZ, 128);
		if (found == null)
			return false;
		Location exit = new Location(worldServerInstance.getWorld(), found.posX, found.posY, found.posZ, par8,
				par1Entity.rotationPitch);
		Vector velocity = par1Entity.getBukkitEntity().getVelocity();
		adjustExit(par1Entity, exit, velocity);
		par1Entity.setLocationAndAngles(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
		if (par1Entity.motionX != velocity.getX() || par1Entity.motionY != velocity.getY()
				|| par1Entity.motionZ != velocity.getZ()) {
			par1Entity.getBukkitEntity().setVelocity(velocity);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public ChunkCoordinates findPortal(double x, double y, double z, int short1) {
		if (worldServerInstance.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END)
			return findEndPortal(worldServerInstance.provider.getEntrancePortalLocation());
		double d3 = -1.0D;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = MathHelper.floor_double(x);
		int i1 = MathHelper.floor_double(z);
		long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
		boolean flag = true;
		double d4;
		int k1;
		int j2;
		if (destinationCoordinateCache.containsItem(j1)) {
			Teleporter.PortalPosition chunkcoordinatesportal = (Teleporter.PortalPosition) destinationCoordinateCache
					.getValueByKey(j1);
			d3 = 0.0D;
			i = chunkcoordinatesportal.posX;
			j = chunkcoordinatesportal.posY;
			k = chunkcoordinatesportal.posZ;
			chunkcoordinatesportal.lastUpdateTime = worldServerInstance.getTotalWorldTime();
			flag = false;
		} else {
			for (k1 = l - short1; k1 <= l + short1; ++k1) {
				double d5 = k1 + 0.5D - x;

				for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
					double d6 = l1 + 0.5D - z;

					for (j2 = worldServerInstance.getActualHeight() - 1; j2 >= 0; --j2) {
						if (worldServerInstance.getBlock(k1, j2, l1) == Blocks.portal) {
							while (worldServerInstance.getBlock(k1, j2 - 1, l1) == Blocks.portal) {
								--j2;
							}
							d4 = j2 + 0.5D - y; // CraftBukkit
							double d7 = d5 * d5 + d4 * d4 + d6 * d6;
							if (d3 < 0.0D || d7 < d3) {
								d3 = d7;
								i = k1;
								j = j2;
								k = l1;
							}
						}
					}
				}
			}
		}

		if (d3 >= 0.0D) {
			if (flag) {
				destinationCoordinateCache.add(j1,
						getPortalPositionInstance(i, j, k, worldServerInstance.getTotalWorldTime()));
				destinationCoordinateKeys.add(j1);
			}

			// CraftBukkit start - Moved entity teleportation logic into exit
			return new ChunkCoordinates(i, j, k);
		} else
			return null;
	}

	// Entity repositioning logic split out from original b method and combined with
	// repositioning logic for The End from original a method
	public void adjustExit(Entity entity, Location position, Vector velocity) {
		Location from = position.clone();
		Vector before = velocity.clone();
		int i = position.getBlockX();
		int j = position.getBlockY();
		int k = position.getBlockZ();
		float f = position.getYaw();
		if (worldServerInstance.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
			// entity.setPositionRotation((double) i, (double) j, (double) k, entity.yaw,
			// 0.0F);
			// entity.motX = entity.motY = entity.motZ = 0.0D;
			position.setPitch(0.0F);
			velocity.setX(0);
			velocity.setY(0);
			velocity.setZ(0);
		} else {
			double d4;
			int k1;
			// CraftBukkit end
			double d8 = i + 0.5D;
			double d9 = j + 0.5D;
			d4 = k + 0.5D;
			int j2 = -1;
			if (worldServerInstance.getBlock(i - 1, j, k) == Blocks.portal) {
				j2 = 2;
			}

			if (worldServerInstance.getBlock(i + 1, j, k) == Blocks.portal) {
				j2 = 0;
			}

			if (worldServerInstance.getBlock(i, j, k - 1) == Blocks.portal) {
				j2 = 3;
			}

			if (worldServerInstance.getBlock(i, j, k + 1) == Blocks.portal) {
				j2 = 1;
			}
			int k2 = entity.getTeleportDirection();
			if (j2 > -1) {
				int l2 = Direction.rotateLeft[j2];
				int i3 = Direction.offsetX[j2];
				int j3 = Direction.offsetZ[j2];
				int k3 = Direction.offsetX[l2];
				int l3 = Direction.offsetZ[l2];
				boolean flag1 = !worldServerInstance.isAirBlock(i + i3 + k3, j, k + j3 + l3)
						|| !worldServerInstance.isAirBlock(i + i3 + k3, j + 1, k + j3 + l3);
				boolean flag2 = !worldServerInstance.isAirBlock(i + i3, j, k + j3)
						|| !worldServerInstance.isAirBlock(i + i3, j + 1, k + j3);

				if (flag1 && flag2) {
					j2 = Direction.rotateOpposite[j2];
					l2 = Direction.rotateOpposite[l2];
					i3 = Direction.offsetX[j2];
					j3 = Direction.offsetZ[j2];
					k3 = Direction.offsetX[l2];
					l3 = Direction.offsetZ[l2];
					k1 = i - k3;
					d8 -= k3;
					int i4 = k - l3;
					d4 -= l3;
					flag1 = !worldServerInstance.isAirBlock(k1 + i3 + k3, j, i4 + j3 + l3)
							|| !worldServerInstance.isAirBlock(k1 + i3 + k3, j + 1, i4 + j3 + l3);
					flag2 = !worldServerInstance.isAirBlock(k1 + i3, j, i4 + j3)
							|| !worldServerInstance.isAirBlock(k1 + i3, j + 1, i4 + j3);
				}

				float f1 = 0.5F;
				float f2 = 0.5F;
				if (!flag1 && flag2) {
					f1 = 1.0F;
				} else if (flag1 && !flag2) {
					f1 = 0.0F;
				} else if (flag1) {
					f2 = 0.0F;
				}

				d8 += k3 * f1 + f2 * i3;
				d4 += l3 * f1 + f2 * j3;
				float f3 = 0.0F;
				float f4 = 0.0F;
				float f5 = 0.0F;
				float f6 = 0.0F;
				if (j2 == k2) {
					f3 = 1.0F;
					f4 = 1.0F;
				} else if (j2 == Direction.rotateOpposite[k2]) {
					f3 = -1.0F;
					f4 = -1.0F;
				} else if (j2 == Direction.rotateRight[k2]) {
					f5 = 1.0F;
					f6 = -1.0F;
				} else {
					f5 = -1.0F;
					f6 = 1.0F;
				}
				double d10 = velocity.getX();
				double d11 = velocity.getZ();
				// CraftBukkit start - Adjust position and velocity instances instead of entity
				velocity.setX(d10 * f3 + d11 * f6);
				velocity.setZ(d10 * f5 + d11 * f4);
				f = f - k2 * 90 + j2 * 90;
			} else {
				// entity.motX = entity.motY = entity.motZ = 0.0D;
				velocity.setX(0);
				velocity.setY(0);
				velocity.setZ(0);
			}

			// entity.setPositionRotation(d8, d9, d4, entity.yaw, entity.pitch);
			position.setX(d8);
			position.setY(d9);
			position.setZ(d4);
			position.setYaw(f);
		}
		EntityPortalExitEvent event = new EntityPortalExitEvent(entity.getBukkitEntity(), from, position, before,
				velocity);
		Bukkit.getPluginManager().callEvent(event);
		Location to = event.getTo();
		if (event.isCancelled() || to == null || !entity.isEntityAlive()) {
			position.setX(from.getX());
			position.setY(from.getY());
			position.setZ(from.getZ());
			position.setYaw(from.getYaw());
			position.setPitch(from.getPitch());
			velocity.copy(before);
		} else {
			position.setX(to.getX());
			position.setY(to.getY());
			position.setZ(to.getZ());
			position.setYaw(to.getYaw());
			position.setPitch(to.getPitch());
			velocity.copy(event.getAfter()); // event.getAfter() will never be null, as setAfter() will cause an NPE if
												// null is passed in
		}
	}

	public boolean makePortal(Entity p_85188_1_) {
		// CraftBukkit start - Allow for portal creation to be based on coordinates
		// instead of entity
		return createPortal(p_85188_1_.posX, p_85188_1_.posY, p_85188_1_.posZ, 16);
	}

	public boolean createPortal(double x, double y, double z, int b0) {
		if (worldServerInstance.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
			createEndPortal(x, y, z);
			return true;
		}
		double d0 = -1.0D;
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y);
		int k = MathHelper.floor_double(z);
		int l = i;
		int i1 = j;
		int j1 = k;
		int k1 = 0;
		int l1 = random.nextInt(4);
		int i2;
		double d1;
		int j2;
		int k2;
		int l2;
		double d2;
		int i3;
		int j3;
		int k3;
		int l3;
		int i4;
		int j4;
		int k4;
		double d3;
		double d4;
		for (i2 = i - b0; i2 <= i + b0; ++i2) {
			d1 = i2 + 0.5D - x;
			for (j2 = k - b0; j2 <= k + b0; ++j2) {
				d2 = j2 + 0.5D - z;

				label294: for (k2 = worldServerInstance.getActualHeight() - 1; k2 >= 0; --k2) {
					if (worldServerInstance.isAirBlock(i2, k2, j2)) {
						while (k2 > 0 && worldServerInstance.isAirBlock(i2, k2 - 1, j2)) {
							--k2;
						}

						for (i3 = l1; i3 < l1 + 4; ++i3) {
							l2 = i3 % 2;
							k3 = 1 - l2;
							if (i3 % 4 >= 2) {
								l2 = -l2;
								k3 = -k3;
							}

							for (j3 = 0; j3 < 3; ++j3) {
								for (i4 = 0; i4 < 4; ++i4) {
									for (l3 = -1; l3 < 4; ++l3) {
										k4 = i2 + (i4 - 1) * l2 + j3 * k3;
										j4 = k2 + l3;
										int l4 = j2 + (i4 - 1) * k3 - j3 * l2;
										if (l3 < 0 && !worldServerInstance.getBlock(k4, j4, l4).getMaterial().isSolid()
												|| l3 >= 0 && !worldServerInstance.isAirBlock(k4, j4, l4)) {
											continue label294;
										}
									}
								}
							}

							d3 = k2 + 0.5D - y;
							d4 = d1 * d1 + d3 * d3 + d2 * d2;
							if (d0 < 0.0D || d4 < d0) {
								d0 = d4;
								l = i2;
								i1 = k2;
								j1 = j2;
								k1 = i3 % 4;
							}
						}
					}
				}
			}
		}

		if (d0 < 0.0D) {
			for (i2 = i - b0; i2 <= i + b0; ++i2) {
				d1 = i2 + 0.5D - x;
				for (j2 = k - b0; j2 <= k + b0; ++j2) {
					d2 = j2 + 0.5D - z;

					label232: for (k2 = worldServerInstance.getActualHeight() - 1; k2 >= 0; --k2) {
						if (worldServerInstance.isAirBlock(i2, k2, j2)) {
							while (k2 > 0 && worldServerInstance.isAirBlock(i2, k2 - 1, j2)) {
								--k2;
							}

							for (i3 = l1; i3 < l1 + 2; ++i3) {
								l2 = i3 % 2;
								k3 = 1 - l2;
								for (j3 = 0; j3 < 4; ++j3) {
									for (i4 = -1; i4 < 4; ++i4) {
										l3 = i2 + (j3 - 1) * l2;
										k4 = k2 + i4;
										j4 = j2 + (j3 - 1) * k3;
										if (i4 < 0 && !worldServerInstance.getBlock(l3, k4, j4).getMaterial().isSolid()
												|| i4 >= 0 && !worldServerInstance.isAirBlock(l3, k4, j4)) {
											continue label232;
										}
									}
								}

								d3 = k2 + 0.5D - y;
								d4 = d1 * d1 + d3 * d3 + d2 * d2;
								if (d0 < 0.0D || d4 < d0) {
									d0 = d4;
									l = i2;
									i1 = k2;
									j1 = j2;
									k1 = i3 % 2;
								}
							}
						}
					}
				}
			}
		}

		int i5 = l;
		int j5 = i1;
		j2 = j1;
		int k5 = k1 % 2;
		int l5 = 1 - k5;
		if (k1 % 4 >= 2) {
			k5 = -k5;
			l5 = -l5;
		}

		boolean flag;
		if (d0 < 0.0D) {
			if (i1 < 70) {
				i1 = 70;
			}

			if (i1 > worldServerInstance.getActualHeight() - 10) {
				i1 = worldServerInstance.getActualHeight() - 10;
			}

			j5 = i1;
			for (k2 = -1; k2 <= 1; ++k2) {
				for (i3 = 1; i3 < 3; ++i3) {
					for (l2 = -1; l2 < 3; ++l2) {
						k3 = i5 + (i3 - 1) * k5 + k2 * l5;
						j3 = j5 + l2;
						i4 = j2 + (i3 - 1) * l5 - k2 * k5;
						flag = l2 < 0;
						worldServerInstance.setBlock(k3, j3, i4, flag ? Blocks.obsidian : Blocks.air);
					}
				}
			}
		}

		for (k2 = 0; k2 < 4; ++k2) {
			for (i3 = 0; i3 < 4; ++i3) {
				for (l2 = -1; l2 < 4; ++l2) {
					k3 = i5 + (i3 - 1) * k5;
					j3 = j5 + l2;
					i4 = j2 + (i3 - 1) * l5;
					flag = i3 == 0 || i3 == 3 || l2 == -1 || l2 == 3;
					worldServerInstance.setBlock(k3, j3, i4, flag ? Blocks.obsidian : Blocks.portal, 0, 2);
				}
			}
			for (i3 = 0; i3 < 4; ++i3) {
				for (l2 = -1; l2 < 4; ++l2) {
					k3 = i5 + (i3 - 1) * k5;
					j3 = j5 + l2;
					i4 = j2 + (i3 - 1) * l5;
					worldServerInstance.notifyBlocksOfNeighborChange(k3, j3, i4,
							worldServerInstance.getBlock(k3, j3, i4));
				}
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void removeStalePortalLocations(long p_85189_1_) {
		if (p_85189_1_ % 100L == 0L) {
			Iterator iterator = destinationCoordinateKeys.iterator();
			long j = p_85189_1_ - 600L;

			while (iterator.hasNext()) {
				Long olong = (Long) iterator.next();
				Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) destinationCoordinateCache
						.getValueByKey(olong.longValue());

				if (portalposition == null || portalposition.lastUpdateTime < j) {
					iterator.remove();
					destinationCoordinateCache.remove(olong.longValue());
				}
			}
		}
	}

	public PortalPosition getPortalPositionInstance(int p_i1962_2_, int p_i1962_3_, int p_i1962_4_, long p_i1962_5_) {
		return new PortalPosition(p_i1962_2_, p_i1962_3_, p_i1962_4_, p_i1962_5_);
	}

	public class PortalPosition extends ChunkCoordinates {
		public long lastUpdateTime;
		public PortalPosition(int p_i1962_2_, int p_i1962_3_, int p_i1962_4_, long p_i1962_5_) {
			super(p_i1962_2_, p_i1962_3_, p_i1962_4_);
			lastUpdateTime = p_i1962_5_;
		}
	}
}