package net.minecraft.village;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class VillageSiege {
	private final World worldObj;
	private boolean field_75535_b;
	private int field_75536_c = -1;
	private int field_75533_d;
	private int field_75534_e;
	private Village theVillage;
	private int field_75532_g;
	private int field_75538_h;
	private int field_75539_i;
	public VillageSiege(World p_i1676_1_) {
		worldObj = p_i1676_1_;
	}

	public void tick() {
		boolean flag = false;

		if (flag) {
			if (field_75536_c == 2) {
				field_75533_d = 100;
				return;
			}
		} else {
			if (worldObj.isDaytime()) {
				field_75536_c = 0;
				return;
			}

			if (field_75536_c == 2)
				return;

			if (field_75536_c == 0) {
				float f = worldObj.getCelestialAngle(0.0F);

				if (f < 0.5D || f > 0.501D)
					return;

				field_75536_c = worldObj.rand.nextInt(10) == 0 ? 1 : 2;
				field_75535_b = false;

				if (field_75536_c == 2)
					return;
			}
		}

		if (!field_75535_b) {
			if (!func_75529_b())
				return;

			field_75535_b = true;
		}

		if (field_75534_e > 0) {
			--field_75534_e;
		} else {
			field_75534_e = 2;

			if (field_75533_d > 0) {
				spawnZombie();
				--field_75533_d;
			} else {
				field_75536_c = 2;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean func_75529_b() {
		List list = worldObj.playerEntities;
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			EntityPlayer entityplayer = (EntityPlayer) iterator.next();
			theVillage = worldObj.villageCollectionObj.findNearestVillage((int) entityplayer.posX,
					(int) entityplayer.posY, (int) entityplayer.posZ, 1);

			if (theVillage != null && theVillage.getNumVillageDoors() >= 10
					&& theVillage.getTicksSinceLastDoorAdding() >= 20 && theVillage.getNumVillagers() >= 20) {
				ChunkCoordinates chunkcoordinates = theVillage.getCenter();
				float f = theVillage.getVillageRadius();
				boolean flag = false;
				int i = 0;

				while (true) {
					if (i < 10) {
						field_75532_g = chunkcoordinates.posX
								+ (int) (MathHelper.cos(worldObj.rand.nextFloat() * (float) Math.PI * 2.0F) * f * 0.9D);
						field_75538_h = chunkcoordinates.posY;
						field_75539_i = chunkcoordinates.posZ
								+ (int) (MathHelper.sin(worldObj.rand.nextFloat() * (float) Math.PI * 2.0F) * f * 0.9D);
						flag = false;
						Iterator iterator1 = worldObj.villageCollectionObj.getVillageList().iterator();

						while (iterator1.hasNext()) {
							Village village = (Village) iterator1.next();

							if (village != theVillage
									&& village.isInRange(field_75532_g, field_75538_h, field_75539_i)) {
								flag = true;
								break;
							}
						}

						if (flag) {
							++i;
							continue;
						}
					}

					if (flag)
						return false;

					Vec3 vec3 = func_75527_a(field_75532_g, field_75538_h, field_75539_i);

					if (vec3 != null) {
						field_75534_e = 0;
						field_75533_d = 20;
						return true;
					}

					break;
				}
			}
		}

		return false;
	}

	private boolean spawnZombie() {
		Vec3 vec3 = func_75527_a(field_75532_g, field_75538_h, field_75539_i);

		if (vec3 == null)
			return false;
		else {
			EntityZombie entityzombie;

			try {
				entityzombie = new EntityZombie(worldObj);
				entityzombie.onSpawnWithEgg(null);
				entityzombie.setVillager(false);
			} catch (Exception exception) {
				exception.printStackTrace();
				return false;
			}

			entityzombie.setLocationAndAngles(vec3.xCoord, vec3.yCoord, vec3.zCoord, worldObj.rand.nextFloat() * 360.0F,
					0.0F);
			worldObj.spawnEntityInWorld(entityzombie);
			ChunkCoordinates chunkcoordinates = theVillage.getCenter();
			entityzombie.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ,
					theVillage.getVillageRadius());
			return true;
		}
	}

	private Vec3 func_75527_a(int p_75527_1_, int p_75527_2_, int p_75527_3_) {
		for (int l = 0; l < 10; ++l) {
			int i1 = p_75527_1_ + worldObj.rand.nextInt(16) - 8;
			int j1 = p_75527_2_ + worldObj.rand.nextInt(6) - 3;
			int k1 = p_75527_3_ + worldObj.rand.nextInt(16) - 8;

			if (theVillage.isInRange(i1, j1, k1)
					&& SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, worldObj, i1, j1, k1)) {
				Vec3.createVectorHelper(i1, j1, k1);
			}
		}

		return null;
	}
}