package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiContainerCreative extends InventoryEffectRenderer {
	private static final ResourceLocation field_147061_u = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");
	private static final InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
	private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
	private float currentScroll;
	private boolean isScrolling;
	private boolean wasClicking;
	private GuiTextField searchField;
	private List field_147063_B;
	private Slot field_147064_C;
	private boolean field_147057_D;
	private CreativeCrafting field_147059_E;
	private static final String __OBFID = "CL_00000752";
	private static int tabPage = 0;
	private int maxPages = 0;

	public GuiContainerCreative(EntityPlayer p_i1088_1_) {
		super(new GuiContainerCreative.ContainerCreative(p_i1088_1_));
		p_i1088_1_.openContainer = inventorySlots;
		allowUserInput = true;
		ySize = 136;
		xSize = 195;
	}

	@Override
	public void updateScreen() {
		if (!mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
	}

	@Override
	protected void handleMouseClick(Slot p_146984_1_, int p_146984_2_, int p_146984_3_, int p_146984_4_) {
		field_147057_D = true;
		boolean flag = p_146984_4_ == 1;
		p_146984_4_ = p_146984_2_ == -999 && p_146984_4_ == 0 ? 4 : p_146984_4_;
		ItemStack itemstack1;
		InventoryPlayer inventoryplayer;

		if (p_146984_1_ == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && p_146984_4_ != 5) {
			inventoryplayer = mc.thePlayer.inventory;

			if (inventoryplayer.getItemStack() != null) {
				if (p_146984_3_ == 0) {
					mc.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
					mc.playerController.sendPacketDropItem(inventoryplayer.getItemStack());
					inventoryplayer.setItemStack(null);
				}

				if (p_146984_3_ == 1) {
					itemstack1 = inventoryplayer.getItemStack().splitStack(1);
					mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack1, true);
					mc.playerController.sendPacketDropItem(itemstack1);

					if (inventoryplayer.getItemStack().stackSize == 0) {
						inventoryplayer.setItemStack(null);
					}
				}
			}
		} else {
			int l;

			if (p_146984_1_ == field_147064_C && flag) {
				for (l = 0; l < mc.thePlayer.inventoryContainer.getInventory().size(); ++l) {
					mc.playerController.sendSlotPacket(null, l);
				}
			} else {
				ItemStack itemstack;

				if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
					if (p_146984_1_ == field_147064_C) {
						mc.thePlayer.inventory.setItemStack(null);
					} else if (p_146984_4_ == 4 && p_146984_1_ != null && p_146984_1_.getHasStack()) {
						itemstack = p_146984_1_
								.decrStackSize(p_146984_3_ == 0 ? 1 : p_146984_1_.getStack().getMaxStackSize());
						mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack, true);
						mc.playerController.sendPacketDropItem(itemstack);
					} else if (p_146984_4_ == 4 && mc.thePlayer.inventory.getItemStack() != null) {
						mc.thePlayer.dropPlayerItemWithRandomChoice(mc.thePlayer.inventory.getItemStack(), true);
						mc.playerController.sendPacketDropItem(mc.thePlayer.inventory.getItemStack());
						mc.thePlayer.inventory.setItemStack(null);
					} else {
						mc.thePlayer.inventoryContainer.slotClick(
								p_146984_1_ == null ? p_146984_2_
										: ((GuiContainerCreative.CreativeSlot) p_146984_1_).field_148332_b.slotNumber,
								p_146984_3_, p_146984_4_, mc.thePlayer);
						mc.thePlayer.inventoryContainer.detectAndSendChanges();
					}
				} else if (p_146984_4_ != 5 && p_146984_1_.inventory == field_147060_v) {
					inventoryplayer = mc.thePlayer.inventory;
					itemstack1 = inventoryplayer.getItemStack();
					ItemStack itemstack2 = p_146984_1_.getStack();
					ItemStack itemstack3;

					if (p_146984_4_ == 2) {
						if (itemstack2 != null && p_146984_3_ >= 0 && p_146984_3_ < 9) {
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							mc.thePlayer.inventory.setInventorySlotContents(p_146984_3_, itemstack3);
							mc.thePlayer.inventoryContainer.detectAndSendChanges();
						}

						return;
					}

					if (p_146984_4_ == 3) {
						if (inventoryplayer.getItemStack() == null && p_146984_1_.getHasStack()) {
							itemstack3 = p_146984_1_.getStack().copy();
							itemstack3.stackSize = itemstack3.getMaxStackSize();
							inventoryplayer.setItemStack(itemstack3);
						}

						return;
					}

					if (p_146984_4_ == 4) {
						if (itemstack2 != null) {
							itemstack3 = itemstack2.copy();
							itemstack3.stackSize = p_146984_3_ == 0 ? 1 : itemstack3.getMaxStackSize();
							mc.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
							mc.playerController.sendPacketDropItem(itemstack3);
						}

						return;
					}

					if (itemstack1 != null && itemstack2 != null && itemstack1.isItemEqual(itemstack2)
							&& ItemStack.areItemStackTagsEqual(itemstack1, itemstack2)) // Forge: Bugfix, Compare NBT
																						// data, allow for deletion of
																						// enchanted books, MC-12770
					{
						if (p_146984_3_ == 0) {
							if (flag) {
								itemstack1.stackSize = itemstack1.getMaxStackSize();
							} else if (itemstack1.stackSize < itemstack1.getMaxStackSize()) {
								++itemstack1.stackSize;
							}
						} else if (itemstack1.stackSize <= 1) {
							inventoryplayer.setItemStack(null);
						} else {
							--itemstack1.stackSize;
						}
					} else if (itemstack2 != null && itemstack1 == null) {
						inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
						itemstack1 = inventoryplayer.getItemStack();

						if (flag) {
							itemstack1.stackSize = itemstack1.getMaxStackSize();
						}
					} else {
						inventoryplayer.setItemStack(null);
					}
				} else {
					inventorySlots.slotClick(p_146984_1_ == null ? p_146984_2_ : p_146984_1_.slotNumber, p_146984_3_,
							p_146984_4_, mc.thePlayer);

					if (Container.func_94532_c(p_146984_3_) == 2) {
						for (l = 0; l < 9; ++l) {
							mc.playerController.sendSlotPacket(inventorySlots.getSlot(45 + l).getStack(), 36 + l);
						}
					} else if (p_146984_1_ != null) {
						itemstack = inventorySlots.getSlot(p_146984_1_.slotNumber).getStack();
						mc.playerController.sendSlotPacket(itemstack,
								p_146984_1_.slotNumber - inventorySlots.inventorySlots.size() + 9 + 36);
					}
				}
			}
		}
	}

	@Override
	public void initGui() {
		if (mc.playerController.isInCreativeMode()) {
			super.initGui();
			buttonList.clear();
			Keyboard.enableRepeatEvents(true);
			searchField = new GuiTextField(fontRendererObj, guiLeft + 82, guiTop + 6, 89, fontRendererObj.FONT_HEIGHT);
			searchField.setMaxStringLength(15);
			searchField.setEnableBackgroundDrawing(false);
			searchField.setVisible(false);
			searchField.setTextColor(16777215);
			int i = selectedTabIndex;
			selectedTabIndex = -1;
			setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
			field_147059_E = new CreativeCrafting(mc);
			mc.thePlayer.inventoryContainer.addCraftingToCrafters(field_147059_E);
			int tabCount = CreativeTabs.creativeTabArray.length;
			if (tabCount > 12) {
				buttonList.add(new GuiButton(101, guiLeft, guiTop - 50, 20, 20, "<"));
				buttonList.add(new GuiButton(102, guiLeft + xSize - 20, guiTop - 50, 20, 20, ">"));
				maxPages = (tabCount - 12) / 10 + 1;
			}
		} else {
			mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (mc.thePlayer != null && mc.thePlayer.inventory != null) {
			mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(field_147059_E);
		}

		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (!CreativeTabs.creativeTabArray[selectedTabIndex].hasSearchBar()) {
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindChat)) {
				setCurrentCreativeTab(CreativeTabs.tabAllSearch);
			} else {
				super.keyTyped(p_73869_1_, p_73869_2_);
			}
		} else {
			if (field_147057_D) {
				field_147057_D = false;
				searchField.setText("");
			}

			if (!checkHotbarKeys(p_73869_2_)) {
				if (searchField.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
					updateCreativeSearch();
				} else {
					super.keyTyped(p_73869_1_, p_73869_2_);
				}
			}
		}
	}

	private void updateCreativeSearch() {
		GuiContainerCreative.ContainerCreative containercreative = (GuiContainerCreative.ContainerCreative) inventorySlots;
		containercreative.itemList.clear();

		CreativeTabs tab = CreativeTabs.creativeTabArray[selectedTabIndex];
		if (tab.hasSearchBar() && tab != CreativeTabs.tabAllSearch) {
			tab.displayAllReleventItems(containercreative.itemList);
			updateFilteredItems(containercreative);
			return;
		}

		Iterator iterator = Item.itemRegistry.iterator();

		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item != null && item.getCreativeTab() != null) {
				item.getSubItems(item, null, containercreative.itemList);
			}
		}
		updateFilteredItems(containercreative);
	}

	// split from above for custom search tabs
	private void updateFilteredItems(GuiContainerCreative.ContainerCreative containercreative) {
		Iterator iterator;
		Enchantment[] aenchantment = Enchantment.enchantmentsList;
		int j = aenchantment.length;

		if (CreativeTabs.creativeTabArray[selectedTabIndex] != CreativeTabs.tabAllSearch) {
			j = 0; // Forge: Don't add enchants to custom tabs.
		}
		for (int i = 0; i < j; ++i) {
			Enchantment enchantment = aenchantment[i];

			if (enchantment != null && enchantment.type != null) {
				Items.enchanted_book.func_92113_a(enchantment, containercreative.itemList);
			}
		}

		iterator = containercreative.itemList.iterator();
		String s1 = searchField.getText().toLowerCase();

		while (iterator.hasNext()) {
			ItemStack itemstack = (ItemStack) iterator.next();
			boolean flag = false;
			Iterator iterator1 = itemstack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips).iterator();

			while (true) {
				if (iterator1.hasNext()) {
					String s = (String) iterator1.next();

					if (!s.toLowerCase().contains(s1)) {
						continue;
					}

					flag = true;
				}

				if (!flag) {
					iterator.remove();
				}

				break;
			}
		}

		currentScroll = 0.0F;
		containercreative.scrollTo(0.0F);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];

		if (creativetabs != null && creativetabs.drawInForegroundOfTab()) {
			GL11.glDisable(GL11.GL_BLEND);
			fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel()), 8, 6, 4210752);
		}
	}

	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0) {
			int l = p_73864_1_ - guiLeft;
			int i1 = p_73864_2_ - guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;

			for (int k1 = 0; k1 < j1; ++k1) {
				CreativeTabs creativetabs = acreativetabs[k1];

				if (creativetabs != null && func_147049_a(creativetabs, l, i1))
					return;
			}
		}

		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		if (p_146286_3_ == 0) {
			int l = p_146286_1_ - guiLeft;
			int i1 = p_146286_2_ - guiTop;
			CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
			int j1 = acreativetabs.length;

			for (int k1 = 0; k1 < j1; ++k1) {
				CreativeTabs creativetabs = acreativetabs[k1];

				if (creativetabs != null && func_147049_a(creativetabs, l, i1)) {
					setCurrentCreativeTab(creativetabs);
					return;
				}
			}
		}

		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	private boolean needsScrollBars() {
		if (CreativeTabs.creativeTabArray[selectedTabIndex] == null)
			return false;
		return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()
				&& CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
				&& ((GuiContainerCreative.ContainerCreative) inventorySlots).func_148328_e();
	}

	private void setCurrentCreativeTab(CreativeTabs p_147050_1_) {
		if (p_147050_1_ == null)
			return;
		int i = selectedTabIndex;
		selectedTabIndex = p_147050_1_.getTabIndex();
		GuiContainerCreative.ContainerCreative containercreative = (GuiContainerCreative.ContainerCreative) inventorySlots;
		field_147008_s.clear();
		containercreative.itemList.clear();
		p_147050_1_.displayAllReleventItems(containercreative.itemList);

		if (p_147050_1_ == CreativeTabs.tabInventory) {
			Container container = mc.thePlayer.inventoryContainer;

			if (field_147063_B == null) {
				field_147063_B = containercreative.inventorySlots;
			}

			containercreative.inventorySlots = new ArrayList();

			for (int j = 0; j < container.inventorySlots.size(); ++j) {
				GuiContainerCreative.CreativeSlot creativeslot = new GuiContainerCreative.CreativeSlot(
						(Slot) container.inventorySlots.get(j), j);
				containercreative.inventorySlots.add(creativeslot);
				int k;
				int l;
				int i1;

				if (j >= 5 && j < 9) {
					k = j - 5;
					l = k / 2;
					i1 = k % 2;
					creativeslot.xDisplayPosition = 9 + l * 54;
					creativeslot.yDisplayPosition = 6 + i1 * 27;
				} else if (j >= 0 && j < 5) {
					creativeslot.yDisplayPosition = -2000;
					creativeslot.xDisplayPosition = -2000;
				} else if (j < container.inventorySlots.size()) {
					k = j - 9;
					l = k % 9;
					i1 = k / 9;
					creativeslot.xDisplayPosition = 9 + l * 18;

					if (j >= 36) {
						creativeslot.yDisplayPosition = 112;
					} else {
						creativeslot.yDisplayPosition = 54 + i1 * 18;
					}
				}
			}

			field_147064_C = new Slot(field_147060_v, 0, 173, 112);
			containercreative.inventorySlots.add(field_147064_C);
		} else if (i == CreativeTabs.tabInventory.getTabIndex()) {
			containercreative.inventorySlots = field_147063_B;
			field_147063_B = null;
		}

		if (searchField != null) {
			if (p_147050_1_.hasSearchBar()) {
				searchField.setVisible(true);
				searchField.setCanLoseFocus(false);
				searchField.setFocused(true);
				searchField.setText("");
				searchField.width = p_147050_1_.getSearchbarWidth();
				searchField.xPosition = guiLeft + 82 /* default left */ + 89 - searchField.width;
				updateCreativeSearch();
			} else {
				searchField.setVisible(false);
				searchField.setCanLoseFocus(true);
				searchField.setFocused(false);
			}
		}

		currentScroll = 0.0F;
		containercreative.scrollTo(0.0F);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && needsScrollBars()) {
			int j = ((GuiContainerCreative.ContainerCreative) inventorySlots).itemList.size() / 9 - 5 + 1;

			if (i > 0) {
				i = 1;
			}

			if (i < 0) {
				i = -1;
			}

			currentScroll = (float) (currentScroll - (double) i / (double) j);

			if (currentScroll < 0.0F) {
				currentScroll = 0.0F;
			}

			if (currentScroll > 1.0F) {
				currentScroll = 1.0F;
			}

			((GuiContainerCreative.ContainerCreative) inventorySlots).scrollTo(currentScroll);
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		boolean flag = Mouse.isButtonDown(0);
		int k = guiLeft;
		int l = guiTop;
		int i1 = k + 175;
		int j1 = l + 18;
		int k1 = i1 + 14;
		int l1 = j1 + 112;

		if (!wasClicking && flag && p_73863_1_ >= i1 && p_73863_2_ >= j1 && p_73863_1_ < k1 && p_73863_2_ < l1) {
			isScrolling = needsScrollBars();
		}

		if (!flag) {
			isScrolling = false;
		}

		wasClicking = flag;

		if (isScrolling) {
			currentScroll = (p_73863_2_ - j1 - 7.5F) / (l1 - j1 - 15.0F);

			if (currentScroll < 0.0F) {
				currentScroll = 0.0F;
			}

			if (currentScroll > 1.0F) {
				currentScroll = 1.0F;
			}

			((GuiContainerCreative.ContainerCreative) inventorySlots).scrollTo(currentScroll);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		int start = tabPage * 10;
		int i2 = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);
		if (tabPage != 0) {
			start += 2;
		}
		boolean rendered = false;

		for (int j2 = start; j2 < i2; ++j2) {
			CreativeTabs creativetabs = acreativetabs[j2];

			if (creativetabs == null) {
				continue;
			}
			if (renderCreativeInventoryHoveringText(creativetabs, p_73863_1_, p_73863_2_)) {
				rendered = true;
				break;
			}
		}

		if (!rendered && renderCreativeInventoryHoveringText(CreativeTabs.tabAllSearch, p_73863_1_, p_73863_2_)) {
			renderCreativeInventoryHoveringText(CreativeTabs.tabInventory, p_73863_1_, p_73863_2_);
		}

		if (field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && func_146978_c(
				field_147064_C.xDisplayPosition, field_147064_C.yDisplayPosition, 16, 16, p_73863_1_, p_73863_2_)) {
			drawCreativeTabHoveringText(I18n.format("inventory.binSlot"), p_73863_1_, p_73863_2_);
		}

		if (maxPages != 0) {
			String page = String.format("%d / %d", tabPage + 1, maxPages + 1);
			int width = fontRendererObj.getStringWidth(page);
			GL11.glDisable(GL11.GL_LIGHTING);
			zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			fontRendererObj.drawString(page, guiLeft + xSize / 2 - width / 2, guiTop - 44, -1);
			zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
	}

	@Override
	protected void renderToolTip(ItemStack p_146285_1_, int p_146285_2_, int p_146285_3_) {
		if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
			List list = p_146285_1_.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
			CreativeTabs creativetabs = p_146285_1_.getItem().getCreativeTab();

			if (creativetabs == null && p_146285_1_.getItem() == Items.enchanted_book) {
				Map map = EnchantmentHelper.getEnchantments(p_146285_1_);

				if (map.size() == 1) {
					Enchantment enchantment = Enchantment.enchantmentsList[((Integer) map.keySet().iterator().next())
							.intValue()];
					CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
					int k = acreativetabs.length;

					for (int l = 0; l < k; ++l) {
						CreativeTabs creativetabs1 = acreativetabs[l];

						if (creativetabs1.func_111226_a(enchantment.type)) {
							creativetabs = creativetabs1;
							break;
						}
					}
				}
			}

			if (creativetabs != null) {
				list.add(1, "" + EnumChatFormatting.BOLD + EnumChatFormatting.BLUE
						+ I18n.format(creativetabs.getTranslatedTabLabel()));
			}

			for (int i1 = 0; i1 < list.size(); ++i1) {
				if (i1 == 0) {
					list.set(i1, p_146285_1_.getRarity().rarityColor + (String) list.get(i1));
				} else {
					list.set(i1, EnumChatFormatting.GRAY + (String) list.get(i1));
				}
			}

			func_146283_a(list, p_146285_2_, p_146285_3_);
		} else {
			super.renderToolTip(p_146285_1_, p_146285_2_, p_146285_3_);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableGUIStandardItemLighting();
		CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
		CreativeTabs[] acreativetabs = CreativeTabs.creativeTabArray;
		int k = acreativetabs.length;
		int l;

		int start = tabPage * 10;
		k = Math.min(acreativetabs.length, (tabPage + 1) * 10 + 2);
		if (tabPage != 0) {
			start += 2;
		}

		for (l = start; l < k; ++l) {
			CreativeTabs creativetabs1 = acreativetabs[l];
			mc.getTextureManager().bindTexture(field_147061_u);

			if (creativetabs1 == null) {
				continue;
			}

			if (creativetabs1.getTabIndex() != selectedTabIndex) {
				func_147051_a(creativetabs1);
			}
		}

		if (tabPage != 0) {
			if (creativetabs != CreativeTabs.tabAllSearch) {
				mc.getTextureManager().bindTexture(field_147061_u);
				func_147051_a(CreativeTabs.tabAllSearch);
			}
			if (creativetabs != CreativeTabs.tabInventory) {
				mc.getTextureManager().bindTexture(field_147061_u);
				func_147051_a(CreativeTabs.tabInventory);
			}
		}

		mc.getTextureManager().bindTexture(new ResourceLocation(
				"textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		searchField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i1 = guiLeft + 175;
		k = guiTop + 18;
		l = k + 112;
		mc.getTextureManager().bindTexture(field_147061_u);

		if (creativetabs.shouldHidePlayerInventory()) {
			drawTexturedModalRect(i1, k + (int) ((l - k - 17) * currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0,
					12, 15);
		}

		if (creativetabs == null || creativetabs.getTabPage() != tabPage) {
			if (creativetabs != CreativeTabs.tabAllSearch && creativetabs != CreativeTabs.tabInventory)
				return;
		}

		func_147051_a(creativetabs);

		if (creativetabs == CreativeTabs.tabInventory) {
			GuiInventory.func_147046_a(guiLeft + 43, guiTop + 45, 20, guiLeft + 43 - p_146976_2_,
					guiTop + 45 - 30 - p_146976_3_, mc.thePlayer);
		}
	}

	protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_) {
		if (p_147049_1_.getTabPage() != tabPage) {
			if (p_147049_1_ != CreativeTabs.tabAllSearch && p_147049_1_ != CreativeTabs.tabInventory)
				return false;
		}

		int k = p_147049_1_.getTabColumn();
		int l = 28 * k;
		byte b0 = 0;

		if (k == 5) {
			l = xSize - 28 + 2;
		} else if (k > 0) {
			l += k;
		}

		int i1;

		if (p_147049_1_.isTabInFirstRow()) {
			i1 = b0 - 32;
		} else {
			i1 = b0 + ySize;
		}

		return p_147049_2_ >= l && p_147049_2_ <= l + 28 && p_147049_3_ >= i1 && p_147049_3_ <= i1 + 32;
	}

	protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_) {
		int k = p_147052_1_.getTabColumn();
		int l = 28 * k;
		byte b0 = 0;

		if (k == 5) {
			l = xSize - 28 + 2;
		} else if (k > 0) {
			l += k;
		}

		int i1;

		if (p_147052_1_.isTabInFirstRow()) {
			i1 = b0 - 32;
		} else {
			i1 = b0 + ySize;
		}

		if (func_146978_c(l + 3, i1 + 3, 23, 27, p_147052_2_, p_147052_3_)) {
			drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel()), p_147052_2_,
					p_147052_3_);
			return true;
		} else
			return false;
	}

	protected void func_147051_a(CreativeTabs p_147051_1_) {
		boolean flag = p_147051_1_.getTabIndex() == selectedTabIndex;
		boolean flag1 = p_147051_1_.isTabInFirstRow();
		int i = p_147051_1_.getTabColumn();
		int j = i * 28;
		int k = 0;
		int l = guiLeft + 28 * i;
		int i1 = guiTop;
		byte b0 = 32;

		if (flag) {
			k += 32;
		}

		if (i == 5) {
			l = guiLeft + xSize - 28;
		} else if (i > 0) {
			l += i;
		}

		if (flag1) {
			i1 -= 28;
		} else {
			k += 64;
			i1 += ySize - 4;
		}

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(1F, 1F, 1F); // Forge: Reset color in case Items change it.
		GL11.glEnable(GL11.GL_BLEND); // Forge: Make sure blend is enabled else tabs show a white border.
		drawTexturedModalRect(l, i1, j, k, 28, b0);
		zLevel = 100.0F;
		itemRender.zLevel = 100.0F;
		l += 6;
		i1 += 8 + (flag1 ? 1 : -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		ItemStack itemstack = p_147051_1_.getIconItemStack();
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, l, i1);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), itemstack, l, i1);
		GL11.glDisable(GL11.GL_LIGHTING);
		itemRender.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
		}

		if (p_146284_1_.id == 101) {
			tabPage = Math.max(tabPage - 1, 0);
		} else if (p_146284_1_.id == 102) {
			tabPage = Math.min(tabPage + 1, maxPages);
		}
	}

	public int func_147056_g() {
		return selectedTabIndex;
	}

	@SideOnly(Side.CLIENT)
	static class ContainerCreative extends Container {
		public List itemList = new ArrayList();
		private static final String __OBFID = "CL_00000753";

		public ContainerCreative(EntityPlayer p_i1086_1_) {
			InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
			int i;

			for (i = 0; i < 5; ++i) {
				for (int j = 0; j < 9; ++j) {
					addSlotToContainer(
							new Slot(GuiContainerCreative.field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
				}
			}

			for (i = 0; i < 9; ++i) {
				addSlotToContainer(new Slot(inventoryplayer, i, 9 + i * 18, 112));
			}

			scrollTo(0.0F);
		}

		@Override
		public boolean canInteractWith(EntityPlayer p_75145_1_) {
			return true;
		}

		public void scrollTo(float p_148329_1_) {
			int i = itemList.size() / 9 - 5 + 1;
			int j = (int) (p_148329_1_ * i + 0.5D);

			if (j < 0) {
				j = 0;
			}

			for (int k = 0; k < 5; ++k) {
				for (int l = 0; l < 9; ++l) {
					int i1 = l + (k + j) * 9;

					if (i1 >= 0 && i1 < itemList.size()) {
						GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9,
								(ItemStack) itemList.get(i1));
					} else {
						GuiContainerCreative.field_147060_v.setInventorySlotContents(l + k * 9, null);
					}
				}
			}
		}

		public boolean func_148328_e() {
			return itemList.size() > 45;
		}

		@Override
		protected void retrySlotClick(int p_75133_1_, int p_75133_2_, boolean p_75133_3_, EntityPlayer p_75133_4_) {
		}

		@Override
		public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
			if (p_82846_2_ >= inventorySlots.size() - 9 && p_82846_2_ < inventorySlots.size()) {
				Slot slot = (Slot) inventorySlots.get(p_82846_2_);

				if (slot != null && slot.getHasStack()) {
					slot.putStack(null);
				}
			}

			return null;
		}

		@Override
		public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
			return p_94530_2_.yDisplayPosition > 90;
		}

		@Override
		public boolean canDragIntoSlot(Slot p_94531_1_) {
			return p_94531_1_.inventory instanceof InventoryPlayer
					|| p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
		}
	}

	@SideOnly(Side.CLIENT)
	class CreativeSlot extends Slot {
		private final Slot field_148332_b;
		private static final String __OBFID = "CL_00000754";

		public CreativeSlot(Slot p_i1087_2_, int p_i1087_3_) {
			super(p_i1087_2_.inventory, p_i1087_3_, 0, 0);
			field_148332_b = p_i1087_2_;
		}

		@Override
		public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
			field_148332_b.onPickupFromSlot(p_82870_1_, p_82870_2_);
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return field_148332_b.isItemValid(p_75214_1_);
		}

		@Override
		public ItemStack getStack() {
			return field_148332_b.getStack();
		}

		@Override
		public boolean getHasStack() {
			return field_148332_b.getHasStack();
		}

		@Override
		public void putStack(ItemStack p_75215_1_) {
			field_148332_b.putStack(p_75215_1_);
		}

		@Override
		public void onSlotChanged() {
			field_148332_b.onSlotChanged();
		}

		@Override
		public int getSlotStackLimit() {
			return field_148332_b.getSlotStackLimit();
		}

		@Override
		public IIcon getBackgroundIconIndex() {
			return field_148332_b.getBackgroundIconIndex();
		}

		@Override
		public ItemStack decrStackSize(int p_75209_1_) {
			return field_148332_b.decrStackSize(p_75209_1_);
		}

		@Override
		public boolean isSlotInInventory(IInventory p_75217_1_, int p_75217_2_) {
			return field_148332_b.isSlotInInventory(p_75217_1_, p_75217_2_);
		}
	}
}