package net.minecraft.creativetab;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {
	public static CreativeTabs[] creativeTabArray = new CreativeTabs[12];
	public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks") {
		private static final String __OBFID = "CL_00000006";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.brick_block);
		}
	};
	public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations") {
		private static final String __OBFID = "CL_00000010";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.double_plant);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int func_151243_f() {
			return 5;
		}
	};
	public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone") {
		private static final String __OBFID = "CL_00000011";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.redstone;
		}
	};
	public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation") {
		private static final String __OBFID = "CL_00000012";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.golden_rail);
		}
	};
	public static final CreativeTabs tabMisc = new CreativeTabs(4, "misc") {
		private static final String __OBFID = "CL_00000014";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.lava_bucket;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.all });
	public static final CreativeTabs tabAllSearch = new CreativeTabs(5, "search") {
		private static final String __OBFID = "CL_00000015";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.compass;
		}
	}.setBackgroundImageName("item_search.png");
	public static final CreativeTabs tabFood = new CreativeTabs(6, "food") {
		private static final String __OBFID = "CL_00000016";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.apple;
		}
	};
	public static final CreativeTabs tabTools = new CreativeTabs(7, "tools") {
		private static final String __OBFID = "CL_00000017";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.iron_axe;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.digger, EnumEnchantmentType.fishing_rod,
			EnumEnchantmentType.breakable });
	public static final CreativeTabs tabCombat = new CreativeTabs(8, "combat") {
		private static final String __OBFID = "CL_00000018";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.golden_sword;
		}
	}.func_111229_a(new EnumEnchantmentType[] { EnumEnchantmentType.armor, EnumEnchantmentType.armor_feet,
			EnumEnchantmentType.armor_head, EnumEnchantmentType.armor_legs, EnumEnchantmentType.armor_torso,
			EnumEnchantmentType.bow, EnumEnchantmentType.weapon });
	public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing") {
		private static final String __OBFID = "CL_00000007";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.potionitem;
		}
	};
	public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials") {
		private static final String __OBFID = "CL_00000008";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.stick;
		}
	};
	public static final CreativeTabs tabInventory = new CreativeTabs(11, "inventory") {
		private static final String __OBFID = "CL_00000009";

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.chest);
		}
	}.setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
	private final int tabIndex;
	private final String tabLabel;
	private String backgroundImageName = "items.png";
	private boolean hasScrollbar = true;
	private boolean drawTitle = true;
	private EnumEnchantmentType[] field_111230_s;
	@SideOnly(Side.CLIENT)
	private ItemStack field_151245_t;
	private static final String __OBFID = "CL_00000005";

	public CreativeTabs(String lable) {
		this(getNextID(), lable);
	}

	public CreativeTabs(int p_i1853_1_, String p_i1853_2_) {
		if (p_i1853_1_ >= creativeTabArray.length) {
			CreativeTabs[] tmp = new CreativeTabs[p_i1853_1_ + 1];
			for (int x = 0; x < creativeTabArray.length; x++) {
				tmp[x] = creativeTabArray[x];
			}
			creativeTabArray = tmp;
		}
		tabIndex = p_i1853_1_;
		tabLabel = p_i1853_2_;
		creativeTabArray[p_i1853_1_] = this;
	}

	@SideOnly(Side.CLIENT)
	public int getTabIndex() {
		return tabIndex;
	}

	public CreativeTabs setBackgroundImageName(String p_78025_1_) {
		backgroundImageName = p_78025_1_;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public String getTabLabel() {
		return tabLabel;
	}

	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return "itemGroup." + getTabLabel();
	}

	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if (field_151245_t == null) {
			field_151245_t = new ItemStack(getTabIconItem(), 1, func_151243_f());
		}

		return field_151245_t;
	}

	@SideOnly(Side.CLIENT)
	public abstract Item getTabIconItem();

	@SideOnly(Side.CLIENT)
	public int func_151243_f() {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	public String getBackgroundImageName() {
		return backgroundImageName;
	}

	@SideOnly(Side.CLIENT)
	public boolean drawInForegroundOfTab() {
		return drawTitle;
	}

	public CreativeTabs setNoTitle() {
		drawTitle = false;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldHidePlayerInventory() {
		return hasScrollbar;
	}

	public CreativeTabs setNoScrollbar() {
		hasScrollbar = false;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public int getTabColumn() {
		if (tabIndex > 11)
			return (tabIndex - 12) % 10 % 5;
		return tabIndex % 6;
	}

	@SideOnly(Side.CLIENT)
	public boolean isTabInFirstRow() {
		if (tabIndex > 11)
			return (tabIndex - 12) % 10 < 5;
		return tabIndex < 6;
	}

	@SideOnly(Side.CLIENT)
	public EnumEnchantmentType[] func_111225_m() {
		return field_111230_s;
	}

	public CreativeTabs func_111229_a(EnumEnchantmentType... p_111229_1_) {
		field_111230_s = p_111229_1_;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_111226_a(EnumEnchantmentType p_111226_1_) {
		if (field_111230_s == null)
			return false;
		else {
			EnumEnchantmentType[] aenumenchantmenttype = field_111230_s;
			int i = aenumenchantmenttype.length;

			for (int j = 0; j < i; ++j) {
				EnumEnchantmentType enumenchantmenttype1 = aenumenchantmenttype[j];

				if (enumenchantmenttype1 == p_111226_1_)
					return true;
			}

			return false;
		}
	}

	@SideOnly(Side.CLIENT)
	public void displayAllReleventItems(List p_78018_1_) {
		Iterator iterator = Item.itemRegistry.iterator();

		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();

			if (item == null) {
				continue;
			}

			for (CreativeTabs tab : item.getCreativeTabs()) {
				if (tab == this) {
					item.getSubItems(item, this, p_78018_1_);
				}
			}
		}

		if (func_111225_m() != null) {
			addEnchantmentBooksToList(p_78018_1_, func_111225_m());
		}
	}

	@SideOnly(Side.CLIENT)
	public void addEnchantmentBooksToList(List p_92116_1_, EnumEnchantmentType... p_92116_2_) {
		Enchantment[] aenchantment = Enchantment.enchantmentsList;
		int i = aenchantment.length;

		for (int j = 0; j < i; ++j) {
			Enchantment enchantment = aenchantment[j];

			if (enchantment != null && enchantment.type != null) {
				boolean flag = false;

				for (int k = 0; k < p_92116_2_.length && !flag; ++k) {
					if (enchantment.type == p_92116_2_[k]) {
						flag = true;
					}
				}

				if (flag) {
					p_92116_1_.add(Items.enchanted_book
							.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
				}
			}
		}
	}

	public int getTabPage() {
		if (tabIndex > 11)
			return (tabIndex - 12) / 10 + 1;
		return 0;
	}

	public static int getNextID() {
		return creativeTabArray.length;
	}

	/**
	 * Determines if the search bar should be shown for this tab.
	 *
	 * @return True to show the bar
	 */
	public boolean hasSearchBar() {
		return tabIndex == CreativeTabs.tabAllSearch.tabIndex;
	}

	/**
	 * Gets the width of the search bar of the creative tab, use this if your
	 * creative tab name overflows together with a custom texture.
	 *
	 * @return The width of the search bar, 89 by default
	 */
	public int getSearchbarWidth() {
		return 89;
	}
}