package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MobSpawnerBaseLogic {
	public int spawnDelay = 20;
	private String entityTypeName = "Pig";
	private List potentialEntitySpawns;
	private MobSpawnerBaseLogic.WeightedRandomMinecart randomEntity;
	public double field_98287_c;
	public double field_98284_d;
	private int minSpawnDelay = 200;
	private int maxSpawnDelay = 800;
	private int spawnCount = 4;
	private Entity field_98291_j;
	private int maxNearbyEntities = 6;
	private int activatingRangeFromPlayer = 16;
	private int spawnRange = 4;
	private static final String __OBFID = "CL_00000129";

	public String getEntityNameToSpawn() {
		if (getRandomEntity() == null) {
			if (entityTypeName.equals("Minecart")) {
				entityTypeName = "MinecartRideable";
			}

			return entityTypeName;
		} else
			return getRandomEntity().entityTypeName;
	}

	public void setEntityName(String p_98272_1_) {
		entityTypeName = p_98272_1_;
	}

	public boolean isActivated() {
		return getSpawnerWorld().getClosestPlayer(getSpawnerX() + 0.5D, getSpawnerY() + 0.5D, getSpawnerZ() + 0.5D,
				activatingRangeFromPlayer) != null;
	}

	public void updateSpawner() {
		if (isActivated()) {
			double d2;

			if (getSpawnerWorld().isRemote) {
				double d0 = getSpawnerX() + getSpawnerWorld().rand.nextFloat();
				double d1 = getSpawnerY() + getSpawnerWorld().rand.nextFloat();
				d2 = getSpawnerZ() + getSpawnerWorld().rand.nextFloat();
				getSpawnerWorld().spawnParticle("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
				getSpawnerWorld().spawnParticle("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);

				if (spawnDelay > 0) {
					--spawnDelay;
				}

				field_98284_d = field_98287_c;
				field_98287_c = (field_98287_c + 1000.0F / (spawnDelay + 200.0F)) % 360.0D;
			} else {
				if (spawnDelay == -1) {
					resetTimer();
				}

				if (spawnDelay > 0) {
					--spawnDelay;
					return;
				}

				boolean flag = false;

				for (int i = 0; i < spawnCount; ++i) {
					Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());

					if (entity == null)
						return;

					int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(),
							AxisAlignedBB
									.getBoundingBox(getSpawnerX(), getSpawnerY(), getSpawnerZ(), getSpawnerX() + 1,
											getSpawnerY() + 1, getSpawnerZ() + 1)
									.expand(spawnRange * 2, 4.0D, spawnRange * 2))
							.size();

					if (j >= maxNearbyEntities) {
						resetTimer();
						return;
					}

					d2 = getSpawnerX()
							+ (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * spawnRange;
					double d3 = getSpawnerY() + getSpawnerWorld().rand.nextInt(3) - 1;
					double d4 = getSpawnerZ()
							+ (getSpawnerWorld().rand.nextDouble() - getSpawnerWorld().rand.nextDouble()) * spawnRange;
					EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
					entity.setLocationAndAngles(d2, d3, d4, getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

					if (entityliving == null || entityliving.getCanSpawnHere()) {
						func_98265_a(entity);
						getSpawnerWorld().playAuxSFX(2004, getSpawnerX(), getSpawnerY(), getSpawnerZ(), 0);

						if (entityliving != null) {
							entityliving.spawnExplosionParticle();
						}

						flag = true;
					}
				}

				if (flag) {
					resetTimer();
				}
			}
		}
	}

	public Entity func_98265_a(Entity p_98265_1_) {
		if (getRandomEntity() != null) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			p_98265_1_.writeToNBTOptional(nbttagcompound);
			Iterator iterator = getRandomEntity().field_98222_b.func_150296_c().iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				NBTBase nbtbase = getRandomEntity().field_98222_b.getTag(s);
				nbttagcompound.setTag(s, nbtbase.copy());
			}

			p_98265_1_.readFromNBT(nbttagcompound);

			if (p_98265_1_.worldObj != null) {
				p_98265_1_.worldObj.spawnEntityInWorld(p_98265_1_);
			}

			NBTTagCompound nbttagcompound2;

			for (Entity entity1 = p_98265_1_; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2) {
				nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
				Entity entity2 = EntityList.createEntityByName(nbttagcompound2.getString("id"), p_98265_1_.worldObj);

				if (entity2 != null) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					entity2.writeToNBTOptional(nbttagcompound1);
					Iterator iterator1 = nbttagcompound2.func_150296_c().iterator();

					while (iterator1.hasNext()) {
						String s1 = (String) iterator1.next();
						NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
						nbttagcompound1.setTag(s1, nbtbase1.copy());
					}

					entity2.readFromNBT(nbttagcompound1);
					entity2.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw,
							entity1.rotationPitch);

					if (p_98265_1_.worldObj != null) {
						p_98265_1_.worldObj.spawnEntityInWorld(entity2);
					}

					entity1.mountEntity(entity2);
				}

				entity1 = entity2;
			}
		} else if (p_98265_1_ instanceof EntityLivingBase && p_98265_1_.worldObj != null) {
			((EntityLiving) p_98265_1_).onSpawnWithEgg((IEntityLivingData) null);
			getSpawnerWorld().spawnEntityInWorld(p_98265_1_);
		}

		return p_98265_1_;
	}

	private void resetTimer() {
		if (maxSpawnDelay <= minSpawnDelay) {
			spawnDelay = minSpawnDelay;
		} else {
			int i = maxSpawnDelay - minSpawnDelay;
			spawnDelay = minSpawnDelay + getSpawnerWorld().rand.nextInt(i);
		}

		if (potentialEntitySpawns != null && potentialEntitySpawns.size() > 0) {
			setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart) WeightedRandom
					.getRandomItem(getSpawnerWorld().rand, potentialEntitySpawns));
		}

		func_98267_a(1);
	}

	public void readFromNBT(NBTTagCompound p_98270_1_) {
		entityTypeName = p_98270_1_.getString("EntityId");
		spawnDelay = p_98270_1_.getShort("Delay");

		if (p_98270_1_.hasKey("SpawnPotentials", 9)) {
			potentialEntitySpawns = new ArrayList();
			NBTTagList nbttaglist = p_98270_1_.getTagList("SpawnPotentials", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				potentialEntitySpawns
						.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
			}
		} else {
			potentialEntitySpawns = null;
		}

		if (p_98270_1_.hasKey("SpawnData", 10)) {
			setRandomEntity(new MobSpawnerBaseLogic.WeightedRandomMinecart(p_98270_1_.getCompoundTag("SpawnData"),
					entityTypeName));
		} else {
			setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart) null);
		}

		if (p_98270_1_.hasKey("MinSpawnDelay", 99)) {
			minSpawnDelay = p_98270_1_.getShort("MinSpawnDelay");
			maxSpawnDelay = p_98270_1_.getShort("MaxSpawnDelay");
			spawnCount = p_98270_1_.getShort("SpawnCount");
		}

		if (p_98270_1_.hasKey("MaxNearbyEntities", 99)) {
			maxNearbyEntities = p_98270_1_.getShort("MaxNearbyEntities");
			activatingRangeFromPlayer = p_98270_1_.getShort("RequiredPlayerRange");
		}

		if (p_98270_1_.hasKey("SpawnRange", 99)) {
			spawnRange = p_98270_1_.getShort("SpawnRange");
		}

		if (getSpawnerWorld() != null && getSpawnerWorld().isRemote) {
			field_98291_j = null;
		}
	}

	public void writeToNBT(NBTTagCompound p_98280_1_) {
		p_98280_1_.setString("EntityId", getEntityNameToSpawn());
		p_98280_1_.setShort("Delay", (short) spawnDelay);
		p_98280_1_.setShort("MinSpawnDelay", (short) minSpawnDelay);
		p_98280_1_.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
		p_98280_1_.setShort("SpawnCount", (short) spawnCount);
		p_98280_1_.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
		p_98280_1_.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
		p_98280_1_.setShort("SpawnRange", (short) spawnRange);

		if (getRandomEntity() != null) {
			p_98280_1_.setTag("SpawnData", getRandomEntity().field_98222_b.copy());
		}

		if (getRandomEntity() != null || potentialEntitySpawns != null && potentialEntitySpawns.size() > 0) {
			NBTTagList nbttaglist = new NBTTagList();

			if (potentialEntitySpawns != null && potentialEntitySpawns.size() > 0) {
				Iterator iterator = potentialEntitySpawns.iterator();

				while (iterator.hasNext()) {
					MobSpawnerBaseLogic.WeightedRandomMinecart weightedrandomminecart = (MobSpawnerBaseLogic.WeightedRandomMinecart) iterator
							.next();
					nbttaglist.appendTag(weightedrandomminecart.func_98220_a());
				}
			} else {
				nbttaglist.appendTag(getRandomEntity().func_98220_a());
			}

			p_98280_1_.setTag("SpawnPotentials", nbttaglist);
		}
	}

	public boolean setDelayToMin(int p_98268_1_) {
		if (p_98268_1_ == 1 && getSpawnerWorld().isRemote) {
			spawnDelay = minSpawnDelay;
			return true;
		} else
			return false;
	}

	@SideOnly(Side.CLIENT)
	public Entity func_98281_h() {
		if (field_98291_j == null) {
			Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), (World) null);
			entity = func_98265_a(entity);
			field_98291_j = entity;
		}

		return field_98291_j;
	}

	public MobSpawnerBaseLogic.WeightedRandomMinecart getRandomEntity() {
		return randomEntity;
	}

	public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
		randomEntity = p_98277_1_;
	}

	public abstract void func_98267_a(int p_98267_1_);

	public abstract World getSpawnerWorld();

	public abstract int getSpawnerX();

	public abstract int getSpawnerY();

	public abstract int getSpawnerZ();

	public class WeightedRandomMinecart extends WeightedRandom.Item {
		public final NBTTagCompound field_98222_b;
		public final String entityTypeName;
		private static final String __OBFID = "CL_00000130";

		public WeightedRandomMinecart(NBTTagCompound p_i1945_2_) {
			super(p_i1945_2_.getInteger("Weight"));
			NBTTagCompound nbttagcompound1 = p_i1945_2_.getCompoundTag("Properties");
			String s = p_i1945_2_.getString("Type");

			if (s.equals("Minecart")) {
				if (nbttagcompound1 != null) {
					switch (nbttagcompound1.getInteger("Type")) {
					case 0:
						s = "MinecartRideable";
						break;
					case 1:
						s = "MinecartChest";
						break;
					case 2:
						s = "MinecartFurnace";
					}
				} else {
					s = "MinecartRideable";
				}
			}

			field_98222_b = nbttagcompound1;
			entityTypeName = s;
		}

		public WeightedRandomMinecart(NBTTagCompound p_i1946_2_, String p_i1946_3_) {
			super(1);

			if (p_i1946_3_.equals("Minecart")) {
				if (p_i1946_2_ != null) {
					switch (p_i1946_2_.getInteger("Type")) {
					case 0:
						p_i1946_3_ = "MinecartRideable";
						break;
					case 1:
						p_i1946_3_ = "MinecartChest";
						break;
					case 2:
						p_i1946_3_ = "MinecartFurnace";
					}
				} else {
					p_i1946_3_ = "MinecartRideable";
				}
			}

			field_98222_b = p_i1946_2_;
			entityTypeName = p_i1946_3_;
		}

		public NBTTagCompound func_98220_a() {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("Properties", field_98222_b);
			nbttagcompound.setString("Type", entityTypeName);
			nbttagcompound.setInteger("Weight", itemWeight);
			return nbttagcompound;
		}
	}
}