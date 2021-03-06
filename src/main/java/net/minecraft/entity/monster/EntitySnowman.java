package net.minecraft.entity.monster;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.EntityBlockFormEvent;

public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
	private static final String __OBFID = "CL_00001650";

	public EntitySnowman(World p_i1692_1_) {
		super(p_i1692_1_);
		setSize(0.4F, 1.8F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
		tasks.addTask(2, new EntityAIWander(this, 1.0D));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(4, new EntityAILookIdle(this));
		targetTasks.addTask(1,
				new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, IMob.mobSelector));
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		if (isWet()) {
			attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j, k) > 1.0F) {
			attackEntityFrom(CraftEventFactory.MELTING, 1.0F);
		}

		for (int l = 0; l < 4; ++l) {
			i = MathHelper.floor_double(posX + (l % 2 * 2 - 1) * 0.25F);
			j = MathHelper.floor_double(posY);
			k = MathHelper.floor_double(posZ + (l / 2 % 2 * 2 - 1) * 0.25F);

			if (worldObj.getBlock(i, j, k).getMaterial() == Material.air
					&& worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j, k) < 0.8F
					&& Blocks.snow_layer.canPlaceBlockAt(worldObj, i, j, k)) {

				BlockState blockState = worldObj.getWorld().getBlockAt(i, j, k).getState();
				blockState.setType(CraftMagicNumbers.getMaterial(Blocks.snow_layer));
				EntityBlockFormEvent event = new EntityBlockFormEvent(this.getBukkitEntity(), blockState.getBlock(),
						blockState);
				worldObj.getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					blockState.update(true);
				}
			}
		}
	}

	@Override
	protected Item getDropItem() {
		return Items.snowball;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(16);

		for (int k = 0; k < j; ++k) {
			dropItem(Items.snowball, 1);
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
		EntitySnowball entitysnowball = new EntitySnowball(worldObj, this);
		double d0 = p_82196_1_.posX - posX;
		double d1 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D - entitysnowball.posY;
		double d2 = p_82196_1_.posZ - posZ;
		float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2) * 0.2F;
		entitysnowball.setThrowableHeading(d0, d1 + f1, d2, 1.6F, 12.0F);
		playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
		worldObj.spawnEntityInWorld(entitysnowball);
	}
}