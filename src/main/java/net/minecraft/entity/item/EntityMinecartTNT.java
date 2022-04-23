package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart {
	private int minecartTNTFuse = -1;
	private static final String __OBFID = "CL_00001680";

	public EntityMinecartTNT(World p_i1727_1_) {
		super(p_i1727_1_);
	}

	public EntityMinecartTNT(World p_i1728_1_, double p_i1728_2_, double p_i1728_4_, double p_i1728_6_) {
		super(p_i1728_1_, p_i1728_2_, p_i1728_4_, p_i1728_6_);
	}

	@Override
	public int getMinecartType() {
		return 3;
	}

	@Override
	public Block func_145817_o() {
		return Blocks.tnt;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (minecartTNTFuse > 0) {
			--minecartTNTFuse;
			worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
		} else if (minecartTNTFuse == 0) {
			explodeCart(motionX * motionX + motionZ * motionZ);
		}

		if (isCollidedHorizontally) {
			double d0 = motionX * motionX + motionZ * motionZ;

			if (d0 >= 0.009999999776482582D) {
				explodeCart(d0);
			}
		}
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);
		double d0 = motionX * motionX + motionZ * motionZ;

		if (!p_94095_1_.isExplosion()) {
			entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0F);
		}

		if (p_94095_1_.isFireDamage() || p_94095_1_.isExplosion() || d0 >= 0.009999999776482582D) {
			explodeCart(d0);
		}
	}

	protected void explodeCart(double p_94103_1_) {
		if (!worldObj.isRemote) {
			double d1 = Math.sqrt(p_94103_1_);

			if (d1 > 5.0D) {
				d1 = 5.0D;
			}

			worldObj.createExplosion(this, posX, posY, posZ, (float) (4.0D + rand.nextDouble() * 1.5D * d1), true);
			setDead();
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
		if (p_70069_1_ >= 3.0F) {
			float f1 = p_70069_1_ / 10.0F;
			explodeCart(f1 * f1);
		}

		super.fall(p_70069_1_);
	}

	@Override
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
		if (p_96095_4_ && minecartTNTFuse < 0) {
			ignite();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 10) {
			ignite();
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public void ignite() {
		minecartTNTFuse = 80;

		if (!worldObj.isRemote) {
			worldObj.setEntityState(this, (byte) 10);
			worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
		}
	}

	@SideOnly(Side.CLIENT)
	public int func_94104_d() {
		return minecartTNTFuse;
	}

	public boolean isIgnited() {
		return minecartTNTFuse > -1;
	}

	@Override
	public float func_145772_a(Explosion p_145772_1_, World p_145772_2_, int p_145772_3_, int p_145772_4_,
			int p_145772_5_, Block p_145772_6_) {
		return isIgnited() && (BlockRailBase.func_150051_a(p_145772_6_)
				|| BlockRailBase.func_150049_b_(p_145772_2_, p_145772_3_, p_145772_4_ + 1, p_145772_5_)) ? 0.0F
						: super.func_145772_a(p_145772_1_, p_145772_2_, p_145772_3_, p_145772_4_, p_145772_5_,
								p_145772_6_);
	}

	@Override
	public boolean func_145774_a(Explosion p_145774_1_, World p_145774_2_, int p_145774_3_, int p_145774_4_,
			int p_145774_5_, Block p_145774_6_, float p_145774_7_) {
		return (!isIgnited() || (!BlockRailBase.func_150051_a(p_145774_6_)
				&& !BlockRailBase.func_150049_b_(p_145774_2_, p_145774_3_, p_145774_4_ + 1, p_145774_5_))) && super.func_145774_a(p_145774_1_, p_145774_2_, p_145774_3_, p_145774_4_, p_145774_5_,
				p_145774_6_, p_145774_7_);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("TNTFuse", 99)) {
			minecartTNTFuse = p_70037_1_.getInteger("TNTFuse");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("TNTFuse", minecartTNTFuse);
	}
}