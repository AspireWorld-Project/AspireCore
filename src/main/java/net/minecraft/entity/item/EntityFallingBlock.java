package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityFallingBlock extends Entity {
	private Block field_145811_e;
	public int field_145814_a;
	public int field_145812_b;
	public boolean field_145813_c;
	private boolean field_145808_f;
	private boolean field_145809_g;
	private int field_145815_h;
	private float field_145816_i;
	public NBTTagCompound field_145810_d;
	private static final String __OBFID = "CL_00001668";

	public EntityFallingBlock(World p_i1706_1_) {
		super(p_i1706_1_);
		field_145813_c = true;
		field_145815_h = 40;
		field_145816_i = 2.0F;
	}

	public EntityFallingBlock(World p_i45318_1_, double p_i45318_2_, double p_i45318_4_, double p_i45318_6_,
			Block p_i45318_8_) {
		this(p_i45318_1_, p_i45318_2_, p_i45318_4_, p_i45318_6_, p_i45318_8_, 0);
	}

	public EntityFallingBlock(World p_i45319_1_, double p_i45319_2_, double p_i45319_4_, double p_i45319_6_,
			Block p_i45319_8_, int p_i45319_9_) {
		super(p_i45319_1_);
		field_145813_c = true;
		field_145815_h = 40;
		field_145816_i = 2.0F;
		field_145811_e = p_i45319_8_;
		field_145814_a = p_i45319_9_;
		preventEntitySpawning = true;
		setSize(0.98F, 0.98F);
		yOffset = height / 2.0F;
		setPosition(p_i45319_2_, p_i45319_4_, p_i45319_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i45319_2_;
		prevPosY = p_i45319_4_;
		prevPosZ = p_i45319_6_;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	public void onUpdate() {
		if (field_145811_e.getMaterial() == Material.air) {
			setDead();
		} else {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			++field_145812_b;
			motionY -= 0.03999999910593033D;
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.9800000190734863D;
			motionY *= 0.9800000190734863D;
			motionZ *= 0.9800000190734863D;
			if (!worldObj.isRemote) {
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(posY);
				int k = MathHelper.floor_double(posZ);
				if (field_145812_b == 1) {
					if (worldObj.getBlock(i, j, k) != field_145811_e
							|| worldObj.getBlockMetadata(i, j, k) != field_145814_a || CraftEventFactory
									.callEntityChangeBlockEvent(this, i, j, k, Blocks.air, 0).isCancelled()) {
						setDead();
						return;
					}
					worldObj.setBlockToAir(i, j, k);
				}
				if (onGround) {
					motionX *= 0.699999988079071D;
					motionZ *= 0.699999988079071D;
					motionY *= -0.5D;
					if (worldObj.getBlock(i, j, k) != Blocks.piston_extension) {
						setDead();
						if (!field_145808_f
								&& worldObj.canPlaceEntityOnSide(field_145811_e, i, j, k, true, 1, null, null)
								&& !BlockFalling.func_149831_e(worldObj, i, j - 1, k)) {
							if (CraftEventFactory
									.callEntityChangeBlockEvent(this, i, j, k, field_145811_e, field_145814_a)
									.isCancelled())
								return;
							worldObj.setBlock(i, j, k, field_145811_e, field_145814_a, 3);
							if (field_145811_e instanceof BlockFalling) {
								((BlockFalling) field_145811_e).func_149828_a(worldObj, i, j, k, field_145814_a);
							}
							if (field_145810_d != null && field_145811_e instanceof ITileEntityProvider) {
								TileEntity tileentity = worldObj.getTileEntity(i, j, k);
								if (tileentity != null) {
									NBTTagCompound nbttagcompound = new NBTTagCompound();
									tileentity.writeToNBT(nbttagcompound);
									for (Object o : field_145810_d.func_150296_c()) {
										String s = (String) o;
										NBTBase nbtbase = field_145810_d.getTag(s);
										if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
											nbttagcompound.setTag(s, nbtbase.copy());
										}
									}
									tileentity.readFromNBT(nbttagcompound);
									tileentity.markDirty();
								}
							}
						} else if (field_145813_c && !field_145808_f) {
							entityDropItem(
									new ItemStack(field_145811_e, 1, field_145811_e.damageDropped(field_145814_a)),
									0.0F);
						}
					}
				} else if (field_145812_b > 100 && !worldObj.isRemote && (j < 1 || j > 256) || field_145812_b > 600) {
					if (field_145813_c) {
						entityDropItem(new ItemStack(field_145811_e, 1, field_145811_e.damageDropped(field_145814_a)),
								0.0F);
					}
					setDead();
				}
			}
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
		if (field_145809_g) {
			int i = MathHelper.ceiling_float_int(p_70069_1_ - 1.0F);

			if (i > 0) {
				ArrayList arraylist = new ArrayList(worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox));
				boolean flag = field_145811_e == Blocks.anvil;
				DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
				Iterator iterator = arraylist.iterator();

				while (iterator.hasNext()) {
					Entity entity = (Entity) iterator.next();
					entity.attackEntityFrom(damagesource,
							Math.min(MathHelper.floor_float(i * field_145816_i), field_145815_h));
				}

				if (flag && rand.nextFloat() < 0.05000000074505806D + i * 0.05D) {
					int j = field_145814_a >> 2;
					int k = field_145814_a & 3;
					++j;

					if (j > 2) {
						field_145808_f = true;
					} else {
						field_145814_a = k | j << 2;
					}
				}
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setByte("Tile", (byte) Block.getIdFromBlock(field_145811_e));
		p_70014_1_.setInteger("TileID", Block.getIdFromBlock(field_145811_e));
		p_70014_1_.setByte("Data", (byte) field_145814_a);
		p_70014_1_.setByte("Time", (byte) field_145812_b);
		p_70014_1_.setBoolean("DropItem", field_145813_c);
		p_70014_1_.setBoolean("HurtEntities", field_145809_g);
		p_70014_1_.setFloat("FallHurtAmount", field_145816_i);
		p_70014_1_.setInteger("FallHurtMax", field_145815_h);

		if (field_145810_d != null) {
			p_70014_1_.setTag("TileEntityData", field_145810_d);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.hasKey("TileID", 99)) {
			field_145811_e = Block.getBlockById(p_70037_1_.getInteger("TileID"));
		} else {
			field_145811_e = Block.getBlockById(p_70037_1_.getByte("Tile") & 255);
		}

		field_145814_a = p_70037_1_.getByte("Data") & 255;
		field_145812_b = p_70037_1_.getByte("Time") & 255;

		if (p_70037_1_.hasKey("HurtEntities", 99)) {
			field_145809_g = p_70037_1_.getBoolean("HurtEntities");
			field_145816_i = p_70037_1_.getFloat("FallHurtAmount");
			field_145815_h = p_70037_1_.getInteger("FallHurtMax");
		} else if (field_145811_e == Blocks.anvil) {
			field_145809_g = true;
		}

		if (p_70037_1_.hasKey("DropItem", 99)) {
			field_145813_c = p_70037_1_.getBoolean("DropItem");
		}

		if (p_70037_1_.hasKey("TileEntityData", 10)) {
			field_145810_d = p_70037_1_.getCompoundTag("TileEntityData");
		}

		if (field_145811_e.getMaterial() == Material.air) {
			field_145811_e = Blocks.sand;
		}
	}

	public void func_145806_a(boolean p_145806_1_) {
		field_145809_g = p_145806_1_;
	}

	@Override
	public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
		super.addEntityCrashInfo(p_85029_1_);
		p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(field_145811_e)));
		p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(field_145814_a));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0F;
	}

	@SideOnly(Side.CLIENT)
	public World func_145807_e() {
		return worldObj;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	public Block func_145805_f() {
		return field_145811_e;
	}
}