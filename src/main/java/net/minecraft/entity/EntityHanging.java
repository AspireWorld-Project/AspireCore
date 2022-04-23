package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Painting;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.ultramine.server.internal.UMEventFactory;

import java.util.Iterator;
import java.util.List;

public abstract class EntityHanging extends Entity {
	private int tickCounter1;
	public int hangingDirection;
	public int field_146063_b;
	public int field_146064_c;
	public int field_146062_d;
	private static final String __OBFID = "CL_00001546";

	public EntityHanging(World p_i1588_1_) {
		super(p_i1588_1_);
		yOffset = 0.0F;
		setSize(0.5F, 0.5F);
	}

	public EntityHanging(World p_i1589_1_, int p_i1589_2_, int p_i1589_3_, int p_i1589_4_, int p_i1589_5_) {
		this(p_i1589_1_);
		field_146063_b = p_i1589_2_;
		field_146064_c = p_i1589_3_;
		field_146062_d = p_i1589_4_;
	}

	@Override
	protected void entityInit() {
	}

	public void setDirection(int p_82328_1_) {
		hangingDirection = p_82328_1_;
		prevRotationYaw = rotationYaw = p_82328_1_ * 90;
		float f = getWidthPixels();
		float f1 = getHeightPixels();
		float f2 = getWidthPixels();

		if (p_82328_1_ != 2 && p_82328_1_ != 0) {
			f = 0.5F;
		} else {
			f2 = 0.5F;
			rotationYaw = prevRotationYaw = Direction.rotateOpposite[p_82328_1_] * 90;
		}

		f /= 32.0F;
		f1 /= 32.0F;
		f2 /= 32.0F;
		float f3 = field_146063_b + 0.5F;
		float f4 = field_146064_c + 0.5F;
		float f5 = field_146062_d + 0.5F;
		float f6 = 0.5625F;

		if (p_82328_1_ == 2) {
			f5 -= f6;
		}

		if (p_82328_1_ == 1) {
			f3 -= f6;
		}

		if (p_82328_1_ == 0) {
			f5 += f6;
		}

		if (p_82328_1_ == 3) {
			f3 += f6;
		}

		if (p_82328_1_ == 2) {
			f3 -= func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 1) {
			f5 += func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 0) {
			f3 += func_70517_b(getWidthPixels());
		}

		if (p_82328_1_ == 3) {
			f5 -= func_70517_b(getWidthPixels());
		}

		f4 += func_70517_b(getHeightPixels());
		setPosition(f3, f4, f5);
		float f7 = -0.03125F;
		boundingBox.setBounds(f3 - f - f7, f4 - f1 - f7, f5 - f2 - f7, f3 + f + f7, f4 + f1 + f7, f5 + f2 + f7);
	}

