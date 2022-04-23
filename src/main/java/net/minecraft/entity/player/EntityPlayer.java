package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.*;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.ultramine.server.util.MinecraftUtil;

import java.util.*;
import java.util.Map.Entry;

public abstract class EntityPlayer extends EntityLivingBase implements ICommandSender {
	public static final String PERSISTED_NBT_TAG = "PlayerPersisted";
	public HashMap<Integer, ChunkCoordinates> spawnChunkMap = new HashMap<>();
	public HashMap<Integer, Boolean> spawnForcedMap = new HashMap<>();

	public InventoryPlayer inventory = new InventoryPlayer(this);
	private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
	public Container inventoryContainer;
	public Container openContainer;
	protected FoodStats foodStats = new FoodStats();
	protected int flyToggleTimer;
	public float prevCameraYaw;
	public float cameraYaw;
	public int xpCooldown;
	public double field_71091_bM;
	public double field_71096_bN;
	public double field_71097_bO;
	public double field_71094_bP;
	public double field_71095_bQ;
	public double field_71085_bR;
	protected boolean sleeping;
	public ChunkCoordinates playerLocation;
	private int sleepTimer;
	public float field_71079_bU;
	@SideOnly(Side.CLIENT)
	public float field_71082_cx;
	public float field_71089_bV;
	private ChunkCoordinates spawnChunk;
	private boolean spawnForced;
	private ChunkCoordinates startMinecartRidingCoordinate;
	public PlayerCapabilities capabilities = new PlayerCapabilities();
	public int oldLevel = -1;
	public int experienceLevel;
	public int experienceTotal;
	public float experience;
	private ItemStack itemInUse;
	private int itemInUseCount;
	protected float speedOnGround = 0.1F;
	protected float speedInAir = 0.02F;
	private int field_82249_h;
	private final GameProfile field_146106_i;
	public EntityFishHook fishEntity;
	private static final String __OBFID = "CL_00001711";

