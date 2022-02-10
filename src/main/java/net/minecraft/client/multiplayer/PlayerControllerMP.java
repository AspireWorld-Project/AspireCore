package net.minecraft.client.multiplayer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

@SideOnly(Side.CLIENT)
public class PlayerControllerMP {
	private final Minecraft mc;
	private final NetHandlerPlayClient netClientHandler;
	private int currentBlockX = -1;
	private int currentBlockY = -1;
	private int currentblockZ = -1;
	private ItemStack currentItemHittingBlock;
	private float curBlockDamageMP;
	private float stepSoundTickCounter;
	private int blockHitDelay;
	private boolean isHittingBlock;
	private WorldSettings.GameType currentGameType;
	private int currentPlayerItem;
	private static final String __OBFID = "CL_00000881";

	public PlayerControllerMP(Minecraft p_i45062_1_, NetHandlerPlayClient p_i45062_2_) {
		currentGameType = WorldSettings.GameType.SURVIVAL;
		mc = p_i45062_1_;
		netClientHandler = p_i45062_2_;
	}

	public static void clickBlockCreative(Minecraft p_78744_0_, PlayerControllerMP p_78744_1_, int p_78744_2_,
			int p_78744_3_, int p_78744_4_, int p_78744_5_) {
		if (!p_78744_0_.theWorld.extinguishFire(p_78744_0_.thePlayer, p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_)) {
			p_78744_1_.onPlayerDestroyBlock(p_78744_2_, p_78744_3_, p_78744_4_, p_78744_5_);
		}
	}

