package net.minecraft.server.management;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.ultramine.server.ConfigurationHandler;

public class ItemInWorldManager {
	/** Forge reach distance */
	private double blockReachDistance = 5.0d;
	public World theWorld;
	public EntityPlayerMP thisPlayerMP;
	private WorldSettings.GameType gameType;
	private boolean isDestroyingBlock;
	private int initialDamage;
	private int partiallyDestroyedBlockX;
	private int partiallyDestroyedBlockY;
	private int partiallyDestroyedBlockZ;
	private int curblockDamage;
	private boolean receivedFinishDiggingPacket;
	private int posX;
	private int posY;
	private int posZ;
	private int initialBlockDamage;
	private int durabilityRemainingOnBlock;
	private static final String __OBFID = "CL_00001442";

	private static final boolean isServer = FMLCommonHandler.instance().getSide().isServer();

	public ItemInWorldManager(World p_i1524_1_) {
		gameType = WorldSettings.GameType.NOT_SET;
		durabilityRemainingOnBlock = -1;
		theWorld = p_i1524_1_;
	}

	public void setGameType(WorldSettings.GameType p_73076_1_) {
		gameType = p_73076_1_;
		p_73076_1_.configurePlayerCapabilities(thisPlayerMP.capabilities);
		thisPlayerMP.sendPlayerAbilities();
	}

	public WorldSettings.GameType getGameType() {
		return gameType;
	}

	public boolean isCreative() {
		return gameType.isCreative();
	}

	public void initializeGameType(WorldSettings.GameType p_73077_1_) {
		if (gameType == WorldSettings.GameType.NOT_SET) {
			gameType = p_73077_1_;
		}

		setGameType(gameType);
	}

	public void updateBlockRemoving() {
		++curblockDamage;
		float f;
		int j;

		if (receivedFinishDiggingPacket) {
			int i = curblockDamage - initialBlockDamage;
			Block block = theWorld.getBlock(posX, posY, posZ);

			if (block.getMaterial() == Material.air) {
				receivedFinishDiggingPacket = false;
			} else {
				f = block.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, posX, posY, posZ)
						* (i + 1);
				j = (int) (f * 10.0F);

				if (j != durabilityRemainingOnBlock) {
					theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), posX, posY, posZ, j);
					durabilityRemainingOnBlock = j;
				}

