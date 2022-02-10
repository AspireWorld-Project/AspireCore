package net.minecraft.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime {
	private static final String __OBFID = "CL_00001691";

	public EntityMagmaCube(World p_i1737_1_) {
		super(p_i1737_1_);
		isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
	}

	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL && worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty()
				&& !worldObj.isAnyLiquid(boundingBox);
	}

	@Override
	public int getTotalArmorValue() {
		return getSlimeSize() * 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	protected String getSlimeParticle() {
		return "flame";
	}

	@Override
	protected EntitySlime createInstance() {
		return new EntityMagmaCube(worldObj);
	}

	@Override
	protected Item getDropItem() {
		return Items.magma_cream;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		Item item = getDropItem();

		if (item != null && getSlimeSize() > 1) {
			int j = rand.nextInt(4) - 2;

			if (p_70628_2_ > 0) {
				j += rand.nextInt(p_70628_2_ + 1);
			}

			for (int k = 0; k < j; ++k) {
				dropItem(item, 1);
			}
		}
	}

	@Override
	public boolean isBurning() {
		return false;
	}

	@Override
	protected int getJumpDelay() {
		return super.getJumpDelay() * 4;
	}

	@Override
	protected void alterSquishAmount() {
		squishAmount *= 0.9F;
	}

	@Override
	protected void jump() {
		motionY = 0.42F + getSlimeSize() * 0.1F;
		isAirBorne = true;
		net.minecraftforge.common.ForgeHooks.onLivingJump(this);
	}

	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected boolean canDamagePlayer() {
		return true;
	}

	@Override
	protected int getAttackStrength() {
		return super.getAttackStrength() + 2;
	}

	@Override
	protected String getJumpSound() {
		return getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
	}

	@Override
	public boolean handleLavaMovement() {
		return false;
	}

	@Override
	protected boolean makesSoundOnLand() {
		return true;
	}
}