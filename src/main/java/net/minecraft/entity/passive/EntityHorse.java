package net.minecraft.entity.passive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.HorseJumpEvent;

import java.util.Iterator;
import java.util.List;

public class EntityHorse extends EntityAnimal implements IInvBasic {
	private static final IEntitySelector horseBreedingSelector = new IEntitySelector() {
		private static final String __OBFID = "CL_00001642";

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_ instanceof EntityHorse && ((EntityHorse) p_82704_1_).func_110205_ce();
		}
	};
	public static final IAttribute horseJumpStrength = new RangedAttribute("horse.jumpStrength", 0.7D, 0.0D, 2.0D)
			.setDescription("Jump Strength").setShouldWatch(true);
	private static final String[] horseArmorTextures = new String[] { null,
			"textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png",
			"textures/entity/horse/armor/horse_armor_diamond.png" };
	private static final String[] field_110273_bx = new String[] { "", "meo", "goo", "dio" };
	private static final int[] armorValues = new int[] { 0, 5, 7, 11 };
	private static final String[] horseTextures = new String[] { "textures/entity/horse/horse_white.png",
			"textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png",
			"textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png",
			"textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };
	private static final String[] field_110269_bA = new String[] { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
	private static final String[] horseMarkingTextures = new String[] { null,
			"textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png",
			"textures/entity/horse/horse_markings_whitedots.png",
			"textures/entity/horse/horse_markings_blackdots.png" };
	private static final String[] field_110292_bC = new String[] { "", "wo_", "wmo", "wdo", "bdo" };
	private int eatingHaystackCounter;
	private int openMouthCounter;
	private int jumpRearingCounter;
	public int field_110278_bp;
	public int field_110279_bq;
	protected boolean horseJumping;
	public AnimalChest horseChest;
	private boolean hasReproduced;
	protected int temper;
	protected float jumpPower;
	private boolean field_110294_bI;
	private float headLean;
	private float prevHeadLean;
	private float rearingAmount;
	private float prevRearingAmount;
	private float mouthOpenness;
	private float prevMouthOpenness;
	private int field_110285_bP;
	private String field_110286_bQ;
	private String[] field_110280_bR = new String[3];
	public int maxDomestication = 100;
	private static final String __OBFID = "CL_00001641";

	public EntityHorse(World p_i1685_1_) {
		super(p_i1685_1_);
		setSize(1.4F, 1.6F);
		isImmuneToFire = false;
		setChested(false);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.2D));
		tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
		tasks.addTask(6, new EntityAIWander(this, 0.7D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		func_110226_cD();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(19, Byte.valueOf((byte) 0));
		dataWatcher.addObject(20, Integer.valueOf(0));
		dataWatcher.addObject(21, String.valueOf(""));
		dataWatcher.addObject(22, Integer.valueOf(0));
	}

	public void setHorseType(int p_110214_1_) {
		dataWatcher.updateObject(19, Byte.valueOf((byte) p_110214_1_));
		func_110230_cF();
	}

	public int getHorseType() {
		return dataWatcher.getWatchableObjectByte(19);
	}

	public void setHorseVariant(int p_110235_1_) {
		dataWatcher.updateObject(20, Integer.valueOf(p_110235_1_));
		func_110230_cF();
	}

	public int getHorseVariant() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	@Override
	public String getCommandSenderName() {
		if (hasCustomNameTag())
			return getCustomNameTag();
		else {
			int i = getHorseType();

			switch (i) {
			case 0:
			default:
				return StatCollector.translateToLocal("entity.horse.name");
			case 1:
				return StatCollector.translateToLocal("entity.donkey.name");
			case 2:
				return StatCollector.translateToLocal("entity.mule.name");
			case 3:
				return StatCollector.translateToLocal("entity.zombiehorse.name");
			case 4:
				return StatCollector.translateToLocal("entity.skeletonhorse.name");
			}
		}
	}

	private boolean getHorseWatchableBoolean(int p_110233_1_) {
		return (dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
	}

	private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
		int j = dataWatcher.getWatchableObjectInt(16);

		if (p_110208_2_) {
			dataWatcher.updateObject(16, Integer.valueOf(j | p_110208_1_));
		} else {
			dataWatcher.updateObject(16, Integer.valueOf(j & ~p_110208_1_));
		}
	}

	public boolean isAdultHorse() {
		return !isChild();
	}

	public boolean isTame() {
		return getHorseWatchableBoolean(2);
	}

	public boolean func_110253_bW() {
		return isAdultHorse();
	}

	public String func_152119_ch() {
		return dataWatcher.getWatchableObjectString(21);
	}

	public void func_152120_b(String p_152120_1_) {
		dataWatcher.updateObject(21, p_152120_1_);
	}

	public float getHorseSize() {
		int i = getGrowingAge();
		return i >= 0 ? 1.0F : 0.5F + (-24000 - i) / -24000.0F * 0.5F;
	}

	@Override
	public void setScaleForAge(boolean p_98054_1_) {
		if (p_98054_1_) {
			setScale(getHorseSize());
		} else {
			setScale(1.0F);
		}
	}

	public boolean isHorseJumping() {
		return horseJumping;
	}

	public void setHorseTamed(boolean p_110234_1_) {
		setHorseWatchableBoolean(2, p_110234_1_);
	}

	public void setHorseJumping(boolean p_110255_1_) {
		horseJumping = p_110255_1_;
	}

	@Override
	public boolean allowLeashing() {
		return !func_110256_cu() && super.allowLeashing();
	}

	@Override
	protected void func_142017_o(float p_142017_1_) {
		if (p_142017_1_ > 6.0F && isEatingHaystack()) {
			setEatingHaystack(false);
		}
	}

	public boolean isChested() {
		return getHorseWatchableBoolean(8);
	}

	public int func_110241_cb() {
		return dataWatcher.getWatchableObjectInt(22);
	}

	private int getHorseArmorIndex(ItemStack p_110260_1_) {
		if (p_110260_1_ == null)
			return 0;
		else {
			Item item = p_110260_1_.getItem();
			return item == Items.iron_horse_armor ? 1
					: item == Items.golden_horse_armor ? 2 : item == Items.diamond_horse_armor ? 3 : 0;
		}
	}

	public boolean isEatingHaystack() {
		return getHorseWatchableBoolean(32);
	}

	public boolean isRearing() {
		return getHorseWatchableBoolean(64);
	}

	public boolean func_110205_ce() {
		return getHorseWatchableBoolean(16);
	}

	public boolean getHasReproduced() {
		return hasReproduced;
	}

	public void func_146086_d(ItemStack p_146086_1_) {
		dataWatcher.updateObject(22, Integer.valueOf(getHorseArmorIndex(p_146086_1_)));
		func_110230_cF();
	}

	public void func_110242_l(boolean p_110242_1_) {
		setHorseWatchableBoolean(16, p_110242_1_);
	}

	public void setChested(boolean p_110207_1_) {
		setHorseWatchableBoolean(8, p_110207_1_);
	}

	public void setHasReproduced(boolean p_110221_1_) {
		hasReproduced = p_110221_1_;
	}

	public void setHorseSaddled(boolean p_110251_1_) {
		setHorseWatchableBoolean(4, p_110251_1_);
	}

	public int getTemper() {
		return temper;
	}

	public void setTemper(int p_110238_1_) {
		temper = p_110238_1_;
	}

	public int increaseTemper(int p_110198_1_) {
		int j = MathHelper.clamp_int(getTemper() + p_110198_1_, 0, getMaxTemper());
		setTemper(j);
		return j;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		Entity entity = p_70097_1_.getEntity();
		return riddenByEntity != null && riddenByEntity.equals(entity) ? false
				: super.attackEntityFrom(p_70097_1_, p_70097_2_);
	}

	@Override
	public int getTotalArmorValue() {
		return armorValues[func_110241_cb()];
	}

	@Override
	public boolean canBePushed() {
		return riddenByEntity == null;
	}

	public boolean prepareChunkForSpawn() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posZ);
		worldObj.getBiomeGenForCoords(i, j);
		return true;
	}

	public void dropChests() {
		if (!worldObj.isRemote && isChested()) {
			dropItem(Item.getItemFromBlock(Blocks.chest), 1);
			setChested(false);
		}
	}

	private void func_110266_cB() {
		openHorseMouth();
		worldObj.playSoundAtEntity(this, "eating", 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
	}

	@Override
	protected void fall(float p_70069_1_) {
		if (p_70069_1_ > 1.0F) {
			playSound("mob.horse.land", 0.4F, 1.0F);
		}

		int i = MathHelper.ceiling_float_int(p_70069_1_ * 0.5F - 3.0F);

		if (i > 0) {
			attackEntityFrom(DamageSource.fall, i);

			if (riddenByEntity != null) {
				riddenByEntity.attackEntityFrom(DamageSource.fall, i);
			}

			Block block = worldObj.getBlock(MathHelper.floor_double(posX),
					MathHelper.floor_double(posY - 0.2D - prevRotationYaw), MathHelper.floor_double(posZ));

			if (block.getMaterial() != Material.air) {
				Block.SoundType soundtype = block.stepSound;
				worldObj.playSoundAtEntity(this, soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F,
						soundtype.getPitch() * 0.75F);
			}
		}
	}

	private int func_110225_cC() {
		int i = getHorseType();
		//return isChested() && (i == 1 || i == 2) ? 17 : 2;
		return this.isChested() ? 17 : 2;
	}

	public void func_110226_cD() {
		AnimalChest animalchest = horseChest;
		horseChest = new AnimalChest("HorseChest", func_110225_cC());
		horseChest.setAnimal(this);
		horseChest.func_110133_a(getCommandSenderName());

		if (animalchest != null) {
			animalchest.func_110132_b(this);
			int i = Math.min(animalchest.getSizeInventory(), horseChest.getSizeInventory());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = animalchest.getStackInSlot(j);

				if (itemstack != null) {
					horseChest.setInventorySlotContents(j, itemstack.copy());
				}
			}

			animalchest = null;
		}

		horseChest.func_110134_a(this);
		func_110232_cE();
	}

	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		// Moved from die method above
		if (!this.worldObj.isRemote) {
			this.dropChestItems();
		}
	}

	private void func_110232_cE() {
		if (!worldObj.isRemote) {
			setHorseSaddled(horseChest.getStackInSlot(0) != null);

			if (func_110259_cr()) {
				func_146086_d(horseChest.getStackInSlot(1));
			}
		}
	}

	@Override
	public void onInventoryChanged(InventoryBasic p_76316_1_) {
		int i = func_110241_cb();
		boolean flag = isHorseSaddled();
		func_110232_cE();

		if (ticksExisted > 20) {
			if (i == 0 && i != func_110241_cb()) {
				playSound("mob.horse.armor", 0.5F, 1.0F);
			} else if (i != func_110241_cb()) {
				playSound("mob.horse.armor", 0.5F, 1.0F);
			}

			if (!flag && isHorseSaddled()) {
				playSound("mob.horse.leather", 0.5F, 1.0F);
			}
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		prepareChunkForSpawn();
		return super.getCanSpawnHere();
	}

	protected EntityHorse getClosestHorse(Entity p_110250_1_, double p_110250_2_) {
		double d1 = Double.MAX_VALUE;
		Entity entity1 = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(p_110250_1_,
				p_110250_1_.boundingBox.addCoord(p_110250_2_, p_110250_2_, p_110250_2_), horseBreedingSelector);
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			Entity entity2 = (Entity) iterator.next();
			double d2 = entity2.getDistanceSq(p_110250_1_.posX, p_110250_1_.posY, p_110250_1_.posZ);

			if (d2 < d1) {
				entity1 = entity2;
				d1 = d2;
			}
		}

		return (EntityHorse) entity1;
	}

	public double getHorseJumpStrength() {
		return getEntityAttribute(horseJumpStrength).getAttributeValue();
	}

	@Override
	protected String getDeathSound() {
		openHorseMouth();
		int i = getHorseType();
		return i == 3 ? "mob.horse.zombie.death"
				: i == 4 ? "mob.horse.skeleton.death" : i != 1 && i != 2 ? "mob.horse.death" : "mob.horse.donkey.death";
	}

	@Override
	protected Item getDropItem() {
		boolean flag = rand.nextInt(4) == 0;
		int i = getHorseType();
		return i == 4 ? Items.bone : i == 3 ? flag ? Item.getItemById(0) : Items.rotten_flesh : Items.leather;
	}

	@Override
	protected String getHurtSound() {
		openHorseMouth();

		if (rand.nextInt(3) == 0) {
			makeHorseRear();
		}

		int i = getHorseType();
		return i == 3 ? "mob.horse.zombie.hit"
				: i == 4 ? "mob.horse.skeleton.hit" : i != 1 && i != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit";
	}

	public boolean isHorseSaddled() {
		return getHorseWatchableBoolean(4);
	}

	@Override
	protected String getLivingSound() {
		openHorseMouth();

		if (rand.nextInt(10) == 0 && !isMovementBlocked()) {
			makeHorseRear();
		}

		int i = getHorseType();
		return i == 3 ? "mob.horse.zombie.idle"
				: i == 4 ? "mob.horse.skeleton.idle" : i != 1 && i != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle";
	}

	protected String getAngrySoundName() {
		openHorseMouth();
		makeHorseRear();
		int i = getHorseType();
		return i != 3 && i != 4 ? i != 1 && i != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry" : null;
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
		Block.SoundType soundtype = p_145780_4_.stepSound;

		if (worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer) {
			soundtype = Blocks.snow_layer.stepSound;
		}

		if (!p_145780_4_.getMaterial().isLiquid()) {
			int l = getHorseType();

			if (riddenByEntity != null && l != 1 && l != 2) {
				++field_110285_bP;

				if (field_110285_bP > 5 && field_110285_bP % 3 == 0) {
					playSound("mob.horse.gallop", soundtype.getVolume() * 0.15F, soundtype.getPitch());

					if (l == 0 && rand.nextInt(10) == 0) {
						playSound("mob.horse.breathe", soundtype.getVolume() * 0.6F, soundtype.getPitch());
					}
				} else if (field_110285_bP <= 5) {
					playSound("mob.horse.wood", soundtype.getVolume() * 0.15F, soundtype.getPitch());
				}
			} else if (soundtype == Block.soundTypeWood) {
				playSound("mob.horse.wood", soundtype.getVolume() * 0.15F, soundtype.getPitch());
			} else {
				playSound("mob.horse.soft", soundtype.getVolume() * 0.15F, soundtype.getPitch());
			}
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(horseJumpStrength);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 6;
	}

	public int getMaxTemper() {
		return maxDomestication;
	}

	@Override
	protected float getSoundVolume() {
		return 0.8F;
	}

	@Override
	public int getTalkInterval() {
		return 400;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_110239_cn() {
		return getHorseType() == 0 || func_110241_cb() > 0;
	}

	private void func_110230_cF() {
		field_110286_bQ = null;
	}

	@SideOnly(Side.CLIENT)
	private void setHorseTexturePaths() {
		field_110286_bQ = "horse/";
		field_110280_bR[0] = null;
		field_110280_bR[1] = null;
		field_110280_bR[2] = null;
		int i = getHorseType();
		int j = getHorseVariant();
		int k;

		if (i == 0) {
			k = j & 255;
			int l = (j & 65280) >> 8;
			field_110280_bR[0] = horseTextures[k];
			field_110286_bQ = field_110286_bQ + field_110269_bA[k];
			field_110280_bR[1] = horseMarkingTextures[l];
			field_110286_bQ = field_110286_bQ + field_110292_bC[l];
		} else {
			field_110280_bR[0] = "";
			field_110286_bQ = field_110286_bQ + "_" + i + "_";
		}

		k = func_110241_cb();
		field_110280_bR[2] = horseArmorTextures[k];
		field_110286_bQ = field_110286_bQ + field_110273_bx[k];
	}

	@SideOnly(Side.CLIENT)
	public String getHorseTexture() {
		if (field_110286_bQ == null) {
			setHorseTexturePaths();
		}

		return field_110286_bQ;
	}

	@SideOnly(Side.CLIENT)
	public String[] getVariantTexturePaths() {
		if (field_110286_bQ == null) {
			setHorseTexturePaths();
		}

		return field_110280_bR;
	}

	public void openGUI(EntityPlayer p_110199_1_) {
		if (!worldObj.isRemote && (riddenByEntity == null || riddenByEntity == p_110199_1_) && isTame()) {
			horseChest.func_110133_a(getCommandSenderName());
			p_110199_1_.displayGUIHorse(this, horseChest);
		}
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();

		if (itemstack != null && itemstack.getItem() == Items.spawn_egg)
			return super.interact(p_70085_1_);
		else if (!isTame() && func_110256_cu())
			return false;
		else if (isTame() && isAdultHorse() && p_70085_1_.isSneaking()) {
			openGUI(p_70085_1_);
			return true;
		} else if (func_110253_bW() && riddenByEntity != null)
			return super.interact(p_70085_1_);
		else {
			if (itemstack != null) {
				boolean flag = false;

				if (func_110259_cr()) {
					byte b0 = -1;

					if (itemstack.getItem() == Items.iron_horse_armor) {
						b0 = 1;
					} else if (itemstack.getItem() == Items.golden_horse_armor) {
						b0 = 2;
					} else if (itemstack.getItem() == Items.diamond_horse_armor) {
						b0 = 3;
					}

					if (b0 >= 0) {
						if (!isTame()) {
							makeHorseRearWithSound();
							return true;
						}

						openGUI(p_70085_1_);
						return true;
					}
				}

				if (!flag && !func_110256_cu()) {
					float f = 0.0F;
					short short1 = 0;
					byte b1 = 0;

					if (itemstack.getItem() == Items.wheat) {
						f = 2.0F;
						short1 = 60;
						b1 = 3;
					} else if (itemstack.getItem() == Items.sugar) {
						f = 1.0F;
						short1 = 30;
						b1 = 3;
					} else if (itemstack.getItem() == Items.bread) {
						f = 7.0F;
						short1 = 180;
						b1 = 3;
					} else if (Block.getBlockFromItem(itemstack.getItem()) == Blocks.hay_block) {
						f = 20.0F;
						short1 = 180;
					} else if (itemstack.getItem() == Items.apple) {
						f = 3.0F;
						short1 = 60;
						b1 = 3;
					} else if (itemstack.getItem() == Items.golden_carrot) {
						f = 4.0F;
						short1 = 60;
						b1 = 5;

						if (isTame() && getGrowingAge() == 0) {
							flag = true;
							func_146082_f(p_70085_1_);
						}
					} else if (itemstack.getItem() == Items.golden_apple) {
						f = 10.0F;
						short1 = 240;
						b1 = 10;

						if (isTame() && getGrowingAge() == 0) {
							flag = true;
							func_146082_f(p_70085_1_);
						}
					}

					if (getHealth() < getMaxHealth() && f > 0.0F) {
						this.heal(f);
						flag = true;
					}

					if (!isAdultHorse() && short1 > 0) {
						addGrowth(short1);
						flag = true;
					}

					if (b1 > 0 && (flag || !isTame()) && b1 < getMaxTemper()) {
						flag = true;
						increaseTemper(b1);
					}

					if (flag) {
						func_110266_cB();
					}
				}

				if (!isTame() && !flag) {
					if (itemstack != null && itemstack.interactWithEntity(p_70085_1_, this))
						return true;

					makeHorseRearWithSound();
					return true;
				}

				if (!flag && func_110229_cs() && !isChested()
						&& itemstack.getItem() == Item.getItemFromBlock(Blocks.chest)) {
					setChested(true);
					playSound("mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
					flag = true;
					func_110226_cD();
				}

				if (!flag && func_110253_bW() && !isHorseSaddled() && itemstack.getItem() == Items.saddle) {
					openGUI(p_70085_1_);
					return true;
				}

				if (flag) {
					if (!p_70085_1_.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
						p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem,
								(ItemStack) null);
					}

					return true;
				}
			}

			if (func_110253_bW() && riddenByEntity == null) {
				if (itemstack != null && itemstack.interactWithEntity(p_70085_1_, this))
					return true;
				else {
					func_110237_h(p_70085_1_);
					return true;
				}
			} else
				return super.interact(p_70085_1_);
		}
	}

	private void func_110237_h(EntityPlayer p_110237_1_) {
		p_110237_1_.rotationYaw = rotationYaw;
		p_110237_1_.rotationPitch = rotationPitch;
		setEatingHaystack(false);
		setRearing(false);

		if (!worldObj.isRemote) {
			p_110237_1_.mountEntity(this);
		}
	}

	public boolean func_110259_cr() {
		return getHorseType() == 0;
	}

	public boolean func_110229_cs() {
		int i = getHorseType();
		return i == 2 || i == 1;
	}

	@Override
	protected boolean isMovementBlocked() {
		return riddenByEntity != null && isHorseSaddled() ? true : isEatingHaystack() || isRearing();
	}

	public boolean func_110256_cu() {
		int i = getHorseType();
		return i == 3 || i == 4;
	}

	public boolean func_110222_cv() {
		return func_110256_cu() || getHorseType() == 2;
	}

	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return false;
	}

	private void func_110210_cH() {
		field_110278_bp = 1;
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (!worldObj.isRemote) {
			dropChestItems();
		}
	}

	@Override
	public void onLivingUpdate() {
		if (rand.nextInt(200) == 0) {
			func_110210_cH();
		}

		super.onLivingUpdate();

		if (!worldObj.isRemote) {
			if (rand.nextInt(900) == 0 && deathTime == 0) {
				this.heal(1.0F);
			}

			if (!isEatingHaystack() && riddenByEntity == null && rand.nextInt(300) == 0
					&& worldObj.getBlock(MathHelper.floor_double(posX), MathHelper.floor_double(posY) - 1,
							MathHelper.floor_double(posZ)) == Blocks.grass) {
				setEatingHaystack(true);
			}

			if (isEatingHaystack() && ++eatingHaystackCounter > 50) {
				eatingHaystackCounter = 0;
				setEatingHaystack(false);
			}

			if (func_110205_ce() && !isAdultHorse() && !isEatingHaystack()) {
				EntityHorse entityhorse = getClosestHorse(this, 16.0D);

				if (entityhorse != null && getDistanceSqToEntity(entityhorse) > 4.0D) {
					PathEntity pathentity = worldObj.getPathEntityToEntity(this, entityhorse, 16.0F, true, false, false,
							true);
					setPathToEntity(pathentity);
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (worldObj.isRemote && dataWatcher.hasChanges()) {
			dataWatcher.func_111144_e();
			func_110230_cF();
		}

		if (openMouthCounter > 0 && ++openMouthCounter > 30) {
			openMouthCounter = 0;
			setHorseWatchableBoolean(128, false);
		}

		if (!worldObj.isRemote && jumpRearingCounter > 0 && ++jumpRearingCounter > 20) {
			jumpRearingCounter = 0;
			setRearing(false);
		}

		if (field_110278_bp > 0 && ++field_110278_bp > 8) {
			field_110278_bp = 0;
		}

		if (field_110279_bq > 0) {
			++field_110279_bq;

			if (field_110279_bq > 300) {
				field_110279_bq = 0;
			}
		}

		prevHeadLean = headLean;

		if (isEatingHaystack()) {
			headLean += (1.0F - headLean) * 0.4F + 0.05F;

			if (headLean > 1.0F) {
				headLean = 1.0F;
			}
		} else {
			headLean += (0.0F - headLean) * 0.4F - 0.05F;

			if (headLean < 0.0F) {
				headLean = 0.0F;
			}
		}

		prevRearingAmount = rearingAmount;

		if (isRearing()) {
			prevHeadLean = headLean = 0.0F;
			rearingAmount += (1.0F - rearingAmount) * 0.4F + 0.05F;

			if (rearingAmount > 1.0F) {
				rearingAmount = 1.0F;
			}
		} else {
			field_110294_bI = false;
			rearingAmount += (0.8F * rearingAmount * rearingAmount * rearingAmount - rearingAmount) * 0.6F - 0.05F;

			if (rearingAmount < 0.0F) {
				rearingAmount = 0.0F;
			}
		}

		prevMouthOpenness = mouthOpenness;

		if (getHorseWatchableBoolean(128)) {
			mouthOpenness += (1.0F - mouthOpenness) * 0.7F + 0.05F;

			if (mouthOpenness > 1.0F) {
				mouthOpenness = 1.0F;
			}
		} else {
			mouthOpenness += (0.0F - mouthOpenness) * 0.7F - 0.05F;

			if (mouthOpenness < 0.0F) {
				mouthOpenness = 0.0F;
			}
		}
	}

	private void openHorseMouth() {
		if (!worldObj.isRemote) {
			openMouthCounter = 1;
			setHorseWatchableBoolean(128, true);
		}
	}

	private boolean func_110200_cJ() {
		return riddenByEntity == null && ridingEntity == null && isTame() && isAdultHorse() && !func_110222_cv()
				&& getHealth() >= getMaxHealth();
	}

	@Override
	public void setEating(boolean p_70019_1_) {
		setHorseWatchableBoolean(32, p_70019_1_);
	}

	public void setEatingHaystack(boolean p_110227_1_) {
		setEating(p_110227_1_);
	}

	public void setRearing(boolean p_110219_1_) {
		if (p_110219_1_) {
			setEatingHaystack(false);
		}

		setHorseWatchableBoolean(64, p_110219_1_);
	}

	private void makeHorseRear() {
		if (!worldObj.isRemote) {
			jumpRearingCounter = 1;
			setRearing(true);
		}
	}

	public void makeHorseRearWithSound() {
		makeHorseRear();
		String s = getAngrySoundName();

		if (s != null) {
			playSound(s, getSoundVolume(), getSoundPitch());
		}
	}

	public void dropChestItems() {
		dropItemsInChest(this, horseChest);
		dropChests();
	}

	private void dropItemsInChest(Entity p_110240_1_, AnimalChest p_110240_2_) {
		if (p_110240_2_ != null && !worldObj.isRemote) {
			for (int i = 0; i < p_110240_2_.getSizeInventory(); ++i) {
				ItemStack itemstack = p_110240_2_.getStackInSlot(i);

				if (itemstack != null) {
					entityDropItem(itemstack, 0.0F);
				}
			}
		}
	}

	public boolean setTamedBy(EntityPlayer p_110263_1_) {
		func_152120_b(p_110263_1_.getUniqueID().toString());
		setHorseTamed(true);
		return true;
	}

	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase && isHorseSaddled()) {
			prevRotationYaw = rotationYaw = riddenByEntity.rotationYaw;
			rotationPitch = riddenByEntity.rotationPitch * 0.5F;
			setRotation(rotationYaw, rotationPitch);
			rotationYawHead = renderYawOffset = rotationYaw;
			p_70612_1_ = ((EntityLivingBase) riddenByEntity).moveStrafing * 0.5F;
			p_70612_2_ = ((EntityLivingBase) riddenByEntity).moveForward;

			if (p_70612_2_ <= 0.0F) {
				p_70612_2_ *= 0.25F;
				field_110285_bP = 0;
			}

			if (onGround && jumpPower == 0.0F && isRearing() && !field_110294_bI) {
				p_70612_1_ = 0.0F;
				p_70612_2_ = 0.0F;
			}

			if (jumpPower > 0.0F && !isHorseJumping() && onGround) {
				motionY = getHorseJumpStrength() * jumpPower;

				if (this.isPotionActive(Potion.jump)) {
					motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
				}

				setHorseJumping(true);
				isAirBorne = true;

				if (p_70612_2_ > 0.0F) {
					float f2 = MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F);
					float f3 = MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F);
					motionX += -0.4F * f2 * jumpPower;
					motionZ += 0.4F * f3 * jumpPower;
					playSound("mob.horse.jump", 0.4F, 1.0F);
				}

				jumpPower = 0.0F;
				net.minecraftforge.common.ForgeHooks.onLivingJump(this);
			}

			stepHeight = 1.0F;
			jumpMovementFactor = getAIMoveSpeed() * 0.1F;

			if (!worldObj.isRemote) {
				setAIMoveSpeed((float) getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
				super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			}

			if (onGround) {
				jumpPower = 0.0F;
				setHorseJumping(false);
			}

			prevLimbSwingAmount = limbSwingAmount;
			double d1 = posX - prevPosX;
			double d0 = posZ - prevPosZ;
			float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

			if (f4 > 1.0F) {
				f4 = 1.0F;
			}

			limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
			limbSwing += limbSwingAmount;
		} else {
			stepHeight = 0.5F;
			jumpMovementFactor = 0.02F;
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Bukkit.MaxDomestication", this.maxDomestication);
		p_70014_1_.setBoolean("EatingHaystack", isEatingHaystack());
		p_70014_1_.setBoolean("ChestedHorse", isChested());
		p_70014_1_.setBoolean("HasReproduced", getHasReproduced());
		p_70014_1_.setBoolean("Bred", func_110205_ce());
		p_70014_1_.setInteger("Type", getHorseType());
		p_70014_1_.setInteger("Variant", getHorseVariant());
		p_70014_1_.setInteger("Temper", getTemper());
		p_70014_1_.setBoolean("Tame", isTame());
		p_70014_1_.setString("OwnerUUID", func_152119_ch());

		if (isChested()) {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 2; i < horseChest.getSizeInventory(); ++i) {
				ItemStack itemstack = horseChest.getStackInSlot(i);

				if (itemstack != null) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte) i);
					itemstack.writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			p_70014_1_.setTag("Items", nbttaglist);
		}

		if (horseChest.getStackInSlot(1) != null) {
			p_70014_1_.setTag("ArmorItem", horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
		}

		if (horseChest.getStackInSlot(0) != null) {
			p_70014_1_.setTag("SaddleItem", horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.hasKey("Bukkit.MaxDomestication")) {
			maxDomestication = p_70037_1_.getInteger("Bukkit.MaxDomestication");
		}
		super.readEntityFromNBT(p_70037_1_);
		setEatingHaystack(p_70037_1_.getBoolean("EatingHaystack"));
		func_110242_l(p_70037_1_.getBoolean("Bred"));
		setChested(p_70037_1_.getBoolean("ChestedHorse"));
		setHasReproduced(p_70037_1_.getBoolean("HasReproduced"));
		setHorseType(p_70037_1_.getInteger("Type"));
		setHorseVariant(p_70037_1_.getInteger("Variant"));
		setTemper(p_70037_1_.getInteger("Temper"));
		setHorseTamed(p_70037_1_.getBoolean("Tame"));

		if (p_70037_1_.hasKey("OwnerUUID", 8)) {
			func_152120_b(p_70037_1_.getString("OwnerUUID"));
		}

		IAttributeInstance iattributeinstance = getAttributeMap().getAttributeInstanceByName("Speed");

		if (iattributeinstance != null) {
			getEntityAttribute(SharedMonsterAttributes.movementSpeed)
					.setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
		}

		if (isChested()) {
			NBTTagList nbttaglist = p_70037_1_.getTagList("Items", 10);
			func_110226_cD();

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound1.getByte("Slot") & 255;

				if (j >= 2 && j < horseChest.getSizeInventory()) {
					horseChest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
				}
			}
		}

		ItemStack itemstack;

		if (p_70037_1_.hasKey("ArmorItem", 10)) {
			itemstack = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("ArmorItem"));

			if (itemstack != null && func_146085_a(itemstack.getItem())) {
				horseChest.setInventorySlotContents(1, itemstack);
			}
		}

		if (p_70037_1_.hasKey("SaddleItem", 10)) {
			itemstack = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("SaddleItem"));

			if (itemstack != null && itemstack.getItem() == Items.saddle) {
				horseChest.setInventorySlotContents(0, itemstack);
			}
		} else if (p_70037_1_.getBoolean("Saddle")) {
			horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
		}

		func_110232_cE();
	}

	@Override
	public boolean canMateWith(EntityAnimal p_70878_1_) {
		if (p_70878_1_ == this)
			return false;
		else if (p_70878_1_.getClass() != this.getClass())
			return false;
		else {
			EntityHorse entityhorse = (EntityHorse) p_70878_1_;

			if (func_110200_cJ() && entityhorse.func_110200_cJ()) {
				int i = getHorseType();
				int j = entityhorse.getHorseType();
				return i == j || i == 0 && j == 1 || i == 1 && j == 0;
			} else
				return false;
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable p_90011_1_) {
		EntityHorse entityhorse = (EntityHorse) p_90011_1_;
		EntityHorse entityhorse1 = new EntityHorse(worldObj);
		int i = getHorseType();
		int j = entityhorse.getHorseType();
		int k = 0;

		if (i == j) {
			k = i;
		} else if (i == 0 && j == 1 || i == 1 && j == 0) {
			k = 2;
		}

		if (k == 0) {
			int i1 = rand.nextInt(9);
			int l;

			if (i1 < 4) {
				l = getHorseVariant() & 255;
			} else if (i1 < 8) {
				l = entityhorse.getHorseVariant() & 255;
			} else {
				l = rand.nextInt(7);
			}

			int j1 = rand.nextInt(5);

			if (j1 < 2) {
				l |= getHorseVariant() & 65280;
			} else if (j1 < 4) {
				l |= entityhorse.getHorseVariant() & 65280;
			} else {
				l |= rand.nextInt(5) << 8 & 65280;
			}

			entityhorse1.setHorseVariant(l);
		}

		entityhorse1.setHorseType(k);
		double d1 = getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue()
				+ p_90011_1_.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + func_110267_cL();
		entityhorse1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(d1 / 3.0D);
		double d2 = getEntityAttribute(horseJumpStrength).getBaseValue()
				+ p_90011_1_.getEntityAttribute(horseJumpStrength).getBaseValue() + func_110245_cM();
		entityhorse1.getEntityAttribute(horseJumpStrength).setBaseValue(d2 / 3.0D);
		double d0 = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
				+ p_90011_1_.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
				+ func_110203_cN();
		entityhorse1.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(d0 / 3.0D);
		return entityhorse1;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
		int i = 0;
		int l;

		if (p_110161_1_1 instanceof EntityHorse.GroupData) {
			l = ((EntityHorse.GroupData) p_110161_1_1).field_111107_a;
			i = ((EntityHorse.GroupData) p_110161_1_1).field_111106_b & 255 | rand.nextInt(5) << 8;
		} else {
			if (rand.nextInt(10) == 0) {
				l = 1;
			} else {
				int j = rand.nextInt(7);
				int k = rand.nextInt(5);
				l = 0;
				i = j | k << 8;
			}

			p_110161_1_1 = new EntityHorse.GroupData(l, i);
		}

		setHorseType(l);
		setHorseVariant(i);

		if (rand.nextInt(5) == 0) {
			setGrowingAge(-24000);
		}

		if (l != 4 && l != 3) {
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(func_110267_cL());

			if (l == 0) {
				getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(func_110203_cN());
			} else {
				getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.17499999701976776D);
			}
		} else {
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
			getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
		}

		if (l != 2 && l != 1) {
			getEntityAttribute(horseJumpStrength).setBaseValue(func_110245_cM());
		} else {
			getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
		}

		setHealth(getMaxHealth());
		return (IEntityLivingData) p_110161_1_1;
	}

	@SideOnly(Side.CLIENT)
	public float getGrassEatingAmount(float p_110258_1_) {
		return prevHeadLean + (headLean - prevHeadLean) * p_110258_1_;
	}

	@SideOnly(Side.CLIENT)
	public float getRearingAmount(float p_110223_1_) {
		return prevRearingAmount + (rearingAmount - prevRearingAmount) * p_110223_1_;
	}

	@SideOnly(Side.CLIENT)
	public float func_110201_q(float p_110201_1_) {
		return prevMouthOpenness + (mouthOpenness - prevMouthOpenness) * p_110201_1_;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	public void setJumpPower(int p_110206_1_) {
		if (isHorseSaddled()) {
			if (p_110206_1_ < 0) {
				p_110206_1_ = 0;
			}

			float power;

			if (p_110206_1_ >= 90) {
				//jumpPower = 1.0F;
				power = 1.0F;
			} else {
				//jumpPower = 0.4F + 0.4F * p_110206_1_ / 90.0F;
				power = 0.4F + 0.4F * (float)p_110206_1_ / 90.0F;
			}

			HorseJumpEvent event = CraftEventFactory.callHorseJumpEvent(this, power);
			if (!event.isCancelled()) {
				this.field_110294_bI = true;
				this.makeHorseRear();
				this.jumpPower = event.getPower();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	protected void spawnHorseParticles(boolean p_110216_1_) {
		String s = p_110216_1_ ? "heart" : "smoke";

		for (int i = 0; i < 7; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(s, posX + rand.nextFloat() * width * 2.0F - width,
					posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
					d2);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 7) {
			spawnHorseParticles(true);
		} else if (p_70103_1_ == 6) {
			spawnHorseParticles(false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();

		if (prevRearingAmount > 0.0F) {
			float f = MathHelper.sin(renderYawOffset * (float) Math.PI / 180.0F);
			float f1 = MathHelper.cos(renderYawOffset * (float) Math.PI / 180.0F);
			float f2 = 0.7F * prevRearingAmount;
			float f3 = 0.15F * prevRearingAmount;
			riddenByEntity.setPosition(posX + f2 * f, posY + getMountedYOffset() + riddenByEntity.getYOffset() + f3,
					posZ - f2 * f1);

			if (riddenByEntity instanceof EntityLivingBase) {
				((EntityLivingBase) riddenByEntity).renderYawOffset = renderYawOffset;
			}
		}
	}

	private float func_110267_cL() {
		return 15.0F + rand.nextInt(8) + rand.nextInt(9);
	}

	private double func_110245_cM() {
		return 0.4000000059604645D + rand.nextDouble() * 0.2D + rand.nextDouble() * 0.2D + rand.nextDouble() * 0.2D;
	}

	private double func_110203_cN() {
		return (0.44999998807907104D + rand.nextDouble() * 0.3D + rand.nextDouble() * 0.3D + rand.nextDouble() * 0.3D)
				* 0.25D;
	}

	public static boolean func_146085_a(Item p_146085_0_) {
		return p_146085_0_ == Items.iron_horse_armor || p_146085_0_ == Items.golden_horse_armor
				|| p_146085_0_ == Items.diamond_horse_armor;
	}

	@Override
	public boolean isOnLadder() {
		return false;
	}

	public static class GroupData implements IEntityLivingData {
		public int field_111107_a;
		public int field_111106_b;
		private static final String __OBFID = "CL_00001643";

		public GroupData(int p_i1684_1_, int p_i1684_2_) {
			field_111107_a = p_i1684_1_;
			field_111106_b = p_i1684_2_;
		}
	}

	public void createChest() {
		func_110226_cD();
	}

	public AnimalChest getHorseChest() {
		return horseChest;
	}

	public void setHorseChest(AnimalChest horseChest) {
		this.horseChest = horseChest;
	}

	public IAttribute getStaticHorseJumpStrength() {
		return horseJumpStrength;
	}

	public int getMaxDomestication() {
		return maxDomestication;
	}

	public void setMaxDomestication(int maxDomestication) {
		this.maxDomestication = maxDomestication;
	}
}