package net.minecraft.entity.effect;

import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.ultramine.bukkit.util.LightningEffectSwitcher;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityLightningBolt extends EntityWeatherEffect {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;
	private static final String __OBFID = "CL_00001666";

	public EntityLightningBolt(World p_i1703_1_, double p_i1703_2_, double p_i1703_4_, double p_i1703_6_) {
		super(p_i1703_1_);
		setLocationAndAngles(p_i1703_2_, p_i1703_4_, p_i1703_6_, 0.0F, 0.0F);
		lightningState = 2;
		boltVertex = rand.nextLong();
		boltLivingTime = rand.nextInt(3) + 1;
		if (!LightningEffectSwitcher.isEffect && !p_i1703_1_.isRemote
				&& p_i1703_1_.getGameRules().getGameRuleBooleanValue("doFireTick")
				&& (p_i1703_1_.difficultySetting == EnumDifficulty.NORMAL
						|| p_i1703_1_.difficultySetting == EnumDifficulty.HARD)
				&& p_i1703_1_.doChunksNearChunkExist(MathHelper.floor_double(p_i1703_2_),
						MathHelper.floor_double(p_i1703_4_), MathHelper.floor_double(p_i1703_6_), 10)) {
			int i = MathHelper.floor_double(p_i1703_2_);
			int j = MathHelper.floor_double(p_i1703_4_);
			int k = MathHelper.floor_double(p_i1703_6_);
			if (p_i1703_1_.getBlock(i, j, k).getMaterial() == Material.air
					&& Blocks.fire.canPlaceBlockAt(p_i1703_1_, i, j, k))
				if (!CraftEventFactory.callBlockIgniteEvent(p_i1703_1_, i, j, k, this).isCancelled()) {
					p_i1703_1_.setBlock(i, j, k, Blocks.fire);
				}
			for (i = 0; i < 4; ++i) {
				j = MathHelper.floor_double(p_i1703_2_) + rand.nextInt(3) - 1;
				k = MathHelper.floor_double(p_i1703_4_) + rand.nextInt(3) - 1;
				int l = MathHelper.floor_double(p_i1703_6_) + rand.nextInt(3) - 1;
				if (p_i1703_1_.getBlock(j, k, l).getMaterial() == Material.air
						&& Blocks.fire.canPlaceBlockAt(p_i1703_1_, j, k, l))
					if (!CraftEventFactory.callBlockIgniteEvent(p_i1703_1_, j, k, l, this).isCancelled()) {
						p_i1703_1_.setBlock(j, k, l, Blocks.fire);
					}
			}
		}
		LightningEffectSwitcher.isEffect = false;

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (lightningState == 2) {
			worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 10000.0F,
					0.8F + rand.nextFloat() * 0.2F);
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2.0F, 0.5F + rand.nextFloat() * 0.2F);
		}
		--lightningState;
		if (lightningState < 0) {
			if (boltLivingTime == 0) {
				setDead();
			} else if (lightningState < -rand.nextInt(10)) {
				--boltLivingTime;
				lightningState = 1;
				boltVertex = rand.nextLong();
				if (!LightningEffectSwitcher.isEffect && !worldObj.isRemote
						&& worldObj.getGameRules().getGameRuleBooleanValue("doFireTick")
						&& worldObj.doChunksNearChunkExist(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
								MathHelper.floor_double(posZ), 10)) {
					int i = MathHelper.floor_double(posX);
					int j = MathHelper.floor_double(posY);
					int k = MathHelper.floor_double(posZ);
					if (worldObj.getBlock(i, j, k).getMaterial() == Material.air
							&& Blocks.fire.canPlaceBlockAt(worldObj, i, j, k))
						if (!CraftEventFactory.callBlockIgniteEvent(worldObj, i, j, k, this).isCancelled()) {
							worldObj.setBlock(i, j, k, Blocks.fire);
						}
				}
			}
		}
		if (lightningState >= 0 && !LightningEffectSwitcher.isEffect) {
			if (worldObj.isRemote) {
				worldObj.lastLightningBolt = 2;
			} else {
				double d0 = 3.0D;
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX - d0,
						posY - d0, posZ - d0, posX + d0, posY + 6.0D + d0, posZ + d0));
				for (Object aList : list) {
					Entity entity = (Entity) aList;
					if (!ForgeEventFactory.onEntityStruckByLightning(entity, this)) {
						entity.onStruckByLightning(this);
					}
				}
			}
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
}