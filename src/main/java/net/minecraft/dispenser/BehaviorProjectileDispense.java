package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.util.Vector;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
	private static final String __OBFID = "CL_00001394";

	@Override
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
		World world = blockSource.getWorld();
		IPosition iposition = BlockDispenser.func_149939_a(blockSource);
		EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
		IProjectile iprojectile = getProjectileEntity(world, iposition);
		ItemStack itemstack1 = itemStack.splitStack(1);
		org.bukkit.block.Block block = world.getWorld().getBlockAt(blockSource.getXInt(), blockSource.getYInt(),
				blockSource.getZInt());
		CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);
		BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new Vector(
				enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1F, enumfacing.getFrontOffsetZ()));
		world.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			itemStack.stackSize++;
			return itemStack;
		}
		if (!event.getItem().equals(craftItem)) {
			if (event.getItem().getType() == Material.AIR)
				return itemStack;
			itemStack.stackSize++;
			ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
			IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
					.getObject(eventStack.getItem());
			if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
					&& ibehaviordispenseitem != this) {
				ibehaviordispenseitem.dispense(blockSource, eventStack);
				return itemStack;
			}
		}
		iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY() + 0.1F,
				enumfacing.getFrontOffsetZ(), func_82500_b(), func_82498_a());
		((Entity) iprojectile).setProjectileSource(new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource(
				(TileEntityDispenser) blockSource.getBlockTileEntity()));
		world.spawnEntityInWorld((Entity) iprojectile);
		return itemStack;
	}

	@Override
	protected void playDispenseSound(IBlockSource p_82485_1_) {
		p_82485_1_.getWorld().playAuxSFX(1002, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
	}

	protected abstract IProjectile getProjectileEntity(World p_82499_1_, IPosition p_82499_2_);

	protected float func_82498_a() {
		return 6.0F;
	}

	protected float func_82500_b() {
		return 1.1F;
	}
}