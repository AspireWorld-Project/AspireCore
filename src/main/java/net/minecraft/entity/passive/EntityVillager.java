package net.minecraft.entity.passive;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityVillager extends EntityAgeable implements IMerchant, INpc {
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	private EntityPlayer buyingPlayer;
	private MerchantRecipeList buyingList;
	private int timeUntilReset;
	private boolean needsInitilization;
	private int wealth;
	private String lastBuyingPlayer;
	private boolean isLookingForHome;
	private float field_82191_bN;
	public static final Map villagersSellingList = new HashMap();
	public static final Map blacksmithSellingList = new HashMap();
	private static final String __OBFID = "CL_00001707";

	public EntityVillager(World p_i1747_1_) {
		this(p_i1747_1_, 0);
	}

	public EntityVillager(World p_i1748_1_, int p_i1748_2_) {
		super(p_i1748_1_);
		setProfession(p_i1748_2_);
		setSize(0.6F, 1.8F);
		getNavigator().setBreakDoors(true);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(1, new EntityAITradePlayer(this));
		tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		tasks.addTask(2, new EntityAIMoveIndoors(this));
		tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		tasks.addTask(6, new EntityAIVillagerMate(this));
		tasks.addTask(7, new EntityAIFollowGolem(this));
		tasks.addTask(8, new EntityAIPlay(this, 0.32D));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
		tasks.addTask(9, new EntityAIWander(this, 0.6D));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void updateAITick() {
		if (--randomTickDivider <= 0) {
			worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(posX),
					MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
			randomTickDivider = 70 + rand.nextInt(50);
			villageObj = worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(posX),
					MathHelper.floor_double(posY), MathHelper.floor_double(posZ), 32);

			if (villageObj == null) {
				detachHome();
			} else {
				ChunkCoordinates chunkcoordinates = villageObj.getCenter();
				setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ,
						(int) (villageObj.getVillageRadius() * 0.6F));

				if (isLookingForHome) {
					isLookingForHome = false;
					villageObj.setDefaultPlayerReputation(5);
				}
			}
		}

		if (!isTrading() && timeUntilReset > 0) {
			--timeUntilReset;

			if (timeUntilReset <= 0) {
				if (needsInitilization) {
					if (buyingList.size() > 1) {
						Iterator iterator = buyingList.iterator();

						while (iterator.hasNext()) {
							MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

							if (merchantrecipe.isRecipeDisabled()) {
								merchantrecipe.func_82783_a(rand.nextInt(6) + rand.nextInt(6) + 2);
							}
						}
					}

					addDefaultEquipmentAndRecipies(1);
					needsInitilization = false;

					if (villageObj != null && lastBuyingPlayer != null) {
						worldObj.setEntityState(this, (byte) 14);
						villageObj.setReputationForPlayer(lastBuyingPlayer, 1);
					}
				}

				addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITick();
	}

	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !p_70085_1_.isSneaking()) {
			if (!worldObj.isRemote) {
				setCustomer(p_70085_1_);
				p_70085_1_.displayGUIMerchant(this, getCustomNameTag());
			}

			return true;
		} else
			return super.interact(p_70085_1_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Profession", getProfession());
		p_70014_1_.setInteger("Riches", wealth);

		if (buyingList != null) {
			p_70014_1_.setTag("Offers", buyingList.getRecipiesAsTags());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setProfession(p_70037_1_.getInteger("Profession"));
		wealth = p_70037_1_.getInteger("Riches");

		if (p_70037_1_.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound1 = p_70037_1_.getCompoundTag("Offers");
			buyingList = new MerchantRecipeList(nbttagcompound1);
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected String getLivingSound() {
		return isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.villager.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.villager.death";
	}

	public void setProfession(int p_70938_1_) {
		dataWatcher.updateObject(16, Integer.valueOf(p_70938_1_));
	}

	public int getProfession() {
		return dataWatcher.getWatchableObjectInt(16);
	}

	public boolean isMating() {
		return isMating;
	}

	public void setMating(boolean p_70947_1_) {
		isMating = p_70947_1_;
	}

	public void setPlaying(boolean p_70939_1_) {
		isPlaying = p_70939_1_;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	@Override
	public void setRevengeTarget(EntityLivingBase p_70604_1_) {
		super.setRevengeTarget(p_70604_1_);

		if (villageObj != null && p_70604_1_ != null) {
			villageObj.addOrRenewAgressor(p_70604_1_);

			if (p_70604_1_ instanceof EntityPlayer) {
				byte b0 = -1;

				if (isChild()) {
					b0 = -3;
				}

				villageObj.setReputationForPlayer(p_70604_1_.getCommandSenderName(), b0);

				if (isEntityAlive()) {
					worldObj.setEntityState(this, (byte) 13);
				}
			}
		}
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (villageObj != null) {
			Entity entity = p_70645_1_.getEntity();

			if (entity != null) {
				if (entity instanceof EntityPlayer) {
					villageObj.setReputationForPlayer(entity.getCommandSenderName(), -2);
				} else if (entity instanceof IMob) {
					villageObj.endMatingSeason();
				}
			} else if (entity == null) {
				EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 16.0D);

				if (entityplayer != null) {
					villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(p_70645_1_);
	}

	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
		buyingPlayer = p_70932_1_;
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	public boolean isTrading() {
		return buyingPlayer != null;
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
		p_70933_1_.incrementToolUses();
		livingSoundTime = -getTalkInterval();
		playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());

		if (p_70933_1_.hasSameIDsAs((MerchantRecipe) buyingList.get(buyingList.size() - 1))) {
			timeUntilReset = 40;
			needsInitilization = true;

			if (buyingPlayer != null) {
				lastBuyingPlayer = buyingPlayer.getCommandSenderName();
			} else {
				lastBuyingPlayer = null;
			}
		}

		if (p_70933_1_.getItemToBuy().getItem() == Items.emerald) {
			wealth += p_70933_1_.getItemToBuy().stackSize;
		}
	}

	@Override
	public void func_110297_a_(ItemStack p_110297_1_) {
		if (!worldObj.isRemote && livingSoundTime > -getTalkInterval() + 20) {
			livingSoundTime = -getTalkInterval();

			if (p_110297_1_ != null) {
				playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
			} else {
				playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
			}
		}
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		if (buyingList == null) {
			addDefaultEquipmentAndRecipies(1);
		}

		return buyingList;
	}

	private float adjustProbability(float p_82188_1_) {
		float f1 = p_82188_1_ + field_82191_bN;
		return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
	}

	private void addDefaultEquipmentAndRecipies(int p_70950_1_) {
		if (buyingList != null) {
			field_82191_bN = MathHelper.sqrt_float(buyingList.size()) * 0.2F;
		} else {
			field_82191_bN = 0.0F;
		}

		MerchantRecipeList merchantrecipelist;
		merchantrecipelist = new MerchantRecipeList();
		VillagerRegistry.manageVillagerTrades(merchantrecipelist, this, getProfession(), rand);
		int k;
		label50:

		switch (getProfession()) {
		case 0:
			func_146091_a(merchantrecipelist, Items.wheat, rand, adjustProbability(0.9F));
			func_146091_a(merchantrecipelist, Item.getItemFromBlock(Blocks.wool), rand, adjustProbability(0.5F));
			func_146091_a(merchantrecipelist, Items.chicken, rand, adjustProbability(0.5F));
			func_146091_a(merchantrecipelist, Items.cooked_fished, rand, adjustProbability(0.4F));
			func_146089_b(merchantrecipelist, Items.bread, rand, adjustProbability(0.9F));
			func_146089_b(merchantrecipelist, Items.melon, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.apple, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.cookie, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.shears, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.flint_and_steel, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.cooked_chicken, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.arrow, rand, adjustProbability(0.5F));

			if (rand.nextFloat() < adjustProbability(0.5F)) {
				merchantrecipelist.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10),
						new ItemStack(Items.emerald), new ItemStack(Items.flint, 4 + rand.nextInt(2), 0)));
			}

			break;
		case 1:
			func_146091_a(merchantrecipelist, Items.paper, rand, adjustProbability(0.8F));
			func_146091_a(merchantrecipelist, Items.book, rand, adjustProbability(0.8F));
			func_146091_a(merchantrecipelist, Items.written_book, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.bookshelf), rand, adjustProbability(0.8F));
			func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.glass), rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.compass, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.clock, rand, adjustProbability(0.2F));

			if (rand.nextFloat() < adjustProbability(0.07F)) {
				Enchantment enchantment = Enchantment.enchantmentsBookList[rand
						.nextInt(Enchantment.enchantmentsBookList.length)];
				int i1 = MathHelper.getRandomIntegerInRange(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
				ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i1));
				k = 2 + rand.nextInt(5 + i1 * 10) + 3 * i1;
				merchantrecipelist
						.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, k), itemstack));
			}

			break;
		case 2:
			func_146089_b(merchantrecipelist, Items.ender_eye, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.experience_bottle, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.redstone, rand, adjustProbability(0.4F));
			func_146089_b(merchantrecipelist, Item.getItemFromBlock(Blocks.glowstone), rand, adjustProbability(0.3F));
			Item[] aitem = new Item[] { Items.iron_sword, Items.diamond_sword, Items.iron_chestplate,
					Items.diamond_chestplate, Items.iron_axe, Items.diamond_axe, Items.iron_pickaxe,
					Items.diamond_pickaxe };
			Item[] aitem1 = aitem;
			int j = aitem.length;
			k = 0;

			while (true) {
				if (k >= j) {
					break label50;
				}

				Item item = aitem1[k];

				if (rand.nextFloat() < adjustProbability(0.05F)) {
					merchantrecipelist.add(new MerchantRecipe(new ItemStack(item, 1, 0),
							new ItemStack(Items.emerald, 2 + rand.nextInt(3), 0), EnchantmentHelper
									.addRandomEnchantment(rand, new ItemStack(item, 1, 0), 5 + rand.nextInt(15))));
				}

				++k;
			}
		case 3:
			func_146091_a(merchantrecipelist, Items.coal, rand, adjustProbability(0.7F));
			func_146091_a(merchantrecipelist, Items.iron_ingot, rand, adjustProbability(0.5F));
			func_146091_a(merchantrecipelist, Items.gold_ingot, rand, adjustProbability(0.5F));
			func_146091_a(merchantrecipelist, Items.diamond, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.iron_sword, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.diamond_sword, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.iron_axe, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.diamond_axe, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.iron_pickaxe, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.diamond_pickaxe, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.iron_shovel, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_shovel, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.iron_hoe, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_hoe, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.iron_boots, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_boots, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.iron_helmet, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_helmet, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.iron_chestplate, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_chestplate, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.iron_leggings, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.diamond_leggings, rand, adjustProbability(0.2F));
			func_146089_b(merchantrecipelist, Items.chainmail_boots, rand, adjustProbability(0.1F));
			func_146089_b(merchantrecipelist, Items.chainmail_helmet, rand, adjustProbability(0.1F));
			func_146089_b(merchantrecipelist, Items.chainmail_chestplate, rand, adjustProbability(0.1F));
			func_146089_b(merchantrecipelist, Items.chainmail_leggings, rand, adjustProbability(0.1F));
			break;
		case 4:
			func_146091_a(merchantrecipelist, Items.coal, rand, adjustProbability(0.7F));
			func_146091_a(merchantrecipelist, Items.porkchop, rand, adjustProbability(0.5F));
			func_146091_a(merchantrecipelist, Items.beef, rand, adjustProbability(0.5F));
			func_146089_b(merchantrecipelist, Items.saddle, rand, adjustProbability(0.1F));
			func_146089_b(merchantrecipelist, Items.leather_chestplate, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.leather_boots, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.leather_helmet, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.leather_leggings, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.cooked_porkchop, rand, adjustProbability(0.3F));
			func_146089_b(merchantrecipelist, Items.cooked_beef, rand, adjustProbability(0.3F));
		}

		if (merchantrecipelist.isEmpty()) {
			func_146091_a(merchantrecipelist, Items.gold_ingot, rand, 1.0F);
		}

		Collections.shuffle(merchantrecipelist);

		if (buyingList == null) {
			buyingList = new MerchantRecipeList();
		}

		for (int l = 0; l < p_70950_1_ && l < merchantrecipelist.size(); ++l) {
			buyingList.addToListWithCheck((MerchantRecipe) merchantrecipelist.get(l));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRecipes(MerchantRecipeList p_70930_1_) {
	}

	public static void func_146091_a(MerchantRecipeList p_146091_0_, Item p_146091_1_, Random p_146091_2_,
			float p_146091_3_) {
		if (p_146091_2_.nextFloat() < p_146091_3_) {
			p_146091_0_.add(new MerchantRecipe(func_146088_a(p_146091_1_, p_146091_2_), Items.emerald));
		}
	}

	private static ItemStack func_146088_a(Item p_146088_0_, Random p_146088_1_) {
		return new ItemStack(p_146088_0_, func_146092_b(p_146088_0_, p_146088_1_), 0);
	}

	private static int func_146092_b(Item p_146092_0_, Random p_146092_1_) {
		Tuple tuple = (Tuple) villagersSellingList.get(p_146092_0_);
		return tuple == null ? 1
				: ((Integer) tuple.getFirst()).intValue() >= ((Integer) tuple.getSecond()).intValue()
						? ((Integer) tuple.getFirst()).intValue()
						: ((Integer) tuple.getFirst()).intValue() + p_146092_1_.nextInt(
								((Integer) tuple.getSecond()).intValue() - ((Integer) tuple.getFirst()).intValue());
	}

	public static void func_146089_b(MerchantRecipeList p_146089_0_, Item p_146089_1_, Random p_146089_2_,
			float p_146089_3_) {
		if (p_146089_2_.nextFloat() < p_146089_3_) {
			int i = func_146090_c(p_146089_1_, p_146089_2_);
			ItemStack itemstack;
			ItemStack itemstack1;

			if (i < 0) {
				itemstack = new ItemStack(Items.emerald, 1, 0);
				itemstack1 = new ItemStack(p_146089_1_, -i, 0);
			} else {
				itemstack = new ItemStack(Items.emerald, i, 0);
				itemstack1 = new ItemStack(p_146089_1_, 1, 0);
			}

			p_146089_0_.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	private static int func_146090_c(Item p_146090_0_, Random p_146090_1_) {
		Tuple tuple = (Tuple) blacksmithSellingList.get(p_146090_0_);
		return tuple == null ? 1
				: ((Integer) tuple.getFirst()).intValue() >= ((Integer) tuple.getSecond()).intValue()
						? ((Integer) tuple.getFirst()).intValue()
						: ((Integer) tuple.getFirst()).intValue() + p_146090_1_.nextInt(
								((Integer) tuple.getSecond()).intValue() - ((Integer) tuple.getFirst()).intValue());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 12) {
			generateRandomParticles("heart");
		} else if (p_70103_1_ == 13) {
			generateRandomParticles("angryVillager");
		} else if (p_70103_1_ == 14) {
			generateRandomParticles("happyVillager");
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
		VillagerRegistry.applyRandomTrade(this, worldObj.rand);
		return p_110161_1_;
	}

	@SideOnly(Side.CLIENT)
	private void generateRandomParticles(String p_70942_1_) {
		for (int i = 0; i < 5; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(p_70942_1_, posX + rand.nextFloat() * width * 2.0F - width,
					posY + 1.0D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, d0, d1,
					d2);
		}
	}

	public void setLookingForHome() {
		isLookingForHome = true;
	}

	@Override
	public EntityVillager createChild(EntityAgeable p_90011_1_) {
		EntityVillager entityvillager = new EntityVillager(worldObj);
		entityvillager.onSpawnWithEgg((IEntityLivingData) null);
		return entityvillager;
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	static {
		villagersSellingList.put(Items.coal, new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
		villagersSellingList.put(Items.iron_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		villagersSellingList.put(Items.gold_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		villagersSellingList.put(Items.diamond, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		villagersSellingList.put(Items.paper, new Tuple(Integer.valueOf(24), Integer.valueOf(36)));
		villagersSellingList.put(Items.book, new Tuple(Integer.valueOf(11), Integer.valueOf(13)));
		villagersSellingList.put(Items.written_book, new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
		villagersSellingList.put(Items.ender_pearl, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		villagersSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
		villagersSellingList.put(Items.porkchop, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagersSellingList.put(Items.beef, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagersSellingList.put(Items.chicken, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagersSellingList.put(Items.cooked_fished, new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
		villagersSellingList.put(Items.wheat_seeds, new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
		villagersSellingList.put(Items.melon_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagersSellingList.put(Items.pumpkin_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagersSellingList.put(Items.wheat, new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
		villagersSellingList.put(Item.getItemFromBlock(Blocks.wool),
				new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
		villagersSellingList.put(Items.rotten_flesh, new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
		blacksmithSellingList.put(Items.flint_and_steel, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.shears, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.iron_sword, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.diamond_sword, new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.iron_axe, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.diamond_axe, new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
		blacksmithSellingList.put(Items.iron_pickaxe, new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
		blacksmithSellingList.put(Items.diamond_pickaxe, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Items.iron_shovel, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_shovel, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_hoe, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_hoe, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_boots, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_boots, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_helmet, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Items.diamond_helmet, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.iron_chestplate, new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.diamond_chestplate, new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
		blacksmithSellingList.put(Items.iron_leggings, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		blacksmithSellingList.put(Items.diamond_leggings, new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
		blacksmithSellingList.put(Items.chainmail_boots, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Items.chainmail_helmet, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Items.chainmail_chestplate, new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
		blacksmithSellingList.put(Items.chainmail_leggings, new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.bread, new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
		blacksmithSellingList.put(Items.melon, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		blacksmithSellingList.put(Items.apple, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		blacksmithSellingList.put(Items.cookie, new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glass),
				new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.bookshelf),
				new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_chestplate, new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
		blacksmithSellingList.put(Items.leather_boots, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_helmet, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.leather_leggings, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Items.saddle, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		blacksmithSellingList.put(Items.experience_bottle, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.redstone, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.compass, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Items.clock, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glowstone),
				new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
		blacksmithSellingList.put(Items.cooked_porkchop, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Items.cooked_beef, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Items.cooked_chicken, new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
		blacksmithSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Items.arrow, new Tuple(Integer.valueOf(-12), Integer.valueOf(-8)));
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	@Override
	public org.ultramine.server.EntityType computeEntityType() {
		return org.ultramine.server.EntityType.ANIMAL;
	}
}