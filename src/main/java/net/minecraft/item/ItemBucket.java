package net.minecraft.item;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class ItemBucket extends Item {
	private final Block isFull;
	private static final String __OBFID = "CL_00000000";

	public ItemBucket(Block p_i45331_1_) {
		maxStackSize = 1;
		isFull = p_i45331_1_;
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		boolean flag = isFull == Blocks.air;
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, player, flag);
		if (movingobjectposition == null)
			return itemStack;
		else {
			FillBucketEvent event = new FillBucketEvent(player, itemStack, world, movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event))
				return itemStack;
			else if (event.getResult() == Event.Result.ALLOW) {
				if (player.capabilities.isCreativeMode)
					return itemStack;
				else if (--itemStack.stackSize <= 0)
					return event.result;
				else {
					if (!player.inventory.addItemStackToInventory(event.result)) {
						player.dropPlayerItemWithRandomChoice(event.result, false);
					}
					return itemStack;
				}
			} else {
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;
					if (!world.canMineBlock(player, i, j, k))
						return itemStack;
					if (flag) {
						if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
							return itemStack;
						Material material = world.getBlock(i, j, k).getMaterial();
						int l = world.getBlockMetadata(i, j, k);
						if (material == Material.water && l == 0) {
							PlayerBucketFillEvent fillEvent = CraftEventFactory.callPlayerBucketFillEvent(player, i, j,
									k, -1, itemStack, Items.water_bucket);
							if (fillEvent.isCancelled())
								return itemStack;
							world.setBlockToAir(i, j, k);
							return func_150910_a(itemStack, player, Items.water_bucket, fillEvent.getItemStack());
						}
						if (material == Material.lava && l == 0) {
							PlayerBucketFillEvent fillEvent = CraftEventFactory.callPlayerBucketFillEvent(player, i, j,
									k, -1, itemStack, Items.lava_bucket);
							if (fillEvent.isCancelled())
								return itemStack;
							world.setBlockToAir(i, j, k);
							return func_150910_a(itemStack, player, Items.lava_bucket, fillEvent.getItemStack());
						}
					} else {
						if (isFull == Blocks.air) {
							PlayerBucketEmptyEvent cbEvent = CraftEventFactory.callPlayerBucketEmptyEvent(player, i, j,
									k, movingobjectposition.sideHit, itemStack);
							if (cbEvent.isCancelled())
								return itemStack;
							return CraftItemStack.asNMSCopy(cbEvent.getItemStack());
						}
						int clickedX = i, clickedY = j, clickedZ = k;
						if (movingobjectposition.sideHit == 0) {
							--j;
						}
						if (movingobjectposition.sideHit == 1) {
							++j;
						}
						if (movingobjectposition.sideHit == 2) {
							--k;
						}
						if (movingobjectposition.sideHit == 3) {
							++k;
						}
						if (movingobjectposition.sideHit == 4) {
							--i;
						}
						if (movingobjectposition.sideHit == 5) {
							++i;
						}
						if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack))
							return itemStack;
						PlayerBucketEmptyEvent cbEvent = CraftEventFactory.callPlayerBucketEmptyEvent(player, clickedX,
								clickedY, clickedZ, movingobjectposition.sideHit, itemStack);
						if (cbEvent.isCancelled())
							return itemStack;
						if (tryPlaceContainedLiquid(world, i, j, k) && !player.capabilities.isCreativeMode)
							return CraftItemStack.asNMSCopy(cbEvent.getItemStack());
					}
				}
				return itemStack;
			}
		}
	}

	private ItemStack func_150910_a(ItemStack itemstack, EntityPlayer entityplayer, Item item,
			org.bukkit.inventory.ItemStack result) {
		if (entityplayer.capabilities.isCreativeMode)
			return itemstack;
		else if (--itemstack.stackSize <= 0)
			return CraftItemStack.asNMSCopy(result);
		else {
			if (!entityplayer.inventory.addItemStackToInventory(CraftItemStack.asNMSCopy(result))) {
				entityplayer.dropPlayerItemWithRandomChoice(CraftItemStack.asNMSCopy(result), false);
			}
			return itemstack;
		}
	}

	public boolean tryPlaceContainedLiquid(World p_77875_1_, int p_77875_2_, int p_77875_3_, int p_77875_4_) {
		if (isFull == Blocks.air)
			return false;
		else {
			Material material = p_77875_1_.getBlock(p_77875_2_, p_77875_3_, p_77875_4_).getMaterial();
			boolean flag = !material.isSolid();

			if (!p_77875_1_.isAirBlock(p_77875_2_, p_77875_3_, p_77875_4_) && !flag)
				return false;
			else {
				if (p_77875_1_.provider.isHellWorld && isFull == Blocks.flowing_water) {
					p_77875_1_.playSoundEffect(p_77875_2_ + 0.5F, p_77875_3_ + 0.5F, p_77875_4_ + 0.5F, "random.fizz",
							0.5F, 2.6F + (p_77875_1_.rand.nextFloat() - p_77875_1_.rand.nextFloat()) * 0.8F);

					for (int l = 0; l < 8; ++l) {
						p_77875_1_.spawnParticle("largesmoke", p_77875_2_ + Math.random(), p_77875_3_ + Math.random(),
								p_77875_4_ + Math.random(), 0.0D, 0.0D, 0.0D);
					}
				} else {
					if (!p_77875_1_.isRemote && flag && !material.isLiquid()) {
						p_77875_1_.func_147480_a(p_77875_2_, p_77875_3_, p_77875_4_, true);
					}

					p_77875_1_.setBlock(p_77875_2_, p_77875_3_, p_77875_4_, isFull, 0, 3);
				}

				return true;
			}
		}
	}
}