				if (f >= 1.0F) {
					receivedFinishDiggingPacket = false;
					tryHarvestBlock(posX, posY, posZ);
				}
			}
		} else if (isDestroyingBlock) {
			Block block1 = theWorld.getBlock(partiallyDestroyedBlockX, partiallyDestroyedBlockY,
					partiallyDestroyedBlockZ);

			if (block1.getMaterial() == Material.air) {
				theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), partiallyDestroyedBlockX,
						partiallyDestroyedBlockY, partiallyDestroyedBlockZ, -1);
				durabilityRemainingOnBlock = -1;
				isDestroyingBlock = false;
			} else {
				int k = curblockDamage - initialDamage;
				f = block1.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, partiallyDestroyedBlockX,
						partiallyDestroyedBlockY, partiallyDestroyedBlockZ) * (k + 1);
				j = (int) (f * 10.0F);

				if (j != durabilityRemainingOnBlock) {
					theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), partiallyDestroyedBlockX,
							partiallyDestroyedBlockY, partiallyDestroyedBlockZ, j);
					durabilityRemainingOnBlock = j;
				}
			}
		}
	}

	public void onBlockClicked(int p_73074_1_, int p_73074_2_, int p_73074_3_, int p_73074_4_) {
		// CraftBukkit start
		org.bukkit.event.player.PlayerInteractEvent cbEvent = CraftEventFactory.callPlayerInteractEvent(thisPlayerMP,
				org.bukkit.event.block.Action.LEFT_CLICK_BLOCK, p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_,
				thisPlayerMP.inventory.getCurrentItem());

		if (!gameType.isAdventure()
				|| thisPlayerMP.isCurrentToolAdventureModeExempt(p_73074_1_, p_73074_2_, p_73074_3_)) {
			net.minecraftforge.event.entity.player.PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(
					thisPlayerMP, net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.LEFT_CLICK_BLOCK,
					p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_, theWorld); // Forge

			if (cbEvent.isCancelled() || event.isCanceled()) {
				// Let the client know the block still exists
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S23PacketBlockChange(p_73074_1_, p_73074_2_, p_73074_3_, theWorld));
				// Update any tile entity data for this block
				TileEntity tileentity = theWorld.getTileEntity(p_73074_1_, p_73074_2_, p_73074_3_);

				if (tileentity != null) {
					Packet packet = tileentity.getDescriptionPacket();
					if (packet != null) {
						thisPlayerMP.playerNetServerHandler.sendPacket(packet);
					}
				}

				return;
			}

			// CraftBukkit end
			if (isCreative()) {
				if (!theWorld.extinguishFire((EntityPlayer) null, p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_)) {
					tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
				}
			} else {
				initialDamage = curblockDamage;
				float f = 1.0F;
				Block block = theWorld.getBlock(p_73074_1_, p_73074_2_, p_73074_3_);

				// CraftBukkit start - Swings at air do *NOT* exist.
				if (cbEvent.useInteractedBlock() == org.bukkit.event.Event.Result.DENY
						|| event.useBlock == cpw.mods.fml.common.eventhandler.Event.Result.DENY) // Cauldron
				{
					// If we denied a door from opening, we need to send a correcting update to the
					// client, as it already opened the door.
					if (block == Blocks.wooden_door) {
						// For some reason *BOTH* the bottom/top part have to be marked updated.
						boolean bottom = (theWorld.getBlockMetadata(p_73074_1_, p_73074_2_, p_73074_3_) & 8) == 0;
						thisPlayerMP.playerNetServerHandler
								.sendPacket(new S23PacketBlockChange(p_73074_1_, p_73074_2_, p_73074_3_, theWorld));
						thisPlayerMP.playerNetServerHandler.sendPacket(new S23PacketBlockChange(p_73074_1_,
								p_73074_2_ + (bottom ? 1 : -1), p_73074_3_, theWorld));
					} else if (block == Blocks.trapdoor) {
						thisPlayerMP.playerNetServerHandler
								.sendPacket(new S23PacketBlockChange(p_73074_1_, p_73074_2_, p_73074_3_, theWorld));
					}
				} else if (!block.isAir(theWorld, p_73074_1_, p_73074_2_, p_73074_3_)) {
					block.onBlockClicked(theWorld, p_73074_1_, p_73074_2_, p_73074_3_, thisPlayerMP);
					f = block.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, p_73074_1_,
							p_73074_2_, p_73074_3_);
					// Allow fire punching to be blocked
					theWorld.extinguishFire((EntityPlayer) null, p_73074_1_, p_73074_2_, p_73074_3_, p_73074_4_);
				}
				if (cbEvent.useItemInHand() == org.bukkit.event.Event.Result.DENY
						|| event.useItem == cpw.mods.fml.common.eventhandler.Event.Result.DENY) // Forge
				{
					// If we 'insta destroyed' then the client needs to be informed.
					if (f > 1.0f) {
						thisPlayerMP.playerNetServerHandler
								.sendPacket(new S23PacketBlockChange(p_73074_1_, p_73074_2_, p_73074_3_, theWorld));
					}

					return;
				}

				org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(
						thisPlayerMP, p_73074_1_, p_73074_2_, p_73074_3_, thisPlayerMP.inventory.getCurrentItem(),
						f >= 1.0f);

				if (blockEvent.isCancelled()) {
					// Let the client know the block still exists
					thisPlayerMP.playerNetServerHandler
							.sendPacket(new S23PacketBlockChange(p_73074_1_, p_73074_2_, p_73074_3_, theWorld));
					return;
				}

				if (blockEvent.getInstaBreak()) {
					f = 2.0f;
				}

				// CraftBukkit end

				if (!block.isAir(theWorld, p_73074_1_, p_73074_2_, p_73074_3_) && f >= 1.0F) {
					tryHarvestBlock(p_73074_1_, p_73074_2_, p_73074_3_);
				} else {
					isDestroyingBlock = true;
					partiallyDestroyedBlockX = p_73074_1_;
					partiallyDestroyedBlockY = p_73074_2_;
					partiallyDestroyedBlockZ = p_73074_3_;
					int i1 = (int) (f * 10.0F);
					theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), p_73074_1_, p_73074_2_,
							p_73074_3_, i1);
					durabilityRemainingOnBlock = i1;
				}
			}
		}
	}

	public void uncheckedTryHarvestBlock(int p_73082_1_, int p_73082_2_, int p_73082_3_) {
		if (p_73082_1_ == partiallyDestroyedBlockX && p_73082_2_ == partiallyDestroyedBlockY
				&& p_73082_3_ == partiallyDestroyedBlockZ) {
			int l = curblockDamage - initialDamage;
			Block block = theWorld.getBlock(p_73082_1_, p_73082_2_, p_73082_3_);

			if (!block.isAir(theWorld, p_73082_1_, p_73082_2_, p_73082_3_)) {
				float f = block.getPlayerRelativeBlockHardness(thisPlayerMP, thisPlayerMP.worldObj, p_73082_1_,
						p_73082_2_, p_73082_3_) * (l + 1);

				if (f >= 0.7F || !isServer
						|| !ConfigurationHandler.getServerConfig().settings.security.checkBreakSpeed) {
					isDestroyingBlock = false;
					theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), p_73082_1_, p_73082_2_,
							p_73082_3_, -1);
					tryHarvestBlock(p_73082_1_, p_73082_2_, p_73082_3_);
				} else if (!receivedFinishDiggingPacket) {
					isDestroyingBlock = false;
					receivedFinishDiggingPacket = true;
					posX = p_73082_1_;
					posY = p_73082_2_;
					posZ = p_73082_3_;
					initialBlockDamage = initialDamage;
				}
			}
		}
	}

	public void cancelDestroyingBlock(int p_73073_1_, int p_73073_2_, int p_73073_3_) {
		isDestroyingBlock = false;
		theWorld.destroyBlockInWorldPartially(thisPlayerMP.getEntityId(), partiallyDestroyedBlockX,
				partiallyDestroyedBlockY, partiallyDestroyedBlockZ, -1);
	}

	private boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_) {
		return removeBlock(p_73079_1_, p_73079_2_, p_73079_3_, false);
	}

	private boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_, boolean canHarvest) {
		Block block = theWorld.getBlock(p_73079_1_, p_73079_2_, p_73079_3_);
		int l = theWorld.getBlockMetadata(p_73079_1_, p_73079_2_, p_73079_3_);
		block.onBlockHarvested(theWorld, p_73079_1_, p_73079_2_, p_73079_3_, l, thisPlayerMP);
		boolean flag = block.removedByPlayer(theWorld, thisPlayerMP, p_73079_1_, p_73079_2_, p_73079_3_, canHarvest);

		if (flag) {
			block.onBlockDestroyedByPlayer(theWorld, p_73079_1_, p_73079_2_, p_73079_3_, l);
		}

		return flag;
	}

	public boolean tryHarvestBlock(int p_73084_1_, int p_73084_2_, int p_73084_3_) {
		BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(theWorld, gameType, thisPlayerMP, p_73084_1_,
				p_73084_2_, p_73084_3_);
		if (event.isCanceled())
			return false;
		else {
			ItemStack stack = thisPlayerMP.getCurrentEquippedItem();
			if (stack != null
					&& stack.getItem().onBlockStartBreak(stack, p_73084_1_, p_73084_2_, p_73084_3_, thisPlayerMP))
				return false;
			Block block = theWorld.getBlock(p_73084_1_, p_73084_2_, p_73084_3_);

			if (block == Blocks.air)
				return false; // CraftBukkit - A plugin set block to air without cancelling
			int l = theWorld.getBlockMetadata(p_73084_1_, p_73084_2_, p_73084_3_);
			theWorld.playAuxSFXAtEntity(thisPlayerMP, 2001, p_73084_1_, p_73084_2_, p_73084_3_,
					Block.getIdFromBlock(block)
							+ (theWorld.getBlockMetadata(p_73084_1_, p_73084_2_, p_73084_3_) << 12));
			boolean flag = false;

			if (isCreative()) {
				flag = this.removeBlock(p_73084_1_, p_73084_2_, p_73084_3_);
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S23PacketBlockChange(p_73084_1_, p_73084_2_, p_73084_3_, theWorld));
			} else {
				ItemStack itemstack = thisPlayerMP.getCurrentEquippedItem();
				boolean flag1 = block.canHarvestBlock(thisPlayerMP, l);

				if (itemstack != null) {
					itemstack.func_150999_a(theWorld, block, p_73084_1_, p_73084_2_, p_73084_3_, thisPlayerMP);

					if (itemstack.stackSize == 0) {
						thisPlayerMP.destroyCurrentEquippedItem();
					}
				}

				flag = this.removeBlock(p_73084_1_, p_73084_2_, p_73084_3_, flag1);
				if (flag && flag1) {
					block.harvestBlock(theWorld, thisPlayerMP, p_73084_1_, p_73084_2_, p_73084_3_, l);
				}
			}

			// Drop experience
			if (!isCreative() && flag && event != null) {
				block.dropXpOnBlockBreak(theWorld, p_73084_1_, p_73084_2_, p_73084_3_, event.getExpToDrop());
			}
			return flag;
		}
	}

	public boolean tryUseItem(EntityPlayer p_73085_1_, World p_73085_2_, ItemStack p_73085_3_) {
		int i = p_73085_3_.stackSize;
		int j = p_73085_3_.getItemDamage();
		ItemStack itemstack1 = p_73085_3_.useItemRightClick(p_73085_2_, p_73085_1_);

		if (itemstack1 == p_73085_3_ && (itemstack1 == null || itemstack1.stackSize == i
				&& itemstack1.getMaxItemUseDuration() <= 0 && itemstack1.getItemDamage() == j))
			return false;
		else {
			p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = itemstack1;

			if (isCreative()) {
				itemstack1.stackSize = i;

				if (itemstack1.isItemStackDamageable()) {
					itemstack1.setItemDamage(j);
				}
			}

			if (itemstack1.stackSize == 0) {
				p_73085_1_.inventory.mainInventory[p_73085_1_.inventory.currentItem] = null;
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thisPlayerMP, itemstack1));
			}

			if (!p_73085_1_.isUsingItem()) {
				((EntityPlayerMP) p_73085_1_).sendContainerToPlayer(p_73085_1_.inventoryContainer);
			}

			return true;
		}
	}

	public boolean activateBlockOrUseItem(EntityPlayer p_73078_1_, World p_73078_2_, ItemStack p_73078_3_,
			int p_73078_4_, int p_73078_5_, int p_73078_6_, int p_73078_7_, float p_73078_8_, float p_73078_9_,
			float p_73078_10_) {
		// CraftBukkit start - Interact
		Block block = p_73078_2_.getBlock(p_73078_4_, p_73078_5_, p_73078_6_);
		boolean isAir = block.isAir(p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_); // Cauldron
		boolean denyResult = false, denyItem = false, denyBlock = false;

		if (!isAir) {
			org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(p_73078_1_,
					org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_,
					p_73078_3_);
			net.minecraftforge.event.entity.player.PlayerInteractEvent forgeEvent = ForgeEventFactory.onPlayerInteract(
					p_73078_1_, net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK,
					p_73078_4_, p_73078_5_, p_73078_6_, p_73078_7_, p_73078_2_);
			// Cauldron start
			// if forge event is explicitly cancelled, return
			if (forgeEvent.isCanceled()) {
				thisPlayerMP.playerNetServerHandler
						.sendPacket(new S23PacketBlockChange(p_73078_4_, p_73078_5_, p_73078_6_, theWorld));
				return false;
			}
			denyItem = event.useItemInHand() == org.bukkit.event.Event.Result.DENY
					|| forgeEvent.useItem == cpw.mods.fml.common.eventhandler.Event.Result.DENY;
			denyBlock = event.useInteractedBlock() == org.bukkit.event.Event.Result.DENY
					|| forgeEvent.useBlock == cpw.mods.fml.common.eventhandler.Event.Result.DENY;
			denyResult = denyItem || denyBlock;
			// if we have no explicit deny, check if item can be used
			if (!denyItem) {
				Item item = p_73078_3_ != null ? p_73078_3_.getItem() : null;
				// try to use an item in hand before activating a block. Used for items such as
				// IC2's wrench.
				if (item != null && item.onItemUseFirst(p_73078_3_, p_73078_1_, p_73078_2_, p_73078_4_, p_73078_5_,
						p_73078_6_, p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_)) {
					if (p_73078_3_.stackSize <= 0) {
						ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, p_73078_3_);
					}
					return true;
				}
			}
			// Cauldron end
			if (denyBlock) {
				// If we denied a door from opening, we need to send a correcting update to the
				// client, as it already opened the door.
				if (block == Blocks.wooden_door) {
					boolean bottom = (p_73078_2_.getBlockMetadata(p_73078_4_, p_73078_5_, p_73078_6_) & 8) == 0;
					((EntityPlayerMP) p_73078_1_).playerNetServerHandler.sendPacket(new S23PacketBlockChange(p_73078_4_,
							p_73078_5_ + (bottom ? 1 : -1), p_73078_6_, p_73078_2_));
				}
			} else if (!p_73078_1_.isSneaking() || p_73078_3_ == null || p_73078_1_.getHeldItem().getItem()
					.doesSneakBypassUse(p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_1_)) {
				denyResult |= block.onBlockActivated(p_73078_2_, p_73078_4_, p_73078_5_, p_73078_6_, p_73078_1_,
						p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_);
				if (thisPlayerMP != null && !(thisPlayerMP.openContainer instanceof ContainerPlayer)) {
					if (thisPlayerMP.openContainer.getBukkitView() == null) {
						TileEntity te = thisPlayerMP.worldObj.getTileEntity(p_73078_4_, p_73078_5_, p_73078_6_);
						CraftEntity bukkitPlayer = thisPlayerMP.getBukkitEntity();
						if (te != null && te instanceof IInventory) {
							thisPlayerMP.openContainer.setBukkitView(new CraftInventoryView((HumanEntity) bukkitPlayer,
									new CraftInventory((IInventory) te), thisPlayerMP.openContainer));
						} else {
							thisPlayerMP.openContainer.setBukkitView(new CraftInventoryView((HumanEntity) bukkitPlayer,
									Bukkit.createInventory((InventoryHolder) bukkitPlayer, InventoryType.CHEST),
									thisPlayerMP.openContainer));
						}
					}
					thisPlayerMP.openContainer = CraftEventFactory.callInventoryOpenEvent(thisPlayerMP,
							thisPlayerMP.openContainer, false);
					if (thisPlayerMP.openContainer == null) {
						thisPlayerMP.openContainer = thisPlayerMP.inventoryContainer;
						return false;
					}
				}
			}

			if (p_73078_3_ != null && !denyResult && p_73078_3_.stackSize > 0) {
				int meta = p_73078_3_.getItemDamage();
				int size = p_73078_3_.stackSize;
				denyResult = p_73078_3_.tryPlaceItemIntoWorld(p_73078_1_, p_73078_2_, p_73078_4_, p_73078_5_,
						p_73078_6_, p_73078_7_, p_73078_8_, p_73078_9_, p_73078_10_);

				// The item count should not decrement in Creative mode.
				if (isCreative()) {
					p_73078_3_.setItemDamage(meta);
					p_73078_3_.stackSize = size;
				}

				if (p_73078_3_.stackSize <= 0) {
					ForgeEventFactory.onPlayerDestroyItem(thisPlayerMP, p_73078_3_);
				}
			}

			// If we have 'true' and no explicit deny *or* an explicit allow -- run the item
			// part of the hook
			if (p_73078_3_ != null && (!denyResult && event.useItemInHand() != org.bukkit.event.Event.Result.DENY
					|| event.useItemInHand() == org.bukkit.event.Event.Result.ALLOW)) {
				tryUseItem(p_73078_1_, p_73078_2_, p_73078_3_);
			}
		}

		return denyResult;
		// CraftBukkit end
	}

	public void setWorld(WorldServer p_73080_1_) {
		theWorld = p_73080_1_;
	}

	public double getBlockReachDistance() {
		return blockReachDistance;
	}

	public void setBlockReachDistance(double distance) {
		blockReachDistance = distance;
	}
}