	public EntityPlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
		super(p_i45324_1_);
		entityUniqueID = func_146094_a(p_i45324_2_);
		field_146106_i = p_i45324_2_;
		inventoryContainer = new ContainerPlayer(inventory, !p_i45324_1_.isRemote, this);
		openContainer = inventoryContainer;
		yOffset = 1.62F;
		ChunkCoordinates chunkcoordinates = p_i45324_1_.getSpawnPoint();
		setLocationAndAngles(chunkcoordinates.posX + 0.5D, chunkcoordinates.posY + 1, chunkcoordinates.posZ + 0.5D,
				0.0F, 0.0F);
		field_70741_aB = 180.0F;
		fireResistance = 20;
		eyeHeight = getDefaultEyeHeight();
		theInventoryEnderChest.setOwner(this);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		dataWatcher.addObject(17, Float.valueOf(0.0F));
		dataWatcher.addObject(18, Integer.valueOf(0));
	}

	@SideOnly(Side.CLIENT)
	public ItemStack getItemInUse() {
		return itemInUse;
	}

	@SideOnly(Side.CLIENT)
	public int getItemInUseCount() {
		return itemInUseCount;
	}

	public boolean isUsingItem() {
		return itemInUse != null;
	}

	@SideOnly(Side.CLIENT)
	public int getItemInUseDuration() {
		return isUsingItem() ? itemInUse.getMaxItemUseDuration() - itemInUseCount : 0;
	}

	public void stopUsingItem() {
		if (itemInUse != null) {
			if (!ForgeEventFactory.onUseItemStop(this, itemInUse, itemInUseCount)) {
				itemInUse.onPlayerStoppedUsing(worldObj, this, itemInUseCount);
			}
		}

		clearItemInUse();
	}

	public void clearItemInUse() {
		itemInUse = null;
		itemInUseCount = 0;

		if (!worldObj.isRemote) {
			setEating(false);
		}
	}

	public boolean isBlocking() {
		return isUsingItem() && itemInUse.getItem().getItemUseAction(itemInUse) == EnumAction.block;
	}

	@Override
	public void onUpdate() {
		FMLCommonHandler.instance().onPlayerPreTick(this);
		if (itemInUse != null) {
			ItemStack itemstack = inventory.getCurrentItem();

			if (itemstack == itemInUse) {
				itemInUseCount = ForgeEventFactory.onItemUseTick(this, itemInUse, itemInUseCount);
				if (itemInUseCount <= 0) {
					onItemUseFinish();
				} else {
					itemInUse.getItem().onUsingTick(itemInUse, this, itemInUseCount);
					if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
						updateItemUse(itemstack, 5);
					}

					if (--itemInUseCount == 0 && !worldObj.isRemote) {
						onItemUseFinish();
					}
				}
			} else {
				clearItemInUse();
			}
		}

		if (xpCooldown > 0) {
			--xpCooldown;
		}

		if (isPlayerSleeping()) {
			++sleepTimer;

			if (sleepTimer > 100) {
				sleepTimer = 100;
			}

			if (!worldObj.isRemote) {
				if (!isInBed()) {
					wakeUpPlayer(true, true, false);
				} else if (worldObj.isDaytime()) {
					wakeUpPlayer(false, true, true);
				}
			}
		} else if (sleepTimer > 0) {
			++sleepTimer;

			if (sleepTimer >= 110) {
				sleepTimer = 0;
			}
		}

		super.onUpdate();

		if (!worldObj.isRemote && openContainer != null && !ForgeHooks.canInteractWith(this, openContainer)) {
			closeScreen();
			openContainer = inventoryContainer;
		}

		if (isBurning() && capabilities.disableDamage) {
			extinguish();
		}

		field_71091_bM = field_71094_bP;
		field_71096_bN = field_71095_bQ;
		field_71097_bO = field_71085_bR;
		double d3 = posX - field_71094_bP;
		double d0 = posY - field_71095_bQ;
		double d1 = posZ - field_71085_bR;
		double d2 = 10.0D;

		if (d3 > d2) {
			field_71091_bM = field_71094_bP = posX;
		}

		if (d1 > d2) {
			field_71097_bO = field_71085_bR = posZ;
		}

		if (d0 > d2) {
			field_71096_bN = field_71095_bQ = posY;
		}

		if (d3 < -d2) {
			field_71091_bM = field_71094_bP = posX;
		}

		if (d1 < -d2) {
			field_71097_bO = field_71085_bR = posZ;
		}

		if (d0 < -d2) {
			field_71096_bN = field_71095_bQ = posY;
		}

		field_71094_bP += d3 * 0.25D;
		field_71085_bR += d1 * 0.25D;
		field_71095_bQ += d0 * 0.25D;

		if (ridingEntity == null) {
			startMinecartRidingCoordinate = null;
		}

		if (!worldObj.isRemote) {
			foodStats.onUpdate(this);
			addStat(StatList.minutesPlayedStat, 1);
		}
		FMLCommonHandler.instance().onPlayerPostTick(this);
	}

	@Override
	public int getMaxInPortalTime() {
		return capabilities.disableDamage ? 0 : 80;
	}

	@Override
	protected String getSwimSound() {
		return "game.player.swim";
	}

	@Override
	protected String getSplashSound() {
		return "game.player.swim.splash";
	}

	@Override
	public int getPortalCooldown() {
		return 10;
	}

	@Override
	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		worldObj.playSoundToNearExcept(this, p_85030_1_, p_85030_2_, p_85030_3_);
	}

	protected void updateItemUse(ItemStack p_71010_1_, int p_71010_2_) {
		if (p_71010_1_.getItemUseAction() == EnumAction.drink) {
			playSound("random.drink", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (p_71010_1_.getItemUseAction() == EnumAction.eat) {
			for (int j = 0; j < p_71010_2_; ++j) {
				Vec3 vec3 = Vec3.createVectorHelper((rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D,
						0.0D);
				vec3.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
				vec3.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
				Vec3 vec31 = Vec3.createVectorHelper((rand.nextFloat() - 0.5D) * 0.3D, -rand.nextFloat() * 0.6D - 0.3D,
						0.6D);
				vec31.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
				vec31.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
				vec31 = vec31.addVector(posX, posY + getEyeHeight(), posZ);
				String s = "iconcrack_" + Item.getIdFromItem(p_71010_1_.getItem());

				if (p_71010_1_.getHasSubtypes()) {
					s = s + "_" + p_71010_1_.getItemDamage();
				}

				worldObj.spawnParticle(s, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D,
						vec3.zCoord);
			}

			playSound("random.eat", 0.5F + 0.5F * rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		}
	}

	protected void onItemUseFinish() {
		if (itemInUse != null) {
			org.bukkit.inventory.ItemStack originalItemStackCopy = CraftItemStack.asBukkitCopy(itemInUse);
			PlayerItemConsumeEvent event = new PlayerItemConsumeEvent((Player) getBukkitEntity(),
					originalItemStackCopy);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled() || event.getItem().getType() == org.bukkit.Material.AIR) // Fix for internal server
																								// error.
			{
				// Update client
				if (this instanceof EntityPlayerMP) {
					((EntityPlayerMP) this).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0,
							openContainer.getSlotFromInventory(inventory, inventory.currentItem).getSlotIndex(),
							itemInUse));
					((CraftPlayer) getBukkitEntity()).updateInventory();
					((CraftPlayer) getBukkitEntity()).updateScaledHealth();
				}
				// Fix for recursive event spam.
				itemInUse = null;
				return;
			}
			// Plugin modified the item, process it but don't remove it
			if (!originalItemStackCopy.equals(event.getItem())) {
				CraftItemStack.asNMSCopy(event.getItem()).onFoodEaten(worldObj, this);
				// Update client
				if (this instanceof EntityPlayerMP) {
					((EntityPlayerMP) this).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0,
							openContainer.getSlotFromInventory(inventory, inventory.currentItem).getSlotIndex(),
							itemInUse));
					((CraftPlayer) getBukkitEntity()).updateInventory();
					((CraftPlayer) getBukkitEntity()).updateScaledHealth();
				}
				// Fix for recursive event spam.
				itemInUse = null;
				return;
			}
			updateItemUse(itemInUse, 16);
			int i = itemInUse.stackSize;
			ItemStack itemstack = itemInUse.onFoodEaten(worldObj, this);

			itemstack = ForgeEventFactory.onItemUseFinish(this, itemInUse, itemInUseCount, itemstack);

			if (itemstack != itemInUse || itemstack != null && itemstack.stackSize != i) {
				inventory.mainInventory[inventory.currentItem] = itemstack;

				if (itemstack != null && itemstack.stackSize == 0) {
					inventory.mainInventory[inventory.currentItem] = null;
				}
			}

			clearItemInUse();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 9) {
			onItemUseFinish();
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	@Override
	protected boolean isMovementBlocked() {
		return getHealth() <= 0.0F || isPlayerSleeping();
	}

	public void closeScreen() {
		openContainer = inventoryContainer;
	}

	@Override
	public void mountEntity(Entity p_70078_1_) {
		setPassengerOf(p_70078_1_);
	}

	@Override
	public void updateRidden() {
		if (!worldObj.isRemote && isSneaking()) {
			mountEntity((Entity) null);
			setSneaking(false);
		} else {
			double d0 = posX;
			double d1 = posY;
			double d2 = posZ;
			float f = rotationYaw;
			float f1 = rotationPitch;
			super.updateRidden();
			prevCameraYaw = cameraYaw;
			cameraYaw = 0.0F;
			addMountedMovementStat(posX - d0, posY - d1, posZ - d2);

			if (ridingEntity instanceof EntityLivingBase
					&& ((EntityLivingBase) ridingEntity).shouldRiderFaceForward(this)) {
				rotationPitch = f1;
				rotationYaw = f;
				renderYawOffset = ((EntityLivingBase) ridingEntity).renderYawOffset;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preparePlayerToSpawn() {
		yOffset = 1.62F;
		setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		setHealth(getMaxHealth());
		deathTime = 0;
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		updateArmSwingProgress();
	}

	@Override
	public void onLivingUpdate() {
		if (flyToggleTimer > 0) {
			--flyToggleTimer;
		}

		if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL && getHealth() < getMaxHealth()
				&& worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration")
				&& ticksExisted % 20 * 12 == 0) {
			this.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
		}

		inventory.decrementAnimations();
		prevCameraYaw = cameraYaw;
		super.onLivingUpdate();
		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (!worldObj.isRemote) {
			iattributeinstance.setBaseValue(capabilities.getWalkSpeed());
		}

		jumpMovementFactor = speedInAir;

		if (isSprinting()) {
			jumpMovementFactor = (float) (jumpMovementFactor + speedInAir * 0.3D);
		}

		setAIMoveSpeed((float) iattributeinstance.getAttributeValue());
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		float f1 = (float) Math.atan(-motionY * 0.20000000298023224D) * 15.0F;

		if (f > 0.1F) {
			f = 0.1F;
		}

		if (!onGround || getHealth() <= 0.0F) {
			f = 0.0F;
		}

		if (onGround || getHealth() <= 0.0F) {
			f1 = 0.0F;
		}

		cameraYaw += (f - cameraYaw) * 0.4F;
		cameraPitch += (f1 - cameraPitch) * 0.8F;

		if (getHealth() > 0.0F) {
			AxisAlignedBB axisalignedbb = null;

			if (ridingEntity != null && !ridingEntity.isDead) {
				axisalignedbb = boundingBox.func_111270_a(ridingEntity.boundingBox).expand(1.0D, 0.0D, 1.0D);
			} else {
				axisalignedbb = boundingBox.expand(1.0D, 0.5D, 1.0D);
			}

			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

			if (list != null) {
				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);

					if (!entity.isDead) {
						collideWithPlayer(entity);
					}
				}
			}
		}
	}

	private void collideWithPlayer(Entity p_71044_1_) {
		p_71044_1_.onCollideWithPlayer(this);
	}

	public int getScore() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	public void setScore(int p_85040_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
	}

	public void addScore(int p_85039_1_) {
		int j = getScore();
		dataWatcher.updateObject(18, Integer.valueOf(j + p_85039_1_));
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (ForgeHooks.onLivingDeath(this, p_70645_1_))
			return;
		super.onDeath(p_70645_1_);
		setSize(0.2F, 0.2F);
		setPosition(posX, posY, posZ);
		motionY = 0.10000000149011612D;

		captureDrops = true;
		capturedDrops.clear();

		if (getCommandSenderName().equals("Notch")) {
			func_146097_a(new ItemStack(Items.apple, 1), true, false);
		}

		if (!worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			inventory.dropAllItems();
		}

		captureDrops = false;

		if (!worldObj.isRemote) {
			PlayerDropsEvent event = new PlayerDropsEvent(this, p_70645_1_, capturedDrops, recentlyHit > 0);
			if (!MinecraftForge.EVENT_BUS.post(event)) {
				for (EntityItem item : capturedDrops) {
					joinEntityItemWithWorld(item);
				}
			}
		}

		if (p_70645_1_ != null) {
			motionX = -MathHelper.cos((attackedAtYaw + rotationYaw) * (float) Math.PI / 180.0F) * 0.1F;
			motionZ = -MathHelper.sin((attackedAtYaw + rotationYaw) * (float) Math.PI / 180.0F) * 0.1F;
		} else {
			motionX = motionZ = 0.0D;
		}

		yOffset = 0.1F;
		addStat(StatList.deathsStat, 1);
	}

	@Override
	protected String getHurtSound() {
		return "game.player.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "game.player.die";
	}

	@Override
	public void addToPlayerScore(Entity p_70084_1_, int p_70084_2_) {
		addScore(p_70084_2_);
		Collection collection = getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.totalKillCount);

		if (p_70084_1_ instanceof EntityPlayer) {
			addStat(StatList.playerKillsStat, 1);
			collection.addAll(getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.playerKillCount));
		} else {
			addStat(StatList.mobKillsStat, 1);
		}

		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
			Score score = getWorldScoreboard().func_96529_a(getCommandSenderName(), scoreobjective);
			score.func_96648_a();
		}
	}

	public EntityItem dropOneItem(boolean p_71040_1_) {
		ItemStack stack = inventory.getCurrentItem();

		if (stack == null)
			return null;

		if (stack.getItem().onDroppedByPlayer(stack, this)) {
			int count = p_71040_1_ && inventory.getCurrentItem() != null ? inventory.getCurrentItem().stackSize : 1;
			return ForgeHooks.onPlayerTossEvent(this, inventory.decrStackSize(inventory.currentItem, count), true);
		}

		return null;
	}

	public EntityItem dropPlayerItemWithRandomChoice(ItemStack p_71019_1_, boolean p_71019_2_) {
		return ForgeHooks.onPlayerTossEvent(this, p_71019_1_, false);
	}

	public EntityItem func_146097_a(ItemStack p_146097_1_, boolean p_146097_2_, boolean p_146097_3_) {
		if (p_146097_1_ == null)
			return null;
		else if (p_146097_1_.stackSize == 0)
			return null;
		else {
			EntityItem entityitem = new EntityItem(worldObj, posX, posY - 0.30000001192092896D + getEyeHeight(), posZ,
					p_146097_1_);
			entityitem.delayBeforeCanPickup = 40;

			if (p_146097_3_) {
				entityitem.func_145799_b(getCommandSenderName());
			}

			float f = 0.1F;
			float f1;

			if (p_146097_2_) {
				f1 = rand.nextFloat() * 0.5F;
				float f2 = rand.nextFloat() * (float) Math.PI * 2.0F;
				entityitem.motionX = -MathHelper.sin(f2) * f1;
				entityitem.motionZ = MathHelper.cos(f2) * f1;
				entityitem.motionY = 0.20000000298023224D;
			} else {
				f = 0.3F;
				entityitem.motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
						* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f;
				entityitem.motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
						* MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * f;
				entityitem.motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI) * f + 0.1F;
				f = 0.02F;
				f1 = rand.nextFloat() * (float) Math.PI * 2.0F;
				f *= rand.nextFloat();
				entityitem.motionX += Math.cos(f1) * f;
				entityitem.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				entityitem.motionZ += Math.sin(f1) * f;
			}

			Player player = (Player) getBukkitEntity();
			CraftItem drop = new CraftItem((CraftServer) Bukkit.getServer(), entityitem);
			PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
			Bukkit.getServer().getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				// player.getInventory().addItem(drop.getItemStack());
				org.bukkit.inventory.ItemStack cur = player.getInventory().getItemInHand();
				if (p_146097_3_ && (cur == null || cur.getAmount() == 0)) {
					// The complete stack was dropped
					player.getInventory().setItemInHand(drop.getItemStack());
				} else if (p_146097_3_ && cur.isSimilar(drop.getItemStack()) && drop.getItemStack().getAmount() == 1) {
					// Only one item is dropped
					cur.setAmount(cur.getAmount() + 1);
					player.getInventory().setItemInHand(cur);
				} else {
					// Fallback
					player.getInventory().addItem(drop.getItemStack());
				}
				return null;
			}
			joinEntityItemWithWorld(entityitem);
			addStat(StatList.dropStat, 1);
			return entityitem;
		}
	}

	public boolean isSleeping() {
		return sleeping;
	}

	@Override
	public void setPassengerOf(Entity p_70078_1_) {
		// CraftBukkit end
		if (ridingEntity != null && p_70078_1_ == null) {
			Bukkit.getServer().getPluginManager().callEvent(new org.spigotmc.event.entity.EntityDismountEvent(
					getBukkitEntity(), ridingEntity.getBukkitEntity())); // Spigot
			// CraftBukkit start - use parent method instead to correctly fire
			// VehicleExitEvent
			Entity originalVehicle = ridingEntity;
			// First statement moved down, second statement handled in parent
			// method.
			/*
			 * if (!this.world.isStatic) { this.l(this.vehicle); }
			 *
			 * if (this.vehicle != null) { this.vehicle.passenger = null; }
			 *
			 * this.vehicle = null;
			 */
			super.setPassengerOf(null);

			if (!worldObj.isRemote && ridingEntity == null) {
				dismountEntity(originalVehicle);
			}
			// CraftBukkit end
		} else {
			super.setPassengerOf(p_70078_1_); // CraftBukkit - call new parent
		}
	}

	public void joinEntityItemWithWorld(EntityItem p_71012_1_) {
		if (captureDrops) {
			capturedDrops.add(p_71012_1_);
			return;
		}
		worldObj.spawnEntityInWorld(p_71012_1_);
	}

	@Deprecated // Metadata sensitive version, named getBreakSpeed
	public float getCurrentPlayerStrVsBlock(Block p_146096_1_, boolean p_146096_2_) {
		return getBreakSpeed(p_146096_1_, p_146096_2_, 0, 0, -1, 0);
	}

	@Deprecated // Location Specifc, one below, remove in 1.8
	public float getBreakSpeed(Block p_146096_1_, boolean p_146096_2_, int meta) {
		return getBreakSpeed(p_146096_1_, p_146096_2_, meta, 0, -1, 0);
	}

	public float getBreakSpeed(Block p_146096_1_, boolean p_146096_2_, int meta, int x, int y, int z) {
		ItemStack stack = inventory.getCurrentItem();
		float f = stack == null ? 1.0F : stack.getItem().getDigSpeed(stack, p_146096_1_, meta);

		if (f > 1.0F) {
			int i = EnchantmentHelper.getEfficiencyModifier(this);
			ItemStack itemstack = inventory.getCurrentItem();

			if (i > 0 && itemstack != null) {
				float f1 = i * i + 1;

				boolean canHarvest = ForgeHooks.canToolHarvestBlock(p_146096_1_, meta, itemstack);

				if (!canHarvest && f <= 1.0F) {
					f += f1 * 0.08F;
				} else {
					f += f1;
				}
			}
		}

		if (this.isPotionActive(Potion.digSpeed)) {
			f *= 1.0F + (getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
		}

		if (this.isPotionActive(Potion.digSlowdown)) {
			f *= 1.0F - (getActivePotionEffect(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
		}

		if (isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
			f /= 5.0F;
		}

		if (!onGround) {
			f /= 5.0F;
		}

		f = ForgeEventFactory.getBreakSpeed(this, p_146096_1_, meta, f, x, y, z);
		return f < 0 ? 0 : f;
	}

	public boolean canHarvestBlock(Block p_146099_1_) {
		return ForgeEventFactory.doPlayerHarvestCheck(this, p_146099_1_, inventory.func_146025_b(p_146099_1_));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		entityUniqueID = func_146094_a(field_146106_i);
		NBTTagList nbttaglist = p_70037_1_.getTagList("Inventory", 10);
		inventory.readFromNBT(nbttaglist);
		inventory.currentItem = p_70037_1_.getInteger("SelectedItemSlot");
		sleeping = p_70037_1_.getBoolean("Sleeping");
		sleepTimer = p_70037_1_.getShort("SleepTimer");
		experience = p_70037_1_.getFloat("XpP");
		experienceLevel = p_70037_1_.getInteger("XpLevel");
		experienceTotal = p_70037_1_.getInteger("XpTotal");
		setScore(p_70037_1_.getInteger("Score"));

		if (sleeping) {
			playerLocation = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ));
			wakeUpPlayer(true, true, false);
		}

		if (p_70037_1_.hasKey("SpawnX", 99) && p_70037_1_.hasKey("SpawnY", 99) && p_70037_1_.hasKey("SpawnZ", 99)) {
			spawnChunk = new ChunkCoordinates(p_70037_1_.getInteger("SpawnX"), p_70037_1_.getInteger("SpawnY"),
					p_70037_1_.getInteger("SpawnZ"));
			spawnForced = p_70037_1_.getBoolean("SpawnForced");
		}

		NBTTagList spawnlist = null;
		spawnlist = p_70037_1_.getTagList("Spawns", 10);
		for (int i = 0; i < spawnlist.tagCount(); i++) {
			NBTTagCompound spawndata = spawnlist.getCompoundTagAt(i);
			int spawndim = spawndata.getInteger("Dim");
			spawnChunkMap.put(spawndim, new ChunkCoordinates(spawndata.getInteger("SpawnX"),
					spawndata.getInteger("SpawnY"), spawndata.getInteger("SpawnZ")));
			spawnForcedMap.put(spawndim, spawndata.getBoolean("SpawnForced"));
		}

		foodStats.readNBT(p_70037_1_);
		capabilities.readCapabilitiesFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("EnderItems", 9)) {
			NBTTagList nbttaglist1 = p_70037_1_.getTagList("EnderItems", 10);
			theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
		p_70014_1_.setInteger("SelectedItemSlot", inventory.currentItem);
		p_70014_1_.setBoolean("Sleeping", sleeping);
		p_70014_1_.setShort("SleepTimer", (short) sleepTimer);
		p_70014_1_.setFloat("XpP", experience);
		p_70014_1_.setInteger("XpLevel", experienceLevel);
		p_70014_1_.setInteger("XpTotal", experienceTotal);
		p_70014_1_.setInteger("Score", getScore());

		if (spawnChunk != null) {
			p_70014_1_.setInteger("SpawnX", spawnChunk.posX);
			p_70014_1_.setInteger("SpawnY", spawnChunk.posY);
			p_70014_1_.setInteger("SpawnZ", spawnChunk.posZ);
			p_70014_1_.setBoolean("SpawnForced", spawnForced);
		}

		NBTTagList spawnlist = new NBTTagList();
		for (Entry<Integer, ChunkCoordinates> entry : spawnChunkMap.entrySet()) {
			ChunkCoordinates spawn = entry.getValue();
			if (spawn == null) {
				continue;
			}
			Boolean forced = spawnForcedMap.get(entry.getKey());
			if (forced == null) {
				forced = false;
			}
			NBTTagCompound spawndata = new NBTTagCompound();
			spawndata.setInteger("Dim", entry.getKey());
			spawndata.setInteger("SpawnX", spawn.posX);
			spawndata.setInteger("SpawnY", spawn.posY);
			spawndata.setInteger("SpawnZ", spawn.posZ);
			spawndata.setBoolean("SpawnForced", forced);
			spawnlist.appendTag(spawndata);
		}
		p_70014_1_.setTag("Spawns", spawnlist);

		foodStats.writeNBT(p_70014_1_);
		capabilities.writeCapabilitiesToNBT(p_70014_1_);
		p_70014_1_.setTag("EnderItems", theInventoryEnderChest.saveInventoryToNBT());
	}

	public void displayGUIChest(IInventory p_71007_1_) {
	}

	public void func_146093_a(TileEntityHopper p_146093_1_) {
	}

	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
	}

	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
	}

	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_) {
	}

	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
	}

	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_) {
	}

	@Override
	public float getEyeHeight() {
		return eyeHeight;
	}

	protected void resetHeight() {
		yOffset = 1.62F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (ForgeHooks.onLivingAttack(this, p_70097_1_, p_70097_2_))
			return false;
		if (isEntityInvulnerable())
			return false;
		else if (capabilities.disableDamage && !p_70097_1_.canHarmInCreative())
			return false;
		else {
			entityAge = 0;

			if (getHealth() <= 0.0F)
				return false;
			else {
				if (isPlayerSleeping() && !worldObj.isRemote) {
					wakeUpPlayer(true, true, false);
				}

				if (p_70097_1_.isDifficultyScaled()) {
					if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
						p_70097_2_ = 0.0F;
					}

					if (worldObj.difficultySetting == EnumDifficulty.EASY) {
						p_70097_2_ = p_70097_2_ / 2.0F + 1.0F;
					}

					if (worldObj.difficultySetting == EnumDifficulty.HARD) {
						p_70097_2_ = p_70097_2_ * 3.0F / 2.0F;
					}
				}

				if (p_70097_2_ == 0.0F)
					return false;
				else {
					Entity entity = p_70097_1_.getEntity();

					if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity != null) {
						entity = ((EntityArrow) entity).shootingEntity;
					}

					addStat(StatList.damageTakenStat, Math.round(p_70097_2_ * 10.0F));
					return super.attackEntityFrom(p_70097_1_, p_70097_2_);
				}
			}
		}
	}

	public boolean canAttackPlayer(EntityPlayer p_96122_1_) {
		Team team = getTeam();
		Team team1 = p_96122_1_.getTeam();
		return team == null ? true : !team.isSameTeam(team1) ? true : team.getAllowFriendlyFire();
	}

	@Override
	protected void damageArmor(float p_70675_1_) {
		inventory.damageArmor(p_70675_1_);
	}

	@Override
	public int getTotalArmorValue() {
		return inventory.getTotalArmorValue();
	}

	public float getArmorVisibility() {
		int i = 0;
		ItemStack[] aitemstack = inventory.armorInventory;
		int j = aitemstack.length;

		for (int k = 0; k < j; ++k) {
			ItemStack itemstack = aitemstack[k];

			if (itemstack != null) {
				++i;
			}
		}

		return (float) i / (float) inventory.armorInventory.length;
	}

	@Override
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
		super.damageEntity(p_70665_1_, p_70665_2_);
	}

	public void func_146101_a(TileEntityFurnace p_146101_1_) {
	}

	public void func_146102_a(TileEntityDispenser p_146102_1_) {
	}

	public void func_146100_a(TileEntity p_146100_1_) {
	}

	public void func_146095_a(CommandBlockLogic p_146095_1_) {
	}

	public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
	}

	public void func_146104_a(TileEntityBeacon p_146104_1_) {
	}

	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
	}

	public void displayGUIBook(ItemStack p_71048_1_) {
	}

	public boolean interactWith(Entity p_70998_1_) {
		if (MinecraftForge.EVENT_BUS.post(new EntityInteractEvent(this, p_70998_1_)))
			return false;
		ItemStack itemstack = getCurrentEquippedItem();
		ItemStack itemstack1 = itemstack != null ? itemstack.copy() : null;

		if (!p_70998_1_.interactFirst(this)) {
			if (itemstack != null && p_70998_1_ instanceof EntityLivingBase) {
				if (capabilities.isCreativeMode) {
					itemstack = itemstack1;
				}

				if (itemstack.interactWithEntity(this, (EntityLivingBase) p_70998_1_)) {
					if (itemstack.stackSize <= 0 && !capabilities.isCreativeMode) {
						destroyCurrentEquippedItem();
					}

					return true;
				}
			}

			return false;
		} else {
			if (itemstack != null && itemstack == getCurrentEquippedItem()) {
				if (itemstack.stackSize <= 0 && !capabilities.isCreativeMode) {
					destroyCurrentEquippedItem();
				} else if (itemstack.stackSize < itemstack1.stackSize && capabilities.isCreativeMode) {
					itemstack.stackSize = itemstack1.stackSize;
				}
			}

			return true;
		}
	}

	public ItemStack getCurrentEquippedItem() {
		return inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		ItemStack orig = getCurrentEquippedItem();
		inventory.setInventorySlotContents(inventory.currentItem, (ItemStack) null);
		MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this, orig));
	}

	@Override
	public double getYOffset() {
		return yOffset - 0.5F;
	}

	public void attackTargetEntityWithCurrentItem(Entity p_71059_1_) {
		if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(this, p_71059_1_)))
			return;
		ItemStack stack = getCurrentEquippedItem();
		if (stack != null && stack.getItem().onLeftClickEntity(stack, this, p_71059_1_))
			return;
		if (p_71059_1_.canAttackWithItem()) {
			if (!p_71059_1_.hitByEntity(this)) {
				float f = (float) getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
				int i = 0;
				float f1 = 0.0F;

				if (p_71059_1_ instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) p_71059_1_);
					i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) p_71059_1_);
				}

				if (isSprinting()) {
					++i;
				}

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = fallDistance > 0.0F && !onGround && !isOnLadder() && !isInWater()
							&& !this.isPotionActive(Potion.blindness) && ridingEntity == null
							&& p_71059_1_ instanceof EntityLivingBase;

					if (flag && f > 0.0F) {
						f *= 1.5F;
					}

					f += f1;
					boolean flag1 = false;
					int j = EnchantmentHelper.getFireAspectModifier(this);

					if (p_71059_1_ instanceof EntityLivingBase && j > 0 && !p_71059_1_.isBurning()) {
						flag1 = true;
						p_71059_1_.setFire(1);
					}

					boolean flag2 = p_71059_1_.attackEntityFrom(DamageSource.causePlayerDamage(this), f);

					if (flag2) {
						if (i > 0) {
							p_71059_1_.addVelocity(-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F,
									0.1D, MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F) * i * 0.5F);
							motionX *= 0.6D;
							motionZ *= 0.6D;
							setSprinting(false);
						}

						if (flag) {
							onCriticalHit(p_71059_1_);
						}

						if (f1 > 0.0F) {
							onEnchantmentCritical(p_71059_1_);
						}

						if (f >= 18.0F) {
							triggerAchievement(AchievementList.overkill);
						}

						setLastAttacker(p_71059_1_);

						if (p_71059_1_ instanceof EntityLivingBase) {
							EnchantmentHelper.func_151384_a((EntityLivingBase) p_71059_1_, this);
						}

						EnchantmentHelper.func_151385_b(this, p_71059_1_);
						ItemStack itemstack = getCurrentEquippedItem();
						Object object = p_71059_1_;

						if (p_71059_1_ instanceof EntityDragonPart) {
							IEntityMultiPart ientitymultipart = ((EntityDragonPart) p_71059_1_).entityDragonObj;

							if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase) {
								object = ientitymultipart;
							}
						}

						if (itemstack != null && object instanceof EntityLivingBase) {
							itemstack.hitEntity((EntityLivingBase) object, this);

							if (itemstack.stackSize <= 0) {
								destroyCurrentEquippedItem();
							}
						}

						if (p_71059_1_ instanceof EntityLivingBase) {
							addStat(StatList.damageDealtStat, Math.round(f * 10.0F));

							if (j > 0) {
								p_71059_1_.setFire(j * 4);
							}
						}

						addExhaustion(0.3F);
					} else if (flag1) {
						p_71059_1_.extinguish();
					}
				}
			}
		}
	}

	public void onCriticalHit(Entity p_71009_1_) {
	}

	public void onEnchantmentCritical(Entity p_71047_1_) {
	}

	@SideOnly(Side.CLIENT)
	public void respawnPlayer() {
	}

	@Override
	public void setDead() {
		super.setDead();
		inventoryContainer.onContainerClosed(this);

		if (openContainer != null) {
			InventoryCloseEvent event = new InventoryCloseEvent(this.openContainer.getBukkitView());
			if (this.openContainer.getBukkitView() != null) {
				Bukkit.getServer().getPluginManager().callEvent(event); // Cauldron - allow vanilla mods to bypass
			}
			openContainer.onContainerClosed(this);
		}
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return !sleeping && super.isEntityInsideOpaqueBlock();
	}

	public GameProfile getGameProfile() {
		return field_146106_i;
	}

	public EntityPlayer.EnumStatus sleepInBedAt(int p_71018_1_, int p_71018_2_, int p_71018_3_) {
		PlayerSleepInBedEvent event = new PlayerSleepInBedEvent(this, p_71018_1_, p_71018_2_, p_71018_3_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.result != null)
			return event.result;
		if (!worldObj.isRemote) {
			if (isPlayerSleeping() || !isEntityAlive())
				return EntityPlayer.EnumStatus.OTHER_PROBLEM;

			if (!worldObj.provider.isSurfaceWorld())
				return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;

			if (worldObj.isDaytime())
				return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;

			if (Math.abs(posX - p_71018_1_) > 3.0D || Math.abs(posY - p_71018_2_) > 2.0D
					|| Math.abs(posZ - p_71018_3_) > 3.0D)
				return EntityPlayer.EnumStatus.TOO_FAR_AWAY;

			double d0 = 8.0D;
			double d1 = 5.0D;
			List list = worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(p_71018_1_ - d0,
					p_71018_2_ - d1, p_71018_3_ - d0, p_71018_1_ + d0, p_71018_2_ + d1, p_71018_3_ + d0));

			if (!list.isEmpty())
				return EntityPlayer.EnumStatus.NOT_SAFE;
		}

		if (isRiding()) {
			mountEntity((Entity) null);
			if (getBukkitEntity() instanceof Player) {
				org.bukkit.block.Block bedBlock = worldObj.getWorld().getBlockAt(p_71018_1_, p_71018_2_, p_71018_3_);
				PlayerBedEnterEvent tevent = new PlayerBedEnterEvent((Player) getBukkitEntity(), bedBlock);
				worldObj.getServer().getPluginManager().callEvent(tevent);
				if (tevent.isCancelled())
					return EntityPlayer.EnumStatus.OTHER_PROBLEM;
			}
		}

		setSize(0.2F, 0.2F);
		yOffset = 0.2F;

		if (worldObj.blockExists(p_71018_1_, p_71018_2_, p_71018_3_)) {
			int l = worldObj.getBlock(p_71018_1_, p_71018_2_, p_71018_3_).getBedDirection(worldObj, p_71018_1_,
					p_71018_2_, p_71018_3_);
			float f1 = 0.5F;
			float f = 0.5F;

			switch (l) {
			case 0:
				f = 0.9F;
				break;
			case 1:
				f1 = 0.1F;
				break;
			case 2:
				f = 0.1F;
				break;
			case 3:
				f1 = 0.9F;
			}

			func_71013_b(l);
			setPosition(p_71018_1_ + f1, p_71018_2_ + 0.9375F, p_71018_3_ + f);
		} else {
			setPosition(p_71018_1_ + 0.5F, p_71018_2_ + 0.9375F, p_71018_3_ + 0.5F);
		}

		sleeping = true;
		sleepTimer = 0;
		playerLocation = new ChunkCoordinates(p_71018_1_, p_71018_2_, p_71018_3_);
		motionX = motionZ = motionY = 0.0D;

		if (!worldObj.isRemote) {
			worldObj.updateAllPlayersSleepingFlag();
		}

		return EntityPlayer.EnumStatus.OK;
	}

	private void func_71013_b(int p_71013_1_) {
		field_71079_bU = 0.0F;
		field_71089_bV = 0.0F;

		switch (p_71013_1_) {
		case 0:
			field_71089_bV = -1.8F;
			break;
		case 1:
			field_71079_bU = 1.8F;
			break;
		case 2:
			field_71089_bV = 1.8F;
			break;
		case 3:
			field_71079_bU = -1.8F;
		}
	}

	public void wakeUpPlayer(boolean p_70999_1_, boolean p_70999_2_, boolean p_70999_3_) {
		MinecraftForge.EVENT_BUS.post(
				new net.minecraftforge.event.entity.player.PlayerWakeUpEvent(this, p_70999_1_, p_70999_2_, p_70999_3_));
		setSize(0.6F, 1.8F);
		resetHeight();
		ChunkCoordinates chunkcoordinates = playerLocation;
		ChunkCoordinates chunkcoordinates1 = playerLocation;
		Block block = chunkcoordinates == null ? null
				: worldObj.getBlock(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);

		if (chunkcoordinates != null
				&& block.isBed(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, this)) {
			block.setBedOccupied(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, this,
					false);
			chunkcoordinates1 = block.getBedSpawnPosition(worldObj, chunkcoordinates.posX, chunkcoordinates.posY,
					chunkcoordinates.posZ, this);

			if (chunkcoordinates1 == null) {
				chunkcoordinates1 = new ChunkCoordinates(chunkcoordinates.posX, chunkcoordinates.posY + 1,
						chunkcoordinates.posZ);
			}

			setPosition(chunkcoordinates1.posX + 0.5F, chunkcoordinates1.posY + yOffset + 0.1F,
					chunkcoordinates1.posZ + 0.5F);
		}

		sleeping = false;

		if (!worldObj.isRemote && p_70999_2_) {
			worldObj.updateAllPlayersSleepingFlag();
		}

		if (this.getBukkitEntity() instanceof Player) {
			Player player = (Player) this.getBukkitEntity();
			org.bukkit.block.Block bed;

			if (chunkcoordinates != null) {
				bed = this.worldObj.getWorld().getBlockAt(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ);
			} else {
				bed = this.worldObj.getWorld().getBlockAt(player.getLocation());
			}

			PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
			this.worldObj.getServer().getPluginManager().callEvent(event);
		}

		if (p_70999_1_) {
			sleepTimer = 0;
		} else {
			sleepTimer = 100;
		}

		if (p_70999_3_) {
			this.setSpawnChunk(playerLocation, false);
		}
	}

	private boolean isInBed() {
		return worldObj.getBlock(playerLocation.posX, playerLocation.posY, playerLocation.posZ).isBed(worldObj,
				playerLocation.posX, playerLocation.posY, playerLocation.posZ, this);
	}

	public static ChunkCoordinates verifyRespawnCoordinates(World p_71056_0_, ChunkCoordinates p_71056_1_,
			boolean p_71056_2_) {
		IChunkProvider ichunkprovider = p_71056_0_.getChunkProvider();
		ichunkprovider.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ - 3 >> 4);
		ichunkprovider.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ - 3 >> 4);
		ichunkprovider.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ + 3 >> 4);
		ichunkprovider.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ + 3 >> 4);

		if (p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ).isBed(p_71056_0_, p_71056_1_.posX,
				p_71056_1_.posY, p_71056_1_.posZ, null)) {
			ChunkCoordinates chunkcoordinates1 = p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ)
					.getBedSpawnPosition(p_71056_0_, p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ, null);
			return chunkcoordinates1;
		} else {
			Material material = p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ).getMaterial();
			Material material1 = p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY + 1, p_71056_1_.posZ)
					.getMaterial();
			boolean flag1 = !material.isSolid() && !material.isLiquid();
			boolean flag2 = !material1.isSolid() && !material1.isLiquid();
			return p_71056_2_ && flag1 && flag2 ? p_71056_1_ : null;
		}
	}

	@SideOnly(Side.CLIENT)
	public float getBedOrientationInDegrees() {
		if (playerLocation != null) {
			int x = playerLocation.posX;
			int y = playerLocation.posY;
			int z = playerLocation.posZ;
			int j = worldObj.getBlock(x, y, z).getBedDirection(worldObj, x, y, z);

			switch (j) {
			case 0:
				return 90.0F;
			case 1:
				return 0.0F;
			case 2:
				return 270.0F;
			case 3:
				return 180.0F;
			}
		}

		return 0.0F;
	}

	@Override
	public boolean isPlayerSleeping() {
		return sleeping;
	}

	public boolean isPlayerFullyAsleep() {
		return sleeping && sleepTimer >= 100;
	}

	public int getSleepTimer() {
		return sleepTimer;
	}

	@SideOnly(Side.CLIENT)
	protected boolean getHideCape(int p_82241_1_) {
		return (dataWatcher.getWatchableObjectByte(16) & 1 << p_82241_1_) != 0;
	}

	protected void setHideCape(int p_82239_1_, boolean p_82239_2_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_82239_2_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1 << p_82239_1_)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & ~(1 << p_82239_1_))));
		}
	}

	public void addChatComponentMessage(IChatComponent p_146105_1_) {
	}

	@Deprecated
	public ChunkCoordinates getBedLocation() {
		return getBedLocation(dimension);
	}

	@Deprecated
	public boolean isSpawnForced() {
		return isSpawnForced(dimension);
	}

	public void setSpawnChunk(ChunkCoordinates p_71063_1_, boolean p_71063_2_) {
		if (dimension != 0) {
			setSpawnChunk(p_71063_1_, p_71063_2_, dimension);
			return;
		}
		if (p_71063_1_ != null) {
			spawnChunk = new ChunkCoordinates(p_71063_1_);
			spawnForced = p_71063_2_;
		} else {
			spawnChunk = null;
			spawnForced = false;
		}
	}

	public void triggerAchievement(StatBase p_71029_1_) {
		addStat(p_71029_1_, 1);
	}

	public void addStat(StatBase p_71064_1_, int p_71064_2_) {
	}

	@Override
	public void jump() {
		super.jump();
		addStat(StatList.jumpStat, 1);

		if (isSprinting()) {
			addExhaustion(0.8F);
		} else {
			addExhaustion(0.2F);
		}
	}

	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		double d0 = posX;
		double d1 = posY;
		double d2 = posZ;

		if (capabilities.isFlying && ridingEntity == null) {
			double d3 = motionY;
			float f2 = jumpMovementFactor;
			jumpMovementFactor = capabilities.getFlySpeed();
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			motionY = d3 * 0.6D;
			jumpMovementFactor = f2;
		} else {
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}

		addMovementStat(posX - d0, posY - d1, posZ - d2);
	}

	@Override
	public float getAIMoveSpeed() {
		return (float) getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
	}

	public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
		if (ridingEntity == null) {
			int i;

			if (isInsideOfMaterial(Material.water)) {
				i = Math.round(MathHelper.sqrt_double(
						p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (i > 0) {
					addStat(StatList.distanceDoveStat, i);
					addExhaustion(0.015F * i * 0.01F);
				}
			} else if (isInWater()) {
				i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (i > 0) {
					addStat(StatList.distanceSwumStat, i);
					addExhaustion(0.015F * i * 0.01F);
				}
			} else if (isOnLadder()) {
				if (p_71000_3_ > 0.0D) {
					addStat(StatList.distanceClimbedStat, (int) Math.round(p_71000_3_ * 100.0D));
				}
			} else if (onGround) {
				i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (i > 0) {
					addStat(StatList.distanceWalkedStat, i);

					if (isSprinting()) {
						addExhaustion(0.099999994F * i * 0.01F);
					} else {
						addExhaustion(0.01F * i * 0.01F);
					}
				}
			} else {
				i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (i > 25) {
					addStat(StatList.distanceFlownStat, i);
				}
			}
		}
	}

	private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
		if (ridingEntity != null) {
			int i = Math.round(
					MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_)
							* 100.0F);

			if (i > 0) {
				if (ridingEntity instanceof EntityMinecart) {
					addStat(StatList.distanceByMinecartStat, i);

					if (startMinecartRidingCoordinate == null) {
						startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor_double(posX),
								MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
					} else if (startMinecartRidingCoordinate.getDistanceSquared(MathHelper.floor_double(posX),
							MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) >= 1000000.0D) {
						addStat(AchievementList.onARail, 1);
					}
				} else if (ridingEntity instanceof EntityBoat) {
					addStat(StatList.distanceByBoatStat, i);
				} else if (ridingEntity instanceof EntityPig) {
					addStat(StatList.distanceByPigStat, i);
				} else if (ridingEntity instanceof EntityHorse) {
					addStat(StatList.field_151185_q, i);
				}
			}
		}
	}

	@Override
	protected void fall(float p_70069_1_) {
		if (!capabilities.allowFlying) {
			if (p_70069_1_ >= 2.0F) {
				addStat(StatList.distanceFallenStat, (int) Math.round(p_70069_1_ * 100.0D));
			}

			super.fall(p_70069_1_);
		} else {
			MinecraftForge.EVENT_BUS.post(new PlayerFlyableFallEvent(this, p_70069_1_));
		}
	}

	@Override
	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
	}

	@Override
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		if (p_70074_1_ instanceof IMob) {
			triggerAchievement(AchievementList.killEnemy);
		}

		int i = EntityList.getEntityID(p_70074_1_);
		EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo) EntityList.entityEggs
				.get(Integer.valueOf(i));

		if (entityegginfo != null) {
			addStat(entityegginfo.field_151512_d, 1);
		}
	}

	@Override
	public void setInWeb() {
		if (!capabilities.isFlying) {
			super.setInWeb();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getItemIcon(ItemStack p_70620_1_, int p_70620_2_) {
		IIcon iicon = super.getItemIcon(p_70620_1_, p_70620_2_);

		if (p_70620_1_.getItem() == Items.fishing_rod && fishEntity != null) {
			iicon = Items.fishing_rod.func_94597_g();
		} else {
			if (itemInUse != null && p_70620_1_.getItem() == Items.bow) {
				int j = p_70620_1_.getMaxItemUseDuration() - itemInUseCount;

				if (j >= 18)
					return Items.bow.getItemIconForUseDuration(2);

				if (j > 13)
					return Items.bow.getItemIconForUseDuration(1);

				if (j > 0)
					return Items.bow.getItemIconForUseDuration(0);
			}
			iicon = p_70620_1_.getItem().getIcon(p_70620_1_, p_70620_2_, this, itemInUse, itemInUseCount);
		}

		return iicon;
	}

	public ItemStack getCurrentArmor(int p_82169_1_) {
		return inventory.armorItemInSlot(p_82169_1_);
	}

	public void addExperience(int p_71023_1_) {
		addScore(p_71023_1_);
		int j = Integer.MAX_VALUE - experienceTotal;

		if (p_71023_1_ > j) {
			p_71023_1_ = j;
		}

		experience += (float) p_71023_1_ / (float) xpBarCap();

		for (experienceTotal += p_71023_1_; experience >= 1.0F; experience /= xpBarCap()) {
			experience = (experience - 1.0F) * xpBarCap();
			addExperienceLevel(1);
		}
	}

	public void addExperienceLevel(int p_82242_1_) {
		if (p_82242_1_ < 0 && true) {
			int xp = MinecraftUtil.countXPCostForLevel(experienceLevel);
			xp += (MinecraftUtil.countXPCostForLevel(experienceLevel + 1) - xp) * experience;
			xp -= MinecraftUtil.countXPCostForLevel(-p_82242_1_);
			experienceLevel = 0;
			experience = 0.0F;
			experienceTotal = 0;
			if (xp > 0) {
				addExperience(xp);
			}
			return;
		}

		experienceLevel += p_82242_1_;

		if (experienceLevel < 0) {
			experienceLevel = 0;
			experience = 0.0F;
			experienceTotal = 0;
		}

		if (p_82242_1_ > 0 && experienceLevel % 5 == 0 && field_82249_h < ticksExisted - 100.0F) {
			float f = experienceLevel > 30 ? 1.0F : experienceLevel / 30.0F;
			worldObj.playSoundAtEntity(this, "random.levelup", f * 0.75F, 1.0F);
			field_82249_h = ticksExisted;
		}
	}

	public int xpBarCap() {
		return experienceLevel >= 30 ? 62 + (experienceLevel - 30) * 7
				: experienceLevel >= 15 ? 17 + (experienceLevel - 15) * 3 : 17;
	}

	public void addExhaustion(float p_71020_1_) {
		if (!capabilities.disableDamage) {
			if (!worldObj.isRemote) {
				foodStats.addExhaustion(p_71020_1_);
			}
		}
	}

	public FoodStats getFoodStats() {
		return foodStats;
	}

	public boolean canEat(boolean p_71043_1_) {
		return (p_71043_1_ || foodStats.needFood()) && !capabilities.disableDamage;
	}

	public boolean shouldHeal() {
		return getHealth() > 0.0F && getHealth() < getMaxHealth();
	}

	public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_) {
		if (p_71008_1_ != itemInUse) {
			p_71008_2_ = ForgeEventFactory.onItemUseStart(this, p_71008_1_, p_71008_2_);
			if (p_71008_2_ <= 0)
				return;
			itemInUse = p_71008_1_;
			itemInUseCount = p_71008_2_;

			if (!worldObj.isRemote) {
				setEating(true);
			}
		}
	}

	public boolean isCurrentToolAdventureModeExempt(int p_82246_1_, int p_82246_2_, int p_82246_3_) {
		if (capabilities.allowEdit)
			return true;
		else {
			Block block = worldObj.getBlock(p_82246_1_, p_82246_2_, p_82246_3_);

			if (block.getMaterial() != Material.air) {
				if (block.getMaterial().isAdventureModeExempt())
					return true;

				if (getCurrentEquippedItem() != null) {
					ItemStack itemstack = getCurrentEquippedItem();

					if (itemstack.func_150998_b(block) || itemstack.func_150997_a(block) > 1.0F)
						return true;
				}
			}

			return false;
		}
	}

	public boolean canPlayerEdit(int p_82247_1_, int p_82247_2_, int p_82247_3_, int p_82247_4_, ItemStack p_82247_5_) {
		return capabilities.allowEdit ? true : p_82247_5_ != null ? p_82247_5_.canEditBlocks() : false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (((EntityPlayerMP) this).isKeepLevel())
			return 0;
		else {
			int i = experienceLevel * 7;
			return i > 100 ? 100 : i;
		}
	}

	@Override
	protected boolean isPlayer() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender() {
		return true;
	}

	public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_) {
		if (p_71049_2_) {
			inventory.copyInventory(p_71049_1_.inventory);
			setHealth(p_71049_1_.getHealth());
			foodStats = p_71049_1_.foodStats;
			experienceLevel = p_71049_1_.experienceLevel;
			experienceTotal = p_71049_1_.experienceTotal;
			experience = p_71049_1_.experience;
			setScore(p_71049_1_.getScore());
			teleportDirection = p_71049_1_.teleportDirection;
			// Copy and re-init ExtendedProperties when switching dimensions.
			extendedProperties = p_71049_1_.extendedProperties;
			for (net.minecraftforge.common.IExtendedEntityProperties p : extendedProperties.values()) {
				p.init(this, worldObj);
			}
		} else if (p_71049_1_.keepInventoryOnClone) {
			inventory.copyInventory(p_71049_1_.inventory);
			experienceLevel = p_71049_1_.experienceLevel;
			experienceTotal = p_71049_1_.experienceTotal;
			experience = p_71049_1_.experience;
			setScore(p_71049_1_.getScore());
		}

		theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;

		spawnChunkMap = p_71049_1_.spawnChunkMap;
		spawnForcedMap = p_71049_1_.spawnForcedMap;

		// Copy over a section of the Entity Data from the old player.
		// Allows mods to specify data that persists after players respawn.
		NBTTagCompound old = p_71049_1_.getEntityData();
		if (old.hasKey(PERSISTED_NBT_TAG)) {
			getEntityData().setTag(PERSISTED_NBT_TAG, old.getCompoundTag(PERSISTED_NBT_TAG));
		}
		MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.player.PlayerEvent.Clone(this, p_71049_1_, !p_71049_2_));
	}

	@Override
	protected boolean canTriggerWalking() {
		return !capabilities.isFlying;
	}

	public void sendPlayerAbilities() {
	}

	public void setGameType(WorldSettings.GameType p_71033_1_) {
	}

	@Override
	public String getCommandSenderName() {
		return field_146106_i.getName();
	}

	@Override
	public World getEntityWorld() {
		return worldObj;
	}

	public InventoryEnderChest getInventoryEnderChest() {
		return theInventoryEnderChest;
	}

	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return p_71124_1_ == 0 ? inventory.getCurrentItem() : inventory.armorInventory[p_71124_1_ - 1];
	}

	@Override
	public ItemStack getHeldItem() {
		return inventory.getCurrentItem();
	}

	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		if (p_70062_1_ == 0) {
			inventory.mainInventory[inventory.currentItem] = p_70062_2_;
		} else {
			inventory.armorInventory[p_70062_1_ - 1] = p_70062_2_;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInvisibleToPlayer(EntityPlayer p_98034_1_) {
		if (!isInvisible())
			return false;
		else {
			Team team = getTeam();
			return team == null || p_98034_1_ == null || p_98034_1_.getTeam() != team || !team.func_98297_h();
		}
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return inventory.armorInventory;
	}

	@SideOnly(Side.CLIENT)
	public boolean getHideCape() {
		return this.getHideCape(1);
	}

	@Override
	public boolean isPushedByWater() {
		return !capabilities.isFlying;
	}

	public Scoreboard getWorldScoreboard() {
		return worldObj.getScoreboard();
	}

	@Override
	public Team getTeam() {
		return getWorldScoreboard().getPlayersTeam(getCommandSenderName());
	}

	@Override
	public IChatComponent func_145748_c_() {
		ChatComponentText chatcomponenttext = new ChatComponentText(
				ScorePlayerTeam.formatPlayerName(getTeam(), getDisplayName()));
		chatcomponenttext.getChatStyle().setChatClickEvent(
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getCommandSenderName() + " "));
		return chatcomponenttext;
	}

	@Override
	public void setAbsorptionAmount(float p_110149_1_) {
		if (p_110149_1_ < 0.0F) {
			p_110149_1_ = 0.0F;
		}

		getDataWatcher().updateObject(17, Float.valueOf(p_110149_1_));
	}

	@Override
	public float getAbsorptionAmount() {
		return getDataWatcher().getWatchableObjectFloat(17);
	}

	public static UUID func_146094_a(GameProfile p_146094_0_) {
		UUID uuid = p_146094_0_.getId();

		if (uuid == null) {
			uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_146094_0_.getName()).getBytes(Charsets.UTF_8));
		}

		return uuid;
	}

	public static enum EnumChatVisibility {
		FULL(0, "options.chat.visibility.full"), SYSTEM(1, "options.chat.visibility.system"), HIDDEN(2,
				"options.chat.visibility.hidden");
		private static final EntityPlayer.EnumChatVisibility[] field_151432_d = new EntityPlayer.EnumChatVisibility[values().length];
		private final int chatVisibility;
		private final String resourceKey;

		private static final String __OBFID = "CL_00001714";

		private EnumChatVisibility(int p_i45323_3_, String p_i45323_4_) {
			chatVisibility = p_i45323_3_;
			resourceKey = p_i45323_4_;
		}

		public int getChatVisibility() {
			return chatVisibility;
		}

		public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(int p_151426_0_) {
			return field_151432_d[p_151426_0_ % field_151432_d.length];
		}

		@SideOnly(Side.CLIENT)
		public String getResourceKey() {
			return resourceKey;
		}

		static {
			EntityPlayer.EnumChatVisibility[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				EntityPlayer.EnumChatVisibility var3 = var0[var2];
				field_151432_d[var3.chatVisibility] = var3;
			}
		}
	}

	public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
		FMLNetworkHandler.openGui(this, mod, modGuiId, world, x, y, z);
	}

	/*
	 * ======================================== FORGE START
	 * =====================================
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getPosition(float par1) {
		if (par1 == 1.0F)
			return Vec3.createVectorHelper(posX, posY + (getEyeHeight() - getDefaultEyeHeight()), posZ);
		else {
			double d0 = prevPosX + (posX - prevPosX) * par1;
			double d1 = prevPosY + (posY - prevPosY) * par1 + (getEyeHeight() - getDefaultEyeHeight());
			double d2 = prevPosZ + (posZ - prevPosZ) * par1;
			return Vec3.createVectorHelper(d0, d1, d2);
		}
	}

	/**
	 * A dimension aware version of getBedLocation.
	 *
	 * @param dimension
	 *            The dimension to get the bed spawn for
	 * @return The player specific spawn location for the dimension. May be null.
	 */
	public ChunkCoordinates getBedLocation(int dimension) {
		return dimension == 0 ? spawnChunk : spawnChunkMap.get(dimension);
	}

	/**
	 * A dimension aware version of isSpawnForced. Noramally isSpawnForced is used
	 * to determine if the respawn system should check for a bed or not. This just
	 * extends that to be dimension aware.
	 *
	 * @param dimension
	 *            The dimension to get whether to check for a bed before spawning
	 *            for
	 * @return The player specific spawn location for the dimension. May be null.
	 */
	public boolean isSpawnForced(int dimension) {
		if (dimension == 0)
			return spawnForced;
		Boolean forced = spawnForcedMap.get(dimension);
		return forced == null ? false : forced;
	}

	/**
	 * A dimension aware version of setSpawnChunk. This functions identically, but
	 * allows you to specify which dimension to affect, rather than affecting the
	 * player's current dimension.
	 *
	 * @param chunkCoordinates
	 *            The spawn point to set as the player-specific spawn point for the
	 *            dimension
	 * @param forced
	 *            Whether or not the respawn code should check for a bed at this
	 *            location (true means it won't check for a bed)
	 * @param dimension
	 *            Which dimension to apply the player-specific respawn point to
	 */
	public void setSpawnChunk(ChunkCoordinates chunkCoordinates, boolean forced, int dimension) {
		if (dimension == 0) {
			if (chunkCoordinates != null) {
				spawnChunk = new ChunkCoordinates(chunkCoordinates);
				spawnForced = forced;
			} else {
				spawnChunk = null;
				spawnForced = false;
			}
			return;
		}

		if (chunkCoordinates != null) {
			spawnChunkMap.put(dimension, new ChunkCoordinates(chunkCoordinates));
			spawnForcedMap.put(dimension, forced);
		} else {
			spawnChunkMap.remove(dimension);
			spawnForcedMap.remove(dimension);
		}
	}

	public float eyeHeight;
	private String displayname;

	/**
	 * Returns the default eye height of the player
	 *
	 * @return player default eye height
	 */
	public float getDefaultEyeHeight() {
		return 0.12F;
	}

	/**
	 * Get the currently computed display name, cached for efficiency.
	 *
	 * @return the current display name
	 */
	public String getDisplayName() {
		if (displayname == null) {
			displayname = ForgeEventFactory.getPlayerDisplayName(this, getCommandSenderName());
		}
		return displayname;
	}

	/**
	 * Force the displayed name to refresh
	 */
	public void refreshDisplayName() {
		displayname = ForgeEventFactory.getPlayerDisplayName(this, getCommandSenderName());
	}
	/*
	 * ======================================== FORGE END
	 * =====================================
	 */

	public static enum EnumStatus {
		OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE;

		private static final String __OBFID = "CL_00001712";
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	protected boolean keepInventoryOnClone;

	@Override
	public boolean isEntityPlayer() {
		return true;
	}
}
