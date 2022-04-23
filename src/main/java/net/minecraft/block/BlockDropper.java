package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.ultramine.bukkit.util.BukkitUtil;

public class BlockDropper extends BlockDispenser {
	private final IBehaviorDispenseItem field_149947_P = new BehaviorDefaultDispenseItem();
	private static final String __OBFID = "CL_00000233";

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon("furnace_side");
		field_149944_M = p_149651_1_.registerIcon("furnace_top");
		field_149945_N = p_149651_1_.registerIcon(getTextureName() + "_front_horizontal");
		field_149946_O = p_149651_1_.registerIcon(getTextureName() + "_front_vertical");
	}

	@Override
	protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_) {
		return field_149947_P;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDropper();
	}

	@Override
	protected void func_149941_e(World world, int x, int y, int z) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, x, y, z);
		TileEntityDispenser tileentitydispenser = (TileEntityDispenser) blocksourceimpl.getBlockTileEntity();
		if (tileentitydispenser != null) {
			int l = tileentitydispenser.func_146017_i();
			if (l < 0) {
				world.playAuxSFX(1001, x, y, z, 0);
			} else {
				ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
				int i1 = world.getBlockMetadata(x, y, z) & 7;
				IInventory iinventory = TileEntityHopper.func_145893_b(world, x + Facing.offsetsXForSide[i1],
						y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
				ItemStack itemstack1;
				if (iinventory != null) {
					CraftItemStack oitemstack = CraftItemStack.asCraftMirror(itemstack.copy().splitStack(1));
					org.bukkit.inventory.Inventory destinationInventory;
					if (iinventory instanceof InventoryLargeChest) {
						destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest(
								(InventoryLargeChest) iinventory);
					} else {
						destinationInventory = BukkitUtil.getInventoryOwner(iinventory).getInventory();
					}
					InventoryMoveItemEvent event = new InventoryMoveItemEvent(
							BukkitUtil.getInventoryOwner(tileentitydispenser).getInventory(), oitemstack.clone(),
							destinationInventory, true);
					world.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled())
						return;
					itemstack1 = TileEntityHopper.func_145889_a(iinventory, CraftItemStack.asNMSCopy(event.getItem()),
							Facing.oppositeSide[i1]);
					if (event.getItem().equals(oitemstack) && itemstack1 == null) {
						itemstack1 = itemstack.copy();
						if (--itemstack1.stackSize == 0) {
							itemstack1 = null;
						}
					} else {
						itemstack1 = itemstack.copy();
					}
				} else {
					itemstack1 = field_149947_P.dispense(blocksourceimpl, itemstack);
					if (itemstack1 != null && itemstack1.stackSize == 0) {
						itemstack1 = null;
					}
				}
				tileentitydispenser.setInventorySlotContents(l, itemstack1);
			}
		}
	}
}