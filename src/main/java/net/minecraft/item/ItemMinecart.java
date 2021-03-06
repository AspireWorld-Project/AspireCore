package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemMinecart extends Item {
	private static final IBehaviorDispenseItem dispenserMinecartBehavior = new BehaviorDefaultDispenseItem() {
		private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
		private static final String __OBFID = "CL_00000050";

		@Override
		public ItemStack dispenseStack(IBlockSource p_82487_1_, ItemStack p_82487_2_) {
			EnumFacing enumfacing = BlockDispenser.func_149937_b(p_82487_1_.getBlockMetadata());
			World world = p_82487_1_.getWorld();
			double d0 = p_82487_1_.getX() + enumfacing.getFrontOffsetX() * 1.125F;
			double d1 = p_82487_1_.getY() + enumfacing.getFrontOffsetY() * 1.125F;
			double d2 = p_82487_1_.getZ() + enumfacing.getFrontOffsetZ() * 1.125F;
			int i = p_82487_1_.getXInt() + enumfacing.getFrontOffsetX();
			int j = p_82487_1_.getYInt() + enumfacing.getFrontOffsetY();
			int k = p_82487_1_.getZInt() + enumfacing.getFrontOffsetZ();
			Block block = world.getBlock(i, j, k);
			double d3;

			if (BlockRailBase.func_150051_a(block)) {
				d3 = 0.0D;
			} else {
				if (block.getMaterial() != Material.air || !BlockRailBase.func_150051_a(world.getBlock(i, j - 1, k)))
					return behaviourDefaultDispenseItem.dispense(p_82487_1_, p_82487_2_);

				d3 = -1.0D;
			}

			EntityMinecart entityminecart = EntityMinecart.createMinecart(world, d0, d1 + d3, d2,
					((ItemMinecart) p_82487_2_.getItem()).minecartType);

			if (p_82487_2_.hasDisplayName()) {
				entityminecart.setMinecartName(p_82487_2_.getDisplayName());
			}

			world.spawnEntityInWorld(entityminecart);
			p_82487_2_.splitStack(1);
			return p_82487_2_;
		}

		@Override
		protected void playDispenseSound(IBlockSource p_82485_1_) {
			p_82485_1_.getWorld().playAuxSFX(1000, p_82485_1_.getXInt(), p_82485_1_.getYInt(), p_82485_1_.getZInt(), 0);
		}
	};
	public int minecartType;
	private static final String __OBFID = "CL_00000049";

	public ItemMinecart(int p_i45345_1_) {
		maxStackSize = 1;
		minecartType = p_i45345_1_;
		setCreativeTab(CreativeTabs.tabTransport);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (BlockRailBase.func_150051_a(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_))) {
			if (!p_77648_3_.isRemote) {

				PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(p_77648_2_, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_);
				if (event.isCancelled()) {
					return false;
				}

				EntityMinecart entityminecart = EntityMinecart.createMinecart(p_77648_3_, p_77648_4_ + 0.5F,
						p_77648_5_ + 0.5F, p_77648_6_ + 0.5F, minecartType);

				if (p_77648_1_.hasDisplayName()) {
					entityminecart.setMinecartName(p_77648_1_.getDisplayName());
				}

				p_77648_3_.spawnEntityInWorld(entityminecart);
			}

			--p_77648_1_.stackSize;
			return true;
		} else
			return false;
	}
}