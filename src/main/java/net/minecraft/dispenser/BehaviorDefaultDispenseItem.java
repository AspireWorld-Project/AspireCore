package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {
	private static final String __OBFID = "CL_00001195";

	@Override
	public final ItemStack dispense(IBlockSource p_82482_1_, ItemStack p_82482_2_) {
		ItemStack itemstack1 = dispenseStack(p_82482_1_, p_82482_2_);
		playDispenseSound(p_82482_1_);
		spawnDispenseParticles(p_82482_1_, BlockDispenser.func_149937_b(p_82482_1_.getBlockMetadata()));
		return itemstack1;
	}

	protected ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
		EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
		if (!doDispense(blockSource.getWorld(), itemStack.splitStack(1), 6, enumfacing, blockSource)) {
			itemStack.stackSize++;
		}
		return itemStack;
	}

	private boolean doDispense(World world, ItemStack itemstack, int i, EnumFacing enumfacing,
			IBlockSource iblocksource) {
		IPosition iposition = BlockDispenser.func_149939_a(iblocksource);
		double d0 = iposition.getX();
		double d1 = iposition.getY();
		double d2 = iposition.getZ();
		EntityItem entityitem = new EntityItem(world, d0, d1 - 0.3D, d2, itemstack);
		double d3 = world.rand.nextDouble() * 0.1D + 0.2D;
		entityitem.motionX = enumfacing.getFrontOffsetX() * d3;
		entityitem.motionY = 0.20000000298023224D;
		entityitem.motionZ = enumfacing.getFrontOffsetZ() * d3;
		entityitem.motionX = world.rand.nextGaussian() * 0.007499999832361937D * i;
		entityitem.motionY = world.rand.nextGaussian() * 0.007499999832361937D * i;
		entityitem.motionZ = world.rand.nextGaussian() * 0.007499999832361937D * i;
		Block block = world.getWorld().getBlockAt(iblocksource.getXInt(), iblocksource.getYInt(),
				iblocksource.getZInt());
		CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);
		BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
				new org.bukkit.util.Vector(entityitem.motionX, entityitem.motionY, entityitem.motionZ));
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return false;
		if (event.getItem().getType() != Material.AIR) {
			entityitem.setEntityItemStack(CraftItemStack.asNMSCopy(event.getItem()));
			entityitem.motionX = event.getVelocity().getX();
			entityitem.motionY = event.getVelocity().getY();
			entityitem.motionZ = event.getVelocity().getZ();
			if (!event.getItem().equals(craftItem)) {
				ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
				IBehaviorDispenseItem ibehaviordispenseitem = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry
						.getObject(eventStack.getItem());
				if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider
						&& ibehaviordispenseitem.getClass() != BehaviorDefaultDispenseItem.class) {
					ibehaviordispenseitem.dispense(iblocksource, eventStack);
				} else {
					world.spawnEntityInWorld(entityitem);
				}
				return false;
			}
			world.spawnEntityInWorld(entityitem);
		}
		return true;
	}

	public static void doDispense(World p_82486_0_, ItemStack p_82486_1_, int p_82486_2_, EnumFacing p_82486_3_,
			IPosition p_82486_4_) {
		double d0 = p_82486_4_.getX();
		double d1 = p_82486_4_.getY();
		double d2 = p_82486_4_.getZ();
		EntityItem entityitem = new EntityItem(p_82486_0_, d0, d1 - 0.3D, d2, p_82486_1_);
		double d3 = p_82486_0_.rand.nextDouble() * 0.1D + 0.2D;
		entityitem.motionX = p_82486_3_.getFrontOffsetX() * d3;
		entityitem.motionY = 0.20000000298023224D;
		entityitem.motionZ = p_82486_3_.getFrontOffsetZ() * d3;
		entityitem.motionX += p_82486_0_.rand.nextGaussian() * 0.007499999832361937D * p_82486_2_;
		entityitem.motionY += p_82486_0_.rand.nextGaussian() * 0.007499999832361937D * p_82486_2_;
		entityitem.motionZ += p_82486_0_.rand.nextGaussian() * 0.007499999832361937D * p_82486_2_;
		p_82486_0_.spawnEntityInWorld(entityitem);
	}

	protected void playDispenseSound(IBlockSource p_82485_1_) {
		p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
	}

	protected void spawnDispenseParticles(IBlockSource p_82489_1_, EnumFacing p_82489_2_) {
		p_82489_1_.getWorld().playAuxSFX(2000, p_82489_1_.getXInt(), p_82489_1_.getYInt(), p_82489_1_.getZInt(),
				func_82488_a(p_82489_2_));
	}

	private int func_82488_a(EnumFacing p_82488_1_) {
		return p_82488_1_.getFrontOffsetX() + 1 + (p_82488_1_.getFrontOffsetZ() + 1) * 3;
	}
}