package net.minecraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.ultramine.bukkit.api.IInventoryTransactionProvider;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Container {
	public List inventoryItemStacks = new ArrayList();
	public List inventorySlots = new ArrayList();
	public int windowId;
	@SideOnly(Side.CLIENT)
	private short transactionID;
	private int field_94535_f = -1;
	private int field_94536_g;
	private final Set field_94537_h = new HashSet();
	protected List crafters = new ArrayList();
	private final Set playerList = new HashSet();
	private static final String __OBFID = "CL_00001730";

	protected Slot addSlotToContainer(Slot p_75146_1_) {
		p_75146_1_.slotNumber = inventorySlots.size();
		inventorySlots.add(p_75146_1_);
		inventoryItemStacks.add(null);
		return p_75146_1_;
	}

	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		if (crafters.contains(p_75132_1_))
			throw new IllegalArgumentException("Listener already listening");
		else {
			crafters.add(p_75132_1_);
			p_75132_1_.sendContainerAndContentsToPlayer(this, getInventory());
			detectAndSendChanges();
		}
	}

	public List getInventory() {
		ArrayList arraylist = new ArrayList();

		for (int i = 0; i < inventorySlots.size(); ++i) {
			arraylist.add(((Slot) inventorySlots.get(i)).getStack());
		}

		return arraylist;
	}

	@SideOnly(Side.CLIENT)
	public void removeCraftingFromCrafters(ICrafting p_82847_1_) {
		crafters.remove(p_82847_1_);
	}

	public void detectAndSendChanges() {
		for (int i = 0; i < inventorySlots.size(); ++i) {
			ItemStack itemstack = ((Slot) inventorySlots.get(i)).getStack();
			ItemStack itemstack1 = (ItemStack) inventoryItemStacks.get(i);

			if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
				itemstack1 = itemstack == null ? null : itemstack.copy();
				inventoryItemStacks.set(i, itemstack1);

				for (int j = 0; j < crafters.size(); ++j) {
					((ICrafting) crafters.get(j)).sendSlotContents(this, i, itemstack1);
				}
			}
		}
	}

	public boolean enchantItem(EntityPlayer p_75140_1_, int p_75140_2_) {
		return false;
	}

	public Slot getSlotFromInventory(IInventory p_75147_1_, int p_75147_2_) {
		for (int j = 0; j < inventorySlots.size(); ++j) {
			Slot slot = (Slot) inventorySlots.get(j);

			if (slot.isSlotInInventory(p_75147_1_, p_75147_2_))
				return slot;
		}

		return null;
	}

	public Slot getSlot(int p_75139_1_) {
		return (Slot) inventorySlots.get(p_75139_1_);
	}

	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		Slot slot = (Slot) inventorySlots.get(p_82846_2_);
		return slot != null ? slot.getStack() : null;
	}

	public ItemStack slotClick(int index, int p_75144_2_, int p_75144_3_, EntityPlayer player) {
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = player.inventory;
		int i1;
		ItemStack itemstack3;
		ItemStack itemstack4;
		int j1;
		if (p_75144_3_ == 5) {
			int l = field_94536_g;
			field_94536_g = func_94532_c(p_75144_2_);
			if ((l != 1 || field_94536_g != 2) && l != field_94536_g) {
				func_94533_d();
			} else if (inventoryplayer.getItemStack() == null) {
				func_94533_d();
			} else if (field_94536_g == 0) {
				field_94535_f = func_94529_b(p_75144_2_);
				if (func_94528_d(field_94535_f)) {
					field_94536_g = 1;
					field_94537_h.clear();
				} else {
					func_94533_d();
				}
			} else if (field_94536_g == 1) {
				Slot slot = (Slot) inventorySlots.get(index);
				if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true)
						&& slot.isItemValid(inventoryplayer.getItemStack())
						&& inventoryplayer.getItemStack().stackSize > field_94537_h.size() && canDragIntoSlot(slot)) {
					field_94537_h.add(slot);
				}
			} else if (field_94536_g == 2) {
				if (!field_94537_h.isEmpty()) {
					itemstack3 = inventoryplayer.getItemStack().copy();
					i1 = inventoryplayer.getItemStack().stackSize;
					Map<Integer, ItemStack> draggedSlots = new HashMap<>(); // CraftBukkit - Store
																			// slots from drag in
																			// map (raw slot id ->
																			// new stack)
					for (Object aField_94537_h : field_94537_h) {
						Slot slot1 = (Slot) aField_94537_h;
						if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true)
								&& slot1.isItemValid(inventoryplayer.getItemStack())
								&& inventoryplayer.getItemStack().stackSize >= field_94537_h.size()
								&& canDragIntoSlot(slot1)) {
							itemstack4 = itemstack3.copy();
							j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							func_94525_a(field_94537_h, field_94535_f, itemstack4, j1);
							if (itemstack4.stackSize > itemstack4.getMaxStackSize()) {
								itemstack4.stackSize = itemstack4.getMaxStackSize();
							}

							if (itemstack4.stackSize > slot1.getSlotStackLimit()) {
								itemstack4.stackSize = slot1.getSlotStackLimit();
							}

							i1 -= itemstack4.stackSize - j1;
							draggedSlots.put(slot1.slotNumber, itemstack4); // CraftBukkit - Put in map instead of
																			// setting
						}
					}

					// CraftBukkit start - InventoryDragEvent
					InventoryView view = getBukkitView();
					org.bukkit.inventory.ItemStack newcursor = CraftItemStack.asCraftMirror(itemstack3);
					newcursor.setAmount(i1);
					Map<Integer, org.bukkit.inventory.ItemStack> eventmap = new HashMap<>();
					for (Map.Entry<Integer, ItemStack> ditem : draggedSlots.entrySet()) {
						eventmap.put(ditem.getKey(), CraftItemStack.asBukkitCopy(ditem.getValue()));
					}

					// It's essential that we set the cursor to the new value here to prevent item
					// duplication if a plugin closes the inventory.
					ItemStack oldCursor = inventoryplayer.getItemStack();
					inventoryplayer.setItemStack(CraftItemStack.asNMSCopy(newcursor));
					InventoryDragEvent event = new InventoryDragEvent(view,
							newcursor.getType() != org.bukkit.Material.AIR ? newcursor : null,
							CraftItemStack.asBukkitCopy(oldCursor), field_94535_f == i1, eventmap); // Should be
																									// dragButton
					Bukkit.getPluginManager().callEvent(event);
					// Whether or not a change was made to the inventory that requires an update.
					boolean needsUpdate = event.getResult() != Event.Result.DEFAULT;

					if (event.getResult() != Event.Result.DENY) {
						for (Map.Entry<Integer, ItemStack> dslot : draggedSlots.entrySet())
							if (view != null) {
								view.setItem(dslot.getKey(), CraftItemStack.asBukkitCopy(dslot.getValue()));
							}
						// The only time the carried item will be set to null is if the inventory is
						// closed by the server.
						// If the inventory is closed by the server, then the cursor items are dropped.
						// This is why we change the cursor early.
						if (inventoryplayer.getItemStack() != null) {
							inventoryplayer.setItemStack(CraftItemStack.asNMSCopy(event.getCursor()));
							needsUpdate = true;
						}
					} else {
						inventoryplayer.setItemStack(oldCursor);
					}
					if (needsUpdate && player instanceof EntityPlayerMP) {
						((EntityPlayerMP) player).sendContainerToPlayer(this);
						// CraftBukkit end
					}
				}
				func_94533_d();
			} else {
				func_94533_d();
			}
		} else if (field_94536_g != 0) {
			func_94533_d();
		} else {
			Slot slot2;
			int l1;
			ItemStack itemstack5;
			if ((p_75144_3_ == 0 || p_75144_3_ == 1) && (p_75144_2_ == 0 || p_75144_2_ == 1)) {
				if (index == -999) {
					if (inventoryplayer.getItemStack() != null) {
						if (p_75144_2_ == 0) {
							player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
							inventoryplayer.setItemStack(null);
						}

						if (p_75144_2_ == 1) {
							player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);
							if (inventoryplayer.getItemStack().stackSize == 0) {
								inventoryplayer.setItemStack(null);
							}
						}
					}
				} else if (p_75144_3_ == 1) {
					if (index < 0)
						return null;
					slot2 = (Slot) inventorySlots.get(index);
					if (slot2 != null && slot2.canTakeStack(player)) {
						itemstack3 = transferStackInSlot(player, index);
						if (itemstack3 != null) {
							Item item = itemstack3.getItem();
							itemstack = itemstack3.copy();
							if (slot2.getStack() != null && slot2.getStack().getItem() == item) {
								retrySlotClick(index, p_75144_2_, true, player);
							}
						}
					}
				} else {
					if (index < 0)
						return null;

					slot2 = (Slot) inventorySlots.get(index);
					if (slot2 != null) {
						itemstack3 = slot2.getStack();
						itemstack4 = inventoryplayer.getItemStack();
						if (itemstack3 != null) {
							itemstack = itemstack3.copy();
						}

						if (itemstack3 == null) {
							if (itemstack4 != null && slot2.isItemValid(itemstack4)) {
								l1 = p_75144_2_ == 0 ? itemstack4.stackSize : 1;
								if (l1 > slot2.getSlotStackLimit()) {
									l1 = slot2.getSlotStackLimit();
								}

								if (itemstack4.stackSize >= l1) {
									slot2.putStack(itemstack4.splitStack(l1));
								}

								if (itemstack4.stackSize == 0) {
									inventoryplayer.setItemStack(null);
								}
							}
						} else if (slot2.canTakeStack(player)) {
							if (itemstack4 == null) {
								l1 = p_75144_2_ == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
								itemstack5 = slot2.decrStackSize(l1);
								inventoryplayer.setItemStack(itemstack5);
								if (itemstack3.stackSize == 0) {
									slot2.putStack(null);
								}

								slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
							} else if (slot2.isItemValid(itemstack4)) {
								if (itemstack3.getItem() == itemstack4.getItem()
										&& itemstack3.getItemDamage() == itemstack4.getItemDamage()
										&& ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {
									l1 = p_75144_2_ == 0 ? itemstack4.stackSize : 1;
									if (l1 > slot2.getSlotStackLimit() - itemstack3.stackSize) {
										l1 = slot2.getSlotStackLimit() - itemstack3.stackSize;
									}

									if (l1 > itemstack4.getMaxStackSize() - itemstack3.stackSize) {
										l1 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
									}

									itemstack4.splitStack(l1);
									if (itemstack4.stackSize == 0) {
										inventoryplayer.setItemStack(null);
									}

									itemstack3.stackSize += l1;
								} else if (itemstack4.stackSize <= slot2.getSlotStackLimit()) {
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack3);
								}
							} else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1
									&& (!itemstack3.getHasSubtypes()
											|| itemstack3.getItemDamage() == itemstack4.getItemDamage())
									&& ItemStack.areItemStackTagsEqual(itemstack3, itemstack4)) {
								l1 = itemstack3.stackSize;
								if (l1 > 0 && l1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {
									itemstack4.stackSize += l1;
									itemstack3 = slot2.decrStackSize(l1);
									if (itemstack3.stackSize == 0) {
										slot2.putStack(null);
									}

									slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			} else if (p_75144_3_ == 2 && p_75144_2_ >= 0 && p_75144_2_ < 9) {
				slot2 = (Slot) inventorySlots.get(index);
				if (slot2.canTakeStack(player)) {
					itemstack3 = inventoryplayer.getStackInSlot(p_75144_2_);
					boolean flag = itemstack3 == null
							|| slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack3);
					l1 = -1;
					if (!flag) {
						l1 = inventoryplayer.getFirstEmptyStack();
						flag |= l1 > -1;
					}

					if (slot2.getHasStack() && flag) {
						itemstack5 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(p_75144_2_, itemstack5.copy());
						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack3))
								&& itemstack3 != null) {
							if (l1 > -1) {
								inventoryplayer.addItemStackToInventory(itemstack3);
								slot2.decrStackSize(itemstack5.stackSize);
								slot2.putStack(null);
								slot2.onPickupFromSlot(player, itemstack5);
							}
						} else {
							slot2.decrStackSize(itemstack5.stackSize);
							slot2.putStack(itemstack3);
							slot2.onPickupFromSlot(player, itemstack5);
						}
					} else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3)) {
						inventoryplayer.setInventorySlotContents(p_75144_2_, null);
						slot2.putStack(itemstack3);
					}
				}
			} else if (p_75144_3_ == 3 && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null
					&& index >= 0) {
				slot2 = (Slot) inventorySlots.get(index);
				if (slot2 != null && slot2.getHasStack()) {
					itemstack3 = slot2.getStack().copy();
					itemstack3.stackSize = itemstack3.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack3);
				}
			} else if (p_75144_3_ == 4 && inventoryplayer.getItemStack() == null && index >= 0) {
				slot2 = (Slot) inventorySlots.get(index);
				if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player)) {
					itemstack3 = slot2.decrStackSize(p_75144_2_ == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(player, itemstack3);
					player.dropPlayerItemWithRandomChoice(itemstack3, true);
				}
			} else if (p_75144_3_ == 6 && index >= 0) {
				slot2 = (Slot) inventorySlots.get(index);
				itemstack3 = inventoryplayer.getItemStack();
				if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
					i1 = p_75144_2_ == 0 ? 0 : inventorySlots.size() - 1;
					l1 = p_75144_2_ == 0 ? 1 : -1;

					for (int i2 = 0; i2 < 2; ++i2) {
						for (j1 = i1; j1 >= 0 && j1 < inventorySlots.size()
								&& itemstack3.stackSize < itemstack3.getMaxStackSize(); j1 += l1) {
							Slot slot3 = (Slot) inventorySlots.get(j1);
							if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true)
									&& slot3.canTakeStack(player) && func_94530_a(itemstack3, slot3)
									&& (i2 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize())) {
								int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize,
										slot3.getStack().stackSize);
								ItemStack itemstack2 = slot3.decrStackSize(k1);
								itemstack3.stackSize += k1;
								if (itemstack2.stackSize <= 0) {
									slot3.putStack(null);
								}
								slot3.onPickupFromSlot(player, itemstack2);
							}
						}
					}
				}
				detectAndSendChanges();
			}
		}
		return itemstack;
	}

	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
		return true;
	}

	protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
		slotClick(p_75133_1_, p_75133_2_, 1, p_75133_4_);
	}

	public void onContainerClosed(EntityPlayer p_75134_1_) {
		InventoryPlayer inventoryplayer = p_75134_1_.inventory;

		if (inventoryplayer.getItemStack() != null) {
			p_75134_1_.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
			inventoryplayer.setItemStack(null);
		}
	}

	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		detectAndSendChanges();
	}

	public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_) {
		getSlot(p_75141_1_).putStack(p_75141_2_);
	}

	@SideOnly(Side.CLIENT)
	public void putStacksInSlots(ItemStack[] p_75131_1_) {
		for (int i = 0; i < p_75131_1_.length; ++i) {
			getSlot(i).putStack(p_75131_1_[i]);
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
	}

	@SideOnly(Side.CLIENT)
	public short getNextTransactionID(InventoryPlayer p_75136_1_) {
		++transactionID;
		return transactionID;
	}

	public boolean isPlayerNotUsingContainer(EntityPlayer p_75129_1_) {
		return !playerList.contains(p_75129_1_);
	}

	public void setPlayerIsPresent(EntityPlayer p_75128_1_, boolean p_75128_2_) {
		if (p_75128_2_) {
			playerList.remove(p_75128_1_);
		} else {
			playerList.add(p_75128_1_);
		}
	}

	public abstract boolean canInteractWith(EntityPlayer p_75145_1_);

	protected boolean mergeItemStack(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
		boolean flag1 = false;
		int k = p_75135_2_;

		if (p_75135_4_) {
			k = p_75135_3_ - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (p_75135_1_.isStackable()) {
			while (p_75135_1_.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem()
						&& (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage())
						&& ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1)) {
					int l = itemstack1.stackSize + p_75135_1_.stackSize;

					if (l <= p_75135_1_.getMaxStackSize()) {
						p_75135_1_.stackSize = 0;
						itemstack1.stackSize = l;
						slot.onSlotChanged();
						flag1 = true;
					} else if (itemstack1.stackSize < p_75135_1_.getMaxStackSize()) {
						p_75135_1_.stackSize -= p_75135_1_.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = p_75135_1_.getMaxStackSize();
						slot.onSlotChanged();
						flag1 = true;
					}
				}

				if (p_75135_4_) {
					--k;
				} else {
					++k;
				}
			}
		}

		if (p_75135_1_.stackSize > 0) {
			if (p_75135_4_) {
				k = p_75135_3_ - 1;
			} else {
				k = p_75135_2_;
			}

			while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 == null) {
					slot.putStack(p_75135_1_.copy());
					slot.onSlotChanged();
					p_75135_1_.stackSize = 0;
					flag1 = true;
					break;
				}

				if (p_75135_4_) {
					--k;
				} else {
					++k;
				}
			}
		}

		return flag1;
	}

	public static int func_94529_b(int p_94529_0_) {
		return p_94529_0_ >> 2 & 3;
	}

	public static int func_94532_c(int p_94532_0_) {
		return p_94532_0_ & 3;
	}

	@SideOnly(Side.CLIENT)
	public static int func_94534_d(int p_94534_0_, int p_94534_1_) {
		return p_94534_0_ & 3 | (p_94534_1_ & 3) << 2;
	}

	public static boolean func_94528_d(int p_94528_0_) {
		return p_94528_0_ == 0 || p_94528_0_ == 1;
	}

	protected void func_94533_d() {
		field_94536_g = 0;
		field_94537_h.clear();
	}

	public static boolean func_94527_a(Slot p_94527_0_, ItemStack p_94527_1_, boolean p_94527_2_) {
		boolean flag1 = p_94527_0_ == null || !p_94527_0_.getHasStack();

		if (p_94527_0_ != null && p_94527_0_.getHasStack() && p_94527_1_ != null
				&& p_94527_1_.isItemEqual(p_94527_0_.getStack())
				&& ItemStack.areItemStackTagsEqual(p_94527_0_.getStack(), p_94527_1_)) {
			int i = p_94527_2_ ? 0 : p_94527_1_.stackSize;
			flag1 |= p_94527_0_.getStack().stackSize + i <= p_94527_1_.getMaxStackSize();
		}

		return flag1;
	}

	public static void func_94525_a(Set p_94525_0_, int p_94525_1_, ItemStack p_94525_2_, int p_94525_3_) {
		switch (p_94525_1_) {
		case 0:
			p_94525_2_.stackSize = MathHelper.floor_float((float) p_94525_2_.stackSize / (float) p_94525_0_.size());
			break;
		case 1:
			p_94525_2_.stackSize = 1;
		}

		p_94525_2_.stackSize += p_94525_3_;
	}

	public boolean canDragIntoSlot(Slot p_94531_1_) {
		return true;
	}

	public static int calcRedstoneFromInventory(IInventory p_94526_0_) {
		if (p_94526_0_ == null)
			return 0;
		else {
			int i = 0;
			float f = 0.0F;

			for (int j = 0; j < p_94526_0_.getSizeInventory(); ++j) {
				ItemStack itemstack = p_94526_0_.getStackInSlot(j);

				if (itemstack != null) {
					f += (float) itemstack.stackSize
							/ (float) Math.min(p_94526_0_.getInventoryStackLimit(), itemstack.getMaxStackSize());
					++i;
				}
			}

			f /= p_94526_0_.getSizeInventory();
			return MathHelper.floor_float(f * 14.0F) + (i > 0 ? 1 : 0);
		}
	}

	private boolean isOpened = false;
	private boolean isClosedByEventCancelling = false;

	public InventoryView bukkitView;
	private boolean isBukkitViewCreated;

	public InventoryView getBukkitView() {
		if (!isBukkitViewCreated) {
			isBukkitViewCreated = true;
			bukkitView = computeBukkitView();
			return bukkitView;
		}

		return bukkitView; // nullable here
	}

	public void setBukkitView(InventoryView bukkitView) {
		this.bukkitView = bukkitView;
		isBukkitViewCreated = true;
	}

	private InventoryView computeBukkitView() {
		Container container = this;

		Set<IInventory> uniqueInventorySet = new HashSet<>();
		for (Object o : inventorySlots) {
			uniqueInventorySet.add(((Slot) o).inventory);
		}
		List<IInventory> invs = new ArrayList<>(uniqueInventorySet);

		InventoryPlayer playerInv = null;

		for (Iterator<IInventory> it = invs.iterator(); it.hasNext();) {
			IInventory inv = it.next();
			if (inv instanceof InventoryPlayer) {
				InventoryPlayer foundPlayerInv = (InventoryPlayer) inv;
				// noinspection SuspiciousMethodCalls
				// if(crafters.contains(foundPlayerInv.player))
				{
					playerInv = foundPlayerInv;
					it.remove();
					break;
				}
			}
		}

		if (playerInv == null)
			return null;

		Inventory craftInv = null;
		if (invs.size() == 1) {
			IInventory firstInv = invs.get(0);
			if (container instanceof ContainerEnchantment) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting(firstInv);
			} else if (firstInv instanceof InventoryPlayer) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer(
						(CraftHumanEntity) ((InventoryPlayer) firstInv).player.getBukkitEntity());
			} else if (firstInv instanceof InventoryLargeChest) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest(
						(InventoryLargeChest) firstInv);
			} else if (firstInv instanceof TileEntityBeacon) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryBeacon((TileEntityBeacon) firstInv);
			} else if (firstInv instanceof TileEntityBrewingStand) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryBrewer(firstInv);
			} else if (firstInv instanceof TileEntityFurnace) {
				craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryFurnace((TileEntityFurnace) firstInv);
			}
		} else if (invs.size() == 2) {
			InventoryCraftResult result = findInstance(invs, InventoryCraftResult.class);
			if (result != null) {
				InventoryCrafting crafting = findInstance(invs, InventoryCrafting.class);
				if (crafting != null) {
					craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryCrafting(crafting, result);
				} else {
					InventoryBasic basic = findInstance(invs, InventoryBasic.class);
					if (basic != null && "Repair".equals(basic.getInventoryName())) {
						craftInv = new org.bukkit.craftbukkit.inventory.CraftInventoryAnvil(basic, result);
					}
				}
			}
		}

		CraftPlayer bukkitPlayer = (CraftPlayer) playerInv.player.getBukkitEntity();

		if (craftInv == null) {
			if (invs.size() != 1) {
				craftInv = Bukkit.getServer().createInventory(bukkitPlayer, InventoryType.CHEST);
			} else {
				craftInv = new CraftInventory(invs.get(0));
			}
		}

		return new CraftInventoryView(bukkitPlayer, craftInv, container);
	}

	@Nullable
	private static <T> T findInstance(List<?> list, Class<T> type) {
		for (Object o : list)
			if (type.isInstance(o))
				return type.cast(o);
		return null;
	}

	public void transferTo(Container other, CraftHumanEntity player) {
		InventoryView source = player.getOpenInventory(), destination = other.getBukkitView();
		if (source != null) {
			if (source.getTopInventory() instanceof CraftInventory) {
				IInventory topInventory = ((CraftInventory) source.getTopInventory()).getInventory();
				if (topInventory instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider topInventoryProvider = (IInventoryTransactionProvider) topInventory;
					if (topInventoryProvider.getViewers().contains(player)) {
						try {
							topInventoryProvider.onClose(player);
						} catch (AbstractMethodError error) {

						}
					}
				}
			} else {
				if (source.getTopInventory() instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider topInventoryProvider = (IInventoryTransactionProvider) source
							.getTopInventory();
					if (topInventoryProvider.getViewers().contains(player)) {
						try {
							topInventoryProvider.onClose(player);
						} catch (AbstractMethodError error){

						}
					}
				}
			}
			if (source.getBottomInventory() instanceof CraftInventory) {
				IInventory bottomInventory = ((CraftInventory) source.getBottomInventory()).getInventory();
				if (bottomInventory instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider bottomInventoryProvider = (IInventoryTransactionProvider) bottomInventory;
					if (bottomInventoryProvider.getViewers().contains(player)) {
						try {
							bottomInventoryProvider.onClose(player);
						} catch (AbstractMethodError error){

						}
					}
				}
			} else {
				if (source.getBottomInventory() instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider bottomInventoryProvider = (IInventoryTransactionProvider) source
							.getBottomInventory();
					if (bottomInventoryProvider.getViewers().contains(player)) {
						try {
							bottomInventoryProvider.onClose(player);
						} catch (AbstractMethodError error) {

						}
					}
				}
			}
		}
		if (destination != null) {
			if (destination.getTopInventory() instanceof CraftInventory) {
				IInventory topInventory = ((CraftInventory) destination.getTopInventory()).getInventory();
				if (topInventory instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider topInventoryProvider = (IInventoryTransactionProvider) topInventory;
					if (!topInventoryProvider.getViewers().contains(player)) {
						try {
							topInventoryProvider.onOpen(player);
						} catch (AbstractMethodError error){

						}
					}
				}
			} else {
				if (destination.getTopInventory() instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider topInventoryProvider = (IInventoryTransactionProvider) destination
							.getTopInventory();
					if (!topInventoryProvider.getViewers().contains(player)) {
						try {
							topInventoryProvider.onOpen(player);
						} catch (AbstractMethodError error) {

						}
					}
				}
			}
			if (destination.getBottomInventory() instanceof CraftInventory) {
				IInventory bottomInventory = ((CraftInventory) destination.getBottomInventory()).getInventory();
				if (bottomInventory instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider bottomInventoryProvider = (IInventoryTransactionProvider) bottomInventory;
					if (!bottomInventoryProvider.getViewers().contains(player)) {
						try {
							bottomInventoryProvider.onOpen(player);
						} catch (AbstractMethodError error) {

						}
					}
				}
			} else {
				if (destination.getBottomInventory() instanceof IInventoryTransactionProvider) {
					IInventoryTransactionProvider bottomInventoryProvider = (IInventoryTransactionProvider) destination
							.getBottomInventory();
					if (!bottomInventoryProvider.getViewers().contains(player)) {
						try {
							bottomInventoryProvider.onOpen(player);
						} catch (AbstractMethodError error) {

						}
					}
				}
			}
		}
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public boolean isClosedByEventCancelling() {
		return isClosedByEventCancelling;
	}

	public void setClosedByEventCancelling(boolean closedByEventCancelling) {
		isClosedByEventCancelling = closedByEventCancelling;
	}
}