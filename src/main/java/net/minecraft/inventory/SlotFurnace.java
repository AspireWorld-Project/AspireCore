package net.minecraft.inventory;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public class SlotFurnace extends Slot {
	private final EntityPlayer thePlayer;
	private int field_75228_b;
	private static final String __OBFID = "CL_00001749";

	public SlotFurnace(EntityPlayer p_i1813_1_, IInventory p_i1813_2_, int p_i1813_3_, int p_i1813_4_, int p_i1813_5_) {
		super(p_i1813_2_, p_i1813_3_, p_i1813_4_, p_i1813_5_);
		thePlayer = p_i1813_1_;
	}

	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (getHasStack()) {
			field_75228_b += Math.min(p_75209_1_, getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);
		super.onPickupFromSlot(p_82870_1_, p_82870_2_);
	}

	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		field_75228_b += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	protected void onCrafting(ItemStack p_75208_1_) {
		p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, field_75228_b);

		if (!thePlayer.worldObj.isRemote) {
			int i = field_75228_b;
			float f = FurnaceRecipes.smelting().func_151398_b(p_75208_1_);
			int j;

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				j = MathHelper.floor_float(i * f);

				if (j < MathHelper.ceiling_float_int(i * f) && (float) Math.random() < i * f - j) {
					++j;
				}

				i = j;
			}

			if (this.inventory instanceof TileEntityFurnace) {

				Player player = (Player) thePlayer.getBukkitEntity();
				TileEntityFurnace furnace = ((TileEntityFurnace) this.inventory);
				org.bukkit.block.Block block = thePlayer.worldObj.getWorld().getBlockAt(furnace.xCoord, furnace.yCoord, furnace.zCoord);
				FurnaceExtractEvent event = new FurnaceExtractEvent(player, block, CraftMagicNumbers.getMaterial(p_75208_1_.getItem()), p_75208_1_.stackSize, i);
				thePlayer.worldObj.getServer().getPluginManager().callEvent(event);
				i = event.getExpToDrop();

			}

			while (i > 0) {
				j = EntityXPOrb.getXPSplit(i);
				i -= j;
				thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(thePlayer.worldObj, thePlayer.posX,
						thePlayer.posY + 0.5D, thePlayer.posZ + 0.5D, j));
			}
		}

		field_75228_b = 0;

		FMLCommonHandler.instance().firePlayerSmeltedEvent(thePlayer, p_75208_1_);

		if (p_75208_1_.getItem() == Items.iron_ingot) {
			thePlayer.addStat(AchievementList.acquireIron, 1);
		}

		if (p_75208_1_.getItem() == Items.cooked_fished) {
			thePlayer.addStat(AchievementList.cookFish, 1);
		}
	}
}