	public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
		currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
	}

	public boolean enableEverythingIsScrewedUpMode() {
		return false;
	}

	public void setGameType(WorldSettings.GameType p_78746_1_) {
		currentGameType = p_78746_1_;
		currentGameType.configurePlayerCapabilities(mc.thePlayer.capabilities);
	}

	public void flipPlayer(EntityPlayer p_78745_1_) {
		p_78745_1_.rotationYaw = -180.0F;
	}

	public boolean shouldDrawHUD() {
		return currentGameType.isSurvivalOrAdventure();
	}

	public boolean onPlayerDestroyBlock(int p_78751_1_, int p_78751_2_, int p_78751_3_, int p_78751_4_) {
		ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
		if (stack != null && stack.getItem() != null
				&& stack.getItem().onBlockStartBreak(stack, p_78751_1_, p_78751_2_, p_78751_3_, mc.thePlayer))
			return false;

		if (currentGameType.isAdventure()
				&& !mc.thePlayer.isCurrentToolAdventureModeExempt(p_78751_1_, p_78751_2_, p_78751_3_))
			return false;
		else if (currentGameType.isCreative() && mc.thePlayer.getHeldItem() != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
			return false;
		else {
			WorldClient worldclient = mc.theWorld;
			Block block = worldclient.getBlock(p_78751_1_, p_78751_2_, p_78751_3_);

			if (block.getMaterial() == Material.air)
				return false;
			else {
				worldclient.playAuxSFX(2001, p_78751_1_, p_78751_2_, p_78751_3_, Block.getIdFromBlock(block)
						+ (worldclient.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_) << 12));
				int i1 = worldclient.getBlockMetadata(p_78751_1_, p_78751_2_, p_78751_3_);
				boolean flag = block.removedByPlayer(worldclient, mc.thePlayer, p_78751_1_, p_78751_2_, p_78751_3_);

				if (flag) {
					block.onBlockDestroyedByPlayer(worldclient, p_78751_1_, p_78751_2_, p_78751_3_, i1);
				}

				currentBlockY = -1;

				if (!currentGameType.isCreative()) {
					ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();

					if (itemstack != null) {
						itemstack.func_150999_a(worldclient, block, p_78751_1_, p_78751_2_, p_78751_3_, mc.thePlayer);

						if (itemstack.stackSize == 0) {
							mc.thePlayer.destroyCurrentEquippedItem();
						}
					}
				}

				return flag;
			}
		}
	}

	public void clickBlock(int p_78743_1_, int p_78743_2_, int p_78743_3_, int p_78743_4_) {
		if (!currentGameType.isAdventure()
				|| mc.thePlayer.isCurrentToolAdventureModeExempt(p_78743_1_, p_78743_2_, p_78743_3_)) {
			if (currentGameType.isCreative()) {
				netClientHandler
						.addToSendQueue(new C07PacketPlayerDigging(0, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
				clickBlockCreative(mc, this, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_);
				blockHitDelay = 5;
			} else if (!isHittingBlock || !sameToolAndBlock(p_78743_1_, p_78743_2_, p_78743_3_)) {
				if (isHittingBlock) {
					netClientHandler.addToSendQueue(
							new C07PacketPlayerDigging(1, currentBlockX, currentBlockY, currentblockZ, p_78743_4_));
				}

				netClientHandler
						.addToSendQueue(new C07PacketPlayerDigging(0, p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_));
				Block block = mc.theWorld.getBlock(p_78743_1_, p_78743_2_, p_78743_3_);
				boolean flag = block.getMaterial() != Material.air;

				if (flag && curBlockDamageMP == 0.0F) {
					block.onBlockClicked(mc.theWorld, p_78743_1_, p_78743_2_, p_78743_3_, mc.thePlayer);
				}

				if (flag && block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.thePlayer.worldObj, p_78743_1_,
						p_78743_2_, p_78743_3_) >= 1.0F) {
					onPlayerDestroyBlock(p_78743_1_, p_78743_2_, p_78743_3_, p_78743_4_);
				} else {
					isHittingBlock = true;
					currentBlockX = p_78743_1_;
					currentBlockY = p_78743_2_;
					currentblockZ = p_78743_3_;
					currentItemHittingBlock = mc.thePlayer.getHeldItem();
					curBlockDamageMP = 0.0F;
					stepSoundTickCounter = 0.0F;
					mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.getEntityId(), currentBlockX, currentBlockY,
							currentblockZ, (int) (curBlockDamageMP * 10.0F) - 1);
				}
			}
		}
	}

	public void resetBlockRemoving() {
		if (isHittingBlock) {
			netClientHandler
					.addToSendQueue(new C07PacketPlayerDigging(1, currentBlockX, currentBlockY, currentblockZ, -1));
		}

		isHittingBlock = false;
		curBlockDamageMP = 0.0F;
		mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.getEntityId(), currentBlockX, currentBlockY,
				currentblockZ, -1);
	}

	public void onPlayerDamageBlock(int p_78759_1_, int p_78759_2_, int p_78759_3_, int p_78759_4_) {
		syncCurrentPlayItem();

		if (blockHitDelay > 0) {
			--blockHitDelay;
		} else if (currentGameType.isCreative()) {
			blockHitDelay = 5;
			netClientHandler
					.addToSendQueue(new C07PacketPlayerDigging(0, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
			clickBlockCreative(mc, this, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
		} else {
			if (sameToolAndBlock(p_78759_1_, p_78759_2_, p_78759_3_)) {
				Block block = mc.theWorld.getBlock(p_78759_1_, p_78759_2_, p_78759_3_);

				if (block.getMaterial() == Material.air) {
					isHittingBlock = false;
					return;
				}

				curBlockDamageMP += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.thePlayer.worldObj,
						p_78759_1_, p_78759_2_, p_78759_3_);

				if (stepSoundTickCounter % 4.0F == 0.0F) {
					mc.getSoundHandler()
							.playSound(new PositionedSoundRecord(
									new ResourceLocation(block.stepSound.getStepResourcePath()),
									(block.stepSound.getVolume() + 1.0F) / 8.0F, block.stepSound.getPitch() * 0.5F,
									p_78759_1_ + 0.5F, p_78759_2_ + 0.5F, p_78759_3_ + 0.5F));
				}

				++stepSoundTickCounter;

				if (curBlockDamageMP >= 1.0F) {
					isHittingBlock = false;
					netClientHandler.addToSendQueue(
							new C07PacketPlayerDigging(2, p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_));
					onPlayerDestroyBlock(p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
					curBlockDamageMP = 0.0F;
					stepSoundTickCounter = 0.0F;
					blockHitDelay = 5;
				}

				mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.getEntityId(), currentBlockX, currentBlockY,
						currentblockZ, (int) (curBlockDamageMP * 10.0F) - 1);
			} else {
				clickBlock(p_78759_1_, p_78759_2_, p_78759_3_, p_78759_4_);
			}
		}
	}

	public float getBlockReachDistance() {
		return currentGameType.isCreative() ? 5.0F : 4.5F;
	}

	public void updateController() {
		syncCurrentPlayItem();

		if (netClientHandler.getNetworkManager().isChannelOpen()) {
			netClientHandler.getNetworkManager().processReceivedPackets();
		} else if (netClientHandler.getNetworkManager().getExitMessage() != null) {
			netClientHandler.getNetworkManager().getNetHandler()
					.onDisconnect(netClientHandler.getNetworkManager().getExitMessage());
		} else {
			netClientHandler.getNetworkManager().getNetHandler()
					.onDisconnect(new ChatComponentText("Disconnected from server"));
		}
	}

	private boolean sameToolAndBlock(int p_85182_1_, int p_85182_2_, int p_85182_3_) {
		ItemStack itemstack = mc.thePlayer.getHeldItem();
		boolean flag = currentItemHittingBlock == null && itemstack == null;

		if (currentItemHittingBlock != null && itemstack != null) {
			flag = itemstack.getItem() == currentItemHittingBlock.getItem()
					&& ItemStack.areItemStackTagsEqual(itemstack, currentItemHittingBlock)
					&& (itemstack.isItemStackDamageable()
							|| itemstack.getItemDamage() == currentItemHittingBlock.getItemDamage());
		}

		return p_85182_1_ == currentBlockX && p_85182_2_ == currentBlockY && p_85182_3_ == currentblockZ && flag;
	}

	private void syncCurrentPlayItem() {
		int i = mc.thePlayer.inventory.currentItem;

		if (i != currentPlayerItem) {
			currentPlayerItem = i;
			netClientHandler.addToSendQueue(new C09PacketHeldItemChange(currentPlayerItem));
		}
	}

	public boolean onPlayerRightClick(EntityPlayer p_78760_1_, World p_78760_2_, ItemStack p_78760_3_, int p_78760_4_,
			int p_78760_5_, int p_78760_6_, int p_78760_7_, Vec3 p_78760_8_) {
		syncCurrentPlayItem();
		float f = (float) p_78760_8_.xCoord - p_78760_4_;
		float f1 = (float) p_78760_8_.yCoord - p_78760_5_;
		float f2 = (float) p_78760_8_.zCoord - p_78760_6_;
		boolean flag = false;

		if (p_78760_3_ != null && p_78760_3_.getItem() != null && p_78760_3_.getItem().onItemUseFirst(p_78760_3_,
				p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, f, f1, f2))
			return true;

		if (!p_78760_1_.isSneaking() || p_78760_1_.getHeldItem() == null || p_78760_1_.getHeldItem().getItem()
				.doesSneakBypassUse(p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_1_)) {
			flag = p_78760_2_.getBlock(p_78760_4_, p_78760_5_, p_78760_6_).onBlockActivated(p_78760_2_, p_78760_4_,
					p_78760_5_, p_78760_6_, p_78760_1_, p_78760_7_, f, f1, f2);
		}

		if (!flag && p_78760_3_ != null && p_78760_3_.getItem() instanceof ItemBlock) {
			ItemBlock itemblock = (ItemBlock) p_78760_3_.getItem();

			if (!itemblock.func_150936_a(p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_, p_78760_7_, p_78760_1_,
					p_78760_3_))
				return false;
		}

		netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(p_78760_4_, p_78760_5_, p_78760_6_,
				p_78760_7_, p_78760_1_.inventory.getCurrentItem(), f, f1, f2));

		if (flag)
			return true;
		else if (p_78760_3_ == null)
			return false;
		else if (currentGameType.isCreative()) {
			int j1 = p_78760_3_.getItemDamage();
			int i1 = p_78760_3_.stackSize;
			boolean flag1 = p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_,
					p_78760_7_, f, f1, f2);
			p_78760_3_.setItemDamage(j1);
			p_78760_3_.stackSize = i1;
			return flag1;
		} else {
			if (!p_78760_3_.tryPlaceItemIntoWorld(p_78760_1_, p_78760_2_, p_78760_4_, p_78760_5_, p_78760_6_,
					p_78760_7_, f, f1, f2))
				return false;
			if (p_78760_3_.stackSize <= 0) {
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(p_78760_1_, p_78760_3_));
			}
			return true;
		}
	}

	public boolean sendUseItem(EntityPlayer p_78769_1_, World p_78769_2_, ItemStack p_78769_3_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255,
				p_78769_1_.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
		int i = p_78769_3_.stackSize;
		ItemStack itemstack1 = p_78769_3_.useItemRightClick(p_78769_2_, p_78769_1_);

		if (itemstack1 == p_78769_3_ && (itemstack1 == null || itemstack1.stackSize == i))
			return false;
		else {
			p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = itemstack1;

			if (itemstack1.stackSize <= 0) {
				p_78769_1_.inventory.mainInventory[p_78769_1_.inventory.currentItem] = null;
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(p_78769_1_, itemstack1));
			}

			return true;
		}
	}

	public EntityClientPlayerMP func_147493_a(World p_147493_1_, StatFileWriter p_147493_2_) {
		return new EntityClientPlayerMP(mc, p_147493_1_, mc.getSession(), netClientHandler, p_147493_2_);
	}

	public void attackEntity(EntityPlayer p_78764_1_, Entity p_78764_2_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78764_2_, C02PacketUseEntity.Action.ATTACK));
		p_78764_1_.attackTargetEntityWithCurrentItem(p_78764_2_);
	}

	public boolean interactWithEntitySendPacket(EntityPlayer p_78768_1_, Entity p_78768_2_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C02PacketUseEntity(p_78768_2_, C02PacketUseEntity.Action.INTERACT));
		return p_78768_1_.interactWith(p_78768_2_);
	}

	public ItemStack windowClick(int p_78753_1_, int p_78753_2_, int p_78753_3_, int p_78753_4_,
			EntityPlayer p_78753_5_) {
		short short1 = p_78753_5_.openContainer.getNextTransactionID(p_78753_5_.inventory);
		ItemStack itemstack = p_78753_5_.openContainer.slotClick(p_78753_2_, p_78753_3_, p_78753_4_, p_78753_5_);
		netClientHandler.addToSendQueue(
				new C0EPacketClickWindow(p_78753_1_, p_78753_2_, p_78753_3_, p_78753_4_, itemstack, short1));
		return itemstack;
	}

	public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
		netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
	}

	public void sendSlotPacket(ItemStack p_78761_1_, int p_78761_2_) {
		if (currentGameType.isCreative()) {
			netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(p_78761_2_, p_78761_1_));
		}
	}

	public void sendPacketDropItem(ItemStack p_78752_1_) {
		if (currentGameType.isCreative() && p_78752_1_ != null) {
			netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, p_78752_1_));
		}
	}

	public void onStoppedUsingItem(EntityPlayer p_78766_1_) {
		syncCurrentPlayItem();
		netClientHandler.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
		p_78766_1_.stopUsingItem();
	}

	public boolean gameIsSurvivalOrAdventure() {
		return currentGameType.isSurvivalOrAdventure();
	}

	public boolean isNotCreative() {
		return !currentGameType.isCreative();
	}

	public boolean isInCreativeMode() {
		return currentGameType.isCreative();
	}

	public boolean extendedReach() {
		return currentGameType.isCreative();
	}

	public boolean func_110738_j() {
		return mc.thePlayer.isRiding() && mc.thePlayer.ridingEntity instanceof EntityHorse;
	}
}