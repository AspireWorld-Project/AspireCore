package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.*;

public class Explosion {
	public boolean isFlaming;
	public boolean isSmoking = true;
	private final int field_77289_h = 16;
	private final Random explosionRNG = new Random();
	private final World worldObj;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public Entity exploder;
	public float explosionSize;
	@SuppressWarnings("rawtypes")
	public List affectedBlockPositions = new ArrayList();
	@SuppressWarnings("rawtypes")
	private final Map field_77288_k = new HashMap();
	public Explosion(World p_i1948_1_, Entity p_i1948_2_, double p_i1948_3_, double p_i1948_5_, double p_i1948_7_,
			float p_i1948_9_) {
		worldObj = p_i1948_1_;
		exploder = p_i1948_2_;
		explosionSize = p_i1948_9_;
		explosionX = p_i1948_3_;
		explosionY = p_i1948_5_;
		explosionZ = p_i1948_7_;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doExplosionA() {
		// CraftBukkit start
		if (explosionSize < 0.1F)
			return;
		// CraftBukkit end
		float f = explosionSize;
		HashSet hashset = new HashSet();
		int i;
		int j;
		int k;
		double d5;
		double d6;
		double d7;

		for (i = 0; i < field_77289_h; ++i) {
			for (j = 0; j < field_77289_h; ++j) {
				for (k = 0; k < field_77289_h; ++k) {
					if (i == 0 || i == field_77289_h - 1 || j == 0 || j == field_77289_h - 1 || k == 0
							|| k == field_77289_h - 1) {
						double d0 = i / (field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d1 = j / (field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d2 = k / (field_77289_h - 1.0F) * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f1 = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
						d5 = explosionX;
						d6 = explosionY;
						d7 = explosionZ;

						for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
							int j1 = MathHelper.floor_double(d5);
							int k1 = MathHelper.floor_double(d6);
							int l1 = MathHelper.floor_double(d7);
							Block block = worldObj.getBlock(j1, k1, l1);

							if (block.getMaterial() != Material.air) {
								float f3 = exploder != null ? exploder.func_145772_a(this, worldObj, j1, k1, l1, block)
										: block.getExplosionResistance(exploder, worldObj, j1, k1, l1, explosionX,
												explosionY, explosionZ);
								f1 -= (f3 + 0.3F) * f2;
							}

							if (f1 > 0.0F && (exploder == null
									|| exploder.func_145774_a(this, worldObj, j1, k1, l1, block, f1))) {
								hashset.add(new ChunkPosition(j1, k1, l1));
							}

							d5 += d0 * f2;
							d6 += d1 * f2;
							d7 += d2 * f2;
						}
					}
				}
			}
		}

		affectedBlockPositions.addAll(hashset);
		explosionSize *= 2.0F;
		i = MathHelper.floor_double(explosionX - explosionSize - 1.0D);
		j = MathHelper.floor_double(explosionX + explosionSize + 1.0D);
		k = MathHelper.floor_double(explosionY - explosionSize - 1.0D);
		int i2 = MathHelper.floor_double(explosionY + explosionSize + 1.0D);
		int l = MathHelper.floor_double(explosionZ - explosionSize - 1.0D);
		int j2 = MathHelper.floor_double(explosionZ + explosionSize + 1.0D);
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(exploder,
				AxisAlignedBB.getBoundingBox(i, k, l, j, i2, j2));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(worldObj, this, list, explosionSize);
		Vec3 vec3 = Vec3.createVectorHelper(explosionX, explosionY, explosionZ);

		for (int i1 = 0; i1 < list.size(); ++i1) {
			Entity entity = (Entity) list.get(i1);
			double d4 = entity.getDistance(explosionX, explosionY, explosionZ) / explosionSize;

			if (d4 <= 1.0D) {
				d5 = entity.posX - explosionX;
				d6 = entity.posY + entity.getEyeHeight() - explosionY;
				d7 = entity.posZ - explosionZ;
				double d9 = MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);

				if (d9 != 0.0D) {
					d5 /= d9;
					d6 /= d9;
					d7 /= d9;
					double d10 = worldObj.getBlockDensity(vec3, entity.boundingBox);
					double d11 = (1.0D - d4) * d10;
					// CraftBukkit start
					CraftEventFactory.entityDamage = exploder;
					if (!entity.attackEntityFrom(DamageSource.setExplosionSource(this),
							(int) ((d11 * d11 + d11) / 2.0D * 8.0D * explosionSize + 1.0D))) {
						CraftEventFactory.entityDamage = null;
					}
					// if (!MinecraftServer.cauldronConfig.allowTntPunishment.getValue()) continue;
					// CraftBukkit end
					double d8 = EnchantmentProtection.func_92092_a(entity, d11);
					entity.motionX += d5 * d8;
					entity.motionY += d6 * d8;
					entity.motionZ += d7 * d8;

					if (entity instanceof EntityPlayer) {
						field_77288_k.put(entity, Vec3.createVectorHelper(d5 * d11, d6 * d11, d7 * d11));
					}
				}
			}
		}

		explosionSize = f;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doExplosionB(boolean p_77279_1_) {
		worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0F,
				(1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (explosionSize >= 2.0F && isSmoking) {
			worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
		} else {
			worldObj.spawnParticle("largeexplode", explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
		}

		Iterator iterator;
		ChunkPosition chunkposition;
		int i;
		int j;
		int k;
		Block block;

		if (isSmoking) {
			// CraftBukkit start
			org.bukkit.World bworld = worldObj.getWorld();
			org.bukkit.entity.Entity explode = exploder == null ? null : exploder.getBukkitEntity();
			Location location = new Location(bworld, explosionX, explosionY, explosionZ);
			List<org.bukkit.block.Block> blockList = new ArrayList<>();

			for (int i1 = affectedBlockPositions.size() - 1; i1 >= 0; i1--) {
				ChunkPosition cpos = (ChunkPosition) affectedBlockPositions.get(i1);
				org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.chunkPosX, cpos.chunkPosY, cpos.chunkPosZ);

				if (bblock.getType() != org.bukkit.Material.AIR) {
					blockList.add(bblock);
				}
			}

			EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, 0.3F);
			Bukkit.getServer().getPluginManager().callEvent(event);
			affectedBlockPositions.clear();

			for (org.bukkit.block.Block bblock : event.blockList()) {
				ChunkPosition coords = new ChunkPosition(bblock.getX(), bblock.getY(), bblock.getZ());
				affectedBlockPositions.add(coords);
			}

			if (event.isCancelled()) {
				wasCanceled = true;
				return;
			}
			// CraftBukkit end
			iterator = affectedBlockPositions.iterator();

			while (iterator.hasNext()) {
				chunkposition = (ChunkPosition) iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = worldObj.getBlock(i, j, k);

				if (p_77279_1_) {
					double d0 = i + worldObj.rand.nextFloat();
					double d1 = j + worldObj.rand.nextFloat();
					double d2 = k + worldObj.rand.nextFloat();
					double d3 = d0 - explosionX;
					double d4 = d1 - explosionY;
					double d5 = d2 - explosionZ;
					double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / (d6 / explosionSize + 0.1D);
					d7 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					worldObj.spawnParticle("explode", (d0 + explosionX * 1.0D) / 2.0D, (d1 + explosionY * 1.0D) / 2.0D,
							(d2 + explosionZ * 1.0D) / 2.0D, d3, d4, d5);
					worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
				}

				if (block.getMaterial() != Material.air) {
					if (block.canDropFromExplosion(this)) {
						// CraftBukkit - add yield
						block.dropBlockAsItemWithChance(worldObj, i, j, k, worldObj.getBlockMetadata(i, j, k),
								event.getYield(), 0);
					}

					block.onBlockExploded(worldObj, i, j, k, this);
				}
			}
		}

		if (isFlaming) {
			iterator = affectedBlockPositions.iterator();

			while (iterator.hasNext()) {
				chunkposition = (ChunkPosition) iterator.next();
				i = chunkposition.chunkPosX;
				j = chunkposition.chunkPosY;
				k = chunkposition.chunkPosZ;
				block = worldObj.getBlock(i, j, k);
				Block block1 = worldObj.getBlock(i, j - 1, k);

				if (block.getMaterial() == Material.air && block1.func_149730_j() && explosionRNG.nextInt(3) == 0) {
					// CraftBukkit start - Ignition by explosion
					if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(worldObj, i, j, k, this)
							.isCancelled()) {
						worldObj.setBlock(i, j, k, Blocks.fire);
					}
					// CraftBukkit end
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Map func_77277_b() {
		return field_77288_k;
	}

	public EntityLivingBase getExplosivePlacedBy() {
		return exploder == null ? null
				: exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) exploder).getTntPlacedBy()
						: exploder instanceof EntityLivingBase ? (EntityLivingBase) exploder : null;
	}

	public boolean wasCanceled = false; // CraftBukkit

	public boolean wasCanceled() {
		return wasCanceled;
	}
}