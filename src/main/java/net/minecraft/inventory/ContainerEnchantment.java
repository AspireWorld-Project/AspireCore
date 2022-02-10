package net.minecraft.inventory;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ContainerEnchantment extends Container {
	public IInventory tableInventory = new InventoryBasic("Enchant", true, 1) {
		private static final String __OBFID = "CL_00001746";

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void markDirty() {
			super.markDirty();
			ContainerEnchantment.this.onCraftMatrixChanged(this);
		}
	};
	private World worldPointer;
	private int posX;
	private int posY;
	private int posZ;
	private Random rand = new Random();
	public long nameSeed;
	public int[] enchantLevels = new int[3];
	private static final String __OBFID = "CL_00001745";
	private Player player;

	public ContainerEnchantment(InventoryPlayer p_i1811_1_, World p_i1811_2_, int p_i1811_3_, int p_i1811_4_,
			int p_i1811_5_) {
		worldPointer = p_i1811_2_;
		posX = p_i1811_3_;
		posY = p_i1811_4_;
		posZ = p_i1811_5_;
		addSlotToContainer(new Slot(tableInventory, 0, 25, 47) {
			private static final String __OBFID = "CL_00001747";

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return true;
			}
		});
		int l;

		for (l = 0; l < 3; ++l) {
			for (int i1 = 0; i1 < 9; ++i1) {
				addSlotToContainer(new Slot(p_i1811_1_, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}

		for (l = 0; l < 9; ++l) {
			addSlotToContainer(new Slot(p_i1811_1_, l, 8 + l * 18, 142));
		}
		player = (Player) p_i1811_1_.player.getBukkitEntity();
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, enchantLevels[0]);
		p_75132_1_.sendProgressBarUpdate(this, 1, enchantLevels[1]);
		p_75132_1_.sendProgressBarUpdate(this, 2, enchantLevels[2]);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);
			icrafting.sendProgressBarUpdate(this, 0, enchantLevels[0]);
			icrafting.sendProgressBarUpdate(this, 1, enchantLevels[1]);
			icrafting.sendProgressBarUpdate(this, 2, enchantLevels[2]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ >= 0 && p_75137_1_ <= 2) {
			enchantLevels[p_75137_1_] = p_75137_2_;
		} else {
			super.updateProgressBar(p_75137_1_, p_75137_2_);
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		if (p_75130_1_ == tableInventory) {
			ItemStack itemstack = p_75130_1_.getStackInSlot(0);
			int i;

			if (itemstack != null) {
				nameSeed = rand.nextLong();

				if (!worldPointer.isRemote) {
					i = 0;
					int j;
					float power = 0;

					for (j = -1; j <= 1; ++j) {
						for (int k = -1; k <= 1; ++k) {
							if ((j != 0 || k != 0) && worldPointer.isAirBlock(posX + k, posY, posZ + j)
									&& worldPointer.isAirBlock(posX + k, posY + 1, posZ + j)) {
								power += ForgeHooks.getEnchantPower(worldPointer, posX + k * 2, posY, posZ + j * 2);
								power += ForgeHooks.getEnchantPower(worldPointer, posX + k * 2, posY + 1, posZ + j * 2);

								if (k != 0 && j != 0) {
									power += ForgeHooks.getEnchantPower(worldPointer, posX + k * 2, posY, posZ + j);
									power += ForgeHooks.getEnchantPower(worldPointer, posX + k * 2, posY + 1, posZ + j);
									power += ForgeHooks.getEnchantPower(worldPointer, posX + k, posY, posZ + j * 2);
									power += ForgeHooks.getEnchantPower(worldPointer, posX + k, posY + 1, posZ + j * 2);
								}
							}
						}
					}

					for (j = 0; j < 3; ++j) {
						enchantLevels[j] = EnchantmentHelper.calcItemStackEnchantability(rand, j, (int) power,
								itemstack);
					}

					if (this.getBukkitView() != null) {
						CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
						PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(),
								worldPointer.getWorld().getBlockAt(posX, posY, posZ), item, enchantLevels, i);
						event.setCancelled(!itemstack.isItemEnchantable());
						Bukkit.getPluginManager().callEvent(event);
						if (event.isCancelled()) {
							for (i = 0; i < 3; ++i) {
								enchantLevels[i] = 0;
							}
							return;
						}
					}

					detectAndSendChanges();
				}
			} else {
				for (i = 0; i < 3; ++i) {
					enchantLevels[i] = 0;
				}
			}
		}
	}

	@Override
	public boolean enchantItem(EntityPlayer p_75140_1_, int p_75140_2_) {
		ItemStack itemstack = tableInventory.getStackInSlot(0);
		if (enchantLevels[p_75140_2_] <= 0 || itemstack == null
				|| p_75140_1_.experienceLevel < enchantLevels[p_75140_2_] && !p_75140_1_.capabilities.isCreativeMode)
			return false;
		else {
			if (!worldPointer.isRemote) {
				List list = EnchantmentHelper.buildEnchantmentList(rand, itemstack, enchantLevels[p_75140_2_]);
				boolean flag = itemstack.getItem() == Items.book;
				if (list != null) {
					Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new java.util.HashMap<>();
					for (Object obj : list) {
						EnchantmentData instance = (EnchantmentData) obj;
						enchants.put(org.bukkit.enchantments.Enchantment.getById(instance.enchantmentobj.effectId),
								instance.enchantmentLevel);
					}

					CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
					EnchantItemEvent event = new EnchantItemEvent((Player) p_75140_1_.getBukkitEntity(),
							this.getBukkitView(), worldPointer.getWorld().getBlockAt(posX, posY, posZ), item,
							enchantLevels[p_75140_2_], enchants, p_75140_2_);
					if (this.getBukkitView() != null) {
						Bukkit.getPluginManager().callEvent(event); // Cauldron - allow vanilla mods to bypass
					}
					int level = event.getExpLevelCost();

					if (event.isCancelled()
							|| level > p_75140_1_.experienceLevel && !p_75140_1_.capabilities.isCreativeMode
							|| enchants.isEmpty())
						return false;
					boolean applied = !flag;
					for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : event.getEnchantsToAdd()
							.entrySet()) {
						try {
							if (flag) {
								int enchantId = entry.getKey().getId();
								if (net.minecraft.enchantment.Enchantment.enchantmentsList[enchantId] == null) {
									continue;
								}
								EnchantmentData enchantment = new EnchantmentData(enchantId, entry.getValue());
								Items.enchanted_book.addEnchantment(itemstack, enchantment);
								applied = true;
								itemstack.func_150996_a(Items.enchanted_book);
								break;
							} else {
								item.addEnchantment(entry.getKey(), entry.getValue());
							}
						} catch (IllegalArgumentException e) {
							/* Just swallow invalid enchantments */
						}
					}
					// Only down level if we've applied the enchantments
					if (applied) {
						p_75140_1_.addExperienceLevel(-level);
					}
					onCraftMatrixChanged(tableInventory);
				}
			}
			return true;
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);

		if (!worldPointer.isRemote) {
			ItemStack itemstack = tableInventory.getStackInSlotOnClosing(0);

			if (itemstack != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return worldPointer.getBlock(posX, posY, posZ) != Blocks.enchanting_table ? false
				: p_75145_1_.getDistanceSq(posX + 0.5D, posY + 0.5D, posZ + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ == 0) {
				if (!mergeItemStack(itemstack1, 1, 37, true))
					return null;
			} else {
				if (((Slot) inventorySlots.get(0)).getHasStack()
						|| !((Slot) inventorySlots.get(0)).isItemValid(itemstack1))
					return null;

				if (itemstack1.hasTagCompound() && itemstack1.stackSize == 1) {
					((Slot) inventorySlots.get(0)).putStack(itemstack1.copy());
					itemstack1.stackSize = 0;
				} else if (itemstack1.stackSize >= 1) {
					((Slot) inventorySlots.get(0))
							.putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getItemDamage()));
					--itemstack1.stackSize;
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
				return null;

			slot.onPickupFromSlot(p_82846_1_, itemstack1);
		}

		return itemstack;
	}
}