	private float func_70517_b(int p_70517_1_) {
		return p_70517_1_ == 32 ? 0.5F : p_70517_1_ == 64 ? 0.5F : 0.0F;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (tickCounter1++ == 100 && !worldObj.isRemote) {
			tickCounter1 = 0;

			if (!isDead && !onValidSurface()) {
				Material material = worldObj.getBlock((int) posX, (int) posY, (int) posZ).getMaterial();
				HangingBreakEvent.RemoveCause cause;
				if (!material.equals(Material.air)) {
					// TODO: This feels insufficient to catch 100% of suffocation cases
					cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
				} else {
					cause = HangingBreakEvent.RemoveCause.PHYSICS;
				}
				HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), cause);
				Bukkit.getPluginManager().callEvent(event);
				PaintingBreakEvent paintingEvent = null;
				if (this instanceof EntityPainting) {
					// Fire old painting event until it can be removed
					paintingEvent = new PaintingBreakEvent((Painting) this.getBukkitEntity(),
							PaintingBreakEvent.RemoveCause.valueOf(cause.name()));
					paintingEvent.setCancelled(event.isCancelled());
					Bukkit.getPluginManager().callEvent(paintingEvent);
				}
				if (isDead || event.isCancelled() || paintingEvent != null && paintingEvent.isCancelled())
					return;
				setDead();
				onBroken(null);
			}
		}
	}

	public boolean onValidSurface() {
		if (!worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty())
			return false;
		else {
			int i = Math.max(1, getWidthPixels() / 16);
			int j = Math.max(1, getHeightPixels() / 16);
			int k = field_146063_b;
			int l = field_146064_c;
			int i1 = field_146062_d;

			if (hangingDirection == 2) {
				k = MathHelper.floor_double(posX - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 1) {
				i1 = MathHelper.floor_double(posZ - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 0) {
				k = MathHelper.floor_double(posX - getWidthPixels() / 32.0F);
			}

			if (hangingDirection == 3) {
				i1 = MathHelper.floor_double(posZ - getWidthPixels() / 32.0F);
			}

			l = MathHelper.floor_double(posY - getHeightPixels() / 32.0F);

			for (int j1 = 0; j1 < i; ++j1) {
				for (int k1 = 0; k1 < j; ++k1) {
					Material material;

					if (hangingDirection != 2 && hangingDirection != 0) {
						material = worldObj.getBlock(field_146063_b, l + k1, i1 + j1).getMaterial();
					} else {
						material = worldObj.getBlock(k + j1, l + k1, field_146062_d).getMaterial();
					}

					if (!material.isSolid())
						return false;
				}
			}

			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox);
			Iterator iterator = list.iterator();
			Entity entity;

			do {
				if (!iterator.hasNext())
					return true;

				entity = (Entity) iterator.next();
			} while (!(entity instanceof EntityHanging));

			return false;
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean hitByEntity(Entity p_85031_1_) {
		return p_85031_1_ instanceof EntityPlayer && attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p_85031_1_), 0.0F);
	}

	@Override
	public void func_145781_i(int p_145781_1_) {
		worldObj.func_147450_X();
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			if (!isDead && !worldObj.isRemote && !UMEventFactory.fireHangingBreak(this, p_70097_1_)) {
				HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(),
						HangingBreakEvent.RemoveCause.DEFAULT);
				PaintingBreakEvent paintingEvent = null;
				if (p_70097_1_.getEntity() != null) {
					event = new org.bukkit.event.hanging.HangingBreakByEntityEvent((Hanging) this.getBukkitEntity(),
							p_70097_1_.getEntity() == null ? null : p_70097_1_.getEntity().getBukkitEntity());
					if (this instanceof EntityPainting) {
						// Fire old painting event until it can be removed
						paintingEvent = new org.bukkit.event.painting.PaintingBreakByEntityEvent(
								(Painting) this.getBukkitEntity(),
								p_70097_1_.getEntity() == null ? null : p_70097_1_.getEntity().getBukkitEntity());
					}
				} else if (p_70097_1_.isExplosion()) {
					event = new HangingBreakEvent((Hanging) this.getBukkitEntity(),
							HangingBreakEvent.RemoveCause.EXPLOSION);
				}
				Bukkit.getPluginManager().callEvent(event);
				if (paintingEvent != null) {
					paintingEvent.setCancelled(event.isCancelled());
					Bukkit.getPluginManager().callEvent(paintingEvent);
				}
				if (isDead || event.isCancelled() || paintingEvent != null && paintingEvent.isCancelled())
					return true;
				setDead();
				setBeenAttacked();
				onBroken(p_70097_1_.getEntity());
			}

			return true;
		}
	}

	@Override
	public void moveEntity(double p_70091_1_, double p_70091_3_, double p_70091_5_) {
		if (!worldObj.isRemote && !isDead
				&& p_70091_1_ * p_70091_1_ + p_70091_3_ * p_70091_3_ + p_70091_5_ * p_70091_5_ > 0.0D) {
			if (isDead)
				return;
			// TODO - Does this need its own cause? Seems to only be triggered by pistons
			HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(),
					HangingBreakEvent.RemoveCause.PHYSICS);
			Bukkit.getPluginManager().callEvent(event);
			if (isDead || event.isCancelled())
				return;
			onBroken(null);
		}
	}

	@Override
	public void addVelocity(double p_70024_1_, double p_70024_3_, double p_70024_5_) {
		if (!worldObj.isRemote && !isDead
				&& p_70024_1_ * p_70024_1_ + p_70024_3_ * p_70024_3_ + p_70024_5_ * p_70024_5_ > 0.0D) {
			setDead();
			onBroken(null);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setByte("Direction", (byte) hangingDirection);
		p_70014_1_.setInteger("TileX", field_146063_b);
		p_70014_1_.setInteger("TileY", field_146064_c);
		p_70014_1_.setInteger("TileZ", field_146062_d);

		switch (hangingDirection) {
		case 0:
			p_70014_1_.setByte("Dir", (byte) 2);
			break;
		case 1:
			p_70014_1_.setByte("Dir", (byte) 1);
			break;
		case 2:
			p_70014_1_.setByte("Dir", (byte) 0);
			break;
		case 3:
			p_70014_1_.setByte("Dir", (byte) 3);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.hasKey("Direction", 99)) {
			hangingDirection = p_70037_1_.getByte("Direction");
		} else {
			switch (p_70037_1_.getByte("Dir")) {
			case 0:
				hangingDirection = 2;
				break;
			case 1:
				hangingDirection = 1;
				break;
			case 2:
				hangingDirection = 0;
				break;
			case 3:
				hangingDirection = 3;
			}
		}

		field_146063_b = p_70037_1_.getInteger("TileX");
		field_146064_c = p_70037_1_.getInteger("TileY");
		field_146062_d = p_70037_1_.getInteger("TileZ");
		setDirection(hangingDirection);
	}

	public abstract int getWidthPixels();

	public abstract int getHeightPixels();

	public abstract void onBroken(Entity p_110128_1_);

	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}
}