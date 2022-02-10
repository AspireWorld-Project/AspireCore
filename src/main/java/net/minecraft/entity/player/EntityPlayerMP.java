package net.minecraft.entity.player;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.ultramine.core.economy.account.Account;
import org.ultramine.core.economy.service.Economy;
import org.ultramine.core.permissions.Permissions;
import org.ultramine.core.service.InjectService;
import org.ultramine.server.WorldConstants;
import org.ultramine.server.chunk.ChunkSendManager;
import org.ultramine.server.data.player.PlayerData;
import org.ultramine.server.event.PlayerDeathEvent;
import org.ultramine.server.internal.UMEventFactory;
import org.ultramine.server.internal.UMHooks;
import org.ultramine.server.util.BasicTypeParser;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.registry.LanguageRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
	private static final Logger logger = LogManager.getLogger();
	private String translator = "en_US";
	public NetHandlerPlayServer playerNetServerHandler;
	public final MinecraftServer mcServer;
	public final ItemInWorldManager theItemInWorldManager;
	public double managedPosX;
	public double managedPosZ;
	public final List loadedChunks = new LinkedList();
	private final List destroyedItemsNetCache = new LinkedList();
	private StatisticsFile field_147103_bO;
	private float field_130068_bO = Float.MIN_VALUE;
	private float lastHealth = -1.0E8F;
	private int lastFoodLevel = -99999999;
	private boolean wasHungry = true;
	private int lastExperience = -99999999;
	private int field_147101_bU = 60;
	private EntityPlayer.EnumChatVisibility chatVisibility;
	private boolean chatColours = true;
	private long field_143005_bX = System.currentTimeMillis();
	public int currentWindowId;
	public boolean isChangingQuantityOnly;
	public int ping;
	public boolean playerConqueredTheEnd;
	private static final String __OBFID = "CL_00001440";

	// CraftBukkit start
	public String displayName;
	public String listName;
	public org.bukkit.Location compassTarget;
	public int newExp = 0;
	public int newLevel = 0;
	public int newTotalExp = 0;
	public boolean keepLevel = false;
	public double maxHealthCache;
	// CraftBukkit end
	// Spigot start
	public boolean collidesWithEntities = true;

	public EntityPlayerMP(MinecraftServer p_i45285_1_, WorldServer p_i45285_2_, GameProfile p_i45285_3_,
			ItemInWorldManager p_i45285_4_) {
		super(p_i45285_2_, p_i45285_3_);
		p_i45285_4_.thisPlayerMP = this;
		theItemInWorldManager = p_i45285_4_;

		mcServer = p_i45285_1_;
		stepHeight = 0.0F;
		yOffset = 0.0F;

		renderDistance = p_i45285_1_.getConfigurationManager().getViewDistance();
	}

	public String getTranslator() {
		return translator;
	}

	public int getLastExperience() {
		return lastExperience;
	}

	public void setLastExperience(int lastExperience) {
		this.lastExperience = lastExperience;
	}

	public int getField_147101_bU() {
		return field_147101_bU;
	}

	public void setField_147101_bU(int field_147101_bU) {
		this.field_147101_bU = field_147101_bU;
	}

	public String getBukkitDisplayName() {
		return displayName;
	}

	public void setBukkitDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getBukkitListName() {
		return listName;
	}

	public void setBukkitListName(String listName) {
		this.listName = listName;
	}

	public org.bukkit.Location getCompassTarget() {
		return compassTarget;
	}

	public void setCompassTarget(org.bukkit.Location compassTarget) {
		this.compassTarget = compassTarget;
	}

	public int getNewExp() {
		return newExp;
	}

	public void setNewExp(int newExp) {
		this.newExp = newExp;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public int getNewTotalExp() {
		return newTotalExp;
	}

	public void setNewTotalExp(int newTotalExp) {
		this.newTotalExp = newTotalExp;
	}

	public boolean isKeepLevel() {
		return keepLevel;
	}

	public void setKeepLevel(boolean keepLevel) {
		this.keepLevel = keepLevel;
	}

	public double getMaxHealthCache() {
		return maxHealthCache;
	}

	public void setMaxHealthCache(double maxHealthCache) {
		this.maxHealthCache = maxHealthCache;
	}

	public boolean isCollidesWithEntities() {
		return collidesWithEntities;
	}

	public void setCollidesWithEntities(boolean collidesWithEntities) {
		this.collidesWithEntities = collidesWithEntities;
	}

	public void closeScreenSilent() {
		playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(openContainer.windowId));
		openContainer.onContainerClosed(this);
		openContainer = inventoryContainer;
	}

	public int nextContainerCounter() {
		currentWindowId = currentWindowId % 100 + 1;
		return currentWindowId;
	}

	@Override
	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_, int p_71002_3_, String p_71002_4_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerEnchantment(inventory, worldObj, p_71002_1_, p_71002_2_, p_71002_3_));
		if (container == null)
			return;
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 4,
				p_71002_4_ == null ? "" : p_71002_4_, 9, p_71002_4_ != null));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_, int p_71058_3_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerWorkbench(inventory, worldObj, p_71058_1_, p_71058_2_, p_71058_3_));
		if (container == null)
			return;
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 1, "Crafting", 9, true));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerRepair(inventory, worldObj, p_82244_1_, p_82244_2_, p_82244_3_, this));
		if (container == null)
			return;
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 8, "Repairing", 9, true));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void displayGUIChest(IInventory p_71007_1_) {
		if (openContainer != inventoryContainer) {
			closeScreen();
		}
		Container container = CraftEventFactory.callInventoryOpenEvent(this, new ContainerChest(inventory, p_71007_1_));
		if (container == null) {
			p_71007_1_.closeInventory(); // Cauldron - prevent chest from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 0, p_71007_1_.getInventoryName(),
				p_71007_1_.getSizeInventory(), p_71007_1_.hasCustomInventoryName()));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void func_146093_a(TileEntityHopper p_146093_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerHopper(inventory, p_146093_1_));
		if (container == null) {
			p_146093_1_.closeInventory(); // Cauldron - prevent chest from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 9, p_146093_1_.getInventoryName(),
				p_146093_1_.getSizeInventory(), p_146093_1_.hasCustomInventoryName()));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerHopper(inventory, p_96125_1_));
		if (container == null) {
			p_96125_1_.closeInventory(); // Cauldron - prevent chest from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 9, p_96125_1_.getInventoryName(),
				p_96125_1_.getSizeInventory(), p_96125_1_.hasCustomInventoryName()));
		openContainer = container; // CraftBukkit - Use container we passed to event
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void func_146101_a(TileEntityFurnace p_146101_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerFurnace(inventory, p_146101_1_));
		if (container == null) {
			p_146101_1_.closeInventory(); // Cauldron - prevent chests from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 2, p_146101_1_.getInventoryName(),
				p_146101_1_.getSizeInventory(), p_146101_1_.hasCustomInventoryName()));
		openContainer = container; // CraftBukkit - Use container we passed to event
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void func_146102_a(TileEntityDispenser p_146102_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerDispenser(inventory, p_146102_1_));
		if (container == null) {
			p_146102_1_.closeInventory(); // Cauldron - prevent chests from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId,
				p_146102_1_ instanceof TileEntityDropper ? 10 : 3, p_146102_1_.getInventoryName(),
				p_146102_1_.getSizeInventory(), p_146102_1_.hasCustomInventoryName()));
		openContainer = container; // CraftBukkit - Use container we passed to event
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerBrewingStand(inventory, p_146098_1_));
		if (container == null) {
			p_146098_1_.closeInventory(); // Cauldron - prevent chests from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 5, p_146098_1_.getInventoryName(),
				p_146098_1_.getSizeInventory(), p_146098_1_.hasCustomInventoryName()));
		openContainer = container; // CraftBukkit - Use container we passed to event
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void func_146104_a(TileEntityBeacon p_146104_1_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerBeacon(inventory, p_146104_1_));
		if (container == null) {
			p_146104_1_.closeInventory(); // Cauldron - prevent chests from being stuck in open state on clients
			return;
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 7, p_146104_1_.getInventoryName(),
				p_146104_1_.getSizeInventory(), p_146104_1_.hasCustomInventoryName()));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerMerchant(inventory, p_71030_1_, worldObj));
		if (container == null)
			return;
		nextContainerCounter();
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
		InventoryMerchant inventorymerchant = ((ContainerMerchant) openContainer).getMerchantInventory();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 6,
				p_71030_2_ == null ? "" : p_71030_2_, inventorymerchant.getSizeInventory(), p_71030_2_ != null));
		MerchantRecipeList merchantrecipelist = p_71030_1_.getRecipes(this);
		if (merchantrecipelist != null) {
			PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
			try {
				packetbuffer.writeInt(currentWindowId);
				merchantrecipelist.func_151391_a(packetbuffer);
				playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", packetbuffer));
			} catch (Exception var10) {
				logger.error("Couldn't send trade list", var10);
			} finally {
				packetbuffer.release();
			}
		}
	}

	@Override
	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
		Container container = CraftEventFactory.callInventoryOpenEvent(this,
				new ContainerHorseInventory(inventory, p_110298_2_, p_110298_1_));
		if (container == null) {
			p_110298_2_.closeInventory(); // Cauldron - prevent chests from being stuck in open state on clients
			return;
		}
		if (openContainer != inventoryContainer) {
			closeScreen();
		}
		nextContainerCounter();
		playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(currentWindowId, 11, p_110298_2_.getInventoryName(),
				p_110298_2_.getSizeInventory(), p_110298_2_.hasCustomInventoryName(), p_110298_1_.getEntityId()));
		openContainer = container;
		openContainer.windowId = currentWindowId;
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.hasKey("playerGameType", 99)) {
			if (MinecraftServer.getServer().getForceGamemode()) {
				theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
			} else {
				theItemInWorldManager
						.setGameType(WorldSettings.GameType.getByID(p_70037_1_.getInteger("playerGameType")));
			}
		}
		((CraftPlayer) getBukkitEntity()).readExtraData(p_70037_1_);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("playerGameType", theItemInWorldManager.getGameType().getID());
		((CraftPlayer) getBukkitEntity()).setExtraData(p_70014_1_);
	}

	@Override
	public void addExperienceLevel(int p_82242_1_) {
		super.addExperienceLevel(p_82242_1_);
		lastExperience = -1;
	}

	public void addSelfToInternalCraftingInventory() {
		openContainer.addCraftingToCrafters(this);
	}

	@Override
	protected void resetHeight() {
		yOffset = 0.0F;
	}

	@Override
	public float getEyeHeight() {
		return super.getEyeHeight();
	}

	@Override
	public void onUpdate() {
		theItemInWorldManager.updateBlockRemoving();
		--field_147101_bU;

		if (hurtResistantTime > 0) {
			--hurtResistantTime;
		}

		openContainer.detectAndSendChanges();

		if (!worldObj.isRemote && !ForgeHooks.canInteractWith(this, openContainer)) {
			closeScreen();
			openContainer = inventoryContainer;
		}

		while (!destroyedItemsNetCache.isEmpty()) {
			int i = Math.min(destroyedItemsNetCache.size(), 127);
			int[] aint = new int[i];
			Iterator iterator = destroyedItemsNetCache.iterator();
			int j = 0;

			while (iterator.hasNext() && j < i) {
				aint[j++] = ((Integer) iterator.next()).intValue();
				iterator.remove();
			}

			playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(aint));
		}

		getChunkMgr().update();

		if (field_143005_bX > 0L && mcServer.func_143007_ar() > 0
				&& MinecraftServer.getSystemTimeMillis() - field_143005_bX > mcServer.func_143007_ar() * 1000 * 60) {
			playerNetServerHandler.kickPlayerFromServer("You have been idle for too long!");
		}
	}

	public void onUpdateEntity() {
		try {
			super.onUpdate();

			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemstack = inventory.getStackInSlot(i);

				if (itemstack != null && itemstack.getItem().isMap()) {
					Packet packet = ((ItemMapBase) itemstack.getItem()).func_150911_c(itemstack, worldObj, this);

					if (packet != null) {
						playerNetServerHandler.sendPacket(packet);
					}
				}
			}

			if (getHealth() != lastHealth || lastFoodLevel != foodStats.getFoodLevel()
					|| foodStats.getSaturationLevel() == 0.0F != wasHungry) {
				playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(getHealth(), foodStats.getFoodLevel(),
						foodStats.getSaturationLevel()));
				lastHealth = getHealth();
				lastFoodLevel = foodStats.getFoodLevel();
				wasHungry = foodStats.getSaturationLevel() == 0.0F;
			}

			if (getHealth() + getAbsorptionAmount() != field_130068_bO) {
				field_130068_bO = getHealth() + getAbsorptionAmount();
				Collection collection = getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.health);
				Iterator iterator = collection.iterator();

				while (iterator.hasNext()) {
					ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
					getWorldScoreboard().func_96529_a(getCommandSenderName(), scoreobjective)
							.func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
				}
			}

			if (experienceTotal != lastExperience) {
				lastExperience = experienceTotal;
				playerNetServerHandler
						.sendPacket(new S1FPacketSetExperience(experience, experienceTotal, experienceLevel));
			}

			if (ticksExisted % 20 * 5 == 0 && !func_147099_x().hasAchievementUnlocked(AchievementList.field_150961_L)) {
				func_147098_j();
			}

			if (this.oldLevel == -1) {
				this.oldLevel = this.experienceLevel;
			}

			if (this.oldLevel != this.experienceLevel) {
				CraftEventFactory.callPlayerLevelChangeEvent(this.worldObj.getServer().getPlayer((EntityPlayerMP) this), this.oldLevel, this.experienceLevel);
				this.oldLevel = this.experienceLevel;
			}

		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
			addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	protected void func_147098_j() {
		BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX),
				MathHelper.floor_double(posZ));

		if (biomegenbase != null) {
			String s = biomegenbase.biomeName;
			JsonSerializableSet jsonserializableset = (JsonSerializableSet) func_147099_x()
					.func_150870_b(AchievementList.field_150961_L);

			if (jsonserializableset == null) {
				jsonserializableset = (JsonSerializableSet) func_147099_x()
						.func_150872_a(AchievementList.field_150961_L, new JsonSerializableSet());
			}

			jsonserializableset.add(s);

			if (func_147099_x().canUnlockAchievement(AchievementList.field_150961_L)
					&& jsonserializableset.size() == BiomeGenBase.explorationBiomesList.size()) {
				HashSet hashset = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
				Iterator iterator = jsonserializableset.iterator();

				while (iterator.hasNext()) {
					String s1 = (String) iterator.next();
					Iterator iterator1 = hashset.iterator();

					while (iterator1.hasNext()) {
						BiomeGenBase biomegenbase1 = (BiomeGenBase) iterator1.next();

						if (biomegenbase1.biomeName.equals(s1)) {
							iterator1.remove();
						}
					}

					if (hashset.isEmpty()) {
						break;
					}
				}

				if (hashset.isEmpty()) {
					triggerAchievement(AchievementList.field_150961_L);
				}
			}
		}
	}

	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (ForgeHooks.onLivingDeath(this, p_70645_1_))
			return;
		PlayerDeathEvent umEvent = UMEventFactory.firePlayerDeath(this, p_70645_1_, func_110142_aN().func_151521_b(),
				worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"));
		if (umEvent.getDeathMessage() != null) {
			mcServer.getConfigurationManager().sendChatMsg(umEvent.getDeathMessage());
		}

		if (!umEvent.isKeepInventory() && umEvent.isProcessDrops()) {
			captureDrops = true;
			capturedDrops.clear();

			inventory.dropAllItems();

			captureDrops = false;
			PlayerDropsEvent event = new PlayerDropsEvent(this, p_70645_1_, capturedDrops, recentlyHit > 0);
			if (!MinecraftForge.EVENT_BUS.post(event)) {
				for (EntityItem item : capturedDrops) {
					joinEntityItemWithWorld(item);
				}
			}
		} else {
			if (umEvent.isKeepInventory()) {
				keepInventoryOnClone = true;
			}
		}

		Collection collection = worldObj.getScoreboard().func_96520_a(IScoreObjectiveCriteria.deathCount);
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
			Score score = getWorldScoreboard().func_96529_a(getCommandSenderName(), scoreobjective);
			score.func_96648_a();
		}

		EntityLivingBase entitylivingbase = func_94060_bK();

		if (entitylivingbase != null) {
			int i = EntityList.getEntityID(entitylivingbase);
			EntityList.EntityEggInfo entityegginfo = (EntityList.EntityEggInfo) EntityList.entityEggs
					.get(Integer.valueOf(i));

			if (entityegginfo != null) {
				addStat(entityegginfo.field_151513_e, 1);
			}

			entitylivingbase.addToPlayerScore(this, scoreValue);
		}

		addStat(StatList.deathsStat, 1);
		func_110142_aN().func_94549_h();
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			boolean flag = mcServer.isDedicatedServer() && getServerForPlayer().getConfig().settings.pvp
					&& "fall".equals(p_70097_1_.damageType);

			if (!flag && field_147101_bU > 0 && p_70097_1_ != DamageSource.outOfWorld)
				return false;
			else {
				if (p_70097_1_ instanceof EntityDamageSource) {
					Entity entity = p_70097_1_.getEntity();

					if (entity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer) entity))
						return false;

					if (entity instanceof EntityArrow) {
						EntityArrow entityarrow = (EntityArrow) entity;

						if (entityarrow.shootingEntity instanceof EntityPlayer
								&& !canAttackPlayer((EntityPlayer) entityarrow.shootingEntity))
							return false;
					}
				}

				return super.attackEntityFrom(p_70097_1_, p_70097_2_);
			}
		}
	}

	@Override
	public boolean canAttackPlayer(EntityPlayer p_96122_1_) {
		return !getServerForPlayer().getConfig().settings.pvp ? false : super.canAttackPlayer(p_96122_1_);
	}

	@Override
	public void travelToDimension(int p_71027_1_) {
		if (mcServer.worldServerForDimension(p_71027_1_) == null)
			return;
		int enderLink = ((WorldServer) worldObj).getConfig().portals.enderLink;
		if (dimension == enderLink && p_71027_1_ == enderLink) {
			triggerAchievement(AchievementList.theEnd2);
			worldObj.removeEntity(this);
			playerConqueredTheEnd = true;
			playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
		} else {
			if (p_71027_1_ == enderLink) {
				triggerAchievement(AchievementList.theEnd);
				ChunkCoordinates chunkcoordinates = mcServer.worldServerForDimension(p_71027_1_)
						.getEntrancePortalLocation();

				if (chunkcoordinates != null) {
					playerNetServerHandler.setPlayerLocation(chunkcoordinates.posX, chunkcoordinates.posY,
							chunkcoordinates.posZ, 0.0F, 0.0F);
				}
			} else {
				triggerAchievement(AchievementList.portal);
			}

			mcServer.getConfigurationManager().transferPlayerToDimension(this, p_71027_1_);
			lastExperience = -1;
			lastHealth = -1.0F;
			lastFoodLevel = -1;
		}
	}

	private void func_147097_b(TileEntity p_147097_1_) {
		if (p_147097_1_ != null) {
			Packet packet = p_147097_1_.getDescriptionPacket();

			if (packet != null) {
				playerNetServerHandler.sendPacket(packet);
			}
		}
	}

	@Override
	public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
		super.onItemPickup(p_71001_1_, p_71001_2_);
		// this.openContainer.detectAndSendChanges();
	}

	@Override
	public EntityPlayer.EnumStatus sleepInBedAt(int p_71018_1_, int p_71018_2_, int p_71018_3_) {
		EntityPlayer.EnumStatus enumstatus = super.sleepInBedAt(p_71018_1_, p_71018_2_, p_71018_3_);

		if (enumstatus == EntityPlayer.EnumStatus.OK) {
			S0APacketUseBed s0apacketusebed = new S0APacketUseBed(this, p_71018_1_, p_71018_2_, p_71018_3_);
			getServerForPlayer().getEntityTracker().func_151247_a(this, s0apacketusebed);
			playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
			playerNetServerHandler.sendPacket(s0apacketusebed);
		}

		return enumstatus;
	}

	@Override
	public void wakeUpPlayer(boolean p_70999_1_, boolean p_70999_2_, boolean p_70999_3_) {
		if (isPlayerSleeping()) {
			getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));
		}

		super.wakeUpPlayer(p_70999_1_, p_70999_2_, p_70999_3_);

		if (playerNetServerHandler != null) {
			playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
		}
	}

	@Override
	public void mountEntity(Entity p_70078_1_) {
		super.mountEntity(p_70078_1_);
		playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, ridingEntity));
		playerNetServerHandler.setPlayerLocation(posX, posY, posZ, rotationYaw, rotationPitch);
	}

	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
	}

	public void handleFalling(double p_71122_1_, boolean p_71122_3_) {
		super.updateFallState(p_71122_1_, p_71122_3_);
	}

	@Override
	public void func_146100_a(TileEntity p_146100_1_) {
		if (p_146100_1_ instanceof TileEntitySign) {
			((TileEntitySign) p_146100_1_).func_145912_a(this);
			playerNetServerHandler.sendPacket(
					new S36PacketSignEditorOpen(p_146100_1_.xCoord, p_146100_1_.yCoord, p_146100_1_.zCoord));
		}
	}

	public void getNextWindowId() {
		currentWindowId = currentWindowId % 100 + 1;
	}

	@Override
	public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
		if (!(p_71111_1_.getSlot(p_71111_2_) instanceof SlotCrafting)) {
			if (!isChangingQuantityOnly) {
				playerNetServerHandler.sendPacket(new S2FPacketSetSlot(p_71111_1_.windowId, p_71111_2_, p_71111_3_));
			}
		}
	}

	public void sendContainerToPlayer(Container p_71120_1_) {
		sendContainerAndContentsToPlayer(p_71120_1_, p_71120_1_.getInventory());
	}

	@Override
	public void sendContainerAndContentsToPlayer(Container p_71110_1_, List p_71110_2_) {
		playerNetServerHandler.sendPacket(new S30PacketWindowItems(p_71110_1_.windowId, p_71110_2_));
		playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, inventory.getItemStack()));
	}

	@Override
	public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
		playerNetServerHandler.sendPacket(new S31PacketWindowProperty(p_71112_1_.windowId, p_71112_2_, p_71112_3_));
	}

	@Override
	public void closeScreen() {
		CraftEventFactory.handleInventoryCloseEvent(this);
		playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(openContainer.windowId));
		closeContainer();
	}

	public void updateHeldItem() {
		if (!isChangingQuantityOnly) {
			playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, inventory.getItemStack()));
		}
	}

	public void closeContainer() {
		UMEventFactory.fireInventoryClose(this);
		openContainer.onContainerClosed(this);
		openContainer = inventoryContainer;
	}

	public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean p_110430_4_) {
		if (ridingEntity != null) {
			if (p_110430_1_ >= -1.0F && p_110430_1_ <= 1.0F) {
				moveStrafing = p_110430_1_;
			}

			if (p_110430_2_ >= -1.0F && p_110430_2_ <= 1.0F) {
				moveForward = p_110430_2_;
			}

			isJumping = p_110430_3_;
			setSneaking(p_110430_4_);
		}
	}

	@Override
	public void addStat(StatBase p_71064_1_, int p_71064_2_) {
		if (p_71064_1_ != null && field_147103_bO != null) {
			if (p_71064_1_.isAchievement()
					&& MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.AchievementEvent(this,
							(net.minecraft.stats.Achievement) p_71064_1_)))
				return;
			field_147103_bO.func_150871_b(this, p_71064_1_, p_71064_2_);
			Iterator iterator = getWorldScoreboard().func_96520_a(p_71064_1_.func_150952_k()).iterator();

			while (iterator.hasNext()) {
				ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
				getWorldScoreboard().func_96529_a(getCommandSenderName(), scoreobjective).func_96648_a();
			}

			if (field_147103_bO.func_150879_e()) {
				field_147103_bO.func_150876_a(this);
			}
		}
	}

	public void mountEntityAndWakeUp() {
		if (riddenByEntity != null) {
			riddenByEntity.mountEntity(this);
		}

		if (sleeping) {
			wakeUpPlayer(true, false, false);
		}
	}

	public void setPlayerHealthUpdated() {
		lastHealth = -1.0E8F;
	}

	@Override
	public void addChatComponentMessage(IChatComponent p_146105_1_) {
		playerNetServerHandler.sendPacket(new S02PacketChat(UMHooks.onChatSend(this, p_146105_1_)));
	}

	@Override
	protected void onItemUseFinish() {
		playerNetServerHandler.sendPacket(new S19PacketEntityStatus(this, (byte) 9));
		super.onItemUseFinish();
	}

	@Override
	public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_) {
		super.setItemInUse(p_71008_1_, p_71008_2_);

		if (p_71008_1_ != null && p_71008_1_.getItem() != null
				&& p_71008_1_.getItem().getItemUseAction(p_71008_1_) == EnumAction.eat) {
			getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
		}
	}

	@Override
	public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_) {
		super.clonePlayer(p_71049_1_, p_71049_2_);
		lastExperience = -1;
		lastHealth = -1.0F;
		lastFoodLevel = -1;
		destroyedItemsNetCache.addAll(((EntityPlayerMP) p_71049_1_).destroyedItemsNetCache);
		translator = ((EntityPlayerMP) p_71049_1_).translator;
		renderDistance = ((EntityPlayerMP) p_71049_1_).renderDistance;
		chatVisibility = ((EntityPlayerMP) p_71049_1_).chatVisibility;
		chatColours = ((EntityPlayerMP) p_71049_1_).chatColours;
	}

	@Override
	protected void onNewPotionEffect(PotionEffect p_70670_1_) {
		super.onNewPotionEffect(p_70670_1_);
		playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), p_70670_1_));
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_) {
		super.onChangedPotionEffect(p_70695_1_, p_70695_2_);
		playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), p_70695_1_));
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
		super.onFinishedPotionEffect(p_70688_1_);
		playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(getEntityId(), p_70688_1_));
	}

	@Override
	public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_, double p_70634_5_) {
		playerNetServerHandler.setPlayerLocation(p_70634_1_, p_70634_3_, p_70634_5_, rotationYaw, rotationPitch);
	}

	@Override
	public void onCriticalHit(Entity p_71009_1_) {
		getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(p_71009_1_, 4));
	}

	@Override
	public void onEnchantmentCritical(Entity p_71047_1_) {
		getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(p_71047_1_, 5));
	}

	@Override
	public void sendPlayerAbilities() {
		if (playerNetServerHandler != null) {
			playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities(capabilities));
		}
	}

	public WorldServer getServerForPlayer() {
		return (WorldServer) worldObj;
	}

	@Override
	public void setGameType(WorldSettings.GameType p_71033_1_) {
		theItemInWorldManager.setGameType(p_71033_1_);
		playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, p_71033_1_.getID()));
	}

	@Override
	public void addChatMessage(IChatComponent p_145747_1_) {
		playerNetServerHandler.sendPacket(new S02PacketChat(UMHooks.onChatSend(this, p_145747_1_)));
	}

	@Override
	public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
		return true;
	}

	public String getPlayerIP() {
		String s = playerNetServerHandler.netManager.getSocketAddress().toString();
		s = s.substring(s.indexOf("/") + 1);
		s = s.substring(0, s.indexOf(":"));
		return s;
	}

	public void func_147100_a(C15PacketClientSettings p_147100_1_) {
		translator = p_147100_1_.func_149524_c();
		int i = /* 256 >> */ p_147100_1_.func_149521_d();

		renderDistance = MathHelper.clamp_int(i, 3, WorldConstants.MAX_VIEW_DISTANCE);

		chatVisibility = p_147100_1_.func_149523_e();
		chatColours = p_147100_1_.func_149520_f();

		if (mcServer.isSinglePlayer() && mcServer.getServerOwner().equals(getCommandSenderName())) {
			mcServer.func_147139_a(p_147100_1_.func_149518_g());
		}

		setHideCape(1, !p_147100_1_.func_149519_h());
	}

	public EntityPlayer.EnumChatVisibility func_147096_v() {
		return chatVisibility;
	}

	public void requestTexturePackLoad(String p_147095_1_) {
		playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|RPack", p_147095_1_.getBytes(Charsets.UTF_8)));
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY + 0.5D),
				MathHelper.floor_double(posZ));
	}

	public void func_143004_u() {
		field_143005_bX = MinecraftServer.getSystemTimeMillis();
	}

	public StatisticsFile func_147099_x() {
		return field_147103_bO;
	}

	public void func_152339_d(Entity p_152339_1_) {
		if (p_152339_1_ instanceof EntityPlayer) {
			playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
		} else {
			destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
		}
	}

	public long func_154331_x() {
		return field_143005_bX;
	}

	/*
	 * ===================================== FORGE START
	 * =====================================
	 */
	/**
	 * Returns the default eye height of the player
	 *
	 * @return player default eye height
	 */
	@Override
	public float getDefaultEyeHeight() {
		return 1.62F;
	}

	/*
	 * ===================================== ULTRAMINE START
	 * =====================================
	 */

	private int renderDistance;
	private final ChunkSendManager chunkMgr = new ChunkSendManager(this);
	private PlayerData playerData;
	@InjectService
	private static Permissions perms;
	@InjectService
	private static Economy economy;

	public boolean hasPermission(String permission) {
		return perms.has(this, permission);
	}

	public String getMeta(String key) {
		return perms.getMeta(this, key);
	}

	public Account getAccount() {
		return economy.getPlayerAccount(this);
	}

	@Override
	public boolean isEntityPlayerMP() {
		return true;
	}

	public ChunkSendManager getChunkMgr() {
		return chunkMgr;
	}

	public int getRenderDistance() {
		return renderDistance;
	}

	public PlayerData getData() {
		return playerData;
	}

	public void setData(PlayerData playerData) {
		playerData.setProfile(getGameProfile());
		this.playerData = playerData;
	}

	public String getTabListName() {
		String meta = getMeta("tablistcolor");
		EnumChatFormatting color = meta.isEmpty() ? null : BasicTypeParser.parseColor(meta);
		String name = color == null ? getCommandSenderName() : color.toString() + getCommandSenderName();
		return name.length() > 16 ? name.substring(0, 16) : name;
	}

	public String translate(String key) {
		String translated = LanguageRegistry.instance().getStringLocalization(key, getTranslator());
		if (translated.isEmpty()) {
			translated = LanguageRegistry.instance().getStringLocalization(key, "en_US");
		}
		return translated.isEmpty() ? key : translated;
	}

	/**
	 * Переносит игрока в другой мир без использования порталов. Обратите внимение:
	 * сначала нужно установить координаты назначения <code>setPosition()</code>, а
	 * потом уже переносить в другой мир.
	 */
	public void transferToDimension(int dim) {
		mcServer.getConfigurationManager().transferPlayerToDimension(this, dim, (net.minecraft.world.Teleporter) null);
		lastExperience = -1;
		lastHealth = -1.0F;
		lastFoodLevel = -1;
	}

	/** Safe transferToDimension and setPosition */
	public boolean setWorldPosition(int dim, double x, double y, double z) {
		if (dim == dimension) {
			playerNetServerHandler.setPlayerLocation(x, y, z, rotationYaw, rotationPitch);
			return true;
		}
		if (mcServer.worldServerForDimension(dim) == null)
			return false;
		int lastDim = dimension;
		double lastX = posX;
		double lastY = posY;
		double lastZ = posZ;
		setPosition(x, y, z);
		try {
			transferToDimension(dim);
		} catch (RuntimeException e) {
			setPosition(lastX, lastY, lastZ);
			dimension = lastDim;
			throw e;
		}
		return true;
	}

	/** Safe transferToDimension and setPositionAndRotation */
	public boolean setWorldPositionAndRotation(int dim, double x, double y, double z, float yaw, float pitch) {
		if (dim == dimension) {
			playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
			return true;
		}
		if (mcServer.worldServerForDimension(dim) == null)
			return false;
		int lastDim = dimension;
		double lastX = posX;
		double lastY = posY;
		double lastZ = posZ;
		float lastYaw = rotationYaw;
		float lastPitch = rotationPitch;
		setPositionAndRotation(x, y, z, yaw, pitch);
		try {
			transferToDimension(dim);
		} catch (RuntimeException e) {
			setPositionAndRotation(lastX, lastY, lastZ, lastYaw, lastPitch);
			dimension = lastDim;
			throw e;
		}
		return true;
	}

	public void setStatisticsFile(StatisticsFile stats) {
		field_147103_bO = stats;
	}

	public void hide() {
		if (!isHidden()) {
			getData().core().setHidden(true);
			((WorldServer) worldObj).getEntityTracker().hidePlayer(this);
			mcServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S38PacketPlayerListItem(getTabListName(), false, 9999));
		}
	}

	public void show() {
		if (isHidden()) {
			getData().core().setHidden(false);
			((WorldServer) worldObj).getEntityTracker().showPlayer(this);
			mcServer.getConfigurationManager()
					.sendPacketToAllPlayers(new S38PacketPlayerListItem(getTabListName(), true, ping));
		}
	}

	public boolean isHidden() {
		return getData() != null && getData().core().isHidden();
	}
}
